package datapager.datapager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import datapager.data.DataPageDO;
import datapager.data.PagedListHolder;

/**
 * 生成sql接口
 * 
 * @author James
 *
 */
public interface IProduceSql<T> extends Serializable {
	/**
	 * 生成分页sql语句
	 * 
	 * @param dataPageInfo
	 * @return
	 */
	String sqlWithPaging(DataPageDO dataPageDO);

	/**
	 * 执行sql 语句
	 * 
	 * @param dataPageDO
	 * @return
	 * @throws DataPagerException
	 */
	PagedListHolder<T> dbExecuteSql(DataPageDO dataPageDO) throws DataPagerException;

	/**
	 * 生成分页sql语句
	 * 
	 * @param sql
	 * @return
	 */
	String getDataCountSql(String sql);
}
