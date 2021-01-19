<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String ctxPath = request.getContextPath();
%>

<jsp:include page="../managerHeader.jsp" />

<style>
#prodRegisterState {
	float: left;
	width: 95%;
	margin: 20px;
}

.divCell, .divContent {
	text-align: center;
}

#prodRegisterDiv {
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

#tblProductRegister {
	width: 100%;
	height: 1000px;
	margin: 20px 25px;
}

#tblProductRegister > tr > td {
	height: 100px;
}
</style>

<script type="text/javascript">
	var flagProdIdDuplicateClick = false;
	var flagSerialIdDuplicateClick = false;

	$(document).ready(function(){
		$("span.error").hide();
		
		$("input#productid").focus();
		
		// 재고량 스피너
		$("input#spinnerPqty").spinner({
			spin:function(event,ui){
				if(ui.value > 100) {
					$(this).spinner("value", 100);
					return false;
				}
				else if(ui.value < 0) {
					$(this).spinner("value", 0);
					return false;
				}// end of if(ui.value > 100) {}----------------------------
			}// end of spin:function(event,ui) {}----------------------------
		});// end of $("input#spinnerPqty").spinner()--------
		
		// 제품 아이디 유효성 검사
		$("input#productid").blur(function(){
			var productid = $(this).val().trim();
			
			var regExp = new RegExp(/^[a-zA-Z0-9- ]{3,20}$/g);
			var bool = regExp.test(productid)
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 제품명 유효성 검사
		$("input#productname").blur(function(){
			var productname = $(this).val().trim();
			
			if(productname == "") {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 썸네일 이미지 유효성 검사
		$("input#imgfilename").blur(function(){
			var imgfilename = $(this).val().trim();
			
			if(imgfilename == "") {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 제품 아이디 중복확인 검사하기
		$("button#prodIdCheck").click(function(){
			$("span.error").hide();
			
			flagProdIdDuplicateClick = true;
			
			$.ajax({
				url:"<%= ctxPath%>/manager/product/prodIdDuplicateCheck.sg",
				data:{"productid":$("input#productid").val()},  // data 는 /MyMVC/member/idDuplicateCheck.up로 전송해야할 데이터를 말한다.
				type:"post",
				dataType:"JSON",
				success:function(json){
					if(json.isExists) {  // json.isExists --> 변수.key값
						// 입력한 userid 가 이미 사용 중인 경우
					//	alert($("input#userid").val() + " 은(는) 이미 사용 중인 아이디이므로 사용할 수 없습니다.");
						$("span#prodIdCheckResult").html($("input#productid").val() + " 은(는) 이미 등록한 제품 아이디입니다.").css("color","red");
						$("input#productid").val("");
					} else {
						// 입력한 userid 가 tbl_member 테이블에 존재하지 않는 경우
					//	alert($("input#userid").val() + " 은(는) 사용가능합니다.");
						$("span#prodIdCheckResult").html($("input#productid").val() + " 은(는) 사용 가능합니다.").css("color","green");
					}// end of if(json.isExists){}-------------------
				},
				error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});
	});// end of $(document).ready(function(){})-----------------------------
	
	function goProdRegister() {
		// 아이디 중복 검사 확인하기 //
		if(!flagProdIdDuplicateClick) {
			alert("제품 아이디 중복확인을 해주세요.");
			return;
		}
		
		// 제품 용도 라디오 선택 여부 확인하기 //
		var foruseCheckedLength = $("input:radio[name=foruse]:checked").length;
		
		if(foruseCheckedLength == 0) {
			alert("용도를 선택해주세요.");
			return;
		}
		
		// 제품 연결 방식 라디오 선택 여부 확인하기 //
		var connectionCheckedLength = $("input:radio[name=connection]:checked").length;
		
		if(connectionCheckedLength == 0) {
			alert("연결 방식을 선택해주세요.");
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
			frm.action = "<%= ctxPath%>/manager/product/productRegister.sg";
			frm.method = "post";
			frm.submit();
		}// end of if(!bFlagRequiredInfo){}------------------
	}// end of function goCpRegister() {}------------------------
</script>

<div id="prodRegisterState">
	<div class="row">
		<div class="col-sm-12">
			<div class="well" style="background-color: white;">
				<h4>제품 등록</h4> 
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="well divContent" style="background-color: white;">
				<form name="registerFrm">
					<h3 style="font-weight: bold; margin-left: 40px;">제품 등록 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>)</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="prodRegisterDiv" style="background-color: white;">
						<table id="tblProductRegister">
							<tbody>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품 아이디&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="productid" id="productid" class="requiredInfo" />
										<button type="button" id="prodIdCheck">제품 아이디 중복확인</button>
										<span id="prodIdCheckResult"></span>
										<span class="error">영문자, 숫자, - 를 결합한 10자~20자의 아이디를 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품명&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="productname" id="productname" class="requiredInfo" />&nbsp;&nbsp;
										<span class="error">제품명을 입력해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										카테고리&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<select name="category" style="height: 40px;">
											<option value="mouse">마우스</option>
											<option value="keyboard">키보드</option>
											<option value="headset">헤드셋</option>
											<option value="speaker">스피커</option>
										</select>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										용도&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="radio" name="foruse" id="home" value="가정용" />&nbsp;<label for="home">가정용</label>&nbsp;&nbsp;
										<input type="radio" name="foruse" id="office" value="사무용" />&nbsp;<label for="office">사무용</label>&nbsp;&nbsp;
										<input type="radio" name="foruse" id="education" value="교육용" />&nbsp;<label for="education">교육용</label>&nbsp;&nbsp;
										<input type="radio" name="foruse" id="gaming" value="게이밍" />&nbsp;<label for="gaming">게이밍</label>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										연결 방식&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="radio" name="connection" id="usb" value="USB수신기" />&nbsp;<label for="usb">USB수신기</label>&nbsp;&nbsp;
										<input type="radio" name="connection" id="bluetooth" value="블루투스" />&nbsp;<label for="bluetooth">블루투스</label>&nbsp;&nbsp;
										<input type="radio" name="connection" id="cable" value="유선" />&nbsp;<label for="cable">유선</label>&nbsp;&nbsp;
										<input type="radio" name="connection" id="compound" value="복합수신기" />&nbsp;<label for="compound">복합수신기</label>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										가격&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="price" id="price" class="requiredInfo" placeholder="숫자만 입력하세요." /> 원
										<span class="error">숫자로만 작성해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										썸네일 이미지 경로&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="imgfilename" id="imgfilename" class="requiredInfo" />
										<span class="error"></span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품 설명 이미지 경로&nbsp;
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="carouselimg" id="carouselimg" />
										<span class="error"></span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										상세제품 이미지 경로&nbsp;
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="detailimg" id="detailimg" />
										<span class="error"></span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="prodRegisterBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goProdRegister();">제품 등록</button>
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