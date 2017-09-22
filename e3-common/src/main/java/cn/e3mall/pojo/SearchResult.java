package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Serializable{
	private int page;
	private int totalPages;
	private long recourdCount;
	private List<SearchItem> items = new ArrayList<SearchItem>();
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public List<SearchItem> getItems() {
		return items;
	}
	public void setItems(List<SearchItem> items) {
		this.items = items;
	}
	

	
}
