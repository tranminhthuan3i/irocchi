package com.iii.irocchi.UpDow;

import java.io.File;
import java.io.IOException;

import com.iii.irocchi.R;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FileDownloadMainActivity extends Activity implements
		OnClickListener {

	private FileDownloadMainActivity thisActivity;
	private Thread downloadThread;
	private ProgressDialog progressdialog;

	private Button btnClick;
	private EditText txtURL;

	// Sử dụng để giao tiếp, biết được trạng thái FileDownloadThread
	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
	public static final int MESSAGE_DOWNLOAD_COMPLATE = 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
	public static final int MESSAGE_CONNECTING_STARTED = 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_dowload);

		// set cho các thuộc tính được khai báo trên về mặc định ban đầu
		thisActivity = this;
		downloadThread = null;
		progressdialog = null;

		// lấy id của EditText
		txtURL = (EditText) thisActivity.findViewById(R.id.url_input);

		// lấy id Của Button
		btnClick = (Button) thisActivity.findViewById(R.id.download_button);

		// set Onclick cho button
		btnClick.setOnClickListener(this.thisActivity);

	}

	private static final String AUDIO_RECORDER_FOLDER = "Irocchi/AudioRecorderIrocchi";

	public Handler activityHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			// msg.what là để nhận biết được bạn đang nhận Mesager gì

			// case 1 : Update Message ProgressBar
			case MESSAGE_UPDATE_PROGRESS_BAR:
				if (progressdialog != null) {

					// lấy Current ProgressBar
					int currentProgress = msg.arg1;

					// Update ProgressBar
					progressdialog.setProgress(currentProgress);
				}

				break;

			// case 2 : Bắt đầu kết nối
			case MESSAGE_CONNECTING_STARTED:

				// đoạn này chưa hiểu (msg.obj instanceof String)

				if (msg.obj != null && msg.obj instanceof String) {

					// lấy URL của tập tin đang được tải về
					String url = String.valueOf(msg.obj);

					// Cắt chuỗi URL cho ngăn lại // mục đích : để vừa với
					// ProgressDialog
					if (url.length() > 16) {
						String sUrl = url.substring(0, 15);
						sUrl += "...";
						url = sUrl;
					}
					// lấy Các chuỗi thông báo trong Tài nguyên String
					String progressTitle1 = thisActivity
							.getString(R.string.progress_dialog_title_connecting);
					String progressMessage1 = thisActivity
							.getString(R.string.progress_dialog_message_prefix_connecting);
					progressMessage1 += " " + url;

					// nếu đang tồn tại progressDialog thì bỏ qua và set nó bằng
					// null
					// mục đich : tạo một cái mới bên dưới
					dismissCurrentProgressDialog();

					// tạo một progress mới set các thuộc tính cho Progress
					progressdialog = new ProgressDialog(thisActivity);
					progressdialog.setTitle(progressTitle1);
					progressdialog.setMessage(progressMessage1);
					progressdialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressdialog.setIndeterminate(true);

					// gửi một thông báo
					Message newMsg = Message.obtain(this,
							MESSAGE_DOWNLOAD_CANCELED);
					progressdialog.setCancelMessage(newMsg);
					progressdialog.show();
				}
				break;

			// case 3 : Bắt đầu download
			case MESSAGE_DOWNLOAD_STARTED:
				if (msg.obj != null && msg.obj instanceof String) {

					// lấy giá trị tối đa
					int maxValue = msg.arg1;

					// Tên file được tải
					String fileName = String.valueOf(msg.obj);

					// lấy Các chuỗi thông báo trong Tài nguyên String
					String progressTitle = thisActivity
							.getString(R.string.progress_dialog_title_downloading);
					String progressMsg = thisActivity
							.getString(R.string.progress_dialog_message_prefix_downloading);
					progressMsg += "" + fileName;

					// nếu đang tồn tại progressDialog thì bỏ qua và set nó bằng
					// null
					// mục đich : tạo một cái mới bên dưới
					dismissCurrentProgressDialog();

					// tạo mới một progress = maxValue và progress = 0, set các
					// thuộc tính cho Progress
					progressdialog = new ProgressDialog(thisActivity);
					progressdialog.setTitle(progressTitle);
					progressdialog.setMessage(progressMsg);
					progressdialog
							.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressdialog.setProgress(0);
					progressdialog.setMax(maxValue);

					// gửi một thông báo
					Message newMsg = Message.obtain(this,
							MESSAGE_DOWNLOAD_CANCELED);
					progressdialog.setCancelMessage(newMsg);
					progressdialog.setCancelable(true);
					progressdialog.show();
				}
				break;

			// case 4 : download thành công
			case MESSAGE_DOWNLOAD_COMPLATE:

				// Xóa bỏ Progress đang tồn tại
				dismissCurrentProgressDialog();

				// Hiển thị message thông báo tải file thành công
				displayMessage(getString(R.string.user_message_download_completes));

				// Play audio file you have saved to SDCard
				// this method is temp file so it is not ...

				runOnUiThread(new Runnable() {
					public void run() {

						String filepath = Environment
								.getExternalStorageDirectory().getPath();
						File file = new File(filepath);
						if (!file.exists()) {
							file.mkdirs();
						}
						File[] sdDirList = file.listFiles();
						// Temp

						if (sdDirList != null) {
							MediaPlayer mediaPlayer;
								Uri uri = Uri.parse(filepath
										+ "/"
										+ sdDirList[sdDirList.length - 1]
												.getName()); // temp

								mediaPlayer = MediaPlayer.create(
										FileDownloadMainActivity.this, uri);
								Toast.makeText(getApplicationContext(),
										"Sound is playing", Toast.LENGTH_SHORT)
										.show();

							try {
								mediaPlayer.prepare();

							} catch (IllegalStateException e) {
								Log.i("Log : ", "IllegalStateException : "+e.getMessage());
								e.printStackTrace();
							} catch (IOException e) {
								Log.i("Log : ", "IOException : "+e.getMessage());
								e.printStackTrace();
							}

							mediaPlayer.start();
						}
					}
				});

				break;

			// case 5 : kết thúc download
			case MESSAGE_DOWNLOAD_CANCELED:

				// kiểm tra nếu có Thread mà bị Error
				if (downloadThread != null) {
					downloadThread.interrupt();
				}
				// Xóa bỏ progress đang tồn tại
				dismissCurrentProgressDialog();

				// Hiển thị thông báo
				displayMessage(getString(R.string.user_message_download_canceled));
				break;

			// case 6 : có lỗi xảy ra
			case MESSAGE_ENCOUNTERED_ERROR:

				if (msg.obj != null && msg.obj instanceof String) {
					String errorMsg = String.valueOf(msg.obj);

					// Xóa bỏ progress đang tồn tại
					dismissCurrentProgressDialog();

					// Hiển thị thông báo lỗi
					displayMessage(errorMsg);
				}
				break;
			default:
				break;
			}
		}

	};

	// Dismiss Progressbar, Set ProgressDialog = null
	public void dismissCurrentProgressDialog() {
		if (progressdialog != null) {
			progressdialog.hide();
			progressdialog.dismiss();
			progressdialog = null;
		}
	}

	// Display Message
	public void displayMessage(String message) {
		if (message != null) {
			Toast.makeText(thisActivity, message, Toast.LENGTH_LONG).show();
		}
	}

	// set onclick cho button (Khởi chạy Thread FileDownloadThread())
	public void onClick(View v) {
		String url = txtURL.getText().toString();
		downloadThread = new FileDownloadThread(thisActivity,
				"http://117.6.131.222:81/irocchi/uploads/1.mp4");
		// downloadThread = new FileDownloadThread(thisActivity, url);
		downloadThread.start();
	}

}
