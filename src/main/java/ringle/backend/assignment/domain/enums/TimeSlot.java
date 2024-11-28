package ringle.backend.assignment.domain.enums;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum TimeSlot {
    SLOT_00_00(1), SLOT_00_30(2), SLOT_01_00(3), SLOT_01_30(4), SLOT_02_00(5), SLOT_02_30(6),
    SLOT_05_00(7), SLOT_05_30(8), SLOT_06_00(9), SLOT_06_30(10), SLOT_07_00(11), SLOT_07_30(12), SLOT_08_00(13), SLOT_08_30(14), SLOT_09_00(15),
    SLOT_09_30(16), SLOT_10_00(17), SLOT_10_30(18), SLOT_11_00(19), SLOT_11_30(20), SLOT_12_00(21), SLOT_12_30(22), SLOT_13_00(23), SLOT_13_30(24),
    SLOT_19_00(25), SLOT_19_30(26), SLOT_20_00(27), SLOT_20_30(28), SLOT_21_00(29), SLOT_21_30(30), SLOT_22_00(31), SLOT_22_30(32), SLOT_23_00(33), SLOT_23_30(34);

    private final int order;

    TimeSlot(int order) {
        this.order = order;
    }

    public static TimeSlot getNextSlot(TimeSlot currentSlot) {
        for (TimeSlot slot : values()) {
            if (slot.getOrder() == currentSlot.getOrder() + 1) {
                return slot;
            }
        }
        return null;
    }
}
