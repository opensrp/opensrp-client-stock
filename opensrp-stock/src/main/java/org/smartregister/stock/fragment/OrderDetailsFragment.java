package org.smartregister.stock.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smartregister.stock.R;
import org.smartregister.stock.domain.OrderShipment;
import org.smartregister.stock.domain.ShipmentLineItem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ORDER_SHIPMENT_JSON = "ORDER SHIPMENT JSON";

    // TODO: Rename and change types of parameters
    private String orderShipmentJSON;
    private OrderShipment orderShipment;

    private OnFragmentInteractionListener mListener;

    private TextView orderCodeTv;
    private TextView orderedDateTv;
    private TextView shippedDateTv;
    private TextView supplyingFacilityNameTv;
    private TextView receivingFacilityNameTv;
    private TextView processingPeriodStartTv;
    private TextView processingPeriodEndTv;

    private TableLayout stockItemsTl;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderShipmentJSON Parameter 1.
     * @return A new instance of fragment OrderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance(String orderShipmentJSON) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_SHIPMENT_JSON, orderShipmentJSON);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            orderShipmentJSON = getArguments().getString(ORDER_SHIPMENT_JSON);
            orderShipment = new Gson()
                    .fromJson(orderShipmentJSON, OrderShipment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_order_details, container, false);

        initialiseViews(mainView);
        populateViews();

        return mainView;
    }

    private void initialiseViews(View mainView) {
        orderCodeTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_orderCode);
        orderedDateTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_orderedDate);
        shippedDateTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_shippedDate);
        supplyingFacilityNameTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_supplyingFacilityName);
        receivingFacilityNameTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_receivingFacilityName);
        processingPeriodStartTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_processingPeriodStartDate);
        processingPeriodEndTv = (TextView) mainView.findViewById(R.id.tv_orderDetails_processingPeriodEndDate);
        stockItemsTl = (TableLayout) mainView.findViewById(R.id.tl_orderDetails_tableLayout);
    }

    private void populateViews() {
        orderCodeTv.setText(orderShipment.getOrder().getId());
        orderedDateTv.setText("Ordered: " + getUserFriendlyDate(orderShipment.getOrder().getDateCreatedByClient()));

        if (orderShipment.getShipment() != null) {
            shippedDateTv.setText("Shipped: " + getUserFriendlyDate(orderShipment.getShipment().getServerVersion()));
            supplyingFacilityNameTv.setText("From: " + orderShipment.getShipment().getSupplyingFacilityName());
            receivingFacilityNameTv.setText("To: " + orderShipment.getShipment().getReceivingFacilityName());
            processingPeriodStartTv.setText("Processing Period Start Date: " + getUserFriendlyDate(orderShipment.getShipment().getProcessingPeriodStartDate()));
            processingPeriodEndTv.setText("Processing Period End Date: " + getUserFriendlyDate(orderShipment.getShipment().getProcessingPeriodEndDate()));

            ShipmentLineItem[] shipmentLineItems = orderShipment.getShipment().getShipmentLineItems();

            for(ShipmentLineItem shipmentLineItem: shipmentLineItems) {
                //Todo: Add colouring
                stockItemsTl.addView(createTableRow(shipmentLineItem));
            }
        }
    }

    private TableRow createTableRow(ShipmentLineItem shipmentLineItem) {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tableRow.addView(createTextViewCell(shipmentLineItem.getAntigenType()));
        tableRow.addView(createTextViewCell(shipmentLineItem.getOrderedQuantity() + ""));
        tableRow.addView(createTextViewCell(shipmentLineItem.getShippedQuantity() + ""));
        tableRow.addView(createTextViewCell(shipmentLineItem.getNumberOfDoses() + ""));

        return tableRow;
    }

    private TextView createTextViewCell(String text) {
        TextView cellTv = new TextView(context);
        cellTv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cellTv.setText(text);

        return cellTv;
    }

    private String getUserFriendlyDate(long dateLong) {
        return getUserFriendlyDate(new Date(dateLong));
    }

    private String getUserFriendlyDate(Date date) {
        return dateFormat.format(date);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
