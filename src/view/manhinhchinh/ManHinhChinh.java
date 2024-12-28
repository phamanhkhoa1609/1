/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.manhinhchinh;

import dao.KetCa_DAO;
import dao.NhanVien_DAO;
import dao.QuanLy_DAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import modal.NhanVien;
import modal.QuanLy;
import modal.TaiKhoan;
import utils.CapNhatHoaDonTuDong;
import view.dangNhap.DangNhap;
import view.quanlyban.ManHinhQuanLyBan;
import view.quanlydatban.ManHinhDatBan;
import view.quanlydatban.ManHinhLichSuDatBan;
import view.quanlygoimon.ManHinhGoiMon;
import view.quanlygoimon.ManHinhThongTinMonAn;
import view.quanlyhoadon.ManHinhThanhToanHoaDon;
import view.quanlyhoadon.ManHinhThongTinHoaDon;
//import view.quanlyhoadon.ManHinhThongTinHoaDon;
import view.quanlykhachhang.ManHinhQuanLyKhachHang;
import view.quanlykhuyenmai.ManHinhTaoKhuyenMai;
import view.quanlykhuyenmai.ManHinhThongTinKhuyenMai;
import view.quanlynhanvien.ManHinhQuanLyNhanVien;
import view.thongkevabaocao.ManHinhBaoCao;
import view.thongkevabaocao.ManHinhThongKe;

/**
 *
 * @author ACER
 */
public class ManHinhChinh extends javax.swing.JFrame {

    private JButton buttonTruoc = null;
    private static TaiKhoan taiKhoan;
    private String maKetCa; // Mã kết ca
    private LocalDateTime ngayGioBatDau; // Ngày giờ bắt đầu ca
    private CapNhatHoaDonTuDong capNhatHoaDonTuDong = new CapNhatHoaDonTuDong();
    /**
     * Creates new form ManHinhChinh
     * @param taiKhoan
     */
    public ManHinhChinh(TaiKhoan taiKhoan) {
        initComponents();
        this.taiKhoan=taiKhoan;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        MyFrame();
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/trangChu.jpg"));
         Image img = icon.getImage();  // Lấy hình ảnh gốc
 
         Image scaledImg = img.getScaledInstance(1200, 682, Image.SCALE_SMOOTH);
         JLabel label = new JLabel(new ImageIcon(scaledImg));
         jPanel7.setLayout(new java.awt.BorderLayout());
         jPanel7.add(label, BorderLayout.CENTER);
//         jPanel7.validate();
//         jPanel7.repaint();
        startClock();
//        quanLyBan();
        btnThongTinNhanVien.setText(taiKhoan.getTenNguoiDung());
        setupCardLayout();
        
        this.maKetCa = generateShiftCode(); // Gọi hàm tạo mã kết ca
        this.ngayGioBatDau = LocalDateTime.now();
        
        // Tiến hành lưu kết ca vào cơ sở dữ liệu khi bắt đầu
        saveShiftData();
        capNhatHoaDonTuDong.batDau();
         
    }
    
    public void dongManHinh() {
        capNhatHoaDonTuDong.dung(); // Dừng lịch trình khi đóng màn hình
    }
    
    private void saveShiftData() {
        KetCa_DAO ketCaDAO = new KetCa_DAO();
        try {
            System.out.println("Vai trò: " + taiKhoan.getVaiTro());
            System.out.println("Mã người dùng: " + layMaNguoiDung());

            ketCaDAO.saveShiftData(taiKhoan.getVaiTro(),layMaNguoiDung(),maKetCa , ngayGioBatDau, null); // Chưa có giờ kết thúc
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String generateShiftCode() {
        // Tạo mã kết ca theo định dạng "KC-yyyyMMdd-XXX" (chắc chắn sẽ là mã hợp lệ)
        KetCa_DAO ketCaDAO = new KetCa_DAO();
        try {
            return ketCaDAO.getNextMaKetCa();
        } catch (SQLException e) {
            e.printStackTrace();
            return "KC-ERROR"; // Trường hợp lỗi khi tạo mã kết ca
        }
    }
    
    public String layMaNguoiDung() throws SQLException{
        String ma = null;
        if(taiKhoan.getVaiTro().equals("NhanVien")){
            NhanVien_DAO nv_DAO = new NhanVien_DAO();
            NhanVien nv = nv_DAO.layThongTinNhanVien(taiKhoan.getTenNguoiDung());
            ma = nv.getMaNhanVien();
        }else{
            QuanLy_DAO ql_dao = new QuanLy_DAO();
            QuanLy ql = ql_dao.layThongTinQuanLy(taiKhoan.getTenNguoiDung());
            ma = ql.getMaQuanLy();
        }
        
        return ma;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonMenu = new javax.swing.ButtonGroup();
        jPanelTong = new javax.swing.JPanel();
        jPanelLogo = new javax.swing.JPanel();
        jLabelLoGo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanelQuanLy = new javax.swing.JPanel();
        btnQuanLyBan = new javax.swing.JButton();
        btnQuanLyDatBan = new javax.swing.JButton();
        btnQuanLyMonAn = new javax.swing.JButton();
        btnQuanLyKhachHang = new javax.swing.JButton();
        btnQuanLyNhanVien = new javax.swing.JButton();
        btnQuanLyHoaDon = new javax.swing.JButton();
        btnQuanLyKhuyenMai = new javax.swing.JButton();
        btnThongKeBaoCao = new javax.swing.JButton();
        jPanelLight = new javax.swing.JPanel();
        btnLight = new javax.swing.JButton();
        btnDark = new javax.swing.JButton();
        jPanelCaiDat = new javax.swing.JPanel();
        btnHoTro = new javax.swing.JButton();
        btnThongTinNhanVien = new javax.swing.JButton();
        jLabelThoiGian = new javax.swing.JLabel();
        jLabelNhanTime = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanelChinh = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản Lý Đặt Bàn Trong Nhà Hàng OXY");
        setFocusCycleRoot(false);
        setLocationByPlatform(true);
        setResizable(false);

        jPanelTong.setBackground(new java.awt.Color(255, 255, 255));

        jPanelLogo.setBackground(new java.awt.Color(255, 255, 255));

        jLabelLoGo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/oxyLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanelLogoLayout = new javax.swing.GroupLayout(jPanelLogo);
        jPanelLogo.setLayout(jPanelLogoLayout);
        jPanelLogoLayout.setHorizontalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabelLoGo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelLogoLayout.setVerticalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogoLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelLoGo)
                .addContainerGap())
        );

        jPanelQuanLy.setBackground(new java.awt.Color(153, 255, 255));
        jPanelQuanLy.setLayout(new java.awt.GridLayout(8, 0));

        btnQuanLyBan.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyBan.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/DatBan.png"))); // NOI18N
        btnQuanLyBan.setText("Quản Lý Bàn");
        btnQuanLyBan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyBan.setBorderPainted(false);
        btnQuanLyBan.setFocusPainted(false);
        btnQuanLyBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyBanActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyBan);

        btnQuanLyDatBan.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyDatBan.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyDatBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Ban.png"))); // NOI18N
        btnQuanLyDatBan.setText("Quản Lý Đặt Bàn");
        btnQuanLyDatBan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyDatBan.setBorderPainted(false);
        btnQuanLyDatBan.setFocusPainted(false);
        btnQuanLyDatBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyDatBanActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyDatBan);

        btnQuanLyMonAn.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyMonAn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/GoiMon.png"))); // NOI18N
        btnQuanLyMonAn.setText("Quản Lý Món Ăn");
        btnQuanLyMonAn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyMonAn.setBorderPainted(false);
        btnQuanLyMonAn.setFocusPainted(false);
        btnQuanLyMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyMonAnActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyMonAn);

        btnQuanLyKhachHang.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyKhachHang.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/KhachHang.png"))); // NOI18N
        btnQuanLyKhachHang.setText("Quản Lý Khách Hàng");
        btnQuanLyKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyKhachHang.setBorderPainted(false);
        btnQuanLyKhachHang.setFocusPainted(false);
        btnQuanLyKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyKhachHangActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyKhachHang);

        btnQuanLyNhanVien.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyNhanVien.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/NhanVien.png"))); // NOI18N
        btnQuanLyNhanVien.setText("Quản Lý Nhân Viên");
        btnQuanLyNhanVien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyNhanVien.setBorderPainted(false);
        btnQuanLyNhanVien.setFocusPainted(false);
        btnQuanLyNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyNhanVienActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyNhanVien);

        btnQuanLyHoaDon.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/HoaDon.png"))); // NOI18N
        btnQuanLyHoaDon.setText("Quản Lý Hóa Đơn");
        btnQuanLyHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyHoaDon.setBorderPainted(false);
        btnQuanLyHoaDon.setFocusPainted(false);
        btnQuanLyHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyHoaDonActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyHoaDon);

        btnQuanLyKhuyenMai.setBackground(new java.awt.Color(153, 255, 255));
        btnQuanLyKhuyenMai.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuanLyKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/KhuyenMai.png"))); // NOI18N
        btnQuanLyKhuyenMai.setText("Quản Lý Khuyến Mãi");
        btnQuanLyKhuyenMai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnQuanLyKhuyenMai.setBorderPainted(false);
        btnQuanLyKhuyenMai.setFocusPainted(false);
        btnQuanLyKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyKhuyenMaiActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnQuanLyKhuyenMai);

        btnThongKeBaoCao.setBackground(new java.awt.Color(153, 255, 255));
        btnThongKeBaoCao.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnThongKeBaoCao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ThongKe.png"))); // NOI18N
        btnThongKeBaoCao.setText("Thống Kê Và Báo Cáo");
        btnThongKeBaoCao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThongKeBaoCao.setBorderPainted(false);
        btnThongKeBaoCao.setFocusPainted(false);
        btnThongKeBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeBaoCaoActionPerformed(evt);
            }
        });
        jPanelQuanLy.add(btnThongKeBaoCao);

        jPanelLight.setLayout(new java.awt.GridLayout(1, 2));

        btnLight.setBackground(new java.awt.Color(204, 255, 204));
        btnLight.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnLight.setForeground(new java.awt.Color(0, 51, 51));
        btnLight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/question (4).png"))); // NOI18N
        btnLight.setText("Light");
        btnLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLightActionPerformed(evt);
            }
        });
        jPanelLight.add(btnLight);

        btnDark.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.focusedBackground"));
        btnDark.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnDark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dark.png"))); // NOI18N
        btnDark.setText("Dark");
        btnDark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarkActionPerformed(evt);
            }
        });
        jPanelLight.add(btnDark);

        javax.swing.GroupLayout jPanelTongLayout = new javax.swing.GroupLayout(jPanelTong);
        jPanelTong.setLayout(jPanelTongLayout);
        jPanelTongLayout.setHorizontalGroup(
            jPanelTongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelTongLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelQuanLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelLight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelTongLayout.setVerticalGroup(
            jPanelTongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTongLayout.createSequentialGroup()
                .addComponent(jPanelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelQuanLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelLight, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelCaiDat.setBackground(new java.awt.Color(255, 255, 255));

        btnHoTro.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnHoTro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/9042837_question_mark_circle_icon.png"))); // NOI18N
        btnHoTro.setText("Hỗ trợ");
        btnHoTro.setBorderPainted(false);
        btnHoTro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoTroActionPerformed(evt);
            }
        });

        btnThongTinNhanVien.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnThongTinNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user.png"))); // NOI18N
        btnThongTinNhanVien.setText("Nguyễn Tuấn Anh");
        btnThongTinNhanVien.setBorderPainted(false);
        btnThongTinNhanVien.setFocusPainted(false);
        btnThongTinNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnThongTinNhanVien.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnThongTinNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongTinNhanVienActionPerformed(evt);
            }
        });

        jLabelThoiGian.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelThoiGian.setText("Time");

        jLabelNhanTime.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabelNhanTime.setText("Thời gian :");

        jButton1.setBackground(new java.awt.Color(255, 204, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/homeIcon.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Home");

        javax.swing.GroupLayout jPanelCaiDatLayout = new javax.swing.GroupLayout(jPanelCaiDat);
        jPanelCaiDat.setLayout(jPanelCaiDatLayout);
        jPanelCaiDatLayout.setHorizontalGroup(
            jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCaiDatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelNhanTime)
                .addGap(44, 44, 44)
                .addComponent(jLabelThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(222, 222, 222)
                .addComponent(btnHoTro)
                .addGap(18, 18, 18)
                .addComponent(btnThongTinNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelCaiDatLayout.setVerticalGroup(
            jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCaiDatLayout.createSequentialGroup()
                .addGroup(jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThongTinNhanVien)
                        .addComponent(btnHoTro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCaiDatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCaiDatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelNhanTime))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelCaiDatLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanelChinh.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChinh.setFocusable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelChinhLayout = new javax.swing.GroupLayout(jPanelChinh);
        jPanelChinh.setLayout(jPanelChinhLayout);
        jPanelChinhLayout.setHorizontalGroup(
            jPanelChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChinhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelChinhLayout.setVerticalGroup(
            jPanelChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChinhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCaiDat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCaiDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanelTong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private JPopupMenu popupMenu;

public void MyFrame() { 
    
        // Khởi tạo popupMenu
        popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Thông tin chi tiết");
        menuItem1.setFont(new Font("Arial", Font.PLAIN, 16)); 
        menuItem1.setPreferredSize(new Dimension(200, 30));
        JMenuItem menuItem2 = new JMenuItem("Đăng xuất");
        menuItem2.setFont(new Font("Arial", Font.PLAIN, 16)); 
        menuItem2.setPreferredSize(new Dimension(200, 30));
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        menuItem1.addActionListener(new ActionListener() {
   @Override
    public void actionPerformed(ActionEvent e) {
        ThongTinTaiKhoan tttk = null;
        try {
            tttk = new ThongTinTaiKhoan(taiKhoan);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhChinh.class.getName()).log(Level.SEVERE, null, ex);
        }
        tttk.setSize(800, 600);
        tttk.setLocationRelativeTo(null);
        tttk.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        tttk.setModal(true);
        tttk.setVisible(true);
    }
});

// Đăng ký sự kiện cho "Đăng xuất"
menuItem2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Xử lý khi người dùng chọn "Đăng xuất"
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
       dispose();
            DangNhap dangNhap=new DangNhap();
            dangNhap.setVisible(true);
            
        }
    }
});
        
    }

public void startClock() {
        // Tạo Timer để cập nhật thời gian
        Timer timer = new Timer(1000, new ActionListener() { // Cập nhật mỗi giây
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLabel(); // Gọi hàm cập nhật label
            }
        });
        timer.start(); // Bắt đầu timer
    }

    public void updateLabel() {
        // Lấy thời gian và ngày hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentTime = sdf.format(new Date());
        jLabelThoiGian.setText(currentTime); // Cập nhật text cho JLabel
    }
    
     private void setupCardLayout() {
        // Thiết lập CardLayout cho jPanel5
        jPanelChinh.setLayout(new java.awt.CardLayout());

        // Tạo jPanel5 và thêm vào layout với tên "card1"
        jPanelChinh.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChinh.setFocusable(false);
        
//        GoiMon goiMonPanel = new GoiMon();
//        jPanelChinh.add(goiMonPanel, "card3");
    }
    
    private void btnQuanLyBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyBanActionPerformed
        // TODO add your handling code here:
        ManHinhQuanLyBan quanLyBan = null;
        try {
            quanLyBan = new ManHinhQuanLyBan();
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhChinh.class.getName()).log(Level.SEVERE, null, ex);
        }
        JTabbedPane tabQLBan = new JTabbedPane();
        tabQLBan.addTab("Quản lý bàn", quanLyBan);
        
        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQLBan);
        jPanel7.validate();
        jPanel7.repaint();
        
//        JButton buttonDangChon = (JButton) evt.getSource();
//    
//        // Đổi màu của nút hiện tại
//        buttonDangChon.setForeground(Color.blue);
//    
//        // Nếu có nút trước đó thì đổi lại màu ban đầu
//        if (buttonTruoc != null && buttonTruoc != buttonDangChon) {
//            buttonTruoc.setForeground(Color.black);  // Đặt lại màu ban đầu
//        }
//    
//         // Cập nhật lại nút trước đó
//        buttonTruoc = buttonDangChon;
    }//GEN-LAST:event_btnQuanLyBanActionPerformed

    private void btnThongTinNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongTinNhanVienActionPerformed
        // TODO add your handling code here:
        btnThongTinNhanVien.setForeground(new Color(205 , 0 , 0 ));
        btnThongTinNhanVien.setBackground(Color.decode("#CAE5E8"));
        btnThongTinNhanVien.setFocusPainted(false);
        btnThongTinNhanVien.setBorderPainted(false);
        
          popupMenu.setPreferredSize(new Dimension(btnThongTinNhanVien.getWidth(), popupMenu.getPreferredSize().height));
          popupMenu.show(btnThongTinNhanVien, 0, btnThongTinNhanVien.getHeight());
        
    }//GEN-LAST:event_btnThongTinNhanVienActionPerformed

    private void btnHoTroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoTroActionPerformed
        // TODO add your handling code here:
            String url = "https://duchauuuuu.github.io/DHAUDEPTRYVCL/html_duchau/tintuc.html";

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop API không được hỗ trợ trên hệ thống này.");
        }
    }//GEN-LAST:event_btnHoTroActionPerformed

    private void btnQuanLyDatBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyDatBanActionPerformed
        // TODO add your handling code here:
        ManHinhDatBan datBan = null;
        try {
            datBan = new ManHinhDatBan(taiKhoan);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhChinh.class.getName()).log(Level.SEVERE, null, ex);
        }
        ManHinhLichSuDatBan lichSuDatBan = new ManHinhLichSuDatBan();
        JTabbedPane tabQLDatBan = new JTabbedPane();
        tabQLDatBan.addTab("Đặt bàn", datBan);
        tabQLDatBan.addTab("Lịch sử đặt bàn", lichSuDatBan);
        
        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQLDatBan);
        jPanel7.validate();
        jPanel7.repaint();
    }//GEN-LAST:event_btnQuanLyDatBanActionPerformed

    private void btnQuanLyMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyMonAnActionPerformed
        if(taiKhoan.getVaiTro().equalsIgnoreCase("QuanLy")){
            ManHinhThongTinMonAn thongTinMonAn = new ManHinhThongTinMonAn();
            JTabbedPane tabQLGoiMon = new JTabbedPane();
            tabQLGoiMon.addTab("Thông tin Món ăn", thongTinMonAn);

            jPanel7.removeAll();
            jPanel7.setLayout(new java.awt.BorderLayout());
            jPanel7.add(tabQLGoiMon);
            jPanel7.validate();
            jPanel7.repaint();
        }else{
            JOptionPane.showMessageDialog(this, "Bạn không có quyền sử dụng chức năng này");
        }
    }//GEN-LAST:event_btnQuanLyMonAnActionPerformed

    private void btnDarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarkActionPerformed
        // TODO add your handling code here:
        updateDarkMode();
    }//GEN-LAST:event_btnDarkActionPerformed

    private void btnLightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLightActionPerformed
          // TODO add your handling code here:
      updateLightMode();
    }//GEN-LAST:event_btnLightActionPerformed

    private void btnQuanLyKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyKhachHangActionPerformed
        // TODO add your handling code here:
        ManHinhQuanLyKhachHang quanLyKhachHang = new ManHinhQuanLyKhachHang();
        JTabbedPane tabQlKhachHang = new JTabbedPane();
        tabQlKhachHang.addTab("Quản lý Khách hàng", quanLyKhachHang);
        
        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQlKhachHang);
        jPanel7.validate();
        jPanel7.repaint();
    }//GEN-LAST:event_btnQuanLyKhachHangActionPerformed

    private void btnQuanLyNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyNhanVienActionPerformed
        if(taiKhoan.getVaiTro().equalsIgnoreCase("QuanLy")){
            ManHinhQuanLyNhanVien quanLyNhanVien = new ManHinhQuanLyNhanVien();
            JTabbedPane tabQLNhanVien = new JTabbedPane();
            tabQLNhanVien.addTab("Quản lý Nhân viên", quanLyNhanVien);

            jPanel7.removeAll();
            jPanel7.setLayout(new java.awt.BorderLayout());
            jPanel7.add(tabQLNhanVien);
            jPanel7.validate();
            jPanel7.repaint();
        }else{
            JOptionPane.showMessageDialog(this, "Bạn không có quyền sử dụng chức năng này");
        }
    }//GEN-LAST:event_btnQuanLyNhanVienActionPerformed

    private void btnQuanLyHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyHoaDonActionPerformed
        // TODO add your handling code here:
        ManHinhThanhToanHoaDon thanhToanHoaDon = new ManHinhThanhToanHoaDon();
        ManHinhThongTinHoaDon thongTinHoaDon = new ManHinhThongTinHoaDon();
        JTabbedPane tabQLHoaDon = new JTabbedPane();
        tabQLHoaDon.addTab("Thanh toán Hóa đơn", thanhToanHoaDon);
        tabQLHoaDon.addTab("Thông tin Hóa đơn", thongTinHoaDon);
        
        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQLHoaDon);
        jPanel7.validate();
        jPanel7.repaint();
    }//GEN-LAST:event_btnQuanLyHoaDonActionPerformed

    private void btnQuanLyKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyKhuyenMaiActionPerformed
       ManHinhTaoKhuyenMai taoKhuyenMai = new ManHinhTaoKhuyenMai();
        ManHinhThongTinKhuyenMai timKiemKhuyenMai = new ManHinhThongTinKhuyenMai();
        JTabbedPane tabQLKhuyenMai = new JTabbedPane();
        if(taiKhoan.getVaiTro().equalsIgnoreCase("QuanLy")){
            tabQLKhuyenMai.addTab("Tạo khuyến mãi", taoKhuyenMai);
            tabQLKhuyenMai.addTab("Thông tin khuyến mãi", timKiemKhuyenMai);
        }else{
            tabQLKhuyenMai.addTab("Thông tin khuyến mãi", timKiemKhuyenMai);
        }
        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQLKhuyenMai);
        jPanel7.validate();
        jPanel7.repaint();
    }//GEN-LAST:event_btnQuanLyKhuyenMaiActionPerformed

    private void btnThongKeBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeBaoCaoActionPerformed
        JTabbedPane tabQLThongKeVaBaoCao = new JTabbedPane();
        try {
            tabQLThongKeVaBaoCao.addTab("Thống kê", new ManHinhThongKe(taiKhoan));
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhChinh.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi tải màn hình Thống kê: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        try {
            tabQLThongKeVaBaoCao.addTab("Báo cáo", new ManHinhBaoCao(taiKhoan));
        } catch (Exception ex) {
            Logger.getLogger(ManHinhChinh.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi tải màn hình Báo cáo: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        jPanel7.removeAll();
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(tabQLThongKeVaBaoCao);
        jPanel7.validate();
        jPanel7.repaint();
    }//GEN-LAST:event_btnThongKeBaoCaoActionPerformed

    
 
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // Xóa tất cả thành phần trong JPanel7
       jPanel7.removeAll();
       jPanel7.setLayout(new java.awt.BorderLayout());

       // Đọc hình ảnh từ đường dẫn tương đối
       ImageIcon icon = new ImageIcon(getClass().getResource("/image/trangChu.jpg"));
       Image img = icon.getImage();  // Lấy hình ảnh gốc

       // Tạo hình ảnh mới với kích thước bằng với JPanel7
       Image scaledImg = img.getScaledInstance(jPanel7.getWidth(), jPanel7.getHeight(), Image.SCALE_SMOOTH);

       // Tạo JLabel với hình ảnh đã thay đổi kích thước
       JLabel label = new JLabel(new ImageIcon(scaledImg));

       // Thêm JLabel vào JPanel7
       jPanel7.add(label, BorderLayout.CENTER);

       // Xác thực và vẽ lại JPanel7
       jPanel7.validate();
       jPanel7.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ImageIcon icon = new ImageIcon(getClass().getResource("/image/trangChu.jpg"));
//        Image img = icon.getImage();  // Lấy hình ảnh gốc
//
//        // Tạo hình ảnh mới với kích thước bằng với JPanel7
//        Image scaledImg = img.getScaledInstance(jPanel7.getWidth(), jPanel7.getHeight(), Image.SCALE_SMOOTH);
//
//        // Tạo JLabel với hình ảnh đã thay đổi kích thước
//        JLabel label = new JLabel(new ImageIcon(scaledImg));
//
//        // Thêm JLabel vào JPanel7
//        jPanel7.add(label, BorderLayout.CENTER);
//               new ManHinhChinh(taiKhoan).setVisible(true);
//            }
//        });
//    }
    
    public static void main(String args[]) {
        ManHinhChinh manHinhChinh = new ManHinhChinh(taiKhoan);

        // Dừng lịch trình khi thoát chương trình
        Runtime.getRuntime().addShutdownHook(new Thread(manHinhChinh::dongManHinh));
    }
    
//    public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//    try {
//        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//            if ("Nimbus".equals(info.getName())) {
//                javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                break;
//            }
//        }
//    } catch (ClassNotFoundException ex) {
//        java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//    } catch (InstantiationException ex) {
//        java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//    } catch (IllegalAccessException ex) {
//        java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//        java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//    }
    //</editor-fold>

    /* Create and display the form */
//    java.awt.EventQueue.invokeLater(new Runnable() {
//        public void run() {
//            ManHinhChinh frame = new ManHinhChinh(taiKhoan);
//            frame.setVisible(true);
//
//            // Thiết lập hình ảnh sau khi JFrame được hiển thị
//            frame.jPanel7.addComponentListener(new java.awt.event.ComponentAdapter() {
//                @Override
//                public void componentResized(java.awt.event.ComponentEvent evt) {
//                    // Chỉ thực hiện khi panel có kích thước
//                    ImageIcon icon = new ImageIcon(getClass().getResource("/image/bunBo.jpg"));
//                    Image img = icon.getImage();
//
//                    // Điều chỉnh kích thước hình ảnh theo kích thước của jPanel7
//                    Image scaledImg = img.getScaledInstance(frame.jPanel7.getWidth(), frame.jPanel7.getHeight(), Image.SCALE_SMOOTH);
//
//                    // Tạo JLabel chứa hình ảnh
//                    JLabel label = new JLabel(new ImageIcon(scaledImg));
//
//                    System.out.println("OKk");
//                    // Xóa tất cả các thành phần trước đó và thêm hình ảnh
//                    frame.jPanel7.removeAll();
//                    frame.jPanel7.add(label, BorderLayout.CENTER);
//
//                    // Xác thực và vẽ lại jPanel7
//                    frame.jPanel7.revalidate();
//                    frame.jPanel7.repaint();
//                }
//            });
//        }
//    });
//}

//    public static void main(String args[]) {
//           /* Set the Nimbus look and feel */
//           //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//           /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//            */
//           try {
//               for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                   if ("Nimbus".equals(info.getName())) {
//                       javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                       break;
//                   }
//               }
//           } catch (ClassNotFoundException ex) {
//               java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//           } catch (InstantiationException ex) {
//               java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//           } catch (IllegalAccessException ex) {
//               java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//           } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//               java.util.logging.Logger.getLogger(ManHinhChinh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//           }
//           //</editor-fold>
//
//           /* Create and display the form */
//           java.awt.EventQueue.invokeLater(new Runnable() {
//               public void run() {
//                    new ManHinhChinh(taiKhoan).setVisible(true);
//                    System.out.println("OKkkkk");
//               }
//           });
//       }
//
//    
//    
        public void updateLightMode() {

        btnHoTro.setBackground(new java.awt.Color(240, 240, 240));
        btnHoTro.setForeground(java.awt.Color.BLACK);

        btnQuanLyBan.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyBan.setForeground(java.awt.Color.BLACK);

        btnQuanLyDatBan.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyDatBan.setForeground(java.awt.Color.BLACK);

        btnQuanLyMonAn.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyMonAn.setForeground(java.awt.Color.BLACK);

        btnQuanLyHoaDon.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyHoaDon.setForeground(java.awt.Color.BLACK);

        btnQuanLyKhachHang.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyKhachHang.setForeground(java.awt.Color.BLACK);

        btnQuanLyKhuyenMai.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyKhuyenMai.setForeground(java.awt.Color.BLACK);

        btnQuanLyNhanVien.setBackground(new java.awt.Color(240, 240, 240));
        btnQuanLyNhanVien.setForeground(java.awt.Color.BLACK);

        btnThongKeBaoCao.setBackground(new java.awt.Color(240, 240, 240));
        btnThongKeBaoCao.setForeground(java.awt.Color.BLACK);

        btnThongTinNhanVien.setBackground(new java.awt.Color(240, 240, 240));
        btnThongTinNhanVien.setForeground(java.awt.Color.BLACK);

        // Cập nhật màu cho các JLabel
        jLabel4.setForeground(java.awt.Color.BLACK);
        jLabelLoGo.setForeground(java.awt.Color.BLACK);
        jLabelNhanTime.setForeground(java.awt.Color.BLACK);
        jLabelThoiGian.setForeground(java.awt.Color.BLACK);

        // Cập nhật màu nền cho các JPanel
        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCaiDat.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChinh.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLight.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLogo.setBackground(new java.awt.Color(255, 255, 255));
        jPanelQuanLy.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTong.setBackground(new java.awt.Color(255, 255, 255));
    }
    public void updateDarkMode() {
    // Cập nhật màu cho các JButton
    

    btnHoTro.setBackground(new java.awt.Color(79, 79, 79));
    btnHoTro.setForeground(java.awt.Color.WHITE);

    btnQuanLyBan.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyBan.setForeground(java.awt.Color.WHITE);

    btnQuanLyDatBan.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyDatBan.setForeground(java.awt.Color.WHITE);

    btnQuanLyMonAn.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyMonAn.setForeground(java.awt.Color.WHITE);

    btnQuanLyHoaDon.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyHoaDon.setForeground(java.awt.Color.WHITE);

    btnQuanLyKhachHang.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyKhachHang.setForeground(java.awt.Color.WHITE);

    btnQuanLyKhuyenMai.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyKhuyenMai.setForeground(java.awt.Color.WHITE);

    btnQuanLyNhanVien.setBackground(new java.awt.Color(79, 79, 79));
    btnQuanLyNhanVien.setForeground(java.awt.Color.WHITE);

    btnThongKeBaoCao.setBackground(new java.awt.Color(79, 79, 79));
    btnThongKeBaoCao.setForeground(java.awt.Color.WHITE);

    btnThongTinNhanVien.setBackground(new java.awt.Color(79, 79, 79));
    btnThongTinNhanVien.setForeground(java.awt.Color.WHITE);

    // Cập nhật màu cho các JLabel
    jLabel4.setForeground(java.awt.Color.WHITE);
    jLabelLoGo.setForeground(java.awt.Color.WHITE);
    jLabelNhanTime.setForeground(java.awt.Color.WHITE);
    jLabelThoiGian.setForeground(java.awt.Color.WHITE);

    // Cập nhật màu nền cho các JPanel
    jPanel7.setBackground(new java.awt.Color(79, 79, 79)); // Màu nền "4f4f4f" cho JPanel
    jPanelCaiDat.setBackground(new java.awt.Color(79, 79, 79));
    jPanelChinh.setBackground(new java.awt.Color(79, 79, 79));
    jPanelLight.setBackground(new java.awt.Color(79, 79, 79));
    jPanelLogo.setBackground(new java.awt.Color(79, 79, 79));
    jPanelQuanLy.setBackground(new java.awt.Color(79, 79, 79));
    jPanelTong.setBackground(new java.awt.Color(79, 79, 79));
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDark;
    private javax.swing.JButton btnHoTro;
    private javax.swing.JButton btnLight;
    private javax.swing.JButton btnQuanLyBan;
    private javax.swing.JButton btnQuanLyDatBan;
    private javax.swing.JButton btnQuanLyHoaDon;
    private javax.swing.JButton btnQuanLyKhachHang;
    private javax.swing.JButton btnQuanLyKhuyenMai;
    private javax.swing.JButton btnQuanLyMonAn;
    private javax.swing.JButton btnQuanLyNhanVien;
    private javax.swing.JButton btnThongKeBaoCao;
    private javax.swing.JButton btnThongTinNhanVien;
    private javax.swing.ButtonGroup buttonMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelLoGo;
    private javax.swing.JLabel jLabelNhanTime;
    private javax.swing.JLabel jLabelThoiGian;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelCaiDat;
    private javax.swing.JPanel jPanelChinh;
    private javax.swing.JPanel jPanelLight;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JPanel jPanelQuanLy;
    private javax.swing.JPanel jPanelTong;
    // End of variables declaration//GEN-END:variables
}
