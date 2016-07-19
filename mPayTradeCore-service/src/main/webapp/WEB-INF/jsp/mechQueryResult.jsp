<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>商户信息查询结果</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
<style type="text/css">
	#contentDiv{ margin:0 auto; width:800px; height:1000px} 
</style>
</head>
<body>
  <div class="demo-content" id="#contentDiv">
	<div class="span16 doc-content" id="formDiv">  
	<h1>商户信息查询结果</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">收单机构编号：</label>
		  <div class="controls">
			<span class="control-text">${acquirer_id}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户标识号：</label>
		  <div class="controls">
			<span class="control-text">${sub_merchant_id}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户外部编号：</label>
		  <div class="controls">
			<span class="control-text">${external_id}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户名称：</label>
		  <div class="controls">
			<span class="control-text">${name}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">商户简称：</label>
		  <div class="controls">
			<span class="control-text">${alias_name}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">客服电话：</label>
		  <div class="controls">
			<span class="control-text">${service_phone}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">联系人名称：</label>
		  <div class="controls">
			<span class="control-text">${contact_name}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">联系人电话：</label>
		  <div class="controls">
			<span class="control-text">${contact_phone}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">联系人手机号：</label>
		  <div class="controls">
			<span class="control-text">${contact_mobile}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">联系人邮箱：</label>
		  <div class="controls">
			<span class="control-text">${contact_email}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">经营类目：</label>
		  <div class="controls">
			<span class="control-text">${category_id}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">商户来源标识：</label>
		  <div class="controls">
			<span class="control-text">${source}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">备注信息：</label>
		  <div class="controls">
			<span class="control-text">${memo}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">结果码：</label>
		  <div class="controls">
			<span class="control-text">${code}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">结果信息：</label>
		  <div class="controls">
			<span class="control-text">${msg}</span>
		  </div>
		</div>  
	  </form>
	<script src="http://g.tbcdn.cn/fi/bui/jquery-1.8.1.min.js"></script>
	<script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
	<script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>
  <!-- script start --> 
	<script type="text/javascript">
	      BUI.use('bui/form',function(Form){
	      
	      new Form.Form({
	        srcNode : '#J_Form'
	      }).render();
	      
	  });  
	      
	</script>
<!-- script end -->
	</div>
<!-- script end -->
  </div>
</body>
</html>  