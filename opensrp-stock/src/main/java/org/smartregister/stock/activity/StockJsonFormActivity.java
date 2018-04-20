package org.smartregister.stock.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.validators.edittext.MinNumericValidator;

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
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by keyman on 11/04/2017.
 */
public class StockJsonFormActivity extends JsonFormActivity {

    private static final String TAG = StockJsonFormActivity.class.getName();
    private MaterialEditText balancetextview;
    private StockJsonFormFragment stockJsonFormFragment;

    private MinNumericValidator negativeBalanceValidator;

    private String mainDateFieldKey;
    private boolean otherStockFieldsEnabled = false;

    private HashMap<String, Boolean> previousReadOnlyValues = new HashMap<>();
    private ArrayList<View> labels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init(String json) {
        super.init(json);
        disableAllFieldsExceptDateField(getmJSONObject());
    }

    /**
     * Disables(Read_only kind) other fields in stock forms Received, Issued & Loss/Adjustment other
     * than the first date_picker. This in essence is supposed to restrict the user from filling other
     * fields without entering the date first
     *
     * @param form
     * @return JSONObject
     */
    private void disableAllFieldsExceptDateField(@NonNull JSONObject form) {
        if (form.has(JsonFormConstants.FIRST_STEP_NAME)) {
            try {
                JSONObject step1 = form.getJSONObject(JsonFormConstants.FIRST_STEP_NAME);
                String title = (step1.has("title")) ? step1.getString("title") : null;
                if (!TextUtils.isEmpty(title) && (title.contains("Stock Issued") || title.contains("Stock Loss/Adjustment")
                        || title.contains("Stock Received"))) {
                    if (step1.has(JsonFormConstants.FIELDS)) {
                        JSONArray fields = step1.getJSONArray(JsonFormConstants.FIELDS);

                        int size = fields.length();

                        boolean foundDateField = false;
                        for (int i = 0; i < size; i++) {
                            JSONObject currField = fields.getJSONObject(i);

                            if (!foundDateField && JsonFormConstants.DATE_PICKER.equals(currField.getString(JsonFormConstants.TYPE))) {
                                mainDateFieldKey = currField.optString(JsonFormConstants.KEY, null);
                                continue;
                            }

                            String key = currField.optString(JsonFormConstants.KEY, null);
                            if (!TextUtils.isEmpty(key)) {
                                if (currField.has(JsonFormConstants.READ_ONLY)) {
                                    previousReadOnlyValues.put(key, currField.getBoolean(JsonFormConstants.READ_ONLY));
                                }
                            }
                            currField.put(JsonFormConstants.READ_ONLY, true);
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

        }
    }

    @Override
    public void initializeFormFragment() {
        stockJsonFormFragment = StockJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, stockJsonFormFragment).commit();
        negativeBalanceValidator = new MinNumericValidator(getResources().getString(R.string.negative_balance), 0);
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        refreshCalculateLogic(key, value);

        // Should run on when the first date field is changed i.e. the only field not disabled
        if (!TextUtils.isEmpty(mainDateFieldKey) && mainDateFieldKey.equals(key) && !otherStockFieldsEnabled) {
            if (!TextUtils.isEmpty(value)) {
                ArrayList<View> views = getFormDataViews();

                Iterator<View> viewIterator = views.iterator();
                while(viewIterator.hasNext()) {
                    View view = viewIterator.next();
                    String viewKey = (String) view.getTag(R.id.key);

                    if (viewKey == null || (!TextUtils.isEmpty(viewKey) && !mainDateFieldKey.equals(viewKey))) {
                        String address = (String) view.getTag(R.id.address);
                        if (!TextUtils.isEmpty(address)) {
                            JSONObject jsonObject = getObjectUsingAddress(address.split(":"));
                            boolean previousValue = false;

                            if (previousReadOnlyValues.containsKey(viewKey)) {
                                previousValue = previousReadOnlyValues.get(viewKey);
                            }

                            jsonObject.put(JsonFormConstants.READ_ONLY, previousValue);
                            toggleViewVisibility(view, !previousValue);

                        } else {
                            toggleViewVisibility(view, true);
                        }

                        if (view instanceof MaterialEditText) {
                            view.setFocusable(true);
                        }

                        // Re-hide fields not relevant, since they will be enabled
                        if (!TextUtils.isEmpty(viewKey)) {
                            refreshSkipLogic(viewKey, null);
                        }
                    }
                }

                Iterator<View> labelViewsIterator = labels.iterator();

                // Re-enable labels which are not part of form-data-elements
                while(labelViewsIterator.hasNext()) {
                    View label = labelViewsIterator.next();
                    label.setEnabled(true);
                }

                otherStockFieldsEnabled = true;
            }
        }
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
                String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
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

                    JSONArray fields = object.getJSONArray("fields");
                    int vialsused = 0;
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
                                    refreshDosesWasted(balancetextview, currentBalance, Integer.parseInt(wastedvials), dosesPerVial);
                                    refreshVialsBalance(vaccineName, str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()));
                                }
                            }

                        }
                    }
                    if (!StringUtils.isBlank(vialsvalue) && StringUtils.isNumeric(vialsvalue) && StringUtils.isNumeric(wastedvials)) {
                        newBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()) - Integer.parseInt(vialsvalue) - Integer.parseInt(wastedvials);
                        refreshVialsBalance(vaccineName, newBalance);
                    }

                    if (currentBalance % dosesPerVial == 0) {
                        vialsused = currentBalance / dosesPerVial;
                    } else if (currentBalance != 0) {
                        vialsused = (currentBalance / dosesPerVial) + 1;
                    }
                    if(StringUtils.isBlank(balancetextview.getText().toString())) {
                        balancetextview.setText(String.valueOf(vialsused));
                    }
                    refreshVialsBalance(vaccineName, calculateNewStock(str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()), vialsused));
                    refreshDosesWasted(balancetextview, currentBalance, Integer.parseInt(wastedvials), dosesPerVial);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int calculateNewStock(int currentStock, int issuedStock) {
        if (issuedStock != 0) {
            return currentStock - issuedStock;
        }
        return currentStock;
    }

    private int calculateDosesWasted(int childrenVaccinated, int vialsIssued, int wastedVials, int dosesPerVial) {
        if (childrenVaccinated <= (vialsIssued * dosesPerVial)) {
            return ((vialsIssued * dosesPerVial) - childrenVaccinated) + (wastedVials * dosesPerVial);
        }
        return (wastedVials * dosesPerVial);
    }

    private void displayChildrenVialsUsed(int childrenVaccinated, int vialsUsed, int vialsWasted) {
        stockJsonFormFragment.getLabelViewFromTag("Children_Vaccinated_Count", "Children vaccinated on this date: " + childrenVaccinated);
        stockJsonFormFragment.getLabelViewFromTag("Vials_Issued_Count", "Estimated vials issued on this date: " + vialsUsed);
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
                    refreshVialsBalance(vaccineName, existingbalance);
                    if (value != null && !StringUtils.isBlank(value) && StringUtils.isNumeric(value) && StringUtils.isNumeric(wastedvials)) {
                        newBalance = existingbalance - Integer.parseInt(value) - Integer.parseInt(wastedvials);
                        refreshVialsBalance(vaccineName, newBalance);

                    } else {
                        refreshVialsBalance(vaccineName, existingbalance);

                    }
                    int vialsused = 0;
                    StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsused = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsused = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    refreshDosesWasted(balancetextview, currentBalanceVaccineUsed, Integer.parseInt(wastedvials), dosesPerVial);
                    displayChildrenVialsUsed(currentBalanceVaccineUsed, vialsused, Integer.parseInt(wastedvials));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshDosesWasted(MaterialEditText issuedVials, int currentBalanceVaccineUsed, int vialsWasted, int dosesPerVial) {
        int vialsIssued = 0;
        if (issuedVials != null && issuedVials.getText() != null && !issuedVials.getText().toString().trim().equals("")) {
            try {
                vialsIssued = Integer.parseInt(issuedVials.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        int wastedDoses = calculateDosesWasted(currentBalanceVaccineUsed, vialsIssued, vialsWasted, dosesPerVial);
        stockJsonFormFragment.getLabelViewFromTag("Doses_wasted", "Total wasted doses: " + wastedDoses + " doses");
    }


    public void refreshVialsBalance(String vaccineName, int newBalance) {
        stockJsonFormFragment.getLabelViewFromTag("Vials_Balance", "New " + vaccineName + " balance: " + newBalance + " vials");
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
                    refreshVialsBalance(vaccineName, existingbalance);
                    if (wastedvials == null || StringUtils.isBlank(wastedvials)) {
                        wastedvials = "0";
                    }
                    if (vialsvalue != null && !StringUtils.isBlank(vialsvalue) && StringUtils.isNumeric(wastedvials)) {

                        newBalance = existingbalance - Integer.parseInt(vialsvalue) - Integer.parseInt(wastedvials);
                        refreshVialsBalance(vaccineName, newBalance);
                    } else {
                        refreshVialsBalance(vaccineName, existingbalance);
                    }
                    int vialsused = 0;
                    StockTypeRepository vaccine_typesRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccine_typesRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsused = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsused = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    displayChildrenVialsUsed(currentBalanceVaccineUsed, vialsused, Integer.parseInt(wastedvials));
                    refreshDosesWasted(balancetextview, currentBalanceVaccineUsed, Integer.parseInt(wastedvials), dosesPerVial);
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
                String vaccineName = object.getString("title").replace("Stock Received", "").trim();
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
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "New " + vaccineName + " balance: " + displaybalance + " vials");

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
                    String vaccineName = object.getString("title").replace("Stock Received", "").trim();
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

                                StockRepository str = StockLibrary.getInstance().getStockRepository();
                                currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                            }

                            if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                                displaybalance = currentBalance + Integer.parseInt(value);
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "New " + vaccineName + " balance: " + displaybalance + " vials");

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
                String vaccineName = object.getString("title").replace("Stock Loss/Adjustment", "").trim();
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

                            StockRepository str = StockLibrary.getInstance().getStockRepository();
                            currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                        }
                        if (questions.getString("key").equalsIgnoreCase("Vials_Adjustment") && (questions.has("value"))) {
                            label = questions.getString("value");
                            vialsvalue = label;
                        }
                        if (vialsvalue != null && !vialsvalue.equalsIgnoreCase("") && NumberUtils.isNumber(vialsvalue)) {
                            displaybalance = currentBalance + Integer.parseInt(vialsvalue);
                            if (balancetextview != null && displaybalance < 0) {
                                balancetextview.addValidator(negativeBalanceValidator);
                            } else if (balancetextview != null && displaybalance >= 0)
                                balancetextview.getValidators().remove(negativeBalanceValidator);
                            stockJsonFormFragment.getLabelViewFromTag("Balance", "New " + vaccineName + " balance: " + displaybalance + " vials");

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
                    if (balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase(key)) {
                                balancetextview = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    String vaccineName = object.getString("title").replace("Stock Loss/Adjustment", "").trim();
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

                                StockRepository str = StockLibrary.getInstance().getStockRepository();
                                currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                            }
                            if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("-") && NumberUtils.isNumber(value)) {
                                displaybalance = currentBalance + Integer.parseInt(value);
                                if (balancetextview != null && displaybalance < 0) {
                                    balancetextview.addValidator(negativeBalanceValidator);
                                } else if (balancetextview != null && displaybalance >= 0)
                                    balancetextview.getValidators().remove(negativeBalanceValidator);
                                stockJsonFormFragment.getLabelViewFromTag("Balance", "New " + vaccineName + " balance: " + displaybalance + " vials");

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

    public void addLabel(View view) {
        labels.add(view);
    }
}
