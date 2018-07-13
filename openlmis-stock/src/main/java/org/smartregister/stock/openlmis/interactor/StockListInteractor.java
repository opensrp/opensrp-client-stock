package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.domain.CommodityType;
import org.smartregister.stock.openlmis.domain.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by samuelgithengi on 7/13/18.
 */
public class StockListInteractor {

    public List<Program> getPrograms() {
        List<Program> programs = new ArrayList<>();
        Program program = new Program();
        program.setName("Essential Drugs");
        programs.add(program);
        return programs;
    }

    public List<CommodityType> getCommodityTypes() {
        List<CommodityType> commodityTypes = new ArrayList<>();
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "BCG", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "OPV", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "Penta", "", null, null, System.currentTimeMillis()));
        commodityTypes.add(new CommodityType(UUID.randomUUID(), "PC2", "", null, null, System.currentTimeMillis()));
        return commodityTypes;
    }
}
