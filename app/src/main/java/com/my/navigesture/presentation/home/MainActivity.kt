package com.my.navigesture

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.eightbitlab.rxbus.Bus
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class MainActivity : AppCompatActivity(), DiscreteSeekBar.OnProgressChangeListener {
    val REQUEST_CODE = 10101
    val publishSubject = PublishSubject.create<Int>()
    val compositSubscription: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initData()

        this.seekBar!!.setOnProgressChangeListener(this)
        this.seekBar!!.progress = Utils.getDetaulScaleValue(this.applicationContext)

        val isEnable = Utils.isAccessibilityServiceEnabled(this@MainActivity, MainActivity::class.java)
        Toast.makeText(this@MainActivity, "fdsafdsa   " + isEnable, Toast.LENGTH_SHORT).show()
    }

    private fun initData() {
        if (Settings.canDrawOverlays(this)) {
            finishActivity()
        } else {
            checkDrawOverlayPermission()
        }

        //Notify scale changed
        compositSubscription.add(publishSubject.flatMap { getScaleObservable(it) }
                .subscribeOn(Schedulers.io())
                .subscribe())

    }

    fun getScaleObservable(value: Int): Observable<Int>{

        return Observable.just(value)
                .doOnNext { Bus.send(ScaleData(it)) }
                .doOnNext { Utils.saveScaleToLocal(this.applicationContext, it) }

    }

    private fun checkDrawOverlayPermission() {

        if (!Settings.canDrawOverlays(this)) {
            var intent: Intent? = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {

            // Double-check that the user granted it, and didn't just dismiss the request
            if (Settings.canDrawOverlays(this)) {

                openAccessibilitySettings()

                // Launch the service
                finishActivity()
            } else {

                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun finishActivity() {
//        finish()
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        publishSubject.onNext(value)
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onDestroy() {
        compositSubscription?.clear()
        super.onDestroy()
    }




}
