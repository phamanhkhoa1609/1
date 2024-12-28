/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.quanlydatban;

import Parttern.BanManager;
import dao.BanAn_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import dao.QuanLy_DAO;
import java.awt.CardLayout;
import java.awt.Color;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import modal.BanAn;
import modal.HoaDon;
import modal.KhachHang;
import modal.NhanVien;
import modal.QuanLy;
import modal.TaiKhoan;
import view.quanlygoimon.ManHinhGoiMon;

/**
 *
 * @author ADMIN
 */
public class ManHinhThongTinBan extends JDialog {
    private BanAn banAn;
    HoaDon_DAO hoadon_DAO = new HoaDon_DAO();

    /**
     * Creates new form ManHinhThongTinBan
     * @param banAn
     * @throws java.sql.SQLException
     */
    private ManHinhDatBan manHinhDatBanInstance; 
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private TaiKhoan taiKhoan;
    
    public ManHinhThongTinBan(BanAn banAn, ManHinhDatBan manHinhDatBanInstance, TaiKhoan taiKhoan) throws SQLException{
        this.banAn = banAn;
        this.manHinhDatBanInstance = manHinhDatBanInstance;  // Lưu tham chiếu
        this.taiKhoan = taiKhoan;
        initComponents();
        layThongTinBan();
        layThongTinKhachHang();
        soDienThoaiText.setRequestFocusEnabled(true);
        setTitle("Bàn " + banAn.getMaBan());
    }
    
    //Lấy thông tin của bàn lên các trường
    public void layThongTinBan() throws SQLException{
        BanAn_DAO banAn_DAO = new BanAn_DAO();
        BanAn ban = banAn_DAO.layThongTinBanAn(banAn.getMaBan());
        
        maBanText.setText(ban.getMaBan());
        loaiBanText.setText(ban.getLoaiBan().getTenLoaiBan());
        trangThaiBanText.setText(ban.getTrangThaiBanAn());
        viTriText.setText(ban.getViTri());
        
        if(ban.getTrangThaiBanAn().equals("Trống")){
            trangThaiBanText.setForeground(Color.GREEN);
            themKhachHangButton.setEnabled(false);
            datNgayButton.setEnabled(true); // Cho phép đặt
            huyDatButton.setEnabled(false);
            goiMonButton.setEnabled(false);
        }else if(ban.getTrangThaiBanAn().equals("Đã đặt")){
            trangThaiBanText.setForeground(Color.RED);
            datNgayButton.setEnabled(false);
            huyDatButton.setEnabled(true);// Cho phép hủy
            goiMonButton.setEnabled(true);// Cho phép gọi món
            themKhachHangButton.setEnabled(false);
        }else if(ban.getTrangThaiBanAn().equals("Đang sử dụng")){
            trangThaiBanText.setForeground(Color.YELLOW);
            datNgayButton.setEnabled(false);
            huyDatButton.setEnabled(false);
            themKhachHangButton.setEnabled(false);  
            goiMonButton.setEnabled(true); // Cho phép gọi món
        }
        
        if(banAn.getLoaiBan().getMaLoaiBan().equals("LB001")){
            iconBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/chair4.png")));
            soLuongKhachSpinner.setModel(new SpinnerNumberModel(1,1,6,1));
        } else if(banAn.getLoaiBan().getMaLoaiBan().equals("LB002")){
            iconBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/banVIP.png")));
            soLuongKhachSpinner.setModel(new SpinnerNumberModel(1,1,15,1));
        }
    }
    
    //Lấy thông tin của khách hàng (nếu đã đặt), hoặc để trống nếu chưa có
    public void layThongTinKhachHang() throws SQLException{
        String maBan = maBanText.getText();
        String trangThai = trangThaiBanText.getText();
        HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
        
        if(trangThai.equals("Đang sử dụng") || trangThai.equals("Đã đặt")){
            HoaDon hd = hoaDon_DAO.layHoaDon(maBan);

            if(hd!=null){
                System.out.println(hd.getMaHoaDon() + " " + hd.getKhachHang().getMaKhachHang());
                
                khachHangText.setText(hd.getKhachHang().getTenKhachHang());
                soDienThoaiText.setText(hd.getKhachHang().getSoDienThoai());
                soLuongKhachSpinner.setValue(hd.getSoLuongKhach());
                
                khachHangText.setEditable(false);
                soDienThoaiText.setEditable(false);
                soLuongKhachSpinner.setEnabled(false);
            }else{
                JOptionPane.showMessageDialog(this, "Thông tin Khách hàng có Hóa đơn chưa được tạo đúng cách", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            System.out.println("Trống");
            khachHangText.setText("");
            soDienThoaiText.setText("");
        }
    }
    
    //Lấy thông tin khách hàng thông qua số điện thoại để phục vụ cho việc 'đặt bàn' và lập hóa đơn (nếu có) hoặc ẩn danh
    public KhachHang layThongTinKhachHangSDT() {
        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        
        String tenKhach = khachHangText.getText();
        String soDienThoai = soDienThoaiText.getText();
        if (tenKhach.isEmpty() || soDienThoai.isEmpty()) {
            return new KhachHang("KH000");
        }

        try {
            KhachHang khach =  khachHang_DAO.timKhachHangTheoSDT(soDienThoai);
            return (khach != null) ? khach : new KhachHang("KH000");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return new KhachHang("KH000");
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSouth = new java.awt.Panel();
        panelWestBottom = new javax.swing.JPanel();
        panelCenterBottom = new javax.swing.JPanel();
        goiMonButton = new javax.swing.JButton();
        huyDatButton = new javax.swing.JButton();
        datNgayButton = new javax.swing.JButton();
        panelCenter = new java.awt.Panel();
        iconBan = new javax.swing.JLabel();
        panelThongTin = new javax.swing.JPanel();
        maBanText = new javax.swing.JTextField();
        labelTrangThaiBanAn = new javax.swing.JLabel();
        trangThaiBanText = new javax.swing.JTextField();
        labelMaBan = new javax.swing.JLabel();
        labelLoaiBan = new javax.swing.JLabel();
        loaiBanText = new javax.swing.JLabel();
        labelKhachHang = new javax.swing.JLabel();
        khachHangText = new javax.swing.JTextField();
        themKhachHangButton = new javax.swing.JButton();
        soDienThoaiText = new javax.swing.JTextField();
        labelSoDienThoai = new javax.swing.JLabel();
        labelSoLuongKhach = new javax.swing.JLabel();
        soLuongKhachSpinner = new javax.swing.JSpinner();
        viTriLabel = new javax.swing.JLabel();
        viTriText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(600, 600));
        setResizable(false);

        panelSouth.setPreferredSize(new java.awt.Dimension(600, 100));
        panelSouth.setLayout(new java.awt.BorderLayout());

        panelWestBottom.setPreferredSize(new java.awt.Dimension(210, 100));

        javax.swing.GroupLayout panelWestBottomLayout = new javax.swing.GroupLayout(panelWestBottom);
        panelWestBottom.setLayout(panelWestBottomLayout);
        panelWestBottomLayout.setHorizontalGroup(
            panelWestBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );
        panelWestBottomLayout.setVerticalGroup(
            panelWestBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelSouth.add(panelWestBottom, java.awt.BorderLayout.WEST);

        panelCenterBottom.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 2, 14))); // NOI18N
        panelCenterBottom.setPreferredSize(new java.awt.Dimension(350, 100));

        goiMonButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        goiMonButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/restaurant.png"))); // NOI18N
        goiMonButton.setText("Gọi món");
        goiMonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goiMonButtonActionPerformed(evt);
            }
        });

        huyDatButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        huyDatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew.png"))); // NOI18N
        huyDatButton.setText("Hủy đặt");
        huyDatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                huyDatButtonActionPerformed(evt);
            }
        });

        datNgayButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        datNgayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/checkout.png"))); // NOI18N
        datNgayButton.setText("Đặt ngay");
        datNgayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datNgayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCenterBottomLayout = new javax.swing.GroupLayout(panelCenterBottom);
        panelCenterBottom.setLayout(panelCenterBottomLayout);
        panelCenterBottomLayout.setHorizontalGroup(
            panelCenterBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(huyDatButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datNgayButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goiMonButton, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );
        panelCenterBottomLayout.setVerticalGroup(
            panelCenterBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterBottomLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelCenterBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(goiMonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(huyDatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datNgayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelSouth.add(panelCenterBottom, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelSouth, java.awt.BorderLayout.SOUTH);

        panelCenter.setPreferredSize(new java.awt.Dimension(600, 300));

        panelThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        maBanText.setEditable(false);
        maBanText.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        labelTrangThaiBanAn.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        labelTrangThaiBanAn.setText("Trạng thái:");

        trangThaiBanText.setEditable(false);
        trangThaiBanText.setBackground(new java.awt.Color(153, 153, 153));
        trangThaiBanText.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        trangThaiBanText.setText("\"Trống\"");

        labelMaBan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labelMaBan.setText("Bàn:");

        labelLoaiBan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labelLoaiBan.setText("Loại bàn:");

        loaiBanText.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        loaiBanText.setText("\"VIP\"");

        labelKhachHang.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        labelKhachHang.setText("Khách hàng:");

        khachHangText.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        themKhachHangButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ThemKhachHang.png"))); // NOI18N
        themKhachHangButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themKhachHangButtonActionPerformed(evt);
            }
        });

        soDienThoaiText.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        soDienThoaiText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soDienThoaiTextActionPerformed(evt);
            }
        });

        labelSoDienThoai.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labelSoDienThoai.setText("Số điện thoại");

        labelSoLuongKhach.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        labelSoLuongKhach.setText("Số lượng khách:");
        labelSoLuongKhach.setToolTipText("");

        soLuongKhachSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        viTriLabel.setText("Vị trí:");

        viTriText.setText("\"Giữa phòng\"");

        javax.swing.GroupLayout panelThongTinLayout = new javax.swing.GroupLayout(panelThongTin);
        panelThongTin.setLayout(panelThongTinLayout);
        panelThongTinLayout.setHorizontalGroup(
            panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(labelMaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(maBanText, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(labelLoaiBan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loaiBanText)))
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(viTriLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labelTrangThaiBanAn)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(viTriText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(38, 38, 38))
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(trangThaiBanText, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(24, Short.MAX_VALUE))))
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(labelSoLuongKhach)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(soLuongKhachSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelSoDienThoai)
                                    .addComponent(labelKhachHang))
                                .addGap(12, 12, 12)
                                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(soDienThoaiText)
                                    .addComponent(khachHangText, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(themKhachHangButton)))
                        .addGap(9, 9, 9))))
        );
        panelThongTinLayout.setVerticalGroup(
            panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinLayout.createSequentialGroup()
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMaBan)
                    .addComponent(maBanText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTrangThaiBanAn)
                    .addComponent(trangThaiBanText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLoaiBan)
                    .addComponent(loaiBanText)
                    .addComponent(viTriLabel)
                    .addComponent(viTriText))
                .addGap(18, 18, 18)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themKhachHangButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelKhachHang)
                        .addComponent(khachHangText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSoDienThoai)
                    .addComponent(soDienThoaiText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSoLuongKhach)
                    .addComponent(soLuongKhachSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCenterLayout = new javax.swing.GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenterLayout.setHorizontalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(iconBan, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCenterLayout.setVerticalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCenterLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(panelThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCenterLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(iconBan, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        getContentPane().add(panelCenter, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void datNgayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datNgayButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đặt bàn ngay?", "Xác nhận đặt?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        HoaDon hd = new HoaDon();
        HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();

        hd.setBanAn(new BanAn(maBanText.getText()));
        hd.setKhachHang(layThongTinKhachHangSDT());
        
        if(taiKhoan.getVaiTro().equalsIgnoreCase("NhanVien")){
            NhanVien_DAO nv_dao = new NhanVien_DAO();
            NhanVien nv = null;
            try {
                nv = nv_dao.layThongTinNhanVien(taiKhoan.getTenNguoiDung());
            } catch (SQLException ex) {
                Logger.getLogger(ManHinhThongTinBan.class.getName()).log(Level.SEVERE, null, ex);
            }
            hd.setNhanVien(nv);
        }else{
            QuanLy_DAO ql_dao = new QuanLy_DAO();
            QuanLy ql = null;
            try {
                ql = ql_dao.layThongTinQuanLy(taiKhoan.getTenNguoiDung());
            } catch (SQLException ex) {
                Logger.getLogger(ManHinhThongTinBan.class.getName()).log(Level.SEVERE, null, ex);
            }
            hd.setQuanLy(ql);
        }


        try {
            hd.setSoLuongKhach((int) soLuongKhachSpinner.getValue());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String trangThaiThanhToan = "Chưa thanh toán";
            try {
                if (hoaDon_DAO.lapHoaDon1(hd, trangThaiThanhToan) != null) {
                    JOptionPane.showMessageDialog(this, "Đặt bàn thành công, mời gọi món");
                    capNhatTrangThaiBanAn("Đang sử dụng");
                    layThongTinBan();
                    layThongTinKhachHang();
                } else {
                    JOptionPane.showMessageDialog(this, "Đặt bàn không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Đặt bàn không thành công dau", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_datNgayButtonActionPerformed

    private void soDienThoaiTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soDienThoaiTextActionPerformed
        String soDienThoai = soDienThoaiText.getText().trim();

        // Kiểm tra xem số điện thoại có đủ 10 số và chỉ chứa các ký tự số hay không
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số điện thoại (10 số)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            khachHangText.setText("");
            return; 
        }

        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        try {
            KhachHang kh = khachHang_DAO.timKhachHangTheoSDT(soDienThoai);
            if (kh != null) {
                khachHangText.setText(kh.getTenKhachHang());
                khachHangText.setRequestFocusEnabled(true);
                themKhachHangButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Khách hàng chưa có tài khoản!!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                khachHangText.setText("");
                themKhachHangButton.setEnabled(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_soDienThoaiTextActionPerformed

    private void huyDatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_huyDatButtonActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy đặt bàn?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
        
            String trangThaiMoi = "Đã hủy";

            try {
                hoaDon_DAO.capNhatTrangThaiHoaDonSauKhiHuy(trangThaiMoi, banAn.getMaBan());
                JOptionPane.showMessageDialog(this, "Đã hủy đặt bàn!");
                capNhatTrangThaiBanAn("Trống");
                layThongTinBan();
                layThongTinKhachHang();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra!");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_huyDatButtonActionPerformed

    private void themKhachHangButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themKhachHangButtonActionPerformed
        String tenKhachHang = khachHangText.getText();
        String soDT = soDienThoaiText.getText();

        if (tenKhachHang.isEmpty() || soDT.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        try {
            KhachHang kh = khachHang_DAO.timKhachHangTheoSDT(soDT);
            if (kh == null) {
                khachHang_DAO.themKhachHang(tenKhachHang, soDT);
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Khách hàng này đã tạo tài khoản", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_themKhachHangButtonActionPerformed

    private void goiMonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goiMonButtonActionPerformed
        String maBan = maBanText.getText();  

        if (isBanPhu(maBan)) {
            BanAn_DAO baDAO = new BanAn_DAO();
            HoaDon trangThaiBan = baDAO.layTrangThaiBanNha(maBan);

            if (trangThaiBan != null && trangThaiBan.getBanAn() != null) {
                System.out.println("Bàn phụ đã thanh toán: " + trangThaiBan.getBanAn().getMaBan());
                // Bàn phụ đã thanh toán, cho phép gọi món
                System.out.println("Bàn phụ đã thanh toán, có thể gọi món.");
            } else {
                String maBanChinh = layMaBanChinh(maBan);
                String soDienThoaiKhach = laySoDienThoaiKH(maBanChinh);

                JOptionPane.showMessageDialog(this, 
                    "Thông tin của khách hàng: " + soDienThoaiKhach + "\n" +
                    "Bàn phụ này đã được gộp với bàn chính " + maBanChinh + ".\n" +
                    "Hãy gọi món tại bàn chính!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        capNhatTrangThaiBanAn("Đang sử dụng");
        this.setVisible(false);
        try {
            ManHinhGoiMon manHinhGoiMon = new ManHinhGoiMon(maBan, this, taiKhoan);
            JFrame frame = new JFrame("Gọi Món - Mã Bàn: " + maBan);
            frame.setSize(1245, 750);
            frame.setLocationRelativeTo(null);

            frame.add(manHinhGoiMon);

            // Thiết lập hành động khi đóng cửa sổ
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        } catch (Exception ex) {
            Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_goiMonButtonActionPerformed

    private boolean isBanPhu(String maBan) {
        return maBan != null && maBan.equals(BanManager.getInstance().getMaBanPhu());
    }
    
    private String layMaBanChinh(String maBanPhu) {
        return BanManager.getInstance().getMaBanChinh();
    }

    private String laySoDienThoaiKH(String maBanChinh) {
        KhachHang_DAO kh = new KhachHang_DAO();
        return kh.laySoDienThoaiKhachGB(maBanChinh);
    }

    public void capNhatTrangThaiBanAn(String trangThai){
        String maBan = maBanText.getText();
        
        BanAn_DAO banAn_DAO = new BanAn_DAO();
        try{
            banAn_DAO.capNhatTrangThaiBan(maBan, trangThai);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton datNgayButton;
    private javax.swing.JButton goiMonButton;
    private javax.swing.JButton huyDatButton;
    private javax.swing.JLabel iconBan;
    private javax.swing.JTextField khachHangText;
    private javax.swing.JLabel labelKhachHang;
    private javax.swing.JLabel labelLoaiBan;
    private javax.swing.JLabel labelMaBan;
    private javax.swing.JLabel labelSoDienThoai;
    private javax.swing.JLabel labelSoLuongKhach;
    private javax.swing.JLabel labelTrangThaiBanAn;
    private javax.swing.JLabel loaiBanText;
    private javax.swing.JTextField maBanText;
    private java.awt.Panel panelCenter;
    private javax.swing.JPanel panelCenterBottom;
    private java.awt.Panel panelSouth;
    private javax.swing.JPanel panelThongTin;
    private javax.swing.JPanel panelWestBottom;
    private javax.swing.JTextField soDienThoaiText;
    private javax.swing.JSpinner soLuongKhachSpinner;
    private javax.swing.JButton themKhachHangButton;
    private javax.swing.JTextField trangThaiBanText;
    private javax.swing.JLabel viTriLabel;
    private javax.swing.JLabel viTriText;
    // End of variables declaration//GEN-END:variables

}
