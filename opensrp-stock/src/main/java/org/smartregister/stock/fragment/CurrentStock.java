package org.smartregister.stock.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.Context;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.activity.StockControlActivity;
import org.smartregister.stock.activity.StockJsonFormActivity;
import org.smartregister.stock.adapter.StockPaginatedCursorAdapter;
import org.smartregister.stock.domain.Stock;
import org.smartregister.stock.provider.StockRowSmartClientsProvider;
import org.smartregister.stock.repository.StockRepository;
import org.smartregister.stock.repository.StockTypeRepository;
import org.smartregister.util.FormUtils;
import org.smartregister.util.JsonFormUtils;

import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.text.MessageFormat.format;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentStock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentStock extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    ///////////////////////////////////////block for list///////////////////
    public static final String DIALOG_TAG = "dialog";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_CODE_GET_JSON = 3432;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOADER_ID = 0;
    private static int totalcount = 0;
    private static int currentlimit = 20;
    private static int currentoffset = 0;
    private final String filters = "";
    private final PaginationViewHandler paginationViewHandler = new PaginationViewHandler();
    private boolean refreshList;
    private ListView clientsView;
    private ProgressBar clientsProgressView;
    private String mainSelect;
    private String Sortqueries;
    private String tablename;
    private String countSelect;
    private StockRepository stockRepository;
    private StockPaginatedCursorAdapter clientAdapter;
    private View mView;
    private boolean isPaused;
    private TextView pageInfoView;
    private Button nextPageView;
    private Button previousPageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentStock.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentStock newInstance(String param1, String param2) {
        CurrentStock fragment = new CurrentStock();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String getTablename() {
        return tablename;
    }

    private void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public StockPaginatedCursorAdapter getClientsCursorAdapter() {
        return clientAdapter;
    }

    public void setClientsAdapter(StockPaginatedCursorAdapter clientsAdapter) {
        this.clientAdapter = clientsAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current__stock, container, false);
        mView = view;
        clientsProgressView = (ProgressBar) view.findViewById(R.id.client_list_progress);
        clientsView = (ListView) view.findViewById(R.id.list);
        paginationViewHandler.addPagination(clientsView);

        stockRepository = StockLibrary.getInstance().getStockRepository();

        Button received = (Button) view.findViewById(R.id.received);
        Button issued = (Button) view.findViewById(R.id.issued);
        Button adjustment = (Button) view.findViewById(R.id.loss_adj);

        TextView vaccine_name = (TextView) view.findViewById(R.id.name);
        vaccine_name.setText(((StockControlActivity) getActivity()).stockType.getName() + " Stock: ");

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchReceivedForm();
            }
        });
        issued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIssuedForm();
            }
        });
        adjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAdjustmentForm();
            }
        });

        onInitialization();
        refresh();
        return view;
    }

    private void getValueForStock(View view) {
        TextView stockvalue = (TextView) view.findViewById(R.id.vials);
        stockvalue.setText("" + stockRepository.getCurrentStockNumber(((StockControlActivity) getActivity()).stockType) + " vials");

    }


    private void onInitialization() {
        String tableName = StockRepository.stock_TABLE_NAME;
//        String parentTableName = "ec_mother";


        StockRowSmartClientsProvider hhscp = new StockRowSmartClientsProvider(getActivity(),
                stockRepository);
        clientAdapter = new StockPaginatedCursorAdapter(getActivity(), null, hhscp, stockRepository);
        clientsView.setAdapter(clientAdapter);
        clientAdapter.notifyDataSetChanged();
        setTablename(tableName);
        SmartRegisterQueryBuilder countqueryBUilder = new SmartRegisterQueryBuilder();
        countqueryBUilder.SelectInitiateMainTableCounts(tableName);
        countSelect = countqueryBUilder.mainCondition("stocks." + StockRepository.STOCK_TYPE_ID + " = " + ((StockControlActivity) getActivity()).stockType.getId());
        countExecute();
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.setSelectquery("Select * FROM stocks Where stocks." + StockRepository.STOCK_TYPE_ID + " = " + ((StockControlActivity) getActivity()).stockType.getId());
        queryBUilder.orderbyCondition(StockRepository.DATE_CREATED + " DESC, " + StockRepository.DATE_UPDATED + " DESC");
        mainSelect = queryBUilder.mainCondition("");
        Sortqueries = "";

        currentlimit = 20;
        currentoffset = 0;

        filterandSortInInitializeQueries();

        refresh();
    }

    private void launchReceivedForm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), StockJsonFormActivity.class);
        try {
            JSONObject form = FormUtils.getInstance(getActivity().getApplicationContext()).getFormJson("stock_received_form");
            String vaccine_name = ((StockControlActivity) getActivity()).stockType.getName();
            String formMetadata = form.toString().replace("[vaccine]", vaccine_name);
            intent.putExtra("json", formMetadata);
            startActivityForResult(intent, REQUEST_CODE_GET_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchAdjustmentForm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), StockJsonFormActivity.class);
        try {
            JSONObject form = FormUtils.getInstance(getActivity().getApplicationContext()).getFormJson("stock_adjustment_form");
            StockControlActivity activity = ((StockControlActivity) getActivity());
            String vaccine_name = activity.stockType.getName();
            String formmetadata = form.toString().replace("[vaccine]", vaccine_name);
            intent.putExtra("json", formmetadata);
            intent.putExtra("json", formmetadata);
            startActivityForResult(intent, REQUEST_CODE_GET_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchIssuedForm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), StockJsonFormActivity.class);
        try {
            StockTypeRepository vaccineTypeRepository = StockLibrary.getInstance().getStockTypeRepository();
            JSONObject form = FormUtils.getInstance(getActivity().getApplicationContext()).getFormJson("stock_issued_form");
            String vaccine_name = ((StockControlActivity) getActivity()).stockType.getName();
            String formMetadata = form.toString().replace("[vaccine]", vaccine_name);
            int dosesPerVial = vaccineTypeRepository.getDosesPerVial(vaccine_name);
            formMetadata = formMetadata.replace("[number_of_doses]", String.valueOf(dosesPerVial));
            intent.putExtra("json", formMetadata);
            startActivityForResult(intent, REQUEST_CODE_GET_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        countExecute();
        filterandSortInInitializeQueries();
        refresh();
        getValueForStock(mView);
    }

    private void returnfromform() {
        ((StockControlActivity) getActivity()).planningStockFragment.loadDataView(((StockControlActivity) getActivity()).planningStockFragment.mainview);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    String jsonString = data.getStringExtra("json");
                    Log.d("JSONResult", jsonString);
                    JSONObject jsonForm = new JSONObject(jsonString);
                    JSONObject step = jsonForm.getJSONObject("step1");
                    String FormTitle = step.getString("title");
                    if (FormTitle.contains("Stock Issued")) {
                        processStockIssued(jsonString);
                    }
                    if (FormTitle.contains("Stock Received")) {
                        processStockReceived(jsonString);
                    }
                    if (FormTitle.contains("Stock Loss/Adjustment")) {
                        processStockLossAdjustment(jsonString);
                    }

                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }

    private void processStockLossAdjustment(String jsonString) {
        JSONObject jsonForm = null;
        try {
            jsonForm = new JSONObject(jsonString);
            JSONArray fields = JsonFormUtils.fields(jsonForm);
            String Date_Stock_Received = JsonFormUtils.getFieldValue(fields, "Date_Stock_loss_adjustment");
            String Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Reason_for_adjustment");
            String vials_received = JsonFormUtils.getFieldValue(fields, "Vials_Adjustment");
            if (Received_Stock_From.equalsIgnoreCase("Other")) {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "adjusted_Stock_TO_Other");
            } else {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Reason_for_adjustment");
            }

            StockRepository str = StockLibrary.getInstance().getStockRepository();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);

            Date encounterDate = new Date();
            if (StringUtils.isNotBlank(Date_Stock_Received)) {
                Date dateTime = JsonFormUtils.formatDate(Date_Stock_Received, false);
                if (dateTime != null) {
                    encounterDate = dateTime;
                }
            }
            Stock stock = new Stock(null, Stock.loss_adjustment, allSharedPreferences.fetchRegisteredANM(), Integer.parseInt(vials_received), encounterDate.getTime(), Received_Stock_From, StockRepository.TYPE_Unsynced, System.currentTimeMillis(), "" + ((StockControlActivity) (getActivity())).stockType.getId());
            str.add(stock);
            returnfromform();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processStockReceived(String jsonString) {
        JSONObject jsonForm = null;
        try {
            jsonForm = new JSONObject(jsonString);
            JSONArray fields = JsonFormUtils.fields(jsonForm);
            String Date_Stock_Received = JsonFormUtils.getFieldValue(fields, "Date_Stock_Received");
            String Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Received_Stock_From");
            if (Received_Stock_From.equalsIgnoreCase("DHO")) {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Received_Stock_From");
            } else {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Received_Stock_From_Other");
            }
            String vials_received = JsonFormUtils.getFieldValue(fields, "Vials_Received");

            StockRepository str = StockLibrary.getInstance().getStockRepository();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);

            Date encounterDate = new Date();
            if (StringUtils.isNotBlank(Date_Stock_Received)) {
                Date dateTime = JsonFormUtils.formatDate(Date_Stock_Received, false);
                if (dateTime != null) {
                    encounterDate = dateTime;
                }
            }
            Stock stock = new Stock(null, Stock.received, allSharedPreferences.fetchRegisteredANM(), Integer.parseInt(vials_received), encounterDate.getTime(), Received_Stock_From, StockRepository.TYPE_Unsynced, System.currentTimeMillis(), "" + ((StockControlActivity) (getActivity())).stockType.getId());
            str.add(stock);
            returnfromform();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processStockIssued(String jsonString) {
        JSONObject jsonForm = null;
        try {
            jsonForm = new JSONObject(jsonString);
            JSONArray fields = JsonFormUtils.fields(jsonForm);
            String Date_Stock_Received = JsonFormUtils.getFieldValue(fields, "Date_Stock_Issued");
            String Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Issued_Stock_To");
            if (Received_Stock_From.equalsIgnoreCase("Other")) {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Issued_Stock_TO_Other");
            } else {
                Received_Stock_From = JsonFormUtils.getFieldValue(fields, "Issued_Stock_To");
            }
            String vials_received = JsonFormUtils.getFieldValue(fields, "Vials_Issued");
            String vials_wasted = JsonFormUtils.getFieldValue(fields, "Vials_Wasted");
            if (StringUtils.isNotBlank(vials_wasted)) {
                vials_received = "" + (Integer.parseInt(vials_received) + Integer.parseInt(vials_wasted));
            }

            StockRepository str = StockLibrary.getInstance().getStockRepository();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);

            Date encounterDate = new Date();
            if (StringUtils.isNotBlank(Date_Stock_Received)) {
                Date dateTime = JsonFormUtils.formatDate(Date_Stock_Received, false);
                if (dateTime != null) {
                    encounterDate = dateTime;
                }
            }
            Stock stock = new Stock(null, Stock.issued, allSharedPreferences.fetchRegisteredANM(), -1 * Integer.parseInt(vials_received), encounterDate.getTime(), Received_Stock_From, StockRepository.TYPE_Unsynced, System.currentTimeMillis(), "" + ((StockControlActivity) (getActivity())).stockType.getId());
            str.add(stock);
            returnfromform();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentPageCount() {
        if (currentoffset != 0) {
            if ((currentoffset / currentlimit) != 0) {
                return (currentoffset / currentlimit) + 1;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    private int getTotalcount() {
        if (totalcount % currentlimit == 0) {
            return totalcount / currentlimit;
        } else {
            return (totalcount / currentlimit) + 1;
        }
    }

    private void refresh() {
        pageInfoView.setText(
                format(getResources().getString(R.string.str_page_info),
                        getCurrentPageCount(),
                        getTotalcount()));
        if (hasNextPage()) {
            paginationViewHandler.getPaginationView().setVisibility(VISIBLE);
            pageInfoView.setVisibility(VISIBLE);
            nextPageView.setVisibility(VISIBLE);
        } else {
            paginationViewHandler.getPaginationView().setVisibility(INVISIBLE);
            pageInfoView.setVisibility(INVISIBLE);
            nextPageView.setVisibility(INVISIBLE);
        }
        if (hasPreviousPage())
            previousPageView.setVisibility(VISIBLE);
        else
            previousPageView.setVisibility(INVISIBLE);
    }

    private boolean hasNextPage() {

        return totalcount > (currentoffset + currentlimit);
    }

    private boolean hasPreviousPage() {
        return currentoffset != 0;
    }

    private void gotoNextPage() {
        if (currentoffset + currentlimit < totalcount) {
            currentoffset = currentoffset + currentlimit;
            filterandSortExecute();
        }
    }

    private void goBackToPreviousPage() {
        if (currentoffset > 0) {
            currentoffset = currentoffset - currentlimit;
            filterandSortExecute();
        }
    }

    private void filterandSortInInitializeQueries() {
        if (isPausedOrRefreshList()) {
            this.showProgressView();
            this.filterandSortExecute();
        } else {
            this.initialFilterandSortExecute();
        }
    }


    private void initialFilterandSortExecute() {
        Loader<Cursor> loader = getLoaderManager().getLoader(LOADER_ID);
        showProgressView();
        if (loader != null) {
            filterandSortExecute();
        } else {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    private void filterandSortExecute() {
        refresh();

        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void showProgressView() {
        if (clientsProgressView.getVisibility() == INVISIBLE) {
            clientsProgressView.setVisibility(VISIBLE);
        }

        if (clientsView.getVisibility() == VISIBLE) {
            clientsView.setVisibility(INVISIBLE);
        }
    }

    private void hideProgressView() {
        if (clientsProgressView.getVisibility() == VISIBLE) {
            clientsProgressView.setVisibility(INVISIBLE);
        }
        if (clientsView.getVisibility() == INVISIBLE) {
            clientsView.setVisibility(VISIBLE);
        }
    }

    private String filterandSortQuery() {
        SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder(mainSelect);

        String query = "";
        try {
            sqb.addCondition(filters);
            query = sqb.orderbyCondition(Sortqueries);
            query = sqb.Endquery(sqb.addlimitandOffset(query, currentlimit, currentoffset));

        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString(), e);
        }

        return query;
    }

    private void countExecute() {
        Cursor c = null;

        try {
            SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder(countSelect);
            String query = "";

            sqb.addCondition(filters);
            query = sqb.orderbyCondition(Sortqueries);
            query = sqb.Endquery(query);

            Log.i(getClass().getName(), query);
            c = StockLibrary.getInstance().getRepository().getReadableDatabase().rawQuery(query, null);
//            c = commonRepository().rawCustomQueryForAdapter(query);
            c.moveToFirst();
            totalcount = c.getInt(0);
            Log.v("total count here", "" + totalcount);
            currentlimit = 20;
            currentoffset = 0;

        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        switch (id) {
            case LOADER_ID:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity()) {
                    @Override
                    public Cursor loadInBackground() {
                        String query = filterandSortQuery();
                        Cursor cursor = StockLibrary.getInstance().getRepository().getReadableDatabase().rawQuery(query, null);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressView();
                            }
                        });

                        return cursor;
                    }
                };
            default:
                // An invalid id was passed in
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        clientAdapter.swapCursor(cursor);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        clientAdapter.swapCursor(null);
    }

    private boolean isPausedOrRefreshList() {
        return isPaused() || isRefreshList();
    }

    private boolean isRefreshList() {
        return refreshList;
    }

    private void setRefreshList(boolean refreshList) {
        this.refreshList = refreshList;
    }

    protected Context context() {
        return StockLibrary.getInstance().getContext();
    }

    private boolean isPaused() {
        return isPaused;
    }

    ////////////////////////////////////////////////////////////////
    // Inner classes
    ////////////////////////////////////////////////////////////////
    private class PaginationViewHandler implements View.OnClickListener {


        private void addPagination(ListView clientsView) {
            LinearLayout toaddNote = new LinearLayout(getActivity());
            toaddNote.setOrientation(LinearLayout.VERTICAL);
            ViewGroup footerView = getPaginationView();
            nextPageView = (Button) footerView.findViewById(R.id.btn_next_page);
            previousPageView = (Button) footerView.findViewById(R.id.btn_previous_page);
            pageInfoView = (TextView) footerView.findViewById(R.id.txt_page_info);

            nextPageView.setOnClickListener(this);
            previousPageView.setOnClickListener(this);

            footerView.setLayoutParams(new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT,
                    (int) getResources().getDimension(R.dimen.pagination_bar_height)));
            toaddNote.addView(footerView);


            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            toaddNote.addView(getActivity().getLayoutInflater().inflate(R.layout.current_stock_note, null), p);

            clientsView.addFooterView(toaddNote);
            refresh();
        }

        private ViewGroup getPaginationView() {

            return (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.smart_register_pagination, null);
        }

        public void refreshListView() {
            setRefreshList(true);
            setRefreshList(false);
        }


        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.btn_next_page) {
                gotoNextPage();

            } else if (i == R.id.btn_previous_page) {
                goBackToPreviousPage();

            }
        }

    }
}