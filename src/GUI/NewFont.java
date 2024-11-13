package GUI;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class NewFont {
    public static void usingCustomFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Define the font files
            File[] fontFiles = {
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-Bold.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-BoldItalic.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-Italic.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-Medium.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-MediumItalic.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-Regular.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-SemiBold.ttf"),
                new File("C:\\Users\\otaku\\Documents\\VSC\\Java LMS\\Resources\\Arimo\\static\\Arimo-SemiBoldItalic.ttf")
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
        // You can now use the fonts in your Java application
    }
}
