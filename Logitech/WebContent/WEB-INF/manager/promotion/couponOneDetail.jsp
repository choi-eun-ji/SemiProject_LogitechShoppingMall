<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../managerHeader.jsp" />

<style>
#couponState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

div#couponInfo {
	width: 95%;
	text-align: left;
	border: solid 0px red;
	margin-top: 30px; 
	font-size: 13pt;
	line-height: 200%;
}

span.myli {
	display: inline-block;
	width: 200px;
	border: solid 0px blue;
	margin-top: 20px;
}

div.divMyli {
	display: inline-block;
	width: 3px;
	height: 35px;
	background-color: #ccc;
	vertical-align: middle;
	margin-right: 50px;
}
</style>

<script type="text/javascript">
	var goBackURL = "";

	$(document).ready(function(){
		goBackURL = "${goBackURL}";
		
		goBackURL = goBackURL.replace(/ /gi, "&");
	});// end of $(document).ready(function(){})-----------------------------
	
	function goCouponList() {
		location.href = "/Logitech/"+goBackURL;
	}// end of function goMemberList(){}-----------------------------
</script>

<div id="couponState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>쿠폰 정보</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<c:if test="${empty ecvo}">
					존재하지 않는 쿠폰입니다.<br>
				</c:if>
				
				<c:if test="${not empty ecvo}">
					<h3 style="margin-left: 40px;">${ecvo.eachcouponcode} 쿠폰 상세 정보</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="couponInfo" style="background-color: white;">
						<ol>	
							<li><span class="myli">쿠폰아이디 : </span><div class="divMyli"></div>${ecvo.fk_couponcode}</li>
							<li><span class="myli">개별쿠폰번호 : </span><div class="divMyli"></div>${ecvo.eachcouponcode}</li>
							<li><span class="myli">쿠폰명 : </span><div class="divMyli"></div>${ecvo.coupvo.couponname}</li>
							<li><span class="myli">할인액 : </span><div class="divMyli"></div>${ecvo.coupvo.discount}</li>
							<li><span class="myli">최소주문금액 : </span><div class="divMyli"></div>${ecvo.coupvo.minprice}</li>
							<li><span class="myli">적용회원등급 : </span><div class="divMyli"></div>${ecvo.coupvo.fk_membershipname}</li>
							<li><span class="myli">발급회원번호 : </span><div class="divMyli"></div>${ecvo.fk_memberno}</li>
							<li><span class="myli">상태 : </span><div class="divMyli"></div><c:choose><c:when test="${ecvo.status == 0}">미사용</c:when><c:otherwise>사용완료</c:otherwise></c:choose></li>
							<li><span class="myli">만료일자 : </span><div class="divMyli"></div>${ecvo.endday}</li>
						</ol>
					</div>
				
					<div style="background-color: white; margin: 50px 100px;">
						<button type="button" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="javascript:goCouponList();">쿠폰목록으로 이동</button>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../managerFooter.jsp" />