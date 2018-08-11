import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        ApplicationBot bot = new ApplicationBot(getToken());
    }

    private static String getToken() throws IOException {
        File file = new File(Main.class.getClassLoader().getResource("application.config").getFile());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader.readLine();
    }
}
