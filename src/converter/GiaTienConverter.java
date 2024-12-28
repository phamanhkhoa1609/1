/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter;

/**
 *
 * @author ACER
 */
public class GiaTienConverter {
    
    public static String chuyenDoiTien(Double giaTien ){
        return String.format("%,.0f VNĐ", giaTien);
    }
    
    public static String chuyenDoiPhanTram(Double tiLePhanTram){
        return String.format("%.0f%%", tiLePhanTram);
       }
    
}
