<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String ctxPath = request.getContextPath();
%>
<jsp:include page="../header.jsp" />

<style type="text/css">
	button {
	  background:none;
	  border:0;
	  outline:0;
	  cursor:pointer;
	}
	.tab_menu_container {
	  display:flex;
	}
	.tab_menu_btn {
	  width:120px;
	  height:40px;
	  transition:0.3s all;
	  font-size: 10pt;
	}
	.tab_menu_btn.on {
	  border-bottom:2px solid #df0000;
	  font-weight:700;
	  color:#FF6666;
	}
	.tab_menu_btn:hover {
	  color:#FF6666;
	}
	.tab_box {
	  display:none;
	  padding:20px;
	}
	.tab_box.on {
	  display:block;
	}
	#title {
	  font-weight: bold;
	  font-size: 18pt;
	}
	.container {
	padding-top: 40px;
	}
	
	tr#ordertr > td {
		padding: 10px;
		width: 500px;
		background-color: #f4f4f0;
		padding-top: 20px;
		padding-bottom: 20px;
		text-align: center;
	}
	
	#ordertable {
		margin-top: 70px;
		font-size: 8pt;
	}
	
	span#info {
		font-weight: bold;
		font-size: 10pt;
	}
	.eachimg {
		width: 80px;
		height: 80px;
	}
	.trcs > td {
		padding: 13px;
	}
	#upwid {
		padding:0px;
	}
	#ordertr2 > td {
		text-align: center;
	}
	.trcs {
		font-size: 10pt;
		font-weight: bold;
		background-color: #f4f4f0;
	}
	#total > td{
				padding-left: 15px;
				padding-bottom: 10px;
		
	}
	#maininfo > li {
		font-size: 5pt;
		color: #999999;
	}
	#btngroup > button {
		font-size: 9pt;
		border: solid 1px #999999;
		color: #808080;
	}
	#title {
	   text-align: center;
	   font-size: 18pt;
	   margin-top: 50px;  
	}
</style>
<script type="text/javascript">
$(document).ready(function() {
	
	$(".hidentrtoday").css("display","none");
	$(".hidentrweek").css("display", "none");
	$(".hidentrmonth1").css("display","none");
	$(".hidentrmonth3").css("display","none");
	$(".hidentrmonth6").css("display", "none");
	//버튼 색 제거,추가
	$('.tab_menu_btn').on('click',function(){
	  $('.tab_menu_btn').removeClass('on');
	  $(this).addClass('on')
	});

	//1번 컨텐츠
	$('.tab_menu_btn1').on('click',function(){
	  $('.tab_box').hide();
	  $('.tab_box1').show();
	});

	//2번 컨텐츠
	
	//3번 컨텐츠
	$('.tab_menu_btn3').on('click',function(){
	  $('.tab_box').hide();
	  $('.tab_box3').show();
	});
	
	var sum = 0;
	for(var i=0; i<${sizeli}; i++){
		 sum = parseInt($("#pr1"+i).val()) * parseInt($("#num1" + i).val());
		$("#prices1"+i).text(sum);		
	}
	
	
});

function todayfunc(){
	$(".hidentr").css("display","none");
	$(".hidentrtoday").css("display","");
	$(".hidentrweek").css("display","none");
	$(".hidentrmonth1").css("display","none");
	$(".hidentrmonth3").css("display","none");
	$(".hidentrmonth6").css("display", "none");
	
	var sum = 0;
	for(var i=0; i<${todayli}; i++){
		 sum = parseInt($("#pr2"+i).val()) * parseInt($("#num2" + i).val());
		$("#prices2"+i).text(sum);		
	}
	
}

function weekfunc() {
	$(".hidentrweek").css("display","");
	$(".hidentr").css("display","none");
	$(".hidentrtoday").css("display","none");
	$(".hidentrmonth1").css("display","none");
	$(".hidentrmonth3").css("display","none");
	$(".hidentrmonth6").css("display", "none");
	
	var sum = 0;
	for(var i=0; i<${weekli}; i++){
		 sum = parseInt($("#pr3"+i).val()) * parseInt($("#num3" + i).val());
		$("#prices3"+i).text(sum);		
	}
}

function month1func() {
	$(".hidentr").css("display","none");
	$(".hidentrtoday").css("display","none");
	$(".hidentrweek").css("display","none");
	$(".hidentrmonth1").css("display","");
	$(".hidentrmonth3").css("display","none");
	$(".hidentrmonth6").css("display", "none");
	
	var sum = 0;
	for(var i=0; i<${month1li}; i++){
		 sum = parseInt($("#pr4"+i).val()) * parseInt($("#num4" + i).val());
		$("#prices4"+i).text(sum);		
	}
}

function month3func() {
	$(".hidentr").css("display","none");
	$(".hidentrtoday").css("display","none");
	$(".hidentrweek").css("display","none");
	$(".hidentrmonth1").css("display","none");
	$(".hidentrmonth3").css("display","");
	$(".hidentrmonth6").css("display", "none");
	
	var sum = 0;
	for(var i=0; i<${month3li}; i++){
		 sum = parseInt($("#pr5"+i).val()) * parseInt($("#num5" + i).val());
		$("#prices5"+i).text(sum);		
	}
	
}


function month6func() {
	$(".hidentr").css("display","none");
	$(".hidentrtoday").css("display","none");
	$(".hidentrweek").css("display","none");
	$(".hidentrmonth1").css("display","none");
	$(".hidentrmonth3").css("display","none");
	$(".hidentrmonth6").css("display", "");
	
	var sum = 0;
	for(var i=0; i<${month6li}; i++){
		 sum = parseInt($("#pr6"+i).val()) * parseInt($("#num6" + i).val());
		$("#prices6"+i).text(sum);		
	}
}
</script>

<head>
<div id="title">ORDER</div>
</head>
<div class="container">
<div class="tab_wrap">
  <div class="tab_menu_container">
    <button class="tab_menu_btn1 tab_menu_btn on" type="button">${sessionScope.loginuser.name}주문내역조회 (<span>${sumorder}</span>)</button>
  </div> <!-- tab_menu_container e -->

  <div class="tab_box_container">
    <div class="tab_box1 tab_box on">
		<div id="btngroup">
			<button type="button" onclick="todayfunc();">오늘</button>
			<button type="button" onclick="weekfunc();">1주일</button>
			<button type="button" onclick="month1func();">1개월</button>
			<button type="button" onclick="month3func();">3개월</button>
			<button type="button" onclick="month6func();">6개월</button>
		</div>
		<div>
		<br><br>
			<ul id="maininfo">
				<li>기본적으로 최근 6개월간의 자료가 조회됩니다.</li>
				<li>완료 후 6개월 이상의 주문건은 조회되지 않습니다.</li>
				<li>주문번호를 클릭하시면 해당 주문에 대한 상세내역을 보실 수 있습니다.</li>
			</ul>
		</div>
		<div id="ordertable">
			<span id="info">주문 상품 정보</span>
			<table>
				<tr id="ordertr">
					<td>이미지</td>
					<td>상품정보</td>
					<td>수량</td>
					<td>상품구매금액</td>
					<td>주문처리상태</td>
				</tr>
				<c:forEach var="map" items="${listmap}" varStatus="status">
					
					<c:if test="${map.ordernum ne no}">
						<c:set var="no" value="${map.ordernum}"/>
						
						<tr id="iftr1" class="hidentr trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total1" class="hidentr">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr2" class="hidentr">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid1">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num1${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr1${status.index}"/>
							<span id="prices1${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
				
				
				
				<c:forEach var="map" items="${todaylist}" varStatus="status">
					
					<c:if test="${map.ordernum ne no2}">
						<c:set var="no2" value="${map.ordernum}"/>
						
						<tr id="iftr2" class="hidentrtoday trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total2" class="hidentrtoday">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr2" class="hidentrtoday">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid2">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num2${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr2${status.index}"/>
							<span id="prices2${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
				
				
				
				<c:forEach var="map" items="${weeklist}" varStatus="status">
					
					<c:if test="${map.ordernum ne no3}">
						<c:set var="no3" value="${map.ordernum}"/>
						
						<tr id="iftr3" class="hidentrweek trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total3" class="hidentrweek">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr3" class="hidentrweek">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid3">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num3${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr3${status.index}"/>
							<span id="prices3${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
				
				
				
				
				<c:forEach var="map" items="${month1list}" varStatus="status">
					
					<c:if test="${map.ordernum ne no4}">
						<c:set var="no4" value="${map.ordernum}"/>
						
						<tr id="iftr4" class="hidentrmonth1 trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total4" class="hidentrmonth1">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr4" class="hidentrmonth1">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid4">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num4${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr4${status.index}"/>
							<span id="prices4${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
				
				
				
				
				<c:forEach var="map" items="${month3list}" varStatus="status">
					
					<c:if test="${map.ordernum ne no5}">
						<c:set var="no5" value="${map.ordernum}"/>
						
						<tr id="iftr5" class="hidentrmonth3 trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total5" class="hidentrmonth3">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr5" class="hidentrmonth3">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid5">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num5${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr5${status.index}"/>
							<span id="prices5${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
				
				
				
				
				<c:forEach var="map" items="${month6list}" varStatus="status">
					
					<c:if test="${map.ordernum ne no6}">
						<c:set var="no6" value="${map.ordernum}"/>
						
						<tr id="iftr6" class="hidentrmonth6 trcs">
							<td colspan="1">주문번호:&nbsp;${map.ordernum}</td>
							<td colspan="5">주문날짜:&nbsp;${map.purchaseday}</td>
						</tr>
						<tr id="total6" class="hidentrmonth6">
							<td colspan="6">총 결제금액: &nbsp;${map.totalprice}</span></td>
							
						</tr>
					</c:if>
				
					<tr id="ordertr6" class="hidentrmonth6">
						<td><img class="eachimg" src="${map.imgfilename}" /></td>
						<td id="upwid6">
							${map.fk_productid}<br>
							<span>${map.productname}</span><br>
							<span>[옵션:${map.color}]</span>
						</td>
						<td>
							<span>${map.volume}</span>
							<input class= "numvals" value="${map.volume}" hidden="true" id="num6${status.index}"/>						
						</td>
						<td>
							<input value="${map.price}" hidden="true" id="pr6${status.index}"/>
							<span id="prices6${status.index}"></span>
						</td>
						<td>${map.deliverystatus}</td>
					</tr>
				</c:forEach>
				
			</table>
		</div>
	</div>

  </div> <!-- tab_box_container e -->
</div> <!-- tab_wrap e -->

</div>

<jsp:include page="../footer.jsp" />




















