package org.smartregister.stock.openlmis.interactor;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.widget.LotFactory;
import org.smartregister.stock.openlmis.widget.OpenLMISDatePickerFactory;

/**
 * Created by samuelgithengi on 8/16/18.
 */
public class OpenLMISJsonFormInteractor extends JsonFormInteractor {
    private static final OpenLMISJsonFormInteractor INSTANCE = new OpenLMISJsonFormInteractor();

    private OpenLMISJsonFormInteractor() {
        super();
    }

    @Override
    protected void registerWidgets() {
        super.registerWidgets();
        map.put(JsonFormConstants.DATE_PICKER, new OpenLMISDatePickerFactory());
        map.put(OpenLMISConstants.LOT_WIDGET, new LotFactory());
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}
