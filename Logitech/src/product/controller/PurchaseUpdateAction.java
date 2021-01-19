package product.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class PurchaseUpdateAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String receiver = request.getParameter("name");
		String postcode = request.getParameter("postcode");
		String address = request.getParameter("address");
		String detailaddress = request.getParameter("detailAddress");
		String extraaddress = request.getParameter("extraAddress");
		String point = request.getParameter("point");
		
		String fk_memberno = request.getParameter("userid");
		String totalprice = request.getParameter("paymoney");
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("receiver", receiver);
		paraMap.put("postcode", postcode);
		paraMap.put("address", address);
		paraMap.put("detailaddress", detailaddress);
		paraMap.put("extraaddress", extraaddress);
		paraMap.put("point", point);
		paraMap.put("fk_memberno", fk_memberno);
		paraMap.put("totalprice", totalprice);
		
		InterMemberDAO mdao = new MemberDAO();
		int n = mdao.purchaseUpdate(paraMap);  // DB의 코인 및 포인트 데이터 변경하기
		
		String message = "";
		String loc = "";
		
		if(n == 1) {
			message = "결제가 완료되었습니다.";
			loc = request.getContextPath()+"/index.sg";
		} else {
			message = "결제가 실패했습니다.";
			loc = "javascript:history.back()";
		}// end of if(n == 1){}------------------------
		
		request.setAttribute("message", message);
		request.setAttribute("loc", loc);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
		
	}

}
