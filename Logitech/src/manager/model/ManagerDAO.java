package manager.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.model.MemberVO;
import member.model.MembershipVO;
import myshop.model.CouponVO;
import myshop.model.EachCouponVO;
import myshop.model.EventVO;
import myshop.model.JoinEventVO;
import myshop.model.OneQueryVO;
import myshop.model.ProductCategoryVO;
import myshop.model.ProductOptionVO;
import myshop.model.ProductQAVO;
import myshop.model.ProductVO;
import myshop.model.PurchaseDetailVO;
import myshop.model.PurchaseVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class ManagerDAO implements InterManagerDAO {
	
	private DataSource ds;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	public ManagerDAO() {
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semi_oracle");
		    
		    aes = new AES256(SecretMyKey.KEY);  // SecretMyKey.KEY 는 우리가 만든 비밀키이다.
		} catch(NamingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}// end of public ManagerDAO(){}---------------------
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기
	private void close() {
		try {
		       if(rs != null)    {rs.close(); rs=null;}
		       if(pstmt != null) {pstmt.close(); pstmt=null;}
		       if(conn != null)  {conn.close(); conn=null;}
		    } catch(SQLException e) {
		       e.printStackTrace();
		    }
	}// end of private void close(){}----------------------

	
	// 관리자 로그인 화면에서 입력받은 정보에 해당하는 한 명의 정보를 select 하여 반환하는 메소드(관리자 로그인 메소드)
	@Override
	public ManagerVO selectOneManager(Map<String, String> paraMap) throws SQLException {
		ManagerVO mvo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select managerid, managerpwd, managertype, manageremail, managermobile, managerno\n"+
						 "from manager\n"+
						 "where managerid = ? and managerpwd = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("managerId"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("managerPwd")));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mvo = new ManagerVO();
				mvo.setManagerid(rs.getString(1));
				mvo.setManagerpwd(rs.getString(2));
				mvo.setManagertype(rs.getString(3));
				mvo.setManageremail(aes.decrypt(rs.getString(4)));
				mvo.setManagermobile(aes.decrypt(rs.getString(5)));
				mvo.setManagerno(rs.getInt(6));
			}// end of if(rs.next()){}-----------------------
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return mvo;
	}// end of public ManagerVO selectOneManager(Map<String, String> paraMap) throws SQLException{}

	
	// 페이징 처리를 하여 모든 회원 또는 검색한 회원 목록 보여주기 메소드
	@Override
	public List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException {
		List<MemberVO> memberList = new ArrayList<MemberVO>();
		
		try {
			conn = ds.getConnection();
			/*
			String sql = "select memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point, registerday, lastpwdchangeday, idle, status, dropday\n"+
					"from\n"+
					"    (\n"+
					"    select rownum AS rno, memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point, registerday, lastpwdchangeday, idle, status, dropday\n"+
					"    from\n"+
					"        (\n"+
					"        select memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point\n"+
					"             , to_char(registerday, 'yyyy-mm-dd') AS registerday\n"+
					"             , to_char(lastpwdchangeday, 'yyyy-mm-dd') AS lastpwdchangeday\n"+
					"             , idle, status, dropday\n"+
					"        from member\n";
			*/
			String sql = "select memberno, userid, birthday, agreeemail, agreesms, fk_membershipname, registerday, idle, status\n"+
					"from\n"+
					"    (\n"+
					"    select rownum AS rno, memberno, userid, birthday, agreeemail, agreesms, fk_membershipname, registerday, idle, status\n"+
					"    from\n"+
					"        (\n"+
					"        select memberno, userid, birthday, agreeemail, agreesms, fk_membershipname\n"+
					"             , to_char(registerday, 'yyyy-mm-dd') AS registerday\n"+
					"             , idle, status\n"+
					"        from member\n";
					
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			/*
			String memberno = paraMap.get("memberno");
			String userid = paraMap.get("userid");
			String birthday = paraMap.get("birthday");
			String agreeemail = paraMap.get("agreeemail");
			String agreesms = paraMap.get("agreesms");
			String fk_membershipname = paraMap.get("fk_membershipname");
			String registerday = paraMap.get("registerday");
			String idle = paraMap.get("idle");
			String status = paraMap.get("status");
			*/
			String selectedHead = paraMap.get("selectedHead");
			String sortFlag = paraMap.get("sortFlag");
			
			// DAO(SQL문)
			if(selectedHead != null && !selectedHead.trim().isEmpty()) {
				// 정렬 선택 시 넘겨받은 sortFlag 순으로 정렬
				sql += "        order by "+selectedHead+" "+sortFlag+"\n";
			} else {
				// 기본 정렬
				sql += "        order by memberno desc\n";
				
			/*
			} else if(userid != null && !userid.trim().isEmpty()) {
				sql += "        order by "+userid+" "+sortFlag+"\n";
			} else if(birthday != null && !birthday.trim().isEmpty()) {
				sql += "        order by "+birthday+" "+sortFlag+"\n";
			} else if(agreeemail != null && !agreeemail.trim().isEmpty()) {
				sql += "        order by "+agreeemail+" "+sortFlag+"\n";
			} else if(agreesms != null && !agreesms.trim().isEmpty()) {
				sql += "        order by "+agreesms+" "+sortFlag+"\n";
			} else if(fk_membershipname != null && !fk_membershipname.trim().isEmpty()) {
				sql += "        order by "+fk_membershipname+" "+sortFlag+"\n";
			} else if(registerday != null && !registerday.trim().isEmpty()) {
				sql += "        order by "+registerday+" "+sortFlag+"\n";
			} else if(idle != null && !idle.trim().isEmpty()) {
				sql += "        order by "+idle+" "+sortFlag+"\n";
			} else if(status != null && !status.trim().isEmpty()) {
				sql += "        order by "+userid+" "+sortFlag+"\n";
			*/
			}// end of if(selectedHead != null && !selectedHead.trim().isEmpty() ){}---------------------
			
			
			sql += "        ) V\n"+
				"    ) T\n"+
				"where T.rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentPageNo = Integer.parseInt(paraMap.get("currentPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(1, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(2, searchKey2);
					pstmt.setInt(3, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(4, (currentPageNo * sizePerPage));
				} else {
					pstmt.setInt(2, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(3, (currentPageNo * sizePerPage));
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			} else {
				pstmt.setInt(1, (currentPageNo - 1 ) * sizePerPage + 1);
				pstmt.setInt(2, (currentPageNo * sizePerPage));
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				/*
				 * mvo.setMemberno(rs.getInt(1)); mvo.setUserid(rs.getString(2));
				 * mvo.setPwd(rs.getString(3)); mvo.setName(rs.getString(4));
				 * mvo.setEmail(rs.getString(5)); mvo.setMobile(rs.getString(6));
				 * mvo.setBirthday(rs.getString(7)); mvo.setPostcode(rs.getString(8));
				 * mvo.setAddress(rs.getString(9)); mvo.setDetailaddress(rs.getString(10));
				 * mvo.setExtraaddress(rs.getString(11)); mvo.setAgreeemail(rs.getString(12));
				 * mvo.setAgreesms(rs.getString(13)); mvo.setAgreethird(rs.getString(14));
				 * mvo.setFk_membershipname(rs.getString(15)); mvo.setPoint(rs.getInt(16));
				 * mvo.setRegisterday(rs.getString(17));
				 * mvo.setLastpwdchangeday(rs.getString(18)); mvo.setIdle(rs.getInt(19));
				 * mvo.setStatus(rs.getInt(20)); mvo.setDropday(rs.getString(21));
				 */
				mvo.setMemberno(rs.getInt(1));
				mvo.setUserid(rs.getString(2));
				mvo.setBirthday(rs.getString(3));
				mvo.setAgreeemail(rs.getString(4));
				mvo.setAgreesms(rs.getString(5));
				mvo.setFk_membershipname(rs.getString(6));
				mvo.setRegisterday(rs.getString(7));
				mvo.setIdle(rs.getInt(8));
				mvo.setStatus(rs.getInt(9));
				
				memberList.add(mvo);
			}// end of while(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return memberList;
	}

	
	// 페이징 처리를 위해 전체회원에 대한 총 페이지 수 알아오기(select) 메소드
	@Override
	public int getTotalPage(Map<String, String> paraMap) throws SQLException {
		int totalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select ceil(count(*)/?)\n"+
						"from member\n";					
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(2, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(3, searchKey2);
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchWord != null && !searchWord.trim().isEmpty()){}----------------------
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalPage = rs.getInt(1);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return totalPage;
	}// end of public int getTotalPage(Map<String, String> paraMap) throws SQLException{}-----------

	
	// 페이징 처리를 하여 모든 제품 또는 검색한 제품 목록 보여주기 메소드
	@Override
	public List<ProductOptionVO> selectPagingProductOption(Map<String, String> paraMap) throws SQLException {
		List<ProductOptionVO> poList = new ArrayList<ProductOptionVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price\n"+
					"from\n"+
					"    (\n"+
					"    select rownum AS rno, productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price\n"+
					"    from\n"+
					"        (\n"+
					"        select productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price\n"+
					"        from product P join productoption O\n"+
					"        on P.productid = O.fk_productid\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			// if 조건문을 통해 검색조건에 대한 경우의 수를 구분
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			String selectedHead = paraMap.get("selectedHead");
			String sortFlag = paraMap.get("sortFlag");
			
			if(selectedHead != null && !selectedHead.trim().isEmpty()) {
				sql += "        order by "+selectedHead+" "+sortFlag+"\n";
			} else {
				sql += "        order by productserialid\n";
			}// end of if(selectedHead != null && !selectedHead.trim().isEmpty() ){}---------------------
			
			sql += "        ) V\n"+
				"    ) T\n"+
				"where T.rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentPageNo = Integer.parseInt(paraMap.get("currentPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(1, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(2, searchKey2);
					pstmt.setInt(3, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(4, (currentPageNo * sizePerPage));
				} else {
					pstmt.setInt(2, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(3, (currentPageNo * sizePerPage));
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			} else {
				pstmt.setInt(1, (currentPageNo - 1 ) * sizePerPage + 1);
				pstmt.setInt(2, (currentPageNo * sizePerPage));
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductOptionVO povo = new ProductOptionVO();
				
				povo.setProductserialid(rs.getString(1));
				povo.setFk_productid(rs.getString(2));
				povo.setColor(rs.getString(3));
				povo.setStock(rs.getInt(4));
				povo.setSaleday(rs.getString(5));
				
				ProductVO pvo = new ProductVO();
				pvo.setProductname(rs.getString(6));
				pvo.setFk_category(rs.getString(7));
				pvo.setCharacter(rs.getString(8));
				pvo.setPrice(rs.getInt(9));
				povo.setPvo(pvo);
				
				poList.add(povo);
			}// end of while(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return poList;
	}// end of public List<ProductOptionVO> selectPagingProductOption(Map<String, String> paraMap) throws SQLException{}

	
	// 페이징 처리를 위해 전체판매제품에 대한 총 페이지 수 알아오기(select) 메소드
	@Override
	public int getPOTotalPage(Map<String, String> paraMap) throws SQLException {
		int pototalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select ceil(count(*)/?)\n"+
						"from product P join productoption O\n"+
						"on P.productid = O.fk_productid";					
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(2, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(3, searchKey2);
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchWord != null && !searchWord.trim().isEmpty()){}----------------------
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pototalPage = rs.getInt(1);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return pototalPage;
	}// end of public int getPOTotalPage(Map<String, String> paraMap) throws SQLException{}---------

	
	// 재고량을 수정하는 메소드
	@Override
	public int stockUpdate(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			// DAO
			String sql = "update productoption set stock = stock "+paraMap.get("mark")+" ?\n"+
						"where productserialid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(paraMap.get("stockUpdate")));
			pstmt.setString(2, paraMap.get("productserialid"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return result;
	}// end of public int stockUpdate(Map<String, String> paraMap) throws SQLException {}
	
	
	// memberno 값을 가져와 회원 1명에 대한 상세정보 알아오기(select) 메소드
	@Override
	public MemberVO memberOneDetail(String memberno) throws SQLException {
		MemberVO mvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select memberno, userid, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress\n"+
						 "     , case when agreeemail = '1' then '수신' else '거절' end AS agreeemail\n"+
						 "     , case when agreesms = '1' then '수신' else '거절' end AS agreesms\n"+
						 "     , case when agreethird = '1' then '동의' else '거절' end AS agreethird\n"+
						 "     , fk_membershipname, point\n"+
						 "     , to_char(registerday, 'yyyy-mm-dd') AS registerday\n"+
						 "     , to_char(lastpwdchangeday, 'yyyy-mm-dd') AS lastpwdchangeday\n"+
						 "     , idle, status, dropday\n"+
						 "from member\n"+
						 "where memberno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mvo = new MemberVO();
				mvo.setMemberno(rs.getInt(1));
				mvo.setUserid(rs.getString(2));
				mvo.setName(rs.getString(3));
				mvo.setEmail(aes.decrypt(rs.getString(4)));
				mvo.setMobile(aes.decrypt(rs.getString(5)));
				mvo.setBirthday(rs.getString(6));
				mvo.setPostcode(rs.getString(7));
				mvo.setAddress(rs.getString(8));
				mvo.setDetailaddress(rs.getString(9));
				mvo.setExtraaddress(rs.getString(10));
				mvo.setAgreeemail(rs.getString(11));
				mvo.setAgreesms(rs.getString(12));
				mvo.setAgreethird(rs.getString(13));
				mvo.setFk_membershipname(rs.getString(14));
				mvo.setPoint(rs.getInt(15));
				mvo.setRegisterday(rs.getString(16));
				mvo.setLastpwdchangeday(rs.getString(17));
				mvo.setIdle(rs.getInt(18));
				mvo.setStatus(rs.getInt(19));
				mvo.setDropday(rs.getString(20));
			}// end of if(rs.next()){}------------------------
		
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return mvo;
	}// end of public MemberVO memberOneDetail(String memberno) throws SQLException{}---------------

	
	// productserialid 값을 가져와 선택한 판매제품에 대한 상세정보 알아오기(select) 메소드
	@Override
	public ProductOptionVO prodOptionOneDetail(String productserialid) throws SQLException {
		ProductOptionVO povo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productid, productserialid, productname, color, character, price, stock, saleday\n"+
						"from product P join productoption O\n"+
						"on P.productid = O.fk_productid\n"+
						"where productserialid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productserialid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				povo = new ProductOptionVO();
				
				ProductVO pvo = new ProductVO();
				pvo.setProductid(rs.getString(1));
				povo.setProductserialid(rs.getString(2));
				pvo.setProductname(rs.getString(3));
				povo.setColor(rs.getString(4));
				pvo.setCharacter(rs.getString(5));
				pvo.setPrice(rs.getInt(6));
				povo.setStock(rs.getInt(7));
				povo.setSaleday(rs.getString(8));
				povo.setPvo(pvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return povo;
	}// end of public ProductOptionVO prodOptionOneDetail(String productserialid) throws SQLException{}

	
	// 페이징 처리를 하여 모든 주문 또는 검색한 주문 목록 보여주기 메소드
	@Override
	public List<PurchaseDetailVO> selectPagingPurchaseList(Map<String, String> paraMap) throws SQLException {
		List<PurchaseDetailVO> pdList = new ArrayList<PurchaseDetailVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, discount, totalstatus\n"+
					"from\n"+
					"(\n"+
					"select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus\n"+
					"from\n"+
					"(\n"+
					"select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus\n"+
					"from\n"+
					"(\n"+
					"select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus\n"+
					"from purchasedetail D\n"+
					"join purchase P\n"+
					"on D.fk_purchaseno = P.purchaseno\n"+
					"join product R\n"+
					"on D.fk_productid = R.productid\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			sql += ") V\n"+
					"group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus\n";
			
			String selectedHead = paraMap.get("selectedHead");
			String sortFlag = paraMap.get("sortFlag");
			
			if(selectedHead != null && !selectedHead.trim().isEmpty()) {
				sql += "        order by "+selectedHead+" "+sortFlag+"\n";
			} else {
				sql += "        order by fk_purchaseno desc\n";
			}// end of if(selectedHead != null && !selectedHead.trim().isEmpty() ){}---------------------
			/*
			sql += ") V\n"+
					"group by fk_purchaseno, fk_memberno, payment, purchaseday\n"+
					") W\n"+
					") X\n"+
					"where X.rno between ? and ?";
			*/
			sql += ") W\n"+
					") X\n"+
					"where X.rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentPageNo = Integer.parseInt(paraMap.get("currentPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(1, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(2, searchKey2);
					pstmt.setInt(3, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(4, (currentPageNo * sizePerPage));
				} else {
					pstmt.setInt(2, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(3, (currentPageNo * sizePerPage));
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			} else {
				pstmt.setInt(1, (currentPageNo - 1 ) * sizePerPage + 1);
				pstmt.setInt(2, (currentPageNo * sizePerPage));
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PurchaseDetailVO pdvo = new PurchaseDetailVO();
				
				pdvo.setFk_purchaseno(rs.getInt(1));
				
				PurchaseVO purcvo = new PurchaseVO();
				purcvo.setFk_memberno(rs.getInt(2));
				purcvo.setPayment(rs.getString(3));
				purcvo.setPurchaseday(rs.getString(4));
				
				pdvo.setDetailtotalpay(rs.getInt(5));
				
				purcvo.setTotalprice(rs.getInt(6));
				purcvo.setDiscount(rs.getInt(7));
				purcvo.setTotalstatus(rs.getString(8));
				
				pdvo.setPurcvo(purcvo);
				
				pdList.add(pdvo);
			}// end of while(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return pdList;
	}// end of public List<PurchaseDetailVO> selectPagingPurchaseList(Map<String, String> paraMap) throws SQLException{}
	
	
	// 페이징 처리를 위해 전체 주문에 대한 총 페이지 수 알아오기(select) 메소드
	@Override
	public int getPurcTotalPage(Map<String, String> paraMap) throws SQLException {
		int purctotalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select ceil(count(*)/?)\n"+
					"from\n"+
					"(\n"+
					"select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus\n"+
					"from\n"+
					"(\n"+
					"select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus\n"+
					"from\n"+
					"(\n"+
					"select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus\n"+
					"from purchasedetail D\n"+
					"join purchase P\n"+
					"on D.fk_purchaseno = P.purchaseno\n"+
					"join product R\n"+
					"on D.fk_productid = R.productid\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			sql += "order by fk_purchaseno desc\n"+
					") V\n"+
					"group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus\n"+
					") W\n"+
					") X";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(2, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(3, searchKey2);
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchWord != null && !searchWord.trim().isEmpty()){}----------------------
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				purctotalPage = rs.getInt(1);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return purctotalPage;
	}// end of public int getPurcTotalPage(Map<String, String> paraMap) throws SQLException{}

	
	// purchasedetailid 값을 가져와 선택한 주문에 대한 상세정보 알아오기(select) 메소드
	@Override
	public List<PurchaseDetailVO> purchaseOneDetail(String fk_purchaseno) throws SQLException {
		List<PurchaseDetailVO> pdList = new ArrayList<PurchaseDetailVO>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select purchasedetailid, fk_productserialid, volume, price, deliverystatus, imgfilename\n"+
						"from purchasedetail D\n"+
						"join product R\n"+
						"on D.fk_productid = R.productid\n"+
						"where fk_purchaseno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_purchaseno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PurchaseDetailVO pdvo = new PurchaseDetailVO();
				
				pdvo.setPurchasedetailid(rs.getInt(1));
				pdvo.setFk_productserialid(rs.getString(2));
				pdvo.setVolume(rs.getInt(3));
				
				ProductVO pvo = new ProductVO();
				pvo.setPrice(rs.getInt(4));
				
				pdvo.setDeliverystatus(rs.getString(5));
				
				pvo.setImgfilename(rs.getString(6));
				pdvo.setPvo(pvo);
				
				pdList.add(pdvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return pdList;
	}// end of public PurchaseDetailVO purchaseOneDetail(String purchasedetailid) throws SQLException
	
	
	// purchasedetailid 값을 가져와 주문자에 대한 상세정보 알아오기(select) 메소드
	@Override
	public PurchaseVO purchaseByMember(String fk_purchaseno) throws SQLException {
		PurchaseVO purcvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select purchaseno, name, receiver, P.postcode, P.address, P.detailaddress, P.extraaddress, payment, totalprice, purchaseday\n"+
						"from purchase P\n"+
						"join member M\n"+
						"on P.fk_memberno = M.memberno\n"+
						"where purchaseno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_purchaseno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				purcvo = new PurchaseVO();
				
				purcvo.setPurchaseno(rs.getInt(1));
				
				MemberVO mvo = new MemberVO();
				mvo.setName(rs.getString(2));
				
				purcvo.setReceiver(rs.getString(3));
				purcvo.setPostcode(rs.getString(4));
				purcvo.setAddress(rs.getString(5));
				purcvo.setDetailaddress(rs.getString(6));
				purcvo.setExtraaddress(rs.getString(7));
				purcvo.setPayment(rs.getString(8));
				purcvo.setTotalprice(rs.getInt(9));
				purcvo.setPurchaseday(rs.getString(10));
				purcvo.setMvo(mvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return purcvo;
	}// end of public PurchaseVO purchaseByMember(String fk_purchaseno) throws SQLException{}
	
	
	// 페이징 처리를 하여 모든 쿠폰 또는 검색한 쿠폰 목록 보여주기 메소드
	@Override
	public List<EachCouponVO> selectPagingCoupon(Map<String, String> paraMap) throws SQLException {
		List<EachCouponVO> ecList = new ArrayList<EachCouponVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
					"from\n"+
					"(\n"+
					"select rownum AS rno, fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
					"from\n"+
					" (\n"+
					" select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
					" from eachcoupon E left join coupon C\n"+
					" on E.fk_couponcode = C.couponcode\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			String selectedHead = paraMap.get("selectedHead");
			String sortFlag = paraMap.get("sortFlag");
			
			if(selectedHead != null && !selectedHead.trim().isEmpty()) {
				sql += "        order by "+selectedHead+" "+sortFlag+"\n";
			} else {
				sql += " order by endday desc\n";
			}// end of if(selectedHead != null && !selectedHead.trim().isEmpty() ){}---------------------
			
			sql += " ) V\n"+
				") T\n"+
				"where T.rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentPageNo = Integer.parseInt(paraMap.get("currentPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(1, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(2, searchKey2);
					pstmt.setInt(3, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(4, (currentPageNo * sizePerPage));
				} else {
					pstmt.setInt(2, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(3, (currentPageNo * sizePerPage));
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			} else {
				pstmt.setInt(1, (currentPageNo - 1 ) * sizePerPage + 1);
				pstmt.setInt(2, (currentPageNo * sizePerPage));
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EachCouponVO ecvo = new EachCouponVO();
				ecvo.setFk_couponcode(rs.getString(1));
				ecvo.setEachcouponcode(rs.getString(2));
				
				CouponVO coupvo = new CouponVO();
				coupvo.setCouponname(rs.getString(3));
				coupvo.setDiscount(rs.getInt(4));
				coupvo.setMinprice(rs.getInt(5));
				coupvo.setFk_membershipname(rs.getString(6));
				
				ecvo.setFk_memberno(rs.getInt(7));
				ecvo.setStatus(rs.getInt(8));
				ecvo.setEndday(rs.getString(9));
				
				ecvo.setCoupvo(coupvo);
				
				ecList.add(ecvo);
			}// end of while(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return ecList;
	}// end of public List<EachCouponVO> selectPagingCoupon(Map<String, String> paraMap) throws SQLException{}

	
	// 페이징 처리를 위해 전체 쿠폰에 대한 총 페이지 수 알아오기(select) 메소드
	@Override
	public int getCoupTotalPage(Map<String, String> paraMap) throws SQLException {
		int coupTotalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select ceil(count(*)/?)\n"+
					"from\n"+
					"(\n"+
					"select rownum AS rno, fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
					"from\n"+
					" (\n"+
					" select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
					" from eachcoupon E left join coupon C\n"+
					" on E.fk_couponcode = C.couponcode\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			sql += " order by endday desc\n" +
					" ) V\n"+
					") T";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(2, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(3, searchKey2);
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchWord != null && !searchWord.trim().isEmpty()){}----------------------
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				coupTotalPage = rs.getInt(1);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return coupTotalPage;
	}// end of public int getCoupTotalPage(Map<String, String> paraMap) throws SQLException{}

	
	// eachcouponcode 값을 가져와 개별 쿠폰에 대한 상세정보 알아오기(select) 메소드
	@Override
	public EachCouponVO couponOneDetail(String eachcouponcode) throws SQLException {
		EachCouponVO ecvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday\n"+
						"from eachcoupon E\n"+
						"right join coupon C\n"+
						"on E.fk_couponcode = C.couponcode\n"+
						"where eachcouponcode = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, eachcouponcode);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ecvo = new EachCouponVO();
				
				ecvo.setFk_couponcode(rs.getString(1));
				ecvo.setEachcouponcode(rs.getString(2));
				
				CouponVO coupvo = new CouponVO();
				coupvo.setCouponname(rs.getString(3));
				coupvo.setDiscount(rs.getInt(4));
				coupvo.setMinprice(rs.getInt(5));
				coupvo.setFk_membershipname(rs.getString(6));
				
				ecvo.setFk_memberno(rs.getInt(7));
				ecvo.setStatus(rs.getInt(8));
				ecvo.setEndday(rs.getString(9));
				ecvo.setCoupvo(coupvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return ecvo;
	}// end of public EachCouponVO couponOneDetail(String eachcouponcode) throws SQLException{}

	
	// 페이징 처리를 하여 모든 쿠폰 또는 검색한 쿠폰 목록 보여주기 메소드
	@Override
	public List<EventVO> selectPagingEvent(Map<String, String> paraMap) throws SQLException {
		List<EventVO> evoList = new ArrayList<EventVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select seq_event, eventname, fk_productid, startday, endday, joincnt\n"+
					"from\n"+
					"(\n"+
					"select rownum AS rno, seq_event, eventname, fk_productid, startday, endday, nvl(joincnt, 0) AS joincnt\n"+
					"from\n"+
					"(\n"+
					"select seq_event, eventname, fk_productid, startday, endday, joincnt\n"+
					"from\n"+
					"(\n"+
					"select fk_event, count(*) AS joincnt\n"+
					"from joinevent\n"+
					"group by fk_event\n"+
					") V\n"+
					"right join event E\n"+
					"on V.fk_event = E.seq_event\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			String selectedHead = paraMap.get("selectedHead");
			String sortFlag = paraMap.get("sortFlag");
			
			if(selectedHead != null && !selectedHead.trim().isEmpty()) {
				sql += "        order by "+selectedHead+" "+sortFlag+"\n";
			} else {
				sql += " order by seq_event desc\n";
			}// end of if(selectedHead != null && !selectedHead.trim().isEmpty() ){}---------------------
			
			sql += ") W\n"+
				") X\n"+
				"where X.rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			
			int currentPageNo = Integer.parseInt(paraMap.get("currentPageNo"));
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(1, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(2, searchKey2);
					pstmt.setInt(3, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(4, (currentPageNo * sizePerPage));
				} else {
					pstmt.setInt(2, (currentPageNo - 1 ) * sizePerPage + 1);
					pstmt.setInt(3, (currentPageNo * sizePerPage));
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			} else {
				pstmt.setInt(1, (currentPageNo - 1 ) * sizePerPage + 1);
				pstmt.setInt(2, (currentPageNo * sizePerPage));
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EventVO evo = new EventVO();
				evo.setSeq_event(rs.getInt(1));
				evo.setEventname(rs.getString(2));
				evo.setFk_productid(rs.getString(3));
				evo.setStartday(rs.getNString(4));
				evo.setEndday(rs.getString(5));
				evo.setJoincnt(rs.getInt(6));
				
				evoList.add(evo);
			}// end of while(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return evoList;
	}// end of public List<EventVO> selectPagingEvent(Map<String, String> paraMap) throws SQLException

	
	// 페이징 처리를 위해 전체 이벤트에 대한 총 페이지 수 알아오기(select) 메소드
	@Override
	public int getEventTotalPage(Map<String, String> paraMap) throws SQLException {
		int eventTotalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select ceil(count(*)/?)\n"+
						" from event\n";
			
			String searchOption1 = paraMap.get("searchOption1");
			String searchKey1 = paraMap.get("searchKey1");
			String searchOption2 = paraMap.get("searchOption2");
			String searchKey2 = paraMap.get("searchKey2");
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				sql += " where upper("+searchOption1+") like upper('%'||?||'%')\n";
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					sql += " and upper("+searchOption2+") like upper('%'||?||'%')\n";
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchKey1 != null && !searchKey1.trim().isEmpty() ){}---------------------
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sizePerPage"));
			
			if(searchKey1 != null && !searchKey1.trim().isEmpty() ) {
				pstmt.setString(2, searchKey1);
				
				if(searchKey2 != null && !searchKey2.trim().isEmpty() ) {
					pstmt.setString(3, searchKey2);
				}// end of if(searchKey2 != null && !searchKey2.trim().isEmpty() ){}---------------------
			}// end of if(searchWord != null && !searchWord.trim().isEmpty()){}----------------------
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				eventTotalPage = rs.getInt(1);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return eventTotalPage;
	}// end of public int getEventTotalPage(Map<String, String> paraMap) throws SQLException{}

	
	// seq_event 값을 가져와 개별 이벤트에 대한 상세정보 알아오기(select) 메소드
	@Override
	public List<JoinEventVO> eventOneDetail(String seq_event) throws SQLException {
		List<JoinEventVO> jevoList = new ArrayList<JoinEventVO>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select seq_event, seq_joinevent, userid, eventcomment, winstatus\n"+
					"from joinevent J\n"+
					"right join member M\n"+
					"on J.fk_memberno = M.memberno\n"+
					"right join event E\n"+
					"on J.fk_event = E.seq_event\n"+
					"where seq_event = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq_event);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JoinEventVO jevo = new JoinEventVO();
				
				EventVO evo = new EventVO();
				evo.setSeq_event(rs.getInt(1));
				
				jevo.setSeq_joinevent(rs.getInt(2));
				
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(3));
				
				jevo.setEventcomment(rs.getString(4));
				jevo.setWinstatus(rs.getInt(5));
				jevo.setEvo(evo);
				jevo.setMvo(mvo);
				
				jevoList.add(jevo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return jevoList;
	}// end of public JoinEventVO eventOneDetail(String seq_event) throws SQLException{}

	
	// Ajax(JSON) 를 사용하여 일대일 문의 목록을 "더 보기" 방식으로 페이징 처리하기 위해 전체 문의 수를 알아오는 메소드
	@Override
	public int totalOQCount(String today) throws SQLException {
		int totalOQCount = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select count(*)\n"+
						 "from onequery\n"+
						 "where to_char(writeday, 'yyyy-mm-dd') = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, today);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalOQCount = rs.getInt(1);
			}// end of if(rs.next()){}---------------------
		} finally {
			close();
		}
		
		return totalOQCount;
	}// end of public int totalOQCount(String seq_oq) throws SQLException{}

	
	// Ajax(JSON) 를 이용하여 "더 보기" 방식(페이징 처리)으로 일대일 문의를 5개씩 잘라서(start ~ end) 조회하는 메소드
	@Override
	public List<OneQueryVO> selectByWriteday(Map<String, String> paraMap) throws SQLException {
		List<OneQueryVO> oqvoList = new ArrayList<OneQueryVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select seq_oq, userid, category, title, answerstatus\n"+
					"from\n"+
					" (\n"+
					" select row_number() over(order by seq_oq asc) AS rno, seq_oq, userid, category, title, answerstatus\n"+
					" from onequery O\n"+
					" join member M\n"+
					" on O.fk_memberno = M.memberno\n"+
					" where to_char(writeday, 'yyyy-mm-dd') = ?\n"+
					" ) V\n"+
					"where rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("today"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OneQueryVO oqvo = new OneQueryVO();
				
				oqvo.setSeq_oq(rs.getInt(1));
				
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(2));
				
				oqvo.setCategory(rs.getString(3));
				oqvo.setTitle(rs.getString(4));
				oqvo.setAnswerstatus(rs.getInt(5));
				oqvo.setMvo(mvo);
				
				oqvoList.add(oqvo);
			}// end of while(rs.next()){}---------------------
		} finally {
			close();
		}
		
		return oqvoList;
	}// end of public List<OneQueryVO> selectByWriteday(Map<String, String> paraMap) throws SQLException

	
	// 일대일 문의 처리를 위해 상세정보 알아오기(select) 메소드
	@Override
	public OneQueryVO oneQueryDetail(String seq_oq) throws SQLException {
		OneQueryVO oqvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select seq_oq, userid, category, title, content\n"+
					" from onequery O\n"+
					" join member M\n"+
					" on O.fk_memberno = M.memberno\n"+
					" where seq_oq = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq_oq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				oqvo = new OneQueryVO();
				
				oqvo.setSeq_oq(rs.getInt(1));
				
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(2));
				
				oqvo.setCategory(rs.getString(3));
				oqvo.setTitle(rs.getString(4));
				oqvo.setContent(rs.getString(5));
				oqvo.setMvo(mvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return oqvo;
	}// end of public OneQueryVO oneQueryDetail(String seq_oq) throws SQLException{}

	
	// Ajax(JSON) 를 사용하여 일대일 문의 목록을 "더 보기" 방식으로 페이징 처리하기 위해 전체 문의 수를 알아오는 메소드
	@Override
	public int totalPQACount(String today) throws SQLException {
		int totalPQACount = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select count(*)\n"+
						 "from productqa\n"+
						 "where to_char(writeday, 'yyyy-mm-dd') = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, today);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalPQACount = rs.getInt(1);
			}// end of if(rs.next()){}---------------------
		} finally {
			close();
		}
		
		return totalPQACount;
	}// end of public int totalPQACount(String today) throws SQLException{}------------------------

	
	// Ajax(JSON) 를 이용하여 "더 보기" 방식(페이징 처리)으로 제품 문의를 5개씩 잘라서(start ~ end) 조회하는 메소드
	@Override
	public List<ProductQAVO> selectPQAByWriteday(Map<String, String> paraMap) throws SQLException {
		List<ProductQAVO> pqaList = new ArrayList<ProductQAVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select seq_qa, userid, fk_productid, content, V.status\n"+
					"from\n"+
					" (\n"+
					" select row_number() over(order by seq_qa asc) AS rno, seq_qa, userid, fk_productid, content, Q.status\n"+
					" from productqa Q\n"+
					" join member M\n"+
					" on Q.fk_memberno = M.memberno\n"+
					" where to_char(writeday, 'yyyy-mm-dd') = ?\n"+
					" ) V\n"+
					"where rno between ? and ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("today"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductQAVO pqavo = new ProductQAVO();
				
				pqavo.setSeq_qa(rs.getInt(1));
				
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(2));
				
				pqavo.setFk_productid(rs.getString(3));
				pqavo.setContent(rs.getString(4));
				pqavo.setStatus(rs.getInt(5));
				pqavo.setMvo(mvo);
				
				pqaList.add(pqavo);
			}// end of while(rs.next()){}---------------------
		} finally {
			close();
		}
		
		return pqaList;
	}// end of public List<ProductQAVO> selectPQAByWriteday(Map<String, String> paraMap) throws SQLException

	
	// 매니저 계정등록 메소드(manager 테이블에 insert)
	@Override
	public int registerManager(ManagerVO mgvo) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "insert into manager(managerno, managerid, managerpwd, managertype, manageremail, managermobile) "
					   + "values(manager_seq.nextval, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mgvo.getManagerid());
			pstmt.setString(2, Sha256.encrypt(mgvo.getManagerpwd()));  // 암호를 SHA256 알고리즘으로 단방향 암호화 한다.
			pstmt.setString(3, mgvo.getManagertype());
			pstmt.setString(4, aes.encrypt(mgvo.getManageremail()));  // 이메일을 AES256 알고리즘으로 양방향 암호화 한다.
			pstmt.setString(5, aes.encrypt(mgvo.getManagermobile()));  // 전화번호를 AES256 알고리즘으로 양방향 암호화 한다.
			
			result = pstmt.executeUpdate();		
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}// end of public int registerManager(ManagerVO mgvo) throws SQLException {}-------------------

	// modal 창에서 userid 클릭 시 아이디, 이메일, 전화번호, 수신 정보를 select 하는 메소드
	@Override
	public MemberVO checkAgreeStatus(String userid) throws SQLException {
		MemberVO mvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select name, email, mobile\n"+
						 "     , case when agreeemail = '1' then '수신' else '거절' end AS agreeemail\n"+
						 "     , case when agreesms = '1' then '수신' else '거절' end AS agreesms\n"+
						 "from member\n"+
						 "where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mvo = new MemberVO();
				mvo.setName(rs.getString(1));
				mvo.setEmail(aes.decrypt(rs.getString(2)));
				mvo.setMobile(aes.decrypt(rs.getString(3)));
				mvo.setAgreeemail(rs.getString(4));
				mvo.setAgreesms(rs.getString(5));
			}// end of if(rs.next()){}------------------------
		
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return mvo;
	}// end of public MemberVO checkAgreeStatus(String userid) throws SQLException {}

	
	// 일대일 문의글에 대한 답변 insert 메소드
	@Override
	public int oneQueryAnswer(Map<String, String> paraMap) throws SQLException {
		int insertResult = 0;
		int updateResult = 0;
		int result = 2;
		
		try {
			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "insert into oqanswer(seq_oq, oq_content) "
					   + "values(?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("seq_oq"));
			pstmt.setString(2, paraMap.get("oq_content"));
			
			insertResult = pstmt.executeUpdate();
			
			sql = "update onequery set answerstatus = 1 "
				+ "where seq_oq = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("seq_oq"));
			
			updateResult = pstmt.executeUpdate();
			
			if(insertResult == 1 && updateResult == 1) {
				conn.commit();
				result = 1;
			} else {
				conn.rollback();
			}// end of if(insertResult == 1 && updateResult == 1) {}-----------------------
		} catch(SQLException e) {
			conn.rollback();
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}// end of public int insertOneQueryAnswer(String seq_oq) throws SQLException {}---------------

	
	// 제품 문의 상세정보 select
	@Override
	public ProductQAVO productQADetail(String seq_qa) throws SQLException {
		ProductQAVO pqavo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select seq_qa, userid, fk_productid, content\n"+
					" from productqa P\n"+
					" join member M\n"+
					" on P.fk_memberno = M.memberno\n"+
					" where seq_qa = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq_qa);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pqavo = new ProductQAVO();
				
				pqavo.setSeq_qa(rs.getInt(1));
				
				MemberVO mvo = new MemberVO();
				mvo.setUserid(rs.getString(2));
				
				pqavo.setFk_productid(rs.getString(3));
				pqavo.setContent(rs.getString(4));
				pqavo.setMvo(mvo);
			}// end of if(rs.next()){}------------------------
		
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return pqavo;
	}// end of public ProductQAVO productQADetail(String seq_oq) throws SQLException {}

	
	// 제품 문의글에 대한 답변 insert 및 답변 상태 update 메소드
	@Override
	public int productQAAnswer(Map<String, String> paraMap) throws SQLException {
		int insertResult = 0;
		int updateResult = 0;
		int result = 2;
		
		try {
			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "insert into qaanswer(fk_seq_qa, content) "
					   + "values(?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("seq_qa"));
			pstmt.setString(2, paraMap.get("oq_content"));
			
			insertResult = pstmt.executeUpdate();
			
			sql = "update productqa set status = 1 "
				+ "where seq_qa = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("seq_qa"));
			
			updateResult = pstmt.executeUpdate();
			
			if(insertResult == 1 && updateResult == 1) {
				conn.commit();
				result = 1;
			} else {
				conn.rollback();
			}// end of if(insertResult == 1 && updateResult == 1) {}-----------------------
		} catch(SQLException e) {
			conn.rollback();
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}

	// 전체 회원 대상 탈퇴 상태 업데이트 메소드
	@Override
	public int statusUpdate() throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			

			
		} finally {
			close();
		}
		
		return result;
	}
	
	
	// 제품 가격 수정 메소드
	@Override
	public int priceUpdate(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "update product set price = ?\n"+
					 	"where productid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("price"));
			pstmt.setString(2, paraMap.get("productid"));
			
			result = pstmt.executeUpdate();
		
		} finally {
			close();
		}
		
		return result;
	}// end of public int priceUpdate(Map<String, String> paraMap) throws SQLException {}-----------------

	
	// 제품 정보 수정 메소드
	@Override
	public int productOptionUpdate(ProductOptionVO povo) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "update productoption set productserialid = ?, color = ?, stock = ?\n"+
					 	"where productserialid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, povo.getProductserialid());
			pstmt.setString(2, povo.getColor());
			pstmt.setInt(3, povo.getStock());
			pstmt.setString(4, povo.getProductserialid());
			
			result = pstmt.executeUpdate();
		
		} finally {
			close();
		}
		
		return result;
	}// end of public int statusUpdate() throws SQLException {}----------------------------

	
	// 주문 제품 배송하여 배송 상태와 배송 제품의 재고량을 수정하는 메소드
	@Override
	public int updateDelivery(Map<String, String> paraMap) throws SQLException {
		int result1 = 0;
		int result2 = 0;
		
		try {
			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "update productoption set stock = stock - ?\n"+
					 	"where productserialid = ?";
			
			int stock = Integer.parseInt(paraMap.get("stock"));
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stock);
			pstmt.setString(2, paraMap.get("productserialid"));
			
			result1 = pstmt.executeUpdate();
			
			sql = "update purchasedetail set deliverystatus = '배송완료'\n"+
				 "where purchasedetailid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("purchasedetailid"));
			
			result2 = pstmt.executeUpdate();
			
			if(result1 == 1 && result2 == 1) {
				// 재고 수정과 배송 상태 수정 모두 성공했을 때만 commit 실행
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch(SQLException e) {
			conn.rollback();
		} finally {
			close();
		}
		
		return result2;
	}// end of public int updateDelivery(Map<String, String> paraMap) throws SQLException {}

	
	// 주문 내 개별 상품 전체가 배송완료된 경우 주문 진행상태를 수정하는 메소드
	@Override
	public int purchaseListUpdate(String purchaseno) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "update purchase set totalstatus = '처리완료'\n"+
					 	"where purchaseno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, purchaseno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	}// end of public int purchaseListUpdate(String purchaseno) throws SQLException {}

	
	// 이벤트 당첨자 당첨 상태를 update 하는 메소드 
	@Override
	public int eventSelectWinner(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select memberno\n"+
						"from member\n"+
						"where userid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			int memberno = rs.getInt(1);
			
			sql = "update joinevent set winstatus = 1\n"+
				"where seq_joinevent = ? and fk_memberno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("fk_event"));
			pstmt.setInt(2, memberno);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}// end of public int eventSelectWinner(Map<String, String> paraMap) throws SQLException {}

	
	// 제품 카테고리 조회 메소드
	@Override
	public List<ProductCategoryVO> productCategory() throws SQLException {
		List<ProductCategoryVO> pcvoList = new ArrayList<ProductCategoryVO>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select category\n"+
						"from productcategory";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductCategoryVO pcvo = new ProductCategoryVO();
				pcvo.setCategory(rs.getString(1));
				pcvoList.add(pcvo);
			}
			
		} finally {
			close();
		}
		
		return pcvoList;
	}// end of public List<ProductCategoryVO> productCategory() throws SQLException {}-------------
	
	
	// 회원등급 테이블 조회 메소드
	@Override
	public List<MembershipVO> membership() throws SQLException {
		List<MembershipVO> msvoList = new ArrayList<MembershipVO>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select membershipname, minstandard, maxstandard\n"+
						"from membership";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MembershipVO msvo = new MembershipVO();
				msvo.setMembershipname(rs.getString(1));
				msvo.setMinstandard(rs.getInt(2));
				msvo.setMaxstandard(rs.getInt(3));
				msvoList.add(msvo);
			}
			
		} finally {
			close();
		}
		
		return msvoList;
	}// end of public List<MembershipVO> membership() throws SQLException {}---------------------

	
	// 쿠폰 코드 중복검사 메소드
	@Override
	public boolean codeDuplicateCheck(String couponcode) throws SQLException {
		boolean isExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select couponcode\n"+
						 "from coupon\n"+
						 "where couponcode = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, couponcode);
			
			rs = pstmt.executeQuery();
			
			isExist = rs.next();  // 행이 있으면 중복된 userid ==> true
								  // 행이 없으면 사용가능한 userid ==> false
			
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return isExist;
	}// end of public boolean codeDuplicateCheck(String couponcode) throws SQLException {}---------

	
	// 쿠폰 등록 메소드
	@Override
	public int registerCoupon(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "insert into coupon(couponcode, couponname, discount, minprice, fk_membershipname) "
					   + "values(?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("couponcode"));
			pstmt.setString(2, paraMap.get("couponname"));
			pstmt.setInt(3, Integer.parseInt(paraMap.get("discount")));
			pstmt.setInt(4, Integer.parseInt(paraMap.get("minprice")));
			pstmt.setString(5, paraMap.get("fk_membershipname"));
			
			result = pstmt.executeUpdate();		
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}// end of public int registerCoupon(Map<String, String> paraMap) throws SQLException {}

	
	// 이벤트 등록 시 카테고리별 상품 목록을 불러오는 메소드
	@Override
	public List<ProductVO> productByCategory(String category) throws SQLException {
		List<ProductVO> pvoList = new ArrayList<ProductVO>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productid\n"+
						"from product\n"+
						"where fk_category = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, category);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo = new ProductVO();
				pvo.setProductid(rs.getString(1));
				pvoList.add(pvo);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return pvoList;
	}// end of public List<ProductVO> productByCategory(String category) throws SQLException {}

	
	// 상품 이미지 조회 메소드
	@Override
	public ProductVO productImg(String productid) throws SQLException {
		ProductVO pvo = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productid, imgfilename\n"+
						"from product\n"+
						"where productid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pvo = new ProductVO();
				pvo.setProductid(rs.getString(1));
				pvo.setImgfilename(rs.getString(2));
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return pvo;
	}// end of public ProductVO productImg(String productid) throws SQLException {}----------------

	
	// 이벤트 등록 메소드
	@Override
	public int registerEvent(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "insert into event(seq_event, eventname, fk_productid, startday, endday, carouselimg) "
					   + "values(event_seq.nextval, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("eventname"));
			pstmt.setString(2, paraMap.get("fk_productid"));
			pstmt.setString(3, paraMap.get("startday"));
			pstmt.setString(4, paraMap.get("endday"));
			pstmt.setString(5, paraMap.get("carouselimg"));
			
			result = pstmt.executeUpdate();		
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}

	
	// 관리자 아이디 중복검사 메소드
	@Override
	public boolean idDuplicateCheck(String managerid) throws SQLException {
		boolean isExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select managerid\n"+
						 "from manager\n"+
						 "where managerid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, managerid);
			
			rs = pstmt.executeQuery();
			
			isExist = rs.next();  // 행이 있으면 중복된 userid ==> true
								  // 행이 없으면 사용가능한 userid ==> false
			
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return isExist;
	}// end of public boolean idDuplicateCheck(String managerid) throws SQLException {}

	
	// 수정하고자 하는 관리자 정보를 불러오는 메소드
	@Override
	public ManagerVO idExistCheck(String updateid) throws SQLException {
		ManagerVO mvo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select managerid, managerpwd, managertype, manageremail, managermobile, managerno\n"+
						 "from manager\n"+
						 "where managerid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updateid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mvo = new ManagerVO();
				mvo.setManagerid(rs.getString(1));
				mvo.setManagerpwd(rs.getString(2));
				mvo.setManagertype(rs.getString(3));
				mvo.setManageremail(aes.decrypt(rs.getString(4)));
				mvo.setManagermobile(aes.decrypt(rs.getString(5)));
				mvo.setManagerno(rs.getInt(6));
			}// end of if(rs.next()){}-----------------------
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return mvo;
	}
	
	
	// 관리자 계정수정 메소드(manager 테이블에 insert)
	@Override
	public int updateManager(ManagerVO mgvo) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "update manager set managerid = ?, managerpwd = ?, managertype = ?, manageremail = ?, managermobile = ? "
					   + "where managerid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mgvo.getManagerid());
			pstmt.setString(2, Sha256.encrypt(mgvo.getManagerpwd()));  // 암호를 SHA256 알고리즘으로 단방향 암호화 한다.
			pstmt.setString(3, mgvo.getManagertype());
			pstmt.setString(4, aes.encrypt(mgvo.getManageremail()));  // 이메일을 AES256 알고리즘으로 양방향 암호화 한다.
			pstmt.setString(5, aes.encrypt(mgvo.getManagermobile()));  // 전화번호를 AES256 알고리즘으로 양방향 암호화 한다.
			pstmt.setString(6, mgvo.getManagerid());
			
			result = pstmt.executeUpdate();		
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}// end of public int updateManager(ManagerVO mgvo) throws SQLException {}--------------------

	
	// 제품 아이디 중복검사 메소드
	@Override
	public boolean prodIdDuplicateCheck(String productid) throws SQLException {
		boolean isExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productid\n"+
						 "from product\n"+
						 "where upper(productid) = upper(?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productid);
			
			rs = pstmt.executeQuery();
			
			isExist = rs.next();  // 행이 있으면 중복된 userid ==> true
								  // 행이 없으면 사용가능한 userid ==> false
			
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return isExist;
	}// end of public boolean prodIdDuplicateCheck(String productid) throws SQLException {}

	
	// 제품 일련번호 중복검사 메소드
	@Override
	public boolean serialIdDuplicateCheck(String productserialid) throws SQLException {
		boolean isExist = false;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productserialid\n"+
						 "from productoption\n"+
						 "where upper(productserialid) = upper(?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productserialid);
			
			rs = pstmt.executeQuery();
			
			isExist = rs.next();  // 행이 있으면 중복된 userid ==> true
								  // 행이 없으면 사용가능한 userid ==> false
			
		} finally {
			close();
		}// end of try~finally{}----------------------
		
		return isExist;
	}// end of public boolean serialIdDuplicateCheck(String productserialid) throws SQLException {}

	
	// 제품 등록 메소드
	@Override
	public int registerProduct(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String carouselimg = paraMap.get("carouselimg");
			String detailimg = paraMap.get("detailimg");
			
			String sql = "";
			
			if(carouselimg == null) {
				
				if(detailimg == null) {
					sql = "insert into product(productid, productname, fk_category, price, character, imgfilename) "
							   + "values(?, ?, ?, ?, ?, ?)";
				} else {
					sql = "insert into product(productid, productname, fk_category, price, character, imgfilename, detailimg) "
							   + "values(?, ?, ?, ?, ?, ?, ?)";
				}
				
			} else if(detailimg == null) {
				sql = "insert into product(productid, productname, fk_category, price, character, imgfilename, carouselimg) "
						   + "values(?, ?, ?, ?, ?, ?, ?)";
			} else {
				sql = "insert into product(productid, productname, fk_category, price, character, imgfilename, carouselimg, detailimg) "
						   + "values(?, ?, ?, ?, ?, ?, ?, ?)";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("productid"));
			pstmt.setString(2, paraMap.get("productname"));
			pstmt.setString(3, paraMap.get("category"));
			pstmt.setInt(4, Integer.parseInt(paraMap.get("price")));
			pstmt.setString(5, paraMap.get("character"));
			pstmt.setString(6, paraMap.get("imgfilename"));
			
			if(carouselimg == null) {
				
				if(detailimg != null) {
					pstmt.setString(7, paraMap.get("detailimg"));
				}
				
			} else if(detailimg == null) {
				pstmt.setString(7, paraMap.get("carouselimg"));
			} else {
				pstmt.setString(7, paraMap.get("carouselimg"));
				pstmt.setString(8, paraMap.get("detailimg"));
			}
			
			result = pstmt.executeUpdate();		
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}

	
	// 제품 옵션 등록 시 제품 아이디를 찾는 메소드
	@Override
	public List<ProductVO> prodIdFind(String productid) throws SQLException {
		List<ProductVO> pvoList = new ArrayList<ProductVO>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "select productid, productname\n"+
						 "from product\n"+
						 "where upper(productid) like upper('%'||?||'%')";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO pvo = new ProductVO();
				pvo.setProductid(rs.getString(1));
				pvo.setProductname(rs.getString(2));
				pvoList.add(pvo);
			}// end of if(rs.next()){}-----------------------
		} finally {
			close();
		}
		
		return pvoList;
	}// end of public ProductVO prodIdFind(String productid) throws SQLException {}

	
	// 제품 옵션 등록 메소드
	@Override
	public int registerOption(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "insert into productoption(productserialid, fk_productid, color, stock) "
					   + "values(?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("productserialid"));
			pstmt.setString(2, paraMap.get("productid"));
			pstmt.setString(3, paraMap.get("color"));
			pstmt.setInt(4, Integer.parseInt(paraMap.get("stock")));
			
			result = pstmt.executeUpdate();		
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}

	
	// 회원 현황 요약 메소드
	@Override
	public int summaryMember() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*)\n" +
						"from member";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}// end of public String summaryMember() throws SQLException {}----------------------

	
	// 제품 현황 요약 메소드
	@Override
	public int summaryProduct() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select sum(stock)\n" +
						"from productoption";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}// end of public int summaryProduct() throws SQLException {}-----------------------

	
	// 주문 현황 요약 메소드
	@Override
	public int summaryPurchase() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*)\n" +
						"from purchasedetail";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}// end of public int summaryPurchase() throws SQLException {}------------------------

	
	// 쿠폰 현황 요약 메소드
	@Override
	public int summaryCoupon() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*)\n" +
						"from eachcoupon";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}// end of public int summaryCoupon() throws SQLException {}-----------------------

	
	// 신규 회원 그래프 데이터 가져오기
	@Override
	public List<Map<String, Integer>> newMemberChart() throws SQLException {
		List<Map<String, Integer>> chartList = new ArrayList<Map<String,Integer>>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select W.dt AS registerday, nvl(sum(cnt), 0) AS cnt\n"+
					"from\n"+
					"(\n"+
					"select to_char(registerday, 'yyyymmdd') AS registerday, count(*) AS cnt\n"+
					"from member\n"+
					"where to_char(registerday, 'yyyymmdd') between (to_char(sysdate-6, 'yyyymmdd')) and (to_char(sysdate, 'yyyymmdd'))\n"+
					"group by to_char(registerday, 'yyyymmdd')\n"+
					"order by registerday desc\n"+
					") V,\n"+
					"(\n"+
					"select to_char(to_date(to_char(sysdate-6, 'yyyymmdd'), 'yyyymmdd') + level - 1, 'yyyymmdd') AS dt\n"+
					"from dual\n"+
					"connect by level <= (to_date(to_char(sysdate, 'yyyymmdd'), 'yyyymmdd') - to_date(to_char(sysdate-6, 'yyyymmdd'), 'yyyymmdd') + 1)\n"+
					") W\n"+
					"where W.dt = V.registerday(+)\n"+
					"group by W.dt\n"+
					"order by W.dt desc";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Integer> chartMap = new HashMap<String, Integer>();
				chartMap.put("registerday", Integer.parseInt(rs.getString(1).substring(6)));
				chartMap.put("cnt", rs.getInt(2));
				chartList.add(chartMap);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return chartList;
	}// end of public List<Map<String, String>> newMemberChart() throws SQLException {}------------

	
	// 신규 회원 막대바 가져오기
	@Override
	public int newMemberBar() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*)\n"+
					"from member\n"+
					"where to_char(registerday, 'yyyymmdd') = to_char(sysdate, 'yyyymmdd')\n";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}// end of public int newMemberBar() throws SQLException {}------------------------

	
	// 신규 이벤트 참여 막대바 가져오기
	@Override
	public int joinEventBar() throws SQLException {
		int cnt = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*) AS cnt\n"+
					"from joinevent J\n"+
					"join\n"+
					"(\n"+
					"select seq_event\n"+
					"from\n"+
					"(\n"+
					"select row_number() over(order by seq_event desc) AS rno, seq_event\n"+
					"from event\n"+
					"where to_char(endday, 'yyyymmdd') >= to_char(sysdate, 'yyyymmdd')\n"+
					") V\n"+
					"where V.rno = 1\n"+
					") W\n"+
					"on J.fk_event = W.seq_event\n"+
					"group by fk_event";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return cnt;
	}

	
	// 재고 보충을 위해 품절 상품 알아오기
	@Override
	public List<Map<String, String>> displayStock() throws SQLException {
		List<Map<String, String>> stockList = new ArrayList<Map<String,String>>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select productserialid, stock\n"+
					"from productoption\n"+
					"where stock <= 10\n"+
					"order by stock";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, String> stockMap = new HashMap<String, String>();
				stockMap.put("productserialid", rs.getString(1));
				stockMap.put("stock", rs.getString(2));
				stockList.add(stockMap);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return stockList;
	}

	
	// 인기 판매 상품 순위 알아오기
	@Override
	public List<Map<String, String>> displayBest() throws SQLException {
		List<Map<String, String>> bestList = new ArrayList<Map<String,String>>();
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select fk_productserialid, sum\n"+
					"from\n"+
					"(\n"+
					"select row_number() over(order by sum desc) AS rno, fk_productserialid, sum\n"+
					"from\n"+
					"(\n"+
					"select fk_productserialid, sum(volume) AS sum\n"+
					"from\n"+
					"(\n"+
					"select fk_productserialid, volume\n"+
					"from purchasedetail\n"+
					") V\n"+
					"group by fk_productserialid\n"+
					") W\n"+
					") X\n"+
					"where X.rno between 1 and 10";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, String> bestMap = new HashMap<String, String>();
				bestMap.put("fk_productserialid", rs.getString(1));
				bestMap.put("sum", rs.getString(2));
				bestList.add(bestMap);
			}
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return bestList;
	}

	
	// 회원 탈퇴 처리하기
	@Override
	public int memberDropout(String userid) throws SQLException {
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			// DAO(SQL문)
			// 탈퇴 예정일이 지난 경우 탈퇴 처리를 update
			String sql = "update member set status = 2 "
					   + "where userid = ? and dropday <= sysdate";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			result = pstmt.executeUpdate();		
			
		} finally {
			close();
		}// end of try~finally{}-----------------------
		
		return result;
	}
	
}