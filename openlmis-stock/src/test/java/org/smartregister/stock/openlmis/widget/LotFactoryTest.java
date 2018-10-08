package org.smartregister.stock.openlmis.widget;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import org.joda.time.LocalDate;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.activity.OpenLMISJsonForm;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.domain.openlmis.StockCardLineItemReason;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.shadow.ShadowPopupMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartregister.stock.openlmis.TestData.ADJUST_WIDGET_JSON;
import static org.smartregister.stock.openlmis.TestData.ISSUE_FORM_JSON;
import static org.smartregister.stock.openlmis.TestData.LOT_WIDGET_JSON;
import static org.smartregister.stock.openlmis.TestData.LOT_WIDGET_ONE_EXISTING_JSON;
import static org.smartregister.stock.openlmis.TestData.LOT_WIDGET_TWO_EXISTING_JSON;
import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;
import static org.smartregister.stock.openlmis.widget.LotFactory.IS_STOCK_ISSUE;

/**
 * Created by samuelgithengi on 9/3/18.
 */
@Config(shadows = {ShadowPopupMenu.class})
public class LotFactoryTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private OpenLMISJsonFormFragment formFragment;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ReasonRepository reasonRepository;

    private OpenLMISJsonForm activity;

    private LotFactory lotFactory;

    private List<Lot> lots;

    @Before
    public void setUp() {
        lots = new ArrayList<>();
        lotFactory = new LotFactory(lotRepository, reasonRepository, stockRepository);

        Intent intent = new Intent();
        intent.putExtra("json", ISSUE_FORM_JSON);
        activity = Robolectric.buildActivity(OpenLMISJsonForm.class, intent).create().get();
        activity.clearFormDataViews();
        activity.clearConstrainedViews();

        TradeItem tradeItem = new TradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6");
        Lot lot = new Lot("7c6d239f-0bbc-4cab-b218-888d8be89d24",
                "LC1265", 9889l, 293982l,
                tradeItem.getId(), true);

        lots.add(lot);
        Lot lot2 = new Lot("9da34cac-4753-4763-a749-10741cdcce33",
                "LC8063", 89239490024l, 8323492l,
                tradeItem.getId(), true);
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
        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_TWO_EXISTING_JSON), formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals(3, lotFactory.lotsContainer.getChildCount());

    }

    @Test
    public void testGetViewsFromJsonIssueWithTwoLots() throws Exception {
        when(lotRepository.findLotsByTradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6", true)).thenReturn(lots);
        Map<String, Integer> lotStockBalances = new HashMap<>();
        lotStockBalances.put(lots.get(0).getId().toString(), 10);
        lotStockBalances.put(lots.get(1).getId().toString(), 10);
        when(lotRepository.getStockByLot("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6")).thenReturn(lotStockBalances);
        JSONObject lotJson = new JSONObject(LOT_WIDGET_TWO_EXISTING_JSON);
        lotJson.put(IS_STOCK_ISSUE, true);
        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, lotJson, formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals(3, lotFactory.lotsContainer.getChildCount());

    }


    @Test
    public void testGetViewsFromJsonIssueWithSingleLot() throws Exception {
        when(lotRepository.findLotsByTradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6", true)).thenReturn(lots);
        Map<String, Integer> lotStockBalances = new HashMap<>();
        lotStockBalances.put(lots.get(0).getId().toString(), 10);
        when(lotRepository.getStockByLot("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6")).thenReturn(lotStockBalances);
        JSONObject lotJson = new JSONObject(LOT_WIDGET_ONE_EXISTING_JSON);
        lotJson.put(IS_STOCK_ISSUE, true);
        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, lotJson, formFragment);
        assertEquals(1, viewsFromJson.size());

        assertEquals(2, lotFactory.lotsContainer.getChildCount());

    }


    @Test
    public void testGetViewsFromJsonLotAdjustment() throws Exception {

        List<View> viewsFromJson = lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(ADJUST_WIDGET_JSON), formFragment);
        assertEquals(1, viewsFromJson.size());
        assertEquals(2, lotFactory.lotsContainer.getChildCount());

    }

    @Test
    public void testRemoveLotRow() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_TWO_EXISTING_JSON), formFragment);
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
        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        ((EditText) lotRow.findViewById(R.id.quantity_textview)).setText(String.valueOf(10));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");

        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

    }

    @Test
    public void testValidateZeroQuantity() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        EditText quantity = lotRow.findViewById(R.id.quantity_textview);
        quantity.setText(String.valueOf(0));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");

        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        quantity.setText(String.valueOf(1));
        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

    }


    @Test
    public void testValidateNegativeStockBalance() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");
        EditText lotEditText = lotRow.findViewById(R.id.lot_dropdown);
        EditText quantityEditText = lotRow.findViewById(R.id.quantity_textview);
        lotEditText.setText("LC1265 Exp. 21-09-2018");
        quantityEditText.setText(String.valueOf(15));
        lotEditText.setTag(R.id.is_stock_issue, true);
        quantityEditText.setTag(R.id.stock_balance, 12);

        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        quantityEditText.setText(String.valueOf(10));

        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());


    }


    @Test
    public void testAddLotAdjustmentRow() throws Exception {
        lotFactory.getViewsFromJson("step1", activity, formFragment, new JSONObject(ADJUST_WIDGET_JSON), formFragment);
        assertEquals(2, lotFactory.lotsContainer.getChildCount());
        View lotRow = lotFactory.lotsContainer.getChildAt(1);
        lotRow.findViewById(R.id.add_lot).performClick();
        assertEquals(3, lotFactory.lotsContainer.getChildCount());
    }


    @Test
    public void testPopulateStatusOptions() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_ONE_EXISTING_JSON), formFragment);
        assertEquals("VVM1", lotFactory.getSelectedLotDTos().get(0).getLotStatus());

        TextInputEditText status = new TextInputEditText(activity);
        status.setTag(R.id.lot_id, "7c6d239f-0bbc-4cab-b218-888d8be89d24");
        PopupMenu popup = lotFactory.populateStatusOptions(activity, status,true);
        status.performClick();

        MenuItem item = popup.getMenu().getItem(1);
        org.robolectric.shadows.ShadowPopupMenu shadowPopupMenu = Shadows.shadowOf(popup);
        shadowPopupMenu.getOnMenuItemClickListener().onMenuItemClick(item);

        verify(formFragment, times(2)).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());
        assertEquals("VVM2", status.getText().toString());
        assertEquals("VVM2", lotFactory.getSelectedLotDTos().get(0).getLotStatus());
    }

    @Test
    public void testPopulateLotOptions() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_JSON), formFragment);
        assertTrue(lotFactory.getSelectedLotDTos().isEmpty());

        TextInputEditText lotDropdown = new TextInputEditText(activity);
        lotDropdown.setTag(R.id.lot_position, 0);
        PopupMenu popup = lotFactory.populateLotOptions(activity, lotDropdown);
        lotDropdown.performClick();

        MenuItem item = popup.getMenu().getItem(0);
        org.robolectric.shadows.ShadowPopupMenu shadowPopupMenu = Shadows.shadowOf(popup);
        shadowPopupMenu.getOnMenuItemClickListener().onMenuItemClick(item);

        verify(formFragment).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());
        assertEquals(1, lotFactory.getSelectedLotDTos().size());
        assertEquals("LC1265 Exp. " + new LocalDate(9889l).toString(DATE_FORMAT), lotDropdown.getText().toString());
        assertEquals("7c6d239f-0bbc-4cab-b218-888d8be89d24", lotFactory.getSelectedLotDTos().get(0).getLotId());
        assertEquals("7c6d239f-0bbc-4cab-b218-888d8be89d24", lotDropdown.getTag(R.id.lot_id));
    }


    private void selectLot() throws Exception {
        when(lotRepository.findLotsByTradeItem("0cf8b2f4-3e1b-4d35-b839-5a4268ef03d6", false)).thenReturn(lots);
        lotFactory.getViewsFromJson("step1", activity, formFragment, new JSONObject(ADJUST_WIDGET_JSON), formFragment);
        TextInputEditText lotDropdown = new TextInputEditText(activity);
        lotDropdown.setTag(R.id.lot_position, 0);
        PopupMenu popup = lotFactory.populateLotOptions(activity, lotDropdown);
        lotDropdown.performClick();

        MenuItem item = popup.getMenu().getItem(0);
        org.robolectric.shadows.ShadowPopupMenu shadowPopupMenu = Shadows.shadowOf(popup);
        shadowPopupMenu.getOnMenuItemClickListener().onMenuItemClick(item);
    }

    @Test
    public void testPopulateReasonsOptions() throws Exception {
        selectLot();

        TextInputEditText reason = new TextInputEditText(activity);
        reason.setTag(R.id.lot_id, "7c6d239f-0bbc-4cab-b218-888d8be89d24");


        List<Reason> reasons = new ArrayList<>();
        reasons.add(new Reason("id_1", "program_1", "type_1",
                new StockCardLineItemReason("id_1", "Damage", null, "DEBIT",
                        "ADJUSTMENT", null)));
        when(reasonRepository.findReasons(null, null,"", null)).thenReturn(reasons);
        PopupMenu popup = lotFactory.populateReasonsOptions(activity, reason,true);

        reason.performClick();


        MenuItem item = popup.getMenu().getItem(0);
        org.robolectric.shadows.ShadowPopupMenu shadowPopupMenu = Shadows.shadowOf(popup);
        shadowPopupMenu.getOnMenuItemClickListener().onMenuItemClick(item);

        assertEquals("Damage", reason.getText().toString());
        assertEquals("Damage", lotFactory.getSelectedLotDTos().get(0).getReason());

        verify(formFragment, times(2)).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());


    }

    @Test
    public void testAdjustStockAdd() throws Exception {
        selectLot();

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        lotRow.findViewById(R.id.add_stock).performClick();
        assertEquals("1", ((TextInputEditText) lotRow.findViewById(R.id.quantity_textview)).getText().toString());
        assertEquals("+1", ((TextInputEditText) lotRow.findViewById(R.id.adjustment_textview)).getText().toString());

    }

    @Test
    public void testAdjustStockSubtract() throws Exception {
        selectLot();
        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        lotRow.findViewById(R.id.subtract_stock).performClick();
        assertEquals("-1", ((TextInputEditText) lotRow.findViewById(R.id.quantity_textview)).getText().toString());
        assertEquals("-1", ((TextInputEditText) lotRow.findViewById(R.id.adjustment_textview)).getText().toString());

    }

    @Test
    public void testValidateAdjustmentReason() throws Exception {
        selectLot();
        lotFactory.getViewsFromJson("step1", activity, formFragment, new JSONObject(ADJUST_WIDGET_JSON), formFragment);

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        ((EditText) lotRow.findViewById(R.id.quantity_textview)).setText(String.valueOf(10));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");

        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        ((EditText) lotRow.findViewById(R.id.reason_dropdown)).setText("Damaged");

        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

    }

    @Test
    public void testValidateAdjustmentNegative() throws Exception {
        selectLot();
        lotFactory.getViewsFromJson("step1", activity, formFragment, new JSONObject(ADJUST_WIDGET_JSON), formFragment);

        View lotRow = lotFactory.lotsContainer.getChildAt(0);
        ((EditText) lotRow.findViewById(R.id.lot_dropdown)).setText("LC1265 Exp. 21-09-2018");
        ((EditText) lotRow.findViewById(R.id.quantity_textview)).setText(String.valueOf(10));
        ((EditText) lotRow.findViewById(R.id.status_dropdown)).setText("VVM1");
        ((EditText) lotRow.findViewById(R.id.reason_dropdown)).setText("Damaged");
        assertTrue(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

        ((EditText) lotRow.findViewById(R.id.quantity_textview)).setText("-1");
        assertFalse(LotFactory.validate(formFragment, lotFactory.lotsContainer, true).isValid());

    }

    @Test
    public void testSelectExistingLot() throws Exception {
        lotFactory.getViewsFromJson("step2", activity, formFragment, new JSONObject(LOT_WIDGET_ONE_EXISTING_JSON), formFragment);
        assertEquals(1, lotFactory.getSelectedLotDTos().size());
        View lotRow = lotFactory.lotsContainer.getChildAt(0);

        TextInputEditText lotDropdown = lotRow.findViewById(R.id.lot_dropdown);
        PopupMenu popup = lotFactory.populateLotOptions(activity, lotDropdown);
        lotDropdown.performClick();

        MenuItem item = popup.getMenu().getItem(0);
        org.robolectric.shadows.ShadowPopupMenu shadowPopupMenu = Shadows.shadowOf(popup);
        shadowPopupMenu.getOnMenuItemClickListener().onMenuItemClick(item);

        verify(formFragment, times(2)).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());
        assertEquals(1, lotFactory.getSelectedLotDTos().size());
        assertEquals("LC1265 Exp. " + new LocalDate(9889l).toString(DATE_FORMAT), lotDropdown.getText().toString());
        assertEquals("7c6d239f-0bbc-4cab-b218-888d8be89d24", lotFactory.getSelectedLotDTos().get(0).getLotId());
        assertEquals("7c6d239f-0bbc-4cab-b218-888d8be89d24", lotDropdown.getTag(R.id.lot_id));

    }

}
