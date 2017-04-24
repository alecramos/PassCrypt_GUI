package passcrypt_gui;

import java.sql.*;
import java.util.Scanner;

public class dbCon {

    public void runMe(String host, String database, String user, String password)
            throws Exception {

        /* run driverTest method shown below */
        driverTest();

        /* make the connection to the database */
        Connection conMe = makeCon(host, database, user, password);

        /* now run a select query of the intended database */
        exeQuery(conMe, "SELECT * FROM Students");

        /* close the database */
        conMe.close();
    }

    protected void driverTest() throws Exception {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL Driver Found");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found ... ");
            throw (e);
        }
    }

    protected Connection makeCon(String host, String database, String user, String password)
            throws Exception {

        String url = "";
        try {
            url = "jdbc:mysql://" + host + ":3306/" + database;
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established to " + url + "...");
            return con;
        } catch (java.sql.SQLException e) {
            System.out.println("Connection couldn't be established to " + url);
            throw (e);
        }
    }

    protected void exeQuery(Connection con, String sqlStatement)
            throws Exception {
        Scanner add = new Scanner(System.in);
        String userInput = ""; 
        while(!userInput.equals("q"))
        {
            try 
            {
                System.out.println("Press ' v' to view the table, press ' i ' to add a row, or press ' u ' to update, q to quit.");
                  // for adding/inserting a row    
                userInput = add.nextLine();     // same ^ 
                Statement cs = con.createStatement();
                ResultSet sqls = cs.executeQuery(sqlStatement);

                Scanner scan = new Scanner(System.in);
                int studentID = 0;
                String studentName = "";
                String studentMajor = "";
                int creditsCompleted = 0;

                if (userInput.equals("v")) {
                    while (sqls.next()) {
                        int stuID = (Integer.parseInt(sqls.getObject("StudentID").toString()));
                        String stuName = (sqls.getObject("StudentName").toString());
                        String stuMajor = (sqls.getObject("StudentMajor").toString());
                        int stuCredits = (Integer.parseInt(sqls.getObject("CreditsCompleted").toString()));
                        // for viewing info on the tables.
                        System.out.println(stuID + " | " + stuName + " | " + stuMajor + " | " + stuCredits + " credits completed "); // same ^                                                                              
                    }
                }
                if (userInput.equals("u")) {
                    //could be updated to read from console
                    System.out.print("Enter StudentID: ");
                    int userStudentID = scan.nextInt();

                    System.out.print("Enter Credits Completed: ");
                    int creditsCom = scan.nextInt();

                    String sql = "UPDATE Students SET CreditsCompleted = ? WHERE StudentID= ?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setInt(1, creditsCom);
                    statement.setInt(2, userStudentID);
                    //the order 1,2,etc refers to the sql statement and not the order 
                    //of the mysql table
                    statement.executeUpdate();

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("An existing user was updated successfully!");
                    }
                }

                if (userInput.equals("i")) {

                    System.out.print("Student ID: ");
                    studentID = scan.nextInt();

                    System.out.print("Student Name: ");
                    studentName = scan.next();

                    System.out.print("Student Major: ");
                    studentMajor = scan.next();

                    System.out.print("Credits Comlpeted: ");
                    creditsCompleted = scan.nextInt();

                    System.out.println("Inserting records into table...");
                    cs = con.createStatement();

                    String query = " INSERT INTO Students (StudentID, StudentName, StudentMajor, CreditsCompleted)"
                            + " values (?, ?, ?, ?)";

                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setInt(1, studentID);
                    statement.setString(2, studentName);
                    statement.setString(3, studentMajor);
                    statement.setInt(4, creditsCompleted);
                    statement.executeUpdate();
                }
            sqls.close();
            }
        catch (SQLException e) 
        {
             System.out.println("Error executing sql statement");
            throw (e);
        }
        }
    

}

public static void main (String args[]) throws Exception {
            dbCon a = new dbCon();
            a.runMe("rds-mysql-passcrtpt.cqxkllauwd8o.us-east-2.rds.amazonaws.com:3306", "PassCrypt_paswords", "username goes here", "you thought I'd give you a password lol");     
}
}
