package org.smartregister.stock.repository;

import org.json.JSONObject;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.ActiveChildrenStats;

/**
 * Created by samuelgithengi on 2/9/18.
 */

public abstract class StockExternalRepository extends BaseRepository {

    public StockExternalRepository(Repository repository) {
        super();
    }

    public abstract int getVaccinesUsedUntilDate(Long date, String vaccineName);

    public abstract int getVaccinesUsedToday(Long date, String vaccineName);

    public abstract ActiveChildrenStats getActiveChildrenStat();

    public abstract int getVaccinesDueBasedOnSchedule(JSONObject vaccineobject);
}
