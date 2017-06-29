package datapager.datapager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datapager.data.DataPageDO;
import datapager.data.PagedListHolder;
import datapager.tools.databaseconnector.DatabaseConnector;
import datapager.tools.databaseconnector.DatabaseConnectorAdapter;

import datapager.util.DataPagerLogger;

import static java.util.Objects.requireNonNull;

/**
 * ���������
 * 
 * @author James
 *
 */
public class DataPager<T> implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(DataPager.class);
	private static final long serialVersionUID = 1092149745399170912L;
	private final DataSource dataSource;
	private final DPJdbcUtils dpjdbcUtils;

	/**
	 * ����һ������Դ�Ͳ�ѯ��
	 * 
	 * @param dataSource
	 * @param query
	 */
	public DataPager(DataSource dataSource) {
		requireNonNull(dataSource, "Need provide dataSource");
		this.dataSource = dataSource;
		logger.info("<<<<<< initialize  DataPager >>>>>>");
		this.dpjdbcUtils = DPJdbcUtils.getInstance(dataSource);
	}

	/**
	 * �����
	 * 
	 * @return
	 */
	public PagedListHolder<T> pager(DataPageDO dataPagedo) {
		try {
			Connection connection = dataSource.getConnection();
			DatabaseConnector adapter = DatabaseConnectorAdapter.Adapter(connection);
			IProduceSql<T> produceSqlImpl = adapter.getProduceSqlImpl();
			PagedListHolder<T> ph = produceSqlImpl.dbExecuteSql(dataPagedo);
			ph.setDataPager(this);
			return ph;
		} catch (DataPagerException | SQLException e) {
			logger.warn("can't get Connection from datasource", e);
		}
		return null;
	}

}