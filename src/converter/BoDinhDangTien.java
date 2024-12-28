/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter;

import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class BoDinhDangTien {
    
   // Phương thức để chuyển đổi chuỗi giá tiền thành kiểu double
    public double parsePrice(String priceString) {
        // Kiểm tra nếu chuỗi giá là null hoặc trống
        if (priceString == null || priceString.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Giá không hợp lệ: chuỗi rỗng");
            return 0;
        }
        
        // Xóa " VNĐ", xóa dấu cách đầu và cuối, và thay thế dấu phẩy bằng dấu chấm
        String cleanString = priceString.trim().replace(" VNĐ", "").replace(".", "").replace(",", ".");
        
        try {
            return Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá không hợp lệ: " + priceString);
            return 0; // Hoặc xử lý theo cách khác
        }
    }
}
