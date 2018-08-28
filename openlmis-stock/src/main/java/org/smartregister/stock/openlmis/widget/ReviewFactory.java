package org.smartregister.stock.openlmis.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.adapter.ReviewAdapter;
import org.smartregister.stock.openlmis.fragment.OpenLMISJsonFormFragment;
import org.smartregister.stock.openlmis.widget.helper.LotDto;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.smartregister.stock.openlmis.widget.LotFactory.DISPENSING_UNIT;
import static org.smartregister.stock.openlmis.widget.LotFactory.NET_CONTENT;
import static org.smartregister.stock.openlmis.widget.LotFactory.TRADE_ITEM;
import static org.smartregister.stock.openlmis.widget.LotFactory.TRADE_ITEM_ID;
import static org.smartregister.util.JsonFormUtils.FIELDS;


/**
 * Created by samuelgithengi on 8/27/18.
 */
public class ReviewFactory implements FormWidgetFactory {

    public final static String REVIEW_TYPE = "review_type";
    public final static String DATE = "date";
    public final static String FACILITY = "facility";
    public final static String REASON = "reason";
    public final static String STEP2 = "step2";
    public final static String STOCK_LOTS = "stockLots";


    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {

        String tradeItem = jsonObject.getString(TRADE_ITEM);
        String tradeItemId = jsonObject.getString(TRADE_ITEM_ID);
        long netContent = jsonObject.getLong(NET_CONTENT);
        String dispensingUnit = jsonObject.getString(DISPENSING_UNIT);

        List<View> views = new ArrayList<>(1);
        View root = LayoutInflater.from(context).inflate(R.layout.openlmis_native_form_item_review, null);
        ((TextView) root.findViewById(R.id.review_type)).setText(jsonObject.getString(REVIEW_TYPE));

        JSONObject formJSon = new JSONObject(formFragment.getCurrentJsonState());
        JSONArray step1Fields = JsonFormUtils.fields(formJSon);
        ((TextView) root.findViewById(R.id.date)).setText(JsonFormUtils.getFieldValue(step1Fields, jsonObject.getString(DATE)));
        ((TextView) root.findViewById(R.id.facility)).setText(JsonFormUtils.getFieldValue(step1Fields, jsonObject.getString(FACILITY)));
        ((TextView) root.findViewById(R.id.reason)).setText(JsonFormUtils.getFieldValue(step1Fields, jsonObject.getString(REASON)));
        views.add(root);

        step1Fields = formJSon.getJSONObject(STEP2).getJSONArray(FIELDS);

        String lotsJSON = JsonFormUtils.getFieldValue(step1Fields, STOCK_LOTS);
        RecyclerView reviewRecyclerView = root.findViewById(R.id.review_recyclerView);
        Type listType = new TypeToken<List<LotDto>>() {
        }.getType();
        List<LotDto> selectedLotDTos = LotFactory.gson.fromJson(lotsJSON, listType);
        reviewRecyclerView.setAdapter(new ReviewAdapter(tradeItem, selectedLotDTos));

        displayDosesQuantity((OpenLMISJsonFormFragment) formFragment, context, selectedLotDTos, dispensingUnit, netContent);

        return views;
    }

    private void displayDosesQuantity(OpenLMISJsonFormFragment jsonFormFragment, Context context,
                                      List<LotDto> selectedLotDTos, String dispensingUnit, long netContent) {
        int totalQuantity = 0;
        for (LotDto lot : selectedLotDTos)
            totalQuantity += lot.getQuantity();
        jsonFormFragment.setBottomNavigationText(context.getString(R.string.issued_dose_formatter,
                totalQuantity, dispensingUnit, totalQuantity * netContent));
    }
}
