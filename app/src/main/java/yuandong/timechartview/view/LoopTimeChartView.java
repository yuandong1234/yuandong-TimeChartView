package yuandong.timechartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * 可左右切换的折线图
 * Created by yuandong on 2017/2/20.
 */

public class LoopTimeChartView extends TimeLineChartView {
    private static String TAG = LoopTimeChartView.class.getSimpleName();

    private Scroller mScroller;
    private int index;//当前屏幕界面的位置
    private int criticalWidth;//临界长度，超过这个长度触发滚动上一屏或下一屏
    private int width;//当前屏幕中view的宽度
    private int lastPointX;//上一次按下的x坐标
    private int lastMoveX;//上一次滚动的长度
    private boolean isEvent;//是否可以触发滚动
    private boolean canScrollToRight = false;//是否可以向右滚动到下一页
    private ChangeListener listener;
    private boolean isScrollComplete = true;//是否完成滚动

    /**
     * @param canScrolltoRright true ：不可以滑到下一页
     */
    public void setCanScrollToRight(boolean canScrolltoRright) {
        this.canScrollToRight = canScrolltoRright;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public LoopTimeChartView(Context context) {
        this(context, null);
    }

    public LoopTimeChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            if (isScrollComplete) {
                isScrollComplete = false;
                requestLayout();
            }
        }
    }

    private boolean intercept = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //判断是否在边缘开始滑动 否则不侧滑
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            if (x >= xOrigin - 2 && x <= width - padding) {
                if (isShowVerticalLineX) {
                    intercept = true;
                } else {
                    intercept = false;
                }
            } else {
                intercept = false;
            }
        }
        //拦截使父类处理事件
        if (intercept) return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPointX = (int) event.getX();
                mScroller.forceFinished(true);
                isEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(lastPointX - event.getX()) > 100) {
                    isEvent = true;
                }
                if (isEvent) {
                    if (canScrollToRight && lastPointX > event.getX()) break;
                    int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
                    smoothScrollTo(totalMoveX);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isEvent) {
                    if (canScrollToRight && lastPointX > event.getX()) break;
                    if (Math.abs(lastPointX - event.getX()) > 25) {
                        int orientation = 0;
                        if (lastPointX > event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            index++;
                            orientation = 1;
                        } else if (lastPointX < event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            index--;
                            orientation = -1;
                        }
                        smoothScrollTo(width * index);
                        lastMoveX = width * index;
                        if (listener != null && orientation != 0) listener.callback(orientation);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.width = w;
        criticalWidth = (int) (1F / 3F * w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw ()...");
        //根据坐标画左右和中间的视图
        draw(canvas, width * (index - 1), null);
        draw(canvas, width * index, values);
        draw(canvas, width * (index + 1), null);
    }

    private void draw(Canvas canvas, int position, int[] values) {
        canvas.save();
        canvas.translate(position, 0);
        super.drawView(canvas, values);
        canvas.restore();

    }

    private void smoothScrollTo(int fx) {
        int dx = fx - mScroller.getFinalX();
        int dy = -mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();
    }


    public void scroll(int orientation) {
        index += orientation;
        smoothScrollTo(width * index);
        lastMoveX = width * index;
    }

    public interface ChangeListener {
        void callback(int orientation);
    }
}

