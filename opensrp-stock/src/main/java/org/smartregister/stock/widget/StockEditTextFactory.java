package org.smartregister.stock.widget;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.util.ViewUtil;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.widgets.EditTextFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.stock.R;
import org.smartregister.stock.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelgithengi on 2/26/18.
 */

public class StockEditTextFactory extends EditTextFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        if (jsonObject.has(Constants.NUMBER_PICKER) && jsonObject.get(Constants.NUMBER_PICKER).toString().equalsIgnoreCase(Boolean.TRUE.toString())) {
            List<View> views = new ArrayList<>(1);

            RelativeLayout rootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
                    R.layout.item_edit_text_number_picker, null);
            final MaterialEditText editText = (MaterialEditText) rootLayout.findViewById(R.id.edit_text);

            attachJson(stepName, context, formFragment, jsonObject, listener, false);

            JSONArray canvasIds = new JSONArray();
            rootLayout.setId(ViewUtil.generateViewId());
            canvasIds.put(rootLayout.getId());
            editText.setTag(R.id.canvas_ids, canvasIds.toString());
            ((JsonApi) context).addFormDataView(editText);
            views.add(rootLayout);

            Button plusbutton = (Button) rootLayout.findViewById(R.id.addbutton);
            Button minusbutton = (Button) rootLayout.findViewById(R.id.minusbutton);

            plusbutton.setId(ViewUtil.generateViewId());
            minusbutton.setId(ViewUtil.generateViewId());

            canvasIds.put(plusbutton.getId());
            canvasIds.put(minusbutton.getId());

            plusbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String edittesxtstring = editText.getText().toString();
                    if (edittesxtstring.equalsIgnoreCase("")) {
                        editText.setText("0");
                    } else {
                        edittesxtstring = "" + (Integer.parseInt(edittesxtstring) + 1);
                        editText.setText(edittesxtstring);
                    }
                }
            });
            minusbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String edittesxtstring = editText.getText().toString();
                    if (edittesxtstring.equalsIgnoreCase("")) {
                        editText.setText("0");
                    } else {
                        edittesxtstring = "" + (Integer.parseInt(edittesxtstring) - 1);
                        editText.setText(edittesxtstring);
                    }
                }
            });

            editText.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);

            if (jsonObject.has(JsonFormConstants.READ_ONLY)) {
                boolean readOnly = jsonObject.getBoolean(JsonFormConstants.READ_ONLY);

                editText.setEnabled(!readOnly);
                editText.setFocusable(!readOnly);

                plusbutton.setEnabled(!readOnly);
                minusbutton.setEnabled(!readOnly);
            }

            editText.setTag(R.id.canvas_ids, canvasIds.toString());


            return views;
        } else {
            return super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);
        }
    }

    @Override
    protected void attachLayout(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, MaterialEditText editText, ImageView editButton) throws Exception {
        super.attachLayout(stepName, context, formFragment, jsonObject, editText, editButton);
    }
}
