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
	height: 500px;
}

#tblProductRegister > tr > td {
	height: 200px;
}
</style>

<script type="text/javascript">
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
			
			if(productserialid == "") {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 제품 일련번호 유효성 검사
		$("input#productserialid").blur(function(){
			var productserialid = $(this).val().trim();
			
			var regExp = new RegExp(/^[a-zA-Z0-9- ]{3,20}$/g);
			var bool = regExp.test(productserialid)
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 색상 유효성 검사
		$("input#color").blur(function(){
			var color = $(this).val().trim();
			
			var regExp = new RegExp(/^[a-z]+$/g);
			var bool = regExp.test(color)
			
			if(!bool) {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 재고량 유효성 검사
		$("input#spinnerPqty").blur(function(){
			var spinnerPqty = $(this).val().trim();
			
			if(spinnerPqty == "") {
				$(this).parent().find(".error").show();
				$(this).focus();
			} else {
				$(this).parent().find(".error").hide();
			}
		});
		
		// 검색어를 포함한 제품 아이디 목록을 가져와 등록할 상품에 대한 선택지 제공
		$("button#prodIdFind").click(function(){
			flagIdExistClick = true;
			
			$.ajax({
				url:"<%= ctxPath%>/manager/product/prodIdFind.sg",
				type:"GET",
				data:{"productid":$("input#productid").val()},  // 검색으로 입력받은 상품
				dataType:"JSON",
				success:function(json){
					if(json.length > 0) {
						var html = "<select id='searchResultId'>" +  // select 태그 html
								"<option>제품 아이디</option>";
						
						$.each(json, function(index, item){
							html += "<option>"+item.productid+"</option>"  // 가져온 상품 리스트 반복 추가
						})
						
						html += "</select>";
						
						$("span#prodIdFindResult").html(html);
						
						$("select#searchResultId").change(function(){
							if($(this).val() != "제품 아이디") {
								$("input#productid").val($(this).val());
								$("input#productserialid").val($(this).val());
							} else {
								alert("제품 아이디를 선택해주세요.");
								$("input#productid").val("");
							}
						})
					}
				},
				error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});
		
		// 제품 일련번호 중복확인 검사하기
		$("button#serialIdCheck").click(function(){
			$("span.error").hide();
			
			flagSerialIdDuplicateClick = true;
			
			$.ajax({
				url:"<%= ctxPath%>/manager/product/pSerialIdDuplicateCheck.sg",
				data:{"productserialid":$("input#productserialid").val()},  // data 는 /MyMVC/member/idDuplicateCheck.up로 전송해야할 데이터를 말한다.
				type:"post",
				dataType:"JSON",
				success:function(json){
					if(json.isExists) {  // json.isExists --> 변수.key값
						// 입력한 userid 가 이미 사용 중인 경우
					//	alert($("input#userid").val() + " 은(는) 이미 사용 중인 아이디이므로 사용할 수 없습니다.");
						$("span#serialIdCheckResult").html($("input#productserialid").val() + " 은(는) 이미 등록한 제품 일련번호입니다.").css("color","red");
						$("input#productserialid").val("");
					} else {
						// 입력한 userid 가 tbl_member 테이블에 존재하지 않는 경우
					//	alert($("input#userid").val() + " 은(는) 사용가능합니다.");
						$("span#serialIdCheckResult").html($("input#productserialid").val() + " 은(는) 사용 가능합니다.").css("color","green");
					}// end of if(json.isExists){}-------------------
				},
				error:function(request, status, error){  // error 는 결과를 받아올 수 없는 코드 상의 오류이다.
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});// end of $.ajax({})----------------------
		});
	});// end of $(document).ready(function(){})-----------------------------
	
	function goOptionRegister() {
		// 아이디 일련번호 검사 확인하기 //
		if(!flagSerialIdDuplicateClick) {
			alert("제품 일련번호 중복확인을 해주세요.");
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
			frm.action = "<%= ctxPath%>/manager/product/optionRegister.sg";
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
										<button type="button" id="prodIdFind">제품 아이디 찾기</button>
										<span id="prodIdFindResult"></span>
										<span class="error">등록할 제품 아이디를 입력해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										제품 일련번호&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="productserialid" id="productserialid" class="requiredInfo" />
										<button type="button" id="serialIdCheck">제품 일련번호 중복확인</button>
										<span id="serialIdCheckResult" style="font-size: 10pt;"></span>
										<span class="error">영문자, 숫자, - 를 결합한 10자~20자의 일련번호를 등록해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										색상&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input type="text" name="color" id="color" class="requiredInfo" placeholder="black(영문소문자로 작성)" />
										<span class="error">색상에 해당하는 영문소문자로 작성해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
										재고량&nbsp;<span class="star">*</span>
									</td>
									<td style="width: 70%; text-align: left;">
										<input id="spinnerPqty" name="stock" value="0" style="width: 30px; height: 20px;" /> 개
										<span class="error">재고량을 입력해주세요.</span>
									</td>
								</tr>
								<tr>
									<td style="width: 30%; font-weight: bold;">
									</td>
									<td style="width: 70%; text-align: left;">
										<button type="button" id="optionRegisterBtn" style="border: none; width: 150px; height: 50px; background-color: #1a1a1a; color: white; text-align: center;" onclick="goOptionRegister();">옵션 등록</button>
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