// Nguyen Duc Hau 13/10/2024

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.quanlyhoadon;

import Parttern.BanManager;
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
import java.time.LocalDateTime;
import javax.swing.ImageIcon;
/**
 *
 * @author ADMIN
 */
public class FrameThanhToan extends javax.swing.JFrame {
    private HoaDon hoaDon; // Đối tượng HoaDon
//    private ChiTietHoaDon chiTietHoaDon;
    /**
     * Creates new form FrameThanhToan
     */
     // Constructor nhận đối tượng HoaDon
        public FrameThanhToan(HoaDon hoaDon) {
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
    //     for (ChiTietHoaDon chiTiet : chiTietHoaDonList) {
    //        MonAn monAn = chiTiet.getMonAn();
    //        double thanhTien = (chiTiet.getMonAn().getGiaMonAn()*chiTiet.getSoLuongMonAn())-((chiTiet.getMonAn().getGiaMonAn()*chiTiet.getSoLuongMonAn())*chiTiet.getMonAnKhuyenMai().getPhanTramGiamGia()/100);
    //        tongTienMonAn += thanhTien; // Cộng dồn tổng tiền món ăn
    //
    //        // Thêm dòng vào bảng danh sách món ăn
    //        model.addRow(new Object[]{
    //            monAn.getMaMonAn(),
    //            monAn.getTenMonAn(),
    //            GiaTienConverter.chuyenDoiTien(monAn.getGiaMonAn()),
    //            chiTiet.getSoLuongMonAn(),
    //           GiaTienConverter.chuyenDoiPhanTram( monAn.getMonAnKhuyenMai().getPhanTramGiamGia()),
    //            GiaTienConverter.chuyenDoiTien(thanhTien)
    //        });
    //    }
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

        // Tính và hiển thị số tiền tăng theo VAT (10%)
        double vat = 0.1;
        double tienGiamTheoVAT = tongTienMonAn * vat;
        jTextFieldVAT.setText(GiaTienConverter.chuyenDoiTien(tienGiamTheoVAT));

        // Hiển thị chiết khấu
        double chietKhau = hoaDon.getChietKhau();
        jTextFieldchietKhau.setText(GiaTienConverter.chuyenDoiTien(chietKhau));

        // Tính và hiển thị tổng hóa đơn
        double tongHoaDon = tongTienMonAn - (chietKhau/100*tongTienMonAn) +( vat*(tongTienMonAn-(chietKhau/100*tongTienMonAn)));
        double tongHoaDon2=Math.round(tongHoaDon/1000)*1000;
        hoaDon.setTongTien(tongHoaDon);
    //    jTextFieldtongHoaDon.setText(GiaTienConverter.chuyenDoiTien(tongHoaDon));

        jTextFieldtongHoaDon.setText(GiaTienConverter.chuyenDoiTien(tongHoaDon2));

        // Gợi ý số tiền khách đưa
        double[] goiY = tinhToanGoiY(tongHoaDon);
        jTextFieldgoiYTien1.setText(GiaTienConverter.chuyenDoiTien(goiY[0]));
        jTextFieldgoiYTien2.setText(GiaTienConverter.chuyenDoiTien(goiY[1]));
        jTextFieldgoiYTien3.setText(GiaTienConverter.chuyenDoiTien(goiY[2]));
        jTextFieldgoiYTien4.setText(GiaTienConverter.chuyenDoiTien(goiY[3]));
        jTextFieldgoiYTien5.setText(GiaTienConverter.chuyenDoiTien(goiY[4]));
        jTextFieldgoiYTien6.setText(GiaTienConverter.chuyenDoiTien(goiY[5]));

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
        jLabelmaKhachHang7 = new javax.swing.JLabel();
        jLabelmaKhachHang8 = new javax.swing.JLabel();
        jLabelmaKhachHang9 = new javax.swing.JLabel();
        jTextFieldtongHoaDon = new javax.swing.JTextField();
        jLabelmaKhachHang10 = new javax.swing.JLabel();
        jTextFieldtienKhachDua = new javax.swing.JTextField();
        jLabelmaKhachHang11 = new javax.swing.JLabel();
        jTextFieldVAT = new javax.swing.JTextField();
        jLabelmaKhachHang12 = new javax.swing.JLabel();
        jTextFieldchietKhau = new javax.swing.JTextField();
        jLabelmaKhachHang13 = new javax.swing.JLabel();
        jTextFieldphuongThucThanhToan = new javax.swing.JTextField();
        jLabelmaKhachHang14 = new javax.swing.JLabel();
        jTextFieldtienTraLaiChoKhach = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldtongTienMonAn = new javax.swing.JTextField();
        jButtonThanhToan = new javax.swing.JButton();
        jButtonInHoaDon = new javax.swing.JButton();
        jComboBoxloaiKhuyenMai = new javax.swing.JComboBox<>();
        jTextFieldgoiYTien5 = new javax.swing.JTextField();
        jTextFieldgoiYTien6 = new javax.swing.JTextField();
        jTextFieldgoiYTien4 = new javax.swing.JTextField();
        jTextFieldgoiYTien1 = new javax.swing.JTextField();
        jTextFieldgoiYTien2 = new javax.swing.JTextField();
        jTextFieldgoiYTien3 = new javax.swing.JTextField();
        jComboBoxGiamGia = new javax.swing.JComboBox<>();
        jLabelmaKhachHang15 = new javax.swing.JLabel();
        jTextFieldsoTienGiamGia = new javax.swing.JTextField();

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
        jLabelmaKhachHang4.setText("Người phụ trách:");

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
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTabledanhSachMonAn);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Danh sách món ăn");

        jLabelmaKhachHang7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang7.setText("Khuyến mãi:");

        jLabelmaKhachHang8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang8.setText("Giảm giá:");

        jLabelmaKhachHang9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang9.setText("Tổng hóa đơn:");

        jTextFieldtongHoaDon.setEditable(false);
        jTextFieldtongHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtongHoaDonActionPerformed(evt);
            }
        });

        jLabelmaKhachHang10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang10.setText("Tiền khách đưa:");

        jTextFieldtienKhachDua.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldtienKhachDuaFocusLost(evt);
            }
        });
        jTextFieldtienKhachDua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtienKhachDuaActionPerformed(evt);
            }
        });
        jTextFieldtienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldtienKhachDuaKeyReleased(evt);
            }
        });

        jLabelmaKhachHang11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang11.setText("VAT(10%):");

        jTextFieldVAT.setEditable(false);
        jTextFieldVAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVATActionPerformed(evt);
            }
        });

        jLabelmaKhachHang12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang12.setText("Chiết khấu:");

        jTextFieldchietKhau.setEditable(false);
        jTextFieldchietKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldchietKhauActionPerformed(evt);
            }
        });

        jLabelmaKhachHang13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang13.setText("Phương thức thanh toán:");

        jTextFieldphuongThucThanhToan.setEditable(false);
        jTextFieldphuongThucThanhToan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldphuongThucThanhToan.setText("Tiền mặt");
        jTextFieldphuongThucThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldphuongThucThanhToanActionPerformed(evt);
            }
        });

        jLabelmaKhachHang14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang14.setText("Tiền trả lại cho khách:");

        jTextFieldtienTraLaiChoKhach.setEditable(false);
        jTextFieldtienTraLaiChoKhach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtienTraLaiChoKhachActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Tổng tiền món ăn:");

        jTextFieldtongTienMonAn.setEditable(false);

        jButtonThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/payment-method.png"))); // NOI18N
        jButtonThanhToan.setText("Thanh toán");
        jButtonThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThanhToanActionPerformed(evt);
            }
        });

        jButtonInHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonInHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pdf-file.png"))); // NOI18N
        jButtonInHoaDon.setText("In hóa đơn");
        jButtonInHoaDon.setEnabled(false);
        jButtonInHoaDon.setPreferredSize(new java.awt.Dimension(118, 55));
        jButtonInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInHoaDonActionPerformed(evt);
            }
        });

        jComboBoxloaiKhuyenMai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phần trăm", "Quà tặng", "Tiền" }));
        jComboBoxloaiKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxloaiKhuyenMaiActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien5.setEditable(false);
        jTextFieldgoiYTien5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien5MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien5ActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien6.setEditable(false);
        jTextFieldgoiYTien6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien6MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien6ActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien4.setEditable(false);
        jTextFieldgoiYTien4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien4MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien4ActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien1.setEditable(false);
        jTextFieldgoiYTien1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien1MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien1ActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien2.setEditable(false);
        jTextFieldgoiYTien2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien2MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien2ActionPerformed(evt);
            }
        });

        jTextFieldgoiYTien3.setEditable(false);
        jTextFieldgoiYTien3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldgoiYTien3MouseClicked(evt);
            }
        });
        jTextFieldgoiYTien3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgoiYTien3ActionPerformed(evt);
            }
        });

        jComboBoxGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGiamGiaActionPerformed(evt);
            }
        });

        jLabelmaKhachHang15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelmaKhachHang15.setText("Số tiền giảm giá:");

        jTextFieldsoTienGiamGia.setEditable(false);
        jTextFieldsoTienGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsoTienGiamGiaActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldmaBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldmaHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldtenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldsoLuongKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelmaKhachHang7)
                                .addGap(57, 57, 57)
                                .addComponent(jComboBoxloaiKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelmaKhachHang8)
                                    .addComponent(jLabelmaKhachHang15)
                                    .addComponent(jLabelmaKhachHang10)
                                    .addComponent(jLabelmaKhachHang9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldtienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldsoTienGiamGia)
                                        .addComponent(jComboBoxGiamGia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextFieldtongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(147, 147, 147)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelmaKhachHang12)
                                    .addComponent(jLabelmaKhachHang13)
                                    .addComponent(jLabelmaKhachHang11)
                                    .addComponent(jLabelmaKhachHang14))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jTextFieldtienTraLaiChoKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldVAT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldchietKhau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldphuongThucThanhToan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldtongTienMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldgoiYTien1, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jTextFieldgoiYTien4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldgoiYTien5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldgoiYTien6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButtonThanhToan)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldgoiYTien2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldgoiYTien3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxloaiKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldtongTienMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelmaKhachHang7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelmaKhachHang11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelmaKhachHang8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelmaKhachHang15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldsoTienGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldchietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelmaKhachHang9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldtongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmaKhachHang13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldphuongThucThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelmaKhachHang10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldtienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldtienTraLaiChoKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelmaKhachHang14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldgoiYTien2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldgoiYTien3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldgoiYTien1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldgoiYTien5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldgoiYTien6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldgoiYTien4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

    private void jTextFieldtongHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtongHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtongHoaDonActionPerformed

    private void jTextFieldtienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtienKhachDuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtienKhachDuaActionPerformed

    private void jTextFieldVATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVATActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldVATActionPerformed

    private void jTextFieldchietKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldchietKhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldchietKhauActionPerformed

    private void jTextFieldphuongThucThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldphuongThucThanhToanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldphuongThucThanhToanActionPerformed

    private void jTextFieldtienTraLaiChoKhachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtienTraLaiChoKhachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtienTraLaiChoKhachActionPerformed

    private void jTextFieldgoiYTien5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien5ActionPerformed

    private void jTextFieldgoiYTien6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien6ActionPerformed

    private void jTextFieldgoiYTien4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien4ActionPerformed

    private void jTextFieldgoiYTien1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien1ActionPerformed

    private void jTextFieldgoiYTien2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien2ActionPerformed

    private void jTextFieldgoiYTien3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgoiYTien3ActionPerformed
 
 //Phương thức tính toán số tiền gợi ý dựa trên tổng hóa đơn
private double[] tinhToanGoiY(double tongHoaDon) {
    double[] goiY = new double[6];

      // Làm tròn số tiền tới bội số của 1000 (số tiền ban đầu)
    double tongHoaDonLamTron = Math.round(tongHoaDon / 1000.0) * 1000;  // Làm tròn đến bội số của 1000

    // Gợi ý các số tiền, áp dụng theo quy tắc tính toán
    goiY[0] = tongHoaDonLamTron;  // Gợi ý đầu tiên là chính số tiền làm tròn (ví dụ 1.628.000)

    // Gợi ý thứ 2: Làm tròn lên bội số của 1000
    goiY[1] = tongHoaDonLamTron + 1000;  // Gợi ý số tròn tiếp theo (+1k)

    // Gợi ý thứ 3: Làm tròn lên bội số gần nhất (ví dụ: tăng lên 5k, 10k tùy vào khoảng cách)
    goiY[2] = Math.ceil((tongHoaDonLamTron + 5000) / 5000.0) * 5000;  // Gợi ý thêm 5k

    // Gợi ý thứ 4: Làm tròn lên bội số gần nhất (ví dụ: tăng lên 10k, 20k tùy vào khoảng cách)
    goiY[3] = Math.ceil((tongHoaDonLamTron + 10000) / 10000.0) * 10000;  // Gợi ý thêm 10k

    // Gợi ý thứ 5: Làm tròn lên bội số gần nhất (ví dụ: tăng lên 50k, 100k tùy vào khoảng cách)
    goiY[4] = Math.ceil((tongHoaDonLamTron + 50000) / 50000.0) * 50000;  // Gợi ý thêm 50k

    // Gợi ý thứ 6: Tăng mạnh, làm tròn lên bội số gần nhất 100k hoặc 1 triệu tùy vào số tiền
    goiY[5] = Math.ceil((tongHoaDonLamTron + 100000) / 100000.0) * 100000;  // Gợi ý thêm 100k hoặc 1M

    return goiY;
}
// ham set tien goi y
private void setGoiYTien(JTextField textFieldGoiY) {
    // Lấy giá trị từ JTextField gợi ý tiền
    String goiYText = textFieldGoiY.getText();
    if (goiYText != null && !goiYText.isEmpty()) {
        // Xóa các ký tự không cần thiết trước khi chuyển đổi
        goiYText = goiYText.replace(" VNĐ", "").replace(".", "").replace(",", "").trim();

        // Chuyển đổi sang double
        try {
            double goiY = Double.parseDouble(goiYText);
        
            // Đặt giá trị vào jTextFieldtienKhachDua
            jTextFieldtienKhachDua.setText(GiaTienConverter.chuyenDoiTien(goiY));

            // Tính số tiền trả lại
            double tongHoaDon = Double.parseDouble(jTextFieldtongHoaDon.getText().replace(" VNĐ", "").replace(".", "").replace(",", "").trim());
            double tienTraLai = goiY - tongHoaDon;

            // Cập nhật số tiền trả lại vào jTextFieldtienTraLaiChoKhach
            jTextFieldtienTraLaiChoKhach.setText(GiaTienConverter.chuyenDoiTien(tienTraLai));
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu không thể chuyển đổi
            System.out.println("Lỗi chuyển đổi số: " + e.getMessage());
        }
    }
}
private double tongHoaDonSauGiamGia = 0; // Khai báo biến tổng hóa đơn sau giảm giá
private double soTienGiamGia = 0; // Khai báo biến số tiền giảm giá
private String giamGiaTruoc = ""; // Khai báo biến giamGiaTruoc để lưu giá trị giảm giá trước đó

    private void jComboBoxloaiKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxloaiKhuyenMaiActionPerformed
        // TODO add your handling code here:
   double tongHoaDonGoc = hoaDon.getTongTien(); // Lấy tổng hóa đơn gốc ban đầu từ hóa đơn
    soTienGiamGia = 0; // Đặt giá trị khởi tạo của giảm giá bằng 0

    // Lấy loại khuyến mãi được chọn
    String loaiKhuyenMai = (String) jComboBoxloaiKhuyenMai.getSelectedItem();
    jComboBoxGiamGia.removeAllItems(); // Xóa tất cả các mục hiện có trong JComboBox giảm giá

    // Khởi tạo đối tượng DAO để lấy dữ liệu khuyến mãi
    KhuyenMaiKhachHang_DAO dao = new KhuyenMaiKhachHang_DAO();
    
    // Lấy danh sách khuyến mãi của khách hàng
    List<KhuyenMaiKhachHang> dsKhuyenMai = dao.getKhuyenMaiByMaKhachHang(hoaDon.getKhachHang().getMaKhachHang());

    // Kiểm tra nếu không có khuyến mãi nào
    if (dsKhuyenMai.isEmpty()) {
//        jComboBoxGiamGia.addItem("Không có khuyến mãi nào");
        tongHoaDonSauGiamGia = tongHoaDonGoc;
        return;
    }

    // Sắp xếp danh sách khuyến mãi theo giá trị giảm giá từ cao xuống thấp
    dsKhuyenMai.sort((km1, km2) -> {
        double giaTri1 = km1.getKhuyenMai().getGiaTriKhuyenMai();
        double giaTri2 = km2.getKhuyenMai().getGiaTriKhuyenMai();
        return Double.compare(giaTri2, giaTri1); // Sắp xếp giảm dần
    });
       
    // Thêm khuyến mãi vào JComboBox giảm giá dựa trên loại khuyến mãi
    for (KhuyenMaiKhachHang kmkh : dsKhuyenMai) {
        KhuyenMai km = kmkh.getKhuyenMai();
        if (km != null && km.getLoaiKhuyenMai() != null &&
            km.getLoaiKhuyenMai().equals(loaiKhuyenMai) &&
            km.getSoLuongToiThieu()>0 &&
            !km.getNgayBatDau().isAfter(LocalDateTime.now())&& // Ngày bắt đầu nhỏ hơn thời điểm hiện tại
            !km.getNgayKetThuc().isBefore(LocalDateTime.now()) && // Ngày kết thúc lớn hơn thời điểm hiện tại    
            hoaDon.getKhachHang().getDiemTichLuy()>=km.getDiemYeuCau() &&
            "Active".equals(km.getTrangThai()) &&
            !hoaDon.getKhachHang().getMaKhachHang().equals("KH000")  ) {
            if ("Quà tặng".equals(km.getLoaiKhuyenMai())) {
                jComboBoxGiamGia.addItem(km.getMoTaKhuyenMai()+"");
                soTienGiamGia = 0; // Nếu là quà tặng, không có giảm giá tiền mặt
            } else if ("Phần trăm".equals(km.getLoaiKhuyenMai())) {
                jComboBoxGiamGia.addItem("Được giảm " + km.getGiaTriKhuyenMai() + "%");
                soTienGiamGia = tongHoaDonGoc * km.getGiaTriKhuyenMai() / 100; // Tính giảm giá phần trăm
            } else if ("Tiền".equals(km.getLoaiKhuyenMai())) {
//                jComboBoxGiamGia.addItem("Được giảm " + (km.getGiaTriKhuyenMai() * 1000) + " VNĐ");
                jComboBoxGiamGia.addItem("Được giảm " + (km.getGiaTriKhuyenMai()) + " VNĐ");
                
//                soTienGiamGia = km.getGiaTriKhuyenMai() * 1000; // Tính giảm giá tiền cụ thể
soTienGiamGia = km.getGiaTriKhuyenMai() ; // Tính giảm giá tiền cụ thể
            }
        }
    }

    // Tính toán lại tổng hóa đơn sau giảm giá
    tongHoaDonSauGiamGia = tongHoaDonGoc - soTienGiamGia;

    // Làm tròn tổng hóa đơn sau giảm giá
    tongHoaDonSauGiamGia = Math.round(tongHoaDonSauGiamGia / 1000) * 1000;

    // Cập nhật giao diện
    jTextFieldtongHoaDon.setText(GiaTienConverter.chuyenDoiTien(tongHoaDonSauGiamGia));
    jTextFieldsoTienGiamGia.setText(GiaTienConverter.chuyenDoiTien(soTienGiamGia));

    // Lưu giá trị khuyến mãi đầu tiên
    if (jComboBoxGiamGia.getItemCount() > 0) {
        giamGiaTruoc = (String) jComboBoxGiamGia.getItemAt(0);
        jComboBoxGiamGia.setSelectedIndex(0); // Tự động chọn mã giảm giá đầu tiên
    }


    }//GEN-LAST:event_jComboBoxloaiKhuyenMaiActionPerformed




//private String giamGiaTruoc = ""; // Lưu giá trị giảm giá trước đó để so sánh
    private void jComboBoxGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGiamGiaActionPerformed
        // TODO add your handling code here:
   String giamGiaChon = (String) jComboBoxGiamGia.getSelectedItem();

    // Nếu lựa chọn giảm giá là null hoặc không hợp lệ, reset về giá trị gốc
    if (giamGiaChon == null ) {
        soTienGiamGia = 0;
        tongHoaDonSauGiamGia = hoaDon.getTongTien();
    } else {
        // Cập nhật lại số tiền giảm giá dựa trên loại khuyến mãi mới chọn
    if ("Quà tặng".equals(giamGiaChon)) {
    // Nếu là quà tặng, không có giảm giá tiền mặt
    soTienGiamGia = 0;
} else if (giamGiaChon.contains("%")) {
    // Nếu khuyến mãi là phần trăm
    // Giả sử định dạng là "Được giảm 10%"
    String[] parts = giamGiaChon.split(" ");
    double phanTramGiam = Double.parseDouble(parts[2].replace("%", "")); // Lấy phần trăm giảm
    System.out.print("Phần trăm giảm: " + phanTramGiam);

    // Tính toán lại số tiền giảm giá
    soTienGiamGia = hoaDon.getTongTien() * phanTramGiam / 100;
} else if (giamGiaChon.contains("VNĐ")) {
    // Nếu khuyến mãi là giảm giá cụ thể bằng VNĐ
    // Giả sử định dạng là "Được giảm 5000 VNĐ"
    String[] parts = giamGiaChon.split(" ");
    soTienGiamGia = Double.parseDouble(parts[2]); // Lấy giá trị số từ chuỗi
}
        tongHoaDonSauGiamGia = hoaDon.getTongTien() - soTienGiamGia; // Tính lại tổng hóa đơn sau khi giảm giá
    }

    // Làm tròn tổng hóa đơn sau giảm giá
    tongHoaDonSauGiamGia = Math.round(tongHoaDonSauGiamGia / 1000) * 1000;

    // Cập nhật trên giao diện
    jTextFieldsoTienGiamGia.setText(GiaTienConverter.chuyenDoiTien(soTienGiamGia));
    jTextFieldtongHoaDon.setText(GiaTienConverter.chuyenDoiTien(tongHoaDonSauGiamGia));

    // Cập nhật giá trị khuyến mãi trước đó
    giamGiaTruoc = giamGiaChon;

    
    }//GEN-LAST:event_jComboBoxGiamGiaActionPerformed

    private void jTextFieldsoTienGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsoTienGiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldsoTienGiamGiaActionPerformed

    private void jTextFieldtienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldtienKhachDuaKeyReleased
        // TODO add your handling code here:
  String tienKhachDuaText = jTextFieldtienKhachDua.getText();

    // Kiểm tra xem chuỗi có chứa ký tự không phải số hay không
    if (!tienKhachDuaText.matches("\\d*")) { // Nếu không phải là số
        // Hiển thị thông báo lỗi
        JOptionPane.showMessageDialog(this, "Phải nhập số!");
        
        // Đặt lại nội dung của jTextFieldtienKhachDua thành rỗng
        jTextFieldtienKhachDua.setText(""); 
        return; // Ngừng xử lý nếu có ký tự không hợp lệ
    }

    // Nếu chuỗi không rỗng
    if (!tienKhachDuaText.isEmpty()) {
        // Chuyển đổi số tiền khách đưa từ String sang double
        try {
            // Loại bỏ ký tự không phải số trước khi chuyển đổi
            double tienKhachDua = Double.parseDouble(tienKhachDuaText);

            // Lấy tổng hóa đơn, cũng xử lý loại bỏ ký tự không cần thiết
            double tongHoaDon = Double.parseDouble(jTextFieldtongHoaDon.getText().replaceAll("[^0-9]", "").trim());

            // Tính số tiền trả lại
            double tienTraLai = tienKhachDua - tongHoaDon;

            // Cập nhật số tiền trả lại vào jTextFieldtienTraLaiChoKhach
            jTextFieldtienTraLaiChoKhach.setText(GiaTienConverter.chuyenDoiTien(tienTraLai));

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu không thể chuyển đổi
            System.out.println("Lỗi chuyển đổi số: " + e.getMessage());
            jTextFieldtienTraLaiChoKhach.setText("0");
        }
    } else {
        // Nếu không có giá trị thì đặt số tiền trả lại là 0
        jTextFieldtienTraLaiChoKhach.setText("0");
    }

    
    }//GEN-LAST:event_jTextFieldtienKhachDuaKeyReleased

    private void jTextFieldgoiYTien1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien1MouseClicked
        // TODO add your handling code here:
        setGoiYTien(jTextFieldgoiYTien1);
    }//GEN-LAST:event_jTextFieldgoiYTien1MouseClicked

    private void jTextFieldgoiYTien2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien2MouseClicked
        // TODO add your handling code here:
        setGoiYTien(jTextFieldgoiYTien2);
    }//GEN-LAST:event_jTextFieldgoiYTien2MouseClicked

    private void jTextFieldgoiYTien3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien3MouseClicked
        // TODO add your handling code here:
          setGoiYTien(jTextFieldgoiYTien3);
    }//GEN-LAST:event_jTextFieldgoiYTien3MouseClicked

    private void jTextFieldgoiYTien4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien4MouseClicked
        // TODO add your handling code here:
         setGoiYTien(jTextFieldgoiYTien4);
    }//GEN-LAST:event_jTextFieldgoiYTien4MouseClicked

    private void jTextFieldgoiYTien5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien5MouseClicked
          // TODO add your handling code here:
           setGoiYTien(jTextFieldgoiYTien5);
    }//GEN-LAST:event_jTextFieldgoiYTien5MouseClicked

    private void jTextFieldgoiYTien6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldgoiYTien6MouseClicked
        // TODO add your handling code here:
         setGoiYTien(jTextFieldgoiYTien6);
    }//GEN-LAST:event_jTextFieldgoiYTien6MouseClicked

    private void jTextFieldtienKhachDuaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldtienKhachDuaFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldtienKhachDuaFocusLost

    private void jButtonInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInHoaDonActionPerformed
        // TODO add your handling code here:
  int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn in hóa đơn không?", "Xác nhận in hóa đơn", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return; // Kết thúc phương thức nếu người dùng chọn không
    }  
try {
    // Kích thước trang PDF (5 inch x 8 inch)
    float width = 8.0f;  
    float height = 10.0f;

    // Tạo tài liệu với kích thước tùy chỉnh
    Document document = new Document(new com.itextpdf.text.Rectangle(width * 72, height * 72));
    File file = File.createTempFile("hoa_don", ".pdf");
    FileOutputStream stream = new FileOutputStream(file);
    PdfWriter writer = PdfWriter.getInstance(document, stream);

    // Mở tài liệu
    document.open();

    // Font hỗ trợ Unicode
    BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font unicodeFont = new Font(baseFont, 12, Font.NORMAL);
    Font boldFont = new Font(baseFont, 12, Font.BOLD);

    // Tạo bảng cho logo và tên nhà hàng
    PdfPTable headerTable = new PdfPTable(2); // 2 cột
    headerTable.setWidthPercentage(100); // Chiếm toàn bộ chiều rộng
    float[] columnWidths = {1f, 3f}; // Chia tỉ lệ 1 phần logo và 3 phần text
    headerTable.setWidths(columnWidths);

//     ImageIcon logo = new ImageIcon(getClass().getResource("/image/trangChu.jpg"));
//         java.awt.Image img = icon.getImage();  // Lấy hình ảnh gốc
    // // Thêm logo vào cột đầu tiên
    // Image logo = Image.getInstance("C:\\Users\\ADMIN\\OneDrive\\Documents\\oxy.jpg");
    // logo.scaleToFit(50, 50); // Tùy chỉnh kích thước logo
    // PdfPCell logoCell = new PdfPCell(logo);
    // logoCell.setBorder(PdfPCell.NO_BORDER); // Không viền cho ô logo
    // logoCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Căn lề trái
    // headerTable.addCell(logoCell);

    // Thêm tên nhà hàng và thông tin công ty vào cột thứ hai
    PdfPCell companyInfoCell = new PdfPCell();
    companyInfoCell.setBorder(PdfPCell.NO_BORDER); // Không viền cho ô thông tin
    Paragraph companyInfo = new Paragraph();
    companyInfo.add(new Phrase("Nhà hàng Oxy\n", boldFont)); // Tên nhà hàng
    companyInfo.add(new Phrase("CÔNG TY CỔ PHẦN 4 THÀNH VIÊN HẬU ANH DUY ANH\n", unicodeFont)); // Thông tin công ty
    companyInfo.add(new Phrase("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Hồ Chí Minh\n", unicodeFont)); // Địa chỉ
    companyInfo.add(new Phrase("Hotline: 0786012569", unicodeFont)); // Hotline
    companyInfoCell.addElement(companyInfo); // Thêm thông tin vào ô
    companyInfoCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Căn giữa theo chiều dọc
    headerTable.addCell(companyInfoCell);

    // Thêm bảng vào document
    document.add(headerTable);

    // Thêm tiêu đề hóa đơn
    Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", boldFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    // Thêm thông tin hóa đơn
    document.add(new Paragraph("Số hóa đơn: " + hoaDon.getMaHoaDon(), unicodeFont));
    if(hoaDon.getNhanVien()!=null){
    document.add(new Paragraph("Nhân viên: " +hoaDon.getNhanVien().getHo()+" " +hoaDon.getNhanVien().getTen(), unicodeFont));    
    } else{
        document.add(new Paragraph("Quản lý: "+hoaDon.getQuanLy().getHo()+" " + hoaDon.getQuanLy().getTen(), unicodeFont));
    }
    
    document.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getTenKhachHang(), unicodeFont));
    document.add(new Paragraph("Số điện thoại khách hàng: " + hoaDon.getKhachHang().getSoDienThoai(), unicodeFont));

    // Thêm khoảng trắng
//    document.add(Chunk.NEWLINE);
    document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));

    // Thêm tiêu đề bảng (Column Header)
    PdfPTable table = new PdfPTable(6);
    table.setWidthPercentage(100);
    table.setSpacingBefore(10f); // Khoảng cách trước bảng
    table.setSpacingAfter(10f); // Khoảng cách sau bảng

    // Thêm tiêu đề cột
    String[] headers = {"Mã món", "Tên món", "Giá", "Số lượng", "Giảm giá", "Số tiền"};
    
    for (String header : headers) {
        table.addCell(createCell(header, boldFont));
    }

   // Thêm dữ liệu bảng (Detail)
for (ChiTietHoaDon chiTiet : hoaDon.getChiTietHoaDons()) {
    MonAn monAn = chiTiet.getMonAn();
    double phanTramGiamGia = 0.0; // Mặc định không có khuyến mãi
     MonAnKhuyenMai_DAO monAnKhuyenMaiDao=new MonAnKhuyenMai_DAO();
        List<MonAnKhuyenMai> monAnKhuyenMaiList=monAnKhuyenMaiDao.getAllMonAnKhuyenMai();
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

    // Thêm dòng vào bảng PDF
    table.addCell(createCell(monAn.getMaMonAn(), unicodeFont));
    table.addCell(createCell(monAn.getTenMonAn(), unicodeFont));
    table.addCell(createCell(String.valueOf(monAn.getGiaMonAn()), unicodeFont));
    table.addCell(createCell(String.valueOf(chiTiet.getSoLuongMonAn()), unicodeFont));
    table.addCell(createCell(String.valueOf(phanTramGiamGia) + "%", unicodeFont));
    table.addCell(createCell(String.valueOf(thanhTien), unicodeFont));
}

    // Thêm bảng vào document
    document.add(table);

    // Thêm page footer
         document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
//    document.add(Chunk.NEWLINE);
// Lấy thông tin từ JTextField và loại bỏ phần " VNĐ"
String tienKhachDua = jTextFieldtienKhachDua.getText().replace("VNĐ", "").replace(".", "").replace(",", "").trim();
String tienTraLai = jTextFieldtienTraLaiChoKhach.getText().replace("VNĐ", "").replace(".", "").replace(",", "").trim();

    document.add(new Paragraph("VAT: 10%", unicodeFont));
    document.add(new Paragraph("Chiết khấu: " + hoaDon.getChietKhau(), unicodeFont));
    document.add(new Paragraph("Số tiền giảm giá: " + jTextFieldsoTienGiamGia.getText(), unicodeFont));
    document.add(new Paragraph("Tổng tiền: " + jTextFieldtongHoaDon.getText(), boldFont));
    document.add(new Paragraph("Tiền nhận: " + jTextFieldtienKhachDua.getText(), unicodeFont));
    document.add(new Paragraph("Tiền trả lại: " + jTextFieldtienTraLaiChoKhach.getText(), unicodeFont));
    document.add(new Paragraph("Phương thức thanh toán: " +"Tiền mặt", unicodeFont));
  
       document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------"));
    
    // Thêm mã vạch
    Barcode128 barcode = new Barcode128();
    barcode.setCode(hoaDon.getMaHoaDon());
    Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
    barcodeImage.scaleToFit(250, 30);
    barcodeImage.setAlignment(Element.ALIGN_RIGHT);
    document.add(barcodeImage);
    
    // Thêm thông điệp cảm ơn ở cuối hóa đơn
    
    Paragraph thankYouMessage = new Paragraph("Nhà hàng Oxy chân thành cảm ơn quý khách!\n" +
        "Chúng tôi rất hân hạnh được phục vụ quý khách lần sau.", unicodeFont);
thankYouMessage.setAlignment(Element.ALIGN_CENTER); // Căn giữa đoạn văn
document.add(thankYouMessage);
    // Đóng tài liệu
    document.close();
    stream.close();

    // Mở file PDF sau khi hoàn tất
    Desktop.getDesktop().open(file);
} catch (Exception e) {
    e.printStackTrace();
}



    }//GEN-LAST:event_jButtonInHoaDonActionPerformed
// Phương thức tạo ô không có viền
private PdfPCell createCell(String content, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(content, font));
    cell.setBorder(PdfPCell.NO_BORDER); // Bỏ viền
    return cell;
}
 

    private void jButtonThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThanhToanActionPerformed
        // TODO add your handling code here:
 // Lấy thông tin từ giao diện
//     ManHinhThanhToanHoaDon manHinhThanhToanHoaDon= new ManHinhThanhToanHoaDon();
  // Lấy thông tin từ giao diện
    // Lấy thông tin từ giao diện
    double tongHoaDon = Double.parseDouble(jTextFieldtongHoaDon.getText().replace(" VNĐ", "").replace(".", "").replace(",", "").trim());
    double tienKhachDua = Double.parseDouble(jTextFieldtienKhachDua.getText().replace(" VNĐ", "").replace(".", "").replace(",", "").trim());
    
    // Kiểm tra xem tiền khách đưa có đủ để thanh toán không
    if (tienKhachDua < tongHoaDon) {
        JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ để thanh toán!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // bo sung
    
    // Kiểm tra xem bàn hiện tại có phải là bàn phụ không
    String maBan = hoaDon.getBanAn().getMaBan();
    String maBanPhu = layMaBanPhu(maBan);
    if (isBanPhu(maBan)) {
        // Lấy thông tin về bàn chính
        String maBanChinh = layMaBanChinh(maBan);
        

        JOptionPane.showMessageDialog(this, 
            "Đây là bàn phụ : " + maBanPhu + " và không thể thanh toán. Hãy thanh toán tại bàn chính " + maBanChinh + ".", 
            "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // end
    
    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thanh toán không?", "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }
    
    // Cập nhật cơ sở dữ liệu
    try {
        HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
        
        hoaDon.setTongTien(tongHoaDon);
        // Giả sử bạn có một phương thức để cập nhật hóa đơn trong cơ sở dữ liệu
        boolean isUpdated = hoaDon_DAO.updateInvoice(hoaDon);
        
        if (isUpdated) {
            // Cập nhật trạng thái nút in hóa đơn sau khi thanh toán thành công
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            jButtonInHoaDon.setEnabled(true);
           
            KhachHang khachHang = hoaDon.getKhachHang();  // Lấy khách hàng từ hóa đơn
          
           BanAn_DAO banAn_DAO = new BanAn_DAO();
           
           // bo sung
           
            banAn_DAO.capNhatTrangThaiBan(hoaDon.getBanAn().getMaBan(), "Trống");
            
            boolean isUpdatedBanPhu = hoaDon_DAO.updateInvoicePhu(maBanPhu);
            if(isUpdatedBanPhu){
                 banAn_DAO.capNhatTrangThaiBan(maBanPhu, "Trống");
            }
           // end
           // cua Hau
//           String trangThaiMoi = "Trống";
//            banAn_DAO.capNhatTrangThaiBan(hoaDon.getBanAn().getMaBan(), trangThaiMoi);
//            
//             // Cập nhật trạng thái bàn phụ (nếu có) là đã thanh toán
//            if (isBanPhu(maBan)) {
//                String maBanPhu = layMaBanPhu(maBan);
//                System.out.println("ban phu :" + maBanPhu);
//                banAn_DAO.capNhatTrangThaiBan(maBanPhu, "Đã thanh toán");  // Cập nhật bàn chính là đã thanh toán
//            }
            
            
            
            
           
// Cập nhật số lượng khuyến mãi cho khách hàng
            KhuyenMaiKhachHang_DAO kmKhachHangDAO = new KhuyenMaiKhachHang_DAO();
            List<KhuyenMaiKhachHang> dsKhuyenMai = kmKhachHangDAO.getKhuyenMaiByMaKhachHang(khachHang.getMaKhachHang());

            String loaiKhuyenMai = (String) jComboBoxloaiKhuyenMai.getSelectedItem();
            String giamGiaChon = (String) jComboBoxGiamGia.getSelectedItem();
            if(giamGiaChon==null){
                giamGiaChon="";
            }
            if(giamGiaChon.equals("Không có khuyến mãi nào") ){
                giamGiaChon="";
            }
            
            double phanTramGiam=0;
    if(loaiKhuyenMai.equals("Phần trăm") ){
         if (!giamGiaChon.isEmpty()) { // Kiểm tra xem giá trị giảm giá có rỗng không
        String[] parts = giamGiaChon.split(" ");
        if (parts.length > 2) { // Kiểm tra phần tử của mảng có đủ không
            phanTramGiam = Double.parseDouble(parts[2].replace("%", "")); // Lấy phần trăm giảm
        }
    }   
    }
      // 11/12 
    if(loaiKhuyenMai.equals("Tiền") ){
     if (!giamGiaChon.isEmpty()) { // Kiểm tra xem giá trị giảm giá có rỗng không
        String[] parts = giamGiaChon.split(" ");
        if (parts.length > 2) { // Kiểm tra phần tử của mảng có đủ không
            phanTramGiam = Double.parseDouble(parts[2]); // Lấy giá trị giảm
        }
    }  
    }
    //
    
    
    
            // Duyệt qua danh sách khuyến mãi của khách hàng và chỉ giảm số lượng nếu đúng loại và giá trị giảm giá
            for (KhuyenMaiKhachHang kmkh : dsKhuyenMai) {
                // Phần trăm
                if (kmkh.getKhuyenMai().getLoaiKhuyenMai().equals(loaiKhuyenMai) && kmkh.getKhuyenMai().getGiaTriKhuyenMai()== phanTramGiam) {
                    int soLuongToiThieu = kmkh.getKhuyenMai().getSoLuongToiThieu();
                    if (soLuongToiThieu > 0) {
                        int soLuongMoi = soLuongToiThieu - 1;
                        kmkh.getKhuyenMai().setSoLuongToiThieu(soLuongMoi);
                        
                        if(khachHang.getDiemTichLuy()>0){
                        khachHang.setDiemTichLuy(khachHang.getDiemTichLuy()- (int) kmkh.getKhuyenMai().getDiemYeuCau());    
                        }
                        
                        
                          // Gọi hàm cập nhật số lượng tối thiểu khuyến mãi
        KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
        khuyenMaiDAO.capNhatSoLuongToiThieu(kmkh.getKhuyenMai().getMaKhuyenMai(), soLuongMoi);
                  
                    }
                    break;  // Dừng vòng lặp sau khi tìm và cập nhật khuyến mãi phù hợp
                }
                
                // tiền 11/12
                else if (kmkh.getKhuyenMai().getLoaiKhuyenMai().equals(loaiKhuyenMai) && kmkh.getKhuyenMai().getGiaTriKhuyenMai()== phanTramGiam) {
                    int soLuongToiThieu = kmkh.getKhuyenMai().getSoLuongToiThieu();
                    if (soLuongToiThieu > 0) {
                        int soLuongMoi = soLuongToiThieu - 1;
                        kmkh.getKhuyenMai().setSoLuongToiThieu(soLuongMoi);
                        
                        if(khachHang.getDiemTichLuy()>0){
                        khachHang.setDiemTichLuy(khachHang.getDiemTichLuy()- (int) kmkh.getKhuyenMai().getDiemYeuCau());    
                        }
                
                          // Gọi hàm cập nhật số lượng tối thiểu khuyến mãi
        KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
        khuyenMaiDAO.capNhatSoLuongToiThieu(kmkh.getKhuyenMai().getMaKhuyenMai(), soLuongMoi);
                  
                    }
                    break;  // Dừng vòng lặp sau khi tìm và cập nhật khuyến mãi phù hợp
                }
                // Quà tặng
                
                else if(kmkh.getKhuyenMai().getLoaiKhuyenMai().equals(loaiKhuyenMai) && kmkh.getKhuyenMai().getMoTaKhuyenMai()== giamGiaChon){
                    int soLuongToiThieu = kmkh.getKhuyenMai().getSoLuongToiThieu();
                    if (soLuongToiThieu > 0) {
                        int soLuongMoi = soLuongToiThieu - 1;
                        kmkh.getKhuyenMai().setSoLuongToiThieu(soLuongMoi);
                        
                          // Gọi hàm cập nhật số lượng tối thiểu khuyến mãi
            KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
            khuyenMaiDAO.capNhatSoLuongToiThieu(kmkh.getKhuyenMai().getMaKhuyenMai(), soLuongMoi);
                  
                    }
                    break;  // Dừng vòng lặp sau khi tìm và cập nhật khuyến mãi phù hợp
                }
            }
          // Cập nhật điểm tích lũy
            
            int diemMoi = khachHang.getDiemTichLuy() + (int) (tongHoaDon / 1000);
            khachHang.setDiemTichLuy(diemMoi);
           
            // Gọi hàm cập nhật điểm tích lũy
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            khachHangDAO.capNhatDiemTichLuy(khachHang.getMaKhachHang(), diemMoi);   
        } else {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thanh toán. Vui lòng thử lại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
    }
     jButtonThanhToan.setEnabled(false);
//     ManHinhThongTinHoaDon mh=new ManHinhThongTinHoaDon();
//     mh.docdulieuDSHD();
    }//GEN-LAST:event_jButtonThanhToanActionPerformed

    
    private boolean isBanPhu(String maBan) {
        return maBan != null && maBan.equals(BanManager.getInstance().getMaBanPhu());
    }
    
    private String layMaBanChinh(String maBanChinh) {
        return BanManager.getInstance().getMaBanChinh();
    }
    
    private String layMaBanPhu(String maBanPhu) {
        return BanManager.getInstance().getMaBanPhu();
    }

    public List<String> getBansPhuByChinh(String maBanChinh) {
        List<String> bansPhu = new ArrayList<>();
        // Dựa vào logic của BanManager để lấy danh sách bàn phụ
        if (BanManager.getInstance().getMaBanChinh().equals(maBanChinh)) {
            // Trả về danh sách bàn phụ nếu bàn chính khớp
            bansPhu.add(BanManager.getInstance().getMaBanPhu());
        }
        System.out.println("Ban phu llllllllllllll : " + bansPhu);
        return bansPhu;
    }
    private String laySoDienThoaiKH(String maBanChinh) {
        KhachHang_DAO kh = new KhachHang_DAO();
        return kh.laySoDienThoaiKhachGB(maBanChinh);
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
            java.util.logging.Logger.getLogger(FrameThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInHoaDon;
    private javax.swing.JButton jButtonThanhToan;
    private javax.swing.JComboBox<String> jComboBoxGiamGia;
    private javax.swing.JComboBox<String> jComboBoxloaiKhuyenMai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelmaKhachHang;
    private javax.swing.JLabel jLabelmaKhachHang1;
    private javax.swing.JLabel jLabelmaKhachHang10;
    private javax.swing.JLabel jLabelmaKhachHang11;
    private javax.swing.JLabel jLabelmaKhachHang12;
    private javax.swing.JLabel jLabelmaKhachHang13;
    private javax.swing.JLabel jLabelmaKhachHang14;
    private javax.swing.JLabel jLabelmaKhachHang15;
    private javax.swing.JLabel jLabelmaKhachHang2;
    private javax.swing.JLabel jLabelmaKhachHang3;
    private javax.swing.JLabel jLabelmaKhachHang4;
    private javax.swing.JLabel jLabelmaKhachHang5;
    private javax.swing.JLabel jLabelmaKhachHang6;
    private javax.swing.JLabel jLabelmaKhachHang7;
    private javax.swing.JLabel jLabelmaKhachHang8;
    private javax.swing.JLabel jLabelmaKhachHang9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabledanhSachMonAn;
    private javax.swing.JTextField jTextFieldVAT;
    private javax.swing.JTextField jTextFieldchietKhau;
    private javax.swing.JTextField jTextFieldgioLap;
    private javax.swing.JTextField jTextFieldgoiYTien1;
    private javax.swing.JTextField jTextFieldgoiYTien2;
    private javax.swing.JTextField jTextFieldgoiYTien3;
    private javax.swing.JTextField jTextFieldgoiYTien4;
    private javax.swing.JTextField jTextFieldgoiYTien5;
    private javax.swing.JTextField jTextFieldgoiYTien6;
    private javax.swing.JTextField jTextFieldmaBan;
    private javax.swing.JTextField jTextFieldmaHoaDon;
    private javax.swing.JTextField jTextFieldphuongThucThanhToan;
    private javax.swing.JTextField jTextFieldsoDienThoaiKhachHang;
    private javax.swing.JTextField jTextFieldsoLuongKhachHang;
    private javax.swing.JTextField jTextFieldsoTienGiamGia;
    private javax.swing.JTextField jTextFieldtenKhachHang;
    private javax.swing.JTextField jTextFieldtenNhanVien;
    private javax.swing.JTextField jTextFieldtienKhachDua;
    private javax.swing.JTextField jTextFieldtienTraLaiChoKhach;
    private javax.swing.JTextField jTextFieldtongHoaDon;
    private javax.swing.JTextField jTextFieldtongTienMonAn;
    // End of variables declaration//GEN-END:variables
}
