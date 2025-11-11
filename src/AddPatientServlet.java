package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONObject;

public class AddPatientServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            String patientId = request.getParameter("patientId");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String status = request.getParameter("status");
            String temperature = request.getParameter("temperature");
            
            if (patientId == null || patientId.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                status == null || status.trim().isEmpty() ||
                temperature == null || temperature.trim().isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(new JSONObject().put("success", false).put("message", "All fields required").toString());
                return;
            }
            
            Connection conn = DatabaseUtil.getConnection();
            if (conn == null) throw new Exception("DB connection failed");
            
            String sql = "INSERT INTO patients (patient_id, first_name, last_name, email, phone, status, temperature) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, patientId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, status);
            stmt.setDouble(7, Double.parseDouble(temperature));
            
            int result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(new JSONObject().put("success", true).put("message", "Patient added").toString());
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
