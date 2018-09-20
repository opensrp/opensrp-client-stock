package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.interactor.StockListBaseInteractor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public abstract class StockListBasePresenter {

    protected abstract StockListBaseInteractor getStockListInteractor();

    public Map<String, Set<String>> searchIdsByPrograms(String programId) {
        return getStockListInteractor().searchIdsByPrograms(programId);
    }

    public List<CommodityType> findCommodityTypesByIds(Set<String> ids) {
        return getStockListInteractor().findCommodityTypesByIds(ids);
    }
}
