<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>退款订单查询</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  
	<style type="text/css">
		#contentDiv{margin:15px auto auto auto;} 
	</style>
</head>
<body>
  <div class="demo-content" id="contentDiv">
	<div class="span16 doc-content" id="formDiv">  
	<h1>退款订单查询</h1>
	  <form id="J_Form" action="/mPay/simulator/refundQuery" method="post" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label"><s>*</s>收单机构号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="acquirer_id">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户标识号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="merchant_id">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户订单号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="out_trade_no">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">支付宝订单号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="trade_no">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>退款请求号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="out_request_no">
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