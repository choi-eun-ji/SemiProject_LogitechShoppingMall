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

public class NewMemberChartController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		InterManagerDAO mdao = new ManagerDAO();
		List<Map<String, Integer>> chartList = mdao.newMemberChart();
		
		JSONArray jsonArr = new JSONArray();
		
		if(chartList.size() != 0) {
			for(Map<String, Integer> chartMap : chartList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("registerday", chartMap.get("registerday"));
				jsonObj.put("cnt", chartMap.get("cnt"));
				jsonArr.put(jsonObj);
			}
		}
		
		String json = jsonArr.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {}

}
