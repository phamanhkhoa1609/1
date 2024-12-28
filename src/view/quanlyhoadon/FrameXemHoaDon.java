// Nguyen Duc Hau 13/10/2024

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.quanlyhoadon;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import converter.GiaTienConverter;
import dao.BanAn_DAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modal.ChiTietHoaDon;
import modal.HoaDon;
import modal.MonAn;
import dao.ChiTietHoaDon_DAO;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMaiKhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.MonAnKhuyenMai_DAO;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import modal.KhachHang;
import modal.KhuyenMai;
import modal.KhuyenMaiKhachHang;
import modal.MonAnKhuyenMai;
import dao.MonAnKhuyenMai_DAO;
import dao.MonAn_DAO;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import javax.swing.ImageIcon;
/**
 *
 * @author ADMIN
 */
public class FrameXemHoaDon extends javax.swing.JFrame {
    private HoaDon hoaDon; // Đối tượng HoaDon
//    private ChiTietHoaDon chiTietHoaDon;
    /**
     * Creates new form FrameThanhToan
     */
     // Constructor nhận đối tượng HoaDon
        public FrameXemHoaDon(HoaDon hoaDon) {
            this.hoaDon = hoaDon;  // Lưu đối tượng HoaDon vào biến instance
            initComponents();  // Khởi tạo các thành phần giao diện
            setLocationRelativeTo(null);  // Căn giữa cửa sổ

            // Hiển thị thông tin hóa đơn
            hienThiThongTinHoaDon();

        }

        // Hàm để hiển thị thông tin hóa đơn
        private void hienThiThongTinHoaDon() {
            // Hiển thị thông tin mã hóa đơn
        jTextFieldmaHoaDon.setText(hoaDon.getMaHoaDon());

        // Hiển thị giờ lập hóa đơn
        // Kiểm tra xem ngày giờ lập có null không trước khi định dạng
    if (hoaDon.getNgayGioLap() != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = hoaDon.getNgayGioLap().format(formatter);
        jTextFieldgioLap.setText(formattedDate);
    } else {
        jTextFieldgioLap.setText("Không có thông tin"); 
    }


        // Hiển thị mã bàn
        jTextFieldmaBan.setText(hoaDon.getBanAn().getMaBan());

        // Hiển thị số điện thoại khách hàng
        jTextFieldsoDienThoaiKhachHang.setText(hoaDon.getKhachHang().getSoDienThoai());

        // Hiển thị tên khách hàng
        jTextFieldtenKhachHang.setText(hoaDon.getKhachHang().getTenKhachHang());

         // Hiển thị tên nhân viên
       if(hoaDon.getNhanVien()!=null){
        jTextFieldtenNhanVien.setText(hoaDon.getNhanVien().getHo()+" "+ hoaDon.getNhanVien().getTen());
       }
       else {
      jTextFieldtenNhanVien.setText(hoaDon.getQuanLy().getHo()+" "+hoaDon.getQuanLy().getTen());
           
       }

        // Hiển thị số lượng khách hàng
        jTextFieldsoLuongKhachHang.setText(String.valueOf(hoaDon.getSoLuongKhach()));

        // Hiển thị danh sách món ăn
        DefaultTableModel model = (DefaultTableModel) jTabledanhSachMonAn.getModel();
        model.setRowCount(0); // Xóa các dòng cũ
        double tongTienMonAn = 0.0; // Khởi tạo biến tổng tiền món ăn
       // Lấy danh sách món ăn từ hóa đơn hiện tại
        ChiTietHoaDon_DAO chiTietHoaDonDao=new ChiTietHoaDon_DAO();
        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonDao.getAllChiTietHoaDon(hoaDon.getMaHoaDon());
        hoaDon.setChiTietHoaDons(chiTietHoaDonList);
   
            MonAnKhuyenMai_DAO monAnKhuyenMaiDao=new MonAnKhuyenMai_DAO();
            List<MonAnKhuyenMai> monAnKhuyenMaiList=monAnKhuyenMaiDao.getAllMonAnKhuyenMai();
    // Duyệt qua danh sách chi tiết hóa đơn
    for (ChiTietHoaDon chiTiet : chiTietHoaDonList) {
        MonAn monAn = chiTiet.getMonAn();
        double phanTramGiamGia = 0.0; // Mặc định không có khuyến mãi

        // Kiểm tra xem món ăn có khuyến mãi không
        for (MonAnKhuyenMai monkm : monAnKhuyenMaiList) {
            if (monAn.getMaMonAn().equals(monkm.getMonAn().getMaMonAn())) {
                // Nếu có khuyến mãi, lấy phần trăm giảm giá
                phanTramGiamGia = monkm.getPhanTramGiamGia();
                break; // Đã tìm thấy, không cần tiếp tục duyệt
            }
        }

        // Tính thành tiền sau khi áp dụng khuyến mãi (nếu có)
        double thanhTien = (monAn.getGiaMonAn() * chiTiet.getSoLuongMonAn()) - 
                           ((monAn.getGiaMonAn() * chiTiet.getSoLuongMonAn()) * phanTramGiamGia / 100);
        tongTienMonAn += thanhTien; // Cộng dồn tổng tiền món ăn

        // Thêm dòng vào bảng danh sách món ăn
        model.addRow(new Object[]{
            monAn.getMaMonAn(),
            monAn.getTenMonAn(),
            GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()),
            chiTiet.getSoLuongMonAn(),
            GiaTienConverter.chuyenDoiPhanTram(phanTramGiamGia),
            GiaTienConverter.chuyenDoiTien(thanhTien)
        });
        }
        // Hiển thị tổng tiền món ăn
        jTextFieldtongTienMonAn.setText(GiaTienConverter.chuyenDoiTien(tongTienMonAn));
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelmaKhachHang = new javax.swing.JLabel();
        jTextFieldmaBan = new javax.swing.JTextField();
        jLabelmaKhachHang1 = new javax.swing.JLabel();
        jTextFieldtenKhachHang = new javax.swing.JTextField();
        jLabelmaKhachHang2 = new javax.swing.JLabel();
        jTextFieldgioLap = new javax.swing.JTextField();
        jLabelmaKhachHang3 = new javax.swing.JLabel();
        jTextFieldsoDienThoaiKhachHang = new javax.swing.JTextField();
        jLabelmaKhachHang4 = new javax.swing.JLabel();
        jTextFieldtenNhanVien = new javax.swing.JTextField();
        jLabelmaKhachHang5 = new javax.swing.JLabel();
        jTextFieldsoLuongKhachHang = new javax.swing.JTextField();
        jLabelmaKhachHang6 = new javax.swing.JLabel();
        jTextFieldmaHoaDon = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabledanhSachMonAn = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldtongTienMonAn = new javax.swing.JTextField();
        jButtonXoaMonAn = new javax.swing.JButton();
        jButtonCapNhatHoaDon = new javax.swing.JButton();
        jButtonHuyHoaDon = new javax.swing.JButton();
        jButtonXoaTatCaMonAn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Hóa đơn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabelmaKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang.setText("Mã bàn:");

        jTextFieldmaBan.setEditable(false);
        jTextFieldmaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmaBanActionPerformed(evt);
            }
        });

        jLabelmaKhachHang1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang1.setText("Tên khách hàng:");

        jTextFieldtenKhachHang.setEditable(false);
        jTextFieldtenKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtenKhachHangActionPerformed(evt);
            }
        });

        jLabelmaKhachHang2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang2.setText("Giờ lập:");

        jTextFieldgioLap.setEditable(false);
        jTextFieldgioLap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgioLapActionPerformed(evt);
            }
        });

        jLabelmaKhachHang3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang3.setText("Số điện thoại khách hàng:");

        jTextFieldsoDienThoaiKhachHang.setEditable(false);
        jTextFieldsoDienThoaiKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsoDienThoaiKhachHangActionPerformed(evt);
            }
        });

        jLabelmaKhachHang4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang4.setText("Tên nhân viên:");

        jTextFieldtenNhanVien.setEditable(false);
        jTextFieldtenNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtenNhanVienActionPerformed(evt);
            }
        });

        jLabelmaKhachHang5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang5.setText("Số lượng khách hàng:");

        jTextFieldsoLuongKhachHang.setEditable(false);
        jTextFieldsoLuongKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsoLuongKhachHangActionPerformed(evt);
            }
        });

        jLabelmaKhachHang6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang6.setText("Mã hóa đơn:");

        jTextFieldmaHoaDon.setEditable(false);
        jTextFieldmaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmaHoaDonActionPerformed(evt);
            }
        });

        jTabledanhSachMonAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã món ăn", "Tên món ăn", "Giá món ăn", "Số lượng", "Giảm giá", "Số tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabledanhSachMonAn.setRowHeight(40);
        jTabledanhSachMonAn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTabledanhSachMonAnKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTabledanhSachMonAn);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Danh sách món ăn");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Tổng tiền món ăn:");

        jTextFieldtongTienMonAn.setEditable(false);
        jTextFieldtongTienMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtongTienMonAnActionPerformed(evt);
            }
        });

        jButtonXoaMonAn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonXoaMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew.png"))); // NOI18N
        jButtonXoaMonAn.setText("Xóa món ăn");
        jButtonXoaMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaMonAnActionPerformed(evt);
            }
        });

        jButtonCapNhatHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonCapNhatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        jButtonCapNhatHoaDon.setText("Cập nhật hóa đơn");
        jButtonCapNhatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCapNhatHoaDonActionPerformed(evt);
            }
        });

        jButtonHuyHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonHuyHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        jButtonHuyHoaDon.setText("Hủy hóa đơn");
        jButtonHuyHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHuyHoaDonActionPerformed(evt);
            }
        });

        jButtonXoaTatCaMonAn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonXoaTatCaMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        jButtonXoaTatCaMonAn.setText("Xóa tất cả món ăn");
        jButtonXoaTatCaMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaTatCaMonAnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelmaKhachHang3)
                            .addComponent(jLabelmaKhachHang2)
                            .addComponent(jLabelmaKhachHang4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldtenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldsoDienThoaiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldgioLap, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelmaKhachHang1)
                            .addComponent(jLabelmaKhachHang)
                            .addComponent(jLabelmaKhachHang6)
                            .addComponent(jLabelmaKhachHang5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldmaBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldmaHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldtenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldsoLuongKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(24, 24, 24)
                .addComponent(jTextFieldtongTienMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jButtonXoaMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCapNhatHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonHuyHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonXoaTatCaMonAn)))
                .addGap(0, 137, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldmaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelmaKhachHang6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldgioLap, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldmaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelmaKhachHang2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelmaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldsoDienThoaiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldtenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldtenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldsoLuongKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldtongTienMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonXoaMonAn)
                    .addComponent(jButtonCapNhatHoaDon)
                    .addComponent(jButtonHuyHoaDon)
                    .addComponent(jButtonXoaTatCaMonAn))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldmaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmaBanActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmaBanActionPerformed

    private void jTextFieldtenKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtenKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtenKhachHangActionPerformed

    private void jTextFieldgioLapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgioLapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgioLapActionPerformed

    private void jTextFieldsoDienThoaiKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsoDienThoaiKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldsoDienThoaiKhachHangActionPerformed

    private void jTextFieldtenNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtenNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtenNhanVienActionPerformed

    private void jTextFieldsoLuongKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsoLuongKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldsoLuongKhachHangActionPerformed

    private void jTextFieldmaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmaHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmaHoaDonActionPerformed

    private void jButtonXoaMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaMonAnActionPerformed
        // TODO add your handling code here:
         int selectedRow = jTabledanhSachMonAn.getSelectedRow();
    if (selectedRow != -1) {
       int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa món ăn này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        // Nếu người dùng chọn "Yes", thực hiện xóa
        if (confirm == JOptionPane.YES_OPTION) {
            // Lấy mô hình bảng
            DefaultTableModel model = (DefaultTableModel) jTabledanhSachMonAn.getModel();
            
            // Xóa món ăn khỏi JTable
            model.removeRow(selectedRow);
            
            // Cập nhật lại tổng tiền
            updateTotal();
            
            // Hiển thị thông báo đã xóa thành công
            JOptionPane.showMessageDialog(this, "Đã xóa món ăn thành công.");
        }
    } else {
        // Thông báo nếu chưa chọn dòng nào
        JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn để xóa.");
    }
    }//GEN-LAST:event_jButtonXoaMonAnActionPerformed

    private void jButtonCapNhatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCapNhatHoaDonActionPerformed
        // TODO add your handling code here:
   // Lấy mã hóa đơn từ giao diện
    String maHoaDon = jTextFieldmaHoaDon.getText();
    ChiTietHoaDon_DAO chiTietHoaDon_DAO = new ChiTietHoaDon_DAO();

    // Lấy danh sách mã món ăn hiện tại trong JTable
    int rowCount = jTabledanhSachMonAn.getRowCount();
    List<String> currentItems = new ArrayList<>();
    for (int i = 0; i < rowCount; i++) {
        String maMonAn = jTabledanhSachMonAn.getValueAt(i, 0).toString();
        currentItems.add(maMonAn);
    }

    // Lấy danh sách mã món ăn ban đầu từ cơ sở dữ liệu
    List<ChiTietHoaDon> originalItems = chiTietHoaDon_DAO.getAllChiTietHoaDon(maHoaDon);
    for (ChiTietHoaDon chiTiet : originalItems) {
        String maMonAn = chiTiet.getMonAn().getMaMonAn();
        if (!currentItems.contains(maMonAn)) {
            // Xóa món ăn khỏi cơ sở dữ liệu nếu không có trong danh sách hiện tại
            chiTietHoaDon_DAO.deleteChiTietHoaDon(maHoaDon, maMonAn);
        }
    }

    // Cập nhật hoặc thêm mới các món ăn còn lại trong JTable
    for (int i = 0; i < rowCount; i++) {
        String maMonAn = jTabledanhSachMonAn.getValueAt(i, 0).toString();
        int soLuongMonAn = Integer.parseInt(jTabledanhSachMonAn.getValueAt(i, 3).toString());
        
        String giaMonAnStr = jTabledanhSachMonAn.getValueAt(i, 2).toString().replaceAll("[^\\d]", "");
        double giaMonAn = Double.parseDouble(giaMonAnStr);
        
        String phanTramGiamGiaStr = jTabledanhSachMonAn.getValueAt(i, 4).toString().replaceAll("[^\\d]", "");
        double phanTramGiamGia = Double.parseDouble(phanTramGiamGiaStr);

        // Tính thành tiền
        double thanhTien = (giaMonAn * soLuongMonAn) - ((giaMonAn * soLuongMonAn) * phanTramGiamGia / 100);

        // Cập nhật hoặc thêm mới chi tiết món ăn
        chiTietHoaDon_DAO.updateChiTietHoaDon(maHoaDon, maMonAn, soLuongMonAn, thanhTien);

        // Cập nhật số lượng món ăn trong bảng MonAn
        MonAn_DAO monAn_DAO = new MonAn_DAO();
        monAn_DAO.updateSoLuongMonAn(maMonAn, soLuongMonAn);
    }
    
    // Hiển thị thông báo khi cập nhật hoàn tất
    JOptionPane.showMessageDialog(this, "Hóa đơn đã được cập nhật thành công.");
    }//GEN-LAST:event_jButtonCapNhatHoaDonActionPerformed

    private void jTabledanhSachMonAnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabledanhSachMonAnKeyReleased
        // TODO add your handling code here:
          int row = jTabledanhSachMonAn.getSelectedRow();
  
    int column = jTabledanhSachMonAn.getSelectedColumn();
    
//    // Lưu giá trị cũ của số lượng để khôi phục nếu cần
//    String oldQuantityValue = jTabledanhSachMonAn.getValueAt(row, 3).toString(); // Lưu lại giá trị cũ
//
    // Kiểm tra nếu thay đổi ở cột số lượng (cột 3)
    if (column == 3) { 
        try {
            // Lấy giá trị số lượng và giá từ JTable
            String quantityStr = jTabledanhSachMonAn.getValueAt(row, 3).toString().trim();  // Lấy số lượng nhập vào
            int quantity = Integer.parseInt(quantityStr); // Chuyển đổi số lượng thành số nguyên
           
            // Lấy giá trị từ cột 2 và tách bỏ dấu phần trăm (nếu có)
            String priceStr = jTabledanhSachMonAn.getValueAt(row, 2).toString().trim();
            priceStr = priceStr.replaceAll("[^\\d.]", "");  // Loại bỏ ký tự không phải số và dấu chấm (giữ lại dấu chấm thập phân)
            
             // Lấy phần trăm giảm giá từ cột 4
            String discountStr = jTabledanhSachMonAn.getValueAt(row, 4).toString().trim();
            discountStr = discountStr.replaceAll("[^\\d]", "");  // Loại bỏ ký tự không phải số
//            double discountPercent = discountStr.isEmpty() ? 0 : Double.parseDouble(discountStr);
            
            // Nếu không thể chuyển đổi giá trị thành số, hiển thị thông báo lỗi
            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giá trị món ăn không hợp lệ.");
                return;
            }
            
            double price = Double.parseDouble(priceStr); // Lấy giá món ăn
            double discount = Double.parseDouble(discountStr); // Lấy giá món ăn
            String maMonAn = jTabledanhSachMonAn.getValueAt(row, 0).toString(); // Lấy mã món ăn
            
            // Kiểm tra số lượng có hợp lệ (lớn hơn 0)
            if (quantity <= 0) {
                 
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.");
//                 jTabledanhSachMonAn.setValueAt(oldQuantityValue, row, 3); // Khôi phục lại số lượng cũ
                // Để con trỏ vẫn ở đây và cho phép người dùng nhập lại mà không khôi phục giá trị cũ
                   jTabledanhSachMonAn.editCellAt(row, 3);  // Giữ con trỏ tại ô số lượng và kích hoạt chế độ chỉnh sửa
return;
            }

            // Lấy thông tin chi tiết hóa đơn từ DAO
            ChiTietHoaDon_DAO chiTietHoaDonDao = new ChiTietHoaDon_DAO();
            List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonDao.getAllChiTietHoaDon(hoaDon.getMaHoaDon());
            hoaDon.setChiTietHoaDons(chiTietHoaDonList);

            // Tìm chi tiết món ăn trong hóa đơn
            ChiTietHoaDon chiTiet = null;
            for (ChiTietHoaDon ct : chiTietHoaDonList) {
                if (ct.getMonAn().getMaMonAn().equals(maMonAn)) {
                    chiTiet = ct;
                    break;
                }
            }

            // Cập nhật số lượng và tính lại tổng tiền
            if (chiTiet != null) {
                MonAn monAn = chiTiet.getMonAn();
                
                if (monAn != null) {
                    // Kiểm tra nếu số lượng sửa không vượt quá tồn kho
                    if (quantity <= monAn.getSoLuong()) {
                        // Cập nhật lại tổng tiền cho món ăn
                         // Tính thành tiền sau khi áp dụng phần trăm giảm giá
      
                        double total = quantity * price *1000;
                        total-=(total*discount/100);
                        jTabledanhSachMonAn.setValueAt(converter.GiaTienConverter.chuyenDoiTien(total), row, 5); // Cập nhật cột tổng tiền

                        // Cập nhật lại tổng hóa đơn
                        updateTotal();
                    } else {
                        // Nếu số lượng không hợp lệ, khôi phục lại giá trị cũ và thông báo lỗi
                      
                        JOptionPane.showMessageDialog(this, "Không thể sửa số lượng vượt quá tồn kho.");
                            jTabledanhSachMonAn.editCellAt(row, 3);  // Giữ con trỏ tại ô số lượng và kích hoạt chế độ chỉnh sửa  
//                        jTabledanhSachMonAn.setValueAt(oldQuantityValue, row, 3); // Khôi phục lại số lượng cũ
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Nếu có lỗi khi chuyển đổi dữ liệu, khôi phục lại số lượng cũ và thông báo lỗi
          
            JOptionPane.showMessageDialog(this, "Giá trị hoặc số lượng không hợp lệ.");
             jTabledanhSachMonAn.editCellAt(row, 3);  // Giữ con trỏ tại ô số lượng và kích hoạt chế độ chỉnh sửa
//              jTabledanhSachMonAn.setValueAt(oldQuantityValue, row, 3); // Khôi phục lại số lượng cũ
        }
    }
    }//GEN-LAST:event_jTabledanhSachMonAnKeyReleased

    private void jButtonHuyHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHuyHoaDonActionPerformed
        // TODO add your handling code here:
        // Kiểm tra điều kiện trước khi hủy hóa đơn
    if (jTextFieldtongTienMonAn.getText().equals("0 VNĐ") || hoaDon.getTrangThaiThanhToan().equals("Đang xử lý")) {
        // Thực hiện hủy hóa đơn
        try {
            // Cập nhật trạng thái hóa đơn thành "Đã hủy"
            HoaDon_DAO hoaDonDao = new HoaDon_DAO();
            hoaDon.setTrangThaiThanhToan("Đã hủy"); // Cập nhật trạng thái hóa đơn
           
            BanAn_DAO banAn_DAO = new BanAn_DAO();
           String trangThaiMoi = "Trống";
            banAn_DAO.capNhatTrangThaiBan(hoaDon.getBanAn().getMaBan(), trangThaiMoi);
            
            LocalDateTime currentTime = LocalDateTime.now();
            
            hoaDonDao.capNhatTrangThaiDaHuy(hoaDon, currentTime); // Gọi phương thức để cập nhật trong cơ sở dữ liệu

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(null, "Hóa đơn đã được hủy thành công!");

            // Bạn có thể làm mới lại giao diện hoặc đóng cửa sổ nếu cần
            this.dispose(); // Đóng cửa sổ sau khi hủy thành công
//            HoaDon_DAO hd=new HoaDon_DAO();
//            hd.getAllHoaDonThongTin();
            
            
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(null, "Lỗi khi hủy hóa đơn: " + e.getMessage());
        }
    } else {
        // Thông báo nếu hóa đơn không thể hủy
        JOptionPane.showMessageDialog(null, "Hóa đơn không thể hủy. Điều kiện: Tổng tiền phải là 0 hoặc trạng thái phải là 'Đang xử lý'.");
    }
    }//GEN-LAST:event_jButtonHuyHoaDonActionPerformed

    private void jTextFieldtongTienMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtongTienMonAnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtongTienMonAnActionPerformed

    private void jButtonXoaTatCaMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaTatCaMonAnActionPerformed
        // TODO add your handling code here:
        // Lấy mô hình bảng
    DefaultTableModel model = (DefaultTableModel) jTabledanhSachMonAn.getModel();
    
    // Kiểm tra bảng có dữ liệu không
    if (model.getRowCount() > 0) {
        // Hiển thị hộp thoại xác nhận
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tất cả các món ăn không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa tất cả các dòng trong bảng
            model.setRowCount(0);

            // Cập nhật lại tổng tiền
            updateTotal();
            
            // Thông báo đã xóa
            JOptionPane.showMessageDialog(this, "Đã xóa tất cả món ăn.");
        }
    } else {
        // Thông báo nếu không có món ăn để xóa
        JOptionPane.showMessageDialog(this, "Không có món ăn nào để xóa.");
    }
    }//GEN-LAST:event_jButtonXoaTatCaMonAnActionPerformed
 private void updateTotal() {
    double totalAmount = 0;
     // Tính thành tiền sau khi áp dụng khuyến mãi (nếu có)
      
    // Duyệt qua từng dòng trong bảng và tính tổng tiền
    DefaultTableModel model = (DefaultTableModel) jTabledanhSachMonAn.getModel();
   for (int i = 0; i < model.getRowCount(); i++) {
        // Lấy giá trị từ cột giá món ăn (cột thứ 3), số lượng món ăn (cột thứ 4) và phần trăm giảm giá (cột thứ 5)
        String giaMonAnStr = model.getValueAt(i, 2).toString();  // Cột thứ 3 là giá món ăn
        String soLuongMonAnStr = model.getValueAt(i, 3).toString();  // Cột thứ 4 là số lượng món ăn
        String phanTramGiamGiaStr = model.getValueAt(i, 4).toString(); // Cột thứ 5 là phần trăm giảm giá
        
        // Loại bỏ các ký tự không phải số (nếu có) như " VNĐ" từ giá món ăn và giảm giá
        giaMonAnStr = giaMonAnStr.replaceAll("[^\\d.]", "");  // Loại bỏ ký tự không phải số và dấu chấm
        soLuongMonAnStr = soLuongMonAnStr.replaceAll("[^\\d.]", "");  // Loại bỏ ký tự không phải số và dấu chấm
        phanTramGiamGiaStr = phanTramGiamGiaStr.replaceAll("[^\\d.]", ""); // Loại bỏ ký tự không phải số từ phần trăm giảm giá

        // Chuyển đổi giá trị thành số
        double giaMonAn = Double.parseDouble(giaMonAnStr);  // Chuyển đổi giá món ăn thành số
        int soLuongMonAn = Integer.parseInt(soLuongMonAnStr);  // Chuyển đổi số lượng món ăn thành số nguyên
        double phanTramGiamGia = Double.parseDouble(phanTramGiamGiaStr);  // Chuyển đổi phần trăm giảm giá thành số
        
        // Tính thành tiền sau khi áp dụng phần trăm giảm giá
        double thanhTien = giaMonAn * soLuongMonAn;  // Tính thành tiền chưa giảm giá
        thanhTien -= (thanhTien * phanTramGiamGia / 100);  // Áp dụng phần trăm giảm giá vào thành tiền

        // Thêm thành tiền vào tổng tiền
        totalAmount += thanhTien*1000;
        
    }
     System.out.println(totalAmount);
    // Cập nhật tổng tiền vào label/txt
    jTextFieldtongTienMonAn.setText(GiaTienConverter.chuyenDoiTien(totalAmount)); // Cập nhật tổng tiền
}
 
 
 

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameXemHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameXemHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameXemHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameXemHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrameThanhToan().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCapNhatHoaDon;
    private javax.swing.JButton jButtonHuyHoaDon;
    private javax.swing.JButton jButtonXoaMonAn;
    private javax.swing.JButton jButtonXoaTatCaMonAn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelmaKhachHang;
    private javax.swing.JLabel jLabelmaKhachHang1;
    private javax.swing.JLabel jLabelmaKhachHang2;
    private javax.swing.JLabel jLabelmaKhachHang3;
    private javax.swing.JLabel jLabelmaKhachHang4;
    private javax.swing.JLabel jLabelmaKhachHang5;
    private javax.swing.JLabel jLabelmaKhachHang6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabledanhSachMonAn;
    private javax.swing.JTextField jTextFieldgioLap;
    private javax.swing.JTextField jTextFieldmaBan;
    private javax.swing.JTextField jTextFieldmaHoaDon;
    private javax.swing.JTextField jTextFieldsoDienThoaiKhachHang;
    private javax.swing.JTextField jTextFieldsoLuongKhachHang;
    private javax.swing.JTextField jTextFieldtenKhachHang;
    private javax.swing.JTextField jTextFieldtenNhanVien;
    private javax.swing.JTextField jTextFieldtongTienMonAn;
    // End of variables declaration//GEN-END:variables
}
