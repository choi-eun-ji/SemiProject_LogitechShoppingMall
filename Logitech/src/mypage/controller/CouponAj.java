package mypage.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import common.controller.AbstractController;
import myshop.model.EachCouponVO;
import product.model.*;

public class CouponAj extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String memberno = "9";
		InterProductDAO pdao = new ProductDAO();
		String str_arrObj = request.getParameter("str_arrObj");
		System.out.println(str_arrObj);
		
		Gson gson = new Gson();
		EachCouponVO[] arr_Ecvo = gson.fromJson(str_arrObj, EachCouponVO[].class); 
		
		ArrayList<EachCouponVO> ecvoList = new ArrayList<>();
		
		for(EachCouponVO pcvo : arr_Ecvo) {
			ecvoList.add(pcvo);
		}
		int n = 0;
		for(int i=0; i<ecvoList.size(); i++){
			n += pdao.insertEachCoupon(ecvoList.get(i).getFk_couponcode(), memberno);
		}
		boolean b = false;
		if(n == ecvoList.size()){
			b = true;
		}
		System.out.println(b);
		
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("b", b);
		
		String json = jsonObject.toString();
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
