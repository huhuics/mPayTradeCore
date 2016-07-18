<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>订单查询结果</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  
</head>
<body>
  <div class="demo-content">
	<div class="span16 doc-content" id="formDiv">  
	<h1>订单查询结果</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">买家支付宝账号：</label>
		  <div class="controls">
			<input type="text" class="input-large" value="${buyerLogonId}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商家订单号：</label>
		  <div class="controls">
			<input type="text" class="input-large" value="${outTradeNo}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">交易状态：</label>
		  <div class="controls">
			<input type="text" class="input-large" value="${tradeStatus}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">订单金额：</label>
		  <div class="controls">
			<input type="text" class="input-large" value="${totalAmount}" readonly="">
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