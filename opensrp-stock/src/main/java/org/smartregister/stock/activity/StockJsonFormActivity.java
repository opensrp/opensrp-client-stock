package org.smartregister.stock.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
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

import timber.log.Timber;

/**
 * Created by keyman on 11/04/2017.
 */
public class StockJsonFormActivity extends JsonFormActivity {

    private static final String TAG = StockJsonFormActivity.class.getName();
    private MaterialEditText balanceTextView;
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
                if (!TextUtils.isEmpty(title) && (title.contains(getString(R.string.stock_issued)) || title.contains(getString(R.string.stock_loss))
                        || title.contains(getString(R.string.stock_received)))) {
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
        getSupportFragmentManager().beginTransaction().add(R.id.container, stockJsonFormFragment).commit();
        negativeBalanceValidator = new MinNumericValidator(getResources().getString(R.string.negative_balance), 0);
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean popup) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId, popup);

        refreshCalculateLogic(key, value);

        // Should run on when the first date field is changed i.e. the only field not disabled
        if (!TextUtils.isEmpty(mainDateFieldKey) && mainDateFieldKey.equals(key) && !otherStockFieldsEnabled) {
            if (!TextUtils.isEmpty(value)) {
                ArrayList<View> views = new ArrayList<>(getFormDataViews());

                Iterator<View> viewIterator = views.iterator();
                while (viewIterator.hasNext()) {
                    View view = viewIterator.next();
                    String viewKey = (String) view.getTag(R.id.key);

                    if (viewKey == null || (!TextUtils.isEmpty(viewKey) && !mainDateFieldKey.equals(viewKey))) {
                        String address = (String) view.getTag(R.id.address);
                        if (!TextUtils.isEmpty(address)) {
                            JSONObject jsonObject = getObjectUsingAddress(address.split(":"), false);
                            boolean previousValue = false;

                            if (previousReadOnlyValues.containsKey(viewKey)) {
                                previousValue = previousReadOnlyValues.get(viewKey);
                            }

                            jsonObject.put(JsonFormConstants.READ_ONLY, previousValue);
                            toggleViewVisibility(view, !previousValue, false);
                        } else {
                            toggleViewVisibility(view, true, false);
                        }

                        if (view instanceof MaterialEditText) {
                            view.setFocusable(true);
                        }

                        // Re-hide fields not relevant, since they will be enabled
                        if (!TextUtils.isEmpty(viewKey)) {
                            boolean isForNextStep = getmJSONObject().getJSONObject(stepName).has(JsonFormConstants.NEXT);
                            refreshSkipLogic(viewKey, null, false, stepName, isForNextStep);
                        }
                    }
                }

                Iterator<View> labelViewsIterator = labels.iterator();

                // Re-enable labels which are not part of form-data-elements
                while (labelViewsIterator.hasNext()) {
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
        stockVialsEnteredInReceivedForm(key, value);
        stockDateEnteredInReceivedForm(key, value);
        stockDateEnteredInIssuedForm(key, value);
        stockVialsEnteredInIssuedForm(key, value);
        stockWastedVialsEnteredInIssuedForm(key, value);
        stockDateEnteredInAdjustmentForm(key, value);
        stockVialsEnteredInAdjustmentForm(key, value);
    }

    private void stockDateEnteredInIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_issued))) {
                String vaccineName = object.getString("title").replace(getString(R.string.stock_issued), "").trim();
                StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Date_Stock_Issued") && value != null && !value.equalsIgnoreCase("")) {
                    if (balanceTextView == null) {
                        ArrayList<View> views = new ArrayList<>(getFormDataViews());

                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balanceTextView = (MaterialEditText) views.get(i);
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
                                currentBalance = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkIfMeasles(vaccineName.toLowerCase()));
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
                                    refreshDosesWasted(balanceTextView, currentBalance, Integer.parseInt(wastedvials), dosesPerVial);
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
                    if (StringUtils.isBlank(balanceTextView.getText().toString())) {
                        balanceTextView.setText(String.valueOf(vialsused));
                    }
                    refreshVialsBalance(vaccineName, calculateNewStock(str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()), vialsused));
                    refreshDosesWasted(balanceTextView, currentBalance, Integer.parseInt(wastedvials), dosesPerVial);
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
        try {
            stockJsonFormFragment.getLabelViewFromTag("Children_Vaccinated_Count", String.format(getString(R.string.children_vaccinated), childrenVaccinated));
            if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                stockJsonFormFragment.getLabelViewFromTag("Vials_Issued_Count", String.format(getString(R.string.estimated_doses_issued), vialsUsed));
            else
                stockJsonFormFragment.getLabelViewFromTag("Vials_Issued_Count", String.format(getString(R.string.estimated_vials_issued), vialsUsed));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    private void stockVialsEnteredInIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_issued))) {
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Vials_Issued")) {
                    if (balanceTextView == null) {
                        ArrayList<View> views = new ArrayList<>(getFormDataViews());

                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balanceTextView = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalanceVaccineUsed = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String wastedvials = "0";
                    String vaccineName = object.getString("title").replace(getString(R.string.stock_issued), "").trim();
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
                                    currentBalanceVaccineUsed = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkIfMeasles(vaccineName.toLowerCase()));
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
                    int vialsUsed = 0;
                    StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsUsed = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsUsed = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    refreshDosesWasted(balanceTextView, currentBalanceVaccineUsed, Integer.parseInt(wastedvials), dosesPerVial);
                    displayChildrenVialsUsed(currentBalanceVaccineUsed, vialsUsed, Integer.parseInt(wastedvials));

                    int receivedQuantity = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                    int usedQuantity = vialsUsed + Integer.parseInt(wastedvials);

                    if (balanceTextView != null
                            && !value.trim().equals("") && !value.trim().equals("0")
                            && (usedQuantity > receivedQuantity)) {

                        String errorMessage = getString(R.string.stock_quantity_error);
                        if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                            errorMessage = getString(R.string.stock_quantity_error_doses);
                        balanceTextView.addValidator(new StockQuantityValidator(errorMessage, receivedQuantity, usedQuantity));
                        balanceTextView.setError(errorMessage);
                    } else {
                        balanceTextView.setError("");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StringFormatInvalid")
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
        try {
            stockJsonFormFragment.getLabelViewFromTag("Doses_wasted", String.format(getString(R.string.total_wasted_doses), wastedDoses));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    @SuppressLint("StringFormatInvalid")
    public void refreshVialsBalance(String vaccineName, int newBalance) {
        try {
            if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                stockJsonFormFragment.getLabelViewFromTag("Vials_Balance", String.format(getString(R.string.new_vaccine_balance_doses), vaccineName, newBalance));
            else
                stockJsonFormFragment.getLabelViewFromTag("Vials_Balance", String.format(getString(R.string.new_vaccine_balance), vaccineName, newBalance));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    private void stockWastedVialsEnteredInIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_issued))) {
                StockRepository str = StockLibrary.getInstance().getStockRepository();
                if (key.equalsIgnoreCase("Vials_Wasted")) {
                    if (balanceTextView == null) {
                        ArrayList<View> views = new ArrayList<>(getFormDataViews());

                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText && ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                balanceTextView = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalanceVaccineUsed = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String vialsValue = "";
                    String wastedVials = value;
                    String vaccineName = object.getString("title").replace(getString(R.string.stock_issued), "").trim();
                    int existingBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());

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

                                existingBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                                StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
                                currentBalanceVaccineUsed = stockExternalRepository.getVaccinesUsedToday(encounterDate.getTime(), checkIfMeasles(vaccineName.toLowerCase()));
                            } else if (questions.getString("key").equalsIgnoreCase("Vials_Issued")) {
                                if (questions.has("value")) {
                                    if (!StringUtils.isBlank(questions.getString("value")) && StringUtils.isNumeric(questions.getString("value"))) {
                                        vialsValue = questions.getString("value");
                                    }
                                } else {
                                    vialsValue = "0";
                                }
                            }
                        }
                    }
                    refreshVialsBalance(vaccineName, existingBalance);
                    if (wastedVials == null || StringUtils.isBlank(wastedVials)) {
                        wastedVials = "0";
                    }
                    if (vialsValue != null && !StringUtils.isBlank(vialsValue) && StringUtils.isNumeric(wastedVials)) {
                        newBalance = existingBalance - Integer.parseInt(vialsValue) - Integer.parseInt(wastedVials);
                        refreshVialsBalance(vaccineName, newBalance);
                    } else {
                        refreshVialsBalance(vaccineName, existingBalance);
                    }
                    int vialsUsed = 0;
                    StockTypeRepository vaccine_typesRepository = StockLibrary.getInstance().getStockTypeRepository();
                    int dosesPerVial = vaccine_typesRepository.getDosesPerVial(vaccineName);
                    if (currentBalanceVaccineUsed % dosesPerVial == 0) {
                        vialsUsed = currentBalanceVaccineUsed / dosesPerVial;
                    } else if (currentBalanceVaccineUsed != 0) {
                        vialsUsed = (currentBalanceVaccineUsed / dosesPerVial) + 1;
                    }
                    displayChildrenVialsUsed(currentBalanceVaccineUsed, vialsUsed, Integer.parseInt(wastedVials));
                    refreshDosesWasted(balanceTextView, currentBalanceVaccineUsed, Integer.parseInt(wastedVials), dosesPerVial);

                    int receivedQuantity = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                    int usedQuantity = vialsUsed + Integer.parseInt(wastedVials);

                    MaterialEditText vialsWasted = null;
                    ArrayList<View> views = new ArrayList<>(getFormDataViews());

                    for (int i = 0; i < views.size(); i++) {
                        if (views.get(i) instanceof MaterialEditText && ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase("Vials_Wasted")) {
                            vialsWasted = (MaterialEditText) views.get(i);
                        }
                    }

                    if (vialsWasted != null
                            && !value.trim().equals("") && !value.trim().equals("0")
                            && (usedQuantity > receivedQuantity)) {

                        String errorMessage = getString(R.string.stock_quantity_error);
                        if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                            errorMessage = getString(R.string.stock_quantity_error_doses);
                        vialsWasted.addValidator(new StockQuantityValidator(errorMessage, str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime()), usedQuantity));
                        vialsWasted.setError(errorMessage);
                    } else {
                        vialsWasted.setError("");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void stockDateEnteredInReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_received))
                    && key.equalsIgnoreCase("Date_Stock_Received")
                    && value != null && !value.equalsIgnoreCase("")) {
                String label = "";
                int currentBalance = 0;
                int displaybalance = 0;
                String vialsvalue = "";
                String vaccineName = object.getString("title").replace(getString(R.string.stock_received), "").trim();
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
//                            if (balanceTextView != null) {
//                                balanceTextView.setErrorColor(getResources().getColor(R.color.dark_grey));
//                                balanceTextView.setError("New balance : " + displaybalance);
//                            }
                            try {
                                if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                                    stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance_doses), vaccineName, displaybalance));
                                else
                                    stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance), vaccineName, displaybalance));
                            } catch (Exception e) {
                                Timber.e(e, "Error formatting language string");
                            }
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

    private void stockVialsEnteredInReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_received))) {
                if (key.equalsIgnoreCase("Vials_Received") && value != null && !value.equalsIgnoreCase("")) {
//                    if(balancetextview == null) {
//                        ArrayList<View> views = new ArrayList<>(getFormDataViews());
//
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
                    String vaccineName = object.getString("title").replace(getString(R.string.stock_received), "").trim();
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
                                try {
                                    if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                                        stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance_doses), vaccineName, displaybalance));
                                    else
                                        stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance), vaccineName, displaybalance));
                                } catch (Exception e) {
                                    Timber.e(e, "Error formatting language string");
                                }
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

    private void stockDateEnteredInAdjustmentForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_loss)) &&
                    key.equalsIgnoreCase("Date_Stock_loss_adjustment") && value != null && !value.equalsIgnoreCase("")) {
                String label = "";
                int currentBalance = 0;
                int displaybalance = 0;
                String vialsvalue = "";
                String vaccineName = object.getString("title").replace(getString(R.string.stock_loss), "").trim();
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
                            if (balanceTextView != null && displaybalance < 0) {
                                balanceTextView.addValidator(negativeBalanceValidator);
                            } else if (balanceTextView != null && displaybalance >= 0) {
                                balanceTextView.getValidators().remove(negativeBalanceValidator);
                            }
                            try {
                                if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                                    stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance_doses), vaccineName, displaybalance));
                                else
                                    stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance), vaccineName, displaybalance));
                            } catch (Exception e) {
                                Timber.e(e, "Error formatting language string");
                            }
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

    private void stockVialsEnteredInAdjustmentForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains(getString(R.string.stock_loss))) {
                if (key.equalsIgnoreCase("Vials_Adjustment") && value != null && !value.equalsIgnoreCase("")) {
                    if (balanceTextView == null) {
                        ArrayList<View> views = new ArrayList<>(getFormDataViews());

                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText &&
                                    ((String) views.get(i).getTag(R.id.key)).equalsIgnoreCase(key)) {
                                balanceTextView = (MaterialEditText) views.get(i);
                            }
                        }
                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    String vaccineName = object.getString("title").replace(getString(R.string.stock_loss), "").trim();
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
                                if (balanceTextView != null && displaybalance < 0) {
                                    balanceTextView.addValidator(negativeBalanceValidator);
                                } else if (balanceTextView != null && displaybalance >= 0) {
                                    balanceTextView.getValidators().remove(negativeBalanceValidator);
                                }
                                try {
                                    if (StockLibrary.getInstance().useOnlyDosesForCalculation())
                                        stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance_doses), vaccineName, displaybalance));
                                    else
                                        stockJsonFormFragment.getLabelViewFromTag("Balance", String.format(getString(R.string.new_vaccine_balance), vaccineName, displaybalance));
                                } catch (Exception e) {
                                    Timber.e(e, "Error formatting language string");
                                }
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

    private String checkIfMeasles(String vaccineName) {
        if (vaccineName.equalsIgnoreCase("M/MR")) {
            return "measles";
        }
        return vaccineName;
    }

    public void addLabel(View view) {
        labels.add(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            confirmCloseTitle = getString(R.string.confirm_form_close);
            confirmCloseMessage = this.getString(R.string.confirm_form_close_explanation);
            setConfirmCloseTitle(confirmCloseTitle);
            setConfirmCloseMessage(confirmCloseMessage);
        } catch (Exception e) {
            Timber.e(e.toString());
        }
    }

    class StockQuantityValidator extends METValidator {

        private int receivedQuantity;
        private int usedQuantity;

        public StockQuantityValidator(@NonNull String errorMessage, int receivedQuantity, int usedQuantity) {
            super(errorMessage);
            this.receivedQuantity = receivedQuantity;
            this.usedQuantity = usedQuantity;
        }

        @Override
        public boolean isValid(@NonNull CharSequence charSequence, boolean b) {
            boolean isValid = true;

            if (StringUtils.isBlank(charSequence)) {
                return isValid;
            }

            if (usedQuantity > receivedQuantity) {
                isValid = false;
            }

            return isValid;
        }
    }
}
