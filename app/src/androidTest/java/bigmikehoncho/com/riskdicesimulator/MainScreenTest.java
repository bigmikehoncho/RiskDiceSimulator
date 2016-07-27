package bigmikehoncho.com.riskdicesimulator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickResolveBattle_SnackbarWarningShows() throws Exception {
        onView(withId(R.id.btn_resolve_attack))
                .perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(isDisplayed()));
    }

    @Test
    public void changeBattleSettings_LaunchResolveScreen() throws Exception {
        onView(withId(R.id.numberPicker_attackerUnitCount)).perform(click()).perform(typeText("10"));
        onView(withId(R.id.numberPicker_defenderUnitCount)).perform(click()).perform(typeText("10"));

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(doesNotExist());
    }
}