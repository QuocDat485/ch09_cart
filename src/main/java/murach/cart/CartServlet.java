package murach.cart;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.data.*;
import murach.business.*;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletContext sc = getServletContext();
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";  // default action
        }

        // perform action and set URL to appropriate page
        String url = "/index.jsp";
        if (action.equals("shop")) {            
            url = "/index.jsp";    // the "index" page
        } 
        else if (action.equals("cart")) {
        	String mode = request.getParameter("mode");
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart;
            
            synchronized(session) {
            	 cart = (Cart) session.getAttribute("cart");
                 if (cart == null) {
                     cart = new Cart();
                     session.setAttribute("cart", cart);
                 }
            }
           

            int quantity;
            boolean isSpecificQuantity = false;
            if (quantityString != null && !quantityString.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        quantity = 1; // Reset to 1 if negative
                    } else {
                        isSpecificQuantity = true; // Người dùng nhập số lượng cụ thể
                    }
                } catch (NumberFormatException nfe) {
                    quantity = 1; // Mặc định thêm 1 nếu không hợp lệ
                }
            } else {
                quantity = 1; // Mặc định thêm 1 nếu không có quantityString
            }

            String path = sc.getRealPath("/WEB-INF/products.txt");
            Product product = ProductIO.getProduct(productCode, path);

            LineItem lineItem = new LineItem();
            lineItem.setProduct(product);
            lineItem.setQuantity(quantity);
            if("update".equals(mode)) {
            	cart.updateItemQuantity(lineItem, quantity);
            }
            else {
	            if (quantity > 0) {    
	                cart.addItem(lineItem);
	            } else if (quantity == 0) {
	                cart.removeItem(lineItem);
	            }
            }
            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        }
        else if (action.equals("checkout")) {
            url = "/checkout.jsp";
        }

        sc.getRequestDispatcher(url)
                .forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}