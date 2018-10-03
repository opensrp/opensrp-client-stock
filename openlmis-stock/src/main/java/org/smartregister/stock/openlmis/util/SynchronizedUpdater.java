package org.smartregister.stock.openlmis.util;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.domain.openlmis.CommodityType;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.domain.openlmis.Orderable;
import org.smartregister.stock.openlmis.domain.openlmis.TradeItem;
import org.smartregister.stock.openlmis.repository.SearchRepository;
import org.smartregister.stock.openlmis.repository.TradeItemRepository;
import org.smartregister.stock.openlmis.repository.openlmis.CommodityTypeRepository;
import org.smartregister.stock.openlmis.repository.openlmis.DispensableRepository;
import org.smartregister.stock.openlmis.repository.openlmis.OrderableRepository;

import java.util.Collections;
import java.util.List;

public class SynchronizedUpdater {

    private static SynchronizedUpdater synchronizedUpdater;
    private static DispensableRepository dispensableRepository = OpenLMISLibrary.getInstance().getDispensableRepository();
    private static TradeItemRepository tradeItemRegisterRepository = OpenLMISLibrary.getInstance().getTradeItemRegisterRepository();
    private static OrderableRepository orderableRepository = OpenLMISLibrary.getInstance().getOrderableRepository();
    private static org.smartregister.stock.openlmis.repository.openlmis.TradeItemRepository tradeItemRepository = OpenLMISLibrary.getInstance().getTradeItemRepository();
    private SearchRepository searchRepository = OpenLMISLibrary.getInstance().getSearchRepository();
    private CommodityTypeRepository commodityTypeRepository = OpenLMISLibrary.getInstance().getCommodityTypeRepository();

    private SynchronizedUpdater() {
    }

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

        if (orderable.getCommodityTypeId() != null) {
            List<org.smartregister.stock.openlmis.domain.TradeItem> registerTradeItems = tradeItemRegisterRepository.getTradeItemByCommodityType(orderable.getCommodityTypeId());
            for (org.smartregister.stock.openlmis.domain.TradeItem registerTradeItem : registerTradeItems) {
                if (registerTradeItem.getName() == null) {
                    Dispensable dispensable = dispensableRepository.findDispensable(orderable.getDispensableId());
                    if (dispensable != null) {
                        registerTradeItem.setDispensable(dispensable);
                    }
                    registerTradeItem.setName(orderable.getFullProductName());
                    registerTradeItem.setUseVvm(orderable.getUseVvm());
                    registerTradeItem.setHasLots(orderable.getHasLots());
                    tradeItemRegisterRepository.addOrUpdate(registerTradeItem);
                }
            }
            if (StringUtils.isNotBlank(orderable.getCommodityTypeId()))
                searchRepository.addOrUpdate(commodityTypeRepository.findCommodityTypeById(
                        orderable.getCommodityTypeId()), registerTradeItems);
            return;
        }

        org.smartregister.stock.openlmis.domain.TradeItem registerTradeItem = tradeItemRegisterRepository.getTradeItemById(orderable.getTradeItemId());
        registerTradeItem = registerTradeItem == null ? new org.smartregister.stock.openlmis.domain.TradeItem(orderable.getTradeItemId()) : registerTradeItem;
        registerTradeItem.setNetContent(orderable.getNetContent());
        registerTradeItem.setName(orderable.getFullProductName());
        registerTradeItem.setUseVvm(orderable.getUseVvm());
        registerTradeItem.setHasLots(orderable.getHasLots());
        Dispensable dispensable = dispensableRepository.findDispensable(orderable.getDispensableId());
        if (dispensable != null) {
            registerTradeItem.setDispensable(dispensable);
        }
        tradeItemRegisterRepository.addOrUpdate(registerTradeItem);
        if (StringUtils.isNotBlank(registerTradeItem.getCommodityTypeId()))
            searchRepository.addOrUpdate(commodityTypeRepository.findCommodityTypeById(
                    registerTradeItem.getCommodityTypeId()), Collections.singletonList(registerTradeItem));

    }

    public void updateInformation(Dispensable dispensable) {
        List<Orderable> orderables = orderableRepository.findOrderables(null, null, null, null, dispensable.getId(), null, null);
        for (Orderable orderable : orderables) {
            org.smartregister.stock.openlmis.domain.TradeItem tradeItem = tradeItemRegisterRepository.getTradeItemById(orderable.getTradeItemId());
            List<org.smartregister.stock.openlmis.domain.TradeItem> tradeItemsByCommodityType = tradeItemRegisterRepository.getTradeItemByCommodityType(orderable.getCommodityTypeId());
            if (tradeItem != null) {
                tradeItem.setDispensable(dispensable);
                tradeItemRegisterRepository.addOrUpdate(tradeItem);

            } else if (tradeItemsByCommodityType.size() > 0) {
                String commodityTypeId = null;
                for (org.smartregister.stock.openlmis.domain.TradeItem savedTradeItem : tradeItemsByCommodityType) {
                    savedTradeItem.setDispensable(dispensable);
                    tradeItemRegisterRepository.addOrUpdate(savedTradeItem);
                    if (commodityTypeId == null && StringUtils.isNotBlank(savedTradeItem.getCommodityTypeId()))
                        commodityTypeId = savedTradeItem.getCommodityTypeId();
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

            List<TradeItem> savedTradeItems = tradeItemRepository.findTradeItems(tradeItem.getId(), null, null);
            TradeItem savedTradeItem;
            if (savedTradeItems.size() > 0) {
                savedTradeItem = savedTradeItems.get(0);
                tradeItemRegister.setDateUpdated(savedTradeItem.getDateUpdated());
            }

            List<Orderable> savedOrderables = orderableRepository.findOrderables(null, null, null, null, null, null, commodityType.getId());
            for (Orderable savedOrderable : savedOrderables) {
                updateInformation(savedOrderable);
            }
            tradeItemRegisterRepository.addOrUpdate(tradeItemRegister);
        }

        searchRepository.addOrUpdate(commodityType, tradeItemRegisterRepository.getTradeItemByCommodityType(commodityType.getId()));
    }
}
