package edu.leti.jmeter.sampler.http;

import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.sampler.PostWriter;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;

import java.io.*;
import java.net.URLConnection;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class AMFPostWriter extends PostWriter {
    private final static byte[] CRLF = {0x0d, 0x0A};
    private static final byte[] DASH_DASH_BYTES = {'-', '-'};
    private static final String DASH_DASH = "--";  // $NON-NLS-1$

    @Override
    public String sendPostData(URLConnection connection, HTTPSampler sampler) throws IOException {
        // Buffer to hold the post body, except file content
        StringBuilder postedBody = new StringBuilder(1000);

        AMFHTTPSampler AMFHTTPSampler = (AMFHTTPSampler) sampler;
        HTTPFileArg files[] = AMFHTTPSampler.getHTTPFiles();

        String contentEncoding = AMFHTTPSampler.getContentEncoding();
        if (contentEncoding == null || contentEncoding.length() == 0) {
            contentEncoding = ENCODING;
        }

        // Check if we should do a multipart/form-data or an
        // application/x-www-form-urlencoded post request
        if (AMFHTTPSampler.getUseMultipartForPost()) {
            OutputStream out = connection.getOutputStream();

            // Write the form data post body, which we have constructed
            // in the setHeaders. This contains the multipart start divider
            // and any form data, i.e. arguments
            out.write(formDataPostBody);
            // Retrieve the formatted data using the same encoding used to create it
            postedBody.append(new String(formDataPostBody, contentEncoding));

            // Add any files
            for (int i = 0; i < files.length; i++) {
                HTTPFileArg file = files[i];
                // First write the start multipart file
                byte[] header = file.getHeader().getBytes();  //
                out.write(header);
                // Retrieve the formatted data using the same encoding used to create it
                postedBody.append(new String(header)); //
                // Write the actual file content
                writeFileToStream(file.getPath(), out);
                // We just add placeholder text for file content
                postedBody.append("<actual file content, not shown here>"); // $NON-NLS-1$
                // Write the end of multipart file
                byte[] fileMultipartEndDivider = getFileMultipartEndDivider();
                out.write(fileMultipartEndDivider);
                // Retrieve the formatted data using the same encoding used to create it
                postedBody.append(new String(fileMultipartEndDivider, ENCODING));
                if (i + 1 < files.length) {
                    out.write(CRLF);
                    postedBody.append(new String(CRLF));
                }
            }
            // Write end of multipart
            byte[] multipartEndDivider = getMultipartEndDivider();
            out.write(multipartEndDivider);
            postedBody.append(new String(multipartEndDivider, ENCODING));

            out.flush();
            out.close();
        } else if (AMFHTTPSampler.getAmfMessage() != null) {
            OutputStream out = connection.getOutputStream();
            out.write(AMFHTTPSampler.getAmfMessage().getBytes());
            out.flush();
            out.close();

            postedBody.append(AMFHTTPSampler.getAmfMessage());
        } else {
            OutputStream out = connection.getOutputStream();
            out.write("No message!".getBytes());
            out.flush();
            out.close();

            postedBody.append("No message!");
        }

        return postedBody.toString();
    }

    private void writeFileToStream(String filename, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        // 1k - the previous 100k made no sense (there's tons of buffers
        // elsewhere in the chain) and it caused OOM when many concurrent
        // uploads were being done. Could be fixed by increasing the evacuation
        // ratio in bin/jmeter[.bat], but this is better.
        InputStream in = new BufferedInputStream(new FileInputStream(filename));
        int read;
        try {
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            in.close();
        }
    }

    private byte[] getFileMultipartEndDivider() throws IOException {
        byte[] ending = getMultipartDivider();
        byte[] completeEnding = new byte[ending.length + CRLF.length];
        System.arraycopy(CRLF, 0, completeEnding, 0, CRLF.length);
        System.arraycopy(ending, 0, completeEnding, CRLF.length, ending.length);
        return completeEnding;
    }

    private byte[] getMultipartEndDivider() {
        byte[] ending = DASH_DASH_BYTES;
        byte[] completeEnding = new byte[ending.length + CRLF.length];
        System.arraycopy(ending, 0, completeEnding, 0, ending.length);
        System.arraycopy(CRLF, 0, completeEnding, ending.length, CRLF.length);
        return completeEnding;
    }

    private byte[] getMultipartDivider() throws IOException {
        return (DASH_DASH + getBoundary()).getBytes(ENCODING);
    }
}
