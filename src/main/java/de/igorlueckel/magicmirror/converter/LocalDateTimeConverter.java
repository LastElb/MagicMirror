package de.igorlueckel.magicmirror.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime date) {
        if (date == null)
            return null;
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date value) {
        if (value == null)
            return null;
        return LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
    }
}