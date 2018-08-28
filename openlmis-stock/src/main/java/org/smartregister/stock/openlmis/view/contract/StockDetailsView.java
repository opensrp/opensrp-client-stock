package org.smartregister.stock.openlmis.view.contract;

import android.content.Context;

/**
 * Created by samuelgithengi on 8/1/18.
 */
public interface StockDetailsView {

    void showLotsHeader();

    void showTransactionsHeader();

    void collapseLots();

    void expandLots();

    Context getContext();
}
