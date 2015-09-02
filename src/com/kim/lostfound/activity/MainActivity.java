package com.kim.lostfound.activity;



import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.kim.lostfound.base.EditPopupWindow;
import com.kim.lostfound.bean.Found;
import com.kim.lostfound.bean.Lost;
import com.kim.lostfound.config.Constants;
import com.kim.lostfound.i.IPopupItemClick;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;





public class MainActivity extends BaseActivity implements OnClickListener,
		OnItemLongClickListener, IPopupItemClick {
	private RelativeLayout layout_action;//
	private LinearLayout layout_all;
	private TextView tv_lost;
	private ListView listview;
	private Button btn_add;
	private RelativeLayout progress;
	private LinearLayout layout_no;
	private TextView tv_no;
	
	private List<Lost> losts;
	private LostAdapter lostAdapter;
	
	private List<Found> founds;
	private FoundAdapter foundAdapter;
	
	EditPopupWindow mPopupWindow;
	int position;
	
	private Button layout_found;
	private Button layout_lost;
	private PopupWindow morePop;
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initViews() {
		layout_action = (RelativeLayout) findViewById(R.id.layout_action);
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		// 默认是失物界面
		tv_lost = (TextView) findViewById(R.id.tv_lost);
		tv_lost.setTag("Lost");
		listview = (ListView) findViewById(R.id.list_lost);
		btn_add = (Button) findViewById(R.id.btn_add);
		progress = (RelativeLayout) findViewById(R.id.progress);
		layout_no = (LinearLayout) findViewById(R.id.layout_no);
		tv_no = (TextView) findViewById(R.id.tv_no);
		// 初始化长按弹窗
		initEditPop();
	}

	@Override
	public void initListeners() {
		listview.setOnItemLongClickListener(this);
		btn_add.setOnClickListener(this);
		layout_all.setOnClickListener(this);
	}

	@Override
	public void initData() {
		findLostAll();
	}

	private void findLostAll() {
		beforeGetDataView();
		BmobQuery<Lost> query = new BmobQuery<Lost>();
		query.order("-createdAt");
		query.findObjects(this, new FindListener<Lost>() {
			
			@Override
			public void onSuccess(List<Lost> losts) {
				if(losts == null || losts.size() <= 0) {
					showErrorView(0);
					lostAdapter.notifyDataSetChanged();
					return;
				}
				MainActivity.this.losts = losts;
				lostAdapter = new LostAdapter();
				listview.setAdapter(lostAdapter);
				showView();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				showErrorView(0);
				ShowToast(arg1);
			}
		});
	}
	
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
	}
	
	private void beforeGetDataView() {
		layout_no.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 请求出错或者无数据时候显示的界面 showErrorView
	 * 
	 * @return void
	 * @throws
	 */
	private void showErrorView(int tag) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
		if (tag == 0) {
			tv_no.setText(getResources().getText(R.string.list_no_data_lost));
		} else {
			tv_no.setText(getResources().getText(R.string.list_no_data_found));
		}
	}
	
	@Override
	public void onClick(View v) {
		if(btn_add == v) {
			Intent intent = new Intent(this, AddActivity.class);
			intent.putExtra("from", tv_lost.getTag().toString());
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		} else if(layout_all == v) {
			showListPop();
		} else if(layout_lost == v) {
			tv_lost.setText("失物");
			tv_lost.setTag("Lost");
			morePop.dismiss();
			findLostAll();
		} else if(layout_found == v) {
			tv_lost.setText("招领");
			tv_lost.setTag("found");
			morePop.dismiss();
			findFoundAll();
		}
	}
	
	private void findFoundAll() {
		beforeGetDataView();
		BmobQuery<Found> query = new BmobQuery<Found>();
		query.order("-createdAt");
		query.findObjects(this, new FindListener<Found>() {
			
			@Override
			public void onSuccess(List<Found> founds) {
				if(founds == null || founds.size() <= 0) {
					showErrorView(1);
					foundAdapter.notifyDataSetChanged();
					return;
				}
				MainActivity.this.founds = founds;
				foundAdapter = new FoundAdapter();
				listview.setAdapter(foundAdapter);
				showView();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				showErrorView(1);
				ShowToast(arg1);
			}
		});
	}

	// 在标题栏下拉事件的弹窗
	private void showListPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_lost, null);
		layout_found = (Button) view.findViewById(R.id.layout_found);
		layout_lost = (Button) view.findViewById(R.id.layout_lost);
		layout_found.setOnClickListener(this);
		layout_lost.setOnClickListener(this);
		morePop = new PopupWindow(view, mScreenWidth, 6);
		
		// 弹窗
		morePop.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					morePop.dismiss();
					return true;
				}
				return false;
			}
		});
		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setOutsideTouchable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从顶部弹下
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(layout_action, 0, -dip2px(this, 2.0F));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constants.REQUESTCODE_ADD && resultCode == RESULT_OK) {
			if("Lost".equals(tv_lost.getTag())) {
				Lost lost = data.getParcelableExtra("lost");
				String lost_op = data.getStringExtra("lost_op");
				if("update".equals(lost_op)) {
					Lost oldLost = (Lost) lostAdapter.getItem(position);
					oldLost.setTitle(lost.getTitle());
					oldLost.setDescribe(lost.getDescribe());
					oldLost.setPhone(lost.getPhone());
				} else {
					losts.add(0, lost);
				}
				lostAdapter.notifyDataSetChanged();
			} else if("found".equals(tv_lost.getTag())) {
				Found found = data.getParcelableExtra("found");
				String found_op = data.getStringExtra("found_op");
				if("update".equals(found_op)) {
					Found old = founds.get(position);
					old.setTitle(found.getTitle());
					old.setDescribe(found.getPhone());
					old.setDescribe(found.getDescribe());
				} else {
					founds.add(0, found);
				}
				foundAdapter.notifyDataSetChanged();
			}
		}
	}
	
	class LostAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return losts.size();
		}

		@Override
		public Object getItem(int position) {
			return losts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_list, null);
				vh = new ViewHolder();
				vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				vh.tv_describe = (TextView) convertView.findViewById(R.id.tv_describe);
				vh.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				vh.tv_createAt = (TextView) convertView.findViewById(R.id.tv_time);
				convertView.setTag(vh);
			}
			vh = (ViewHolder) convertView.getTag();
			Lost lost = losts.get(position);
			vh.tv_title.setText(lost.getTitle());
			vh.tv_describe.setText(lost.getDescribe());
			vh.tv_createAt.setText(lost.getCreatedAt());
			vh.tv_phone.setText(lost.getPhone());
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView tv_title;
		TextView tv_describe;
		TextView tv_phone;
		TextView tv_createAt;
	}
	
	class FoundAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return founds.size();
		}

		@Override
		public Object getItem(int position) {
			return founds.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_list, null);
				vh = new ViewHolder();
				vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				vh.tv_describe = (TextView) convertView.findViewById(R.id.tv_describe);
				vh.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				vh.tv_createAt = (TextView) convertView.findViewById(R.id.tv_time);
				convertView.setTag(vh);
			}
			vh = (ViewHolder) convertView.getTag();
			Found found = founds.get(position);
			vh.tv_title.setText(found.getTitle());
			vh.tv_describe.setText(found.getDescribe());
			vh.tv_createAt.setText(found.getCreatedAt());
			vh.tv_phone.setText(found.getPhone());
			return convertView;
		}
		
	}
	
	private void initEditPop() {
		mPopupWindow = new EditPopupWindow(this, 200, 48);
		mPopupWindow.setOnPopupItemClickListner(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		this.position = position;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 
				location[0], getStateBar() + location[1]);
		return false;
	}
	
	@Override
	public void onEdit(View v) {
		if("Lost".equals(tv_lost.getTag().toString()));
		Lost lost = (Lost) lostAdapter.getItem(position);
		Intent intent = new Intent(this, AddActivity.class);
		intent.putExtra("from", "edit_lost");
		intent.putExtra("lost", lost);
		startActivityForResult(intent, Constants.REQUESTCODE_ADD);
	}

	@Override
	public void onDelete(View v) {
		if("Lost".equals(tv_lost.getTag().toString())) {
			Lost lost = (Lost) lostAdapter.getItem(position);
			lost.delete(this, lost.getObjectId(), new DeleteListener() {
				
				@Override
				public void onSuccess() {
					ShowToast("失物信息已删除");
					losts.remove(position);
					lostAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					ShowToast("删除失败：" + arg1);
				}
			});
		}
	}
	
	public void callPhone(View v){
		Intent intent = new Intent();
		//打开 拨号面板，并输入号码
		intent.setAction(Intent.ACTION_DIAL);
		String phoneNumber = ((TextView)v).getText().toString();
		intent.setData(Uri.parse("tel:"+phoneNumber));
		startActivity(intent);
	}
}
