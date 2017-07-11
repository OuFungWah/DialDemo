package com.example.daildemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.daildemo.R;

import java.util.concurrent.Semaphore;

/**
 * 刻度盘控件
 * <p>
 * Created by 区枫华 on 2017/7/5.
 */

public class DialView extends View {

    /**
     * 信号量
     */
    private Semaphore isRunningSem = new Semaphore(1);

    /**
     * 是否在运行动画
     */
    private boolean isRunning = false;

    /**
     * 动画刷新频率
     */
    private static int ANIM_INTERVAL = 15;

    /**
     * 正方形的边长
     */
    private int len;

    /**
     * 正方形
     */
    private RectF rectF;
    /**
     * 圆弧起始角度
     */
    private static float startAngle = 120;
    /**
     * 圆弧结束角度
     */
    private static float sweepAngle = 300;
    /**
     * 坐标轴旋转的角度
     */
    private float rotateAngle;
    /**
     * 圆弧画笔
     */
    private static Paint paint;
    /**
     * 文字画笔
     */
    private static Paint textPaint;
    /**
     * 目标角度
     */
    private float targetAngle;
    /**
     * 刻度线的宽度
     */
    private static int lineWidth = 4;
    /**
     * 百分比
     */
    private float percent = 0;
    /**
     * 颜色变化监听器
     */
    private OnColorChangeListener mOnColorChangeListener;

    //初始化画笔
    static {
        paint = new Paint();
        textPaint = new Paint();
        //设置颜色
        paint.setColor(Color.WHITE);
        textPaint.setColor(Color.WHITE);
        //设置抗锯齿
        paint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        //空心
        paint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(lineWidth);
        paint.setStrokeWidth(lineWidth);
    }

    public DialView(Context context) {
        super(context);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
    }

    /**
     * 获取xml设置的属性
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        //获取属性列表
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.dailview);
        //获取xml属性中的目标角度
        targetAngle = typedArray.getFloat(R.styleable.dailview_target_angle, 0);
    }

    /**
     * 测量限定控件
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //以最小值为正方形的长
        len = Math.min(width, height);
        rectF = new RectF(0, 0, len, len);
        //设置测量的宽和高
        setMeasuredDimension(len, len);
    }

    /**
     * 重写绘制方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画圆
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
        drawDial(canvas);
    }

    protected void drawDial(Canvas canvas) {
        int red = 255;
        int green = 0;
        int radius = len / 2;
        //先保存之前canvas的内容
        canvas.save();
        //移动canvas(X轴移动距离，Y轴移动距离)
        canvas.translate(radius, radius);
        //旋转坐标系
        canvas.rotate(30);
        Paint linePatin = new Paint();
        //设置画笔颜色
        linePatin.setColor(Color.WHITE);
        //线宽
        linePatin.setStrokeWidth(lineWidth);
        //设置画笔抗锯齿
        linePatin.setAntiAlias(true);
        //确定每次旋转的角度
        rotateAngle = sweepAngle / 100;
        //绘制有色部分的画笔
        Paint targetLinePaint = new Paint();

        targetLinePaint.setStrokeWidth(lineWidth);
        targetLinePaint.setAntiAlias(true);

        float hasDraw = 0;
        for (int i = 0; i <= 100; i++) {
            if (hasDraw <= targetAngle && targetAngle != 0) {//需要绘制有色部分的时候
                //计算已经绘制的比例
                percent = hasDraw / sweepAngle;
                red = 255 - (int) (255 * percent);
                green = (int) (255 * percent);
                targetLinePaint.setARGB(255, red, green, 0);
                //画一条刻度线
                canvas.drawLine(0, radius, 0, radius - 60, targetLinePaint);

            } else {//不需要绘制有色部分
                //画一条刻度线
                canvas.drawLine(0, radius, 0, radius - 60, linePatin);
            }
            hasDraw += rotateAngle;
            canvas.rotate(rotateAngle);
        }
        //恢复坐标轴
        canvas.restore();
        //绘制圈内内容
        drawCenterContents(canvas, red, green, radius);
        if (mOnColorChangeListener != null) {
            mOnColorChangeListener.onColorChange(red, green);
        }
    }

    /**
     * 绘制圈内内容
     *
     * @param canvas 画布
     * @param red    红色值
     * @param green  绿色值
     * @param radius 半径
     */
    private void drawCenterContents(Canvas canvas, int red, int green, int radius) {
        //绘制颜色内圈
        Paint smallPaint = new Paint();
        smallPaint.setARGB(120, red, green, 60);
        smallPaint.setAntiAlias(true);
        int smallCircleRadius = radius - 90;
        canvas.drawCircle(radius, radius, smallCircleRadius, smallPaint);
        //绘制百分比文字
        textPaint.setTextSize(200);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("" + (int) (percent * 100), radius, radius + 50, textPaint);
    }

    /**
     * 动画的形式改变角度
     *
     * @param angle 改变的目标角度
     */
    public void changeAngle(int angle) {
        if (isRunning) {
            return;
        } else {
            try {
                //先抢占信号量
                isRunningSem.acquire();
                //动态下降
                decline();
                //动态增长
                increasing(angle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从目前的角度下降到0
     */
    public void decline() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = (int) targetAngle; i >= 0; i -= rotateAngle) {
                        targetAngle = i;
                        isRunning = true;
                        Thread.sleep(ANIM_INTERVAL);
                        postInvalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRunningSem.release();
                    isRunning = false;
                }
            }

        }).start();
    }

    /**
     * 从0上升到目标角度
     */
    public void increasing(final int angle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isRunningSem.acquire();
                    for (int i = (int) targetAngle; i <= angle; i += rotateAngle) {
                        targetAngle = i;
                        isRunning = true;
                        Thread.sleep(ANIM_INTERVAL);
                        postInvalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRunningSem.release();
                    isRunning = false;
                }
            }
        }).start();
    }

    /**
     * 自定义目标角度（0~300）degree
     *
     * @param targetAngle
     */
    public void setTargetAngle(float targetAngle) {
        if (targetAngle >= 0 && targetAngle <= 100) {
            this.targetAngle = targetAngle * 3;
        } else if (targetAngle < 0) {
            this.targetAngle = 0;
        } else if (targetAngle > 100) {
            this.targetAngle = targetAngle * 3;
        }
    }

    /**
     * 设置动画运行速度（10~300）ms,如果超出范围会自动取相应的极值
     *
     * @param animInterval
     */
    public static void setAnimInterval(int animInterval) {
        if (animInterval >= 10 && animInterval <= 300) {
            ANIM_INTERVAL = animInterval;
        } else if (animInterval < 10) {
            ANIM_INTERVAL = 10;
        } else if (animInterval > 300) {
            ANIM_INTERVAL = 300;
        }
    }

    /**
     * 设置颜色改变监听器
     *
     * @param mOnColorChangeListener
     */
    public void setOnColorChangeListener(OnColorChangeListener mOnColorChangeListener) {
        this.mOnColorChangeListener = mOnColorChangeListener;
    }
}


