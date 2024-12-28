/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
// Nguyen Duc Hau 04/10/2024 
package view.quanlyban;


import connectDB.ConnectJDBC;
import dao.BanAn_DAO;
import dao.LoaiBan_DAO;
import java.awt.Color;
import modal.BanAn;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import modal.KhachHang;
import modal.LoaiBan;


/**
 *
 * @author ADMIN
 */


public class ManHinhQuanLyBan extends javax.swing.JPanel {

    private BanAn_DAO banAnDao;
    Connection connection;
    
    private JTextField textFieldMaBan; // TextField cho mã bàn
    private JComboBox<String> comboBoxviTri; // ComboBox cho vị trí
    private JComboBox<String> comboBoxloaiBan; // ComboBox cho loại bàn
    private JComboBox<String> comboBoxtang; // ComboBox cho tầng
    private JButton buttonTimKiem; // Nút tìm kiếm
    
    private JCheckBox checkBoxMaBan; // JCheckBox cho mã bàn
    private JCheckBox checkBoxViTri; // JCheckBox cho vị trí
    private JCheckBox checkBoxLoaiBan; // JCheckBox cho loại bàn
    private JCheckBox checkBoxTang; // JCheckBox cho tầng
    private BanAn_DAO banAnDAO = new BanAn_DAO();
    private LoaiBan_DAO loaiBan_DAO = new LoaiBan_DAO();
    

    public ManHinhQuanLyBan() throws SQLException {
        initComponents();
        
        // Khởi tạo JComboBox với dữ liệu từ data sql lên
        
        jComboBoxviTri.setModel(new DefaultComboBoxModel<>(banAnDAO.layTatCaViTri()));
        jComboBoxviTri2.setModel(new DefaultComboBoxModel<>(banAnDAO.layTatCaViTri()));
        jComboBoxloaiBan.setModel(new DefaultComboBoxModel<>(loaiBan_DAO.layTatCaTenLoaiBan2()));
        jComboBoxloaiBan2.setModel(new DefaultComboBoxModel<>(loaiBan_DAO.layTatCaTenLoaiBan2()));
        jComboBoxtang.setModel(new DefaultComboBoxModel<>(banAnDAO.layTatCaTang()));
        jComboBoxtang2.setModel(new DefaultComboBoxModel<>(banAnDAO.layTatCaTang()));
        //jComboBoxtrangThaiBan2.setModel(new DefaultComboBoxModel<>(banAnDAO.layTatCaTrangThai()));
        

        connection = ConnectJDBC.getConnection();
        
        banAnDao = new BanAn_DAO();
       
        
        // Khởi tạo mô hình bảng và thiết lập cho jTableDanhSachBan
        String[] columnNames = {"Mã bàn", "Loại bàn", "Vị trí", "Tầng", "Mô tả", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        jTableDanhSachBan.setModel(model);
        
        // Lấy 10 bàn ăn đầu tiên từ cơ sở dữ liệu và hiển thị trên bảng
        layDanhSachBanAn();
        
         // Thiết lập sự kiện khi click vào bảng
        jTableDanhSachBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDanhSachBanMouseClicked(evt);
            }
        });
        
        // Đặt JTextField mã bàn không cho phép chỉnh sửa
        jTextFieldmaBan2.setEditable(false);
        jTextFieldmaBan2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Mã bàn sẽ tự động được tạo bởi hệ thống.");
            }
        });
        
        
        // áp cứng điều kiện mã bàn khi tìm kiếm
        jTextFieldmaBan.setText("B"); // Đặt giá trị mặc định là "B"
        jTextFieldmaBan.setCaretPosition(1); // Đặt con trỏ ở vị trí sau "B"

        // Áp dụng DocumentFilter trực tiếp
        ((AbstractDocument) jTextFieldmaBan.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidInput(string, fb)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidInput(text, fb)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                // Không cho phép xóa ký tự "B"
                if (offset >= 1) {
                    super.remove(fb, offset, length);
                }
            }

            private boolean isValidInput(String input, DocumentFilter.FilterBypass fb) {
                // Kiểm tra nếu chuỗi nhập vào chỉ chứa số
                String currentText;
                try {
                    currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                } catch (BadLocationException e) {
                    return false;
                }

                String newText = currentText + input;

                // Kiểm tra độ dài của chuỗi sau "B"
                if (newText.length() > 5) { // "B" + 4 số => tổng cộng 5
                    return false;
                }

                return input.matches("[0-9]*"); // Chỉ cho phép số
            }
        });

            // Thêm jTextField vào layout của JPanel
            this.add(jTextFieldmaBan); // Thêm vào JPanel (có thể cần điều chỉnh vị trí)
    }
    
        // 1. Đảm bảo các phần tử trong JComboBox được thiết lập sẵn: (viết trong hàm chính)
        // Thiết lập các mục sẵn có trong ComboBox

        // 2. Phần xử lý khi chọn một dòng từ jTableDanhSachBan:
        // Xử lý sự kiện chọn một dòng từ bảng
        private void jTableDanhSachBanMouseClicked(java.awt.event.MouseEvent evt) {
            int selectedRow = jTableDanhSachBan.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy dữ liệu từ dòng được chọn
                String maBan = jTableDanhSachBan.getValueAt(selectedRow, 0).toString();
                String loaiBan = jTableDanhSachBan.getValueAt(selectedRow, 1).toString();
                String viTri = jTableDanhSachBan.getValueAt(selectedRow, 2).toString();
                String tang = jTableDanhSachBan.getValueAt(selectedRow, 3).toString();
                String moTa = jTableDanhSachBan.getValueAt(selectedRow, 4).toString();
                String trangThai = jTableDanhSachBan.getValueAt(selectedRow, 5).toString();

                // Cập nhật giá trị cho JTextField
                jTextFieldmaBan2.setText(maBan);
                jTextFieldmoTa.setText(moTa);



                // Cập nhật giá trị cho JComboBox
                if (jComboBoxloaiBan2.getItemCount() > 0) {
                    for (int i = 0; i < jComboBoxloaiBan2.getItemCount(); i++) {
                        if (removeAccent(jComboBoxloaiBan2.getItemAt(i).toString()).equals(removeAccent(loaiBan))) {
                            jComboBoxloaiBan2.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                if (jComboBoxviTri2.getItemCount() > 0) {
                    for (int i = 0; i < jComboBoxviTri2.getItemCount(); i++) {
                        String comboBoxValue = removeAccent(jComboBoxviTri2.getItemAt(i).toString());
                        String tableValue = removeAccent(viTri);
                        if (comboBoxValue.equals(tableValue)) {
                            jComboBoxviTri2.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                if (jComboBoxtang2.getItemCount() > 0) {
                    for (int i = 0; i < jComboBoxtang2.getItemCount(); i++) {
                        if (removeAccent(jComboBoxtang2.getItemAt(i).toString()).equals(removeAccent(tang))) {
                            jComboBoxtang2.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                if (jComboBoxtrangThaiBan2.getItemCount() > 0) {
                    for (int i = 0; i < jComboBoxtrangThaiBan2.getItemCount(); i++) {
                        if (removeAccent(jComboBoxtrangThaiBan2.getItemAt(i).toString()).equals(removeAccent(trangThai))) {
                            jComboBoxtrangThaiBan2.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }



        //1. Tạo Hàm Để Xóa Dấu đối với tất cả các combobox
        //Trước tiên, hãy tạo một hàm giúp bạn loại bỏ dấu khỏi chuỗi:
        public String removeAccent(String text) {
            String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
            return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        }


        //2. Cập Nhật Hàm jTableDanhSachBanMouseClicked
        //Sau đó, trong hàm jTableDanhSachBanMouseClicked, bạn có thể sử dụng hàm này để so sánh các chuỗi:


    //---------------------------------------------//
        private void layDanhSachBanAn() {
            try {
                List<BanAn> danhSachBanAn = banAnDao.layDanhSachBanAnChiTietDuy(null, null, null, null); // Lấy tất cả bàn
                DefaultTableModel model = (DefaultTableModel) jTableDanhSachBan.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

                // Thêm dữ liệu vào bảng
                for (int i = 0; i < Math.min(10, danhSachBanAn.size()); i++) {
                    BanAn ban = danhSachBanAn.get(i);
                    Object[] rowData = {ban.getMaBan(), ban.getLoaiBan().getMaLoaiBan(), ban.getViTri(), 
                                        ban.getTang(), ban.getMoTa(), ban.getTrangThaiBanAn()};
                    model.addRow(rowData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTimKiem = new javax.swing.JPanel();
        jLabelmaBan = new javax.swing.JLabel();
        jLabelloaiBan = new javax.swing.JLabel();
        jLabelviTri = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        jCheckBoxmaBan = new javax.swing.JCheckBox();
        jCheckBoxloaiBan = new javax.swing.JCheckBox();
        jCheckBoxviTri = new javax.swing.JCheckBox();
        jLabeltang = new javax.swing.JLabel();
        jCheckBoxtang = new javax.swing.JCheckBox();
        jComboBoxloaiBan = new javax.swing.JComboBox<>();
        jComboBoxviTri = new javax.swing.JComboBox<>();
        jComboBoxtang = new javax.swing.JComboBox<>();
        jTextFieldmaBan = new javax.swing.JTextField();
        jPanelChucNang = new javax.swing.JPanel();
        btnThemBan = new javax.swing.JButton();
        btnXoaBan = new javax.swing.JButton();
        btnSuaBan = new javax.swing.JButton();
        btnCapNhatTrangThaiBan = new javax.swing.JButton();
        jPanelChucNangTextField = new javax.swing.JPanel();
        jLabelmaBan2 = new javax.swing.JLabel();
        jLabelloaiBan2 = new javax.swing.JLabel();
        jLabelviTri2 = new javax.swing.JLabel();
        jLabeltang2 = new javax.swing.JLabel();
        jLabelloaiBan3 = new javax.swing.JLabel();
        jLabeltang3 = new javax.swing.JLabel();
        jTextFieldmoTa = new javax.swing.JTextField();
        jTextFieldmaBan2 = new javax.swing.JTextField();
        jComboBoxviTri2 = new javax.swing.JComboBox<>();
        jComboBoxloaiBan2 = new javax.swing.JComboBox<>();
        jComboBoxtrangThaiBan2 = new javax.swing.JComboBox<>();
        jComboBoxtang2 = new javax.swing.JComboBox<>();
        jPanelTuaDeDanhSachBan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDanhSachBan = new javax.swing.JTable();
        jButtonReload = new javax.swing.JButton();

        jPanelTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTimKiem.setPreferredSize(new java.awt.Dimension(677, 150));

        jLabelmaBan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaBan.setText("Mã bàn:");

        jLabelloaiBan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelloaiBan.setText("Loại bàn:");

        jLabelviTri.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelviTri.setText("Vị trí:");

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search (1) (1).png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jCheckBoxmaBan.setMaximumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaBan.setMinimumSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaBan.setPreferredSize(new java.awt.Dimension(30, 30));
        jCheckBoxmaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxmaBanActionPerformed(evt);
            }
        });

        jCheckBoxloaiBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxloaiBanActionPerformed(evt);
            }
        });

        jCheckBoxviTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxviTriActionPerformed(evt);
            }
        });

        jLabeltang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeltang.setText("Tầng:");

        jCheckBoxtang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxtangActionPerformed(evt);
            }
        });

        jComboBoxloaiBan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Loai ban--" }));
        jComboBoxloaiBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxloaiBanActionPerformed(evt);
            }
        });

        jComboBoxviTri.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Vi tri--" }));
        jComboBoxviTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxviTriActionPerformed(evt);
            }
        });

        jComboBoxtang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Tang--" }));
        jComboBoxtang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtangActionPerformed(evt);
            }
        });

        jTextFieldmaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmaBanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTimKiemLayout = new javax.swing.GroupLayout(jPanelTimKiem);
        jPanelTimKiem.setLayout(jPanelTimKiemLayout);
        jPanelTimKiemLayout.setHorizontalGroup(
            jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelloaiBan)
                            .addComponent(jLabelmaBan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxloaiBan, 0, 112, Short.MAX_VALUE)
                            .addComponent(jTextFieldmaBan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addComponent(jCheckBoxmaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jLabelviTri))
                            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                .addComponent(jCheckBoxloaiBan)
                                .addGap(50, 50, 50)
                                .addComponent(jLabeltang)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxtang, 0, 134, Short.MAX_VALUE)
                            .addComponent(jComboBoxviTri, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxtang)
                            .addComponent(jCheckBoxviTri)))
                    .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addComponent(btnTimKiem)))
                .addContainerGap())
        );
        jPanelTimKiemLayout.setVerticalGroup(
            jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(jLabelviTri, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabelmaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBoxviTri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBoxmaBan, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jComboBoxviTri, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldmaBan, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxloaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelloaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addGroup(jPanelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanelTimKiemLayout.createSequentialGroup()
                                    .addComponent(jLabeltang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jCheckBoxtang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxtang))))
                    .addComponent(jCheckBoxloaiBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanelChucNang.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jPanelChucNang.setForeground(new java.awt.Color(255, 255, 255));

        btnThemBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/them.png"))); // NOI18N
        btnThemBan.setText("Thêm Bàn");
        btnThemBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemBanActionPerformed(evt);
            }
        });

        btnXoaBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew.png"))); // NOI18N
        btnXoaBan.setText("Xóa Bàn");
        btnXoaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaBanActionPerformed(evt);
            }
        });

        btnSuaBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaBan.setText("Sửa Bàn");
        btnSuaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaBanActionPerformed(evt);
            }
        });

        btnCapNhatTrangThaiBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew2.png"))); // NOI18N
        btnCapNhatTrangThaiBan.setText("Cập Nhật Trạng Thái Bàn");
        btnCapNhatTrangThaiBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatTrangThaiBanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelChucNangLayout = new javax.swing.GroupLayout(jPanelChucNang);
        jPanelChucNang.setLayout(jPanelChucNangLayout);
        jPanelChucNangLayout.setHorizontalGroup(
            jPanelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThemBan)
                .addGap(18, 18, 18)
                .addComponent(btnXoaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCapNhatTrangThaiBan)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanelChucNangLayout.setVerticalGroup(
            jPanelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChucNangLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSuaBan, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(btnThemBan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaBan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCapNhatTrangThaiBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanelChucNangTextField.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChucNangTextField.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông Tin\n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jLabelmaBan2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaBan2.setText("Mã bàn:");

        jLabelloaiBan2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelloaiBan2.setText("Loại bàn:");

        jLabelviTri2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelviTri2.setText("Vị trí:");

        jLabeltang2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeltang2.setText("Tầng:");

        jLabelloaiBan3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelloaiBan3.setText("Mô tả:");

        jLabeltang3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabeltang3.setText("Trạng thái bàn ");

        jTextFieldmoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmoTaActionPerformed(evt);
            }
        });

        jTextFieldmaBan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmaBan2ActionPerformed(evt);
            }
        });

        jComboBoxviTri2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Vi tri--" }));
        jComboBoxviTri2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxviTri2ActionPerformed(evt);
            }
        });

        jComboBoxloaiBan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Loai ban--" }));
        jComboBoxloaiBan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxloaiBan2ActionPerformed(evt);
            }
        });

        jComboBoxtrangThaiBan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trống", "Đã đặt", "Đang sử dụng" }));
        jComboBoxtrangThaiBan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtrangThaiBan2ActionPerformed(evt);
            }
        });

        jComboBoxtang2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Tang--" }));
        jComboBoxtang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtang2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelChucNangTextFieldLayout = new javax.swing.GroupLayout(jPanelChucNangTextField);
        jPanelChucNangTextField.setLayout(jPanelChucNangTextFieldLayout);
        jPanelChucNangTextFieldLayout.setHorizontalGroup(
            jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabelloaiBan3)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelChucNangTextFieldLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelloaiBan2)
                            .addComponent(jLabelmaBan2))
                        .addGap(18, 18, 18)))
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldmoTa)
                    .addComponent(jTextFieldmaBan2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxloaiBan2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                        .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabeltang2)
                            .addComponent(jLabelviTri2))
                        .addGap(92, 92, 92)
                        .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                                .addComponent(jComboBoxviTri2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2))
                            .addComponent(jComboBoxtang2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                        .addComponent(jLabeltang3)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxtrangThaiBan2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanelChucNangTextFieldLayout.setVerticalGroup(
            jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChucNangTextFieldLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelmaBan2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldmaBan2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelviTri2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxviTri2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(55, 55, 55)
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabeltang2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelloaiBan2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxloaiBan2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxtang2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanelChucNangTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelloaiBan3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabeltang3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldmoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxtrangThaiBan2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanelTuaDeDanhSachBan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Danh Sách Bàn");

        jTableDanhSachBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã bàn", "Loại Bàn", "Vị trí", "Tầng", "Mô Tả", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDanhSachBan.setRowHeight(40);
        jScrollPane1.setViewportView(jTableDanhSachBan);
        if (jTableDanhSachBan.getColumnModel().getColumnCount() > 0) {
            jTableDanhSachBan.getColumnModel().getColumn(4).setResizable(false);
        }

        jButtonReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        jButtonReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTuaDeDanhSachBanLayout = new javax.swing.GroupLayout(jPanelTuaDeDanhSachBan);
        jPanelTuaDeDanhSachBan.setLayout(jPanelTuaDeDanhSachBanLayout);
        jPanelTuaDeDanhSachBanLayout.setHorizontalGroup(
            jPanelTuaDeDanhSachBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTuaDeDanhSachBanLayout.createSequentialGroup()
                .addGroup(jPanelTuaDeDanhSachBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTuaDeDanhSachBanLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jButtonReload)
                        .addGap(125, 125, 125)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelTuaDeDanhSachBanLayout.setVerticalGroup(
            jPanelTuaDeDanhSachBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTuaDeDanhSachBanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTuaDeDanhSachBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelTuaDeDanhSachBanLayout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(jButtonReload)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .addComponent(jPanelChucNang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelChucNangTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTuaDeDanhSachBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelChucNangTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTuaDeDanhSachBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // -- THÊM BÀN -- //--------------------------------------------
    
    private void btnThemBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemBanActionPerformed

        jComboBoxtang2.setEnabled(true); // Ensure it's enabled here
        
        // Cập nhật mã bàn chỉ khi thêm bàn
        jComboBoxtang2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTextFieldmaBan2.getText().isEmpty()) { // Chỉ cập nhật nếu trường mã bàn trống
                    String selectedTang = (String) jComboBoxtang2.getSelectedItem();
                    updateMaBan(selectedTang); // Gọi hàm tạo mã bàn mới
                }
            }
        });

        String selectedTang = (String) jComboBoxtang2.getSelectedItem();
        String maBan = "B" + selectedTang.charAt(selectedTang.length() - 1); // Tạo mã bàn
        int soBan;

        // Kiểm tra xem mã bàn hiện tại có rỗng không
        if (jTextFieldmaBan2.getText().isEmpty()) {
            updateMaBan(selectedTang);  // Chỉ tạo mã bàn mới nếu trường mã bàn rỗng
        }

        try {
            soBan = banAnDao.tuTaoMaBanTiepTheoDuy(maBan); // Lấy số bàn tiếp theo từ cơ sở dữ liệu
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy số bàn tiếp theo.");
            return;
        }

        // Gán mã bàn vào textfield (không cho phép chỉnh sửa)
        String maBanHoanChinh = maBan + String.format("%03d", soBan);
        jTextFieldmaBan2.setText(maBanHoanChinh);

        // Kiểm tra mô tả (không bắt buộc nhập)
        String moTa = jTextFieldmoTa.getText().trim();
        if (moTa.isEmpty()) {
            moTa = "B"; // Gán giá trị mặc định
        }

        // Kiểm tra các trường khác
        String viTri = (String) jComboBoxviTri2.getSelectedItem();
        String loaiBan = (String) jComboBoxloaiBan2.getSelectedItem();
        String trangThai = (String) jComboBoxtrangThaiBan2.getSelectedItem();

        // Kiểm tra xem các trường có hợp lệ không
        if (viTri == null || loaiBan == null || trangThai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thông tin cho vị trí, loại bàn và trạng thái.");
            return;
        }

        // Hiển thị thông báo xác nhận
        int response = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn tạo bàn mới với các thông tin sau không?\n" +
            "Mã bàn: " + maBanHoanChinh + "\n" +
            "Mô tả: " + moTa + "\n" +
            "Vị trí: " + viTri + "\n" +
            "Loại bàn: " + loaiBan + "\n" + 
            "Trạng thái: " + trangThai + "\n" +
            selectedTang, 
            "Xác nhận tạo bàn",
            JOptionPane.YES_NO_OPTION);

        // Nếu người dùng chọn "Có", tiếp tục thêm bàn
        if (response == JOptionPane.YES_OPTION) {
            // Thực hiện thêm bàn vào cơ sở dữ liệu
            try {
                banAnDao.addBanDuy(maBanHoanChinh, moTa, viTri, loaiBan, trangThai, selectedTang);
                JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
                loadTableData(); // Tải lại dữ liệu bảng để cập nhật danh sách bàn
                clearForm(); // Reset form sau khi thêm thành công
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm bàn vào cơ sở dữ liệu.");
            }
        }
    }//GEN-LAST:event_btnThemBanActionPerformed

    //////////// fix chức năng mã bàn quay về null khi nhấn no
    // tự động tạo mã bàn dựa trên tầng được chọn __ dựa vào hàm getNextBanNumber trong class DAO để lấy được
    private void updateMaBan(String selectedTang) {
        if (selectedTang != null && !selectedTang.isEmpty()) {
            String maBan = "B" + selectedTang.charAt(selectedTang.length() - 1); // Tạo mã bàn dựa trên tầng
            int soBan;

            try {
                soBan = banAnDao.tuTaoMaBanTiepTheoDuy(maBan); // Lấy số bàn tiếp theo từ cơ sở dữ liệu
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy số bàn tiếp theo.");
                return;
            }

            // Gán mã bàn vào textfield (không cho phép chỉnh sửa)
            String maBanHoanChinh = maBan + String.format("%03d", soBan);
            jTextFieldmaBan2.setText(maBanHoanChinh);
        }
    }

    // Hàm clearForm để reset toàn bộ input sau khi thêm bàn thành công
    private void clearForm() {
        jTextFieldmaBan2.setText("");
        jTextFieldmoTa.setText("");
        jComboBoxviTri2.setSelectedIndex(0);
        jComboBoxloaiBan2.setSelectedIndex(0);
        jComboBoxtrangThaiBan2.setSelectedIndex(0);
        jComboBoxtang2.setSelectedIndex(0);
        jComboBoxtang2.setEnabled(true); // Enable the JComboBox for tầng
    }


    // đảm bảo rằng bảng được cập nhật sau khi một bàn được thêm thành công.
    private void loadTableData() {
    try {
        List<BanAn> danhSachBan = banAnDao.getAllBanDuy(); // Lấy danh sách bàn từ DAO
        String[] columnNames = {"Mã Bàn", "Mô Tả", "Vị Trí", "Tầng", "Mã Loại Bàn", "Trạng Thái"}; // Tên cột
        Object[][] data = new Object[danhSachBan.size()][columnNames.length]; // Tạo mảng dữ liệu

        // Điền dữ liệu vào mảng
        for (int i = 0; i < danhSachBan.size(); i++) {
            BanAn banAn = danhSachBan.get(i);
            data[i][0] = banAn.getMaBan();
            data[i][1] = banAn.getMoTa();
            data[i][2] = banAn.getViTri();
            data[i][3] = banAn.getTang();
            data[i][4] = banAn.getLoaiBan(); // Hoặc bạn có thể lấy tên loại bàn từ mã
            data[i][5] = banAn.getTrangThaiBanAn();
        }

        // Tạo bảng mới và cập nhật giao diện
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        // Giả sử bạn có một panel để chứa bảng
        jTableDanhSachBan.removeAll(); // Xóa bảng cũ
        jTableDanhSachBan.add(scrollPane); // Thêm bảng mới
        jTableDanhSachBan.revalidate(); // Cập nhật lại giao diện
        jTableDanhSachBan.repaint(); // Vẽ lại panel

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu bàn.");
        }
    }

    
    
    // -- XÓA BÀN -- //--------------------------------------------
    
    private void btnXoaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaBanActionPerformed
        // TODO add your handling code here:
                                                  
//        // Lấy dòng đang được chọn trong bảng
//        int selectedRow = jTableDanhSachBan.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn để xóa.");
//            return; // Nếu chưa chọn bàn nào, không tiếp tục thực hiện
//        }
//
//        // Lấy mã bàn từ dòng được chọn
//        String maBan = jTableDanhSachBan.getValueAt(selectedRow, 0).toString();
//
//        // Xác nhận lại với người dùng xem họ có muốn xóa bàn này không
//        int response = JOptionPane.showConfirmDialog(this, 
//                "Bạn có chắc chắn muốn xóa bàn với mã: " + maBan + " không?", 
//                "Xác nhận xóa bàn", 
//                JOptionPane.YES_NO_OPTION);
//
//        if (response == JOptionPane.YES_OPTION) {
//            // Thực hiện xóa bàn
//            try {
//                banAnDao.deleteBanDuy(maBan); // Gọi phương thức xóa bàn trong DAO
//                JOptionPane.showMessageDialog(this, "Xóa bàn thành công!");
//                loadTableData(); // Cập nhật lại bảng sau khi xóa
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Lỗi khi xóa bàn khỏi cơ sở dữ liệu.");
//            }
//        }                                   


        // Lấy dòng đang được chọn trong bảng
        int selectedRow = jTableDanhSachBan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn để xóa.");
            return; // Nếu chưa chọn bàn nào, không tiếp tục thực hiện
        }

        // Lấy mã bàn từ dòng được chọn
        String maBan = jTableDanhSachBan.getValueAt(selectedRow, 0).toString();

        // Kiểm tra xem có hóa đơn nào liên quan đến mã bàn này không
        try {
            if (banAnDao.isBanHasHoaDon(maBan)) {
                int response = JOptionPane.showConfirmDialog(this, 
                        "Đang có hóa đơn chưa mã bàn này. Bạn có chắc chắn muốn xóa không?", 
                        "Xác nhận xóa bàn", 
                        JOptionPane.YES_NO_OPTION);

                if (response != JOptionPane.YES_OPTION) {
                    return; // Nếu người dùng không xác nhận, không tiếp tục thực hiện
                }
            } else {
                // Xác nhận lại với người dùng xem họ có muốn xóa bàn này không
                int response = JOptionPane.showConfirmDialog(this, 
                        "Bạn có chắc chắn muốn xóa bàn với mã: " + maBan + " không?", 
                        "Xác nhận xóa bàn", 
                        JOptionPane.YES_NO_OPTION);

                if (response != JOptionPane.YES_OPTION) {
                    return; // Nếu người dùng không xác nhận, không tiếp tục thực hiện
                }
            }

            // Cập nhật maBan trong HoaDon về NULL
            banAnDao.updateMaBanInHoaDon(maBan);

            // Thực hiện xóa bàn
            banAnDao.deleteBanDuy(maBan); // Gọi phương thức xóa bàn trong DAO
            JOptionPane.showMessageDialog(this, "Xóa bàn thành công!");
            loadTableData(); // Cập nhật lại bảng sau khi xóa
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa bàn khỏi cơ sở dữ liệu: " + e.getMessage());
        }
    }//GEN-LAST:event_btnXoaBanActionPerformed

    
    private void jCheckBoxtangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxtangActionPerformed
        // TODO add your handling code here:
        
        if (!jCheckBoxtang.isSelected()) return;

        if (jComboBoxtang.getSelectedItem() == null || jComboBoxtang.getSelectedItem().toString().equals("--Tầng--")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn tầng");
            jCheckBoxtang.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBoxtangActionPerformed

    private void jCheckBoxloaiBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxloaiBanActionPerformed
        if (!jCheckBoxloaiBan.isSelected()) return; // Nếu checkbox không được chọn thì không làm gì

        if (jComboBoxloaiBan.getSelectedItem() == null || jComboBoxloaiBan.getSelectedItem().toString().equals("--Loại bàn--")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn loại bàn");
            jCheckBoxloaiBan.setSelected(false); // Bỏ chọn checkbox
        }
    }//GEN-LAST:event_jCheckBoxloaiBanActionPerformed


    
    //--Đi chung với nhau để xét điều kiện tiêu chí tìm kiếm--//
    // Khi chua chon tieu chi nao het ma nhan nut tim kiem truoc
    private boolean isAtLeastOneCheckboxSelected() {
        return jCheckBoxviTri.isSelected() || jCheckBoxloaiBan.isSelected() || jCheckBoxtang.isSelected() || jCheckBoxmaBan.isSelected();
    }

    
    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // Kiểm tra xem có ít nhất một checkbox được chọn không
        if (!isAtLeastOneCheckboxSelected()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn tiêu chí để tìm kiếm");
            return; // Dừng hàm nếu không có tiêu chí nào được chọn
        }

        // Kiểm tra cho loại bàn
        if (jCheckBoxloaiBan.isSelected() && jComboBoxloaiBan.getSelectedItem().equals("--Loai ban--")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn loại bàn!");
            jCheckBoxloaiBan.setSelected(false);
        }

        // Kiểm tra cho vị trí
        if (jCheckBoxviTri.isSelected() && jComboBoxviTri.getSelectedItem().equals("--Vi tri--")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn vị trí!");
            jCheckBoxviTri.setSelected(false);
        }

        // Kiểm tra cho tầng
        if (jCheckBoxtang.isSelected() && jComboBoxtang.getSelectedItem().equals("--Tang--")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tầng!");
            jCheckBoxtang.setSelected(false);
        }

        // Lấy giá trị từ các thành phần giao diện
        String maBan = jTextFieldmaBan.getText().trim();
        String viTri = (String) jComboBoxviTri.getSelectedItem();
        String loaiBan = (String) jComboBoxloaiBan.getSelectedItem();
        String tang = (String) jComboBoxtang.getSelectedItem();

        // Khởi tạo danh sách kết quả
        List<BanAn> danhSachBanAn = new ArrayList<>();

        try {
            // Gọi DAO để lấy danh sách bàn ăn chi tiết theo tiêu chí đã chọn
            danhSachBanAn = banAnDao.layDanhSachBanAnChiTietDuy(
                    jCheckBoxmaBan.isSelected() ? maBan : null,
                    jCheckBoxloaiBan.isSelected() ? loaiBan : null,
                    jCheckBoxtang.isSelected() ? tang : null,
                    jCheckBoxviTri.isSelected() ? viTri : null
            );

            // Cập nhật bảng với dữ liệu mới
            DefaultTableModel model = (DefaultTableModel) jTableDanhSachBan.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

            // Thêm dữ liệu vào bảng
            for (BanAn ban : danhSachBanAn) {
                Object[] rowData = {
                    ban.getMaBan(),
                    ban.getLoaiBan().getMaLoaiBan(),
                    ban.getViTri(),
                    ban.getTang(),
                    ban.getMoTa(),
                    ban.getTrangThaiBanAn()
                };
                model.addRow(rowData);
            }

            // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
            flashTable(jTableDanhSachBan);

        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình tìm kiếm. Vui lòng thử lại.");
        }


    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void jCheckBoxviTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxviTriActionPerformed
        // TODO add your handling code here:
  
    }//GEN-LAST:event_jCheckBoxviTriActionPerformed

    private void jComboBoxtangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtangActionPerformed
        // TODO add your handling code here:
        // Kiểm tra nếu người dùng chọn phần tử khác "--Tầng--" thì checkbox sẽ được chọn
        if (!jComboBoxtang.getSelectedItem().equals("--Tang--")) {
            jCheckBoxtang.setSelected(true);  // Tự động chọn checkbox
        } else {
            jCheckBoxtang.setSelected(false); // Nếu chọn "--Tầng--", bỏ chọn checkbox
        }
        
    }//GEN-LAST:event_jComboBoxtangActionPerformed


    private void jCheckBoxmaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxmaBanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxmaBanActionPerformed


    private void jComboBoxloaiBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxloaiBanActionPerformed
        // Kiểm tra nếu người dùng chọn phần tử khác "--Tầng--" thì checkbox sẽ được chọn
        if (!jComboBoxloaiBan.getSelectedItem().equals("--Loai Ban--")) {
            jCheckBoxloaiBan.setSelected(true);  // Tự động chọn checkbox
        } else {
            jCheckBoxloaiBan.setSelected(false); // Nếu chọn "--Loai ban--", bỏ chọn checkbox
        }
    }//GEN-LAST:event_jComboBoxloaiBanActionPerformed

    
    private void jTextFieldmaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmaBanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmaBanActionPerformed

    private void jTextFieldmaBan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmaBan2ActionPerformed
        // TODO add your handling code here:
 
    }//GEN-LAST:event_jTextFieldmaBan2ActionPerformed

    private void jTextFieldmoTaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmoTaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmoTaActionPerformed

    private void btnSuaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaBanActionPerformed
    
    // Lấy dòng đã chọn từ bảng
       int selectedRow = jTableDanhSachBan.getSelectedRow();

       if (selectedRow == -1) {
           JOptionPane.showMessageDialog(null, "Vui lòng chọn một bàn để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
           return;
       }

       // Lấy thông tin từ các trường nhập liệu
       String maBan = jTextFieldmaBan2.getText();
       String loaiBan = (String) jComboBoxloaiBan2.getSelectedItem();
       String viTri = (String) jComboBoxviTri2.getSelectedItem();
       String tang = (String) jComboBoxtang2.getSelectedItem();
       String moTa = jTextFieldmoTa.getText();
       String trangThai = (String) jComboBoxtrangThaiBan2.getSelectedItem();

       // Lấy các giá trị cũ từ bảng để so sánh
       String oldLoaiBan = jTableDanhSachBan.getValueAt(selectedRow, 1).toString();
       String oldViTri = jTableDanhSachBan.getValueAt(selectedRow, 2).toString();
       String oldTang = jTableDanhSachBan.getValueAt(selectedRow, 3).toString();
       String oldMoTa = jTableDanhSachBan.getValueAt(selectedRow, 4).toString();
       String oldTrangThai = jTableDanhSachBan.getValueAt(selectedRow, 5).toString();

       // Kiểm tra xem dữ liệu đã thay đổi hay chưa
       if (removeAccent(loaiBan).equals(removeAccent(oldLoaiBan)) &&
           removeAccent(viTri).equals(removeAccent(oldViTri)) &&
           moTa.equals(oldMoTa) &&
           removeAccent(trangThai).equals(removeAccent(oldTrangThai))) {
           JOptionPane.showMessageDialog(null, "Dữ liệu chưa thay đổi, không có gì để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
           return;
       }

       // Vô hiệu hóa JComboBox tầng
       jComboBoxtang2.setEnabled(false);

       int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn sửa thông tin bàn này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
       if (confirm != JOptionPane.YES_OPTION) {
           return;
       }

       try {
           // Khai báo maLoaiBan
           String maLoaiBan;

           // Chuyển đổi từ tên loại bàn sang chuỗi không dấu
           String tenLoaiBanKhongDau = removeAccent(loaiBan);
           System.out.println("Tên loại bàn (không dấu): " + tenLoaiBanKhongDau); // In giá trị của tenLoaiBan không dấu ra console

           // Chuyển đổi từ tên loại bàn không dấu sang mã loại bàn
           if (tenLoaiBanKhongDau.equalsIgnoreCase("Ban Thuong")) {
               maLoaiBan = "LB001";
           } else if (tenLoaiBanKhongDau.equalsIgnoreCase("Ban VIP")) {
               maLoaiBan = "LB002";
           } else {
               JOptionPane.showMessageDialog(null, "Loại bàn không hợp lệ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
               return; // Thoát nếu loại bàn không hợp lệ
           }

           // Tạo đối tượng LoaiBan từ maLoaiBan
           LoaiBan lb = new LoaiBan();
           lb.setMaLoaiBan(maLoaiBan); // Gán mã loại bàn

           // Tạo đối tượng BanAn để cập nhật
           BanAn ban = new BanAn();
           ban.setMaBan(maBan);
           ban.setLoaiBan(lb); // Sử dụng đối tượng LoaiBan đã tạo
           ban.setMoTa(moTa);
           ban.setTang(tang);
           ban.setTrangThaiBanAn(trangThai);
           ban.setViTri(viTri);

           // Gọi phương thức updateBan từ BanAn_DAO để cập nhật thông tin bàn
           banAnDao.updateBanDuy(ban);

           JOptionPane.showMessageDialog(null, "Sửa bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
           loadTableData(); // Cập nhật lại bảng để hiển thị thông tin mới

       } catch (SQLException ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Lỗi khi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
       }finally {
           // Kích hoạt lại JComboBox tầng
           jComboBoxtang2.setEnabled(true);
       }

        resetFields();
        
        // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
        flashTable(jTableDanhSachBan);
        
        layDanhSachBanAn();
    }//GEN-LAST:event_btnSuaBanActionPerformed

    private void btnCapNhatTrangThaiBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatTrangThaiBanActionPerformed


    // Lấy dòng đã chọn từ bảng
        int selectedRow = jTableDanhSachBan.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một bàn để cập nhật trạng thái.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin từ JComboBox trạng thái
        String maBan = jTableDanhSachBan.getValueAt(selectedRow, 0).toString(); // Giả sử cột đầu tiên là mã bàn
        String trangThai = (String) jComboBoxtrangThaiBan2.getSelectedItem();

        // Kiểm tra xem trạng thái có thay đổi không
        String oldTrangThai = jTableDanhSachBan.getValueAt(selectedRow, 5).toString(); // Giả sử cột 5 là trạng thái
        if (removeAccent(trangThai).equals(removeAccent(oldTrangThai))) {
            JOptionPane.showMessageDialog(null, "Trạng thái chưa thay đổi, không có gì để cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn cập nhật trạng thái bàn này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Tạo đối tượng BanAn để cập nhật
            BanAn ban = new BanAn();
            ban.setMaBan(maBan);
            ban.setTrangThaiBanAn(trangThai);

            // Gọi phương thức updateTrangThai từ BanAn_DAO để cập nhật trạng thái bàn
            banAnDao.updateTrangThaiDuy(ban);

            JOptionPane.showMessageDialog(null, "Cập nhật trạng thái bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadTableData(); // Cập nhật lại bảng để hiển thị thông tin mới

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }     
        
        
        
        
        
        
    }//GEN-LAST:event_btnCapNhatTrangThaiBanActionPerformed

    private void jComboBoxloaiBan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxloaiBan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxloaiBan2ActionPerformed

    private void jComboBoxtrangThaiBan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtrangThaiBan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxtrangThaiBan2ActionPerformed

    private void jComboBoxtang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtang2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxtang2ActionPerformed

    private void jComboBoxviTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxviTriActionPerformed
        // Kiểm tra nếu người dùng chọn phần tử khác "--Tầng--" thì checkbox sẽ được chọn
        if (!jComboBoxviTri.getSelectedItem().equals("--Vi tri--")) {
            jCheckBoxviTri.setSelected(true);  // Tự động chọn checkbox
        } else {
            jCheckBoxviTri.setSelected(false); // Nếu chọn "--Vi tri--", bỏ chọn checkbox
        }
    }//GEN-LAST:event_jComboBoxviTriActionPerformed

    private void jComboBoxviTri2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxviTri2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxviTri2ActionPerformed

    private void jButtonReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReloadActionPerformed
        // TODO add your handling code here:
        
        resetFields();
        
        // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
        flashTable(jTableDanhSachBan);
        
        layDanhSachBanAn();
    }//GEN-LAST:event_jButtonReloadActionPerformed
    
   

// các chỉnh sửa thêm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhatTrangThaiBan;
    private javax.swing.JButton btnSuaBan;
    private javax.swing.JButton btnThemBan;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoaBan;
    private javax.swing.JButton jButtonReload;
    private javax.swing.JCheckBox jCheckBoxloaiBan;
    private javax.swing.JCheckBox jCheckBoxmaBan;
    private javax.swing.JCheckBox jCheckBoxtang;
    private javax.swing.JCheckBox jCheckBoxviTri;
    private javax.swing.JComboBox<String> jComboBoxloaiBan;
    private javax.swing.JComboBox<String> jComboBoxloaiBan2;
    private javax.swing.JComboBox<String> jComboBoxtang;
    private javax.swing.JComboBox<String> jComboBoxtang2;
    private javax.swing.JComboBox<String> jComboBoxtrangThaiBan2;
    private javax.swing.JComboBox<String> jComboBoxviTri;
    private javax.swing.JComboBox<String> jComboBoxviTri2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelloaiBan;
    private javax.swing.JLabel jLabelloaiBan2;
    private javax.swing.JLabel jLabelloaiBan3;
    private javax.swing.JLabel jLabelmaBan;
    private javax.swing.JLabel jLabelmaBan2;
    private javax.swing.JLabel jLabeltang;
    private javax.swing.JLabel jLabeltang2;
    private javax.swing.JLabel jLabeltang3;
    private javax.swing.JLabel jLabelviTri;
    private javax.swing.JLabel jLabelviTri2;
    private javax.swing.JPanel jPanelChucNang;
    private javax.swing.JPanel jPanelChucNangTextField;
    private javax.swing.JPanel jPanelTimKiem;
    private javax.swing.JPanel jPanelTuaDeDanhSachBan;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDanhSachBan;
    private javax.swing.JTextField jTextFieldmaBan;
    private javax.swing.JTextField jTextFieldmaBan2;
    private javax.swing.JTextField jTextFieldmoTa;
    // End of variables declaration//GEN-END:variables
    
    // Các hàm  
    // Phần bổ sung các sự kiện cho JComboBox và JCheckBox trong lớp ManHinhBan
    // Phần bổ sung sự kiện cho nút btnTimKiem với xử lý lỗi

    //    2. Tạo hiệu ứng nhấp nháy cho bảng:
    //Bạn có thể tạo một phương thức flashTable để thay đổi màu nền của bảng trong vài giây nhằm báo hiệu cho người dùng biết rằng bảng đã được cập nhật.
    //
    
    //Tạo phương thức flashTable để nhấp nháy hiệu ứng khi cập nhật xong
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
        
        
        jTextFieldmaBan2.setText("");
        jTextFieldmoTa.setText("");

        // Làm mới các JComboBox
        jComboBoxloaiBan.setSelectedIndex(0); // Chọn giá trị mặc định đầu tiên
        jComboBoxloaiBan2.setSelectedIndex(0); 
        jComboBoxviTri.setSelectedIndex(0); 
        jComboBoxviTri2.setSelectedIndex(0); 
        jComboBoxtang.setSelectedIndex(0); 
        jComboBoxtang2.setSelectedIndex(0); 
        jComboBoxtrangThaiBan2.setSelectedIndex(0); // Chọn giá trị mặc định đầu tiên



        // Làm mới các JCheckBox
        jCheckBoxmaBan.setSelected(false);
        jCheckBoxloaiBan.setSelected(false);
        jCheckBoxtang.setSelected(false);
        jCheckBoxviTri.setSelected(false);
        // Bỏ chọn dòng trong bảng
        jTableDanhSachBan.clearSelection();
    }
    

}