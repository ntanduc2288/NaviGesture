package com.my.navigesture

import android.graphics.PixelFormat
import android.view.WindowManager
import android.widget.Toast

class Test {

    internal var windowManager: WindowManager? = null

    private val testPrivate = 1
    var testPublic = 2
    protected var testProtected = 3

    constructor(data: Long) {}

    constructor(a: Int) {
        val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
    }
}
