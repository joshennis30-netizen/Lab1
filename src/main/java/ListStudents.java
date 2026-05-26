import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class ListStudents {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";
}
