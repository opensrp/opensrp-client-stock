package org.smartregister.stock.openlmis.widget;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import org.joda.time.LocalDate;
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
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.smartregister.stock.openlmis.TestData.ISSUE_FORM_JSON;
import static org.smartregister.stock.openlmis.TestData.LOT_WIDGET_JSON;
import static org.smartregister.stock.openlmis.TestData.LOT_WIDGET_WITH_DATA_JSON;
import static org.smartregister.stock.openlmis.widget.LotFactory.IS_STOCK_ISSUE;

/**
 * Created by samuelgithengi on 9/3/18.
 */
public class LotFactoryTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private OpenLMISJsonFormFragment formFragment;

    @Mock
    private LotRepository lotRepository;

    private OpenLMISJsonForm activity;

    private LotFactory lotFactory;

    private List<Lot> lots;

    @Before
    public void setUp() {
        lots = new ArrayList<>();
        lotFactory = new LotFactory(lotRepository);
        Intent intent = new Intent();
        intent.putExtra("json", ISSUE_FORM_JSON);
        activity = Robolectric.buildActivity(OpenLMISJsonForm.class, intent).create().get();
        activity.clearFormDataViews();
        activity.clearConstrainedViews();

        TradeItem tradeItem = new TradeItem(UUID.fromString("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6"));
        Lot lot = new Lot(UUID.fromString("7c6d239f-0bbc-4cab-b218-888d8be89d24"),
                "LC1265", new LocalDate("2018-09-21"), new LocalDate(),
                tradeItem, true);

        lots.add(lot);
        Lot lot2 = new Lot(UUID.fromString("9da34cac-4753-4763-a749-10741cdcce33"),
                "LC8063", new LocalDate("2018-10-06"), new LocalDate(),
                tradeItem, true);
        lots.add(lot2);
        when(lotRepository.findLotsByTradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6")).thenReturn(lots);

    }

    @Test
    public void testGetViewsFromJson() throws Exception {

        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertEquals(1, viewsFromJson.size());
        assertEquals(2, lotFactory.lotsContainer.getChildCount());

    }

    @Test
    public void testGetViewsFromJsonWithExistingData() throws Exception {
        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_WITH_DATA_JSON), formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals(3, lotFactory.lotsContainer.getChildCount());

    }

    @Test
    public void testGetViewsFromJsonIssueWithExistingData() throws Exception {
        when(lotRepository.findLotsByTradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6", true)).thenReturn(lots);
        Map<String, Integer> lotStockBalances = new HashMap<>();
        lotStockBalances.put(lots.get(0).getId().toString(), 10);
        lotStockBalances.put(lots.get(1).getId().toString(), 10);
        when(lotRepository.getStockByLot("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6")).thenReturn(lotStockBalances);
        JSONObject lotJson = new JSONObject(LOT_WIDGET_WITH_DATA_JSON);
        lotJson.put(IS_STOCK_ISSUE, true);
        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, lotJson, formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals(3, lotFactory.lotsContainer.getChildCount());

    }


    @Test
    public void testRemoveLotRow() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_WITH_DATA_JSON), formFragment);
        assertEquals(3, lotFactory.lotsContainer.getChildCount());
        View secondRow = lotFactory.lotsContainer.getChildAt(1);
        secondRow.findViewById(R.id.cancel_button).performClick();
        assertEquals(2, lotFactory.lotsContainer.getChildCount());
    }

    @Test
    public void testAddLotRow() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertEquals(2, lotFactory.lotsContainer.getChildCount());
        View lotRow = lotFactory.lotsContainer.getChildAt(1);
        lotRow.findViewById(R.id.add_lot).performClick();
        assertEquals(3, lotFactory.lotsContainer.getChildCount());
    }

    @Test
    public void testValidateHasValues() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        ((EditText) lotRow.findViewById(R.id.quantity_textview)).setText(String.valueOf(10));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");

        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

    }

    @Test
    public void testValidateZeroQuantity() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        EditText quantity = lotRow.findViewById(R.id.quantity_textview);
        quantity.setText(String.valueOf(0));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");

        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

        quantity.setText(String.valueOf(1));
        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

    }


    @Test
    public void testValidateNegativeStockBalance() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");
        EditText lotEditText = lotRow.findViewById(R.id.lot_dropdown);
        EditText quantityEditText = lotRow.findViewById(R.id.quantity_textview);
        lotEditText.setText("LC1265 Exp. 21-09-2018");
        quantityEditText.setText(String.valueOf(15));
        lotEditText.setTag(R.id.is_stock_issue, true);
        quantityEditText.setTag(R.id.stock_balance, 12);

        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());

        quantityEditText.setText(String.valueOf(10));

        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer).isValid());


    }


}
