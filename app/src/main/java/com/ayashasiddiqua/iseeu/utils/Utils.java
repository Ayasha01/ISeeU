package com.ayashasiddiqua.iseeu.utils;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static int COMPRESS_QUALITY = 50;

    public static byte[] frameByteToJpegByte(byte[] data, Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
                    size.width, size.height, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compressToJpeg(
                    new Rect(0, 0, image.getWidth(), image.getHeight()), COMPRESS_QUALITY,
                    outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}
