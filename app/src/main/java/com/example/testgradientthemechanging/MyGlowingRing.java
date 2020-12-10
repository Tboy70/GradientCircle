package com.example.testgradientthemechanging;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MyGlowingRing extends androidx.appcompat.widget.AppCompatImageView {

    //The coefficients for computation blur
    //and glowing values depending on body stroke width
    private static final float GLOWING_MULTIPLIER = 1;
    private static final float BLUR_MULTIPLIER = 1;

    //dp neon ring geometry params
    private static final float RING_RADIUS = 70;
    private static final float BODY_STROKE_WIDTH = 6;
    private static final float GLOW_STROKE_WIDTH
            = GLOWING_MULTIPLIER * BODY_STROKE_WIDTH;
    private static final float BLUR_RADIUS
            = BLUR_MULTIPLIER * BODY_STROKE_WIDTH;

    //colors foe body and glowing area
    private final static int BODY_COLOR = 0xFFBEFF09;
    private final static int GLOW_COLOR_20P_ALPHA = 0x33BCFF00;
    //alternative color for glowing area
    private final static int GLOW_COLOR_80P_ALPHA = 0xCBBCFF00;
    private int bodyColor = BODY_COLOR;
    private int glowingColor = GLOW_COLOR_20P_ALPHA;

    //geometry params in Px:
    private float ringRadius;
    private float bodyStrokeWidthPx;
    private float glowStrokeWidthPx;
    private float blurRadiusPx;

    //painting objects for blur ring, shadowLayer ring
    // and shaderGradientRing respectively:
    private Paint bodyPaint;
    private Paint glowingPaint;

    private float centerBlurRingY;
    private float centerRingX;

    public MyGlowingRing(Context context) {
        super(context);
        init();
        initBlurPainting();
    }

    public MyGlowingRing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initBlurPainting();
    }

    public MyGlowingRing(Context context, @Nullable AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initBlurPainting();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerRingX = w / 2f;
        centerBlurRingY = h / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBlurRing(canvas);
    }

    private void init() {

        setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ringRadius = convertDpToPixel(RING_RADIUS, getContext());
        bodyStrokeWidthPx = convertDpToPixel(BODY_STROKE_WIDTH, getContext());
        glowStrokeWidthPx = convertDpToPixel(GLOW_STROKE_WIDTH, getContext());
        blurRadiusPx = convertDpToPixel(BLUR_RADIUS, getContext());
    }

    private void initBlurPainting() {
        bodyPaint = new Paint();
        bodyPaint.setAntiAlias(true);
        bodyPaint.setColor(bodyColor);
        bodyPaint.setStrokeWidth(bodyStrokeWidthPx);
        bodyPaint.setStyle(Paint.Style.STROKE);

        glowingPaint = new Paint();
        glowingPaint.setAntiAlias(true);
        glowingPaint.setColor(glowingColor);
        glowingPaint.setMaskFilter(new BlurMaskFilter(blurRadiusPx,
                BlurMaskFilter.Blur.NORMAL));
        glowingPaint.setStrokeWidth(glowStrokeWidthPx);
        glowingPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawBlurRing(Canvas canvas) {
        canvas.drawCircle(centerRingX, centerBlurRingY, ringRadius, glowingPaint);
        canvas.drawCircle(centerRingX, centerBlurRingY, ringRadius, bodyPaint);
    }

    // helper  util method
    private static float convertDpToPixel(float valueDp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueDp,
                displayMetrics);
    }
}