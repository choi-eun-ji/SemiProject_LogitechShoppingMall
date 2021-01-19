package common.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;
import manager.model.ManagerVO;
import member.model.*;
import myshop.model.ProductCategoryVO;
import myshop.model.ProductVO;

public abstract class AbstractController implements InterCommand {
	
	InterManagerDAO mdao = new ManagerDAO();
	
/*
	=== 다음의 나오는 것은 우리끼리한 약속이다. ===
	
	※ view 단 페이지(.jsp)로 이동시 forward 방법(dispatcher)으로 이동시키고자 한다라면 
	자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
		-> forward 방법은 데이터까지 넘어간다.
	     
	super.setRedirect(false); 
	super.setViewPage("/WEB-INF/index.jsp");


	※ URL 주소를 변경하여 페이지 이동시키고자 한다라면
	즉, sendRedirect 를 하고자 한다라면    
	자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
		-> sendRedirect 방법은 데이터는 넘어가지 않고 페이지만 이동한다. 여기서 페이지 이동은 해당 URL을 주소창에 직접 적어 이동하는 효과와 같은 것이다.
		-> URL 주소는 주로 상대경로를 사용한다(주로 같은 폴더(경로) 내에서 페이지 이동을 많이 하기 때문에 끝 URL만 바꿔주는 상대경로가 편하다).
	          
	super.setRedirect(true);
	super.setViewPage("registerMember.up");               
*/

	private boolean isRedirect = false;
	// isRedirect 변수의 값이 false라면 view단 페이지(.jsp)로 forward 방법(dispatcher)으로 이동하겠다고 약속하는 것이다.
	// isRedirect 변수의 값이 true라면 sendRedirect로 페이지 이동을 시키겠다고 약속하는 것이다.
	
	private String viewPage;
	// viewPage는 isRedirect 값이 false라면 view단 페이지(.jsp)의 경로명이고,
	// 			 isRedirect 값이 true라면 이동해야할 페이지 URL 주소이다.

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	// @@ 로그인 유무를 검사@@ //
	// 로그인 했으면 true 를 반환하고,
	// 로그인 안했으면 false 를 반환한다.
	public boolean checkLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			// 로그인 한 경우
			return true;
		} else {
			// 로그인 안 한 경우
			return false;
		}// end of if(loginUser != null){}-----------------------
	}// end of public boolean checkLogin(HttpServletRequest request){}--------------------
	
	/////////////////////////////////////////////
	// @@ 로그인 유무를 검사@@ //
	// 로그인 했으면 true 를 반환하고,
	// 로그인 안했으면 false 를 반환한다.
	public boolean checkLoginManager(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ManagerVO loginManager = (ManagerVO)session.getAttribute("loginManager");
		
		if(loginManager != null) {
			// 로그인 한 경우
			return true;
		} else {
			// 로그인 안 한 경우
			return false;
		}// end of if(loginUser != null){}-----------------------
	}// end of public boolean checkLogin(HttpServletRequest request){}--------------------
	
	
	// modal 창에서 userid 클릭 시 아이디와 수신 정보를 select 하는 메소드
	public void checkAgreeStatus(HttpServletRequest request, String userid) throws SQLException {
		MemberVO mbvo = mdao.checkAgreeStatus(userid);
		
		request.setAttribute("mbvo", mbvo);
	}// end of public void checkAgreeStatus(String userid) throws SQLException {}
	
	
	// 제품 카테고리 조회 메소드
	public void productCategory(HttpServletRequest request) throws SQLException {
		List<ProductCategoryVO> pcvoList = mdao.productCategory();
		
		request.setAttribute("pcvoList", pcvoList);
	}// end of public void productCategory(HttpServletRequest request) throws SQLException {}
	
	
	// 회원등급 테이블 조회 메소드
	public void membership(HttpServletRequest request) throws SQLException {
		List<MembershipVO> msvoList = mdao.membership();
		
		request.setAttribute("msvoList", msvoList);
	}// end of public void productCategory(HttpServletRequest request) throws SQLException {}
	
	
	// 상품 이미지 조회 메소드
	public void productImg(HttpServletRequest request, String productid) throws SQLException {
		ProductVO pvo = mdao.productImg(productid);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("productid", pvo.getProductid());
		jsonObj.put("imgfilename", pvo.getImgfilename());
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
	}// end of public void productCategory(HttpServletRequest request) throws SQLException {}
	
}
