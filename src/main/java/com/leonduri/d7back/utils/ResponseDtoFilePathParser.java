package com.leonduri.d7back.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseDtoFilePathParser {
    private static final String baseFilePath = "/file/";
    private static final String profileFilePath = baseFilePath + "profiles/";
    private static final String thumbnailFilePath = baseFilePath + "thumbnails/";
    private static final String videoFilePath = baseFilePath + "videos/";

    public static String parseProfileFilePath(String originalPath) {
        String[] paths = originalPath.split("/");
        return profileFilePath + paths[paths.length - 1];
    }

    public static String parseThumbnailFilePath(String originalPath) {
        String[] paths = originalPath.split("/");
        return thumbnailFilePath + paths[paths.length - 1];
    }

    public static String parseVideoFilePath(String originalPath) {
        String[] paths = originalPath.split("/");
        return videoFilePath + paths[paths.length - 1];
    }

}
