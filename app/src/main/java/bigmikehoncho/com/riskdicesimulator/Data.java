package bigmikehoncho.com.riskdicesimulator;

/**
 * Created by Sarah on 7/7/2016.
 */
public class Data {
    private RiskDiceSimulator mLatestResults;

    private static Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
    }

    public void setLatestResults(RiskDiceSimulator diceSimulator){
        mLatestResults = diceSimulator;
    }

    public RiskDiceSimulator getLatestResults(){
        return mLatestResults;
    }
}
