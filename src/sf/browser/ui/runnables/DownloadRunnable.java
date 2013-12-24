package sf.browser.ui.runnables;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import sf.browser.model.items.DownloadItem;
import sf.browser.utils.IOUtils;

public class DownloadRunnable implements Runnable {

	private static final int BUFFER_SIZE = 4096;

	private DownloadItem mParent;

	private boolean mAborted;

	public DownloadRunnable(DownloadItem parent) {
		this.mParent = parent;
		this.mAborted = false;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mParent.onFinished();
		}
	};

	/**
	 * Compute the file name given the url.
	 * @return The file name
	 */
	private String getFileNameFromUrl() {
		String fileName = this.mParent.getUrl().substring(this.mParent.getUrl().lastIndexOf("/")+1);

		int queryParamStart = fileName.indexOf("?");
		if (queryParamStart > 0) {
			fileName = fileName.substring(0, queryParamStart);
		}
		return fileName;
	}

	private File getFile() {
		File downloadFolder = IOUtils.getDownloadFolder();

		if (downloadFolder != null) {
			return new File(downloadFolder, getFileNameFromUrl());
		} else {
			mParent.setErrorMessage("Unable to get download folder from SD Card.");
			return null;
		}
	}

	@Override
	public void run() {
		File downloadFile = this.getFile();
		
		if (downloadFile != null) {
			if (downloadFile.exists()) {
				downloadFile.delete();
			}

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try {
				mParent.onStart();

				URL url = new URL(mParent.getUrl());
				URLConnection conn = url.openConnection();

				InputStream is = conn.getInputStream();
				
				int size = conn.getContentLength();
				
				String fileHeader = conn.getHeaderField("Content-Disposition");
				if (fileHeader != null) {
					fileHeader = fileHeader.toLowerCase();
					int index = fileHeader.indexOf("filename");
					if (index != -1) {
						String name = fileHeader.substring(index+"filename".length()+1, fileHeader.length());
						
						name = name.replace("'", "").replace("\"", "");
						
						if (downloadFile != null) {
							downloadFile = new File(IOUtils.getDownloadFolder(), name);
							mParent.updateFileName(name);
						}
					}
				}

				double oldCompleted = 0;
				double completed = 0;

				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(new FileOutputStream(downloadFile));
				
				boolean downLoading = true;
				byte[] buffer = new byte[BUFFER_SIZE];
				int downloaded = 0;
				int read;
				
				while ((downLoading) && (!mAborted)) {
					if ( (size - downloaded < BUFFER_SIZE) && 
							(size - downloaded > 0)) {
						buffer = new byte[size-downloaded];
					}
					
					read = bis.read(buffer);
					
					if (read > 0) {
						bos.write(buffer, 0, read);
						downloaded += read;
						
						completed = ((downloaded * 100f) / size);
					} else {
						downLoading = false;
					}
					
					// Notify each 5% or more
					if (oldCompleted + 5 < completed) {
						mParent.onProgress((int) completed);
						oldCompleted = completed;
					}
				}
			} catch (IOException ioe) {
				mParent.setErrorMessage(ioe.getMessage());
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException ioe) {
						mParent.setErrorMessage(ioe.getMessage());
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException ioe) {
						mParent.setErrorMessage(ioe.getMessage());
					}
				}
			}
			
			if (mAborted) {
				if (downloadFile.exists()) {
					downloadFile.delete();
				}
			}
		}

		mHandler.sendEmptyMessage(0);
	}

	/**
	 * Abort this download
	 */
	public void abort() {
		this.mAborted = true;
	}
}
