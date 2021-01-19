package mypage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.ParameterMap;

import common.controller.AbstractController;
import product.model.*;

public class Main extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterProductDAO pdao = new ProductDAO();

		int usernum = 9;
		String totalpt = pdao.selectSumPoint(usernum);

		if (totalpt == null) {
			totalpt = "0";
		}

		// 해당 유저가 사용한 포인트 금액 가져오기
		String pt = pdao.selectUsedPoint(usernum);
		if (pt == null) {
			pt = "0";
		}
		
		// 유저가 사용 가능한 쿠폰 갯수 가져오기
		int cnt = pdao.selectCntCoupon(usernum);
		
		// 유저가 총 구매한 구매금액의 합계 가져오기
		int sumprice = pdao.selectSumPrice(usernum);
		
		// 유저의 등급 가져오기
		String grade = pdao.selectGrade(Integer.toString(usernum));
		
		// 등급의 최소, 최대값 가져오기
		Map<String, String> paramap = new HashMap<>();
		paramap = pdao.selectMinMaxGrade(grade);
		String min = paramap.get("min");
		String max = paramap.get("max");
		
		// 해당 유저의 등급의 다음 등급 가져오기
		String nextgrade = pdao.selectNextGrade(max);
		
		// 결제 완료 갯수 가져오기
		int mediumNum = pdao.selectMediumNum(usernum);
		
		// 배송완료 갯수 가져오기
		int endNum = pdao.selectEndNum(usernum);
		
		request.setAttribute("grade", grade);
		request.setAttribute("totalpt", totalpt);
		request.setAttribute("usedpoint", pt);
		request.setAttribute("cnt", cnt);
		request.setAttribute("sumprice", sumprice);
		request.setAttribute("min", min);
		request.setAttribute("max", max);
		request.setAttribute("nextgrade", nextgrade);
		request.setAttribute("mediumNum", mediumNum);
		request.setAttribute("endNum", endNum);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/mypage/mypage_main.jsp");
	}

}
