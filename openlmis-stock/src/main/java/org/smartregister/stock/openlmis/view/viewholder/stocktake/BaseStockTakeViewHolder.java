package org.smartregister.stock.openlmis.view.viewholder.stocktake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.StockTake;
import org.smartregister.stock.openlmis.domain.openlmis.Reason;
import org.smartregister.stock.openlmis.listener.StockTakeListener;

import java.util.List;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.StockStatus.VVM1;
import static org.smartregister.stock.openlmis.util.OpenLMISConstants.StockStatus.VVM2;

/**
 * Created by samuelgithengi on 10/2/18.
 */
public class BaseStockTakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "StockTakeLotViewHolder";

    protected Context context;

    private TextInputEditText stockOnHandTextView;

    private TextInputEditText physicalCountTextView;

    private TextInputEditText statusTextView;

    private TextInputEditText differenceTextView;

    private TextInputLayout difference;

    private TextInputEditText reasonTextView;

    private TextInputLayout reason;

    private TextView noChangeButton;

    private TextView addStockButton;

    private TextView subtractStockButton;

    protected int stockOnHand;

    private int physicalCount;

    private List<Reason> stockAdjustReasons;

    private StockTakeListener stockTakeListener;

    protected StockTake stockTake;

    public BaseStockTakeViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        stockOnHandTextView = itemView.findViewById(R.id.stock_on_hand_textview);
        physicalCountTextView = itemView.findViewById(R.id.quantity_textview);
        statusTextView = itemView.findViewById(R.id.status_textview);
        differenceTextView = itemView.findViewById(R.id.adjustment_textview);
        difference = itemView.findViewById(R.id.adjustment);
        reasonTextView = itemView.findViewById(R.id.reason_textview);
        reason = itemView.findViewById(R.id.reason);
        noChangeButton = itemView.findViewById(R.id.no_change);
        addStockButton = itemView.findViewById(R.id.add_stock);
        subtractStockButton = itemView.findViewById(R.id.subtract_stock);

        addStockButton.setOnClickListener(this);
        subtractStockButton.setOnClickListener(this);
        noChangeButton.setOnClickListener(this);
        statusTextView.setOnClickListener(this);
        reasonTextView.setOnClickListener(this);

        physicalCountTextView.addTextChangedListener(new PhysicalCountTextWatcher());

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
        stockTake.setStatus(status);
    }

    public void setDifference(int difference) {
        stockTake.setQuantity(difference);
        if (difference != 0)
            displayDifferenceAndReason();
        if (difference > 0)
            differenceTextView.setText("+".concat(String.valueOf(difference)));
        else
            differenceTextView.setText(String.valueOf(difference));
    }

    public void setReason(String reason) {
        reasonTextView.setText(reason);
        stockTake.setReasonId(reason);
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

    public void activateNoChange(boolean activate) {
        noChangeButton.setSelected(activate);
        toggleInputs(!activate);
        if (activate) {
            noChangeButton.setTextColor(context.getResources().getColor(R.color.white));
            stockTake.setNoChange(true);
            setPhysicalCount(stockOnHand);
            setReason("");
            setStatus("");
            hideDifferenceAndReason();
        } else {
            noChangeButton.setTextColor(context.getResources().getColor(R.color.add_subtract));
            stockTake.setNoChange(false);
        }
    }

    private void toggleInputs(boolean isEnabled) {
        physicalCountTextView.setEnabled(isEnabled);
        statusTextView.setEnabled(isEnabled);
        reasonTextView.setEnabled(isEnabled);
        addStockButton.setEnabled(isEnabled);
        subtractStockButton.setEnabled(isEnabled);
    }

    private void hideDifferenceAndReason() {
        difference.setVisibility(View.GONE);
        reason.setVisibility(View.GONE);
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
            activateNoChange(!noChangeButton.isSelected());
        }
        validateData();

    }

    private void validateData() {
        if (stockTake.isNoChange()) {
            stockTake.setValid(true);
        } else if (physicalCount < 0 || StringUtils.isBlank(differenceTextView.getText()) ||
                StringUtils.isBlank(reasonTextView.getText()) ||
                StringUtils.isBlank(statusTextView.getText())) {
            stockTake.setValid(false);
        } else {
            stockTake.setValid(true);

        }
        stockTakeListener.registerStockTake(stockTake);
    }

    public void setStockAdjustReasons(List<Reason> stockAdjustReasons) {
        this.stockAdjustReasons = stockAdjustReasons;
    }

    public void setStockTakeListener(StockTakeListener stockTakeListener) {
        this.stockTakeListener = stockTakeListener;
    }

    public StockTake getStockTake() {
        return stockTake;
    }

    public void setStockTake(StockTake stockTake) {
        this.stockTake = stockTake;
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
                try {
                    physicalCount = Integer.valueOf(editable.toString());
                    physicalCountTextView.setError(null);
                } catch (NumberFormatException e) {
                    Log.e(TAG, context.getString(R.string.physical_count_invalid), e);
                    physicalCountTextView.setError(context.getString(R.string.physical_count_invalid));
                    return;
                }
            }
            setDifference(physicalCount - stockOnHand);
            validateData();
        }
    }
}