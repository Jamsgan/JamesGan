package datapager.tools.databaseconnector;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static datapager.util.DatabaseUtility.checkConnection;
import datapager.datapager.DataPagerException;
import static datapager.util.Utility.isBlank;
import datapager.util.DataPagerLogger;
import datapager.util.StringFormat;

/**
 * 数据库链接注册
 * 
 * @author James
 */
public final class DatabaseConnectorRegistry implements Iterable<String> {
	private static final Logger logger = LoggerFactory.getLogger(DefaultDatabaseConnector.class);

	/**
	 * 加载数据库链接
	 * 
	 * @return
	 * @throws DataPagerException
	 */
	private static Map<String, DatabaseConnector> loadDatabaseConnectorRegistry() throws DataPagerException {
		final Map<String, DatabaseConnector> databaseConnectorRegistry = new HashMap<>();
		try {
			/**
			 * 加载实现类信息
			 */
			logger.debug("start load  DatabaseConnector service");
			final ServiceLoader<DatabaseConnector> serviceLoader = ServiceLoader.load(DatabaseConnector.class);
			for (final DatabaseConnector databaseConnector : serviceLoader) {
				// 得到数据库类型（mysql/oracle）
				final String databaseSystemIdentifier = databaseConnector.getDatabaseServerType()
						.getDatabaseSystemIdentifier();
				try {
					logger.debug("Loading database connector, {}={}",
							databaseSystemIdentifier, databaseConnector.getClass().getName());
					// Validate that the JDBC driver is available
					databaseConnector.checkDatabaseConnectionOptions();
					// Put in map
					databaseConnectorRegistry.put(databaseSystemIdentifier, databaseConnector);
				} catch (final Exception e) {
					logger.error("Could not load database connector, {}={}",
							databaseSystemIdentifier, databaseConnector.getClass().getName(), e);
				}
			}
		} catch (final Exception e) {
			throw new DataPagerException("Could not load database connector registry", e);
		}

		return databaseConnectorRegistry;
	}

	private final Map<String, DatabaseConnector> databaseConnectorRegistry;

	public DatabaseConnectorRegistry() throws DataPagerException {
		databaseConnectorRegistry = loadDatabaseConnectorRegistry();
		logRegisteredJdbcDrivers();
	}

	public boolean hasDatabaseSystemIdentifier(final String databaseSystemIdentifier) {
		return databaseConnectorRegistry.containsKey(databaseSystemIdentifier);
	}

	/*
	 * @Override public Iterator<String> iterator() { return
	 * lookupAvailableDatabaseConnectors().iterator(); }
	 */
	public DatabaseConnector lookupDatabaseConnector(final Connection connection) {
		try {
			checkConnection(connection);
			final String url = connection.getMetaData().getURL();
			return lookupDatabaseConnectorFromUrl(url);
		} catch (final SQLException | DataPagerException e) {
			return DatabaseConnector.UNKNOWN;
		}
	}

	/**
	 * 根据数据库类型查找数据库信息
	 * 
	 * @param databaseSystemIdentifier
	 * @return
	 */
	public DatabaseConnector lookupDatabaseConnector(final String databaseSystemIdentifier) {
		if (hasDatabaseSystemIdentifier(databaseSystemIdentifier)) {
			return databaseConnectorRegistry.get(databaseSystemIdentifier);
		} else {
			return DatabaseConnector.UNKNOWN;
		}
	}

	/**
	 * 查询数据库链接通过url识别
	 * 
	 * @param url
	 * @return
	 */
	public DatabaseConnector lookupDatabaseConnectorFromUrl(final String url) {
		if (isBlank(url)) {
			return DatabaseConnector.UNKNOWN;
		}
		for (final DatabaseConnector databaseConnector : databaseConnectorRegistry.values()) {
			final Pattern connectionUrlPattern = databaseConnector.getConnectionUrlPattern();
			if (connectionUrlPattern == null) {
				continue;
			}
			if (connectionUrlPattern.matcher(url).matches()) {
				return databaseConnector;
			}
		}
		// return DatabaseConnector.UNKNOWN;
		return new DefaultDatabaseConnector();
	}

	private void logRegisteredJdbcDrivers() {
		if (!logger.isDebugEnabled()) {
			return;
		}
		try {
			final StringBuilder buffer = new StringBuilder(1024);
			buffer.append("Registered JDBC drivers:");
			for (final Driver driver : Collections.list(DriverManager.getDrivers())) {
				buffer.append(String.format("%n%s %d.%d", driver.getClass().getName(), driver.getMajorVersion(),
						driver.getMinorVersion()));
			}
			logger.debug(buffer.toString());
		} catch (final Exception e) {
			logger.error("Could not log registered JDBC drivers", e);
		}
	}

	private Collection<String> lookupAvailableDatabaseConnectors() {
		final List<String> availableDatabaseConnectors = new ArrayList<>(databaseConnectorRegistry.keySet());
		Collections.sort(availableDatabaseConnectors);
		return availableDatabaseConnectors;
	}

	@Override
	public Iterator<String> iterator() {
		return null;
	}

}
