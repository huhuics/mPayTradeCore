<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资金管理查询</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  
	<style type="text/css">
		#contentDiv{ margin:15px auto auto auto;position:relative;} 
	</style>
</head>
<body>
  <div class="demo-content" id="contentDiv">
	<div class="span16 doc-content" id="formDiv">  
	<h1>资金管理查询</h1>
	  <form id="J_Form" action="/mPay/simulator/funMngQuery" method="post" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label"><s>*</s>接口类型：</label>
		  <div class="controls">
				<input type="text" class="input-large" name="interfacetype" value="${interfacetype}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label"><s>*</s>查询类型：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="querytype" value="${querytype}">
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">工作日：</label>
		  <div class="controls">
			<input type="text" class="input-large" name="wrkdate" value="${wrkdate}">
		  </div>
		</div>
		
		<div class="control-group">
		<label class="control-label">*接口类型：</label>
		  <div class="controls">
			 1：代表查询工作日
          	 2：代表查询报表数据
		  </div>
		</div>
		<div class="control-group">
		<label class="control-label">*查询类型：</label>
		  <div class="controls">
				11：代表返回当前系统工作日
		  </div>
		  <div class="controls">
                12：代表返回当前系统下一个工作日
		  </div>
		</div>
		<div class="control-group">
		<label class="control-label">&nbsp;&nbsp;&nbsp;      </label>
		  <div class="controls">
                21：返回指定日期的付款行报表数据
		  </div>
		  <div class="controls">
                22：返回指定日期的归集行报表数据
		  </div>
		</div>
		<div class="control-group">
		<label class="control-label">&nbsp;&nbsp;&nbsp;      </label>
		  <div class="controls">
                23：返回指定日期的转清机构号报表数据
		  </div>
		</div>
		<div class="control-group">
		<label class="control-label">*工作日 ：</label>
		  <div class="controls">
				用于查询付款行和归集行报表数据
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