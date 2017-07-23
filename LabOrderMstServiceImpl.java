package com.ddsc.km.lab.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.BeanUtilsHelper;
import com.ddsc.core.util.DateHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.lab.service.ILabOrderMstService;
import com.ddsc.km.lab.dao.ILabItemMstDao;
import com.ddsc.km.lab.dao.ILabOrderItemDao;
import com.ddsc.km.lab.dao.ILabOrderMstDao;
import com.ddsc.km.lab.entity.LabItemMst;
import com.ddsc.km.lab.entity.LabOrderItem;
import com.ddsc.km.lab.entity.LabOrderMst;
import com.ddsc.km.lab.service.ILabItemMstService;

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

public class LabOrderMstServiceImpl implements ILabOrderMstService {
	
	private ILabOrderMstDao labOrderMstDao;
	private ILabOrderItemDao labOrderItemDao;
	
	private ILabItemMstDao labItemMstDao;
	private ILabItemMstService labItemMstService;
	
	@Override
	public LabOrderMst create(LabOrderMst entity, UserInfo info) throws DdscApplicationException {
		try{
			LabItemMst labItemMst;
			entity.setOrderStatus("0");
			labOrderMstDao.save(entity, info);
			for(LabOrderItem labOrderItem:entity.getLabOrderItemList()){
				labItemMst = new LabItemMst();
				BigDecimal itemBookQty = BigDecimal.ZERO;
				
				if(labOrderItem.getLabItemMst().getItemBookQty() != null){
					itemBookQty = labOrderItem.getOrderQty().add(labOrderItem.getLabItemMst().getItemBookQty());
				}else{
					itemBookQty = labOrderItem.getOrderQty();
				}
				labItemMst = labOrderItem.getLabItemMst();
				labItemMst.setItemBookQty(itemBookQty);
				this.getLabItemMstService().update(labItemMst, info);
				labOrderItem.setCustId(entity.getLabCustMst().getCustId());
				labOrderItem.setOrderId(entity.getOrderId());
				this.labOrderItemDao.save(labOrderItem, info);
				
			}
			return entity;
		}catch (DdscApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabOrderMst update(LabOrderMst entity, UserInfo info) throws DdscApplicationException {
		try{
			LabOrderMst	labOrderMstVO = getLabOrderMstDao().get(entity.getOrderId(), info);
			
			LabOrderMst labOrderMst;
			List<LabOrderItem> labOrderItemList;
			
			BigDecimal itemQty;//庫存
			BigDecimal itemBookQty;//預定數量
			BigDecimal orderQty;//訂購數量
			String custId;
			String orderId;
			if(entity.getVer().getTime() == labOrderMstVO.getVer().getTime()){
				for(LabOrderItem labOrderItem:entity.getLabOrderItemList()){
					 itemQty = BigDecimal.ZERO;
					 itemBookQty = BigDecimal.ZERO;
					 orderQty = BigDecimal.ZERO;
					 custId = entity.getLabCustMst().getCustId();
					 orderId = entity.getOrderId();
					
					labOrderMst = new LabOrderMst();
					labOrderItemList= new ArrayList<LabOrderItem>();
					
					LabOrderItem labOrderItemPo = this.getLabOrderItemDao().get(labOrderItem.getOrderItemOid(), info);
					LabItemMst labItemMstPo = this.getLabItemMstDao().get(labOrderItem.getLabItemMst().getItemId(), info);

					if("0".equals(entity.getOrderStatus())){
						if("insert".equals(labOrderItem.getOperate())){
							//商品明細下單數量+商品主檔的預訂庫存
							if(labItemMstPo.getItemBookQty() != null){
								itemBookQty = labOrderItem.getOrderQty().add(labItemMstPo.getItemBookQty());							
							}else{
								itemBookQty = labOrderItem.getOrderQty();
							}
							
							labOrderItem.setOrderId(orderId);
							labOrderItem.setCustId(custId);
							
							labItemMstPo.setItemBookQty(itemBookQty);
							
							this.getLabItemMstService().update(labItemMstPo, info);
							labOrderItemList.add(labOrderItemDao.save(labOrderItem, info));
							
						}else if("update".equals(labOrderItem.getOperate())){
							
							labOrderItem.setOrderId(orderId);
							labOrderItem.setCustId(custId);
							//db出來的值
							BigDecimal beforecompareQty = labOrderItemPo.getOrderQty();
							//修改後的值
							BigDecimal newcompareQty = labOrderItem.getOrderQty();
							
							int compareOrderQty = beforecompareQty.compareTo(newcompareQty);
							BigDecimal newQty;
							//如果訂購數量變少
							if(compareOrderQty == 1){
								
								newQty = beforecompareQty.subtract(newcompareQty);
								itemBookQty = (labOrderItem.getLabItemMst().getItemBookQty()).subtract(newQty);
								
							}//如果訂購數量變多
							else if(compareOrderQty == -1){
								
								newQty = newcompareQty.subtract(beforecompareQty);
								itemBookQty = (labOrderItem.getLabItemMst().getItemBookQty()).add(itemBookQty);
								
							}//如果訂購數量不變
							else{
								itemBookQty = labOrderItem.getLabItemMst().getItemBookQty();
							}
							labOrderItem.getLabItemMst().setItemBookQty(itemBookQty);
							
							this.getLabItemMstService().update(labItemMstPo, info);
							
							BeanUtilsHelper.copyProperties(labOrderItemPo, labOrderItem , labOrderItem.obtainLocaleFieldNames());
							labOrderItemList.add(this.labOrderItemDao.update(labOrderItemPo, info));
							
							
						}else if("delete".equals(labOrderItem.getOperate())){
							labOrderItem.setOrderId(orderId);
							labOrderItem.setCustId(custId);
							
							itemBookQty = (labOrderItem.getLabItemMst().getItemBookQty()).subtract(labOrderItem.getOrderQty());
							labItemMstPo.setItemBookQty(itemBookQty);
							
							this.getLabItemMstService().update(labItemMstPo, info);
							
							BeanUtilsHelper.copyProperties(labOrderItemPo, labOrderItem , labOrderItem.obtainLocaleFieldNames());
							this.labOrderItemDao.delete(labOrderItemPo, info);
						}else{
							labOrderItemList.add(labOrderItem);
						}
						
					}else if("1".equals(entity.getOrderStatus())){
						String dateHelper = new DateHelper().getSystemDate("/");
						entity.setShipDate(dateHelper);
						itemQty = labOrderItemPo.getLabItemMst().getItemQty();//庫存
						itemBookQty = labOrderItemPo.getLabItemMst().getItemBookQty();//預定數量
						orderQty  = labOrderItemPo.getOrderQty();//訂購數量
						
						
						labOrderItem.getLabItemMst().setItemQty(itemQty.subtract(orderQty));
						labOrderItem.getLabItemMst().setItemBookQty(itemBookQty.subtract(orderQty));
						
						this.getLabItemMstService().update(labItemMstPo, info);
						
					}else if("9".equals(entity.getOrderStatus())){
						itemBookQty = (labOrderItemPo.getLabItemMst().getItemBookQty()).subtract(labOrderItemPo.getOrderQty());
						
						
						
						labOrderItem.getLabItemMst().setItemBookQty(itemBookQty);
						
						this.getLabItemMstService().update(labItemMstPo, info);
					}
					
					labOrderMst.setLabOrderItemList(labOrderItemList);
				}
				
				
				BeanUtilsHelper.copyProperties(labOrderMstVO, entity, entity.obtainLocaleFieldNames());
				labOrderMst = this.getLabOrderMstDao().update(labOrderMstVO, info);
				
				return labOrderMst;
			}else{
				// 資料已被異動無法更新, 丟出 Exception
				throw new DdscApplicationException(DdscApplicationException.DDSCEXCEPTION_TYPE_ERROR, "eP.0013");
			}
		} catch (DdscApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabOrderMst delete(LabOrderMst entity, UserInfo info) throws DdscApplicationException {
		try{
			LabOrderMst labOredrMstPo =  this.get(entity.getOrderId(), info);
			BigDecimal itemBookQty;
			if(labOredrMstPo.getVer().getTime() == entity.getVer().getTime()){
				for(LabOrderItem labOrderItem:labOredrMstPo.getLabOrderItemList()){
					itemBookQty = BigDecimal.ZERO;
					LabOrderItem labOrderItemPo = this.getLabOrderItemDao().get(labOrderItem.getOrderItemOid(), info);
					LabItemMst labItemMstPo =  this.getLabItemMstDao().get(labOrderItem.getLabItemMst().getItemId(), info);
					
					itemBookQty = (labOrderItem.getLabItemMst().getItemBookQty()).subtract(labOrderItem.getOrderQty());
					
					labItemMstPo.setItemBookQty(itemBookQty);
					this.getLabItemMstService().update(labItemMstPo, info);
					
					BeanUtilsHelper.copyProperties(labOrderItemPo, labOrderItem, labOrderItem.obtainLocaleFieldNames());
					this.getLabOrderItemDao().delete(labOrderItem, info);
				}
				BeanUtilsHelper.copyProperties(labOredrMstPo, labOredrMstPo, labOredrMstPo.obtainLocaleFieldNames());
				this.getLabOrderMstDao().delete(labOredrMstPo, info);
				
			}else{
				throw new DdscApplicationException(DdscApplicationException.DDSCEXCEPTION_TYPE_ERROR, "eP.0013");
			}		
			
			return entity;
			
		} catch (DdscApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Override
	public LabOrderMst get(String id, UserInfo info) throws DdscApplicationException {
		try{
			LabOrderMst labOrderMst = this.getLabOrderMstDao().get(id, info);
			labOrderMst.setLabOrderItemList(this.getLabOrderItemDao().getList(labOrderMst.getOrderId(), info));
			return labOrderMst;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Override
	public List<LabOrderMst> searchAll(UserInfo info) throws DdscApplicationException {
		return null;
	}

	@Override
	public Pager searchByConditions(Map<String, Object> conditions, Pager pager, UserInfo userInfo) throws DdscApplicationException {
		try{
			return labOrderMstDao.searchByConditions(conditions, pager, userInfo);			
		}catch (DdscApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new DdscApplicationException(e, userInfo);
		}
	}

	@Override
	public List<LabOrderMst> searchByConditions(Map<String, Object> conditions, UserInfo userInfo) throws DdscApplicationException {
		return null;
	}

	@Override
	public List<Object> queryDataByParamsByService(Map<String, Object> conditions, UserInfo userInfo) throws DdscApplicationException {
		return null;
	}

	@Override
	public int getDataRowCountByConditions(Map<String, Object> conditions, UserInfo info) throws DdscApplicationException {
	
		return getDataRowCountByConditions(conditions, info);

	}

	public ILabOrderMstDao getLabOrderMstDao() {
		return labOrderMstDao;
	}

	public void setLabOrderMstDao(ILabOrderMstDao labOrderMstDao) {
		this.labOrderMstDao = labOrderMstDao;
	}

	public ILabOrderItemDao getLabOrderItemDao() {
		return labOrderItemDao;
	}

	public void setLabOrderItemDao(ILabOrderItemDao labOrderItemDao) {
		this.labOrderItemDao = labOrderItemDao;
	}

	public ILabItemMstDao getLabItemMstDao() {
		return labItemMstDao;
	}

	public void setLabItemMstDao(ILabItemMstDao labItemMstDao) {
		this.labItemMstDao = labItemMstDao;
	}

	public ILabItemMstService getLabItemMstService() {
		return labItemMstService;
	}

	public void setLabItemMstService(ILabItemMstService labItemMstService) {
		this.labItemMstService = labItemMstService;
	}
}
