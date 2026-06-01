import java.sql.*;
import java.time.Instant;

public class AddStudent {
    private static final String URL = "jdbc:h2:./data/studentdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "secret";

    public static void main(String[] args) throws Exception{

        String name = args[0].trim();
        if (name.isEmpty() || name.length() > 80) {
            errorMessage("Name must be between 1 and 80 characters");
            System.exit(1);
        }

        String program = args[1];
        if (!program.matches("^[A-Z0-9]{2,12}$")){
            errorMessage("program must be uppercase and between 2-12 letters or digits");
            System.exit(1);
        }

        double GPA = 0.0;
        try {
            GPA = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            errorMessage("GPA out of range, must be a decimal between 0.00 and 4.00, got" + args[2]);
            System.exit(1);
        }
        if (GPA < 0.0 || GPA > 4.0) {
            errorMessage("GPA must be between 0.00 and 4.00, got" + args[2]);
            System.exit(1);
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO student (name, program, gpa) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setQueryTimeout(10);
            ps.setString(1, name);
            ps.setString(2, program);
            ps.setDouble(3, GPA);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.printf("Inserted student #%d · %s · program %s · GPA %.2f%n", id, args[0], args[1], GPA);
                }
            }
        }
    }
    public static void errorMessage(String message){
        System.err.printf("%s | ERROR | AddStudent | message=%s%n", Instant.now(), message);
    }
}