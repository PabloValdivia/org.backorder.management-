/**

import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

	public class RefreshData extends SvrProcess {


		ProcessInfoParameter[] para = getParameter();
			for (ProcessInfoParameter p:para) {
				String name = p.getParameterName();
				if (p.getParameter() == null)
				else if(name.equals("IsActive")){
					IsActive = "Y".equals(p.getParameter());
			}
		}
	}

		String whereClause = "EXISTS (SELECT T_Selection_ID FROM T_Selection WHERE T_Selection.AD_PInstance_ID=? AND T_Selection.T_Selection_ID=BM_OrderManagementLine.BM_OrderManagementLine_ID)";
		ArrayList<Integer> pmlines = new ArrayList<Integer>();
		List<MBM_OrderManagementLine> lines = new Query(Env.getCtx(),MBM_OrderManagementLine.Table_Name,whereClause,get_TrxName())
		.setParameters(getAD_PInstance_ID())

		for (MBM_OrderManagementLine pmline:lines){
			MOrderLine orderline = new Query(Env.getCtx(),MOrderLine.Table_Name,MOrderLine.COLUMNNAME_C_OrderLine_ID+"=?",get_TrxName())

	}
}