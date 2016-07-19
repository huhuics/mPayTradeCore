<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>商户入驻</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  
	<style type="text/css">
		#contentDiv{ margin:0 auto; width:800px; height:1000px} 
	</style>
</head>
<body>
  <div class="demo-content" id="contentDiv">
	<div class="span16 doc-content" id="formDiv">  
	<h1>条码支付</h1>
	  <form id="J_Form" action="/mPay/simulator/mechCreate" method="post" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户外部编号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="external_id" value="${external_id}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>收单机构编号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="acquirer_id" value="${acquirer_id}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户名称：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="name" value="${name}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户简称：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="alias_name" value="${alias_name}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>客服电话：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="service_phone" value="${service_phone}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">联系人名称：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="contact_name" value="${contact_name}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">联系人电话：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="contact_phone" value="${contact_phone}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">联系人手机：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="contact_mobile" value="${contact_mobile}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">联系人邮箱：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="contact_email" value="${contact_email}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>经营类目：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="category_id" value="${category_id}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户来源标识：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="source" value="${source}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户备注：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="memo" value="${memo}">
		  </div>
		</div>
		<div class="row actions-bar">       
			<div class="form-actions span13 offset3">
			  <button type="submit" class="button button-primary">提交</button>
			  <button type="reset" class="button">重置</button>
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