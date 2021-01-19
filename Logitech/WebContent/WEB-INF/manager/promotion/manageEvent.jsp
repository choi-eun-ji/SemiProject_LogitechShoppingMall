<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../managerHeader.jsp" />

<style>
#eventState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

#searchOption1, #searchOption2 {
	height: 30px;
	margin-right: 10px;
}

#searchKey1, #searchKey2 {
	width: 150px;
	height: 30px;
	margin-right: 20px;
}

#btnSearch {
	margin-right: 20px;
}

#sizePerPage {
	height: 30px;
}

#tblEvent {
	width: 100%;
}

#tblEvent > thead > tr {
	background-color: #bfbfbf;
}

#tblEvent, #tblEvent tr, #tblEvent th, #tblEvent td {
	border: solid 1px #ccc;
	text-align: center;
}

#tblEvent th, #tblEvent td {
	height: 70px;
}

#tHeadRow > th:hover {
	background-color: #e6ffe6;
	cursor: pointer;
}

tr.eventInfo:hover {
	background-color: #e6e6e6;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("span.hiddenVal").hide();
		$("span.hiddenSort").hide();
	
		if("${sortFlag}" != null) {
			$(".hiddenSort").html("${sortFlag}");
		}
		
		if("${searchKey1}" != "") {  // 처음에는 검색어가 있지 않기 때문에 if 문으로 경우를 나눈다.
			$("select#searchOption1").val("${searchOption1}");
			$("input#searchKey1").val("${searchKey1}");
		}// end of if("${searchKey1}" != ""){}---------------------
		
		if("${searchKey2}" != "") {  // 처음에는 검색어가 있지 않기 때문에 if 문으로 경우를 나눈다.
			$("select#searchOption2").val("${searchOption2}");
			$("input#searchKey2").val("${searchKey2}");
		}// end of if("${searchKey1}" != ""){}---------------------
		
		var searchKey1 = $("input#searchKey1").val().trim();
		
		if(searchKey1 == "") {
			$("input#searchKey2").prop("disabled", true);
		}// end of if(searchKey1 == ""){}------------------------
		
		$("input#searchKey1").blur(function(){
			if($(this).val().trim() != "") {
				$("input#searchKey2").prop("disabled", false);
			} else {
				$("input#searchKey2").prop("disabled", true);
			}// end of if($(this).val().trim() != ""){}------------------------
		})// end of $("input#searchKey1").blur(function(){})------------------------
		
		$("input#searchKey1").keyup(function(event){
			if(event.keyCode == 13) {  // 검색어에서 엔터를 치면 검색되도록 한다.
				goSearch();
			}// end of if(event.keyCode == 13){}-----------------
		});// end of $("input#searchKey1").keyup(function(){})--------------------
		
		$("input#searchKey2").keyup(function(event){
			if(event.keyCode == 13) {  // 검색어에서 엔터를 치면 검색되도록 한다.
				goSearch();
			}// end of if(event.keyCode == 13){}-----------------
		});// end of $("input#searchKey2").keyup(function(){})--------------------
				
		$("select#sizePerPage").bind("change", function(){
			goSearch();
		});// end of $("select#sizePerPage").bind("change", function(){})-------------------
		
		if("${sizePerPage}" != "") {
			$("select#sizePerPage").val("${sizePerPage}");
		}// end of if("${sizePerPage}" != ""){}------------------------
		
		$("tr#tHeadRow > th").click(function(){
			var selectedHead = $(this).children(".hiddenVal").text();
			var sortFlag = $(this).children(".hiddenSort").text();
			
			if(sortFlag == "asc") {
				sortFlag = "desc";
			} else {
				sortFlag = "asc";
			}// end of if(sortFlag == asc){}--------------------
			
			location.href = '<%= ctxPath%>/manager/promotion/eventList.sg?currentPageNo=1&sizePerPage='+"${sizePerPage}"+'&searchOption1='+"${searchOption1}"+'&searchKey1='+"${searchKey1}"+'&searchOption2='+"${searchOption2}"+'&searchKey2='+"${searchKey2}"+'&selectedHead='+selectedHead+'&sortFlag='+sortFlag;
		});// end of $("tr#tHeadRow > th").click(function(){})----------------------
		
		$("tr.eventInfo > td.couponRow").click(function(){
			var seq_event = $(this).parent().find(".seq_event").text();
			var endday = $(this).parent().find(".endday").text();
			
			location.href = "<%= ctxPath%>/manager/promotion/eventOneDetail.sg?seq_event="+seq_event+"&endday="+endday+"&goBackURL=${goBackURL}";
		});// end of $("tr.ecouponInfo").click(function(){})----------------------
	});// end of $(document).ready(function(){})--------------------------
	
	function goSearch() {
		var frm = document.frmList;
		frm.action = "<%= ctxPath%>/manager/promotion/eventList.sg";
		frm.method = "get";
		frm.submit();
	}// end of function goSearch(){}------------------------
</script>

<form name="frmList">
	<div id="eventState">
		<div class="row">
			<div class="col-sm-12">
				<div class="well" style="background-color: white;">
					<h4>이벤트 현황</h4> 
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<div class="well divCell" style="background-color: white;">
					<select id="searchOption1" name="searchOption1">
						<option>옵션1</option>
						<option value="seq_event">이벤트번호</option>
						<option value="eventname">이벤트명</option>
						<option value="fk_productid">이벤트상품</option>
						<option value="startday">시작일자</option>
						<option value="endday">종료일자</option>
					</select>
					
					<input type="text" id="searchKey1" name="searchKey1" placeholder="1차 검색 조건" />
					
					<select id="searchOption2" name="searchOption2">
						<option>옵션2</option>
						<option value="seq_event">이벤트번호</option>
						<option value="eventname">이벤트명</option>
						<option value="fk_productid">이벤트상품</option>
						<option value="startday">시작일자</option>
						<option value="endday">종료일자</option>
					</select>
					
					<input type="text" id="searchKey2" name="searchKey2" placeholder="2차 검색 조건" />
					
					<button type="button" id="btnSearch" onclick="goSearch();">검색</button>
					
					<select id="sizePerPage" name="sizePerPage">
						<option value="5">5건씩 보기</option>
						<option value="10" selected="selected">10건씩 보기</option>
						<option value="20">20건씩 보기</option>
					</select>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<div class="well divContent" style="background-color: white;">
					<table id="tblEvent">
						<thead>
							<tr id="tHeadRow" align="center">
								<th>이벤트번호<span class="hiddenVal">seq_event</span><span class="hiddenSort">desc</span></th>
								<th>이벤트명<span class="hiddenVal">eventname</span><span class="hiddenSort">desc</span></th>
								<th>이벤트상품<span class="hiddenVal">fk_productid</span><span class="hiddenSort">desc</span></th>
								<th>시작일자<span class="hiddenVal">startday</span><span class="hiddenSort">desc</span></th>
								<th>종료일자<span class="hiddenVal">endday</span><span class="hiddenSort">desc</span></th>	
								<th>참여자 수<span class="hiddenVal">joincnt</span><span class="hiddenSort">desc</span></th>	
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="evo" items="${evoList}" varStatus="vstatus">
								<tr class="eventInfo">
									<td class="seq_event couponRow">${evo.seq_event}</td>
									<td class="couponRow">${evo.eventname}</td>
									<td class="couponRow">${evo.fk_productid}</td>
									<td class="couponRow">${evo.startday}</td>
									<td class="endday couponRow">${evo.endday}</td>
									<td class="couponRow">${evo.joincnt}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
					<div id="divBar" style="background-color: white; margin: 30px;">
						${pageBar}
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

<jsp:include page="../managerFooter.jsp" />