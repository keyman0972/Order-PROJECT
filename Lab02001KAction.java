package com.ddsc.km.lab.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddsc.core.action.AbstractAction;
import com.ddsc.core.action.IBaseAction;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.exception.DdscAuthException;
import com.ddsc.core.util.DateHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.lab.entity.LabOrderItem;
import com.ddsc.km.lab.entity.LabOrderMst;
import com.ddsc.km.lab.service.ILabCustMstService;
import com.ddsc.km.lab.service.ILabItemMstService;
import com.ddsc.km.lab.service.ILabOrderMstService;
import com.ddsc.km.lab.entity.LabCustMst;
import com.ddsc.lab.sn.ISnMkr000010Service;

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

public class Lab02001KAction extends AbstractAction implements IBaseAction {
	

	private static final long serialVersionUID = 7359143081188459480L;
	private ILabOrderMstService labOrderMstService;
	private List<LabOrderMst> labOrderMstList;
	private LabOrderMst labOrderMst;
	
	private List<LabOrderItem> labOrderItemList;
	private ISnMkr000010Service snMkr000010Service;
	
	private ILabCustMstService labCustMstService;
	private ILabItemMstService labItemMstService;
	
	@Override
	public String init() throws Exception {
		try {
		} 
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_SEARCH);
		return SUCCESS;
	}

	@Override
	public String create() throws Exception {
		try {
			String dateHelper = new DateHelper().getSystemDate("/");
			String snMkr = snMkr000010Service.genSn(getUserInfo());
			
			labOrderMst = new LabOrderMst();
			labOrderMst.setOrderDate(dateHelper);
			labOrderMst.setOrderId(snMkr);
			
			labOrderItemList = new ArrayList<LabOrderItem>();
			labOrderItemList.add(new LabOrderItem());
			labOrderMst.setLabOrderItemList(labOrderItemList);
			
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_CREATE_SUBMIT);
		return SUCCESS;
	}

	@Override
	public String createSubmit() throws Exception {
		try {
			if (this.hasConfirm() == true) {
				setNextAction(ACTION_CREATE_CONFIRM);
				return RESULT_CONFIRM;
			}
			else {
				return this.createConfirm();
			}
		}
		catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
	}

	@Override
	public String createConfirm() throws Exception {
		try{
			labOrderMst = getLabOrderMstService().create(labOrderMst, getUserInfo());
			setNextAction(ACTION_CREATE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_CREATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_CREATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String update() throws Exception {
		try{
			labOrderMst = getLabOrderMstService().get(labOrderMst.getOrderId(),  this.getUserInfo());
			
			if(labOrderMst.getLabOrderItemList().size() == 0){
				labOrderItemList = new ArrayList<LabOrderItem>();
				labOrderItemList.add(new LabOrderItem());
				labOrderMst.setLabOrderItemList(labOrderItemList);
			}
			
			setNextAction(ACTION_UPDATE_SUBMIT);
			return SUCCESS;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
	}

	@Override
	public String updateSubmit() throws Exception {
		try {
			if (hasConfirm()) {
				setNextAction(ACTION_UPDATE_CONFIRM);
				return RESULT_CONFIRM;
			}else {
				return this.updateConfirm();
			}
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String updateConfirm() throws Exception {
		try{
			labOrderMst = getLabOrderMstService().update(labOrderMst, getUserInfo());
			setNextAction(ACTION_UPDATE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		try{
			
			labOrderMst = getLabOrderMstService().get(labOrderMst.getOrderId(),  this.getUserInfo());
		
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_DELETE_CONFIRM);
		return SUCCESS;
	}

	@Override
	public String deleteConfirm() throws Exception {
		try {
			labOrderMst = getLabOrderMstService().delete(labOrderMst, this.getUserInfo());
			setNextAction(ACTION_DELETE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_DELETE_CONFIRM);
			return RESULT_CONFIRM;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_DELETE_CONFIRM);
			return RESULT_CONFIRM;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		try {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("labCustMst.custId", labOrderMst.getLabCustMst().getCustId());
			conditions.put("labCustMst.custName", labOrderMst.getLabCustMst().getCustName());
			conditions.put("orderDate", labOrderMst.getOrderDate());
			conditions.put("orderId", labOrderMst.getOrderId());
			Pager resultPager = getLabOrderMstService().searchByConditions(conditions, getPager(), this.getUserInfo());
			labOrderMstList = (List<LabOrderMst>) resultPager.getData();
			this.setLabOrderMstList(labOrderMstList);
			setPager(resultPager);
			if (labOrderMstList == null || labOrderMstList.size() <= 0) {
				this.addActionError(this.getText("w.0001"));
			}
		} catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		} catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_SEARCH);
		return SUCCESS;
	}
	
	@Override
	public String query() throws Exception {
		try{
			labOrderMst = getLabOrderMstService().get(labOrderMst.getOrderId(), this.getUserInfo());		
		} catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		} catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_QUERY);
		return SUCCESS;
	}

	@Override
	public String copy() throws Exception {
		try {
			String dateHelper = new DateHelper().getSystemDate("/");
			String snMkr = snMkr000010Service.genSn(this.getUserInfo());
			
			labOrderMst = getLabOrderMstService().get(labOrderMst.getOrderId(), getUserInfo());
			
			labOrderMst.setOrderId(snMkr);
			labOrderMst.setOrderDate(dateHelper);
			
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_COPY_SUBMIT);
		return SUCCESS;
	}

	@Override
	public String copySubmit() throws Exception {
		try {
			if (this.hasConfirm() == true) {
				// 有確認頁
				setNextAction(ACTION_COPY_CONFIRM);
				return RESULT_CONFIRM;
			}
			else {
				// 沒有確認頁, 直接存檔
				return this.copyConfirm();
			}
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return SUCCESS;
		}
	}

	@Override
	public String copyConfirm() throws Exception {
		try {
			labOrderMst = getLabOrderMstService().create(labOrderMst, getUserInfo());
			setNextAction(ACTION_COPY);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}
	}
	
	@Deprecated
	@Override
	public String approve() throws Exception {
		return null;
	}
	
	@Override
	public void validate() {
		try {
			setUpInfo();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
	}

	/**
	 * 檢核 - 按送出鈕(新增頁)
	 */
	public void validateCreateSubmit() {
		try {
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(新增頁)
	 */
	public void validateCreateConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			setUpInfo();
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按送出鈕(新增頁)
	 */
	public void validateUpdateSubmit() {
		try {
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(新增頁)
	 */
	public void validateUpdateConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			setUpInfo();
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}
	
	/**
	 * 檢核 - 按確定鈕(刪除頁)
	 */
	public void validateDeleteConfirm() {
		try {
			setUpInfo();
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
	}

	/**
	 * 檢核 - 按送出鈕(複製頁)
	 */
	public void validateCopySubmit() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(複製頁)
	 */
	public void validateCopyConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			setUpInfo();
			this.checkValidateRule();
		}
		catch (DdscAuthException e) {
			throw e;
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}



	/**
	 * 資料檢核
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkValidateRule() throws Exception {
		boolean isValid = true;
		LabCustMst labCustMst = this.getLabCustMstService().get(this.getLabOrderMst().getLabCustMst().getCustId(), this.getUserInfo()) ;
		try {			
			String custId =  labOrderMst.getLabCustMst().getCustId();
			
			// 取得並檢核客戶料是否存在
			labOrderMst.setLabCustMst(this.getLabCustMstService().get(custId, this.getUserInfo()));
			
			if (labOrderMst.getLabCustMst() == null) {
				this.addFieldError("custId", this.getText("custId") + this.getText("eP.0003"));
				isValid = false;
			}else{
				this.getLabOrderMst().setLabCustMst(labCustMst);
			}
			
			List<LabOrderItem> labOrderItemList = labOrderMst.getLabOrderItemList();
			
			for (int i = 0; i < labOrderItemList.size(); i++) {
				// 系統(產品)代碼
				String itemId = labOrderItemList.get(i).getLabItemMst().getItemId();
				
				labOrderItemList.get(i).setLabItemMst(this.getLabItemMstService().get(itemId, this.getUserInfo()));
				labOrderItemList.get(i).setCustId(custId);
				
				if(labOrderItemList.get(i).getLabItemMst() == null){
					this.addFieldError("itemId", this.getText("itemId") + this.getText("eP.0003"));
					isValid = false;
					break;
				}
			}
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		return isValid;
	}

	public ILabOrderMstService getLabOrderMstService() {
		return labOrderMstService;
	}

	public void setLabOrderMstService(ILabOrderMstService labOrderMstService) {
		this.labOrderMstService = labOrderMstService;
	}

	public List<LabOrderMst> getLabOrderMstList() {
		return labOrderMstList;
	}

	public void setLabOrderMstList(List<LabOrderMst> labOrderMstList) {
		this.labOrderMstList = labOrderMstList;
	}

	public LabOrderMst getLabOrderMst() {
		return labOrderMst;
	}

	public void setLabOrderMst(LabOrderMst labOrderMst) {
		this.labOrderMst = labOrderMst;
	}

	public List<LabOrderItem> getLabOrderItemList() {
		return labOrderItemList;
	}

	public void setLabOrderItemList(List<LabOrderItem> labOrderItemList) {
		this.labOrderItemList = labOrderItemList;
	}

	public ILabCustMstService getLabCustMstService() {
		return labCustMstService;
	}

	public void setLabCustMstService(ILabCustMstService labCustMstService) {
		this.labCustMstService = labCustMstService;
	}

	public ILabItemMstService getLabItemMstService() {
		return labItemMstService;
	}

	public void setLabItemMstService(ILabItemMstService labItemMstService) {
		this.labItemMstService = labItemMstService;
	}

	public ISnMkr000010Service getSnMkr000010Service() {
		return snMkr000010Service;
	}

	public void setSnMkr000010Service(ISnMkr000010Service snMkr000010Service) {
		this.snMkr000010Service = snMkr000010Service;
	}
	
}
