package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataProviderUtil {
    public static Object[][] getJsonData(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
            Object[][] result = new Object[data.size()][1];
            for (int i = 0; i < data.size(); i++) {
                result[i][0] = data.get(i);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data: " + filePath, e);
        }
    }

    public static Object[][] getExcelData(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
            Object[][] data = new Object[rowCount - 1][colCount];
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    data[i - 1][j] = row.getCell(j).toString();
                }
            }
            workbook.close();
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel data: " + filePath, e);
        }
    }
} 