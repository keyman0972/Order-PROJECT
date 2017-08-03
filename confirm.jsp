<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/include.Taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<base target="_self" />
<s:include value="/WEB-INF/pages/include/include.Scripts.jsp" />
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.gridList.plugin.js"/>"></script>
<script language="javascript">
$(document).ready(function() {
    $("#tblGrid").initGrid({height:'480'});   
});
</script>
</head>
<body>
<s:form method="post" theme="simple" action="%{progAction}">
	<s:hidden name="labOrderMst.ver" />
	<div class="progTitle"> 
       <!-- 程式標題 --> <s:include value="/WEB-INF/pages/include/include.ConfirmTitle.jsp" /> <!-- 程式標題 -->
    </div>
    <div id="tb">
    <table width="100%" border="0" cellpadding="4" cellspacing="0" >
			<tr class="trBgOdd">
				<td width="20%" class="colNameAlign required">*<s:text name="orderDate" />：</td>
				<td width="30%">
					<s:property  value="labOrderMst.orderDate" />
					<s:hidden name="labOrderMst.orderDate"/></td>
				<td width="20%" class="colNameAlign required">*<s:text name="orderId" />：</td>
				<td width="30%">
					<s:property value="labOrderMst.orderId" />
					<s:hidden name="labOrderMst.orderId"/></td>
			</tr>
			<tr class="trBgEven">
				<td width="20%" class="colNameAlign required">*<s:text name="custId" />：</td>
				<td width="30%">
					<s:property  value="labOrderMst.labCustMst.custId" />-<s:property  value="labOrderMst.labCustMst.custName" />
					<s:hidden name="labOrderMst.labCustMst.custId"/>
				</td>
				<td width="20%" class="colNameAlign required">*<s:text name="orderAmt" />：</td>
				<td width="30%">
					<s:label id="orderAmt" name="labOrderMst.orderAmt"/>
					<s:hidden id="orderAmtHidden" name="labOrderMst.orderAmt"/>
				</td>
		</table>
	<table width="100%" border="0" cellpadding="4" cellspacing="0" >
		<tbody>
		</tbody> 
    </table>
    <fieldset style="-moz-border-radius:4px;">
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
	            <td id="SN" align="center" width="30"><s:property value="#stat.index + 1" /></td>
	            <td align="center" width="20">
	                <label>
	                    <s:if test="operate eq \"insert\""><s:text name="fix.00001"/></s:if>
	                    <s:elseif test="operate eq \"update\""><s:text name="fix.00185"/></s:elseif>
	                    <s:elseif test="operate eq \"delete\""><s:text name="fix.00182"/></s:elseif>
	                    <s:else>&nbsp;</s:else>
	                </label>
	            </td>
	            <td width="28%">
	                <s:label id="%{'itemId' + #stat.index}" value="%{labItemMst.itemId}" />
               		<s:label id="%{'itemName_' + #stat.index}" value="%{labItemMst.itemName}" />
               		<s:hidden id="%{'hidden_itemId' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemId'}"/>
	            </td>
	            <td width="15%" align="center">
		            <s:label id="%{'itemPrice_' + #stat.index}" value="%{labItemMst.itemPrice}" />
	            	<s:hidden id="%{'hidden_itemPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPrice'}" />
	            </td>
	            <td width="15%" align="center">
		            <s:label id="%{'itemPromoPrice_' + #stat.index}" value="%{labItemMst.itemPromoPrice}" />
	            	<s:hidden id="%{'hidden_itemPromoPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].labItemMst.itemPromoPrice'}"></s:hidden>
	            </td>
	            <td width="15%" align="center">
		            <s:label id="%{'orderQty_' + #stat.index}" value="%{orderQty}" />
		            <s:hidden id="%{'hidden_orderQty_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderQty'}" />
	            </td>
	            <td>
		            <s:label id="%{'subTotal_' + #stat.index}" value="%{subTotal}" />
		            <s:hidden id="%{'hidden_subTotal_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].subTotal'}"  />
	            </td>
	            <td style="display: none;">
	            	<s:hidden id="%{'operate_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].operate'}"/>
				    <s:hidden id="%{'ver_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].ver'}" value="%{ver}" />
	                <s:hidden id="%{'orderItemOid_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderItemOid'}" />
	                <s:hidden id="%{'hidden_orderPrice_' + #stat.index}" name="%{'labOrderMst.labOrderItemList['+#stat.index+'].orderPrice'}" />
	            </td>
	        </tr>
	        </s:iterator>
	    </tbody>
    </table>
    </fieldset>
    </div>
    <!-- 按鍵組合 -->
        <s:include value="/WEB-INF/pages/include/include.ConfirmButton.jsp" /> 
    <!-- 按鍵組合 -->
</s:form>
</body>
</html>