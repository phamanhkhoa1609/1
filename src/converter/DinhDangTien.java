/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter;

import java.text.DecimalFormat;

/**
 *
 * @author ADMIN
 */
public class DinhDangTien {
    // Phương thức chuyển đổi giá thành định dạng chuỗi
    public static String chuyenSangVND(double amount) {
        // Sử dụng DecimalFormat để định dạng số
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount) + " VNĐ";
    }
    
    public static String chuyenSangVNDKetCa(double amount) {
        // Sử dụng DecimalFormat để định dạng số
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount) + " VND";
    }
}
