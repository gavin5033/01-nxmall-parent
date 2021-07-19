var TT = EGOU = {
	checkLogin : function(){
		var _ticket = $.cookie("NX_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8085/user/token/" + _ticket,
			//dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到奈学！<a id='my_logout_id' href=\"http://localhost:8085/user/logout/"+_ticket+"\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}




$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
	$(".link-logout").live("click",function(){
		var href=$(this).attr("href");
//		$.post(href,function(data){
//			if(data.status==200){
//				$("#loginbar").html('您好！欢迎来到奈学！<a href="javascript:login()">[登录]</a>&nbsp;<a href="javascript:regist()">[免费注册]</a>');
//			}
//		})
		$.ajax({
			url:href,
			type:'post',
			// jsonp:'callback',
			// dataType:'jsonp',
			// jsonpCallback:'abc',
			success:function(data){
				if(data.status==200){
					$("#loginbar").html('您好！欢迎来到奈学！<a href="javascript:login()">[登录]</a>&nbsp;<a href="javascript:regist()">[免费注册]</a>');
				}
			}
		});
		return false;
	})
});