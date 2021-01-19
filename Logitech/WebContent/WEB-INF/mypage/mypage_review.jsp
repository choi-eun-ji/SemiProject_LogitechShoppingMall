<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%
   String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet"
   href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<jsp:include page="../header.jsp" />


<style type="text/css">
#title {
   text-align: center;
   font-size: 18pt;
   margin-bottom: 60px;
}

button.btn1 {
   background: none;
   border: 0;
   outline: 0;
   cursor: pointer;
}

.tab_menu_container {
   display: flex;
}

.tab_menu_btn {
   width: 120px;
   height: 40px;
   transition: 0.3s all;
   font-size: 10pt;
}

.tab_menu_btn.on {
   border-bottom: 2px solid #df0000;
   font-weight: 700;
   color: #FF6666;
}

.tab_menu_btn:hover {
   color: #FF6666;
}

.tab_box {
   display: none;
   padding: 20px;
}

.tab_box.on {
   display: block;
}

img {
   width: 80px;
   heigth: 80px;
}

.checked {
   color: orange;
}

#status {
   color: red;
}

#titlehead>td {
   padding: 10px;
   margin-bottom: 10px;
   background-color: #e6e6e6;
}

#titlehead2>td {
   padding: 10px;
   font-size: 10pt;
   vertical-align: middle;
}

.star-fg .glyphicon-heart {
   color: red !important;
}

#starui>li {
   display: inline-block;
}

.all {
   color: gray;
}

.inputs {
   font-size: 30pt;
   padding-left: 60px;
}

#stardiv {
   padding-top: 20px;
   padding-bottom: 0px;
}

.btnall {
	background-color: #e9e9e2;
}

#yesbtn {
	background-color: #e9e9e2;
	border: none;
	color: black;
	font-size: 10pt;
	margin-top: 20px;
	font-weight: bold;
}
.yesbtn2 {
	background-color: #e9e9e2;
	color: black;
	font-size: 8pt;
	height: 30px;
	border-radius: 5px;
	border: none;
}
#title {
   text-align: center;
   font-size: 18pt;
   margin-top: 50px;  
}
</style>
<script src="js/addons/rating.js"></script>
<script type="text/javascript">
   $(document).ready(function() {
	   $(".hispan").hide();
      //버튼 색 제거,추가
      $('.tab_menu_btn').on('click', function() {
         $('.tab_menu_btn').removeClass('on');
         $(this).addClass('on')
      });

      //1번 컨텐츠
      $('.tab_menu_btn1').on('click', function() {
         $('.tab_box').hide();
         $('.tab_box1').show();
      });

      //2번 컨텐츠
      $('.tab_menu_btn2').on('click', function() {
         $('.tab_box').hide();
         $('.tab_box2').show();
      });

      var reviewnum = 0;
      $("#starnums").val(reviewnum);
      $("#star0").click(function() {
         reviewnum = 1;
         $("#star0").css("color", "#efef4e");
         $("#star1").css("color", "gray");
         $("#star2").css("color", "gray");
         $("#star3").css("color", "gray");
         $("#star4").css("color", "gray");
         $(".inputs").text(reviewnum);
         $("#starnums").val(reviewnum);
      });

      $("#star1").click(function() {
         reviewnum = 2;
         $("#star0").css("color", "#efef4e");
         $("#star1").css("color", "#efef4e");
         $("#star2").css("color", "gray");
         $("#star3").css("color", "gray");
         $("#star4").css("color", "gray");
         $(".inputs").text(reviewnum);
         $("#starnums").val(reviewnum);
      });
      $("#star2").click(function() {
         reviewnum = 3;
         $("#star0").css("color", "#efef4e");
         $("#star1").css("color", "#efef4e");
         $("#star2").css("color", "#efef4e");
         $("#star3").css("color", "gray");
         $("#star4").css("color", "gray");
         $(".inputs").text(reviewnum);
         $("#starnums").val(reviewnum);
      });
      $("#star3").click(function() {
         reviewnum = 4;
         $("#star0").css("color", "#efef4e");
         $("#star1").css("color", "#efef4e");
         $("#star2").css("color", "#efef4e");
         $("#star3").css("color", "#efef4e");
         $("#star4").css("color", "gray");
         $(".inputs").text(reviewnum);
         $("#starnums").val(reviewnum);
      });
      $("#star4").click(function() {
         reviewnum = 5;
         $("#star0").css("color", "#efef4e");
         $("#star1").css("color", "#efef4e");
         $("#star2").css("color", "#efef4e");
         $("#star3").css("color", "#efef4e");
         $("#star4").css("color", "#efef4e");
         $(".inputs").text(reviewnum);
         $("#starnums").val(reviewnum);
      });
      
      
      
      $("#stars0").click(function() {
         reviewnum = 1;
         $("#stars0").css("color", "#efef4e");
         $("#stars1").css("color", "gray");
         $("#stars2").css("color", "gray");
         $("#stars3").css("color", "gray");
         $("#stars4").css("color", "gray");
         $(".inputs2").text(reviewnum);
         $("#starnums2").val(reviewnum);
      });

      $("#stars1").click(function() {
         reviewnum = 2;
         $("#stars0").css("color", "#efef4e");
         $("#stars1").css("color", "#efef4e");
         $("#stars2").css("color", "gray");
         $("#stars3").css("color", "gray");
         $("#stars4").css("color", "gray");
         $(".inputs2").text(reviewnum);
         $("#starnums2").val(reviewnum);
      });
      $("#stars2").click(function() {
         reviewnum = 3;
         $("#stars0").css("color", "#efef4e");
         $("#stars1").css("color", "#efef4e");
         $("#stars2").css("color", "#efef4e");
         $("#stars3").css("color", "gray");
         $("#stars4").css("color", "gray");
         $(".inputs2").text(reviewnum);
         $("#starnums2").val(reviewnum);
      });
      $("#stars3").click(function() {
         reviewnum = 4;
         $("#stars0").css("color", "#efef4e");
         $("#stars1").css("color", "#efef4e");
         $("#stars2").css("color", "#efef4e");
         $("#stars3").css("color", "#efef4e");
         $("#stars4").css("color", "gray");
         $(".inputs2").text(reviewnum);
         $("#starnums2").val(reviewnum);
      });
      $("#stars4").click(function() {
         reviewnum = 5;
         $("#stars0").css("color", "#efef4e");
         $("#stars1").css("color", "#efef4e");
         $("#stars2").css("color", "#efef4e");
         $("#stars3").css("color", "#efef4e");
         $("#stars4").css("color", "#efef4e");
         $(".inputs2").text(reviewnum);
         $("#starnums2").val(reviewnum);
      });
      var textCountLimit = 10;
      var textLength = 0;
      $('textarea[name=content]').keyup(function() {
         // 텍스트영역의 길이를 체크
         textLength = $(this).val().trim().length;

         // 입력된 텍스트 길이를 #textCount 에 업데이트 해줌
         $('#textCount').text(textLength);

      });
      
      $("#sub").click(function() {
        
            var frm = document.registerFrm;
            frm.action="<%=ctxPath%>/mypage/orderreview.sg";
            frm.method="POST";
            frm.submit();
         
      });
      
      $("#sub2").click(function() {
    	  var frm = document.registerFrm2;
          frm.action="<%=ctxPath%>/mypage/orderreview.sg";
          frm.method="POST";
          frm.submit();
      });
      
      $("#sub3").click(function() {
    	 var seq = $("#seq").val();
    	 location.href='orderreview.sg?delseq='+seq;
      });
     

   });
   
   function funcmodal(index) {
      $("#modalproid").val($("#proid"+index).text());
      $("#modalprocolor").val($("#procolor"+index).text());
      $("#purchaseno").val($("#pcno"+index).text());
   }
   
   function funcmodal2(index) {
      $("#reviewComment2").text($("#contents"+index).text());
      
      var score = $("#scoreinput"+index).val();
      
      for(var i=0; i<score; i++){
         $("#stars"+i).css("color", "#efef4e");
      }
      $(".inputs2").text(score);
      $("#starnums2").val(score);
      
      $("#seq").val($("#seqre"+index).val());
   }   
   
</script>

<div class="container">
<div id="title">REVIEW</div>
   <div class="tab_wrap">
      <div class="tab_menu_container">
         <button class="tab_menu_btn1 tab_menu_btn on btn1" type="button">리뷰쓰기
            (<span>${recnt}</span>)</button>
         <button class="tab_menu_btn2 tab_menu_btn btn1" type="button">작성한
            리뷰 (<span>${writecnt}</span>)</button>
      </div>
      <!-- tab_menu_container e -->

      <div class="tab_box_container">
         <div class="tab_box1 tab_box on">
            <table id="table1" class="table">
               <thead>
                  <tr>
                     <th>상품이미지</th>
                     <th>상품명</th>
                     <th>리뷰</th>
                  </tr>
               </thead>
               <tbody>
                  <form name="registerFrm">

                     <c:forEach items="${listmap}" var="map" varStatus="status">
                        <c:if test="${map.ordernum ne no}">
                           <c:set var="no" value="${map.ordernum}" />

                           <tr id="iftr1" class="hidentr trcs">
                              <td colspan="3"><span style="font-size: 10pt; background-color: #e9e9e2; padding: 3px;">주문날짜:&nbsp;${map.purchaseday}</span></td>
                           </tr>
                        </c:if>
                        <tr>
                           <td><img src="${map.imgfilename}"></td>
                           <td class="noimg"><span id="proid${status.index}">${map.fk_productid}</span><br>
                              <span style="font-size: 10pt;">${map.productname}</span><br><span style="font-weight: bold; padding-left: 50px; font-size: 10pt;">[옵션:<span
                              id="procolor${status.index}">${map.color}</span>]</span>
                              <span id="pcno${status.index}" class="hispan">${map.purchaseno}</span>
                           </td>
                           <td class="noimg">
                              <button type="button" class="btn btn-info btn-lg" id="yesbtn"
                                 data-toggle="modal" data-target="#myModal"
                                 onclick="funcmodal(${status.index});">리뷰쓰기</button> <!-- Modal -->
                              <div id="myModal" class="modal fade" role="dialog">
                                 <div class="modal-dialog">


                                    <!-- Modal content-->
                                    <div class="modal-content">
                                       <div class="modal-header">
                                          <button type="button" class="close btnplall btnall"
                                             data-dismiss="modal">&times;</button>
                                          <h4 class="modal-title">리뷰쓰기</h4>
                                       </div>

                                       <div class="modal-body">

                                          <p style="font-weight: bold;">구매하신 상품은 만족하셨나요?</p>
                                          <div data-role="ratingbar" data-steps="2" id="stardiv">
                                             <ul id="starui">
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em" id="star0"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.1em;" id="star1"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.15em;"
                                                      id="star2"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.2em;" id="star3"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.25em;"
                                                      id="star4"></span></a></li>
                                                <li><span class="inputs">0</span><span>점</span></li>
                                             </ul>
                                          </div>
                                          <hr>
                                          <label for="reviewComment" class="title">리뷰를
                                             작성해주세요.</label>
                                          <textarea rows="10" cols="60" id="reviewComment"
                                             name="content"
                                             placeholder="텍스트 리뷰는 200pt 포토 리뷰는 500pt를 드려요!&#13;&#10;(10자 이상 작성시 리뷰 등록 가능)">
                                          </textarea>
                                          <hr>
                                          <input
                                             type="text" value="" id="modalproid" name="fk_productid" hidden="true" />
                                          <input type="text" value="" id="modalprocolor" name="color" hidden="true"/>
                                          <input type="text" value="" id="starnums" name="score" hidden="true"/>
                                          <input type="text" value="" id="purchaseno" name="fk_purchaseno" hidden="true"/>

                                       </div>
                                       <div class="modal-footer">
                                          <button type="submit" class="btn btn-default btnall"
                                             data-dismiss="modal" id="sub">리뷰 등록하기</button>
                                          <button type="button" class="btn btn-default btnall"
                                             data-dismiss="modal">닫기</button>
                                       </div>
                                    </div>

                                 </div>
                              </div>
                           </td>

                        </tr>
                     </c:forEach>
                  </form>
               </tbody>
            </table>

         </div>
         <div class="tab_box2 tab_box">
            <div>
               <table class="table table-striped">
                  <thead>
                     <tr id="titlehead">
                        <th>작성날짜</th>
                        <th>이미지</th>
                        <th>상품정보</th>
                        <th>리뷰내용</th>
                        <th>선택</th>
                     </tr>
                  </thead>
                  <Tbody>
                  <form name="registerFrm2">
                  <c:forEach items="${hashlist}" var="hash" varStatus="status">
                     <tr id="titlehead2" >
                        <td>${hash.writeday}</td>
                        <td><img class="store img-thumbnail"
                           src="${hash.imgfilename}"></td>
                        <td>
                           <span>${hash.fk_productid}</span>
                           <br> ${hash.productname}
                           <br> <span style="font-weight: bold;">[옵션:${hash.color}]</span>
                            <input type="text" hidden="true" value="${hash.score}" id="scoreinput${status.index}"/>
                            <input type="text" value="${hash.seq_review}" id="seqre${status.index}" name="seqreviewno" hidden="true"/>
                            
                        <td>
                           <span id="contents${status.index}">${hash.content}</span><br>
                        <td>
                            <button type="button" class="btn btn-info btn-lg yesbtn2"
                                 data-toggle="modal" data-target="#myModal2" onclick="funcmodal2(${status.index});">수정 / 삭제</button>
                           <div id="myModal2" class="modal fade" role="dialog">
                                 <div class="modal-dialog">


                                    <!-- Modal content-->
                                    <div class="modal-content">
                                       <div class="modal-header">
                                          <button type="button" class="close btnplall"
                                             data-dismiss="modal">&times;</button>
                                          <h4 class="modal-title">리뷰쓰기</h4>
                                       </div>

                                       <div class="modal-body">

                                          <p style="font-weight: bold;">구매하신 상품은 만족하셨나요?</p>
                                          <div data-role="ratingbar" data-steps="2" id="stardiv">
                                             <ul id="starui">
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em" id="stars0"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.1em;" id="stars1"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.15em;"
                                                      id="stars2"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.2em;" id="stars3"></span></a></li>
                                                <li><a href="#"><span
                                                      class="glyphicon glyphicon-star all"
                                                      style="font-size: 2.5em; padding: 0 0.25em;"
                                                      id="stars4"></span></a></li>
                                                <li><span class="inputs2">0</span><span>점</span></li>
                                             </ul>
                                          </div>
                                          <hr>
                                          <label for="reviewComment" class="title">리뷰를
                                             작성해주세요.</label>
                                          <textarea rows="10" cols="60" id="reviewComment2"
                                             name="content2" 
                                             placeholder="텍스트 리뷰는 200pt 포토 리뷰는 500pt를 드려요!&#13;&#10;(10자 이상 작성시 리뷰 등록 가능)">
                                          </textarea>
                                          <hr>
                                          <label for="addFile" class="title">사진첨부</label> <input
                                             type="file" id="addFile" name="reviewimg" /> 
                                          <input type="text" value="" id="starnums2" name="score2" hidden="true"/>
                                          <input type="text" value="" id="seq" name="seq_review" hidden="true"/>

                                       </div>
                                       <div class="modal-footer">
                                          <button type="submit" class="btn btn-default btnall"
                                             data-dismiss="modal" id="sub2">리뷰 수정하기</button>
                                          <button type="button" class="btn btn-default btnall"
                                             data-dismiss="modal" id="sub3">삭제</button>
                                             <button type="button" class="btn btn-default btnall"
                                             data-dismiss="modal">닫기</button>
                                       </div>
                                    </div>

                                 </div>
                              </div>
                            <br>
                        </td>
                     </tr>
                  </c:forEach>                  
                  </form>
                  <!-- </form> -->
                  </Tbody>
               </table>
            </div>
            <hr />
  
            <div align="center">
               ${pageBar}    
            </div>   
         </div>

      </div>
      <!-- tab_box_container e -->
   </div>
   <!-- tab_wrap e -->

</div>

<jsp:include page="../footer.jsp" />



















