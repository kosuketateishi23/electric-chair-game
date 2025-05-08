package game;

import java.util.*;

public class Chair {
    private final int number;
    private boolean isUsed;
    private boolean hasElectricShock;

    public Chair(int number) {
        this.number = number;
        this.isUsed = false;
        this.hasElectricShock = false;
    }

    public int getNumber() {
        return number;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean hasElectricShock() {
        return hasElectricShock;
    }

    public void placeElectricShock() {
        this.hasElectricShock = true;
    }

    public void clearElectricShock() {
        this.hasElectricShock = false;
    }

    @Override
    public String toString() {
        return "Chair{" +
                "number=" + number +
                ", isUsed=" + isUsed +
                ", hasElectricShock=" + hasElectricShock +
                '}';
    }

    // --- Static utility methods to manage a collection of chairs ---

    // 初期の椅子の状態を生成
    public static List<Chair> initializeChairs(int count) {
        List<Chair> chairs = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            chairs.add(new Chair(i));
        }
        return chairs;
    }

    // 椅子に座る処理（番号指定）
    public static Chair sitOnChair(List<Chair> chairs, int chairNumber) {
        for (Chair chair : chairs) {
            if (chair.getNumber() == chairNumber && !chair.isUsed()) {
                return chair;
            }
        }
        return null; // 無効または使用済みの椅子
    }

    // 使用済みの椅子を削除
    public static void removeUsedChairs(List<Chair> chairs) {
        chairs.removeIf(Chair::isUsed);
    }

    // 残っている椅子を取得
    public static List<Chair> getRemainingChairs(List<Chair> chairs) {
        List<Chair> remaining = new ArrayList<>();
        for (Chair chair : chairs) {
            if (!chair.isUsed()) {
                remaining.add(chair);
            }
        }
        return remaining;
    }
}
