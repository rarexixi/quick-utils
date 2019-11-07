package org.xi.quick.utils.poi;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xi.quick.annotation.ExcelCol;
import org.xi.quick.annotation.ExcelCols;
import org.xi.quick.model.ExportExcelModel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiUtils {

    /**
     * 导出多标签页Excel
     *
     * @param excelModelList
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(List<ExportExcelModel<T>> excelModelList, OutputStream outputStream) throws IllegalAccessException, IOException {

        Workbook workbook = new XSSFWorkbook();
        for (ExportExcelModel<T> excelModel : excelModelList) {
            exportExcel(workbook, excelModel.getSheetName(), excelModel.getClazz(), excelModel.getList(), excelModel.getCondition(), excelModel.getEnumFieldsMap());
        }
        workbook.write(outputStream);
    }

    /**
     * 导出单标签页
     *
     * @param excelModel
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(ExportExcelModel<T> excelModel, OutputStream outputStream) throws IllegalAccessException, IOException {

        exportExcel(excelModel.getSheetName(), excelModel.getClazz(), excelModel.getList(), excelModel.getCondition(), excelModel.getEnumFieldsMap(), outputStream);
    }

    /**
     * 导出单标签页
     *
     * @param sheetName    标签页名称
     * @param clazz        对应的类
     * @param list         对应的列表
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(String sheetName, Class<T> clazz, List<T> list, OutputStream outputStream) throws IllegalAccessException, IOException {

        exportExcel(sheetName, clazz, list, null, null, outputStream);
    }

    /**
     * 导出单标签页
     *
     * @param sheetName    标签页名称
     * @param clazz        对应的类
     * @param list         对应的列表
     * @param condition    相关条件
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(String sheetName, Class<T> clazz, List<T> list, String condition, OutputStream outputStream) throws IllegalAccessException, IOException {

        exportExcel(sheetName, clazz, list, condition, null, outputStream);
    }

    /**
     * 导出单标签页
     *
     * @param sheetName     标签页名称
     * @param clazz         对应的类
     * @param list          对应的列表
     * @param enumFieldsMap 字段枚举Map
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(String sheetName, Class<T> clazz, List<T> list, Map<String, Map<String, String>> enumFieldsMap, OutputStream outputStream) throws IllegalAccessException, IOException {

        exportExcel(sheetName, clazz, list, null, enumFieldsMap, outputStream);
    }

    /**
     * 导出单标签页
     *
     * @param sheetName     标签页名称
     * @param clazz         对应的类
     * @param list          对应的列表
     * @param condition     条件
     * @param enumFieldsMap 字段枚举Map
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(String sheetName, Class<T> clazz, List<T> list, String condition, Map<String, Map<String, String>> enumFieldsMap, OutputStream outputStream) throws IllegalAccessException, IOException {

        Workbook workbook = new XSSFWorkbook();
        exportExcel(workbook, sheetName, clazz, list, condition, enumFieldsMap);
        workbook.write(outputStream);
    }

    /**
     * 导出
     *
     * @param workbook
     * @param sheetName     标签页名称
     * @param clazz         对应的类
     * @param list          对应的列表
     * @param condition     条件
     * @param enumFieldsMap 字段枚举Map
     * @param <T>
     * @throws IllegalAccessException
     */
    private static <T> void exportExcel(Workbook workbook, String sheetName, Class<T> clazz, List<T> list, String condition, Map<String, Map<String, String>> enumFieldsMap) throws IllegalAccessException {

        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle defaultHeaderCellStyle = getDefaultHeaderCellStyle(workbook);
        CellStyle defaultCellStyle = getDefaultCellStyle(workbook);

        Row row;
        Cell cell;
        int rowIndex = 0;
        int colIndex = 0;

        List<FieldSort> fieldSorts = new ArrayList<>();
        row = sheet.createRow(rowIndex++);
        // 设置header 高度
        row.setHeight((short) 512);
        for (Field field : clazz.getDeclaredFields()) {
            ExcelCol excelCol = getExcelCol(field, condition);
            if (excelCol == null) continue;
            field.setAccessible(true);
            fieldSorts.add(new FieldSort(field, excelCol));
        }

        fieldSorts.sort(Comparator.comparing(fieldSort -> fieldSort.getExcelCol().order()));

        for (FieldSort fieldSort : fieldSorts) {
            Field field = fieldSort.getField();
            ExcelCol excelCol = fieldSort.getExcelCol();
            String name = excelCol.value();
            if (name == null || StringUtils.isBlank(name)) {
                name = field.getName();
            }
            // 设置每列宽度为20个字符，这个参数的单位是1/256个字符宽度
            int colWidth = excelCol.width();
            if (colWidth > 0) {
                sheet.setColumnWidth(colIndex, colWidth * 256);
            } else {
                sheet.autoSizeColumn(colIndex);
            }

            cell = row.createCell(colIndex++);
            cell.setCellValue(name);
            cell.setCellStyle(defaultHeaderCellStyle);
        }

        Row[] rows = new Row[list.size()];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sheet.createRow(rowIndex++);
        }

        colIndex = 0;
        for (FieldSort fieldSort : fieldSorts) {
            Field field = fieldSort.getField();
            ExcelCol excelCol = fieldSort.getExcelCol();
            Map<String, String> fieldEnumMap = enumFieldsMap == null ? null : enumFieldsMap.getOrDefault(field.getName(), null);
            rowIndex = 0;
            boolean isEnumField = fieldEnumMap != null && !fieldEnumMap.isEmpty();
            for (T model : list) {
                cell = rows[rowIndex++].createCell(colIndex);
                Object val = field.get(model);
                String value;
                String pattern = excelCol.formatter();
                if (StringUtils.isBlank(pattern)) {
                    value = val == null ? "" : val + "";
                } else {
                    if (val instanceof Date) {
                        value = getDateTime(val, excelCol.formatter());
                    } else {
                        value = getVal(val, excelCol.formatter());
                    }
                }
                String mapValue = isEnumField ? fieldEnumMap.getOrDefault(value, null) : value;
                value = mapValue == null ? value : mapValue;
                cell.setCellValue(value);
                cell.setCellStyle(defaultCellStyle);
            }
            colIndex++;
        }
        fieldSorts.forEach(fieldSort -> fieldSort.getField().setAccessible(false));
    }

    /**
     * 导出单标签页
     *
     * @param sheetName     标签页名称
     * @param columnMap     对应的列名映射
     * @param list          对应的列表
     * @param condition     条件
     * @param enumFieldsMap 字段枚举Map
     * @param outputStream
     * @param <T>
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void exportExcel(String sheetName, Map<String, String> columnMap, List<Map<String, String>> list, String condition, Map<String, Map<String, String>> enumFieldsMap, OutputStream outputStream) throws IllegalAccessException, IOException {

        Workbook workbook = new XSSFWorkbook();
        exportExcel(workbook, sheetName, columnMap, list, condition, enumFieldsMap);
        workbook.write(outputStream);
    }

    /**
     * 导出
     *
     * @param workbook
     * @param sheetName     标签页名称
     * @param columnMap     对应的列名映射
     * @param list          对应的列表
     * @param condition     条件
     * @param enumFieldsMap 字段枚举Map
     * @param <T>
     * @throws IllegalAccessException
     */
    private static <T> void exportExcel(Workbook workbook, String sheetName, Map<String, String> columnMap, List<Map<String, String>> list, String condition, Map<String, Map<String, String>> enumFieldsMap) throws IllegalAccessException {

        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle defaultHeaderCellStyle = getDefaultHeaderCellStyle(workbook);

        Row row;
        Cell cell;
        int rowIndex = 0, colIndex = 0;

        row = sheet.createRow(rowIndex++);
        // 设置header 高度
        row.setHeight((short) 512);
        List<String> colNameList = new ArrayList<>();
        for (Map.Entry<String, String> entry : columnMap.entrySet()) {
            sheet.autoSizeColumn(colIndex);
            colNameList.add(entry.getKey());
            cell = row.createCell(colIndex++);
            cell.setCellValue(entry.getValue());
            cell.setCellStyle(defaultHeaderCellStyle);
        }

        for (Map<String, String> rowItem : list) {
            row = sheet.createRow(rowIndex++);
            colIndex = 0;
            for (String colName : colNameList) {
                cell = row.createCell(colIndex++);
                cell.setCellValue(rowItem.getOrDefault(colName, ""));
            }
        }
    }

    private static ExcelCol getExcelCol(Field field, String condition) {

        boolean conditionIsBlank = StringUtils.isBlank(condition);

        ExcelCol excelCol = field.getAnnotation(ExcelCol.class);
        if (excelCol != null) {
            if ((conditionIsBlank && StringUtils.isBlank(excelCol.condition())) ||
                    (!conditionIsBlank && condition.equals(excelCol.condition()))) {
                return excelCol;
            }
        }
        ExcelCols excelCols = field.getAnnotation(ExcelCols.class);
        if (excelCols == null) return null;
        ExcelCol[] cols = excelCols.value();
        if (cols == null || cols.length == 0) return null;
        for (ExcelCol col : cols) {
            if ((conditionIsBlank && StringUtils.isBlank(col.condition())) ||
                    (!conditionIsBlank && condition.equals(col.condition()))) {
                return col;
            }
        }
        return null;
    }

    /**
     * 获取表头单元格样式
     *
     * @param wb
     * @return
     */
    private static CellStyle getDefaultHeaderCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setFontName("黑体");
        style.setFont(titleFont);
        return style;
    }

    /**
     * 获取默认单元格样式
     *
     * @param wb
     * @return
     */
    private static CellStyle getDefaultCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    private static String getDateTime(Object obj, String pattern) {
        if (obj == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(obj);
    }

    private static String getVal(Object obj, String pattern) {
        if (obj == null) return "";
        return String.format(pattern, obj);
    }

    private final static class FieldSort {
        public FieldSort(Field field, ExcelCol excelCol) {
            this.field = field;
            this.excelCol = excelCol;
        }

        private Field field;
        private ExcelCol excelCol;

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public ExcelCol getExcelCol() {
            return excelCol;
        }

        public void setExcelCol(ExcelCol excelCol) {
            this.excelCol = excelCol;
        }
    }
}
