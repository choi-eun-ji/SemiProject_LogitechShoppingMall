package manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;

public class IdDuplicateCheckController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String managerid = request.getParameter("managerid");
		
		// System.out.println(">>> 확인용 userid ==> " + userid);
		
		InterManagerDAO mdao = new ManagerDAO();
		boolean isExists = mdao.idDuplicateCheck(managerid);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isExists", isExists);  // {"isExists":true} 또는 {"isExists":false}
		String json = jsonObj.toString();  // {"isExists":true} 또는 {"isExists":false} 을 문자열 형태로 만든다.
	//	System.out.println(">>> 확인용 json ==> " + json);
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}