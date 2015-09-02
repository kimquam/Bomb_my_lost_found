package com.kim.lostfound.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.bmob.v3.BmobObject;

/** 创建失物对象
  * @ClassName: Lost
  * @Description: TODO
  * @author smile
  * @date 2014-5-21 上午11:27:03
  */
public class Lost extends BmobObject implements Parcelable {

	private String title;//标题
	private String describe;//描述
	private String phone;//联系手机
	
	public Lost() {}
	public Lost(Parcel in) {
		title = in.readString();
		describe = in.readString();
		phone = in.readString();
		setCreatedAt(in.readString());
		setObjectId(in.readString());
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(describe);
		dest.writeString(phone);
		dest.writeString(getCreatedAt());
		dest.writeString(getObjectId());
	}
	
	public static final Parcelable.Creator<Lost> CREATOR = new Parcelable.Creator<Lost>() {
		public Lost createFromParcel(Parcel in) {
			return new Lost(in);
		}

		public Lost[] newArray(int size) {
			return new Lost[size];
		}
	};

}
