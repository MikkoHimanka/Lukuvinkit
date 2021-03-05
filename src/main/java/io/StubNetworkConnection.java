package io;
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;

public class StubNetworkConnection implements NetworkConnection{
    private boolean connected;
    private List<URL> validUrls;
    public StubNetworkConnection(boolean connected, List<URL> validUrls) {
        this.connected = connected;
        this.validUrls = validUrls;
    }
    public void setConnectionStatus(boolean connected) {
        this.connected = connected;
    }
    public boolean isConnected() {
        return connected;
    }
    public boolean httpOk(URL url) {
        return validUrls.contains(url);
    }
}
