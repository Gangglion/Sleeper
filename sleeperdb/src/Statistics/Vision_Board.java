package Statistics;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Vision_Board{
  private static Vision_Board vision_board = new Vision_Board();

  public static Vision_Board getVision_Board() {
    return vision_board;
  }

  private String returns;
  //private String returns;
  private Connection con = null;
  private PreparedStatement pstmt = null;
  private ResultSet rs = null;

  //로컬용 테스트
  //private String url="jdbc:mariadb://localhost:3307/localtest?user=root&password=root";

  //remote test
  private String url="jdbc:mariadb://glion.ddns.net:3306/Sleeper?user=glion&password=setterGetter4s^g";
  public String select(String memId,String startCal,String endCal) {
    returns="";
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      con = DriverManager.getConnection(url);
      String query = "SELECT sleep_t, awake_t, CAST(sleep_date AS char) as sleep_date FROM mem_statistic where mem_id = '"+memId+"' and " +
              " sleep_date >='"+startCal+"' and + sleep_date <='"+endCal+"'order by num";
      //String query = "SELECT sleep_t, awake_t, sleep_date FROM mem_statistic";
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      while(rs.next() )
      {
        returns += rs.getString("sleep_t")+","+rs.getString("awake_t")+","+
                rs.getString("sleep_date")+"//";
      } // end while
      //테스트용 print
      System.out.println(returns);
    } catch (Exception e) {
      e.printStackTrace();
    } // end try~catch

    finally {
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
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
    return returns;
  }// end select()
  public String select(String memId) {
    returns="";
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      con = DriverManager.getConnection(url);
      String query = "SELECT sleep_t, awake_t, CAST(sleep_date AS char) as sleep_date FROM mem_statistic where mem_id = '"+memId+"'order by num";
      //String query = "SELECT sleep_t, awake_t, sleep_date FROM mem_statistic";
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      while(rs.next() )
      {
        returns += rs.getString("sleep_t")+","+rs.getString("awake_t")+","+
                rs.getString("sleep_date")+"//";
      } // end while
      //테스트용 print
      System.out.println(returns);
    } catch (Exception e) {
      e.printStackTrace();
    } // end try~catch

    finally {
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
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
    return returns;
  }// end select()

}
