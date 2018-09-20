package org.smartregister.stock.openlmis.interactor;

import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
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

    protected StockListBaseInteractor(CommodityTypeRepository commodityTypeRepository, ProgramOrderableRepository programOrderableRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
        this.programOrderableRepository = programOrderableRepository;
    }

    public List<CommodityType> findCommodityTypesByIds(Set<String> ids) {
        return commodityTypeRepository.findCommodityTypesByIds(ids);
    }

    public Map<String, Set<String>> searchIdsByPrograms(String programId) {
        return programOrderableRepository.searchIdsByPrograms(programId);
    }
}
