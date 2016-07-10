package bigmikehoncho.com.riskdicesimulator;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsActivityFragment extends Fragment {
    private static final String TAG = ResultsActivityFragment.class.getSimpleName();

    public static final String ARG_SIMULATOR = "simulator";
    private static final String STATE_SIMULATOR = "simulator";

    private RiskDiceSimulator mDiceSimulator;

    private TextView mTextAttackersRemaining;
    private TextView mTextDefendersRemaining;
    private TextView mTextAttackersLost;
    private TextView mTextDefendersLost;

    public ResultsActivityFragment() {

    }

    public void setDiceSimulator(RiskDiceSimulator diceSimulator) {
        mDiceSimulator = diceSimulator;
        setUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(STATE_SIMULATOR, mDiceSimulator);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(savedInstanceState == null) {
//            Bundle args = getArguments();
//            mDiceSimulator = (RiskDiceSimulator) args.getSerializable(ARG_SIMULATOR);
//        } else {
//            mDiceSimulator = (RiskDiceSimulator) savedInstanceState.getSerializable(STATE_SIMULATOR);
//        }

        if(savedInstanceState == null){

        } else {
            mDiceSimulator = (RiskDiceSimulator) savedInstanceState.getSerializable(STATE_SIMULATOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFields(view);
        setUI();
    }

    protected void setFields(View view) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.layout_attack);
        mTextAttackersLost = (TextView) ll.findViewById(R.id.text_attackersLost);
        mTextAttackersRemaining = (TextView) ll.findViewById(R.id.text_attackersRemaining);
        mTextDefendersLost = (TextView) ll.findViewById(R.id.text_defendersLost);
        mTextDefendersRemaining = (TextView) ll.findViewById(R.id.text_defendersRemaining);
    }

    protected void setUI() {
        Log.i(TAG, "simulator: " + mDiceSimulator);
        if (mDiceSimulator != null) {
            mTextAttackersRemaining.setText(String.valueOf(mDiceSimulator.getAttackerUnitCount()));
            mTextAttackersLost.setText(String.valueOf(mDiceSimulator.getAttackersLost()));
            mTextDefendersRemaining.setText(String.valueOf(mDiceSimulator.getDefenderUnitCount()));
            mTextDefendersLost.setText(String.valueOf(mDiceSimulator.getDefendersLost()));
        }
    }
}
