package GUI;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class NewFont {
    public static void usingCustomFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Define the font files
            File[] fontFiles = {
                new File("Resources/Arimo/static/Arimo-Bold.ttf"),
                new File("Resources/Arimo/static/Arimo-BoldItalic.ttf"),
                new File("Resources/Arimo/static/Arimo-Italic.ttf"),
                new File("Resources/Arimo/static/Arimo-Medium.ttf"),
                new File("Resources/Arimo/static/Arimo-MediumItalic.ttf"),
                new File("Resources/Arimo/static/Arimo-Regular.ttf"),
                new File("Resources/Arimo/static/Arimo-SemiBold.ttf"),
                new File("Resources/Arimo/static/Arimo-SemiBoldItalic.ttf"),
            };

            // Register each font
            for (File fontFile : fontFiles) {
                if (fontFile.exists()) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                    ge.registerFont(font);
                }
            }
        } catch (FontFormatException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading fonts: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        usingCustomFonts();
    }
}
