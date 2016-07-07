package bigmikehoncho.com.riskdicesimulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Sarah on 7/4/2016.
 */
public class ResultDialog extends DialogFragment {
    private static final String TAG = ResultDialog.class.getSimpleName();

    public static final String ARG_PAUSE_TIME = "pauseTime";
    private static final String STATE_ATTACKERS_REMAINING = "attackRemaining";
    private static final String STATE_ATTACKERS_LOST = "attackLost";
    private static final String STATE_DEFENDERS_REMAINING = "defenseRemaining";
    private static final String STATE_DEFENDERS_LOST = "defenseLost";
    private static final String STATE_ATTACKERS_LIMIT = "attackSafety";
    private static final String STATE_TOGGLE = "toggle";

    private Context mContext;
    private CountDownTimer mTimer;
    private RiskDiceSimulator mDiceSimulator;

    private ToggleButton mTogglePause;
    private Drawable drawPause;
    private Drawable drawPlay;
    private TextView mTextAttackersRemaining;
    private TextView mTextDefendersRemaining;
    private TextView mTextAttackersLost;
    private TextView mTextDefendersLost;

    private long pauseTime;

    public void setDefendersRemaining(int defendersRemaining) {
        mTextDefendersRemaining.setText(String.valueOf(defendersRemaining));
    }

    public void setDefendersLost(int defendersLost) {
        mTextDefendersLost.setText(String.valueOf(defendersLost));
    }

    public void setAttackersRemaining(int attackersRemaining) {
        mTextAttackersRemaining.setText(String.valueOf(attackersRemaining));
    }

    public void setAttackersLost(int attackersLost) {
        mTextAttackersLost.setText(String.valueOf(attackersLost));
    }

    public void setDiceSimulator(RiskDiceSimulator diceSimulator) {
        this.mDiceSimulator = diceSimulator;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_ATTACKERS_REMAINING, mDiceSimulator.getAttackerUnitCount());
        outState.putInt(STATE_ATTACKERS_LOST, mDiceSimulator.getAttackersLost());
        outState.putInt(STATE_DEFENDERS_REMAINING, mDiceSimulator.getDefenderUnitCount());
        outState.putInt(STATE_DEFENDERS_LOST, mDiceSimulator.getDefendersLost());
        outState.putInt(STATE_ATTACKERS_LIMIT, mDiceSimulator.getAttackerLimit());
        outState.putBoolean(STATE_TOGGLE, mTogglePause.isChecked());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        drawPause = ContextCompat.getDrawable(mContext, android.R.drawable.ic_media_pause);
        drawPlay = ContextCompat.getDrawable(mContext, android.R.drawable.ic_media_play);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_result, null);
        mTogglePause = (ToggleButton) view.findViewById(R.id.btn_toggle_attack);
        mTextAttackersLost = (TextView) view.findViewById(R.id.text_attackersLost);
        mTextAttackersRemaining = (TextView) view.findViewById(R.id.text_attackersRemaining);
        mTextDefendersLost = (TextView) view.findViewById(R.id.text_defendersLost);
        mTextDefendersRemaining = (TextView) view.findViewById(R.id.text_defendersRemaining);

        if(savedInstanceState == null){
            mTogglePause.setButtonDrawable(drawPlay);
        } else {
            mDiceSimulator = new RiskDiceSimulator();
            mDiceSimulator.setAttackerUnitCount(savedInstanceState.getInt(STATE_ATTACKERS_REMAINING));
            mDiceSimulator.setAttackersLost(savedInstanceState.getInt(STATE_ATTACKERS_LOST));
            mDiceSimulator.setDefenderUnitCount(savedInstanceState.getInt(STATE_DEFENDERS_REMAINING));
            mDiceSimulator.setDefendersLost(savedInstanceState.getInt(STATE_DEFENDERS_LOST));
            mDiceSimulator.setAttackerLimit(savedInstanceState.getInt(STATE_ATTACKERS_LIMIT));

            if(savedInstanceState.getBoolean(STATE_TOGGLE)){
                mTogglePause.setButtonDrawable(drawPause);
                mTogglePause.setChecked(true);
            } else {
                mTogglePause.setButtonDrawable(drawPlay);
                mTogglePause.setChecked(false);
            }
        }

        setFields();

        Bundle args = getArguments();
        pauseTime = args.getLong(ARG_PAUSE_TIME);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setView(view);

        /* TODO:: Latest results on main screen. History page possibly (stats).
        *  Animation for damaged player. Circular progress bar */

        return builder.create();
    }

    private void setFields() {
        if (mDiceSimulator != null) {
            setAttackersLost(mDiceSimulator.getAttackersLost());
            setAttackersRemaining(mDiceSimulator.getAttackerUnitCount());
            setDefendersLost(mDiceSimulator.getDefendersLost());
            setDefendersRemaining(mDiceSimulator.getDefenderUnitCount());

            if (pauseTime > 0) {
                mTogglePause.setEnabled(true);
                mTimer = new CountDownTimer(pauseTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        continualAttack();
                    }
                };
                mTogglePause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.i(TAG, "toggle: " + isChecked);
                        if (isChecked) {
                            mTogglePause.setButtonDrawable(drawPause);
                            continualAttack();
                        } else {
                            mTogglePause.setButtonDrawable(drawPlay);
                            mTimer.cancel();
                        }
                    }
                });
            }
        }
    }

    private void continualAttack() {
        Log.i(TAG, "continualAttack");
        mDiceSimulator.rollOnce();
        setFields();
        if (mDiceSimulator.isAttackPossible()) {
            mTimer.start();
        } else {
            mTogglePause.setEnabled(false);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.i(TAG, "onDismiss");
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDismiss(dialog);
    }
}
