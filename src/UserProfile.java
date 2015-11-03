
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import java.awt.print.UserProfile;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;


interface UserInterface{
    String GetUserName();
    String GetUserPassword();
    void SetUserName(String username);
    void SetUserPassword(String password);
    String GetUserStatus(String user_name);
    void IssueUserBook(String user_name,String []book_name, int n_books);
}

public class UserProfile {
    String username;
    String password;
    String []books = new String[5];

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

    public void  IssueUserProfile(String user_name,String []book_name, int n_books){
        String booknames;
        GetUserStatus(user_name);
    }

    public String GetUserStatus(String user_name){ //GetUserStatus()
        try {
            FileInputStream excel_file_1 = new FileInputStream("UsersInfo.xlsx");
            Workbook workbook_1 = new XSSFWorkbook(excel_file_1);
            Sheet FirstSheet_1 = workbook_1.getSheetAt(0);

            int total_users = FirstSheet_1.getLastRowNum();
            System.out.println("Total number of registered users of library: "+total_users);
            int n_users=0;
            Iterator<Row> rowIterator = FirstSheet_1.iterator();
            Row nextRow = rowIterator.next();   //Skip first row
            while (rowIterator.hasNext()){
                nextRow = rowIterator.next();
                int i=0;
                Iterator<Cell> cellIterator = nextRow.iterator();
                Cell cell = cellIterator.next(); //Skip first column
                cell = cellIterator.next(); //Skip Second column
                //System.out.println(cell.getStringCellValue()+ n_users);
                if((!(cell.getStringCellValue().equals(user_name))) & (n_users<(total_users))){
                    n_users++;
                    if(n_users == total_users){
                        return "Given user is not registered. Please register and try again.";
                    }else{
                        continue;
                    }
                }else{
                    cell = cellIterator.next(); //Skip third column
                    while (cellIterator.hasNext()){
                        cell = cellIterator.next();
                        switch (cell.getCellType()){
                            case Cell.CELL_TYPE_STRING:
                                //System.out.print(cell.getStringCellValue());
                                this.books[i] =  cell.getStringCellValue();
                                i++;
                                break;
                            default:
                                System.out.println("You have entered in default case in class UserProfile");
                                break;
                        }
                    }
                    break;
                }
            }
         workbook_1.close();

        }catch(IOException e){
          System.out.println("Given file not found in UserProfile class");
        }
        //return "1. "+this.books[0]+" 2. "+this.books[1]+" 3. "+this.books[2]+" 4. "+this.books[3]+" 5. "+this.books[4];
        return this.books[0]+this.books[1]+this.books[2]+this.books[3]+this.books[4];
    }

    public static void main(String []args){
        UserProfile userProfile = new UserProfile();
        System.out.println("Enter the user's name for status");
        Scanner scanIn = new Scanner(System.in);
        //System.out.println(userProfile.GetUserStatus(scanIn.next()));
    }
}
