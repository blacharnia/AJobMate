package rafpio.ajobmate.xml;

import java.io.InputStream;
import java.util.ArrayList;

import rafpio.ajobmate.model.RSSMessage;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class FeedParser {

    static final String RSS = "rss";

    static final String CHANNEL = "channel";
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";
    private InputStream inputStream;

    public FeedParser(InputStream is) {
        this.inputStream = is;
    }

    public ArrayList<RSSMessage> parse() {
        final RSSMessage currentMessage = new RSSMessage();
        RootElement root = new RootElement(RSS);
        final ArrayList<RSSMessage> messages = new ArrayList<RSSMessage>();
        Element channel = root.getChild(CHANNEL);
        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                messages.add(currentMessage.copy());
            }
        });
        item.getChild(TITLE).setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentMessage.setTitle(body);
                    }
                });
        item.getChild(LINK).setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentMessage.setLink(body);
                    }
                });
        item.getChild(DESCRIPTION).setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentMessage.setDescription(body);
                    }
                });
        item.getChild(PUB_DATE).setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentMessage.setDate(body);
                    }
                });
        try {
            Xml.parse(inputStream, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

}
