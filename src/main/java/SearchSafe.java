import java.sql.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class SearchSafe {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";

    public static void main(String[] args) throws Exception {
        DriverManager.setLoginTimeout(5);

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = "SELECT id, name, program, gpa FROM student WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setQueryTimeout(10);
            ps.setString(1, "%" + args[0] + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String program = rs.getString("program");
                    double GPA = rs.getDouble("gpa");

                    System.out.printf("Student #%d · %s · program %s · GPA %.2f%n", id, name, program, GPA);
                }
            }
        }
    }
}