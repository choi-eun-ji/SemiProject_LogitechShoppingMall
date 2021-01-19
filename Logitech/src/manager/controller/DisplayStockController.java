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

public class DisplayStockController extends AbstractController {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		InterManagerDAO mdao = new ManagerDAO();
		List<Map<String, String>> stockList = mdao.displayStock();
		
		JSONArray jsonArr = new JSONArray();
		
		if(stockList.size() != 0) {
			for(Map<String, String> stockMap : stockList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("productserialid", stockMap.get("productserialid"));
				jsonObj.put("stock", stockMap.get("stock"));
				jsonArr.put(jsonObj);
			}
		}
		
		String json = jsonArr.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
