package datapager.datapager;

import java.util.List;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import datapager.data.DataPageDO;
import datapager.data.PagedListHolder;
import datapager.tools.databaseconnector.DefaultDatabaseConnector;
import datapager.util.DataPagerLogger;

/**
 * ִ��sql�ĳ�����
 * 
 * @author James
 *
 */
public abstract class AbstractProduceSql implements IProduceSql {
	private static final long serialVersionUID = -8765681133034150312L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractProduceSql.class);
	/*
	 * public AbstractProduceSql(){
	 * 
	 * }
	 */
	@Override
	public PagedListHolder dbExecuteSql(DataPageDO dataPageDO) throws DataPagerException {
		String sql = dataPageDO.getExecuteSql();
		try {
			logger.info("execute sql:\n {}", sql);
			JdbcTemplate jdbcTemplate = DPJdbcUtils.getInstance().getJdbcTemplate();
			//�õ���ҳ����
			List queryForList = jdbcTemplate.query(this.sqlWithPaging(dataPageDO),
					new BeanPropertyRowMapper(dataPageDO.getDataClazz()));
			PagedListHolder ph = new PagedListHolder(queryForList);
			//�õ���������
			int totalCount = jdbcTemplate.queryForObject(this.getDataCountSql(dataPageDO.getExecuteSql()),
					Integer.class);
			ph.setTotalCount(totalCount);
			ph.setTotalCount(totalCount);
			ph.setCurrentPage(dataPageDO.getCurrentPage());
			ph.setPageSize(dataPageDO.getPageSize());
			ph.setExecuteSql(sql);
			ph.setResultObjectClazz(dataPageDO.getDataClazz());
			ph.setDefaultImpl(false);
			return ph;
		} catch (DataPagerException e) {
			logger.error("execute sql fail",e);
			throw new DataPagerException("execute sql fail");
		}
	}
}