package promotion;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

@FunctionalInterface
public interface PromotionStrategy {
    void promote(User user);
}
