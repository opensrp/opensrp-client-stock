package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.views.JsonFormFragmentView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.repository.openlmis.LotRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonRepository;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.CREDIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.DEBIT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.EXPIRING_MONTHS_WARNING;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.LOT_WIDGET;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.PROGRAM_ID;

/**
 * Created by samuelgithengi on 8/23/18.
 */
public class LotFactory implements FormWidgetFactory {

    private final static String STATUS_FIELD_NAME = "lot_status";

    public final static String TRADE_ITEM = "trade_item";
    public final static String TRADE_ITEM_ID = "trade_item_id";
    public final static String NET_CONTENT = "net_content";
    public final static String DISPENSING_UNIT = "dispensing_unit";
    protected final static String IS_STOCK_ISSUE = "is_stock_issue";
    protected final static String IS_STOCK_ADJUSTMENT = "is_stock_adjustment";
    protected final static String USE_VMM = "use_vvm";

    public final static Gson gson = new GsonBuilder().create();

    protected LinearLayout lotsContainer;

    private Context context;

    private String stepName;

    private String key;

    private OpenLMISJsonFormFragment jsonFormFragment;

    private LotListener lotListener = new LotListener();

    private Map<String, Lot> lotMap;

    private Map<String, Lot> selectedLotsMap;

    private JSONArray statusOptions;

    private long netContent;

    private String dispensingUnit;

    private List<LotDto> selectedLotDTos;

    private boolean isStockIssue;

    private boolean isStockAdjustment;

    private boolean useVvm;

    private Map<String, Integer> lotStockBalances;

    private LotRepository lotRepository;

    private ReasonRepository reasonsRepository;

    private String programId;

    public LotFactory(LotRepository lotRepository, ReasonRepository reasonRepository) {
        this.lotRepository = lotRepository;
        this.reasonsRepository = reasonRepository;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, JsonFormFragment jsonFormFragment, JSONObject jsonObject, CommonListener commonListener) throws Exception {
        this.stepName = stepName;
        this.context = context;
        this.jsonFormFragment = (OpenLMISJsonFormFragment) jsonFormFragment;
        selectedLotDTos = new ArrayList<>();
        lotMap = new LinkedHashMap<>();
        selectedLotsMap = new LinkedHashMap<>();

        key = jsonObject.getString(KEY);
        List<View> views = new ArrayList<>(1);

        isStockAdjustment = jsonObject.optBoolean(IS_STOCK_ADJUSTMENT);
        View root = LayoutInflater.from(context).inflate(isStockAdjustment ?
                R.layout.openlmis_native_form_item_lot_adjustment : R.layout.openlmis_native_form_item_lot, null);

        if (!isStockAdjustment) {
            TextView tradeItem = root.findViewById(R.id.trade_item);
            tradeItem.setText(jsonObject.getString(TRADE_ITEM));
        }

        netContent = jsonObject.getLong(NET_CONTENT);
        dispensingUnit = jsonObject.getString(DISPENSING_UNIT);

        lotsContainer = root.findViewById(R.id.lots_Container);
        lotsContainer.setTag(com.vijay.jsonwizard.R.id.address, stepName + ":" + key);
        lotsContainer.setTag(com.vijay.jsonwizard.R.id.type, LOT_WIDGET);

        root.findViewById(R.id.add_lot).setOnClickListener(lotListener);

        String selectedLotDTosJSON = jsonObject.optString(VALUE);
        if (!selectedLotDTosJSON.isEmpty()) {
            Type listType = new TypeToken<List<LotDto>>() {
            }.getType();
            selectedLotDTos = gson.fromJson(selectedLotDTosJSON, listType);
        }


        isStockIssue = jsonObject.optBoolean(IS_STOCK_ISSUE);
        useVvm = jsonObject.optBoolean(USE_VMM);
        List<Lot> lots;
        String tradeItemId = jsonObject.getString(TRADE_ITEM_ID);
        programId = jsonObject.optString(PROGRAM_ID);
        if (isStockIssue || isStockAdjustment) {
            lots = lotRepository.findLotsByTradeItem(tradeItemId, isStockIssue);
            lotStockBalances = lotRepository.getStockByLot(tradeItemId);
        } else {
            lots = lotRepository.findLotsByTradeItem(tradeItemId);
        }
        for (Lot lot : lots) {
            if (selectedLotDTos.contains(new LotDto(lot.getId())))
                selectedLotsMap.put(lot.getId(), lot);
            else
                lotMap.put(lot.getId(), lot);
        }

        TextInputEditText lotDropdown = root.findViewById(R.id.lot_dropdown);
        lotDropdown.setTag(R.id.lot_position, 0);
        lotDropdown.setTag(R.id.is_stock_issue, isStockIssue);
        lotDropdown.setTag(R.id.is_stock_adjustment, isStockAdjustment);
        populateLotOptions(context, lotDropdown);

        if (useVvm) {
            TextInputEditText statusDropdown = root.findViewById(R.id.status_dropdown);
            statusOptions = jsonObject.getJSONArray(STATUS_FIELD_NAME);
            populateStatusOptions(context, statusDropdown);
        } else {
            hideStatusDropdown(root);
        }

        if (isStockAdjustment)
            populateReasonsOptions(context, (TextInputEditText) root.findViewById(R.id.reason_dropdown));
        views.add(root);

        ((JsonApi) context).addFormDataView(lotsContainer);

        restoreAdditionalLotRows(lotDropdown);

        return views;
    }

    private void restoreAdditionalLotRows(TextInputEditText lotDropdown) {
        if (selectedLotDTos.isEmpty()) {
            return;
        }
        showQuantityAndStatus(lotDropdown, selectedLotDTos.get(0).getLotId(), selectedLotDTos.get(0));
        if (selectedLotDTos.size() == 1)
            this.jsonFormFragment.validateActivateNext();
        else

            for (int i = 1; i < selectedLotDTos.size(); i++) {
                LotDto lotDto = selectedLotDTos.get(i);
                showQuantityAndStatus(addLotRow(), lotDto.getLotId(), lotDto);
            }
        this.jsonFormFragment.validateActivateNext();
    }


    private TextInputEditText addLotRow() {
        View lotView = LayoutInflater.from(context).inflate(isStockAdjustment ?
                R.layout.native_form_lot_adjustment_row : R.layout.native_form_lot_row, null);
        int viewIndex = lotsContainer.getChildCount() - 1;
        View cancelButton = lotView.findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setTag(R.id.lot_position, viewIndex);
        TextInputEditText lotDropdown = lotView.findViewById(R.id.lot_dropdown);
        lotDropdown.setTag(R.id.lot_position, viewIndex);
        lotDropdown.setTag(R.id.is_stock_issue, isStockIssue);
        lotDropdown.setTag(R.id.is_stock_adjustment, isStockAdjustment);
        populateLotOptions(context, lotDropdown);
        if (useVvm)
            populateStatusOptions(context, (TextInputEditText) lotView.findViewById(R.id.status_dropdown));
        else
            hideStatusDropdown(lotView);
        if (isStockAdjustment)
            populateReasonsOptions(context, (TextInputEditText) lotView.findViewById(R.id.reason_dropdown));
        cancelButton.setOnClickListener(lotListener);
        lotsContainer.addView(lotView, viewIndex);
        writeValues();
        return lotDropdown;
    }

    private void hideStatusDropdown(View lotRow) {
        lotRow.findViewById(R.id.lot_status).setVisibility(View.GONE);
        lotRow.findViewById(R.id.status_dropdown).setTag(R.id.use_vvm, true);

    }

    private void removeLotRow(View view) {
        View lotRow = lotsContainer.getChildAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
        Object lotId = lotRow.findViewById(R.id.lot_dropdown).getTag(R.id.lot_id);
        if (lotId != null) {
            lotMap.put(lotId.toString(), selectedLotsMap.remove(lotId.toString()));
            writeValues();
            if (selectedLotDTos.contains(new LotDto(lotId.toString())))
                selectedLotDTos.remove(selectedLotDTos.indexOf(new LotDto(lotId.toString())));
        }
        lotsContainer.removeView(lotRow);
        displayDosesQuantity();
        writeValues();
    }


    private void showQuantityAndStatus(View view, String lotId, @Nullable LotDto lotDto) {
        int viewIndex = Integer.parseInt(view.getTag(R.id.lot_position).toString());
        View lotRow = lotsContainer.getChildAt(viewIndex);
        lotRow.findViewById(R.id.lot_quantity).setVisibility(View.VISIBLE);
        if (useVvm) {
            lotRow.findViewById(R.id.lot_status).setVisibility(View.VISIBLE);
        }
        TextInputEditText quantity = lotRow.findViewById(R.id.quantity_textview);
        quantity.setTag(R.id.lot_id, lotId);
        quantity.setTag(R.id.lot_position, viewIndex);
        if (isStockIssue)
            quantity.setTag(R.id.stock_balance, lotStockBalances.get(lotId));
        else if (isStockAdjustment) {
            showAdjustmentAndReason(lotRow, quantity, lotId);
        }
        quantity.addTextChangedListener(new QuantityTextWatcher(quantity));
        TextInputEditText status = lotRow.findViewById(R.id.status_dropdown);
        status.setTag(R.id.lot_id, lotId);
        if (isStockAdjustment)
            lotRow.findViewById(R.id.reason_dropdown).setTag(R.id.lot_id, lotId);
        if (lotDto != null) {
            quantity.setText(String.valueOf(lotDto.getQuantity()));
            quantity.setTag(R.id.lot_id, lotDto.getLotId());
            status.setText(lotDto.getLotStatus());
            status.setTag(R.id.lot_id, lotDto.getLotId());

            TextInputEditText lot = lotRow.findViewById(R.id.lot_dropdown);
            lot.setText(context.getString(R.string.lotcode_and_expiry,
                    selectedLotsMap.get(lotId).getLotCode(), new LocalDate(selectedLotsMap.get(lotId).getExpirationDate()).toString(DATE_FORMAT)));
            lot.setTag(R.id.lot_id, lotDto.getLotId());

        }

    }

    @VisibleForTesting
    protected PopupMenu populateStatusOptions(final Context context, final TextInputEditText editText) {
        final PopupMenu popupMenu = new PopupMenu(context, editText);
        for (int i = 0; i < statusOptions.length(); i++) {
            popupMenu.getMenu().add(statusOptions.optString(i));
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setText(menuItem.getTitle());
                        String lotId = editText.getTag(R.id.lot_id).toString();
                        LotDto lotDto = selectedLotDTos.get(selectedLotDTos.indexOf(new LotDto(lotId)));
                        lotDto.setLotStatus(menuItem.getTitle().toString());
                        writeValues();
                        return true;
                    }
                });
            }

        });
        return popupMenu;
    }

    @VisibleForTesting
    protected PopupMenu populateLotOptions(final Context context, final TextInputEditText editText) {
        final PopupMenu popupMenu = new PopupMenu(context, editText);
        LocalDate expiringDateWarning = new LocalDate().plusMonths(EXPIRING_MONTHS_WARNING);
        for (Lot lot : lotMap.values()) {
            MenuItem menuitem = popupMenu.getMenu().add(context.getString(R.string.lotcode_and_expiry,
                    lot.getLotCode(), new LocalDate(lot.getExpirationDate()).toString(DATE_FORMAT)));
            View actionView = new View(context);
            actionView.setTag(R.id.lot_id, lot.getId());
            menuitem.setActionView(actionView);
            if (expiringDateWarning.isAfter(new LocalDate(lot.getExpirationDate()))) {
                SpannableString spanString = new SpannableString(menuitem.getTitle());
                spanString.setSpan(new ForegroundColorSpan(Color.RED), 0, spanString.length(), 0);
                menuitem.setTitle(spanString);
            }

        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Object previousLotId = editText.getTag(R.id.lot_id);
                        LotDto previousDto = new LotDto();
                        if (previousLotId != null) {
                            lotMap.put(previousLotId.toString(), selectedLotsMap.remove(previousLotId.toString()));
                            previousDto = selectedLotDTos.get(selectedLotDTos.indexOf(new LotDto(previousLotId.toString())));
                            selectedLotDTos.remove(previousDto);
                        }
                        String selectedLotId = menuItem.getActionView().getTag(R.id.lot_id).toString();
                        editText.setText(menuItem.getTitle().toString());
                        editText.setTag(R.id.lot_id, selectedLotId);
                        if (!selectedLotDTos.contains(new LotDto(selectedLotId))) {
                            selectedLotDTos.add(new LotDto(selectedLotId, previousDto.getQuantity(),
                                    previousDto.getLotStatus(), menuItem.getTitle().toString()));
                        }
                        showQuantityAndStatus(editText, selectedLotId, null);
                        selectedLotsMap.put(selectedLotId, lotMap.remove(selectedLotId));
                        writeValues();
                        displayDosesQuantity();
                        return true;
                    }
                });
            }
        });
        return popupMenu;
    }

    @VisibleForTesting
    protected PopupMenu populateReasonsOptions(final Context context, final TextInputEditText editText) {
        final PopupMenu popupMenu = new PopupMenu(context, editText);
        for (Reason reason : reasonsRepository.findReasons(null, null, programId, isStockAdjustment ? null : isStockIssue ? CREDIT : DEBIT)) {
            MenuItem menuItem = popupMenu.getMenu().add(reason.getStockCardLineItemReason().getName());
            View actionView = new View(context);
            actionView.setTag(R.id.reason_id, reason.getId());
            actionView.setTag(R.id.reason_type, reason.getStockCardLineItemReason().getReasonType());
            menuItem.setActionView(actionView);
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setText(menuItem.getTitle());
                        editText.setTag(R.id.reason_type, menuItem.getActionView().getTag(R.id.reason_type));
                        String lotId = editText.getTag(R.id.lot_id).toString();
                        LotDto lotDto = selectedLotDTos.get(selectedLotDTos.indexOf(new LotDto(lotId)));
                        lotDto.setReason(menuItem.getTitle().toString());
                        lotDto.setReasonId((String) menuItem.getActionView().getTag(R.id.reason_id));
                        lotDto.setReasonType((String) menuItem.getActionView().getTag(R.id.reason_type));
                        writeValues();
                        return true;
                    }
                });
            }
        });
        return popupMenu;
    }

    private void adjustStock(View view, boolean add) {
        View lotRow = lotsContainer.getChildAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
        TextInputEditText quantity = lotRow.findViewById(R.id.quantity_textview);
        int adjustment = add ? 1 : -1;
        int physicalCount = adjustment;
        if (StringUtils.isNotBlank(quantity.getText()))
            physicalCount = Integer.parseInt(quantity.getText().toString()) + adjustment;
        quantity.setText(String.valueOf(physicalCount));
        if (physicalCount < 0) {
            quantity.setError(context.getString(R.string.negative_balance));
            quantity.requestFocus();
        } else {
            quantity.setError(null);
            quantity.clearFocus();
        }

    }


    private void showAdjustmentAndReason(View lotRow, TextInputEditText quantity, String lotId) {
        quantity.setTag(R.id.stock_balance, lotStockBalances.get(lotId));

        View cancel = lotRow.findViewById(R.id.cancel_button);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cancel.getLayoutParams();
        params.topToTop = -1;

    }

    private void displayStockAdjustment(LotDto lotDto, TextInputEditText quantity) {
        int viewIndex = Integer.parseInt(quantity.getTag(R.id.lot_position).toString());
        View lotRow = lotsContainer.getChildAt(viewIndex);
        showAdjustmentAndReason(lotRow, quantity, lotDto.getLotId());
        lotRow.findViewById(R.id.reason).setVisibility(View.VISIBLE);
    }

    private void displayDosesQuantity() {
        int totalQuantity = 0;
        for (LotDto lot : selectedLotDTos) {
            if (isStockAdjustment && lot.getQuantity() != 0) {
                totalQuantity += 1;
            } else if (!isStockAdjustment)
                totalQuantity += lot.getQuantity();
        }
        if (isStockAdjustment)
            jsonFormFragment.setBottomNavigationText(context.getResources().getQuantityString(R.plurals.adjusted_lots,
                    totalQuantity, totalQuantity));
        else if (isStockIssue)
            jsonFormFragment.setBottomNavigationText(context.getString(R.string.issued_dose_formatter,
                    totalQuantity, dispensingUnit, totalQuantity * netContent));
        else
            jsonFormFragment.setBottomNavigationText(context.getString(R.string.received_dose_formatter,
                    totalQuantity, dispensingUnit, totalQuantity * netContent));
    }

    private void writeValues() {
        jsonFormFragment.writeValue(stepName, key, gson.toJson(selectedLotDTos), "", "", "");
    }


    private class LotListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.add_lot)
                addLotRow();
            else if (view.getId() == R.id.cancel_button)
                removeLotRow(view);
            else if (view.getId() == R.id.add_stock)
                adjustStock(view, true);
            else if (view.getId() == R.id.subtract_stock)
                adjustStock(view, false);

        }
    }


    private class QuantityTextWatcher implements TextWatcher {

        private TextInputEditText editText;

        private QuantityTextWatcher(TextInputEditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {//do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {//do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String lotId = editText.getTag(R.id.lot_id).toString();
            LotDto lotDto = selectedLotDTos.get(selectedLotDTos.indexOf(new LotDto(lotId)));
            if (editable.toString().isEmpty())
                lotDto.setQuantity(0);
            else {
                try {
                    int value = Integer.parseInt(editable.toString());
                    lotDto.setQuantity(value);
                    if (isStockAdjustment) {
                        displayStockAdjustment(lotDto, editText);
                    }
                } catch (NumberFormatException e) {
                    editText.setError(context.getString(R.string.quantity_to_large));
                }
            }
            writeValues();
            displayDosesQuantity();
        }
    }

    public static ValidationStatus validate(JsonFormFragmentView formFragmentView,
                                            LinearLayout lotsContainer) {
        boolean isValid = true;
        for (int i = 0; i < lotsContainer.getChildCount() - 1; i++) {
            TextInputEditText lot = lotsContainer.getChildAt(i).findViewById(R.id.lot_dropdown);
            TextInputEditText quantity = lotsContainer.getChildAt(i).findViewById(R.id.quantity_textview);
            TextInputEditText status = lotsContainer.getChildAt(i).findViewById(R.id.status_dropdown);
            boolean isStockAdjustment = lot.getTag(R.id.is_stock_adjustment) != null
                    && Boolean.valueOf(lot.getTag(R.id.is_stock_adjustment).toString());

            boolean isStockIssue = lot.getTag(R.id.is_stock_issue) != null && Boolean.valueOf(lot.getTag(R.id.is_stock_issue).toString());

            boolean useVVM = status.getTag(R.id.use_vvm) != null
                    && Boolean.valueOf(status.getTag(R.id.use_vvm).toString());

            int stockonHand = 0;
            if (quantity.getTag(R.id.stock_balance) != null)
                stockonHand = Integer.parseInt(quantity.getTag(R.id.stock_balance).toString());

            if (StringUtils.isBlank(lot.getText()) || StringUtils.isBlank(quantity.getText()) ||
                    (StringUtils.isBlank(status.getText()) && !useVVM))
                isValid = false;
            if (isStockIssue && StringUtils.isNotBlank(quantity.getText()) &&
                    Integer.parseInt(quantity.getText().toString()) > stockonHand) {
                quantity.setError(lotsContainer.getContext().getString(R.string.stock_issued_more_balance,
                        Integer.parseInt(quantity.getText().toString()),
                        Integer.parseInt(quantity.getTag(R.id.stock_balance).toString())));
                isValid = false;
            }
            if (StringUtils.isNotBlank(quantity.getText()) && !isStockAdjustment && "0".equals(quantity.getText().toString())) {
                isValid = false;
            }
            if (isStockAdjustment) {
                TextInputEditText reason = lotsContainer.getChildAt(i).findViewById(R.id.reason_dropdown);
                if (StringUtils.isBlank(reason.getText()))
                    isValid = false;
                else if (DEBIT.equals(reason.getTag(R.id.reason_type)) && StringUtils.isNotBlank(quantity.getText()) &&
                        Integer.parseInt(quantity.getText().toString()) > stockonHand) {
                    quantity.setError(lotsContainer.getContext().getString(R.string.stock_adjust_more_balance,
                            Integer.parseInt(quantity.getText().toString()),
                            stockonHand));
                    isValid = false;
                }
            }
            if (isValid && StringUtils.isNotBlank(quantity.getError()))
                quantity.setError(null);
        }
        if (isValid) {
            return new
                    ValidationStatus(true, null, formFragmentView, lotsContainer);
        } else
            return new ValidationStatus(false, "Not Valid", formFragmentView, lotsContainer);

    }

    public List<LotDto> getSelectedLotDTos() {
        return selectedLotDTos;
    }
}
