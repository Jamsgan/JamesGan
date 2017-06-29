package datapager.data;

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datapager.datapager.DataPager;

import static java.util.Objects.requireNonNull;

/**
 * PagedListHolder is a simple state holder for handling lists of objects,
 * separating them into pages. Page numbering starts with 0.
 *
 * <p>
 * This is mainly targetted at usage in web UIs. Typically, an instance will be
 * instantiated with a list of beans, put into the session, and exported as
 * model. The properties can all be set/get programmatically, but the most
 * common way will be data binding, i.e. populating the bean from request
 * parameters. The getters will mainly be used by the view.
 *
 * <p>
 * Supports sorting the underlying list via a {@link SortDefinition}
 * implementation, available as property "sort". By default, a
 * {@link MutableSortDefinition} instance will be used, toggling the ascending
 * value on setting the same property again.
 *
 * <p>
 * The data binding names have to be called "pageSize" and "sort.ascending", as
 * expected by BeanWrapper. Note that the names and the nesting syntax match the
 * respective JSTL EL expressions, like "myModelAttr.pageSize" and
 * "myModelAttr.sort.ascending".
 *
 * @author Juergen Hoeller
 * @since 19.05.2003
 * @see #getPageList()
 * @see org.springframework.beans.support.MutableSortDefinition
 */
@SuppressWarnings("serial")
public class PagedListHolder<E> implements Serializable {

	/**
	 * 默认页面大小
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 默认当前页
	 */
	public static final int DEFAULT_CURRENT_PAGE = 1;
	/**
	 * 默认页码条大小
	 */
	public static final int DEFAULT_MAX_LINKED_PAGES = 10;
	/**
	 * 分页数据
	 */
	private List<E> source;
	/**
	 * 数据更新时间
	 */
	private Date refreshDate;
	/**
	 * 页面大小
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	/**
	 * 当前页数
	 */
	private int currentPage = DEFAULT_CURRENT_PAGE;
	/**
	 * 新的数据集合
	 */
	private boolean newPageSet;
	/**
	 * 最大页码条
	 */
	private int maxLinkedPages = DEFAULT_MAX_LINKED_PAGES;

	/**
	 * 执行sql语句
	 */
	private String executeSql;
	/**
	 * 返回的数据对象类型
	 */
	private Class resultObjectClazz;

	/**
	 * 分页操作
	 */
	private DataPager dataPager;
	/**
	 * 总数据大小
	 */
	private int totalCount;
	/**
	 * 是否是默认实现
	 */
	public boolean isDefaultImpl = false;

	/**
	 * Return the source list for this holder.
	 */
	public List<E> getSource() {
		return this.source;
	}

	/**
	 * Return the last time the list has been fetched from the source provider.
	 */
	public Date getRefreshDate() {
		return this.refreshDate;
	}

	/**
	 * Set the current page size. Resets the current page number if changed.
	 * <p>
	 * Default value is 10.
	 */
	public void setPageSize(int pageSize) {
		if (pageSize != this.pageSize) {
			this.pageSize = pageSize;
			if (!this.newPageSet) {
				this.currentPage = 1;
			}
		}
	}

	/**
	 * Return the current page size.
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * Set the current page number. Page numbering starts with 0.
	 */
	public void setPage(int page) {

		this.currentPage = page;
		this.newPageSet = true;
	}

	/**
	 * Return the current page number. Page numbering starts with 0.
	 */
	/*
	 * public int getPage() { this.newPageSet = false; if (this.currentPage >=
	 * getPageCount()) { this.currentPage = getPageCount() - 1; } return
	 * this.currentPage; }
	 */

	/**
	 * Set the maximum number of page links to a few pages around the current
	 * one.
	 */
	public void setMaxLinkedPages(int maxLinkedPages) {
		this.maxLinkedPages = maxLinkedPages;
	}

	/**
	 * Return the maximum number of page links to a few pages around the current
	 * one.
	 */
	public int getMaxLinkedPages() {
		return this.maxLinkedPages;
	}

	/**
	 * Return the number of pages for the current source list.
	 * 
	 */
	public int getPageCount() {
		if (isDefaultImpl()) {
			// 获得集合大小
			float nrOfPages = (float) getNrOfElements() / getPageSize();
			return (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages);
		}
		return (int) Math.ceil(getTotalCount() / getPageSize());
	}

	/**
	 * Return if the current page is the first one.
	 */
	public boolean isFirstPage() {
		return getCurrentPage() == 1;
	}

	/**
	 * Return if the current page is the last one.
	 */
	public boolean isLastPage() {
		return getCurrentPage() == getPageCount();
	}

	/**
	 * Switch to previous page. Will stay on first page if already on first
	 * page.
	 */
	public void previousPage() {
		if (!isFirstPage()) {
			this.currentPage--;
		}
	}

	/**
	 * Switch to next page. Will stay on last page if already on last page.
	 */
	public void nextPage() {
		if (!isLastPage()) {
			this.currentPage++;
		}
	}

	/**
	 * Return the total number of elements in the source list.
	 */
	public int getNrOfElements() {
		return getSource().size();
	}

	/**
	 * Return the element index of the first element on the current page.
	 * Element numbering starts with 0.
	 */
	public int getFirstElementOnPage() {
		return (getPageSize() * (getCurrentPage() - 1));
	}

	/**
	 * Return the element index of the last element on the current page. Element
	 * numbering starts with 0.
	 */
	public int getLastElementOnPage() {
		int endIndex = getPageSize() * getCurrentPage();
		int size = getNrOfElements();
		return (endIndex > size ? size : endIndex) - 1;
	}

	/**
	 * 返回一个子数据集合
	 */
	public List<E> getPageList() {
		if (isDefaultImpl)
			return getSource().subList(getFirstElementOnPage(), getLastElementOnPage() + 1);
		requireNonNull(dataPager, "please Initialization DataPager first");
		requireNonNull(executeSql, "please Initialization DataPager first");
		requireNonNull(resultObjectClazz, "please Initialization DataPager first");
		return dataPager.pager(new DataPageDO(executeSql, getCurrentPage(), getPageSize(), resultObjectClazz))
				.getSource();
	}

	/**
	 * Return the first page to which create a link around the current page.
	 */
	public int getFirstLinkedPage() {
		return Math.max(0, getCurrentPage() - (getMaxLinkedPages() / 2));
	}

	/**
	 * Return the last page to which create a link around the current page.
	 */
	public int getLastLinkedPage() {
		return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
	}

	public PagedListHolder(List<E> source) {
		setSource(source);
	}

	/**
	 * Set the source list for this holder.
	 */
	public void setSource(List<E> source) {
		requireNonNull(source, "Source List must not be null");
		this.source = source;
		this.refreshDate = new Date();
	}

	public PagedListHolder() {
		this(new ArrayList<E>(0));
	}

	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总页数
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设置当前页
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			this.currentPage = DEFAULT_CURRENT_PAGE;
			return;
		}
		if (currentPage > getPageCount()) {
			this.currentPage = getPageCount();
			return;
		}
		this.currentPage = currentPage;
	}

	public boolean isDefaultImpl() {
		return isDefaultImpl;
	}

	public void setDefaultImpl(boolean isDefaultImpl) {
		this.isDefaultImpl = isDefaultImpl;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}

	public Class getResultObjectClazz() {
		return resultObjectClazz;
	}

	public void setResultObjectClazz(Class resultObjectClazz) {
		this.resultObjectClazz = resultObjectClazz;
	}

	public DataPager getDataPager() {
		return dataPager;
	}

	public void setDataPager(DataPager dataPager) {
		this.dataPager = dataPager;
	}

}
