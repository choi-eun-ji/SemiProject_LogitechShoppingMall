<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../header.jsp" />
<style>
	ul#upul > li{
		display: block;
	}
	
	.updatelab {
		background-color: #f4f4f0;
		margin-right: 20px;
	}
	h6 {
		font-size: 12pt;
		text-align: center;
		margin-bottom: 50px;
	}
	th {
		padding-bottom: 20px;
	}
	td {
		padding: 10px;
		padding-right: 80px;
	}
	.second {
		border: solid 1px #e9e9e2;
		width: 100px;
	}
	input {
		border: solid 1px #d9d9d9;
	}
	
	#btn {
		padding: 20px;
		text-align: center;
	}
	label {
		color: red;
	}
	#idbtn {
		width:90px;
		font-size: 5pt;
		border: solid 1px gray;
		 border-radius: 5px;
		 background-color: white;
	}
	.btns {
		background-color: #deded3;
		border-radius: 8px;
		font-size: 12px;
		border:none;
		width:130px;
		height: 40px;
		font-weight: bold;
	}
	#title {
	   text-align: center;
	   font-size: 18pt;
	   margin-top: 50px;  
	}
</style>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script type="text/javascript">
	var useridFlag = false;
	var emailFlag = false;
	
	$(document).ready(function() {
		$("label.errorText1").hide();
		$("label.errorText").hide();
		$("label.errorText2").hide();
		$("label.errorText3").hide();
		$("label.errorText4").hide();
		$("#name").val("${mvo.name}");
		$("#userid").val("${mvo.userid}");
		$("#email").val("${mvo.email}");
		
		var ph = "${mvo.mobile}";
		var pharr = ph.split("-");
		
		$("#hp1").val(pharr[0]);
		$("#hp2").val(pharr[1]);
		$("#hp3").val(pharr[2]);
		$("#postcode").val("${mvo.postcode}");
		$("#address").val("${mvo.address}");
		$("#detailAddress").val("${mvo.detailaddress}");
		$("#extraAddress").val("${mvo.extraaddress}");
		
		var birth = "${mvo.birthday}";
		var birtharr = birth.split("-");
		$("#birthyyyy").val(birtharr[0]);
		$("#birthmm").val(birtharr[1]);
		$("#birthdd").val(birtharr[2]);
		
		
		$("img#zipcodeSearch").click(function() {
			alert("dd");
			new daum.Postcode({
	            oncomplete: function(data) {
	                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	                var addr = ''; // 주소 변수
	                var extraAddr = ''; // 참고항목 변수

	                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                    addr = data.roadAddress;
	                } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                    addr = data.jibunAddress;
	                }

	                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
	                if(data.userSelectedType === 'R'){
	                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
	                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
	                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
	                        extraAddr += data.bname;
	                    }
	                    // 건물명이 있고, 공동주택일 경우 추가한다.
	                    if(data.buildingName !== '' && data.apartment === 'Y'){
	                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                    }
	                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	                    if(extraAddr !== ''){
	                        extraAddr = ' (' + extraAddr + ')';
	                    }
	                    // 조합된 참고항목을 해당 필드에 넣는다.
	                    document.getElementById("extraAddress").value = extraAddr;
	                
	                } else {
	                    document.getElementById("extraAddress").value = '';
	                }

	                // 우편번호와 주소 정보를 해당 필드에 넣는다.
	                document.getElementById('postcode').value = data.zonecode;
	                document.getElementById("address").value = addr;
	                // 커서를 상세주소 필드로 이동한다.
	                document.getElementById("detailAddress").focus();
	            }
	        }).open();		
		});
	});
	
	// 아이디 중복검사 버튼
	function isExistUseridCheck() {

		// 버튼을 눌렀을 때 ajax(비동기)를 통해 data를 url 페이지로 post방식으로 보내준다
  		 $.ajax({
  			url:"<%= ctxPath %>/member/useridDuplicateCheck.sg",	// 보낼 주소
  			data:{"userid":$("input#userid").val()},				// 보낼 데이터(키:밸류)
  			type:"post",										// 메서드(post/get)
  			dataType:"json",									// "/MyMVC/member/idDuplicateCheck.up" 로부터 실행되어진 결과물을 받아오는 데이터 타입을 말한다
  			success:function(json){								// 성공했을 때 실행할 것	(data)는 결과물 json 객체를 말함(). 객체명은 아무거나 써도 객체는 들어옴
  				if(json.isExists){	// 객체명.key값 == value값
  					// 입력한 userid가 이미 사용중 이라면
  					//$("span#idcheckResult").html($("input#userid").val() + "은 사용불가능한 아이디입니다").css({"color":"red"});
  					alert("사용불가능 아이디");
  					$("input#userid").focus();
  					useridFlag = false;
  				}else{
  					// 입력한 userid 가 DB 테이블에 존재하지 않는 경우라면
  					//$("span#idcheckResult").html("사용가능한 아이디입니다.").css({"color":"navy"});
  					alert("사용가능 아이디");
  					useridFlag = true;
  				}
  			},
  			error: function(request, status, error){			// 실패했을 때 실행할 것(코딩 개떡같이 했을 때 => 정상적으로 받아온 데이터 true/false와 상관없음)
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
           }									
  		 });
	}
	// 이메일 중복검사 버튼
	function isExistEmailCheck() {

		// 버튼을 눌렀을 때 ajax(비동기)를 통해 data를 url 페이지로 post방식으로 보내준다
  		 $.ajax({
  			url:"<%= ctxPath %>/member/emailDuplicateCheck.sg",	// 보낼 주소
  			data:{"email":$("input#email").val()},				// 보낼 데이터(키:밸류)
  			type:"post",										// 메서드(post/get)
  			dataType:"json",									// "/MyMVC/member/idDuplicateCheck.up" 로부터 실행되어진 결과물을 받아오는 데이터 타입을 말한다
  			success:function(json){								// 성공했을 때 실행할 것	(data)는 결과물 json 객체를 말함(). 객체명은 아무거나 써도 객체는 들어옴
  				if(json.isExists){	// 객체명.key값 == value값
  					// 입력한 userid가 이미 사용중 이라면
  					//$("span#idcheckResult").html($("input#userid").val() + "은 사용불가능한 아이디입니다").css({"color":"red"});
  					alert("사용불가능 이메일");
  					$("input#email").val("");
  					$("input#email").focus();
  					emailFlag = false;
  				}else{
  					// 입력한 userid 가 DB 테이블에 존재하지 않는 경우라면
  					//$("span#idcheckResult").html("사용가능한 아이디입니다.").css({"color":"navy"});
  					alert("사용가능 이메일");
  					emailFlag = true;
  				}
  			},
  			error: function(request, status, error){			// 실패했을 때 실행할 것(코딩 개떡같이 했을 때 => 정상적으로 받아온 데이터 true/false와 상관없음)
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
           }									
  		 });
	}
	
	function funcEdit(){
		$("label.errorText").hide();
		
		
	    //// 아이디 검사
		var userid = $("input[name=userid]").val();
		var regExp = new RegExp(/^[a-z]+[a-z0-9]{5,14}$/g);
		
		var bool = regExp.test(userid);
		if (!bool) {
			// 불가
			$("input[name=userid]").val("");
			$("input[name=userid]").focus();
			$("label.errorText1").show();
			return false;
		}
		
		
		//// 이메일검사
		var email = $("input[name=email]").val().trim();
		
		var regExp = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
		var bool = regExp.test(email);
		if(!bool){
			// 불가
			$("input[name=email]").val("");
			$("input[name=email]").focus();
			$("label.errorText3").show();
			return false;
		}
		//// 비밀번호검사 
		var pwd = $("input[name=pwd]").val();
		var regExp = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
		
		var bool = regExp.test(pwd);
		if(!bool){
			// 불가
			$("input[name=pwd]").val("");
			$("input[name=pwd]").focus();
			$("label.errorText2").show();
			return false;
		}
		
		//// 비밀번호체크 검사
		pwd = $("input[name=pwd]").val();
		var pwdcheck = $("input#pwdCheck").val();
		
		if (pwd != pwdcheck) {
			// 불가
			$("input[name=pwd]").val();
			$("input#pwdCheck").val("");
			$("input#pwdCheck").focus();
			$("label.errorText4").show();
			return false;
		}
		
		var bFlagRequiredInfo = false;
		
		$(".requireInput").each(function() {
			var data = $(this).val().trim();
			
			if(data == ""){
				bFlagRequiredInfo = true;
				$(this).next().show();
				return false;
			}
		});
		
		if(!bFlagRequiredInfo){
			var frm = document.registerFrm;
			frm.action = "editmyinfo.sg";
			frm.method = "post";
			frm.submit();
		}
	}
	
	function funcEditno() {
		alert("회원정보 수정이 취소되었습니다.");
		location.href='main.sg';
	}
</script>
<div class="container">
<div id="title">EDIT PROFILE</div>
		<form name="registerFrm">

			<table id="tblMemberRegister">
				<thead>
					<tr>
						<th colspan="2" id="th">회원정보수정
						</th>
					</tr>
				</thead>
				<tbody>
					<tr> 
						<td style="width: 20%; font-weight: bold;" class="updatelab">성명&nbsp;<span
							class="star">*</span></td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							name="name" id="name" class="requireInput" /> 
						<label class="errorText">이름을 입력하시지 않았습니다.</label>
						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">아이디&nbsp;<span
							class="star">*</span></td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							name="userid" id="userid" class="requireInput" />&nbsp;&nbsp; <!-- 아이디중복체크 -->
							<button type="button" onclick="isExistUseridCheck()" id="idbtn">아이디 중복확인</button>
							<label class="errorText1">띄어쓰기 없이 영문자+숫자 6~15글자 이하로 입력해주세요.</label>		      
 						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">비밀번호&nbsp;<span
							class="star">*</span></td>
						<td style="width: 80%; text-align: left;" class="second"><input
							type="password" name="pwd" id="pwd" class="requireInput" /> 
						<label class="errorText2">영문자+숫자+특수문자 조합 6~15글자로 설정해주세요.</label>
						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">비밀번호확인&nbsp;<span
							class="star">*</span></td>
						<td style="width: 80%; text-align: left;" class="second"><input
							type="password" id="pwdCheck" class="requireInput" />
						<label class="errorText4">비밀번호가 일치하지 않습니다.</label>
						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">이메일&nbsp;<span
							class="star">*</span></td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							name="email" id="email" class="requireInput"
							placeholder="abc@def.com" />
							<span style="display: inline-block; width: 80px; height: 20px; border: solid 1px gray; border-radius: 5px; font-size: 5pt; text-align: center; margin-left: 10px; cursor: pointer;"
							onclick="isExistEmailCheck();">이메일중복확인</span> <span
							id="emailCheckResult"></span> <%-- ==== 퀴즈 끝 ==== --%>
						<label class="errorText3">이메일 형식에 맞춰 입력해주세요.</label>
						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab" >연락처</td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							id="hp1" name="hp1" size="6" maxlength="3" value="010" />&nbsp;-&nbsp;
							<input type="text" id="hp2" name="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
							<input type="text" id="hp3" name="hp3" size="6" maxlength="4" />
							<!--<span class="error">휴대폰 형식이 아닙니다.</span>--></td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">우편번호</td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							id="postcode" name="postcode" size="6" maxlength="5" />&nbsp;&nbsp;
							<%-- 우편번호 찾기 --%> <img id="zipcodeSearch"
							src="../images/b_zipcode.gif" style="vertical-align: middle;" />
							<!--<span class="error">우편번호 형식이 아닙니다.</span>--></td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">주소</td>
						<td style="width: 80%; text-align: left;" class="second"><input type="text"
							id="address" name="address" size="40" placeholder="주소" /><br />
							<input type="text" id="detailAddress" name="detailAddress"
							size="40" placeholder="상세주소" />&nbsp;<input type="text"
							id="extraAddress" name="extraAddress" size="40"
							placeholder="참고항목" /> <!--<span class="error">주소를 입력하세요</span>--></td>
					</tr>

					<tr>
						<td style="width: 20%; font-weight: bold;" class="updatelab">생년월일</td>
						<td style="width: 80%; text-align: left;" class="second"><input
							type="number" id="birthyyyy" name="birthyyyy" min="1950"
							max="2050" step="1" value="1995" style="width: 80px; height: 32px;  border: solid 1px #e6e6e6;" required />

							<select id="birthmm" name="birthmm"
							style="margin-left: 2%; width: 60px; padding: 6px; height: 32px; border: solid 1px #e6e6e6;">
							 
					               <option value ="01">01</option>
					               <option value ="02">02</option>
					               <option value ="03">03</option>
					               <option value ="04">04</option>
					               <option value ="05">05</option>
					               <option value ="06">06</option>
					               <option value ="07">07</option>
					               <option value ="08">08</option>
					               <option value ="09">09</option>
					               <option value ="10">10</option>
					               <option value ="11">11</option>
					               <option value ="12">12</option>
					               
							</select> 
							<select id="birthdd" name="birthdd" style="margin-left: 2%; width: 60px; padding: 6px; height: 32px;border: solid 1px #e6e6e6;">
								
					               <option value ="01">01</option>
					               <option value ="02">02</option>
					               <option value ="03">03</option>
					               <option value ="04">04</option>
					               <option value ="05">05</option>
					               <option value ="06">06</option>
					               <option value ="07">07</option>
					               <option value ="08">08</option>
					               <option value ="09">09</option>
					               <option value ="10">10</option>
					               <option value ="11">11</option>
					               <option value ="12">12</option>
					               <option value ="13">13</option>
					               <option value ="14">14</option>
					               <option value ="15">15</option>
					               <option value ="16">16</option>
					               <option value ="17">17</option>
					               <option value ="18">18</option>
					               <option value ="19">19</option>
					               <option value ="20">20</option>
					               <option value ="21">21</option>
					               <option value ="22">22</option>
					               <option value ="23">23</option>
					               <option value ="24">24</option>
					               <option value ="25">25</option>
					               <option value ="26">26</option>
					               <option value ="27">27</option>
					               <option value ="28">28</option>
					               <option value ="29">29</option>
					               <option value ="30">30</option>
					               <option value ="31">31</option>
					           
						</select></td>
					</tr>
				</tbody>
			</table>
			<div id="btn">
			<button type="button" id="okbtn" onclick="funcEdit();" class="btns">회원정보수정</button>
			<button type="button" id="deletebtn" class="btns" onclick="funcEditno();">취소</button>
			</div>
		</form>
</div>

<jsp:include page="../footer.jsp" />


