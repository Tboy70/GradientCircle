package com.example.testgradientthemechanging

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue


class MyGradientTextView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val gradientOne = TypedValue()
        context.theme.resolveAttribute(R.attr.gradientColorOne, gradientOne, true)

        val gradientTwo = TypedValue()
        context.theme.resolveAttribute(R.attr.gradientColorTwo, gradientTwo, true)

        if (changed) {
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                gradientOne.data,
                gradientTwo.data,
                Shader.TileMode.CLAMP
            )

        }
    }


}