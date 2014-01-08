package org.emergent.android.weave.client;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class WeaveResponse {

	private final WeaveTransport.WeaveResponseHeaders m_responseHeaders;
	private final String m_body;
	private URI m_uri;

	public WeaveResponse(HttpResponse response) throws IOException {
		m_responseHeaders = new WeaveTransport.WeaveResponseHeaders(response);
		HttpEntity entity = response.getEntity();
		m_body = entity == null ? null : EntityUtils.toString(entity);
	}
	
	public WeaveTransport.WeaveResponseHeaders getResponseHeaders() {
		return m_responseHeaders;
	}
	
	public String getBody() {
		return m_body;
	}
	
	public Date getServerTimestamp() {
		return m_responseHeaders.getServerTimestamp();
	}
	
	public long getBackoffSeconds() {
		return m_responseHeaders.getBackoffSeconds();
	}
	
	public URI getUri() {
		return m_uri;
	}
	
	public void setUri(URI uri) {
		this.m_uri = uri;
	}
}
