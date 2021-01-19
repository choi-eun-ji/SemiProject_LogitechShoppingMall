package manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;

public class SummaryCouponController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		InterManagerDAO mdao = new ManagerDAO();
		int cnt = mdao.summaryCoupon();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cnt", cnt);
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {}

}
