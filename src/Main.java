import java.io.IOException;
import java.util.concurrent.TimeoutException;

import network.JabberClient;
import network.JabberServer;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        if(args.length > 0 && args[0].equals("server")){
            JabberServer server = new JabberServer();
            server.start();
        }else{
            JabberClient client = new JabberClient();
            client.connect();
        }
    }
}
