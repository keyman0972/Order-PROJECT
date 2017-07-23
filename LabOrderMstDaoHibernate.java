package com.ddsc.km.lab.dao.hibernate;

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
import com.ddsc.km.lab.dao.ILabOrderMstDao;
import com.ddsc.km.lab.entity.LabOrderMst;

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

public class LabOrderMstDaoHibernate extends GenericDaoHibernate<LabOrderMst,String> implements ILabOrderMstDao {

	@Override
	public Pager searchByConditions(Map<String, Object> conditions, Pager pager, UserInfo userInfo) throws DdscApplicationException {
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT ORD.ORDER_ID, ORD.CUST_ID, ORD.ORDER_AMT, ORD.ORDER_STATUS, ORD.ORDER_DATE, ORD.SHIP_DATE, ORD.ORDER_NOTE, CUST.CUST_NAME ");
		sbsql.append("FROM LAB_ORDER_MST ORD ");
		sbsql.append("INNER JOIN LAB_CUST_MST CUST ");
		sbsql.append("ON (ORD.CUST_ID = CUST.CUST_ID) ");

		String keyword = "WHERE ";
		List<Object> value = new ArrayList<Object>();
		if (StringUtils.isNotEmpty((String) conditions.get("labCustMst.custId"))) {
			sbsql.append(keyword + "CUST.CUST_ID LIKE ? ");
			value.add(conditions.get("labCustMst.custId") + "%");
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("labCustMst.custName"))) {
			sbsql.append(keyword +"CUST.CUST_NAME LIKE ? ");
			value.add("%"+conditions.get("labCustMst.custName") + "%");
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("orderDate"))) {
			sbsql.append(keyword + "ORD.ORDER_DATE = ? ");
			value.add(conditions.get("orderDate"));
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("orderId"))) {
			sbsql.append(keyword + "ORD.ORDER_ID LIKE ? ");
			value.add(conditions.get("orderId") + "%");
			keyword = "AND ";
		}
		List<String> orderStatus = (List<String>) conditions.get("orderStatus");
		if (orderStatus != null && !orderStatus.isEmpty()) {
			sbsql.append(keyword + "ORD.ORDER_STATUS IN ( "+this.getSqlQuestionMark(orderStatus.size())+" )");
			value.addAll(orderStatus);
			keyword = "AND ";
		}
		sbsql.append(" ORDER BY ORD.ORDER_ID ");
		
		// 建立List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>(); 並add
		List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>();
		scalarList.add(new HibernateScalarHelper("ORDER_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("CUST_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("CUST_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ORDER_AMT", Hibernate.BIG_DECIMAL));
		scalarList.add(new HibernateScalarHelper("ORDER_STATUS", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ORDER_DATE", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("SHIP_DATE", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("ORDER_NOTE", Hibernate.STRING));
		
		// 回傳
		return super.findBySQLQueryMapPagination(sbsql.toString(), pager, scalarList, value, userInfo);
	}

}
