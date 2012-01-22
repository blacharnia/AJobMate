package rafpio.ajobmate.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import rafpio.ajobmate.model.RSSMessage;
import rafpio.ajobmate.xml.FeedParser;
import android.webkit.URLUtil;

public class RequestHandler {

    private static final String FEED_URL = "http://www.pracuj.pl/praca/rss.aspx?SE=1&R=7&C=3000015";
    private static final int CONNECTION_TIMEOUT = 10000;
    public static final String UPDATE_URL = "http://jjobm8.appspot.com/jjobm8";

    public static InputStream requestFullUpdate() {
        InputStream is = null;
        try {
            is = setupHTTPConnection(UPDATE_URL + "?action=getFullUpdate");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static InputStream requestInitServerUpdate() {
        InputStream is = null;
        try {
            is = setupHTTPConnection(UPDATE_URL + "?action=initServerUpdate");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static DataOutputStream sendServerUpdate() {
        DataOutputStream out = null;
        URL servleturl;
        try {
            servleturl = new URL(UPDATE_URL);

            URLConnection servletconnection = servleturl.openConnection();
            servletconnection.setDoInput(true);
            servletconnection.setDoOutput(true);
            servletconnection.setUseCaches(false);
            servletconnection.setDefaultUseCaches(false);
            out = new DataOutputStream(servletconnection.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static InputStream setupHTTPConnection(String url)
            throws IOException {

        if (!URLUtil.isValidUrl(url)) {
            throw new MalformedURLException();
        }

        URI uri = URI.create(url);

        HttpGet request = new HttpGet(uri);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
        request.setParams(httpParams);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        return response.getEntity().getContent();
    }

    public static InputStream requestCurrentVersion() {
        InputStream is = null;
        try {
            is = setupHTTPConnection(UPDATE_URL + "?action=getCurrentVersion");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static List<RSSMessage> requestRssOffers() {

        try {
            InputStream is = setupHTTPConnection(FEED_URL);
            ArrayList<RSSMessage> rssOffers = new FeedParser(is).parse();
            is.close();
            return rssOffers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
