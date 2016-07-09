package bigmikehoncho.com.riskdicesimulator;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsActivityFragment extends Fragment {
    private static final String TAG =ResultsActivityFragment.class.getSimpleName();

    private RiskDiceSimulator mDiceSimulator;

    private TextView mTvAttackersRemaining;

    public ResultsActivityFragment() {

    }

    public void setDiceSimulator(RiskDiceSimulator diceSimulator) {
        mDiceSimulator = diceSimulator;
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

    protected void setFields(View view){
        mTvAttackersRemaining = (TextView) view.findViewById(R.id.tv_attackersRemaining);
    }

    protected void setUI(){
        Log.i(TAG, "simulator: " + mDiceSimulator);
        if(mDiceSimulator != null) {
            mTvAttackersRemaining.setText(String.valueOf(mDiceSimulator.getAttackerUnitCount()));
        }
    }
}
