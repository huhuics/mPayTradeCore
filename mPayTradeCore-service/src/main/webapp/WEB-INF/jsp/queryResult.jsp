<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>订单查询结果</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
<style type="text/css">
	#contentDiv{ margin:0 auto; width:800px; height:1000px} 
</style>
</head>
<body>
  <div class="demo-content" id="#contentDiv">
	<div class="span16 doc-content" id="formDiv">  
	<h1>订单查询结果</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">买家支付宝账号：</label>
		  <div class="controls">
			<span class="control-text">${buyerLogonId}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">商户订单号：</label>
		  <div class="controls">
			<span class="control-text">${outTradeNo}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">交易状态：</label>
		  <div class="controls">
			<span class="control-text">${tradeStatus}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">订单金额：</label>
		  <div class="controls">
			<span class="control-text">${totalAmount}</span>
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
		<div class="control-group">
		  <label class="control-label">子结果码：</label>
		  <div class="controls">
			<span class="control-text">${subCode}</span>
		  </div>
		</div>  
		<div class="control-group">
		  <label class="control-label">子信息：</label>
		  <div class="controls">
			<span class="control-text">${subMsg}</span>
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