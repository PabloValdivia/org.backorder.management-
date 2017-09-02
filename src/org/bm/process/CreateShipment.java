/**


import org.compiere.model.Query;
import org.compiere.util.Env;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.compiere.util.DB;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MSequence;
import org.compiere.process.SvrProcess;

	public class CreateShipment extends SvrProcess {


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

		List<MBM_OrderManagementLine> lines = new Query(Env.getCtx(),MBM_OrderManagementLine.Table_Name,whereClause,get_TrxName())
		.setParameters(getAD_PInstance_ID()).list();

		for (MBM_OrderManagementLine line:lines){
			int a = line.get_ID();

			log.info("Selected line ID = "+a);

	}

	return "RESULT: "+lines.toString();

	}
}