package com.example.timer_test3;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TimerDemoActivity extends ActionBarActivity {

	private Button btn_start;
	private Button btn_pause;
	private TextView textview;
	private EditText edittext;

	private Timer timer = null;
	private TimerTask timertask = null;
	private Handler handler = null;

	private boolean isStop = true;
	private boolean isPause = false;
	private static int count = 0;
	private static final int UPDATE_TEXTVIEW = 0;
	private float rate;

	private Vibrator vibrator;
	private SoundPool sound;
	private int soundId;
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer_demo);

		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundId = sound.load(getApplicationContext(), R.raw.card, 1);
		sound.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				flag = true; // 表示加载完成
			}
		});

		btn_start = (Button) findViewById(R.id.button_start);
		btn_pause = (Button) findViewById(R.id.button_pause);
		textview = (TextView) findViewById(R.id.textView1);
		edittext = (EditText) findViewById(R.id.edittext_rate);

		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isStop = !isStop;
				if (!isStop) {
					startTimer();
				} else {
					stopTimer();
				}
				if (isStop) {
					// btn_start.setText(R.string.start);
					btn_start.setBackgroundResource(R.drawable.bt_1);
				} else {
					// btn_start.setText(R.string.stop);
					btn_start.setBackgroundResource(R.drawable.bt_2);
				}
			}

		});
		btn_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isPause = !isPause;
				if (isPause) {
					// btn_pause.setText(R.string.resume);
					btn_pause.setBackgroundResource(R.drawable.bt_1);
				} else {
					// btn_pause.setText(R.string.pause);
					btn_pause.setBackgroundResource(R.drawable.bt_3);
				}
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_TEXTVIEW:
					updateTextView();
					break;
				default:
					break;
				}
			}
		};

	}

	private void updateTextView() {
		textview.setText(String.valueOf(count));
	}

	private void startTimer() {
		rate = Float.parseFloat(edittext.getText().toString());
		rate = 1000 * rate;
		if (timer == null) {
			timer = new Timer();
		}

		if (timertask == null) {
			timertask = new TimerTask() {
				@Override
				public void run() {
					//Log.d("aaa", "count: " + String.valueOf(count));
					sendMessage(UPDATE_TEXTVIEW);
					vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					long[] pattern = { 0, 400 };
					vibrator.vibrate(pattern, -1);
					if (flag) {
						sound.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
					}

					do {
						try {
							//Log.i("bbb", "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					} while (isPause);

					count++;
				}
			};
		}

		if (timer != null && timertask != null)
			timer.schedule(timertask, 0, (long) rate);
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timertask != null) {
			timertask.cancel();
			timertask = null;
		}
		count = 0;
	}

	private void sendMessage(int id) {
		// TODO Auto-generated method stub
		if (handler != null) {
			Message message = Message.obtain(handler, id);
			handler.sendMessage(message);
		}
	}

	protected void onDestroy() {
		sound.release();
		sound = null;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
