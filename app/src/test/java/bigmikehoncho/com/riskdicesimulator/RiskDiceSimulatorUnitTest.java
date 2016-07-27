package bigmikehoncho.com.riskdicesimulator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RiskDiceSimulatorUnitTest {

    private static final int ATTACKERS_INITIAL = 10;
    private static final int DEFENDERS_INITIAL = 10;
    private static final int ATTACKERS_SAFETY = 3;
    private static final int DEFENDERS_SAFETY = 3;
    RiskDiceSimulator mSimulator;

    @Before
    public void setUp(){
        mSimulator = new RiskDiceSimulator();
        mSimulator.setAttackerSafety(ATTACKERS_SAFETY);
        mSimulator.setDefenderSafety(DEFENDERS_SAFETY);
        mSimulator.setAttackerUnitCount(ATTACKERS_INITIAL);
        mSimulator.setDefenderUnitCount(DEFENDERS_INITIAL);
    }

    @Test
    public void testAttackSimulation(){
        // Split
        ArrayList<Integer> attackDice = new ArrayList<>();
        attackDice.add(5);
        attackDice.add(0);
        attackDice.add(5);
        ArrayList<Integer> defenseDice = new ArrayList<>();
        defenseDice.add(0);
        defenseDice.add(5);

        int[] results = RiskDiceSimulator.getDiceResults(attackDice, defenseDice);
        int[] expected1 = {1, 1};
        assertArrayEquals(expected1, results);

        // Attackers lose 2
        attackDice.clear();
        attackDice.add(0);
        attackDice.add(0);
        attackDice.add(0);
        defenseDice.clear();
        defenseDice.add(0);
        defenseDice.add(0);

        results = RiskDiceSimulator.getDiceResults(attackDice, defenseDice);
        int[] expected2 = {2, 0};
        assertArrayEquals(expected2, results);

        // Defenders lose 2
        attackDice.clear();
        attackDice.add(2);
        attackDice.add(0);
        attackDice.add(1);
        defenseDice.clear();
        defenseDice.add(0);
        defenseDice.add(1);

        results = RiskDiceSimulator.getDiceResults(attackDice, defenseDice);
        int[] expected3 = {0, 2};
        assertArrayEquals(expected3, results);

        // Attackers lose 1
        attackDice.clear();
        attackDice.add(2);
        defenseDice.clear();
        defenseDice.add(0);
        defenseDice.add(2);

        results = RiskDiceSimulator.getDiceResults(attackDice, defenseDice);
        int[] expected4 = {1, 0};
        assertArrayEquals(expected4, results);

        // Defenders lose 1
        attackDice.clear();
        attackDice.add(5);
        attackDice.add(1);
        attackDice.add(0);
        defenseDice.clear();
        defenseDice.add(4);

        results = RiskDiceSimulator.getDiceResults(attackDice, defenseDice);
        int[] expected5 = {0, 1};
        assertArrayEquals(expected5, results);
    }

    @Test
    public void checkIfValidBattle(){
        assertTrue(mSimulator.isAttackPossible());
    }

    @Test
    public void runBattle(){
        mSimulator.rollOnce();
        assertTrue(mSimulator.getAttackerUnitCount() == ATTACKERS_INITIAL - mSimulator.getAttackersLost());
        assertTrue(mSimulator.getDefenderUnitCount() == DEFENDERS_INITIAL - mSimulator.getDefendersLost());

        mSimulator.simulateAll();
        assertEquals("Attacker units after battle", mSimulator.getAttackerUnitCount(), ATTACKERS_INITIAL - mSimulator.getAttackersLost());
        assertEquals("Defender units after battle", mSimulator.getDefenderUnitCount(), DEFENDERS_INITIAL - mSimulator.getDefendersLost());
    }
}