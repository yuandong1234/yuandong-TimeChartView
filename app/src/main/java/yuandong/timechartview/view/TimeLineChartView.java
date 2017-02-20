package yuandong.timechartview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import yuandong.timechartview.R;
import yuandong.timechartview.utils.DateUtil;
import yuandong.timechartview.utils.DensityUtils;
import yuandong.timechartview.utils.ScreenUtils;

/**
 * 时刻折线图
 * Created by yuandong on 2017/2/17.
 */

public class TimeLineChartView extends View {
    private static String TAG = TimeLineChartView.class.getSimpleName();
    //时间类型
    public static final int TIME = 0;//天
    public static final int WEEK = 1;//周
    public static final int MONTH = 2;//月
    private int timeType = WEEK;//默认为周

    private int foldLineColor = Color.RED;//折线颜色 默认为红色
    private int lineColor = Color.parseColor("#50000000");//坐标线、内部线 文字颜色
    private Paint linePaint;//线条画笔
    private TextPaint textPaint;//文字画笔
    private Paint dashPaint;//虚线画笔

    private String unit = "次/分钟";//单位
    private String[] xTitles = {"日", "一", "二", "三", "四", "五", "六"};//x轴坐标标题
    private String[] yTitles = {"40", "90", "140"};//y轴做标标题

    private int padding;//view 边距
    private float leftTitleWidth;//左侧标题宽度
    private float xOrigin, yOrigin;//零点坐标
    private float xWidth;//X轴宽度
    private float width;//View宽度
    private float xSpaceWidth;//X轴文字之间的间隙
    private int[] values;//数据源
    private int[] times;//时刻集合    注意 ：只有在timeType=TIME的时候会用到
    private int maxValue = 140;//y轴最大值
    private int minValue = 40;//y轴最小值
    private String selectedDate;//选择日期 yyyy.MM.dd

    private PointF[] pointFs;//折线点
    private Path mPath = new Path();//折线path
    private ArrayList<Path> pathList;

    private float verticalLineX;//竖线
    private boolean isShowVerticalLineX;//是否显示内部滑动竖线
    private SelectCallbackListener listener;


    public TimeLineChartView(Context context) {
        this(context, null);
    }

    public TimeLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeLineChartView, 0, 0);
        foldLineColor = typeArray.getColor(R.styleable.TimeLineChartView_LineColor, Color.RED);
        int type = typeArray.getInt(R.styleable.TimeLineChartView_TimeType, 1);//默认1：周
        switch (type) {
            case 0:
                timeType = TIME;

                break;
            case 1:
                timeType = WEEK;
                break;
            case 2:
                timeType = MONTH;
                break;
        }
        typeArray.recycle();
    }

    private void init(Context context) {
        setXTitleType();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(DensityUtils.sp2px(context, 12));
        textPaint.setStrokeWidth(1);
        textPaint.setColor(lineColor);

        dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setColor(Color.RED);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{5, 5}, 0);
        dashPaint.setPathEffect(pathEffect);

        mPath = new Path();
        pathList = new ArrayList<>();

        //width= ScreenUtils.getScreenWidth(context);
        padding = DensityUtils.dp2px(context, 20);
        leftTitleWidth = textPaint.measureText("0000", 0, 4);
//        Rect bounds = new Rect();// 矩形
//        textPaint.getTextBounds("六", 0, 1, bounds);
//        int textHeight = bounds.height();
//        Log.e(TAG,"文字宽度："+leftTitleWidth+" 文字高度："+textHeight);
        xOrigin = padding + leftTitleWidth;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //TODO 注意：在这个方法中首次得到getWidth的宽度等于0
        // width = getWidth();
//        xWidth = width - 2f * padding - leftTitleWidth;
//        yOrigin = xWidth / 2f + padding + 30;//按比例得到高度

        width = MeasureSpec.getSize(widthMeasureSpec);
        xWidth = width - 2f * padding - leftTitleWidth;
        yOrigin = xWidth / 2f + padding + 30;//按大概比例得到高度（自己可以随便定义）
        int height = (int) (yOrigin + leftTitleWidth / 2 + padding);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算view的宽度
//        width = getWidth();
//        Log.e(TAG,"view 的宽度："+width);
//        xWidth = width - 2f * padding - leftTitleWidth;
//        yOrigin = xWidth / 2f + padding + 30;//按比例得到高度

        //绘制坐标线
        linePaint.setStrokeWidth(DensityUtils.dp2px(getContext(), 1));
        linePaint.setColor(lineColor);
        canvas.drawLine(xOrigin, padding, xOrigin, yOrigin, linePaint);
        canvas.drawLine(xOrigin, yOrigin, xOrigin + xWidth, yOrigin, linePaint);

        //画间隔线
        linePaint.setStrokeWidth(DensityUtils.dp2px(getContext(), 0.5f));
        float ySpaceHeight = (yOrigin - padding * 2) / 4f;//间隔
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(xOrigin + padding, yOrigin - ySpaceHeight * i,
                    xOrigin + xWidth, yOrigin - ySpaceHeight * i, linePaint);
        }

        //画y坐标单位、y轴坐标标题
        float xTextCenter = padding + leftTitleWidth / 2f;
        canvas.drawText(unit, xTextCenter, 3 * padding / 4, textPaint);

        for (int i = 0; i < yTitles.length; i++) {
            canvas.drawText(yTitles[i], xTextCenter, yOrigin - ySpaceHeight * 2 * i, textPaint);
        }
        //画X轴坐标
        getXSpaceWidth();
        float x = xOrigin + padding;
        float y = yOrigin + leftTitleWidth / 1.5f;
        switch (timeType) {
            case TIME:
                //绘制时刻点
                for (int i = 0; i <= 24 * 60 * 60; i += 6 * 60 * 60) {
                    canvas.drawText(xTitles[i / (6 * 60 * 60)], x + xSpaceWidth * i, y, textPaint);
                }
                //绘制临界日期
                y = yOrigin + leftTitleWidth / 0.9f;
                if (TextUtils.isEmpty(selectedDate)) {
                    selectedDate = DateUtil.convertDateToString(DateUtil.DATE_DOT, new Date());
                }
                String currentMonth = DateUtil.changeTime(DateUtil.DATE_DOT, DateUtil.DATE_MONTH, selectedDate);
                if (currentMonth.startsWith("0")) {
                    currentMonth = currentMonth.replaceFirst("0", "");
                }
                String currentDay = DateUtil.changeTime(DateUtil.DATE_DOT, DateUtil.DATE_DAY, selectedDate);
                if (currentDay.startsWith("0")) {
                    currentDay = currentDay.replaceFirst("0", "");
                }
                String nextDate = DateUtil.addDate(DateUtil.DATE_DOT, selectedDate, DateUtil.DATE_DAY, 1);
                String nextMonth = DateUtil.changeTime(DateUtil.DATE_DOT, DateUtil.DATE_MONTH, nextDate);
                if (nextMonth.startsWith("0")) {
                    nextMonth = nextMonth.replaceFirst("0", "");
                }
                String nextDay = DateUtil.changeTime(DateUtil.DATE_DOT, DateUtil.DATE_DAY, nextDate);
                if (nextDay.startsWith("0")) {
                    nextDay = nextDay.replaceFirst("0", "");
                }
                canvas.drawText(currentMonth + "/" + currentDay, x, y, textPaint);
                canvas.drawText(nextMonth + "/" + nextDay, x + xSpaceWidth * 24 * 60 * 60, y, textPaint);

                break;
            case WEEK:
                for (int i = 0; i < xTitles.length; i++) {
                    canvas.drawText(xTitles[i], xOrigin + padding + xSpaceWidth * i, y, textPaint);
                }
                break;
            case MONTH:
                for (int i = 0; i < 31; i++) {
                    if (i == 0) {//1
                        canvas.drawText(xTitles[0], x + xSpaceWidth * i, y, textPaint);
                    } else if (i == 6) {//7
                        canvas.drawText(xTitles[1], x + xSpaceWidth * i, y, textPaint);
                    } else if (i == 13) {//14
                        canvas.drawText(xTitles[2], x + xSpaceWidth * i, y, textPaint);
                    } else if (i == 20) {//21
                        canvas.drawText(xTitles[3], x + xSpaceWidth * i, y, textPaint);
                    } else if (i == 27) {//28
                        canvas.drawText(xTitles[4], x + xSpaceWidth * i, y, textPaint);
                    }
                }
                break;
        }

        // TODO 绘制折线
        if (values != null && values.length > 0) {
            if (timeType == TIME) {
                if (times != null && times.length > 0) {
                    drawLinePoint(canvas);
                }
            } else {
                drawLinePoint(canvas);
            }
        }


    }

    //绘制折线
    private void drawLinePoint(Canvas canvas) {
        //清除所有的折线路径
        if (pathList.size() > 0) {
            for (Path path : pathList) {
                path.rewind();
            }
        }
        pointFs = new PointF[values.length];
        float x = xOrigin + padding;
        for (int i = 0; i < values.length; i++) {
            if (timeType == TIME) {
                pointFs[i] = computePoint(values[i], x + xSpaceWidth * times[i]);
            } else {
                pointFs[i] = computePoint(values[i], x + xSpaceWidth * i);
            }
        }
        pathList.clear();
        pathList.add(mPath);

        boolean isStartDrawPath = false;
        ArrayList<PointF> dashPoint = new ArrayList<>();//虚线点
        boolean isDashPoint = false;


        for (int i = 0; i < pointFs.length; i++) {
            PointF point = pointFs[i];
            if (point.y > 0) {
                isStartDrawPath = true;
                Path currentPath = pathList.get(pathList.size() - 1);
                if (currentPath.isEmpty()) {
                    currentPath.moveTo(point.x, point.y);
                } else {
                    currentPath.lineTo(point.x, point.y);
                }
                //TODO
                //把画虚线需要的点放在一个集合中
                if (isDashPoint) {
                    //把虚点的下一个实点添加到集合
                    dashPoint.add(point);
                    isDashPoint = false;
                }
            } else {
                if (isStartDrawPath) {
                    Path newPath = new Path();
                    pathList.add(newPath);
                    //TODO 该点为虚点
                    if (!isDashPoint) {
                        isDashPoint = true;
                        //把虚点的上一个实点添加到集合
                        dashPoint.add(pointFs[i - 1]);
                    }
                }
            }
        }

        //画折线
        linePaint.setColor(Color.RED);
        for (Path path : pathList) {
            canvas.drawPath(path, linePaint);
        }

        // TODO 画虚线
        if (dashPoint.size() > 1) {
            for (int i = 0; i < dashPoint.size(); i += 2) {
                if (i + 1 <= dashPoint.size() - 1) {
                    PointF point1 = dashPoint.get(i);
                    PointF point2 = dashPoint.get(i + 1);
                    Path path = new Path();
                    path.moveTo(point1.x, point1.y);
                    path.lineTo(point2.x, point2.y);
                    canvas.drawPath(path, dashPaint);
                }
            }
        }

        //画点
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(DensityUtils.dp2px(getContext(), 2));
        for (PointF p : pointFs) {
            canvas.drawPoint(p.x, p.y, linePaint);
        }

        //画竖线
        if (verticalLineX != 0&&isShowVerticalLineX) {
            linePaint.setStrokeWidth(DensityUtils.dp2px(getContext(), 0.5f));
            canvas.drawLine(verticalLineX, yOrigin, verticalLineX, padding * 2 - 5, linePaint);
        }

    }

    /**
     * 计算点坐标
     *
     * @param values
     * @return
     */
    private PointF computePoint(int values, float x) {
        final PointF point = new PointF();
        //tODO 待修改
        float height = yOrigin - padding * 2;//
        point.x = x;
        if (values > 0) {
            point.y = yOrigin - (values - minValue) * height / (maxValue - minValue);
        } else {
            point.y = -5;//这个值随便取，只要小于0就可以
        }
        return point;
    }


    //获得X轴标题之间的间隔距离
    private void getXSpaceWidth() {
        switch (timeType) {
            case TIME:
                xSpaceWidth = (xWidth - padding * 2) / (24 * 60 * 60);
                break;
            case WEEK:
                xSpaceWidth = (xWidth - padding * 2) / 6;
                break;
            case MONTH:
                xSpaceWidth = (xWidth - padding * 2) / 30;
                break;
        }
    }

    //设置x轴类型标题
    private void setXTitleType() {
        switch (timeType) {
            case TIME:
                xTitles = new String[]{"00:00", "06:00", "12:00", "18:00", "00:00"};
                break;
            case WEEK:
                xTitles = new String[]{"日", "一", "二", "三", "四", "五", "六"};
                break;
            case MONTH:
                xTitles = new String[]{"1", "7", "14", "21", "28"};
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (pointFs == null || pointFs.length == 0) return false;

        float y = event.getY();
        float xStart = xOrigin + padding;
        float xEnd = width - padding;
        verticalLineX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (verticalLineX > xStart && verticalLineX < xEnd && y > 0 && y < yOrigin) {
                    // TODO 手指在范围内
                    for(int i=0;i<pointFs.length;i++){
                        if(Math.abs(verticalLineX-pointFs[i].x)<=1){
                            if (listener != null) {
                                listener.onSelected(i,pointFs[i].x,pointFs[i].y,values[i],true);
                            }
                           break;
                        }else{
                            if (listener != null) {
                                listener.onSelected(i,pointFs[i].x,pointFs[i].y,values[i],false);
                            }
                        }
                    }


                } else {
                    verticalLineX = 0;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                setVerticalLineX();
                break;
        }

        return true;
    }

    /**
     * 手指抬起 计算最近的点
     */
    private void setVerticalLineX() {
        int index;
        if (verticalLineX <= xOrigin + padding) {
            verticalLineX = pointFs[0].x;
            index = 0;
        } else if (verticalLineX >= xWidth + leftTitleWidth) {
            verticalLineX = pointFs[pointFs.length - 1].x;
            index = pointFs.length - 1;
        } else {
            float[] arr = new float[pointFs.length];
            for (int i = 0; i < pointFs.length; i++) {
                arr[i] = pointFs[i].x;
            }

            Arrays.sort(arr);
            int i = Arrays.binarySearch(arr, verticalLineX);
            if (i < 0) {
                i = -i - 1;
                if (i >= arr.length || arr[i] - verticalLineX > verticalLineX - arr[i - 1]) {
                    i--;
                }
            }
            verticalLineX = pointFs[i].x;
            index = i;
        }
        if (listener != null) {
            listener.onSelected(index,pointFs[index].x,pointFs[index].y,values[index],true);
        }
        invalidate();
    }

    //设置数据
    public void setData(int[] data) {
        this.values = data;
        invalidate();
    }

    //设置数据(仅当typeTime==TIME的时候使用)
    public void setData(int[] dataValues, int[] timeValues) {
        this.values = dataValues;
        this.times = timeValues;
        invalidate();
    }

    //设置是否开启内部滑动竖线
    public void setShowVerticalLineX(boolean showVerticalLineX) {
        isShowVerticalLineX = showVerticalLineX;
    }
    //设置是否开启内部滑动竖线监听事件
    public void setSelectCallbackListener(SelectCallbackListener listener) {
        this.listener = listener;
    }

    public interface SelectCallbackListener {
        /**
         *
         * @param num 选中的点的位置
         * @param x 选中的点的x坐标
         * @param y 选中的点y坐标
         * @param value  选中的点对应的数据值
         * @param isSelected  是否在选中某个点的左右范围
         */
        void onSelected(int num, float x, float y, float value ,boolean isSelected);
    }
}
