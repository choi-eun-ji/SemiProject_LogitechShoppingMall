<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../managerHeader.jsp" />

<style>
#managerRState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

#managerUpdateDiv {
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

#tblManagerUpdate {
	width: 100%;
	height: 500px;
	margin: 20px 25px;
}

#tblManagerUpdate > tr > td {
	height: 100px;
}
</style>

<script type="text/javascript">
//	var flagIdDuplicateClick = false;

	$(document).ready(function(){
		$("span.error").hide();
		
		$("input#updateid").focus();
		
		// 아이디가 name인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#updateid").blur(function(){
			
			var managerid = $(this).val();
			
			var regExp = new RegExp(/^[A-Za-z0-9]{5,12}$/g);
			// 영문자, 숫자 형태의 5~12 자리의 아이디 정규표현식 객체 생성
			var bool = regExp.test(managerid);
			
			if(!bool) {
				// 입력하지 않거나 공백만 입력한 경우
				$(this).parent().find(".error").show();
				$(this).focus();
			} else if(managerid == 'adminsg') {
				$(this).parent().find(".error").show();
				$(this).val("");
				$(this).focus();
			} else {
				// 공백이 아닌 글자를 제대로 입력한 경우
				$(this).parent().find(".error").hide();
			}// end of if(managerid == ""){}-----------------
			
		});// end of $("input#name").blur(function(){})-------------------
		
		// 아이디가 managerpwd인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#managerpwd").blur(function(){
			
			var pwd = $(this).val();
			
			// var regExp = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			// 또는
			var regExp = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
			// 숫자/문자/특수문자/ 포함 형태의 8~15자리 이내의 암호 정규표현식 객체 생성
			var bool = regExp.test(pwd);
			
			if(!bool) {
				// 암호가 정규표현식에 위배된 경우
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				// 암호가 정규표현식에 맞는 경우
				$(this).parent().find(".error").hide();
			}// end of if(!bool){}-----------------
			
		});// end of $("input#pwd").blur(function(){})-------------------
		
		
		// 아이디가 pwdcheck인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#pwdcheck").blur(function(){
			
			var managerpwd = $("input#managerpwd").val();
			var pwdcheck = $(this).val();
			
			if(managerpwd != pwdcheck) {
				// 암호와 암호확인 값이 다른 경우
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				// 암호와 암호확인 값이 같은 경우
				$(this).parent().find(".error").hide();
			}// end of if(managerpwd != pwdcheck){}-----------------
			
		});// end of $("input#pwdcheck").blur(function(){})-------------------
		
		
		// 아이디가 manageremail인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#manageremail").blur(function(){
			
			var manageremail = $(this).val();
			
			var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			var bool = regExp.test(manageremail);
			
			if(!bool) {
				// 이메일 형식에 맞지 않는 경우
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				// 이메일 형식에 맞는 경우
				$(this).parent().find(".error").hide();
			}// end of if(!bool){}-----------------
			
		});// end of $("input#manageremail").blur(function(){})-------------------
		
		
		// 아이디가 managermobile인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#managermobile").blur(function(){
			
			var managermobile = $(this).val();
			
			var regExp = /^\d{8,15}$/g;
			var bool = regExp.test(managermobile);
			
			if(!bool) {
				// 가운데 번호(국번) 형식에 맞지 않는 경우
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				// 가운데 번호 형식에 맞는 경우
				$(this).parent().find(".error").hide();
			}// end of if(!bool){}-----------------
			
		});// end of $("input#managermobile").blur(function(){})-------------------
		
     	// 관리자 아이디 확인하기 //
		$("button#manageridCheck").click(function(){
			flagIdExistClick = true;
			
			$.ajax({
				url:"<%= ctxPath%>/manager/manager/idExistCheck.sg",
				type:"post",
				data:{"updateid":$("input#updateid").val()},
				success:function(text){  // text 는 dataType:"json"을 생략하면 자바스크립트 객체(JSON)로 인식하지 않고 받아온 결과물 그대로 String 타입의 {"isExists":true} 또는 {"isExists":false} 로 인식한다.
					var json = JSON.parse(text);
					// JSON.parse(text); 은 JSON 형식으로 되어진 문자열을 자바스크립트 객체로 변환해주는 것이다.
		            // 조심할 것은 text 는 반드시 JSON 형식으로 되어진 문자열이어야 한다.
		            
					if(!json.isExists) {  // json.isExists --> 변수.key값
						// 수정하고자 하는 아이디가 없는 경우
						$("span#manageridCheckResult").html("없는 계정입니다.").css("color","red");
					//	$("span.error").show().css("color","red");
						$("input#updateid").val("");
					} else {
						// 입력한 userid 가 tbl_member 테이블에 존재하지 않는 경우
					//	alert($("input#userid").val() + " 은(는) 사용가능합니다.");
						$("span#manageridCheckResult").html("수정 가능합니다.").css("color","green");
					//	$("span.error").hide().css("color","green");
						$("option[value="+json.managertype+"]").prop("selected", true);
						$("input#manageremail").val(json.manageremail);
						$("input#managermobile").val(json.managermobile);
					}// end of if(json.isExists){}-------------------
				},
				error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});
	});// end of $(document).ready(function(){})-----------------------------
	
	function goUpdate() {
		// 아이디 중복 검사 확인하기 //
		if(!flagIdExistClick) {
			alert("수정할 아이디를 확인해주세요.");
			return;
		}
		
		// 필수입력사항 모두가 입력되었는지 검사하기 //
		var bFlagRequiredInfo = false;
		
		$(".requiredInfo").each(function(){
			var data = $(this).val();
			if(data == "") {
				bFlagRequiredInfo = true;
				alert("* 표시의 필수입력사항을 모두 입력해주세요.");
				return false;  // break
			}// end of if(data == ""){}----------------
		});// end of $(".requiredInfo").each(function(){})--------------------
		
		if(!bFlagRequiredInfo) {
			var frm = document.updateFrm;
			frm.action = "<%= ctxPath%>/manager/manager/managerUpdate.sg";
			frm.method = "POST";
			frm.submit();
		}// end of if(!bFlagRequiredInfo){}------------------
	}// end of function goUpdate(){}----------------------------
</script>

<div id="managerRState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>관리자 등록</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<form name="updateFrm">
					<h3 style="font-weight: bold; margin-left: 40px;">관리자 수정 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>)</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="managerUpdateDiv" style="background-color: white;">
						<table id="tblManagerUpdate">
							<c:choose>
								<c:when test="${sessionScope.loginManager.managerno == 1}">
									<tr>
										<td style="width: 30%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
										<td style="width: 70%; text-align: left;">
											<input type="hidden" name="managerid" id="managerid" value="${sessionScope.loginManager.managerid}" class="requiredInfo" required />
											<input type="text" name="updateid" id="updateid" class="requiredInfo" required />
											<button type="button" id="manageridCheck">계정 확인</button>
											<span id="manageridCheckResult"></span><br>
											<span class="error">수정할 수 없는 아이디입니다.</span>
										</td>
									</tr>
								</c:when>
								
								<c:otherwise>
									<tr>
										<td style="width: 30%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
										<td style="width: 70%; text-align: left;">
											<input type="text" name="managerid" id="managerid" value="${sessionScope.loginManager.managerid}" class="requiredInfo" required disabled="disabled" />
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
							<tr>
								<td style="width: 30%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
								<td style="width: 70%; text-align: left;">
									<input type="password" name="managerpwd" id="managerpwd" class="requiredInfo" required />
									<span class="error">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로만 입력가능합니다.</span>
								</td>
							</tr>
							<tr>
								<td style="width: 30%; font-weight: bold;">비밀번호확인&nbsp;<span class="star">*</span></td>
								<td style="width: 70%; text-align: left;">
									<input type="password" id="pwdcheck" class="requiredInfo" required /> 
									<span class="error">암호가 일치하지 않습니다.</span>
								</td>
							</tr>
							
							<c:choose>
								<c:when test="${sessionScope.loginManager.managerno == 1}">
									<tr>
										<td style="width: 30%; font-weight: bold;">관리 범위</td>
										<td style="width: 70%; text-align: left;">
											<select name="managertype" id="managertype" class="requiredInfo">
												<option>전체</option>
												<option>회원</option>
												<option>제품</option>
												<option>주문</option>
												<option>판촉</option>
											</select>
										</td>
									</tr>
								</c:when>
								
								<c:otherwise>
									<tr>
										<td style="width: 30%; font-weight: bold;">관리 범위</td>
										<td style="width: 70%; text-align: left;">
											<select name="managertype" id="managertype" class="requiredInfo" disabled="disabled">
												<option <c:if test="${sessionScope.loginManager.managertype == '전체'}">selected</c:if>>전체</option>
												<option <c:if test="${sessionScope.loginManager.managertype == '회원'}">selected</c:if>>회원</option>
												<option <c:if test="${sessionScope.loginManager.managertype == '제품'}">selected</c:if>>제품</option>
												<option <c:if test="${sessionScope.loginManager.managertype == '주문'}">selected</c:if>>주문</option>
												<option <c:if test="${sessionScope.loginManager.managertype == '판촉'}">selected</c:if>>판촉</option>
											</select>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
							
							<tr>
								<td style="width: 30%; font-weight: bold;">담당 이메일&nbsp;<span class="star">*</span></td>
								<td style="width: 70%; text-align: left;">
									<input type="text" name="manageremail" id="manageremail" value="${sessionScope.loginManager.manageremail}" class="requiredInfo" placeholder="admin@squadg.com" /> 
									<span class="error">이메일 형식에 맞지 않습니다.</span>
								</td>
							</tr>
							<tr>
								<td style="width: 30%; font-weight: bold;">담당 전화번호</td>
								<td style="width: 70%; text-align: left;">
									<input type="text" id="managermobile" name="managermobile" value="${sessionScope.loginManager.managermobile}" size="15" maxlength="15" />&nbsp;&nbsp;
									<span class="error">전화번호 형식이 아닙니다.</span>
								</td>
							</tr>
							<tr>
								<td style="width: 30%; font-weight: bold;">
								</td>
								<td style="width: 70%; text-align: left;">
									<button type="button" id="managerUpdateBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goUpdate();">관리자 수정</button>
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../managerFooter.jsp" />