package com.ddsc.km.lab.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

import com.ddsc.core.dao.hibernate.GenericDaoHibernate;
import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.HibernateScalarHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.lab.dao.ILabOrderItemDao;
import com.ddsc.km.lab.entity.LabItemMst;
import com.ddsc.km.lab.entity.LabOrderItem;

/**
 * <table>
 * <tr>
 * <th>版本</th>
 * <th>日期</th>
 * <th>詳細說明</th>
 * <th>modifier</th>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2017/7/7</td>
 * <td>新建檔案</td>
 * <td>"keyman"</td>
 * </tr>
 * </table>
 * @author "keyman"
 *
 * 類別說明 :
 *
 *
 * 版權所有 Copyright 2008 © 中菲電腦股份有限公司 本網站內容享有著作權，禁止侵害，違者必究。 <br>
 * (C) Copyright Dimerco Data System Corporation Inc., Ltd. 2009 All Rights
 */

public class LabOrderItemDaoHibernate extends GenericDaoHibernate<LabOrderItem,String> implements ILabOrderItemDao {
	
	@Override
	public List<LabOrderItem> getOrderItemList(String id, UserInfo info) throws DdscApplicationException {
		List<Object> values = new ArrayList<Object>();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT ORDITEM.ORDER_ITEM_OID, ORDITEM.ORDER_ID, ORDITEM.ITEM_ID, ORDITEM.CUST_ID, ORDITEM.ORDER_PRICE, ORDITEM.ORDER_QTY, ORDITEM.SUB_TOTAL ");
		sbsql.append("FROM LAB_ORDER_ITEM ORDITEM ");
		sbsql.append("INNER JOIN LAB_ORDER_MST ORD ON (ORDITEM.ORDER_ID = ORD.ORDER_ID) ");
		sbsql.append("LEFT JOIN LAB_ITEM_MST ITEM ON (ORDITEM.ITEM_ID = ITEM.ITEM_ID) ");

		String keyword = "WHERE ";
		List<Object> value = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(id)) {
			sbsql.append(keyword + "ORDITEM.ITEM_ID = ? ");
			value.add(id);
			keyword = "AND ";
		}
		
		sbsql.append(" ORDER BY ORDITEM.ORDER_ITEM_OID ");
		
		// 建立List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>(); 並add
		List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>();
		scalarList.add(new HibernateScalarHelper("ORDER_ITEM_OID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ORDER_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ITEM_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("CUST_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ORDER_PRICE", Hibernate.BIG_DECIMAL));
		scalarList.add(new HibernateScalarHelper("ORDER_QTY", Hibernate.BIG_INTEGER));
		scalarList.add(new HibernateScalarHelper("SUB_TOTAL", Hibernate.BIG_DECIMAL));
		
		List<Object> alist = super.findBySQLQuery(sbsql.toString(), scalarList, values, info);
		
		List<LabOrderItem> entityList = new ArrayList<LabOrderItem>();
		
		LabOrderItem labOrderItem;
		LabItemMst labItemMst;
		Object[] temp;
		for(int i =0 ; i < alist.size(); i++){
			labOrderItem = new LabOrderItem();
			labItemMst = new LabItemMst();
			temp = (Object[]) alist.get(i);
			labOrderItem.setOrderItemOid((String)temp[0]);
			labOrderItem.setOrderId((String)temp[1]);
			labItemMst.setItemId((String)temp[2]);
			labOrderItem.setCustId((String)temp[3]);
			labOrderItem.setOrderPrice((BigDecimal)temp[4]);
			labOrderItem.setOrderQty((BigDecimal)temp[5]);
			labOrderItem.setSubTotal((BigDecimal)temp[6]);
			labOrderItem.setLabItemMst(labItemMst);
			entityList.add(labOrderItem);
		}
		return entityList ;
	}

	@Override
	public List<LabOrderItem> getList(String id, UserInfo info) throws DdscApplicationException {
		List<Object> values = new ArrayList<Object>();
		StringBuffer sbsql= new StringBuffer();
		sbsql.append("select item");
		sbsql.append(" from LabOrderItem item");
		sbsql.append(" where item.orderId= ?");
		if(StringUtils.isNotEmpty(id)){
			values.add(id);
		}
		return super.findByHQLString(sbsql.toString(), values, info) ;
	}

}
