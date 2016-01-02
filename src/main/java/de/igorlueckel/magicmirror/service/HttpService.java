package de.igorlueckel.magicmirror.service;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/**
 * Created by Igor on 12.04.2015.
 */
@Service
public class HttpService {
    AsyncHttpClient asyncHttpClient;

    public HttpService() {
        AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36").setFollowRedirect(true).build();
        setAsyncHttpClient(new AsyncHttpClient(cf));
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    public void setAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    @PreDestroy
    void onDestroy() {
        asyncHttpClient.close();
    }
}
