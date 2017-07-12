# DialDemo
变化刻度自定义View Demo，通过自定义View的方式实现
### 准备工作：创建自定义View
其实自定义 View 就是创建一个类，继承 View 或其他的 View 的子 View。根据需要来重写它里面相应的方法。这里我们就继承了 View
* 因为这次自定义刻度 View 重点是在于绘制，所以我们需要使用到两个很重要的类。一个是 Canvas（画布），一个是 Paint（画笔）。因为重写的 onDraw 方法里面会传入进来一个 canvas 的对象，所以就不需要我们自己去创建画布对象，我们只需要去创建没有的画笔对象即可.
<br/>我们使用静态变量来定义用到的画笔，然后用静态代码块来初始化画笔。
```java
    //初始化画笔
    static {
        paint = new Paint();
        textPaint = new Paint();
        linePatin = new Paint();
        targetLinePaint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        textPaint.setColor(Color.WHITE);
        linePatin.setColor(Color.WHITE);
        //设置抗锯齿
        paint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        linePatin.setAntiAlias(true);
        targetLinePaint.setAntiAlias(true);
        //空心
        paint.setStyle(Paint.Style.STROKE);
        //设置线宽
        textPaint.setStrokeWidth(lineWidth);
        paint.setStrokeWidth(lineWidth);
        linePatin.setStrokeWidth(lineWidth);
        targetLinePaint.setStrokeWidth(lineWidth);
    }
```
### 一、画圆弧
* 因为需要画圆弧，所以为了方便操作我们需要在测量控件长宽的时候给它限制为一个正方形，所以我们需要重写 onMeasure 方法（这个方法是用于测量控件的长和宽的）
```java

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
        //以长和宽的最小值为正方形的边长
        len = Math.min(width, height);
        //创建一个和View的大小一致的正方形
        rectF = new RectF(0, 0, len, len);
        //调用这个方法保存测量的宽和高
        setMeasuredDimension(len, len);
    }
    
```
* 限定好 View 的范围以后我们就开始画出圆弧，重写 onDraw 方法，在 onDraw 方法里面调用 canvas.drawArc 画圆弧
<br/>`
    void drawArc (RectF oval, float startAngle,float sweepAngle,boolean useCenter,Paint paint);
`
    
     * oval:圆弧所在的正方形
     * startAngle:圆弧开始的角度
     * sweepAngle:圆弧扫过的角度
     * useCenter:是否将圆弧的两个端点与圆心连起
     * paint:用来绘制圆弧的画笔
     
### 三、画刻度