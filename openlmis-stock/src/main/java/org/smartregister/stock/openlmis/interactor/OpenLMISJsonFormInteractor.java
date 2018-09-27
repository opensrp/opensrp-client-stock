package org.smartregister.stock.openlmis.interactor;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.stock.openlmis.OpenLMISLibrary;
import org.smartregister.stock.openlmis.repository.openlmis.ReasonsRepository;
import org.smartregister.stock.openlmis.util.OpenLMISConstants;
import org.smartregister.stock.openlmis.widget.LotFactory;
import org.smartregister.stock.openlmis.widget.OpenLMISDatePickerFactory;
import org.smartregister.stock.openlmis.widget.OpenLMISEditTextFactory;
import org.smartregister.stock.openlmis.widget.ReviewFactory;

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
        map.put(OpenLMISConstants.LOT_WIDGET, new LotFactory(OpenLMISLibrary.getInstance().getLotRepository(), new ReasonsRepository()));
        map.put(OpenLMISConstants.REVIEW_WIDGET, new ReviewFactory());
        map.put(JsonFormConstants.EDIT_TEXT, new OpenLMISEditTextFactory());
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}
