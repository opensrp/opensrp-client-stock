package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public abstract class StockListBaseInteractor {

    protected CommodityTypeRepository commodityTypeRepository;

    protected ProgramOrderableRepository programOrderableRepository;

    protected TradeItemRepository tradeItemRepository;

    protected SearchRepository searchRepository;

    protected StockListBaseInteractor(CommodityTypeRepository commodityTypeRepository,
                                      ProgramOrderableRepository programOrderableRepository,
                                      TradeItemRepository tradeItemRepository,
                                      SearchRepository searchRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
        this.programOrderableRepository = programOrderableRepository;
        this.tradeItemRepository = tradeItemRepository;
        this.searchRepository = searchRepository;
    }

    public List<CommodityType> findCommodityTypesByIds(Set<String> ids) {
        return commodityTypeRepository.findCommodityTypesByIds(ids);
    }

    public Map<String, Set<String>> searchIdsByPrograms(String programId) {
        return programOrderableRepository.searchIdsByPrograms(programId);
    }

    public List<TradeItem> getTradeItemsByCommodityType(String commodityTypeId) {
        return tradeItemRepository.getTradeItemByCommodityType(commodityTypeId);
    }


    public Map<String, Set<String>> searchIds(String searchPhrase) {
        return searchRepository.searchIds(searchPhrase);
    }
}
