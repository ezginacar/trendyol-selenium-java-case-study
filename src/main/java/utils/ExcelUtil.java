package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ExcelUtil {

    private static final String FILE_PATH =
            "src/test/resources/TestData.xlsx";

    private static final Map<String, String> testData = new ConcurrentHashMap<>();

    private static volatile boolean loaded = false;

    private ExcelUtil() {
    }

    public static String getValue(String key) {
        if (!loaded) {
            loadExcel();
        }

        String value = testData.get(key);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Could not found key: " + key
            );
        }

        return value;
    }

    private static synchronized void loadExcel() {
        if (loaded) {
            return;
        }

        String sheetName = ConfigUtil.get("environment");

        try (
                FileInputStream inputStream = new FileInputStream(FILE_PATH);
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Could not find excel sheet " + sheetName
                );
            }

            for (Row row : sheet) {

                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);

                if (keyCell == null || valueCell == null) {
                    continue;
                }

                String key = keyCell.getStringCellValue().trim();
                String value = valueCell.toString().trim();

                testData.put(key, value);
            }

            loaded = true;

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not read excel file: " + FILE_PATH,
                    exception
            );
        }
    }
}