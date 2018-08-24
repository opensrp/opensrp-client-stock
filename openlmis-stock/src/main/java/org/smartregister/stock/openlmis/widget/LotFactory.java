package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ERR;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY_ID;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY_PARENT;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.V_REQUIRED;
import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;

/**
 * Created by samuelgithengi on 8/23/18.
 */
public class LotFactory implements FormWidgetFactory {

    private final static String STATUS_FIELD_NAME = "lot_status";
    private final static String TAG = "LotFactory";

    private final static String TRADE_ITEM = "trade_item";
    private final static String TRADE_ITEM_ID = "trade_item_id";
    private final static String NET_CONTENT = "net_content";
    private final static String DISPENSING_UNIT = "dispensing_unit";

    private final static Gson gson = new GsonBuilder().create();

    private LinearLayout lotsContainer;

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

    public LotFactory() {
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, JsonFormFragment jsonFormFragment, JSONObject jsonObject, CommonListener commonListener) throws Exception {
        this.stepName = stepName;
        this.context = context;
        this.jsonFormFragment = (OpenLMISJsonFormFragment) jsonFormFragment;
        selectedLotDTos = new ArrayList();
        lotMap = new HashMap<>();
        selectedLotsMap = new HashMap<>();

        key = jsonObject.getString(KEY);
        List<View> views = new ArrayList<>(1);
        View root = LayoutInflater.from(context).inflate(R.layout.openlmis_native_form_item_lot, null);

        TextView tradeItem = root.findViewById(R.id.trade_item);
        tradeItem.setText(jsonObject.getString(TRADE_ITEM));

        netContent = jsonObject.getLong(NET_CONTENT);
        dispensingUnit = jsonObject.getString(DISPENSING_UNIT);

        lotsContainer = root.findViewById(R.id.lots_Container);
        root.findViewById(R.id.add_lot).setOnClickListener(lotListener);

        List<Lot> lots = OpenLMISLibrary.getInstance().getLotRepository().findLotsByTradeItem(jsonObject.getString(TRADE_ITEM_ID));
        for (Lot lot : lots)
            lotMap.put(lot.getId().toString(), lot);

        TextInputEditText lotDropdown = root.findViewById(R.id.lot_dropdown);
        lotDropdown.setTag(R.id.lot_position, 0);
        populateLotOptions(context, lotDropdown);


        TextInputEditText statusDropdown = root.findViewById(R.id.status_dropdown);
        statusOptions = jsonObject.getJSONArray(STATUS_FIELD_NAME);
        populateStatusOptions(context, statusDropdown);
        views.add(root);
        return views;
    }


    private void addLotRow() {
        View lotView = LayoutInflater.from(context).inflate(R.layout.native_form_lot_row, null);
        int viewIndex = lotsContainer.getChildCount() - 1;
        View cancelButton = lotView.findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setTag(R.id.lot_position, viewIndex);
        TextInputEditText lotDropdown = lotView.findViewById(R.id.lot_dropdown);
        lotDropdown.setTag(R.id.lot_position, viewIndex);
        populateLotOptions(context, lotDropdown);
        populateStatusOptions(context, (TextInputEditText) lotView.findViewById(R.id.status_dropdown));
        cancelButton.setOnClickListener(lotListener);
        lotsContainer.addView(lotView, viewIndex);
    }

    private void removeLotRow(View view) {
        View lotRow = lotsContainer.getChildAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
        Object lotId = lotRow.findViewById(R.id.lot_dropdown).getTag(R.id.lot_id);
        if (lotId != null) {
            lotMap.put(lotId.toString(), selectedLotsMap.remove(lotId.toString()));
            writeValues();
        }
        lotsContainer.removeView(lotRow);
        displayDosesQuantity();
    }


    private void showQuantityAndStatus(View view, String lotId) {
        View lotRow = lotsContainer.getChildAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
        lotRow.findViewById(R.id.lot_quantity).setVisibility(View.VISIBLE);
        TextInputEditText quantity = lotRow.findViewById(R.id.quantity_textview);
        quantity.setTag(R.id.lot_id, lotId);
        quantity.addTextChangedListener(new QuantityTextWatcher(quantity));
        lotRow.findViewById(R.id.lot_status).setVisibility(View.VISIBLE);
        lotRow.findViewById(R.id.status_dropdown).setTag(R.id.lot_id, lotId);

    }

    private void populateProperties(TextInputEditText editText, JSONObject jsonObject) {
        String key = jsonObject.optString(KEY);
        String type = jsonObject.optString(TYPE);
        String openMrsEntityParent = jsonObject.optString(OPENMRS_ENTITY_PARENT);
        String openMrsEntity = jsonObject.optString(OPENMRS_ENTITY);
        String openMrsEntityId = jsonObject.optString(OPENMRS_ENTITY_ID);
        editText.setTag(com.vijay.jsonwizard.R.id.key, key);
        editText.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent, openMrsEntityParent);
        editText.setTag(com.vijay.jsonwizard.R.id.openmrs_entity, openMrsEntity);
        editText.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_id, openMrsEntityId);
        editText.setTag(com.vijay.jsonwizard.R.id.type, type);

        JSONObject requiredObject = jsonObject.optJSONObject(V_REQUIRED);
        String valueToSelect;
        if (requiredObject != null) {
            valueToSelect = requiredObject.optString(VALUE);
            if (!TextUtils.isEmpty(valueToSelect)) {
                editText.setTag(com.vijay.jsonwizard.R.id.v_required, valueToSelect);
                editText.setTag(com.vijay.jsonwizard.R.id.error, requiredObject.optString(ERR));
            }
        }
    }

    private void populateStatusOptions(final Context context, final TextInputEditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                for (int i = 0; i < statusOptions.length(); i++) {
                    popupMenu.getMenu().add(statusOptions.optString(i));
                }
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
    }

    private void populateLotOptions(final Context context, final TextInputEditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                for (Lot lot : lotMap.values()) {
                    MenuItem menuitem = popupMenu.getMenu().add(context.getString(R.string.lotcode_and_expiry,
                            lot.getLotCode(), lot.getExpirationDate().toString(DATE_FORMAT)));
                    View actionView = new View(context);
                    actionView.setTag(R.id.lot_id, lot.getId());
                    menuitem.setActionView(actionView);
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String lotId = menuItem.getActionView().getTag(R.id.lot_id).toString();
                        editText.setText(menuItem.getTitle());
                        editText.setTag(R.id.lot_id, lotId);
                        showQuantityAndStatus(editText, lotId);
                        if (!selectedLotDTos.contains(new LotDto(lotId))) {
                            selectedLotDTos.add(new LotDto(lotId));
                        }
                        selectedLotsMap.put(lotId, lotMap.remove(lotId));
                        return true;
                    }
                });
            }
        });
    }

    private void displayDosesQuantity() {
        int totalQuantity = 0;
        for (LotDto lot : selectedLotDTos)
            totalQuantity += lot.getQuantity();
        jsonFormFragment.setBottomNavigationText(context.getString(R.string.issued_dose_formatter,
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
            else if (view.getId() == R.id.lot_dropdown)
                showQuantityAndStatus(view, view.getTag(R.id.lot_id).toString());

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
            else
                lotDto.setQuantity(Integer.parseInt(editable.toString()));
            writeValues();
            displayDosesQuantity();
        }
    }

}
