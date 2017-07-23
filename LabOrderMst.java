package com.ddsc.km.lab.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ddsc.core.entity.BaseEntity;

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

@Entity
@Table(name="LAB_ORDER_MST")
public class LabOrderMst extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -4561715946049874081L;
	
	private String orderId;
	private LabCustMst labCustMst;
	private BigDecimal orderAmt;
	private String orderStatus;
	private String orderDate;
	private String shipDate;
	private String orderNote;
	private List<LabOrderItem> labOrderItemList;
	
	@Id
	@Column(name="ORDER_ID")
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@OneToOne(targetEntity = LabCustMst.class, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "CUST_ID", referencedColumnName = "CUST_ID")
	public LabCustMst getLabCustMst() {
		return labCustMst;
	}

	public void setLabCustMst(LabCustMst labCustMst) {
		this.labCustMst = labCustMst;
	}
	
	@Column(name="ORDER_AMT")
	public BigDecimal getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(BigDecimal orderAmt) {
		this.orderAmt = orderAmt;
	}
	
	@Column(name="ORDER_STATUS")
	public String getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	@Column(name="ORDER_DATE")
	public String getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	@Column(name="SHIP_DATE")
	public String getShipDate() {
		return shipDate;
	}
	
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	
	@Column(name="ORDER_NOTE")
	public String getOrderNote() {
		return orderNote;
	}
	
	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}
	
	@Transient
	public List<LabOrderItem> getLabOrderItemList() {
		return labOrderItemList;
	}

	public void setLabOrderItemList(List<LabOrderItem> labOrderItemList) {
		this.labOrderItemList = labOrderItemList;
	}
}
