package rafpio.ajobmate.core;


public class DataContainer {

    public static final String[] OPTIONS = { "My Offers", "Online offers", "Tasks",
            "History" };

    private static class DataContainerHolder {
        private static final DataContainer INSTANCE = new DataContainer();
    }

    public static DataContainer getInstance() {
        return DataContainerHolder.INSTANCE;
    }

    private DataContainer() {
    }

    /*
     * public void addRssMessage(final RSSMessage rssMesssage) {
     * mRSSMessages.add(rssMesssage); }
     * 
     * public int getNumOfRssMessages() { return mRSSMessages.size(); }
     * 
     * public void deleteRssMessages() { mRSSMessages.clear(); }
     * 
     * public void addRssMessages(final ArrayList<RSSMessage> rssMsgs) {
     * mRSSMessages.addAll(rssMsgs); }
     * 
     * public RSSMessage getRssMessageAt(int index) { return
     * mRSSMessages.get(index); }
     */
}
