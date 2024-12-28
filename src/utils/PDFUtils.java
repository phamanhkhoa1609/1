/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author ACER
 */
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public class PDFUtils {
    public static Paragraph createTitle(String text) {
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Paragraph title = new Paragraph(text);
        title.setFont(titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        return title;
    }
}

