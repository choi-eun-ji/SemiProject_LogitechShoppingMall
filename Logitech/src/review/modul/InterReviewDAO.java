package review.modul;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cs.model.FaqVO;

public interface InterReviewDAO {
	
	
	// 해당 제품의 review를 가져오기.
	List<ReviewVO> getReviewlist(String fk_productid) throws SQLException;
	
	// 해당 제품의 평점 평균을 가져오기.
	String getAverage(String productid) throws SQLException;
	
	// 리뷰에 참여한 명수 구하기.
	String getReviewNum(String productid) throws SQLException;
	
	// 페이징 처리한 제품의 review를 가져오기.
	List<ReviewVO> selectPagingReview(Map<String, String> paraMap) throws SQLException;
	
	// 페이징 처리한 제품의 총페이지를 가져오기. 
	int getTotalPage(Map<String, String> paraMap) throws SQLException;
	
	
	
	
}
	