package mypage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.*;

public class MyCoupon extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String memberno = "9";
		ArrayList<Map<String, String>> maplist = new ArrayList<>();

		InterProductDAO pdao = new ProductDAO();
		String grade = pdao.selectGrade(memberno);

		System.out.println(grade);
		maplist = pdao.selectCouponList(memberno, grade.toLowerCase());
		System.out.println(maplist.size());
		String name = maplist.get(0).get("name");

		request.setAttribute("maplist", maplist);
		request.setAttribute("grade", grade);
		request.setAttribute("name", name);
		request.setAttribute("size", maplist.size());

		// 쿠폰 내역 조회 페이징 처리
		// currentShowPageNo : 사용자가 보고자하는 페이지 넘버
		// 메뉴에서 회원목록 만을 클릭했을 경우에는 currentShowPageNo는 null이 된다.
		String currentShowPageNo = request.getParameter("currentShowPageNo");

		// currentShowPageNo가 null이라면 1로 바꿔주어야 한다. (null 페이지는 안됨)
		if (currentShowPageNo == null) {
			currentShowPageNo = "1";
		}

		// 한 페이지당 화면상에 보여줄 제품의 개수는 10으로 한다.
		// GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo에 숫자 아닌 문자로 입력한 경우
		// 이를 무작위로 조작하지 못하도록 exception을 설정해준다.
		try {
			Integer.parseInt(currentShowPageNo);
		} catch (NumberFormatException e) {
			currentShowPageNo = "1";
		}

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("usernum", memberno);
		paraMap.put("currentShowPageNo", currentShowPageNo);

		ArrayList<Map<String, String>> maparr = pdao.selectEachCoupon(paraMap);
		request.setAttribute("maparr", maparr);

		// 페이지바를 만들기 위해서 특정카테고리의 제품개수에 대한 총페이지수를 알아오기
		int totalPage = pdao.getTotalCouponPage(memberno);

		String pageBar = "";
		int blockSize = 10; // 블럭 당 보여지는 페이지 번호의 개수이다.
		int loop = 1; // 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수까지만 증가하는 용도이다.
		int pageNo = 0; // 페이지바에서 보여지는 첫번째 번호이다.

		// 다음은 pageNo를 구하는 공식
		pageNo = ((Integer.parseInt(currentShowPageNo) - 1) / blockSize) * blockSize + 1;

		// **** [이전][맨처음] 만들기 **** //
		// pageNo - 1 == 11 - 1 == 10 ==> currentShowPageNo
		if (pageNo != 1) {
			pageBar += "&nbsp;<a href='mypoint.sg?currentShowpage=1'>[맨처음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='mypoint.sg?currentShowpage=" + (pageNo - 1) + "'>[이전]</a>&nbsp;";
		}

		while (!(loop > blockSize || pageNo > totalPage)) {

			if (pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "&nbsp;<span style='color:black; padding: 2px 4px;'>" + pageNo + "</span>&nbsp;";
			} else {
				pageBar += "&nbsp;<a href='mypoint.sg?currentShowPageNo=" + pageNo + "'>" + pageNo + "</a>&nbsp;";
			}

			loop++; // 1 2 3 4 5 6 7 8 9 10

			pageNo++; // 1 2 3 4 5 6 7 8 9 10
						// 11 12 13 14 15 16 17 18 19 20
						// 21
		} // end of while---------------------------------

		// **** [다음][마지막] 만들기 **** //
		// pageNo ==> 11
		if (!(pageNo > totalPage)) {
			pageBar += "&nbsp;<a href='mypoint.sg?currentShowPageNo=" + pageNo + "'>[다음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='mypoint.sg?currentShowPageNo=" + totalPage + "'>[마지막]</a>&nbsp;";
		}

		request.setAttribute("pageBar", pageBar);
		String couponnum = null;
		couponnum = request.getParameter("couponnum");
		int n = 0;
		if (couponnum != null) {
			n = pdao.insertEachCoupon(couponnum, memberno);
		}
		request.setAttribute("n", n);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/mypage/mypage_cupon.jsp");
	}

}
