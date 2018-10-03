package org.smartregister.stock.openlmis.presenter;

import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.interactor.StockListBaseInteractor;

import java.util.HashMap;
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

    public List<TradeItem> findTradeItemsByCommodityType(String commodityTypeId) {
        return getStockListInteractor().getTradeItemsByCommodityType(commodityTypeId);
    }

    public Map<String, Set<String>> searchIds(String searchPhrase) {
        return getStockListInteractor().searchIds(searchPhrase);
    }

    public Map<String, Set<String>> filterValidPrograms(Map<String, Set<String>> programIds,
                                                        Map<String, Set<String>> searchedIds) {
        if (programIds == null)
            return searchedIds;
        Map<String, Set<String>> filteredIds = new HashMap<>();
        for (String key : searchedIds.keySet()) {
            if (programIds.containsKey(key)) {
                Set<String> tradeItems = searchedIds.get(key);
                tradeItems.retainAll(programIds.get(key));
                filteredIds.put(key, tradeItems);
            }
        }
        return filteredIds;
    }
}
