package pertanian;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection conn;

    public static Connection getKoneksi() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pertanian_2310010463",
                    "root", "");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + e.getMessage());
        }
        return conn;
    }
}