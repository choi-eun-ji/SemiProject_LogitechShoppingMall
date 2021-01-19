package manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;
import manager.model.ManagerVO;

public class IdExistCheckController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String updateid = request.getParameter("updateid");
		
		// System.out.println(">>> 확인용 userid ==> " + userid);
		
		InterManagerDAO mdao = new ManagerDAO();
		ManagerVO mvo = mdao.idExistCheck(updateid);
		
		JSONObject jsonObj = new JSONObject(); // {}
		if(mvo != null) {
			jsonObj.put("isExists", true);
			jsonObj.put("updateid", mvo.getManagerid());
			jsonObj.put("managerpwd", mvo.getManagerpwd());
			jsonObj.put("managertype", mvo.getManagertype());
			jsonObj.put("manageremail", mvo.getManageremail());
			jsonObj.put("managermobile", mvo.getManagermobile());
			jsonObj.put("managerno", mvo.getManagerno());
			
			HttpSession session = request.getSession();
			session.setAttribute("updateManager", mvo);
		} else {
			jsonObj.put("isExists", false);
		}
		
		String json = jsonObj.toString();  // {"isExists":true} 또는 {"isExists":false} 을 문자열 형태로 만든다.
	//	System.out.println(">>> 확인용 json ==> " + json);
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {}

}
