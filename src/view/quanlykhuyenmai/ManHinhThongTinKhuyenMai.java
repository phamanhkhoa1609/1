/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.quanlykhuyenmai;

import connectDB.ConnectJDBC;
import java.awt.Color;


import java.time.LocalDateTime;

import com.toedter.calendar.JDateChooser;


import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.MonAnKhuyenMai_DAO;
import dao.MonAn_DAO;
import dao.KhuyenMaiKhachHang_DAO;

import modal.KhuyenMai;
import modal.KhachHang;
import modal.MonAn;
import modal.MonAnKhuyenMai;
import modal.KhuyenMaiKhachHang;



import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.time.Instant;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JComboBox;

import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import modal.QuanLy;

import com.toedter.calendar.JDateChooser;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static view.quanlykhuyenmai.ManHinhTaoKhuyenMai.removeDiacritics;

/**
 *
 * @author ADMIN
 */
public class ManHinhThongTinKhuyenMai extends javax.swing.JPanel {
    
private DefaultTableModel tableModel;    

private MonAn_DAO monAnDAO;
private KhachHang_DAO khachHangDAO;
private KhuyenMai_DAO khuyenMaiDAO;
private MonAnKhuyenMai_DAO monAnKhuyenMaiDAO;
private KhuyenMaiKhachHang_DAO khuyenMaiKhachHangDAO;

Connection connection; 
    /**
     * Creates new form ManHinhTimKiemKhuyenMai
     */

    public ManHinhThongTinKhuyenMai() {
        
        
        connection = ConnectJDBC.getConnection();
        this.khuyenMaiKhachHangDAO = new KhuyenMaiKhachHang_DAO();
        this.khuyenMaiDAO = new KhuyenMai_DAO();
        this.monAnKhuyenMaiDAO = new MonAnKhuyenMai_DAO();
        this.monAnDAO = new MonAn_DAO();
        this.khachHangDAO = new KhachHang_DAO();
        initComponents();
        
        
        
        // Hiển thị dữ liệu lên bảng
        try {
            List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiDuy2();

            // Lấy 5 dòng đầu tiên từ danh sách khuyến mãi
            List<KhuyenMai> top5KhuyenMai = danhSachKhuyenMai.subList(0, Math.min(5, danhSachKhuyenMai.size()));

            hienThiLenBang(top5KhuyenMai);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        jTextFieldmaKhuyenMai.setText("KM"); // Đặt giá trị mặc định là "KM"
        jTextFieldmaKhuyenMai.setCaretPosition(2); // Đặt con trỏ ở vị trí sau "KM"

        // Áp dụng DocumentFilter trực tiếp
        ((AbstractDocument) jTextFieldmaKhuyenMai.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidInput(string, fb)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidInput(text, fb)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                // Không cho phép xóa ký tự "KM"
                if (offset >= 2) {
                    super.remove(fb, offset, length);
                }
            }

            private boolean isValidInput(String input, FilterBypass fb) {
                // Kiểm tra nếu chuỗi nhập vào chỉ chứa số
                String currentText;
                try {
                    currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                } catch (BadLocationException e) {
                    return false;
                }

                String newText = currentText + input;

                // Kiểm tra độ dài của chuỗi sau "KM"
                if (newText.length() > 5) { // "KM" + 3 số => tổng cộng 5
                    return false;
                }

                return input.matches("[0-9]*"); // Chỉ cho phép số
            }
        });

        // Thêm jTextField vào layout của JPanel
        this.add(jTextFieldmaKhuyenMai); // Thêm vào JPanel (có thể cần điều chỉnh vị trí)


        // Áp dụng DocumentFilter trực tiếp cho giaTriKhuyenMai
        ((AbstractDocument) jTextFieldgiaTriKhuyenMai.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidInput(string, fb)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidInput(text, fb)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String input, FilterBypass fb) {
                // Kiểm tra nếu chuỗi nhập vào chỉ chứa số
                String currentText;
                try {
                    currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                } catch (BadLocationException e) {
                    return false;
                }

                String newText = currentText + input;

                // Kiểm tra độ dài không quá 12 ký tự (có thể điều chỉnh)
                if (newText.length() > 12) {
                    return false;
                }

                return newText.matches("\\d*"); // Chỉ cho phép số
            }
        });

        // Áp dụng DocumentFilter cho soLuongToiThieu
        ((AbstractDocument) jTextFieldsoLuongToiThieu.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidInput(string, fb)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidInput(text, fb)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String input, FilterBypass fb) {
                String currentText;
                try {
                    currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                } catch (BadLocationException e) {
                    return false;
                }

                String newText = currentText + input;

                if (newText.length() > 10) {
                    return false;
                }

                return newText.matches("\\d*");
            }
        });

        // Áp dụng DocumentFilter cho diemYeuCau
        ((AbstractDocument) jTextFielddiemYeuCau.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidInput(string, fb)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidInput(text, fb)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String input, FilterBypass fb) {
                String currentText;
                try {
                    currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                } catch (BadLocationException e) {
                    return false;
                }

                String newText = currentText + input;

                if (newText.length() > 10) {
                    return false;
                }

                return newText.matches("\\d*");
            }
        });

        // Thêm các JTextField vào layout của JPanel
        this.add(jTextFieldgiaTriKhuyenMai);
        this.add(jTextFieldsoLuongToiThieu);
        this.add(jTextFielddiemYeuCau);
       
        initComboBoxes(); // Gọi phương thức khởi tạo JComboBox
        
        
        jTabledanhSachKhuyenMai.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jTabledanhSachKhuyenMaiMouseClicked(evt);
            }
        });
        
        
        
        // Load loại trạng thái khuyến mãi đã hết hạn chưa cũng như khuyến mãi đã hết số lượng chưa                       
        try {
            // Cập nhật trạng thái khuyến mãi
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();


            // Reload danh sách áp dụng cho tất cả các khuyến mãi
            reloadKhuyenMaiKhachHang();

          

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        jTextFieldkhachHang.setEnabled(false);
        
    }
    

        


    private void jTabledanhSachKhuyenMaiMouseClicked(java.awt.event.MouseEvent evt) {                                           
        // Lấy vị trí dòng được chọn
        int row = jTabledanhSachKhuyenMai.getSelectedRow();

        if (row != -1) { // Kiểm tra nếu có dòng nào được chọn
            // Lấy dữ liệu từ bảng
            String maKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 0).toString();
            String tenKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 1).toString();
            String moTaKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 2).toString();
            LocalDateTime ngayBatDau = (LocalDateTime) jTabledanhSachKhuyenMai.getValueAt(row, 3);
            LocalDateTime ngayKetThuc = (LocalDateTime) jTabledanhSachKhuyenMai.getValueAt(row, 4);
            String trangThai = jTabledanhSachKhuyenMai.getValueAt(row, 5).toString();



            //double giaTriKhuyenMai = Double.parseDouble(jTabledanhSachKhuyenMai.getValueAt(row, 6).toString());

            // Lấy giá trị khuyến mãi
            String giaTriKhuyenMaiStr = jTabledanhSachKhuyenMai.getValueAt(row, 6).toString();

            // Loại bỏ tất cả ký tự không phải số
            String giaTriKhuyenMaiOnlyNumbers = giaTriKhuyenMaiStr.replaceAll("[^0-9.]", ""); // Giữ lại số và dấu chấm

            // Chuyển đổi sang số thực (double) và sau đó sang số nguyên (int)
            double giaTriKhuyenMaiDouble = Double.parseDouble(giaTriKhuyenMaiOnlyNumbers);
            int giaTriKhuyenMai = (int) giaTriKhuyenMaiDouble; // Chuyển đổi sang số nguyên


            String loaiKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 7).toString();
            double diemYeuCau = Double.parseDouble(jTabledanhSachKhuyenMai.getValueAt(row, 8).toString());
            Integer soLuongToiThieu = Integer.parseInt(jTabledanhSachKhuyenMai.getValueAt(row, 9).toString());

            // Tạo đối tượng DecimalFormat để định dạng số
            DecimalFormat df = new DecimalFormat("#.##"); // Định dạng với 2 chữ số thập phân

            // Tạm thời vô hiệu hóa DocumentFilter
            ((AbstractDocument) jTextFieldmaKhuyenMai.getDocument()).setDocumentFilter(null);

            // Hiển thị dữ liệu lên các trường tương ứng
            jTextFieldmaKhuyenMai.setText(maKhuyenMai);
            jTextFieldtenKhuyenMai.setText(tenKhuyenMai);
            jTextFieldmoTa.setText(moTaKhuyenMai);
            jDateChooserngayBatDau.setDate(java.sql.Date.valueOf(ngayBatDau.toLocalDate())); // Chuyển đổi LocalDateTime sang java.sql.Date
            jDateChooserngayKetThuc.setDate(java.sql.Date.valueOf(ngayKetThuc.toLocalDate())); // Chuyển đổi LocalDateTime sang java.sql.Date
            jComboBoxtrangThai.setSelectedItem(trangThai);

            // Thiết lập giá trị vào JTextField với định dạng
            //jTextFieldgiaTriKhuyenMai.setText(df.format(giaTriKhuyenMai));

            // Thiết lập giá trị vào JTextField với định dạng
            jTextFieldgiaTriKhuyenMai.setText(String.valueOf(giaTriKhuyenMai)); // Chỉ hiển thị số nguyên


            jComboBoxloaiKhuyenMai.setSelectedItem(loaiKhuyenMai);
            jTextFielddiemYeuCau.setText(df.format(diemYeuCau));
            jTextFieldsoLuongToiThieu.setText(String.valueOf(soLuongToiThieu));

            // Bật lại DocumentFilter
            ((AbstractDocument) jTextFieldmaKhuyenMai.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string != null && isValidInput(string, fb)) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text != null && isValidInput(text, fb)) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                    // Không cho phép xóa ký tự "KM"
                    if (offset >= 2) {
                        super.remove(fb, offset, length);
                    }
                }

                private boolean isValidInput(String input, FilterBypass fb) {
                    // Kiểm tra nếu chuỗi nhập vào chỉ chứa số
                    String currentText;
                    try {
                        currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    } catch (BadLocationException e) {
                        return false;
                    }

                    String newText = currentText + input;

                    // Kiểm tra độ dài của chuỗi sau "KM"
                    if (newText.length() > 5) { // "KM" + 3 số => tổng cộng 5
                        return false;
                    }

                    return input.matches("[0-9]*"); // Chỉ cho phép số
                }
            });
        }
    }
    
    //-----------------------------------//---------------------------------------------//   
    //-----------------------------------//---------------------------------------------//


    //Lấy 5 dòng đầu tiên từ danh sách khuyến mãi
    private void hienThiLenBang(List<KhuyenMai> danhSachKhuyenMai) {
        // Xóa tất cả dữ liệu cũ trong bảng
        DefaultTableModel model = (DefaultTableModel) jTabledanhSachKhuyenMai.getModel();
        model.setRowCount(0); // Xóa tất cả các hàng hiện có trong bảng

        // Thêm dữ liệu từ danh sách khuyến mãi vào bảng
        for (KhuyenMai km : danhSachKhuyenMai) {
            String maKhuyenMai = km.getMaKhuyenMai();
            String tenKhuyenMai = km.getTenKhuyenMai();
            String moTaKhuyenMai = km.getMoTaKhuyenMai();
            LocalDateTime ngayBatDau = km.getNgayBatDau();
            LocalDateTime ngayKetThuc = km.getNgayKetThuc();
            String trangThai = km.getTrangThai();
            double giaTriKhuyenMai = km.getGiaTriKhuyenMai();
            String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
            double diemYeuCau = km.getDiemYeuCau();
            Integer soLuongToiThieu = km.getSoLuongToiThieu();

            // Định dạng giá trị khuyến mãi với đơn vị
            String giaTriKhuyenMaiHienThi;
            if (loaiKhuyenMai.equals("tien")) {
                giaTriKhuyenMaiHienThi = String.format("%.0f VNĐ", giaTriKhuyenMai);
            } else if (loaiKhuyenMai.equals("phan tram") || loaiKhuyenMai.equals("mon an")) {
                giaTriKhuyenMaiHienThi = String.format("%.0f %%", giaTriKhuyenMai);
            } else {
                giaTriKhuyenMaiHienThi = String.format("%.0f", giaTriKhuyenMai); // Định dạng cho các loại khác
            }

            // Thêm một hàng mới vào bảng
            model.addRow(new Object[]{
                maKhuyenMai,
                tenKhuyenMai,
                moTaKhuyenMai,
                ngayBatDau,
                ngayKetThuc,
                trangThai,
                giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
                km.getLoaiKhuyenMai(), // Giữ nguyên loại khuyến mãi gốc
                diemYeuCau,
                soLuongToiThieu
            });
        }        
    } 

    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTimKiem = new javax.swing.JPanel();
        jLabelmaBan = new javax.swing.JLabel();
        jLabelloaiKhuyenMai = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        jCheckBoxmaKhuyenMai = new javax.swing.JCheckBox();
        jCheckBoxloaiKhuyenMai = new javax.swing.JCheckBox();
        jLabeltrangThai = new javax.swing.JLabel();
        jCheckBoxtrangThai = new javax.swing.JCheckBox();
        jComboBoxloaiKhuyenMai = new javax.swing.JComboBox<>();
        jComboBoxtrangThai = new javax.swing.JComboBox<>();
        jTextFieldmaKhuyenMai = new javax.swing.JTextField();
        jLabelngayBatDau = new javax.swing.JLabel();
        jDateChooserngayBatDau = new com.toedter.calendar.JDateChooser();
        jLabelngayKetThuc = new javax.swing.JLabel();
        jDateChooserngayKetThuc = new com.toedter.calendar.JDateChooser();
        jTextFieldtenKhuyenMai = new javax.swing.JTextField();
        jCheckBoxtenKhuyenMai = new javax.swing.JCheckBox();
        jTextFieldmoTa = new javax.swing.JTextField();
        jCheckBoxmoTa = new javax.swing.JCheckBox();
        jLabelmoTa = new javax.swing.JLabel();
        jCheckBoxngayKetThuc = new javax.swing.JCheckBox();
        jCheckBoxngayBatDau = new javax.swing.JCheckBox();
        jLabeldiemYeuCau = new javax.swing.JLabel();
        jCheckBoxdiemYeuCau = new javax.swing.JCheckBox();
        jLabeltenKhuyenMai = new javax.swing.JLabel();
        jLabelsoLuongToiThieu = new javax.swing.JLabel();
        jTextFieldsoLuongToiThieu = new javax.swing.JTextField();
        jCheckBoxsoLuongToiThieu = new javax.swing.JCheckBox();
        jLabelgiaTriKhuyenMai = new javax.swing.JLabel();
        jCheckBoxgiaTriKhuyenMai = new javax.swing.JCheckBox();
        btnSua = new javax.swing.JButton();
        btnDungKhuyenMai = new javax.swing.JButton();
        jTextFielddiemYeuCau = new javax.swing.JTextField();
        jTextFieldgiaTriKhuyenMai = new javax.swing.JTextField();
        jButtonxoaTrang = new javax.swing.JButton();
        jLabelsoDienThoai = new javax.swing.JLabel();
        jTextFieldsoDienThoai = new javax.swing.JTextField();
        jCheckBoxsoDienThoai = new javax.swing.JCheckBox();
        jLabelkhachHang1 = new javax.swing.JLabel();
        jTextFieldkhachHang = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabledanhSachKhuyenMai = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButtonReload = new javax.swing.JButton();

        jPanelTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTimKiem.setPreferredSize(new java.awt.Dimension(677, 150));

        jLabelmaBan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaBan.setText("Mã:");

        jLabelloaiKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelloaiKhuyenMai.setText("Loại:");

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search (1) (1).png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jCheckBoxmaKhuyenMai.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaKhuyenMai.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaKhuyenMai.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxmaKhuyenMaiActionPerformed(evt);
            }
        });

        jCheckBoxloaiKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxloaiKhuyenMaiActionPerformed(evt);
            }
        });

        jLabeltrangThai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeltrangThai.setText("Trạng thái:");

        jCheckBoxtrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxtrangThaiActionPerformed(evt);
            }
        });

        jComboBoxloaiKhuyenMai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Loai km--" }));
        jComboBoxloaiKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxloaiKhuyenMaiActionPerformed(evt);
            }
        });

        jComboBoxtrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Trang thai--" }));
        jComboBoxtrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtrangThaiActionPerformed(evt);
            }
        });

        jTextFieldmaKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmaKhuyenMaiActionPerformed(evt);
            }
        });

        jLabelngayBatDau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelngayBatDau.setText("Ngày bắt đầu:");

        jLabelngayKetThuc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelngayKetThuc.setText("Ngày kết thúc:");

        jTextFieldtenKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtenKhuyenMaiActionPerformed(evt);
            }
        });

        jCheckBoxtenKhuyenMai.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxtenKhuyenMai.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxtenKhuyenMai.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxtenKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxtenKhuyenMaiActionPerformed(evt);
            }
        });

        jTextFieldmoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmoTaActionPerformed(evt);
            }
        });

        jCheckBoxmoTa.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmoTa.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmoTa.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxmoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxmoTaActionPerformed(evt);
            }
        });

        jLabelmoTa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmoTa.setText("Mô tả:");

        jCheckBoxngayKetThuc.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayKetThuc.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayKetThuc.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxngayKetThucActionPerformed(evt);
            }
        });

        jCheckBoxngayBatDau.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayBatDau.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayBatDau.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxngayBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxngayBatDauActionPerformed(evt);
            }
        });

        jLabeldiemYeuCau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeldiemYeuCau.setText("Điểm:");

        jCheckBoxdiemYeuCau.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxdiemYeuCau.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxdiemYeuCau.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxdiemYeuCau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxdiemYeuCauActionPerformed(evt);
            }
        });

        jLabeltenKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeltenKhuyenMai.setText("Tên:");

        jLabelsoLuongToiThieu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelsoLuongToiThieu.setText("Số lượng:");

        jTextFieldsoLuongToiThieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsoLuongToiThieuActionPerformed(evt);
            }
        });

        jCheckBoxsoLuongToiThieu.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoLuongToiThieu.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoLuongToiThieu.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoLuongToiThieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxsoLuongToiThieuActionPerformed(evt);
            }
        });

        jLabelgiaTriKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelgiaTriKhuyenMai.setText("Giá trị:");

        jCheckBoxgiaTriKhuyenMai.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxgiaTriKhuyenMai.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxgiaTriKhuyenMai.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxgiaTriKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxgiaTriKhuyenMaiActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pen (1).png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnDungKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDungKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew2.png"))); // NOI18N
        btnDungKhuyenMai.setText("Dừng khuyến mãi");
        btnDungKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDungKhuyenMaiActionPerformed(evt);
            }
        });

        jTextFielddiemYeuCau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFielddiemYeuCauActionPerformed(evt);
            }
        });

        jButtonxoaTrang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonxoaTrang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        jButtonxoaTrang.setText("Xóa trắng");
        jButtonxoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonxoaTrangActionPerformed(evt);
            }
        });

        jLabelsoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelsoDienThoai.setText("SĐT:");

        jTextFieldsoDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsoDienThoaiActionPerformed(evt);
            }
        });

        jCheckBoxsoDienThoai.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoDienThoai.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoDienThoai.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxsoDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxsoDienThoaiActionPerformed(evt);
            }
        });

        jLabelkhachHang1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelkhachHang1.setText("Khách Hàng:");

        jTextFieldkhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldkhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTimKiemLayout = new javax.swing.GroupLayout(jPanelTimKiem);
        jPanelTimKiem.setLayout(jPanelTimKiemLayout);
        jPanelTimKiemLayout.setHorizontalGroup(
            jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelloaiKhuyenMai)
                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelsoLuongToiThieu, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelmaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBoxloaiKhuyenMai, 0, 126, Short.MAX_VALUE)
                            .addComponent(jTextFieldsoLuongToiThieu)
                            .addComponent(jTextFieldmaKhuyenMai, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxloaiKhuyenMai)
                            .addComponent(jCheckBoxsoLuongToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxmaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addComponent(btnDungKhuyenMai)
                        .addGap(2, 2, 2)))
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelmoTa)
                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabeltrangThai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                .addComponent(jLabeltenKhuyenMai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldtenKhuyenMai)
                            .addComponent(jComboBoxtrangThai, 0, 225, Short.MAX_VALUE)
                            .addComponent(jTextFieldmoTa)))
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCheckBoxtrangThai)
                                .addComponent(jCheckBoxmoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBoxtenKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelgiaTriKhuyenMai)
                                .addComponent(jLabeldiemYeuCau)
                                .addComponent(jLabelsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldgiaTriKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(jTextFielddiemYeuCau)
                            .addComponent(jTextFieldsoDienThoai, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                        .addComponent(jCheckBoxdiemYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabelngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelTimKiemLayout.createSequentialGroup()
                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jCheckBoxsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jCheckBoxgiaTriKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabelngayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                            .addComponent(jLabelkhachHang1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jDateChooserngayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldkhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jDateChooserngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jButtonxoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(13, 13, 13)))
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxngayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanelTimKiemLayout.setVerticalGroup(
            jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabeltenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jCheckBoxsoLuongToiThieu, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                                .addComponent(jLabelsoLuongToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldsoLuongToiThieu))
                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelgiaTriKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheckBoxgiaTriKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                                .addComponent(jTextFieldgiaTriKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelngayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                                .addComponent(jTextFielddiemYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabelmaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextFieldmaKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jCheckBoxmaKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                                        .addComponent(jLabelmoTa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBoxloaiKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabelloaiKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jCheckBoxloaiKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(0, 0, Short.MAX_VALUE)))
                                                .addGap(81, 81, 81)))
                                        .addGap(32, 32, 32))
                                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                                    .addGap(1, 1, 1)
                                                    .addComponent(jLabeldiemYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jCheckBoxdiemYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabelngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jDateChooserngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jCheckBoxngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabeltrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(116, 116, 116)))
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDungKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonxoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createSequentialGroup()
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jCheckBoxtenKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldtenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxtrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                                    .addComponent(jCheckBoxtrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldmoTa)
                                    .addComponent(jCheckBoxmoTa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooserngayBatDau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBoxngayBatDau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(116, 116, 116)
                                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBoxsoDienThoai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextFieldsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelkhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldkhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(98, 98, 98))))
        );

        jTabledanhSachKhuyenMai.setAutoCreateRowSorter(true);
        jTabledanhSachKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã khuyến mãi", "Tên khuyễn mãi", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Giảm giá", "Loại", "Điểm yêu cầu", "Số lượng tối thiểu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabledanhSachKhuyenMai.setToolTipText("");
        jTabledanhSachKhuyenMai.setRowHeight(40);
        jScrollPane3.setViewportView(jTabledanhSachKhuyenMai);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Danh Sách Khuyễn Mãi");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButtonReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        jButtonReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jButtonReload, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(292, 292, 292)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addGap(474, 474, 474))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonReload, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelTimKiem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1361, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    //Chức năng hiện dữ liệu lên bảng cho chức năng button Tìm kiếm

    private void updateTableWithResults(List<KhuyenMai> danhSachKhuyenMai) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã khuyến mãi", "Tên khuyến mãi", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Giá trị khuyến mãi", "Loại khuyến mãi", "Điểm yêu cầu", "Số lượng tối thiểu"});

        if (danhSachKhuyenMai.isEmpty()) {
            // Nếu không tìm thấy kết quả, thêm dòng với từng từ trong thông báo
            String[] messageWords = {"", "", "", "", "", "", "", "", "", ""};
            model.addRow(messageWords);

            // Hiển thị thông báo cho người dùng
            JOptionPane.showMessageDialog(null, "Không tìm thấy khuyến mãi phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (KhuyenMai km : danhSachKhuyenMai) {
                double giaTriKhuyenMai = km.getGiaTriKhuyenMai();
                String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
                String giaTriKhuyenMaiHienThi;

                // Định dạng giá trị khuyến mãi với đơn vị
                if (loaiKhuyenMai.equals("tien")) {
                    giaTriKhuyenMaiHienThi = String.format("%.0f VNĐ", giaTriKhuyenMai);
                } else if (loaiKhuyenMai.equals("phan tram") || loaiKhuyenMai.equals("mon an")) {
                    giaTriKhuyenMaiHienThi = String.format("%.0f %%", giaTriKhuyenMai);
                } else {
                    giaTriKhuyenMaiHienThi = String.format("%.0f", giaTriKhuyenMai); // Định dạng cho các loại khác
                }

                Object[] row = new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getMoTaKhuyenMai(),
                    km.getNgayBatDau(),
                    km.getNgayKetThuc(),
                    km.getTrangThai(),
                    giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
                    km.getLoaiKhuyenMai(), // Giữ nguyên loại khuyến mãi gốc
                    km.getDiemYeuCau(),
                    km.getSoLuongToiThieu()
                };
                model.addRow(row);
            }
        }

        jTabledanhSachKhuyenMai.setModel(model);

        // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
        flashTable(jTabledanhSachKhuyenMai);
    }
    
    
    
    private void initComboBoxes() {
        KhuyenMai_DAO dao = new KhuyenMai_DAO();
        try {
            List<String> loaiKhuyenMaiList = dao.layDanhSachLoaiKhuyenMaiDuy();
            for (String loai : loaiKhuyenMaiList) {
                jComboBoxloaiKhuyenMai.addItem(loai);
            }

            List<String> trangThaiList = dao.layDanhSachTrangThaiDuy();
            for (String trangThai : trangThaiList) {
                jComboBoxtrangThai.addItem(trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
  
    // Lấy số điện thoại từ trường nhập
    //String soDienThoai = jTextFieldsoDienThoai.getText().trim(); // Giả sử bạn có một jTextField cho số điện thoại

    
    String maKhuyenMai = jCheckBoxmaKhuyenMai.isSelected() ? jTextFieldmaKhuyenMai.getText() : null;
    String tenKhuyenMai = jCheckBoxtenKhuyenMai.isSelected() ? jTextFieldtenKhuyenMai.getText() : null;
    String moTaKhuyenMai = jCheckBoxmoTa.isSelected() ? jTextFieldmoTa.getText() : null;
    Date ngayBatDau = jCheckBoxngayBatDau.isSelected() ? jDateChooserngayBatDau.getDate() : null;
    Date ngayKetThuc = jCheckBoxngayKetThuc.isSelected() ? jDateChooserngayKetThuc.getDate() : null;
    String trangThai = jCheckBoxtrangThai.isSelected() ? (String) jComboBoxtrangThai.getSelectedItem() : null;
//    Double diemYeuCau = jCheckBoxdiemYeuCau.isSelected() ? Double.valueOf(jTextFielddiemYeuCau.getText()) : null;
    

    // Khai báo các biến cho giaTriKhuyenMai và soLuongToiThieu
    Double giaTriKhuyenMai = null;
    Integer soLuongToiThieu = null;

    // Chuyển đổi giaTriKhuyenMai từ String sang Double
    if (jCheckBoxgiaTriKhuyenMai.isSelected()) {
        String giaTriKhuyenMaiText = jTextFieldgiaTriKhuyenMai.getText().trim();
        if (!giaTriKhuyenMaiText.isEmpty()) {
            try {
                giaTriKhuyenMai = Double.valueOf(giaTriKhuyenMaiText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập giá trị khuyến mãi hợp lệ!");
                return; // Dừng hàm nếu không hợp lệ
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập giá trị khuyến mãi!");
            return; // Dừng hàm nếu không hợp lệ
        }
    }

    // Chuyển đổi soLuongToiThieu từ String sang Integer
    if (jCheckBoxsoLuongToiThieu.isSelected()) {
        String soLuongToiThieuText = jTextFieldsoLuongToiThieu.getText().trim();
        if (!soLuongToiThieuText.isEmpty()) {
            try {
                soLuongToiThieu = Integer.valueOf(soLuongToiThieuText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng tối thiểu hợp lệ!");
                return; // Dừng hàm nếu không hợp lệ
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng tối thiểu!");
            return; // Dừng hàm nếu không hợp lệ
        }
    }
    
    

    Double diemYeuCau = null;
    if (jCheckBoxdiemYeuCau.isSelected()) {
        String diemYeuCauText = jTextFielddiemYeuCau.getText().trim();
        if (!diemYeuCauText.isEmpty()) {
            try {
                diemYeuCau = Double.valueOf(diemYeuCauText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm yêu cầu hợp lệ!");
                return; // Dừng hàm nếu không hợp lệ
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm yêu cầu!");
            return; // Dừng hàm nếu không hợp lệ
        }
    }
    
    

    String loaiKhuyenMai = jCheckBoxloaiKhuyenMai.isSelected() ? (String) jComboBoxloaiKhuyenMai.getSelectedItem() : null;
    
    
    // Kiểm tra cho combobox loại km
        if (jCheckBoxloaiKhuyenMai.isSelected() && jComboBoxloaiKhuyenMai.getSelectedItem().equals("--Loai km--")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn loại khuyến mãi!");
            jCheckBoxloaiKhuyenMai.setSelected(false);
        }

        // Kiểm tra cho combobox trạng thái
        if (jCheckBoxtrangThai.isSelected() && jComboBoxtrangThai.getSelectedItem().equals("--Trang thai--")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái!");
            jCheckBoxtrangThai.setSelected(false);
        }
    

        if (jCheckBoxmoTa.isSelected() && moTaKhuyenMai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mô tả khuyến mãi!");
            return; // Dừng hàm nếu không hợp lệ
        }

        if (jCheckBoxngayBatDau.isSelected() && ngayBatDau == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu!");
            return; // Dừng hàm nếu không hợp lệ
        }

        if (jCheckBoxngayKetThuc.isSelected() && ngayKetThuc == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày kết thúc!");
            return; // Dừng hàm nếu không hợp lệ
        }

        // Kiểm tra cho tên khuyến mãi
        if (jCheckBoxtenKhuyenMai.isSelected() && tenKhuyenMai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên khuyến mãi!");
            return; // Dừng hàm nếu không hợp lệ
        }
        
        // Kiểm tra cho giá trị khuyến mãi
        if (jCheckBoxgiaTriKhuyenMai.isSelected() && giaTriKhuyenMai == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập giá trị khuyến mãi!");
            return; // Dừng hàm nếu không hợp lệ
        }

        // Kiểm tra cho số lượng tối thiểu
        if (jCheckBoxsoLuongToiThieu.isSelected() && soLuongToiThieu == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng tối thiểu!");
            return; // Dừng hàm nếu không hợp lệ
        }
   
        // Kiểm tra cho số điện thoại
        String soDienThoai = jCheckBoxsoDienThoai.isSelected() ? jTextFieldsoDienThoai.getText().trim() : null;
        if (jCheckBoxsoDienThoai.isSelected() && (soDienThoai == null || !soDienThoai.matches("\\d{10}"))) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại hợp lệ (10 số)!");
            return; // Dừng hàm nếu không hợp lệ
        }

        KhuyenMai_DAO dao = new KhuyenMai_DAO();
        try {
        List<KhuyenMai> danhSachKhuyenMai;
        if (jCheckBoxsoDienThoai.isSelected()) {
            danhSachKhuyenMai = dao.timKiemKhuyenMaiTheoSDT(soDienThoai, maKhuyenMai, tenKhuyenMai, 
                                                            moTaKhuyenMai, ngayBatDau, ngayKetThuc, 
                                                            trangThai, diemYeuCau, loaiKhuyenMai, 
                                                            giaTriKhuyenMai, soLuongToiThieu);
        } else {
            danhSachKhuyenMai = dao.timKiemKhuyenMaiDuy(maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, 
                                                         ngayBatDau, ngayKetThuc, trangThai, 
                                                         diemYeuCau, loaiKhuyenMai, 
                                                         giaTriKhuyenMai, soLuongToiThieu);
        }
        updateTableWithResults(danhSachKhuyenMai);
        } catch (SQLException e) {
            e.printStackTrace();
        }
               
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void jCheckBoxmaKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxmaKhuyenMaiActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckBoxmaKhuyenMaiActionPerformed

    private void jCheckBoxloaiKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxloaiKhuyenMaiActionPerformed
   
    }//GEN-LAST:event_jCheckBoxloaiKhuyenMaiActionPerformed

    private void jCheckBoxtrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxtrangThaiActionPerformed

    }//GEN-LAST:event_jCheckBoxtrangThaiActionPerformed

    private void jComboBoxloaiKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxloaiKhuyenMaiActionPerformed
        // Kiểm tra nếu người dùng chọn phần tử khác "--Tầng--" thì checkbox sẽ được chọn
        if (!jComboBoxloaiKhuyenMai.getSelectedItem().equals("--Loai km--")) {
            jCheckBoxloaiKhuyenMai.setSelected(true);  // Tự động chọn checkbox
        } else {
            jCheckBoxloaiKhuyenMai.setSelected(false); // Nếu chọn "--Loai ban--", bỏ chọn checkbox
        }
    }//GEN-LAST:event_jComboBoxloaiKhuyenMaiActionPerformed

    private void jComboBoxtrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtrangThaiActionPerformed
        // TODO add your handling code here:
        // Kiểm tra nếu người dùng chọn phần tử khác "--Tầng--" thì checkbox sẽ được chọn
        if (!jComboBoxtrangThai.getSelectedItem().equals("--Trang thai--")) {
            jCheckBoxtrangThai.setSelected(true);  // Tự động chọn checkbox
        } else {
            jCheckBoxtrangThai.setSelected(false); // Nếu chọn "--Tầng--", bỏ chọn checkbox
        }

    }//GEN-LAST:event_jComboBoxtrangThaiActionPerformed

    private void jTextFieldmaKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmaKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmaKhuyenMaiActionPerformed

    private void jButtonReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReloadActionPerformed
    
        resetFields();   
        
        try {
            // Cập nhật trạng thái khuyến mãi
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();

            // Hiển thị lại danh sách khuyến mãi
            hienThiDanhSachKhuyenMai();

            JOptionPane.showMessageDialog(this, "Vui lòng chờ giây lát !");

            // Reload danh sách áp dụng cho tất cả các khuyến mãi
            reloadKhuyenMaiKhachHang();


            // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
            flashTable(jTabledanhSachKhuyenMai);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonReloadActionPerformed

    private void jTextFieldtenKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtenKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtenKhuyenMaiActionPerformed

    private void jCheckBoxtenKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxtenKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxtenKhuyenMaiActionPerformed

    private void jTextFieldmoTaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmoTaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmoTaActionPerformed

    private void jCheckBoxmoTaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxmoTaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxmoTaActionPerformed

    private void jCheckBoxngayKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxngayKetThucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxngayKetThucActionPerformed

    private void jCheckBoxngayBatDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxngayBatDauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxngayBatDauActionPerformed

    private void jCheckBoxdiemYeuCauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxdiemYeuCauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxdiemYeuCauActionPerformed

    private void jTextFieldsoLuongToiThieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsoLuongToiThieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldsoLuongToiThieuActionPerformed

    private void jCheckBoxsoLuongToiThieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxsoLuongToiThieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxsoLuongToiThieuActionPerformed

    private void jCheckBoxgiaTriKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxgiaTriKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxgiaTriKhuyenMaiActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
 
                                    
        // Lấy dòng được chọn trong bảng
        int row = jTabledanhSachKhuyenMai.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn một dòng nào trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy mã khuyến mãi để tìm trong bảng
        String maKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 0).toString();

        // Lấy dữ liệu hiện tại từ bảng
        LocalDateTime currentNgayKetThuc = (LocalDateTime) jTabledanhSachKhuyenMai.getValueAt(row, 4);
        LocalDateTime currentNgayBatDau = (LocalDateTime) jTabledanhSachKhuyenMai.getValueAt(row, 3); // Cột 3 là ngày bắt đầu
        String trangThai = standardizeString(jComboBoxtrangThai.getSelectedItem().toString());

        // Kiểm tra trạng thái và ngày hết hạn
        if (currentNgayKetThuc.isBefore(LocalDateTime.now()) && trangThai.equals("expired")) {
            JOptionPane.showMessageDialog(this, "Không được phép sửa khi Khuyến mãi đã hết hạn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu từ các trường nhập
        String tenKhuyenMai = jTextFieldtenKhuyenMai.getText().trim();
        String moTaKhuyenMai = jTextFieldmoTa.getText().trim();
        LocalDateTime ngayKetThuc = jDateChooserngayKetThuc.getDate()
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Kiểm tra ngày hết hạn không trước ngày hôm nay
        if (ngayKetThuc.toLocalDate().isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Ngày hết hạn không được trước ngày hôm nay!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra ngày kết thúc phải sau hoặc bằng ngày bắt đầu
        if (ngayKetThuc.toLocalDate().isBefore(currentNgayBatDau.toLocalDate())) {
            JOptionPane.showMessageDialog(this, "Ngày hết hạn phải sau hoặc bằng ngày bắt đầu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        
        // Lấy ngày bắt đầu từ trường nhập
        LocalDateTime ngayBatDau = jDateChooserngayBatDau.getDate()
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Kiểm tra ngày bắt đầu
        if (ngayBatDau == null || ngayBatDau.toLocalDate().isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải từ ngày hôm nay trở đi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra ngày bắt đầu không được sau ngày kết thúc
        if (ngayBatDau.isAfter(ngayKetThuc)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Xử lý loại khuyến mãi
        String loaiKhuyenMai = removeDiacritics(jComboBoxloaiKhuyenMai.getSelectedItem().toString().trim().toLowerCase());

        // Lấy giá trị khuyến mãi và loại bỏ đơn vị
        String giaTriKhuyenMaiStr = jTabledanhSachKhuyenMai.getValueAt(row, 6).toString();
        giaTriKhuyenMaiStr = giaTriKhuyenMaiStr.replace("%", "").replace("VNĐ", "").trim(); // Loại bỏ dấu phần trăm và VNĐ
        double currentGiaTriKhuyenMai = Double.parseDouble(giaTriKhuyenMaiStr);
        double giaTriKhuyenMai = currentGiaTriKhuyenMai;

        boolean isChanged = false;

        // Lấy giá trị mới từ trường nhập và loại bỏ đơn vị
        String newGiaTriKhuyenMaiStr = jTextFieldgiaTriKhuyenMai.getText().trim();
        newGiaTriKhuyenMaiStr = newGiaTriKhuyenMaiStr.replace("%", "").replace("VNĐ", "").trim(); // Loại bỏ dấu phần trăm và VNĐ

        try {
            if (!newGiaTriKhuyenMaiStr.equals(String.valueOf((int) currentGiaTriKhuyenMai))) {
                double tempValue = Double.parseDouble(newGiaTriKhuyenMaiStr);

                if (loaiKhuyenMai.equals("tien")) {
                    if (tempValue < 1) {
                        JOptionPane.showMessageDialog(this, "Giá trị khuyến mãi phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    giaTriKhuyenMai = tempValue * 1000;
                } else if (loaiKhuyenMai.equals("phan tram") || loaiKhuyenMai.equals("mon an")) {
                    if (tempValue < 1 || tempValue > 100) {
                        JOptionPane.showMessageDialog(this, "Giá trị phải nằm trong khoảng từ 1 đến 100!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    giaTriKhuyenMai = tempValue;
                } else if (loaiKhuyenMai.equals("qua tang")) {
                    JOptionPane.showMessageDialog(this, "Không thể sửa giá trị khuyến mãi cho loại 'Quà tặng'!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                isChanged = true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị hợp lệ cho giá trị khuyến mãi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra các thay đổi khác
        double diemYeuCau = Double.parseDouble(jTextFielddiemYeuCau.getText());
        int soLuongToiThieu = Integer.parseInt(jTextFieldsoLuongToiThieu.getText());

        if (!tenKhuyenMai.equalsIgnoreCase(jTabledanhSachKhuyenMai.getValueAt(row, 1).toString())) isChanged = true;
        if (!moTaKhuyenMai.equalsIgnoreCase(jTabledanhSachKhuyenMai.getValueAt(row, 2).toString()) && !loaiKhuyenMai.equals("mon an")) isChanged = true;
        if (diemYeuCau != Double.parseDouble(jTabledanhSachKhuyenMai.getValueAt(row, 8).toString())) isChanged = true;
        if (soLuongToiThieu != Integer.parseInt(jTabledanhSachKhuyenMai.getValueAt(row, 9).toString())) isChanged = true;
        if (!ngayKetThuc.equals(currentNgayKetThuc)) isChanged = true;
        if (!ngayBatDau.equals(currentNgayKetThuc)) isChanged = true;

        // Thông báo nếu không có thay đổi
        if (!isChanged) {
            JOptionPane.showMessageDialog(this, "Bạn chưa thay đổi thông tin nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Thực hiện cập nhật
        try {
            //khuyenMaiDAO.suaKhuyenMaiDuy(maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayKetThuc, giaTriKhuyenMai, diemYeuCau, soLuongToiThieu);
            khuyenMaiDAO.suaKhuyenMaiDuy(maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, 
                                  ngayBatDau, ngayKetThuc, giaTriKhuyenMai, 
                                  diemYeuCau, soLuongToiThieu);
            if (loaiKhuyenMai.equals("mon an")) {
                float phanTramGiamGia = Float.parseFloat(newGiaTriKhuyenMaiStr);
                int soLuongToiThieuMonAn = Integer.parseInt(jTextFieldsoLuongToiThieu.getText());
                monAnKhuyenMaiDAO.updateMonAnKhuyenMaiSuaDuy(maKhuyenMai, phanTramGiamGia, soLuongToiThieuMonAn);
            }

            KhuyenMai khuyenMaiDaSua = khuyenMaiDAO.layKhuyenMaiTheoMaDuy(maKhuyenMai);
            updateTableWithSingleResult(khuyenMaiDaSua);

            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khuyến mãi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin khuyến mãi: " + e.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
        }

        // Đặt lại trường nhập
        resetFields();
    }//GEN-LAST:event_btnSuaActionPerformed

    //-----------------------------------------------//------------------------------------//
    
    public static String standardizeString(String input) {
        return removeDiacritics(input).toLowerCase().trim();
    }

        // Hàm để loại bỏ dấu
    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }
    
    
    

    // chức năng chỉ hiển thị 1 dòng mà khuyến mãi mới vừa sửa __ Chức năng button Sửa
    private void updateTableWithSingleResult(KhuyenMai khuyenMai) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã khuyến mãi", "Tên khuyến mãi", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Giá trị khuyến mãi", "Loại khuyến mãi", "Điểm yêu cầu", "Số lượng tối thiểu"});

        // Định dạng giá trị khuyến mãi với đơn vị
        double giaTriKhuyenMai = khuyenMai.getGiaTriKhuyenMai();
        String loaiKhuyenMai = removeDiacritics(khuyenMai.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
        String giaTriKhuyenMaiHienThi;
        if (loaiKhuyenMai.equals("tien")) {
            giaTriKhuyenMaiHienThi = String.format("%.0f VNĐ", giaTriKhuyenMai);
        } else if (loaiKhuyenMai.equals("phan tram") || loaiKhuyenMai.equals("mon an")) {
            giaTriKhuyenMaiHienThi = String.format("%.0f %%", giaTriKhuyenMai);
        } else {
            giaTriKhuyenMaiHienThi = String.format("%.0f", giaTriKhuyenMai); // Định dạng cho các loại khác
        }

        // Thêm dòng khuyến mãi vừa sửa vào bảng
        Object[] row = new Object[]{
            khuyenMai.getMaKhuyenMai(),
            khuyenMai.getTenKhuyenMai(),
            khuyenMai.getMoTaKhuyenMai(),
            khuyenMai.getNgayBatDau(),
            khuyenMai.getNgayKetThuc(),
            khuyenMai.getTrangThai(),
            giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
            khuyenMai.getLoaiKhuyenMai(), // Giữ nguyên loại khuyến mãi gốc
            khuyenMai.getDiemYeuCau(),
            khuyenMai.getSoLuongToiThieu()
        };
        model.addRow(row);

        jTabledanhSachKhuyenMai.setModel(model);

        // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
        flashTable(jTabledanhSachKhuyenMai);
    }
    
    
    // Không dùng tới
    private void refreshTable(String maKhuyenMai) {
        try {


            // Lấy thông tin khuyến mãi theo mã
            KhuyenMai khuyenMai = khuyenMaiDAO.layKhuyenMaiTheoMaDuy(maKhuyenMai);

            if (khuyenMai != null) {
                // Tạo danh sách chỉ chứa khuyến mãi vừa lấy
                List<KhuyenMai> khuyenMais = new ArrayList<>();
                khuyenMais.add(khuyenMai);

                // Cập nhật mô hình bảng với danh sách khuyến mãi này
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"Mã khuyến mãi", "Tên khuyến mãi", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Giá trị khuyến mãi", "Loại khuyến mãi", "Điểm yêu cầu", "Số lượng tối thiểu"});

                // Thêm thông tin khuyến mãi vào bảng
                Object[] row = new Object[]{
                    khuyenMai.getMaKhuyenMai(),
                    khuyenMai.getTenKhuyenMai(),
                    khuyenMai.getMoTaKhuyenMai(),
                    khuyenMai.getNgayBatDau(),
                    khuyenMai.getNgayKetThuc(),
                    khuyenMai.getTrangThai(),
                    khuyenMai.getGiaTriKhuyenMai(),
                    khuyenMai.getLoaiKhuyenMai(),
                    khuyenMai.getDiemYeuCau(),
                    khuyenMai.getSoLuongToiThieu()
                };
                model.addRow(row);

                // Cập nhật bảng
                jTabledanhSachKhuyenMai.setModel(model);
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy khuyến mãi với mã: " + maKhuyenMai);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi làm mới bảng: " + e.getMessage());
        }
    }
            
    private void btnDungKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDungKhuyenMaiActionPerformed
        // TODO add your handling code here:
        
        // Kiểm tra xem người dùng đã chọn một dòng trong bảng chưa
        int selectedRow = jTabledanhSachKhuyenMai.getSelectedRow();

        if (selectedRow == -1) {
            // Nếu không có dòng nào được chọn, hiển thị thông báo
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để dừng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // Thoát khỏi phương thức
        }

        // Lấy mã khuyến mãi từ dòng đã chọn
        String maKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(selectedRow, 0).toString(); // Giả sử cột 0 là mã khuyến mãi
        String trangThai = jTabledanhSachKhuyenMai.getValueAt(selectedRow, 5).toString(); // Giả sử cột 5 là trạng thái

        // Kiểm tra trạng thái khuyến mãi
        if (trangThai.equalsIgnoreCase("Expired")) {
            JOptionPane.showMessageDialog(this, "Khuyến mãi này đã hết hạn và không thể dừng nữa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // Thoát khỏi phương thức
        }

        // Hiển thị hộp thoại xác nhận
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn dừng khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Nếu người dùng xác nhận, thực hiện cập nhật
            try {
                // Cập nhật trạng thái và ngày kết thúc khuyến mãi
                khuyenMaiDAO.dungKhuyenMaiDuy(maKhuyenMai);
                JOptionPane.showMessageDialog(this, "Khuyến mãi đã được dừng thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                // Cập nhật lại danh sách khuyến mãi
                hienThiDanhSachKhuyenMai();

                // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
                flashTable(jTabledanhSachKhuyenMai);

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi dừng khuyến mãi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDungKhuyenMaiActionPerformed

    private void jTextFielddiemYeuCauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFielddiemYeuCauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFielddiemYeuCauActionPerformed

    private void jButtonxoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonxoaTrangActionPerformed
        // TODO add your handling code here:
        resetFields();        
    }//GEN-LAST:event_jButtonxoaTrangActionPerformed

    private void jTextFieldsoDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsoDienThoaiActionPerformed
        // TODO add your handling code here:
        String soDienThoai = jTextFieldsoDienThoai.getText().trim();
        // Kiểm tra xem số điện thoại có đủ 10 số và chỉ chứa các ký tự số hay không
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số điện thoại (10 số)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            jTextFieldkhachHang.setText("");
            return; 
        }

        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        try {
            KhachHang kh = khachHang_DAO.timKhachHangTheoSDT(soDienThoai);
            if (kh != null) {
                jTextFieldkhachHang.setText(kh.getTenKhachHang());
                jTextFieldkhachHang.setRequestFocusEnabled(true);

            } else {
                JOptionPane.showMessageDialog(this, "Khách hàng chưa có tài khoản!!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                jTextFieldkhachHang.setText("");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTextFieldsoDienThoaiActionPerformed

    private void jCheckBoxsoDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxsoDienThoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxsoDienThoaiActionPerformed

    private void jTextFieldkhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldkhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldkhachHangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDungKhuyenMai;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton jButtonReload;
    private javax.swing.JButton jButtonxoaTrang;
    private javax.swing.JCheckBox jCheckBoxdiemYeuCau;
    private javax.swing.JCheckBox jCheckBoxgiaTriKhuyenMai;
    private javax.swing.JCheckBox jCheckBoxloaiKhuyenMai;
    private javax.swing.JCheckBox jCheckBoxmaKhuyenMai;
    private javax.swing.JCheckBox jCheckBoxmoTa;
    private javax.swing.JCheckBox jCheckBoxngayBatDau;
    private javax.swing.JCheckBox jCheckBoxngayKetThuc;
    private javax.swing.JCheckBox jCheckBoxsoDienThoai;
    private javax.swing.JCheckBox jCheckBoxsoLuongToiThieu;
    private javax.swing.JCheckBox jCheckBoxtenKhuyenMai;
    private javax.swing.JCheckBox jCheckBoxtrangThai;
    private javax.swing.JComboBox<String> jComboBoxloaiKhuyenMai;
    private javax.swing.JComboBox<String> jComboBoxtrangThai;
    private com.toedter.calendar.JDateChooser jDateChooserngayBatDau;
    private com.toedter.calendar.JDateChooser jDateChooserngayKetThuc;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabeldiemYeuCau;
    private javax.swing.JLabel jLabelgiaTriKhuyenMai;
    private javax.swing.JLabel jLabelkhachHang1;
    private javax.swing.JLabel jLabelloaiKhuyenMai;
    private javax.swing.JLabel jLabelmaBan;
    private javax.swing.JLabel jLabelmoTa;
    private javax.swing.JLabel jLabelngayBatDau;
    private javax.swing.JLabel jLabelngayKetThuc;
    private javax.swing.JLabel jLabelsoDienThoai;
    private javax.swing.JLabel jLabelsoLuongToiThieu;
    private javax.swing.JLabel jLabeltenKhuyenMai;
    private javax.swing.JLabel jLabeltrangThai;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelTimKiem;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabledanhSachKhuyenMai;
    private javax.swing.JTextField jTextFielddiemYeuCau;
    private javax.swing.JTextField jTextFieldgiaTriKhuyenMai;
    private javax.swing.JTextField jTextFieldkhachHang;
    private javax.swing.JTextField jTextFieldmaKhuyenMai;
    private javax.swing.JTextField jTextFieldmoTa;
    private javax.swing.JTextField jTextFieldsoDienThoai;
    private javax.swing.JTextField jTextFieldsoLuongToiThieu;
    private javax.swing.JTextField jTextFieldtenKhuyenMai;
    // End of variables declaration//GEN-END:variables


    // Hàm để load và hiển thị dữ liệu khuyến mãi lên bảng
    public void hienThiDanhSachKhuyenMai() {
        try {
            // Cập nhật trạng thái khuyến mãi trước
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();

            // Lấy danh sách khuyến mãi sau khi cập nhật
            List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiDuy2();

            // Tạo model cho bảng
            DefaultTableModel model = (DefaultTableModel) jTabledanhSachKhuyenMai.getModel();
            model.setRowCount(0); // Xóa các dòng hiện tại

            // Lặp qua danh sách khuyến mãi để thêm vào bảng
            for (KhuyenMai km : danhSachKhuyenMai) {
                double giaTriKhuyenMai = km.getGiaTriKhuyenMai();
                String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
                String giaTriKhuyenMaiHienThi;

                // Định dạng giá trị khuyến mãi với đơn vị
                if (loaiKhuyenMai.equals("tien")) {
                    giaTriKhuyenMaiHienThi = String.format("%.0f VNĐ", giaTriKhuyenMai);
                } else if (loaiKhuyenMai.equals("phan tram") || loaiKhuyenMai.equals("mon an")) {
                    giaTriKhuyenMaiHienThi = String.format("%.0f %%", giaTriKhuyenMai);
                } else {
                    giaTriKhuyenMaiHienThi = String.format("%.0f", giaTriKhuyenMai); // Định dạng cho các loại khác
                }

                model.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getMoTaKhuyenMai(),
                    km.getNgayBatDau(),
                    km.getNgayKetThuc(),
                    km.getTrangThai(),
                    giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
                    km.getLoaiKhuyenMai(), // Giữ nguyên loại khuyến mãi gốc
                    km.getDiemYeuCau(),
                    km.getSoLuongToiThieu()
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    // Hàm để reload dữ liệu khuyến mãi khách hàng
    public void reloadKhuyenMaiKhachHang() throws SQLException {
        resetFields();    
        // Step 1: Clear data in the intermediary table
        khuyenMaiKhachHangDAO.xoaTatCaKhuyenMaiKhachHangDuy();

        // Step 2: Get the list of active promotions
        List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiHieuLucDuy();

        // Step 3: Loop through each promotion
        for (KhuyenMai km : danhSachKhuyenMai) {
            String maKhuyenMai = km.getMaKhuyenMai();
            double diemYeuCau = km.getDiemYeuCau();

            // Lấy loại khuyến mãi và loại bỏ dấu
            String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase().trim());


    //        // Kiểm tra loại khuyến mãi
    //        if (loaiKhuyenMai == null || (!loaiKhuyenMai.equals("phan tram") && !loaiKhuyenMai.equals("tien"))) {
    //            continue; // Bỏ qua nếu không phải loại khuyến mãi hợp lệ
    //        }

            // Kiểm tra loại khuyến mãi
            if (loaiKhuyenMai == null || 
                (!loaiKhuyenMai.equals("phan tram") && !loaiKhuyenMai.equals("tien")) || 
                loaiKhuyenMai.equals("mon an")) {
                continue; // Bỏ qua nếu không phải loại khuyến mãi hợp lệ
            }





            // Get the list of customers
            List<KhachHang> danhSachKhachHang = khachHangDAO.layDanhSachKhachHangDuy();

            for (KhachHang khachHang : danhSachKhachHang) {
                // Gọi phương thức getDiemTichLuy để lấy điểm tích lũy từ cơ sở dữ liệu
                Integer diemTichLuy = khachHangDAO.getDiemTichLuyDuy(khachHang.getMaKhachHang());

                // Check the loyalty points condition
                if (diemTichLuy != null) {
                    double diemTichLuyDouble = (double)diemTichLuy; // Convert to double

                    if (diemTichLuyDouble >= diemYeuCau) {
                        // Add to the intermediary table
                        khuyenMaiKhachHangDAO.themKhuyenMaiKhachHangDuy(khachHang.getMaKhachHang(), maKhuyenMai);
                    }
                }
            }
        }

        // Update the display table if necessary
        // hienThiDanhSachApDung(); // Or the corresponding method
    }

    // Hiệu ứng nhấp nháy khi mới update dữ liệu bảng
    private void flashTable(JTable table) {
            final Color originalColor = table.getBackground(); // Lưu lại màu gốc của bảng
            final Color flashColor = Color.LIGHT_GRAY; // Màu nhấp nháy

            // Tạo một Thread để thực hiện hiệu ứng nhấp nháy
            new Thread(() -> {
                try {
                    // Thay đổi màu bảng để nhấp nháy
                    table.setBackground(flashColor);
                    Thread.sleep(300); // Đợi 300ms
                    table.setBackground(originalColor); // Trả lại màu gốc
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    // Phương thức để làm mới các trường nhập liệu
    private void resetFields() {
        // Làm mới các JTextField
    //    jTextFieldmaKhuyenMai.setText("");
        jTextFieldtenKhuyenMai.setText("");
        jTextFieldmoTa.setText("");
        jTextFielddiemYeuCau.setText("");
        jTextFieldsoLuongToiThieu.setText("");
        jTextFieldgiaTriKhuyenMai.setText("");

        // Đặt giá trị mặc định là "KM"
        jTextFieldmaKhuyenMai.setText("KM");


        // Làm mới các JComboBox
        jComboBoxloaiKhuyenMai.setSelectedIndex(0); // Chọn giá trị mặc định đầu tiên
        jComboBoxtrangThai.setSelectedIndex(0); // Chọn giá trị mặc định đầu tiên

        // Làm mới các JDateChooser
        jDateChooserngayBatDau.setDate(null);
        jDateChooserngayKetThuc.setDate(null);

        // Làm mới các JCheckBox
        jCheckBoxmaKhuyenMai.setSelected(false);
        jCheckBoxtenKhuyenMai.setSelected(false);
        jCheckBoxmoTa.setSelected(false);
        jCheckBoxdiemYeuCau.setSelected(false);
        jCheckBoxngayBatDau.setSelected(false);
        jCheckBoxngayKetThuc.setSelected(false);
        jCheckBoxtrangThai.setSelected(false);
        jCheckBoxloaiKhuyenMai.setSelected(false);
        jCheckBoxsoLuongToiThieu.setSelected(false);
        jCheckBoxgiaTriKhuyenMai.setSelected(false);

        // Bỏ chọn dòng trong bảng
    jTabledanhSachKhuyenMai.clearSelection();

    }
}
