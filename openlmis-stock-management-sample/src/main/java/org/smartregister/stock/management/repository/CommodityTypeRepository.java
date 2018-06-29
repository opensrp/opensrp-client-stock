package org.smartregister.stock.management.repository;

import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.stock.management.domain.CommodityType;

public class CommodityTypeRepository extends BaseRepository {

    public static final String TAG = CommodityType.class.getName();
    public static final String COMMODITY_TYPE_TABLE = "commodity_types";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PARENT = "parent";
    public static final String CLASSIFICATION_SYSTEM = "classification_system";
    public static final String CLASSIFICATION_ID = "classification_id";
    public static final String DATE_UPDATED = "date_updated";
    public static final String[] COMMODITY_TYPE_TABLE_COLUMNS = {ID, NAME, PARENT, CLASSIFICATION_SYSTEM, CLASSIFICATION_ID, DATE_UPDATED};

    public static final String CREATE_COMMODITY_TYPE_TABLE =

            "CREATE TABLE " + COMMODITY_TYPE_TABLE
            + "("
                    + ID + " VARCHAR NOT NULL,"
                    + NAME + " VARCHAR NOT NULL,"
                    + PARENT + " VARCHAR,"
                    + CLASSIFICATION_SYSTEM + " VARCHAR,"
                    + CLASSIFICATION_ID + " VARCHAR,"
                    + DATE_UPDATED + " INTEGER"
            + ")";

    public CommodityTypeRepository(Repository repository) { super(repository); }
}
