package ngsMetadataToolPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Drew on 7/19/16.
 */
public class spreadsheetEditor {
    static XSSFRow row;

    public static void main(String[] args, List<String> fileNameList, int sheet, List<File> files, String person,
                            String project, String cancerType, String endType) throws Exception{
        FileInputStream fis = new FileInputStream(new File("/Users/REO/OneDrive/NGS-Metadata.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet spreadsheet = workbook.getSheetAt(sheet);
        Iterator < Row > rowIterator = spreadsheet.iterator();

        int totalRowCounter = 1;
        while (rowIterator.hasNext())
        {
            row = (XSSFRow) rowIterator.next();
            Iterator < Cell > cellIterator = row.cellIterator();
            while ( cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(
                                cell.getNumericCellValue() + " \t\t " );
                        break;
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(
                                cell.getStringCellValue() + " \t\t " );
                        break;
                }
            }
            totalRowCounter++;
            System.out.println();
        }

        System.out.println(totalRowCounter);
        //The following data will be written to the spreadsheet
        int i = 0;
        int rowIteratorFor = 0;
        Map< String, Object[] > dataToBeAdded = new TreeMap< String, Object[] >();
        System.out.println("File name list size: " + fileNameList.size());
        for(i=0; i < fileNameList.size(); i++){
            rowIteratorFor = (totalRowCounter+i);

            //date format for sequence date

            //tells if end type is single or paired

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            dataToBeAdded.put(Integer.toString(rowIteratorFor), new Object[]{"", fileNameList.get(i),
                    sdf.format(files.get(i).lastModified()).toString(), person, project, "", "", "", "", "", cancerType,
                    "", "", "", "", (files.get(i).getParent()).toString(), "", endType, ""});
        }
        System.out.println(dataToBeAdded.toString());
        //Add information to spreadsheet
        Set< String > keyid = dataToBeAdded.keySet();
        int rowid = totalRowCounter-1;
        System.out.println("Total row count: " + totalRowCounter);
        for (String key : keyid)
        {
            row = spreadsheet.createRow(rowid++);
            Object [] objectArr = dataToBeAdded.get(key);
            int cellid = 0;
            for (Object obj : objectArr)
            {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        fis.close();

        FileOutputStream outFile =new FileOutputStream(new File("/Users/REO/OneDrive/NGS-Metadata.xlsx"));
        workbook.write(outFile);
        outFile.close();
    }

}