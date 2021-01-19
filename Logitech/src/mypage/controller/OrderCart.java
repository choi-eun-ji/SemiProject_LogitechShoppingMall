package mypage.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import common.controller.AbstractController;
import product.model.*;

public class OrderCart extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String usernum = "9";
		InterProductDAO pdao = new ProductDAO();

		ArrayList<ProductVO> cartprolist = pdao.selectCartProduct(usernum);
		ArrayList<ArrayList<String>> colorlist = new ArrayList<>();
		ArrayList<String> idlist = new ArrayList<>();
		request.setAttribute("cartprolist", cartprolist);

		for (int i = 0; i < cartprolist.size(); i++) {
			ArrayList<String> arrlist = pdao.selectcolor(cartprolist.get(i).getPcvo().getFk_productid());
			colorlist.add(arrlist);
		}
		request.setAttribute("colorlist", colorlist);

		String seqcart = request.getParameter("seqcart");
		if (seqcart != null) {
			String[] seqcartarr = seqcart.split(",");

			for (int i = 0; i < seqcartarr.length; i++) {
				pdao.deleteCart(seqcartarr[i]);
			}
		}

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/mypage/mypage_cart.jsp");

	}

}
