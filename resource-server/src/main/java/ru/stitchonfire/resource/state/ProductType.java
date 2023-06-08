package ru.stitchonfire.resource.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {
    PROCESSOR("Процессор"),
    VIDEO_CARD("Видеокарта"),
    SSD("ССД"),
    HDD("Жесткий диск"),
    MOTHERBOARD("Материнская плата"),
    RAM("Оперативная память"),
    AIR_COOLING("Воздушное охлаждение"),
    WATER_COOLING("Водяное охлаждение"),
    CASE("Корпус");

    private final String name;
}
