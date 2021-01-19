<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<%
	String ctxPath = request.getContextPath();
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<%-- <script src="https://code.jquery.com/jquery-1.12.4.js"></script> --%>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<style type="text/css">
#yjwrapper {margin: 0 auto;}

#sideMenu {
	float: left;
	width: 20%;
	min-height: 1000px;
	border-radius: 8px;
}

#logOutdiv {margin: 10px 20px;}

#logOutdiv:hover {
	background-color: #f2f2f2;
	cursor: pointer;
}

ul {border-radius: 8px;}

.menu {
	margin: 20px;
	border: none;
}

.menuList {line-height: 65px;}

.lowermenu {width: 150px;}

.lowermenuA {
/*	border: solid 1px gray;*/
	display: table;
	width: 100%;
	height: 100%;
	text-align: center;
}

.lowermenuSpan {
	display: table-cell;
	width: 100%;
	height: 100%;
	vertical-align: middle;
}

li.menuList:hover, li.lowermenu:hover {
	background-color: #00A0C6;
}

#content, #content div {
	background-color: #e6e6e6;
}

#copyright {
    position: relative;
    min-height: 100px;
}

#copyrightIn {
	position: absolute;
	bottom: 20px;
	width: 100%;
    margin-left: 20px;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$(".menu").menu();
		
		$( "#accordion" ).accordion({
			collapsible: true
		});
	});// end of $(document).ready(function(){})--------------------------
	
	function goSignOut() {
		location.href = "<%= ctxPath%>/manager/signOut.sg";
	}// end of function goSignOut(){}----------------------------
</script>
</head>
<body>

<div id="yjwrapper">
	<div id="sideMenu">
		<img src="<%= ctxPath%>/images/logo.png" width="300px;" height="250px;" />
		
		<div id="logOutdiv">
			<img src="<%= ctxPath%>/images/icon_log out.png" width="40px;" height="40px;" onclick="goSignOut();" />&nbsp;&nbsp;
			<span onclick="goSignOut();">로그아웃</span>
		</div>
		
		<ul class="menu">
			<%-- 홈 --%>
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/managerIndex.sg'">
				<img src="<%= ctxPath%>/images/icon_home.png" width="65px;" height="60px;" style="margin-left: 10px; background-color: inherit; border: none;" />&nbsp;&nbsp;홈
	        </li>
	        
	        <%-- 회원관리 --%>
	        <c:if test="${sessionScope.loginManager.managertype == '전체' || sessionScope.loginManager.managertype == '회원'}">
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/member/memberList.sg'">
		      	<img src="<%= ctxPath%>/images/icon_find.png" width="65px;" height="60px;" style="margin-left: 10px; background-color: inherit; border: none;" />&nbsp;&nbsp;회원관리
	        </li>
	        </c:if>
	        <%--
	        <li class="menuList">
		       	<div>
					<img src="<%= ctxPath%>/images/icon_user.png" width="40px;" height="40px;" />&nbsp;&nbsp;회원관리
		       	</div>
		       	<ul>
					<li class="lowermenu">
						<a href="<%= ctxPath%>/manager/memberList.sg" class="lowermenuA">&nbsp;&nbsp;<span class="lowermenuSpan">회원현황</span></a>
					</li>
					
					<li class="lowermenu">
						<a href="manageMembership.jsp" class="lowermenuA">&nbsp;&nbsp;<span class="lowermenuSpan">등급관리</span></a>
					</li>
			    </ul>
	        </li>
	        --%>
	        
	        <%-- 제품관리 --%>
	        <c:if test="${sessionScope.loginManager.managertype == '전체' || sessionScope.loginManager.managertype == '제품'}">
	        <li class="menuList">
		       	<div>
					<img src="<%= ctxPath%>/images/icon_product-description.png" width="45px;" height="45px;" style="margin-left: 10px;" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;제품관리
		       	</div>
		       	<ul>
					<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/product/productOptionList.sg'">
						&nbsp;&nbsp;&nbsp;제품현황
					</li>
					
					<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/product/productRegisterSelect.sg'">
						&nbsp;&nbsp;&nbsp;제품등록
					</li>
			    </ul>
	        </li>
	        </c:if>
	        
	        <%-- 판매관리 --%>
	        <c:if test="${sessionScope.loginManager.managertype == '전체' || sessionScope.loginManager.managertype == '주문'}">
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/purchase/purchaseList.sg'">
		      	<img src="<%= ctxPath%>/images/icon_presentation.png" width="60px;" height="60px;" style="margin-left: 10px; background-color: inherit; border: none;" />&nbsp;&nbsp;주문관리
	        </li>
	        </c:if>
	        
	        <%-- 판촉관리 --%>
	        <c:if test="${sessionScope.loginManager.managertype == '전체' || sessionScope.loginManager.managertype == '판촉'}">
	        <li class="menuList">
		       	<div>
					<img src="<%= ctxPath%>/images/icon_shopping.png" width="45px;" height="45px;" style="margin-left: 10px;" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;판촉관리
		       	</div>
		       	<ul>
					<li class="lowermenu menuList">
						&nbsp;&nbsp;&nbsp;판촉현황
						<ul>
							<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/promotion/couponList.sg'">
								&nbsp;&nbsp;&nbsp;쿠폰현황
							</li>
							
							<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/promotion/eventList.sg'">
								&nbsp;&nbsp;&nbsp;이벤트현황
							</li>
						</ul>
					</li>
					
					<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/promotion/couponRegister.sg'">
						&nbsp;&nbsp;&nbsp;쿠폰등록
					</li>
					
					<li class="lowermenu" onclick="javascript:location.href='<%= ctxPath%>/manager/promotion/eventRegister.sg'">
						&nbsp;&nbsp;&nbsp;이벤트등록
					</li>
			    </ul>
	        </li>
	        </c:if>
      	</ul>
      	
      	<hr style="width: 95%; color: black;">
      	
      	<ul class="menu">
      		<c:if test="${sessionScope.loginManager.managerno == 1}">
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/manager/managerRegister.sg'">
	        	&nbsp;&nbsp;관리자 계정 등록
	        </li>
	        </c:if>
	        
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/manager/managerUpdate.sg?&managerid=${sessionScope.loginManager.managerid}'">
	        	&nbsp;&nbsp;관리자 계정 수정
	        </li>
	        
	        <li class="menuList" onclick="javascript:location.href='<%= ctxPath%>/manager/promotion/eventRegister.sg'">
				&nbsp;&nbsp;사용자 페이지로 이동
			</li>
      	</ul>
      	
      	<div id="copyright">
      		<div id="copyrightIn">
				<h5>&copy; 2020 Sist team one. All rights reserved</h4>
			</div>
		</div>
	</div>
	
	<div id="content" style="float: left; width: 80%">