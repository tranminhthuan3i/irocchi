package com.iii.irocchi.UpDow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.iii.irocchi.R;

import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class FileDownloadThread extends Thread{
	
	private FileDownloadMainActivity downloadMain;
	private String downloadURL;
	private static final int DOWNLOAD_BUFFER_SIZE = 1024;
	
	public FileDownloadThread(FileDownloadMainActivity downloadMain, String url) {
		this.downloadMain = downloadMain;
		this.downloadURL = url;
		Log.i("Log : ", "Url : "+url);
	}

	@Override
	public void run() {
		
		URL url;
		URLConnection connectURL;
		int fileSize;
		int lastShash;
		String fimeName;
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		File outFile;
		FileOutputStream fileStream;
		Message msg;

		// gửi đi một Message hiển thị URL download
		msg = Message.obtain(downloadMain.activityHandler , FileDownloadMainActivity.MESSAGE_CONNECTING_STARTED, 0, 0, downloadURL);
		downloadMain.activityHandler.sendMessage(msg);
		
		try {
				// connect Url
				url = new URL(downloadURL);
				connectURL = url.openConnection();
				
				// Cho phép sử dụng bộ nhớ đệm hay không
				connectURL.setUseCaches(false);
				connectURL.connect();
				
				// Lấy ra size của file tải về
				fileSize = connectURL.getContentLength();
				
				
				// lấy ra tên file  
				lastShash = url.toString().lastIndexOf('/');
				fimeName = "";
				if(lastShash >= 0){
					fimeName = url.toString().substring(lastShash + 1 );
				}
				if(fimeName.equals("")){
					fimeName = "file.bin";
				}
				
				
				// gửi đi một Message nội dung download
				int fileSizeKB = fileSize/1024;
				msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_CONNECTING_STARTED, fileSizeKB, 0, fimeName);
				downloadMain.activityHandler.sendMessage(msg);
				
				
				// Bắt đầu Tải file

				// Đường dẫn tuyệt đối đến SDcard
				String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				
				outFile = new File(path, fimeName);
				inStream = new BufferedInputStream(url.openStream());
				fileStream = new FileOutputStream(outFile);
				outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
				byte [] arrayBytes = new byte[DOWNLOAD_BUFFER_SIZE];
				int readByteSize = 0;
				int totalReadByte = 0;
				int status = 0;
				
					while ((readByteSize = (inStream.read(arrayBytes, 0, arrayBytes.length)))>=0) {
						
						
							if(!interrupted()){
								Log.v("Log : ", "readByteSize : "+readByteSize);
								outStream.write(arrayBytes, 0, readByteSize);
								
								//gửi đi một Message Update Progress bar
								totalReadByte += readByteSize/DOWNLOAD_BUFFER_SIZE;
								msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_UPDATE_PROGRESS_BAR, totalReadByte, 0);
								downloadMain.activityHandler.sendMessage(msg);
							
								status = 0;
							}else {
								status = 1;
							}
					}
					if(status == 0){

						// thông báo file đã tải thành công
						msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_DOWNLOAD_COMPLATE);
						downloadMain.activityHandler.sendMessage(msg);
					}else if(status == 1) {
						// kiểm tra nếu xẩy ra sự cố sẽ xóa file
						outFile.delete();
						// thông báo file đã tải không thành công
						msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_DOWNLOAD_CANCELED);
						downloadMain.activityHandler.sendMessage(msg);
					}
					
				// đóng các stream
				outStream.close();
				fileStream.close();
				inStream.close();
				
			
		}catch (MalformedURLException e) {
			Log.v("Log : ", "MalformedURLException : "+e.getMessage());
			String newMsg = downloadMain.getString(R.string.error_message_bad_url);
			msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_ENCOUNTERED_ERROR, 0, 0, newMsg);
			downloadMain.activityHandler.sendMessage(msg);
				
		} catch (IOException e) {
			Log.v("Log : ", "IOException : "+e.getMessage());
			String newMsg = downloadMain.getString(R.string.error_message_connect_networks);
			msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_ENCOUNTERED_ERROR, 0, 0, newMsg);
			downloadMain.activityHandler.sendMessage(msg);
		} catch (Exception e) {
			Log.v("Log : ", "Exception : "+e.getMessage());
			String newMsg = downloadMain.getString(R.string.error_message_general);
			msg = Message.obtain(downloadMain.activityHandler, FileDownloadMainActivity.MESSAGE_ENCOUNTERED_ERROR, 0, 0, newMsg);
			downloadMain.activityHandler.sendMessage(msg);
		}
		
		
		super.run();
	}
}
