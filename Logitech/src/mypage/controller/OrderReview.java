package mypage.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.*;
import review.modul.ReviewVO;

public class OrderReview extends AbstractController {

	@SuppressWarnings("unused")
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = request.getMethod();
		String memberno = "9";
		InterProductDAO pdao = new ProductDAO();

		ArrayList<HashMap<String, String>> listmap = new ArrayList<>();

		listmap = pdao.viewReviewSelect(memberno);

		request.setAttribute("listmap", listmap);
		request.setAttribute("listsize", listmap.size());

		// 리뷰 쓰기 갯수 알아오기
		int recnt = pdao.selectNumReview(memberno);
		request.setAttribute("recnt", recnt);
		
		int writecnt = pdao.selectWriteReview(memberno);
		request.setAttribute("writecnt", writecnt);
		// == 페이징 처리
		String currentShowPageNo = request.getParameter("currentShowPageNo");

		// currentShowPageNo가 null이라면 1로 바꿔주어야 한다. (null 페이지는 안됨)
		if (currentShowPageNo == null) {
			currentShowPageNo = "1";
		}

		// 한 페이지당 화면상에 보여줄 제품의 개수는 10으로 한다.
		// GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo에 숫자 아닌 문자로 입력한 경우
		// 이를 무작위로 조작하지 못하도록 exception을 설정해준다.
		try {
			Integer.parseInt(currentShowPageNo);
		} catch (NumberFormatException e) {
			currentShowPageNo = "1";
		}

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("memberno", memberno);
		paraMap.put("currentShowPageNo", currentShowPageNo);

		ArrayList<HashMap<String, String>> hashlist = pdao.selectMyReview(paraMap);
		request.setAttribute("hashlist", hashlist);
		System.out.println("길이==>" + hashlist.size());

		// 페이지바를 만들기 위해서 특정카테고리의 제품개수에 대한 총페이지수를 알아오기
		int totalPage = pdao.getTotalReviewPage(memberno);
		System.out.println("totalpage=>" + totalPage);
		String pageBar = "";
		int blockSize = 10; // 블럭 당 보여지는 페이지 번호의 개수이다.
		int loop = 1; // 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수까지만 증가하는 용도이다.
		int pageNo = 0; // 페이지바에서 보여지는 첫번째 번호이다.

		pageNo = ((Integer.parseInt(currentShowPageNo) - 1) / blockSize) * blockSize + 1;

		// **** [이전][맨처음] 만들기 **** //
		// pageNo - 1 == 11 - 1 == 10 ==> currentShowPageNo
		if (pageNo != 1) {
			pageBar += "&nbsp;<a href='orderreview.sg?currentShowpage=1'>[맨처음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='orderreview.sg?currentShowpage=" + (pageNo - 1) + "'>[이전]</a>&nbsp;";
		}

		while (!(loop > blockSize || pageNo > totalPage)) {

			if (pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "&nbsp;<span style='color:black; padding: 2px 4px;'>" + pageNo + "</span>&nbsp;";
			} else {
				pageBar += "&nbsp;<a href='orderreview.sg?currentShowPageNo=" + pageNo + "'>" + pageNo + "</a>&nbsp;";
			}

			loop++; // 1 2 3 4 5 6 7 8 9 10

			pageNo++; // 1 2 3 4 5 6 7 8 9 10
						// 11 12 13 14 15 16 17 18 19 20
						// 21
		} // end of while---------------------------------

		// **** [다음][마지막] 만들기 **** //
		// pageNo ==> 11
		if (!(pageNo > totalPage)) {
			pageBar += "&nbsp;<a href='orderreview.sg?currentShowPageNo=" + pageNo + "'>[다음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='orderreview.sg?currentShowPageNo=" + totalPage + "'>[마지막]</a>&nbsp;";
		}

		request.setAttribute("pageBar", pageBar);
		String delseq = null;
		delseq = request.getParameter("delseq");
		System.out.println("리뷰다~" + delseq);
		
		if(delseq != null){
			int n = pdao.deleteReview(delseq);
			System.out.println(n);
			
			if(n==1){
				response.setContentType("text/html; charset=UTF-8");
				
				PrintWriter out = response.getWriter();
				if(n==1){
					out.println("<script>alert('리뷰가 삭제 되었습니다.');location.href='orderreview.sg';</script>");
				}
				else{
					out.println("<script>alert('리뷰가 삭제가 실패되었습니다.');location.href='orderreview.sg';</script>");
				}
				out.flush();
			}
		}
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/mypage/mypage_review.jsp");

		if (method.equalsIgnoreCase("POST")) {
			String separ = null;
			String fk_productid = null;

			String del = null;
			del = request.getParameter("del");
			
			System.out.println("zzzzzzz"+del);
			
			separ = request.getParameter("separ");
			String seqreviewno = request.getParameter("seqreviewno");
			fk_productid = request.getParameter("fk_productid");

			System.out.println("id==->" + fk_productid);
			if(fk_productid != null){
				System.out.println("post첫번째!");
				String content = request.getParameter("content");
				String score = request.getParameter("score");
				String color = request.getParameter("color");
				String fk_purchaseno = request.getParameter("fk_purchaseno");
	
				content = content.trim();
	
				ReviewVO rv = new ReviewVO();
				rv.setFk_productid(fk_productid);
				rv.setContent(content);
				rv.setScore(score);
				rv.setColor(color);
				rv.setUserno(memberno);
				rv.setFk_purchaseno(fk_purchaseno);
	
				int n = pdao.insertReview(rv);
	
				response.setContentType("text/html; charset=UTF-8");
	
				PrintWriter out = response.getWriter();
				if (n == 1) {
					out.println("<script>alert('리뷰가 등록되었습니다.');location.href='orderreview.sg';</script>");
				}
				out.flush();
				request.setAttribute("n1", n);
			}//////////// end of if
			else{
				
				System.out.println("post두번째!");
				String updatescore = request.getParameter("score2");
				String updateseq = request.getParameter("seq_review");
				String updatecontent = request.getParameter("content2");
				
				ReviewVO revo = new ReviewVO();
				revo.setScore(updatescore);
				revo.setSeq_review(updateseq);
				revo.setContent(updatecontent);
				
				int n = pdao.updateReview(revo);
				
				response.setContentType("text/html; charset=UTF-8");
				
				PrintWriter out = response.getWriter();
				if(n==1){
					out.println("<script>alert('리뷰가 수정되었습니다.');location.href='orderreview.sg';</script>");
				}
				else{
					out.println("<script>alert('리뷰가 수정이 실패되었습니다.');location.href='orderreview.sg';</script>");
				}
				out.flush();
				
			}

		} /////////// post 방식!
	}
}
