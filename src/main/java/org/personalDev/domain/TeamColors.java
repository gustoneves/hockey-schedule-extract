package org.personalDev.domain;

public enum TeamColors{
    SUB_15("11"),
    SUB_15_B("4"),
    SUB_17("5");

    private final String colorId;

    TeamColors(String colorId) {
        this.colorId = colorId;
    }

    public String getColorId() {
        return colorId;
    }
}
