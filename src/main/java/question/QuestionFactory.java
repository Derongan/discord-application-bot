package question;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;


public class QuestionFactory {
    public QuestionAsker getAsker(Member member, Role role, long id){
        return new QuestionAsker(member, role, id);
    }
}
