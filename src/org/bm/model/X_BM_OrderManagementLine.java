/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.bm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for BM_OrderManagementLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_BM_OrderManagementLine extends PO implements I_BM_OrderManagementLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170902L;

    /** Standard Constructor */
    public X_BM_OrderManagementLine (Properties ctx, int BM_OrderManagementLine_ID, String trxName)
    {
      super (ctx, BM_OrderManagementLine_ID, trxName);
      /** if (BM_OrderManagementLine_ID == 0)
        {
			setBM_OrderManagementLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_BM_OrderManagementLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_BM_OrderManagementLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Available.
		@param Available Available	  */
	public void setAvailable (String Available)
	{
		set_Value (COLUMNNAME_Available, Available);
	}

	/** Get Available.
		@return Available	  */
	public String getAvailable () 
	{
		return (String)get_Value(COLUMNNAME_Available);
	}

	/** Set OrderManagementLine.
		@param BM_OrderManagementLine_ID OrderManagementLine	  */
	public void setBM_OrderManagementLine_ID (int BM_OrderManagementLine_ID)
	{
		if (BM_OrderManagementLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_BM_OrderManagementLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_BM_OrderManagementLine_ID, Integer.valueOf(BM_OrderManagementLine_ID));
	}

	/** Get OrderManagementLine.
		@return OrderManagementLine	  */
	public int getBM_OrderManagementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BM_OrderManagementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_BM_OrderManagement getBM_OrderManagement() throws RuntimeException
    {
		return (I_BM_OrderManagement)MTable.get(getCtx(), I_BM_OrderManagement.Table_Name)
			.getPO(getBM_OrderManagement_ID(), get_TrxName());	}

	/** Set OrderManagement.
		@param BM_OrderManagement_ID OrderManagement	  */
	public void setBM_OrderManagement_ID (int BM_OrderManagement_ID)
	{
		if (BM_OrderManagement_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_BM_OrderManagement_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_BM_OrderManagement_ID, Integer.valueOf(BM_OrderManagement_ID));
	}

	/** Get OrderManagement.
		@return OrderManagement	  */
	public int getBM_OrderManagement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BM_OrderManagement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BackOrders.
		@param BackOrders BackOrders	  */
	public void setBackOrders (String BackOrders)
	{
		set_Value (COLUMNNAME_BackOrders, BackOrders);
	}

	/** Get BackOrders.
		@return BackOrders	  */
	public String getBackOrders () 
	{
		return (String)get_Value(COLUMNNAME_BackOrders);
	}

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_Value (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DaysSinceOrder.
		@param DaysSinceOrder DaysSinceOrder	  */
	public void setDaysSinceOrder (BigDecimal DaysSinceOrder)
	{
		set_Value (COLUMNNAME_DaysSinceOrder, DaysSinceOrder);
	}

	/** Get DaysSinceOrder.
		@return DaysSinceOrder	  */
	public BigDecimal getDaysSinceOrder () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DaysSinceOrder);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DaysTillPromised.
		@param DaysTillPromised DaysTillPromised	  */
	public void setDaysTillPromised (BigDecimal DaysTillPromised)
	{
		set_Value (COLUMNNAME_DaysTillPromised, DaysTillPromised);
	}

	/** Get DaysTillPromised.
		@return DaysTillPromised	  */
	public BigDecimal getDaysTillPromised () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DaysTillPromised);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Insufficient.
		@param Insufficient Insufficient	  */
	public void setInsufficient (boolean Insufficient)
	{
		set_Value (COLUMNNAME_Insufficient, Boolean.valueOf(Insufficient));
	}

	/** Get Insufficient.
		@return Insufficient	  */
	public boolean isInsufficient () 
	{
		Object oo = get_Value(COLUMNNAME_Insufficient);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line Amount.
		@param LineNetAmt 
		Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt)
	{
		set_Value (COLUMNNAME_LineNetAmt, LineNetAmt);
	}

	/** Get Line Amount.
		@return Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineNetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException
    {
		return (I_M_AttributeSetInstance)MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
			.getPO(getM_AttributeSetInstance_ID(), get_TrxName());	}

	/** Set Attribute Set Instance.
		@param M_AttributeSetInstance_ID 
		Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
	{
		if (M_AttributeSetInstance_ID < 0) 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, null);
		else 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
	}

	/** Get Attribute Set Instance.
		@return Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Ordered Quantity.
		@param QtyOrdered 
		Ordered Quantity
	  */
	public void setQtyOrdered (BigDecimal QtyOrdered)
	{
		set_Value (COLUMNNAME_QtyOrdered, QtyOrdered);
	}

	/** Get Ordered Quantity.
		@return Ordered Quantity
	  */
	public BigDecimal getQtyOrdered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOrdered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ready.
		@param Ready Ready	  */
	public void setReady (boolean Ready)
	{
		set_Value (COLUMNNAME_Ready, Boolean.valueOf(Ready));
	}

	/** Get Ready.
		@return Ready	  */
	public boolean isReady () 
	{
		Object oo = get_Value(COLUMNNAME_Ready);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set TotalBackOrders.
		@param TotalBackOrders TotalBackOrders	  */
	public void setTotalBackOrders (BigDecimal TotalBackOrders)
	{
		set_Value (COLUMNNAME_TotalBackOrders, TotalBackOrders);
	}

	/** Get TotalBackOrders.
		@return TotalBackOrders	  */
	public BigDecimal getTotalBackOrders () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalBackOrders);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalOnHand.
		@param TotalOnHand TotalOnHand	  */
	public void setTotalOnHand (BigDecimal TotalOnHand)
	{
		set_Value (COLUMNNAME_TotalOnHand, TotalOnHand);
	}

	/** Get TotalOnHand.
		@return TotalOnHand	  */
	public BigDecimal getTotalOnHand () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalOnHand);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}