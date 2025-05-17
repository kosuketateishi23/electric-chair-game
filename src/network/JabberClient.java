package network;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeoutException;

import utils.InputWithTimeout;

public class JabberClient{
    private Socket socket;
    private BufferedReader in;
    private BufferedReader keyboardIn = new BufferedReader(new InputStreamReader((System.in)));
    private PrintWriter out;
    private InetAddress addr;

    public void connect() throws IOException, TimeoutException{
        addr = InetAddress.getByName("localhost");
        socket = new Socket(addr, JabberServer.PORT);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("~")) {
                // 入力を求められたら、タイムアウト付きで受け付ける
                System.out.println("📥 サーバー: " + line);
                String input;
                try{
                    input = InputWithTimeout.readLineWithTimeout(keyboardIn, 60);
                    if(input == null || input.trim().isEmpty()){
                        input = "ENPTY";
                    }
                } catch(TimeoutException e){
                    System.out.println("⏰ タイムアウトです。続けるには Enter を押してください...");
                    flushStdin();
                    input = "";
                }    
                out.println(input);
                out.flush();
            }else{
                System.out.println("💬 サーバー: " + line);
            }
        }

        System.out.println("🔌 接続が切断されました。");
    }

    private void flushStdin() {
        try {
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (IOException ignored) {
        }
    }
}