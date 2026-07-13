package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExcelTestResultUtil {

    private static final String RESULT_FILE_PATH =
            "target/TestResults.xlsx";

    private static final String RESULT_SHEET_NAME =
            "Test Results";

    private static final String SUMMARY_SHEET_NAME =
            "Summary";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private static long suiteStartTime;
    private static String executionStartDate;

    private ExcelTestResultUtil() {
    }

    public static synchronized void initializeResultFile() {

        suiteStartTime = System.currentTimeMillis();
        executionStartDate =
                LocalDateTime.now().format(DATE_FORMATTER);

        File resultFile = new File(RESULT_FILE_PATH);

        createParentDirectory(resultFile);

        if (resultFile.exists() && !resultFile.delete()) {
            throw new IllegalStateException(
                    "Could not delete existing test result file: "
                            + resultFile.getAbsolutePath()
            );
        }

        try (
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream =
                        new FileOutputStream(resultFile)
        ) {
            Sheet resultSheet =
                    workbook.createSheet(RESULT_SHEET_NAME);

            Sheet summarySheet =
                    workbook.createSheet(SUMMARY_SHEET_NAME);

            createResultHeader(resultSheet, workbook);
            createInitialSummary(summarySheet, workbook);

            workbook.write(outputStream);

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not create test result Excel file: "
                            + RESULT_FILE_PATH,
                    exception
            );
        }
    }

    public static synchronized void writeResult(
            String testName,
            String status,
            String failureReason,
            long durationMillis
    ) {
        File resultFile = new File(RESULT_FILE_PATH);

        if (!resultFile.exists()) {
            initializeResultFile();
        }

        try (
                FileInputStream inputStream =
                        new FileInputStream(resultFile);

                Workbook workbook =
                        WorkbookFactory.create(inputStream)
        ) {
            Sheet resultSheet =
                    workbook.getSheet(RESULT_SHEET_NAME);

            if (resultSheet == null) {
                resultSheet =
                        workbook.createSheet(RESULT_SHEET_NAME);

                createResultHeader(resultSheet, workbook);
            }

            int newRowNumber =
                    resultSheet.getLastRowNum() + 1;

            Row row =
                    resultSheet.createRow(newRowNumber);

            String environment =
                    ConfigUtil.get("environment");

            String browser =
                    ConfigUtil.get("browser");

            double durationSeconds =
                    durationMillis / 1000.0;

            row.createCell(0).setCellValue(
                    LocalDateTime.now().format(DATE_FORMATTER)
            );

            row.createCell(1).setCellValue(environment);
            row.createCell(2).setCellValue(browser);
            row.createCell(3).setCellValue(testName);
            row.createCell(4).setCellValue(durationSeconds);
            row.createCell(5).setCellValue(status);

            row.createCell(6).setCellValue(
                    failureReason == null
                            ? ""
                            : failureReason
            );

            applyStatusStyle(
                    row.getCell(5),
                    status,
                    workbook
            );

            updateSummary(workbook);

            try (
                    FileOutputStream outputStream =
                            new FileOutputStream(resultFile)
            ) {
                workbook.write(outputStream);
            }

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not write test result Excel file: "
                            + RESULT_FILE_PATH,
                    exception
            );
        }
    }

    public static synchronized void finalizeSummary() {

        File resultFile =
                new File(RESULT_FILE_PATH);

        if (!resultFile.exists()) {
            return;
        }

        try (
                FileInputStream inputStream =
                        new FileInputStream(resultFile);

                Workbook workbook =
                        WorkbookFactory.create(inputStream)
        ) {
            updateSummary(workbook);

            try (
                    FileOutputStream outputStream =
                            new FileOutputStream(resultFile)
            ) {
                workbook.write(outputStream);
            }

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not finalize summary sheet.",
                    exception
            );
        }
    }

    private static void createResultHeader(
            Sheet sheet,
            Workbook workbook
    ) {
        Row headerRow =
                sheet.createRow(0);

        headerRow.createCell(0)
                .setCellValue("Execution Time");

        headerRow.createCell(1)
                .setCellValue("Environment");

        headerRow.createCell(2)
                .setCellValue("Browser");

        headerRow.createCell(3)
                .setCellValue("Test Name");

        headerRow.createCell(4)
                .setCellValue("Duration (Seconds)");

        headerRow.createCell(5)
                .setCellValue("Status");

        headerRow.createCell(6)
                .setCellValue("Error Message");

        CellStyle headerStyle =
                createHeaderStyle(workbook);

        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        sheet.setColumnWidth(0, 22 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 55 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 14 * 256);
        sheet.setColumnWidth(6, 100 * 256);

        sheet.createFreezePane(0, 1);
    }

    private static void createInitialSummary(
            Sheet sheet,
            Workbook workbook
    ) {
        Row titleRow =
                sheet.createRow(0);

        titleRow.createCell(0)
                .setCellValue("Test Execution Summary");

        titleRow.getCell(0)
                .setCellStyle(createHeaderStyle(workbook));

        sheet.setColumnWidth(0, 28 * 256);
        sheet.setColumnWidth(1, 25 * 256);

        writeSummaryRow(sheet, 2, "Execution Date", "");
        writeSummaryRow(sheet, 3, "Environment", "");
        writeSummaryRow(sheet, 4, "Browser", "");
        writeSummaryRow(sheet, 5, "Total Tests", "0");
        writeSummaryRow(sheet, 6, "Passed", "0");
        writeSummaryRow(sheet, 7, "Failed", "0");
        writeSummaryRow(sheet, 8, "Skipped", "0");
        writeSummaryRow(sheet, 9, "Success Rate", "0.00 %");
        writeSummaryRow(sheet, 10, "Suite Duration", "0.00 s");
        writeSummaryRow(
                sheet,
                11,
                "Average Test Duration",
                "0.00 s"
        );
    }

    private static void updateSummary(
            Workbook workbook
    ) {
        Sheet resultSheet =
                workbook.getSheet(RESULT_SHEET_NAME);

        Sheet summarySheet =
                workbook.getSheet(SUMMARY_SHEET_NAME);

        if (summarySheet == null) {
            summarySheet =
                    workbook.createSheet(SUMMARY_SHEET_NAME);

            createInitialSummary(summarySheet, workbook);
        }

        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        double totalTestDuration = 0.0;

        if (resultSheet != null) {

            for (
                    int rowIndex = 1;
                    rowIndex <= resultSheet.getLastRowNum();
                    rowIndex++
            ) {
                Row row =
                        resultSheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                Cell durationCell =
                        row.getCell(4);

                Cell statusCell =
                        row.getCell(5);

                if (statusCell == null) {
                    continue;
                }

                total++;

                String status =
                        statusCell.getStringCellValue();

                if ("PASS".equalsIgnoreCase(status)) {
                    passed++;
                } else if ("FAIL".equalsIgnoreCase(status)) {
                    failed++;
                } else if ("SKIPPED".equalsIgnoreCase(status)) {
                    skipped++;
                }

                if (
                        durationCell != null
                                && durationCell.getCellType()
                                == CellType.NUMERIC
                ) {
                    totalTestDuration +=
                            durationCell.getNumericCellValue();
                }
            }
        }

        double successRate =
                total == 0
                        ? 0.0
                        : passed * 100.0 / total;

        double suiteDurationSeconds =
                (System.currentTimeMillis() - suiteStartTime)
                        / 1000.0;

        double averageDuration =
                total == 0
                        ? 0.0
                        : totalTestDuration / total;

        setSummaryValue(
                summarySheet,
                2,
                executionStartDate
        );

        setSummaryValue(
                summarySheet,
                3,
                ConfigUtil.get("environment")
        );

        setSummaryValue(
                summarySheet,
                4,
                ConfigUtil.get("browser")
        );

        setSummaryValue(
                summarySheet,
                5,
                String.valueOf(total)
        );

        setSummaryValue(
                summarySheet,
                6,
                String.valueOf(passed)
        );

        setSummaryValue(
                summarySheet,
                7,
                String.valueOf(failed)
        );

        setSummaryValue(
                summarySheet,
                8,
                String.valueOf(skipped)
        );

        setSummaryValue(
                summarySheet,
                9,
                String.format("%.2f %%", successRate)
        );

        setSummaryValue(
                summarySheet,
                10,
                String.format("%.2f s", suiteDurationSeconds)
        );

        setSummaryValue(
                summarySheet,
                11,
                String.format("%.2f s", averageDuration)
        );
    }

    private static void applyStatusStyle(
            Cell statusCell,
            String status,
            Workbook workbook
    ) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        if ("PASS".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());

        } else if ("FAIL".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());

        } else if ("SKIPPED".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        }

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        statusCell.setCellStyle(style);
    }

    private static CellStyle createHeaderStyle(
            Workbook workbook
    ) {
        CellStyle style =
                workbook.createCellStyle();

        Font font =
                workbook.createFont();

        font.setBold(true);

        style.setFont(font);

        style.setFillForegroundColor(
                IndexedColors.GREY_25_PERCENT.getIndex()
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        return style;
    }

    private static void writeSummaryRow(
            Sheet sheet,
            int rowIndex,
            String metric,
            String value
    ) {
        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue(metric);
        row.createCell(1).setCellValue(value);
    }

    private static void setSummaryValue(
            Sheet sheet,
            int rowIndex,
            String value
    ) {
        Row row =
                sheet.getRow(rowIndex);

        if (row == null) {
            row =
                    sheet.createRow(rowIndex);
        }

        Cell valueCell =
                row.getCell(1);

        if (valueCell == null) {
            valueCell =
                    row.createCell(1);
        }

        valueCell.setCellValue(value);
    }

    private static void createParentDirectory(
            File file
    ) {
        File parentDirectory =
                file.getParentFile();

        if (
                parentDirectory != null
                        && !parentDirectory.exists()
                        && !parentDirectory.mkdirs()
        ) {
            throw new IllegalStateException(
                    "Target directory could not be created: "
                            + parentDirectory.getAbsolutePath()
            );
        }
    }
}