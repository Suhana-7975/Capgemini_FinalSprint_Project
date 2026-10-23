package ecom.BITM.QADemo.utilities;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

    private static final String FILE_PATH = "src/test/resources/TestData.xlsx";

    // rowNum, colNum 0-based: row 0 = header, column 0 = A
    public static String getCellData(String sheetName, int rowNum, int colNum) {

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row row = sheet.getRow(rowNum);
            if (row == null || row.getCell(colNum) == null) {
                return "";
            }

            String value = new DataFormatter().formatCellValue(row.getCell(colNum)).trim();

            
            if (value.equalsIgnoreCase("(blank)")) {
                return "";
            }
            return value;

        } catch (IOException e) {
            throw new RuntimeException("Unable to read TestData.xlsx", e);
        }
    }
}