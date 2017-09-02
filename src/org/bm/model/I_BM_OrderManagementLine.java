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
package org.bm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for BM_OrderManagementLine
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_BM_OrderManagementLine 
{

    /** TableName=BM_OrderManagementLine */
    public static final String Table_Name = "BM_OrderManagementLine";

    /** AD_Table_ID=1000039 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization	  */
	public int getAD_Org_ID();

    /** Column name Available */
    public static final String COLUMNNAME_Available = "Available";

	/** Set Available	  */
	public void setAvailable (String Available);

	/** Get Available	  */
	public String getAvailable();

    /** Column name BM_OrderManagementLine_ID */
    public static final String COLUMNNAME_BM_OrderManagementLine_ID = "BM_OrderManagementLine_ID";

	/** Set OrderManagementLine	  */
	public void setBM_OrderManagementLine_ID (int BM_OrderManagementLine_ID);

	/** Get OrderManagementLine	  */
	public int getBM_OrderManagementLine_ID();

    /** Column name BM_OrderManagement_ID */
    public static final String COLUMNNAME_BM_OrderManagement_ID = "BM_OrderManagement_ID";

	/** Set OrderManagement	  */
	public void setBM_OrderManagement_ID (int BM_OrderManagement_ID);

	/** Get OrderManagement	  */
	public int getBM_OrderManagement_ID();

	public I_BM_OrderManagement getBM_OrderManagement() throws RuntimeException;

    /** Column name BackOrders */
    public static final String COLUMNNAME_BackOrders = "BackOrders";

	/** Set BackOrders	  */
	public void setBackOrders (String BackOrders);

	/** Get BackOrders	  */
	public String getBackOrders();

    /** Column name C_OrderLine_ID */
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

	/** Set Sales Order Line.
	  * Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID);

	/** Get Sales Order Line.
	  * Sales Order Line
	  */
	public int getC_OrderLine_ID();

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By	  */
	public int getCreatedBy();

    /** Column name DaysSinceOrder */
    public static final String COLUMNNAME_DaysSinceOrder = "DaysSinceOrder";

	/** Set DaysSinceOrder	  */
	public void setDaysSinceOrder (BigDecimal DaysSinceOrder);

	/** Get DaysSinceOrder	  */
	public BigDecimal getDaysSinceOrder();

    /** Column name DaysTillPromised */
    public static final String COLUMNNAME_DaysTillPromised = "DaysTillPromised";

	/** Set DaysTillPromised	  */
	public void setDaysTillPromised (BigDecimal DaysTillPromised);

	/** Get DaysTillPromised	  */
	public BigDecimal getDaysTillPromised();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name Insufficient */
    public static final String COLUMNNAME_Insufficient = "Insufficient";

	/** Set Insufficient	  */
	public void setInsufficient (boolean Insufficient);

	/** Get Insufficient	  */
	public boolean isInsufficient();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active	  */
	public void setIsActive (boolean IsActive);

	/** Get Active	  */
	public boolean isActive();

    /** Column name LineNetAmt */
    public static final String COLUMNNAME_LineNetAmt = "LineNetAmt";

	/** Set Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt);

	/** Get Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt();

    /** Column name LineNo */
    public static final String COLUMNNAME_LineNo = "LineNo";

	/** Set Line.
	  * Line No
	  */
	public void setLineNo (int LineNo);

	/** Get Line.
	  * Line No
	  */
	public int getLineNo();

    /** Column name M_AttributeSetInstance_ID */
    public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";

	/** Set Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID);

	/** Get Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID();

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException;

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name QtyOrdered */
    public static final String COLUMNNAME_QtyOrdered = "QtyOrdered";

	/** Set Ordered Quantity.
	  * Ordered Quantity
	  */
	public void setQtyOrdered (BigDecimal QtyOrdered);

	/** Get Ordered Quantity.
	  * Ordered Quantity
	  */
	public BigDecimal getQtyOrdered();

    /** Column name Ready */
    public static final String COLUMNNAME_Ready = "Ready";

	/** Set Ready	  */
	public void setReady (boolean Ready);

	/** Get Ready	  */
	public boolean isReady();

    /** Column name TotalBackOrders */
    public static final String COLUMNNAME_TotalBackOrders = "TotalBackOrders";

	/** Set TotalBackOrders	  */
	public void setTotalBackOrders (BigDecimal TotalBackOrders);

	/** Get TotalBackOrders	  */
	public BigDecimal getTotalBackOrders();

    /** Column name TotalOnHand */
    public static final String COLUMNNAME_TotalOnHand = "TotalOnHand";

	/** Set TotalOnHand	  */
	public void setTotalOnHand (BigDecimal TotalOnHand);

	/** Get TotalOnHand	  */
	public BigDecimal getTotalOnHand();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By	  */
	public int getUpdatedBy();
}
