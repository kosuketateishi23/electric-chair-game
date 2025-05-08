package game;

import java.util.*;

public class GameState {
    private List<Chair> chairs;         // 全イスの状態
    private int scoreA, scoreB;         // プレイヤーAとBのスコア
    private int shocksA, shocksB;       // プレイヤーAとBの感電数
    private boolean isPlayerATurn;      // 今のターンはAかBか
    private boolean gameOver;           // ゲーム終了フラグ

    public static final int MAX_SCORE = 40;
    public static final int MAX_SHOCKS = 3;
    public static final int TOTAL_CHAIRS = 12;

    // コンストラクタ
    public GameState() {
        this.chairs = Chair.initializeChairs(TOTAL_CHAIRS);
        this.scoreA = 0;
        this.scoreB = 0;
        this.shocksA = 0;
        this.shocksB = 0;
        this.isPlayerATurn = true;
        this.gameOver = false;
    }

    // getter類（スコアやターン状態の取得）
    public List<Chair> getChairs() { return chairs; }
    public boolean isPlayerATurn() { return isPlayerATurn; }
    public int getScoreA() { return scoreA; }
    public int getScoreB() { return scoreB; }
    public int getShocksA() { return shocksA; }
    public int getShocksB() { return shocksB; }
    public boolean isGameOver() { return gameOver; }

    // 得点処理
    public void addScore(int points) {
        if (isPlayerATurn) scoreA += points;
        else scoreB += points;
    }

    // 感電処理（スコアリセット＋感電数＋1）
    public void applyShockToDefender() {
        if (isPlayerATurn) {
            // 攻撃A → 感電B
            scoreB = 0;
            shocksB++;
        } else {
            // 攻撃B → 感電A
            scoreA = 0;
            shocksA++;
        }
    }

    // ターン切り替え
    public void switchTurn() {
        isPlayerATurn = !isPlayerATurn;
    }

    // ゲーム終了判定
    public boolean checkGameOver() {
        int remaining = Chair.getRemainingChairs(chairs).size();

        if (scoreA >= MAX_SCORE || scoreB >= MAX_SCORE) {
            gameOver = true;
        } else if (shocksA >= MAX_SHOCKS || shocksB >= MAX_SHOCKS) {
            gameOver = true;
        } else if (remaining <= 1) {
            gameOver = true;
        }
        return gameOver;
    }
}
