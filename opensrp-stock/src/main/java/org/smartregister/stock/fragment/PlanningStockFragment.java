package org.smartregister.stock.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.repository.Repository;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.activity.StockControlActivity;
import org.smartregister.stock.domain.ActiveChildrenStats;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.repository.StockExternalRepository;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.util.StockUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlanningStockFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlanningStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanningStockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    public View mainview;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanningStockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanningStockFragment newInstance(String param1, String param2) {
        PlanningStockFragment fragment = new PlanningStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planning_stock_fragment, container, false);
        mainview = view;
//        creatgraphview(mainview);
        loatDataView(mainview);
        return view;
    }

    private void creatgraphview(View view) {
        DateTime now = new DateTime(System.currentTimeMillis());

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        ViewGroup graphparent = (ViewGroup) graph.getParent();
        final int index = graphparent.indexOfChild(graph);
        graphparent.removeView(graph);
        graph = new GraphView(getActivity());
        graph.setId(R.id.graph);

        graphparent.addView(graph, index);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        String[] montharry;
        if (now.minusMonths(1).monthOfYear() != now.monthOfYear()) {
            montharry = new String[]{now.minusMonths(3).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(2).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(1).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(0).monthOfYear().getAsShortText() + " " + now.year().getAsShortText()
            };
        } else {
            montharry = new String[]{now.minusMonths(3).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(2).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(1).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),
                    now.minusMonths(0).monthOfYear().getAsShortText() + " " + now.year().getAsShortText(),

            };
        }
        staticLabelsFormatter.setHorizontalLabels(montharry);
//        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        DateFormat month_date = new SimpleDateFormat("MMM yyyy");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), month_date));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.removeAllSeries();
        graph.getViewport().setMinX(now.minusMonths(3).toDate().getTime());
        graph.getViewport().setMaxX(now.toDate().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    public void loatDataView(View view) {
        createTitle(view);
        createActiveChildrenStatsView(view);
        creatgraphview(view);
        createGraphDataAndView(view);
        //createStockInfoForlastThreeMonths(view);
        getValueForStock(view);
        getLastThreeMonthStockIssued(view);
        wasteRateCalculate(view);
        vaccinesDueNextMonth(view);
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    private void vaccinesDueNextMonth(View view) {
        int dosesPerVial = ((StockControlActivity) getActivity()).stockType.getQuantity();
        try {
            ((TextView) view.findViewById(R.id.due_vacc_next_month_value)).setText(String.format(getString(R.string.vials_formatted), (int) Math.ceil((double) processVaccinesDueNextMonth() / dosesPerVial)));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    private void wasteRateCalculate(View view) {
        double wastepercent = 0.0;
        StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
        int vaccineGiven = stockExternalRepository.getVaccinesUsedUntilDate(System.currentTimeMillis(), ((StockControlActivity) getActivity()).stockType.getName().toLowerCase().trim());
        int vaccineIssued = -1 * getStockIssuedIntimeFrame(DateTime.now().yearOfEra().withMinimumValue(), DateTime.now()) * (((StockControlActivity) getActivity()).stockType.getQuantity());
        if (vaccineGiven == 0 || vaccineGiven > vaccineIssued) {
            wastepercent = 0.0;
        } else {
            wastepercent = (1 - ((double) vaccineGiven / vaccineIssued)) * 100;
        }
        DecimalFormat df = new DecimalFormat("####0");
        ((TextView) view.findViewById(R.id.avg_vacc_waste_rate_value)).setText("" + df.format(Math.ceil(wastepercent)) + "%");
    }

    @SuppressLint("StringFormatMatches")
    private void getLastThreeMonthStockIssued(View view) {
        TextView lastMonthLabel = (TextView) view.findViewById(R.id.month1);
        TextView lastMonthVialsUsed = (TextView) view.findViewById(R.id.month1vials);
        TextView secondLastMonthLabel = (TextView) view.findViewById(R.id.month2);
        TextView secondLastMonthVialsUsed = (TextView) view.findViewById(R.id.month2vials);
        TextView thirdMonthLabel = (TextView) view.findViewById(R.id.month3);
        TextView thirdMonthVialsUsed = (TextView) view.findViewById(R.id.month3vials);
        TextView threeMonthAverage = (TextView) view.findViewById(R.id.month3average);

        DateTime today = new DateTime(System.currentTimeMillis());
        DateTime startOfThisMonth = today.dayOfMonth().withMinimumValue();

        //////////////////////last month///////////////////////////////////////////////////////////
        DateTime startOfLastMonth = today.minusMonths(1).dayOfMonth().withMinimumValue();
        String lastMonth = startOfLastMonth.monthOfYear().getAsShortText();
        String lastMonthYear = startOfLastMonth.year().getAsShortText();

        int stockIssuedLastMonth = -1 * getStockIssuedIntimeFrame(startOfLastMonth, startOfThisMonth);

        lastMonthLabel.setText(lastMonth + " " + lastMonthYear);
        try {
            lastMonthVialsUsed.setText(String.format(getString(R.string.vials_formatted), stockIssuedLastMonth));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
        //////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////2nd last month///////////////////////////////////////////////////////////
        DateTime startOf2ndLastMonth = startOfLastMonth.minusDays(1).dayOfMonth().withMinimumValue();
        String secondLastMonth = startOf2ndLastMonth.monthOfYear().getAsShortText();
        String secondLastMonthYear = startOf2ndLastMonth.year().getAsShortText();

        int stockIssued2ndLastMonth = -1 * getStockIssuedIntimeFrame(startOf2ndLastMonth, startOfLastMonth);

        secondLastMonthLabel.setText(secondLastMonth + " " + secondLastMonthYear);
        try {
            secondLastMonthVialsUsed.setText(String.format(getString(R.string.vials_formatted), stockIssued2ndLastMonth));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
        //////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////3rd last month///////////////////////////////////////////////////////////
        DateTime startOf3rdLastMonth = startOf2ndLastMonth.minusDays(1).dayOfMonth().withMinimumValue();
        String thirdLastMonth = startOf3rdLastMonth.monthOfYear().getAsShortText();
        String thirdLastMonthYear = startOf3rdLastMonth.year().getAsShortText();
        int stockIssued3rdLastMonth = -1 * getStockIssuedIntimeFrame(startOf3rdLastMonth, startOf2ndLastMonth);

        thirdMonthLabel.setText(thirdLastMonth + " " + thirdLastMonthYear);
        try {
            thirdMonthVialsUsed.setText(String.format(getString(R.string.vials_formatted), stockIssued3rdLastMonth));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
        //////////////////////////////////////////////////////////////////////////////////////////

        int threeMonthAverageValue = (int) Math.ceil((double) (stockIssuedLastMonth + stockIssued2ndLastMonth + stockIssued3rdLastMonth) / 3);
        try {
            threeMonthAverage.setText(String.format(getString(R.string.vials_formatted), threeMonthAverageValue));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    private int getStockIssuedIntimeFrame(DateTime startofLastMonth, DateTime startofthismonth) {
        int sum = 0;
        Repository repo = StockLibrary.getInstance().getRepository();
        net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();

        Cursor c = db.rawQuery("Select sum(value) from stocks where " + StockRepository.DATE_CREATED + " >= " + startofLastMonth.toDate().getTime() + " and " +
                StockRepository.DATE_CREATED + " < " + startofthismonth.toDate().getTime() + " and " +
                StockRepository.TRANSACTION_TYPE + " = '" + Stock.issued + "' and " +
                StockRepository.STOCK_TYPE_ID + " = " + ((StockControlActivity) getActivity()).stockType.getId(), null);
        String stockvalue = "0";
        if (c.getCount() > 0) {
            c.moveToFirst();
            if (c.getString(0) != null && !StringUtils.isBlank(c.getString(0)))
                stockvalue = c.getString(0);
            c.close();
        } else {
            c.close();
        }
        sum = sum + Integer.parseInt(stockvalue);
        return sum;
    }

    @SuppressLint("StringFormatMatches")
    private void getValueForStock(View view) {
        TextView stockValue = (TextView) view.findViewById(R.id.vials);
        try {
            stockValue.setText(String.format(getString(R.string.vials_formatted), StockLibrary.getInstance().getStockRepository().getCurrentStockNumber(((StockControlActivity) getActivity()).stockType)));
        } catch (Exception e) {
            Timber.e(e, "Error formatting language string");
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void createTitle(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.name);
        TextView graphTitleText = (TextView) view.findViewById(R.id.graph_label_text);
        TextView currentStockLabel = (TextView) view.findViewById(R.id.current_stock);
        TextView avgVaccineWasteRateLabel = (TextView) view.findViewById(R.id.avg_vacc_waste_rate_label);
        TextView dueVaccineDescription = (TextView) view.findViewById(R.id.due_vacc_description);
        TextView lastThreeMonthStockTitle = (TextView) view.findViewById(R.id.last_three_months_stock_title);

        String vaccineName = ((StockControlActivity) getActivity()).stockType.getName();

        try {
            titleView.setText(String.format(getString(R.string.stock_planning_title), vaccineName));
            graphTitleText.setText(String.format(getString(R.string.vaccine_stock_levels), vaccineName));
            currentStockLabel.setText(String.format(getString(R.string.current_vaccine_stock), vaccineName));
            avgVaccineWasteRateLabel.setText(String.format(getString(R.string.average_vaccine_waste_rate), vaccineName));
            dueVaccineDescription.setText(String.format(getString(R.string.vaccine_due_next_month_text), vaccineName));
            lastThreeMonthStockTitle.setText(String.format(getString(R.string.vaccine_stock_used), vaccineName));

            DateTime NextMonth = new DateTime(System.currentTimeMillis()).plusMonths(1);
            ((TextView) view.findViewById(R.id.due_vacc_next_month_label)).setText(
                    String.format(getString(R.string.vaccine_due_next_month), vaccineName, NextMonth.monthOfYear().getAsShortText(), NextMonth.year().getAsShortText()));
        } catch (Exception e) {
            Timber.e(e, "Error while formatting language strings");
        }
    }

    private LineGraphSeries<DataPoint> createGraphDataAndView(View view) {
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime threeMonthEarlierIterator = now.minusMonths(3).withTimeAtStartOfDay();
        ArrayList<DataPoint> dataPointsForGraphs = new ArrayList<>();
        while (threeMonthEarlierIterator.isBefore(now)) {
            Repository repo = StockLibrary.getInstance().getRepository();
            net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();

            Cursor c = db.rawQuery("Select sum(value) from stocks where " + StockRepository.DATE_CREATED + " <= " + threeMonthEarlierIterator.toDate().getTime() + " and " + StockRepository.STOCK_TYPE_ID + " = " + ((StockControlActivity) getActivity()).stockType.getId(), null);
            String stockvalue = "0";
            if (c.getCount() > 0) {
                c.moveToFirst();
                if (c.getString(0) != null && !StringUtils.isBlank(c.getString(0)))
                    stockvalue = c.getString(0);
                c.close();
            } else {
                c.close();
            }
            dataPointsForGraphs.add(new DataPoint(threeMonthEarlierIterator.toDate(), Double.parseDouble(stockvalue)));
            threeMonthEarlierIterator = threeMonthEarlierIterator.plusDays(1);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                dataPointsForGraphs.toArray(new DataPoint[dataPointsForGraphs.size()])
        );
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        graph.removeAllSeries();
        series.setThickness(3);
        series.setColor(getResources().getColor(R.color.bluetext));
        graph.addSeries(series);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(series.getHighestValueY());
        return series;
    }

    private int processVaccinesDueNextMonth() {
        int vaccinesDueNextMonth = 0;
        String vaccinename = ((StockControlActivity) getActivity()).stockType.getName();
        if (vaccinename.equalsIgnoreCase("M/MR")) {
            vaccinename = "Measles / MR";
        }
        ArrayList<JSONObject> vaccineArray = readvaccineFileAndReturnVaccinesofSameType(vaccinename);
        for (int i = 0; i < vaccineArray.size(); i++) {
            vaccinesDueNextMonth = vaccinesDueNextMonth + StockLibrary.getInstance()
                    .getStockExternalRepository().getVaccinesDueBasedOnSchedule(vaccineArray.get(i));
        }
        return vaccinesDueNextMonth;
    }

    private ArrayList<JSONObject> readvaccineFileAndReturnVaccinesofSameType(String vaccinetypename) {
        ArrayList<JSONObject> vaccinesOfSameType = new ArrayList<>();
        String vaccineJsonString = StockUtils.getSupportedVaccines(getActivity());
        try {
            JSONArray vaccineEntry = new JSONArray(vaccineJsonString);

            for (int i = 0; i < vaccineEntry.length(); i++) {
                JSONObject objectatindex = vaccineEntry.getJSONObject(i);
                if (objectatindex.has("vaccines")) {
                    JSONArray vaccinearray = objectatindex.getJSONArray("vaccines");
                    for (int j = 0; j < vaccinearray.length(); j++) {
                        if (vaccinearray.getJSONObject(j).has("type") && vaccinearray.getJSONObject(j).getString("type").equalsIgnoreCase(vaccinetypename)) {
                            vaccinesOfSameType.add(vaccinearray.getJSONObject(j));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vaccinesOfSameType;
    }

    private void createActiveChildrenStatsView(View view) {
        TextView zerotoelevenlastmonth = (TextView) view.findViewById(R.id.zerotoelevenlastmonth);
        TextView zerotoeleventhismonth = (TextView) view.findViewById(R.id.zerotoeleventhismonth);
        TextView twelvetofiftyninethismonth = (TextView) view.findViewById(R.id.twelvetofiftyninethismonth);
        TextView twelvetofiftyninelastmont = (TextView) view.findViewById(R.id.twelvetofiftyninelastmont);
        TextView twelvetofiftyninedifference = (TextView) view.findViewById(R.id.twelvetofiftyninedifference);
        TextView zerotoelevendifference = (TextView) view.findViewById(R.id.zerotoelevendifference);
        TextView last_month_total = (TextView) view.findViewById(R.id.last_month_total);
        TextView this_month_total = (TextView) view.findViewById(R.id.this_month_total);
        TextView difference_total = (TextView) view.findViewById(R.id.difference_total);
        String stringDifference0to11 = "";
        String stringDifference12to59 = "";
        StockExternalRepository stockExternalRepository = StockLibrary.getInstance().getStockExternalRepository();
        ActiveChildrenStats activeChildrenStats = stockExternalRepository.getActiveChildrenStat();

        zerotoelevenlastmonth.setText("" + activeChildrenStats.getChildrenLastMonthZeroToEleven());
        zerotoeleventhismonth.setText("" + activeChildrenStats.getChildrenThisMonthZeroToEleven());
        twelvetofiftyninelastmont.setText("" + activeChildrenStats.getChildrenLastMonthtwelveTofiftyNine());
        twelvetofiftyninethismonth.setText("" + activeChildrenStats.getChildrenThisMonthtwelveTofiftyNine());

        this_month_total.setText("" + (activeChildrenStats.getChildrenThisMonthtwelveTofiftyNine() + activeChildrenStats.getChildrenThisMonthZeroToEleven()));
        last_month_total.setText("" + (activeChildrenStats.getChildrenLastMonthtwelveTofiftyNine() + activeChildrenStats.getChildrenLastMonthZeroToEleven()));

        Long difference0to11 = activeChildrenStats.getChildrenLastMonthZeroToEleven() - activeChildrenStats.getChildrenThisMonthZeroToEleven();
        if (difference0to11 < 0) {
            stringDifference0to11 = "" + difference0to11;
        } else {
            stringDifference0to11 = "+" + difference0to11;
        }
        Long difference12to59 = activeChildrenStats.getChildrenLastMonthtwelveTofiftyNine() - activeChildrenStats.getChildrenThisMonthtwelveTofiftyNine();
        if (difference12to59 < 0) {
            stringDifference12to59 = "" + difference12to59;
        } else {
            stringDifference12to59 = "+" + difference12to59;
        }

        twelvetofiftyninedifference.setText(stringDifference12to59);
        zerotoelevendifference.setText(stringDifference0to11);
        if ((difference0to11 + difference12to59) < 0) {
            difference_total.setText("" + (difference0to11 + difference12to59));
        } else {
            difference_total.setText("+" + (difference0to11 + difference12to59));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    public int getDueVaccinesForNextMonthBasedOnDependantVaccineGiven(String dependentvaccinename ,long days ){
//        int toreturn = 0;
//        PathRepository repo = (PathRepository) VaccinatorApplication.getInstance().getRepository();
//        net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();
//        DateTime today = new DateTime(System.currentTimeMillis());
//        DateTime startofthismonth = today.dayOfMonth().withMinimumValue();
//
//        //////////////////////next month///////////////////////////////////////////////////////////
//        DateTime startofNextMonth = today.plusMonths(1).dayOfMonth().withMinimumValue();
//        DateTime EndofNextMonth = today.plusMonths(1).dayOfMonth().withMaximumValue();
//
//        Cursor c = db.rawQuery("Select Count (*) from vaccines where ("+StockRepository.DATE_CREATED+" + "+days +") => "+startofNextMonth.toDate().getTime()+"and ("+StockRepository.DATE_CREATED+" + "+days +") <= "+startofNextMonth.toDate().getTime()+ " and name ='"+dependentvaccinename+"'",null);
//        if(c.getColumnCount()>0){
//            c.moveToFirst();
//            toreturn = Integer.parseInt(c.getString(0));
//            c.close();
//        }
//        return toreturn;
//    }
//    public int getDueVaccinesForNextMonthBasedOnBirth(String dependentvaccinename ,long days,long expiry ){
//        int toreturn = 0;
//        PathRepository repo = (PathRepository) VaccinatorApplication.getInstance().getRepository();
//        net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();
//        DateTime today = new DateTime(System.currentTimeMillis());
//        DateTime startofthismonth = today.dayOfMonth().withMinimumValue();
//
//
//        //////////////////////next month///////////////////////////////////////////////////////////
//        DateTime startofNextMonth = today.plusMonths(1).dayOfMonth().withMinimumValue();
//        DateTime EndofNextMonth = today.plusMonths(1).dayOfMonth().withMaximumValue();
//
//        DateTime startofNextMonthlowerlimit = startofNextMonth.minus(days);
//        DateTime EndofNextMonthlowerlimit = EndofNextMonth.minus(days);
//        DateTime startofNextMonthbeforeExpiry = startofNextMonth.minus(expiry);
////        select * from ec_child where ec_child.dob  < ('2012-02-27T05:00:00.000' + '6 years') and ec_child.base_entity_id not in (Select base_entity_id from vaccines where name = 'penta_1')
//        Cursor c = db.rawQuery("Select Count (*) from ec_child where ec_child.dob <= '"+startofNextMonthlowerlimit+"' and ec_child.dob >= '"+EndofNextMonthlowerlimit+"'  and ec_child.dob >= '"+startofNextMonthbeforeExpiry+"' and ec_child.base_entity_id not in (Select base_entity_id from vaccines where name = '"+dependentvaccinename+"'",null);
////        Cursor c = db.rawQuery("Select Count (*) from vaccines where ("+StockRepository.DATE_CREATED+" + "+days +") => "+startofNextMonth+"and ("+StockRepository.DATE_CREATED+" + "+days +") <= "+startofNextMonth+ " and name ='"+dependentvaccinename+"'",null);
//        if(c.getColumnCount()>0){
//            c.moveToFirst();
//            toreturn = Integer.parseInt(c.getString(0));
//            c.close();
//        }
//        c.close();
//        return toreturn;
//    }
}
