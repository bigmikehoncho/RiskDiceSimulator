package bigmikehoncho.com.riskdicesimulator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsFragment extends Fragment {
    private static final String TAG = ResultsFragment.class.getSimpleName();

    private static final String STATE_SIMULATOR = "simulator";

    private Context mContext;
    private RiskDiceSimulator mDiceSimulator;

    private TextView mTextAttackersRemaining;
    private TextView mTextDefendersRemaining;
    private TextView mTextAttackersLost;
    private TextView mTextDefendersLost;
    private TextView mTextAttackerSafety;
    private TextView mTextDefenderSafety;
    private PieChart mPC;

    public ResultsFragment() {

    }

    public void setDiceSimulator(RiskDiceSimulator diceSimulator) {
        mDiceSimulator = diceSimulator;
        setUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "onSaveInstanceState: " + mDiceSimulator);
        outState.putSerializable(STATE_SIMULATOR, mDiceSimulator);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if(savedInstanceState == null){

        } else {
            mDiceSimulator = (RiskDiceSimulator) savedInstanceState.getSerializable(STATE_SIMULATOR);
            Log.i(TAG, "onCreate: " + mDiceSimulator);
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
        mTextAttackerSafety = (TextView) ll.findViewById(R.id.text_attackerSafety);
        mTextDefenderSafety = (TextView) ll.findViewById(R.id.text_defenderSafety);
        mPC = (PieChart) view.findViewById(R.id.pieChart);
    }

    protected void setUI() {
        Log.i(TAG, "simulator: " + mDiceSimulator);
        if (mDiceSimulator != null) {
            mTextAttackersRemaining.setText(String.valueOf(mDiceSimulator.getAttackerUnitCount()));
            mTextAttackersLost.setText(String.valueOf(mDiceSimulator.getAttackersLost()));
            mTextDefendersRemaining.setText(String.valueOf(mDiceSimulator.getDefenderUnitCount()));
            mTextDefendersLost.setText(String.valueOf(mDiceSimulator.getDefendersLost()));
            mTextAttackerSafety.setText(String.valueOf(mDiceSimulator.getAttackerSafety()));
            mTextDefenderSafety.setText(String.valueOf(mDiceSimulator.getDefenderSafety()));

            float totalUnits = mDiceSimulator.getAttackersLost() + mDiceSimulator.getDefendersLost();
            int percentAttackersLost = Math.round(mDiceSimulator.getAttackersLost()/totalUnits * 100);
            int percentDefendersLost = Math.round(mDiceSimulator.getDefendersLost()/totalUnits * 100);

            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(mDiceSimulator.getDefendersLost(), percentDefendersLost + "%"));
            entries.add(new PieEntry(mDiceSimulator.getAttackersLost(), percentAttackersLost + "%"));
            PieDataSet dataSet= new PieDataSet(entries, "Units Lost");
            int[] colors = {ContextCompat.getColor(mContext, R.color.defender), ContextCompat.getColor(mContext, R.color.attacker)};
            dataSet.setColors(colors);
            dataSet.setDrawValues(false);

            PieData data = new PieData(dataSet);
            mPC.setData(data);
            mPC.setDescription(getString(R.string.results_units_lost));
            mPC.setEntryLabelColor(Color.BLACK);
            mPC.setDescriptionTextSize(20);
        }
    }
}
