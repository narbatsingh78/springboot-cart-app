package com.springbootcartapp.entities;

import java.text.DateFormat.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {

    public LocalDateConverter() {
        super();
    }

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		
		if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String cleaned = value.trim().replace("\"", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(cleaned, formatter);
	}
}



