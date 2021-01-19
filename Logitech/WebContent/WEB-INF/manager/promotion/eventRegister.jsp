<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../managerHeader.jsp" />

<style>
#eventRegisterState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

#evRegisterDiv {
	width: 95%;
	text-align: left;
	border: solid 0px red;
	margin-top: 30px; 
	font-size: 13pt;
	line-height: 200%;
}

.star {
	color: red;
	font-weight: bold;
	font-size: 13pt;
}

.error {
	color: red;
	padding-left: 10px;
	margin-bottom: 5px;
}

span.myli {
	display: inline-block;
	width: 200px;
	border: solid 0px blue;
	margin-top: 20px;
}

#tblEventRegister {
	width: 100%;
	height: 500px;
	margin: 20px 25px;
}

#tblEventRegister > tr > td {
	height: 100px;
}

.popover {
    max-width: 100%;   
}
</style>

<script type="text/javascript">
	var productPreviewCnt = 0;
	var carouselPreviewCnt = 0;

	$(document).ready(function(){
		$("span.error").hide();
		
		$("input#eventname").focus();
		
		// 이벤트명 유효성 검사
		$("input#eventname").blur(function(){
			var eventname = $(this).val().trim();
			
			if(eventname.length < 5 || eventname.length > 25) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		
		// 이벤트 이미지 미리보기와 유효성 검사
		$("input#carouselimg").blur(function(){
			var carouselimg = $(this).val().trim();
			
			if(carouselimg != "") {
				var img = '<img src="'+carouselimg+'" />';
				var html = "<a href='#' id='carouselPreview' data-toggle='popover' data-html='true' data-content='"+img+"'>미리보기</a>";
				$(this).parent().find(".error").hide();
			} else {
				$(this).parent().find(".error").show();
				$(this).focus();
				return;
			}
			
			$("span#carouselPreview").html(html);
			
			$('[data-toggle="popover"]').popover();
			
			$("a#carouselPreview").click(function(){
				carouselPreviewCnt++;
			});
		});
				
		$("select#fk_productid").change(function(){
			var productid = $(this).val();
			
			if(productid != "") {
				$.ajax({
					url:"<%= ctxPath%>/manager/productImg.sg",
					type:"GET",
					async: false,
					data:{"productid":productid},
					dataType:"JSON",
					success:function(json){
						var img = '<img src="'+json.imgfilename+'" />';
						html = "<a href='#' id='productPreview' data-toggle='popover' data-html='true' data-content='"+img+"'>미리보기</a>";
						// 선택한 상품 이미지 미리보기 popover 효과
						
						$("span#productidResult").html(html);
						
						$('[data-toggle="popover"]').popover();
						
						$("a#productPreview").click(function(){
							productPreviewCnt++;
						});
					},
					error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
			               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
				});
			}
		});
		
		// datepicker
		$(function() {
            //모든 datepicker에 대한 공통 옵션 설정
            $.datepicker.setDefaults({
	            dateFormat: 'yy-mm-dd' //Input Display Format 변경
	            ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
	            ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
	            ,changeYear: true //콤보박스에서 년 선택 가능
	            ,changeMonth: true //콤보박스에서 월 선택 가능                
	            ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
	            ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
	            ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
	            ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
	            ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
	            ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
	            ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
	            ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
	            ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
	            // ,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
	            // ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
            });
   
            //input을 datepicker로 선언
            $("#startday").datepicker();                    
            $("#endday").datepicker();
              
            //From의 초기값을 오늘 날짜로 설정
            $('#startday').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
            //To의 초기값을 내일로 설정
            $('#endday').datepicker('setDate', '+1M'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
		});// end of $(function(){})------------------------
		
		// 이벤트 시작일자와 종료일자 유효성 검사
		$("input#startday").blur(function(){
			var startday = $(this).val().trim();
			
			var regExp = new RegExp(/^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$/);
			var bool = regExp.test(startday);
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		$("input#endday").blur(function(){
			var endday = $(this).val().trim();
			
			var regExp = new RegExp(/^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$/);
			var bool = regExp.test(endday);
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
	});// end of $(document).ready(function(){})-----------------------------
	
	function goProductidFind() {
		var category = $("select[name=category]").val();
		// alert(category);
		$.ajax({
			url:"<%= ctxPath%>/manager/promotion/productByCategory.sg",
			type:"GET",
			data:{"category":category},
			dataType:"JSON",
			success:function(json){
				var html = "<option value=''>선택해주세요</option>";
				
				if (json.length > 0) {    
					$.each(json, function(index, item){
					  html +=  "<option id='"+item.productid+"'>"+item.productid+"</option>";     
					}); 
				}// end of if -----------------------
				
				$("select#fk_productid").html(html);
			},
			error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
	               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});
	}
	
	function goEvRegister() {
		// 제품 이미지 미리보기 확인하기 //
		if(productPreviewCnt == 0) {
			alert("제품 아이디 미리보기를 확인해주세요.");
			return;
		}
		
		// 이벤트 이미지 미리보기 확인하기 //
		if(carouselPreviewCnt == 0) {
			alert("이벤트 이미지를 등록하기 위해 미리보기를 확인해주세요.");
			return;
		}
		
		// 필수입력사항 모두가 입력되었는지 검사하기 //
		var bFlagRequiredInfo = false;
		$(".requiredInfo").each(function(){
			var data = $(this).val().trim();
			if(data == "") {
				bFlagRequiredInfo = true;
				alert("* 표시의 필수입력사항을 모두 입력해주세요.");
				return false;  // break
			}// end of if(data == ""){}----------------
		});// end of $(".requiredInfo").each(function(){})--------------------
		
		if(!bFlagRequiredInfo) {
			var frm = document.registerFrm;
			frm.action = "<%= ctxPath%>/manager/promotion/eventRegister.sg";
			frm.method = "post";
			frm.submit();
		}// end of if(!bFlagRequiredInfo){}------------------
	}
</script>

<div id="eventRegisterState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>이벤트 등록</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<form name="registerFrm">
					<h3 style="font-weight: bold; margin-left: 40px;">이벤트 등록 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>)</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="evRegisterDiv" style="background-color: white;">
						<table id="tblEventRegister">
							<tbody>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										이벤트명&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="eventname" id="eventname" class="requiredInfo" />
										<span class="error">5자~25자 사이의 이벤트명을 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품카테고리&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<select name="category">
											<c:forEach var="pcvo" items="${pcvoList}">
												<option>${pcvo.category}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품아이디&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="productidFind" onclick="goProductidFind();">검색하기</button>
										<select name="fk_productid" id="fk_productid">
										</select>
										<span id="productidResult"></span>
										<span class="error">제품아이디를 선택해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										시작일&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="startday" id="startday" class="requiredInfo" />&nbsp;&nbsp;
										<span class="error">이벤트 시작일을 선택해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										종료일&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="endday" id="endday" class="requiredInfo" />
										<span class="error">이벤트 종료일을 선택해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										이벤트 관련 이미지&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="carouselimg" id="carouselimg" class="requiredInfo" />
										<span id="carouselPreview"></span>
										<span class="error">이미지 경로를 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="evRegisterBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goEvRegister();">이벤트 등록</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../managerFooter.jsp" />