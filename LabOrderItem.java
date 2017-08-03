package com.ddsc.km.lab.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
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
@Table(name="LAB_ORDER_ITEM")
public class LabOrderItem extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9191071987686687313L;
	
	private String orderItemOid;
	private String orderId;
	private LabItemMst labItemMst;
	private String custId;
	private BigDecimal orderPrice;
	private BigDecimal orderQty;
	private BigDecimal subTotal;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="ORDER_ITEM_OID")
	public String getOrderItemOid() {
		return orderItemOid;
	}
	
	public void setOrderItemOid(String orderItemOid) {
		this.orderItemOid = orderItemOid;
	}
	
	@Column(name="ORDER_ID")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@OneToOne(targetEntity = LabItemMst.class, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	public LabItemMst getLabItemMst() {
		return labItemMst;
	}

	public void setLabItemMst(LabItemMst labItemMst) {
		this.labItemMst = labItemMst;
	}
	
	@Column(name="CUST_ID")
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	@Column(name="ORDER_PRICE")
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@Column(name="ORDER_QTY")
	public BigDecimal getOrderQty() {
		return orderQty;
	}
	
	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
	
	@Column(name="SUB_TOTAL")
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}
