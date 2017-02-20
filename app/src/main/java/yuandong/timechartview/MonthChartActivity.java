package yuandong.timechartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import yuandong.timechartview.view.TimeLineChartView;

public class MonthChartActivity extends AppCompatActivity implements View.OnClickListener {

    private TimeLineChartView chartView;
    int []values=new int[]{80,60,100,120,90,50,70};
    int []values1=new int[]{0,0,100,120,0,50,70};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        chartView=(TimeLineChartView)findViewById(R.id.chartView);
        findViewById(R.id.tv_01).setOnClickListener(this);
        findViewById(R.id.tv_02).setOnClickListener(this);
        chartView.setData(values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_01:
                chartView.setData(values);
                break;
            case R.id.tv_02:
                chartView.setData(values1);
                break;
        }
    }
}
