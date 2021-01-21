package org.smartregister.stock.repository.dao;


import org.apache.commons.lang3.NotImplementedException;
import org.smartregister.converters.StockConverter;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.pathevaluator.dao.StockDao;
import org.smartregister.stock.repository.StockRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockDaoImpl extends StockRepository implements StockDao {

    @Override
    public List<com.ibm.fhir.model.resource.Bundle> findInventoryItemsInAJurisdiction(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public List<com.ibm.fhir.model.resource.Bundle> findInventoryInAServicePoint(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public List<com.ibm.fhir.model.resource.Bundle> getStockById(String s) {
        StockAndProductDetails stockAndProductDetails = findStockWithStockTypeByStockId(s);
        if (stockAndProductDetails != null) {
            return Collections.singletonList(StockConverter.convertStockToBundleResource(stockAndProductDetails));
        }
        return new ArrayList<>();
    }
}
