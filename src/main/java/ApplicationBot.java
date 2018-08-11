import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import javax.security.auth.login.LoginException;
import java.io.*;

public class ApplicationBot {
    private Applications applications;

    private JDA jda;

    public ApplicationBot(String token) throws LoginException, InterruptedException, IOException {
        BotListener botListener = new BotListener();

        applications = new Applications();

        jda = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(botListener)
                .buildBlocking();

        botListener.register("bot-test-apply", new ApplicationListener(this));
        botListener.register("bot-test-review", new ReviewListener(this));
    }

    public Applications getApplications() {
        return applications;
    }

    public JDA getJDA() {
        return jda;
    }
}
