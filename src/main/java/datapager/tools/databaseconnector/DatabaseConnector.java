package datapager.tools.databaseconnector;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import static datapager.util.Utility.isBlank;

import datapager.datapager.AbstractProduceSql;
import datapager.datapager.Config;
import datapager.datapager.DataPagerException;
import datapager.datapager.IProduceSql;
import datapager.datapager.SingleUseUserCredentials;
import datapager.datapager.UserCredentials;

/**
 * ���ݿ����ӳ�����
 * 
 * @author James
 */
public abstract class DatabaseConnector implements Serializable {

	private static final long serialVersionUID = 6133330582637434099L;
	private static final Logger logger = LoggerFactory.getLogger(DefaultDatabaseConnector.class);
	/**
	 * ����һ��δ֪����
	 */
	protected static final DatabaseConnector UNKNOWN = new DatabaseConnector() {
		private static final long serialVersionUID = 3057770737518232349L;
	};
	/** ���ݿ����� */
	private final DatabaseServerType dbServerType;
	/** �����ĵ�·�� */
	private final String connectionHelpResource;
	/** ���ݿ�������Ϣ�ļ�·�� */
	private final String configResource;
	/** ��ҳsql�ӿ� */
	private final AbstractProduceSql ProduceSqlImpl;
	/** url��ʽ */
	private final Pattern connectionUrlPattern;

	protected DatabaseConnector(final DatabaseServerType dbServerType, final String connectionHelpResource,
			final String configResource, final AbstractProduceSql ProduceSqlImpl, final String connectionUrlPrefix) {
		this.dbServerType = requireNonNull(dbServerType, "No database server type provided");
		if (isBlank(connectionHelpResource)) {
			throw new IllegalArgumentException("No connection help resource provided");
		}
		this.connectionHelpResource = connectionHelpResource;

		this.configResource = configResource;
		this.ProduceSqlImpl = ProduceSqlImpl;

		if (isBlank(connectionUrlPrefix)) {
			throw new IllegalArgumentException("No JDBC connection URL prefix provided");
		}
		// ��֤url
		connectionUrlPattern = Pattern.compile(connectionUrlPrefix);
		logger.debug("Init DatabaseConnector success with {},{},{},{}",
				dbServerType.getDatabaseSystemName(),connectionHelpResource,configResource,ProduceSqlImpl);
	}

	private DatabaseConnector() {
		dbServerType = DatabaseServerType.UNKNOWN;
		connectionHelpResource = null;
		configResource = null;
		ProduceSqlImpl = null;
		connectionUrlPattern = null;
	}

	/**
	 * Checks if the database connection options are valid, the JDBC driver
	 * class can be loaded, and so on. Throws an exception if there is a
	 * problem.
	 *
	 * @throws DataPagerException
	 *             If there is a problem with creating connection options.
	 */
	public void checkDatabaseConnectionOptions() throws DataPagerException {

		newDatabaseConnectionOptions(new SingleUseUserCredentials(), null);

	}

	/**
	 * Gets the complete bundled database configuration set. This is useful in
	 * building the DataPager options.
	 */
	public final Config getConfig() {
		final Config config = Config.loadResource(configResource);
		return config;
	}

	public String getConnectionHelpResource() {
		return connectionHelpResource;
	}

	public final Pattern getConnectionUrlPattern() {
		return connectionUrlPattern;
	}

	public DatabaseServerType getDatabaseServerType() {
		return dbServerType;
	}

	public boolean isUnknownDatabaseSystem() {
		return dbServerType.isUnknownDatabaseSystem();
	}

	/**
	 * Creates a datasource for connecting to a database. Additional connection
	 * options are provided, from the command-line, and configuration file.
	 *
	 * @param additionalConfig
	 *            Configuration from the command-line, and from configuration
	 *            files.
	 */
	public ConnectionOptions newDatabaseConnectionOptions(final UserCredentials userCredentials,
			final Config additionalConfig) throws DataPagerException {
		if (userCredentials == null) {
			throw new IllegalArgumentException("No database connection user credentials provided");
		}

		final Config config = getConfig();
		if (additionalConfig != null) {
			config.putAll(additionalConfig);
			// Remove sensitive properties from the original configuration
			additionalConfig.remove("user");
			additionalConfig.remove("password");
		}

		final ConnectionOptions connectionOptions;
		if (dbServerType.isUnknownDatabaseSystem() || config.hasValue("url")) {
			connectionOptions = new DatabaseConnectionOptions(userCredentials, config);
		} else {
			connectionOptions = new DatabaseConfigConnectionOptions(userCredentials, config);
		}
		return connectionOptions;
	}

	public IProduceSql getProduceSqlImpl() {
		return ProduceSqlImpl;
	}
}