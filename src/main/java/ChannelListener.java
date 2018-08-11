import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ChannelListener {
    void onMessage(MessageReceivedEvent event);
}
