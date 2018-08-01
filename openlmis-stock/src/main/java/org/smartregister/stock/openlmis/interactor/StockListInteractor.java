package org.smartregister.stock.openlmis.interactor;

import org.joda.time.LocalDate;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.repository.StockRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListInteractor {

    private ProgramRepository programRepository;

    private CommodityTypeRepository commodityTypeRepository;

    private TradeItemRepository tradeItemRepository;

    private StockRepository stockRepository;

    public StockListInteractor() {

        programRepository = new ProgramRepository(OpenLMISLibrary.getInstance().getRepository());
        commodityTypeRepository = new CommodityTypeRepository(OpenLMISLibrary.getInstance().getRepository());
        tradeItemRepository = new TradeItemRepository(OpenLMISLibrary.getInstance().getRepository());
        stockRepository = new StockRepository(OpenLMISLibrary.getInstance().getRepository());
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

    public List<TradeItem> getTradeItems(CommodityType commodityType) {
        List<TradeItem> tradeItems = tradeItemRepository.getTradeItemByCommodityType(commodityType.getId().toString());
        for (TradeItem tradeItem : tradeItems) {
            Map<Long, Integer> lots = stockRepository.getNumberOfLotsByTradeItem(tradeItem.getId());
            int totalStock = 0;
            for (int stock : lots.values())
                totalStock += stock;
            tradeItem.setTotalStock(totalStock);
            tradeItem.setNumberOfLots(lots.size());
            if (!lots.isEmpty()) {
                LocalDate maxExpiringDate = new LocalDate(lots.keySet().iterator().next());
                tradeItem.setHasLotExpiring(new LocalDate().plusMonths(3).isAfter(maxExpiringDate));
            }
        }
        return tradeItems;

    }


}
