package so.contacts.hub.services.baseservices.bean;

import java.io.Serializable;

import so.contacts.hub.basefunction.search.bean.SearchItemImage;

public class YellowParams implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String name;
	private String url;
	private String words;
	private String category;
	private int provider;
	private double latitude;
	private double longitude;

	private int webtype;
	private long category_id;
	private String category_name;
	private int remindCode;

	private SearchItemImage itemImageStr;

	private boolean showDianping = true;
	private boolean showSougou = true;
	private boolean showDianhua = true;

	/**
	 * 标志进入应用服务的入口 1: 首页进入 2：搜索中进入
	 */
	private int entry_type = ENTRY_TYPE_HOME_PAGE;
	
	public static final int ENTRY_TYPE_HOME_PAGE = 1; 	//首页进入

	public static final int ENTRY_TYPE_SEARCH_PAGE = 2; //搜索中进入
	
	public static final int ENTRY_TYPE_NOTIFICATION_PAGE = 3; //从通知进入

	public YellowParams() {
		entry_type = ENTRY_TYPE_HOME_PAGE;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public boolean isShowDianping() {
		return showDianping;
	}

	public void setShowDianping(boolean showDianping) {
		this.showDianping = showDianping;
	}

	public boolean isShowSougou() {
		return showSougou;
	}

	public void setShowSougou(boolean showSougou) {
		this.showSougou = showSougou;
	}

	public boolean isShowDianhua() {
		return showDianhua;
	}

	public void setShowDianhua(boolean showDianhua) {
		this.showDianhua = showDianhua;
	}

	public int getProvider() {
		return provider;
	}

	public void setProvider(int provider) {
		this.provider = provider;
	}

	public SearchItemImage getItemImageStr() {
		return itemImageStr;
	}

	public void setItemImageStr(SearchItemImage itemImageStr) {
		this.itemImageStr = itemImageStr;
	}

	public int getWebtype() {
		return webtype;
	}

	public void setWebtype(int webtype) {
		this.webtype = webtype;
	}

	public int getRemindCode() {
		return remindCode;
	}

	public void setRemindCode(int remindCode) {
		this.remindCode = remindCode;
	}

	public int getEntry_type() {
		return entry_type;
	}

	public void setEntry_type(int entry_type) {
		this.entry_type = entry_type;
	}

}
