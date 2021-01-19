package manager.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import manager.model.InterManagerDAO;
import manager.model.ManagerDAO;
import manager.model.ManagerVO;

public class EventRegisterController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// @@ 관리자(admin)로 로그인했을 때만 조회가 가능하도록 한다. @@
		HttpSession session = request.getSession();
		ManagerVO loginManager = (ManagerVO)session.getAttribute("loginManager");
		
		if(loginManager != null && ("전체".equals(loginManager.getManagertype()) || "판촉".equals(loginManager.getManagertype()))) {
			String method = request.getMethod();
			
			if("GET".equalsIgnoreCase(method)) {
				super.productCategory(request);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/manager/promotion/eventRegister.jsp");
			} else {
				String eventname = request.getParameter("eventname");
				String fk_productid = request.getParameter("fk_productid");
				String startday = request.getParameter("startday");
				String endday = request.getParameter("endday");
				String carouselimg = request.getParameter("carouselimg");
				
				Map<String, String> paraMap = new HashMap<String, String>();
				paraMap.put("eventname", eventname);
				paraMap.put("fk_productid", fk_productid);
				paraMap.put("startday", startday);
				paraMap.put("endday", endday);
				paraMap.put("carouselimg", carouselimg);
				
				InterManagerDAO mdao = new ManagerDAO();
				int n = mdao.registerEvent(paraMap);  // 예외 처리를 넘겨받았는데 처리하지 않는 이유는 다시 한 번 넘겨주기 때문이다.
				
				String message = "";
				String loc = "javascript:history.back()";
				
				if(n == 1) {
					message = "이벤트 등록 성공";
				} else {
					message = "이벤트 등록 실패";
				}// end of if(n == 1){}----------------------
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
		} else {
			// 로그인하지 않았거나 일반 사용자로 로그인한 경우
			String message = "관리자만 접근할 수 있습니다.";
			String loc = request.getContextPath()+"/manager/signIn.sg";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}// end of if(loginManager != null && "admin".equals(loginManager.getUserid())){}------------------
	}// end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception{}

}
