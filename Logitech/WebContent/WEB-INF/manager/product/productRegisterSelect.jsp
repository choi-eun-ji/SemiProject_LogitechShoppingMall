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

#newProduct, #newOption {
	background-color: #ccc;
}

#newProduct:hover, #newOption:hover {
	background-image: url("/Logitech/images/productselect-VghbBAYqUJ0-unsplash.jpg");
	background-size: 110%;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("div#newProduct").click(function(){
			location.href = "<%= ctxPath%>/manager/product/productRegister.sg";
		});
		
		$("div#newOption").click(function(){
			location.href = "<%= ctxPath%>/manager/product/optionRegister.sg";
		});
	});// end of $(document).ready(function(){})-----------------------------
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
					<h3 style="font-weight: bold;">제품 등록</h3>
					<hr style="height: 8px; background-color: #ccc; margin: 30px 0;">
					<div id="prodRegisterDiv" style="background-color: white;">
						<table style="margin: 150px 170px; text-align: center;">
							<tr>
								<td><div id="newProduct" style="width: 400px; height: 400px; margin-right: 50px; padding-top: 175px; border-radius: 10px;">신규 제품 등록</div></td>
								<td><div id="newOption" style="width: 400px; height: 400px; padding-top: 175px; border-radius: 10px;">신규 옵션 등록</div></td>
							</tr>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../managerFooter.jsp" />