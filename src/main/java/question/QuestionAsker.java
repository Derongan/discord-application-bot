package question;

import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

public class QuestionAsker {
    private Member applicant;
    private Role role;
    private Consumer<Application> consumer;
    private Application.ApplicationBuilder builder = new Application.ApplicationBuilder();
    private long id;

    private Iterator<String> questions = Arrays.asList("What is your name", "What is your quest").iterator();

    private ListenerAdapter adapter = new ListenerAdapter() {
        @Override
        public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
            if(event.getAuthor().equals(applicant.getUser())) {
                builder.addResponse(event.getMessage().getContentDisplay());
                handleNextQuestion();
            }
        }
    };

    public QuestionAsker(Member applicant, Role role, long id) {
        this.applicant = applicant;
        this.role = role;
        this.id = id;
    }

    public void start(@NotNull Consumer<Application> finishedConsumer){
        applicant.getJDA().addEventListener(adapter);
        this.consumer = finishedConsumer;

        builder.setMember(applicant)
                .setId(id)
                .setRole(role);

        handleNextQuestion();
    }

    private void handleNextQuestion() {
        if(questions.hasNext()) {
            applicant.getUser().openPrivateChannel().queue(a -> {
                String next = questions.next();
                builder.addQuestion(next);
                a.sendMessage(next).queue();
            });
        } else {
            stop();
        }
    }

    private void stop(){
        applicant.getJDA().removeEventListener(adapter);
        consumer.accept(builder.build());
    }

}
