<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.text.SimpleDateFormat, java.util.Calendar"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<%
	String ctxPath = request.getContextPath();

	Calendar cal = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String today = format.format(cal.getTime());
%>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<jsp:include page="managerHeader.jsp" />

<style type="text/css">
#dashboard {
	font-size: 30pt;
	font-weight: bold;
	letter-spacing: 4px;
	padding: 20px;
	padding-left: 50px;
}

#divUl {
/*	border: solid 1px red; */
	width: 90%;
	margin: 0 0 50px 50px;
	padding: 40px;
}

div.summary {
/*	border: solid 1px red; */
	display: inline-block;
	margin: 20px 20px;
}

div.apply {
	margin-right: 50px;
}

span.summaryTitle {
	background-color: #e6e6e6;
	border-radius: 3px;
}

#viewBox {
	display: table;
/*	float: left; */
	table-layout: fixed;
/*	list-style: none; */
	text-align: center;
	width: 100%;
	margin: 0px;
	padding: 0px;
}

#viewBox > li {
	display: table-cell;
	list-style: none;
	margin: 0px;
	padding: 0px;
}

.viewParty {
	text-align: center;
	line-height: 30px;
	margin: 0 auto;
}

li.boxForWhite {
	background-color: white;
}

#divUnbox {
/*	border: solid 1px red; */
	width: 55%;
}

#currentState {
	width: 40%;
	margin-left: 40px;
}

.tblIndex {
	width: 90%;
	margin-left: auto;
	margin-right: auto;
}

.tblIndex > thead > tr {
	background-color: #bfbfbf;
}

.tblIndex, .tblIndex tr, .tblIndex th, .tblIndex td {
	border: solid 1px #ccc;
	text-align: center;
}

.tblIndex th, .tblIndex td {
	height: 50px;
}

#tblStock {
	width: 480px;
}

#tblBest {
	width: 480px;
}

.divTable {
	text-align: center;
	margin: 30px;
}

#displayOneQuery > tr:hover, #displayProductQA > tr:hover {
	background-color: #f2f2f2;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("span#totalHITCountOQ").hide();
		$("span#countHITOQ").hide();
		$("span#totalHITCountPQA").hide();
		$("span#countHITPQA").hide();
		
		displayOneQuery("1");  // 문서가 로딩되면 1~8 까지 상품이 보여야 한다.
		displayProductQA("1");
		
		// 일대일 문의 게시물을 더 보기 위하여 "더 보기" 버튼 클릭액션 이벤트 등록하기
		$("button#btnMoreOneQuery").click(function(){
			if($(this).text() == "처음으로") {
				$("div#displayOneQuery").empty();
				$("span#oqend").empty();
				displayOneQuery("1");
				$(this).text("더 보기");
			} else {
				displayOneQuery($(this).val());
			}// end of if($(this).text() == "처음으로"){}--------------------------
		});// end of $("button#btnMoreOneQuery").click(function(){})----------------------
		
		// 제품 문의 게시물을 더 보기 위하여 "더 보기" 버튼 클릭액션 이벤트 등록하기
		$("button#btnMoreProdQA").click(function(){
			if($(this).text() == "처음으로") {
				$("div#displayProductQA").empty();
				$("span#pqaend").empty();
				displayProductQA("1");
				$(this).text("더 보기");
			} else {
				displayProductQA($(this).val());
			}// end of if($(this).text() == "처음으로"){}--------------------------
		});// end of $("button#btnMoreProdQA").click(function(){})----------------------
		
		
		// 일대일 문의 행 클릭 시 modal 창에 상세 정보 띄우기
		$(document).on("click", "#displayOneQuery > tr", function(){
			var seq_oq = $(this).children(".seq_oq").text();
			var userid = $(this).children(".userid").text();
			// alert(seq_oq);
			
			var html = "<iframe style='border: none; width: 100%; height: 280px;' src='/Logitech/manager/support/oneQueryDetail.sg?seq_oq="+seq_oq+"&userid="+userid+"'></iframe>";
			
			$("div.oqmodal-body").html(html);
			
			$("#oneQueryModal").modal('show');
		});// end of $(document).on("click", "#displayOneQuery > tr", function(){})-----------------
		
		$(document).on("click", "#displayProductQA > tr", function(){
			var seq_qa = $(this).children(".seq_qa").text();
			var userid = $(this).children(".userid").text();
			// alert(seq_oq);
			
			var html = "<iframe style='border: none; width: 100%; height: 280px;' src='/Logitech/manager/support/productQADetail.sg?seq_qa="+seq_qa+"&userid="+userid+"'></iframe>";
			
			$("div.oqmodal-body").html(html);
			
			$("#oneQueryModal").modal('show');
		});// end of $(document).on("click", "#displayOneQuery > tr", function(){})-----------------
		
		// 메인 페이지 정보 출력 함수
		summaryMember();
		summaryProduct();
		summaryPurchase();
		summaryCoupon();
		
		newMemberChart();
		newMemberBar();
		joinEventBar();
		displayStock();
		displayBest();
	});// end of $(document).ready(function(){})-----------------------------
	
	var lenHIT = 5;  // HIT 상품 "더보기..." 버튼을 클릭할 보여줄 상품의 개수(단위) 
	
	// display 할 일대일 문의 정보를 추가 요청하는 함수(Ajax 로 처리한다)
	function displayOneQuery(start) {
		$.ajax({
			url:"<%= ctxPath%>/manager/displayOneQueryJSON.sg",
		//	type:"GET",
			data:{"today":"<%= today%>"
				, "start":start
				, "len":lenHIT},
			dataType:"JSON",
			success:function(json){
				var html = "";
				
				if(json.length > 0) {  // 배열.length -> 배열의 요소가 있는지 없는지를 확인하는 방법이다.
					// 데이터가 존재하는 경우
					$.each(json, function(index, item){  // $.each(배열명, function(index, item){})
						
						html += "<tr class='moreOQInfo'>"
							+ "<td class='seq_oq'>"+item.seq_oq+"</td>"
							+ "<td class='userid'>"+item.userid+"</td>"
							+ "<td>"+item.category+"</td>"
							+ "<td>"+item.title+"</td>"
							+ "<td>"+item.answerstatus+"</td>"
							+ "</tr>";
					});// end of $.each(json, function(index, item){})-----------------------------
					
					// HIT 상품 결과를 출력하기(차곡차곡 쌓는다)
					$("tbody#displayOneQuery").append(html);
					
					// ★★★ 중요 !!! 더보기 버튼의 value 속성에 값을 지정하기 ★★★ //
					$("button#btnMoreOneQuery").val(Number(start) + lenHIT);
					
					// countHIT 에 지금까지 출력된 상품의 개수를 누적하여 기록한다. -> 마지막 페이지를 확인하기 위해서이다.
					$("span#countHITOQ").text( Number($("span#countHITOQ").text()) + json.length);  // 현재 기록된 개수에 불러온 개수(배열의 길이 == 개수)를 더한다.
					
					// 더보기 버튼을 계속해서 클릭해 countHIT 값과 totalHITCount 값이 일치하는 경우
					if($("span#countHITOQ").text() == $("span#totalHITCountOQ").text()) {
						$("span#oqend").html("더 이상 조회할 문의가 없습니다.");
						$("button#btnMoreOneQuery").text("처음으로");
						$("span#countHITOQ").text("0");
					}// end of if($("span#countHIT").text() == $("span#totalHITCount").text()){}-----
				}// end of if(json.length > 0){}------------------------------
			},
			error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});// end of $.ajax({})-----------------------------
	}// end of function displayOneQuery(){}-----------------------------------
	
	// display 할 제품 문의 정보를 추가 요청하는 함수(Ajax 로 처리한다)
	function displayProductQA(start) {
		$.ajax({
			url:"<%= ctxPath%>/manager/displayProductQAJSON.sg",
		//	type:"GET",
			data:{"today":"<%= today%>"
				, "start":start
				, "len":lenHIT},
			dataType:"JSON",
			success:function(json){
				var html = "";
				
				if(json.length > 0) {  // 배열.length -> 배열의 요소가 있는지 없는지를 확인하는 방법이다.
					// 데이터가 존재하는 경우
					$.each(json, function(index, item){  // $.each(배열명, function(index, item){})
						html += "<tr class='morePQAInfo'>"
							+ "<td class='seq_qa'>"+item.seq_qa+"</td>"
							+ "<td class='userid'>"+item.userid+"</td>"
							+ "<td>"+item.fk_productid+"</td>"
							+ "<td>"+item.content+"</td>"
							+ "<td>"+item.status+"</td>"
							+ "</tr>";
					});// end of $.each(json, function(index, item){})-----------------------------
					
					// HIT 상품 결과를 출력하기(차곡차곡 쌓는다)
					$("tbody#displayProductQA").append(html);
					
					// ★★★ 중요 !!! 더보기 버튼의 value 속성에 값을 지정하기 ★★★ //
					$("button#btnMoreProdQA").val(Number(start) + lenHIT);
					
					// countHIT 에 지금까지 출력된 상품의 개수를 누적하여 기록한다. -> 마지막 페이지를 확인하기 위해서이다.
					$("span#countHITPQA").text( Number($("span#countHITPQA").text()) + json.length);  // 현재 기록된 개수에 불러온 개수(배열의 길이 == 개수)를 더한다.
					
					// 더보기 버튼을 계속해서 클릭해 countHIT 값과 totalHITCount 값이 일치하는 경우
					if($("span#countHITPQA").text() == $("span#totalHITCountPQA").text()) {
						$("span#pqaend").html("더 이상 조회할 문의가 없습니다.");
						$("button#btnMoreProdQA").text("처음으로");
						$("span#countHITPQA").text("0");
					}// end of if($("span#countHIT").text() == $("span#totalHITCount").text()){}-----
				}// end of if(json.length > 0){}------------------------------
			},
			error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});// end of $.ajax({})-----------------------------
	}// end of function displayProductQA(){}-----------------------------------
	
	// 요약 데이터 가져오기
	function summaryMember() {
		$.ajax({
			url:"/Logitech/manager/summaryMember.sg",
			dataType:"JSON",
			success:function(json){
				$("span#summaryMember").html(json.cnt);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function summaryProduct() {
		$.ajax({
			url:"/Logitech/manager/summaryProduct.sg",
			dataType:"JSON",
			success:function(json){
				$("span#summaryProduct").html(json.cnt);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function summaryPurchase() {
		$.ajax({
			url:"/Logitech/manager/summaryPurchase.sg",
			dataType:"JSON",
			success:function(json){
				$("span#summaryPurchase").html(json.cnt);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function summaryCoupon() {
		$.ajax({
			url:"/Logitech/manager/summaryCoupon.sg",
			dataType:"JSON",
			success:function(json){
				$("span#summaryCoupon").html(json.cnt);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	// 그래프 그리기
	/*
	function drawCurveTypes() {
		var data = new google.visualization.DataTable();
		data.addColumn('number', 'X');
		data.addColumn('number', 'Dogs');
		data.addColumn('number', 'Cats');
	
		data.addRows([
		  [0, 0, 0],    [1, 10, 5],   [2, 23, 15],  [3, 17, 9],   [4, 18, 10],  [5, 9, 5],
		  [6, 11, 3],   [7, 27, 19],  [8, 33, 25],  [9, 40, 32],  [10, 32, 24], [11, 35, 27],
		  [12, 30, 22], [13, 40, 32], [14, 42, 34], [15, 47, 39], [16, 44, 36], [17, 48, 40],
		  [18, 52, 44], [19, 54, 46], [20, 42, 34], [21, 55, 47], [22, 56, 48], [23, 57, 49],
		  [24, 60, 52], [25, 50, 42], [26, 52, 44], [27, 51, 43], [28, 49, 41], [29, 53, 45],
		  [30, 55, 47], [31, 60, 52], [32, 61, 53], [33, 59, 51], [34, 62, 54], [35, 65, 57],
		  [36, 62, 54], [37, 58, 50], [38, 55, 47], [39, 61, 53], [40, 64, 56], [41, 65, 57],
		  [42, 63, 55], [43, 66, 58], [44, 67, 59], [45, 69, 61], [46, 69, 61], [47, 70, 62],
		  [48, 72, 64], [49, 68, 60], [50, 66, 58], [51, 65, 57], [52, 67, 59], [53, 70, 62],
		  [54, 71, 63], [55, 72, 64], [56, 73, 65], [57, 75, 67], [58, 70, 62], [59, 68, 60],
		  [60, 64, 56], [61, 60, 52], [62, 65, 57], [63, 67, 59], [64, 68, 60], [65, 69, 61],
		  [66, 70, 62], [67, 72, 64], [68, 75, 67], [69, 80, 72]
		]);
	
		var options = {
			hAxis: {
				title: 'Time'
			},
			vAxis: {
				title: 'Popularity'
			},
			series: {
				1: {curveType: 'function'}
			}
		};
	
		var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
		chart.draw(data, options);
	}
		 */
	// 신규 회원 그래프
	function newMemberChart() {
		$.ajax({
			url:"/Logitech/manager/newMemberChart.sg",
			dataType:"JSON",
			success:function(json){
				google.charts.load('current', {packages: ['corechart', 'line']});
				google.charts.setOnLoadCallback(drawCurveTypes);
				
				function drawCurveTypes() {
					var data = new google.visualization.DataTable();
					data.addColumn('number', 'Date');
					data.addColumn('number', 'RegisterCnt');
				
					var outArr = [];
					$.each(json, function(index, item){
						var inArr = [];
						inArr.push(item.registerday);
						inArr.push(item.cnt);
						outArr.push(inArr);
					})
					
					data.addRows(outArr);
				
					var options = {
						hAxis: {
							title: 'Date'
						},
						vAxis: {
							title: 'RegisterCnt'
						},
						series: {
							1: {curveType: 'function'}
						}
					};
				
					var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
					
					var formatter = new google.visualization.NumberFormat({pattern: '######'});
					formatter.format(data, 1);
					
					chart.draw(data, options);
				}
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
		 
	// 막대 바
	function newMemberBar() {
		$.ajax({
			url:"/Logitech/manager/newMemberBar.sg",
			dataType:"JSON",
			success:function(json){
				// alert(json.cnt);
				var percent = json.cnt * 100 / 200;
				// alert(parseInt(percent));
				
				var html = "<div class='progress-bar' role='progressbar' aria-valuenow='"+json.cnt+"' aria-valuemin='0' aria-valuemax='200' style='width:"+percent+"%; background-color: #009999;'>" +
						percent+"%" +
						"</div>";
						
				$("div#newMemberBar").html(html);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function joinEventBar() {
		$.ajax({
			url:"/Logitech/manager/joinEventBar.sg",
			dataType:"JSON",
			success:function(json){
				// alert(json.cnt);
				var percent = json.cnt;
				// alert(parseInt(percent));
				
				var html = "<div class='progress-bar' role='progressbar' aria-valuenow='"+json.cnt+"' aria-valuemin='0' aria-valuemax='100' style='width:"+percent+"%; background-color: #009999;'>" +
						percent+"%" +
						"</div>";
						
				$("div#joinEventBar").html(html);
			},
			error:function(){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	// 재고 테이블
	function displayStock() {
		$.ajax({
			url:"<%= ctxPath%>/manager/displayStock.sg",
		//	type:"GET",
			dataType:"JSON",
			success:function(json){
				var html = "";
				
				if(json.length > 0) {  // 배열.length -> 배열의 요소가 있는지 없는지를 확인하는 방법이다.
					// 데이터가 존재하는 경우
					$.each(json, function(index, item){  // $.each(배열명, function(index, item){})
						html += "<tr>"
							+ "<td>"+item.productserialid+"</td>"
							+ "<td>"+item.stock+"</td>"
							+ "</tr>";
					});// end of $.each(json, function(index, item){})-----------------------------
					
					$("tbody#displayStock").html(html);
				}// end of if(json.length > 0){}------------------------------
			},
			error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});// end of $.ajax({})-----------------------------
	}// end of function displayProductQA(){}-----------------------------------
	
	function displayBest() {
		$.ajax({
			url:"<%= ctxPath%>/manager/displayBest.sg",
		//	type:"GET",
			dataType:"JSON",
			success:function(json){
				var html = "";
				
				if(json.length > 0) {  // 배열.length -> 배열의 요소가 있는지 없는지를 확인하는 방법이다.
					// 데이터가 존재하는 경우
					$.each(json, function(index, item){  // $.each(배열명, function(index, item){})
						html += "<tr>"
							+ "<td>"+(Number(index)+1)+"</td>"
							+ "<td>"+item.fk_productserialid+"</td>"
							+ "<td>"+item.sum+"</td>"
							+ "</tr>";
					});// end of $.each(json, function(index, item){})-----------------------------
					
					$("tbody#displayBest").html(html);
				}// end of if(json.length > 0){}------------------------------
			},
			error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});// end of $.ajax({})-----------------------------
	}
</script>

	<div style="width: 100%;">
		<div id="dashboard">Overview</div>
		
		<div id="divUl" style="background-color: white;">
			<div class="summary apply" style="display: inline-block; background-color: white;">
				<img src="/Logitech/images/icon_020-users.png" width="70px;" height="70px;" style="float: left;" />
				<div style="display: inline-block; background-color: white; margin: 0 10px;">회원 현황<br><span id="summaryMember" style="font-size: 28pt; font-weight: bolder; color: #009999"></span></div>
			</div>
			
			<div style="display: inline-block; width: 3px; height: 100px; background-color: #ccc; vertical-align: middle; margin-right: 50px;"></div>
			
			<div class="summary apply" style="display: inline-block; background-color: white;">
				<img src="/Logitech/images/icon_084-bar chart.png" width="70px;" height="70px;" style="float: left;" />
				<div style="display: inline-block; background-color: white; margin: 0 10px;">제품 현황<br><span id="summaryProduct" style="font-size: 28pt; font-weight: bolder; color: #009999"></span></div>
			</div>
			
			<div style="display: inline-block; width: 3px; height: 100px; background-color: #ccc; vertical-align: middle; margin-right: 50px;"></div>
			
			<div class="summary apply" style="display: inline-block; background-color: white;">
				<img src="/Logitech/images/icon_093-shopping cart.png" width="70px;" height="70px;" style="float: left;" />
				<div style="display: inline-block; background-color: white; margin: 0 10px;">주문 현황<br><span id="summaryPurchase" style="font-size: 28pt; font-weight: bolder; color: #009999"></span></div>
			</div>
			
			<div style="display: inline-block; width: 3px; height: 100px; background-color: #ccc; vertical-align: middle; margin-right: 50px;"></div>
			
			<div class="summary" style="display: inline-block; background-color: white;">
				<img src="/Logitech/images/icon_003-money.png" width="70px;" height="70px;" style="float: left;" />
				<div style="display: inline-block; background-color: white; margin: 0 10px;">쿠폰 현황<br><span id="summaryCoupon" style="font-size: 28pt; font-weight: bolder; color: #009999"></span></div>
			</div>
		</div>
		
		<div id="divUl" style="background-color: white;">
			<div id="divUnbox" style="float: left; background-color: white;">
				<span class="summaryTitle" style="font-size: 18pt;">신규 회원 현황</span><br>
		<%--		<img src="helloGraphics.jsp" width="300px;" height="200px;" /> --%>
				<div id="chart_div"></div>
			</div>
			
			<div id="currentState" style="display: inline-block; background-color: white;">
				<hr>
				<span class="summaryTitle" style="font-size: 15pt;">오늘의 신규 회원</span><br>
				<div class="progress" id="newMemberBar" style="margin-top: 40px;"></div>
				
				<span class="summaryTitle" style="font-size: 15pt;">이벤트 참여 현황</span><br>
				<div class="progress" id="joinEventBar" style="margin-top: 40px;"></div>
			</div>
			
			<div style="display: inline-block; width: 90%; height: 3px; background-color: #ccc; vertical-align: middle; margin: 30px 50px;"></div>
			
			<div style="background-color: white;">
				<div id="productStock" style="float: left; background-color: white;">
					<span class="summaryTitle" style="font-size: 18pt;">품절 상품</span><br>
					<hr>
					<table class="tblIndex" id="tblStock">
						<thead class="tHeadRow" align="center">
							<tr class="tHeadRow">
								<th>제품일련번호</th>
								<th>재고량</th>
							</tr>
						</thead>
						
						<tbody id="displayStock"></tbody>
					</table>
				</div>
				
				<div id="productBest" style="float: left; background-color: white; margin-left: 120px;">
					<span class="summaryTitle" style="font-size: 18pt;">인기 상품</span><br>
					<hr>
					<table class="tblIndex" id="tblBest">
						<thead class="tHeadRow" align="center">
							<tr class="tHeadRow">
								<th>순위</th>
								<th>제품일련번호</th>
								<th>판매량</th>
							</tr>
						</thead>
						
						<tbody id="displayBest"></tbody>
					</table>
				</div>
			</div>
			
			<div style="clear: both; background-color: white;"></div>
		</div>
		
		<div style="clear:both; float:left; width: 43%; margin: 0 0 50px 50px; background-color: white;">
			<br>
			<span class="summaryTitle" style="font-size: 18pt; margin: 0 30px;">일대일 문의 상황</span>
			<hr>
			<table class="tblIndex">
				<thead class="tHeadRow" align="center">
					<tr class="tHeadRow">
						<th>번호</th>
						<th>작성자</th>
						<th>카테고리</th>
						<th>제목</th>
						<th>답변상태</th>
					</tr>
				</thead>
				
				<tbody id="displayOneQuery"></tbody>
			</table>
	
			<div class="divTable" style="background-color: white;">
				<span id="oqend" style="font-size: 12pt; font-weight: bold; color: red;"></span><br/> 
				<button type="button" id="btnMoreOneQuery" value="">더 보기</button>
				<span id="totalHITCountOQ">${totalOQCount}</span>
				<span id="countHITOQ">0</span>
			</div>
		</div>
		
		<div style="float: left; width: 43%; margin: 0 0 50px 50px; background-color: white;">
			<br>	
			<span class="summaryTitle" style="font-size: 18pt; margin: 0 30px;">제품 문의 상황</span>
			<hr>
			<table class="tblIndex">
				<thead class="tHeadRow" align="center">
					<tr>
						<th>번호</th>
						<th>작성자</th>
						<th>제품아이디</th>
						<th>내용</th>
						<th>답변상태</th>
					</tr>
				</thead>
				
				<tbody id="displayProductQA"></tbody>
			</table>
	
			<div class="divTable" style="background-color: white;">
				<span id="pqaend" style="font-size: 12pt; font-weight: bold; color: red;"></span><br/> 
				<button type="button" id="btnMoreProdQA" value=""">더 보기</button>
				<span id="totalHITCountPQA">${totalPQACount}</span>
				<span id="countHITPQA">0</span>
			</div>
		</div>
	</div>

<!-- 일대일 문의 Modal -->
<div id="oneQueryModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">상세 문의 정보</h4>
      </div>
      <div class="modal-body oqmodal-body">
		
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="location.href='javascript:history.go(0);'">Close</button>
      </div>
    </div>

  </div>
</div>

<jsp:include page="managerFooter.jsp" />