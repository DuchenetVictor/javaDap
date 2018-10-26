package dap.client.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import dap.client.Config;

/**
 * base service to make call.
 *
 * @author David_tepoche
 *
 */
public abstract class HttpClient {
    /**
     * return httpStatusCode Ok from server.
     */
    private static final int RESPONSE_OK_SERVEUR_STATUS_CODE = 200;

    /**
     *
     * @param url            the end of the url
     * @param requestMethode If the request is a "POST" "GET"
     * @return inputStream of the response
     * @throws IOException throws if the construction of the call fail
     */
    protected InputStream send(final String url, final String requestMethode) throws IOException {
        URL urlroot = new URL(Config.getConf().getRootUrl());
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(urlroot, getControllerRootURL() + url)
                .openConnection();
        if (httpConnection.getResponseCode() != RESPONSE_OK_SERVEUR_STATUS_CODE) {
            throw new ConnectException("connexion echoué, code retour :" + httpConnection.getResponseCode());
        }
        httpConnection.setRequestMethod(requestMethode);

        InputStream response = httpConnection.getInputStream();

        return response;

    }

    /**
     * provide the root url when you make a call from a child.
     *
     * @return the root url need to call the server.
     */
    abstract String getControllerRootURL();
}
