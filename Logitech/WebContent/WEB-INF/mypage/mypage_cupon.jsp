<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../header.jsp" />

<style type="text/css">
#title {
	text-align: center;
	font-size: 18pt;
	margin-bottom: 60px;
	margin-top: 30px;
}

#seconddiv > li {
	display: block;
	height: 50px;
	vertical-align: middle;
	width: 500px;
	padding-left: 50px;
	padding-right: 50px;
}

#seconddiv > li > span {
	float:right;
	padding-top: 15px;
}

div#seconddiv {
	background-color:#f4f4f0; 
	padding-top: 20px;
	border: solid 1px #e6e6e6;
	border-radius: 10px;
	width: 500px;
	margin-left: 300px;
}

label {
	padding-top: 15px;
}
#forediv{
	text-align: center;
}

#table1 {
	font-size: 10pt;
}

#sendbtn {
	padding: 10px;
	margin-left: 200px;
	margin-top: 20px;
	margin-bottom: 20px;
	border: 0px;

}
#register {
	text-align: center;
	
	padding: 20px;
}
#registtitle {
	margin-bottom: 10px;
	
	font-size: 10pt;
}
#registcontent {
	background-color: #f4f4f0;
	padding:20px;
}
#err {
	font-size: 6pt;
	margin-top: 10px;
}

#licou {
	height: 100px;
	text-align: right;
	margin-top: -30px;
	list-style: none;
}

#btndiv {
	padding: 0px;
	margin-top: 50px;
}

.stybtn {
	background-color: #ffffff;
	border-radius: 8px;
	font-size: 12px;
	border: none;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		
		var size = ${size};
		
		$("#sendbtn").click(function() {
			var arrObj = new Array();
			
			for(var i=0; i<size; i++){
				var pro = {
					"fk_couponcode" : $("#code"+i).val()
				};
				arrObj.push(pro);
			}
			
			var str_arrObj = JSON.stringify(arrObj);
			$.ajax({
				url:"<%= ctxPath%>/mypage/couponaj.sg",
	        	dataType: "json",
	        	data:{"str_arrObj":str_arrObj},	// data는 위의 url로 전송해야할 데이터를 말한다. 
	        	type:"post", 
	        	success:function(json){
	        		if(json.b == true){
	        			alert(${size}+"장의 쿠폰이 발급되었습니다.");
	        			location.reload();
	        			$("#sendbtn").text("쿠폰 발급 완료");
	        		}
	        		else{
	        			alert("이미 발급 받은 쿠폰입니다.");
	        		}
	        		
	        		
	        	},
	        	error: function(request, status, error){
	                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			});
		});
		
		
	});
	
	function func_coupon() {
		var frm = document.registerform;
		frm.action="<%=ctxPath%>/mypage/mycoupon.sg";
		frm.method="POST";
		frm.submit();
		if(${n} == 1){
		alert("쿠폰이 등록되었습니다.");
		location.reload();
		}
		else{
			alert("이미 등록되어진 쿠폰입니다.");
			location.reload();
		}
	}
	
	
</script>
<div class="container">
	<div id="title">MY COUPON</div>

	<div id="seconddiv">		
			<li>
				<label><span>${name}</span> 님의 회원등급 : </label>
				<span style="font-weight: bold;">${grade}</span>
			</li>
			<li >
				<label>${grade} 쿠폰 팩:</label>
				<ul id="licou">
				<c:forEach items="${maplist}" var="map" varStatus="status">
						<li>▶&nbsp;&nbsp;&nbsp;&nbsp;${map.discount}원 쿠폰 팩</li>
						<input type="text" id="code${status.index}" value="${map.couponcode}" hidden="true"/>
				</c:forEach>
				</ul>
			</li>
			<div id="btndiv"><button type="button" id="sendbtn" class="stybtn">쿠폰 발급 받기</button></div>
	</div>
	<br><br><br>
	<h5 class="text-center"><span style="background-color:#f4f4f0; padding:5px; border-radius: 3px; font-size: 10pt;">쿠폰내역보기</span></h5>
	<br>
	<div id="thirddiv" class="container">
		<table id="table1" class="table table-striped">
			<thead>
				<tr>
					<th>쿠폰명</th>
					<th>쿠폰 금액</th>
					<th>최소사용 금액</th>
					<th>사용여부</th>
				</tr>
			</thead>
			<tbody id="bodys">
			 	<c:forEach items="${maparr}" var="maps" varStatus="status">
				<tr>
					<td>${maps.couponname}</td>
					<td>${maps.discount}원</td>
					<td>${maps.minprice}원</td>
					<td>
						<c:if test="${maps.status eq '0'}">
							사용 가능
						</c:if>
						
						<c:if test="${maps.status eq '1'}">
							기한 만료
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>		
	</div>
	<div align="center" id="barcen">
			${pageBar} 	
	</div>	
	<hr/>
	
	<div id="register">
	<form name="registerform">
		<div id="registtitle" style="margin-bottom: 10px;"><span style="background-color:#f4f4f0; padding:5px; border-radius: 3px; font-size: 10pt;">쿠폰인증번호 등록하기</span></div>
		<div id="registcontent">
			<input type="text" id="inputcou" name="couponnum" style="border: none; margin-right: 10px;">
			<button id="couponnumbtn" onclick="func_coupon();" class="stybtn">쿠폰번호인증</button>
			<div id="err">
				반드시 쇼핑몰에서 발생한 쿠폰번호만 입력해주세요. (10~35자 일련번호 "-" 제외)
			</div>
		</div>
	</form>
	</div>
</div>

<jsp:include page="../footer.jsp" />




















