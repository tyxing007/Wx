<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" ng-app>
<head>
<meta charset="utf-8">
<title>推广大使-推广码</title>
<%@include file="/extjsp/jsphead.jsp"%>
</head>
<body class="tg_bg">
<div class="tuiguangma_wrap">
  <div class="tuiguangma">
    <div class="avatar">
      <p class="img"><img src="${userInfo.headPic}" alt=""></p>
      <p>推广大使：<span>${userInfo.nickname}</span></p>
    </div>
    <div class="tuiguangma_code">
      <p><img src="${qr_url}" alt=""></p>
      <p>邀请您扫码(或长按二维码)，加入维书会！</p>
      <p>中国软实力研究权威李维老师每年带你读：</p>
      <p class="book">50本好书</p>
    </div>
  </div>
  <p class="notice">温馨提示：长按二维码白色区域，将图片保存到本地，便于以后发给好友！</p>
</div>

</body>
</html>