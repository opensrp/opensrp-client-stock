package org.smartregister.stock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.smartregister.stock.R;
import org.smartregister.stock.StockLibrary;
import org.smartregister.stock.domain.StockType;
import org.smartregister.stock.repository.StockRepository;

import static org.smartregister.stock.util.Constants.ARG_STOCK_TYPE;

/**
 * Created by samuelgithengi on 2/19/18.
 */

public class StockGridAdapter extends BaseAdapter {

    private final Context context;
    private final StockType[] stockTypes;
    private Class controlActivity;

    public StockGridAdapter(Context context, StockType[] stockTypes, Class controlActivity) {
        this.context = context;
        this.stockTypes = stockTypes;
        this.controlActivity = controlActivity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.stock_grid_block, null);

            // set value into textview
            TextView name = (TextView) gridView.findViewById(R.id.vaccine_type_name);
            TextView doses = (TextView) gridView.findViewById(R.id.doses);
            TextView vials = (TextView) gridView.findViewById(R.id.vials);

            // set image based on selected text
            final StockType stockType = stockTypes[position];
            StockRepository stockRepository = StockLibrary.getInstance().getStockRepository();
            int currentvials = stockRepository.getBalanceFromNameAndDate(stockType.getName(), System.currentTimeMillis());

            name.setText(stockType.getName());
            doses.setText("" + currentvials * stockType.getQuantity() + " doses");
            vials.setText("" + currentvials + " vials");

            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, controlActivity);
                    intent.putExtra(ARG_STOCK_TYPE, stockType);
                    context.startActivity(intent);
                }
            });
        } else {
            gridView = convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return stockTypes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
