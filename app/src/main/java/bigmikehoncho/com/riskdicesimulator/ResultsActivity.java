package bigmikehoncho.com.riskdicesimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class ResultsActivity extends AppCompatActivity {
    private static final String TAG = ResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            ResultsActivityFragment fragmentResults = (ResultsActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment);
            Log.i(TAG, "fragment results: " + fragmentResults);
            if (fragmentResults != null) {
                fragmentResults.setDiceSimulator(Data.getInstance().getLatestResults());
            }
        }
    }

}
