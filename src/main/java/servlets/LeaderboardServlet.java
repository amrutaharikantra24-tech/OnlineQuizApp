package servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/LeaderboardServlet")
public class LeaderboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head>");
        out.println("<title>Leaderboard</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<style>");
        out.println("html, body { overflow-y: scroll !important; height: auto !important; display: block !important; }");
        out.println(".leaderboard { width: 700px; margin: 40px auto; background: rgba(255,255,255,0.92); padding: 40px; border-radius: 24px; box-shadow: 0 15px 40px rgba(140,80,210,0.22); }");
        out.println(".leaderboard h2 { color: #6a3fa0; text-align: center; font-size: 28px; margin-bottom: 25px; padding-bottom: 15px; border-bottom: 2px solid #e8d5f5; }");
        out.println("table { width: 100%; border-collapse: collapse; }");
        out.println("th { background: linear-gradient(135deg, #9b6fd4, #6a3fa0); color: white; padding: 12px 15px; text-align: left; }");
        out.println("td { padding: 12px 15px; border-bottom: 1px solid #e8d5f5; color: #4a2080; }");
        out.println("tr:hover td { background: #f8f0ff; }");
        out.println(".rank1 td { background: #fff8e1; font-weight: bold; }");
        out.println(".rank2 td { background: #f5f5f5; }");
        out.println(".rank3 td { background: #fff3e0; }");
        out.println(".medal { font-size: 20px; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='leaderboard'>");
        out.println("<h2>Leaderboard</h2>");
        out.println("<table>");
        out.println("<tr><th>Rank</th><th>Username</th><th>Score</th><th>Total</th><th>Date</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quizapp", "root", "root");

            PreparedStatement ps = con.prepareStatement(
                "SELECT username, score, total, date FROM results ORDER BY score DESC LIMIT 20");
            ResultSet rs = ps.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String rowClass = "";
                String medal = rank + "";
                if (rank == 1) { rowClass = "rank1"; medal = "🥇"; }
                else if (rank == 2) { rowClass = "rank2"; medal = "🥈"; }
                else if (rank == 3) { rowClass = "rank3"; medal = "🥉"; }

                out.println("<tr class='" + rowClass + "'>");
                out.println("<td class='medal'>" + medal + "</td>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getInt("score") + "</td>");
                out.println("<td>" + rs.getInt("total") + "</td>");
                out.println("<td>" + rs.getString("date") + "</td>");
                out.println("</tr>");
                rank++;
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table><br>");
        out.println("<a href='dashboard.html'><button class='quiz-box button'>Back to Dashboard</button></a>");
        out.println("</div></body></html>");
    }
}