package com.lives.depend.account;

import java.io.Serializable;

/**
 * 保存酷云账号相关信息
 * 
 * @author putao_lhq
 * @version 2014年10月18日
 */
public class BaseAccountInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String username;
	private String uid;
	private String nickname;
	private String avatar_url;
	private String avatar_hd_url;
	private String mobile;
	
	public BaseAccountInfo() {
		
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public void setAvatar_hd_url(String avatar_hd_url) {
		this.avatar_hd_url = avatar_hd_url;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}
	
	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getUid() {
		return uid;
	}

	public String getNickname() {
		return nickname;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public String getAvatar_hd_url() {
		return avatar_hd_url;
	}

	
	/*public BaseAccountInfo(Bundle bundle) {
		name = bundle.getString(Params.NAME);
		username = bundle.getString(Params.USERNAME);
		uid = bundle.getString(Params.UID);
		nickname = bundle.getString(Params.NICKNAME);
		avatar_url = bundle.getString(Params.AVATAR_URL);
		avatar_hd_url = bundle.getString(Params.AVATAR_HD_URL);
		mobile = bundle.getString("mobile");
	}*/
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name: ");
		sb.append(name);
		
		sb.append(",username: ");
		sb.append(username);
		
		sb.append(",uid: ");
		sb.append(uid);
		
		sb.append(",nickname: ");
		sb.append(nickname);
		
		sb.append(",avatar_url: ");
		sb.append(avatar_url);
		
		sb.append(",avatar_hd_url: ");
		sb.append(avatar_hd_url);
		
		sb.append(",mobile: ");
		sb.append(mobile);
		return sb.toString();
	}
}
