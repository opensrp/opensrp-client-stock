package org.smartregister.stock.openlmis.util;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;

import java.util.List;

public class SynchronizedUpdater {

    private static SynchronizedUpdater synchronizedUpdater;
    private static DispensableRepository dispensableRepository =  OpenLMISLibrary.getInstance().getDispensableRepository();
    private static TradeItemRepository tradeItemRegisterRepository = OpenLMISLibrary.getInstance().getTradeItemRegisterRepository();
    private static OrderableRepository orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
    private static org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRepository();

    private SynchronizedUpdater() {}

    public static SynchronizedUpdater getInstance() {
        if (synchronizedUpdater == null) {
            synchronizedUpdater = new SynchronizedUpdater();
            return synchronizedUpdater;
        }
        return synchronizedUpdater;
    }

    public synchronized void updateInfo(Object entity) {
        if (entity instanceof Orderable) {
            updateInformation((Orderable) entity);
        } else if (entity instanceof Dispensable) {
            updateInformation((Dispensable) entity);
        } else if (entity instanceof CommodityType) {
            updateInformation((CommodityType) entity);
        }
    }


    public void updateInformation(Orderable orderable) {

        org.smartregister.stock.openlmis.domain.TradeItem registerTradeItem = tradeItemRegisterRepository.getTradeItemById(orderable.getTradeItemId());
        registerTradeItem = registerTradeItem == null ? new org.smartregister.stock.openlmis.domain.TradeItem(orderable.getTradeItemId()) : registerTradeItem;
        registerTradeItem.setNetContent(orderable.getNetContent());
        registerTradeItem.setName(orderable.getFullProductName());
        Dispensable dispensable = dispensableRepository.findDispensable(orderable.getDispensableId());
        if (dispensable != null) {
            registerTradeItem.setDispensable(dispensable);
        }
        tradeItemRegisterRepository.addOrUpdate(registerTradeItem);
    }

    public void updateInformation(Dispensable dispensable) {

        if (dispensableRepository.findDispensable(dispensable.getId()) == null) {
            List<Orderable> orderables = orderableRepository.findOrderables(null, null, null, null, dispensable.getId(), null, null);
            for (Orderable orderable : orderables) {
                org.smartregister.stock.openlmis.domain.TradeItem tradeItem = tradeItemRegisterRepository.getTradeItemById(orderable.getTradeItemId());
                if (tradeItem != null) {
                    tradeItem.setDispensable(dispensable);
                    tradeItemRegisterRepository.addOrUpdate(tradeItem);
                }
            }
        }
    }

    public void updateInformation(CommodityType commodityType) {

        for (TradeItem tradeItem : commodityType.getTradeItems()) {
            // update trade item register repository
            org.smartregister.stock.openlmis.domain.TradeItem tradeItemRegister = tradeItemRegisterRepository.getTradeItemById(tradeItem.getId());
            tradeItemRegister = tradeItemRegister == null ? new org.smartregister.stock.openlmis.domain.TradeItem(tradeItem.getId()) : tradeItemRegister;
            tradeItemRegister.setCommodityTypeId(commodityType.getId());
            tradeItemRegisterRepository.addOrUpdate(tradeItemRegister);
        }
    }
}
