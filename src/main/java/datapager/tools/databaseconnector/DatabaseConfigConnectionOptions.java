package datapager.tools.databaseconnector;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static datapager.util.Utility.isBlank;
import datapager.datapager.DataPagerException;
import datapager.datapager.UserCredentials;

public final class DatabaseConfigConnectionOptions extends BaseDatabaseConnectionOptions {

	private static final long serialVersionUID = -8141436553988174836L;
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigConnectionOptions.class);

	private static final String HOST = "host";
	private static final String PORT = "port";
	private static final String DATABASE = "database";
	private static final String URLX = "urlx";

	public DatabaseConfigConnectionOptions(final UserCredentials userCredentials, final Map<String, String> properties)
			throws DataPagerException {
		super(userCredentials, properties);
	}

	public String getDatabase() {
		return connectionProperties.get(DATABASE);
	}

	public String getHost() {
		return connectionProperties.get(HOST);
	}

	public int getPort() {
		final String port = connectionProperties.get(PORT);
		try {
			return Integer.parseInt(port);
		} catch (final NumberFormatException e) {
			throw new IllegalArgumentException("Cannot connect to port, " + port);
		}
	}

	public String getUrlX() {
		return connectionProperties.get(URLX);
	}

	public void setDatabase(final String database) {
		// (database can be an empty string)
		if (database != null) {
			connectionProperties.put(DATABASE, database);
		}
	}

	public void setHost(final String host) {
		if (!isBlank(host)) {
			connectionProperties.put(HOST, host);
		}
	}

	public void setPort(final int port) {
		if (port > 0) {
			connectionProperties.put(PORT, String.valueOf(port));
		} else {
			logger.error("Cannot connect to port, {}",port);
		}
	}
}
