package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewStudentsServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            String search = request.getParameter("search");
            
            Connection conn = DatabaseUtil.getConnection();
            if (conn == null) throw new Exception("DB connection failed");
            
            String sql = "SELECT * FROM students";
            if (search != null && !search.trim().isEmpty()) {
                sql += " WHERE first_name LIKE ? OR last_name LIKE ? OR roll_number LIKE ? OR course LIKE ?";
            }
            sql += " ORDER BY roll_number";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
                stmt.setString(4, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            JSONArray students = new JSONArray();
            
            while (rs.next()) {
                JSONObject student = new JSONObject();
                student.put("rollNumber", rs.getString("roll_number"));
                student.put("firstName", rs.getString("first_name"));
                student.put("lastName", rs.getString("last_name"));
                student.put("email", rs.getString("email"));
                student.put("phone", rs.getString("phone"));
                student.put("course", rs.getString("course"));
                student.put("gpa", rs.getDouble("gpa"));
                students.put(student);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            JSONObject response_obj = new JSONObject();
            response_obj.put("students", students);
            out.println(response_obj.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new JSONObject().put("students", new JSONArray()).toString());
        }
    }
}
