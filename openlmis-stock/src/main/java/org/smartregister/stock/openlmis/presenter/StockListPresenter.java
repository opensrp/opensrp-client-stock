package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.domain.Program;
import org.smartregister.stock.openlmis.interactor.StockListInteractor;

import java.util.List;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListPresenter {

    private StockListInteractor stockListInteractor;

    public StockListPresenter(StockListInteractor stockListInteractor) {
        this.stockListInteractor = stockListInteractor;
    }

    public List<Program> getPrograms() {
        return stockListInteractor.getPrograms();
    }

    public List<CommodityType> getCommodityTypes() {
        return stockListInteractor.getCommodityTypes();
    }
}
