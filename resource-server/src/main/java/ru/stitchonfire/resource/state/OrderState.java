package ru.stitchonfire.resource.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState {
    FORMATION("формирование"),
    ON_THE_WAY("в пути"),
    AWAITING_RECEIPT("ожидает получения"),
    DONE("завершен"),
    CANCELED("отменен");

    private final String name;
}
