package org.emergent.android.weave.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This socket factory will create ssl socket that uses configurable validation of
 * certificates (e.g. allowing self-signed).
 * @author user
 *
 */
public class WeaveSSLSocketFactory implements SocketFactory,
		LayeredSocketFactory {
	
	private static final boolean DISABLE_SERVER_CERT_CHECK = true; // TODO look into this.
	
	private SSLContext m_sslcontext = null;
	
	private synchronized SSLContext getSSLContext() throws IOException {
		if (m_sslcontext == null) {
			m_sslcontext = createEasySSLContext();
		}
		
		return m_sslcontext;
	}
	
	public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params)
		throws IOException {
		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);
		
		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
		SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());
		
		if ((localAddress != null) || (localPort > 0)) {
			if (localPort < 0) {
				localPort = 0;
			}
			
			InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
			sslsock.bind(isa);
		}
		
		sslsock.connect(remoteAddress, connTimeout);
		sslsock.setSoTimeout(soTimeout);
		return sslsock;
	}

}
