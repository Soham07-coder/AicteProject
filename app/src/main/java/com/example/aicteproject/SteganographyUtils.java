package com.example.aicteproject;

import android.graphics.Bitmap;
import android.graphics.Color;

public class SteganographyUtils {

    // Encrypt message into an image
    public static Bitmap encodeMessage(Bitmap src, String message) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap encodedImage = src.copy(Bitmap.Config.ARGB_8888, true);

        message += "###"; // Delimiter to mark the end of the message
        int messageIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (messageIndex >= message.length()) return encodedImage;

                int pixel = encodedImage.getPixel(x, y);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                char ch = message.charAt(messageIndex++);
                int ascii = (int) ch;

                b = (b & 0xF8) | (ascii & 0x07); // Hide message in LSB

                encodedImage.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        return encodedImage;
    }

    // Decrypt message from an image
    public static String decodeMessage(Bitmap encodedImage) {
        int width = encodedImage.getWidth();
        int height = encodedImage.getHeight();
        StringBuilder decodedMessage = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = encodedImage.getPixel(x, y);
                int b = Color.blue(pixel);

                char ch = (char) (b & 0x07); // Extract from LSB
                decodedMessage.append(ch);

                if (decodedMessage.toString().endsWith("###")) {
                    return decodedMessage.toString().replace("###", ""); // Stop at delimiter
                }
            }
        }
        return "No hidden message found!";
    }
}
