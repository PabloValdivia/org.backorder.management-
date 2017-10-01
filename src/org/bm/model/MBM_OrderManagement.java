/*** Licensed under the KARMA v.1 Law of Sharing. As others have shared freely to you, so shall you share freely back to us.* If you shall try to cheat and find a loophole in this license, then KARMA will exact your share,* and your worldly gain shall come to naught and those who share shall gain eventually above you.* In compliance with previous GPLv2.0 works of Jorg Janke, Low Heng Sin, Carlos Ruiz and contributors.* This Module Creator is an idea put together and coded by Redhuan D. Oon (red1@red1.org)*/package org.bm.model;

import java.math.BigDecimal;import java.sql.ResultSet;
import java.util.Properties;import org.adempiere.exceptions.AdempiereException;import org.compiere.model.MOrder;import org.compiere.model.MOrderLine;import org.compiere.model.MPriceList;import org.compiere.util.Env;

public class MBM_OrderManagement extends X_BM_OrderManagement{

	private static final long serialVersionUID = -1L;

	public MBM_OrderManagement(Properties ctx, int id, String trxName) {
		super(ctx,id,trxName);
		}

	public MBM_OrderManagement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}		protected boolean afterSave(boolean newRecord, boolean success){		if (newRecord && getC_Order_ID()>0){			BigDecimal OrderedQty = Env.ZERO;			//importing C_Order into BackOrder Sales Management			MOrder order = new MOrder(Env.getCtx(),getC_Order_ID(),get_TrxName());			if (order.getDocStatus().equals(MOrder.DOCSTATUS_Closed)||order.getDocStatus().equals(MOrder.DOCSTATUS_Voided))				throw new AdempiereException("Sales Order is closed or void");			setC_BPartner_ID(order.getC_BPartner_ID());			setAD_Org_ID(order.getAD_Org_ID());			MPriceList pricelist = new MPriceList(Env.getCtx(),order.getM_PriceList_ID(),null);			int M_PriceList_Version_ID = pricelist.getPriceListVersion(order.getCreated()).get_ID();			setM_PriceList_Version_ID(M_PriceList_Version_ID);						//populate BM_OrderManagementLine from C_OrderLine			MOrderLine[] orderlines = order.getLines();			for (MOrderLine orderline:orderlines){				MBM_OrderManagementLine boline = new MBM_OrderManagementLine(getCtx(), 0, get_TrxName());				boline.setBM_OrderManagement_ID(get_ID());				boline.setM_Product_ID(orderline.getM_Product_ID());				boline.setQtyOrdered(orderline.getQtyOrdered());				boline.setM_AttributeSetInstance_ID(orderline.getM_AttributeSetInstance_ID());				boline.setC_OrderLine_ID(orderline.get_ID());				boline.setLineNetAmt(orderline.getLineNetAmt());				boline.saveEx(get_TrxName());				OrderedQty=OrderedQty.add(orderline.getQtyOrdered());			}			setOrderedQty(OrderedQty);			setGrandTotal(order.getGrandTotal());			saveEx(get_TrxName());		}		return true;	}
}
