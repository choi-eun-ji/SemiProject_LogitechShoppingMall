package manager.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import member.model.MemberVO;
import member.model.MembershipVO;
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

public interface InterManagerDAO {

	// 관리자 로그인 화면에서 입력받은 정보에 해당하는 한 명의 정보를 select 하여 반환하는 메소드(관리자 로그인 메소드)
	ManagerVO selectOneManager(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 하여 모든 회원 또는 검색한 회원 목록 보여주기 메소드
	List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위해 전체 회원에 대한 총 페이지 수 알아오기(select) 메소드
	int getTotalPage(Map<String, String> paraMap) throws SQLException;
	
	// memberno 값을 가져와 회원 1명에 대한 상세정보 알아오기(select) 메소드
	MemberVO memberOneDetail(String memberno) throws SQLException;

	// 페이징 처리를 하여 모든 제품 또는 검색한 제품 목록 보여주기 메소드
	List<ProductOptionVO> selectPagingProductOption(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위해 전체 판매제품에 대한 총 페이지 수 알아오기(select) 메소드
	int getPOTotalPage(Map<String, String> paraMap) throws SQLException;
	
	// 재고량을 수정하는 메소드
	int stockUpdate(Map<String, String> paraMap) throws SQLException;

	// productserialid 값을 가져와 선택한 판매제품에 대한 상세정보 알아오기(select) 메소드
	ProductOptionVO prodOptionOneDetail(String productserialid) throws SQLException;

	// 페이징 처리를 하여 모든 주문 또는 검색한 주문 목록 보여주기 메소드
	List<PurchaseDetailVO> selectPagingPurchaseList(Map<String, String> paraMap) throws SQLException;
	
	// 페이징 처리를 위해 전체 주문에 대한 총 페이지 수 알아오기(select) 메소드
	int getPurcTotalPage(Map<String, String> paraMap) throws SQLException;

	// purchasedetailid 값을 가져와 선택한 판매제품에 대한 상세정보 알아오기(select) 메소드
	List<PurchaseDetailVO> purchaseOneDetail(String fk_purchaseno) throws SQLException;
	
	// purchasedetailid 값을 가져와 주문자에 대한 상세정보 알아오기(select) 메소드
	PurchaseVO purchaseByMember(String fk_purchaseno) throws SQLException;
	
	// 페이징 처리를 하여 모든 쿠폰 또는 검색한 쿠폰 목록 보여주기 메소드
	List<EachCouponVO> selectPagingCoupon(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위해 전체 쿠폰에 대한 총 페이지 수 알아오기(select) 메소드
	int getCoupTotalPage(Map<String, String> paraMap) throws SQLException;
	
	// eachcouponcode 값을 가져와 개별 쿠폰에 대한 상세정보 알아오기(select) 메소드
	EachCouponVO couponOneDetail(String eachcouponcode) throws SQLException;

	// 페이징 처리를 하여 모든 이벤트 또는 검색한 이벤트 목록 보여주기 메소드
	List<EventVO> selectPagingEvent(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위해 전체 이벤트에 대한 총 페이지 수 알아오기(select) 메소드
	int getEventTotalPage(Map<String, String> paraMap) throws SQLException;

	// seq_event 값을 가져와 개별 이벤트에 대한 상세정보 알아오기(select) 메소드
	List<JoinEventVO> eventOneDetail(String seq_event) throws SQLException;

	// Ajax(JSON) 를 사용하여 일대일 문의 목록을 "더 보기" 방식으로 페이징 처리하기 위해 전체 문의 수를 알아오는 메소드
	int totalOQCount(String today) throws SQLException;

	// Ajax(JSON) 를 이용하여 "더 보기" 방식(페이징 처리)으로 일대일 문의를 5개씩 잘라서(start ~ end) 조회하는 메소드
	List<OneQueryVO> selectByWriteday(Map<String, String> paraMap) throws SQLException;

	// 일대일 문의 처리를 위해 상세정보 알아오기(select) 메소드
	OneQueryVO oneQueryDetail(String seq_oq) throws SQLException;

	// Ajax(JSON) 를 사용하여 제품 문의 목록을 "더 보기" 방식으로 페이징 처리하기 위해 전체 문의 수를 알아오는 메소드
	int totalPQACount(String today) throws SQLException;

	// Ajax(JSON) 를 이용하여 "더 보기" 방식(페이징 처리)으로 제품 문의를 5개씩 잘라서(start ~ end) 조회하는 메소드
	List<ProductQAVO> selectPQAByWriteday(Map<String, String> paraMap) throws SQLException;

	// 관리자 계정등록 메소드(manager 테이블에 insert)
	int registerManager(ManagerVO mgvo) throws SQLException;

	// modal 창에서 userid 클릭 시 아이디와 수신 정보를 select 하는 메소드
	MemberVO checkAgreeStatus(String userid) throws SQLException;

	// 일대일 문의글에 대한 답변 insert 및 답변 상태 update 메소드
	int oneQueryAnswer(Map<String, String> paraMap) throws SQLException;

	// 제품 문의 상세정보 select
	ProductQAVO productQADetail(String seq_oq) throws SQLException;
	
	// 제품 문의글에 대한 답변 insert 및 답변 상태 update 메소드
	int productQAAnswer(Map<String, String> paraMap) throws SQLException;

	// 전체 회원 대상 탈퇴 상태 업데이트 메소드
	int statusUpdate() throws SQLException;
	
	// 제품 가격 수정 메소드
	int priceUpdate(Map<String, String> paraMap) throws SQLException;

	// 제품옵션 정보 수정 메소드
	int productOptionUpdate(ProductOptionVO povo) throws SQLException;

	// 주문 제품 배송하여 배송 상태와 배송 제품의 재고량을 수정하는 메소드
	int updateDelivery(Map<String, String> paraMap) throws SQLException;

	// 주문 내 개별 상품 전체가 배송완료된 경우 주문 진행상태를 수정하는 메소드
	int purchaseListUpdate(String purchaseno) throws SQLException;

	// 이벤트 당첨자 당첨 상태를 update 하는 메소드 
	int eventSelectWinner(Map<String, String> paraMap) throws SQLException;

	// 제품 카테고리 조회 메소드
	List<ProductCategoryVO> productCategory() throws SQLException;
	
	// 회원등급 테이블 조회 메소드
	List<MembershipVO> membership() throws SQLException;

	// 쿠폰 코드 중복검사 메소드
	boolean codeDuplicateCheck(String couponcode) throws SQLException;

	// 쿠폰 등록 메소드
	int registerCoupon(Map<String, String> paraMap) throws SQLException;

	// 이벤트 등록 시 카테고리별 상품 목록을 불러오는 메소드
	List<ProductVO> productByCategory(String category) throws SQLException;

	// 상품 이미지 조회 메소드
	ProductVO productImg(String productid) throws SQLException;

	// 이벤트 등록 메소드
	int registerEvent(Map<String, String> paraMap) throws SQLException;
	
	// 관리자 아이디 중복검사 메소드
	boolean idDuplicateCheck(String managerid) throws SQLException;

	// 수정하고자 하는 관리자 정보를 불러오는 메소드
	ManagerVO idExistCheck(String updateid) throws SQLException;
	
	// 관리자 계정수정 메소드(manager 테이블에 insert)
	int updateManager(ManagerVO mgvo) throws SQLException;

	// 제품 아이디 중복검사 메소드
	boolean prodIdDuplicateCheck(String productid) throws SQLException;

	// 제품 일련번호 중복검사 메소드
	boolean serialIdDuplicateCheck(String productserialid) throws SQLException;

	// 제품 등록 메소드
	int registerProduct(Map<String, String> paraMap) throws SQLException;

	// 제품 옵션 등록 시 제품 아이디를 찾는 메소드
	List<ProductVO> prodIdFind(String productid) throws SQLException;

	// 제품 옵션 등록 메소드
	int registerOption(Map<String, String> paraMap) throws SQLException;
	
	// 회원 현황 요약 메소드
	int summaryMember() throws SQLException;

	// 제품 현황 요약 메소드
	int summaryProduct() throws SQLException;

	// 주문 현황 요약 메소드
	int summaryPurchase() throws SQLException;

	// 쿠폰 현황 요약 메소드
	int summaryCoupon() throws SQLException;

	// 신규 회원 그래프 데이터 가져오기
	List<Map<String, Integer>> newMemberChart() throws SQLException;

	// 신규 회원 막대바 가져오기
	int newMemberBar() throws SQLException;

	// 신규 이벤트 참여 막대바 가져오기
	int joinEventBar() throws SQLException;

	// 재고 보충을 위해 품절 상품 알아오기
	List<Map<String, String>> displayStock() throws SQLException;

	// 인기 판매 상품 순위 알아오기
	List<Map<String, String>> displayBest() throws SQLException;

	// 회원 탈퇴 처리하기
	int memberDropout(String userid) throws SQLException;
	
}
