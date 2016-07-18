<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>订单查询</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  
	<style type="text/css">
		
	</style>
</head>
<body>
  <div class="demo-content">
	<div class="span16 doc-content" id="formDiv">  
	<h1>条码支付</h1>
	  <form id="J_Form" action="/mPay/simulator/precreate" method="post" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label"><s>*</s>收单机构号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="acquirer_id" value="${acquirer_id}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户标识号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="merchant_id" value="${merchant_id}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>支付场景：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="scene" value="scan_code" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户订单号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="out_trade_no" value="${out_trade_no}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>订单标题：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="subject" value="${subject}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>订单总金额(元)：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="total_amount" data-rules="{required:true}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>支付通知URL：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="notify_url" value="${notify_url}" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">订单描述：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="body"  value="购买商品3件共20.00元" readonly="">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>商户门店编号：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="store_id" value="${store_id }" readonly="">
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