package io;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.lang.RuntimeException;
import java.io.IOException;

public class NetworkConnectionImpl implements NetworkConnection {
    private URL onlineUrl;
    private int timeout;

    // `onlineUrl` should always be online
    // `timeout` is the timeout set for establishing the connection and
    // for reading the content.
    // Does NOT include DNS lookup time!
    public NetworkConnectionImpl(String onlineUrl, int timeout) throws MalformedURLException {
        this.onlineUrl = new URL(onlineUrl);
        this.timeout = timeout;
    }
    public boolean isConnected() throws RuntimeException {
        HttpURLConnection connection;
        try {
            connection = openTimeoutConnection(onlineUrl);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            // Use HEAD to download less stuff
            connection.setRequestMethod("HEAD");
        } catch (ProtocolException ex) {
            throw new RuntimeException(ex);
        }
        return httpOk(connection);
    }
    public boolean httpOk(URL url) {
        HttpURLConnection connection;
        try {
            connection = openTimeoutConnection(url);
            // Do not use HEAD to better support servers
        } catch(IOException ex) {
            // The url was not formatted correctly
            return false;
        }
        return httpOk(connection);
    }
    private boolean httpOk(HttpURLConnection connection) {
        try {
            int response = connection.getResponseCode();
            return response == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
    private HttpURLConnection openTimeoutConnection(URL url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(this.timeout);
        connection.setReadTimeout(this.timeout);
        return connection;
    }
}
