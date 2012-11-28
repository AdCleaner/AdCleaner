package cz.cuni.adcleaner.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class DatabaseLayer {

    public void initConnection() {
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:h2:C:\\H2DB\\database\\EMPLOYEEDB", "sa", "");
            statement = connection.createStatement();
            /*
            resultSet = statement
                    .executeQuery("SELECT EMPNAME FROM EMPLOYEEDETAILS");
            while (resultSet.next()) {
                System.out.println("EMPLOYEE NAME:"
                        + resultSet.getString("EMPNAME"));
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
