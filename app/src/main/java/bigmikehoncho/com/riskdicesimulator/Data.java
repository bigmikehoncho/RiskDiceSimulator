package bigmikehoncho.com.riskdicesimulator;

import java.util.ArrayList;

/**
 * Created by Sarah on 7/7/2016.
 */
public class Data {
    private static final String TAG = Data.class.getSimpleName();

    private static Data ourInstance = new Data();
    private ArrayList<RiskDiceSimulator> mListResults;

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
        mListResults = new ArrayList<>();
    }

    public void setLatestResults(RiskDiceSimulator diceSimulator){
        mListResults.add(0, diceSimulator);
    }

    public RiskDiceSimulator getLatestResults(){
        if(mListResults.isEmpty()){
            return null;
        } else {
            return mListResults.get(0);
        }
    }

    public boolean isResultsEmpty(){
        return mListResults.isEmpty();
    }
}
