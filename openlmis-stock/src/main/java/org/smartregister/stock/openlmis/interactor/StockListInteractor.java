package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Program;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;

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
        commodityTypes.add(new CommodityType(UUID.randomUUID().toString(), "BCG", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID().toString(), "OPV", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID().toString(), "Penta", "", null, null, System.currentTimeMillis()));
         commodityTypes.add(new CommodityType(UUID.randomUUID().toString(), "PC2", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID().toString(), "C1", "", null, null, System.currentTimeMillis()));

        return commodityTypes;
    }

    public List<TradeItem> getTradeItems(CommodityType commodityType) {
        List<TradeItem> tradeItems = new ArrayList<>();
        if (commodityType.getName().equals("C1"))
            return tradeItems;
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setManufacturerOfTradeItem("Intervax " + commodityType.getName() + " 20");

        tradeItems.add(tradeItem);


        tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setManufacturerOfTradeItem("BIntervax " + commodityType.getName() + " 30");

        tradeItems.add(tradeItem);
        if (commodityType.getName().equals("Penta"))
            return tradeItems;

        tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setManufacturerOfTradeItem("Brand B " + commodityType.getName() + " 5");

        tradeItems.add(tradeItem);

        tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setManufacturerOfTradeItem("Antervax " + commodityType.getName() + " 5");

        tradeItems.add(tradeItem);
        return tradeItems;
    }
}
