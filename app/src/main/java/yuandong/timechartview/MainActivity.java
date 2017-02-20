package yuandong.timechartview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import yuandong.timechartview.view.TimeLineChartView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private TimeLineChartView chartView;
    private RelativeLayout toolBar;
    private PopupWindow pop;

    int[] values = new int[]{80, 60, 100, 120, 90, 50, 70};
    int[] values1 = new int[]{0, 0, 100, 120, 0, 50, 70};
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (pop != null) {
                pop.dismiss();
                pop = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = (TimeLineChartView) findViewById(R.id.chartView);
        toolBar = (RelativeLayout) findViewById(R.id.titleBar);
        findViewById(R.id.tv_01).setOnClickListener(this);
        findViewById(R.id.tv_02).setOnClickListener(this);
        chartView.setShowVerticalLineX(true);
        chartView.setData(values);
        chartView.setSelectCallbackListener(new TimeLineChartView.SelectCallbackListener() {

            @Override
            public void onSelected(int num, float x, float y, float value, boolean isSelected) {
                Log.e(TAG, "num :" + num + " x :" + x + " y" + " value:" + value + " isSelected :" + isSelected);
                if (isSelected) {
                    if (pop != null) {
                        pop.dismiss();
                        pop = null;
                    }

                    View view = View.inflate(MainActivity.this, R.layout.layout_popwidow, null);
                    TextView time = (TextView) view.findViewById(R.id.time);
                    TextView data = (TextView) view.findViewById(R.id.value);
                    time.setText("时间点 ："+num + "");
                    data.setText("心率值 ："+value + "");
                    pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
                    pop.showAsDropDown(toolBar, (int) x, (int) y);
                    handler.postDelayed(runnable,500);
                } else {
                        handler.removeCallbacks(runnable);
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_01:
                chartView.setData(values);
                break;
            case R.id.tv_02:
                chartView.setData(values1);
                break;
        }
    }
}
