package utils;

import java.awt.*;
import javax.swing.*;

public class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); // Để nút trong suốt
        setFocusPainted(false); // Bỏ viền khi nhấn vào
        setContentAreaFilled(false); // Loại bỏ vùng nền mặc định
        setBorderPainted(false); // Loại bỏ viền mặc định
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Màu nền cho nút
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Nếu nút đang được nhấn, thay đổi màu viền hoặc hiệu ứng
        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker()); // Đổi màu khi nhấn
        } else {
            g2.setColor(getBackground());
        }

        // Vẽ viền bo tròn
        g2.drawRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Vẽ text bên trong nút
        super.paintComponent(g);
    }
}
