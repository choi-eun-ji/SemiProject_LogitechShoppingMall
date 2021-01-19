package mypage.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.*;

public class OrderCheck extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String usernum = "9";
		ArrayList<HashMap<String, String>> listmap = new ArrayList<>();
		InterProductDAO pdao = new ProductDAO();
		
		listmap = pdao.selectOrderList(usernum);
		
		// 오늘 버튼을 누르면 오늘 날짜의 주문만 보여준다.
		ArrayList<HashMap<String, String>> todaylist = new ArrayList<>();
		todaylist = pdao.todaySelectOrderList(usernum);
		
		// 1주일 버튼을 누르면 1주일 이내의 주문만 보여준다.
		ArrayList<HashMap<String, String>> weeklist = new ArrayList<>();
		weeklist = pdao.weekSelectOrderList(usernum);
		
		// 1개월 버튼을 누르면 1개월 이내의 주문만 보여준다.
		ArrayList<HashMap<String, String>> month1list = new ArrayList<>();
		String month = "-1";
		month1list = pdao.month1SelectOrderList(usernum, month);
		
		// 3개월 버튼을 누르면 6개월 이내의 주문만 보여준다.
		ArrayList<HashMap<String, String>> month3list = new ArrayList<>();
		String month3 = "-3";
		month3list = pdao.month1SelectOrderList(usernum, month3);
		
		// 6개월 버튼을 누르면 6개월 이내의 주문만 보여준다.
		ArrayList<HashMap<String, String>> month6list = new ArrayList<>();
		String month6 = "-6";
		month6list = pdao.month1SelectOrderList(usernum, month6);
		
		// 6개월 동안의 주문 총갯수 얻어오기
		int sumorder = 0;
		sumorder = pdao.sumOrderSelect(usernum);
		
		request.setAttribute("listmap", listmap);
		request.setAttribute("todaylist", todaylist);
		request.setAttribute("weeklist", weeklist);
		request.setAttribute("month1list", month1list);
		request.setAttribute("month3list", month3list);
		request.setAttribute("month6list", month6list);
		request.setAttribute("sumorder", sumorder);
		
		request.setAttribute("sizeli", listmap.size());
		request.setAttribute("todayli", todaylist.size());
		request.setAttribute("weekli", weeklist.size());
		request.setAttribute("month1li", month1list.size());
		request.setAttribute("month3li", month3list.size());
		request.setAttribute("month6li", month6list.size());
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/mypage/mypage_order.jsp");
	}

}
