package product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class PaymentAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// IAMPORT 결제창을 사용하기 위해서는 먼저 로그인이 되어야 한다.
		if(super.checkLogin(request)) {
			// 로그인 한 경우
			String userid = request.getParameter("userid");
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
			if(loginuser.getUserid().equals(userid)) {
				// 로그인 한 사용자가 자신의 코인을 충전하는 경우
				String paymoney = request.getParameter("paymoney");
				
				request.setAttribute("userid", userid);
				request.setAttribute("paymoney", paymoney);
				request.setAttribute("email", loginuser.getEmail());
				request.setAttribute("name", loginuser.getName());
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/pay/paymentGateway.jsp");
			} else {
				// 로그인 했지만 자신이 아닌 타인의 코인을 충전하려는 경우
				String message = "다른 사용자의 코인충전은 결제할 수 없습니다!!";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			}// end of if(loginUser.getUserid().equals(userid)){}-----------------------
		} else {
			// 로그인 안 한 경우
			String message = "코인충전 결제를 하기 위해서는 먼저 로그인을 하세요!!";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}// end of if(super.checkLogin(request)){}------------------------
		
	}

}
