package org.smartregister.stock.openlmis.widget;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.activity.OpenLMISJsonForm;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.smartregister.stock.openlmis.TestData.ISSUE_FORM_JSON;
import static org.smartregister.stock.openlmis.TestData.RECEIVE_JSON_FORM_DATA;
import static org.smartregister.stock.openlmis.TestData.REVIEW_WIDGET_JSON;

/**
 * Created by samuelgithengi on 9/3/18.
 */
public class ReviewFactoryTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private OpenLMISJsonFormFragment formFragment;

    private OpenLMISJsonForm activity;

    private ReviewFactory reviewFactory;

    @Before
    public void setUp() {
        reviewFactory = new ReviewFactory();
        Intent intent = new Intent();
        intent.putExtra("json", ISSUE_FORM_JSON);
        activity = Robolectric.buildActivity(OpenLMISJsonForm.class, intent).create().get();
        activity.clearFormDataViews();
        activity.clearConstrainedViews();
        when(formFragment.getCurrentJsonState()).thenReturn(RECEIVE_JSON_FORM_DATA);

    }

    @Test
    public void testGetViewsFromJson() throws Exception {
        List<View> viewsFromJson = reviewFactory.getViewsFromJson("step3", activity, formFragment, new JSONObject(REVIEW_WIDGET_JSON), formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals("03-09-2018", ((TextView) viewsFromJson.get(0).findViewById(R.id.date)).getText());
        assertEquals("Balaka District Warehouse", ((TextView) viewsFromJson.get(0).findViewById(R.id.facility)).getText());
        assertEquals("Receipts", ((TextView) viewsFromJson.get(0).findViewById(R.id.reason)).getText());

        assertEquals(2, ((RecyclerView) viewsFromJson.get(0).findViewById(R.id.review_recyclerView)).getAdapter().getItemCount());

    }
}
