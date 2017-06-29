package datapager.tools.databaseconnector;

import java.util.Map;

import datapager.datapager.DataPagerException;
import datapager.datapager.SingleUseUserCredentials;
import datapager.datapager.UserCredentials;

public final class DatabaseConnectionOptions extends BaseDatabaseConnectionOptions {
	
	private static final long serialVersionUID = -8141436553988174836L;

	public DatabaseConnectionOptions(final String connectionUrl) throws DataPagerException {
		super(new SingleUseUserCredentials(), toMap(connectionUrl));
	}

	public DatabaseConnectionOptions(final UserCredentials userCredentials, final Map<String, String> properties)
			throws DataPagerException {
		super(userCredentials, properties);
	}

}
