package com.my.navigesture.data.services.naviService

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.my.navigesture.*
import com.my.navigesture.data.services.naviService.TouchListener.FireEvent
import com.my.navigesture.utils.Constants
import com.my.navigesture.utils.Utils
import com.my.navigesture.data.ScaleData


class NaviService: AccessibilityService{

    var windowManager: WindowManager? = null
    private var floatyView: View? = null
    private var viewContainer: LinearLayout? = null

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}


    constructor()

    override fun onCreate() {
        super.onCreate()
        addOverlayView()
        Bus.observe<ScaleData>()
                .subscribe{ resizeContainerHeight(it.getScaleDate()) }
                .registerInBus(this);
    }


    private fun addOverlayView() {
        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.BOTTOM or Gravity.START
        params.x = 0
        params.y = 0

        val interceptorLayout = FrameLayout(this)


        floatyView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.floating_view, interceptorLayout)
        viewContainer = floatyView?.findViewById(R.id.container)
        val back = floatyView?.findViewById<View>(R.id.btnBack)
        var home = floatyView?.findViewById<View>(R.id.btnHome)
        var recent = floatyView?.findViewById<View>(R.id.btnRecent)

        resizeContainerHeight(Utils.getDetaulScaleValue(this.applicationContext))

        back?.setOnClickListener(null)
        home?.setOnClickListener(null)
        recent?.setOnClickListener(null)

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager!!.addView(floatyView, params)


        val backFireListener = object: FireEvent{
            override fun swipeUp() {
                performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }

        back?.setOnTouchListener(TouchListener(backFireListener))

        val homeSwipeUp = object: FireEvent{
            override fun swipeUp() {
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        }

        val homeLongSwipeUp = object : TouchListener.LongSwipeUp {
            override fun longSwipeUp() {
                performGlobalAction(GLOBAL_ACTION_RECENTS)
            }
        }

        home?.setOnTouchListener(TouchListener(homeSwipeUp, homeLongSwipeUp))

        val recentFireListener = object: FireEvent{
            override fun swipeUp() {
                performGlobalAction(GLOBAL_ACTION_RECENTS)
            }
        }

        recent?.setOnTouchListener(TouchListener(recentFireListener))

    }

    private fun resizeContainerHeight(size: Int){
        var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, size)

        viewContainer!!.layoutParams = layoutParams
    }


    override fun onDestroy() {
        super.onDestroy()
        if (floatyView != null) {
            windowManager?.removeView(floatyView);
            floatyView = null;
        }

        Bus.unregister(this)
    }
}