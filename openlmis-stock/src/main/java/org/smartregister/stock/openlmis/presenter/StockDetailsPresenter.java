package org.smartregister.stock.openlmis.presenter;

import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.JsonForm.IS_NON_LOT;
import static org.smartregister.stock.openlmis.widget.LotFactory.TRADE_ITEM_ID;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.OTHER;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.STEP2;
import static org.smartregister.stock.openlmis.widget.ReviewFactory.STOCK_LOTS;
import static org.smartregister.util.JsonFormUtils.FIELDS;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.STEP1;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import static org.smartregister.util.JsonFormUtils.gson;

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

    public void processFormJsonResult(String jsonString, String provider) {
        try {
            totalStockAdjustment = 0;
            JSONObject jsonForm = new JSONObject(jsonString);
            JSONObject step = jsonForm.getJSONObject(STEP1);
            String FormTitle = step.getString("title");
            boolean processed = false;
            if (FormTitle.contains("Issue")) {
                processed = processStockIssued(jsonForm, provider);
            } else if (FormTitle.contains("Receive")) {
                processed = processStockReceived(jsonForm, provider);
            } else if (FormTitle.contains("adjustment")) {
                processed = processStockAdjusted(jsonForm, provider);
            }
            if (processed)
                stockDetailsView.refreshStockDetails(totalStockAdjustment);
        } catch (JSONException e) {
            Log.e(TAG, "error processing Stock form", e);
        }

    }

    private boolean processStockIssued(JSONObject jsonString, String provider) throws JSONException {
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
            return processStockNonLot(STEP1, jsonString, provider, date, facility, issued, reason, quantity, status);
        }
        return processStockLot(STEP2, jsonString, provider, date, facility, reason, issued);
    }

    private boolean processStockReceived(JSONObject jsonString, String provider) throws JSONException {
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
            return processStockNonLot(STEP1, jsonString, provider, date, facility, received, reason, quantity, status);
        }
        return processStockLot(STEP2, jsonString, provider, date, facility, reason, received);
    }

    private boolean processStockAdjusted(JSONObject jsonString, String provider) throws JSONException {
        JSONArray stepFields = JsonFormUtils.fields(jsonString);
        boolean isNonLot = stepFields.getJSONObject(0).optBoolean(IS_NON_LOT);
        if (isNonLot) {
            // extract form values
            JSONObject values = new JSONObject(stepFields.getJSONObject(0).getString("value"));
            int quantity = values.getInt("value");
            String reason = values.getString("reason");
            String status = values.optString("vvmStatus", "VVM1");
            return processStockNonLot(STEP1, jsonString, provider, simpleDateFormat.format(new Date()),
                    null, loss_adjustment, reason, quantity, status);
        }
        return processStockLot(STEP1, jsonString, provider, simpleDateFormat.format(new Date()),
                null, null, loss_adjustment);
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

    private boolean processStockLot(String step, JSONObject jsonString, String provider, String date,
                                    String facility, String reason, String transactionType) throws JSONException {
        JSONArray stepFields = jsonString.getJSONObject(step).getJSONArray(FIELDS);

        String lotsJSON = JsonFormUtils.getFieldValue(stepFields, STOCK_LOTS);

        Type listType = new TypeToken<List<LotDto>>() {}.getType();

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
                    provider, transactionType.equals(issued) ? -lot.getQuantity() : lot.getQuantity(),
                    encounterDate.getTime(), facility == null ? lot.getReasonId() : facility, BaseRepository.TYPE_Unsynced,
                    System.currentTimeMillis(), tradeItem);
            stock.setLotId(lot.getLotId());
            stock.setReason(reason);
            stock.setProgramId(programId);
            stock.setvvmStatus(lot.getLotStatus());

            totalStockAdjustment += stock.getValue();
            stockDetailsInteractor.addStock(stock);
            if (transactionType.equals(received))
                stockDetailsInteractor.updateLotStatus(lot.getLotId(), lot.getLotStatus());
        }
        return true;
    }

    private boolean processStockNonLot(String step, JSONObject jsonString, String provider, String date,
                                    String facility, String transactionType, String reason, int quantity, String status) throws JSONException {

        JSONArray stepFields = jsonString.getJSONObject(step).getJSONArray(FIELDS);

        String stockJSON = JsonFormUtils.getFieldValue(stepFields, STOCK);

        Type stockType = new TypeToken<Stock>() {}.getType();

        String tradeItem = extractValue(stepFields, TRADE_ITEM_ID);
        String programId = extractValue(stepFields, PROGRAM_ID);
        Date encounterDate;
        try {
            encounterDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "error passing stock issue/received date", e);
            encounterDate = new Date();
        }


        Stock stock;
        if (loss_adjustment.equals(transactionType)) {
            stock = gson.fromJson(stockJSON, stockType);
            stock.setTransactionType(transactionType);
            stock.setProviderid(provider);
            stock.setDateCreated(encounterDate.getTime());
            stock.setLocationId(facility);
            stock.setSyncStatus(BaseRepository.TYPE_Unsynced);
            stock.setStockTypeId(tradeItem);
            stock.setDateUpdated(System.currentTimeMillis());
            stock.setValue(quantity);
        } else {
            stock = new Stock(null, transactionType,
                    provider, transactionType.equals(issued) ? -quantity : quantity,
                    encounterDate.getTime(), facility, BaseRepository.TYPE_Unsynced,
                    System.currentTimeMillis(), tradeItem);
        }
        stock.setProgramId(programId);
        stock.setReason(reason);;
        stock.setvvmStatus(status);
        stock.setToFrom("facility_name"); // TODO: GET THIS FROM DEVICE LOG IN LOCATION

        totalStockAdjustment += stock.getValue();
        stockDetailsInteractor.addStock(stock);

        return true;
    }


}

