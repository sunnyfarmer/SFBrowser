package org.emergent.android.weave.client;

public class UserWeave {

	public enum HashNode {
		INFO_COLLECTIONS(false, "/info/collections"),
		META_GLOBAL(false, "/storage/meta/global");
		
		public final boolean userServer;
		public final String nodePath;
		
		HashNode(boolean userServer, String path) {
			this.userServer = userServer;
			this.nodePath = path;
		}
	}
	
	public enum CollectionNode {
		STORAGE_BOOKMARKS("bookmarks"),
		STORAGE_PASSWORDS("passwords");
		
		public final String engineName;
		public final String nodePath;
		
		CollectionNode(String engineName) {
			this.engineName = engineName;
			this.nodePath = "/storage/" + this.engineName;
		}
	}
	
	private final WeaveTransport m_transport;
}
