package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.domain.Program;
import org.smartregister.stock.openlmis.domain.TradeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListInteractor {

    public List<String> getPrograms() {
        List<String> programs = new ArrayList<>();
        Program program = new Program();
        program.setName("Essential Drugs");
        programs.add(program.getName());

        program = new Program();
        program.setName("Malaria Drugs");
        programs.add(program.getName());
        return programs;
    }

    public List<CommodityType> getCommodityTypes() {
        List<CommodityType> commodityTypes = new ArrayList<>();
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "BCG", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "OPV", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "Penta", "", null, null, System.currentTimeMillis()));
         commodityTypes.add(new CommodityType(UUID.randomUUID(), "PC2", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "C1", "", null, null, System.currentTimeMillis()));

        return commodityTypes;
    }

    public List<TradeItem> getTradeItems(CommodityType commodityType) {
        List<TradeItem> tradeItems = new ArrayList<>();
        if (commodityType.getName().equals("C1"))
            return tradeItems;
        TradeItem tradeItem = new TradeItem(UUID.randomUUID());
        tradeItem.setName("Intervax " + commodityType.getName() + " 20");
        tradeItem.setDateUpdated(System.currentTimeMillis());

        tradeItems.add(tradeItem);


        tradeItem = new TradeItem(UUID.randomUUID());
        tradeItem.setName("BIntervax " + commodityType.getName() + " 30");
        tradeItem.setDateUpdated(System.currentTimeMillis());

        tradeItems.add(tradeItem);
        if (commodityType.getName().equals("Penta"))
            return tradeItems;

        tradeItem = new TradeItem(UUID.randomUUID());
        tradeItem.setName("Brand B " + commodityType.getName() + " 5");
        tradeItem.setDateUpdated(System.currentTimeMillis());

        tradeItems.add(tradeItem);

        tradeItem = new TradeItem(UUID.randomUUID());
        tradeItem.setName("Antervax " + commodityType.getName() + " 5");
        tradeItem.setDateUpdated(System.currentTimeMillis());

        tradeItems.add(tradeItem);
        return tradeItems;
    }
}
