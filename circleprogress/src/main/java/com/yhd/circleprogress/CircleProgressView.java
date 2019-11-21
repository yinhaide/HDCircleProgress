package com.yhd.circleprogress;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.FloatRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 圆环的进度条
 * Created by haide.yin(haide.yin@tcl.com) on 2019/1/23 15:38.
 */
public class CircleProgressView extends View {

    private static final String TAG = CircleProgressView.class.getSimpleName();

    /* ********************* 外部设置的属性 *********************** */
    private int behindColor = Color.parseColor("#1A000000");//底部圆弧的颜色，默认为Color.LTGRAY
    private int[] progressColor = new int[]{
             Color.parseColor("#FF44C4F3")
            ,Color.parseColor("#FF3D6DF6")
            ,Color.parseColor("#FF44C4F3")};//渐变圆周颜色数组
    private float ringWidthRatio = 0.08f;//圆环的宽度比例
    private float progressWidthRatio = 0.02f;//进度的宽度比例
    private int animationTime = 2000;
    private float progress = 0.5f;
    private float rotateAngle = -90f;

    /* ********************* 内部私有的属性 *********************** */
    private Paint circlePaint = new Paint();//画圆弧的画笔
    private ValueAnimator animator;//进度设置的过度动画

    /* ********************* 内部方法 *********************** */

    public CircleProgressView(Context context) {
        this(context, null, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //View被窗体移除的时候释放动画资源
        if (animator != null) {
            animator.end();
            animator.cancel();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFoucus) {
        super.onWindowFocusChanged(hasFoucus);
        //View焦点变化
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation(0f, this.progress, animationTime);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        circlePaint.setShader(null); // 清除上一次的shader
        //开始画背景环
        int center = getWidth() / 2;
        float ringWidth = getWidth() * ringWidthRatio;
        float progressWidth = getWidth() * progressWidthRatio;
        circlePaint.setColor(behindColor); // 设置底部圆环的颜色，这里使用第一种颜色
        circlePaint.setStrokeWidth(ringWidth);
        canvas.drawCircle(center, center, center - ringWidth / 2, circlePaint); // 画底部的空心圆
        //开始画进度环
        float progressSep = ringWidth / 2;
        RectF progressRect = new RectF(progressSep, progressSep, getWidth() - progressSep, getWidth() - progressSep); // 圆的外接正方形
        // 绘制颜色渐变圆环
        circlePaint.setColor(Color.BLACK); // 设置任意不透明的底色都可以
        if (progressColor.length > 1) {
            circlePaint.setShader(new SweepGradient(center, center / 2, progressColor, null));
        } else {
            circlePaint.setColor(progressColor[0]);
        }
        circlePaint.setStrokeWidth(progressWidth);
        //旋转整个画布
        canvas.rotate(rotateAngle, center, center);
        // 每次扫过的角度，用来设置进度条圆弧所对应的圆心角
        float alphaAngle = 360.0f * this.progress;
        canvas.drawArc(progressRect, 0, alphaAngle, false, circlePaint);
    }

    /* ********************* 内部私有的方法 *********************** */

    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStyle(Paint.Style.STROKE); // 设置绘制的圆为空心
        circlePaint.setStrokeCap(Paint.Cap.ROUND); // 把每段圆弧改成圆角的
        if (attrs != null) {
            //初始化布局属性
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
            //高度只能大于等于0以及小于等于1
            behindColor = typedArray.getColor(R.styleable.CircleProgressView_cr_behindColor, this.behindColor);
            ringWidthRatio = typedArray.getFloat(R.styleable.CircleProgressView_cr_widthRatio, this.ringWidthRatio);
            progressWidthRatio = typedArray.getFloat(R.styleable.CircleProgressView_cr_progressWidthRatio, this.progressWidthRatio);
            animationTime = typedArray.getInteger(R.styleable.CircleProgressView_cr_animationTime, this.animationTime);
            rotateAngle = typedArray.getFloat(R.styleable.CircleProgressView_cr_rotateAngle, this.rotateAngle);
            progress = typedArray.getFloat(R.styleable.CircleProgressView_cr_progress, this.progress);
            String progressColorString = typedArray.getString(R.styleable.CircleProgressView_cr_progressColor);
            if (!TextUtils.isEmpty(progressColorString)) {
                if (progressColorString.contains(",")) {
                    String[] colorStringArray = progressColorString.split(",");
                    progressColor = new int[colorStringArray.length];
                    for (int i = 0; i < colorStringArray.length; i++) {
                        progressColor[i] = Color.parseColor(colorStringArray[i]);
                    }
                } else {
                    progressColor = new int[1];
                    progressColor[0] = Color.parseColor(progressColorString);
                }
            }
            // TODO: 2019/7/31 检查赋值的有效性
        }
    }

    /**
     * 启动动画
     */
    private void startAnimation(float fromValue, float toValue, int duraction) {
        if (fromValue != toValue) {
            if (animator != null) {
                animator.end();
                animator.cancel();
            }
            animator = ValueAnimator.ofFloat(fromValue, toValue);
            animator.addUpdateListener((animation) -> {
                this.progress = (Float) animation.getAnimatedValue();
                invalidate();
            });
            //实现越界弹回的效果
            //animator.setInterpolator(new OvershootInterpolator());
            //在动画开始的地方快然后慢
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(duraction);
            animator.start();
        }
    }

    /* ********************* 外部公共的方法 *********************** */

    /**
     * 设置圆环背景的底色
     *
     * @param color 颜色
     */
    public void setBehindColor(int color) {
        this.behindColor = color;
        invalidate();
    }

    /**
     * 设置进度条渐变色颜色数组,当设置一个的时候默认为进度背景
     *
     * @param colors 颜色数组
     */
    public void setProgressColor(int[] colors) {
        this.progressColor = colors;
        invalidate();
    }

    /**
     * 设置进度动画时间
     *
     * @param animationTime 动画时间
     */
    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    /**
     * 旋转画布,默认进去起点在水平右边
     *
     * @param rotateAngle 旋转角度
     */
    public void rotate(float rotateAngle) {
        if (this.rotateAngle != rotateAngle) {
            this.rotateAngle = rotateAngle;
            invalidate();
        }
    }

    /**
     * 按进度显示百分比，可选择是否启用数字动画
     *
     * @param progress 进度
     */
    public void setProgress(@FloatRange(from = 0f, to = 1.0f) float progress) {
        setProgress(progress, animationTime);
    }

    /**
     * 按进度显示百分比，可选择是否启用数字动画
     *
     * @param progress  进度
     * @param duraction 动画间隔
     */
    public void setProgress(@FloatRange(from = 0f, to = 1.0f) float progress, int duraction) {
        if (progress >= 0 && progress <= 1.0f && this.progress != progress) {
            startAnimation(this.progress, progress, duraction);
        }
    }
}