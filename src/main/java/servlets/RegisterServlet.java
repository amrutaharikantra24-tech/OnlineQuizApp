
package servlets;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmpassword = request.getParameter("confirmpassword");

        // Check if passwords match
        if (!password.equals(confirmpassword)) {
            response.sendRedirect("register.html?error=passwordmismatch");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quizapp", "root", "root");

            // Check if username already exists
            PreparedStatement check = con.prepareStatement(
                "SELECT * FROM users WHERE username=?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                response.sendRedirect("register.html?error=userexists");
            } else {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                response.sendRedirect("index.html?registered=1");
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}