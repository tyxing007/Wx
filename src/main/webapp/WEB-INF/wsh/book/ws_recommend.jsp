<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en" ng-app>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维书会-书简介</title>
<%@include file="/extjsp/jsphead.jsp" %>
</head>
<body>

<div class="container">
  <ul class="shu_list shu_list_jj">
    <li class="clear">
      <div class="left">
        	<c:if test="${book.bookPicId==null}">
							<img src="<%=basePath%>/reswsh/images/book_base_pic.jpg" alt="">
						</c:if>
			<c:if test="${book.bookPicId !=null}">
							<img src="${book.bookPicId}" alt="">
			</c:if>
      </div>
      <div>
        <h4>${book.bookTitle}</h4>
        <p class="date">
          <span class="left">${book.bookAuthor}  著</span>
          <span class="right"><fmt:formatDate value='${book.bookPubdate}' type="date" dateStyle='medium' /></span>
        </p>
        
        <p class="buttons">
        <c:if test="${canListen=='Y'}">
        	  <a href="<%=basePath %>/bookc/gotoListenBook/${book.bookId}" class="btns">去听书</a>
        </c:if>
        
        <c:if test="${canListen=='N'}">
			  <a class="btns"> 整理中... </a>        
        </c:if>
        </p>
        
      </div>
    </li>
  </ul>
  <ul class="shu_list_tj">
   <li class="bt"><img src="<%=basePath %>/reswsh/images/tui.png" alt="" width="30">推荐理由</li>
   <c:if test="${tiplist.tipInfo1 != null && tiplist.tipInfo1 !=''}"><li> <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tiplist.tipInfo1}</p></li></c:if>
   <c:if test="${tiplist.tipInfo2 != null && tiplist.tipInfo2 !=''}"> <li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tiplist.tipInfo2}</p></li></c:if>
   <c:if test="${tiplist.tipInfo3 != null && tiplist.tipInfo3 !=''}"> <li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tiplist.tipInfo3}</p></li></c:if>
   <c:if test="${tiplist.tipInfo4 != null && tiplist.tipInfo4 !=''}"> <li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tiplist.tipInfo4}</p></li></c:if>
   <c:if test="${tiplist.tipInfo5 != null && tiplist.tipInfo5 !=''}"> <li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tiplist.tipInfo5}</p></li></c:if>
  </ul>
</div>
</body>
</html>