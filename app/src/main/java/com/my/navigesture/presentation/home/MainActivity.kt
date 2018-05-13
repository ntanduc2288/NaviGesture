package com.my.navigesture.presentation.home

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.my.navigesture.R
import kotlinx.android.synthetic.main.main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class MainActivity : AppCompatActivity(), DiscreteSeekBar.OnProgressChangeListener, HomePresenter.View {

    val REQUEST_CODE = 10101
    var presenter: HomePresenter.Presenter? = null

    override fun showScale(value: Int) {
        this.seekBar!!.progress = value
    }

    override fun showAccessibilityView() {
        rlServiceNotRunning.visibility = View.VISIBLE
    }

    override fun hideAccessibilityView() {
        rlServiceNotRunning.visibility = View.GONE
    }

    override fun openAccessibility() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    override fun openOverlaySettings() {
        var intent: Intent? = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initData()
    }

    private fun initData() {
        presenter = HomePresenterImpl(this@MainActivity, this)
        presenter?.checkDrawOverlayPermission()

        this.seekBar!!.setOnProgressChangeListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {

            // Double-check that the user granted it, and didn't just dismiss the request
            if (Settings.canDrawOverlays(this)) {


                presenter?.checkEnableAccessibilityService()

            } else {

                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        presenter?.scaleChanged(value)
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onResume() {
        super.onResume()
        presenter?.checkEnableAccessibilityService()
    }

    override fun onDestroy() {
        presenter?.release()
        super.onDestroy()
    }

    //Button Open Accessibility
    fun clickAccessibility(view: View) {
        openAccessibility()
    }


}
