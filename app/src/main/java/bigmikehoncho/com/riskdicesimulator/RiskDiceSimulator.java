package bigmikehoncho.com.riskdicesimulator;

import java.io.Serializable;
import java.util.ArrayList;
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

    public RiskDiceSimulator() {
    }

    public RiskDiceSimulator(RiskDiceSimulator simulator){
        attackerUnitCount = simulator.getAttackerUnitCount();
        defenderUnitCount = simulator.getDefenderUnitCount();
        attackerSafety = simulator.getAttackerSafety();
        defenderSafety = simulator.getDefenderSafety();
        attackersLost = simulator.getAttackersLost();
        defendersLost = simulator.getDefendersLost();
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

    public void rollDice() {
        attackersLost = 0;
        defendersLost = 0;
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

        Random rand = new Random();
        ArrayList<Integer> attackDice = new ArrayList<>(attackerDiceCount);
        ArrayList<Integer> defenseDice = new ArrayList<>(defenderDiceCount);
        for (int i = 0; i < attackerDiceCount; i++) {
            int attack = rand.nextInt(6);
            if (i == 0) {
                attackDice.add(attack);
            } else {
                for (int j = 0; j < i; j++) {
                    if (attack > attackDice.get(j)) {
                        attackDice.add(j, attack);
                        break;
                    } else {
                        if (j == i - 1) {
                            attackDice.add(attack);
                        }
                        continue;
                    }
                }
            }
        }
        for (int i = 0; i < defenderDiceCount; i++) {
            int defend = rand.nextInt(6);
            if (i == 0) {
                defenseDice.add(defend);
            } else {
                for (int j = 0; j < i; j++) {
                    if (defend > defenseDice.get(j)) {
                        defenseDice.add(j, defend);
                        break;
                    } else {
                        if (j == i - 1) {
                            defenseDice.add(defend);
                        }
                        continue;
                    }
                }
            }
        }

        int[] unitsLost = new int[2]; // First sub = attackersLost - second sub = defendersLost

        for (int i = 0; i < attackerDiceCount && i < defenderDiceCount; i++) {
            if (attackDice.get(i) > defenseDice.get(i)) {
                unitsLost[1]++;
            } else {
                unitsLost[0]++;
            }
        }
        logger.log(Level.INFO, "Attacker Rolls: " + attackDice + " - Defender Rolls: " + defenseDice);
        logger.log(Level.INFO, "Attacker Lost: " + unitsLost[0] + " - Defender Lost: " + unitsLost[1]);

        attackerUnitCount -= unitsLost[0];
        defenderUnitCount -= unitsLost[1];
        attackersLost += unitsLost[0];
        defendersLost += unitsLost[1];
    }

    public boolean isAttackPossible(){
        int possibleUnitsToLose = attackerUnitCount > defenderUnitCount ? Math.min(defenderUnitCount, 2) : Math.min(attackerUnitCount-1, 2);
        return defenderUnitCount > 0
                && attackerUnitCount > 1
                && attackerUnitCount - possibleUnitsToLose >= attackerSafety
                && defenderUnitCount - possibleUnitsToLose >= defenderSafety;
    }

    public void clear(){
        attackersLost = 0;
        defendersLost = 0;
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
}
