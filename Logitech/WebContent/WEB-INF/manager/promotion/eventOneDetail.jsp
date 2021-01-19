<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

div#eventInfo {
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
	font-weight: bold;
	margin-top: 20px;
}
</style>

<script type="text/javascript">
	var cnt = 0;
	var result = 0;
	var html = "";
	
	var seq_joinevent = "";
	var userid = "";
	
	var goBackURL = "";

	$(document).ready(function(){
		$("span.eventIndex").hide();
		
		var flag = false;
		
		$("span.winUserid").each(function(){
			if($("span.winUserid").text().trim() != null) {
				flag = true;
				return;
			}
		});
		
		if(flag) {
			$("button#btnSelect").hide();
			$("button#btnRandom").hide();
		}
		
		$("span.winUserid").each(function(){
			var winUserid = "";
			winUserid += $(this).text().trim();
			
			if(winUserid != "") {
				var html2 = "<h3 style='width:100%; height: 50px; background-color: #999; padding-top: 13px;'>당첨자 목록</h3><br>";
				$("div#winnerTitle").html(html2);
			}
		});
		
		goBackURL = "${goBackURL}";
		
		goBackURL = goBackURL.replace(/ /gi, "&");
	});// end of $(document).ready(function(){})-----------------------------
	
	function goEventList() {
		location.href = "/Logitech/"+goBackURL;
	}// end of function goMemberList(){}-----------------------------
	
	function eventUpdate() {
		$.ajax({
			url:"/Logitech/manager/promotion/eventSelectWinner.sg",
			type:"POST",
			async:false,
			data:{"seq_joinevent":seq_joinevent,
				"userid":userid},
			dataType:"JSON",
			success:function(json){
				result += json.n;
				
				html += "<span style='display: inline-block; font-size: 14pt; font-weight: bold; margin: 15px;'>"+json.userid+"</span><br>";
				// alert(result);
			//	$("div#deliveryResult").html("선택한 상품 배송이 처리되었습니다.");
			
				var html2 = "<h3 style='width:100%; height: 50px; background-color: #999; padding-top: 13px;'>당첨자 목록</h3><br>";
				$("div#winnerTitle").html(html2);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function goWinnerSelect() {
		$("#btnRandom").hide();
		
		cnt = 0;
		result = 0;
	//	html = "<h3 style='width:100%; height: 50px; background-color: #999; padding-top: 13px;'>당첨자 목록</h3><br>";
		
		$("input[name=winnerSelect]:checked").each(function(index){
			cnt++;
			seq_joinevent = $(this).parent().find("#seq_joinevent"+index).text();
			userid = $(this).parent().find("#userid"+index).text();
			alert(userid);
			
			eventUpdate();
		});
		
		if(cnt == result) {
			$("div#resultWinner").html(html);
		}
	}
	
	function goWinnerRandom() {
		$("#btnSelect").hide();
		
		var joinCnt = ${jevoList.size()};
	//	html = "<h3 style='width:100%; height: 50px; background-color: #999; padding-top: 13px;'>당첨자 목록</h3><br>";
		
		$.ajax({
			url:"/Logitech/manager/promotion/eventRandomFunction.sg",
			type:"GET",
			async: false,
			data:{"joinCnt":joinCnt},
			dataType:"JSON",
			success:function(json){
				if(json.length > 0) {
					$.each(json, function(index, item){
						cnt++;
						seq_joinevent = $("span#"+item.n).parent().parent().find("#seq_joinevent"+item.n).text();
						userid = $("span#"+item.n).parent().parent().find("#userid"+item.n).text();

						eventUpdate();
					});
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		   }
		});
		
		if(cnt == result) {
			$("div#resultWinner").html(html);
		}
	}
	
	
</script>

<div id="eventState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>이벤트 정보</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<c:if test="${jevoList.size() == 0}">
					존재하지 않는 이벤트입니다.<br>
				</c:if>
				
				<c:if test="${jevoList.size() != 0}">
					<h3 style='width:100%; height: 50px; background-color: #999; padding-top: 13px;'>이벤트 상세 정보</h3>
					<c:choose>
						<c:when test="${jevoList.get(0).seq_joinevent == 0}">
							<div id="eventInfo" style="background-color: white;">
							<br><br>
							<span style="font-size: 18pt; font-weight: bold; margin: 420px;">이벤트 참여자가 없습니다.</span><br>
							</div>
						</c:when>
						
						<c:otherwise>
							<div id="eventInfo" style="background-color: white;">
								<c:forEach var="jevo" items="${jevoList}" varStatus="vstatus">
									<c:if test="${result >= 0}">
										<input type="checkbox" name="winnerSelect" style="float: left; margin: 105px 50px;" />
									</c:if>
									<ol style="float: left; margin: 20px;">	
										<li><span class="myli">이벤트참여번호 : </span><span id="seq_joinevent${vstatus.index}">${jevo.seq_joinevent}</span><span class="eventIndex" id="${vstatus.index}">${vstatus.index}</span></li>
										<li><span class="myli">참여회원 : </span><span id="userid${vstatus.index}">${jevo.mvo.userid}</span></li>
										<li><span class="myli">참여이유 : </span>${jevo.eventcomment}</li>
									</ol>
									<div style='clear: both; width: 100%; height: 3px; background-color: #b3b3b3;'></div>
								</c:forEach>
							</div>
							
							<div style="background-color: white; margin-bottom: 30px;">
								<div id="winnerTitle" style="background-color: white;"></div>
								<div id="resultWinner" style="background-color: white; margin-bottom: 30px;">
									<c:forEach var="jevo" items="${jevoList}" varStatus="vstatus">
										<c:if test="${jevo.winstatus == 1}">
											<span class='winUserid' style='display: inline-block; font-size: 14pt; font-weight: bold; margin: 15px;'>${jevo.mvo.userid}</span><br>
										</c:if>
									</c:forEach>
								</div>
							</div>
							
							<div style="background-color: white; margin: 50px 100px;">
								<c:if test="${result >= 0}">
									<button type="button" id="btnSelect" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="javascript:goWinnerSelect();">선택 추첨하기</button>
									<button type="button" id="btnRandom" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="javascript:goWinnerRandom();">무작위 추첨하기</button>
								</c:if>
							</div>
						</c:otherwise>
					</c:choose>
					
					<div style="background-color: white; margin: 50px 100px;">
						<button type="button" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="javascript:goEventList();">이벤트 목록으로 이동</button>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../managerFooter.jsp" />