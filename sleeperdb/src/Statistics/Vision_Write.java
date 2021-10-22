package Statistics;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Vision_Write {
  private static Vision_Write vision = new Vision_Write();

  public static Vision_Write getWrite() {
    return vision;
  }
  private String returns = "";
  private Connection conn = null;
  private PreparedStatement pstmt = null;
  private PreparedStatement pstmt2 = null;
  private ResultSet rs = null;
  //로컬용 테스트
  //private String url="jdbc:mariadb://localhost:3307/localtest?user=root&password=root";

  //remote test
  private String url="jdbc:mariadb://glion.ddns.net:3306/localtest?user=glion&password=setterGetter4s^g";
  public String write(String content) {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      conn = DriverManager.getConnection(url);

      Statement stmt = conn.createStatement();
      String seq = "select max(num) from market_vision";
      ResultSet rs = stmt.executeQuery(seq);

      int num = -1;
      if (rs.next())
        num = rs.getInt(1);
      num++;

      String nowTime = getCurrentTime("YYYY,M,d");
      System.out.println(nowTime);

      System.out.println("시간확인" + nowTime);

      String sql = "insert into market_vision(num,day,content) values('"+ num +"','" + nowTime + "','" + content + "')";
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
  public static String getCurrentTime(String timeFormat) {
    return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
  }
}