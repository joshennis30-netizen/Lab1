import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;


public class ListStudents {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";

    public static void main(String[] args) throws Exception{
        DriverManager.setLoginTimeout(5);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
        Statement stmt = conn.createStatement()) {
            stmt.setQueryTimeout(10);
            System.out.printf("%3s %-16s %-4s %4s%n", "id", "name", "prog", "gpa");
            System.out.println("-".repeat(30));
            try (ResultSet rs = stmt.executeQuery("SELECT id, name, program, gpa FROM student ORDER BY id")) {
                while (rs.next()) {
                    System.out.printf("%3d %-16s %-4s %5.2f%n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("program"),
                            rs.getDouble("gpa"));
                }
            }
        }
    }
}
