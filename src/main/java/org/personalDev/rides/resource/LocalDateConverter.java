package org.personalDev.rides.resource;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Provider
public class LocalDateConverter implements ParamConverterProvider {

    private static final String dateFormat = "yyyy-MM-dd";

    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == LocalDate.class) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            return (ParamConverter<T>) new LocalDateParamConverter(formatter);
        }
        return null;
    }
}


class LocalDateParamConverter implements ParamConverter<LocalDate> {

    private final DateTimeFormatter formatter;

    LocalDateParamConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDate fromString(String value) {
        return LocalDate.parse(value, formatter);
    }

    @Override
    public String toString(LocalDate value) {
        return value != null ? value.format(formatter) : "";
    }
}
