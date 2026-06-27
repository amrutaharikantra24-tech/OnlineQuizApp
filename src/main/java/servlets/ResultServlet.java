package servlets;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int score = (int) session.getAttribute("score");
        int total = (int) session.getAttribute("total");
        String resultHTML = (String) session.getAttribute("resultHTML");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head>");
        out.println("<title>Result</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<style>");
        out.println("html, body { overflow-y: scroll !important; height: auto !important; display: block !important; }");
        out.println(".answer-box { padding: 15px 20px; margin-bottom: 15px; border-radius: 12px; }");
        out.println(".correct { background: #e8f8e8; border-left: 5px solid #27ae60; }");
        out.println(".wrong { background: #fde8e8; border-left: 5px solid #e74c3c; }");
        out.println(".qtext { font-weight: bold; color: #2c3e50; margin-bottom: 8px; }");
        out.println(".correct-text { color: #27ae60; font-weight: bold; }");
        out.println(".wrong-text { color: #e74c3c; font-weight: bold; }");
        out.println(".score-box { background: linear-gradient(135deg, #f0e4ff, #e4d0f8); padding: 20px; border-radius: 12px; text-align: center; margin-bottom: 25px; border: 2px solid #c9a8e8; }");
        out.println(".score-box h2 { color: #6a3fa0; font-size: 28px; }");
        out.println(".score-box p { font-size: 22px; color: #5a2d91; font-weight: bold; margin-top: 10px; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='quiz-box'>");
        out.println("<div class='score-box'>");
        out.println("<h2>Quiz Result</h2>");
        out.println("<p>You scored " + score + " out of " + total + "</p>");
        out.println("</div>");
        out.println(resultHTML);
        out.println("<br><a href='dashboard.html'><button class='quiz-box button'>Back to Dashboard</button></a>");
        out.println("&nbsp;<a href='index.html'><button class='logout'>Logout</button></a>");
        out.println("</div></body></html>");
    }
}