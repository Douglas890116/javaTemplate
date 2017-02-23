package com.template.common;

/**
 * Created by Cloud on 2017-02-16.
 */
public enum WeekDays {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private int index;

    WeekDays(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
