package bigmikehoncho.com.riskdicesimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String STATE_ATTACKERS = "attackers";
    private static final String STATE_DEFENDERS = "defenders";
    private static final String STATE_SAFETY = "safety";

    private RiskDiceSimulator mDiceSimulator;
    private ResultDialog mResultDialog;

    private Button btnRollDice;
    private Button btnRollDiceWithPause;
    private NumberPicker npAttackerUnitCount;
    private NumberPicker npDefenderUnitCount;
    private NumberPicker npAttackerSafety;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_ATTACKERS, npAttackerUnitCount.getValue());
        outState.putInt(STATE_DEFENDERS, npDefenderUnitCount.getValue());
        outState.putInt(STATE_SAFETY, npAttackerSafety.getValue());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFields();
        setUI();
        mDiceSimulator = new RiskDiceSimulator();

        if(savedInstanceState == null){
            npAttackerSafety.setValue(3);
        } else {
            npAttackerUnitCount.setValue(savedInstanceState.getInt(STATE_ATTACKERS));
            npDefenderUnitCount.setValue(savedInstanceState.getInt(STATE_DEFENDERS));
            npAttackerSafety.setValue(savedInstanceState.getInt(STATE_SAFETY));
        }
    }

    private void setFields(){
        btnRollDice = (Button) findViewById(R.id.btn_roll_dice);
        btnRollDiceWithPause = (Button) findViewById(R.id.btn_roll_dice_with_pause);
        npAttackerUnitCount = (NumberPicker) findViewById(R.id.numberPicker_attackerUnitCount);
        npDefenderUnitCount = (NumberPicker) findViewById(R.id.numberPicker_defenderUnitCount);
        npAttackerSafety = (NumberPicker) findViewById(R.id.numberPicker_attackerLimit);

        npAttackerUnitCount.setMinValue(0);
        npAttackerUnitCount.setMaxValue(200);
        npDefenderUnitCount.setMinValue(0);
        npDefenderUnitCount.setMaxValue(200);
        npAttackerSafety.setMinValue(0);
        npAttackerSafety.setMaxValue(200);

        btnRollDice.setOnClickListener(this);
        btnRollDiceWithPause.setOnClickListener(this);
    }

    private void setUI(){
    }

    private void rollDice(){
        mDiceSimulator.clear();
        mDiceSimulator.setAttackerUnitCount(npAttackerUnitCount.getValue());
        mDiceSimulator.setDefenderUnitCount(npDefenderUnitCount.getValue());
        mDiceSimulator.setAttackerLimit(npAttackerSafety.getValue());

        mDiceSimulator.rollDice();

        mResultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putLong(ResultDialog.ARG_PAUSE_TIME, 0);
        mResultDialog.setArguments(args);
        mResultDialog.setDiceSimulator(mDiceSimulator);
        mResultDialog.show(getSupportFragmentManager(), "results");
    }

    private void rollDiceWithPause(){
        mDiceSimulator.clear();
        mDiceSimulator.setAttackerUnitCount(npAttackerUnitCount.getValue());
        mDiceSimulator.setDefenderUnitCount(npDefenderUnitCount.getValue());
        mDiceSimulator.setAttackerLimit(npAttackerSafety.getValue());

        mResultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putLong(ResultDialog.ARG_PAUSE_TIME, 2000);
        mResultDialog.setArguments(args);
        mResultDialog.setDiceSimulator(mDiceSimulator);
        mResultDialog.show(getSupportFragmentManager(), "results");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_roll_dice:
                rollDice();
                break;
            case R.id.btn_roll_dice_with_pause:
                rollDiceWithPause();
                break;
        }
    }
}
