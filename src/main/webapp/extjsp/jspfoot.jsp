<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<ul class="wsh_nav">
	<li><a href="#"><span
			class="glyphicon glyphicon-menu-hamburger"></span>维</a>
		<div class="wsh_nav_pop">
			<p>
				<a href="<%=basePath %>/wei/weiList/1">维简介</a>
			</p>
			<p>
				<a href="<%=basePath %>/wei/weiList/2">维作品</a>
			</p>
			<p>
				<a href="<%=basePath %>/wei/weiList/3">维推荐</a>
			</p>
			<p>
				<a href="<%=basePath %>/wei/weiList/4">维课堂</a>
			</p>
			<p>
				<a href="<%=basePath %>/wei/weiList/5">维公益</a>
			</p>
<!-- 			<p> -->
<%-- 				<a href="<%=basePath %>/wei/weiList/6">维荐书</a> --%>
<!-- 			</p> -->
			<p>
				<a href="<%=basePath %>/wei/weiList">全部</a>
			</p>
			<span class="wsh_nav_arrow"></span> <span class="wsh_nav_arrow2"></span>
		</div></li>

	<li><a href="<%=basePath%>/bookc/getAllList"><span
			class="glyphicon glyphicon-menu-hamburger"></span>书</a></li>
	<li><a href="<%=basePath%>/user/toUserCenterPage"><span
			class="glyphicon glyphicon-menu-hamburger"></span>会</a></li>
</ul>
<script>
  $(function(){
    $(".wsh_nav li").click(function(e){
      $(".wsh_nav_pop").slideUp("fast");
      $(this).find(".wsh_nav_pop").slideDown("fast");
      e.stopPropagation();
    })
  })
  $(".wsh_nav_pop").click(function(e){
    e.stopPropagation();
  })
  $("body").click(function(){
    $(".wsh_nav_pop").slideUp("fast");
  })

</script>