package org.smartregister.stock.openlmis.presenter;

import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.stock.openlmis.domain.Stock;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.dto.TradeItemDto;
import org.smartregister.stock.openlmis.interactor.StockDetailsInteractor;
import org.smartregister.stock.openlmis.view.contract.StockDetailsView;
import org.smartregister.stock.openlmis.widget.LotFactory;
import org.smartregister.stock.openlmis.widget.helper.LotDto;
import org.smartregister.stock.openlmis.wrapper.StockWrapper;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.smartregister.stock.domain.Stock.issued;
import static org.smartregister.stock.domain.Stock.loss_adjustment;
import static org.smartregister.stock.domain.Stock.received;
import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;
import static org.smartregister.stock.openlmis.repository.StockRepository.PROGRAM_ID;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.IS_NON_LOT;
import static org.smartregister.stock.openlmis.widget.LotFactory.TRADE_ITEM_ID;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.OTHER;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.STEP2;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.STOCK_LOTS;
import static org.smartregister.util.JsonFormUtils.FIELDS;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.STEP1;
import static org.smartregister.util.JsonFormUtils.getJSONObject;

public class StockDetailsPresenter {

    private static final String TAG = StockDetailsPresenter.class.getName();

    private StockDetailsInteractor stockDetailsInteractor;

    private StockDetailsView stockDetailsView;

    private int totalStockAdjustment;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private static final String STOCK = "stock";

    private String programId;

    public StockDetailsPresenter(StockDetailsView stockDetailsView, String programId) {
        this.stockDetailsView = stockDetailsView;
        this.programId = programId;
        stockDetailsInteractor = new StockDetailsInteractor();

    }

    @VisibleForTesting
    public StockDetailsPresenter(StockDetailsView stockDetailsView, StockDetailsInteractor stockDetailsInteractor) {
        this.stockDetailsView = stockDetailsView;
        this.stockDetailsInteractor = stockDetailsInteractor;

    }

    public int getTotalStockByLot(String lotId) {
        return stockDetailsInteractor.getTotalStockByLot(lotId);
    }

    public List<Lot> findLotsByTradeItem(String tradeItemId) {
        List<Lot> lots = stockDetailsInteractor.findLotsByTradeItem(tradeItemId);
        if (!lots.isEmpty())
            stockDetailsView.showLotsHeader();
        return lots;
    }

    public TradeItem findTradeItem(String tradeItemId) {
        return stockDetailsInteractor.findTradeItem(tradeItemId);
    }

    public List<Stock> findStockByTradeItem(String tradeItemId) {
        List<Stock> stockList = stockDetailsInteractor.getStockByTradeItem(tradeItemId);

        if (!stockList.isEmpty())
            stockDetailsView.showTransactionsHeader();
        return stockList;
    }

    public List<StockWrapper> populateLotNamesAndBalance(TradeItemDto tradeItem, List<Stock> stockTransactions) {
        List<StockWrapper> stockWrapperList = new ArrayList<>();
        Map<String, String> lotName = stockDetailsInteractor.findLotNames(tradeItem.getId());
        int stockCounter = 0;

        Map<String, String> reasonNames = stockDetailsInteractor.findReasonNames(programId);
        Map<String, String> facilityNames = stockDetailsInteractor.findFacilityNames(programId);

        for (Stock stock : stockTransactions) {
            StockWrapper stockWrapper = new StockWrapper(stock, lotName.get(stock.getLotId()),
                    tradeItem.getTotalStock() - stockCounter);
            if (facilityNames.containsKey(stock.getToFrom()))
                stockWrapper.setFacility(facilityNames.get(stock.getToFrom()));
            if (reasonNames.containsKey(stock.getReason()))
                stockWrapper.setReason(reasonNames.get(stock.getReason()));
            stockWrapperList.add(stockWrapper);
            stockCounter += stock.getValue();

        }
        return stockWrapperList;
    }

    public void collapseExpandClicked(int visibility) {
        if (visibility == View.GONE) {
            stockDetailsView.expandLots();
        } else if (visibility == View.VISIBLE) {
            stockDetailsView.collapseLots();
        }
    }

    public void processFormJsonResult(String jsonString, String userFacilityId,
                                      AllSharedPreferences sharedPreferences) {
        try {
            totalStockAdjustment = 0;
            JSONObject jsonForm = new JSONObject(jsonString);
            JSONObject step = jsonForm.getJSONObject(STEP1);
            String FormTitle = step.getString("title");
            boolean processed = false;
            if (FormTitle.contains("Issue")) {
                processed = processStockIssued(jsonForm, userFacilityId, sharedPreferences);
            } else if (FormTitle.contains("Receive")) {
                processed = processStockReceived(jsonForm, userFacilityId, sharedPreferences);
            } else if (FormTitle.contains("adjustment")) {
                processed = processStockAdjusted(jsonForm, userFacilityId, sharedPreferences);
            }
            if (processed)
                stockDetailsView.refreshStockDetails(totalStockAdjustment);
        } catch (JSONException e) {
            Log.e(TAG, "error processing Stock form", e);
        }

    }

    private boolean processStockIssued(JSONObject jsonString, String userFacilityId,
                                       AllSharedPreferences sharedPreferences) throws JSONException {
        JSONArray stepFields = JsonFormUtils.fields(jsonString);

        String date = JsonFormUtils.getFieldValue(stepFields, "Date_Stock_Issued");
        String facility = JsonFormUtils.getFieldValue(stepFields, "Issued_Stock_To");
        String reason = JsonFormUtils.getFieldValue(stepFields, "Issued_Stock_Reason");
        if (reason.equalsIgnoreCase(OTHER)) {
            reason = JsonFormUtils.getFieldValue(stepFields, "Issued_Stock_Reason_Other");
        }

        int steps = Integer.parseInt((String) jsonString.get("count"));
        if (steps == 1) {
            String status = JsonFormUtils.getFieldValue(stepFields, "Status");
            int quantity = Integer.parseInt(JsonFormUtils.getFieldValue(stepFields, "Vials_Issued"));
            return processStockNonLot(STEP1, jsonString, date, facility, issued, reason,
                    quantity, status, userFacilityId, sharedPreferences);
        }
        return processStockLot(STEP2, jsonString, date, facility, issued, reason, userFacilityId,
                sharedPreferences);
    }

    private boolean processStockReceived(JSONObject jsonString, String userFacilityId,
                                         AllSharedPreferences sharedPreferences) throws JSONException {
        JSONArray stepFields = JsonFormUtils.fields(jsonString);

        String date = JsonFormUtils.getFieldValue(stepFields, "Date_Stock_Received");
        String facility = JsonFormUtils.getFieldValue(stepFields, "Receive_Stock_From");
        String reason = JsonFormUtils.getFieldValue(stepFields, "Receive_Stock_Reason");

        if (reason.equalsIgnoreCase(OTHER)) {
            reason = JsonFormUtils.getFieldValue(stepFields, "Receive_Stock_Reason_Other");
        }

        int steps = Integer.parseInt((String) jsonString.get("count"));
        if (steps == 1) {
            String status = JsonFormUtils.getFieldValue(stepFields, "Status");
            int quantity = Integer.parseInt(JsonFormUtils.getFieldValue(stepFields, "Vials_Received"));
            return processStockNonLot(STEP1, jsonString, date, facility, received, reason, quantity, status, userFacilityId, sharedPreferences);
        }
        return processStockLot(STEP2, jsonString, date, facility, received, reason, userFacilityId, sharedPreferences);
    }

    private boolean processStockAdjusted(JSONObject jsonString, String userFacilityId,
                                         AllSharedPreferences sharedPreferences) throws JSONException {
        JSONArray stepFields = JsonFormUtils.fields(jsonString);

        boolean isNonLot = stepFields.getJSONObject(0).optBoolean(IS_NON_LOT);
        if (isNonLot) {
            String date = JsonFormUtils.getFieldValue(stepFields, "Date_Stock_Adjusted");
            String reason = JsonFormUtils.getFieldValue(stepFields, "Adjusted_Stock_Reason");
            int quantity = Integer.parseInt(JsonFormUtils.getFieldValue(stepFields, "Adjusted_Quantity"));
            String status = JsonFormUtils.getFieldValue(stepFields, "Status");
            if (DEBIT.equals(stockDetailsInteractor.findReasonById(reason).getStockCardLineItemReason().getReasonType()))
                quantity = -quantity;
            return processStockNonLot(STEP1, jsonString, date,
                    null, loss_adjustment, reason, quantity, status, userFacilityId, sharedPreferences);
        }
        return processStockLot(STEP1, jsonString, simpleDateFormat.format(new Date()),
                null, loss_adjustment, null, userFacilityId, sharedPreferences);
    }

    private String extractValue(JSONArray stepFields, String key) throws JSONException {
        for (int i = 0; i < stepFields.length(); i++) {
            JSONObject jsonObject = getJSONObject(stepFields, i);
            String keyValue = jsonObject.getString(KEY);
            if (STOCK_LOTS.equals(keyValue) || STOCK.equals(keyValue) || "Status".equals(keyValue)) {
                return jsonObject.optString(key);
            }
        }
        return null;
    }

    private boolean processStockLot(String step, JSONObject jsonString, String date,
                                    String facility, String transactionType, String reason, String facilityId,
                                    AllSharedPreferences sharedPreferences) throws JSONException {
        JSONArray stepFields = jsonString.getJSONObject(step).getJSONArray(FIELDS);

        String lotsJSON = JsonFormUtils.getFieldValue(stepFields, STOCK_LOTS);

        Type listType = new TypeToken<List<LotDto>>() {
        }.getType();

        List<LotDto> selectedLotDTos = LotFactory.gson.fromJson(lotsJSON, listType);

        String tradeItem = extractValue(stepFields, TRADE_ITEM_ID);
        String programId = extractValue(stepFields, PROGRAM_ID);

        Date encounterDate;
        try {
            encounterDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "error passing stock issue/received date", e);
            encounterDate = new Date();
        }

        for (LotDto lot : selectedLotDTos) {
            Stock stock = new Stock(null, transactionType,
                    sharedPreferences.fetchRegisteredANM(), transactionType.equals(issued) ? -lot.getQuantity() : lot.getQuantity(),
                    encounterDate.getTime(), facility, BaseRepository.TYPE_Unsynced,
                    System.currentTimeMillis(), tradeItem);
            stock.setLotId(lot.getLotId());
            stock.setReason(loss_adjustment.equals(transactionType) ? lot.getReasonId() : reason);
            stock.setProgramId(programId);
            stock.setVvmStatus(lot.getLotStatus());
            stock.setFacilityId(facilityId);
            stock.setOrderableId(stockDetailsInteractor.getOrderableId(tradeItem));

            stock.setLocationId(sharedPreferences.fetchDefaultLocalityId(sharedPreferences.fetchRegisteredANM()));
            stock.setTeam(sharedPreferences.fetchDefaultTeam(sharedPreferences.fetchRegisteredANM()));
            stock.setTeamId(sharedPreferences.fetchDefaultTeamId(sharedPreferences.fetchRegisteredANM()));

            if (loss_adjustment.equals(transactionType) && DEBIT.equals(lot.getReasonType()))
                stock.setValue(-lot.getQuantity());
            totalStockAdjustment += stock.getValue();
            stockDetailsInteractor.addStock(stock);
            if (transactionType.equals(received))
                stockDetailsInteractor.updateLotStatus(lot.getLotId(), lot.getLotStatus());
        }
        return true;
    }

    private boolean processStockNonLot(String step, JSONObject jsonString, String date,
                                       String facility, String transactionType, String reason,
                                       int quantity, String status, String facilityId,
                                       AllSharedPreferences sharedPreferences) throws JSONException {

        JSONArray stepFields = jsonString.getJSONObject(step).getJSONArray(FIELDS);

        String tradeItem = extractValue(stepFields, TRADE_ITEM_ID);
        String programId = extractValue(stepFields, PROGRAM_ID);
        Date encounterDate;
        try {
            encounterDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "error passing stock issue/received date", e);
            encounterDate = new Date();
        }

        Stock stock = new Stock(null, transactionType,
                sharedPreferences.fetchRegisteredANM(), transactionType.equals(issued) ? -quantity : quantity,
                encounterDate.getTime(), facility, BaseRepository.TYPE_Unsynced,
                System.currentTimeMillis(), tradeItem);
        stock.setToFrom(facility);
        stock.setProgramId(programId);
        stock.setReason(reason);
        stock.setVvmStatus(status);
        stock.setFacilityId(facilityId);
        stock.setOrderableId(stockDetailsInteractor.getOrderableId(tradeItem));


        stock.setLocationId(sharedPreferences.fetchDefaultLocalityId(sharedPreferences.fetchRegisteredANM()));
        stock.setTeam(sharedPreferences.fetchDefaultTeam(sharedPreferences.fetchRegisteredANM()));
        stock.setTeamId(sharedPreferences.fetchDefaultTeamId(sharedPreferences.fetchRegisteredANM()));

        totalStockAdjustment += stock.getValue();
        stockDetailsInteractor.addStock(stock);

        return true;
    }


}

