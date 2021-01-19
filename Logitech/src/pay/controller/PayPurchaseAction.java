package pay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class PayPurchaseAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userid = request.getParameter("userid"); // 로그인 되어진 유저아이디
		
		//세션에 있는값을 불러오자
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuserkey");
		
		
		if(loginuser.getUserid().equals(userid)) { 
			//로그인된 유저아이디와 세션에서 넘어온 아이디가 같냐
			// = 내가 로그인한 것
			
			//String userid = request.getParameter("userid");
			//String coinmoney = request.getParameter("coinmoney");       
			
			//넘겨주자
			
			request.setAttribute("name", loginuser.getName());
			request.setAttribute("userid", userid);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/pay/paymentGateway.jsp");
			
			
		}
		else {
			
			String message = "비정상적인 결제는 불가합니다!";
			String page = "javascript:history.back()"; // 이전페이지로		
			
			request.setAttribute("messagekey", message);
			request.setAttribute("pagekey", page);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		
		}
		
	}

}
