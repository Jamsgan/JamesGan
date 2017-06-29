package datapager.datapager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import datapager.data.DataPageDO;
import datapager.data.PagedListHolder;

/**
 * ����sql�ӿ�
 * 
 * @author James
 *
 */
public interface IProduceSql<T> extends Serializable {
	/**
	 * ���ɷ�ҳsql���
	 * 
	 * @param dataPageInfo
	 * @return
	 */
	String sqlWithPaging(DataPageDO dataPageDO);

	/**
	 * ִ��sql ���
	 * 
	 * @param dataPageDO
	 * @return
	 * @throws DataPagerException
	 */
	PagedListHolder<T> dbExecuteSql(DataPageDO dataPageDO) throws DataPagerException;

	/**
	 * ���ɷ�ҳsql���
	 * 
	 * @param sql
	 * @return
	 */
	String getDataCountSql(String sql);
}