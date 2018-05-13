package com.my.navigesture

import android.content.Context

class Utils{
    companion object {
        fun getDetaulScaleValue(context: Context): Int{
            val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val scaleData = sharedPreferences.getInt(Constants.SCALE_VALUE, Constants.DEFAULT_HEIGHT)
            return scaleData
        }

        fun saveScaleToLocal(context: Context, value: Int){
            val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(Constants.SCALE_VALUE, value)
            editor.commit()
        }
    }
}