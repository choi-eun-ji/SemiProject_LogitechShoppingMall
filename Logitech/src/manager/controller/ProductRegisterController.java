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

public class ProductRegisterController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// @@ 관리자(admin)로 로그인했을 때만 조회가 가능하도록 한다. @@
		HttpSession session = request.getSession();
		ManagerVO loginManager = (ManagerVO)session.getAttribute("loginManager");
		
		if(loginManager != null && ("전체".equals(loginManager.getManagertype()) || "제품".equals(loginManager.getManagertype()))) {
			String method = request.getMethod();
			
			if("GET".equalsIgnoreCase(method)) {
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/manager/product/productRegister.jsp");
			} else {
				String productid = request.getParameter("productid");
				String productname = request.getParameter("productname");
				String category = request.getParameter("category");
				String foruse = request.getParameter("foruse");
				String connection = request.getParameter("connection");
				String price = request.getParameter("price");
				String imgfilename = request.getParameter("imgfilename");
				String carouselimg = request.getParameter("carouselimg");
				String detailimg = request.getParameter("detailimg");
				
				String character = foruse+","+connection;
				
				Map<String, String> paraMap = new HashMap<String, String>();
				paraMap.put("productid", productid);
				paraMap.put("productname", productname);
				paraMap.put("category", category);
				paraMap.put("price", price);
				paraMap.put("imgfilename", imgfilename);
				paraMap.put("carouselimg", carouselimg);
				paraMap.put("detailimg", detailimg);
				paraMap.put("character", character);
				
				InterManagerDAO mdao = new ManagerDAO();
				int n = mdao.registerProduct(paraMap);  // 예외 처리를 넘겨받았는데 처리하지 않는 이유는 다시 한 번 넘겨주기 때문이다.
				
				String message = "";
				String loc = "javascript:history.back()";
				
				if(n == 1) {
					message = "제품 등록 성공";
				} else {
					message = "제품 등록 실패";
				}// end of if(n == 1){}----------------------
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
			}// end of if("GET".equalsIgnoreCase(method)){}----------------------
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
