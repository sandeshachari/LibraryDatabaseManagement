
import com.sun.istack.internal.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


interface UserInterface{
    String GetUserName();
    String GetUserPassword();
    void SetUserName(String username);
    void SetUserPassword(String password);
    //String GetUserStatus(String user_name);
    //void IssueBook(String user_name,String []book_name, int n_books);
    //void ReturnBook(String user_name,String []book_name,int n_books);


}

interface LambdaExpression{
    boolean checkBlanks(boolean b,String s);
}



public class UserProfile implements UserInterface,LambdaExpression {
    String username;
    String password;
    int maxBooksAllowed = 5;
    String []books = new String[maxBooksAllowed];

    boolean BlankOrNull;
    String PossibleBlankString;

    public String GetUserName(){
        return this.username;
    }
    public String GetUserPassword(){
        return this.password;
    }
    public void SetUserName(String username){
        this.username = username;
    }
    public void SetUserPassword(String password){
        this.password = password;
    }

    public boolean checkBlanks(boolean b, String s){
        if(b==true){
            if(!(s.equals(""))){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    /* There are three operations
     * 1. Get User Status
     * 2. Issue User Book
     * 3. Return Book From User
     * Each operation requires to get at the respective row in excel database.
     * So this operation should be common in code which will return the current row index and
     * retain the excel file open to write or to read
    */

   /* (LambdaExpression ->
    //public static boolean something (){

        if (cellIterator.hasNext()) {
            if (!(nextRow.getCell(cell.getColumnIndex() + 1).getStringCellValue().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


        System.out.println("Do something");
        return true;
    };)
*/





    public int UserCommonOperation(String user_name, String Operation, String book_name){
        String operation = Operation ;
        String dummyString=null;
        try {
            FileInputStream excel_file_1 = new FileInputStream("UsersInfo.xlsx");
            Workbook workbook_1 = new XSSFWorkbook(excel_file_1);
            Sheet FirstSheet_1 = workbook_1.getSheetAt(0);

            int n_users=0;
            int rowNum=0;
            int total_users = FirstSheet_1.getLastRowNum();

            Iterator<Row> rowIterator = FirstSheet_1.iterator();
            Row nextRow = rowIterator.next();   //Skip first row
            while (rowIterator.hasNext()) {

                int i = 0;
                nextRow = rowIterator.next();
                rowNum = nextRow.getRowNum();


                Iterator<Cell> cellIterator = nextRow.iterator();
                Cell cell = cellIterator.next(); //Skip first column
                cell = cellIterator.next(); //Skip Second column
                int lastColumnIndex = cell.getColumnIndex()+ 1 + maxBooksAllowed;
                if ((!(cell.getStringCellValue().equals(user_name))) & (n_users < (total_users))) {
                    n_users++;
                    if (n_users == total_users) {
                        System.out.println("\"Given user is not registered. Please register and try again.\"");
                        return 0;
                    } else {
                        continue;
                    }
                }else{
                    ArrayList<String> user_book_list = new ArrayList<>();
                    cell = cellIterator.next(); //Skip third column
                    switch (operation){
                        case "Get_User_Status":
                                while (cellIterator.hasNext()) {// & (!nextRow.getCell(cell.getColumnIndex()+1).getStringCellValue().equals(""))) {// & (!(cellIterator.next().getStringCellValue().equals("")))){
                                    if (!(nextRow.getCell(cell.getColumnIndex() + 1).getStringCellValue().equals(""))) {
                                        cell = cellIterator.next();
                                        switch (cell.getCellType()) {
                                            case Cell.CELL_TYPE_STRING:
                                                user_book_list.add(cell.getStringCellValue());
                                                break;
                                            default:
                                                System.out.println("You have entered in default case in Get_User_Status switch case");
                                                return 0;
                                            //break;
                                        }
                                    }else{
                                        break;
                                    }
                                }
                            for(String booknameString:  user_book_list){
                                System.out.print(booknameString + "\t\t");
                            }
                           // break;
                            return 1;
                        case "Issue_Book":
                            while (cell.getColumnIndex()!=lastColumnIndex) {
                                if(cellIterator.hasNext()){
                                    BlankOrNull = true;
                                    PossibleBlankString = nextRow.getCell(cell.getColumnIndex() + 1).getStringCellValue();
                                }else{
                                    BlankOrNull = false;
                                }
                                if (cellIterator.hasNext() & checkBlanks(BlankOrNull,PossibleBlankString)){
                                cell = cellIterator.next();
                                System.out.println("Current cell content: " + cell.getStringCellValue());
                                if (cell.getStringCellValue() != null) {
                                    if (cell.getStringCellValue().compareTo(book_name) == 0) {
                                        //if(cell.getStringCellValue().compareTo(book_name)==1){
                                        System.out.println("Sorry the book can not be issued. You already have one copy of it");
                                        return 0;
                                        //break;
                                    } else if ((cell.getColumnIndex() == (lastColumnIndex))) {
                                        System.out.println("Sorry you already have maximum number of books issued. Pls return some to get some!");
                                        return 0;
                                        //break;
                                    }
                                }
                            }else {
                                        System.out.println("Now current column index: "+ cell.getColumnIndex());
                                        try {
                                            FileOutputStream fos = new FileOutputStream(new File("UsersInfo.xlsx"));
                                            int tempCellIndex = cell.getColumnIndex()+1;
                                            cell = FirstSheet_1.getRow(nextRow.getRowNum()).getCell(tempCellIndex);
                                            if(cell == null){
                                                cell = nextRow.createCell(tempCellIndex);
                                            }
                                            cell.setCellValue(book_name);
                                            workbook_1.write(fos);
                                            fos.close();
                                            return  1;
                                        } catch (IOException e) {
                                            System.out.println("Entered in IOException while writing excel file");
                                            return 0;
                                        }
                                    }
                            }
                            break;

                        case "Return_Book":
                            while (cellIterator.hasNext()){// | (!nextRow.getCell(cell.getColumnIndex()+1).getStringCellValue().equals(""))) {
                                cell = cellIterator.next();
                                if (cell.getStringCellValue().compareTo(book_name) != 0) {
                                    //Do Something
                                } else {
                                    switch (cell.getCellType()) {
                                        case Cell.CELL_TYPE_STRING:
                                            for(int k=cell.getColumnIndex();k<(lastColumnIndex+1);k++){
                                                if(cellIterator.hasNext()){
                                                    cell.setCellValue(nextRow.getCell(k+1).getStringCellValue());
                                                    cell = cellIterator.next();
                                                    System.out.println("k value: " + k);
                                                }else{

                                                    //nextRow.removeCell(nextRow.getCell(k));
                                                    cell = nextRow.createCell(k);
                                                    //System.out.println("k value: " + k);
                                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                                    cell.setCellValue(dummyString);
                                                    cell = nextRow.getCell(k,Row.RETURN_BLANK_AS_NULL);
                                                    //System.out.println("Printing value: "+cell.getStringCellValue());
                                                    System.out.println("It should return 1");
                                                    break;
                                                }
                                            }
                                            //workbook_1.write(fos);
                                            excel_file_1.close();
                                            FileOutputStream fos = new FileOutputStream(new File("UsersInfo.xlsx"));
                                            workbook_1.write(fos);
                                            fos.close();
                                            System.out.println("It should return 1 at the end");
                                            return 1;
                                            //break;
                                        default:
                                            System.out.println("You have entered in default case in Issue_Book switch case");
                                            return 0;
                                            //break;
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.println("Something went terribly wrong");
                            return 0;
                            //break;
                    }
                }
            }
            excel_file_1.close();
            workbook_1.close();
        }catch(IOException e){
            System.out.println("Program entered in IOException");
            return 0;
        }
        return 1;
    }


    public static void main(String []args){
        String tempUserName;
        int operation;
        String operationString=null;
        String bookname;
        int bookOption;
        String bookOptionString=null;

        UserProfile userProfile = new UserProfile();

        if(1==1){
        System.out.println("Enter user name");
        Scanner scanIn = new Scanner(System.in);
        tempUserName = scanIn.next();
        System.out.println("Enter the option 1.Get Status 2. Issue Book 3. Return Book");
        operation = scanIn.nextInt();
        switch(operation){
            case 1:
                operationString = "Get_User_Status";
                userProfile.UserCommonOperation(tempUserName,operationString,null);
                break;
            case 2:
                operationString = "Issue_Book";
                System.out.println("Enter the option to issue book 1. Inferno 2. Deception Point 3. The Lost Symbol 4. Mein Kamf 5. Open");
                bookOption = scanIn.nextInt();
                switch(bookOption){
                    case 1: bookOptionString = "Inferno"; break;
                    case 2: bookOptionString = "Deception Point"; break;
                    case 3: bookOptionString = "The Lost Symbol"; break;
                    case 4: bookOptionString = "Mein Kamf"; break;
                    case 5: bookOptionString = "Open"; break;
                    default: System.out.println("You have entered wrong book option"); break;
                }
                if(bookOptionString != null) {
                    userProfile.UserCommonOperation(tempUserName, operationString, bookOptionString);
                }
                break;
            case 3:
                operationString = "Return_Book";
                System.out.println("Enter the option to return book 1. Inferno 2. Deception Point 3. The Lost Symbol 4. Mein Kamf 5. Open");
                bookOption = scanIn.nextInt();
                switch(bookOption){
                    case 1: bookOptionString = "Inferno"; break;
                    case 2: bookOptionString = "Deception Point"; break;
                    case 3: bookOptionString = "The Lost Symbol"; break;
                    case 4:bookOptionString = "Mein Kamf"; break;
                    case 5: bookOptionString = "Open"; break;
                    default: System.out.println("You have entered wrong book option"); break;
                }
                if(bookOptionString !=null) {
                    userProfile.UserCommonOperation(tempUserName, operationString,bookOptionString);
                }
                break;
            default: System.out.println("You have entered wrong option"); break;
        }

    }else{
            //userProfile.UserCommonOperation("sanket.achari", "Issue_Book","Opene");
            //userProfile.UserCommonOperation("sanket.achari", "Issue_Book","The Lost Symbol");
            //userProfile.UserCommonOperation("sanket.achari", "Get_User_Status",null);
            //userProfile.UserCommonOperation("vishwajeet.vatharkar", "Issue_Book","Collosal");
            //userProfile.UserCommonOperation("sandesh.achari", "Return_Book","Inferno");
            //userProfile.UserCommonOperation("sanket.achari", "Return_Book","The Lost Symbol");
            userProfile.UserCommonOperation("vishwajeet.vatharkar", "Issue_Book","Inferno");
        }
    }

}
