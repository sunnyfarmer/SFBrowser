package sf.browser.events;

public interface IDownloadEventsListener {

	/**
	 * 发生事件出发时执行
	 * @param event	事件
	 * @param data 其他内容
	 */
	void onDownloadEvent(String event, Object data);
}
