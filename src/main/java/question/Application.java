package question;

import javafx.util.Pair;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private Member member;
    private Role role;
    private List<Pair<String, String>> responses;
    private long appMessageId;

    public Application(Member member, Role role, List<Pair<String, String>> responses, long id) {
        this.member = member;
        this.responses = responses;
        this.appMessageId = id;
        this.role = role;
    }

    public Member getMember() {
        return member;
    }

    public List<Pair<String, String>> getResponses() {
        return responses;
    }

    public long getAppMessageId() {
        return appMessageId;
    }

    public Role getRole() {
        return role;
    }

    static class ApplicationBuilder {
        List<String> responses = new ArrayList<>();
        List<String> questions = new ArrayList<>();
        Member member;
        Role role;
        long id;

        Application build(){
            List<Pair<String, String>> qa = new ArrayList<>();

            for (int i = 0; i < responses.size(); i++) {
                qa.add(new Pair<>(questions.get(i), responses.get(i)));
            }

            return new Application(member, role, qa, id);
        }

        ApplicationBuilder setMember(Member member){
            this.member = member;
            return this;
        }

        ApplicationBuilder addQuestion(String question){
            questions.add(question);
            return this;
        }

        ApplicationBuilder addResponse(String response){
            responses.add(response);
            return this;
        }

        ApplicationBuilder setId(long id){
            this.id = id;
            return this;
        }

        ApplicationBuilder setRole(Role role){
            this.role = role;
            return this;
        }
    }
}
