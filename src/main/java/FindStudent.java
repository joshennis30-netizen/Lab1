import java.sql.*;
import java.time.Instant;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class FindStudent {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";

    public static void main(String[] args) throws Exception {
        DriverManager.setLoginTimeout(5);

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = "SELECT id, name, program, gpa FROM student WHERE id = ?";

        int theId = 0;
        try {
            theId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            errorMessage("id must be a positive integer, got " + args[0]);
            System.exit(1);
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setQueryTimeout(10);
            ps.setInt(1, theId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("%s %s %6s %-4s %-2s %4s %4.2f%n",
                            "Student #" + rs.getInt("id"),
                            "·", rs.getString("name"),
                            "· Program", rs.getString("program"),
                            "· GPA", rs.getDouble("gpa"));
                } else {
                    System.out.println("No student with id " + theId);
                }
            }
        } catch (Exception e) {
            System.err.printf("%s | ERROR | FindStudent | message=%s%n",
                    Instant.now(), e.getMessage());
        }
    }
    public static void errorMessage(String message) {
        System.err.printf("%s | ERROR | FindStudent | message=%s%n",
                Instant.now(), message);
    }
}