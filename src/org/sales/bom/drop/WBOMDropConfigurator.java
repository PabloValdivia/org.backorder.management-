/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/

/**
 * 2007, Modified by Posterita Ltd.
 */

package org.sales.bom.drop;

import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.Callback;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPricing;
import org.compiere.model.MProject;
import org.compiere.model.MProjectLine;
import org.compiere.model.MProjectPhase;
import org.compiere.model.MProjectTask;
import org.compiere.model.MRole;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.eevolution.model.MPPProductBOMLine;
import org.bm.model.MBM_OrderManagement;
import org.bm.model.MBM_OrderManagementLine;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;

public class WBOMDropConfigurator extends ADForm implements EventListener<Event>, PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8864346687201400591L;

	/**	Product to create BOMs from	*/
	private MProduct m_product;
	
	/** BOM Qty						*/
	private BigDecimal m_qty = Env.ONE;
	
	private Tree testProductBOMTree;
	/**	Line Counter				*/
	@SuppressWarnings("unused")
	private int m_bomLine = 0;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WBOMDropConfigurator.class);
	
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Grid selectionPanel = GridFactory.newGridLayout();
	private Listbox productField = new Listbox();
	private Listbox priceListField = new Listbox();
	private Decimalbox productQty = new Decimalbox();
	private Listbox orderField = new Listbox();
	private Listbox invoiceField = new Listbox();
	private Listbox projectPhaseField = new Listbox();
	private Listbox projectTaskField = new Listbox();
	private Listbox projectField = new Listbox();
	private Label totalDisplay = new Label();
	
	private Groupbox grpSelectionPanel = new Groupbox();
	int recordID = 0;
	int lineno = 0;
	
	public WBOMDropConfigurator()
	{}
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame parent frame
	 */
	protected void initForm()
	{
		log.info("");

		try
		{
			confirmPanel = new ConfirmPanel(true);
			//	Top Selection Panel
			createSelectionPanel(true, false, false);

			//	Center
			createMainPanel();

			confirmPanel.addActionListener(Events.ON_CLICK, this);
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		//sizeIt();
	}	//	init

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (selectionPanel != null)
			selectionPanel.getChildren().clear();
		
		selectionPanel = null;
	}	//	dispose
	
	/*
	 * Setting Job main items for instant display
	 */
	private MBM_OrderManagement setJobInfo(int record){
		MBM_OrderManagement job = new MBM_OrderManagement(Env.getCtx(), record, null); 
		//abort if Order is processed.
		MOrder order = new Query(Env.getCtx(),MOrder.Table_Name,MOrder.COLUMNNAME_C_Order_ID+"=?",null)
				.setParameters(job.getC_Order_ID())
				.first();
		if (order==null)
			throw new AdempiereException("Generate Sales Order from Order Management Lines first.");
		if (order.getDocStatus().equals(MOrder.DOCSTATUS_Completed))
			throw new AdempiereException("Order Completed. BOM Drop not allowed onto it");
		
		productField.removeAllItems();		
		KeyNamePair[] keyNamePair = getProductsPriceListQty(job.get_ID());		
		for (int i = 0; i < keyNamePair.length; i++)
		{
			productField.addItem(keyNamePair[i]);
		}
		ListItem listitem = productField.getSelectedItem();
		KeyNamePair pp = null;
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		m_product = pp!= null ? MProduct.get (Env.getCtx(), pp.getKey()) : null;
		
		return job;
	}
	
	private void setOrder(MBM_OrderManagement job) {
		orderField.removeAllItems();
		KeyNamePair[] keyNamePair = getOrders(job);
		for (int i = 0; i < keyNamePair.length; i++)
		{
			orderField.addItem(keyNamePair[i]);
		} 
	}
	
	private KeyNamePair[] getOrders(MBM_OrderManagement job)
	{ 
		String sql = "SELECT o.C_Order_ID, o.DocumentNo || '_' || o.GrandTotal "
			+ "FROM C_Order o INNER JOIN BM_OrderManagement j ON (o.C_Order_ID=j.C_Order_ID) "
			+ "WHERE j.BM_OrderManagement_ID="+job.get_ID();
	
		return DB.getKeyNamePairs(sql, false);
	}	//	getOrders
	
	/**
	 * Get Products List default to latest updated, and set from Job PriceList and Line Qty also.
	 * @param record
	 * @return
	 */
	private KeyNamePair[] getProductsPriceListQty(int record)
	{
		//set Line Qty
		String SQL = "SELECT BM_OrderManagementLine_ID FROM BM_OrderManagementLine ml INNER JOIN PP_Product_BOM b ON (b.M_Product_ID=ml.M_Product_ID) "
				+"WHERE BM_OrderManagement_ID="+record+" order by ml.updated desc";
		int mgmtlineID = DB.getSQLValue(null, SQL);
		MBM_OrderManagementLine mgmtline = new Query(Env.getCtx(),MBM_OrderManagementLine.Table_Name,MBM_OrderManagementLine.COLUMNNAME_BM_OrderManagementLine_ID+"=?",null)
				.setParameters(mgmtlineID)
				.first(); 
		productQty.setValue(mgmtline.getQtyOrdered());
		m_qty = productQty.getValue();
		
		//get JobLine LineNo of Product parent
		lineno=mgmtline.getLineNo();
		
		//set pricelist
		priceListField.removeAllItems();
		KeyNamePair[] keyNamePair = getPriceList(record);
		for (int i = 0; i < keyNamePair.length; i++)
		{
			priceListField.addItem(keyNamePair[i]);
		}
		ListItem listitem = priceListField.getSelectedItem();	
		KeyNamePair pp = null;		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		ProductBOMTreeNode.PriceListVersion = pp.getKey();
		
		//return Products defaulted to updated JobLine
		String sql = "SELECT p.M_Product_ID, p.Name "
			+ "FROM PP_Product_BOM p INNER JOIN BM_OrderManagementLine l ON (p.M_Product_ID=l.M_Product_ID) "
			+ "WHERE p.IsActive='Y' AND l.BM_OrderManagement_ID="+record+" AND p.AD_Client_ID="+ Env.getAD_Client_ID(Env.getCtx())
			+ " ORDER BY l.Updated DESC";
 		return DB.getKeyNamePairs( 
			sql,false);
	}	//	getProducts
	
	private KeyNamePair[] getPriceList(int jobID)
	{
		String sql = "SELECT v.M_PriceList_Version_ID, v.Name  "
			+ "FROM M_PriceList_Version v INNER JOIN BM_OrderManagement j ON (j.M_PriceList_Version_ID=v.M_PriceList_Version_ID) "
			+ "WHERE j.BM_OrderManagement_ID ="+jobID+" AND v.IsActive='Y'";
	
		return DB.getKeyNamePairs(sql, false);
	}	//	getPriceList
	
	
	/**************************************************************************
	 * 	Create Selection Panel
	 *	@param order
	 *	@param invoice
	 *	@param project
	 */
	
	private void createSelectionPanel (boolean order, boolean invoice, boolean project)
	{
		Caption caption = new Caption(Msg.translate(Env.getCtx(), "Selection"));

//		grpSelectionPanel.setWidth("100%");
		grpSelectionPanel.appendChild(caption);
		grpSelectionPanel.appendChild(selectionPanel);
		
		productField.setRows(1);
		productField.setMold("select");
		
		KeyNamePair[] keyNamePair = getProducts();
		
		for (int i = 0; i < keyNamePair.length; i++)
		{
			productField.addItem(keyNamePair[i]);
		}
		
		Rows rows = selectionPanel.newRows();
		Row boxProductQty = rows.newRow();
		
		Label lblProduct = new Label(Msg.translate(Env.getCtx(), "M_Product_ID"));
		Label lblQty = new Label(Msg.translate(Env.getCtx(), "Qty"));
		productQty.setValue(Env.ONE);
		productField.addEventListener(Events.ON_SELECT, this);
		productQty.addEventListener(Events.ON_CHANGE, this);
		
		productField.setWidth("99%");
		boxProductQty.appendChild(lblProduct.rightAlign());
		boxProductQty.appendChild(productField);
		boxProductQty.appendChild(lblQty.rightAlign());
		boxProductQty.appendChild(productQty);
		
		if (order)
		{
			keyNamePair = getOrders();
			
			orderField.setRows(1);
			orderField.setMold("select");
			orderField.setWidth("99%");
			
			for (int i = 0; i < keyNamePair.length; i++)
			{
				orderField.addItem(keyNamePair[i]);
			}
			keyNamePair = getPriceList();
			
			priceListField.setRows(1);
			priceListField.setMold("select");
			priceListField.setWidth("99%"); 
			for (int i = 0; i < keyNamePair.length; i++)
			{
				priceListField.addItem(keyNamePair[i]);
			}
			Label lblOrder = new Label(Msg.translate(Env.getCtx(), "C_Order_ID"));
			Label lblPriceList = new Label(Msg.translate(Env.getCtx(), "Price"));
			
			Row boxOrder = rows.newRow();
			
			orderField.addEventListener(Events.ON_SELECT, this);
			priceListField.addEventListener(Events.ON_SELECT, this);
			
			boxOrder.appendChild(lblOrder.rightAlign());
			boxOrder.appendChild(orderField);
			boxOrder.appendChild(lblPriceList.rightAlign());
			boxOrder.appendChild(priceListField);
		}

		if (invoice)
		{
			invoiceField.setRows(1);
			invoiceField.setMold("select");
			invoiceField.setWidth("99%");
			
			keyNamePair = getInvoices();
			
			for (int i = 0; i < keyNamePair.length; i++)
			{
				invoiceField.addItem(keyNamePair[i]);
			}
			
			Label lblInvoice = new Label(Msg.translate(Env.getCtx(), "C_Invoice_ID"));
			
			Row boxInvoices = rows.newRow();
			
			invoiceField.addEventListener(Events.ON_SELECT, this);
			
			boxInvoices.appendChild(lblInvoice.rightAlign());
			boxInvoices.appendChild(invoiceField);
			boxInvoices.appendChild(new Space());
			boxInvoices.appendChild(new Space());
		}
		
		if (project)
		{
			projectField.setRows(1);
			projectField.setMold("select");
			projectField.setWidth("99%");
			
			keyNamePair = getProjects();
			
			for (int i = 0; i < keyNamePair.length; i++)
			{
				projectField.addItem(keyNamePair[i]);
			}
			
			Label lblProject = new Label(Msg.translate(Env.getCtx(), "C_Project_ID"));
			
			Row boxProject = rows.newRow();
			
			projectField.addEventListener(Events.ON_SELECT, this);
			
			boxProject.appendChild(lblProject.rightAlign());
			boxProject.appendChild(projectField);
			boxProject.appendChild(new Space());
			boxProject.appendChild(new Space());
			
			//ProjectPhase
			projectPhaseField.setRows(1);
			projectPhaseField.setMold("select");
			projectPhaseField.setWidth("99%");
			
			keyNamePair = getProjectPhases();
			
			for (int i = 0; i < keyNamePair.length; i++)
			{
				projectPhaseField.addItem(keyNamePair[i]);
			}
			
			Label lblProjectPhase = new Label(Msg.translate(Env.getCtx(), "C_ProjectPhase_ID"));
			
			Row boxProjectPhase = rows.newRow();
			
			projectPhaseField.addEventListener(Events.ON_SELECT, this);
			
			boxProjectPhase.appendChild(lblProjectPhase.rightAlign());
			boxProjectPhase.appendChild(projectPhaseField); 
			
			//ProjectTask
			projectTaskField.setRows(1);
			projectTaskField.setMold("select");
			projectTaskField.setWidth("99%");
			
			keyNamePair = getProjectTasks();
			
			for (int i = 0; i < keyNamePair.length; i++)
			{
				projectTaskField.addItem(keyNamePair[i]);
			}
			
			Label lblProjectTask = new Label(Msg.translate(Env.getCtx(), "C_ProjectTask_ID")); 
			
			projectTaskField.addEventListener(Events.ON_SELECT, this);
			
			boxProjectPhase.appendChild(lblProjectTask.rightAlign());
			boxProjectPhase.appendChild(projectTaskField); 
		}
		
		//	Enabled in ActionPerformed
		confirmPanel.setEnabled("Ok", false);
	}	//	createSelectionPanel

	private KeyNamePair[] getProjectTasks() {
		String sql = "SELECT C_ProjectTask_ID, Name "
					+ "FROM C_ProjectTask WHERE IsActive='Y'";
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
					sql, "C_ProjectTask", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true); 
		
	}
 
	private KeyNamePair[] getProjectPhases() { 
		String sql = "SELECT C_ProjectPhase_ID, Name "
					+ "FROM C_ProjectPhase WHERE IsActive='Y'";
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
					sql, "C_ProjectPhase", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}
	
	/**
	 * 	Get Array of BOM Products
	 *	@return products
	 */
	
	private KeyNamePair[] getProducts()
	{
		String sql = "SELECT M_Product_ID, Name "
			+ "FROM PP_Product_BOM "
			+ "WHERE IsActive='Y' AND AD_Client_ID="+ Env.getAD_Client_ID(Env.getCtx())
			+ " ORDER BY Name";
	
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			sql, "M_Product", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}	//	getProducts
	
	/**
	 * 	Get Array of open Orders
	 *	@return orders
	 */
	
	private KeyNamePair[] getOrders()
	{
		String sql = "SELECT C_Order_ID, DocumentNo || '_' || GrandTotal "
			+ "FROM C_Order "
			+ "WHERE Processed='N' AND DocStatus='DR' "
			+ "ORDER BY DocumentNo";
	
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			sql, "C_Order", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}	//	getOrders

	/**
	 * 	Get Array of PriceLists
	 *	@return orders
	 */
	
	private KeyNamePair[] getPriceList()
	{
		String sql = "SELECT M_PriceList_Version_ID, Name  "
			+ "FROM M_PriceList_Version "
			+ "WHERE IsActive='Y'";
	
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			sql, "M_PriceList_Version", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}	//	getPriceList
	/**
	 * 	Get Array of open non service Projects
	 *	@return orders
	 */
	
	private KeyNamePair[] getProjects()
	{
		String sql = "SELECT C_Project_ID, Name "
			+ "FROM C_Project "
			+ "WHERE Processed='N' AND IsSummary='N' AND IsActive='Y'"
			+ " AND ProjectCategory<>'S' "
			+ "ORDER BY Name";
	
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			sql, "C_Project", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}	//	getProjects
	
	/**
	 * 	Get Array of open Invoices
	 *	@return invoices
	 */
	
	private KeyNamePair[] getInvoices()
	{
		String sql = "SELECT C_Invoice_ID, DocumentNo || '_' || GrandTotal "
			+ "FROM C_Invoice "
			+ "WHERE Processed='N' AND DocStatus='DR' "
			+ "ORDER BY DocumentNo";
	
		return DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			sql, "C_Invoice", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO), true);
	}	//	getInvoices

	/**************************************************************************
	 * 	Create Main Panel.
	 * 	Called when changing Product
	 */
	
	private void createMainPanel ()
	{
		if (log.isLoggable(Level.CONFIG)) log.config(": " + m_product);
		this.getChildren().clear();
		//this.invalidate();
		//this.setBorder(null);
		ProductBOMRendererListener.setGrandTotal(Env.ZERO);
		this.appendChild(new Separator());
		this.appendChild(grpSelectionPanel);
		this.appendChild(new Separator());
		Hlayout row = new Hlayout();
				totalDisplay.setValue(Msg.translate(Env.getCtx(),"GrandTotal")+" "+Msg.translate(Env.getCtx(),"Price")+": "+ProductBOMRendererListener.getGrandTotal());
				totalDisplay.setStyle("font-size:32px;color:gray;font-weight: bold;"); 
				row.appendChild(totalDisplay);
				row.appendChild(confirmPanel);
				row.setStyle("text-align:right");
				this.appendChild(row);
		//this.appendChild(confirmPanel);
		this.appendChild(new Separator());
		this.setBorder("normal");
		this.setContentStyle("overflow: auto");		
		
		if (m_product != null && m_product.get_ID() > 0)
		{
			if (m_product.getDescription() != null && m_product.getDescription().length() > 0)
				;//this.setsetToolTipText(m_product.getDescription());
			
			m_bomLine = 0;
			
			testProductBOMTree = new Tree();
			testProductBOMTree.appendChild(new Treecols());
			testProductBOMTree.getTreecols().appendChild(new Treecol(m_product.getName())); //column header
			SupportRadioTreeModel model = new SupportRadioTreeModel(new ProductBOMTreeNode(m_product, m_qty));
			SupportRadioTreeitemRenderer renderer = new SupportRadioTreeitemRenderer();
			renderer.isOpen = true;
			ProductBOMRendererListener rendererListener = new ProductBOMRendererListener();
			rendererListener.setTree(testProductBOMTree);
			rendererListener.addPropertyChangeListener(this);
			renderer.setRendererListener(rendererListener);
			testProductBOMTree.setItemRenderer(renderer);
			testProductBOMTree.setModel(model);
			this.appendChild(testProductBOMTree);
		}
	}	//	createMainPanel

	public void propertyChange(java.beans.PropertyChangeEvent event)
	{
		if (event.getPropertyName().equals("GrandTotal"))
		{
			totalDisplay.setValue(Msg.translate(Env.getCtx(),"GrandTotal")+" "+Msg.translate(Env.getCtx(),"Price")+": "+ProductBOMRendererListener.getGrandTotal());
		}
	}
	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void onEvent (Event e) throws Exception
	{
		if (log.isLoggable(Level.CONFIG)) log.config(e.getName());

		//setting values from Job Production that calls BOM Drop form via its window process
		if (recordID==0){
			recordID=getProcessInfo().getRecord_ID(); 	
			setOrder(setJobInfo(recordID));
			//recreate BOM display instantly

			createMainPanel();
		}
		
		Object source = e.getTarget();

		if (source == productField || source == productQty)
		{
			m_qty = productQty.getValue();
			
			if (source == productQty && testProductBOMTree != null){
				((ProductBOMTreeNode)testProductBOMTree.getModel().getRoot()).setQty(m_qty);
			}
			
			ListItem listitem = productField.getSelectedItem();
			
			KeyNamePair pp = null;
			
			if (listitem != null)
				pp = listitem.toKeyNamePair();
			
			m_product = pp!= null ? MProduct.get (Env.getCtx(), pp.getKey()) : null;
			createMainPanel();
			//sizeIt();
		}
		
		else if (source==priceListField){
			//get PriceList
			ListItem listitem = priceListField.getSelectedItem();	
			KeyNamePair pp = null;		
			if (listitem != null)
				pp = listitem.toKeyNamePair();
			ProductBOMTreeNode.PriceListVersion = pp.getKey();
			createMainPanel();
		}
		
		//	Order
		else if (source == orderField)
		{
			ListItem listitem = orderField.getSelectedItem();
			
			KeyNamePair pp = null;
			
			if (listitem != null)
				pp = listitem.toKeyNamePair();
			
			boolean valid = (pp != null && pp.getKey() > 0);
			
			if (invoiceField != null)
				invoiceField.setEnabled(!valid);
			if (projectField != null){
				projectField.setEnabled(!valid);
				projectPhaseField.setEnabled(!valid);
				projectTaskField.setEnabled(!valid);
			}
				
		}
		//	Invoice
		else if (source == invoiceField)
		{
			ListItem listitem = invoiceField.getSelectedItem();
			
			KeyNamePair pp = null;
						
			if (listitem != null)
				pp = listitem.toKeyNamePair();
			
			boolean valid = (pp != null && pp.getKey() > 0);
			
			if (orderField != null)
				orderField.setEnabled(!valid);
			if (projectField != null)
				projectField.setEnabled(!valid);
		}
		//	Project
		else if (source == projectField)
		{
			ListItem listitem = projectField.getSelectedItem();
			
			KeyNamePair pp = null;
			
			if (listitem != null)
				pp = listitem.toKeyNamePair();
			
			boolean valid = (pp != null && pp.getKey() > 0);
			//
			if (orderField != null)
				orderField.setEnabled(!valid);
			if (invoiceField != null)
				invoiceField.setEnabled(!valid);
		}
		//	OK
		else if (confirmPanel.getButton("Ok").equals(e.getTarget()))
		{
			if (onSave()){
				SessionManager.getAppDesktop().closeActiveWindow();
			}	
		}
		else if (confirmPanel.getButton("Cancel").equals(e.getTarget())){
			SessionManager.getAppDesktop().closeActiveWindow();
		}else
		{
			super.onEvent(e);
		}
			
		//	Enable OK
		boolean OK = m_product != null;
		
		if (OK)
		{
			KeyNamePair pp = null;
			
			if (orderField != null)
			{
				ListItem listitem = orderField.getSelectedItem();
				
				if (listitem != null)
					pp = listitem.toKeyNamePair();
			}
			
			if ((pp == null || pp.getKey() <= 0) && invoiceField != null)
			{
				ListItem listitem = invoiceField.getSelectedItem();
				
				if (listitem != null)
					pp = listitem.toKeyNamePair();
			}
			
			if ((pp == null || pp.getKey() <= 0) && projectField != null)
			{
				ListItem listitem = projectField.getSelectedItem();
				
				if (listitem != null)
					pp = listitem.toKeyNamePair();
			}
			OK = (pp != null && pp.getKey() > 0);
		}
		
		confirmPanel.setEnabled("Ok", OK);
	}	//	actionPerformed

	/**
	 * 	Enable/disable qty based on selection
	 *	@param source JCheckBox or JRadioButton
	 */
	private boolean onSave()
	{
		String trxName = Trx.createTrxName("BDP");	
		Trx localTrx = Trx.get(trxName, true);	//trx needs to be committed too
		try
		{
			if (cmd_save(localTrx)) 
			{
				localTrx.commit();
				return true;
			}
			else
			{
				localTrx.rollback();
				return false;
			}
			
		}
		finally 
		{
			localTrx.close();
		}
	}
	
	/**************************************************************************
	 * 	Save Selection
	 *  @param trx
	 * 	@return true if saved
	 */
	
	private boolean cmd_save(Trx trx)
	{
		ListItem listitem = orderField.getSelectedItem();
		
		KeyNamePair pp = null;
		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		
		if (pp != null && pp.getKey() > 0)
			return cmd_saveOrder (pp.getKey(), trx);
		
		listitem = invoiceField.getSelectedItem();
		
		pp = null;		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		
		if (pp != null && pp.getKey() > 0)
			return cmd_saveInvoice (pp.getKey(), trx);
		
		listitem = projectField.getSelectedItem();		
		pp = null;
		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		
		if (pp != null && pp.getKey() > 0)
			return cmd_saveProject (pp.getKey(), trx);
		
		listitem = projectPhaseField.getSelectedItem();		
		pp = null;
		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		
		if (pp != null && pp.getKey() > 0)
			return cmd_saveProjectPhase (pp.getKey(), trx);
		
		listitem = projectTaskField.getSelectedItem();	
		pp = null;
		
		if (listitem != null)
			pp = listitem.toKeyNamePair();
		
		if (pp != null && pp.getKey() > 0)
			return cmd_saveProjectTask (pp.getKey(), trx);
		
		log.log(Level.SEVERE, "Nothing selected");
		return false;
	}	//	cmd_save

	class TreeItemData {
		ISupportRadioNode dataNode;
		WNumberEditor inputQty;
		public TreeItemData (ISupportRadioNode dataNode, WNumberEditor inputQty){
			this.dataNode = dataNode;
			this.inputQty = inputQty;
		}
	}
	
	protected void travellerTreenode (Tree tree, ISupportRadioNode nodeModel, boolean isRootNode, Callback<TreeItemData> processNode){
		if (!isRootNode && !nodeModel.isChecked()){
			return;
		}
		ProductBOMTreeNode node = (ProductBOMTreeNode)nodeModel;
		int [] nodePath = tree.getModel().getPath(nodeModel);
		Treeitem treeItem = tree.renderItemByPath(nodePath);
		
		if (node.getChildCount()==0) //parent node not included - only final children accepted in drop
			processNode.onCallback(new TreeItemData(nodeModel, (WNumberEditor)treeItem.getAttribute(ProductBOMRendererListener.QTY_COMPONENT)));
		
		for (int i = 0; i < nodeModel.getChildCount(); i++){
			ISupportRadioNode childNode = nodeModel.getChild(i);
			travellerTreenode (tree, childNode, false, processNode);
		}
	}
	
	/**
	 * 	Save to Order
	 *	@param C_Order_ID id
	 *  @param trx 
	 *	@return true if saved
	 */
	Integer lineCount = 0;
	BigDecimal grandtotal = Env.ZERO;
	private boolean cmd_saveOrder (int C_Order_ID, final Trx trx)
	{
		String trxName = trx.getTrxName();
		if (log.isLoggable(Level.CONFIG)) log.config("C_Order_ID=" + C_Order_ID);
		final MOrder order = new MOrder (Env.getCtx(), C_Order_ID,trxName);
		//get Order Manager
		final MBM_OrderManagement mgmt = new Query(Env.getCtx(),MBM_OrderManagement.Table_Name,MBM_OrderManagement.COLUMNNAME_C_Order_ID+"=?",trxName)
				.setParameters(order.get_ID())
				.first();
		//clear mgmt lines of c_orderline ids
		
		
		if (order.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Not found - C_Order_ID=" + C_Order_ID);
			return false;
		}	
		lineCount = 0;
		try 
		{
			ISupportRadioNode productRootNode = (ISupportRadioNode)testProductBOMTree.getModel().getRoot();
			travellerTreenode(testProductBOMTree, productRootNode, true, new Callback<TreeItemData>() {

				public void onCallback(TreeItemData itemData) {
					ProductBOMTreeNode productNode = (ProductBOMTreeNode)itemData.dataNode;
					BigDecimal qty =  productNode.getTotQty();
					int M_Product_ID = productNode.getProductID();
					//delete bom child order lines only -- find corresponding mgmt lines to delete also, if exist
					MOrderLine oline = new Query(Env.getCtx(),MOrderLine.Table_Name,MOrderLine.COLUMNNAME_C_Order_ID+"=? AND "
							+MOrderLine.COLUMNNAME_M_Product_ID+"=?",trxName)
							.setParameters(order.get_ID(),M_Product_ID)
							.first();
					if (oline!=null){
						MBM_OrderManagementLine mline = new Query(Env.getCtx(),MBM_OrderManagementLine.Table_Name,MBM_OrderManagementLine.COLUMNNAME_C_OrderLine_ID+"=?",trxName)
									.setParameters(oline.get_ID())
									.first(); 
							if (mline!=null)
								mline.delete(false);
							log.info("deleting old bomline - "+oline.getM_Product().getName());
							oline.delete(false);
					}
					
					// Create Line
					MOrderLine ol = new MOrderLine(order);
					ol.setM_Product_ID(M_Product_ID, true);
					ol.setQty(qty);
					ol.setPrice();
					ol.setTax();
					ol.saveEx(trxName);
					grandtotal = grandtotal.add(ol.getLineNetAmt());
					lineCount++;
				//
				//BackOrder Management Line creation/recreation
					MBM_OrderManagementLine mline = new MBM_OrderManagementLine(Env.getCtx(),0,trxName);
					mline.setBM_OrderManagement_ID(mgmt.get_ID());
					mline.setM_Product_ID(ol.getM_Product_ID());
					mline.setQtyOrdered(ol.getQtyOrdered());
					mline.setLineNetAmt(ol.getLineNetAmt());
					mline.setC_OrderLine_ID(ol.get_ID());
					mline.setDescription("bom child");
					lineno++;
					mline.setLineNo(lineno);
					mline.saveEx(trxName);
				}
			});
			mgmt.setGrandTotal(grandtotal);
			mgmt.saveEx(trxName);
			order.saveEx(trxName);
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "Line not saved");
			if (trx != null) 
			{
				trx.rollback();
			}
			throw new AdempiereException(e.getMessage());
		}				
		
		FDialog.info(-1, this, Msg.translate(Env.getCtx(), "C_Order_ID")+ " : " + order.getDocumentInfo() + " , " + Msg.translate(Env.getCtx(), "NoOfLines") + " " + Msg.translate(Env.getCtx(), "Inserted") + " = " + lineCount);
		if (log.isLoggable(Level.CONFIG)) log.config("#" + lineCount);
		return true;
	}	//	cmd_saveOrder

	/**
	 * 	Save to Invoice
	 *	@param C_Invoice_ID id
	 *  @param trx 
	 *	@return true if saved
	 */
	
	private boolean cmd_saveInvoice (int C_Invoice_ID, final Trx trx)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_Invoice_ID=" + C_Invoice_ID);
		final MInvoice invoice = new MInvoice (Env.getCtx(), C_Invoice_ID, trx != null ? trx.getTrxName() : null);
		if (invoice.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Not found - C_Invoice_ID=" + C_Invoice_ID);
			return false;
		}
		lineCount = 0;
		
		//	for all bom lines
		try 
		{
			ISupportRadioNode productRootNode = (ISupportRadioNode)testProductBOMTree.getModel().getRoot();
			travellerTreenode(testProductBOMTree, productRootNode, true, new Callback<TreeItemData>() {
				
				
				public void onCallback(TreeItemData itemData) {
					ProductBOMTreeNode productNode = (ProductBOMTreeNode)itemData.dataNode;
					BigDecimal qty =  productNode.getTotQty();
					int M_Product_ID = productNode.getProductID();
					//	Create Line
					MInvoiceLine il = new MInvoiceLine (invoice);
					il.setM_Product_ID(M_Product_ID, true);
					il.setQty(qty);
					il.setPrice();
					il.setTax();
					il.saveEx(trx.getTrxName());
					lineCount++;
				}
			});
			invoice.save(trx.getTrxName());
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "Line not saved");
			if (trx != null) 
			{
				trx.rollback();
			}
			throw new AdempiereException(e.getMessage());
		}		
		
		FDialog.info(-1, this, Msg.translate(Env.getCtx(), "C_Invoice_ID")+ " : " + invoice.getDocumentInfo() +  " , " + Msg.translate(Env.getCtx(), "NoOfLines") + " " + Msg.translate(Env.getCtx(), "Inserted") + " = " + lineCount);
		if (log.isLoggable(Level.CONFIG)) log.config("#" + lineCount);
		return true;
	}	//	cmd_saveInvoice

	/**
	 * 	Save to Project
	 *	@param C_Project_ID id
	 *  @param trx
	 *	@return true if saved
	 */
	private boolean cmd_saveProject (int C_Project_ID, final Trx trx)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_Project_ID=" + C_Project_ID);
		final MProject project = new MProject (Env.getCtx(), C_Project_ID, trx != null ? trx.getTrxName() : null);
		if (project.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Not found - C_Project_ID=" + C_Project_ID);
			return false;
		}
		lineCount = 0;
		
		//	for all bom lines
		try 
		{
			ISupportRadioNode productRootNode = (ISupportRadioNode)testProductBOMTree.getModel().getRoot();
			travellerTreenode(testProductBOMTree, productRootNode, true, new Callback<TreeItemData>() {
				
			
				public void onCallback(TreeItemData itemData) {
					ProductBOMTreeNode productNode = (ProductBOMTreeNode)itemData.dataNode;
					BigDecimal qty =  productNode.getTotQty();
					int M_Product_ID = productNode.getProductID();
					//	Create Line
					MProjectLine pl = new MProjectLine (project);
					pl.setM_Product_ID(M_Product_ID);
					pl.setPlannedQty(qty);
					pl.setPlannedPrice(getStandardPrice(M_Product_ID,qty.doubleValue(), project));
					pl.saveEx(trx.getTrxName());
					lineCount++;
				}
			});
			project.saveEx(trx.getTrxName());
			project.load(trx.getTrxName());
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "Line not saved");
			if (trx != null) 
			{
				trx.rollback();
			}
			throw new AdempiereException(e.getMessage());
		}		
		
		FDialog.info(-1, this, Msg.translate(Env.getCtx(), "C_Project_ID")+ " : " + project.getName() + " , " + Msg.translate(Env.getCtx(), "NoOfLines") + " " + Msg.translate(Env.getCtx(), "Inserted") + " = " + lineCount);
		if (log.isLoggable(Level.CONFIG)) log.config("#" + lineCount);
		return true;
	}	//	cmd_saveProject
	
	/**
	 * 	Save to Project
	 *	@param C_Project_ID id
	 *  @param trx
	 *	@return true if saved
	 */
	private boolean cmd_saveProjectPhase (int C_ProjectPhase_ID, final Trx trx)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_ProjectPhase_ID=" + C_ProjectPhase_ID);
		 MProjectPhase phase = new MProjectPhase (Env.getCtx(), C_ProjectPhase_ID, trx != null ? trx.getTrxName() : null);
		if (phase.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Not found - C_ProjectPhase_ID=" + C_ProjectPhase_ID);
			return false;
		}
		final MProject project = new Query(Env.getCtx(), MProject.Table_Name, MProject.COLUMNNAME_C_Project_ID+"=?", phase.get_TrxName())
		.setParameters(phase.getC_Project_ID())
		.first();
		
		lineCount = 0;
		
		//	for all bom lines
		try 
		{
			ISupportRadioNode productRootNode = (ISupportRadioNode)testProductBOMTree.getModel().getRoot();
			travellerTreenode(testProductBOMTree, productRootNode, true, new Callback<TreeItemData>() {
				
				
				public void onCallback(TreeItemData itemData) {
					ProductBOMTreeNode productNode = (ProductBOMTreeNode)itemData.dataNode;
					BigDecimal qty =  productNode.getTotQty();
					int M_Product_ID = productNode.getProductID();
					//	Create Line
					MProjectLine pl = new MProjectLine (project);
					pl.setM_Product_ID(M_Product_ID);
					pl.setPlannedQty(qty);
					pl.setPlannedPrice(getStandardPrice(M_Product_ID,qty.doubleValue(), project));
					pl.saveEx(trx.getTrxName());
					lineCount++;
				}
			});
			project.saveEx(trx.getTrxName());
			project.load(trx.getTrxName());
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "Line not saved");
			if (trx != null) 
			{
				trx.rollback();
			}
			throw new AdempiereException(e.getMessage());
		}		
		
		FDialog.info(-1, this, Msg.translate(Env.getCtx(), "C_Project_ID")+ " : " + project.getName() + " , " + Msg.translate(Env.getCtx(), "NoOfLines") + " " + Msg.translate(Env.getCtx(), "Inserted") + " = " + lineCount);
		if (log.isLoggable(Level.CONFIG)) log.config("#" + lineCount);
		return true;
	}	//	cmd_saveProject

	/**
	 * 	Save to Project
	 *	@param C_Project_ID id
	 *  @param trx
	 *	@return true if saved
	 */
	private boolean cmd_saveProjectTask (int C_ProjectTask_ID, final Trx trx)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_ProjectTask_ID=" + C_ProjectTask_ID);
		MProjectTask task = new MProjectTask (Env.getCtx(), C_ProjectTask_ID, trx != null ? trx.getTrxName() : null);
		if (task.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Not found - C_ProjectTask_ID=" + C_ProjectTask_ID);
			return false;
		}
		MProjectPhase phase = new Query(Env.getCtx(), MProjectPhase.Table_Name, MProjectPhase.COLUMNNAME_C_ProjectPhase_ID+"=?", task.get_TrxName())
		.setParameters(task.getC_ProjectPhase_ID())
		.first();
		final MProject project = new Query(Env.getCtx(), MProject.Table_Name, MProject.COLUMNNAME_C_Project_ID+"=?", task.get_TrxName())
		.setParameters(phase.getC_Project_ID())
		.first(); 
		lineCount = 0;
		
		//	for all bom lines
		try 
		{
			ISupportRadioNode productRootNode = (ISupportRadioNode)testProductBOMTree.getModel().getRoot();
			travellerTreenode(testProductBOMTree, productRootNode, true, new Callback<TreeItemData>() {
				
			
				public void onCallback(TreeItemData itemData) {
					ProductBOMTreeNode productNode = (ProductBOMTreeNode)itemData.dataNode;
					BigDecimal qty =  productNode.getTotQty();
					int M_Product_ID = productNode.getProductID();
					//	Create Line
					MProjectLine pl = new MProjectLine (project);
					pl.setM_Product_ID(M_Product_ID);
					pl.setPlannedQty(qty);
					pl.setPlannedPrice(getStandardPrice(M_Product_ID,qty.doubleValue(), project));
					pl.saveEx(trx.getTrxName());
					lineCount++;
				}
			});
			project.saveEx(trx.getTrxName());
			project.load(trx.getTrxName());
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "Line not saved");
			if (trx != null) 
			{
				trx.rollback();
			}
			throw new AdempiereException(e.getMessage());
		}		
		
		FDialog.info(-1, this, Msg.translate(Env.getCtx(), "C_Project_ID")+ " : " + project.getName() + " , " + Msg.translate(Env.getCtx(), "NoOfLines") + " " + Msg.translate(Env.getCtx(), "Inserted") + " = " + lineCount);
		if (log.isLoggable(Level.CONFIG)) log.config("#" + lineCount);
		return true;
	}	//	cmd_saveProject
	/**
	 * 	Get Limit Price if exists
	 *	@return limit
	 */
	private BigDecimal getStandardPrice(int M_Product_ID, Double plannedQty, MProject project)
	{
		
		MProductPricing pp = new MProductPricing (M_Product_ID,
			project.getC_BPartner_ID(), new BigDecimal(plannedQty), true);
		pp.setM_PriceList_ID(project.getM_PriceList_ID());
		if (pp.calculatePrice())
			return pp.getPriceStd();
		return Env.ZERO;
	}	//	getLimitPrice

}
