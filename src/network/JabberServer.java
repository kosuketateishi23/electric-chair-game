package network;

import game.GameState;
import game.TurnManager;

import java.io.*;
import java.net.*;

public class JabberServer{
    public static final int PORT = 8080;

    private ServerSocket serverSocket;
    private Socket clientASocket;
    private Socket clientBSocket;
    private BufferedReader inA;
    private BufferedReader inB;
    private PrintWriter outA;
    private PrintWriter outB;

    private GameState gameState;
    private TurnManager turnManager;

    //コンストラクタ
    public JabberServer() throws IOException{
        this.serverSocket = new ServerSocket(PORT);
        gameState = new GameState();
        turnManager = new TurnManager(gameState);
    }

    public BufferedReader getReaderA() {
        return inA;
    }

    public PrintWriter getWriterA() {
        return outA;
    }

    public BufferedReader getReaderB() {
        return inB;
    }

    public PrintWriter getWriterB() {
        return outB;
    }

    public void start() throws IOException{
        System.out.println("プレイヤー2人の接続を待機中．．．");
        clientASocket = serverSocket.accept();

        inA = new BufferedReader(new InputStreamReader(clientASocket.getInputStream()));
        outA = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientASocket.getOutputStream())), true);
        outA.println("あなたはプレイヤーAです");

        clientBSocket = serverSocket.accept();

        inB = new BufferedReader(new InputStreamReader(clientBSocket.getInputStream()));
        outB = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientBSocket.getOutputStream())), true);
        outB.println("あなたはプレイヤーBです");

        System.out.println("🔌 接続完了！通信準備OK");

        // 標準出力リダイレクト用
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);  // サーバー内部出力をキャプチャ

        while (!gameState.isGameOver()) {
            boolean isATurn = gameState.isPlayerATurn();

            BufferedReader attackerIn = isATurn ? inA : inB;
            PrintWriter attackerOut = isATurn ? outA : outB;
            BufferedReader defenderIn = isATurn ? inB : inA;
            PrintWriter defenderOut = isATurn ? outB : outA;

            turnManager.runTurn(attackerIn, attackerOut, defenderIn, defenderOut);

            outA.println("現在のスコア：A=" + gameState.getScoreA() +
                            ", B=" + gameState.getScoreB());
            outA.println("感電回数：A=" + gameState.getShocksA() +
                            ", B=" + gameState.getShocksB());
            outA.println("残り椅子：" + gameState.getChairs().size());

            outB.println("現在のスコア：A=" + gameState.getScoreA() +
                            ", B=" + gameState.getScoreB());
            outB.println("感電回数：A=" + gameState.getShocksA() +
                            ", B=" + gameState.getShocksB());
            outB.println("残り椅子：" + gameState.getChairs().size());
        }

        outA.println("🏁 ゲーム終了");
        outB.println("🏁 ゲーム終了");

        if (gameState.getScoreA() >= 40) {
            outA.println("🎉 プレイヤーAの勝ち（40点達成）");
            outB.println("🎉 プレイヤーAの勝ち（40点達成）");
        } else if (gameState.getScoreB() >= 40) {
            outA.println("🎉 プレイヤーBの勝ち（40点達成）");
            outB.println("🎉 プレイヤーBの勝ち（40点達成）");
        } else if (gameState.getShocksA() >= 3) {
            outA.println("💥 プレイヤーAが3回感電、Bの勝ち！");
            outB.println("💥 プレイヤーAが3回感電、Bの勝ち！");
        } else if (gameState.getShocksB() >= 3) {
            outA.println("💥 プレイヤーBが3回感電、Aの勝ち！");
            outB.println("💥 プレイヤーBが3回感電、Aの勝ち！");
        } else {
            outA.println("🪑 椅子が1脚だけ残り、点数勝負！");
            outB.println("🪑 椅子が1脚だけ残り、点数勝負！");
            if (gameState.getScoreA() > gameState.getScoreB()) {
                outA.println("🎉 プレイヤーAの勝ち（得点）");
                outB.println("🎉 プレイヤーAの勝ち（得点）");
            } else if (gameState.getScoreB() > gameState.getScoreA()) {
                outA.println("🎉 プレイヤーBの勝ち（得点）");
                outB.println("🎉 プレイヤーBの勝ち（得点）");
            } else {
                outA.println("🤝 引き分け！");
                outB.println("🤝 引き分け！");
            }
        }

        inA.close();
        outA.close();
        clientASocket.close();

        inB.close();
        outB.close();
        clientBSocket.close();

        serverSocket.close();
    }    
}
