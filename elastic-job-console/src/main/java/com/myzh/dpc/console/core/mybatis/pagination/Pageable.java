package com.myzh.dpc.console.core.mybatis.pagination;

public class Pageable {

	public static final int DEFAULT_PAGE_SIZE = 20;

	protected int page = 1; // 当前页, 默认为第1页
	protected int pageSize = DEFAULT_PAGE_SIZE; // 每页记录数
	protected long total = -1; // 总记录数, 默认为-1, 表示需要查询
	protected int totalPage = -1; // 总页数, 默认为-1, 表示需要计算
	protected int next;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		computeTotalPage();
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		computeTotalPage();
	}


	public int getTotalPage() {
		return totalPage;
	}

	protected void computeTotalPage() {
		if (getPageSize() > 0 && getTotal() > -1) {
			this.totalPage = (int) (getTotal() % getPageSize() == 0 ? getTotal() / getPageSize() : getTotal() / getPageSize() + 1);
		}
	}
}
