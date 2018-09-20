package org.smartregister.stock.openlmis.domain.openlmis;

public class BaseEntity {

    protected String id;

    protected Long serverVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(Long serverVersion) {
        this.serverVersion = serverVersion;
    }
}
