package com.motompro.tcp_config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {

    public ImageIcon DELETE_ICON;
    public ImageIcon EXPORT_ICON;
    public ImageIcon EDIT_ICON;
    public ImageIcon USE_ICON;

    public Images() {
        try {
            DELETE_ICON = new ImageIcon(resizeImage(ImageIO.read(getClass().getResource("/image/delete_icon.png")), 16, 16));
            EXPORT_ICON = new ImageIcon(resizeImage(ImageIO.read(getClass().getResource("/image/export_icon.png")), 16, 16));
            EDIT_ICON = new ImageIcon(resizeImage(ImageIO.read(getClass().getResource("/image/edit_icon.png")), 16, 16));
            USE_ICON = new ImageIcon(resizeImage(ImageIO.read(getClass().getResource("/image/use_icon.png")), 16, 16));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
