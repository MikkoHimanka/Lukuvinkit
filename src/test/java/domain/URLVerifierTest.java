package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;

import io.StubNetworkConnection;
import io.StubIO;
import java.lang.RuntimeException;
import java.net.MalformedURLException;

public class URLVerifierTest {
    private StubNetworkConnection connection;
    private URLVerifier verifier;
    @Before
    public void init() throws RuntimeException {
        List<URL> validUrls = new ArrayList<URL>();
        try {
            validUrls.add(new URL("http://www.helsinki.fi"));
            validUrls.add(new URL("https://en.wikipedia.org/wiki/Java_(programming_language)"));
            validUrls.add(new URL("https://www.is.fi/viihde"));
        } catch(MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        connection = new StubNetworkConnection(true, validUrls);
        verifier = new URLVerifier(connection);
    }
    @Test
    public void testOkFullUrl() {
        assertEquals(URLVerificationResult.OK, verifier.verify("http://www.helsinki.fi"));
        assertEquals(URLVerificationResult.OK, verifier.verify("https://en.wikipedia.org/wiki/Java_(programming_language)"));
        assertEquals(URLVerificationResult.OK, verifier.verify("www.helsinki.fi"));
    }
    @Test
    public void testOkWithoutProtocolHttp() {
        assertEquals(URLVerificationResult.OK, verifier.verify("www.helsinki.fi"));
    }
    @Test
    public void testOkWithoutProtocolHttps() {
        assertEquals(URLVerificationResult.OK, verifier.verify("www.is.fi/viihde"));
    }
    @Test
    public void testNonemptyInvalid() {
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("http://helsinki.fi://"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("asdfhttp://helsinki.fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("httpss://helsinki.fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("ftp://helsinki.fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("irc://helsinki.fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("ew_/irc://fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("://"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("://helsinki.fi"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("::///"));
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify("www.helsinki.fi://lol"));
    }
    @Test
    public void testEmptyUrl() {
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify(""));
    }
    @Test
    public void testNotExistingUrl() {
        assertEquals(URLVerificationResult.NOT_EXISTING,
                     verifier.verify("www.tampere.fi"));
    }
    @Test
    public void testValidURLNoNetwork() {
        connection.setConnectionStatus(false);
        assertEquals(URLVerificationResult.NETWORK_UNREACHABLE,
                     verifier.verify("www.helsinki.fi"));
    }
    @Test
    public void testInvalidUrlNoNetwork() {
        connection.setConnectionStatus(false);
        assertEquals(URLVerificationResult.INVALID_FORMAT, verifier.verify(""));
    }
    @Test
    public void testPrintResult() {
        StubIO io = new StubIO();
        verifier.printVerificationResult(URLVerificationResult.OK, io);
        verifier.printVerificationResult(URLVerificationResult.INVALID_FORMAT, io);
        verifier.printVerificationResult(URLVerificationResult.NOT_EXISTING, io);
        verifier.printVerificationResult(URLVerificationResult.NETWORK_UNREACHABLE, io);
        List<String> out = io.getPrints();
        assertEquals("Annettu linkki kelpaa.", out.get(0));
        assertEquals("Annetun linkin formaatti ei kelpaa.", out.get(1));
        assertEquals("Annetun linkin sisaltoa ei ole saatavilla.", out.get(2));
        assertEquals("Internet-yhteyden luonti epaonnistui: Linkin oikeellisuus epavarmaa.", out.get(3));
    }
}
