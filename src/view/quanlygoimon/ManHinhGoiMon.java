/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.quanlygoimon;


import converter.GiaTienConverter;
import converter.TongTien;
import dao.BanAn_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import static dao.MonAn_DAO.layMonAnKhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modal.BanAn;
import modal.ChiTietHoaDon;
import modal.HoaDon;
import modal.KhachHang;
import modal.MonAn;
import modal.MonAnKhuyenMai;
import modal.NhanVien;
import modal.TaiKhoan;

/**
 *
 * @author Nguyễn Thế Anh
 */
public class ManHinhGoiMon extends javax.swing.JPanel {

     private Window parentWindow;
    private List<MonAn> danhSachGoiMon;
    MonAn_DAO monAnn = new MonAn_DAO();
    HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
    
//     private final String maNhanVien;
     private final String maBan;
      private TaiKhoan  taiKhoan;
     private Map<String, List<MonAn>> gioHangTheoBan = new HashMap<>();

    
    /**
     * Creates new form ManHinhGoiMon
     */
    public ManHinhGoiMon(String maBan, Window parentWindow, TaiKhoan taiKhoan) throws SQLException{

        initComponents();
        this.parentWindow = parentWindow;
        this.taiKhoan = taiKhoan;
        this.maBan = maBan;
        danhSachGoiMon = new ArrayList<>();
        
            MonAn_DAO monAnn = new MonAn_DAO();
        if (maBan.isEmpty()) {
            layTatCaMOnAnVaoPanel();
        } else {
            HoaDon hd = hoaDon_DAO.layHoaDon1(maBan);
            if (hd != null) {
                jtfMaBan.setText(hd.getBanAn().getMaBan());
                jtfTenKh.setText(hd.getKhachHang().getTenKhachHang());
                jtfSdt.setText(hd.getKhachHang().getSoDienThoai());

                HoaDon_DAO hd_DAO = new HoaDon_DAO();
                try {
                    // Lấy danh sách món ăn đã gọi cho giỏ hàng
                    List<MonAn> gioHang = monAnn.layMonAnDaGoiTheoBan(maBan);
//                    gioHangTheoBan.put(maBan, gioHang);

                    // Kiểm tra trạng thái thanh toán của hóa đơn
                    if (hd_DAO.kiemTraHoaDonThanhToan(maBan)) {
                        System.out.println("Đã tìm thấy hóa đơn thanh toán cho bàn: " + maBan);

                        if (gioHangTheoBan.containsKey(maBan) && gioHangTheoBan.get(maBan) != null) {
                            gioHangTheoBan.get(maBan).clear();
                            System.out.println("Đã xóa giỏ hàng cho bàn: " + maBan);
                        } else {
                            System.out.println("Giỏ hàng không tồn tại hoặc đã rỗng cho bàn: " + maBan);
                        }

                        // Làm mới giao diện nếu hóa đơn đã thanh toán
                        jPanelGoiMon.removeAll();
                        jPanelGoiMon.revalidate();
                        jPanelGoiMon.repaint();
                    } else {
                        System.out.println("Hóa đơn chưa thanh toán, giữ nguyên danh sách món ăn." + maBan);
                         gioHangTheoBan.put(maBan, gioHang);
                        hienThiGioHang(maBan); // Hiển thị giỏ hàng nếu hóa đơn chưa thanh toán
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
                }

                layTatCaMOnAnVaoPanel();
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin khách hàng cho mã bàn này.");
            }
        }

        hienThiGioHang(maBan); // Hiển thị giỏ hàng khi khởi tạo màn hình

//        MonAn_DAO monAnn = new MonAn_DAO();
//        if (maBan.isEmpty()) {
//            layTatCaMOnAnVaoPanel();
//        } else {
//            HoaDon hd = hoaDon_DAO.layHoaDon1(maBan);
//            if (hd != null) {
//                jtfMaBan.setText(hd.getBanAn().getMaBan());
//                jtfTenKh.setText(hd.getKhachHang().getTenKhachHang());
//                jtfSdt.setText(hd.getKhachHang().getSoDienThoai());
//
//                HoaDon_DAO hd_DAO = new HoaDon_DAO();
//                // Lấy danh sách món ăn đã gọi cho giỏ hàng
//                List<MonAn> gioHang = monAnn.layMonAnDaGoiTheoBan(maBan);
//                gioHangTheoBan.put(maBan, gioHang);
                
//                    if (hd_DAO.kiemTraHoaDonThanhToan(maBan)) {
//                        System.out.println("Đã tìm thấy hóa đơn thanh toán cho bàn: " + maBan);
//
//                        if (gioHangTheoBan.containsKey(maBan) && gioHangTheoBan.get(maBan) != null) {
//                            gioHangTheoBan.get(maBan).clear();
//                            // Làm mới giao diện
//                        jPanelGoiMon.removeAll();
//                        jPanelGoiMon.revalidate();
//                        jPanelGoiMon.repaint();
//                            System.out.println("Đã xóa giỏ hàng cho bàn: " + maBan);
//                        } else {
//                            System.out.println("Giỏ hàng không tồn tại hoặc đã rỗng cho bàn: " + maBan);
//                        } 
//                    }



//                // Lấy danh sách món ăn đã gọi cho giỏ hàng
//                List<MonAn> gioHang = monAnn.layMonAnDaGoiTheoBan(maBan);
//                gioHangTheoBan.put(maBan, gioHang);

//                layTatCaMOnAnVaoPanel();
//            } else {
//                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin khách hàng cho mã bàn này.");
//            }
//        }
//
//        hienThiGioHang(maBan);
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelLapHoaDon = new javax.swing.JPanel();
        jPanelChucNang = new javax.swing.JPanel();
        btnHuyMon = new javax.swing.JButton();
        btnLuuHoaDon = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfTenKh = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfSdt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfMaBan = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanelGoiMon = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelDanhMucMonAn = new javax.swing.JPanel();
        btnKhaiVi = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnTrangMieng = new javax.swing.JButton();
        btnDoUong = new javax.swing.JButton();
        jPanelDuoi = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabelTtMonAn = new javax.swing.JLabel();
        jtfTongTienMonAn = new javax.swing.JTextField();
        jLabelGiamGia = new javax.swing.JLabel();
        jtfGiamGia = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabelTongCong = new javax.swing.JLabel();
        jtfTongCong = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtfTimKiemMonAn = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelTongMonAn = new javax.swing.JPanel();

        jPanelChucNang.setBackground(new java.awt.Color(255, 255, 255));

        btnHuyMon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew.png"))); // NOI18N
        btnHuyMon.setText("Hủy tất cả món");
        btnHuyMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyMonActionPerformed(evt);
            }
        });

        btnLuuHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/restaurant.png"))); // NOI18N
        btnLuuHoaDon.setText("Lưu hóa đơn");
        btnLuuHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelChucNangLayout = new javax.swing.GroupLayout(jPanelChucNang);
        jPanelChucNang.setLayout(jPanelChucNangLayout);
        jPanelChucNangLayout.setHorizontalGroup(
            jPanelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHuyMon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnLuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelChucNangLayout.setVerticalGroup(
            jPanelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLuuHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelChucNangLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnHuyMon, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Tên khách hàng: ");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Số điện thoại: ");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Mã bàn:");

        jtfMaBan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 153), null, null, new java.awt.Color(255, 204, 0)));
        jtfMaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaBanActionPerformed(evt);
            }
        });

        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfMaBan, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(jtfSdt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jtfTenKh, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtfTenKh, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtfSdt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfMaBan)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5))))
        );

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanelGoiMon.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGoiMon.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách gọi món", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jPanelGoiMon.setLayout(new javax.swing.BoxLayout(jPanelGoiMon, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane3.setViewportView(jPanelGoiMon);

        javax.swing.GroupLayout jPanelLapHoaDonLayout = new javax.swing.GroupLayout(jPanelLapHoaDon);
        jPanelLapHoaDon.setLayout(jPanelLapHoaDonLayout);
        jPanelLapHoaDonLayout.setHorizontalGroup(
            jPanelLapHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLapHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLapHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanelLapHoaDonLayout.setVerticalGroup(
            jPanelLapHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLapHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(800, 109));

        jPanelDanhMucMonAn.setBackground(new java.awt.Color(255, 255, 204));
        jPanelDanhMucMonAn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanelDanhMucMonAn.setPreferredSize(new java.awt.Dimension(800, 107));

        btnKhaiVi.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnKhaiVi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/diet.png"))); // NOI18N
        btnKhaiVi.setText("Món khai vị");
        btnKhaiVi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhaiViActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bibimbap.png"))); // NOI18N
        jButton2.setText("Món chính");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/fast-food.png"))); // NOI18N
        jButton3.setText("Combo & Set");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnTrangMieng.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnTrangMieng.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dessert.png"))); // NOI18N
        btnTrangMieng.setText("Món tráng miệng");
        btnTrangMieng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrangMiengActionPerformed(evt);
            }
        });

        btnDoUong.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnDoUong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/drink.png"))); // NOI18N
        btnDoUong.setText("Đồ uống");
        btnDoUong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoUongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDanhMucMonAnLayout = new javax.swing.GroupLayout(jPanelDanhMucMonAn);
        jPanelDanhMucMonAn.setLayout(jPanelDanhMucMonAnLayout);
        jPanelDanhMucMonAnLayout.setHorizontalGroup(
            jPanelDanhMucMonAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDanhMucMonAnLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btnKhaiVi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTrangMieng, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDoUong, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDanhMucMonAnLayout.setVerticalGroup(
            jPanelDanhMucMonAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDanhMucMonAnLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelDanhMucMonAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnKhaiVi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDanhMucMonAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTrangMieng, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDoUong, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanelDanhMucMonAn);

        jPanelDuoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel9.setBackground(new java.awt.Color(242, 246, 215));

        jLabelTtMonAn.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTtMonAn.setText("Tổng tiền món ăn: ");

        jLabelGiamGia.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelGiamGia.setText("Giảm giá: ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelTtMonAn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfTongTienMonAn, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(jtfGiamGia))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTtMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTongTienMonAn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jtfGiamGia)
                        .addGap(6, 6, 6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(242, 246, 215));

        jLabelTongCong.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTongCong.setText("Tổng cộng: ");

        jtfTongCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTongCongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelTongCong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfTongCong, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTongCong, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTongCong, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/send.png"))); // NOI18N
        jButton1.setText("Gửi thực đơn cho bếp");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        jButton4.setLabel("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Tìm kiếm món ăn : ");

        jtfTimKiemMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTimKiemMonAnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfTimKiemMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTimKiemMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanelDuoiLayout = new javax.swing.GroupLayout(jPanelDuoi);
        jPanelDuoi.setLayout(jPanelDuoiLayout);
        jPanelDuoiLayout.setHorizontalGroup(
            jPanelDuoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDuoiLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDuoiLayout.setVerticalGroup(
            jPanelDuoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanelTongMonAn.setLayout(new java.awt.GridLayout(0, 4, 10, 10));
        jScrollPane2.setViewportView(jPanelTongMonAn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelDuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelLapHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanelLapHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
 
    private void capNhatGioHang(String maBan, MonAn monAn) {
        gioHangTheoBan.putIfAbsent(maBan, new ArrayList<>());
        List<MonAn> gioHang = gioHangTheoBan.get(maBan);

        // Kiểm tra xem món ăn đã tồn tại trong giỏ hàng hay chưa
        for (MonAn existingMonAn : gioHang) {
            if (existingMonAn.getMaMonAn().equals(monAn.getMaMonAn())) {
                JOptionPane.showMessageDialog(null, "Món ăn đã được chọn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        gioHang.add(monAn);
    }
    
    private void hienThiGioHang(String maBan) {
        jPanelGoiMon.removeAll(); 

        List<MonAn> gioHang = gioHangTheoBan.getOrDefault(maBan, new ArrayList<>());
        for (MonAn monAn : gioHang) {
            double giaGoc = monAn.getGiaMonAn();
            double giaKhuyenMai = giaGoc;

            // Kiểm tra khuyến mãi
            for (MonAnKhuyenMai monAnKM : layMonAnKhuyenMai()) {
                if (monAnKM.getMonAn().getMaMonAn().equals(monAn.getMaMonAn())) {
                    giaKhuyenMai = giaGoc * (1 - monAnKM.getPhanTramGiamGia() / 100);
                    break;
                }
            }

            JPanel monAnPanel = taoPanelMonAn(monAn, giaGoc, giaKhuyenMai);
            jPanelGoiMon.add(monAnPanel);
        }

        jPanelGoiMon.revalidate();
        jPanelGoiMon.repaint();
    }
    
    private void themMonVaoGioHang2(MonAn monAn) {
        capNhatGioHang(maBan, monAn);
        hienThiGioHang(maBan);
    }



    
    private void btnHuyMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyMonActionPerformed

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy tất cả các món trong giỏ hàng không?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            gioHang.clear();
            jPanelGoiMon.removeAll();
            jLabelTongCong.setText("0 VND"); 
            capNhatTongTien(); 
            jPanelGoiMon.revalidate(); 
            jPanelGoiMon.repaint(); 
            JOptionPane.showMessageDialog(this, "Tất cả món ăn đã được hủy.");
        }
    }//GEN-LAST:event_btnHuyMonActionPerformed

    private void btnLuuHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuHoaDonActionPerformed

        
       HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
    BanAn_DAO banAn_DAO = new BanAn_DAO();
    BanAn ban = null;

    try {
        ban = banAn_DAO.layThongTinBanAn(jtfMaBan.getText());
    } catch (SQLException ex) {
        Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Kiểm tra tổng số lượng món ăn trong giỏ hàng
    int tongSoLuong = 0;
    for (MonAn monAn : gioHang) {
        JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
        int soLuong = Integer.parseInt(textFieldSoLuong.getText());
        tongSoLuong += soLuong;
    }

    if (tongSoLuong <= 0) {
        JOptionPane.showMessageDialog(this, "Tổng số lượng món ăn trong giỏ hàng phải lớn hơn 0!");
        return;
    }

    if (ban != null && (ban.getTrangThaiBanAn().equals("Đã đặt") || ban.getTrangThaiBanAn().equals("Đang sử dụng"))) {
        // Kiểm tra xem đã tồn tại hóa đơn hợp lệ
        HoaDon existingHoaDon = hoaDon_DAO.timHoaDonTheoMaBanVaTrangThai12(jtfMaBan.getText());

        if (existingHoaDon == null || !existingHoaDon.getTrangThaiThanhToan().equals("Chưa thanh toán")) {
            JOptionPane.showMessageDialog(this, "Hóa đơn không hợp lệ hoặc đã thanh toán!");
            return;
        }



        // Thêm hoặc cập nhật chi tiết hóa đơn
        ChiTietHoaDon_DAO ct_DAO = new ChiTietHoaDon_DAO();
        for (MonAn monAn : gioHang) {
            JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
            int soLuong = Integer.parseInt(textFieldSoLuong.getText());
            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
            chiTiet.setMonAn(monAn);
            chiTiet.setSoLuongMonAn(soLuong);
            chiTiet.setHoaDon(existingHoaDon);
            chiTiet.setThanhTien(soLuong * monAn.getGiaMonAn());
            
            boolean result = ct_DAO.themVaoHoaHoaCu(chiTiet);
            if (!result) {
                System.out.println("Có lỗi xảy ra khi thêm chi tiết hóa đơn.");
            }
        }

        // Cập nhật tổng tiền và VAT của hóa đơn
        existingHoaDon.setTongTien(existingHoaDon.tinhTongTien());
        existingHoaDon.setVAT(0.1 * existingHoaDon.getTongTien());

        JOptionPane.showMessageDialog(this, "Đã thêm món ăn vào hóa đơn thành công!");
    } else {
        JOptionPane.showMessageDialog(this, "Bàn ăn không khả dụng. Vui lòng chọn bàn khác!");
    }
//        
//        
//        HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
//        BanAn_DAO banAn_DAO = new BanAn_DAO();
//        BanAn ban = null;
//
//        try {
//            ban = banAn_DAO.layThongTinBanAn(jtfMaBan.getText());
//        } catch (SQLException ex) {
//            Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        // Kiểm tra tổng số lượng món ăn trong giỏ hàng
//        int tongSoLuong = 0;
//        for (MonAn monAn : gioHang) {
//            JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
//            int soLuong = Integer.parseInt(textFieldSoLuong.getText());
//            tongSoLuong += soLuong; 
//        }
//        if (tongSoLuong <= 0) {
//            JOptionPane.showMessageDialog(this, "Tổng số lượng món ăn trong giỏ hàng phải lớn hơn 0!");
//            return; 
//        }
//
//        if (ban != null && (ban.getTrangThaiBanAn().equals("Đã đặt") || ban.getTrangThaiBanAn().equals("Đang sử dụng"))) {
//        // Kiểm tra xem đã tồn tại hóa đơn hợp lệ
//        HoaDon existingHoaDon = hoaDon_DAO.timHoaDonTheoMaBanVaTrangThai12(jtfMaBan.getText());
//        HoaDon hd;
//        if (existingHoaDon != null) {
//            // Sử dụng hóa đơn đã tồn tại
//            hd = existingHoaDon;
//        } else {
//            // Tạo hóa đơn mới
//            hd = new HoaDon();
//            hd.setBanAn(ban);
//            hd.setNgayGioLap(LocalDateTime.now());
//            try {
//                hd.setKhachHang(layThongTinKhachHangSDT());
//                hd.setSoLuongKhach(gioHang.size());
//            } catch (SQLException ex) {
//                Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            hd.setNhanVien(new NhanVien(maNhanVien));
//            try {
//                HoaDon newHoaDon = hoaDon_DAO.lapHoaDon1(hd, "Chưa thanh toán");
//                if (newHoaDon != null) {
//                    hd.setMaHoaDon(newHoaDon.getMaHoaDon());
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(ManHinhGoiMon.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        // Thêm hoặc cập nhật chi tiết hóa đơn
//        ChiTietHoaDon_DAO ct_DAO = new ChiTietHoaDon_DAO();
//        for (MonAn monAn : gioHang) {
//            JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
//            int soLuong = Integer.parseInt(textFieldSoLuong.getText());
//            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
//            chiTiet.setMonAn(monAn);
//            chiTiet.setSoLuongMonAn(soLuong);
//            chiTiet.setHoaDon(hd);
//            chiTiet.setThanhTien(soLuong * monAn.getGiaMonAn());
//            boolean result = ct_DAO.themVaoHoaHoaCu(chiTiet);
//            if (!result) {
//                System.out.println("Có lỗi xảy ra khi thêm chi tiết hóa đơn.");
//            }
//        }
//
//        // Cập nhật trạng thái hóa đơn
//        hd.setTongTien(hd.tinhTongTien());
//        hd.setVAT(0.1 * hd.getTongTien());
//
//        JOptionPane.showMessageDialog(this, "Hóa đơn đã được lưu thành công!");
//    } else {
//        JOptionPane.showMessageDialog(this, "Bàn ăn không khả dụng. Vui lòng chọn bàn khác!");
//    }


      
         
    }//GEN-LAST:event_btnLuuHoaDonActionPerformed


    
    private KhachHang layThongTinKhachHangSDT() throws SQLException {
        String soDienThoai = jtfSdt.getText(); 
        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();

        // Lấy thông tin khách hàng từ DAO
        KhachHang khachHang = khachHang_DAO.timKhachHangTheoSDT(soDienThoai);

        if (khachHang != null) {
            return khachHang; 
        } else {
            JOptionPane.showMessageDialog(this, "Khách hàng chưa tạo tài khoản!");
            return null; 
        }
    }   

    
    private void btnKhaiViActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhaiViActionPerformed
        List<MonAn> danhSachKhaiVi = MonAn_DAO.danhSachMonAnTheoDanhMuc("DM001");

         // Xóa các món ăn hiện tại đang hiển thị (nếu có)
         jPanelTongMonAn.removeAll();

         if (danhSachKhaiVi != null && !danhSachKhaiVi.isEmpty()) {
             System.out.println("Tìm thấy danh mục DM002");

             // Tạo layout cho jPanelTongMonAn
             jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachKhaiVi) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
         } else {
             JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btnKhaiViActionPerformed

    private void btnTrangMiengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrangMiengActionPerformed
        // TODO add your handling code here:
           // Lấy danh sách món ăn theo danh mục "KHAIVI" (ví dụ mã là "DM001")
         List<MonAn> danhSachKhaiVi = MonAn_DAO.danhSachMonAnTheoDanhMuc("DM003");

         // Xóa các món ăn hiện tại đang hiển thị (nếu có)
         jPanelTongMonAn.removeAll();

         if (danhSachKhaiVi != null && !danhSachKhaiVi.isEmpty()) {
             System.out.println("Tìm thấy danh mục DM003");

             // Tạo layout cho jPanelTongMonAn
             jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachKhaiVi) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
         } else {
             JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btnTrangMiengActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
           // Lấy danh sách món ăn theo danh mục "KHAIVI" (ví dụ mã là "DM001")
         List<MonAn> danhSachKhaiVi = MonAn_DAO.danhSachMonAnTheoDanhMuc("DM005");

         // Xóa các món ăn hiện tại đang hiển thị (nếu có)
         jPanelTongMonAn.removeAll();

         if (danhSachKhaiVi != null && !danhSachKhaiVi.isEmpty()) {
             System.out.println("Tìm thấy danh mục DM005");

             // Tạo layout cho jPanelTongMonAn
             jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachKhaiVi) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
         } else {
             JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
         // Tạo một JFrameHoaDon mới và hiển thị nó
        FrameHoaDon frameHoaDon = new FrameHoaDon();
       // Kiểm tra xem FrameHoaDon đã tồn tại và đang hiển thị chưa
    if (frameHoaDon == null || !frameHoaDon.isShowing()) {
        // Tạo một JFrameHoaDon mới và hiển thị nó
        frameHoaDon = new FrameHoaDon();
        frameHoaDon.setVisible(true);
        frameHoaDon.setResizable(true);
        frameHoaDon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Đặt JFrame ở giữa màn hình
        frameHoaDon.setLocationRelativeTo(null);
    } else {
        // Nếu frameHoaDon đã tồn tại và đang hiển thị, đưa nó lên trước
        frameHoaDon.toFront();
    }
        
        // Lấy table model của bảng JTable trên JFrameHoaDon
        DefaultTableModel model = (DefaultTableModel) frameHoaDon.getjTableHoaDon().getModel();

        // Xóa các hàng hiện có trong bảng (nếu có)
        model.setRowCount(0);

        // Duyệt qua các món ăn trong giỏ hàng và thêm chúng vào bảng
        int stt = 1;  // Số thứ tự
        for (MonAn monAn : gioHang) {
            double giaGoc = monAn.getGiaMonAn();
            double giamGia = 0;
            double thanhTien = giaGoc;

            // Kiểm tra xem món ăn có khuyến mãi không
            for (MonAnKhuyenMai monAnKM : layMonAnKhuyenMai()) {
                if (monAnKM.getMonAn().getMaMonAn().equals(monAn.getMaMonAn())) {
                    giamGia = monAnKM.getPhanTramGiamGia();
                    thanhTien = giaGoc * (1 - giamGia / 100);
                    break;
                }
            }

            JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
//            if (textFieldSoLuong != null) {
                int soLuong1 = Integer.parseInt(textFieldSoLuong.getText());
            // Tạo hàng mới và thêm vào bảng
            Object[] row = {
                stt++,  // Số thứ tự
                monAn.getMaMonAn(),  // Mã món ăn
                monAn.getTenMonAn(), // Tên món ăn
                soLuong1,  
                giaGoc,  // Giá gốc
                giamGia,  // Giảm giá (%)
                thanhTien  // Thành tiền sau khi giảm giá
            };
            model.addRow(row);
        }

        // Sau khi cập nhật xong, bạn có thể revalidate và repaint nếu cần
        frameHoaDon.getjTableHoaDon().revalidate();
        frameHoaDon.getjTableHoaDon().repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        layTatCaMOnAnVaoPanel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jtfMaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaBanActionPerformed
        // TODO add your handling code here:
        HoaDon hoaDon_DAO = HoaDon_DAO.hienThiThongTinKhachHang(jtfMaBan.getText());
        if(hoaDon_DAO != null){
            jtfTenKh.setText(hoaDon_DAO.getKhachHang().getTenKhachHang());
            jtfSdt.setText(hoaDon_DAO.getKhachHang().getSoDienThoai());
        }else{
           JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin khách hàng cho mã bàn này.");
        }
        
    }//GEN-LAST:event_jtfMaBanActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
           // Lấy danh sách món ăn theo danh mục "KHAIVI" (ví dụ mã là "DM001")
         List<MonAn> danhSachKhaiVi = MonAn_DAO.danhSachMonAnTheoDanhMuc("DM002");

         // Xóa các món ăn hiện tại đang hiển thị (nếu có)
         jPanelTongMonAn.removeAll();

         if (danhSachKhaiVi != null && !danhSachKhaiVi.isEmpty()) {
             System.out.println("Tìm thấy danh mục DM002");

             // Tạo layout cho jPanelTongMonAn
             jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachKhaiVi) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
         } else {
             JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnDoUongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoUongActionPerformed
        // TODO add your handling code here:
           // Lấy danh sách món ăn theo danh mục "KHAIVI" (ví dụ mã là "DM001")
         List<MonAn> danhSachKhaiVi = MonAn_DAO.danhSachMonAnTheoDanhMuc("DM004");

         // Xóa các món ăn hiện tại đang hiển thị (nếu có)
         jPanelTongMonAn.removeAll();

         if (danhSachKhaiVi != null && !danhSachKhaiVi.isEmpty()) {
             System.out.println("Tìm thấy danh mục DM004");

             // Tạo layout cho jPanelTongMonAn
             jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachKhaiVi) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
         } else {
             JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_btnDoUongActionPerformed

    private void jtfTimKiemMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTimKiemMonAnActionPerformed
      // Lấy giá trị từ JTextField (tên món ăn tìm kiếm)
    String tenMonAn = jtfTimKiemMonAn.getText().trim();

    MonAn_DAO m = new MonAn_DAO();
    
    if (tenMonAn.isEmpty()) {
        // Nếu tên món ăn tìm kiếm trống, hiển thị tất cả món ăn
        jPanelTongMonAn.removeAll();  // Xóa tất cả món ăn cũ
        layTatCaMOnAnVaoPanel();  // Tải lại tất cả món ăn từ cơ sở dữ liệu
    } else {
        jPanelTongMonAn.removeAll();
        // Nếu có tên món ăn tìm kiếm, gọi hàm timKiemMonAn để lấy kết quả tìm kiếm
        List<MonAn> danhSachMonAn = m.timKiemMonAn1(tenMonAn);
        jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10));

             // Hiển thị các món ăn từ danh sách khai vị
             for (MonAn monAn : danhSachMonAn) {
                 JPanel monAnPanel = new JPanel();
                 monAnPanel.setLayout(new BorderLayout());
                 monAnPanel.setOpaque(true);
                 monAnPanel.setPreferredSize(new Dimension(200, 250));
                 monAnPanel.setBackground(Color.LIGHT_GRAY);

                 // Tạo JButton chứa ảnh món ăn
                 JButton buttonAnh = new JButton();
                 buttonAnh.setPreferredSize(new Dimension(150, 100));
                 buttonAnh.setOpaque(true);
                 buttonAnh.setBackground(Color.WHITE);
                 buttonAnh.setBorder(BorderFactory.createEmptyBorder());

                 String duongDanAnh = monAn.getAnhMonAn();
                 if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
                     java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);
                     if (imageURL != null) {
                         ImageIcon icon = new ImageIcon(imageURL);
                         Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     } else {
                         // Nếu không tìm thấy ảnh từ đường dẫn, sử dụng ảnh mặc định
                         ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
                         Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                         buttonAnh.setIcon(new ImageIcon(img));
                     }
                 }

                 // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
                 buttonAnh.addActionListener(e -> {
                     themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
                 });

                 // Thêm JButton vào panel món ăn
                 monAnPanel.add(buttonAnh, BorderLayout.CENTER);

                 // Tạo panel chứa thông tin tên và giá món ăn
                 JPanel thongTinPanel = new JPanel();
                 thongTinPanel.setLayout(new GridLayout(2, 1));
                 thongTinPanel.setBackground(Color.WHITE);

                 // Thêm tên món ăn
                 JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
                 textFieldTenMonAn.setEditable(false);
                 textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
                 textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 thongTinPanel.add(textFieldTenMonAn);

                 // Thêm giá món ăn
                 JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
                 textFieldGiaMonAn.setEditable(false);
                 textFieldGiaMonAn.setForeground(Color.RED);
                 textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
                 textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
                 textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
                 thongTinPanel.add(textFieldGiaMonAn);

                 // Thêm thông tin panel vào phần dưới của monAnPanel
                 monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

                 // Thêm monAnPanel vào jPanelTongMonAn
                 jPanelTongMonAn.add(monAnPanel);
             }

             // Cập nhật lại giao diện
             jPanelTongMonAn.revalidate();
             jPanelTongMonAn.repaint();
       
        }

    
    }//GEN-LAST:event_jtfTimKiemMonAnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       if (parentWindow != null) {
           parentWindow.dispose();  // Hoặc parentWindow.dispose();
            System.out.println("Ha");
        } else {
            System.out.println("Parent window is null");
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jtfTongCongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTongCongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTongCongActionPerformed

  

    

public void layTatCaMOnAnVaoPanel() {
    jPanelTongMonAn.setLayout(new GridLayout(0, 4, 10, 10)); 

    List<MonAn> danhSachMonAn = MonAn_DAO.layTatCaMonAn();

    // Duyệt qua từng món ăn và thêm vào panel
    for (MonAn monAn : danhSachMonAn) {
        JPanel monAnPanel = new JPanel();
        monAnPanel.setLayout(new BorderLayout());
        monAnPanel.setOpaque(true);  // Đảm bảo monAnPanel có thể tô màu
        monAnPanel.setPreferredSize(new Dimension(200, 250));
        monAnPanel.setBackground(Color.LIGHT_GRAY);

        // Tạo JButton chứa ảnh món ăn
        JButton buttonAnh = new JButton();
        buttonAnh.setPreferredSize(new Dimension(150, 100));
        buttonAnh.setOpaque(true);
        buttonAnh.setBackground(Color.WHITE);
        buttonAnh.setBorder(BorderFactory.createEmptyBorder());  // Xóa viền nút


        String duongDanAnh = monAn.getAnhMonAn();
        if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
            // Sử dụng ClassLoader để lấy đường dẫn tương đối từ resource
            java.net.URL imageURL = getClass().getClassLoader().getResource(duongDanAnh);

            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image img = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
                buttonAnh.setIcon(new ImageIcon(img));
//                System.out.println("Ảnh được lấy và hiển thị thành công từ đường dẫn tương đối.");
            } else {
//                System.out.println("Không tìm thấy file ảnh từ đường dẫn tương đối: " + duongDanAnh);
            }
        } else {
            // Sử dụng ảnh mặc định
            ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("duong_dan_hinh_anh_mac_dinh.jpg"));
            Image img = defaultIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            buttonAnh.setIcon(new ImageIcon(img));
//            System.out.println("view.quanlygoimon.ManHinhGoiMon.layTatCaMOnAnVaoPanel()");
        }



        // Thêm ActionListener cho JButton để thêm món ăn vào giỏ hàng
        buttonAnh.addActionListener(e -> {
            themMonVaoGioHang(monAn);  // Gọi hàm thêm vào giỏ hàng
        });

        // Thêm JButton vào panel món ăn
        monAnPanel.add(buttonAnh, BorderLayout.CENTER);

        // Tạo panel chứa thông tin tên và giá món ăn
        JPanel thongTinPanel = new JPanel();
        thongTinPanel.setLayout(new GridLayout(2, 1));
        thongTinPanel.setBackground(Color.WHITE);

        // Thêm tên món ăn
        JTextField textFieldTenMonAn = new JTextField(monAn.getTenMonAn());
        textFieldTenMonAn.setEditable(false);
        textFieldTenMonAn.setHorizontalAlignment(JTextField.CENTER);
        textFieldTenMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
        thongTinPanel.add(textFieldTenMonAn);

        // Thêm giá món ăn
        JTextField textFieldGiaMonAn = new JTextField(GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()));
        textFieldGiaMonAn.setEditable(false);
        textFieldGiaMonAn.setForeground(Color.RED);
        textFieldGiaMonAn.setBackground(Color.LIGHT_GRAY);
        textFieldGiaMonAn.setFont(new Font("Time News Roman", Font.BOLD, 16));
        textFieldGiaMonAn.setHorizontalAlignment(JTextField.CENTER);
        thongTinPanel.add(textFieldGiaMonAn);

        // Thêm thông tin panel vào phần dưới của monAnPanel
        monAnPanel.add(thongTinPanel, BorderLayout.SOUTH);

        // Thêm monAnPanel vào JPanelTongMonAn
        jPanelTongMonAn.add(monAnPanel);
    }

    // Cập nhật lại giao diện
    jPanelTongMonAn.revalidate();
    jPanelTongMonAn.repaint();
}




    List<MonAn> gioHang = new ArrayList<>();
//    JTextField textFieldSoLuong;
    
    
    
        public void themMonVaoGioHang(MonAn monAn) {
            List<MonAnKhuyenMai> danhSachMonAnKhuyenMai = layMonAnKhuyenMai();
            double giaGoc = monAn.getGiaMonAn();
            double giaKhuyenMai = giaGoc;

            // Kiểm tra xem món ăn này có khuyến mãi hay không
            for (MonAnKhuyenMai monAnKM : danhSachMonAnKhuyenMai) {
                if (monAnKM.getMonAn().getMaMonAn().equals(monAn.getMaMonAn())) {
                    giaKhuyenMai = giaGoc * (1 - monAnKM.getPhanTramGiamGia() / 100);
                    break;
                }
            }

            // Kiểm tra nếu món ăn đã có trong giỏ hàng
            for (MonAn existingMonAn : gioHang) {
                if (existingMonAn.getMaMonAn().equals(monAn.getMaMonAn())) {
                    JOptionPane.showMessageDialog(null, "Món ăn đã được chọn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return; 
                }
            }

            // Tạo panel mới cho món ăn
            JPanel monAnPanel = taoPanelMonAn(monAn, giaGoc, giaKhuyenMai);
            if (monAnPanel == null) return; // Kiểm tra xem có lỗi khi tạo panel không

            
             
            
            // Thêm món ăn vào giỏ hàng và cập nhật tổng tiền
            gioHang.add(monAn);
            jPanelGoiMon.add(monAnPanel);
            capNhatTongTien();
            jPanelGoiMon.revalidate();
            jPanelGoiMon.repaint();
    }
    private Map<MonAn, JTextField> textFieldSoLuongMap = new HashMap<>();
     private Map<MonAn, JLabel> tfTenMonAn = new HashMap<>();
    
    private JPanel taoPanelMonAn(MonAn monAn, double giaGoc, double giaKhuyenMai) {
        JPanel monAnPanel = new JPanel();
        monAnPanel.setLayout(new GridBagLayout());
        monAnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        monAnPanel.setPreferredSize(new Dimension(300, 100));
        monAnPanel.setMaximumSize(new Dimension(300, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tên món ăn
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel labelTenMonAn = new JLabel(monAn.getTenMonAn());
        labelTenMonAn.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(labelTenMonAn, gbc);

        // Giá món ăn (có gạch ngang nếu có khuyến mãi)
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        if (giaKhuyenMai < giaGoc) {
            JLabel labelGiaGoc = new JLabel(GiaTienConverter.chuyenDoiTien(giaGoc));
            labelGiaGoc.setFont(new Font("Arial", Font.PLAIN, 12));
            labelGiaGoc.setForeground(Color.BLACK);
            // Thêm dấu gạch ngang
            Font fontGoc = labelGiaGoc.getFont();
            Map<TextAttribute, Object> attributes = new HashMap<>(fontGoc.getAttributes());
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            labelGiaGoc.setFont(fontGoc.deriveFont(attributes));
            monAnPanel.add(labelGiaGoc, gbc);

            gbc.gridy = 2; 
            JLabel labelGiaKhuyenMai = new JLabel(GiaTienConverter.chuyenDoiTien(giaKhuyenMai));
            labelGiaKhuyenMai.setFont(new Font("Arial", Font.PLAIN, 12));
            labelGiaKhuyenMai.setForeground(Color.RED);
            monAnPanel.add(labelGiaKhuyenMai, gbc);
        } else {
            JLabel labelGiaMonAn = new JLabel(GiaTienConverter.chuyenDoiTien(giaGoc));
            labelGiaMonAn.setForeground(Color.RED);
            labelGiaMonAn.setFont(new Font("Arial", Font.PLAIN, 12));
            monAnPanel.add(labelGiaMonAn, gbc);
        }

        // Số lượng và các nút điều khiển
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelSoLuong = new JLabel();
        labelSoLuong.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(labelSoLuong, gbc);

        // Tạo một JTextField riêng cho mỗi món ăn
        JTextField textFieldSoLuong = new JTextField("1", 3);
        textFieldSoLuong.setHorizontalAlignment(JTextField.CENTER);
        textFieldSoLuong.setEditable(true);
        textFieldSoLuong.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(textFieldSoLuong, gbc);

        // Nút tăng số lượng
        gbc.gridx = 4;
        JButton buttonTang = new JButton("+");
        buttonTang.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(buttonTang, gbc);

        // Nút giảm số lượng
        gbc.gridy = 1;
        JButton buttonGiam = new JButton("-");
        buttonGiam.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(buttonGiam, gbc);

        // Tổng tiền món ăn
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        JLabel labelTongTien = new JLabel(GiaTienConverter.chuyenDoiTien(giaKhuyenMai));
        labelTongTien.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(labelTongTien, gbc);

        // Nút xóa món ăn
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        JButton buttonXoa = new JButton("Xóa");
        buttonXoa.setFont(new Font("Arial", Font.PLAIN, 12));
        monAnPanel.add(buttonXoa, gbc);

        final double[] giaKhuyenMaiArray = {giaKhuyenMai};

        // Action listener cho các nút
        buttonTang.addActionListener(e -> capNhatSoLuong(textFieldSoLuong, labelTongTien, giaKhuyenMaiArray[0], 1));
        buttonGiam.addActionListener(e -> capNhatSoLuong(textFieldSoLuong, labelTongTien, giaKhuyenMaiArray[0], -1));
        textFieldSoLuong.addActionListener(e -> capNhatTongTienFromTextField(textFieldSoLuong, labelTongTien, giaKhuyenMaiArray[0]));

        buttonXoa.addActionListener(e -> {
            jPanelGoiMon.remove(monAnPanel);
            jPanelGoiMon.revalidate();
            jPanelGoiMon.repaint();
            gioHang.remove(monAn);
            capNhatTongTien();
        });
        textFieldSoLuongMap.put(monAn, textFieldSoLuong);
        tfTenMonAn.put(monAn, labelTenMonAn);
        return monAnPanel;
    }

    private void capNhatTongTienFromTextField(JTextField textFieldSoLuong, JLabel labelTongTien, double giaKhuyenMai) {
        try {
            int soLuong = Integer.parseInt(textFieldSoLuong.getText());
            if(soLuong <= 0){
               JOptionPane.showMessageDialog(this, "Số lượng món ăn phải lớn hơn 0. Vui lòng chọn lại !!");
                textFieldSoLuong.setText("1");  
                labelTongTien.setText(GiaTienConverter.chuyenDoiTien(giaKhuyenMai));
                capNhatTongTien();
                return;

            }
            
        for (Map.Entry<MonAn, JTextField> entry : textFieldSoLuongMap.entrySet()) {
            MonAn monAn = entry.getKey();
            JTextField tfSoLuong = entry.getValue();
            if (tfSoLuong.equals(textFieldSoLuong)) {
                String maMonAn = monAn.getMaMonAn();  
                MonAn monAnDB = MonAn_DAO.layMonAnByMa(maMonAn); 

                if (monAnDB == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy món ăn trong cơ sở dữ liệu.");
                    return;
                }
                if (monAnDB.getSoLuong() < soLuong) {
                    JOptionPane.showMessageDialog(this, "Số lượng món ăn không đủ. Hiện tại còn " + monAnDB.getSoLuong() + " cho món ăn này!");
                    return;
                }
            
            }
        }
            double tongTien = giaKhuyenMai * soLuong;
            labelTongTien.setText(GiaTienConverter.chuyenDoiTien(tongTien));
            capNhatTongTien();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ!");
        }
    }

    private void capNhatSoLuong(JTextField textFieldSoLuong, JLabel labelTongTien, double giaKhuyenMai, int delta) {
        
        
        
        int soLuong = Integer.parseInt(textFieldSoLuong.getText()); 
        if (soLuong + delta > 0) { 
            
            soLuong += delta; 
            textFieldSoLuong.setText(String.valueOf(soLuong)); 

            double tongTien = giaKhuyenMai * soLuong;
            labelTongTien.setText(GiaTienConverter.chuyenDoiTien(tongTien));
            capNhatTongTien();
        }else{
            JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0");
        }
    }


    private void capNhatTongTien() {
        double tongTienHienTai = 0.0;
        double giaGiam = 0.0;

        for (MonAn monAn : gioHang) {
            List<MonAnKhuyenMai> danhSachMonAnKhuyenMai = layMonAnKhuyenMai();
            double giaGoc = monAn.getGiaMonAn();
            double giaKhuyenMai = giaGoc;

            for (MonAnKhuyenMai monAnKM : danhSachMonAnKhuyenMai) {
                if (monAnKM.getMonAn().getMaMonAn().equals(monAn.getMaMonAn())) {
                    giaKhuyenMai = giaGoc * (1 - monAnKM.getPhanTramGiamGia() / 100);
                    break;
                }
            }

            // Lấy số lượng từ textFieldSoLuong map
            JTextField textFieldSoLuong = textFieldSoLuongMap.get(monAn);
            if (textFieldSoLuong != null) {
                int soLuong = Integer.parseInt(textFieldSoLuong.getText());
//                tongTienHienTai += giaKhuyenMai * soLuong;
                    tongTienHienTai += giaGoc * soLuong;
                giaGiam += (giaGoc - giaKhuyenMai) * soLuong;
            }
        }

        jtfTongTienMonAn.setText(GiaTienConverter.chuyenDoiTien(tongTienHienTai));
        jtfGiamGia.setText(GiaTienConverter.chuyenDoiTien(giaGiam));
        capNhatTongCong();
    }

    // Hàm để cập nhật tổng cộng
    public void capNhatTongCong(){
        double tongTien = Double.parseDouble(jtfTongTienMonAn.getText().replace(" VNĐ", "").replace(",", ""));
        jtfTongTienMonAn.setText(GiaTienConverter.chuyenDoiTien(tongTien*1000));
        double giaGiam = Double.parseDouble(jtfGiamGia.getText().replace(" VNĐ", "").replace(",", ""));
        jtfGiamGia.setText(GiaTienConverter.chuyenDoiTien(giaGiam*1000));
        double tongCong = tongTien - giaGiam;
        jtfTongCong.setText(GiaTienConverter.chuyenDoiTien(tongCong*1000));
    }

    



//    public void themMonVaoGioHang(MonAn monAn) {
//    List<MonAnKhuyenMai> danhSachMonAnKhuyenMai = layMonAnKhuyenMai();
//    double giaGoc = monAn.getGiaMonAn();
//    double giaKhuyenMai = giaGoc;
//
//    // Kiểm tra xem món ăn này có khuyến mãi hay không
//    for (MonAnKhuyenMai monAnKM : danhSachMonAnKhuyenMai) {
//        if (monAnKM.getMonAn().getMaMonAn().equals(monAn.getMaMonAn())) {
//            giaKhuyenMai = giaGoc * (1 - monAnKM.getPhanTramGiamGia() / 100);
//            break;
//        }
//    }
//
//    for (Component comp : jPanelGoiMon.getComponents()) {
//        if (comp instanceof JPanel) {
//            JPanel existingPanel = (JPanel) comp;
//
//            // Tìm JLabel chứa tên món ăn
//            JLabel labelTenMonAn = null;
//            JLabel labelTongTien = null;
//
//            for (Component childComp : existingPanel.getComponents()) {
//                if (childComp instanceof JLabel && ((JLabel) childComp).getText().equals(monAn.getTenMonAn())) {
//                    labelTenMonAn = (JLabel) childComp;
//                }
//                if (childComp instanceof JTextField) {
//                    textFieldSoLuong = (JTextField) childComp; // Tham chiếu đúng đến JTextField
//                }
//                if (childComp instanceof JLabel && childComp != labelTenMonAn) { // Tránh nhầm lẫn với labelTên món
//                    labelTongTien = (JLabel) childComp; // Gán đúng cho label tổng tiền
//                }
//            }
//
//
//            // Kiểm tra nếu tìm thấy món ăn
//            if (labelTenMonAn != null) {
//                JOptionPane.showMessageDialog(null, "Món ăn đã được thêm vào giỏ hàng!");
//                return; // Dừng vì món đã có
//            }
//            capNhatTongTienMonAn();
//        }
//        
//    }
//    
//    // Kiểm tra nếu món ăn đã có trong giỏ hàng
//    for (MonAn existingMonAn : gioHang) {
//        if (existingMonAn.getMaMonAn().equals(monAn.getMaMonAn())) {
//            JOptionPane.showMessageDialog(null, "Món ăn đã được chọn", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            return; // Dừng nếu món đã có
//        }
//    }   
//
//
//    // Nếu món chưa có, tạo panel mới cho món ăn
//    JPanel monAnPanel = new JPanel();
//    monAnPanel.setLayout(new GridBagLayout());
//    monAnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
//    monAnPanel.setPreferredSize(new Dimension(370, 100)); // Đặt kích thước cố định cho mỗi món ăn
//    monAnPanel.setMaximumSize(new Dimension(370, 100));  // Kích thước tối đa
//
//    GridBagConstraints gbc = new GridBagConstraints();
//    gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
//
//    // Tên món ăn
//    gbc.gridx = 0;
//    gbc.gridy = 0;
//    gbc.gridwidth = 2;
//    gbc.anchor = GridBagConstraints.WEST;
//    JLabel labelTenMonAn = new JLabel(monAn.getTenMonAn());
//    labelTenMonAn.setFont(new Font("Arial", Font.PLAIN, 12)); // Kích thước chữ nhỏ hơn
//    monAnPanel.add(labelTenMonAn, gbc);
//
//    // Giá món ăn (có gạch ngang nếu có khuyến mãi)
//    gbc.gridy = 1;
//    gbc.gridwidth = 2;
//
//    // Nếu có khuyến mãi, hiển thị giá gốc với dấu gạch ngang và giá khuyến mãi bên dưới
//    if (giaKhuyenMai < giaGoc) {
//        JLabel labelGiaGoc = new JLabel(GiaTienConverter.chuyenDoiTien(giaGoc));
//        labelGiaGoc.setFont(new Font("Arial", Font.PLAIN, 12));
//        labelGiaGoc.setForeground(Color.BLACK);
//
//        // Thêm dấu gạch ngang
//        Font fontGoc = labelGiaGoc.getFont();
//        Map<TextAttribute, Object> attributes = new HashMap<>(fontGoc.getAttributes());
//        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
//        labelGiaGoc.setFont(fontGoc.deriveFont(attributes));
//
//        monAnPanel.add(labelGiaGoc, gbc);
//
//        // Thêm giá khuyến mãi bên dưới
//        gbc.gridy = 2; // Di chuyển xuống dòng tiếp theo
//        JLabel labelGiaKhuyenMai = new JLabel(GiaTienConverter.chuyenDoiTien(giaKhuyenMai));
//        labelGiaKhuyenMai.setFont(new Font("Arial", Font.PLAIN, 12)); // Kích thước chữ nhỏ hơn
//        labelGiaKhuyenMai.setForeground(Color.RED);
//        monAnPanel.add(labelGiaKhuyenMai, gbc);
//    } else {
//        JLabel labelGiaMonAn = new JLabel(GiaTienConverter.chuyenDoiTien(giaGoc));
//        labelGiaMonAn.setForeground(Color.RED);
//        labelGiaMonAn.setFont(new Font("Arial", Font.PLAIN, 12)); // Kích thước chữ nhỏ hơn
//        monAnPanel.add(labelGiaMonAn, gbc);
//    }
//
//    // Số lượng món ăn
//    gbc.gridx = 2;
//    gbc.gridy = 0;
//    gbc.gridwidth = 1;
//    gbc.anchor = GridBagConstraints.CENTER;
//    JLabel labelSoLuong = new JLabel();
//    labelSoLuong.setFont(new Font("Arial", Font.PLAIN, 12)); // Kích thước chữ nhỏ hơn
//    monAnPanel.add(labelSoLuong, gbc);
//
//    // JTextField hiển thị số lượng
//    gbc.gridx = 3;
//    textFieldSoLuong = new JTextField("1", 3);
//    textFieldSoLuong.setHorizontalAlignment(JTextField.CENTER);
//    textFieldSoLuong.setEditable(true);
//    textFieldSoLuong.setFont(new Font("Arial", Font.PLAIN, 12)); // Kích thước chữ nhỏ hơn
//    monAnPanel.add(textFieldSoLuong, gbc);
//
//    
//
//    // Nút tăng số lượng
//    gbc.gridx = 4;
//    gbc.gridy = 0;
//    JButton buttonTang = new JButton("+");
//    buttonTang.setFont(new Font("Arial", Font.PLAIN, 12)); 
//    monAnPanel.add(buttonTang, gbc);
//
//    // Nút giảm số lượng
//    gbc.gridy = 1;
//    JButton buttonGiam = new JButton("-");
//    buttonGiam.setFont(new Font("Arial", Font.PLAIN, 12));
//    monAnPanel.add(buttonGiam, gbc);
//
//    // Tổng tiền món ăn
//    gbc.gridx = 5;
//    gbc.gridy = 0;
//    gbc.gridheight = 2;
//    JLabel labelTongTien = new JLabel(GiaTienConverter.chuyenDoiTien(giaKhuyenMai));
//    labelTongTien.setFont(new Font("Arial", Font.PLAIN, 12));
//    monAnPanel.add(labelTongTien, gbc);
//
//    // Nút xóa món ăn
//    gbc.gridx = 6;
//    gbc.gridheight = 2;
//    JButton buttonXoa = new JButton("Xóa");
//    buttonXoa.setFont(new Font("Arial", Font.PLAIN, 12)); 
//    monAnPanel.add(buttonXoa, gbc);
//
//   final double[] giaKhuyenMaiArray = {giaKhuyenMai};
//
//   buttonTang.addActionListener(e -> {
//    int soLuong = Integer.parseInt(textFieldSoLuong.getText()); // Sử dụng textFieldSoLuong1
//    textFieldSoLuong.setText(String.valueOf(soLuong + 1));
//
//    double tongTien = giaKhuyenMaiArray[0] * (soLuong + 1);
//    labelTongTien.setText(GiaTienConverter.chuyenDoiTien(tongTien));
//    capNhatTongTienMonAn();
//    });
//
//    buttonGiam.addActionListener(e -> {
//        int soLuong = Integer.parseInt(textFieldSoLuong.getText()); // Sử dụng textFieldSoLuong1
//        if (soLuong > 1) {
//            textFieldSoLuong.setText(String.valueOf(soLuong - 1));
//
//            double tongTien = giaKhuyenMaiArray[0] * (soLuong - 1);
//            labelTongTien.setText(GiaTienConverter.chuyenDoiTien(tongTien));
//            capNhatTongTienMonAn();
//        }
//    });
//
//    
//        textFieldSoLuong.addActionListener(e -> {
//        try {
//            int soLuong = Integer.parseInt(textFieldSoLuong.getText()); // Sử dụng textFieldSoLuong1
//            double tongTien = giaKhuyenMaiArray[0] * soLuong;
//            labelTongTien.setText(GiaTienConverter.chuyenDoiTien(tongTien));
//            capNhatTongTienMonAn();
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ!");
//        }
//    });
//
//
//
//    buttonXoa.addActionListener(e -> {
//        jPanelGoiMon.remove(monAnPanel);
//        jPanelGoiMon.revalidate();
//        jPanelGoiMon.repaint();
//         gioHang.remove(monAn);
//         capNhatTongTienMonAn();
//    });
//    
//
//     gioHang.add(monAn); 
//
//    // Cập nhật tổng tiền cho món ăn mới thêm
//     capNhatTongTienMonAn(); 
//    // Thêm panel món ăn vào jPanelGioHang
//    jPanelGoiMon.add(monAnPanel);
//    jPanelGoiMon.revalidate();
//    jPanelGoiMon.repaint();
//}

   







    








    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoUong;
    private javax.swing.JButton btnHuyMon;
    private javax.swing.JButton btnKhaiVi;
    private javax.swing.JButton btnLuuHoaDon;
    private javax.swing.JButton btnTrangMieng;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelGiamGia;
    private javax.swing.JLabel jLabelTongCong;
    private javax.swing.JLabel jLabelTtMonAn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelChucNang;
    private javax.swing.JPanel jPanelDanhMucMonAn;
    private javax.swing.JPanel jPanelDuoi;
    private javax.swing.JPanel jPanelGoiMon;
    private javax.swing.JPanel jPanelLapHoaDon;
    private javax.swing.JPanel jPanelTongMonAn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jtfGiamGia;
    private javax.swing.JTextField jtfMaBan;
    private javax.swing.JTextField jtfSdt;
    private javax.swing.JTextField jtfTenKh;
    private javax.swing.JTextField jtfTimKiemMonAn;
    private javax.swing.JTextField jtfTongCong;
    private javax.swing.JTextField jtfTongTienMonAn;
    // End of variables declaration//GEN-END:variables
}
