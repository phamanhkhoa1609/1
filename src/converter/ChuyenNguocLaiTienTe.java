/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author ACER
 */
public class ChuyenNguocLaiTienTe {
    
//    public static double chuyenNguocLaiTien(String giaTien) {
//        
//        // Xóa " VNĐ", xóa dấu cách đầu và cuối, và thay thế dấu phẩy bằng dấu chấm
//        String lamSachChuoi = giaTien.trim().replace(" VNĐ", "").replace(".", "").replace(",", ".");
//        
//        try {
//            return Double.parseDouble(lamSachChuoi);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Giá không hợp lệ: " + giaTien);
//            return 0;
//        }
//    }
    
    public static double chuyenNguocLaiTien(String giaTien) {
        if (giaTien == null || giaTien.trim().isEmpty()) {
            return 0;
        }
        // Loại bỏ ký tự không hợp lệ, giữ nguyên dấu phân cách ngàn
        String lamSachChuoi = giaTien.trim()
                                     .replace(" VNĐ", "")
                                     .replaceAll("[^\\d,]", ""); // Giữ lại dấu phẩy
        // Xóa dấu phẩy phân cách ngàn
        lamSachChuoi = lamSachChuoi.replace(",", "");
        try {
            return Double.parseDouble(lamSachChuoi);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá không hợp lệ: " + giaTien);
            return 0;
        }
    }
    
    public static String dinhDangTienTe(double soTien) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(soTien);
    }

    
}
