package com.kim.lostfound.activity;


import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.kim.lostfound.bean.Found;
import com.kim.lostfound.bean.Lost;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends BaseActivity implements OnClickListener{
	private EditText edit_title, edit_phone, edit_describe;
	private Button btn_back, btn_true;
	private TextView tv_add;
	
	String from = "";
	String title = "";
	String phone = "";
	String describe = "";
	
	private Lost editLost;
	private Found editFound;
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_add);
	}

	@Override
	public void initViews() {
		tv_add = (TextView) findViewById(R.id.tv_add);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_true = (Button) findViewById(R.id.btn_true);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_describe = (EditText) findViewById(R.id.edit_describe);
		edit_title = (EditText) findViewById(R.id.edit_title);
	}

	@Override
	public void initListeners() {
		btn_true.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		from = intent.getStringExtra("from");
		if("Lost".equals(from)) {
			tv_add.setText("���ʧ����Ϣ");
		} else if("found".equals(from)) {
			tv_add.setText("���������Ϣ");
		} else if("edit_lost".equals(from)) {
			tv_add.setText("�༭ʧ����Ϣ");
			editLost = getIntent().getParcelableExtra("lost");
			title = editLost.getTitle();
			describe = editLost.getDescribe();
			phone = editLost.getPhone();
			initViewData();
		} else if("edit_found".equals(from)){
			tv_add.setText("�༭������Ϣ");
			editFound = getIntent().getParcelableExtra("found");
			title = editFound.getTitle();
			describe = editFound.getDescribe();
			phone = editFound.getPhone();
			initViewData();
		}
	}

	private void initViewData() {
		edit_title.setText(title);
		edit_describe.setText(describe);
		edit_phone.setText(phone);
	}

	@Override
	public void onClick(View v) {
		if(btn_true == v) {
			if("Lost".equals(from)) {
				addInfo();
			} else if("edit_lost".equals(from)){
				editInfo();
			} else if("found".equals(from)) {
				addInfo();
			} else if("edit_found".equals(from)) {
				editInfo();
			}
		} else {
			finish();
		}
	}

	private void editInfo() {
		title = edit_title.getText().toString();
		phone = edit_phone.getText().toString();
		describe = edit_describe.getText().toString();
		if(TextUtils.isEmpty(title)) {
			ShowToast("���ⲻ��Ϊ��");
			return;
		}
		if(TextUtils.isEmpty(phone)) {
			ShowToast("�ֻ�����Ϊ��");
			return;
		}
		if(TextUtils.isEmpty(describe)) {
			ShowToast("��������Ϊ��");
			return;
		}
		
		if("edit_lost".equals(from)) {
			editLost();
		} else if("edit_found".equals(from)) {
			editFound();
		}
	}

	private void editLost() {
		editLost.setTitle(title);
		editLost.setDescribe(describe);
		editLost.setPhone(phone);
		editLost.update(this, editLost.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				ShowToast("�޸�ʧ����Ϣ�ɹ�");
				Intent intent = getIntent();
				intent.putExtra("lost", editLost);
				intent.putExtra("lost_op", "update");
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("�޸�ʧ����Ϣʧ�ܣ�" + arg1);
			}
		});
	}
	
	private void editFound() {
		editFound.setTitle(title);
		editFound.setDescribe(describe);
		editFound.setPhone(phone);
		Intent intent = getIntent();
		intent.putExtra("found_op", "update");
		intent.putExtra("found", editFound);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void addInfo() {
		title = edit_title.getText().toString();
		phone = edit_phone.getText().toString();
		describe = edit_describe.getText().toString();
		if(TextUtils.isEmpty(title)) {
			ShowToast("���ⲻ��Ϊ��");
			return;
		}
		if(TextUtils.isEmpty(phone)) {
			ShowToast("�ֻ�����Ϊ��");
			return;
		}
		if(TextUtils.isEmpty(describe)) {
			ShowToast("��������Ϊ��");
			return;
		}
		if("Lost".equals(from)) {
			addLost();
		} else if("found".equals(from)) {
			addFound();
		}
	}

	private void addFound() {
		final Found found = new Found();
		found.setTitle(title);
		found.setDescribe(describe);
		found.setPhone(phone);
		found.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				Intent intent = getIntent();
				intent.putExtra("found", found);
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("�������ʧ��");
			}
		});
	}

	private void addLost() {
		final Lost lost = new Lost();
		lost.setTitle(title);
		lost.setPhone(phone);
		lost.setDescribe(describe);
		lost.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				ShowToast("���ʧ��ɹ���=" + lost.getCreatedAt());
				Intent intent = getIntent();
				intent.putExtra("lost", lost);
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("���ʧ��ʧ��");
			}
		});
	}

}
