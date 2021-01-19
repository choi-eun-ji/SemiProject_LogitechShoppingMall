package manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;
import manager.model.ManagerVO;
import member.model.MemberVO;

public class ManagerUpdateController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 나의 정보를 수정하기 위한 전제조건은 먼저 자신의 계정으로 로그인 해야 하는 것이다.
		if(super.checkLoginManager(request)) {
			// 로그인 한 경우
			String managerid = request.getParameter("managerid");
			
			HttpSession session = request.getSession();
			ManagerVO loginManager = (ManagerVO)session.getAttribute("loginManager");
			if(loginManager != null && loginManager.getManagerid().equals(managerid)) {
				String method = request.getMethod();
				
				if("GET".equalsIgnoreCase(method)) {
				//	super.setRedirect(false);
					super.setViewPage("/WEB-INF/manager/manager/managerUpdate.jsp");
				} else {
					String updateid = request.getParameter("updateid");
					String managerpwd = request.getParameter("managerpwd");
					String managertype = request.getParameter("managertype");
					String manageremail = request.getParameter("manageremail");
					String managermobile = request.getParameter("managermobile");
					
					ManagerVO mgvo = new ManagerVO();
					mgvo.setManagerid(updateid);
					mgvo.setManagerpwd(managerpwd);
					mgvo.setManagertype(managertype);
					mgvo.setManageremail(manageremail);
					mgvo.setManagermobile(managermobile);
					
					InterManagerDAO mdao = new ManagerDAO();
					int n = mdao.updateManager(mgvo);  // 예외 처리를 넘겨받았는데 처리하지 않는 이유는 다시 한 번 넘겨주기 때문이다.
					
					String message = "";
					String loc = "";
					
					if(n == 1) {
						message = "계정 수정 성공";
						loc = request.getContextPath() + "/manager/managerIndex.sg";  // 시작페이지
					} else {
						message = "계정 수정 실패";
						loc = "javascript:history.back()";  // 자바스크립트를 이용한 이전페이지로 이동하는 것
					}// end of if(n == 1){}----------------------
					
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
					
				//	super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
				}// end of if("GET".equalsIgnoreCase(method)){}----------------------
			} else {
				// 로그인 했지만 자신이 아닌 타인의 정보를 수정하려는 경우
				String message = "다른 관리자의 정보는 수정할 수 없습니다.";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			}// end of if(loginUser.getUserid().equals(userid)){}-----------------------
		} else {
			// 로그인 안 한 경우
			String message = "관리자 정보를 수정하기 위해서는 먼저 로그인을 해주세요.";
			String loc = request.getContextPath()+"/manager/signIn.sg";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}// end of if(super.checkLogin(request)){}------------------------
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception

}
