package datapager.tools.databaseconnector;

import java.util.List;
import java.util.logging.Level;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import datapager.data.DataPageDO;
import datapager.data.PagedListHolder;
import datapager.datapager.AbstractProduceSql;
import datapager.datapager.DPJdbcUtils;
import datapager.datapager.DataPagerException;
import datapager.util.DataPagerLogger;

/**
 * jdbc默认实现分页机制
 * 
 * @author James
 *
 */
public class DafaultProduceSqlImpl extends AbstractProduceSql {
	private static final long serialVersionUID = -2508376795325535706L;
	private static final DataPagerLogger LOGGER = DataPagerLogger.getLogger(DafaultProduceSqlImpl.class.getName());
	private String executesql;

	@Override
	public String sqlWithPaging(DataPageDO dataPageDO) {
		executesql = dataPageDO.getExecuteSql();
		LOGGER.log(Level.INFO, String.format("execute sql \n %s", executesql));
		return executesql;
	}

	@Override
	public PagedListHolder dbExecuteSql(DataPageDO dataPageDO) throws DataPagerException {
		PagedListHolder ph = null;
		String sql = dataPageDO.getExecuteSql();
		try {
			LOGGER.log(Level.INFO, String.format("execute sql:\n %s", sql));
			JdbcTemplate jdbcTemplate = DPJdbcUtils.getInstance().getJdbcTemplate();
			List queryForList = jdbcTemplate.query(this.sqlWithPaging(dataPageDO),
					new BeanPropertyRowMapper(dataPageDO.getDataClazz()));
			ph = new PagedListHolder(queryForList);
			int totalCount = 0;
			if (queryForList == null)
				totalCount = queryForList.size();
			ph.setTotalCount(totalCount);
			ph.setTotalCount(totalCount);
			ph.setCurrentPage(dataPageDO.getCurrentPage());
			ph.setPageSize(dataPageDO.getPageSize());
			ph.setDefaultImpl(true);
			return ph;
		} catch (DataPagerException e) {
			LOGGER.log(Level.INFO, "execute sql fail");
			e.printStackTrace();
			throw new DataPagerException("execute sql fail");
		}
	}

	@Override
	public String getDataCountSql(String sql) {
		return null;
	}
}
