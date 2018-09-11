package org.smartregister.stock.openlmis.repository.openlmis;

import java.util.Arrays;
import java.util.List;

/**
 * Created by samuelgithengi on 9/5/18.
 */
public class ReasonsRepository {

    public List<String> getAdjustmentReasons() {
        return Arrays.asList("Damage", "Beginning Excess", "Transferred", "Other");
    }
}
