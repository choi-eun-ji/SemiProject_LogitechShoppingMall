package mypage.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import common.controller.AbstractController;
import myshop.model.EachCouponVO;
import product.model.*;

public class CartOrderList extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String str_arrObj = request.getParameter("str_arrObj");
		Gson gson = new Gson();
		ProductVO[] arr_pvo = gson.fromJson(str_arrObj, ProductVO[].class);
		ArrayList<ProductVO> pvList = new ArrayList<>();

		for (ProductVO pvo : arr_pvo) {
			pvList.add(pvo);
		}

	}

}
