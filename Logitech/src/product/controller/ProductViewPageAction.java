package product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import my.util.MyUtil;
import product.model.*;
import review.modul.InterReviewDAO;
import review.modul.ReviewDAO;
import review.modul.ReviewVO;

public class ProductViewPageAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String productid = request.getParameter("productid");
		
		
		InterProductDAO idao = new ProductDAO();
		
		List<ProductVO> detailProductList = idao.selectOne(productid);
		
		ProductVO rProductVO = detailProductList.get(0); 
		
		
		request.setAttribute("detailProductList", detailProductList);
		request.setAttribute("rProductVO", rProductVO);
			
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		String sizePerPage = request.getParameter("sizePerPage");
		Map<String,String> paraMap = new HashMap<>();
		
		if( currentShowPageNo == null ) {
			currentShowPageNo = "1";
		}
		
		if( sizePerPage == null )
			
			{ 
			sizePerPage = "5";
		}
			
		try {
			Integer.parseInt(currentShowPageNo);
		}   catch(NumberFormatException e) {
			currentShowPageNo = "1";
		}
		
		
		paraMap.put("currentShowPageNo", currentShowPageNo);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("productid", productid);
		
		InterReviewDAO rdao = new ReviewDAO();
		
		List<ReviewVO> reviewlist = rdao.selectPagingReview(paraMap);
		
		
		
		request.setAttribute("detailProductList", detailProductList);
		request.setAttribute("rProductVO", rProductVO);
		request.setAttribute("productid", productid);
		
		
		String reviewNum=rdao.getReviewNum(productid);
		/* List<ReviewVO> reviewlist=rdao.getReviewlist(productid); */
		String average=rdao.getAverage(productid);
		
		
		request.setAttribute("reviewNum", reviewNum);
		request.setAttribute("reviewlist", reviewlist);
		request.setAttribute("average", average);
		
		// **** ========= 페이지바 만들기 ========= **** //
		
		// 페이징처리를 위해서 전체회원에 대한 총페이지 개수 알아오기(select)
		int totalPage = rdao.getTotalPage(paraMap);
		String pageBar = "";
		
		int blockSize = 5;
		// blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 5개)까지만 증가하는 용도이다. 
		
		int pageNo = 0;
		// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
		
		
		pageNo = ( ( Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1; 
	
		
		if( pageNo != 1 ) {
			pageBar += "&nbsp;<a href='/Logitech/product/productviewpage.sg?productid="+productid+"&currentShowPageNo=1&sizePerPage="+sizePerPage+">[맨처음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='/Logitech/product/productviewpage.sg?productid="+productid+"&currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"'>[이전]</a>&nbsp;";
		}
		
		
		while( !(loop > blockSize || pageNo > totalPage ) ) {
			
			if( pageNo == Integer.parseInt(currentShowPageNo) ) {
				pageBar += "&nbsp;<span style='border:solid 1px gray; color:red; padding: 2px 4px;'>"+pageNo+"</span>&nbsp;";
			}
			
			else {
				pageBar += "&nbsp;<a href='/Logitech/product/productviewpage.sg?productid="+productid+"&currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"'>"+pageNo+"</a>&nbsp;"; 
			}
			
			loop++;  
			          
			pageNo++; 
			
		}// end of while---------------------------------
		
		
		
		 
		if( !( pageNo > totalPage ) ) {
			
			pageBar += "&nbsp;<a style='href='/Logitech/product/productviewpage.sg?productid="+productid+"&currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"'>[다음]</a>&nbsp;";
			pageBar += "&nbsp;<a href='/Logitech/product/productviewpage.sg?productid="+productid+"&currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"'>[마지막]</a>&nbsp;";
			
		}
		
		
		request.setAttribute("pageBar", pageBar);
		
		
		
		String currentURL = MyUtil.getCurrentURL(request);
		currentURL = currentURL.replaceAll("&", " ");
		
		request.setAttribute("goBackURL", currentURL);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/product/productsingle.jsp");
		
		
	
		
		
		
	}

}
