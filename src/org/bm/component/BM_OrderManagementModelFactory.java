/**
import java.sql.ResultSet;
import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;

public class BM_OrderManagementModelFactory implements IModelFactory {
	@Override 	public Class<?> getClass(String tableName) {
		 if (tableName.equals(MBM_OrderManagement.Table_Name)){
			 return MBM_OrderManagement.class;
		 }
  		return null;
	}
	@Override	public PO getPO(String tableName, int Record_ID, String trxName) {
		 if (tableName.equals(MBM_OrderManagement.Table_Name)) {
		     return new MBM_OrderManagement(Env.getCtx(), Record_ID, trxName);
		 }
  		return null;
	}
	@Override	public PO getPO(String tableName, ResultSet rs, String trxName) {
		 if (tableName.equals(MBM_OrderManagement.Table_Name)) {
		     return new MBM_OrderManagement(Env.getCtx(), rs, trxName);
		   }
 		 return null;
	}
}