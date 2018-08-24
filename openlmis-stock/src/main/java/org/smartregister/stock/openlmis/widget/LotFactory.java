package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;

import java.util.ArrayList;
import java.util.List;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ERR;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY_ID;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY_PARENT;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.V_REQUIRED;

/**
 * Created by samuelgithengi on 8/23/18.
 */
public class LotFactory implements FormWidgetFactory {

    private final static String STATUS_FIELD_NAME = "lot_status";
    private final static String TAG = "LotFactory";

    private final static String TRADE_ITEM = "trade_item";
    private final static String TRADE_ITEM_ID = "trade_item_id";

    private LinearLayout lotsContainer;

    private Context context;

    private LotListener lotListener = new LotListener();

    private List<Lot> lotList;

    private JSONArray statusOptions;


    @Override
    public List<View> getViewsFromJson(String s, final Context context, JsonFormFragment jsonFormFragment, JSONObject jsonObject, CommonListener commonListener) throws Exception {
        this.context = context;
        List<View> views = new ArrayList<>(1);
        View root = LayoutInflater.from(context).inflate(R.layout.openlmis_native_form_item_lot, null);

        TextView tradeItem = root.findViewById(R.id.trade_item);
        tradeItem.setText(jsonObject.getString(TRADE_ITEM));

        lotsContainer = root.findViewById(R.id.lots_Container);
        root.findViewById(R.id.add_lot).setOnClickListener(lotListener);

        lotList = OpenLMISLibrary.getInstance().getLotRepository().findLotsByTradeItem(jsonObject.getString(TRADE_ITEM_ID));

        TextInputEditText lotDropdown = root.findViewById(R.id.lot_dropdown);
        lotDropdown.setTag(R.id.lot_position, 0);
        populateLotOptions(context, lotDropdown);


        TextInputEditText statusDropdown = root.findViewById(R.id.status_dropdown);
        statusOptions = jsonObject.getJSONArray(STATUS_FIELD_NAME);
        populateQuantityOptions(context, statusDropdown);
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
        populateQuantityOptions(context, (TextInputEditText) lotView.findViewById(R.id.status_dropdown));
        cancelButton.setOnClickListener(lotListener);
        lotsContainer.addView(lotView, viewIndex);
    }

    private void removeLotRow(View view) {
        lotsContainer.removeViewAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
    }


    private void showQuantityAndStatus(View view) {
        View lotRow = lotsContainer.getChildAt(Integer.parseInt(view.getTag(R.id.lot_position).toString()));
        lotRow.findViewById(R.id.lot_quantity).setVisibility(View.VISIBLE);
        lotRow.findViewById(R.id.lot_status).setVisibility(View.VISIBLE);
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

    private void populateQuantityOptions(final Context context, final TextInputEditText editText) {
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
                for (Lot lot : lotList) {
                    popupMenu.getMenu().add(lot.getLotCode());
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setText(menuItem.getTitle());
                        showQuantityAndStatus(editText);
                        return true;
                    }
                });
            }
        });
    }

    private class LotListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.add_lot)
                addLotRow();
            else if (view.getId() == R.id.cancel_button)
                removeLotRow(view);
            else if (view.getId() == R.id.lot_dropdown)
                showQuantityAndStatus(view);

        }
    }


}
