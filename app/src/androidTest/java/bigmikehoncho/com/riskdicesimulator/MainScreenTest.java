package bigmikehoncho.com.riskdicesimulator;

import android.content.pm.ActivityInfo;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenTest {

    private static final int ATTACKERS_INITIAL = 12;
    private static final int DEFENDERS_INITIAL = 10;
    private static final int ATTACKERS_SAFETY = 5;
    private static final int DEFENDERS_SAFETY = 3;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickResolveBattle_SnackbarWarningShows() {
        onView(withId(R.id.btn_resolve_attack))
                .perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(isDisplayed()));
    }

    @Test
    public void rotateDevice_CheckIfBattleSettingsRetained(){
        onView(withId(R.id.numberPicker_attackerUnitCount))
                .perform(TestUtils.setNumberPicker(ATTACKERS_INITIAL));

        onView(withId(R.id.numberPicker_defenderUnitCount))
                .perform(TestUtils.setNumberPicker(DEFENDERS_INITIAL));

        onView(withId(R.id.numberPicker_attackerSafety))
                .perform(TestUtils.setNumberPicker(ATTACKERS_SAFETY));

        onView(withId(R.id.numberPicker_defenderSafety))
                .perform(TestUtils.setNumberPicker(DEFENDERS_SAFETY));

        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView(withId(R.id.numberPicker_attackerUnitCount))
                .check(matches(withText(String.valueOf(ATTACKERS_INITIAL))));

        onView(withId(R.id.numberPicker_defenderUnitCount))
                .check(matches(withText(String.valueOf(DEFENDERS_INITIAL))));

        onView(withId(R.id.numberPicker_attackerSafety))
                .check(matches(withText(String.valueOf(ATTACKERS_SAFETY))));

        onView(withId(R.id.numberPicker_defenderSafety))
                .check(matches(withText(String.valueOf(DEFENDERS_SAFETY))));
    }

    @Test
    public void changeBattleSettings_LaunchResolveScreen() {
        onView(withId(R.id.btn_resolve_attack))
                .perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(doesNotExist());

        onView(withId(R.id.layout_attack))
                .check(matches(isDisplayed()));

        onView(withId(R.id.text_attackerSafety))
                .check(matches(withText(String.valueOf(ATTACKERS_SAFETY))));

        onView(withId(R.id.text_defenderSafety))
                .check(matches(withText(String.valueOf(DEFENDERS_SAFETY))));

    }

//    @Test
//    public void simulateBattle_CheckResults(){
//        onView(withText(R.string.attack_complete))
//                .perform(click());
//
//        onView(withId(R.id.text_attackersRemaining)).check(align);
//
//    }

}