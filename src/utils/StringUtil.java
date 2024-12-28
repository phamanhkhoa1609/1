/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.JOptionPane;

/**
 *
 * @author ACER
 */
public class StringUtil {
    
    public static boolean isEmpty(String data){
        if(!data.isEmpty()){
            return true;
        }
        return false;
    }
    
   public static boolean rangBuocGia(String data) {
    try {
        Double gia = Double.parseDouble(data);
        if (gia <= 0) {
            JOptionPane.showMessageDialog(null, "Giá phải lớn hơn 0 (không cho phép giá 0 hoặc số âm)");
            return false;
        }
        return true;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Giá phải là số");
        return false;
        }
    }
   
   public static boolean rangBuocSoLuong(int soLuong){
       try {
           if(soLuong <= 0){
                JOptionPane.showMessageDialog(null, "Số lượng phải phải lớn hơn 0");
                return false;
           }
           
       } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên, không được nhập chữ");
            return false;
       }
       return true;
   }

    
}
