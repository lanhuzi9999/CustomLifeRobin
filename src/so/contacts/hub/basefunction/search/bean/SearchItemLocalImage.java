package so.contacts.hub.basefunction.search.bean;

import java.io.Serializable;

public class SearchItemLocalImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String keyword;
	
	private String imgId;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	@Override
	public String toString() {
		return "{keyword:" + keyword + ",imgId:" + imgId + "}";
	}

}
