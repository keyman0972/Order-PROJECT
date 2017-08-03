<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/include.Taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<base target="_self" />
<s:include value="/WEB-INF/pages/include/include.Scripts.jsp" />
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.gridEditList.plugin.js"/>"></script>
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.validation.plugin.js"/>"></script>
<script type="text/javascript" src="<s:url value="/jquery/jquery.alphanumeric.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/ddsc.input.js"/>"></script>
<script language="javascript">
var oTable;
//畫面欄位檢核
function validate() {
	$("#frmSys02001K").validate("clearPrompt"); 
	
	$("#custId").validateRequired({fieldText:'<s:text name="custId" />'});
    
	for(var i = 0; i < $("#tbGrid").find('tr').length; i++){
		$("#itemId_" + i).validateRequired({fieldText:"<s:text name="itemId"/>", contactMessage:getRowNumText(i+1)});
		
		var orderQty = $("#orderQty_" + i).val().toNumber();//訂購
		var itemQty = $("#hidden_itemQty_" + i).val().toNumber();//庫存
		var bookQty = $("#hidden_itemBookQty_" + i).val().toNumber();//預訂
		var hidOrderQty = $("#hidden_orderQty_"+ i).val().toNumber();//原本數量

		if((orderQty - hidOrderQty) > (itemQty - bookQty)){
// 			alert();
			$("#orderQty_" + i).validate("sendPrompt", 
			{message:getRowNumText(i + 1) + '<s:text name="eC.0029"><s:param value="getText(\"orderQty\")"/><s:param value="getText(\"itemQty\")"/></s:text>' + '(' + itemQty + ')'});
		}
		$("#orderQty_" + i).validateRequired({fieldText:"<s:text name="orderQty"/>", contactMessage:getRowNumText(i+1)});
	}
	
    if(!$("#frmSys02001K").validate("isErrors")){
		fnSetOperate(oTable);
	}
    return $("#frmSys02001K").validate("showPromptWithErrors");
}
//小視窗
function chooseitemId(obj) {
	var index = fnGetObjIndex($(this));
	var params = "prodCode=<s:property value="labItemMst.itemId"/>&itemId=" + $("#itemId_"+index).val() ;
	var oRtn = popUpWindow({"popUpPath":"popupWindowKm","winType":"popUpWin01002K","params" : params});

	if (typeof(oRtn) != "undefined" && oRtn != null) {
		$("#itemId_"+index).val(oRtn.itemId);
		$("#itemName_"+index).html(oRtn.itemName);
		$("#itemPrice_"+index).html(oRtn.itemPrice);
		$("#itemPromoPrice_"+index).html(oRtn.itemPromoPrice);
	}
    return false;
}
//小視窗
function chooseCustId(obj) {
	var params = "prodCode=<s:property value="labOrderMst.labCustMst.custId"/>&custId=" + $("#custId").val() ;
	var oRtn = popUpWindow({"popUpPath":"popupWindowKm","winType":"popUpWin01003K","params" : params});
	
	if (typeof(oRtn) != "undefined" && oRtn != null) {
		$("#custId").val(oRtn.custId);
		$("#custName").html(oRtn.custName);
	}
    return false;
}
// 取客戶名稱
function getCustName() {
	var custId = $('#custId').val();
	if(custId != null){
    	$.ajax({
      		type: 'post',
	      	url:'<s:url value="/ajax/ajaxQuery/queryDataByParams.action" />',
	      	data:{queryName: 'findLabCustMst', params: '{custId: "' + custId + '"}'},
	      	success: function (rtn_data) {
	      		if(rtn_data.results.length == 1 && rtn_data.results[0] != "" && rtn_data.results[0] != null){
			  		$('#custName').html(rtn_data.results[0][1]);
	      		}else{
	      			$('#custName').html("");
	      		}
		 	},
    	});
	}else{
		$('#custName').html("");
	}
}
//取商品名稱
function getItemName(){
	var index = fnGetObjIndex($(this)); 
  	var itemId = $('#itemId_'+index).val();
	if(itemId != null){
		$.ajax({
			type: 'post',
			url:'<s:url value="/ajax/ajaxQuery/queryDataByParams.action" />',
			async:false,
			data:{queryName: 'findItemMstByKey', params: '{itemId: "' + itemId + '"}'},
			success: function (rtn_data) {
				if(rtn_data.results.length == 1 && rtn_data.results[0] != "" && rtn_data.results[0] != null){
					$("#itemName_"+index).html(rtn_data.results[0][1]);
					$("#itemPrice_"+index).html(rtn_data.results[0][2]);
					$("#itemPromoPrice_"+index).html(rtn_data.results[0][3]);
					$("#hidden_itemQty_"+ index).val(rtn_data.results[0][4]);
					$("#hidden_itemBookQty_"+ index).val(rtn_data.results[0][5]);
					
				}else{
					$("#itemName_" + index).html("<s:text name="eC.0037"/>");
					$("#itemPrice_" + index).html("");
					$("#itemPromoPrice_" + index).html("");
				}

			}
		});
	}else{
		$("#itemName_" + index).html("");
		$("#itemPrice_" + index).html("");
		$("#itemPromoPrice_" + index).html("");
	}
}
function updatePrice(){
	var index = fnGetObjIndex($(this));
	//訂購數量
	var orderQty = $("#orderQty_" + index).val().toNumber();
	//預定數量
	var bookQty = $("#hidden_itemBookQty_" + index).val().toNumber();
	//庫存
	var itemQty = $("#hidden_itemQty_" + index).val().toNumber();
	//商品價格
	var itemPrice = $("#itemPrice_"+ index).text().toNumber();
	//總金額
	var subTotal = 0;
	
	if($("#itemPromoPrice_"+ index).text().toNumber() != ""){
		itemPrice = $("#itemPromoPrice_"+ index).text().toNumber();
	}
	
	subTotal = accMul(itemPrice , orderQty);
	$("#subTotal_" + index).html(subTotal.toString().formatNunmeric(2));
	$("#hidden_subTotal_" + index).val(subTotal.toString().formatNunmeric(2));
	$("#hidden_orderPrice_" + index).val(itemPrice);
	updateAmt();
}
function updateAmt(){
	var orderAmt = 0;
	
	for(var i =0;i < $(".subTotal").size();i++){
		orderAmt = accAdd(orderAmt, $("#hidden_subTotal_"+ i).val().toNumber());
	}
	
	$("#hidden_orderAmt").val(orderAmt);
	$("#orderAmt").html($("#hidden_orderAmt").val().formatNunmeric(2));
}

$(document).ready(function() {
	oTable = $('#tblGrid').initEditGrid({height:'480'});
	
	$('#btnInsRow').click(function() {
		fnAddTableRow(oTable, function(newRow) {
// 			newRow;
		});
		return false;
	});
	
	$("#imgCustId").bind("click", chooseCustId);
	$(".imgItemId").bind("click", chooseitemId);
	
	$('#custId').bind("change", getCustName);
	$('.AjaxitemId').bind("change", getItemName);
	$('.orderQty').bind("change", updatePrice);
	
	// 單筆刪除
	$('.imgDelete').bind("click", function(event) {
		fnDelTableRow($(this), oTable);
		return false;
	});

	//多筆刪除
	$('#btnRmvRow').click(function() {
		fnDelAllRow(oTable);
		return false;
	});
});
</script>
</head>
<body>
<s:form id="frmSys02001K" method="post" theme="simple" action="%{progAction}" target="ifrConfirm">
<s:hidden name="labOrderMst.ver" />
 	<div class="progTitle"> 
		<!-- 程式標題 --> <s:include value="/WEB-INF/pages/include/include.EditTitle.jsp" /> <!-- 程式標題 -->
    </div>
    <div id="tb">
    <table width="100%" border="0" cellpadding="4" cellspacing="0" >
			<tr class="trBgOdd">
				<td width="20%" class="colNameAlign required">*<s:text name="orderDate" />：</td>
				<td width="30%">
					<s:property value="labOrderMst.orderDate" />
					<s:hidden name="labOrderMst.orderDate"/>
				</td>
				<td width="20%" class="colNameAlign required">*<s:text name="orderId" />：</td>
				<td width="30%">
					<s:property value="labOrderMst.orderId" />
					<s:hidden name="labOrderMst.orderId"/>
					<s:hidden name="labOrderMst.orderStatus" />
				</td>
			</tr>
			<tr class="trBgEven">
				<td width="20%" class="colNameAlign required">*<s:text name="custId" />：</td>
				<td width="30%">
					<s:textfield id="custId" name="labOrderMst.labCustMst.custId" cssClass="enKey" size="16" maxlength="16" />
					<input type="image" id="imgCustId" class="imgPopUp" src="<s:url value="/image_icons/search.png"/>" />
					<s:label id="custName" name="labOrderMst.labCustMst.custName" />
				</td>
				<td width="20%" class="colNameAlign required">*<s:text name="orderAmt" />：</td>
				<td width="30%">
					<s:label id="orderAmt" value="%{labOrderMst.orderAmt}"/>
					<s:hidden id="hidden_orderAmt" name="labOrderMst.orderAmt" value="%{labOrderMst.orderAmt}" />
				</td>
			</tr>
	</table>
    <fieldset style="-moz-border-radius:4px;">
    <div style="width:100%; display:block; float:left; background-color:#b7d3d6">
        <button class="btnInsRow" id="btnInsRow"><s:text name="fix.00255" /></button>
        <button class="btnRmvRow" id="btnRmvRow"><s:text name="fix.00256" /></button>
    </div>
    <table id="tblGrid" width="100%" border="0" cellpadding="2" cellspacing="1">
        <thead>
            <tr align="center" bgcolor="#e3e3e3">
                <th width="30"><s:text name="fix.00164" /></th>
                <th width="20">&nbsp;</th>
                <th width="28%"><s:text name="itemId" /></th>
                <th width="15%"><s:text name="itemPrice" /></th>
                <th width="15%"><s:text name="itemPromoPrice" /></th>
                <th width="15%"><s:text name="orderQty" /></th>
                <th><s:text name="subTotal" /></th>
                <th style="display: none;">&nbsp;</th>          
            </tr>
        </thead>
		<tbody id="tbGrid">
	        <s:iterator value="labOrderMst.labOrderItemList" status="stat">
	        <tr>
	            <td id="SN" align="center" align="center" width="30"><s:property value="#stat.index + 1" /></td>
	            <td align="center" width="20">
	                <input class="imgDelete" type="image" src="<s:url value="/image_icons/delete.png"/>" width="16" height="16" title="<s:text name="fix.00182"/>">
	            </td>
	            <td width="28%">
	                <s:textfield id="%{'itemId_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemId'}" value="%{labOrderMst.labOrderItemList[#stat.index].labItemMst.itemId}" cssClass="enKey AjaxitemId"  maxLength="32" size="16" />
	                <input type="image" id="imgItemId_<s:property value="#stat.index" />" class="programPopUp imgItemId" src="<s:url value="/image_icons/search.png"/>" />
	            	<s:label id="%{'itemName_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemName'}" value="%{labOrderMst.labOrderItemList[#stat.index].labItemMst.itemName}" cssClass="labelCut"/>
	            </td>
	            <td width="15%" align="right">
	            	<s:label id="%{'itemPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPrice'}" value="%{labOrderMst.labOrderItemList[#stat.index].labItemMst.itemPrice}" cssClass="labelCut" />
	            	<s:hidden id="%{'hidden_itemPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPrice'}" />
	            </td>
	            <td width="15%" align="right">
	            	<s:label id="%{'itemPromoPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPromoPrice'}" value="%{labOrderMst.labOrderItemList[#stat.index].labItemMst.itemPromoPrice}" cssClass="labelCut" />
	            	<s:hidden id="%{'hidden_itemPromoPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPromoPrice'}" />
	            </td>
	            <td width="15%">
	            	<s:textfield id="%{'orderQty_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderQty'}" value="%{labOrderMst.labOrderItemList[#stat.index].orderQty}" cssClass="numOnly orderQty"  maxLength="32" size="16"/>
	            	<s:hidden id="%{'hidden_orderQty_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderQty'}" value="%{orderQty}" />
	            </td>
	            <td align="right">
	            	<s:label id="%{'subTotal_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].subTotal'}" value="%{labOrderMst.labOrderItemList[#stat.index].subTotal}" cssClass="labelCut subTotal" />
	            	<s:hidden id="%{'hidden_subTotal_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].subTotal'}" />
	            </td>
	            <td style="display: none;">
	            	<s:hidden id="%{'operate_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].operate'}"/>
		            <s:hidden id="%{'ver_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].ver'}"/>
	                <s:hidden id="%{'orderItemOid_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderItemOid'}" />
	            	<s:hidden id="%{'hidden_itemQty_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemQty'}" value="%{labItemMst.itemQty}" />
					<s:hidden id="%{'hidden_itemBookQty_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemBookQty'}" value="%{labItemMst.itemBookQty}" />
					<s:hidden id="%{'hidden_orderPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderPrice'}" />
	            </td>
	        </tr>
	        </s:iterator>   
		</tbody>
    </table>
    </fieldset>
    </div>
	<!-- 按鍵組合 --> 
	<s:include value="/WEB-INF/pages/include/include.EditButton.jsp" />
	<!-- 按鍵組合 -->
</s:form>
<iframe id="ifrConfirm" name="ifrConfirm" width="100%" height="768" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="display:none; border: 0px none"></iframe>
</body>
</html>