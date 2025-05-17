package game;

import utils.InputWithTimeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class TurnManager {
    private final GameState gameState;
    private final Scanner scanner;

    private static final int TIMEOUT_SWITCH_SECONDS = 60;
    private static final int TIMEOUT_SEAT_SECONDS = 60;
    private static final boolean SHOW_SHOCKED_CHAIR_IF_SAFE = true;

    public TurnManager(GameState gameState) {
        this.gameState = gameState;
        this.scanner = new Scanner(System.in);
    }

    public void setupElectricShock(BufferedReader in, PrintWriter out) throws IOException{
        List<Chair> chairs = gameState.getChairs();
        String attacker = gameState.isPlayerATurn() ? "A" : "B";
        out.println(attacker + "：感電イスの番号を選んでください（制限時間 " + TIMEOUT_SWITCH_SECONDS + " 秒）:");
        for (Chair chair : Chair.getRemainingChairs(chairs)) {
            out.print(chair.getNumber() + " ");
        }
        out.println("~");
        out.flush();

        try {
            String input = InputWithTimeout.readLineWithTimeout(in, TIMEOUT_SWITCH_SECONDS);
            int num = Integer.parseInt(input);

            for (Chair chair : chairs) {
                chair.clearElectricShock();
            }

            for (Chair chair : chairs) {
                if (chair.getNumber() == num) {
                    chair.placeElectricShock();
                    break;
                }
            }

        } catch (TimeoutException e) {
            out.println("時間切れです。ランダムに感電イスをセットします。");
            setRandomShock(chairs);
        } catch (Exception e) {
            out.println("無効な入力です。ランダムに感電イスをセットします。");
            setRandomShock(chairs);
        }

        out.println("相手が入力中です．．．");
    }

    public void seatPlayer(BufferedReader in, PrintWriter out) throws IOException{
        List<Chair> chairs = gameState.getChairs();
        String defender = gameState.isPlayerATurn() ? "B" : "A";
        out.println(defender + "：座る椅子を選んでください（制限時間 " + TIMEOUT_SEAT_SECONDS + " 秒）:");
        for (Chair chair : Chair.getRemainingChairs(chairs)) {
            out.print(chair.getNumber() + " ");
        }
        out.println("~");

        try {
            String input = InputWithTimeout.readLineWithTimeout(in, TIMEOUT_SEAT_SECONDS);
            int chairNum = Integer.parseInt(input);
            Chair chosen = Chair.sitOnChair(chairs, chairNum);

            if (chosen == null) {
                out.println("無効な椅子、または使用済みです。感電とみなします。");
                gameState.applyShockToDefender();
            } else if (chosen.hasElectricShock()) {
                out.println("感電しました！");
                gameState.applyShockToDefender();
            } else {
                int points = chosen.getNumber();
                out.println("無事に座れました！+" + points + "点");
                chosen.setUsed(true);  // ✅ 成功時のみ使用済みにする
                gameState.addScore(points);

                if (SHOW_SHOCKED_CHAIR_IF_SAFE) {
                    for (Chair c : chairs) {
                        if (c.hasElectricShock()) {
                            out.println("※ 感電イスは「イス番号 " + c.getNumber() + "」でした。");
                            break;
                        }
                    }
                }
            }

        } catch (TimeoutException e) {
            out.println("時間切れです。感電扱いになります。");
            gameState.applyShockToDefender();
        } catch (Exception e) {
            out.println("無効な入力です。感電扱いになります。");
            gameState.applyShockToDefender();
        }

        // ✅ isUsed == true の椅子のみが削除される
        Chair.removeUsedChairs(chairs);
    }

    public void runTurn(BufferedReader attackerIn, PrintWriter attackerOut, BufferedReader defenderIn, PrintWriter defenderOut) throws IOException{
        System.out.println("--- ターン開始 ---");
        String attacker = gameState.isPlayerATurn() ? "A" : "B";
        String defender = gameState.isPlayerATurn() ? "B" : "A";
        System.out.println("現在のターン：（攻撃 " + attacker + " / 守備 " + defender + "）");

        setupElectricShock(attackerIn, attackerOut);
        seatPlayer(defenderIn, defenderOut);

        gameState.checkGameOver();
        gameState.switchTurn();
        attackerOut.println("--- ターン終了 ---\n");
        defenderOut.println("--- ターン終了 ---\n");
    }

    private void setRandomShock(List<Chair> chairs) {
        for (Chair chair : chairs) {
            chair.clearElectricShock();
        }
        List<Chair> remaining = Chair.getRemainingChairs(chairs);
        if (!remaining.isEmpty()) {
            int randomIndex = (int) (Math.random() * remaining.size());
            remaining.get(randomIndex).placeElectricShock();
        }
    }
}

