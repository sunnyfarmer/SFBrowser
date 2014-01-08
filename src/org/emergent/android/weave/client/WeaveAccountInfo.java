package org.emergent.android.weave.client;

import java.net.URI;

public class WeaveAccountInfo {

	private final URI m_server;
	private final String m_username;
	private final String m_password;
	private final char[] m_secret;
	
	private WeaveAccountInfo(URI server, String username, String password, char[] secret) {
		if (server == null) {
			throw new NullPointerException("server was null");
		}
		if (username == null) {
			throw new NullPointerException("username was null");
		}
		if (password == null) {
			throw new NullPointerException("password was null");
		}
		if (secret == null) {
			throw new NullPointerException("secret was null");
		}
		
		m_server = server;
		m_username = username;
		m_password = password;
		m_secret = secret;
	}
	
	public URI getServer() {
		return m_server;
	}
	
	public String getServerAsString() {
		return WeaveUtil.
	}
}
