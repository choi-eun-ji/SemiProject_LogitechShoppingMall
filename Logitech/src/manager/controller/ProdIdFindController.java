package manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;
import myshop.model.ProductVO;

public class ProdIdFindController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String productid = request.getParameter("productid");
				
		// System.out.println(">>> 확인용 userid ==> " + userid);
		
		InterManagerDAO mdao = new ManagerDAO();
		List<ProductVO> pvoList = mdao.prodIdFind(productid);
		
		JSONArray jsonArr = new JSONArray();
		
		if(pvoList.size() != 0) {
			for(ProductVO pvo : pvoList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("productid", pvo.getProductid());
				jsonObj.put("productname", pvo.getProductname());
				jsonArr.put(jsonObj);
			}
		}
		
		String json = jsonArr.toString();  // {"isExists":true} 또는 {"isExists":false} 을 문자열 형태로 만든다.
		System.out.println(">>> 확인용 json ==> " + json);
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
