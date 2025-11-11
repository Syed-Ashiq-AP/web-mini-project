package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet for retrieving and displaying vehicle service records
 * Handles GET requests with optional search functionality
 */
public class ViewVehiclesServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Get optional search parameter
            String searchQuery = request.getParameter("search");
            boolean hasSearch = (searchQuery != null && !searchQuery.trim().isEmpty());
            
            // Establish database connection
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                throw new ServletException("Failed to establish database connection");
            }
            
            // Build SQL query
            String sql = buildSqlQuery(hasSearch);
            stmt = conn.prepareStatement(sql);
            
            // Set search parameters if applicable
            if (hasSearch) {
                String searchPattern = "%" + searchQuery.trim() + "%";
                for (int i = 1; i <= 5; i++) {
                    stmt.setString(i, searchPattern);
                }
            }
            
            // Execute query
            rs = stmt.executeQuery();
            JSONArray vehicles = new JSONArray();
            
            // Process results
            while (rs.next()) {
                JSONObject vehicle = createVehicleJson(rs);
                vehicles.put(vehicle);
            }
            
            // Send response
            JSONObject responseJson = new JSONObject();
            responseJson.put("vehicles", vehicles);
            responseJson.put("count", vehicles.length());
            out.println(responseJson.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("vehicles", new JSONArray());
            errorJson.put("error", "Error retrieving vehicle records: " + e.getMessage());
            out.println(errorJson.toString());
            e.printStackTrace();
        } finally {
            // Clean up resources
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(stmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Builds the SQL query with optional search conditions
     */
    private String buildSqlQuery(boolean hasSearch) {
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicles");
        
        if (hasSearch) {
            sql.append(" WHERE owner_name LIKE ? OR vehicle_model LIKE ? OR ")
               .append("vehicle_id LIKE ? OR registration_number LIKE ? OR service_type LIKE ?");
        }
        
        sql.append(" ORDER BY service_date DESC, vehicle_id ASC");
        return sql.toString();
    }
    
    /**
     * Creates a JSON object from a ResultSet row
     */
    private JSONObject createVehicleJson(ResultSet rs) throws Exception {
        JSONObject vehicle = new JSONObject();
        vehicle.put("vehicleId", rs.getString("vehicle_id"));
        vehicle.put("ownerName", rs.getString("owner_name"));
        vehicle.put("vehicleModel", rs.getString("vehicle_model"));
        vehicle.put("registrationNumber", rs.getString("registration_number"));
        vehicle.put("contactNumber", rs.getString("contact_number"));
        vehicle.put("email", rs.getString("email"));
        vehicle.put("serviceType", rs.getString("service_type"));
        vehicle.put("mileage", rs.getInt("mileage"));
        vehicle.put("serviceDate", rs.getTimestamp("service_date").toString());
        return vehicle;
    }
}
