package bigmikehoncho.com.riskdicesimulator;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.NumberPicker;

import org.hamcrest.Matcher;

/**
 * Created by michael on 27/8/16.
 */
public class TestUtils {

    public static ViewAction setNumberPicker(final int number) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                np.setValue(number);
            }
            @Override
            public String getDescription() {
                return "Set the number into the NumberPicker";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }
}
