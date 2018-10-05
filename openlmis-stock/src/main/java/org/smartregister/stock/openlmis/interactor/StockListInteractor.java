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
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.smartregister.stock.openlmis.util.OpenLMISConstants.EXPIRING_MONTHS_WARNING;


/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListInteractor extends StockListBaseInteractor {

    private ProgramRepository programRepository;

    private StockRepository stockRepository;

    private SearchRepository searchRepository;


    public StockListInteractor() {
        this(new ProgramRepository(OpenLMISLibrary.getInstance().getRepository()),
                new CommodityTypeRepository(OpenLMISLibrary.getInstance().getRepository()),
                new TradeItemRepository(OpenLMISLibrary.getInstance().getRepository()),
                new StockRepository(OpenLMISLibrary.getInstance().getRepository()),
                new SearchRepository(OpenLMISLibrary.getInstance().getRepository()),
                new ProgramOrderableRepository(OpenLMISLibrary.getInstance().getRepository()));
    }

    @VisibleForTesting
    protected StockListInteractor(ProgramRepository programRepository,
                                  CommodityTypeRepository commodityTypeRepository,
                                  TradeItemRepository tradeItemRepository,
                                  StockRepository stockRepository,
                                  SearchRepository searchRepository, ProgramOrderableRepository programOrderableRepository) {
        super(commodityTypeRepository, programOrderableRepository, tradeItemRepository, searchRepository);
        this.programRepository = programRepository;
        this.stockRepository = stockRepository;
    }

    public List<Program> getPrograms() {
        return programRepository.findAllPrograms();
    }

    public List<CommodityType> getCommodityTypes() {
        return commodityTypeRepository.findAllCommodityTypes();
    }

    public List<TradeItemWrapper> getTradeItems(String programId, CommodityType commodityType) {
        return populateTradeItemWrapper(programId,getTradeItemsByCommodityType(commodityType.getId()));
    }

    public List<TradeItemWrapper> findTradeItemsByIds(String programId, Set<String> tradeItemIds) {
        return populateTradeItemWrapper(programId, tradeItemRepository.getTradeItemByIds(tradeItemIds));
    }

    private List<TradeItemWrapper> populateTradeItemWrapper(String programId, List<TradeItem> tradeItems) {
        List<TradeItemWrapper> tradeItemWrappers = new ArrayList<>();
        List<String> tradeItemsWithLotsIds = new ArrayList<>();
        List<String> tradeItemsWithoutLotsIds = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            if (tradeItem.isHasLots())
                tradeItemsWithLotsIds.add(tradeItem.getId());
            else
                tradeItemsWithoutLotsIds.add(tradeItem.getId());
        }
        Map<String, List<LotDetailsDto>> lotsListMap = stockRepository.getNumberOfLotsByTradeItem(programId,tradeItemsWithLotsIds);
        Map<String, Integer> nonLotManagedBalances = stockRepository.getTotalStockByTradeItems(programId,tradeItemsWithoutLotsIds);

        for (TradeItem tradeItem : tradeItems) {
            TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
            if (tradeItem.isHasLots()) {
                List<LotDetailsDto> lots = lotsListMap.get(tradeItem.getId());
                int totalStock = 0;
                if (lots != null) {
                    for (LotDetailsDto lotDetailsDto : lots)
                        totalStock += lotDetailsDto.getTotalStock();
                    tradeItemWrapper.setNumberOfLots(lots.size());
                    LocalDate maxExpiringDate = new LocalDate(lots.iterator().next().getMinimumExpiryDate());
                    tradeItemWrapper.setHasLotExpiring(new LocalDate().plusMonths(EXPIRING_MONTHS_WARNING).isAfter(maxExpiringDate));
                }
                tradeItemWrapper.setTotalStock(totalStock);
                tradeItemWrapper.setHasLots(tradeItem.isHasLots());

            } else {
                if (nonLotManagedBalances.containsKey(tradeItem.getId()))
                    tradeItemWrapper.setTotalStock(nonLotManagedBalances.get(tradeItem.getId()));
            }
            tradeItemWrappers.add(tradeItemWrapper);
        }
        return tradeItemWrappers;
    }

}
