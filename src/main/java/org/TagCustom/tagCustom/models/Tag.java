package org.TagCustom.tagCustom.models;

public class Tag {
    private final String id;
    private final String permission;
    private final String display;
    private final String gradient;

    public Tag(String id, String permission, String display, String gradient) {
        this.id = id;
        this.permission = permission;
        this.display = display;
        this.gradient = gradient;
    }

    public String getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public String getDisplay() {
        return display;
    }

    public String getGradient() {
        return gradient;
    }
}
