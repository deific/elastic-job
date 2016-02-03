package com.myzh.dpc.console.core.mybatis.pagination;

import java.util.ArrayList;
import java.util.List;

public class PageImpl<T> extends ArrayList<T> implements Page<T> {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PAGE_SIZE = 20;
	protected int page = 1; // 当前页, 默认为第1页
	protected int pageSize = DEFAULT_PAGE_SIZE; // 每页记录数
	protected long total = -1; // 总记录数, 默认为-1, 表示需要查询
	protected int totalPage = -1; // 总页数, 默认为-1, 表示需要计算
	protected int next;
	protected List<T> results; // 当前页记录List形式
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}

}
