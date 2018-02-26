package org.smartregister.stock.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.fragment.StockJsonFormFragment;
import org.smartregister.stock.repository.StockExternalRepository;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by keyman on 11/04/2017.
 */
public class StockJsonFormActivity extends JsonFormActivity {

    private MaterialEditText balancetextview;
    private StockJsonFormFragment stockJsonFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initializeFormFragment() {
        stockJsonFormFragment = StockJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, stockJsonFormFragment).commit();
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        refreshCalculateLogic(key, value);

    }

    @Override
    public void onFormFinish() {
        super.onFormFinish();
    }

    protected void refreshCalculateLogic(String key, String value) {
        stockVialsenteredinReceivedForm(key, value);
        stockDateEnteredinReceivedForm(key, value);
        stockDateEnteredinIssuedForm(key, value);
        stockVialsEnteredinIssuedForm(key, value);
        stockWastedVialsEnteredinIssuedForm(key, value);
        stockDateEnteredinAdjustmentForm(key, value);
        stockVialsenteredinAdjustmentForm(key, value);
    }

    private void stockDateEnteredinIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Issued")) {
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Date_Stock_Issued") && value != null && !value.equalsIgnoreCase("")) {
                    if (balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balancetextview = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalance = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String vialsvalue = "";
                    String wastedvials = "0";
                    String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Issued") &&
                                    questions.has("value")) {
                                label = questions.getString("value");
                                if (label != null && StringUtils.isNotBlank(label)) {
                                    Date dateTime = JsonFormUtils.formatDate(label, false);
                                    if (dateTime != null) {
                                        encounterDate = dateTime;
                                    }
                                }
                                StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
                                currentBalance = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkifmeasles(vaccineName.toLowerCase()));
                            }

                            if (questions.getString("key").equalsIgnoreCase("Vials_Wasted")) {
                                if (questions.has("value")) {
                                    if (!StringUtils.isBlank(questions.getString("value"))) {
                                        wastedvials = questions.getString("value");
                                    }
                                } else {
                                    wastedvials = "0";
                                }
                            }
                            if (questions.getString("key").equalsIgnoreCase("Vials_Issued")) {
                                if (questions.has("value")) {
                                    if (!StringUtils.isBlank(questions.getString("value"))) {
                                        vialsvalue = questions.getString("value");
                                    }
                                } else {
                                    stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                                }
                            }

                        }
                    }
                    if (!StringUtils.isBlank(vialsvalue) && StringUtils.isNumeric(vialsvalue) && StringUtils.isNumeric(wastedvials)) {
                        newBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()) - Integer.parseInt(vialsvalue) - Integer.parseInt(wastedvials);
                        stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + newBalance);
                    }

                    int vialsused = 0;
                    StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
                    if (currentBalance % dosesPerVial == 0) {
                        vialsused = currentBalance / dosesPerVial;
                    } else if (currentBalance != 0) {
                        vialsused = (currentBalance / dosesPerVial) + 1;
                    }
                    initializeBalanceTextView(currentBalance, vialsused, balancetextview);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeBalanceTextView(int currentBalance, int vialsUsed, MaterialEditText balanceTextView) {
        if (balanceTextView != null) {
            balanceTextView.setErrorColor(Color.BLACK);
            if (currentBalance != 0) {
                Typeface typeFace = Typeface.create(balanceTextView.getTypeface(), Typeface.ITALIC);
                balanceTextView.setAccentTypeface(typeFace);
                balanceTextView.setError(currentBalance + " child(ren) vaccinated today. Assuming " + vialsUsed + " vial(s) used.");
            } else {
                balanceTextView.setError("");
            }
        }
    }

    private void stockVialsEnteredinIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Issued")) {
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Vials_Issued")) {
                    if (balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balancetextview = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalanceVaccineUsed = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String wastedvials = "0";
                    String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                    int existingbalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Issued")) {
                                if (questions.has("value")) {
                                    label = questions.getString("value");
                                    if (label != null && StringUtils.isNotBlank(label)) {
                                        Date dateTime = JsonFormUtils.formatDate(label, false);
                                        if (dateTime != null) {
                                            encounterDate = dateTime;
                                        }
                                    }
                                    existingbalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                                    StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
                                    currentBalanceVaccineUsed = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkifmeasles(vaccineName.toLowerCase()));

                                }
                            } else if (questions.getString("key").equalsIgnoreCase("Vials_Wasted")) {

                                if (questions.has("value")) {
                                    if (!StringUtils.isBlank(questions.getString("value")) && StringUtils.isNumeric(questions.getString("value"))) {
                                        wastedvials = questions.getString("value");
                                    }
                                } else {
                                    wastedvials = "0";
                                }
                            }

                        }
                    }
                    stockJsonFormFragment.getLabelViewFromTag("Balance", "");

                    if (value != null && !StringUtils.isBlank(value) && StringUtils.isNumeric(value) && StringUtils.isNumeric(wastedvials)) {

                        newBalance = existingbalance - Integer.parseInt(value) - Integer.parseInt(wastedvials);
                        stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + newBalance);
                    } else {
                        stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                    }
                    int vialsused = 0;
                    StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsused = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsused = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    initializeBalanceTextView(currentBalanceVaccineUsed, vialsused, balancetextview);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void stockWastedVialsEnteredinIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Issued")) {
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Vials_Wasted")) {
                    if (balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText && ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balancetextview = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalanceVaccineUsed = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String vialsvalue = "";
                    String wastedvials = value;
                    String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                    int existingbalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());

                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Issued") && questions.has("value")) {
                                label = questions.getString("value");
                                if (label != null && StringUtils.isNotBlank(label)) {
                                    Date dateTime = JsonFormUtils.formatDate(label, false);
                                    if (dateTime != null) {
                                        encounterDate = dateTime;
                                    }
                                }

                                existingbalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                                StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
                                currentBalanceVaccineUsed = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkifmeasles(vaccineName.toLowerCase()));
                            } else if (questions.getString("key").equalsIgnoreCase("Vials_Issued")) {
                                if (questions.has("value")) {
                                    if (!StringUtils.isBlank(questions.getString("value")) && StringUtils.isNumeric(questions.getString("value"))) {
                                        vialsvalue = questions.getString("value");
                                    }
                                } else {
                                    vialsvalue = "0";
                                }
                            }
                        }
                    }
                    stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                    if (wastedvials == null || StringUtils.isBlank(wastedvials)) {
                        wastedvials = "0";
                    }
                    if (vialsvalue != null && !StringUtils.isBlank(vialsvalue) && StringUtils.isNumeric(wastedvials)) {

                        newBalance = existingbalance - Integer.parseInt(vialsvalue) - Integer.parseInt(wastedvials);
                        stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + newBalance);
                    } else {
                        stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                    }
                    int vialsused = 0;
                    StockTypeRepository vaccine_typesRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccine_typesRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsused = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsused = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    initializeBalanceTextView(currentBalanceVaccineUsed, vialsused, balancetextview);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void stockDateEnteredinReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Received")
                    && key.equalsIgnoreCase("Date_Stock_Received")
                    && value != null && !value.equalsIgnoreCase("")) {

                String label = "";
                int currentBalance = 0;
                int displaybalance = 0;
                String vialsvalue = "";
                JSONArray fields = object.getJSONArray("fields");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject questions = fields.getJSONObject(i);
                    if (questions.has("key")) {
                        if (questions.getString("key").equalsIgnoreCase("Date_Stock_Received") && questions.has("value")) {
                            Date encounterDate = new Date();
                            label = questions.getString("value");
                            if (label != null && StringUtils.isNotBlank(label)) {
                                Date dateTime = JsonFormUtils.formatDate(label, false);
                                if (dateTime != null) {
                                    encounterDate = dateTime;
                                }
                            }

                            String vaccineName = object.getString("title").replace("Stock Received", "").trim();
                            StockRepository str = StockLibrary.getInstance().getStockRepository();
                            currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                        }
                        if (questions.getString("key").equalsIgnoreCase("Vials_Received") && questions.has("value")) {
                            label = questions.getString("value");
                            vialsvalue = label;
                        }
                        if (vialsvalue != null && !vialsvalue.equalsIgnoreCase("") && StringUtils.isNumeric(vialsvalue)) {
                            displaybalance = currentBalance + Integer.parseInt(vialsvalue);
//                                if (balancetextview != null) {
//                                    balancetextview.setErrorColor(getResources().getColor(R.color.dark_grey));
//                                    balancetextview.setError("New balance : " + displaybalance);
//                                }
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + displaybalance);

                        } else {
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "");

                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void stockVialsenteredinReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Received")) {
                if (key.equalsIgnoreCase("Vials_Received") && value != null && !value.equalsIgnoreCase("")) {
//                    if(balancetextview == null) {
//                        ArrayList<View> views = getFormDataViews();
//                        for (int i = 0; i < views.size(); i++) {
//                            if (views.get(i) instanceof MaterialEditText) {
//                                if (((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase(key)) {
//                                    balancetextview = (MaterialEditText) views.get(i);
//                                }
//                            }
//                        }
//                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Received") && questions.has("value")) {
                                Date encounterDate = new Date();
                                label = questions.getString("value");
                                if (label != null && StringUtils.isNotBlank(label)) {
                                    Date dateTime = JsonFormUtils.formatDate(label, false);
                                    if (dateTime != null) {
                                        encounterDate = dateTime;
                                    }
                                }

                                String vaccineName = object.getString("title").replace("Stock Received", "").trim();
                                StockRepository str = StockLibrary.getInstance().getStockRepository();
                                currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                            }

                            if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                                displaybalance = currentBalance + Integer.parseInt(value);
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + displaybalance);

                            } else {
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                            }
                        }
                    }
                } else {
                    stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                }
            }
        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

    }

    private void stockDateEnteredinAdjustmentForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Loss/Adjustment") &&
                    key.equalsIgnoreCase("Date_Stock_loss_adjustment") && value != null && !value.equalsIgnoreCase("")) {
                String label = "";
                int currentBalance = 0;
                int displaybalance = 0;
                String vialsvalue = "";
                JSONArray fields = object.getJSONArray("fields");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject questions = fields.getJSONObject(i);
                    if (questions.has("key")) {
                        if (questions.getString("key").equalsIgnoreCase("Date_Stock_loss_adjustment") && questions.has("value")) {
                            Date encounterDate = new Date();
                            label = questions.getString("value");
                            if (label != null && StringUtils.isNotBlank(label)) {
                                Date dateTime = JsonFormUtils.formatDate(label, false);
                                if (dateTime != null) {
                                    encounterDate = dateTime;
                                }
                            }

                            String vaccineName = object.getString("title").replace("Stock Loss/Adjustment", "").trim();
                            StockRepository str = StockLibrary.getInstance().getStockRepository();
                            currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                        }
                        if (questions.getString("key").equalsIgnoreCase("Vials_Adjustment") && (questions.has("value"))) {
                            label = questions.getString("value");
                            vialsvalue = label;
                        }
                        if (vialsvalue != null && !vialsvalue.equalsIgnoreCase("") && StringUtils.isNumeric(vialsvalue)) {
                            displaybalance = currentBalance + Integer.parseInt(vialsvalue);
//                                if (balancetextview != null) {
//                                    balancetextview.setErrorColor(getResources().getColor(R.color.dark_grey));
//                                    balancetextview.setError("New balance : " + displaybalance);
//                                }
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + displaybalance);

                        } else {
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "");

                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void stockVialsenteredinAdjustmentForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Loss/Adjustment")) {
                if (key.equalsIgnoreCase("Vials_Adjustment") && value != null && !value.equalsIgnoreCase("")) {
//                    if(balancetextview == null) {
//                        ArrayList<View> views = getFormDataViews();
//                        for (int i = 0; i < views.size(); i++) {
//                            if (views.get(i) instanceof MaterialEditText) {
//                                if (((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase(key)) {
//                                    balancetextview = (MaterialEditText) views.get(i);
//                                }
//                            }
//                        }
//                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_loss_adjustment") && questions.has("value")) {
                                Date encounterDate = new Date();
                                label = questions.getString("value");
                                if (label != null && StringUtils.isNotBlank(label)) {
                                    Date dateTime = JsonFormUtils.formatDate(label, false);
                                    if (dateTime != null) {
                                        encounterDate = dateTime;
                                    }
                                }

                                String vaccineName = object.getString("title").replace("Stock Loss/Adjustment", "").trim();
                                StockRepository str = StockLibrary.getInstance().getStockRepository();
                                currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                            }
                            if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("-") && NumberUtils.isNumber(value)) {
                                displaybalance = currentBalance + Integer.parseInt(value);
//                                if (balancetextview != null) {
//                                    balancetextview.setErrorColor(Color.BLACK);
//                                    balancetextview.setError("New balance : " + displaybalance);
//                                }
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "New balance: " + displaybalance);

                            } else {
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                            }
                        }
                    }
                } else {
                    stockJsonFormFragment.getLabelViewFromTag("Balance", "");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String checkifmeasles(String vaccineName) {
        if (vaccineName.equalsIgnoreCase("M/MR")) {
            return "measles";
        }
        return vaccineName;
    }

}

