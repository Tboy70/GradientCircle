package com.example.testgradientthemechanging

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import kotlin.math.min
import kotlin.math.roundToInt


// Width of the solid circle
private const val BODY_STROKE_WIDTH = 1f

// To multiply the glowing effect
private const val GLOWING_MULTIPLIER = 20f

// To multiply the blur effect
private const val BLUR_MULTIPLIER = 10f

// Width of the glowing effect
private const val GLOW_STROKE_WIDTH = GLOWING_MULTIPLIER * BODY_STROKE_WIDTH

// Radius of the blur area
private const val BLUR_RADIUS = BLUR_MULTIPLIER * BODY_STROKE_WIDTH

class GlowingRingKt : androidx.appcompat.widget.AppCompatImageView {

    private var bodyColor: Int = 0
    private var glowingColor: Int = 0

    private var colorTwo: Int = 0

    // Painting object
    private var linearShaderPaint: Paint? = Paint()
    private var radialShaderPaint: Paint? = Paint()

    // Geometry params in pixels
    private var ringRadius = 0f
    private var blurRadiusPx = 0f
    private var bodyStrokeWidthPx = 0f
    private var glowStrokeWidthPx = 0f

    // Circle position (x, y)
    private var centerRingX = 0f
    private var centerRingY = 0f

    // Ratio from an "all device width" circle
    private var ratioRadius = 1f
    private var gradientType = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val customAttributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GlowingRingKt,
            0, 0
        )
        ratioRadius = customAttributes.getFloat(R.styleable.GlowingRingKt_glowingCircleRadius, 1f)
        gradientType = customAttributes.getInt(R.styleable.GlowingRingKt_gradientType, 0)
        Log.e("TEST", "GradientType : " + gradientType)

        val gradientOne = TypedValue()
        context.theme.resolveAttribute(R.attr.gradientColorOne, gradientOne, true)

        bodyColor = gradientOne.data

        glowingColor = manipulateColor(gradientOne.data, 0.8f).apply {
            alpha = 0.33f
        }

        val gradientTwo = TypedValue()
        context.theme.resolveAttribute(R.attr.gradientColorTwo, gradientTwo, true)
        colorTwo = gradientTwo.data

        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Place the circle to the center of the screen
        centerRingX = w / 2f
        centerRingY = h / 2f

        if (gradientType == 0) {
            radialShaderPaint?.let {
                it.shader = createRadialGradient(centerRingX, centerRingY)
            }
        } else {
            linearShaderPaint?.let { paint ->
                paint.shader = createLinearGradient(centerRingX, centerRingY)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawGradientShaderRing(canvas)
    }

    /**
     * MeasureSpec.EXACTLY --> Must be this size
     * MeasureSpec.AT_MOST --> Can't be bigger than ...
     * MeasureSpec.ELSE --> Be whatever you want
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredWidth = 300
        val desiredHeight = 300
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        val height: Int

        // We change the radius given the ratioRadius + margin (here, 50)
        ringRadius = (widthSize / 2).toFloat() * ratioRadius - 50

        width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                widthSize
            }
            else -> {
                desiredWidth
            }
        }

        height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                widthSize
            }    // Cause it's a circle
            else -> {
                desiredHeight
            }
        }

        setMeasuredDimension(width, height) //MUST CALL THIS
    }

    private fun init() {
        bodyStrokeWidthPx = convertDpToPixel(BODY_STROKE_WIDTH, context)
        glowStrokeWidthPx = convertDpToPixel(GLOW_STROKE_WIDTH, context)
        blurRadiusPx = convertDpToPixel(BLUR_RADIUS, context)

        if (gradientType == 0) {
            initRadialShaderPaint()
        } else {
            initLinearShaderPaint()
        }
    }

    private fun initLinearShaderPaint() {
        linearShaderPaint?.let { paint ->
            paint.isAntiAlias = true
            paint.strokeWidth = convertDpToPixel(4.0f, context)
            paint.style = Paint.Style.STROKE

        }
    }

    private fun initRadialShaderPaint() {
        radialShaderPaint?.let { paint ->
            paint.isAntiAlias = true
            paint.strokeWidth = glowStrokeWidthPx + blurRadiusPx * 2
            paint.style = Paint.Style.STROKE
        }
    }

    private fun drawGradientShaderRing(canvas: Canvas) {

        if (gradientType == 0) {
            radialShaderPaint?.let {
                canvas.drawCircle(centerRingX, centerRingY, ringRadius, it)
            }
        } else {
            linearShaderPaint?.let {
                canvas.drawCircle(centerRingX, centerRingY, ringRadius - 70, it)
                Log.e("TEST", "ringRadius : " + ringRadius)

            }
        }
    }

    private fun createLinearGradient(centerRingX: Float, centerRingY: Float): Shader {
        val listColor = mutableListOf<Int>().apply {
            add(bodyColor)
            add(colorTwo)
        }.toIntArray()

        return LinearGradient(
            0f,
            0f,
            centerRingX * 2,
            centerRingY * 2,
            listColor,
            floatArrayOf(0f, 1f),
            Shader.TileMode.REPEAT
        )
    }

    private fun createRadialGradient(centerRingX: Float, centerRingY: Float): Shader {
        val gradientRadiusPx = ringRadius + glowStrokeWidthPx / 2 + blurRadiusPx

        // transparent
        val innerEndGlowingPx = ringRadius - glowStrokeWidthPx / 2 - blurRadiusPx
        // glowing color
        val innerStartGlowingPx = ringRadius - glowStrokeWidthPx / 4
        // glowing color
        val constantInnerGlowingPx = ringRadius - bodyStrokeWidthPx / 2
        // body color
        val innerBodyEndPx = ringRadius - bodyStrokeWidthPx / 2 - 1
        //body color
        val outerBodyEndPx = ringRadius + bodyStrokeWidthPx / 2 + 1
        // glowing color
        val constantOuterGlowingPx = ringRadius + bodyStrokeWidthPx / 2
        // glowing color
        val outerStartGlowingPx = ringRadius + glowStrokeWidthPx / 4
        // transparent
        val outerEndGlowingPx = ringRadius + glowStrokeWidthPx / 2 + blurRadiusPx

        //  normalized values in same order:
        val innerEndGlowingNormalized = innerEndGlowingPx / gradientRadiusPx
        val innerStartGlowingNormalized = innerStartGlowingPx / gradientRadiusPx
        val constantInnerGlowingNormalized = constantInnerGlowingPx / gradientRadiusPx
        val innerBodyEndNormalized = innerBodyEndPx / gradientRadiusPx
        val outerBodyEndNormalized = outerBodyEndPx / gradientRadiusPx
        val constantOuterGlowingNormalized = constantOuterGlowingPx / gradientRadiusPx
        val outerStartGlowingNormalized = outerStartGlowingPx / gradientRadiusPx
        val outerEndGlowingNormalized = outerEndGlowingPx / gradientRadiusPx

        val stops = floatArrayOf(
            innerEndGlowingNormalized,
            innerStartGlowingNormalized,
            constantInnerGlowingNormalized,
            innerBodyEndNormalized,
            outerBodyEndNormalized,
            constantOuterGlowingNormalized,
            outerStartGlowingNormalized,
            outerEndGlowingNormalized
        )

        val colors = intArrayOf(
            Color.TRANSPARENT,
            glowingColor,
            glowingColor,
            bodyColor,
            bodyColor,
            glowingColor,
            glowingColor,
            Color.TRANSPARENT
        )

        return RadialGradient(
            centerRingX, centerRingY, gradientRadiusPx,
            colors,
            stops,
            Shader.TileMode.MIRROR
        )
    }

    private fun convertDpToPixel(valueDp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, valueDp,
            context.resources.displayMetrics
        )
    }

    private fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.argb(
            a,
            min(r, 255),
            min(g, 255),
            min(b, 255)
        )
    }
}