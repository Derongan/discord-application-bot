import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import question.Application;

public class ReviewListener implements ChannelListener {
    private ApplicationBot bot;

    public ReviewListener(ApplicationBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String message = event.getMessage().getContentDisplay();

            switch (message) {
                case "accept":
                    Application application = bot.getApplications().activeApplication;
                    event.getJDA().getTextChannelsByName("bot-test-apply", true)
                            .get(0)
                            .getMessageById(application.getAppMessageId()).queue(a -> {
                        a.editMessage(new ApplicationListener.ApplicationEmbedFactory(event.getJDA()).getApplicationEmbed(application.getMember(), application.getRole(), ApplicationListener.Status.ACCEPTED)).queue();
                    });
                    break;
                case "reject":
                    break;
                default:
                    break;
            }
        }
    }
}
