package mypage.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.*;

public class EditMyInfo extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		InterProductDAO pdao = new ProductDAO();
		String memberno = "9";

		String method = request.getMethod();
		if (method.equalsIgnoreCase("get")) {
			MemberVO mvo = new MemberVO();

			mvo = pdao.editMember(memberno);

			request.setAttribute("mvo", mvo);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/mypage/mypage_update.jsp");
		} else {
			System.out.println("들어옴~");
			MemberVO mvo = new MemberVO();

			String name = request.getParameter("name");
			mvo.setName(name);
			String userid = request.getParameter("userid");
			mvo.setUserid(userid);

			String email = request.getParameter("email");
			mvo.setEmail(email);

			String mobile = request.getParameter("hp1") + "-" + request.getParameter("hp2") + "-"
					+ request.getParameter("hp3");
			mvo.setMobile(mobile);

			String postcode = request.getParameter("postcode");
			mvo.setPostcode(postcode);

			String address = request.getParameter("address");
			String detailAddress = request.getParameter("detailAddress");
			String extraAddress = request.getParameter("extraAddress");

			mvo.setAddress(address);
			mvo.setDetailaddress(detailAddress);
			mvo.setExtraaddress(extraAddress);

			String birth = request.getParameter("birthyyyy") + "-" + request.getParameter("birthmm") + "-"
					+ request.getParameter("birthdd");
			mvo.setBirthday(birth);
			mvo.setMemberno(Integer.parseInt(memberno));

			String pwd = request.getParameter("pwd");
			mvo.setPwd(pwd);
			int n = pdao.updateMember(mvo);
			System.out.println(n);

			response.setContentType("text/html; charset=UTF-8");

			PrintWriter out = response.getWriter();
			if (n == 1) {
				out.println("<script>alert('정보가 수정되었습니다.');location.href='main.sg';</script>");
			} else {
				out.println("<script>alert('수정이 실패되었습니다.');location.href='main.sg';</script>");
			}
			out.flush();

			super.setViewPage("/WEB-INF/mypage/mypage_main.jsp");
		}

	}

}
