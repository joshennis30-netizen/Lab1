import java.sql.*;
import java.time.Instant;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UpdateGPA {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";

    public static void main(String[] args) throws Exception{
        DriverManager.setLoginTimeout(5);

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = "UPDATE student SET GPA = ? WHERE id = ?";

        int theId = 0;
        try {
            theId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            errorMessage("id must be a positive integer, got " + args[0]);
            System.exit(1);
        }

        double GPA = 0.0;
        try {
            GPA = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            errorMessage("GPA out of range, must be a decimal between 0.00 and 4.00, got\" + args[2]");
            System.exit(1);
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setQueryTimeout(10);
            ps.setDouble(1, GPA);
            ps.setInt(2, theId);

            int idExists = ps.executeUpdate();

            if (idExists == 1) {
                System.out.printf("Updated student#%d · GPA set to %.2f · 1 row changed%n", theId, GPA);
            } else {
                System.out.printf("No update · no student with id %d · 0 rows changed%n", theId);
            }
        }
    }
    public static void errorMessage(String message){
        System.err.printf("%s | ERROR | AddStudent | message=%s%n", Instant.now(), message);
    }
}
