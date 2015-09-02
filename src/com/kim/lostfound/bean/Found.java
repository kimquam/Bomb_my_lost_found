package com.kim.lostfound.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.bmob.v3.BmobObject;

/** 招领
  * @ClassName: Found
  * @Description: TODO
  * @author smile
  * @date 2014-5-21 下午2:16:08
  */
public class Found extends BmobObject implements Parcelable {

	private String title;//标题
	private String describe;//描述
	private String phone;//联系手机
	
	public Found() {
		super();
	}
	public Found(Parcel in) {
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
		// TODO Auto-generated method stub
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
	
	public static final Parcelable.Creator<Found> CREATOR = new Parcelable.Creator<Found>() {
		public Found createFromParcel(Parcel in) {
			return new Found(in);
		}

		public Found[] newArray(int size) {
			return new Found[size];
		}
	};


}
