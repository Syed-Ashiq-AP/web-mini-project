package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet for retrieving and displaying all products from the inventory.
 */
public class ViewProductsServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter();
        
        try {
            conn = DatabaseUtil.getConnection();
            
            if (conn == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"error\": \"Database connection failed\"}");
                return;
            }
            
            stmt = conn.createStatement();
            String sql = "SELECT product_id, product_name, category, quantity, price, supplier, status FROM products ORDER BY product_id";
            rs = stmt.executeQuery(sql);
            
            JSONArray productsArray = new JSONArray();
            
            while (rs.next()) {
                JSONObject product = new JSONObject();
                product.put("productId", rs.getString("product_id"));
                product.put("productName", rs.getString("product_name"));
                product.put("category", rs.getString("category"));
                product.put("quantity", rs.getInt("quantity"));
                product.put("price", rs.getDouble("price"));
                product.put("supplier", rs.getString("supplier"));
                product.put("status", rs.getString("status"));
                
                productsArray.put(product);
            }
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("products", productsArray);
            
            out.write(jsonResponse.toString());
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
            
        } finally {
            // Clean up resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DatabaseUtil.closeConnection(conn);
        }
    }
}
