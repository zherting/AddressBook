package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Address;
import model.Contact;

/**
 * Servlet implementation class addressNavigationServlet
 */
@WebServlet("/addressNavigationServlet")
public class addressNavigationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addressNavigationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AddressHelper ah = new AddressHelper();
		ContactHelper ch = new ContactHelper();
		String act = request.getParameter("doThisToItem");

		if (act == null) {
			// no button has been selected
			getServletContext().getRequestDispatcher("/viewAllAddressServlet").forward(request, response);

		} 
		else if (act.equals("Delete")) {
			try {
				int tempId = Integer.parseInt(request.getParameter("id"));
				System.out.print("tempId: " + tempId);
				int contId = Integer.parseInt(request.getParameter("contId"));
				System.out.print("contId: " + contId);
				Contact cont = ch.searchForContactsById(contId);
				Address addressToDelete = ah.searchForAddressById(tempId);
				cont.removeAddress(addressToDelete);
				ch.updateContacts(cont);
				System.out.println("worked");

			} catch (NumberFormatException e) {
				System.out.println("Forgot to click a button");
			} finally {
				getServletContext().getRequestDispatcher("/viewAllAddressServlet").forward(request, response);
			}

		} 
		else if (act.equals("Edit")) {
			try {
				Integer tempId = Integer.parseInt(request.getParameter("id"));
				Contact contactToEdit = ch.searchForContactsById(tempId);
				List<Address> allAddresses = ah.showAllAddresses();
				List<Address> currentAddress = contactToEdit.getContactAddresses();

				for (int i = 0; i < allAddresses.size(); i++) {
					for (int j = 0; j < currentAddress.size(); j++) {
						if (allAddresses.get(i).getAddressId() == allAddresses.get(j).getAddressId()) {
							allAddresses.remove(i);
						}
					}
				}

				request.setAttribute("contactToEdit", contactToEdit);
				request.setAttribute("allAddressToAdd", allAddresses);
				getServletContext().getRequestDispatcher("/edit-address.jsp").forward(request, response);
			} catch (NumberFormatException e) {
				getServletContext().getRequestDispatcher("/viewAllAddressServlet").forward(request, response);
			}

		} 
		else if (act.equals("Add New")) {
			getServletContext().getRequestDispatcher("/addAddressForContactsServlet").forward(request, response);
		}
		else {
			System.out.println("Bad Parameter Passed");
		}
	}

}
