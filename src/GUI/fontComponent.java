package GUI;

import java.awt.*;

public interface fontComponent {
    // centralized fonts and colors usage throughout entire GUI

    Color PRIMARY_COLOR = new Color(255, 136, 0);
    Color BACKGROUND_COLOR = Color.WHITE;
    Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    Font TITLE_FONT14 = new Font("Segoe UI", Font.BOLD, 14);
    Font TITLE_FONT18 = new Font("Segoe UI", Font.BOLD, 18);

    Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    Font PLAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    Font PLAIN_FONT16 = new Font("Segoe UI", Font.PLAIN, 16);
}
