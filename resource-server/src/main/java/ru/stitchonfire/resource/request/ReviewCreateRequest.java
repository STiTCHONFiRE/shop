package ru.stitchonfire.resource.request;

public record ReviewCreateRequest(
        int rating,
        String text,
        String orderId
) {
}
