function submits(){
	var bas = $("#basepath").val();
	var cancash = $("#cancash").val();
	var subvalue=$("#subvalue").val();
	
	if(typeof($("#subvalue").val()) == 'undefined'){
		alert("不满足条件");
		return ;
	}
	if(parseInt($("#subvalue").val()) < cancash){
		alert("最低申请金额必须大于:"+cancash+"元!");
		return;
	}
 	if(parseInt($("#subvalue").val()) > parseInt($("#spweibi").html())){
		alert("超出最大可提取金额");
		return;
	}
 	$.post(bas+"/user/toTixian",{subvalue:subvalue},function(data){
		if(data=='1'){
			alert("提现申请成功");
			window.location.href=bas+"/user/toUserCenterPage";
		}else if(data=='2'){
			alert("金额有误");
			return ;
		}else if(data=='3'){
			alert("维币不足");
			return;
		}else{
			alert("执行异常，请尝试刷新后从新请求");
			return;
		}
 	});
}