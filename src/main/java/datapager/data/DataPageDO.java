package datapager.data;

import java.io.Serializable;

/**
 * 分页信息查询类
 * 
 * @author James
 *
 */
public class DataPageDO implements Serializable {
	private static final long serialVersionUID = 3795021268827693090L;
	/** 执行的sql语句 */
	private String ExecuteSql;
	/** 当前页 */
	private Integer CurrentPage = PagedListHolder.DEFAULT_CURRENT_PAGE;
	/** 每页大小 */
	private Integer PageSize = PagedListHolder.DEFAULT_PAGE_SIZE;
	/** 数据对象类型 */
	private Class dataClazz = Object.class;

	public DataPageDO() {
		super();
	}

	public DataPageDO(String executeSql, Integer currentPage, Integer pageSize, Class dataClazz) {
		super();
		this.ExecuteSql = executeSql;
		this.CurrentPage = currentPage;
		this.PageSize = pageSize;
		this.dataClazz = dataClazz;
	}

	public DataPageDO(String executeSql, Integer currentPage, Class dataClazz) {
		super();
		this.ExecuteSql = executeSql;
		this.CurrentPage = currentPage;
		this.dataClazz = dataClazz;
	}

	public DataPageDO(String executeSql, Class dataClazz) {
		super();
		this.ExecuteSql = executeSql;
		this.dataClazz = dataClazz;
	}

	public String getExecuteSql() {
		return ExecuteSql;
	}

	public void setExecuteSql(String executeSql) {
		ExecuteSql = executeSql;
	}

	public Integer getCurrentPage() {
		return CurrentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		CurrentPage = currentPage;
	}

	public Integer getPageSize() {
		return PageSize;
	}

	public void setPageSize(Integer pageSize) {
		PageSize = pageSize;
	}

	@Override
	public String toString() {
		return "DataPageInfo [ExecuteSql=" + ExecuteSql + ", CurrentPage=" + CurrentPage + ", PageSize=" + PageSize
				+ "]";
	}

	public Class getDataClazz() {
		return dataClazz;
	}

	public void setDataClazz(Class dataClazz) {
		this.dataClazz = dataClazz;
	}
}
