package bigmikehoncho.com.riskdicesimulator;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Fragment for resolving the Risk attack.  Set up the attack and then send a RiskDiceSimulator to simulate it here.
 */
public class ResolveAttackDialog extends DialogFragment {
    private static final String TAG = ResolveAttackDialog.class.getSimpleName();

    public static final String ARG_SIMULATOR = "simulator";
    private static final String STATE_SIMULATOR = "simulator";
    private static final String STATE_TOGGLE = "toggle";
    private static final long DEFAULT_ATTACK_SPEED = 1750; // 1.75 seconds

    private Context mContext;
    private CountDownTimer mTimer;
    private RiskDiceSimulator mDiceSimulator;

    private Button mBtnQuickComplete;
    private ToggleButton mTogglePause;
    private Drawable mDrawPause;
    private Drawable mDrawPlay;
    private TextView mTextAttackersRemaining;
    private TextView mTextDefendersRemaining;
    private TextView mTextAttackersLost;
    private TextView mTextDefendersLost;
    private TextView mTextAttackerSafety;
    private TextView mTextDefenderSafety;

    private Animation mAnimBulge;

    public void setDefendersRemaining(int defendersRemaining) {
        String currentText = mTextDefendersRemaining.getText().toString();
        if(currentText.isEmpty() || defendersRemaining != Integer.parseInt(currentText)) {
            mTextDefendersRemaining.setText(String.valueOf(defendersRemaining));
            mTextDefendersRemaining.startAnimation(mAnimBulge);
        }
    }

    public void setDefendersLost(int defendersLost) {
        String currentText = mTextDefendersLost.getText().toString();
        if(currentText.isEmpty() || defendersLost != Integer.parseInt(currentText)) {
            mTextDefendersLost.setText(String.valueOf(defendersLost));
            mTextDefendersLost.startAnimation(mAnimBulge);
        }
    }

    public void setAttackersRemaining(int attackersRemaining) {
        String currentText = mTextAttackersRemaining.getText().toString();
        if(currentText.isEmpty() || attackersRemaining != Integer.parseInt(currentText)) {
            mTextAttackersRemaining.setText(String.valueOf(attackersRemaining));
            mTextAttackersRemaining.startAnimation(mAnimBulge);
        }
    }

    public void setAttackersLost(int attackersLost) {
        String currentText = mTextAttackersLost.getText().toString();
        if(currentText.isEmpty() || attackersLost != Integer.parseInt(currentText)) {
            mTextAttackersLost.setText(String.valueOf(attackersLost));
            mTextAttackersLost.startAnimation(mAnimBulge);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(STATE_SIMULATOR, mDiceSimulator);
        outState.putBoolean(STATE_TOGGLE, mTogglePause.isChecked());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        mContext = getContext();

        mDrawPause = ContextCompat.getDrawable(mContext, android.R.drawable.ic_media_pause);
        mDrawPlay = ContextCompat.getDrawable(mContext, android.R.drawable.ic_media_play);
        mAnimBulge = AnimationUtils.loadAnimation(mContext, R.anim.bulge);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_resolve_attack, null);
        setFields(view);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mDiceSimulator = new RiskDiceSimulator((RiskDiceSimulator) args.getSerializable(ARG_SIMULATOR));
            mTogglePause.setButtonDrawable(mDrawPause);
            mTogglePause.setChecked(true);
        } else {
            mDiceSimulator = (RiskDiceSimulator) savedInstanceState.getSerializable(STATE_SIMULATOR);
            if (savedInstanceState.getBoolean(STATE_TOGGLE)) {
                mTogglePause.setButtonDrawable(mDrawPause);
                mTogglePause.setChecked(true);
            } else {
                mTogglePause.setButtonDrawable(mDrawPlay);
                mTogglePause.setChecked(false);
            }
        }

        if (mDiceSimulator == null) {
            Log.w(TAG, "No RiskDiceSimulator set!");
            dismiss();
        }

        setUI();
        checkShouldStartSimulator();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setView(view);

        return builder.create();
    }

    private void setFields(View view) {
        mBtnQuickComplete = (Button) view.findViewById(R.id.btn_quick_complete);
        mTogglePause = (ToggleButton) view.findViewById(R.id.btn_toggle_attack);

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.layout_attack);
        mTextAttackersLost = (TextView) ll.findViewById(R.id.text_attackersLost);
        mTextAttackersRemaining = (TextView) ll.findViewById(R.id.text_attackersRemaining);
        mTextDefendersLost = (TextView) ll.findViewById(R.id.text_defendersLost);
        mTextDefendersRemaining = (TextView) ll.findViewById(R.id.text_defendersRemaining);
        mTextAttackerSafety = (TextView) ll.findViewById(R.id.text_attackerSafety);
        mTextDefenderSafety = (TextView) ll.findViewById(R.id.text_defenderSafety);

        mTimer = new CountDownTimer(DEFAULT_ATTACK_SPEED, DEFAULT_ATTACK_SPEED / 10) {
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
                    mTogglePause.setButtonDrawable(mDrawPause);
                    continualAttack();
                } else {
                    mTogglePause.setButtonDrawable(mDrawPlay);
                    mTimer.cancel();
                }
            }
        });
        mBtnQuickComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mDiceSimulator.rollDice();
                setUI();
            }
        });
    }

    private void setUI() {
        setAttackersLost(mDiceSimulator.getAttackersLost());
        setAttackersRemaining(mDiceSimulator.getAttackerUnitCount());
        setDefendersLost(mDiceSimulator.getDefendersLost());
        setDefendersRemaining(mDiceSimulator.getDefenderUnitCount());
        mTextAttackerSafety.setText(String.valueOf(mDiceSimulator.getAttackerSafety()));
        mTextDefenderSafety.setText(String.valueOf(mDiceSimulator.getDefenderSafety()));

        checkShouldStartSimulator();
    }

    private void checkShouldStartSimulator(){
        if (mDiceSimulator.isAttackPossible()) {
            if(mTogglePause.isChecked()) {
                mTimer.start();
            }
        } else {
            mTogglePause.setEnabled(false);
            mBtnQuickComplete.setEnabled(false);
        }
    }

    private void continualAttack() {
        Log.i(TAG, "continualAttack");
        mDiceSimulator.rollOnce();
        setUI();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        if (mTimer != null) {
            mTimer.cancel();
        }
        Data.getInstance().setLatestResults(mDiceSimulator);
        super.onDestroyView();
    }
}
