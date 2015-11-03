

import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.*;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


interface BookInterface {
    //Getters and Setters for Book Attributes
    //String GetBookName(String book_name);
    void SetBookName(String book_name);
    //String GetAuthorname(String author_name);
    void SetAuthorName(String author_name);
    //int GetYearOfPublication(int year);
    void SetYearOfPublication(int year);
    //int GetNumberOfCopies(int n_copies);
    void SetNumberOfCopies(int n_copies);
    //char GetBookStatus(char status);
    void SetBookStatus(char stauts);
}


public class book implements BookInterface{
    String BookName;
    String AuthorName;
    int YearOfPublication;
    int NumberOfCopies;
    char BookStatus;

    // This is a Constructor
    public book(){
        this.BookName = "Inferno";
        this.AuthorName = "Dan Brown";
        this.YearOfPublication = 2003;
        this.NumberOfCopies = 0;
        this.BookStatus = 'N';
    }
    public String toString(){
        return this.BookName+"-"+this.AuthorName+"-"+(int)this.YearOfPublication+"-"+(int)this.NumberOfCopies+"-"+this.BookStatus;
    }

    public void SetBookName(String book_name){
        this.BookName = book_name;
    }
    public void SetAuthorName(String author_name){
        this.AuthorName = author_name;
    }
    public void SetYearOfPublication(int year){
        this.YearOfPublication = year;
    }
    public void SetNumberOfCopies(int n_copies){
        this.NumberOfCopies = n_copies;
    }
    public void SetBookStatus(char status){
        this.BookStatus = status;
    }

    public static void main(String []args){
        int n_books=0;
        try {

            FileInputStream excel_file = new FileInputStream("BooksInfo.xlsx");
            Workbook workbook = new XSSFWorkbook(excel_file);
            //Workbook workbook = new HSSFWorkbook(excel_file);

            Sheet FirstSheet = workbook.getSheetAt(0);

            n_books = FirstSheet.getLastRowNum();
            book Books[] = new book[n_books];
            System.out.println("Number of different books in library: "+Books.length);
            for(int j=0;j<Books.length;j++) {
                Books[j] = new book();
            }
            int i=0;
            Iterator<Row> rowIterator = FirstSheet.iterator();
            Row nextRow = rowIterator.next();   // Ignore first row
            //nextRow = rowIterator.next();
            while(rowIterator.hasNext()){
                nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Cell cell = cellIterator.next(); //Ignore first column
                int n_attributes=0;
                while(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    switch (cell.getCellType()){
                        case Cell.CELL_TYPE_STRING:
                            n_attributes++;
                            if(n_attributes==1){
                                Books[i].SetBookName(cell.getStringCellValue());
                            }else if(n_attributes==2){
                                Books[i].SetAuthorName(cell.getStringCellValue());
                            }else if(n_attributes==5){

                                File file = new File("BooksInfo.xlsx");
                                FileOutputStream excel_file_1 = new FileOutputStream(file);
                                if(Books[i].NumberOfCopies<=0){
                                    cell.setCellValue("N");
                                }else{
                                    cell.setCellValue("Y");
                                }
                                workbook.write(excel_file_1);
                                excel_file_1.flush();
                                excel_file_1.close();

                                Books[i].SetBookStatus((cell.getStringCellValue()).charAt(0));
                                n_attributes=0;
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            n_attributes++;
                            if(n_attributes==3){
                                Books[i].SetYearOfPublication((int) cell.getNumericCellValue());
                            }else if(n_attributes==4){
                                Books[i].SetNumberOfCopies((int)cell.getNumericCellValue());
                            }
                            break;
                        default:
                            System.out.println("Program just got into default case");
                            break;
                    }
                }
                System.out.println(Books[i].toString());
                i++;
            }
            //workbook.close();
            excel_file.close();
            /*
            try {
                File file = new File("BooksInfo.xlsx");
                FileOutputStream excel_file_1 = new FileOutputStream(file);
                Cell cell = null;
                cell = FirstSheet.getRow(temp_row).getCell(temp_cell);
                cell.setCellValue("N");
                workbook.write(excel_file_1);
                excel_file_1.flush();
                excel_file_1.close();
            }catch (IOException e){
                System.out.println("Excel file not found while writing");
            }
            */
            workbook.close();

        }catch(IOException e){
            System.out.println("Excel file not found");
        }



    }
}
