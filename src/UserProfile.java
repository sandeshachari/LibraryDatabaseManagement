/*
import com.sun.istack.internal.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
*/

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

interface UserInterface{
    //String GetUserName();
    void RegisterNewUser(String user_name, String user_password, Object O);
    void DeleteUser(String user_name);
    String GetUserPassword(String user_name);
    void ChangeUserPassword(String user_name);
    void SetUserName(String username);
    void SetUserPassword(String password);
    void GetUserStatus(String user_name);
    void IssueBook(String user_name,String book_name);
    void ReturnBook(String user_name,String book_nam);
}


public class UserProfile implements UserInterface{
    public String username;
    public String password;
    int maxBooksAllowed = 5;
    /*
    public String GetUserName(){
        return this.username;
    }
    */
    public void DeleteUser(String user_name){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec
            System.out.println("Are you sure you want to delte your account? 1. Yes     2. No");
            Scanner scanIn = new Scanner(System.in);
            int confirmation = scanIn.nextInt();
            if(confirmation==1) {
                stmt.executeUpdate("delete from UserInfo where UserId = " + "'" + user_name + "'");
                stmt.executeUpdate("delete from UserBooksInfo where UserId = " + "'" + user_name + "'");
                System.out.println("Your have been removed from database successfully!");
            }

            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }
    }

    public void RegisterNewUser(String user_name, String user_password, Object O){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec


            stmt.executeUpdate("insert into UserInfo values("+"'"+user_name+"'"+","+"'"+user_password+"'"+","+"'" + O + "'" + ")");
            stmt.executeUpdate("insert into UserBooksInfo values("+"'"+user_name+"'"+"," +null+","+null+"," + null + "," + null + "," + null + ")");

            ResultSet rs1 = stmt.executeQuery("select Password from UserInfo where UserId = "+"'"+user_name+"'");
            while(rs1.next()){
                //System.out.println("Your password is: "+rs1.getString("Password"));
                String currentPassword =rs1.getString("Password");
                rs1.close();
            }

        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }
    }

    public String GetUserPassword(String user_name){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec

            ResultSet rs1 = stmt.executeQuery("select Password from UserInfo where UserId = "+"'"+user_name+"'");
            while(rs1.next()){
                //System.out.println("Your password is: "+rs1.getString("Password"));
                String currentPassword =rs1.getString("Password");
                rs1.close();
                return currentPassword;
            }

        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }
        return "PasswordNotFound";
    }

    public void ChangeUserPassword(String user_name){
        Connection conn = null;
        Statement stmt = null;
        String get_user_password;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec
            System.out.println("Enter old password");
            Scanner scanIn = new Scanner(System.in);
            if(scanIn.next().equals(this.GetUserPassword(user_name))){
                System.out.println("Enter new Password");
                stmt.executeUpdate("update UserInfo set Password = "+"'"+scanIn.next()+"'"+ " where UserId = " + "'" + user_name + "'");
                System.out.println("Password updated successfully!");
            }else{
                System.out.println("Password does not matched");
            }
            //scanIn.close();
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }
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


    public void GetUserStatus(String user_name){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec

            ResultSet rs1 = stmt.executeQuery("select * from UserBooksInfo where UserId = "+"'"+user_name+"'");
            while(rs1.next()){
                System.out.print("1. "+ rs1.getString("Book1")+"\t\t");
                System.out.print("2. "+ rs1.getString("Book2")+"\t\t");
                System.out.print("3. "+ rs1.getString("Book3")+"\t\t");
                System.out.print("4. " + rs1.getString("Book4") + "\t\t");
                System.out.print("5. " + rs1.getString("Book5"));
            }
            rs1.close();
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }
    }
    public void IssueBook(String user_name,String book_name){
        boolean fHaveBook = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec

            ResultSet rs2 = stmt.executeQuery("select * from UserBooksInfo where UserId = " + "'" + user_name + "'");
            while (rs2.next()) {
                int i = 1;
                //System.out.println("Username: " + rs2.getString("UserId"));
                for (i = 1; i < (maxBooksAllowed + 1); i++) {
                    //System.out.println("Book " + i + " : " + rs2.getString("Book" + i));
                    if (rs2.getString("Book" + i) == null) {
                        break;
                    }
                    if (rs2.getString("Book" + i).equals(book_name)) {
                        fHaveBook = true;
                    }
                }
                //System.out.println("Book having status: " + fHaveBook);
                if (i < 6) {
                    if (fHaveBook == true) {
                        System.out.println("The book can't be issued as already have it.");
                    } else {
                        fHaveBook = false;
                        stmt.executeUpdate("update UserBooksInfo set Book" + i + "=" + "'" + book_name + "'" + " where UserId = " + "'" + user_name + "'");
                        System.out.println("Book issued successfully!");
                    }
                } else {
                    System.out.println("You already have maximum number of books allowed. Return to Issue!");
                }
            }
            rs2.close();
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }

        }
    public void ReturnBook(String user_name,String book_name){
        int matchedBookIndex=0,lastBookIndex=0;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec
            ResultSet rs2 = stmt.executeQuery("select * from UserBooksInfo where UserId = " + "'" + user_name + "'");
            while (rs2.next()) {
                int i = 1;
                //System.out.println("Username: " + rs2.getString("UserId"));
                for (i = 1; i <= maxBooksAllowed; i++) {
                    //System.out.println("Book " + i + " : " + rs2.getString("Book" + i));
                    if (rs2.getString("Book" + i) == null) {
                        lastBookIndex = i-1;
                        break;
                    }else{
                        lastBookIndex = 5;
                    }
                    if (rs2.getString("Book" + i).equals(book_name)) {
                        matchedBookIndex = i;
                    }
                }
                System.out.println("matchedBookIndex: "+matchedBookIndex);
                System.out.println("lastBookIndex: "+lastBookIndex);
                if(matchedBookIndex==0){
                    System.out.println("The book is not issued to you. Verify!");
                }else{
                    if(i==5){
                        stmt.executeUpdate("update UserBooksInfo set Book5 =" + null + " where  UserId = " + "'" + user_name + "'");
                    }else{
                        for (int j=matchedBookIndex;j<lastBookIndex;j++){
                            ResultSet rs = stmt.executeQuery("select Book" + (j+1) + " from UserBooksInfo where UserId = " + "'" + user_name + "'");
                            stmt.executeUpdate("update UserBooksInfo set Book"+j+"="+"'"+rs.getString("Book"+(j+1))+"'"+"where UserId = "+ "'" + user_name + "'");
                        }
                        stmt.executeUpdate("update UserBooksInfo set Book"+lastBookIndex+"="+ null+" where  UserId = "+ "'" + user_name + "'");
                    }
                }
            }

            //ResultSet rs3 = stmt.executeQuery("select * from UserBooksInfo where UserId = " + "'" + user_name + "'");
            rs2.close();
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("Entered in SQLException");
        }catch(ClassNotFoundException ce){
            ce.printStackTrace();
            System.out.println("Entered in ClassNotFoundException");
        }

    }

    public static void main(String []args) {
        String tempUserName;
        int operation;
        String bookname;
        int bookOption;
        String bookOptionString = null;
        boolean userExist=false, passwordMatched=false;


        UserProfile userProfile = new UserProfile();


        String SANDESH = new String("SANDESH");
        String SANKET = new String("SANKET");
        String VISHWAJEET = new String("VISHWAJEET");

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // Set Timeout of 30 sec

            stmt.executeUpdate("drop table if exists UserInfo");
            stmt.executeUpdate("create table UserInfo (UserId varchar(255), Password varchar(255), Name Object, primary key(UserId))");
            stmt.executeUpdate("insert into UserInfo values('sandesh.achari', 'sandy'," + "'" + SANDESH + "'" + ")");
            stmt.executeUpdate("insert into UserInfo values('sanket.achari', 'shanky'," + "'" + SANKET + "'" + ")");
            stmt.executeUpdate("insert into UserInfo values('vishwajeet.vatharkar', 'khandya'," + "'" + VISHWAJEET + "'" + ")");

            stmt.executeUpdate("drop table if exists UserBooksInfo");
            stmt.executeUpdate("create table UserBooksInfo (UserId varchar(255), Book1 varchar(255), Book2 varchar(255), Book3 varchar(255), Book4 varchar(255), Book5 varchar(255),foreign key (UserId) references UserInfo (UserId))");
            stmt.executeUpdate("insert into UserBooksInfo values('sandesh.achari', 'Inferno', 'Mein Kamf', 'The Lost Symbol', 'Open', 'Deception Point')");
            stmt.executeUpdate("insert into UserBooksInfo values('sanket.achari', 'The Lost Symbol' , 'Open'," + null + "," + null + "," + null + ")");
            stmt.executeUpdate("insert into UserBooksInfo values('vishwajeet.vatharkar', 'Open' , 'Inferno', 'The Lost Symbol'," + null + "," + null + ")");

            System.out.println("Enter the option: 1. Existing User 2. New User");
            Scanner scanIn = new Scanner(System.in);
            int firstOperation = scanIn.nextInt();
            if (firstOperation == 2) {
                String newUserId, newUserPassword = null;
                String newUserObject = new String();
                System.out.println("Enter UserId");
                newUserId = scanIn.next();
                boolean passwordVerified = false;
                while (passwordVerified == false) {
                    System.out.println("Enter Password");
                    newUserPassword = scanIn.next();
                    System.out.println("Enter Password again to verify");
                    if (scanIn.next().equals(newUserPassword)) {
                        passwordVerified = true;
                    }
                }
                System.out.println("Enter Object");
                newUserObject = scanIn.next();
                userProfile.RegisterNewUser(newUserId, newUserPassword, newUserObject);
                firstOperation = 1;
            }


            if (firstOperation == 1) {
                System.out.println("Enter your UserId");
                tempUserName = scanIn.next();
                ResultSet getUserId = stmt.executeQuery("select UserId from UserBooksInfo");
                while (getUserId.next()) {
                    if (getUserId.getString("UserId").equals(tempUserName)) {
                        userExist = true;
                    }
                }
                if (userExist) {
                    System.out.println("Enter your password");
                    String tempUserPassword = scanIn.next();
                    ResultSet getUserPassword = stmt.executeQuery("select Password from UserInfo");
                    while (getUserPassword.next()) {
                        if (getUserPassword.getString("Password").equals(tempUserPassword)) {
                            passwordMatched = true;
                        }
                    }
                    if (passwordMatched) {
                        System.out.println("Enter the option 1.Get Status 2. Issue Book 3. Return Book 4.Change Password  5.Delete Account");
                        operation = scanIn.nextInt();
                        switch (operation) {
                            case 1:
                                userProfile.GetUserStatus(tempUserName);
                                break;
                            case 2:
                                System.out.println("Enter the option to issue book 1. Inferno 2. Deception Point 3. The Lost Symbol 4. Mein Kamf 5. Open");
                                bookOption = scanIn.nextInt();
                                switch (bookOption) {
                                    case 1:
                                        bookOptionString = "Inferno";
                                        break;
                                    case 2:
                                        bookOptionString = "Deception Point";
                                        break;
                                    case 3:
                                        bookOptionString = "The Lost Symbol";
                                        break;
                                    case 4:
                                        bookOptionString = "Mein Kamf";
                                        break;
                                    case 5:
                                        bookOptionString = "Open";
                                        break;
                                    default:
                                        System.out.println("You have entered wrong book option");
                                        break;
                                }
                                if (bookOptionString != null) {
                                    userProfile.IssueBook(tempUserName, bookOptionString);
                                }
                                break;
                            case 3:
                                System.out.println("Enter the option to return book 1. Inferno 2. Deception Point 3. The Lost Symbol 4. Mein Kamf 5. Open");
                                bookOption = scanIn.nextInt();
                                switch (bookOption) {
                                    case 1:
                                        bookOptionString = "Inferno";
                                        break;
                                    case 2:
                                        bookOptionString = "Deception Point";
                                        break;
                                    case 3:
                                        bookOptionString = "The Lost Symbol";
                                        break;
                                    case 4:
                                        bookOptionString = "Mein Kamf";
                                        break;
                                    case 5:
                                        bookOptionString = "Open";
                                        break;
                                    default:
                                        System.out.println("You have entered wrong book option");
                                        break;
                                }
                                if (bookOptionString != null) {
                                    userProfile.ReturnBook(tempUserName, bookOptionString);
                                }
                                break;
                            case 4:
                                userProfile.ChangeUserPassword(tempUserName);
                                break;
                            case 5:
                                userProfile.DeleteUser(tempUserName);
                                break;
                            default:
                                System.out.println("You have entered wrong option");
                                break;
                        }
                    } else {
                        System.out.println("Entered password is incorrect");
                    }
                } else {
                    System.out.println("You are not registered.");
                }
            } else {
                System.out.println("You have entered incorrect option. Try again!");
            }
        }
            catch(SQLException se){
                System.out.println("Entered in SQLException");
                se.printStackTrace();
            }catch(ClassNotFoundException ce){
                System.out.println("Entered in ClassNotFoundException");
                ce.printStackTrace();
            }
        }
    }
