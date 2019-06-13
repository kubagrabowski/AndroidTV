package com.example.androidtv

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v17.leanback.app.BackgroundManager
import android.util.DisplayMetrics



class SimpleBackgroundManager(mActivity: Activity) {

    private val DEFAULT_BACKGROUND_RES_ID = android.R.drawable.btn_default
    private val mBackgroundManager: BackgroundManager

    init {
        mDefaultBackground = mActivity.getDrawable(DEFAULT_BACKGROUND_RES_ID)
        mBackgroundManager = BackgroundManager.getInstance(mActivity)
        mBackgroundManager.attach(mActivity.window)
        mActivity.windowManager.defaultDisplay.getMetrics(DisplayMetrics())
    }

    fun updateBackground(drawable: Drawable) {
        mBackgroundManager.setBitmap((drawable as BitmapDrawable).bitmap)
    }

    fun clearBackground() {
        mBackgroundManager.drawable = mDefaultBackground
    }

    companion object {
        private var mDefaultBackground: Drawable? = null
    }

}