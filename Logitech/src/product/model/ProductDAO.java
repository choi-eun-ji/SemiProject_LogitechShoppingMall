package product.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;

import member.model.MemberVO;
import member.model.PointVO;
import myshop.model.CouponVO;
import myshop.model.EachCouponVO;
import myshop.model.PurchaseVO;
import product.model.*;
import review.modul.ReviewVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;


public class ProductDAO implements InterProductDAO {

	
	private DataSource ds; 
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	public ProductDAO() {
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
	}
	
	// 자원반납
	
	private void close() {
	      try {
	         if(rs != null)    {rs.close(); rs=null;}
	         if(pstmt != null) {pstmt.close(); pstmt=null;}
	         if(conn != null)  {conn.close(); conn=null;}
	      } catch(SQLException e) {
	         e.printStackTrace();
	      }
	}

	
	//////////////////////////////////////////////////////////////////////////임정섭:시작/////
	
	// 제품 페이지에 보여지는 상품이미지 파일명을 모두 조회(select) 하는 메서드
	
	@Override
	public List<ProductVO> selectAll(String fk_category) throws SQLException {
		
		List<ProductVO> productList = new ArrayList<>();
			
		try {
		
		conn = ds.getConnection();
		
		String sql = "select productid, productname, fk_category, character, price, imgfilename \n"+
				"from product \n"+
				"where fk_category = ?";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, fk_category);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			
			ProductVO pvo = new ProductVO();
			
			pvo.setProductid(rs.getString(1));
			pvo.setProductname(rs.getString(2));
			pvo.setFk_category(rs.getString(3));
			pvo.setCharacter(rs.getString(4));
			pvo.setPrice(rs.getInt(5));		
			pvo.setImgfilename(rs.getString(6));
			
			
			
						
			productList.add(pvo);
			
		}// end of while -----------------------------
		}catch (SQLException e) {
		}finally {
			close();
		}
		
		return productList; 
	}

	
	
	
	
	
	///////////////////////////////////////////////////////////////////////
	
	//고객이 선택한 상품 1개를 조회하는 (select) 메서드
	@Override
	public List<ProductVO> selectOne(String productid) throws SQLException {
		
		List<ProductVO> productList = new ArrayList<>();
		
		try {
		
		conn = ds.getConnection();
		
		String sql = "select productid, productname, fk_category, character, price, imgfilename, carouselimg, detailimg, color, productserialid\n"+
				"from product A\n"+
				"JOIN productoption B\n"+
				"on A.productid = B.fk_productid\n"+
				"where A.productid = ? "; 
		
		
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, productid);
		//pstmt.setString(2, color);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			
			ProductVO pvo = new ProductVO();
			
			pvo.setProductid(rs.getString(1));
			pvo.setProductname(rs.getString(2));
			pvo.setFk_category(rs.getString(3));
			pvo.setCharacter(rs.getString(4));
			pvo.setPrice(rs.getInt(5));		
			pvo.setImgfilename(rs.getString(6));
			pvo.setCarouselimg(rs.getString(7));
			pvo.setDetailimg(rs.getString(8));
			
			
			ProductOptionVO povo = new ProductOptionVO();
			povo.setColor(rs.getString(9));
			
			povo.setProductserialid(rs.getString(10));
			
			pvo.setPovo(povo);
			
			productList.add(pvo);
			
		}// end of while -----------------------------
		
		}catch (SQLException e) {
		    
		
		
		}finally {
			close();
		}
		
		return productList; 
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//컬러선택
	
	@Override
	public List<ProductVO> selectOneColor(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> colorList = new ArrayList<>();
		
		try {
		
		conn = ds.getConnection();
		
		String sql = "select productid, productname, fk_category, character, price, imgfilename, color, productserialid\n"+
				"from product A\n"+
				"JOIN productoption B\n"+
				"on A.productid = B.fk_productid\n"+
				"where A.productid = ? and B.color = ? "; 
		
		
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, paraMap.get("productid"));
		pstmt.setString(2, paraMap.get("color"));
		//pstmt.setString(2, color);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			
			ProductVO pvo = new ProductVO();
			
			pvo.setProductid(rs.getString(1));
			pvo.setProductname(rs.getString(2));
			pvo.setFk_category(rs.getString(3));
			pvo.setCharacter(rs.getString(4));
			pvo.setPrice(rs.getInt(5));		
			pvo.setImgfilename(rs.getString(6));
			
			ProductOptionVO povo = new ProductOptionVO();
			
			povo.setColor(rs.getString(7));
			povo.setProductserialid(rs.getString(8));
			
			pvo.setPovo(povo);
			colorList.add(pvo);
			
		}// end of while -----------------------------
		
		}catch (SQLException e) {
		
		}finally {
			close();
		}
		
		return colorList;
	}


	
	
	
	
	
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//장바구니로 보내기
	@Override
	public int intoCart(PurchaseProductVO ppvo) throws SQLException {
	
		int n = 0;
		
		try {
		
		conn = ds.getConnection();
		
		String sql = "insert into cart(seq_cart, fk_productid, price, selectcolor, fk_memberno, cartpronum) "
			     	+ " values(cart_seq.nextval, ?, ?, ?, ?, ?) "; 
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, ppvo.getFk_productid());
		pstmt.setInt(2, ppvo.getPrice());
		pstmt.setString(3, ppvo.getSelectcolor());
		pstmt.setInt(4, 9);
		pstmt.setString(5, ppvo.getCartpronum());
		
		n = pstmt.executeUpdate();
		
		}catch (SQLException e) {
			e.printStackTrace();
			
		}finally {
			close();
		}
		
		return n;
		
	}

	//////////////////////////////////////////////////////////////////////////임정섭:끝/////
		
	
		
	//////////////////////////////////////////////////////////////////////////박수빈:시작/////
	
	// 키워드로 물품을 검색해서 List에 받아오는 메서드(제품 검색)
	@Override
	public List<ProductVO> searchProductKeyword(String keyword, String type) throws SQLException {
	
	List<ProductVO> pList = new ArrayList<ProductVO>();
	ProductVO pvo = null;
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename \n"+
	"from product\n";
	
	if ("rank".equals(type)) {
	sql += "where lower(character) like '%' || lower(?) || '%' ";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	
	}else if ("search".equals(type)) {
	sql += "where lower(productid) like '%' || lower(?) || '%' or lower(productname) like '%' || lower(?) || '%' or lower(character) like '%' || lower(?) || '%' ";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	pstmt.setString(2, keyword);
	pstmt.setString(3, keyword);
	}
	
	rs = pstmt.executeQuery();
	
	
	while(rs.next()) {
	pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));
	pvo.setImgfilename(rs.getString(6));
	
	pList.add(pvo);
	}
	
	} finally {
	close();
	}
	
	return pList;
	}
	
	// 사용자가 select를 선택했을 때 검색해주는 메서드 (제품 검색)
	@Override
	public List<ProductVO> searchProductSelect(String keyword, String select, String type) throws SQLException {
	
	List<ProductVO> pList = new ArrayList<ProductVO>();
	ProductVO pvo = null;
	
	switch (select) {
	case "highPrice":
	select = " order by price desc ";
	break;
	
	case "lowPrice":
	select = " order by price ";
	break;
	}
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename \n"+
	"from product\n";
	
	if ("rank".equals(type)) {
	sql += "where lower(character) like '%' || lower(?) || '%' ";
	sql += select;
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	
	}else if ("search".equals(type)) {
	sql += "where lower(productid) like '%' || lower(?) || '%' or lower(productname) like '%' || lower(?) || '%' or lower(character) like '%' || lower(?) || '%' ";
	sql += select;
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	pstmt.setString(2, keyword);
	pstmt.setString(3, keyword);
	}
	
	rs = pstmt.executeQuery();
	
	
	while(rs.next()) {
	pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));
	pvo.setImgfilename(rs.getString(6));
	
	pList.add(pvo);
	}
	
	} finally {
	close();
	}
	
	return pList;
	}
	
	// 검색한 키워드 DB에 cnt + 1 해주는 메서드(제품 검색)
	@Override
	public int updateRankingKeyword(String keyword) throws SQLException {
	
	int result = 0;
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "update keywordsearch set searchcnt= searchcnt + 1\n"+
	"where keyword= ? ";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	
	result = pstmt.executeUpdate();
	
	} finally {
	close();
	}
	
	return result;
	}
	
	// 메인페이지의 들어갈 이벤트 정보를 받아오는 메서드 (이벤트 캐러셀)
	@Override
	public List<EventVO> selectCarousel() throws SQLException {
	
	List<EventVO> eventList = new ArrayList<EventVO>();
	
	
	try {
	
	conn = ds.getConnection();
	
	// 이벤트기간이 지나지 않은 진행중인 이벤트만 받아오는 sql문
	String sql = "select seq_event, eventname, fk_productid, to_char(stARTDAY, 'YYYY.MM.DD') as startday, to_char(ENDDAY, 'YYYY.MM.DD') as endday, carouselimg\n"+
	"from event \n"+
	"WHERE trunc(sysdate) BETWEEN TO_DATE(STARTDAY, 'YY/MM/DD') AND\n"+
	"                                TO_DATE(ENDDAY, 'YY/MM/DD')";
	
	pstmt = conn.prepareStatement(sql);
	
	rs = pstmt.executeQuery();
	
	while(rs.next()){
	
	EventVO evo = new EventVO();
	
	evo.setSeq_event(rs.getInt(1));
	evo.setEventname(rs.getString(2));
	evo.setFk_productid(rs.getString(3));
	evo.setStartday(rs.getString(4));
	evo.setEndday(rs.getString(5));
	evo.setCarouselimg(rs.getString(6));
	
	eventList.add(evo);
	}// end of while -----------------------------
	
	}finally {
	close();
	}
	
	return eventList;
	}
	
	// 이벤트 번호를 통해 이벤트 정보를 받아오는 메서드 (이벤트 참여)
	@Override
	public EventVO selectOneEvent(String seq_event) throws SQLException {
	
	EventVO evo = null;
	
	try {
	
	conn = ds.getConnection();
	
	// 이벤트기간이 지나지 않은 진행중인 이벤트만 받아오는 sql문
	String sql = "select seq_event, eventname, fk_productid, startday, endday, carouselimg \n"+
	"from event\n"+
	"where SEQ_EVENT = ? ";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, seq_event);
	
	rs = pstmt.executeQuery();
	
	if(rs.next()){
	
	evo = new EventVO();
	
	evo.setSeq_event(rs.getInt(1));
	evo.setEventname(rs.getString(2));
	evo.setFk_productid(rs.getString(3));
	evo.setStartday(rs.getString(4));
	evo.setEndday(rs.getString(5));
	evo.setCarouselimg(rs.getString(6));
	
	}// end of while -----------------------------
	
	}finally {
	close();
	}
	
	return evo;
	}
	
	// 판매순으로 정렬한 물품 데이터 DB에서 불러오기
	@Override
	public List<ProductVO> selectBestCategoryOrder(String category) throws SQLException {
	
	List<ProductVO> pvoList = new ArrayList<>();
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename, nvl(volume, 0) as volume\n"+
	"from\n"+
	"(\n"+
	"    select fk_productid,  sum(volume) as volume\n"+
	"    from PURCHASEdetail\n"+
	"    group by fk_productid\n"+
	") C\n"+
	"right join product P\n"+
	"on C.fk_productid = P.productid\n"+
	" where fk_category = ? "+
	"order by volume desc";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, category);
	
	rs = pstmt.executeQuery();
	
	int cnt = 0;
	while(rs.next()){
	
	// 인기순 최대 3위까지 받아야 하기때문에 3개만 list에 넣어줌 
	if (cnt == 3) {
	break;
	}
	
	ProductVO pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));		
	pvo.setImgfilename(rs.getString(6));
	pvo.setSale(rs.getInt(7));
	
	pvoList.add(pvo);
	
	cnt++;
	}// end of while -----------------------------
	
	}finally {
	close();
	}
	
	return pvoList;
	}
	
	// 검색한 물품을 판매순으로 정렬해주는 메서드 (제품검색 - select) 
	@Override
	public List<ProductVO> selectBestOrder(String keyword, String type) throws SQLException {
	
	List<ProductVO> pList = new ArrayList<ProductVO>();
	ProductVO pvo = null;
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename, nvl(volume, 0) as volume\n"+
	"from\n"+
	"(\n"+
	"    select fk_productid,  sum(volume) as volume\n"+
	"    from PURCHASEdetail\n"+
	"    group by fk_productid\n"+
	") C\n"+
	"right join product P\n"+
	"on C.fk_productid = P.productid\n";
	
	if ("rank".equals(type)) {
	sql += "where lower(character) like '%' || lower(?) || '%' ";
	sql += "order by volume desc";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	
	}else if ("search".equals(type)) {
	sql += "where lower(productid) like '%' || lower(?) || '%' or lower(productname) like '%' || lower(?) || '%' or lower(character) like '%' || lower(?) || '%' ";
	sql += "order by volume desc";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	pstmt.setString(2, keyword);
	pstmt.setString(3, keyword);
	
	}
	
	rs = pstmt.executeQuery();
	
	
	while(rs.next()) {
	pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));
	pvo.setImgfilename(rs.getString(6));
	
	pList.add(pvo);
	}
	
	} finally {
	close();
	}
	
	return pList;
	}
	
	// 검색한 물품을 인기순(찜)으로 정렬해주는 메서드 (제품검색 - select) 
	@Override
	public List<ProductVO> selectFavOrder(String keyword, String type) throws SQLException {
	
	List<ProductVO> pList = new ArrayList<ProductVO>();
	ProductVO pvo = null;
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename, nvl(status, 0) as status\n"+
	"from\n"+
	"(\n"+
	"    select fk_productid, sum(status) as status\n"+
	"from likeproduct\n"+
	"group by fk_productid\n"+
	") C\n"+
	"right join product P\n"+
	"on C.fk_productid = P.productid\n";
	
	if ("rank".equals(type)) {
	sql += "where lower(character) like '%' || lower(?) || '%' ";
	sql += "order by status desc";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	
	}else if ("search".equals(type)) {
	sql += "where lower(productid) like '%' || lower(?) || '%' or lower(productname) like '%' || lower(?) || '%' or lower(character) like '%' || lower(?) || '%' ";
	sql += "order by status desc";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, keyword);
	pstmt.setString(2, keyword);
	pstmt.setString(3, keyword);
	
	}
	
	rs = pstmt.executeQuery();
	
	
	while(rs.next()) {
	pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));
	pvo.setImgfilename(rs.getString(6));
	
	pList.add(pvo);
	}
	
	} finally {
	close();
	}
	
	return pList;
	
	}
	
	// 로컬스토리지에서 받은 페이지 방문 기록값으로 디비에서 해당 제품들을 받아오는 메서드(최근 본 제품)
	@Override
	public List<ProductVO> selectHistoryItem(String history) throws SQLException {
	
		List<ProductVO> pvoList = new ArrayList<ProductVO>();
		
		// 받은 문자값은 맨 앞에 ,가 붙어있으므로 삭제
		String[] historyArr = history.split(",");
		
		try {
			
			conn = ds.getConnection();
			
			////////////////////////////////////////////////////////////
			String sql = "";
			
			for(int i=0; i<historyArr.length; i++) {
				sql += " select productid, productname, fk_category, character, price, imgfilename from product where productid = '"+historyArr[i]+"' ";       
				
				if( i < historyArr.length-1 ) {
					sql += " UNION ALL ";
				}
			}
			/////////////////////////////////////////////////////////////
			
		/*	
			///////////////////////////////////////////////////////////////////////////////////
			String sql = "select productid, productname, fk_category, character, price, imgfilename \n"+
					"from product\n"+
					"where productid in(";
			
					// sql문 내의 in(?) 의 ? 갯수를 정해주는 for문
					for (int i = 0; i < historyArr.length; i++) {
						if (i == historyArr.length-1) {
							// 문자열의 끝일때 ,를 빼고 넣어준다
							sql += "?";
						}else {
							sql += "?, ";
						}

					}
					
			
			sql += ")\n"+
					"ORDER BY decode(productid, ";
			
			// sql문 내의 order by decode(productid, ?, ? ...) 의 ? 갯수를 정해주는 for문
			for (int i = 1; i <= historyArr.length; i++) {
				if (i == historyArr.length) {
					// 문자열의 끝일때 ,를 빼고 넣어준다
					sql += "?, " + i;
				}else {
					sql += "?, " + i + ", ";	
				}
				
			}
			sql += ")";

			pstmt = conn.prepareStatement(sql);
			
			// sql문 내의 in(?) 의 ? 에 데이터를 매칭해주는 for문
			for (int i = 1; i <= historyArr.length; i++) {
				pstmt.setString(i, historyArr[i-1]);
			}
			
			// sql문 내의 order by decode(productid, ?, ? ...) 의 ? 에 데이터를 매칭해주는 for문
			int limit = historyArr.length+1;
			for (int i = historyArr.length+1; i <= limit + historyArr.length-1; i++) {
				pstmt.setString(i, historyArr[i-limit]);
				
			}
            ////////////////////////////////////////////////////////////////////////////////
		*/	

			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				if (cnt == 20) {
					break;
				}
				
				ProductVO pvo = new ProductVO();
				
				pvo.setProductid(rs.getString(1));
				pvo.setProductname(rs.getString(2));
				pvo.setFk_category(rs.getString(3));
				pvo.setCharacter(rs.getString(4));
				pvo.setPrice(rs.getInt(5));
				pvo.setImgfilename(rs.getString(6));
				
				pvoList.add(pvo);
				
				cnt++;
			}
			
		} finally {
			close();
		}
		
		return pvoList;
		
	}
	
	// 사용자로부터 받은 키워드로 DB의 제품-특성에서 검색
	@Override
	public String selectLikeItem(Map<String, String> paraMap) throws SQLException {
	
	String productid = null;
	
	try {
	
	conn = ds.getConnection();
	
	// 이벤트기간이 지나지 않은 진행중인 이벤트만 받아오는 sql문
	String sql = "select productid\n"+
	"from product\n"+
	"where fk_category = ? and character like '%' || ? || '%' and character like '%' || ? || '%'";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, paraMap.get("ans1"));
	pstmt.setString(2, paraMap.get("ans2"));
	pstmt.setString(3, paraMap.get("ans3"));
	
	rs = pstmt.executeQuery();
	
	if (rs.next()) {
	// 검색한 결과가 있을 시
	productid = rs.getString(1);
	}
	// 검색한 결과가 없을 시 productid는 초기값인 null을 반환
	
	
	
	}finally {
	close();
	}
	
	return productid;
	
	}
	
	// productid를 통해 DB에서 물품을 검색해주는 메서드
	@Override
	public ProductVO selectLikeItemOne(String productid) throws SQLException {
	
	
	ProductVO pvo = new ProductVO();
	
	try {
	
	conn = ds.getConnection();
	
	// 이벤트기간이 지나지 않은 진행중인 이벤트만 받아오는 sql문
	String sql = "select productid, productname, fk_category, character, price, imgfilename\n"+
	"from product\n"+
	"where productid = ?";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, productid);
	
	rs = pstmt.executeQuery();
	
	if (rs.next()) {
	// 검색한 결과가 있을 시
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));
	pvo.setImgfilename(rs.getString(6));
	}
	
	}finally {
	close();
	}
	
	return pvo;
	
	}
	
	// 판매순으로 정렬한 각 3개씩의 물품 데이터 DB에서 불러오는 메서드
	@Override
	public List<ProductVO> selectBest3Items() throws SQLException {
	
	List<ProductVO> pvoList = new ArrayList<>();
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename, nvl(volume, 0) as volume\n"+
	"from\n"+
	"(\n"+
	"    select fk_productid,  sum(volume) as volume\n"+
	"    from PURCHASEdetail\n"+
	"    group by fk_productid\n"+
	") C\n"+
	"right join product P\n"+
	"on C.fk_productid = P.productid\n"+
	"order by volume desc";
	
	pstmt = conn.prepareStatement(sql);
	
	rs = pstmt.executeQuery();
	
	int cnt = 0;
	while(rs.next()){
	
	// 판매순 최대 3위까지 받아야 하기때문에 3개만 list에 넣어줌 
	if (cnt == 3) {
	break;
	}
	
	ProductVO pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));		
	pvo.setImgfilename(rs.getString(6));
	pvo.setSale(rs.getInt(7));
	
	pvoList.add(pvo);
	
	cnt++;
	}// end of while -----------------------------
	
	}finally {
	close();
	}
	
	return pvoList;
	
	}
	
	// 전체 물품의 판매순 정렬 보여주는 메서드
	@Override
	public List<ProductVO> searchAllBestProductSale() throws SQLException {
	
	List<ProductVO> pvoList = new ArrayList<>();
	
	try {
	
	conn = ds.getConnection();
	
	String sql = "select productid, productname, fk_category, character, price, imgfilename, nvl(volume, 0) as volume\n"+
	"from\n"+
	"(\n"+
	"    select fk_productid,  sum(volume) as volume\n"+
	"    from PURCHASEdetail\n"+
	"    group by fk_productid\n"+
	") C\n"+
	"right join product P\n"+
	"on C.fk_productid = P.productid\n"+
	"order by volume desc";
	
	pstmt = conn.prepareStatement(sql);
	
	rs = pstmt.executeQuery();
	
	while(rs.next()){
	ProductVO pvo = new ProductVO();
	
	pvo.setProductid(rs.getString(1));
	pvo.setProductname(rs.getString(2));
	pvo.setFk_category(rs.getString(3));
	pvo.setCharacter(rs.getString(4));
	pvo.setPrice(rs.getInt(5));		
	pvo.setImgfilename(rs.getString(6));
	pvo.setSale(rs.getInt(7));
	
	pvoList.add(pvo);
	
	}// end of while -----------------------------
	
	}finally {
	close();
	}
	
	return pvoList;
	
	}
	
	
	//////////////////////////////////////////////////////////////////////////박수빈:끝/////
	
	
	//////////////////////////////////////////////////////////////////////////최은지:끝/////
		
	@Override
	public ArrayList<ProductVO> selectProduct(String userid) throws SQLException {
		ProductVO product = null;
		ProductOptionVO productoption = null;

		ArrayList<ProductVO> likelist = new ArrayList<>();
		try {

			conn = ds.getConnection();
			String sql = "select productid, productname, price, imgfilename, seq_like\n" + "from PRODUCT P\n"
					+ "inner join LIKEPRODUCT L\n" + "on P.productid = L.fk_productid\n" + "where fk_memberno=?\n"
					+ " order by addtime desc ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				product = new ProductVO();

				product.setProductid(rs.getString(1));
				product.setProductname(rs.getString(2));
				product.setPrice(rs.getInt(3));
				product.setImgfilename(rs.getString(4));
				LikeProductVO lpvo = new LikeProductVO();
				lpvo.setSeq_like(rs.getInt(5));
				product.setLpvo(lpvo);
				likelist.add(product);
			}
		} finally {
			close();
		}

		return likelist;
	}

	@Override
	public ArrayList<LikeProductVO> selectOptionProduct(String userid) throws SQLException {

		ArrayList<LikeProductVO> likeonelist = new ArrayList<>();
		LikeProductVO lpvo = null;

		try {
			conn = ds.getConnection();

			String sql = "select fk_memberno, fk_productid, status\n" + "from likeproduct\n" + "where fk_memberno = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				lpvo = new LikeProductVO();
				lpvo.setFk_memberno(rs.getInt(1));
				lpvo.setFk_productid(rs.getString(2));
				lpvo.setStatus(rs.getInt(3));

				likeonelist.add(lpvo);
			}
		} finally {
			close();
		}

		return likeonelist;
	}

	@Override
	public ArrayList<String> selectColorProduct(String fk_productid) throws SQLException {

		ArrayList<String> templist = new ArrayList<>();
		String str = "";
		try {
			conn = ds.getConnection();

			String sql = "select color\n" + "from PRODUCT p\n" + "inner join PRODUCTOPTION O \n"
					+ "on p.productid = O.fk_productid\n" + "where productid= ? ";
			// "inner join LIKEPRODUCT L\n"+
			// "on P.productid = L.fk_productid\n"+

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_productid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				templist.add(rs.getString(1));
			}
		} finally {
			close();
		}
		return templist;
	}

	// 찜목록에 있는 상품 장바구니에 넣어주는 메소드
	@Override
	public int insertCart(PurchaseProductVO purchaseProductVO) {
		int result = 0;
		try {

			conn = ds.getConnection();
			String sql = "insert into CART(SEQ_CART, PRICE, SELECTCOLOR, FK_MEMBERNO, FK_PRODUCTID) values(CART_SEQ.nextval, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, purchaseProductVO.getPrice());
			pstmt.setString(2, purchaseProductVO.getSelectcolor());
			pstmt.setString(3, "9");
			pstmt.setString(4, purchaseProductVO.getFk_productid());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {

		} finally {
			close();
		}
		return result;
	}

	@Override
	public String selectImgFile(String fk_productid) throws SQLException {
		String img = "";
		try {
			conn = ds.getConnection();

			String sql = "select distinct imgfilename\n" + "from productoption O\n" + "inner join product P\n"
					+ "on P.productid = O.fk_productid\n" + "where P.productid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_productid);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				img = rs.getString(1);
			}

		} finally {
			close();
		}
		return img;
	}

	@Override
	public ArrayList<ProductVO> selectCartProduct(String usernum) throws SQLException {
		ArrayList<ProductVO> cartprolist = new ArrayList<>();
		ProductVO pvo = null;

		try {
			conn = ds.getConnection();
			String sql = "select imgfilename, C.fk_productid, productname, selectcolor, C.price, seq_cart, productserialid \n"
					+ "from cart C\n" + "inner join product P\n" + "on c.fk_productid = P.productid\n"
					+ "inner join productoption O\n"
					+ "on C.fk_productid = O.fk_productid and c.selectcolor = o.color\n" + "where fk_memberno = ? \n"
					+ "order by C.fk_productid";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				pvo = new ProductVO();
				pvo.setImgfilename(rs.getString(1));
				// pvo.setProductid(rs.getString(2));
				PurchaseProductVO pcvo = new PurchaseProductVO();
				pcvo.setFk_productid(rs.getString(2));

				pvo.setProductname(rs.getString(3));

				pcvo.setSelectcolor(rs.getString(4));
				pcvo.setPrice(rs.getInt(5));
				pcvo.setSq_cart(rs.getInt(6));
				pvo.setPcvo(pcvo);

				ProductOptionVO povo = new ProductOptionVO();
				povo.setProductserialid(rs.getString(7));

				pvo.setPovo(povo);
				cartprolist.add(pvo);
			}

		} catch (SQLException e) {

		} finally {
			close();
		}

		return cartprolist;
	}

	@Override
	public ArrayList<String> selectcolor(String productid) throws SQLException {

		ArrayList<String> templist = new ArrayList<>();
		String str = "";
		try {
			conn = ds.getConnection();

			String sql = "select distinct color\n" + "from productoption P\n" + "inner join cart C\n"
					+ "on p.fk_productid = C.fk_productid \n" + "where C.fk_productid = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				templist.add(rs.getString(1));
			}
		} finally {
			close();
		}
		return templist;
	}

	@Override
	public void updateOption(String hiddencolor, String hiddennum) throws SQLException {

		try {

			conn = ds.getConnection();
			String sql = "update cart set selectcolor = ? where seq_cart = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, hiddencolor);
			pstmt.setString(2, hiddennum);

			pstmt.executeUpdate();
		} finally {
			close();
		}
	}

	@Override
	public String selectChangeColor(String hiddenseq) throws SQLException {

		String str = "";
		try {
			conn = ds.getConnection();

			String sql = "select selectcolor\n" + "from cart\n" + "where seq_cart = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hiddenseq);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				str = rs.getString(1);
			}
		} finally {
			close();
		}
		return str;
	}

	@Override
	public void deleteCart(String seqcart) throws SQLException {

		try {
			conn = ds.getConnection();
			String sql = "delete from cart where seq_cart= ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, seqcart);

			pstmt.executeUpdate();
		} catch (SQLException e) {

		} finally {
			close();
		}
	}

	@Override
	public void deleteLikeCart(String string) throws SQLException {

		try {
			conn = ds.getConnection();
			String sql = "delete from likeproduct where seq_like= ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, string);

			pstmt.executeUpdate();
		} catch (SQLException e) {

		} finally {
			close();
		}

	}

	@Override
	public ArrayList<PurchaseVO> selectPoint(int usernum) throws SQLException {
		ArrayList<PurchaseVO> templist = new ArrayList<>();
		try {
			conn = ds.getConnection();

			String sql = "select pointnum, reason, usedpointnum, ordernum, fk_purchaseno, purchaseday\n"
					+ "FROM PURCHASE P\n" + "INNER JOIN TBL_POINT T\n" + "ON P.purchaseno = T.fk_purchaseno\n"
					+ "where fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				PointVO pointvo = new PointVO();
				pointvo.setPointnum(rs.getInt(1));
				pointvo.setReason(rs.getString(2));
				pointvo.setUsedpointnum(rs.getInt(3));
				PurchaseVO pcvo = new PurchaseVO();
				pcvo.setOrdernum(rs.getString(4));
				pointvo.setFk_purchaseno(rs.getInt(5));
				pcvo.setPurchaseday(rs.getString(6));

				pcvo.setPointvo(pointvo);
				templist.add(pcvo);
			}
		} finally {
			close();
		}
		return templist;
	}

	@Override
	public String selectUsedPoint(int usernum) throws SQLException {
		String str = "";
		try {
			conn = ds.getConnection();

			String sql = "select point\n" + "from member\n" + "where memberno= ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				str = rs.getString(1);
			}
		} finally {
			close();
		}
		return str;
	}

	@Override
	public int getTotalPage(int usernum) throws SQLException {

		int totalPage = 0;
		try {
			conn = ds.getConnection();

			String sql = "select ceil(count(*)/ 10)\n" + "FROM PURCHASE P\n" + "INNER JOIN TBL_POINT T\n"
					+ "ON P.purchaseno = T.fk_purchaseno\n" + "where fk_memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);

			rs = pstmt.executeQuery();
			rs.next();

			totalPage = rs.getInt(1);

		} finally {
			close();
		}

		return totalPage;
	}

	@Override
	public ArrayList<PurchaseVO> selectPointList(Map<String, String> paraMap) throws SQLException {

		ArrayList<PurchaseVO> ptlist = new ArrayList<>();

		try {

			conn = ds.getConnection();

			String sql = "select pointnum, reason, ordernum, purchaseday\n" + "FROM \n" + "(\n"
					+ "    select rownum as RNO, pointnum, reason, ordernum, purchaseday\n"
					+ "    from PURCHASE P\n" + "    INNER JOIN TBL_POINT T\n"
					+ "    ON P.purchaseno = T.fk_purchaseno\n" + "    where fk_memberno = ? \n" + ") V\n"
					+ "where RNO between ? and ?";
			pstmt = conn.prepareStatement(sql);

			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = 10;

			pstmt.setString(1, paraMap.get("usernum"));
			pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1));
			pstmt.setInt(3, (currentShowPageNo * sizePerPage));

			rs = pstmt.executeQuery();

			while (rs.next()) {
				PointVO pointvo = new PointVO();
				pointvo.setPointnum(rs.getInt(1));
				pointvo.setReason(rs.getString(2));
				PurchaseVO pcvo = new PurchaseVO();
				pcvo.setOrdernum(rs.getString(3));
				pcvo.setPurchaseday(rs.getString(4));

				pcvo.setPointvo(pointvo);
				ptlist.add(pcvo);
			}

		} finally {
			close();
		}
		return ptlist;
	}

	@Override
	public String selectSumPoint(int usernum) throws SQLException {

		String totalpt = "";
		try {
			conn = ds.getConnection();

			String sql = "select sum(pointnum)\n" + "from PURCHASE P\n" + "INNER JOIN TBL_POINT T\n"
					+ "ON P.purchaseno = T.fk_purchaseno\n" + "where fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);

			rs = pstmt.executeQuery();
			rs.next();

			totalpt = rs.getString(1);

		} finally {
			close();
		}

		return totalpt;
	}
	/////////////////////////////////////////////////////////////////////////// 여기까지
	/////////////////////////////////////////////////////////////////////////// 통합
	/////////////////////////////////////////////////////////////////////////// 완료

	@Override
	public ArrayList<HashMap<String, String>> selectOrderList(String usernum) throws SQLException {

		ArrayList<HashMap<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();
			String sql = "SELECT purchaseday, ordernum, imgfilename, V8.fk_productid, V9.fk_productserialid, productname, color, V8.volume, price, V9.deliverystatus, totalprice\n"
					+ "    from \n" + "    (\n"
					+ "    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"
					+ "    from (\n"
					+ "    select productserialid, fk_productid, color, imgfilename, productname, price\n"
					+ "    from productoption O\n" + "    right outer join product P\n"
					+ "    on o.fk_productid = P.productid\n" + "    )V7\n" + "    inner join purchasedetail D\n"
					+ "    on D.fk_productserialid = V7.productserialid\n" + "    ) V8\n" + "    right outer join \n"
					+ "    (\n"
					+ "    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno\n"
					+ "    from purchasedetail D\n" + "    inner join purchase P\n"
					+ "    on D.fk_purchaseno = P.purchaseno\n" + "    ) V9\n"
					+ "    on V8.purchasedetailid = V9.purchasedetailid\n" + "    where fk_memberno = ? \n"
					+ "    order by purchaseday desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("purchaseday", rs.getString(1));
				hash.put("ordernum", rs.getString(2));
				hash.put("imgfilename", rs.getString(3));
				hash.put("fk_productid", rs.getString(4));
				hash.put("fk_productserialid", rs.getString(5));
				hash.put("productname", rs.getString(6));
				hash.put("color", rs.getString(7));
				hash.put("volume", rs.getString(8));
				hash.put("price", rs.getString(9));
				hash.put("deliverystatus", rs.getString(10));
				hash.put("totalprice", rs.getString(11));

				hashlist.add(hash);
			}
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public ArrayList<HashMap<String, String>> todaySelectOrderList(String usernum) throws SQLException {
		ArrayList<HashMap<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "SELECT purchaseday, ordernum, imgfilename, V8.fk_productid, V9.fk_productserialid, productname, color, V8.volume, price, V9.deliverystatus, totalprice\n"
					+ "    from \n" + "    (\n"
					+ "    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"
					+ "    from (\n"
					+ "    select productserialid, fk_productid, color, imgfilename, productname, price\n"
					+ "    from productoption O\n" + "    right outer join product P\n"
					+ "    on o.fk_productid = P.productid\n" + "    )V7\n" + "    inner join purchasedetail D\n"
					+ "    on D.fk_productserialid = V7.productserialid\n" + "    ) V8\n" + "    right outer join \n"
					+ "    (\n"
					+ "    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno\n"
					+ "    from purchasedetail D\n" + "    inner join purchase P\n"
					+ "    on D.fk_purchaseno = P.purchaseno\n" + "    ) V9\n"
					+ "    on V8.purchasedetailid = V9.purchasedetailid\n"
					+ "    where fk_memberno = ? and to_char(purchaseday,'yy-mm-dd') = to_char(sysdate, 'yy-mm-dd')";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("purchaseday", rs.getString(1));
				hash.put("ordernum", rs.getString(2));
				hash.put("imgfilename", rs.getString(3));
				hash.put("fk_productid", rs.getString(4));
				hash.put("fk_productserialid", rs.getString(5));
				hash.put("productname", rs.getString(6));
				hash.put("color", rs.getString(7));
				hash.put("volume", rs.getString(8));
				hash.put("price", rs.getString(9));
				hash.put("deliverystatus", rs.getString(10));
				hash.put("totalprice", rs.getString(11));

				hashlist.add(hash);
			}
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public ArrayList<HashMap<String, String>> weekSelectOrderList(String usernum) throws SQLException {
		ArrayList<HashMap<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "SELECT purchaseday, ordernum, imgfilename, V8.fk_productid, V9.fk_productserialid, productname, color, V8.volume, price, V9.deliverystatus, totalprice\n"
					+ "    from \n" + "    (\n"
					+ "    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"
					+ "    from (\n"
					+ "    select productserialid, fk_productid, color, imgfilename, productname, price\n"
					+ "    from productoption O\n" + "    right outer join product P\n"
					+ "    on o.fk_productid = P.productid\n" + "    )V7\n" + "    inner join purchasedetail D\n"
					+ "    on D.fk_productserialid = V7.productserialid\n" + "    ) V8\n" + "    right outer join \n"
					+ "    (\n"
					+ "    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno\n"
					+ "    from purchasedetail D\n" + "    inner join purchase P\n"
					+ "    on D.fk_purchaseno = P.purchaseno\n" + "    ) V9\n"
					+ "    on V8.purchasedetailid = V9.purchasedetailid\n"
					+ "    where fk_memberno = ? and sysdate-8 < purchaseday\n" + "    order by purchaseday desc";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("purchaseday", rs.getString(1));
				hash.put("ordernum", rs.getString(2));
				hash.put("imgfilename", rs.getString(3));
				hash.put("fk_productid", rs.getString(4));
				hash.put("fk_productserialid", rs.getString(5));
				hash.put("productname", rs.getString(6));
				hash.put("color", rs.getString(7));
				hash.put("volume", rs.getString(8));
				hash.put("price", rs.getString(9));
				hash.put("deliverystatus", rs.getString(10));
				hash.put("totalprice", rs.getString(11));

				hashlist.add(hash);
			}
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public ArrayList<HashMap<String, String>> month1SelectOrderList(String usernum, String month) throws SQLException {

		ArrayList<HashMap<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "SELECT purchaseday, ordernum, imgfilename, V8.fk_productid, V9.fk_productserialid, productname, color, V8.volume, price, V9.deliverystatus, totalprice\n"
					+ "    from \n" + "    (\n"
					+ "    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"
					+ "    from (\n"
					+ "    select productserialid, fk_productid, color, imgfilename, productname, price\n"
					+ "    from productoption O\n" + "    right outer join product P\n"
					+ "    on o.fk_productid = P.productid\n" + "    )V7\n" + "    inner join purchasedetail D\n"
					+ "    on D.fk_productserialid = V7.productserialid\n" + "    ) V8\n" + "    right outer join \n"
					+ "    (\n"
					+ "    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno\n"
					+ "    from purchasedetail D\n" + "    inner join purchase P\n"
					+ "    on D.fk_purchaseno = P.purchaseno\n" + "    ) V9\n"
					+ "    on V8.purchasedetailid = V9.purchasedetailid\n"
					+ "    where fk_memberno = ? and add_months(sysdate, ?) <= purchaseday\n"
					+ "    order by purchaseday desc";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);
			pstmt.setString(2, month);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("purchaseday", rs.getString(1));
				hash.put("ordernum", rs.getString(2));
				hash.put("imgfilename", rs.getString(3));
				hash.put("fk_productid", rs.getString(4));
				hash.put("fk_productserialid", rs.getString(5));
				hash.put("productname", rs.getString(6));
				hash.put("color", rs.getString(7));
				hash.put("volume", rs.getString(8));
				hash.put("price", rs.getString(9));
				hash.put("deliverystatus", rs.getString(10));
				hash.put("totalprice", rs.getString(11));

				hashlist.add(hash);
			}
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public int sumOrderSelect(String usernum) throws SQLException {
		int size = 0;
		try {
			conn = ds.getConnection();

			String sql = "select count(*)\n" + "    from purchase\n"
					+ "    where fk_memberno = ? and add_months(sysdate, -6) <= purchaseday";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usernum);

			rs = pstmt.executeQuery();
			rs.next();

			size = rs.getInt(1);

		} finally {
			close();
		}

		return size;

	}

	@Override
	public ArrayList<HashMap<String, String>> viewReviewSelect(String memberno) throws SQLException {
		ArrayList<HashMap<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "SELECT purchaseday, ordernum, imgfilename, V8.fk_productid, productname, color, purchaseno\n"
					+ "    from \n" + "    (\n"
					+ "    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"
					+ "    from (\n"
					+ "    select productserialid, fk_productid, color, imgfilename, productname, price\n"
					+ "    from productoption O\n" + "    right outer join product P\n"
					+ "    on o.fk_productid = P.productid\n" + "    )V7\n" + "    inner join purchasedetail D\n"
					+ "    on D.fk_productserialid = V7.productserialid\n" + "    ) V8\n" + "    right outer join \n"
					+ "    (\n"
					+ "    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno, reviewstatus\n"
					+ "    from purchasedetail D\n" + "    inner join purchase P\n"
					+ "    on D.fk_purchaseno = P.purchaseno\n" + "    ) V9\n"
					+ "    on V8.purchasedetailid = V9.purchasedetailid\n"
					+ "    where fk_memberno = ? and deliverystatus = '배송완료' and reviewstatus = '미완료' \n"
					+ "    order by purchaseday desc";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("purchaseday", rs.getString(1));
				hash.put("ordernum", rs.getString(2));
				hash.put("imgfilename", rs.getString(3));
				hash.put("fk_productid", rs.getString(4));
				hash.put("productname", rs.getString(5));
				hash.put("color", rs.getString(6));
				hash.put("purchaseno", rs.getString(7));

				hashlist.add(hash);
			}

		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public int insertReview(ReviewVO rv) {
		int result = 0;
		try {

			conn = ds.getConnection();
			conn.setAutoCommit(false);

			String sql = "    insert into review(seq_review, fk_memberno, fk_productid, content, score, color, fk_purchaseno) values(REVIEW_SEQ.nextval, ?, ?, ?, ?, ?, ?)\n"
					+ "";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, rv.getUserno());
			pstmt.setString(2, rv.getFk_productid());
			pstmt.setString(3, rv.getContent());
			pstmt.setString(4, rv.getScore());
			pstmt.setString(5, rv.getColor());
			pstmt.setString(6, rv.getFk_purchaseno());

			result = pstmt.executeUpdate();

			sql = "update purchasedetail set reviewstatus = '완료' where purchasedetailid in\n" + "    (    \n"
					+ "    select purchasedetailid\n" + "    from purchase P\n" + "    inner join review R\n"
					+ "    on P.purchaseno = R.fk_purchaseno\n" + "    inner join purchasedetail D\n"
					+ "    on P.purchaseno = D.fk_purchaseno\n"
					+ "    where P.purchaseno = ? and color= ? and D.fk_productid = ? \n" + "    )";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rv.getFk_purchaseno());
			pstmt.setString(2, rv.getColor());
			pstmt.setString(3, rv.getFk_productid());

			result = pstmt.executeUpdate();

			sql = "    insert into tbl_point(pointnum, reason, fk_purchaseno) values('150', '리뷰작성에 대한 적립금', ?)\n" + "";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rv.getFk_purchaseno());
			result = pstmt.executeUpdate();

			if (result == 1) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			close();
		}
		return result;
	}

	@Override
	public ArrayList<HashMap<String, String>> selectMyReview(Map<String, String> paraMap) throws SQLException {

		ArrayList<HashMap<String, String>> ptlist = new ArrayList<>();

		try {

			conn = ds.getConnection();

			String sql = "select writeday, imgfilename, fk_productid, productname, color, content, score, seq_review\n"+
					"    FROM\n"+
					"    (\n"+
					"        select rownum as RNO, writeday, imgfilename, V2.fk_productid, productname, V1.color, V1.content, score, V1.seq_review\n"+
					"        from(\n"+
					"        select distinct seq_review, R.fk_purchaseno, reviewstatus, content, writeday, color, score\n"+
					"        from review R\n"+
					"        inner join purchasedetail p\n"+
					"        on R.fk_productid = P.fk_productid\n"+
					"        where reviewstatus = '완료'\n"+
					"        ) V1\n"+
					"        inner join\n"+
					"        (\n"+
					"        select seq_review, productname, imgfilename, R.fk_productid\n"+
					"        from product D\n"+
					"        inner join review R\n"+
					"        on R.fk_productid = D.productid\n"+
					"        \n"+
					"        ) V2\n"+
					"        on V1.seq_review = V2.seq_review\n"+
					"    )\n"+
					"    WHERE RNO BETWEEN ? AND ? ";

			pstmt = conn.prepareStatement(sql);

			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = 10;

			pstmt.setInt(1, (currentShowPageNo * sizePerPage) - (sizePerPage - 1));
			pstmt.setInt(2, (currentShowPageNo * sizePerPage));

			rs = pstmt.executeQuery();

			while (rs.next()) {
				HashMap<String, String> hashmap = new HashMap<>();
				hashmap.put("writeday", rs.getString(1));
				hashmap.put("imgfilename", rs.getString(2));
				hashmap.put("fk_productid", rs.getString(3));
				hashmap.put("productname", rs.getString(4));
				hashmap.put("color", rs.getString(5));
				hashmap.put("content", rs.getString(6));
				hashmap.put("score", rs.getString(7));
				hashmap.put("seq_review", rs.getString(8));

				ptlist.add(hashmap);
			}

		} finally {
			close();
		}
		return ptlist;
	}

	@Override
	public int getTotalReviewPage(String memberno) throws SQLException {

		int n = 0;

		try {

			conn = ds.getConnection();

			String sql = "select ceil(count(*)/ 10)\n" + "    from review\n" + "    where fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();
			rs.next();
			n = rs.getInt(1);

		} finally {
			close();
		}
		return n;
	}

	

	@Override
	public int deleteReview(String seqredel) throws SQLException {

		int n = 0;

		try {
			conn = ds.getConnection();

			String sql = "delete from review where seq_review = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, seqredel);
			n = pstmt.executeUpdate();

		} finally {
			close();
		}
		return n;
	}

	@Override
	public ArrayList<Map<String, String>> selectCouponList(String memberno, String grade) {
		ArrayList<Map<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "select M.memberno, S.membershipname, C.couponname, C.discount, M.name, C.couponcode  \n"
					+ "from membership S\n" + "inner join member M\n" + "on S.membershipname = M.fk_membershipname\n"
					+ "inner join coupon C\n" + "on S.membershipname = C.fk_membershipname\n"
					+ "where M.memberno = ? and C.couponname LIKE '%' || ? || '%' ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);
			pstmt.setString(2, grade);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("membershipname", rs.getString(2));
				hash.put("couponname", rs.getString(3));
				hash.put("discount", rs.getString(4));
				hash.put("name", rs.getString(5));
				hash.put("couponcode", rs.getString(6));

				hashlist.add(hash);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public String selectGrade(String memberno) throws SQLException {

		String grade = "";
		try {
			conn = ds.getConnection();

			String sql = "select S.membershipname\n" + "from member M\n" + "inner join membership S\n"
					+ "on M.fk_membershipname = S.membershipname\n" + "where M.memberno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				grade = rs.getString(1);
			}

		} finally {
			close();
		}
		return grade;
	}

	@Override
	public int insertEachCoupon(String fk_couponcode, String memberno) throws SQLException {
		int result = 0;

		try {
			conn = ds.getConnection();
			String sql = "insert into eachcoupon(eachcouponcode, fk_couponcode, fk_memberno) values( ? ||'_'||to_char(sysdate, 'yymmdd'), ? , ? )\n"
					+ "";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, fk_couponcode);
			pstmt.setString(2, fk_couponcode);
			pstmt.setString(3, memberno);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {

		} finally {
			close();
		}
		return result;
	}

	@Override
	public ArrayList<Map<String, String>> selectEachCoupon(Map<String, String> map) throws SQLException {

		ArrayList<Map<String, String>> hashlist = new ArrayList<>();

		try {
			conn = ds.getConnection();

			String sql = "select couponname, discount, minprice, status\n"+
					"from\n"+
					"(\n"+
					"select rownum as rno, C.couponname, discount, minprice, status\n"+
					"from coupon C\n"+
					"inner join eachcoupon E\n"+
					"ON C.couponcode = E.fk_couponcode\n"+
					"where E.fk_memberno = ? \n"+
					") V\n"+
					"where rno between ? and ? ";

			pstmt = conn.prepareStatement(sql);
			int currentShowPageNo = Integer.parseInt(map.get("currentShowPageNo"));
			int sizePerPage = 10;
			
			pstmt.setString(1, map.get("usernum"));
			pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1));
			pstmt.setInt(3, (currentShowPageNo * sizePerPage));

			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> hash = new HashMap<>();
				hash.put("couponname", rs.getString(1));
				hash.put("discount", rs.getString(2));
				hash.put("minprice", rs.getString(3));
				hash.put("status", rs.getString(4));

				hashlist.add(hash);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return hashlist;
	}

	@Override
	public String selectEachproId(String productid, String color) throws SQLException {

		String grade = "";
		try {
			conn = ds.getConnection();

			String sql = "select productserialid\n" + "from product P\n" + "inner join productoption O\n"
					+ "on P.productid = O.fk_productid\n" + "where O.color = ? and O.fk_productid= ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, color);
			pstmt.setString(2, productid);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				grade = rs.getString(1);
			}

		} finally {
			close();
		}
		return grade;
	}

	////////////////// 정섭오빠 dao///////////////
	// 고객이 선택한 상품 주문페이지로 이동

	@Override
	public List<ProductVO> productPurchase(Map<String, String> paraMap) throws SQLException {

		List<ProductVO> purchaseList = new ArrayList<>();

		try {

			conn = ds.getConnection();

			String sql = "select A.productid, A.productname, A.price, A.imgfilename, B.color, B.productserialid\n"
					+ "				from product A\n" + "				JOIN productoption B\n"
					+ "				on A.productid = B.fk_productid\n"
					+ "				where A.productid = ? and B.color = ? ";

			/*
			 * SELECT a.empno , a.ename , a.deptno , b.dname , b.locno , c.lname
			 * FROM emp a INNER JOIN dept b ON a.deptno = b.deptno INNER JOIN
			 * loc c ON b.locno = c.locno WHERE a.sal >= 2000
			 */

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("productid"));
			pstmt.setString(2, paraMap.get("color"));

			// pstmt.setString(3, paraMap.get("color"));
			// pstmt.setString(4, paraMap.get("productserialid"));

			rs = pstmt.executeQuery();

			if (rs.next()) {

				ProductVO pvo = new ProductVO();

				pvo.setProductid(rs.getString(1));
				pvo.setProductname(rs.getString(2));
				pvo.setPrice(rs.getInt(3));
				pvo.setImgfilename(rs.getString(4));
				// pvo.getPovo().setColor(rs.getString(5));

				ProductOptionVO povo = new ProductOptionVO();

				povo.setColor(rs.getString(5));
				povo.setProductserialid(rs.getString(6));

				pvo.setPovo(povo);

				purchaseList.add(pvo);

			} // end of if -----------------------------

		} catch (SQLException e) {

		} finally {
			close();
		}

		return purchaseList;

	}

	// 구매창에서 쿠폰을 보여주고 선택한다.
	@Override
	public List<EachCouponVO> selectCoupon(Map<String, String> paraMap) throws SQLException {

		List<EachCouponVO> couponList = new ArrayList<>();

		try {

			conn = ds.getConnection();

			String sql = "select couponname, discount, minprice, eachcouponcode, fk_memberno\n" + "from coupon C\n"
					+ "join eachcoupon E\n" + "on C.couponcode = E.fk_couponcode\n"
					+ "where status = 0 and to_char(endday, 'yyyy-mm-dd') >= to_char(sysdate, 'yyyy-mm-dd')\n"
					+ "and fk_memberno = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, paraMap.get("memberno"));

			rs = pstmt.executeQuery();

			while (rs.next()) {

				EachCouponVO ecvo = new EachCouponVO();

				CouponVO cvo = new CouponVO();
				cvo.setCouponname(rs.getString(1));
				cvo.setDiscount(rs.getInt(2));
				cvo.setMinprice(rs.getInt(3));

				ecvo.setEachcouponcode(rs.getString(4));
				ecvo.setFk_memberno(rs.getInt(5));
				ecvo.setCoupvo(cvo);

				couponList.add(ecvo);

			} // end of if -----------------------------

		} catch (SQLException e) {

		} finally {
			close();
		}

		return couponList;
	}
	/////////////////////////////// 여기까지//////////////////////////////////////////

	@Override
	public ProductVO mapPvo(String string) throws SQLException {

		ProductVO pvo = null;

		try {
			conn = ds.getConnection();

			String sql = "select fk_productid, color\n" + "from productoption\n" + "where productserialid = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, string);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				ProductOptionVO povo = new ProductOptionVO();
				povo.setFk_productid(rs.getString(1));
				povo.setColor(rs.getString(2));
				pvo = new ProductVO();
				pvo.setPovo(povo);
			}

		} finally {
			close();
		}
		return pvo;
	}

	@Override
	public ProductVO productPurchase2(Map<String, String> map) throws SQLException {
		ProductVO pvo = null;
		try {

			conn = ds.getConnection();

			String sql = "select A.productid, A.productname, A.price, A.imgfilename, B.color, B.productserialid\n"
					+ "				from product A\n" + "				JOIN productoption B\n"
					+ "				on A.productid = B.fk_productid\n"
					+ "				where A.productid = ? and B.color = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, map.get("productid"));
			pstmt.setString(2, map.get("color"));

			// pstmt.setString(3, paraMap.get("color"));
			// pstmt.setString(4, paraMap.get("productserialid"));

			rs = pstmt.executeQuery();

			if (rs.next()) {

				pvo = new ProductVO();

				pvo.setProductid(rs.getString(1));
				pvo.setProductname(rs.getString(2));
				pvo.setPrice(rs.getInt(3));
				pvo.setImgfilename(rs.getString(4));
				// pvo.getPovo().setColor(rs.getString(5));

				ProductOptionVO povo = new ProductOptionVO();

				povo.setColor(rs.getString(5));
				povo.setProductserialid(rs.getString(6));

				pvo.setPovo(povo);

			} // end of if -----------------------------

		} catch (SQLException e) {

		} finally {
			close();
		}

		return pvo;
	}

	@Override
	public int deleteOrderCart(Map<String, String> map, String memberno) throws SQLException {
		int n = 0;
		try {
			conn = ds.getConnection();
			String sql = "delete from cart where fk_productid= ? and selectcolor= ? and fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, map.get("productid"));
			pstmt.setString(2, map.get("color"));
			pstmt.setString(3, memberno);

			n = pstmt.executeUpdate();
		} catch (SQLException e) {

		} finally {
			close();
		}

		return n;
	}

	@Override
	public int selectCntCoupon(int usernum) throws SQLException {
		int cnt = 0;

		try {
			conn = ds.getConnection();

			String sql = "select count(fk_couponcode)\n" + "from eachcoupon\n"
					+ "where status = '0' and fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);
			rs = pstmt.executeQuery();
			rs.next();

			cnt = rs.getInt(1);

		} finally {
			close();
		}
		return cnt;
	}

	@Override
	public int selectSumPrice(int usernum) throws SQLException {
		int cnt = 0;

		try {
			conn = ds.getConnection();

			String sql = "select sum(totalprice)\n"+
					"from purchase\n"+
					"where fk_memberno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);
			rs = pstmt.executeQuery();
			rs.next();

			cnt = rs.getInt(1);

		} finally {
			close();
		}
		return cnt;
	}

	@Override
	public Map<String, String> selectMinMaxGrade(String grade) throws SQLException {
		
		Map<String, String> hashmap = new HashMap<>();

		try {
			conn = ds.getConnection();

			String sql = "select minstandard, maxstandard\n"+
					"from membership\n"+
					"where membershipname = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, grade);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				hashmap.put("min", rs.getString(1));
				hashmap.put("max", rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return hashmap;
	}

	@Override
	public String selectNextGrade(String min) throws SQLException {

		String str = "";

		try {
			conn = ds.getConnection();

			String sql = "select membershipname\n"+
					"from membership\n"+
					"where minstandard = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, min);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				str = rs.getString(1);
			}

			

		} finally {
			close();
		}
		return str;

	}

	@Override
	public int selectMediumNum(int usernum) throws SQLException {

		int cnt = 0;

		try {
			conn = ds.getConnection();

			String sql = "select count(purchaseno)\n"+
					"from purchase\n"+
					"where fk_memberno = ? and totalstatus = '진행중'";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);
			rs = pstmt.executeQuery();
			rs.next();

			cnt = rs.getInt(1);

		} finally {
			close();
		}
		return cnt;
	}

	@Override
	public int selectEndNum(int usernum) throws SQLException {
		int cnt = 0;

		try {
			conn = ds.getConnection();

			String sql = "select count(purchaseno)\n"+
					"from purchase\n"+
					"where fk_memberno = ? and totalstatus = '처리완료'";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, usernum);
			rs = pstmt.executeQuery();
			rs.next();

			cnt = rs.getInt(1);

		} finally {
			close();
		}
		return cnt;
	}

	@Override
	public int getTotalCouponPage(String memberno) throws SQLException {
		int totalPage = 0;
		try {
			conn = ds.getConnection();

			String sql = "select ceil(count(*)/ 10)\n"+
					"from coupon C\n"+
					"inner join eachcoupon E\n"+
					"ON C.couponcode = E.fk_couponcode\n"+
					"where E.fk_memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();
			rs.next();

			totalPage = rs.getInt(1);

		} finally {
			close();
		}

		return totalPage;
	}

	@Override
	public MemberVO editMember(String memberno) throws SQLException {
		
		MemberVO mvo = new MemberVO();

		try {

			conn = ds.getConnection();

			String sql = "select name, userid, email, mobile, postcode, address, detailaddress, extraaddress, birthday\n"+
					"    from member\n"+
					"    where memberno= ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();

			// 암복호화 해야됨!!!!!!!! 
			while (rs.next()) {

				mvo.setName(rs.getString(1));
				mvo.setUserid(rs.getString(2));
				mvo.setEmail(aes.decrypt(rs.getString(3)));
				System.out.println(mvo.getEmail());
				mvo.setMobile(aes.decrypt(rs.getString(4)));
				mvo.setPostcode(rs.getString(5));
				mvo.setAddress(rs.getString(6));
				mvo.setDetailaddress(rs.getString(7));
				mvo.setExtraaddress(rs.getString(8));
				mvo.setBirthday(rs.getString(9));

			} // end of if -----------------------------

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return mvo;
	}

	@Override
	public int updateMember(MemberVO mvo) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = " update member set name = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mvo.getName());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set userid = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getUserid());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set email = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, aes.encrypt(mvo.getEmail()));
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set mobile = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, aes.encrypt(mvo.getMobile()));
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set postcode = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getPostcode());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set address = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getAddress());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set detailaddress = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getDetailaddress());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set extraaddress = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getExtraaddress());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set birthday = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mvo.getBirthday());
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			sql = " update member set pwd = ? where memberno = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, Sha256.encrypt(mvo.getPwd()));
			pstmt.setInt(2, mvo.getMemberno());
			
			result = pstmt.executeUpdate();
			
			if (result == 1) {
				conn.commit();
			}
			
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	@Override
	public int updateReview(ReviewVO revo) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = " update review set content = ? where seq_review = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, revo.getContent());
			pstmt.setString(2, revo.getSeq_review());
			
			result = pstmt.executeUpdate();
			
			sql = " update review set score = ? where seq_review = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, revo.getScore());
			pstmt.setString(2, revo.getSeq_review());
			
			result = pstmt.executeUpdate();
			
			if (result == 1) {
				conn.commit();
			}
			
			
		}  finally {
			close();
		}
		
		return result;
	}

	@Override
	public int selectNumReview(String memberno) throws SQLException {
		int totalPage = 0;
		try {
			conn = ds.getConnection();

			String sql = "SELECT count(purchaseno)\n"+
					"    from \n"+
					"    (\n"+
					"    select D.purchasedetailid, productserialid, V7.fk_productid, color, imgfilename, productname, price, D.volume\n"+
					"    from (\n"+
					"    select productserialid, fk_productid, color, imgfilename, productname, price\n"+
					"    from productoption O\n"+
					"    right outer join product P\n"+
					"    on o.fk_productid = P.productid\n"+
					"    )V7\n"+
					"    inner join purchasedetail D\n"+
					"    on D.fk_productserialid = V7.productserialid\n"+
					"    ) V8\n"+
					"    right outer join \n"+
					"    (\n"+
					"    select D.purchasedetailid, fk_productserialid, purchaseday, P.fk_memberno, ordernum, deliverystatus, totalprice, purchaseno, reviewstatus\n"+
					"    from purchasedetail D\n"+
					"    inner join purchase P\n"+
					"    on D.fk_purchaseno = P.purchaseno\n"+
					"    ) V9\n"+
					"    on V8.purchasedetailid = V9.purchasedetailid\n"+
					"    where fk_memberno = ? and deliverystatus = '배송완료' and reviewstatus = '미완료' \n"+
					"    order by purchaseday desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();
			rs.next();

			totalPage = rs.getInt(1);

		} finally {
			close();
		}

		return totalPage;
	}

	@Override
	public int selectWriteReview(String memberno) throws SQLException {
		int totalPage = 0;
		try {
			conn = ds.getConnection();

			String sql = "select count(seq_review)\n"+
					"from review\n"+
					"where fk_memberno = ? ";  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberno);

			rs = pstmt.executeQuery();
			rs.next();

			totalPage = rs.getInt(1);

		} finally {
			close();
		}

		return totalPage;
	}
		
	//////////////////////////////////////////////////////////////////////////최은지:끝/////
	
}
	
	
	
	
	