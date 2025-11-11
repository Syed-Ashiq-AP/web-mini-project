package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet for adding vehicle service records to the database
 * Handles POST requests to create new vehicle service entries
 */
public class AddVehicleServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Retrieve and validate form parameters
            String vehicleId = getParameter(request, "vehicleId");
            String ownerName = getParameter(request, "ownerName");
            String vehicleModel = getParameter(request, "vehicleModel");
            String registrationNumber = getParameter(request, "registrationNumber");
            String contactNumber = getParameter(request, "contactNumber");
            String email = getParameter(request, "email");
            String serviceType = getParameter(request, "serviceType");
            String mileageStr = getParameter(request, "mileage");
            
            // Validate all required fields
            if (!isValidInput(vehicleId, ownerName, vehicleModel, registrationNumber, 
                              contactNumber, email, serviceType, mileageStr)) {
                sendErrorResponse(response, out, HttpServletResponse.SC_BAD_REQUEST, 
                                "All fields are required and must not be empty");
                return;
            }
            
            // Parse mileage
            int mileage;
            try {
                mileage = Integer.parseInt(mileageStr);
                if (mileage < 0) {
                    throw new NumberFormatException("Mileage cannot be negative");
                }
            } catch (NumberFormatException e) {
                sendErrorResponse(response, out, HttpServletResponse.SC_BAD_REQUEST, 
                                "Invalid mileage value");
                return;
            }
            
            // Get database connection
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                throw new ServletException("Failed to establish database connection");
            }
            
            // Prepare SQL statement
            String sql = "INSERT INTO vehicles (vehicle_id, owner_name, vehicle_model, " +
                        "registration_number, contact_number, email, service_type, mileage) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, vehicleId);
            stmt.setString(2, ownerName);
            stmt.setString(3, vehicleModel);
            stmt.setString(4, registrationNumber);
            stmt.setString(5, contactNumber);
            stmt.setString(6, email);
            stmt.setString(7, serviceType);
            stmt.setInt(8, mileage);
            
            // Execute insert operation
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                sendSuccessResponse(response, out, "Vehicle service record added successfully");
            } else {
                sendErrorResponse(response, out, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                "Failed to add vehicle service record");
            }
            
        } catch (Exception e) {
            sendErrorResponse(response, out, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                            "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up resources
            DatabaseUtil.closeStatement(stmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Safely retrieves and trims a parameter from the request
     */
    private String getParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value != null ? value.trim() : null;
    }
    
    /**
     * Validates that all input parameters are non-null and non-empty
     */
    private boolean isValidInput(String... params) {
        for (String param : params) {
            if (param == null || param.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sends a success JSON response
     */
    private void sendSuccessResponse(HttpServletResponse response, PrintWriter out, String message) {
        response.setStatus(HttpServletResponse.SC_OK);
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("message", message);
        out.println(json.toString());
    }
    
    /**
     * Sends an error JSON response
     */
    private void sendErrorResponse(HttpServletResponse response, PrintWriter out, 
                                   int statusCode, String message) {
        response.setStatus(statusCode);
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("message", message);
        out.println(json.toString());
    }
}
