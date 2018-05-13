package com.my.navigesture

import android.view.MotionEvent
import android.view.View
import java.util.*

class TouchListener : View.OnTouchListener {
    var fireEvent: FireEvent? = null
    var longSwipeUp: LongSwipeUp? = null

    private val MIN_LONG_SWIPE_TIME = 500 //Millisecond
    private val MIN_SWIPE_DISTANCE = 300
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    internal var startTime: Long = 0
    internal var durationTime: Long = 0
    internal var alreadyLongSwipeUp: Boolean = false

    constructor(fireEvent: FireEvent?) {
        this.fireEvent = fireEvent
    }

    constructor(fireEvent: FireEvent?, longSwipeUp: LongSwipeUp) {
        this.fireEvent = fireEvent
        this.longSwipeUp = longSwipeUp
    }


    interface FireEvent {
        fun swipeUp()
    }

    interface LongSwipeUp {
        fun longSwipeUp()
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(event)
            }
            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event)
            }

        }
        return true
    }

    private fun handleActionMove(event: MotionEvent) {
        x2 = event.x
        y2 = event.y
        durationTime = Calendar.getInstance().timeInMillis
        val reachedDistance = y1 - y2 > MIN_SWIPE_DISTANCE
        val reachedTime = durationTime - startTime > MIN_LONG_SWIPE_TIME

        //if Down to UP sweep event on screen
        if (reachedDistance && reachedTime && alreadyLongSwipeUp == false) {
            if (longSwipeUp != null) {
                alreadyLongSwipeUp = true
                longSwipeUp?.longSwipeUp()
            }
        }
    }

    private fun handleActionUp(event: MotionEvent) {
        alreadyLongSwipeUp = false
        x1 = event.x
        x2 = event.y

        durationTime = Calendar.getInstance().timeInMillis

        val reachedSwipeUp = durationTime - startTime < MIN_LONG_SWIPE_TIME

        if(reachedSwipeUp && (y1 - y2) > MIN_SWIPE_DISTANCE){
            fireEvent?.swipeUp()
        }
    }

    fun handleActionDown(event: MotionEvent) {
        alreadyLongSwipeUp = false
        startTime = Calendar.getInstance().timeInMillis
        x1 = event.x
        x2 = event.y
    }



}