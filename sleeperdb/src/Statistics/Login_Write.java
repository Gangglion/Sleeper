package Statistics;

import java.sql.*;

public class Login_Write {
    private static Login_Write vision = new Login_Write();

    public static Login_Write getWrite() {
        return vision;
    }
    private String returns = "";
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private PreparedStatement pstmt2 = null;
    private ResultSet rs = null;

    private String url="jdbc:mariadb://glion.ddns.net:3306/Sleeper?user=glion&password=setterGetter4s^g";

    public String Write(String userId,String userNick,String nowDate)
    {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            String sql = "insert into Member value('"+ userId+"','"+userNick+"','"+nowDate+"')";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            returns = "success";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return returns;
    }
}
