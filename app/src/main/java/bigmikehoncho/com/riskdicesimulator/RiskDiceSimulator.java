package bigmikehoncho.com.riskdicesimulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dice roll simulator for the game of Risk.  Roll large amounts of attacks at once and get results quickly.
 * Use setters to set the amount of attackers and defenders
 */
public class RiskDiceSimulator implements Serializable{
    private static final Logger logger = Logger.getLogger(RiskDiceSimulator.class.getName());

    private int attackerUnitCount;
    private int defenderUnitCount;
    private int attackerSafety;
    private int defenderSafety;

    private int attackersLost;
    private int defendersLost;

    private List<List<Integer>> attackerLog;
    private List<List<Integer>> defenderLog;

    public RiskDiceSimulator() {
        attackerLog = new ArrayList<>();
        defenderLog = new ArrayList<>();
    }

    public RiskDiceSimulator(RiskDiceSimulator simulator){
        attackerUnitCount = simulator.getAttackerUnitCount();
        defenderUnitCount = simulator.getDefenderUnitCount();
        attackerSafety = simulator.getAttackerSafety();
        defenderSafety = simulator.getDefenderSafety();
        attackersLost = simulator.getAttackersLost();
        defendersLost = simulator.getDefendersLost();
        attackerLog = simulator.getAttackerLog();
        defenderLog = simulator.getDefenderLog();
    }

    @Override
    public String toString() {
        return "attackers: " + attackerUnitCount +
                ", lost: " + attackersLost +
                ", safety: " + attackerSafety +
                ", defenders: " + defenderUnitCount +
                ", lost: " + defendersLost +
                ", safety: " + defenderSafety;
    }

    public void simulateAll() {
        while (isAttackPossible()) {
            rollOnce();
            logger.log(Level.INFO, "Attackers Remaining: " + attackerUnitCount + " - Defenders Remaining: " + defenderUnitCount);
        }
    }

    public void rollOnce() {
        int attackerDiceCount = attackerUnitCount;
        int defenderDiceCount = defenderUnitCount;
        if (attackerDiceCount > 3) {
            attackerDiceCount = 3;
        } else if (attackerDiceCount > 1) {
            attackerDiceCount -= 1;
        }
        if (defenderDiceCount > 1) {
            defenderDiceCount = 2;
        }

        ArrayList<Integer> attackDice = generateDice(attackerDiceCount, 6);
        ArrayList<Integer> defenseDice = generateDice(defenderDiceCount, 6);

        int[] unitsLost = getDiceResults(attackDice, defenseDice);
        logger.log(Level.INFO, "Attacker Rolls: " + attackDice + " - Defender Rolls: " + defenseDice);
        logger.log(Level.INFO, "Attacker Lost: " + unitsLost[0] + " - Defender Lost: " + unitsLost[1]);

        attackerUnitCount -= unitsLost[0];
        defenderUnitCount -= unitsLost[1];
        attackersLost += unitsLost[0];
        defendersLost += unitsLost[1];

        attackerLog.add(attackDice);
        defenderLog.add(defenseDice);
    }

    protected static ArrayList<Integer> generateDice(int diceCount, int sideCount){
        ArrayList<Integer> dice = new ArrayList<>(diceCount);
        Random random = new Random();
        for (int i = 0; i < diceCount; i++) {
            dice.add(random.nextInt(sideCount));
        }
        return dice;
    }

    /*Find the results from one roll of the attack and defense dice*/
    protected static int[] getDiceResults(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){
        Collections.sort(attackDice, Collections.<Integer>reverseOrder());
        Collections.sort(defenseDice, Collections.<Integer>reverseOrder());
        int[] unitsLost = new int[2]; // First sub = attackersLost, second sub = defendersLost

        for (int i = 0; i < attackDice.size() && i < defenseDice.size(); i++) {
            if (attackDice.get(i) > defenseDice.get(i)) {
                unitsLost[1]++;
            } else {
                unitsLost[0]++;
            }
        }

        return unitsLost;
    }

    public boolean isAttackPossible(){
        int possibleUnitsToLose = attackerUnitCount > defenderUnitCount ? Math.min(defenderUnitCount, 2) : Math.min(attackerUnitCount-1, 2);
        return defenderUnitCount > 0
                && attackerUnitCount > 1
                && attackerUnitCount - possibleUnitsToLose >= attackerSafety
                && defenderUnitCount - possibleUnitsToLose >= defenderSafety;
    }

    public void clearBattleHistory(){
        attackersLost = 0;
        defendersLost = 0;
        attackerLog.clear();
        defenderLog.clear();
    }

    public void setAttackerUnitCount(int attackerUnitCount) {
        this.attackerUnitCount = attackerUnitCount;
    }

    public void setDefenderUnitCount(int defenderUnitCount) {
        this.defenderUnitCount = defenderUnitCount;
    }

    public int getAttackerSafety() {
        return attackerSafety;
    }

    public void setAttackerSafety(int attackerLimit) {
        this.attackerSafety = attackerLimit;
    }

    public int getDefenderUnitCount() {
        return defenderUnitCount;
    }

    public int getAttackerUnitCount() {
        return attackerUnitCount;
    }

    public int getAttackersLost() {
        return attackersLost;
    }

    public int getDefendersLost() {
        return defendersLost;
    }

    public void setAttackersLost(int attackersLost) {
        this.attackersLost = attackersLost;
    }

    public void setDefendersLost(int defendersLost) {
        this.defendersLost = defendersLost;
    }

    public int getDefenderSafety() {
        return defenderSafety;
    }

    public void setDefenderSafety(int defenderSafety) {
        this.defenderSafety = defenderSafety;
    }

    public List<Integer> getAttackersLastRoll(){
        return attackerLog.get(attackerLog.size()-1);
    }

    public List<Integer> getDefendersLastRoll(){
        return defenderLog.get(defenderLog.size()-1);
    }

    public List<List<Integer>> getAttackerLog() {
        return attackerLog;
    }

    public List<List<Integer>> getDefenderLog() {
        return defenderLog;
    }
}
