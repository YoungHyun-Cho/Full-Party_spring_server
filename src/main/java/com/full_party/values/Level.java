package com.full_party.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Level {
    LV_0(0, 0),
    LV_1(1, 5),
    LV_2(2, LV_1.levelUpExp + 5),
    LV_3(3, LV_2.levelUpExp + 5 + 5),
    LV_4(4, LV_3.levelUpExp + 5 + 5 + 5),
    LV_5(5, LV_4.levelUpExp + 5 + 5 + 5 + 5),
    LV_6(6, LV_5.levelUpExp + 5 + 5 + 5 + 5 + 5),
    LV_7(7, LV_6.levelUpExp + 5 + 5 + 5 + 5 + 5 + 5),
    LV_8(8, LV_7.levelUpExp + 5 + 5 + 5 + 5 + 5 + 5 + 5),
    LV_9(9, LV_8.levelUpExp + 5 + 5 + 5 + 5 + 5 + 5 + 5 + 5),
    LV_10(10, LV_9.levelUpExp + 5 + 5 + 5 + 5 + 5 + 5 + 5 + 5 + 5);


    @Getter
    private Integer value;

    @Getter
    private Integer levelUpExp;

    @Getter
    private static final Integer MAX_LEVEL = LV_10.value;

    Level(Integer value, Integer levelUpExp) {
        this.value = value;
        this.levelUpExp = levelUpExp;
    }

//    private Integer calculateLevelUpExp() {
//        return getLevel(value - 1).levelUpExp + (5 * (value - 1));
//
//    }

    public static Result calculateLevel(Integer userExp, Integer userLevel) {

        if (userLevel == MAX_LEVEL) return new Result(userLevel, userExp, null); // 만렙인 경우 레벨업 불가
        if (userExp < 0) return new Result(userLevel - 1, getLevel(userLevel).getLevelUpExp() + userExp, NotificationInfo.LEVEL_DOWN); // 경험치 음수인 경우 레벨 다운

        // 만렙이 아니고, 경험치가 양수인 경우, 사용자의 레벨에 따른 레벨업 경험치를 찾고, 사용자의 경험치가 레벨업 경험치보다 크거나 같으면 레벨업
        for (Level level : Level.values()) {
            if (userLevel == level.getValue() && userExp >= level.getLevelUpExp())
                return new Result(userLevel + 1, userExp - getLevel(userLevel).getLevelUpExp(), NotificationInfo.LEVEL_UP);
        }

        return new Result(userLevel, userExp, null);
    }

    public static Level getLevel(Integer userLevel) {
        for (Level level : Level.values()) {
            if (userLevel == level.getValue()) return level;
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    public static class Result {

        private Integer level;
        private Integer exp;
        private NotificationInfo notificationInfo;
    }
}
