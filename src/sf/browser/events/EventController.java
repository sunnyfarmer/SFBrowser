package sf.browser.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EventController {

	private List<IDownloadEventsListener> mDownloadListeners;

	private static class EventControllerHolder {
		private static final EventController INSTANCE = new EventController();
	}

	public static EventController getInstance() {
		return EventControllerHolder.INSTANCE;
	}

	private EventController() {
		mDownloadListeners = new ArrayList<IDownloadEventsListener>();
	}
	
	public synchronized void addDownloadListener(IDownloadEventsListener listener) {
		if (!mDownloadListeners.contains(listener)) {
			mDownloadListeners.add(listener);
		}
	}
	
	public synchronized void removeDownloadListener(IDownloadEventsListener listener) {
		this.mDownloadListeners.remove(listener);
	}

	public synchronized void fireDownloadEvent(String event, Object data) {
		Iterator<IDownloadEventsListener> iter = this.mDownloadListeners.iterator();
		while (iter.hasNext()) {
			iter.next().onDownloadEvent(event, data);
		}
	}
}
