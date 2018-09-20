package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.ProgramOrderableRepository;

/**
 * Created by samuelgithengi on 9/20/18.
 */
public class StockTakeInteractor extends StockListBaseInteractor {

    public StockTakeInteractor() {
        this(new CommodityTypeRepository(OpenLMISLibrary.getInstance().getRepository()),
                new ProgramOrderableRepository(OpenLMISLibrary.getInstance().getRepository()));
    }

    protected StockTakeInteractor(CommodityTypeRepository commodityTypeRepository, ProgramOrderableRepository programOrderableRepository) {
        super(commodityTypeRepository, programOrderableRepository);
    }
}
