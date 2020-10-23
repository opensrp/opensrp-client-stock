package org.smartregister.stock.openlmis.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.widgets.EditTextFactory;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.activity.OpenLMISJsonForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.smartregister.stock.openlmis.TestData.EDIT_TEXT_JSON;
import static org.smartregister.stock.openlmis.TestData.ISSUE_FORM_JSON;

/**
 * Created by samuelgithengi on 8/31/18.
 */
public class OpenLMISJsonFormFragmentTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private OpenLMISJsonFormFragment formFragment;

    private Context context = RuntimeEnvironment.application;

    private OpenLMISJsonForm activity;

    @Mock
    private MenuItem submitButton;


    private void setUp(String stepName) {
        Intent intent = new Intent();
        intent.putExtra("json", ISSUE_FORM_JSON);
        formFragment = OpenLMISJsonFormFragment.getFormFragment(stepName);
        activity = Robolectric.buildActivity(OpenLMISJsonForm.class, intent).create().get();
        activity.getSupportFragmentManager().beginTransaction().add(formFragment, null).commit();
    }

    @Test
    public void testOnCreateView() {
        setUp("step1");
        View view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        assertNotNull(view);

    }

    @Test
    public void testOnCreateViewWithNoPadding() {
        setUp("step3");
        View view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        assertNotNull(view);
        assertEquals(0, formFragment.getMainView().getPaddingBottom());
        assertEquals(0, formFragment.getMainView().getPaddingTop());
        assertEquals(0, formFragment.getMainView().getPaddingStart());
        assertEquals(0, formFragment.getMainView().getPaddingEnd());

    }

    @Test
    public void testInitializeBottomNavigation() {
        setUp("step1");
        View view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        formFragment.initializeBottomNavigation();
        assertEquals(View.GONE, view.findViewById(R.id.previous_button).getVisibility());
        assertEquals(View.VISIBLE, view.findViewById(R.id.next_button).getVisibility());
        assertFalse(view.findViewById(R.id.next_button).isEnabled());


        setUp("step2");
        view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        formFragment.initializeBottomNavigation();
        assertEquals(View.VISIBLE, view.findViewById(R.id.previous_button).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.next_button).getVisibility());
        assertEquals("Review", ((Button) view.findViewById(R.id.next_button)).getText());
        assertFalse(view.findViewById(R.id.next_button).isEnabled());


        setUp("step3");
        view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        formFragment.initializeBottomNavigation();

        assertEquals(View.VISIBLE, view.findViewById(R.id.previous_button).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.next_button).getVisibility());
        assertEquals("Submit", ((Button) view.findViewById(R.id.next_button)).getText());
        assertTrue(view.findViewById(R.id.next_button).isEnabled());

    }

    @Test
    public void testValidateActivateNext() throws Exception {
        setUp("step1");
        View view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        activity.clearConstrainedViews();
        activity.clearFormDataViews();
        formFragment = spy(formFragment);
        formFragment.setSubmitButton(submitButton);
        doReturn(true).when(formFragment).isVisible();
        RelativeLayout relativeLayout = (RelativeLayout) new EditTextFactory().getViewsFromJson("step1", activity, formFragment, new JSONObject(EDIT_TEXT_JSON), formFragment).get(0);
        assertFalse(view.findViewById(R.id.next_button).isEnabled());
        doReturn(true).when(formFragment).isVisible();
        try {
            for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                View child = relativeLayout.getChildAt(i);
                if (child instanceof RelativeLayout) {
                    for (int j = 0; j < ((RelativeLayout) child).getChildCount(); j++) {
                        View editText = ((RelativeLayout) child).getChildAt(0);
                        if (editText instanceof MaterialEditText) {
                            ((MaterialEditText) editText).setText("31-08-2018");
                            break;
                        }
                    }
                }
            }
        } catch (VerifyError e) {
        }
        formFragment.validateActivateNext();
        assertTrue(view.findViewById(R.id.next_button).isEnabled());
    }

    @Test
    public void testSetBottomNavigationText() {
        setUp("step1");
        View view = formFragment.onCreateView(LayoutInflater.from(context), null, null);
        formFragment.setBottomNavigationText("Initializing Test");
        assertEquals("Initializing Test", ((TextView) view.findViewById(R.id.information_textView)).getText());
        assertEquals(View.VISIBLE, view.findViewById(R.id.information_textView).getVisibility());
    }

}
