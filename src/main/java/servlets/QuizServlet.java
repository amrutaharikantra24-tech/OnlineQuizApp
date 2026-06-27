package servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String category = request.getParameter("category");

        int score = 0;
        int total = 0;

        StringBuilder resultHTML = new StringBuilder();

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
                String userAnswer = request.getParameter("q" + qno);
                String correctAnswer = rs.getString("correct_answer");
                String question = rs.getString("question");

                boolean isCorrect = userAnswer != null && userAnswer.equals(correctAnswer);
                if (isCorrect) score++;
                total++;

                // Get correct option text
                String correctOption = "";
                if (correctAnswer.equals("A")) correctOption = rs.getString("option_a");
                else if (correctAnswer.equals("B")) correctOption = rs.getString("option_b");
                else if (correctAnswer.equals("C")) correctOption = rs.getString("option_c");
                else if (correctAnswer.equals("D")) correctOption = rs.getString("option_d");

                // Get user option text
                String userOption = "Not Answered";
                if (userAnswer != null) {
                    if (userAnswer.equals("A")) userOption = rs.getString("option_a");
                    else if (userAnswer.equals("B")) userOption = rs.getString("option_b");
                    else if (userAnswer.equals("C")) userOption = rs.getString("option_c");
                    else if (userAnswer.equals("D")) userOption = rs.getString("option_d");
                }

                resultHTML.append("<div class='answer-box " + (isCorrect ? "correct" : "wrong") + "'>");
                resultHTML.append("<p class='qtext'>" + qno + ". " + question + "</p>");
                resultHTML.append("<p>Your Answer: <span class='" + (isCorrect ? "correct-text" : "wrong-text") + "'>" + userOption + "</span></p>");
                if (!isCorrect) {
                    resultHTML.append("<p>Correct Answer: <span class='correct-text'>" + correctOption + "</span></p>");
                }
                resultHTML.append("</div>");
                qno++;
            }

            // Save result
            PreparedStatement save = con.prepareStatement(
                "INSERT INTO results (username, score, total) VALUES (?, ?, ?)");
            save.setString(1, username);
            save.setInt(2, score);
            save.setInt(3, total);
            save.executeUpdate();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Store in session
        session.setAttribute("resultHTML", resultHTML.toString());
        session.setAttribute("score", score);
        session.setAttribute("total", total);

        response.sendRedirect("ResultServlet");
    }
}