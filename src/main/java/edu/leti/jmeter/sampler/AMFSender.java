package edu.leti.jmeter.sampler;

import org.apache.jmeter.protocol.http.control.CacheManager;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.util.JMeterUtils;

import java.io.IOException;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFSender extends HTTPSampler {
    private static final boolean OBEY_CONTENT_LENGTH =
            JMeterUtils.getPropDefault("httpsampler.obey_contentlength", false); // $NON-NLS-1$

    private static final long serialVersionUID = 233L;

    private static final int MAX_CONN_RETRIES =
            JMeterUtils.getPropDefault("http.java.sampler.retries" // $NON-NLS-1$
                    , 10); // Maximum connection retries

    private static final byte[] NULL_BA = new byte[0];// can share these

    /**
     * Handles writing of a post or put request
     */

    private volatile HttpURLConnection savedConn;


    private transient AMFPostWriter postWriter;

    private byte[] amfMessage;

    @Override
    protected HTTPSampleResult sample(URL url, String method, boolean areFollowingRedirect, int frameDepth) {
        HttpURLConnection conn = null;

        String urlStr = url.toString();

        HTTPSampleResult res = new HTTPSampleResult();
        res.setMonitor(isMonitor());

        res.setSampleLabel(urlStr);
        res.setURL(url);
        res.setHTTPMethod(method);

        res.sampleStart(); // Count the retries as well in the time

        // Check cache for an entry with an Expires header in the future
        final CacheManager cacheManager = getCacheManager();
        if (cacheManager != null && GET.equalsIgnoreCase(method)) {
            if (cacheManager.inCache(url)) {
                res.sampleEnd();
                res.setResponseNoContent();
                res.setSuccessful(true);
                return res;
            }
        }

        try {
            // Sampling proper - establish the connection and read the response:
            // Repeatedly try to connect:
            int retry;
            // Start with 0 so tries at least once, and retries at most MAX_CONN_RETRIES times
            for (retry = 0; retry <= MAX_CONN_RETRIES; retry++) {
                try {
                    conn = setupConnection(url, method, res);
                    // Attempt the connection:
                    savedConn = conn;
                    conn.connect();
                    break;
                } catch (BindException e) {
                    if (retry >= MAX_CONN_RETRIES) {
                        throw e;
                    }
                    if (conn != null) {
                        savedConn = null; // we don't want interrupt to try disconnection again
                        conn.disconnect();
                    }
                    this.setUseKeepAlive(false);
                    continue; // try again
                } catch (IOException e) {
                    throw e;
                }
            }
            if (retry > MAX_CONN_RETRIES) {
                // This should never happen, but...
                throw new BindException();
            }
            // Nice, we've got a connection. Finish sending the request:
            if (method.equals(POST)) {
                String postBody = sendPostData(conn);
                res.setQueryString(postBody);
            } else if (method.equals(PUT)) {
                String putBody = sendPutData(conn);
                res.setQueryString(putBody);
            }
            // Request sent. Now get the response:
            byte[] responseData = readResponse(conn, res);

            res.sampleEnd();
            // Done with the sampling proper.

            // Now collect the results into the HTTPSampleResult:

            res.setResponseData(responseData);

            int errorLevel = conn.getResponseCode();
            String respMsg = conn.getResponseMessage();
            String hdr = conn.getHeaderField(0);
            if (hdr == null) {
                hdr = "(null)";  // $NON-NLS-1$
            }
            if (errorLevel == -1) {// Bug 38902 - sometimes -1 seems to be returned unnecessarily
                if (respMsg != null) {// Bug 41902 - NPE
                    try {
                        errorLevel = Integer.parseInt(respMsg.substring(0, 3));
                    } catch (NumberFormatException e) {
                    }
                } else {
                    respMsg = hdr; // for result
                }
            }
            if (errorLevel == -1) {
                res.setResponseCode("(null)"); // $NON-NLS-1$
            } else {
                res.setResponseCode(Integer.toString(errorLevel));
            }
            res.setSuccessful(isSuccessCode(errorLevel));

            if (respMsg == null) {// has been seen in a redirect
                respMsg = hdr; // use header (if possible) if no message found
            }
            res.setResponseMessage(respMsg);

            String ct = conn.getContentType();
            if (ct != null) {
                res.setContentType(ct);// e.g. text/html; charset=ISO-8859-1
                res.setEncodingAndType(ct);
            }

            res.setResponseHeaders(getResponseHeaders(conn));
            if (res.isRedirect()) {
                res.setRedirectLocation(conn.getHeaderField(HEADER_LOCATION));
            }

            // If we redirected automatically, the URL may have changed
            if (getAutoRedirects()) {
                res.setURL(conn.getURL());
            }

            // Store any cookies received in the cookie manager:
            saveConnectionCookies(conn, url, getCookieManager());

            // Save cache information
            if (cacheManager != null) {
                cacheManager.saveDetails(conn, res);
            }

            res = resultProcessing(areFollowingRedirect, frameDepth, res);

            return res;
        } catch (IOException e) {
            res.sampleEnd();
            // We don't want to continue using this connection, even if KeepAlive is set
            if (conn != null) { // May not exist
                savedConn = null; // we don't want interrupt to try disconnection again
                conn.disconnect();
            }
            savedConn = null; // we don't want interrupt to try disconnection again
            conn = null; // Don't process again
            return errorResult(e, res);
        } finally {
            // calling disconnect doesn't close the connection immediately,
            // but indicates we're through with it. The JVM should close
            // it when necessary.
            savedConn = null; // we don't want interrupt to try disconnection again
            disconnect(conn); // Disconnect unless using KeepAlive
        }
    }

    public void setAmfMessage(byte[] amfMessage) {
        this.amfMessage = amfMessage;
    }

    public byte[] getAmfMessage() {
        return this.amfMessage;
    }

    private String sendPutData(URLConnection connection) throws IOException {
        return postWriter.sendPostData(connection, this);
    }

    protected String sendPostData(URLConnection connection) throws IOException {
        return postWriter.sendPostData(connection, this);
    }

    private void saveConnectionCookies(HttpURLConnection conn, URL u, CookieManager cookieManager) {
        if (cookieManager != null) {
            for (int i = 1; conn.getHeaderFieldKey(i) != null; i++) {
                if (conn.getHeaderFieldKey(i).equalsIgnoreCase(HEADER_SET_COOKIE)) {
                    cookieManager.addCookieFromHeader(conn.getHeaderField(i), u);
                }
            }
        }
    }

    protected void setPostHeaders(URLConnection conn) throws IOException {
        postWriter = new AMFPostWriter();
        postWriter.setHeaders(conn, this);
    }

    private void setPutHeaders(URLConnection conn) throws IOException {
        postWriter = new AMFPostWriter();
        postWriter.setHeaders(conn, this);
    }

}


