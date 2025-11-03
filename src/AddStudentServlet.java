package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONObject;

public class AddStudentServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            String rollNumber = request.getParameter("rollNumber");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String course = request.getParameter("course");
            String gpa = request.getParameter("gpa");
            
            if (rollNumber == null || rollNumber.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                course == null || course.trim().isEmpty() ||
                gpa == null || gpa.trim().isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(new JSONObject().put("success", false).put("message", "All fields required").toString());
                return;
            }
            
            Connection conn = DatabaseUtil.getConnection();
            if (conn == null) throw new Exception("DB connection failed");
            
            String sql = "INSERT INTO students (roll_number, first_name, last_name, email, phone, course, gpa) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, rollNumber);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, course);
            stmt.setDouble(7, Double.parseDouble(gpa));
            
            int result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(new JSONObject().put("success", true).put("message", "Student added").toString());
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(new JSONObject().put("success", false).put("message", "Failed to add").toString());
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new JSONObject().put("success", false).put("message", "Error: " + e.getMessage()).toString());
        }
    }
}
