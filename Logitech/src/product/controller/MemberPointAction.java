package product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class MemberPointAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String memberno = request.getParameter("memberno");
		
		InterMemberDAO mdao = new MemberDAO();
		String totalPoint = mdao.getMemberPoint(memberno);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPoint", totalPoint);
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
