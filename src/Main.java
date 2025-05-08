import game.GameState;
import game.TurnManager;

public class Main {
    public static void main(String[] args) {
        GameState gameState = new GameState();
        TurnManager turnManager = new TurnManager(gameState);

        System.out.println("🎮 電気椅子ゲーム開始！");

        while (!gameState.isGameOver()) {
            turnManager.runTurn();
            System.out.println("現在のスコア：A=" + gameState.getScoreA() +
                               ", B=" + gameState.getScoreB());
            System.out.println("感電回数：A=" + gameState.getShocksA() +
                               ", B=" + gameState.getShocksB());
            System.out.println("残り椅子：" + gameState.getChairs().size());
        }

        System.out.println("\n🏁 ゲーム終了！");

        // 勝敗表示
        if (gameState.getScoreA() >= 40) {
            System.out.println("🎉 プレイヤーAの勝ち（40点達成）");
        } else if (gameState.getScoreB() >= 40) {
            System.out.println("🎉 プレイヤーBの勝ち（40点達成）");
        } else if (gameState.getShocksA() >= 3) {
            System.out.println("💥 プレイヤーAが3回感電、Bの勝ち！");
        } else if (gameState.getShocksB() >= 3) {
            System.out.println("💥 プレイヤーBが3回感電、Aの勝ち！");
        } else {
            System.out.println("🪑 椅子が1脚だけ残り、点数勝負！");
            if (gameState.getScoreA() > gameState.getScoreB()) {
                System.out.println("🎉 プレイヤーAの勝ち（得点）");
            } else if (gameState.getScoreB() > gameState.getScoreA()) {
                System.out.println("🎉 プレイヤーBの勝ち（得点）");
            } else {
                System.out.println("🤝 引き分け！");
            }
        }
    }
}
