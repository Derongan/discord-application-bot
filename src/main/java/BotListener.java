import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class BotListener extends ListenerAdapter {
    private MultiValuedMap<String, ChannelListener> channelListenerMultiMap = new ArrayListValuedHashMap<>();

    void register(String channelName, ChannelListener listener){
        channelListenerMultiMap.put(channelName, listener);
    }

    void unregister(String channelName, ChannelListener listener){
        channelListenerMultiMap.removeMapping(channelName, listener);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();

        channelListenerMultiMap.get(channelName).forEach(a->a.onMessage(event));
    }
}
