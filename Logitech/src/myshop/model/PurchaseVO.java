package myshop.model;

import member.model.MemberVO;
import member.model.PointVO;

public class PurchaseVO {
	
	private int purchaseno;
	private int fk_memberno;
	private String receiver;
	private String postcode;
	private String address;
	private String detailaddress;
	private String extraaddress;
	private String payment;
	private String purchaseday;
	private int totalprice;
	private String totalstatus;
	private String ordernum;
	
	private MemberVO mvo;
	private PointVO pointvo;
	private int discount;
	
	public int getPurchaseno() {
		return purchaseno;
	}
	
	public void setPurchaseno(int purchaseno) {
		this.purchaseno = purchaseno;
	}
	
	public int getFk_memberno() {
		return fk_memberno;
	}
	
	public void setFk_memberno(int fk_memberno) {
		this.fk_memberno = fk_memberno;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getPostcode() {
		return postcode;
	}
	
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDetailaddress() {
		return detailaddress;
	}
	
	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}
	
	public String getExtraaddress() {
		return extraaddress;
	}
	
	public void setExtraaddress(String extraaddress) {
		this.extraaddress = extraaddress;
	}
	
	public String getPayment() {
		return payment;
	}
	
	public void setPayment(String payment) {
		this.payment = payment;
	}
	
	public String getPurchaseday() {
		return purchaseday;
	}
	
	public void setPurchaseday(String purchaseday) {
		this.purchaseday = purchaseday;
	}

	public int getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}
	
	public String getTotalstatus() {
		return totalstatus;
	}

	public void setTotalstatus(String totalstatus) {
		this.totalstatus = totalstatus;
	}

	public MemberVO getMvo() {
		return mvo;
	}

	public void setMvo(MemberVO mvo) {
		this.mvo = mvo;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public PointVO getPointvo() {
		return pointvo;
	}

	public void setPointvo(PointVO pointvo) {
		this.pointvo = pointvo;
	}

}
