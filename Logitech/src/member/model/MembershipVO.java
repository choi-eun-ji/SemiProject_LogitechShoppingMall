package member.model;

public class MembershipVO {
	
	private String membershipname;
	private int minstandard;
	private int maxstandard;
	
	public String getMembershipname() {
		return membershipname;
	}
	
	public void setMembershipname(String membershipname) {
		this.membershipname = membershipname;
	}
	
	public int getMinstandard() {
		return minstandard;
	}
	
	public void setMinstandard(int minstandard) {
		this.minstandard = minstandard;
	}
	
	public int getMaxstandard() {
		return maxstandard;
	}
	
	public void setMaxstandard(int maxstandard) {
		this.maxstandard = maxstandard;
	}

}
