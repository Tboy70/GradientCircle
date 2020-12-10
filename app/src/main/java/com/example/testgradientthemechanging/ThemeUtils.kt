package com.example.testgradientthemechanging


import android.app.Activity

import android.content.Intent
import kotlin.random.Random


object ThemeUtils {

    private var sTheme = 0
    const val THEME_DEFAULT = 0
    const val THEME_TWO = 1
    const val THEME_THREE = 2

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    fun changeToTheme(activity: Activity, theme: Int) {
        if (sTheme != theme) {
            sTheme = theme
            activity.finish()
            activity.startActivity(Intent(activity, activity.javaClass))
        } else {
            changeToTheme(activity, Random.nextInt(0, 3))
        }
    }

    /** Set the theme of the activity, according to the configuration.  */
    fun onActivityCreateSetTheme(activity: Activity) {
        when (sTheme) {
            THEME_DEFAULT -> activity.setTheme(R.style.themeOne)
            THEME_TWO -> activity.setTheme(R.style.themeTwo)
            THEME_THREE -> activity.setTheme(R.style.themeThree)
            else -> activity.setTheme(R.style.themeOne)
        }
    }

}