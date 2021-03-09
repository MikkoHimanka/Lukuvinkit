package domain;

import java.net.URL;
import java.net.MalformedURLException;
import java.lang.IllegalArgumentException;
import java.util.List;
import java.util.ArrayList;

import io.NetworkConnection;
import io.IO;

import java.util.regex.Pattern;

public class URLVerifier {
    private NetworkConnection connection;
    public URLVerifier(NetworkConnection connection) {
        this.connection = connection;
    }

    public URLVerificationResult verify(String address) {
        List<URL> urls;
        try {
            urls = urlsFromString(address);
        } catch (MalformedURLException ex) {
            return URLVerificationResult.INVALID_FORMAT;
        }
        if (!connection.isConnected()) {
            return URLVerificationResult.NETWORK_UNREACHABLE;
        }
        for (URL url: urls) {
            if (connection.httpOk(url)) {
                return URLVerificationResult.OK;
            }
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
    // If this fails, adds http:// and https:// protocol prefixes
    // NOTE: URL doesn't really check the validity of the argument
    private List<URL> urlsFromString(String urlString) throws MalformedURLException {
        List<URL> result = new ArrayList<URL>();
        urlString = urlString.trim();
        if (urlString.isEmpty()) {
            throw new MalformedURLException("URL cannot be empty");
        }
        if (!sanityCheck(urlString)) {
            throw new MalformedURLException("URL did not pass the regex test");
        }
        try {
            result.add(new URL(urlString));
            return result;
        } catch (MalformedURLException ex) {}
        result.add(new URL("http://" + urlString));
        result.add(new URL("https://" + urlString));
        return result;
    }

    private boolean sanityCheck(String url) {
        // https:// and no further ://
        // http:// and no further ://
        // or no ://
        return Pattern.matches("^(https?://)?(?!.*://).*$", url);
    }
}
