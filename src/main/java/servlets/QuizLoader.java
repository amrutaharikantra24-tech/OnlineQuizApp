package servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/QuizLoader")
public class QuizLoader extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String category = request.getParameter("category");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<title>Quiz - " + category.toUpperCase() + "</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<style>");
        out.println("html, body { overflow-y: scroll !important; height: auto !important; display: block !important; }");
        out.println(".timer-box { position: fixed; top: 20px; right: 20px; background: linear-gradient(135deg, #9b6fd4, #6a3fa0); color: white; padding: 12px 25px; border-radius: 12px; font-size: 20px; font-weight: bold; z-index: 999; box-shadow: 0 5px 15px rgba(106,63,160,0.4); }");
        out.println(".timer-box.danger { background: linear-gradient(135deg, #e74c3c, #c0392b); }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='timer-box' id='timer'>30:00</div>");
        out.println("<div class='quiz-box'>");
        out.println("<h2>" + category.toUpperCase() + " Quiz</h2>");
        out.println("<form action='QuizServlet' method='post'>");
        out.println("<input type='hidden' name='category' value='" + category + "'>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quizapp", "root", "root");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM questions WHERE category=?");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            int qno = 1;
            while (rs.next()) {
                out.println("<div class='question'>");
                out.println("<p>" + qno + ". " + rs.getString("question") + "</p>");
                out.println("<label><input type='radio' name='q" + qno + "' value='A'> " + rs.getString("option_a") + "</label><br>");
                out.println("<label><input type='radio' name='q" + qno + "' value='B'> " + rs.getString("option_b") + "</label><br>");
                out.println("<label><input type='radio' name='q" + qno + "' value='C'> " + rs.getString("option_c") + "</label><br>");
                out.println("<label><input type='radio' name='q" + qno + "' value='D'> " + rs.getString("option_d") + "</label>");
                out.println("</div>");
                qno++;
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("<button type='submit'>Submit Quiz</button>");
        out.println("</form></div>");
        out.println("<script>");
        out.println("var minutes = 30, seconds = 0;");
        out.println("var timerEl = document.getElementById('timer');");
        out.println("var interval = setInterval(function() {");
        out.println("  seconds--;");
        out.println("  if (seconds < 0) { seconds = 59; minutes--; }");
        out.println("  if (minutes < 0) { clearInterval(interval); document.querySelector('form').submit(); return; }");
        out.println("  var m = minutes < 10 ? '0' + minutes : minutes;");
        out.println("  var s = seconds < 10 ? '0' + seconds : seconds;");
        out.println("  timerEl.innerHTML = m + ':' + s;");
        out.println("  if (minutes < 5) timerEl.classList.add('danger');");
        out.println("}, 1000);");
        out.println("</script>");
        out.println("</body></html>");
    }
}