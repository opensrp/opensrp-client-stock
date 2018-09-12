package org.smartregister.stock.openlmis.interactor;

import android.support.annotation.VisibleForTesting;

import org.joda.time.LocalDate;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.dto.LotDetailsDto;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.EXPIRING_MONTHS_WARNING;


/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListInteractor {

    private ProgramRepository programRepository;

    private CommodityTypeRepository commodityTypeRepository;

    private TradeItemRepository tradeItemRepository;

    private StockRepository stockRepository;

    private SearchRepository searchRepository;

    public StockListInteractor() {
        this(new ProgramRepository(OpenLMISLibrary.getInstance().getRepository()), new CommodityTypeRepository(OpenLMISLibrary.getInstance().getRepository()), new TradeItemRepository(OpenLMISLibrary.getInstance().getRepository()), new StockRepository(OpenLMISLibrary.getInstance().getRepository()), new SearchRepository(OpenLMISLibrary.getInstance().getRepository()));
    }

    @VisibleForTesting
    protected StockListInteractor(ProgramRepository programRepository, CommodityTypeRepository commodityTypeRepository, TradeItemRepository tradeItemRepository, StockRepository stockRepository, SearchRepository searchRepository) {
        this.programRepository = programRepository;
        this.commodityTypeRepository = commodityTypeRepository;
        this.tradeItemRepository = tradeItemRepository;
        this.stockRepository = stockRepository;
        this.searchRepository = searchRepository;
    }

    public List<String> getPrograms() {

        List<Program> programList = programRepository.findAllPrograms();
        List<String> programs = new ArrayList<>();
        for (Program program : programList)
            programs.add(program.getName());
        return programs;
    }

    public List<CommodityType> getCommodityTypes() {
        return commodityTypeRepository.findAllCommodityTypes();
    }

    public List<TradeItemWrapper> getTradeItems(CommodityType commodityType) {
        return populateTradeItemWrapper(tradeItemRepository.getTradeItemByCommodityType(commodityType.getId().toString()));
    }


    public List<CommodityType> findCommodityTypesByIds(Set<String> ids) {
        return commodityTypeRepository.findCommodityTypesByIds(ids);
    }

    public Map<String, List<String>> searchIds(String searchPhrase) {
        return searchRepository.searchIds(searchPhrase);
    }

    public List<TradeItemWrapper> findTradeItemsByIds(List<String> tradeItemIds) {
        return populateTradeItemWrapper(tradeItemRepository.getTradeItemByIds(new HashSet<String>(tradeItemIds)));
    }

    private List<TradeItemWrapper> populateTradeItemWrapper(List<TradeItem> tradeItems) {
        List<TradeItemWrapper> tradeItemWrappers = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
            List<LotDetailsDto> lots = stockRepository.getNumberOfLotsByTradeItem(tradeItem.getId());
            int totalStock = 0;
            for (LotDetailsDto lotDetailsDto : lots)
                totalStock += lotDetailsDto.getTotalStock();
            tradeItemWrapper.setTotalStock(totalStock);
            tradeItemWrapper.setNumberOfLots(lots.size());
            if (!lots.isEmpty()) {
                LocalDate maxExpiringDate = new LocalDate(lots.iterator().next().getMinimumExpiryDate());
                tradeItemWrapper.setHasLotExpiring(new LocalDate().plusMonths(EXPIRING_MONTHS_WARNING).isAfter(maxExpiringDate));
            }
            tradeItemWrappers.add(tradeItemWrapper);
        }
        return tradeItemWrappers;
    }
}
