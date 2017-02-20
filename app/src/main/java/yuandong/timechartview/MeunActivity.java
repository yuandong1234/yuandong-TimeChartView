package yuandong.timechartview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MeunActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meun);
        findViewById(R.id.tv_01).setOnClickListener(this);
        findViewById(R.id.tv_02).setOnClickListener(this);
        findViewById(R.id.tv_03).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_01:
                startActivity(new Intent(this,DayChartActivity.class));
                break;
            case R.id.tv_02:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.tv_03:
                startActivity(new Intent(this,MonthChartActivity.class));
                break;
        }
    }
}
