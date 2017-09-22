package cn.e3mall.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable{
	/*SELECT a.id, a.title, a.sell_point, a.price, a.image, b.name categoryName 
	FROM tb_item a LEFT JOIN tb_item_cat b ON a.cid=b.id WHERE a.status=1;*/
	private String id;
	private String title;
	private String sell_point;
	private String price;
	private String image;
	private String category_name;
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String[] getImages() {
		if (image != null) {
			String[] str = image.split(",");
			return str;
		}
		return null;
	}
	
	
}
