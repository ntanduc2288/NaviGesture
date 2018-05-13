package com.my.navigesture.utils

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import com.my.navigesture.data.services.naviService.NaviService


class Utils{

    companion object {
        private val TAG: String = Utils::class.java!!.canonicalName

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

        fun getDetaulColorValue(context: Context): Int{
            val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val scaleData = sharedPreferences.getInt(Constants.COLOR, Constants.DEFAULT_COLOR)
            return scaleData
        }

        fun saveColorToLocal(context: Context, value: Int){
            val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(Constants.COLOR, value)
            editor.commit()
        }


        fun isAccessibilitySettingsOn(mContext: Context, serviceClassName: String): Boolean {
            var accessibilityEnabled = 0
            val service = mContext.getPackageName() + "/" + serviceClassName
            try {
                accessibilityEnabled = Settings.Secure.getInt(
                        mContext.applicationContext.contentResolver,
                        android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
            } catch (e: Settings.SettingNotFoundException) {
                Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.message)
            }

            val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

            if (accessibilityEnabled == 1) {
                Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------")
                val settingValue = Settings.Secure.getString(
                        mContext.applicationContext.contentResolver,
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                if (settingValue != null) {
                    mStringColonSplitter.setString(settingValue)
                    while (mStringColonSplitter.hasNext()) {
                        val accessibilityService = mStringColonSplitter.next()

                        Log.v(TAG, "-------------- > accessibilityService :: $accessibilityService $service")
                        if (accessibilityService.equals(service, ignoreCase = true)) {
                            Log.v(TAG, "We've found the correct setting - accessibility is switched on!")
                            return true
                        }
                    }
                }
            } else {
                Log.v(TAG, "***ACCESSIBILITY IS DISABLED***")
            }

            return false
        }
    }
}