package datapager.datapager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import datapager.util.DataPagerLogger;

public class DPJdbcUtils {
	private static final Logger logger = LoggerFactory.getLogger(DPJdbcUtils.class);
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>(); // map
	private static DPJdbcUtils instance = null;
	private final JdbcTemplate jdbcTemplate;

	public static DPJdbcUtils getInstance(DataSource dataSource) {
		if (instance == null) {
			synchronized (DPJdbcUtils.class) {
				if (instance == null) {
					instance = new DPJdbcUtils(dataSource);
				}
			}
		}
		return instance;
	}

	public static DPJdbcUtils getInstance() throws DataPagerException {
		if (instance == null)
			throw new DataPagerException("need initialize DPJdbcUtils with datasource");
		return instance;
	}

	private DPJdbcUtils(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Close the given JDBC Connection and ignore any thrown exception. This is
	 * useful for typical finally blocks in manual JDBC code.
	 * 
	 * @param con
	 *            the JDBC Connection to close (may be {@code null})
	 */
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				logger.error("Could not close JDBC Connection", ex);
			} catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw
				// RuntimeException or Error.
				logger.error("Unexpected exception on closing JDBC Connection", ex);
			}
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}
