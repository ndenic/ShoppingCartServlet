import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/shoppingcart")
public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();

		for (int i = 0; i < cookies.length; i++) {
			Cookie currentCookie = cookies[i];
			String name = currentCookie.getName();
			String value = currentCookie.getValue();

			System.out.println("Recived the coookie " + name + " = " + value);
		}

		// Preuzimanje ili kreiranje objekta sesije
		HttpSession session = request.getSession(true);

		// Pristupanje narudzbenici
		ArrayList<Book> myShoppingCart = (ArrayList<Book>) session.getAttribute("shoppingcart");

		if (myShoppingCart == null) {
			// Ovo je prvi poziv - kreiranje instance narudzbenice
			myShoppingCart = new ArrayList<>();
		}

		// Kreiranje instance objekta knjige na osnovu unetih parametara
		Book selectedBook = new Book();
		selectedBook.title = request.getParameter("booktitle");
		selectedBook.price = Double.parseDouble(request.getParameter("price"));

		// Dodavanje knjige u narudzbenicu
		myShoppingCart.add(selectedBook);

		// Postavljanje narduzbenice nazad u objekat sesije
		session.setAttribute("shoppingcart", myShoppingCart);

		// Priprema web stranice i slanje u pregledac
		PrintWriter out = response.getWriter();

		// Dodavanje sadrzaja narudzbenice na web stranicu
		out.print("<body>Your shopping cart content:");
		myShoppingCart.forEach(book -> out.printf("<br>Title: %s, price: %.2f", book.title, book.price));

		// Dodavanje HTML forme na web stranicu
		out.println("<p>Add the book title and price to the shopping cart:</p>");
		out.println("<form action='shoppingcart' method='get'>");
		out.println("<input type='text' name='booktitle'>");
		out.println("<input type='text' name='price'>");
		out.println("<input type='submit' value='Add to shopping cart'>");
		out.println("<input type='submit' value='Place order' name='placeorder'");
		out.println("</form>");
		out.println("</body>");
		
		//Zatvaranje sesije
		if(request.getParameter("placeorder") != null) {
			session.invalidate();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
