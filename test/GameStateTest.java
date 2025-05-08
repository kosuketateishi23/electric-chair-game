import game.GameState;
import game.Chair;

public class GameStateTest {
    public static void main(String[] args) {
        GameState gs = new GameState();

        System.out.println("初期スコア: A=" + gs.getScoreA() + ", B=" + gs.getScoreB());
        System.out.println("プレイヤーAのターンか？: " + gs.isPlayerATurn());

        gs.addScore(10);
        System.out.println("スコア加算後: A=" + gs.getScoreA());

        gs.applyShock(); // 感電させてみる
        System.out.println("感電後スコア: A=" + gs.getScoreA() + ", 感電数: " + gs.getShocksA());

        gs.switchTurn(); // ターン交代
        System.out.println("今プレイヤーAのターンか？: " + gs.isPlayerATurn());

        System.out.println("残り椅子数: " + Chair.getRemainingChairs(gs.getChairs()).size());

        // 終了条件を試す
        for (int i = 0; i < 3; i++) gs.applyShock(); // 感電数増やして終了に
        System.out.println("ゲーム終了判定: " + gs.checkGameOver());
    }
}
