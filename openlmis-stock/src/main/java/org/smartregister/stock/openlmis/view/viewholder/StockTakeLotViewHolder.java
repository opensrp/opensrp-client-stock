package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.openlmis.Lot;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.listener.StockTakeListener;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.util.List;

import static org.smartregister.stock.openlmis.adapter.LotAdapter.DATE_FORMAT;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.StockStatus.VVM1;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.StockStatus.VVM2;

/**
 * Created by samuelgithengi on 9/21/18.
 */
public class StockTakeLotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;

    private TextView lotCodeAndExpiryTextView;

    private TextInputEditText stockOnHandTextView;

    private TextInputEditText physicalCountTextView;

    private TextInputEditText statusTextView;

    private TextInputEditText differenceTextView;

    private TextInputLayout difference;

    private TextInputEditText reasonTextView;

    private TextInputLayout reason;

    private TextView noChangeTextView;

    private int stockOnHand;

    private int physicalCount;

    private List<Reason> stockAdjustReasons;

    private StockTakeListener stockTakeListener;

    private LotDto lotDto = new LotDto();

    public StockTakeLotViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        lotCodeAndExpiryTextView = itemView.findViewById(R.id.lot_code);
        stockOnHandTextView = itemView.findViewById(R.id.stock_on_hand_textview);
        physicalCountTextView = itemView.findViewById(R.id.quantity_textview);
        statusTextView = itemView.findViewById(R.id.status_textview);
        differenceTextView = itemView.findViewById(R.id.adjustment_textview);
        difference = itemView.findViewById(R.id.adjustment);
        reasonTextView = itemView.findViewById(R.id.reason_textview);
        reason = itemView.findViewById(R.id.reason);
        noChangeTextView = itemView.findViewById(R.id.no_change);
        itemView.findViewById(R.id.add_stock).setOnClickListener(this);
        itemView.findViewById(R.id.subtract_stock).setOnClickListener(this);
        itemView.findViewById(R.id.no_change).setOnClickListener(this);
        statusTextView.setOnClickListener(this);
        reasonTextView.setOnClickListener(this);

        physicalCountTextView.addTextChangedListener(new PhysicalCountTextWatcher());

    }

    public void setLot(Lot lot) {
        lotCodeAndExpiryTextView.setText(context.getString(R.string.stock_take_lot,
                lot.getLotCode(), new DateTime(lot.getExpirationDate()).toString(DATE_FORMAT)));
        lotDto.setLotId(lot.getId());
    }

    public void setStockOnHand(int stockOnHand) {
        this.stockOnHand = stockOnHand;
        stockOnHandTextView.setText(String.valueOf(stockOnHand));
    }

    public void setPhysicalCount(int physicalCount) {
        this.physicalCount = physicalCount;
        physicalCountTextView.setText(String.valueOf(physicalCount));
        if (physicalCount < 0)
            physicalCountTextView.setError(context.getString(R.string.negative_balance));
        else {
            physicalCountTextView.setError(null);
        }
    }

    public void setStatus(String status) {
        statusTextView.setText(status);
        lotDto.setLotStatus(status);
    }

    public void setDifference(int difference) {
        lotDto.setQuantity(difference);
        if (difference != 0)
            displayDifferenceAndReason();
        if (difference > 0)
            differenceTextView.setText("+".concat(String.valueOf(difference)));
        else
            differenceTextView.setText(String.valueOf(difference));
    }

    public void setReason(String reason) {
        reasonTextView.setText(reason);
        lotDto.setReason(reason);
    }

    private void displayDifferenceAndReason() {
        difference.setVisibility(View.VISIBLE);
        reason.setVisibility(View.VISIBLE);
    }

    private void addOrSubtractStock(boolean isAdd) {
        physicalCount += isAdd ? 1 : -1;
        setDifference(physicalCount - stockOnHand);
        setPhysicalCount(physicalCount);
    }

    private void showStatusDropdown() {
        PopupMenu popupMenu = new PopupMenu(context, statusTextView);
        popupMenu.getMenu().add(VVM1);
        popupMenu.getMenu().add(VVM2);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setStatus(item.getTitle().toString());
                validateData();
                return true;
            }
        });
        popupMenu.show();
    }

    private void showReasonsDropdown() {
        PopupMenu popupMenu = new PopupMenu(context, reasonTextView);
        for (Reason reason : stockAdjustReasons) {
            popupMenu.getMenu().add(reason.getStockCardLineItemReason().getName());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setReason(item.getTitle().toString());
                validateData();
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.subtract_stock || view.getId() == R.id.add_stock) {
            addOrSubtractStock(view.getId() == R.id.add_stock);
        } else if (view.getId() == R.id.reason_textview) {
            showReasonsDropdown();
        } else if (view.getId() == R.id.status_textview) {
            showStatusDropdown();
        } else if (view.getId() == R.id.no_change) {
            boolean selected = noChangeTextView.isSelected();
            noChangeTextView.setSelected(!selected);
            if (selected) {
                noChangeTextView.setTextColor(context.getResources().getColor(R.color.add_subtract));
            } else {
                noChangeTextView.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
        validateData();

    }

    public void setStockAdjustReasons(List<Reason> stockAdjustReasons) {
        this.stockAdjustReasons = stockAdjustReasons;
    }

    public void setStockTakeListener(StockTakeListener stockTakeListener) {
        this.stockTakeListener = stockTakeListener;
    }

    private void validateData() {
        if (noChangeTextView.isSelected()) {
            lotDto.setValid(true);
        } else if (physicalCount < 0 || StringUtils.isBlank(differenceTextView.getText()) ||
                StringUtils.isBlank(reasonTextView.getText()) ||
                StringUtils.isBlank(statusTextView.getText())) {
            lotDto.setValid(false);
        } else {
            lotDto.setValid(true);

        }
        stockTakeListener.registerLotDetails(lotDto);
    }

    private class PhysicalCountTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {//do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {//do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (StringUtils.isBlank(editable.toString())) {
                physicalCount = 0;
            } else {
                physicalCount = Integer.valueOf(editable.toString());
            }
            setDifference(physicalCount - stockOnHand);
            validateData();
        }
    }
}