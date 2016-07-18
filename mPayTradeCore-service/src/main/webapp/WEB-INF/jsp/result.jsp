<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>业务结果</title>
 
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
  <style type="text/css">
	#contentDiv{ margin:0 auto; width:800px; height:1000px} 
</style>
</head>
<body>
  <div class="demo-content" id="contentDiv">
 
    <div class="doc-content">
      <h1>业务结果</h1>
      <hr>
      <div class="form-horizontal form-horizontal-simple">
      <c:choose>
      	<c:when test="${code eq '10000'}">
	      <!-- 业务成功 -->
		  <div class="row">
			<div class="span10">
				<div class="tips tips-large tips-success">
				  <span class="x-icon x-icon-success"><i class="icon icon-ok icon-white"></i></span>
				  <div class="tips-content">
					<h2>业务成功</h2>
					<p class="auxiliary-text">
					  你可以继续操作以下内容：
					</p>
					<p>
					  <a class="direct-lnk" title="返回菜单" href="/mPay/simulator/menu">返回菜单</a>
					</p>
				  </div>
				</div>
			</div> 
		  </div>
		  <div class="row detail-row">
	        <div class="span8">
	          <label class="control-label">收单机构编号：</label>
	          <div class="controls">
	          <span class="control-text">${acquirerId}</span>
	          </div>
	        </div>
	        <div class="span8">
	          <label class="control-label">商户标识号：</label>
	          <div class="controls">
	          <span class="control-text">${merchantId}</span>
	          </div>
	        </div>
		  </div>
		  <div class="row detail-row">
	        <div class="span8">
	          <label class="control-label">商户订单号：</label>
	          <div class="controls">
	          <span class="control-text">${outTradeNo}</span>
	          </div>
	        </div>
	        <div class="span8">
	          <label class="control-label">支付宝订单号：</label>
	          <div class="controls">
	          <span class="control-text">${tradeNo}</span>
	          </div>
	        </div>
		   </div>
		   <!-- end 业务成功 -->
      	<c:if test="${not empty qrFilePath}">
	      	<img src="${qrFilePath}" height="256px" width="256px">
      	</c:if>
      	</c:when>
      	<c:otherwise>
	      	<!-- 业务失败 -->
			<div class="row">
				<div class="span10">
					<div class="tips tips-large tips-warning">
					  <span class="x-icon x-icon-error">×</span>
					  <div class="tips-content">
						<h2>业务失败</h2>
						<p class="auxiliary-text">
						  你可以继续操作以下内容：
						</p>
						<p>
						  <a class="direct-lnk" title="返回菜单" href="/mPay/simulator/menu">返回菜单</a>
						</p>
					  </div>
					</div>
				</div> 
			</div> 
			
			<div class="row detail-row">
	        <div class="span8">
	          <label class="control-label">收单机构编号：</label>
	          <div class="controls">
	          <span class="control-text">${acquirerId}</span>
	          </div>
	        </div>
	        <div class="span8">
	          <label class="control-label">商户标识号：</label>
	          <div class="controls">
	          <span class="control-text">${merchantId}</span>
	          </div>
	        </div>
		  </div>
		  <div class="row detail-row">
	        <div class="span8">
	          <label class="control-label">商户订单号：</label>
	          <div class="controls">
	          <span class="control-text">${outTradeNo}</span>
	          </div>
	        </div>
	        <div class="span8">
	          <label class="control-label">支付宝订单号：</label>
	          <div class="controls">
	          <span class="control-text">${tradeNo}</span>
	          </div>
	        </div>
		   </div>
		   <div class="row detail-row">
				<div class="span8">
				  <label class="control-label">结果码：</label>
				  <div class="controls">
				  <span class="control-text">${code}</span>
				  </div>
				</div>
				<div class="span8">
				  <label class="control-label">结果信息：</label>
				  <div class="controls">
				  <span class="control-text">${msg}</span>
				  </div>
				</div>
			</div>
			<div class="row detail-row">
				<div class="span8">
				  <label class="control-label">子结果码：</label>
				  <div class="controls">
				  <span class="control-text">${subCode}</span>
				  </div>
				</div>
				<div class="span8">
				  <label class="control-label">子信息：</label>
				  <div class="controls">
				  <span class="control-text">${subMsg}</span>
				  </div>
				</div>
			</div>
			<!-- end 业务失败 -->
      	</c:otherwise>
      </c:choose>
    
      </div>
    </div>
      
<!-- script end -->
  </div>
</body>
</html>  