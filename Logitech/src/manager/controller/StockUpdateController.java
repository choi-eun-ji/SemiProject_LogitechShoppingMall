package manager.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import manager.model.ManagerDAO;

public class StockUpdateController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// @@ 재고 수정하기 @@ //
		String[] productserialid = request.getParameterValues("productserialid");
		String[] stockUpdate = request.getParameterValues("stockUpdate");
		
		int cnt = 0;
		int n = 0;
		
		for(int i=0; i<stockUpdate.length; i++) {
			if(stockUpdate[i] != "") {
				Map<String, String> paraMap = new HashMap<String, String>();
				
				paraMap.put("productserialid", productserialid[i]);
				
				// Action
				// +, - 기호를 입력받아 재고 추가와 재고 삭감을 동시에 구현
				String mark = stockUpdate[i].substring(0, 1);
				String strStock = stockUpdate[i].substring(1);
				
				paraMap.put("mark", mark);
				paraMap.put("stockUpdate", strStock);
				
				ManagerDAO mdao = new ManagerDAO();
				
				n += mdao.stockUpdate(paraMap);
				cnt++;
			}
		}// end of for(int i=0; i<stockUpdate.length; i++) {}---------------------------
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		jsonObj.put("cnt", cnt);
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
