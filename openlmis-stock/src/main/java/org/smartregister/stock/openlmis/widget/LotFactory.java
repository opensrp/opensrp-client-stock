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
import org.smartregister.stock.openlmis.R;

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


    private final static String LOTS_FIELD_NAME = "lots";

    private final static String STATUS_FIELD_NAME = "status";
    private final static String TAG = "LotFactory";

    private final static String TRADE_ITEM = "trade_item";

    @Override
    public List<View> getViewsFromJson(String s, final Context context, JsonFormFragment jsonFormFragment, JSONObject jsonObject, CommonListener commonListener) throws Exception {
        List<View> views = new ArrayList<>(1);
        View root = LayoutInflater.from(context).inflate(R.layout.openlmis_native_form_item_lot, null);

        TextView tradeItem = root.findViewById(R.id.trade_item);
        tradeItem.setText(jsonObject.getString(TRADE_ITEM));

        final LinearLayout lotsContainer = root.findViewById(R.id.lots_Container);
        root.findViewById(R.id.add_lot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lotsContainer.addView(LayoutInflater.from(context).inflate(R.layout.native_form_lot_row, null),
                        lotsContainer.getChildCount() - 1);
            }
        });

        TextInputEditText lotDropdown = root.findViewById(R.id.lot_dropdown);
        JSONArray options = jsonObject.getJSONArray(LOTS_FIELD_NAME);
        populateSpinnerOptions(context, lotDropdown, options);


        TextInputEditText statusDropdown = root.findViewById(R.id.status_dropdown);
        options = jsonObject.getJSONArray(STATUS_FIELD_NAME);
        populateSpinnerOptions(context, statusDropdown, options);
        views.add(root);
        return views;
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

    private void populateSpinnerOptions(final Context context, final TextInputEditText editText, final JSONArray options) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                for (int i = 0; i < options.length(); i++) {
                    popupMenu.getMenu().add(options.optString(i));
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
}
