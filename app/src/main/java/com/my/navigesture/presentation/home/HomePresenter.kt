package com.my.navigesture.presentation.home

interface HomePresenter {
    interface View {
        fun showScale(value: Int)
        fun showAccessibilityView()
        fun hideAccessibilityView()
        fun openAccessibility()
        fun openOverlaySettings()
    }

    interface Presenter{
        fun checkEnableAccessibilityService()
        fun scaleChanged(value: Int)
        fun colorChanged(color: Int)
        fun checkDrawOverlayPermission()
        fun release()
    }
}