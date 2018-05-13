package com.my.navigesture

class ScaleData{
    private var scaleData: Int = 0

    constructor(scaleData: Int) {
        this.scaleData = scaleData
    }

    fun getScaleDate(): Int {
        return this.scaleData
    }
}