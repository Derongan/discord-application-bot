import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import question.Application;
import question.QuestionAsker;
import question.QuestionFactory;

import java.awt.*;

public class ApplicationListener implements ChannelListener {
    private JDA jda;

    private ApplicationEmbedFactory applicationEmbedFactory;
    private ApplicationReviewEmbedFactory applicationReviewEmbedFactory = new ApplicationReviewEmbedFactory();

    private ApplicationBot bot;

    public ApplicationListener(ApplicationBot bot) {
        this.bot = bot;
        jda = bot.getJDA();
        this.applicationEmbedFactory = new ApplicationEmbedFactory(jda);
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        Member member = event.getMember();
        Message message = event.getMessage();
        TextChannel channel = event.getMessage().getTextChannel();

        if (isApplication(message)) {
            System.out.println(member.getNickname() + " started an application");

            Role role;

            if(message.getContentDisplay().split(" ").length == 2){
                role = jda.getRolesByName("junior builder", true).get(0);
//                role = jda.getRolesByName(message.getContentDisplay().split(" ")[1], true).get(0);
            } else {
                role = jda.getRolesByName("junior builder", true).get(0);
            }

            channel.sendMessage(applicationEmbedFactory.getApplicationEmbed(member, role, Status.INCOMPLETE)).queue(embedMessage -> {
                message.delete().queue();

                QuestionAsker asker = new QuestionFactory().getAsker(member, role, embedMessage.getIdLong());
                asker.start((app) -> {
                    embedMessage.editMessage(applicationEmbedFactory.getApplicationEmbed(member, role, Status.PENDING)).queue();
                    jda.getTextChannelsByName("bot-test-review", true).get(0).sendMessage(applicationReviewEmbedFactory.getApplicationReviewEmbed(app)).queue(review -> {
                        review.addReaction("✅").queue(c -> {
                            review.addReaction("\uD83C\uDDFD").queue();
                        });
                    });

                    bot.getApplications().activeApplication = app;
                });
            });
        }
    }

    public enum Status {
        INCOMPLETE,
        PENDING,
        ACCEPTED,
        REJECTED;
    }

    public static class ApplicationEmbedFactory {
        private JDA jda;

        public ApplicationEmbedFactory(JDA jda) {
            this.jda = jda;
        }

        MessageEmbed getApplicationEmbed(Member member, Role role, Status status) {
            switch (status) {
                case PENDING:
                    return getEmbed(member, role, "pending", Color.GRAY);
                case ACCEPTED:
                    return getEmbed(member, role, "accepted", Color.GREEN);
                case REJECTED:
                    return getEmbed(member, role, "rejected", Color.RED);
                case INCOMPLETE:
                    return getEmbed(member, role,"incomplete", Color.DARK_GRAY);
                default:
                    throw new IllegalArgumentException(status + " is not a valid status");
            }
        }

        private MessageEmbed getEmbed(Member member, Role role, String statusMessage, Color color) {
            return new EmbedBuilder()
                    .setTitle("Application")
                    .setDescription("This message will update when the application is accepted/rejected. The user will also be messaged about the result.")
                    .setColor(color)
                    .addField("User", member.getAsMention(), true)
                    .addField("Role", role.getAsMention(), true)
                    .addBlankField(false)
                    .addField("Status", statusMessage, false)
                    .build();
        }
    }

    private class ApplicationReviewEmbedFactory {
        MessageEmbed getApplicationReviewEmbed(Application application) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Application Review")
                    .setColor(Color.GRAY)
                    .addField("User", application.getMember().getAsMention(), true)
                    .addField("Role", application.getRole().getAsMention(), true)
                    .setThumbnail("https://ddc1.s3.amazonaws.com/IjiqBex9qmufiUO8/CDy2BvBgoiXPo024/lv4LrgOqjQ%3D%3D/approved.png")
                    .addBlankField(false);

            application.getResponses().forEach(a -> {
                builder.addField(a.getKey(), a.getValue(), false);
            });

            return builder.build();
        }
    }


    private boolean isApplication(Message message) {
        return message.getContentDisplay().startsWith("!apply");
    }
}
