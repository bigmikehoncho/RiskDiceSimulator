package bigmikehoncho.com.riskdicesimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ResultsActivity extends AppCompatActivity {
    private static final String TAG = ResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ResultsActivityFragment fragmentResults = (ResultsActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if(fragmentResults != null){
            fragmentResults.setDiceSimulator(Data.getInstance().getLatestResults());
        }
    }

}
