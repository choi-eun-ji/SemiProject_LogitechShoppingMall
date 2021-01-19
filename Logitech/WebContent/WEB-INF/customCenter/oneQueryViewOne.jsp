<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../header.jsp" />

<style>
table#tblOneqRegister {
	width: 100%;
	border: solid;
	border-collapse: collapse;
	
}

table#tblOneqRegister #th {
	height: 40px;
	text-align: center;
	background-color: silver;
	font-size: 14pt;
	border: 1px solid;
}

table#tblOneqRegister td {
	/* border: solid 1px gray;  */
	line-height: 30px;
	padding-top: 8px;
	padding-bottom: 8px;
	border: 1px solid;
	text-align: center;
}


textarea {
	width: 96%;
	height: 97%;
	resize: none;
	margin-left: 2%;
	margin-right: 2%;
}

div#mid-header {
	text-align: left;
	margin-left: 5%;
}

button#commit {
	display: none;
}


button.btn {

	margin-right: 30px;
	margin: 5px 30px; 
}
</style>

<script type="text/javascript">
	
	
	$(document).ready(function () {
		
		var seq_oq="${seq_oq}";
		
		
		// 값 넣어주기
		$("select#h1").val('${h1}');
		$("#hp2").val("${h2}");
		$("#hp3").val("${h3}");
		$("input#eamil_id").val('${email_id}');
		$("select#email_domain").val('${email_domain}');
		
		$("input#category").val('${oqvo.category}');
		$("input#title").val('${oqvo.title}');
		$("textarea#content").val('${oqvo.content}');
		$("input#seq_oq").val('${seq_oq}');
		
		
		
		// 체크박스 
		if('${oqvo.answerform}' == "1") {
			
			$("input#check1").prop("checked", true);
			$("input#check2").prop("checked", false);
			
		} else {
			$("input#check1").prop("checked", false);
			$("input#check2").prop("checked", true);
		}
		
		// 태그 비활성화
		disable();
		
		// 중복체크 해제 이벤트
		$("input#check1").click(function () {
			
			$("input:checkbox[id='check2']").prop("checked", false);
			
		});
		
		$("input#check2").click(function () {
			
			$("input:checkbox[id='check1']").prop("checked", false);
			
		});
		
		
		
		
		

	}); // end of $(document).ready(function () {}
	
	// 태그 비활성화 함수
	function disable() {
		
		$("input").attr('disabled', true);
		$("select").attr('disabled', true);
		$("textarea").attr('disabled', true);
	}
	
	
	// 태그를 활성화 시켜주는 함수
	function able() {
		
		var seq_oq="${seq_oq}";
		
		$("input").attr('disabled', false);
		$("select").attr('disabled', false);
		$("textarea").attr('disabled', false);
		
		$("textarea#admin_content").attr('disabled', true);
		// 함수를 실행시키면 url이 ?seq_oq이 초기화 됨.
		// 따라서 별도의 이벤트가 필요함.
		// 이벤트를 불러일으키면 그게 바뀌고 페이지가 다시 로딩되는데 url이 초기화됨.
		
		
	}
	
	
	// 수정하기
	function modify() {
		
		able();
		
		$("button#commit").css("display", "inline-block");
		
		$("button#modify").css("display", "none");
		
	}
	
	// 변경완료
	function commit() {
		
		
		var frm = document.oqviewFrm;
		frm.action = "/Logitech/oqModify.sg";
		frm.method = "post";
		frm.submit();
	}
	
	// 삭제하기
	function del() {
		
		
		var result = confirm('정말로 삭제하시겠습니까?'); 
		if(result) {
			//yes
			location.href="/Logitech/oqDelete.sg?seq_oq="+'${seq_oq}';
		} else { 
			// no 
		}

			
		
		// 따음표가 있을때는 '' 구분지어주어야함
		
		
	}
	
	// 목록보기
	function golist() {
		location.href="/Logitech/goOqList.sg";
	}
	
</script>

<%
	String ctxPath = request.getContextPath();
%>




<div id="wrap">
	
	 

<div class="row" id="divRegisterFrm">


	<div class="col-md-12" align="center">

		<div style="text-align: left;">
			<h2>내 문의 내역 상세보기</h2>
			<p>고객님께서 문의하신 1:1문의 상세 내역 및 답변 내역입니다.</p>	
		</div> 
		
		
		<form name="oqviewFrm">
			
			<table id="tblOneqRegister">
			<hr>
			
				<tbody>
				
				
					<tr>
						<td style="width: 20%; font-weight: bold;">문의유형</td>
						<td style="width: 80%; text-align: left;"><select
							id="category" name="category"
							style="margin-left: 2%; width: 100px; padding: 8px;">

								<option value="고객등급">고객등급</option>
								<option value="배송">배송</option>
								<option value="환불/교환">환불/교환</option>
								<option value="회원탈퇴">회원탈퇴</option>
						</select></td>
					</tr>

					<tr>
						<td style="width: 20%; font-weight: bold;">제목</td>
						<td style="width: 80%; text-align: left;"><input type="text" id="title"
							name="title" style="width: 96%; margin-left: 2%;" /></td>
					</tr>

					<tr>
						<td style="width: 20%; font-weight: bold;">내용</td>
						<td style="width: 80%; text-align: left;"><textarea
								id="content" name="content" cols="30" rows="10"></textarea></td>
					</tr>

					<tr>
						<td style="width: 20%; font-weight: bold;">답변 희망 형태</td>
						<td style="width: 80%; text-align: left;"><input id="check1"
							name="anform" type="checkbox" style="margin-left: 2%;" value="1">
							<label for="check1">이메일</label> <input id="check2" name="anform"
							type="checkbox" value="0"> <label for="check2">받지않음(마이페이지에서
								확인)</label></td>
					</tr>
					
					<tr>
						<td style="width: 20%; font-weight: bold;">등록일자</td>
						<td style="width: 80%; margin-left: 2%;">${oqvo.writeday} <!--  등록일 -->
						</td>
						<td style="display: none;"><input id="seq_oq" name="seq_oq"> </td>
					</tr>

				</tbody>
			</table>
			</form>
			<br>
			<div style="text-align: left">
				<h3>답변 내용</h3>
			</div>
			<hr>
			<c:if test="${not empty oqavo.oq_content}">
				<table id="tblOneqRegister">
			

				<tbody>
					<tr>
						<td style="width: 20%; font-weight: bold;">답변 내용</td>
						<td style="width: 80%; text-align: left;">
						<textarea id="admin_content" name="content" cols="30" rows="10">${oqavo.oq_content}</textarea>
						</td>
					</tr>
					<tr>
						<td style="width: 20%; font-weight: bold;">답변 등록일자</td>
						<td style="width: 80%; margin-left: 2%;">${oqavo.answerdate}</td> <!--  등록일 -->
					</tr>
				</tbody>

			</table>
				
			</c:if>
			<c:if test="${empty oqavo.oq_content}">
		  		<table>
					<tr>
						<td>고객님의 문의사항이 답변중에 있습니다. 잠시만 기다려 주세요.</td>
					</tr>
			  	</table>
			  	<br>				
			</c:if>
			
			
	
			<c:choose>
				<c:when test="${oqavo.answerdate eq null}">
				<button id="modify" class="btn btn-primary" onclick="modify();">수정하기</button>
				
				<button id="commit" class="btn btn-primary" onclick="commit();">변경완료</button>
				<button class="btn btn-primary" onclick="del();">삭제</button>
				<button class="btn btn-primary" onclick="golist();">목록</button>
	
				</c:when>
				
				<c:otherwise>
					<button class="btn btn-primary" onclick="golist();">목록</button>
				</c:otherwise>
			</c:choose>
			
			

	</div>



</div>
</div>
<jsp:include page="../footer.jsp" />




















