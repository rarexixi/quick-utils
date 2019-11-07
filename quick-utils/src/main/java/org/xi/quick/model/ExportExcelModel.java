package org.xi.quick.model;

import java.util.List;
import java.util.Map;

public class ExportExcelModel<T> {

    private String sheetName;
    private Class<T> clazz;
    private List<T> list;
    private String condition;
    private Map<String, Map<String, String>> enumFieldsMap;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Map<String, Map<String, String>> getEnumFieldsMap() {
        return enumFieldsMap;
    }

    public void setEnumFieldsMap(Map<String, Map<String, String>> enumFieldsMap) {
        this.enumFieldsMap = enumFieldsMap;
    }
}
