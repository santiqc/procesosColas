package com.procesos.colas.interceptor;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class UtilsHelper {

    @Getter
    private static final ObjectMapper JSON_MAPPER;

    @Getter
    private static final XmlMapper XML_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        XML_MAPPER = new XmlMapper();
        XML_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
        XML_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, SerializationFeature.WRAP_ROOT_VALUE);
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        XML_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, Boolean.TRUE);
    }

    public static String protectFields(String jsonTextI, String... protectFields) {
        var jsonText = jsonTextI;
        for (String protectField : protectFields) {
            var regex = ConstantsHelper.QUOTES + protectField + ConstantsHelper.REGEX_REPLACE_JSON_VALUE;
            var m = Pattern.compile(regex).matcher(jsonText);
            while (m.find()) {
                var match = m.group();
                var replace = m.group().replace(protectField, ConstantsHelper.EMPTY_STRING)
                    .replace(ConstantsHelper.QUOTES, ConstantsHelper.EMPTY_STRING)
                    .replace(ConstantsHelper.TWO_DOTS, ConstantsHelper.EMPTY_STRING);
                var placeHolder = StringUtils.repeat(ConstantsHelper.ASTERISK, replace.length());
                var ret = match.replace(replace, placeHolder);
                jsonText = jsonText.replace(match, ret);
            }
        }
        return jsonText;
    }

    public static String getCurrentCorrelative() {
        return MDC.get(ConstantsHelper.CORRELATIVE_ID);
    }

    public static String printIgnore(@NotNull String xmlString, String... tags) {
        if (StringUtils.isEmpty(xmlString)) {
            return xmlString;
        }
        xmlString = safelyCommand(xmlString);
        if (tags != null && !ArrayUtils.isEmpty(tags)) {
            for (String s : tags) {
                var tag = "<".concat(s).concat(">([^<]*)</").concat(s).concat(">");
                var pattern = Pattern.compile(tag);
                var matcher = pattern.matcher(xmlString);
                var listMatches = new ArrayList<String>();

                while (matcher.find()) {
                    listMatches.add(matcher.group(0));
                }

                String se;
                String replaceValue;
                for (Iterator<String> var11 = listMatches.iterator(); var11.hasNext(); xmlString = xmlString.replaceAll(se, replaceValue)) {
                    se = var11.next();
                    var longitud = se.replaceAll("<" + s + ">", "")
                        .replaceAll("</" + s + ">", "").length();
                    replaceValue = "<".concat(s)
                        .concat(StringUtils.rightPad(">", longitud + 1, "*"))
                        .concat("</").concat(s).concat(">");
                }
            }

        }
        return xmlString;
    }

    public static String safelyCommand(@NotNull String xmlString) {
        if (!StringUtils.isEmpty(xmlString)) {
            xmlString = xmlString.replaceAll("(\r\n|\n|\r|\t)", "");
            xmlString = xmlString.replace("&", "&amp;");
        }
        return xmlString;
    }

    public static void assignCorrelative(String componentName, String correlation) {
        var cId = correlation;

        if (correlation == null || correlation.isEmpty()) {
            cId = getCorrelative();
        }

        MDC.putCloseable(ConstantsHelper.CORRELATIVE_ID, cId);
        MDC.putCloseable(ConstantsHelper.COMPONENT_CORRELATIVE, componentName);
    }

    public static String getCorrelative() {
        return UUID.randomUUID().toString().replace(ConstantsHelper.STRING_LINE, ConstantsHelper.EMPTY_STRING);
    }

    public static void assignCorrelative(String componentName) {
        assignCorrelative(componentName, null);
    }

    public static Map<String, Object> convertDTOToMap(Object dto) {
        Map<String, Object> result = new HashMap<>();

        Class<?> dtoClass = dto.getClass();

        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            Object value = null;
            try {
                value = field.get(dto);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            result.put(fieldName, value);

        }

        return result;
    }

    public static MultiValueMap<String, Object> convertDTOToMultiValueMap(Object dto) {
        MultiValueMap<String, Object> result = new LinkedMultiValueMap<>();

        Class<?> dtoClass = dto.getClass();

        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            try {
                Object value = field.get(dto);
                result.add(fieldName, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private UtilsHelper() {
        // Not is necessary this implementation
    }

    public static long calcularDiasRestantes(Date fechaVencimiento){
        Calendar bogotaCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
        long diferenciaEnMilisegundos = fechaVencimiento.getTime() - bogotaCalendar.getTimeInMillis();
        return diferenciaEnMilisegundos / (1000 * 60 * 60 * 24);
    }

    public static long calcularDiasFechas(LocalDateTime inicial, LocalDateTime fechaVencimiento) {
        // Convertir a la zona horaria "America/Bogota"
        ZoneId zoneId = ZoneId.of("America/Bogota");

        return ChronoUnit.DAYS.between(inicial.atZone(zoneId), fechaVencimiento.atZone(zoneId));
    }


}