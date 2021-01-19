<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../managerHeader.jsp" />

<style>
#couponRegisterState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

#cpRegisterDiv {
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

#tblCouponRegister {
	width: 100%;
	height: 500px;
	margin: 20px 25px;
}

#tblCouponRegister > tr > td {
	height: 100px;
}
</style>

<script type="text/javascript">
	var flagCodeDuplicateClick = false;
	
	$(document).ready(function(){
		$("span.error").hide();
		
		$("input#couponcode").focus();
		
		// 쿠폰 코드 유효성 검사
		$("input#couponcode").blur(function(){
			var couponcode = $(this).val().trim();
			
			var regExp = new RegExp(/^[a-zA-Z0-9- ]{3,20}$/g);
			var bool = regExp.test(couponcode)
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		// 쿠폰명 유효성 검사
		$("input#couponname").blur(function(){
			var couponname = $(this).val().trim();
			
			if(couponname.length < 5 || couponname.length > 25) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		// 할인액 유효성 검사 및 할인액 기준 최소사용금액 표시
		$("input#discount").blur(function(){
			var discount = $(this).val().trim();
			
			var regExp = new RegExp(/^[0-9]{1,2}[05]{1}[0]{2}$/);
			var bool = regExp.test(discount);
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
				$("input#minprice").val(Number(discount) * 10);
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		// 최소사용금액 유효성 검사
		$("input#minprice").blur(function(){
			var minprice = $(this).val().trim();
			
			var regExp = new RegExp(/^[0-9]{1,2}[05]{1}[0]{3}$/);
			var bool = regExp.test(minprice);
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).val("");
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}// end of if(minprice == ""){}------------------------------
		});// end of $("input#minprice").blur(function()})-------------------------
		
		// 쿠폰 코드 중복확인 검사하기
		$("button#codeCheck").click(function(){
			flagCodeDuplicateClick = true;
			
			$.ajax({
				url:"<%= ctxPath%>/manager/promotion/codeDuplicateCheck.sg",
				data:{"couponcode":$("input#couponcode").val()},  // data 는 /MyMVC/member/idDuplicateCheck.up로 전송해야할 데이터를 말한다.
				type:"post",
			//	dataType:"json",  // JavaScript Standard Object Notation. dataType 은  /MyMVC/member/idDuplicateCheck.up로부터 실행된 결과물을 받아오는 데이터타입을 말한다.
				success:function(text){  // text 는 dataType:"json"을 생략하면 자바스크립트 객체(JSON)로 인식하지 않고 받아온 결과물 그대로 String 타입의 {"isExists":true} 또는 {"isExists":false} 로 인식한다.
					var json = JSON.parse(text);
					// JSON.parse(text); 은 JSON 형식으로 되어진 문자열을 자바스크립트 객체로 변환해주는 것이다.
		            // 조심할 것은 text 는 반드시 JSON 형식으로 되어진 문자열이어야 한다.
		            
					if(json.isExists) {  // json.isExists --> 변수.key값
						// 입력한 userid 가 이미 사용 중인 경우
					//	alert($("input#userid").val() + " 은(는) 이미 사용 중인 아이디이므로 사용할 수 없습니다.");
						$("span#codeCheckResult").html($("input#couponcode").val() + " 은(는) 이미 등록한 쿠폰 코드입니다.").css("color","red");
						$("input#couponcode").val("");
					} else {
						// 입력한 userid 가 tbl_member 테이블에 존재하지 않는 경우
					//	alert($("input#userid").val() + " 은(는) 사용가능합니다.");
						$("span#codeCheckResult").html($("input#couponcode").val() + " 은(는) 사용 가능합니다.").css("color","green");
					}// end of if(json.isExists){}-------------------
				},
				error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});// end of $("img#idcheck").click(function(){})---------------------
	});// end of $(document).ready(function(){})-----------------------------
	
	function goCpRegister() {
		// 아이디 중복 검사 확인하기 //
		if(!flagCodeDuplicateClick) {
			alert("쿠폰 코드 중복확인을 해주세요.");
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
			frm.action = "<%= ctxPath%>/manager/promotion/couponRegister.sg";
			frm.method = "post";
			frm.submit();
		}// end of if(!bFlagRequiredInfo){}------------------
	}// end of function goCpRegister() {}------------------------
</script>

<div id="couponRegisterState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>쿠폰 등록</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<form name="registerFrm">
					<h3 style="font-weight: bold; margin-left: 40px;">쿠폰 등록 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>)</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="cpRegisterDiv" style="background-color: white;">
						<table id="tblCouponRegister">
							<tbody>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										쿠폰 코드&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="couponcode" id="couponcode" class="requiredInfo" />
										<button type="button" id="codeCheck">쿠폰 코드 중복확인</button>
										<span id="codeCheckResult"></span>
										<span class="error">영문자, 숫자, - 를 결합한 10자~20자 코드를 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										쿠폰명&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="couponname" id="couponname" class="requiredInfo" />
										<span class="error">5자~25자 사이의 쿠폰명을 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										할인액&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="discount" id="discount" class="requiredInfo" />&nbsp;&nbsp;
										<span class="error">5자리 이내, 백 단위부터는 000 또는 500 의 할인액을 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										최소사용금액&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="minprice" id="minprice" class="requiredInfo" />
										<span class="error">최소사용금액을 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										적용회원등급&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<select name="fk_membershipname" id="fk_membershipname" class="requiredInfo" style="width: 130px; height: 40px;">
											<c:forEach var="msvo" items="${msvoList}">
												<option>${msvo.membershipname}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="cpRegisterBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goCpRegister();">쿠폰 등록</button>
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