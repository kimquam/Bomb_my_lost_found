package com.kim.lostfound.activity;


import android.content.Intent;
import android.os.Handler;




/**
 * »¶Ó­Ò³Ãæ
 * @author 180130
 *
 */
public class SplashActivity extends BaseActivity {
	private final static int GO_HOME = 0x100;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
				case GO_HOME:
					goHome();
					break;
				default:
					break;
			}
		};
	};
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_splash);
	}

	protected void goHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		mHandler.sendEmptyMessageDelayed(GO_HOME, 3000);
	}

}
