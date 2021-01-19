package review.modul;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cs.model.FaqVO;
import util.security.AES256;
import util.security.SecretMyKey;

public class ReviewDAO implements InterReviewDAO {
	
	
	private DataSource ds;
	// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	
	// 기본생성자
	public ReviewDAO() {
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/semi_oracle");

			// SecretMyKey.KEY 은 내가 만든 비밀키 이다.
			aes = new AES256(SecretMyKey.KEY);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	// 사용한 자원을 반납하는 close() 메서드 생성하기
	
	private void close() {
	      try {
	         if(rs != null)    {rs.close(); rs=null;}
	         if(pstmt != null) {pstmt.close(); pstmt=null;}
	         if(conn != null)  {conn.close(); conn=null;}
	      } catch(SQLException e) {
	         e.printStackTrace();
	      }
	}

	
	// 해당 제품의 리뷰 가져오기.
	@Override
	public List<ReviewVO> getReviewlist(String fk_productid) throws SQLException {
	
		List<ReviewVO> reviewlist = new ArrayList<ReviewVO>();
		
		
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select seq_review, fk_productid, color, userid, content, score, review_image, to_char(writeday, 'yyyy-mm-dd'), fk_purchaseno "+
					" from review A "+
					" join member B "+
					" on fk_memberno = memberno "+
					" where fk_productid = ? "+
					" order by 1 desc ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_productid);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReviewVO rvo = new ReviewVO();
				 rvo.setSeq_review(rs.getString(1));
				 rvo.setFk_productid(rs.getNString(2));
				 rvo.setColor(rs.getString(3));
				 rvo.setUserid(rs.getString(4));
				 rvo.setContent(rs.getString(5));
				 rvo.setScore(rs.getString(6));
				 rvo.setReview_image(rs.getString(7));
				 rvo.setWirteday(rs.getString(8));
				 rvo.setPurchaswno(rs.getString(9));
				 reviewlist.add(rvo);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return reviewlist;
		
	}

	@Override
	public String getAverage(String productid)  throws SQLException {
		
		
		String sql = " select round((sum(score)) / count(*) , 2) "+
			      	" from review "+
				    " where fk_productid = ?";
		
		conn = ds.getConnection();
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, productid);
		
		rs=pstmt.executeQuery();
		
		rs.next(); 
			
		
		String average=rs.getString(1);
		
		if(average == null) {
			
			average="0";
			
		}
		
		
		close();
		
		return average;
	}

	@Override
	public String getReviewNum(String fk_productid)  throws SQLException {
		String reviewNum="0";
		
		try {
			
			conn = ds.getConnection();
			String sql = " select count(*) "+
					" from review "+
					" where fk_productid = ? ";
			

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_productid);
			
			rs=pstmt.executeQuery();
			
			rs.next(); 
				
			reviewNum=rs.getString(1);

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	finally {
		close();
		
	}
	
		return reviewNum;
	
	

	
	}

	@Override
	public List<ReviewVO> selectPagingReview(Map<String, String> paraMap) throws SQLException {
		 
		List<ReviewVO> reviewlist = new ArrayList<ReviewVO>();
		
		try {
			conn = ds.getConnection();
			
			
			String sql = " select rno, seq_review, fk_productid, color, userid, content, score, review_image, to_char(writeday, 'yyyy-mm-dd'), fk_purchaseno "+
					" from  "+
					"( "+
					"    select rownum AS rno, seq_review, fk_productid, color, userid, content, score, review_image, writeday, fk_purchaseno "+
					"    from "+
					"    ( "+
					"        select seq_review, fk_productid, color, userid, content, score, review_image, writeday, fk_purchaseno "+
					"        from review A "+
					"        join member B "+
					"        on fk_memberno = memberno "+
					"        where fk_productid = ?  "+
					"        order by 1 desc "+
					"    ) V "+
					" ) T "+
					" where rno between ? and ? ";
			
			

			
			
			pstmt = conn.prepareStatement(sql);

			int currentShowPageNo = Integer.parseInt( paraMap.get("currentShowPageNo") );
			int sizePerPage = Integer.parseInt( paraMap.get("sizePerPage") );
			

			pstmt.setString(1, paraMap.get("productid")); // 공식
			pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식
			pstmt.setInt(3, (currentShowPageNo * sizePerPage)); // 공식 
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {  
			   	
				ReviewVO rvo = new ReviewVO();
				
				 rvo.setSeq_review(rs.getString(2));
				 rvo.setFk_productid(rs.getNString(3));
				 rvo.setColor(rs.getString(4));
				 rvo.setUserid(rs.getString(5));
				 rvo.setContent(rs.getString(6));
				 rvo.setScore(rs.getString(7));
				 rvo.setReview_image(rs.getString(8));
				 rvo.setWirteday(rs.getString(9));
				 rvo.setPurchaswno(rs.getString(10));
				 reviewlist.add(rvo);
		
			}
			
		}
		 finally {
			close();
		}		
		
		return reviewlist;	
	}

	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
		
		int totalPage = 0;
		
		
		
		try {
			conn = ds.getConnection();
			
			 String sql = " select ceil( count(*)/ 2 ) "+
					 " from review "+
					 " where fk_productid = ? ";
					    
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("productid"));
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
		} finally {
			close();
		}		
		
		return totalPage;				
		
	}	

	
}
