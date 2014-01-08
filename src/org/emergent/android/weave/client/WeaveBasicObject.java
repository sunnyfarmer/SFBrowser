package org.emergent.android.weave.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class WeaveBasicObject {

	private URI m_uri = null;
	private final URI m_queryUri;
	private final JSONObject m_nodeObj;
	
	public WeaveBasicObject(URI queryUri, JSONObject nodeObj) {
		m_queryUri = queryUri;
		m_nodeObj = nodeObj;
	}
	
	public String getId() throws JSONException {
		return m_nodeObj.getString("id");
	}
	
	public String getSortIndex() throws JSONException {
		return m_nodeObj.getString("sortindex");
	}
	
	public String getModified() throws JSONException {
		return m_nodeObj.getString("modified");
	}
	
	public Date getModifiedDate() throws JSONException {
		return WeaveUtil.toModifiedTimeDate(getModified());
	}
	
	public URI getUri() throws JSONException {
		if (m_uri == null) {
			try {
				String baseUriStr = m_queryUri.toASCIIString();
				String queryPart = m_queryUri.getRawQuery();
				if (queryPart != null) {
					baseUriStr = baseUriStr.substring(0, baseUriStr.indexOf(queryPart) - 1);
				}
				if (!baseUriStr.endsWith("/")) {
					baseUriStr += "/";
				}
				String nodeUriStr = baseUriStr + new URI(null, null, getId(), null).toASCIIString();
				m_uri = new URI(nodeUriStr);
			} catch (URISyntaxException e) {
				throw new JSONException(e.getMessage());
			}
		}
		return m_uri;
	}
	
	public JSONObject getPayload() throws JSONException {
		return new JSONObject(m_nodeObj.getString("payload"));
	}
	
	public JSONObject getEncryptedPayload(UserWeave weave, char[] secret) 
		throws JSONException, IOException, GeneralSecurityException, WeaveException {
		
	}
}
