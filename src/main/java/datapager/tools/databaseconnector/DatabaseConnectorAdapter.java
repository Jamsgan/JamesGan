package datapager.tools.databaseconnector;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datapager.datapager.DataPagerException;
import datapager.tools.databaseconnector.DatabaseConnectorRegistry;
import datapager.util.DataPagerLogger;

/**
 * 数据库连接类适配器
 * @author James
 *
 */
public class DatabaseConnectorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(DefaultDatabaseConnector.class);
	public static DatabaseConnector Adapter(Connection connection) throws DataPagerException {
		DatabaseConnectorRegistry databaseConnectorRegistry;
		try {
			databaseConnectorRegistry = new DatabaseConnectorRegistry();
			logger.info("databaseConnectorRegistry  instance success ");
		} catch (DataPagerException e) {
			logger.error("databaseConnectorRegistry  instance fail",e);
			throw new DataPagerException("databaseConnectorRegistry instance fail");
		}
		return databaseConnectorRegistry.lookupDatabaseConnector(connection);
	}
}
