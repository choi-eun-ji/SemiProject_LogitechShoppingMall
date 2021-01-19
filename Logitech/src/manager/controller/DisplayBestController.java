package manager.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;

public class DisplayBestController extends AbstractController {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		InterManagerDAO mdao = new ManagerDAO();
		List<Map<String, String>> bestList = mdao.displayBest();
		
		JSONArray jsonArr = new JSONArray();
		
		if(bestList.size() != 0) {
			for(Map<String, String> bestMap : bestList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("fk_productserialid", bestMap.get("fk_productserialid"));
				jsonObj.put("sum", bestMap.get("sum"));
				jsonArr.put(jsonObj);
			}
		}
		
		String json = jsonArr.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
