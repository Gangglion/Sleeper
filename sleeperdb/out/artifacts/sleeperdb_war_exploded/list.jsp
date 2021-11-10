<%--
  Created by IntelliJ IDEA.
  User: o0tmd
  Date: 2021-10-20
  Time: 오후 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="Statistics.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
  request.setCharacterEncoding("UTF-8");

  String returns="";
  String type = request.getParameter("type");
  String userId = request.getParameter("userId"); //email
  String userNick = request.getParameter("userNick");
  String startCal = request.getParameter("startCal");
  String endCal = request.getParameter("endCal");
  String startT = request.getParameter("startT");
  String endT = request.getParameter("endT");
  String nowDate = request.getParameter("nowDate");

%>
<%
  if (type == null) {
    return;
  }else if (type.equals("write_settime")) {
    System.out.println("값을받았습니다."+ userId+"\t"+ startT+"\t"+ endT+"\t"+ nowDate+"\t");
    Vision_Write vision_write = Vision_Write.getWrite();
    returns = vision_write.write(userId,startT,endT,nowDate);
    out.println(returns);
  }else if (type.equals("read_data")) {
    System.out.println("값을 리턴합니다.");
    Vision_Board vision_board = Vision_Board.getVision_Board();
    if(startCal==null)
    {
      returns = vision_board.select(userId);
      System.out.println(returns);
      System.out.println("날짜지정x 값 리턴");
    }
    else if(startCal!=null)
    {
      returns=vision_board.select(userId,startCal,endCal);
      System.out.println(returns);
      System.out.println("날짜지정o 값 리턴");
    }
    out.println(returns);
  }
%>