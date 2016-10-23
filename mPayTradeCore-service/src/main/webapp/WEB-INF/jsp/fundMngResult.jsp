<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资金管理查询返回结果</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
<style type="text/css">
	#contentDiv{ margin:15px auto auto auto;} 
	
table{
	width:800px;
}
table tr th,td {
	border: 1px solid black;
	text-align: center;
}

</style>

</head>
<body>
  <div class="demo-content" id="#contentDiv">
	<div class="span16 doc-content" id="formDiv">
	<c:choose>
	<c:when test="${flag == 1}">
	<h1>查询工作日返回结果</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">响应码：</label>
		  <div class="controls">
			<span class="control-text">${map["RespCode"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">响应码描述：</label>
		  <div class="controls">
			<span class="control-text">${map["RespDesc"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">工作日：</label>
		  <div class="controls">
			<span class="control-text">${map["SysDate"]}</span>
		  </div>
		</div>
	  </form>
	</c:when>
	<c:when test="${flag == '2'}">  
	<c:choose>
	<c:when test="${queryType eq '21' and map.RespCode eq '0000'}">
	<h1>付款行报表</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">响应码：</label>
		  <div class="controls">
			<span class="control-text">${map["RespCode"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">响应码描述：</label>
		  <div class="controls">
			<span class="control-text">${map["RespDesc"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">状态码：</label>
		  <div class="controls">
			<span class="control-text">${map["StatusCode"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">状态码描述：</label>
		  <div class="controls">
			<span class="control-text">${map["StatusDesc"]}</span>
		  </div>
		</div>
	  </form>
	  <table border="1px solid">
	  	<thead>
			<tr>
			<th>对账日期</th>
			<th>行号</th>
			<th>账号</th>
			<th>户名</th>
			<th>入_交易资金</th>
			<th>出_交易资金</th>
			<th>入_退款资金</th>
			<th>出_退款资金</th>
			<th>其他收入</th>
			<th>其他支出</th>
			<th>利息收入</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="report" items="${reports}" >   
          <tr>   
             <td> <c:out value="${report.key0}" /> </td>   
             <td> <c:out value="${report.key1}" /> </td>   
             <td> <c:out value="${report.key2}" /> </td>   
             <td> <c:out value="${report.key3}" /> </td>   
             <td> <c:out value="${report.key4}" /> </td>   
             <td> <c:out value="${report.key5}" /> </td>   
             <td> <c:out value="${report.key6}" /> </td>   
             <td> <c:out value="${report.key7}" /> </td>   
             <td> <c:out value="${report.key8}" /> </td>   
             <td> <c:out value="${report.key9}" /> </td>   
             <td> <c:out value="${report.key10}" /> </td>   
          </tr>   
		 </c:forEach>   
		 </tbody>
	  </table>
	  </c:when>
	  <c:when test="${queryType eq '22' and map.RespCode eq '0000'}">
		<h1>归集行报表</h1>
		  <form id="J_Form" action="" class="form-horizontal">
			<div class="control-group">
			  <label class="control-label">响应码：</label>
			  <div class="controls">
				<span class="control-text">${map["RespCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">响应码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["RespDesc"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusDesc"]}</span>
			  </div>
			</div>
		  </form>
	  	<table>
	  	<thead>
			<tr>
			<th>对账日期</th>
			<th>行号</th>
			<th>账号</th>
			<th>户名</th>
			<th>入_商户资金</th>
			<th>出_商户资金</th>
			<th>应收手续费</th>
			<th>实收手续费</th>
			<th>出_退款资金</th>
			<th>利息收入</th>
			<th>当天入账的备付金</th>
			<th>入_其他支出</th>
			<th>出_其他收入</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="report" items="${reports}" >   
          <tr>   
             <td> <c:out value="${report.key0}" /> </td>   
             <td> <c:out value="${report.key1}" /> </td>   
             <td> <c:out value="${report.key2}" /> </td>   
             <td> <c:out value="${report.key3}" /> </td>   
             <td> <c:out value="${report.key4}" /> </td>   
             <td> <c:out value="${report.key5}" /> </td>   
             <td> <c:out value="${report.key6}" /> </td>   
             <td> <c:out value="${report.key7}" /> </td>   
             <td> <c:out value="${report.key8}" /> </td>   
             <td> <c:out value="${report.key9}" /> </td>   
             <td> <c:out value="${report.key10}" /> </td>   
             <td> <c:out value="${report.key11}" /> </td>   
             <td> <c:out value="${report.key12}" /> </td>   
          </tr>   
		 </c:forEach>   
		 </tbody>
	  </table>
	  </c:when>
	  <c:when test="${queryType eq '23' and map.RespCode eq '0000'}">
		<h1>转清机构清算报表</h1>
		  <form id="J_Form" action="" class="form-horizontal">
			<div class="control-group">
			  <label class="control-label">响应码：</label>
			  <div class="controls">
				<span class="control-text">${map["RespCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">响应码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["RespDesc"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusDesc"]}</span>
			  </div>
			</div>
		  </form>
	  	<table>
	  	<thead>
			<tr>
			<th>对账日期</th>
			<th>转清机构编号</th>
			<th>交易资金</th>
			<th>退款交易资金</th>
			<th>手续费</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="report" items="${reports}" >   
          <tr>   
             <td> <c:out value="${report.key0}" /> </td>   
             <td> <c:out value="${report.key1}" /> </td>   
             <td> <c:out value="${report.key2}" /> </td>   
             <td> <c:out value="${report.key3}" /> </td>   
             <td> <c:out value="${report.key4}" /> </td>   
          </tr>   
		 </c:forEach>   
		 </tbody>
	  </table>
	  </c:when>
	  <c:otherwise>
	  	<h1>返回结果</h1>
		  <form id="J_Form" action="" class="form-horizontal">
			<div class="control-group">
			  <label class="control-label">响应码：</label>
			  <div class="controls">
				<span class="control-text">${map["RespCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">响应码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["RespDesc"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusCode"]}</span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label">状态码描述：</label>
			  <div class="controls">
				<span class="control-text">${map["StatusDesc"]}</span>
			  </div>
			</div>
		  </form>
	  </c:otherwise>
	  </c:choose>
	 </c:when>
	 <c:otherwise>
	 <h1>返回结果</h1>
	  <form id="J_Form" action="" class="form-horizontal">
		<div class="control-group">
		  <label class="control-label">响应码：</label>
		  <div class="controls">
			<span class="control-text">${map["RespCode"]}</span>
		  </div>
		</div>
		<div class="control-group">
		  <label class="control-label">响应码描述：</label>
		  <div class="controls">
			<span class="control-text">${map["RespDesc"]}</span>
		  </div>
		</div>
	  </form>
	 </c:otherwise>
	 </c:choose>
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