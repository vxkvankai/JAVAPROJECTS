package com.d3.api.mappings.alerts;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDestination {

    @SerializedName("checked")
    @Expose
    private Boolean checked;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("primary")
    @Expose
    private Boolean primary;
    @SerializedName("outOfBand")
    @Expose
    private Boolean outOfBand;
    @SerializedName("defaultDest")
    @Expose
    private Boolean defaultDest;
    @SerializedName("readOnly")
    @Expose
    private Boolean readOnly;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getOutOfBand() {
        return outOfBand;
    }

    public void setOutOfBand(Boolean outOfBand) {
        this.outOfBand = outOfBand;
    }

    public Boolean getDefaultDest() {
        return defaultDest;
    }

    public void setDefaultDest(Boolean defaultDest) {
        this.defaultDest = defaultDest;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public static UserDestination sendToInbox(int destinationId) {
        UserDestination destination = new UserDestination();
        destination.checked = true;
        destination.defaultDest = false;
        destination.id = destinationId;
        destination.label = "INBOX";
        destination.outOfBand = false;
        destination.primary = false;
        destination.readOnly = false;
        destination.type = "INBOX";
        destination.value = "Inbox";
        return destination;
    }

}

