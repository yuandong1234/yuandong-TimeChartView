package yuandong.timechartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import yuandong.timechartview.view.TimeLineChartView;

public class DayChartActivity extends AppCompatActivity implements View.OnClickListener {
    private TimeLineChartView chartView;
    private  int[]dataValues={110,90,95,80,60,120,70,80};

    private  int[]dataValues1={0,90,95,0,0,0,70,0};
    private int n=60*60;
    private  int[]timeValues={0,n,2*n,3*n,
            4*n,5*n,6*n,7*n }; 


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_chart);
        chartView = (TimeLineChartView) findViewById(R.id.chartView);
        findViewById(R.id.tv_01).setOnClickListener(this);
        findViewById(R.id.tv_02).setOnClickListener(this);
        chartView.setData(dataValues,timeValues);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_01:
                chartView.setData(dataValues,timeValues);
                break;
            case R.id.tv_02:
                chartView.setData(dataValues1,timeValues);
                break;
        }
    }
}
