package bigmikehoncho.com.riskdicesimulator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String STATE_SIMULATOR = "simulator";

    private RiskDiceSimulator mDiceSimulator;
    private ResolveAttackDialog mResolveAttackDialog;

    private View mContent;
    private Button btnResolveAttack;
    private NumberPicker npAttackerUnitCount;
    private NumberPicker npDefenderUnitCount;
    private NumberPicker npAttackerSafety;
    private NumberPicker npDefenderSafety;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        recordSimulatorSettings();
        Log.i(TAG, "onSaveInstanceState: " + mDiceSimulator);
        outState.putSerializable(STATE_SIMULATOR, mDiceSimulator);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFields();
        setFromSavedState(savedInstanceState);
        setUI();
    }

    private void setFields() {
        mContent = findViewById(android.R.id.content);
        btnResolveAttack = (Button) findViewById(R.id.btn_resolve_attack);
        npAttackerUnitCount = (NumberPicker) findViewById(R.id.numberPicker_attackerUnitCount);
        npDefenderUnitCount = (NumberPicker) findViewById(R.id.numberPicker_defenderUnitCount);
        npAttackerSafety = (NumberPicker) findViewById(R.id.numberPicker_attackerSafety);
        npDefenderSafety = (NumberPicker) findViewById(R.id.numberPicker_defenderSafety);

        npAttackerUnitCount.setMinValue(0);
        npAttackerUnitCount.setMaxValue(200);
        npDefenderUnitCount.setMinValue(0);
        npDefenderUnitCount.setMaxValue(200);
        npAttackerSafety.setMinValue(0);
        npAttackerSafety.setMaxValue(200);
        npDefenderSafety.setMinValue(0);
        npDefenderSafety.setMaxValue(200);

        btnResolveAttack.setOnClickListener(this);
    }

    private void setFromSavedState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mDiceSimulator = new RiskDiceSimulator();
            mDiceSimulator.setAttackerSafety(3);
        } else {
            mDiceSimulator = (RiskDiceSimulator) savedInstanceState.getSerializable(STATE_SIMULATOR);
        }
    }

    private void setUI() {
        npAttackerUnitCount.setValue(mDiceSimulator.getAttackerUnitCount());
        npDefenderUnitCount.setValue(mDiceSimulator.getDefenderUnitCount());
        npAttackerSafety.setValue(mDiceSimulator.getAttackerSafety());
        npDefenderSafety.setValue(mDiceSimulator.getDefenderSafety());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_results:
                if (Data.getInstance().isResultsEmpty()) {
                    Snackbar.make(mContent, R.string.warning_no_results, Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, ResultsActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_info:
                startActivity(new Intent(this, AppInfo.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resolveAttack() {
        mDiceSimulator.clear();
        recordSimulatorSettings();

        if (mDiceSimulator.isAttackPossible()) {
            mResolveAttackDialog = new ResolveAttackDialog();
            Bundle args = new Bundle();
            args.putSerializable(ResolveAttackDialog.ARG_SIMULATOR, mDiceSimulator);
            mResolveAttackDialog.setArguments(args);
            mResolveAttackDialog.show(getSupportFragmentManager(), "results");
        } else {
            Snackbar.make(mContent, R.string.warning_attack_not_possible, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void recordSimulatorSettings() {
        mDiceSimulator.setAttackerUnitCount(npAttackerUnitCount.getValue());
        mDiceSimulator.setDefenderUnitCount(npDefenderUnitCount.getValue());
        mDiceSimulator.setAttackerSafety(npAttackerSafety.getValue());
        mDiceSimulator.setDefenderSafety(npDefenderSafety.getValue());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resolve_attack:
                resolveAttack();
                break;
        }
    }
}
