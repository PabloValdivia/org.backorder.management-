/**

import java.math.BigDecimal;
import java.util.Properties;

public class MBM_OrderManagementLine extends X_BM_OrderManagementLine{

	private static final long serialVersionUID = -1L;

	public MBM_OrderManagementLine(Properties ctx, int id, String trxName) {
		super(ctx,id,trxName);
		}

	public MBM_OrderManagementLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
}