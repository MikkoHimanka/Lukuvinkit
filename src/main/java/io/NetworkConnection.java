package io;
import java.net.URL;
public interface NetworkConnection {
    boolean isConnected();
    boolean httpOk(URL url);
}
