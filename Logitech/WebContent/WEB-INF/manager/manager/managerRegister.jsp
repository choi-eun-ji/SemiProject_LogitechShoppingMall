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

#managerRegisterDiv {
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

#tblManagerRegister {
	width: 100%;
	height: 500px;
	margin: 20px 25px;
}

#tblManagerRegister > tr > td {
	height: 100px;
}
</style>

<script type="text/javascript">
//	var flagIdDuplicateClick = false;

	$(document).ready(function(){
		$("span.error").hide();
		
		$("input#managerid").focus();
		
		// 아이디가 name인 것은 포커스를 잃어버렸을 경우 blur 이벤트를 처리해준다.
		$("input#managerid").blur(function(){
			
			var managerid = $(this).val();
			
			var regExp = new RegExp(/^[A-Za-z0-9]{5,12}$/g);
			// 영문자, 숫자 형태의 5~12 자리의 아이디 정규표현식 객체 생성
			var bool = regExp.test(managerid);
			
			if(!bool) {
				// 입력하지 않거나 공백만 입력한 경우
				$(this).parent().find(".error").show();
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
		
     	// 아이디 중복 검사하기 //
		$("button#manageridCheck").click(function(){
			flagIdDuplicateClick = true;  // 가입하기 버튼 클릭 시 "아이디중복확인"을 클릭했는지 여부를 알아보기 위한 용도의 boolean형 변수
			
			$.ajax({
				url:"<%= ctxPath%>/manager/manager/idDuplicateCheck.sg",
				type:"post",
				data:{"managerid":$("input#managerid").val()},
				success:function(text){
					var json = JSON.parse(text);
		            
					if(json.isExists) {
						// 입력한 userid 가 이미 사용 중인 경우
						$("span#manageridCheckResult").html($("input#managerid").val() + " 은(는) 이미 존재하는 아이디입니다.").css("color","red");
						$("input#managerid").val("");
					} else {
						// 입력한 userid 가 tbl_member 테이블에 존재하지 않는 경우
						$("span#manageridCheckResult").html($("input#managerid").val() + " 은(는) 사용 가능합니다").css("color","green");
					}// end of if(json.isExists){}-------------------
				},
				error:function(request, status, error){
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});// end of $("button#manageridCheck").click(function(){})---------------------
	});// end of $(document).ready(function(){})-----------------------------
	
	function goRegister() {
		// 아이디 중복 검사 확인하기 //
		if(!flagIdDuplicateClick) {
			alert("아이디중복확인을 클릭하여 ID중복검사를 해주세요.");
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
			var frm = document.registerFrm;
			frm.action = "<%= ctxPath%>/manager/manager/managerRegister.sg";
			frm.method = "POST";
			frm.submit();
		}// end of if(!bFlagRequiredInfo){}------------------
	}// end of function goRegister(){}----------------------------
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
				<form name="registerFrm">
					<h3 style="font-weight: bold; margin-left: 40px;">관리자 등록 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>)</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="managerRegisterDiv" style="background-color: white;">
						<table id="tblManagerRegister">
							<tbody>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										관리자 아이디&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="managerid" id="managerid" class="requiredInfo" />
										<button type="button" id="manageridCheck">관리자 아이디 중복확인</button>
										<span id="manageridCheckResult"></span>
										<span class="error">영문자 또는 숫자 5~12 자리로 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										관리자 비밀번호&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="password" name="managerpwd" id="managerpwd" class="requiredInfo" />
										<span class="error">영문자, 숫자, 특수문자를 포함한 8~15자리로 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										비밀번호 확인&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="password" name="pwdcheck" id="pwdcheck" class="requiredInfo" />
										<span class="error">비밀번호가 일치하지 않습니다.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										관리 범위&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<select name="managertype" id="managertype" class="requiredInfo">
											<option>전체</option>
											<option>회원</option>
											<option>제품</option>
											<option>주문</option>
											<option>판촉</option>
										</select>
										<span class="error"></span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										담당 이메일&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="manageremail" id="manageremail" class="requiredInfo" />
										<span class="error">올바른 메일을 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										담당 전화번호&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="managermobile" id="managermobile" class="requiredInfo" placeholder="- 없이 숫자만 입력" />
										<span class="error">올바른 전화번호를 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="managerRegisterBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goRegister();">관리자 등록</button>
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