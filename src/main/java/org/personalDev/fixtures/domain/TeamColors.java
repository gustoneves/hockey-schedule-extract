package org.personalDev.fixtures.domain;

import lombok.Getter;

@Getter
public enum TeamColors{
    SUB_17("11"),
    SUB_13("5");

    private final String colorId;

    TeamColors(String colorId) {
        this.colorId = colorId;
    }

}
