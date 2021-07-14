<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemCatList" title="商品列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/cat/list/0',method:'get',pageSize:30,toolbar:itemCatListToolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">ID</th>
        	<th data-options="field:'name',width:100">商品分类名称</th>
            <th data-options="field:'created',width:130,align:'center',formatter:EGO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:EGO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-cart-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>


    function getSelectionsIds(){
    	var itemList = $("#itemCatList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var itemCatListToolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	EGO.createWindow({
        		url : "/item-cat-add",
        	});
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	$.messager.alert('提示','该功能未实现!');
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品规格!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品规格吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/item/cat/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除商品规格成功!',undefined,function(){
            					$("#itemCatList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>