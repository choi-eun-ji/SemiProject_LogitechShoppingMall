package cs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import cs.model.CustomCenterDAO;
import cs.model.InterCutomCenterDAO;
import cs.model.OneQueryAnswerVO;
import cs.model.OneQueryVO;
import member.model.MemberVO;
import util.security.AES256;
import util.security.SecretMyKey;

public class OneQueryViewOne extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session=request.getSession();
		
		MemberVO mvo= (MemberVO)session.getAttribute("loginuser");
		
		
		
		String seq_oq=request.getParameter("seq_oq");
		
		InterCutomCenterDAO cdao = new CustomCenterDAO();
		
		OneQueryVO oqvo=cdao.getoqvo(seq_oq);
		OneQueryAnswerVO oqavo =cdao.getAnswervo(seq_oq);
		
		if(oqavo != null) {
			
			oqavo.getAnswerdate();
			oqavo.getOq_content();
			
		}
		
		
		
	    
	   
		// 로그인한 사용자의 회원번호와 문의내용 글을 남긴 회원번호와 같지 않다면 다른페이지로 보내주기.
		if (mvo.getMemberno() != oqvo.getFk_memberno())  {
			
			super.setViewPage("WEB-INF/customCenter/CustomCenter.jsp");
		}
		
		// 로그인한 사용자의 회원번호와 문의내용의 글을 남긴 회원번호와 같다면
		
		request.setAttribute("oqvo", oqvo);
		request.setAttribute("seq_oq", seq_oq);
		request.setAttribute("oqavo", oqavo);
		
		
		
		
		super.setViewPage("WEB-INF/customCenter/oneQueryViewOne.jsp");
		

	}

}
