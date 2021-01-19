package manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;

public class EventRandomFunctionController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strjoinCnt = request.getParameter("joinCnt");
		int joinCnt = Integer.parseInt(strjoinCnt);
		int winCnt = (int)joinCnt / 100 + 1;

		int[] arrwin = new int[winCnt];
		
		Random random = new Random();
		
		for(int i = 0; i < arrwin.length; i++) { // 당첨자 수만큼 뽑기 위한 for문
			arrwin[i] = random.nextInt(joinCnt-1) + 0; // 1 ~ 마지막 숫자(joinCnt) 중 랜덤으로 하나를 뽑아 arrwin 배열에 저장
			
			for(int j = 0; j < i; j++) { // 중복제거를 위한 for문
				
				if (arrwin[i] == arrwin[j]) {
					i--;
				}
			}
			
		}// end of for(int i = 0; i < arrwin.length; i++) {}---------------------
		
		JSONArray jsonArr = new JSONArray();
		
		for(int n : arrwin) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("n", n);
			jsonArr.put(jsonObj);
		}
		
		String json = jsonArr.toString();
		
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {}

}
