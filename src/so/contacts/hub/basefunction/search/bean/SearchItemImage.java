package so.contacts.hub.basefunction.search.bean;

import java.io.Serializable;
import java.util.List;


public class SearchItemImage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isShowNetImg;
	
	private String defaultImg;
	
	private List<SearchItemLocalImage> localImages;

	public boolean isShowNetImg() {
		return isShowNetImg;
	}

	public void setShowNetImg(boolean isShowNetImg) {
		this.isShowNetImg = isShowNetImg;
	}

	public String getDefaultImg() {
		return defaultImg;
	}

	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}

	public List<SearchItemLocalImage> getLocalImages() {
		return localImages;
	}

	public void setLocalImages(List<SearchItemLocalImage> localImages) {
		this.localImages = localImages;
	}

	@Override
	public String toString() {
		return "{isShowNetImg:"+ isShowNetImg + ",defaultImg:"+defaultImg +",localImages:" + localImages;
	}

	
}
