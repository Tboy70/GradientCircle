package com.example.testgradientthemechanging

import android.graphics.*
import android.graphics.drawable.Drawable
import java.lang.Float.min
import java.lang.Integer.min

class CoronaDrawable : Drawable() {
    private val mPaint = Paint()
    private var mRadius = 0f
    private val mColors =
        intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.BLACK, Color.BLACK and 0xFFFFFF)
    private val mStops = floatArrayOf(0f, 0.85f, 0.85f, 1.0f)

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRadius = min(bounds.width(), bounds.height()) / 2f
        mPaint.shader = shaderFactory(mRadius, mRadius, mColors, mStops)
    }

    override fun draw(c: Canvas) {
        c.drawCircle(mRadius, mRadius, mRadius, mPaint)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(filter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    private fun shaderFactory(centerX: Float, centerY: Float, colors: IntArray, stops: FloatArray) =
        RadialGradient(
            centerX, centerY, min(centerX, centerY), colors, stops, Shader.TileMode.CLAMP
        )
}