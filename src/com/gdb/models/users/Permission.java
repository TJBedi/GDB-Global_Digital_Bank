package com.gdb.models.users;

public class Permission {
    private Feature feature;
    private boolean isAvailable;
    public Feature getFeature() {
        return feature;
    }
    public void setFeature(Feature feature) {
        this.feature = feature;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
