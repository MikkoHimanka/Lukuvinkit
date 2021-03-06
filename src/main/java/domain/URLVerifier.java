package domain;

import java.net.URL;
import java.net.MalformedURLException;
import java.lang.IllegalArgumentException;

import io.NetworkConnection;
import io.IO;

public class URLVerifier {
    private NetworkConnection connection;
    public URLVerifier(NetworkConnection connection) {
        this.connection = connection;
    }

    public URLVerificationResult verify(String address) {
        URL url;
        try {
            url = urlFromString(address);
        } catch (MalformedURLException ex) {
            return URLVerificationResult.INVALID_FORMAT;
        }
        if (!connection.isConnected()) {
            return URLVerificationResult.NETWORK_UNREACHABLE;
        }
        if (connection.httpOk(url)) {
            return URLVerificationResult.OK;
        }
        return URLVerificationResult.NOT_EXISTING;
    }
    
    public static void printVerificationResult(URLVerificationResult result, IO io)
        throws IllegalArgumentException {
		switch (result) {
            case OK:
                io.print("Annettu linkki kelpaa.");
                break;
			case INVALID_FORMAT:
				io.print("Annetun linkin formaatti ei kelpaa.");
                break;
            case NOT_EXISTING:
                io.print("Annetun linkin sisältöä ei ole saatavilla.");
                break;
            case NETWORK_UNREACHABLE:
                io.print("Internet-yhteyden luonti epäonnistui: Linkin oikeellisuus epävarmaa.");
                break;
            default:
              throw new IllegalArgumentException("Unhandled enum value");
		}
    }
    // Tries to create a valid URL from urlString
    // If this fails, adds http:// protocol prefix and tries again
    // NOTE: URL doesn't really check the validity of the argument
    private URL urlFromString(String urlString) throws MalformedURLException {
        if (urlString.isEmpty()) {
            throw new MalformedURLException("URL cannot be empty");
        }
        try {
            return new URL(urlString);
        } catch (MalformedURLException ex) {}
        return new URL("http://" + urlString);
    }
}
