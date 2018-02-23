package org.smartregister.stock.sample.repository;

import org.json.JSONObject;
import org.smartregister.repository.Repository;
import org.smartregister.stock.domain.ActiveChildrenStats;
import org.smartregister.stock.repository.StockExternalRepository;

import java.util.Random;

/**
 * Created by samuelgithengi on 2/9/18.
 */

public class StockHelperRepository extends StockExternalRepository {

    public StockHelperRepository(Repository repository) {
        super(repository);
    }

    @Override
    public int getVaccinesUsedUntilDate(Long date, String vaccineName) {
        return new Random(System.currentTimeMillis()).nextInt(30);
    }

    @Override
    public int getVaccinesUsedToday(Long date, String vaccineName) {
        return new Random(System.currentTimeMillis()).nextInt(30);
    }

    @Override
    public ActiveChildrenStats getActiveChildrenStat() {
        ActiveChildrenStats childrenStats = new ActiveChildrenStats();
        childrenStats.setChildrenLastMonthZeroToEleven(12l);
        childrenStats.setChildrenLastMonthtwelveTofiftyNine(59l);
        childrenStats.setChildrenThisMonthZeroToEleven(14l);
        childrenStats.setChildrenThisMonthtwelveTofiftyNine(61l);
        return childrenStats;
    }

    @Override
    public int getVaccinesDueBasedOnSchedule(JSONObject vaccineobject) {
        return 7;
    }
}
