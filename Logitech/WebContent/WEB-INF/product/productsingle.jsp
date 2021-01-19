<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../header.jsp" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<style>
* {
	box-sizing: border-box;
}

body {
	font-family: Arial;
	background: #f1f1f1;
}

.header {
	padding: 30px;
	text-align: center;
	background: white;
}

.header h1 {
	font-size: 50px;
}

.topnav {
	overflow: hidden;
	background-color: #333;
}

.topnav a {
	float: left;
	display: block;
	color: #f2f2f2;
	text-align: center;
	padding: 14px 16px;
	text-decoration: none;
}

.topnav a:hover {
	background-color: #ddd;
	color: black;
}

.leftcolumn {
	float: left;
	width: 25%;
}

.rightcolumn {
	float: left;
	width: 75%;
	background-color: white; /* #f1f1f1 */
	padding-left: 20px;
}

.fakeimg {
	background-color: #aaa;
	width: 100%;
	padding: 20px;
}

.card {
	background-color: white;
	padding: 20px;
	margin-top: 20px;
}

.row:after {
	content: "";
	display: table;
	clear: both;
}
/* Footer */
.footer {
	padding: 20px;
	text-align: center;
	background: #ddd;
	margin-top: 20px;
}

@media screen and (max-width: 800px) {
	.leftcolumn, .rightcolumn {
		width: 100%;
		padding: 0;
	}
}

@media screen and (max-width: 400px) {
	.topnav a {
		float: none;
		width: 100%;
	}
}

@import
	url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap")
	;

select {
	/* 생략 */
	font-family: "Noto Sansf KR", sans-serif;
	font-size: 1rem;
	font-weight: 400;
	line-height: 1.5;
	color: #444;
	background-color: #fff;
	padding: 0.6em 1.4em 0.5em 0.8em;
	margin: 0;
	border: 1px solid #aaa;
	border-radius: 0.5em;
	box-shadow: 0 1px 0 1px rgba(0, 0, 0, 0.04);
}

select:hover {
	border-color: #888;
}

select:focus {
	border-color: #aaa;
	box-shadow: 0 0 1px 3px rgba(59, 153, 252, 0.7);
	box-shadow: 0 0 0 3px -moz-mac-focusring;
	color: #222;
	outline: none;
}

select:disabled {
	opacity: 0.5;
}


table#tblReview {
	width: 100%;
	border-collapse: collapse;
	height: 200pt;
	
}

table#tblReview #th {
	height: 20px;
	text-align: center;
	font-size: 14pt;
	
	
}


table#tblReview td {
	border: solid 1px gray;  
	line-height: 30px;
	padding-top: 8px;
	padding-bottom: 8px;
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

div#mid-header {
	text-align: left;
	margin-left: 5%;
}

div.wrap {
	width: 80%;
	align-content: center;
}

hr {
	background-color: gray;
	border: solid 2px gray;
}


div.reviewSummary {
	display: flex;
	height: 10vh;
	justify-content: center;
	align-items: center;
}

.fa-star {
	color: orange;
	font-size: 30pt;
	margin-left: 10pt;
}

.checked {
	color: gray;
}

div#pagebar {
	display: flex;
	height: 4vh;
	justify-content: center;
	align-items: center;
}

div.rating {
	width: 600pt;
	text-align: center;
	margin: 0, auto;
}

div.star-wrap {
	width: 45pt;
	display: inline-block;
}

div.star {
	width: 0%;
	overflow: hidden;
}
</style>

<script type="text/javascript">

$(document).ready(function(){
	
 	 
 	 		<%-- var frm = document.selectColor;
 			frm.action = "<%= ctxPath%>/product/purchasedetail.sg"; 
 			frm.method = "POST";
 			frm.submit(); --%>
 			<%-- 수빈:시작 --%>
   
   var proid = "${rProductVO.productid}";
   
   var history = localStorage.getItem("history");
   
   if(history == null) {
      // 세션스토리지에 history가 없을 때 처음 생성해줌
      console.log("히스토리가 null일 때");
      // 처음에 null값이 들어가는걸 방지
      history = "";
      localStorage.setItem("history", proid);

   } 

   // 중복된 값이 들어오는걸 방지(중복됐다면 삭제하고 다시 최신으로 넣어줌)
   if (history.indexOf(proid) != -1) {
      history = history.replace("," + "'" + proid + "'" , "");
   }
   
   history =  "," + "'" + proid + "'" + history;
   
   var check = history.split("").reverse().join("").substr(0, 1);
   
   if (check == ",") {
      history = history.substr(0, history.length -1);
   }
   
   console.log("history 끝=> " + history);
   localStorage.removeItem("history");         // 기존의 세션스토리지 삭제
   localStorage.setItem("history", history);   // 최신값을 넣어줌

   <%-- 수빈:끝 --%>
 			
	<%-- 지훈: 시작 --%>
 	var productid = "${productid}";
 	var average = "${average}";
 	var reviewNum = "${average}";
 	
 	var rating = $('.rating');

 	rating.each(function () {
 		var $this = $(this);
 		var targetScore = $this.attr('data-rate');
 		var firstdigit = targetScore.split('.');
 		
 		if(firstdigit.length > 1) {
 			for(var i = 0; i<targetScore[0]; i++) {
 				$this.find('.star').eq(i).css("width", "100%");
 				$this.find('.star').eq(i).css("color", "orange");
 				
 			}
 			$this.find('.star').eq(firstdigit[0]).css({width:firstdigit[1]+'0%'})
 		} else {
 			for(var i = 0; i<targetScore; i++) {
 				$this.find('.star').eq(i).css({width:'100%'});
 			}
 		}
 		
 		
 	});
 	
 	$("textarea").attr('disabled', true);
 	
 	<%-- 지훈: 끝 --%>		
 			
 			
 			
	
});//end of $(document).ready(function()--------------------
		
		function goBuy() {
	
	        sessionStorage.setItem("amount", $("input#amount").val()); 
	
			if($("select#selectColor").val() == ""){
		 		
		 		alert('색상을 선택해주세요!!');
		 		return;
		 	}
		 	
		 	if($("select#selectColor").val() != ""){
		 		var color = $("#selectColor option:selected").val();
				$("input#color").val(color);
				//alert(color);
				"${detailProductList}"
		 		
				location.href = "purchasedetail.sg?productid=${rProductVO.productid}&productname=${rProductVO.productname}&color="+color;
				
		 		
		 		
		 		
		 		<%-- var frm = document.selectColor;
				frm.action = "<%= ctxPath%>/product/purchasedetail.sg"; 
				frm.method = "POST";
				frm.submit(); --%>
		 	} 
	
}
		
		function goWishList() {
			
			
			if($("select#selectColor").val() == ""){
		 		
		 		alert('색상을 선택해주세요!!');
		 		return;
		 	}
			
			if($("select#selectColor").val() != ""){
	 	 		
				var color = $("#selectColor option:selected").val();
	 			$("input#color").val(color);
	 			
	 			 var cartpronum = $("input#amount").val(); 
	 			
	 			location.href = "<%= ctxPath%>/mypage/ordercart.sg?fk_productid=${rProductVO.productid}&price=${rProductVO.price}&color="+color+"&cartpronum="+cartpronum;
			}   /* +"&cartpronum="+cartpronum+ */
}

		
		
		
		
		
		
		

		
</script>



<!-- 메인케러셀 -->
<div id="myCarousel" class="carousel slide" data-ride="carousel">

	<ol class="carousel-indicators">
		<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		<li data-target="#myCarousel" data-slide-to="1"></li>
		<li data-target="#myCarousel" data-slide-to="2"></li>
		<li data-target="#myCarousel" data-slide-to="3"></li>
	</ol>


	<div class="carousel-inner">
		<div class="item active">
			<img src="/Logitech/images/mousemain.png" alt="mouse"
				style="width: 100%;">
		</div>

		<div class="item">
			<img src="/Logitech/images/keyboardmain.png" alt="keyboard"
				style="width: 100%;">
		</div>

		<div class="item">
			<img src="/Logitech/images/headsetmain.png" alt="headset"
				style="width: 100%;">
		</div>

		<div class="item">
			<img src="/Logitech/images/speakermain.png" alt="speaker"
				style="width: 100%;">
		</div>
	</div>


	<a class="left carousel-control" href="#myCarousel" data-slide="prev">
		<span class="glyphicon glyphicon-chevron-left"></span> <span
		class="sr-only">Previous</span>
	</a> <a class="right carousel-control" href="#myCarousel" data-slide="next">
		<span class="glyphicon glyphicon-chevron-right"></span> <span
		class="sr-only">Next</span>
	</a>
</div>
<!-- <div class="topnav">
  <a href="#">홈</a>
  <a href="#">제품</a>
  <a href="#">링크</a>
   <a href="#" style="float:right"></a>
</div> -->



<div class="row">
	<div class="leftcolumn">
		<div class="card">

			<!-- <h2>상품 정보</h2>
      <h5>제품명</h5>
      <div class="fakeimg" style="height:200px;">구매자정보(이름~주소)</div>
      <p></p>
      <p></p>
    </div>
     <div class="card">
      <h2>2번째</h2>
      <h5>추천물품?</h5>
      <div class="fakeimg" style="height:200px;">이미지</div>
      <p>ABC</p>
      <p>
      </p> -->
		</div>
	</div>

	<!--  <form name="selectColor"> -->

	<div class="rightcolumn">

		<div class="card">
			<h2 id="productname" style="font-weight: bold">${rProductVO.productid}</h2>
			<h2 id="productname" style="font-weight: bold">${rProductVO.productname}</h2>
			<!-- 상품명 -->
			<div class="row">
				<div class="col-md-6">
					<img width="100%" src="${rProductVO.imgfilename}" />
				</div>

				<div class="col-md-6">
					<br>
					<ol>
						<h2 style="font-weight: bold;">
							가격 :
							<fmt:formatNumber value="${rProductVO.price}" pattern="###,###" />
							원
						</h2>
					</ol>
					<br>
					<ol>
						<div>
							<select id="selectColor" name="color">
								<option selected value="">색상을 선택하세요.</option>
								<c:forEach var="pvo" items="${detailProductList}">
									<option value="${pvo.povo.color}">${pvo.povo.color}</option>
								</c:forEach>
							</select>
						</div>
					</ol>
					<jsp:include page="product_amount.jsp"></jsp:include>


					<br>
					<ol>
						<h3>Delivery Info</h3>
					</ol>
					<ol>국내 배송 / 입점사 배송 / CJ대한통운
					</ol>
					<ol>출고 기간 : 평균 2.0일 / 주말, 공휴일 제외
					</ol>
					<br>

					<ol>
						<button type="button" id="goPurchase" class="btn btn-success"
							onclick="goBuy()">주문하기</button>
					</ol>
					<%-- onclick="javascript:location.href='purchasedetail.sg?productid=${rProductVO.productid}&productname=${rProductVO.productname}'" --%>
					<ol>
						<button type="button" id="goCart" class="btn btn-info"
							onclick="goWishList()">장바구니</button>
					</ol>
					<ol>
						<button type="button" class="btn btn-danger">찜하기</button>
					</ol>

				</div>

			</div>

		</div>

		<p></p>


	</div>

	<input type="hidden" id="color" name="color" value="" /> <input
		type="hidden" id="cartpronum" name="carpronum" value="" />

	<!-- </form>
     -->



	<div class="card">
		<h3>&nbsp;</h3>
		<h3></h3>
		<jsp:include page="productinfo.jsp" />


		<%-- 지훈 시작 --%>


		<hr>
		<h3>
			등록된 상품리뷰 (<span id="gun">${reviewNum}</span>)
		</h3>
		<hr>

		<c:choose>
			<c:when test="${reviewNum eq 0}">
				<table id="tbl_review_sumary">
					<tr>
						<h5 style="text-align: center;">등록된 상품평이 없습니다.</h5>
					</tr>
				</table>
			</c:when>
			<c:otherwise>

				<div class="reviewSummary">

					<table id="tbl_review_sumary">

						<tbody>

							<tr>

								<div class="rating" data-rate="${average}">
									<div class="star-wrap">
										<div class="star">
											<i class="fas fa-star"></i>
										</div>
									</div>
									<div class="star-wrap">
										<div class="star">
											<i class="fas fa-star"></i>
										</div>
									</div>
									<div class="star-wrap">
										<div class="star">
											<i class="fas fa-star"></i>
										</div>
									</div>
									<div class="star-wrap">
										<div class="star">
											<i class="fas fa-star"></i>
										</div>
									</div>
									<div class="star-wrap">
										<div class="star">
											<i class="fas fa-star"></i>
										</div>
									</div>
									<span id="score" style="font-size: 20pt; color: black;">&nbsp;&nbsp;${average}</span>
								</div>
							</tr>


						</tbody>
					</table>
				</div>
				<hr>
				<br>

				<c:forEach var="rvo" items="${reviewlist}" varStatus="status">

					<table id="tblReview">
						<tbody>
							<tr>
								<td><div id="review_sumary">

										<c:choose>
											<c:when test="${rvo.score eq 0}">
												<span name="side" class="fa fa-star checked "
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>

											<c:when test="${rvo.score eq 1}">
												<span name="side" class="fa fa-star "
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>

											<c:when test="${rvo.score eq 2}">
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>

											<c:when test="${rvo.score eq 3}">
												<span name="side" class="fa fa-star "
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star "
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star "
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>

											<c:when test="${rvo.score eq 4}">
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star checked"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>

											<c:when test="${rvo.score eq 5}">
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
												<span name="side" class="fa fa-star"
													style="font-size: 10pt; margin-left: 0pt;"></span>
											</c:when>
										</c:choose>
										<span>&nbsp;&nbsp;${rvo.score}</span>
									</div></td>

								<td>색상 : <span id="color">${rvo.color}</span></td>

							</tr>

							<tr>
								<td style="width: 20%; font-weight: bold;">
									<div id="images">
										<img width='150px;' height='150px' src='/Logitech/images/' +'${rvo.review_image}'/>
									</div>
								</td>
								<td colspan="2" style="width: 80%; text-align: center;"><div
										style="text-align: right; margin-right: 13pt; margin-top: -10pt;">
										작성자 : <span id="userid">${rvo.userid}</span> / <span
											id="writeday">${rvo.wirteday}</span>
									</div> <textarea id="content" name="content" cols="15" rows="10"
										style="height: 100pt;">${rvo.content}</textarea></td>
							</tr>
						</tbody>
					</table>
				</c:forEach>

				<div id="pagebar">${pageBar}</div>
	</div>

</div>

</div>

</c:otherwise>
</c:choose>
<%-- 지훈 끝 --%>


<jsp:include page="../footer.jsp" />
