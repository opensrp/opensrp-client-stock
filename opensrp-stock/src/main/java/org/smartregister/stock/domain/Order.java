package org.smartregister.stock.domain;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 07/03/2018.
 * Created by Vincent Karuri - vincentmkaruri@gmail.com on 07/03/2018.
 */

public class Order {

    private String id;
    private String revision;
    private String type = "Order";
    private long dateCreated;
    private long dateEdited;
    private long serverVersion;
    private String locationId;
    private String providerId;
    private long dateCreatedByClient;
    private boolean synced;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(long dateEdited) {
        this.dateEdited = dateEdited;
    }

    public long getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(long serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public long getDateCreatedByClient() {
        return dateCreatedByClient;
    }

    public void setDateCreatedByClient(long dateCreatedByClient) {
        this.dateCreatedByClient = dateCreatedByClient;
    }
}
