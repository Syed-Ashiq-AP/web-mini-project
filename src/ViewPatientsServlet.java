package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewPatientsServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            String search = request.getParameter("search");
            
            Connection conn = DatabaseUtil.getConnection();
            if (conn == null) throw new Exception("DB connection failed");
            
            String sql = "SELECT * FROM patients";
            if (search != null && !search.trim().isEmpty()) {
                sql += " WHERE first_name LIKE ? OR last_name LIKE ? OR patient_id LIKE ? OR status LIKE ?";
            }
            sql += " ORDER BY patient_id";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
                stmt.setString(4, pattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            JSONArray patients = new JSONArray();
            
            while (rs.next()) {
                JSONObject patient = new JSONObject();
                patient.put("patientId", rs.getString("patient_id"));
                patient.put("firstName", rs.getString("first_name"));
                patient.put("lastName", rs.getString("last_name"));
                patient.put("email", rs.getString("email"));
                patient.put("phone", rs.getString("phone"));
                patient.put("status", rs.getString("status"));
                patient.put("temperature", rs.getDouble("temperature"));
                patients.put(patient);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            JSONObject response_obj = new JSONObject();
            response_obj.put("patients", patients);
            out.println(response_obj.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new JSONObject().put("patients", new JSONArray()).toString());
        }
    }
}
