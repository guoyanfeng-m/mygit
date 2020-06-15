package vo;

import java.util.Collections;
import java.util.List;


import com.avaje.ebean.Page;

/**
 * 普通Java分页支持Bean
 * 
 */
public class PageEnt<T> {
	/** 每页记录数 */
	private int pageSize = 10;
	/** 当前页码 */
	private int pageNo = 1;
	/** 总记录数 */
	private int totalRecords = 0;
	/** 总页数 */
	private int totalPages = 1;
	/** 是否有上一页 */
	private boolean havePrePage = false;
	/** 是否有下一页 */
	private boolean haveNextPage = false;
	/** 具体的分页数据 */
	private List<T> listPages=Collections.emptyList();
	
	public PageEnt(Page<T> pages,int pageNo,int pageSize) {
		 if(pages!=null){
			 this.pageSize = pageSize;
			 this.pageNo = pageNo;
			 this.listPages = pages.getList();
			 this.totalRecords = pages.getTotalRowCount();
			 setTotalRecords(totalRecords);
		 }
	}
	
	public PageEnt(List<T> list,int pageNo,int pageSize,int totalRecords) {
		 if(list!=null){ 
			 this.pageSize = pageSize;
			 this.pageNo = pageNo;
			 this.listPages = list;
			 this.totalRecords = totalRecords;
			 setTotalRecords(totalRecords);
		 }
	}
	
	public PageEnt(Page<T> pages) {
		this(pages,pages.getPageIndex()+1);
	}
	
	public PageEnt(Page<T> pages,int pageNo) {
		 if(pages!=null){
			 this.pageNo = pageNo;
			 this.listPages = pages.getList();
			 this.totalRecords = pages.getTotalRowCount();
			 setTotalRecords(totalRecords);
		 }
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1)
			pageSize = 1;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo < 1)
			pageNo = 1;
		this.pageNo = pageNo;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		if (totalRecords < 0)
			totalRecords = 0;

		if (this.pageSize < 1)
			this.pageSize = 1;

		this.totalRecords = totalRecords;
		double allCount = this.totalRecords;
		double discuss = Math.ceil(allCount / this.pageSize);

		// 计算总页数
		this.totalPages = (int) discuss;
		if (this.totalPages == 0) {
			this.pageNo = 1;
		}
		// 计算是否有上一页
		this.havePrePage = this.pageNo > 1;
		// 计算是否有下一页
		this.haveNextPage = this.pageNo < this.totalPages;
	}

 

	public int getTotalPages() {
		return totalPages;
	}

	public boolean getHavePrePage() {
		return havePrePage;
	}

	public boolean getHaveNextPage() {
		return haveNextPage;
	}

	public List<T> getListPages() {
		return listPages;
	}

	public void setListPages(List<T> listPages) {
		this.listPages = listPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setHavePrePage(boolean havePrePage) {
		this.havePrePage = havePrePage;
	}

	public void setHaveNextPage(boolean haveNextPage) {
		this.haveNextPage = haveNextPage;
	}

 

	 
 

}
