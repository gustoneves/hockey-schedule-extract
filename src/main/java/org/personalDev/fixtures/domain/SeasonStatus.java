package org.personalDev.fixtures.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SeasonStatus {
    ONGOING(1),
    DRAFT(2),
    ENDED(3);

    private final int order;
}
