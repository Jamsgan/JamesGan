package datapager.tools.databaseconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认数据库连接类
 * 
 * @author James
 *
 */
public class DefaultDatabaseConnector extends DatabaseConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultDatabaseConnector.class);
	public DefaultDatabaseConnector() {
		super(new DatabaseServerType("default", "Default"), "/help/Connections.default.txt",
				"/datapager-dafault.config.properties", new DafaultProduceSqlImpl(), ".*");
		logger.info("use Default DatabaseConnector");
	}

	private static final long serialVersionUID = -3798526189627416084L;
}
