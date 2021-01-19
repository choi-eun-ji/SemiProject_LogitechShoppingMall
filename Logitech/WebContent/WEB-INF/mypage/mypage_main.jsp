<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../header.jsp" />

<style type="text/css">
	body{
		border: solid 0px red;
		margin: 0;
		padding: 0;
		
		font-family: Arial, "MS Trebuchet", sans-serif;
		word-break: break-all;
	}
	
	div#container {
		text-align: center;
	}

	.iconstr > td {
		
		width: 230px;
		height: 250px;
		text-align: center;
		margin:10px;
		
	}
	
	#ordershorttr1 > td{
		border: solid 1px #f2f2f2;
		width: 190px;
		height: 100px;
		text-align: center;
	} 
 	
	#ordershorttr2 > td{
		color: red;
		text-align: center;
	}
	
	#input {
		width: 100px;
		hegith: 80px;
	}

	.indiv {
		display: inline;
	}
	
	#history2 {
		margin-left: 200px;
	}
	
	.eng {
		font-weight: bold;
		font-size: 13pt;
		padding: 10px;
	}
	a{
		font-size: 8pt;
		color: #cccccc;
		text-decoration: none;
	}
	a:link { color: #cccccc; text-decoration: none;}
 	a:visited { color: #cccccc; text-decoration: none;}
 	a:hover { color: #e6e6e6; text-decoration: underline;}
 	
 	div.bord {
 		border: solid 1px #f2f2f2;
 		margin: 10px;
 		height: 200px;
 		padding-top: 30px;
 	}
	
	#dont {
	padding-bottom: 30px;
	}
	
	#ordershort {
		width: 900px;
		padding: 10px;
	}
	#ortitle {
		font-weight: bold;
		font-size: 12pt;
		padding: 10px;
		background-color: #f2f2f2;
	}
	div#seconddiv {
	width: 880px;
	list-style: none;
	margin-left: 10px;
	
	}
	#maintr > td {
		width: 600px;
		border: solid 1px #f2f2f2;
		padding: 15px;
		padding-left: 30px;
	}

	#maintr > td > li > span {
		float: right;
		font-weight: bold;
	}
	#maintr > td > li >label {
		clear:both;
		font-weight: lighter;
		font-size: 10pt;
	}
	#bars {
		width:300px;
		height: 20px;
	}
	
	#maintr2 > td {
		width:700px;
		padding: 15px;
		padding-left: 30px;
	}
	#grade {
		width: 880px;
		margin-left: 60px;
	}
	.nextgra {
		font-weight: bold;
	}
	#next {
		margin-left: 340px;
	}
	#title {
   text-align: center;
   font-size: 18pt;
   margin-bottom: 60px;
   margin-top: 50px;
	}
	#contain {
		padding-left: 120px;
	}
	

</style>
<script type="text/javascript">

	$(document).ready(function() {
		var min = parseInt($("#sumpoint").text()) - parseInt($("#usedpoint").text());
		$("#okpoint").text(min+"원");
		
		var max = parseInt(${max});
		var total = parseInt(${sumprice});
		
		var val = max - total;
		
		$("#orderpri").text(val);
		
		$("#num1").text(${mediumNum});
		$("#num2").text(${endNum});
	});
</script>
<div class="container">
		<div id="title">MY PAGE</div>
		<div id="contain">
		<div id="grade">
			<table>
				<tr id="maintr2">
					<td>
						<div id="grade1" class="indiv">
							저희 쇼핑몰을 이용해주셔서 감사합니다.<br/>
							홍길동 님은 <span style="font-weight: bold;">[${grade}]</span> 등급입니다.
						</div>
					</td>
					<td>
						<div id="history1" class="indiv">
							<span>${min}</span>&nbsp;<progress value="${sumprice}" min="${min}" max="${max}" id="bars">37500</progress>&nbsp;<span>${max}</span>
						</div>
						<div>
						<span>${grade}</span>
						<span id="next">${nextgrade}</span>
						</div>
						<br/>
						<div id="grade2" >
							<span class="nextgra">[${nextgrade}]</span>까지 남은 구매 금액은 <span class="nextgra" id="orderpri"></span>원 입니다. 
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div id="history">
			<div id="history2" class="indiv">
				<div id="seconddiv">
					<table>
						<tr id="maintr">
						<td>		
							<li>
								<label>총 적립금:</label>
								<span id="sumpoint">${totalpt}원</span>
							</li>
							<li>
								<label>사용가능 적립금:</label>
								<span id="okpoint">0원</span>
							</li>
							<li>
								<label>사용된 적립금:</label>
								<span id="usedpoint">${usedpoint}원</span>
							</li>
						</td>
						<td>
							<li>
								<label>사용가능 쿠폰:</label>
								<span id="usecp">${cnt}개</span>
							</li>
							<li>
								<label>총 주문 금액:</label>
								<span id="usecp">${sumprice}원</span>
							</li>
						</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div id="ordershort">
			<div id="ortitle">나의 주문 처리 현황</div>
			<div id="ordershort1">
				<table>
					<tr id="ordershorttr1">
						<td>0<br><br>주문접수</td>
						<td><span id="num1">0</span><br><br>결제완료</td>
						<td>0<br><br>상품준비중</td>
						<td>0<br><br>배송중</td>
						<td><span id="num2">0</span><br><br>배송완료</td>
					</tr>
					
				</table>
			</div>
		</div>
		<table>
			<Tr class="iconstr"> 
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림1.png"/></div><div class="eng">ORDER</div><div><a href="<%= ctxPath %>/mypage/ordercheck.sg">주문/배송 조회</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림2.png"/></div><div><div class="eng">CART</div><a href="<%= ctxPath %>/mypage/ordercart.sg">장바구니</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림3.png"/></div><div class="eng">WISHLIST</div><div><a href="<%= ctxPath %>/mypage/orderheart.sg">찜목록</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림4.png"/></div><div class="eng">REVIEW</div><div><a href="<%= ctxPath %>/mypage/orderreview.sg">리뷰작성</a></div></div></td>
			</Tr>
			<tr class="iconstr">
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림5.png"/></div><div class="eng">POINT</div><div><a href="<%= ctxPath %>/mypage/mypoint.sg">포인트</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림6.png"/></div><div class="eng">COUPON</div><div><a href="<%= ctxPath %>/mypage/mycoupon.sg">쿠폰</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림7.png"/></div><div class="eng">PROFILE</div><div><a href="<%= ctxPath %>/mypage/editmyinfo.sg">회원정보 수정/탈퇴</a></div></div></td>
				<td><div class="bord"><div><img src="<%= ctxPath %>/images/그림8.png"/></div><div class="eng">Q&A</div><div><a href="<%= ctxPath %>/mypage/mycoupon.sg">1:1 문의 내역</a></div></div></td>
			</tr>
		</table>
</div>
</div>

<jsp:include page="../footer.jsp" />

