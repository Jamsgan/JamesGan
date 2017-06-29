package datapager.datapager;

import java.sql.SQLException;

public class DataPagerSQLException extends SQLException {

	private static final long serialVersionUID = 3424948223257267142L;

	public DataPagerSQLException(final String reason, final SQLException cause) {
		super(reason, cause);
	}

}