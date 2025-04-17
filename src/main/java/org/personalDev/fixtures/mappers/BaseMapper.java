package org.personalDev.fixtures.mappers;

import java.util.List;

public interface BaseMapper<S,T> {

    List<T> toDto(List<S> entities);
}
