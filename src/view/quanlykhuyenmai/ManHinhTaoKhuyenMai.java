/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.quanlykhuyenmai;


import connectDB.ConnectJDBC;
import java.awt.Color;


import java.time.LocalDateTime;
import java.sql.Date;
import com.toedter.calendar.JDateChooser;



import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.MonAnKhuyenMai_DAO;
import dao.MonAn_DAO;
import dao.DanhMucMonAn_DAO;
import dao.KhuyenMaiKhachHang_DAO;

import modal.KhuyenMai;
import modal.KhachHang;
import modal.MonAn;
import modal.DanhMucMonAn;
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




public class ManHinhTaoKhuyenMai extends javax.swing.JPanel {
    
private DefaultTableModel tableModel;

private MonAn_DAO monAnDAO;
private DanhMucMonAn_DAO danhMucMonAnDAO;
private KhachHang_DAO khachHangDAO;
private KhuyenMai_DAO khuyenMaiDAO;
private MonAnKhuyenMai_DAO monAnKhuyenMaiDAO;
private KhuyenMaiKhachHang_DAO khuyenMaiKhachHangDAO;

Connection connection; 
   
    public ManHinhTaoKhuyenMai() {
        
        connection = ConnectJDBC.getConnection();
        this.khuyenMaiKhachHangDAO = new KhuyenMaiKhachHang_DAO();
        this.khuyenMaiDAO = new KhuyenMai_DAO();
        this.monAnKhuyenMaiDAO = new MonAnKhuyenMai_DAO();
        this.monAnDAO = new MonAn_DAO();
        this.khachHangDAO = new KhachHang_DAO();
        // Khởi tạo bảng
        initComponents();
        
        // Thiết lập ngày tối thiểu cho jDateChooserngayBatDau
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // Tạo đối tượng java.sql.Date với thời gian hiện tại
        jDateChooserngayBatDau.setMinSelectableDate(new java.util.Date(currentDate.getTime())); // Chuyển đổi sang java.util.Date

        // Thiết lập ngày tối thiểu cho jDateChooserngayKetThuc
        jDateChooserngayKetThuc.setMinSelectableDate(new java.util.Date(currentDate.getTime())); // Chuyển đổi sang java.util.Date
        

        // Trong hàm khởi tạo
        jComboBoxtenDanhMuc.setEnabled(false); // Khóa mặc định
        jComboBoxtenMonAn.setEnabled(false); // Khóa mặc định
        
        //hienThiTenMonAn(); // Gọi hàm để lấy danh sách món ăn
        hienThiDanhMucMonAn();
        
        
        jComboBoxtenDanhMuc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tenDanhMuc = (String) jComboBoxtenDanhMuc.getSelectedItem();
                hienThiMonAnTheoDanhMuc(tenDanhMuc); // Gọi phương thức để hiển thị món ăn theo danh mục
            }
        });

        // Thiết lập sự kiện cho jRadioButtonmonAn
        jRadioButtonmonAn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jComboBoxtenDanhMuc.setEnabled(true); // Kích hoạt ComboBox
                jComboBoxtenMonAn.setEnabled(true); // Kích hoạt ComboBox
                
                //jTextFieldsoLuongToiThieu.setText("Áp dụng trong thời gian !"); // Đảm bảo trường có giá trị không đổi
                //jTextFieldsoLuongToiThieu.setEnabled(false); // Ẩn trường nhập giá trị khuyến mãi tránh người dùng sửa
                
                
                jTextFieldmoTaKhuyenMai.setText("Không cần nhập"); // Đảm bảo trường có giá trị là tuân theo tên món ăn
                jTextFieldmoTaKhuyenMai.setEnabled(false); // Ẩn trường nhập giá trị khuyến mãi tránh người dùng sửa
                jTextFieldgiaTriKhuyenMai.setEnabled(true);
            }
        });
              

        // Thiết lập sự kiện cho jRadioButtonphanTram
        jRadioButtonphanTram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jComboBoxtenDanhMuc.setEnabled(false);
                jComboBoxtenMonAn.setEnabled(false); // Kích hoạt ComboBox
                jTextFieldgiaTriKhuyenMai.setEnabled(true);
                //jTextFieldsoLuongToiThieu.setEnabled(true); 
                jTextFieldmoTaKhuyenMai.setEnabled(true); // 
            }
        });

        

        // Thiết lập sự kiện cho jRadioButtontongDon
        jRadioButtontien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jComboBoxtenDanhMuc.setEnabled(false);
                jComboBoxtenMonAn.setEnabled(false); // Kích hoạt ComboBox
                jTextFieldgiaTriKhuyenMai.setEnabled(true);
                //jTextFieldsoLuongToiThieu.setEnabled(true); 
                jTextFieldmoTaKhuyenMai.setEnabled(true); 
            }
        });
        
        
        // Thiết lập sự kiện cho jRadioButtonquaTang
        jRadioButtonquaTang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jComboBoxtenDanhMuc.setEnabled(false);
                jComboBoxtenMonAn.setEnabled(false); // Kích hoạt ComboBox
                jTextFieldgiaTriKhuyenMai.setText("0"); // Đảm bảo trường có giá trị là 0
                jTextFieldgiaTriKhuyenMai.setEnabled(false); // Ẩn trường nhập giá trị khuyến mãi nếu cần
                //jTextFieldsoLuongToiThieu.setEnabled(true); 
                jTextFieldmoTaKhuyenMai.setEnabled(true); // Ẩn trường nhập giá trị khuyến mãi nếu cần
            }
        });


        
        // Hiển thị dữ liệu lên bảng
        try {
            List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiDuy();

            // Lấy 10 dòng đầu tiên từ danh sách khuyến mãi
            List<KhuyenMai> top10KhuyenMai = danhSachKhuyenMai.subList(0, Math.min(10, danhSachKhuyenMai.size()));

            hienThiLenBang(top10KhuyenMai);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Hiển thị dữ liệu lên bảng
        hienThiDanhSachKhuyenMai();

        // Thêm sự kiện cho jTable
        jTabledanhSachKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jTabledanhSachKhuyenMai.getSelectedRow();
                if (row != -1) {
                    String maKhuyenMai = jTabledanhSachKhuyenMai.getValueAt(row, 0).toString();
                    try {
                        KhuyenMai khuyenMai = khuyenMaiDAO.layKhuyenMaiTheoMaDuy(maKhuyenMai);
                        jTabledanhSachKhuyenMaiMouseClicked(khuyenMai);
                        hienThiDanhSachApDung(maKhuyenMai); // Gọi phương thức hiển thị danh sách áp dụng
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        
        // Thiết lập trường maKhuyenMai không cho phép chỉnh sửa và thêm thông báo
        jTextFieldmaKhuyenMai.setEditable(false);
        jTextFieldmaKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Mã khuyến mãi sẽ tự động được tạo bởi hệ thống.");
            }
        });
        
        
        // Load loại trạng thái khuyến mãi đã hết hạn chưa cũng như khuyến mãi đã hết số lượng chưa                       
        try {
            // Cập nhật trạng thái khuyến mãi
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();
            flashTable(jTabledanhSachKhuyenMai);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        jTextFieldkhachHang.setEnabled(false);
    }
    
    
    
    // Hàm để loại bỏ dấu
    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }
    
    // Xử lý sự kiện chọn vào bảng sẽ hiện trường dữ liệu tương ứng
    private void jTabledanhSachKhuyenMaiMouseClicked(KhuyenMai khuyenMai) {
        // Cập nhật thông tin khuyến mãi vào các trường nhập liệu
        jTextFieldmaKhuyenMai.setText(khuyenMai.getMaKhuyenMai());
        jTextFieldmoTaKhuyenMai.setText(khuyenMai.getMoTaKhuyenMai());
        jTextFieldgiaTriKhuyenMai.setText(String.valueOf(khuyenMai.getGiaTriKhuyenMai()));
        jTextFielddiemYeuCau.setText(String.valueOf(khuyenMai.getDiemYeuCau()));
        jDateChooserngayBatDau.setDate(Date.valueOf(khuyenMai.getNgayBatDau().toLocalDate())); // Chuyển đổi LocalDateTime thành LocalDate
        jDateChooserngayKetThuc.setDate(Date.valueOf(khuyenMai.getNgayKetThuc().toLocalDate())); // Chuyển đổi LocalDateTime thành LocalDate
        jTextFieldsoLuongToiThieu.setText(String.valueOf(khuyenMai.getSoLuongToiThieu()));
        jTextFieldtenKhuyenMai.setText(khuyenMai.getTenKhuyenMai());
        
        // Cập nhật RadioButton cho loại khuyến mãi
        String loaiKhuyenMai = removeDiacritics(khuyenMai.getLoaiKhuyenMai().toLowerCase().trim());

        switch (loaiKhuyenMai) {
            case "phan tram":
                jRadioButtonphanTram.setSelected(true);
                break;
            case "qua tang":
                jRadioButtonquaTang.setSelected(true);
                break;
            case "tien":
                jRadioButtontien.setSelected(true);
                break;
            case "mon an":
                jRadioButtonmonAn.setSelected(true);
                break;
        }
    }

    //--------------------------/~.~/--------------------------------//   
    // Hiển thị dữ liệu lên bảng
    private void hienThiLenBang(List<KhuyenMai> danhSachKhuyenMai) {
        // Xóa tất cả dữ liệu cũ trong bảng
        DefaultTableModel model = (DefaultTableModel) jTabledanhSachKhuyenMai.getModel();
        model.setRowCount(0); // Xóa tất cả các hàng hiện có trong bảng

        // Thêm dữ liệu từ danh sách khuyến mãi vào bảng
        for (KhuyenMai km : danhSachKhuyenMai) {
            String maKhuyenMai = km.getMaKhuyenMai();
            String tenKhuyenMai = km.getTenKhuyenMai();
            String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
            double giaTriKhuyenMai = km.getGiaTriKhuyenMai();
            String trangThai = km.getTrangThai();

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
                loaiKhuyenMai, // Giữ nguyên loại khuyến mãi gốc
                giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
                trangThai
            });
        }
    }

    
    //Hàm để load và hiển thị dữ liệu khuyến mãi lên bảng
    public void hienThiDanhSachKhuyenMai() {
        try {
            // Cập nhật trạng thái khuyến mãi trước
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();

            // Lấy danh sách khuyến mãi sau khi cập nhật
            List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiDuy();

            // Tạo model cho bảng
            DefaultTableModel model = (DefaultTableModel) jTabledanhSachKhuyenMai.getModel();
            model.setRowCount(0); // Xóa các dòng hiện tại

            // Lặp qua danh sách khuyến mãi để thêm vào bảng
            for (KhuyenMai km : danhSachKhuyenMai) {
                String maKhuyenMai = km.getMaKhuyenMai();
                String tenKhuyenMai = km.getTenKhuyenMai();
                String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase()); // Chuyển về chữ thường và loại bỏ dấu
                double giaTriKhuyenMai = km.getGiaTriKhuyenMai();
                String trangThai = km.getTrangThai();

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
                    km.getLoaiKhuyenMai(), // Giữ nguyên loại khuyến mãi gốc
                    giaTriKhuyenMaiHienThi, // Sử dụng giá trị đã định dạng
                    trangThai
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Khi chọn 1 dòng khuyến mãi thì sẽ xem được đối tượng nào được nhận khuyến mãi này
    public void hienThiDanhSachApDung(String maKhuyenMai) {
        try {
            // Khởi tạo các đối tượng DAO
            MonAnKhuyenMai_DAO monAnKhuyenMaiDAO = new MonAnKhuyenMai_DAO();
            KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            KhuyenMaiKhachHang_DAO khuyenMaiKhachHangDAO = new KhuyenMaiKhachHang_DAO();

            DefaultTableModel model = (DefaultTableModel) jTabledanhSachApDung.getModel();
            model.setRowCount(0); // Xóa các hàng hiện tại

            // Lấy thông tin khuyến mãi
            KhuyenMai khuyenMai = khuyenMaiDAO.layKhuyenMaiTheoMaDuy(maKhuyenMai);

            if (khuyenMai != null) {
                // Kiểm tra trạng thái
                if ("Expired".equals(khuyenMai.getTrangThai())) {
                    model.addRow(new Object[]{"Mã đã hết hạn", "Không còn hiệu lực"});
                    return; // Kết thúc phương thức nếu mã đã hết hạn
                }

                // Kiểm tra số lượng tối thiểu
                if (khuyenMai.getSoLuongToiThieu() <= 0) {
                    model.addRow(new Object[]{"Mã đã hết số lượng", "Không còn hiệu lực"});
                    return; // Kết thúc phương thức nếu số lượng tối thiểu đã hết
                }

                // Nếu còn hiệu lực, kiểm tra mã trong bảng MonAnKhuyenMai
                if (monAnKhuyenMaiDAO.checkKhuyenMaiMonAnDuy(maKhuyenMai)) {
                    List<MonAn> danhSachMonAn = monAnKhuyenMaiDAO.layMonAnTheoKhuyenMaiDuy(maKhuyenMai);
                    for (MonAn monAn : danhSachMonAn) {
                        model.addRow(new Object[]{monAn.getMaMonAn(), monAn.getTenMonAn()}); // Hiển thị mã và tên món ăn
                    }
                } else {
                    // Nếu không có trong MonAnKhuyenMai, lấy danh sách khách hàng có mã khuyến mãi này
                    List<String> danhSachKhachHang = khuyenMaiKhachHangDAO.layDanhSachKhachHangTheoKhuyenMaiDuy(maKhuyenMai);
                    if (!danhSachKhachHang.isEmpty()) {
                        for (String maKhachHang : danhSachKhachHang) {
                            KhachHang khachHang = khachHangDAO.layKhachHangTheoMaDuy(maKhachHang);
                            // Thêm mã khách hàng và tên khách hàng vào bảng
                            model.addRow(new Object[]{khachHang.getMaKhachHang(), khachHang.getTenKhachHang()});
                        }
                    } else {
                        // Phần này dành cho loại khuyến mãi là quà tặng 
                        // Nếu không có trong MonAnKhuyenMai và cũng không có khách hàng, hiển thị thông tin khuyến mãi
                        String moTaKhuyenMai = khuyenMai.getMoTaKhuyenMai(); // Lấy mô tả khuyến mãi từ chính mã khuyến mãi đó
                        model.addRow(new Object[]{"Được tặng", moTaKhuyenMai});
                    }
                }
            } else {
                model.addRow(new Object[]{"Mã không tồn tại", "Không có thông tin"});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần
        }
    }
    
    
   
    //--------------------------/~.~/--------------------------------//  
    // Hàm để reload dữ liệu khuyến mãi khách hàng
    public void reloadKhuyenMaiKhachHang() throws SQLException {
        resetFields();    
        // Step 1: Clear data in the intermediary table
        khuyenMaiKhachHangDAO.xoaTatCaKhuyenMaiKhachHangDuy();

        // Step 2: Get the list of active promotions
        List<KhuyenMai> danhSachKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMaiHieuLucDuy();

        
        // Step lấy ngày hiện tại: Get the current date and time
        LocalDateTime ngayHienTai = LocalDateTime.now();
    
        // Step 3: Loop through each promotion
        for (KhuyenMai km : danhSachKhuyenMai) {
            String maKhuyenMai = km.getMaKhuyenMai();
            double diemYeuCau = km.getDiemYeuCau();
            
            // Lấy ngày bắt đầu và ngày kết thúc
            LocalDateTime ngayBatDau = km.getNgayBatDau();
            LocalDateTime ngayKetThuc = km.getNgayKetThuc();
            
            // Kiểm tra xem khuyến mãi có trong khoảng thời gian hợp lệ không
            if (ngayBatDau.isAfter(ngayHienTai) || ngayKetThuc.isBefore(ngayHienTai)) {
                continue; // Bỏ qua nếu khuyến mãi không trong khoảng thời gian hợp lệ
            }

            // Lấy loại khuyến mãi và loại bỏ dấu
            String loaiKhuyenMai = removeDiacritics(km.getLoaiKhuyenMai().toLowerCase().trim());

            // Kiểm tra loại khuyến mãi
            if (loaiKhuyenMai == null || 
                (!loaiKhuyenMai.equals("phan tram") && !loaiKhuyenMai.equals("tien") && !loaiKhuyenMai.equals("qua tang") ) || 
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
    }


    //--------------------------/~.~/--------------------------------//  

    // Xử lý việc chọn món ăn khi thêm khuyến mãi loại Món Ăn __ Danh Mục
    private void hienThiDanhMucMonAn() {
        try {
            DanhMucMonAn_DAO danhMucDAO = new DanhMucMonAn_DAO(); // Tạo DAO cho danh mục
            List<DanhMucMonAn> danhSachDanhMuc = danhMucDAO.layTatCaDanhMuc(); // Lấy danh sách danh mục

            // Kiểm tra xem danh sách có rỗng không
            if (danhSachDanhMuc.isEmpty()) {
                System.out.println("Danh sách danh mục món ăn rỗng.");
            } else {
                // Thêm tên danh mục vào JComboBox
                for (DanhMucMonAn danhMuc : danhSachDanhMuc) {
                    jComboBoxtenDanhMuc.addItem(danhMuc.getTenDanhMuc());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ
        }
    }

    // Xử lý việc chọn món ăn khi thêm khuyến mãi loại Món Ăn __ Món Ăn
    private void hienThiMonAnTheoDanhMuc(String tenDanhMuc) {
        jComboBoxtenMonAn.removeAllItems(); // Xóa tất cả các món ăn hiện tại
        try {
            MonAn_DAO monAnDAO = new MonAn_DAO();
            List<MonAn> danhSachMonAn = monAnDAO.layMonAnTheoDanhMucDuy(tenDanhMuc); // Lấy món ăn theo danh mục

            if (danhSachMonAn.isEmpty()) {
                System.out.println("Không có món ăn nào trong danh mục này.");
            } else {
                for (MonAn monAn : danhSachMonAn) {
                    jComboBoxtenMonAn.addItem(monAn.getTenMonAn());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ
        }
    }



//--------------------------/~.~/--------------------------------//   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabledanhSachKhuyenMai = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabelmaKhuyenMai = new javax.swing.JLabel();
        jTextFieldmaKhuyenMai = new javax.swing.JTextField();
        jLabelgiamGia = new javax.swing.JLabel();
        jTextFieldgiaTriKhuyenMai = new javax.swing.JTextField();
        jLabelngayBatDau = new javax.swing.JLabel();
        jLabelngayKetThuc = new javax.swing.JLabel();
        jDateChooserngayBatDau = new com.toedter.calendar.JDateChooser();
        jDateChooserngayKetThuc = new com.toedter.calendar.JDateChooser();
        jLabelmoTa = new javax.swing.JLabel();
        jTextFieldmoTaKhuyenMai = new javax.swing.JTextField();
        jLabelloaiKhuyenMai = new javax.swing.JLabel();
        jRadioButtonquaTang = new javax.swing.JRadioButton();
        jRadioButtontien = new javax.swing.JRadioButton();
        jRadioButtonphanTram = new javax.swing.JRadioButton();
        jLabeldiemYeuCau = new javax.swing.JLabel();
        jTextFielddiemYeuCau = new javax.swing.JTextField();
        jTextFieldsoLuongToiThieu = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxtenMonAn = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldtenKhuyenMai = new javax.swing.JTextField();
        jRadioButtonmonAn = new javax.swing.JRadioButton();
        jComboBoxtenDanhMuc = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButtontaoMoi = new javax.swing.JButton();
        jButtondungKhuyenMai = new javax.swing.JButton();
        jButtonxoaTrang = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabledanhSachApDung = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButtonReload = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelsoDienThoai = new javax.swing.JLabel();
        jTextFieldsoDienThoai = new javax.swing.JTextField();
        jCheckBoxsoDienThoai = new javax.swing.JCheckBox();
        jLabelkhachHang1 = new javax.swing.JLabel();
        jTextFieldkhachHang = new javax.swing.JTextField();
        jButtonthemKhuyenMaiKhachHang = new javax.swing.JButton();

        jLabel5.setText("jLabel5");

        jTabledanhSachKhuyenMai.setAutoCreateRowSorter(true);
        jTabledanhSachKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã khuyến mãi", "Tên khuyễn mãi", "Loại", "Giảm giá", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabledanhSachKhuyenMai.setToolTipText("");
        jTabledanhSachKhuyenMai.setRowHeight(40);
        jScrollPane1.setViewportView(jTabledanhSachKhuyenMai);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khuyến mãi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jLabelmaKhuyenMai.setText("Mã khuyễn mãi:");

        jLabelgiamGia.setText("Giá trị khuyến mãi:");

        jTextFieldgiaTriKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldgiaTriKhuyenMaiActionPerformed(evt);
            }
        });

        jLabelngayBatDau.setText("Ngày bắt đầu:");

        jLabelngayKetThuc.setText("Ngày kết thúc:");

        jLabelmoTa.setText("Mô tả:");

        jTextFieldmoTaKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldmoTaKhuyenMaiActionPerformed(evt);
            }
        });

        jLabelloaiKhuyenMai.setText("Loại khuyến mãi:");

        buttonGroup1.add(jRadioButtonquaTang);
        jRadioButtonquaTang.setText("Quà tặng");
        jRadioButtonquaTang.setContentAreaFilled(false);
        jRadioButtonquaTang.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jRadioButtonquaTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonquaTangActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtontien);
        jRadioButtontien.setText("Tiền");
        jRadioButtontien.setContentAreaFilled(false);
        jRadioButtontien.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jRadioButtontien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtontienActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonphanTram);
        jRadioButtonphanTram.setText("Phần trăm");
        jRadioButtonphanTram.setContentAreaFilled(false);
        jRadioButtonphanTram.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabeldiemYeuCau.setText("Điểm yêu cầu:");

        jLabel2.setText("Số lượng tối thiểu");

        jComboBoxtenMonAn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn Món Ăn--", " " }));
        jComboBoxtenMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtenMonAnActionPerformed(evt);
            }
        });

        jLabel4.setText("Chọn món ăn:");

        jLabel6.setText("Tên khuyến mãi:");

        jTextFieldtenKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldtenKhuyenMaiActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonmonAn);
        jRadioButtonmonAn.setText("Món ăn");
        jRadioButtonmonAn.setContentAreaFilled(false);
        jRadioButtonmonAn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jComboBoxtenDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn Danh Mục--" }));
        jComboBoxtenDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxtenDanhMucActionPerformed(evt);
            }
        });

        jLabel7.setText("Chọn danh mục:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtontien)
                            .addComponent(jRadioButtonquaTang))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonphanTram)
                            .addComponent(jRadioButtonmonAn)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelmaKhuyenMai)
                            .addComponent(jLabelngayBatDau)
                            .addComponent(jLabelngayKetThuc)
                            .addComponent(jLabelmoTa)
                            .addComponent(jLabel6)
                            .addComponent(jLabelloaiKhuyenMai)
                            .addComponent(jLabelgiamGia)
                            .addComponent(jLabeldiemYeuCau)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxtenDanhMuc, 0, 223, Short.MAX_VALUE)
                            .addComponent(jTextFieldmaKhuyenMai)
                            .addComponent(jTextFieldmoTaKhuyenMai)
                            .addComponent(jDateChooserngayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserngayKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldgiaTriKhuyenMai)
                            .addComponent(jTextFielddiemYeuCau)
                            .addComponent(jTextFieldsoLuongToiThieu)
                            .addComponent(jComboBoxtenMonAn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldtenKhuyenMai))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldmaKhuyenMai)
                    .addComponent(jLabelmaKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldtenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldmoTaKhuyenMai)
                    .addComponent(jLabelmoTa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserngayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelngayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelngayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelloaiKhuyenMai)
                        .addComponent(jRadioButtonquaTang)
                        .addComponent(jRadioButtonmonAn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButtonphanTram)
                            .addComponent(jRadioButtontien))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldgiaTriKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelgiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFielddiemYeuCau)
                    .addComponent(jLabeldiemYeuCau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldsoLuongToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxtenDanhMuc)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxtenMonAn, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức Năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jButtontaoMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pen (1).png"))); // NOI18N
        jButtontaoMoi.setText("Tạo mới");
        jButtontaoMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtontaoMoiActionPerformed(evt);
            }
        });

        jButtondungKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew2.png"))); // NOI18N
        jButtondungKhuyenMai.setText("Dừng khuyến mãi");
        jButtondungKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtondungKhuyenMaiActionPerformed(evt);
            }
        });

        jButtonxoaTrang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        jButtonxoaTrang.setText("Xóa trắng");
        jButtonxoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonxoaTrangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jButtontaoMoi)
                .addGap(4, 4, 4)
                .addComponent(jButtondungKhuyenMai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonxoaTrang))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtontaoMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtondungKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonxoaTrang, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Áp Dụng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jTabledanhSachApDung.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã", "Tên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabledanhSachApDung.setRequestFocusEnabled(false);
        jTabledanhSachApDung.setRowHeight(40);
        jScrollPane2.setViewportView(jTabledanhSachApDung);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Danh Sách Khuyễn Mãi");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButtonReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        jButtonReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReloadActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thêm khuyến mãi khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 12))); // NOI18N

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
        jLabelkhachHang1.setText("KH:");

        jTextFieldkhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldkhachHangActionPerformed(evt);
            }
        });

        jButtonthemKhuyenMaiKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonthemKhuyenMaiKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/them.png"))); // NOI18N
        jButtonthemKhuyenMaiKhachHang.setText("Thêm KM KH");
        jButtonthemKhuyenMaiKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonthemKhuyenMaiKhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jButtonthemKhuyenMaiKhachHang)
                .addGap(49, 49, 49)
                .addComponent(jCheckBoxsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabelsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldsoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addGap(44, 44, 44)
                .addComponent(jLabelkhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldkhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFieldkhachHang)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelsoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonthemKhuyenMaiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldsoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addComponent(jLabelkhachHang1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCheckBoxsoDienThoai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addComponent(jButtonReload, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 816, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonReload))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jRadioButtonquaTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonquaTangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonquaTangActionPerformed

    private void jRadioButtontienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtontienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtontienActionPerformed

    private void jButtonxoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonxoaTrangActionPerformed
        // TODO add your handling code here:
        resetFields(); 
        
        // Tạo model cho bảng
        DefaultTableModel model = (DefaultTableModel) jTabledanhSachApDung.getModel();
        model.setRowCount(0); // Xóa các dòng hiện tại
    }//GEN-LAST:event_jButtonxoaTrangActionPerformed

    private void jButtondungKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtondungKhuyenMaiActionPerformed
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
        String trangThai = jTabledanhSachKhuyenMai.getValueAt(selectedRow, 4).toString(); // Giả sử cột 4 là trạng thái

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
    }//GEN-LAST:event_jButtondungKhuyenMaiActionPerformed

    private void jTextFieldgiaTriKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldgiaTriKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldgiaTriKhuyenMaiActionPerformed

    private void jTextFieldmoTaKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldmoTaKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldmoTaKhuyenMaiActionPerformed

    
    
    private void jButtontaoMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtontaoMoiActionPerformed
        // TODO add your handling code here:
        
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setTrangThai("Active");
        khuyenMai.setQuanLy(new QuanLy("QL0001")); // Thiết lập mã quản lý mặc định

        try {
            // Tạo mã khuyến mãi tự động
            String maKhuyenMai = khuyenMaiDAO.generateMaKhuyenMaiDuy();
            if (maKhuyenMai == null || maKhuyenMai.isEmpty()) {
                showError("Lỗi hệ thống: Không thể tạo mã khuyến mãi.");
                return;
            }
            khuyenMai.setMaKhuyenMai(maKhuyenMai);

            // Kiểm tra và thiết lập thông tin khuyến mãi
            if (!setKhuyenMaiDetails(khuyenMai, false)) { //False vì không thực hiện chức năng thêm kmkhr
                return; // Nếu có lỗi, dừng lại
            }

            // Hiển thị thông tin khuyến mãi để xác nhận
            if (!confirmKhuyenMai(khuyenMai)) {
                return; // Nếu người dùng không xác nhận, dừng lại
            }


            

            // Thêm khuyến mãi vào cơ sở dữ liệu
            khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);
            JOptionPane.showMessageDialog(null, "Thêm khuyến mãi thành công!");

            
            // Reload danh sách áp dụng cho tất cả các khuyến mãi
            reloadKhuyenMaiKhachHang();
            
            
            // Xóa trắng
            resetFields();

            // Cập nhật lại danh sách khuyến mãi
            hienThiDanhSachKhuyenMai();

            // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
            flashTable(jTabledanhSachKhuyenMai);

            // Tạo model cho bảng
            DefaultTableModel model = (DefaultTableModel) jTabledanhSachApDung.getModel();
            model.setRowCount(0); // Xóa các dòng hiện tại


        } catch (SQLException e) {
            e.printStackTrace();
            showError("Lỗi khi thêm khuyến mãi: " + e.getMessage());
        }
    }//GEN-LAST:event_jButtontaoMoiActionPerformed

    
    // Xử lý CHỨC NĂNG _ THÊM _ Khuyến Mãi
    private boolean setKhuyenMaiDetails(KhuyenMai khuyenMai, boolean isForCustomer) {
        // Kiểm tra tên và mô tả khuyến mãi
        String tenKhuyenMai = jTextFieldtenKhuyenMai.getText().trim();
        if (tenKhuyenMai.isEmpty()) {
            showError("Tên khuyến mãi không được để trống.");
            return false;
        }
        khuyenMai.setTenKhuyenMai(tenKhuyenMai);

        String moTaKhuyenMai = jTextFieldmoTaKhuyenMai.getText().trim();
        if (moTaKhuyenMai.isEmpty()) {
            showError("Mô tả khuyến mãi không được để trống.");
            return false;
        }
        khuyenMai.setMoTaKhuyenMai(moTaKhuyenMai);

        // Ngày bắt đầu và kết thúc
        LocalDateTime ngayBatDau = getNgayBatDau();
        if (ngayBatDau == null) return false;
        khuyenMai.setNgayBatDau(ngayBatDau);

        LocalDateTime ngayKetThuc = getNgayKetThuc(ngayBatDau);
        if (ngayKetThuc == null) return false;
        khuyenMai.setNgayKetThuc(ngayKetThuc);

        // Lấy loại khuyến mãi từ radio button
        String loaiKhuyenMai = getSelectedLoaiKhuyenMai();
        if (loaiKhuyenMai == null) {
            showError("Vui lòng chọn loại khuyến mãi.");
            return false;
        }
        khuyenMai.setLoaiKhuyenMai(loaiKhuyenMai); // Thiết lập loại khuyến mãi

        // Nếu là khuyến mãi cho khách hàng, chỉ cho phép 3 loại
        if (isForCustomer) {
            if (jRadioButtonmonAn.isSelected()) {
                showError("Khuyến mãi cho khách hàng không thể là món ăn.");
                return false;
            } else if (!jRadioButtontien.isSelected() && !jRadioButtonphanTram.isSelected() && !jRadioButtonquaTang.isSelected()) {
                showError("Vui lòng chọn loại khuyến mãi phù hợp cho khách hàng.");
                return false;
            }
        }

        // Kiểm tra loại khuyến mãi
        if (jRadioButtonmonAn.isSelected()) {
            return handleMonAn(khuyenMai);
        } else if (jRadioButtonphanTram.isSelected()) {
            return handlePhanTram(khuyenMai);
        } else if (jRadioButtontien.isSelected()) {
            return handleTien(khuyenMai);
        } else if (jRadioButtonquaTang.isSelected()) {
            return handleQuaTang(khuyenMai);
        }

        return false; // Trả về false nếu không có loại nào được chọn
    }

    // Xác nhận lại trước khi thêm
    private boolean confirmKhuyenMai(KhuyenMai khuyenMai) {
        int confirm = JOptionPane.showConfirmDialog(null,
            "Thông tin khuyến mãi:\n" +
            "Mã khuyến mãi: " + khuyenMai.getMaKhuyenMai() + "\n" +
            "Tên khuyến mãi: " + khuyenMai.getTenKhuyenMai() + "\n" +
            "Mô tả: " + khuyenMai.getMoTaKhuyenMai() + "\n" +
            "Ngày bắt đầu: " + khuyenMai.getNgayBatDau() + "\n" +
            "Ngày kết thúc: " + khuyenMai.getNgayKetThuc() + "\n" +
            "Giá trị khuyến mãi: " + khuyenMai.getGiaTriKhuyenMai() + "\n" +
            "Điểm yêu cầu: " + khuyenMai.getDiemYeuCau() + "\n" +
            "Số lượng tối thiểu : " + khuyenMai.getSoLuongToiThieu() + "\n" +
            "Bạn có chắc chắn muốn thêm khuyến mãi này không?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        return confirm == JOptionPane.YES_OPTION;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }    
    
    
    

    private boolean handleMonAn(KhuyenMai khuyenMai) {
        try {
            // Kiểm tra giá trị khuyến mãi
            double giaTriKhuyenMai = getGiaTriKhuyenMai();
            if (giaTriKhuyenMai < 0) return false; // Nếu có lỗi, dừng lại
            khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);

            // Kiểm tra điểm yêu cầu
            double diemYeuCau = getDiemYeuCau();
            if (diemYeuCau < 0) return false; // Nếu có lỗi, dừng lại
            khuyenMai.setDiemYeuCau(diemYeuCau);

            // Kiểm tra số lượng tối thiểu   
            int soLuongToiThieu = getSoLuongToiThieu();
            if (soLuongToiThieu < 0) return false; // Nếu có lỗi, dừng lại
            khuyenMai.setSoLuongToiThieu(soLuongToiThieu);

            // Gán mặc định số lượng tối thiểu
            //int soLuongToiThieu = 9999;
            //khuyenMai.setSoLuongToiThieu(soLuongToiThieu);

             // Kiểm tra danh mục đã chọn
            Object selectedDanhMuc = jComboBoxtenDanhMuc.getSelectedItem();
            if (selectedDanhMuc == null || selectedDanhMuc.toString().equals("--Chọn Danh Mục--")) {
                showError("Vui lòng chọn danh mục.");
                return false;
            }

            // Kiểm tra món ăn đã chọn chưa
            Object selectedMonAn = jComboBoxtenMonAn.getSelectedItem();
            if (selectedMonAn == null ) {
                showError("Vui lòng chọn danh mục.");
                return false;
            }

            //Kiểm tra món ăn đã chọn
            String tenMonAn = jComboBoxtenMonAn.getSelectedItem().toString();
            if (tenMonAn.equals("--Chọn Món Ăn--")) {
                showError("Vui lòng chọn món ăn.");
                return false;
            }


//            khuyenMai.setMoTaKhuyenMai(tenMonAn);
            
            // Tạo mô tả khuyến mãi theo mẫu "Giảm a% món b"
            String moTaKhuyenMai = String.format("Giảm %.2f%% món %s", giaTriKhuyenMai, tenMonAn);
            khuyenMai.setMoTaKhuyenMai(moTaKhuyenMai);



            // Lấy mã món ăn tương ứng
            MonAn_DAO monAnDAO = new MonAn_DAO(); // Nếu bạn không cần khởi tạo lại ở đây
            String maMonAn = monAnDAO.getMaMonAnDuy(tenMonAn); // Sử dụng đối tượng DAO để gọi phương thức
            if ((maMonAn == null) || selectedDanhMuc.toString().equals("--Chọn Danh Mục--") || selectedMonAn.toString().equals("--Chọn Món Ăn--")) {
                showError("Bạn chưa chọn món ăn.");
                return false;
            }

            // Thêm vào bảng KhuyenMai trước để tránh gây ra lỗi không có maKM từ bảng KhuyenMai từ đầu
            khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);

            // Thêm vào bảng MonAnKhuyenMai
            monAnKhuyenMaiDAO.themMonAnKhuyenMaiDuy(khuyenMai.getMaKhuyenMai(), maMonAn, giaTriKhuyenMai, soLuongToiThieu); 


            // Bỏ thông báo thành công
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            return false;
        }
    }

    private boolean handlePhanTram(KhuyenMai khuyenMai) {
        // Kiểm tra giá trị khuyến mãi
        double giaTriKhuyenMai = getGiaTriKhuyenMai();
        if (giaTriKhuyenMai <= 0) {
    //        JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải lớn hơn 0.");
            return false;
        }
        khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);

        // Kiểm tra điểm yêu cầu
        double diemYeuCau = getDiemYeuCau();
        if (diemYeuCau < 0) {
    //        JOptionPane.showMessageDialog(null, "Điểm yêu cầu phải lớn hơn hoặc bằng 0.");
            return false;
        }
        khuyenMai.setDiemYeuCau(diemYeuCau);

        // Kiểm tra số lượng tối thiểu
        int soLuongToiThieu = getSoLuongToiThieu();
        if (soLuongToiThieu < 0) {
    //        JOptionPane.showMessageDialog(null, "Số lượng tối thiểu phải lớn hơn hoặc bằng 0.");
            return false;
        }
        khuyenMai.setSoLuongToiThieu(soLuongToiThieu);

        return true;
        
//        // Thêm vào bảng KhuyenMai
//        try {
//            khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);
//            // Bỏ thông báo thành công
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khuyến mãi: " + e.getMessage());
//            return false;
//        }
    }

    private boolean handleTien(KhuyenMai khuyenMai) {
        // Kiểm tra giá trị khuyến mãi
        double giaTriKhuyenMai = getGiaTriKhuyenMaiTien();
        if (giaTriKhuyenMai <= 0) {
    //        JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải lớn hơn 0.");
            return false; // Dừng lại nếu có lỗi
        }
        khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);

        // Kiểm tra điểm yêu cầu
        double diemYeuCau = getDiemYeuCau();
        if (diemYeuCau < 0) {
    //        JOptionPane.showMessageDialog(null, "Điểm yêu cầu phải lớn hơn hoặc bằng 0.");
            return false; // Dừng lại nếu có lỗi
        }
        khuyenMai.setDiemYeuCau(diemYeuCau);

        // Kiểm tra số lượng tối thiểu
        int soLuongToiThieu = getSoLuongToiThieu();
        if (soLuongToiThieu < 0) {
    //        JOptionPane.showMessageDialog(null, "Số lượng tối thiểu phải lớn hơn hoặc bằng 0.");
            return false; // Dừng lại nếu có lỗi
        }
        khuyenMai.setSoLuongToiThieu(soLuongToiThieu);

        return true;
        
//        // Thêm vào bảng KhuyenMai
//        try {
//            khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);
//            // Bỏ thông báo thành công
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khuyến mãi: " + e.getMessage());
//            return false;
//        }
    }

    private boolean handleQuaTang(KhuyenMai khuyenMai) {
        // Thiết lập giá trị khuyến mãi mặc định là 0
        double giaTriKhuyenMai = 0; // Mặc định là 0

        // Kiểm tra xem người dùng có nhập giá trị khuyến mãi không
        String giaTriInput = jTextFieldgiaTriKhuyenMai.getText();
        if (!giaTriInput.isEmpty()) {
            try {
                giaTriKhuyenMai = Double.parseDouble(giaTriInput);
                // Nếu người dùng nhập giá trị khác 0, hiển thị thông báo
                if (giaTriKhuyenMai != 0) {
                    JOptionPane.showMessageDialog(null, "Quà tặng không cần nhập giá trị khuyến mãi.");
                    return false; // Dừng lại nếu có lỗi
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi không hợp lệ.");
                return false; // Dừng lại nếu không phải số
            }
        }

        // Thiết lập giá trị khuyến mãi cho đối tượng khuyến mãi
        khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);

        // Kiểm tra điểm yêu cầu
        double diemYeuCau = getDiemYeuCau();
        if (diemYeuCau < 0) {
    //        JOptionPane.showMessageDialog(null, "Điểm yêu cầu phải lớn hơn hoặc bằng 0.");
            return false; // Dừng lại nếu có lỗi
        }
        khuyenMai.setDiemYeuCau(diemYeuCau);

        // Kiểm tra số lượng tối thiểu
        int soLuongToiThieu = getSoLuongToiThieu();
        if (soLuongToiThieu < 0) {
    //        JOptionPane.showMessageDialog(null, "Số lượng tối thiểu phải lớn hơn hoặc bằng 0.");
            return false; // Dừng lại nếu có lỗi
        }
        khuyenMai.setSoLuongToiThieu(soLuongToiThieu);

        return true;
        
//        // Thêm vào bảng KhuyenMai
//        try {
//            khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);
//            // Bỏ thông báo thành công
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khuyến mãi: " + e.getMessage());
//            return false;
//        }
    }



    // Phương thức để lấy ngày bắt đầu
    private LocalDateTime getNgayBatDau() {
        if (jDateChooserngayBatDau.getDate() != null) {
            LocalDateTime ngayBatDau = LocalDateTime.ofInstant(jDateChooserngayBatDau.getDate().toInstant(), ZoneId.systemDefault());
            // Chấp nhận ngày hôm nay hoặc bất kỳ ngày nào trong tương lai
            if (ngayBatDau.isBefore(LocalDateTime.now().minusDays(1))) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu không được trước ngày hôm nay.");
                return null; // Dừng lại nếu điều kiện không thỏa mãn
            }
            return ngayBatDau;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu.");
            return null; // Dừng lại nếu không có ngày
        }
    }

    // Phương thức để lấy ngày kết thúc
    private LocalDateTime getNgayKetThuc(LocalDateTime ngayBatDau) {
        if (jDateChooserngayKetThuc.getDate() != null) {
            LocalDateTime ngayKetThuc = LocalDateTime.ofInstant(jDateChooserngayKetThuc.getDate().toInstant(), ZoneId.systemDefault());
            //if (ngayKetThuc.isBefore(ngayBatDau.plusDays(0))) 
            if (ngayKetThuc.isBefore(ngayBatDau)) 
            {
                JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày bắt đầu ít nhất 1 ngày.");
                return null; // Dừng lại nếu điều kiện không thỏa mãn
            }
            return ngayKetThuc;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày kết thúc.");
            return null; // Dừng lại nếu không có ngày
        }
    }



    // Phương thức để lấy giá trị khuyến mãi
    private int getGiaTriKhuyenMai() {
        String giaTriInput = jTextFieldgiaTriKhuyenMai.getText();
        if (giaTriInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi không được để trống.");
            return -1; // Trả về giá trị không hợp lệ
        }
        try {
            // Chuyển đổi đầu vào thành double
            double giaTriKhuyenMaiDouble = Double.parseDouble(giaTriInput);

            // Kiểm tra xem giá trị có nằm trong khoảng từ 1 đến 100 không
            if (giaTriKhuyenMaiDouble < 1 || giaTriKhuyenMaiDouble > 100) {
                JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải nằm trong khoảng từ 1 đến 100.");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Kiểm tra xem phần thập phân có bằng 0 không
            if (giaTriKhuyenMaiDouble % 1 != 0) {
                JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải là số nguyên (không có phần thập phân).");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Chuyển đổi thành int và trả về
            return (int) giaTriKhuyenMaiDouble;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải là số nguyên hợp lệ.");
            return -1; // Trả về giá trị không hợp lệ
        }
    }


    // Phương thức để lấy giá trị khuyến mãi loại Tiền
    private double getGiaTriKhuyenMaiTien() {
        String giaTriInput = jTextFieldgiaTriKhuyenMai.getText();

        // Kiểm tra nếu trường nhập liệu trống
        if (giaTriInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi không được để trống.");
            return -1; // Dừng lại nếu không có giá trị nhập
        }

        try {
            // Chuyển đổi đầu vào thành double
            double giaTriKhuyenMaiDouble = Double.parseDouble(giaTriInput); // Chuyển đổi sang số thực

            // Kiểm tra xem giá trị có lớn hơn 0 không
            if (giaTriKhuyenMaiDouble <= 0) {
                JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải lớn hơn 0.");
                return -1; // Dừng lại nếu giá trị không hợp lệ
            }

            // Kiểm tra xem phần thập phân có bằng 0 không
            if (giaTriKhuyenMaiDouble % 1 != 0) {
                JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải là số nguyên (không có phần thập phân).");
                return -1; // Dừng lại nếu không phải số nguyên
            }

            // Chuyển đổi thành int và nhân với 1000 để chuyển sang giá trị tiền
            return (int) giaTriKhuyenMaiDouble * 1000; 

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi không hợp lệ. Vui lòng nhập số nguyên.");
            return -1; // Dừng lại nếu không phải số nguyên
        }
    }

    // Phương thức để lấy điểm yêu cầu
    private int getDiemYeuCau() {
        String diemInput = jTextFielddiemYeuCau.getText();
        if (diemInput.isEmpty()) {
            return 0; // Mặc định là 0 nếu không nhập
        }
        try {
            // Chuyển đổi đầu vào thành double
            double diemDouble = Double.parseDouble(diemInput);

            // Kiểm tra xem điểm yêu cầu có lớn hơn hoặc bằng 0 không
            if (diemDouble < 0) {
                JOptionPane.showMessageDialog(null, "Điểm yêu cầu phải lớn hơn hoặc bằng 0.");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Kiểm tra xem phần thập phân có bằng 0 không
            if (diemDouble % 1 != 0) {
                JOptionPane.showMessageDialog(null, "Điểm yêu cầu phải là số nguyên (không có phần thập phân).");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Chuyển đổi thành int
            return (int) diemDouble;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Điểm yêu cầu không hợp lệ. Vui lòng nhập số nguyên.");
            e.printStackTrace(); // In ra thông tin chi tiết về lỗi
            return -1; // Trả về giá trị không hợp lệ
        }
    }


    // Phương thức để lấy số lượng tối thiểu
    private int getSoLuongToiThieu() {
        String soLuongInput = jTextFieldsoLuongToiThieu.getText();
        if (soLuongInput.isEmpty()) {
            return 10; // Mặc định là 10 nếu không nhập
        }
        try {
            // Chuyển đổi đầu vào thành double
            double soLuongDouble = Double.parseDouble(soLuongInput);

            // Kiểm tra xem số lượng tối thiểu có lớn hơn 0 không
            if (soLuongDouble <= 0) {
                JOptionPane.showMessageDialog(null, "Số lượng tối thiểu phải lớn hơn 0.");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Kiểm tra xem phần thập phân có bằng 0 không
            if (soLuongDouble % 1 != 0) {
                JOptionPane.showMessageDialog(null, "Số lượng tối thiểu phải là số nguyên (không có phần thập phân).");
                return -1; // Trả về giá trị không hợp lệ
            }

            // Chuyển đổi thành int
            return (int) soLuongDouble;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số lượng tối thiểu không hợp lệ. Vui lòng nhập số nguyên lớn hơn 0.");
            return -1; // Trả về giá trị không hợp lệ
        }
    }

    // Phương thức để lấy loại khuyến mãi đã chọn
    private String getSelectedLoaiKhuyenMai() {
        if (jRadioButtonphanTram.isSelected()) {
            return "Phần trăm";
        } else if (jRadioButtonmonAn.isSelected()) {
            return "Món ăn";
        } else if (jRadioButtontien.isSelected()) {
            return "Tiền";
        } else if (jRadioButtonquaTang.isSelected()) {
            return "Quà tặng";
        }
        return null; // Nếu không có loại nào được chọn
    }

    // Phương thức để làm mới các trường nhập liệu
    private void resetFields() {
        jTextFieldmaKhuyenMai.setText("");
        jTextFieldtenKhuyenMai.setText("");
        jTextFieldmoTaKhuyenMai.setText("");
        jTextFieldgiaTriKhuyenMai.setText("");
        jTextFielddiemYeuCau.setText("");
        jTextFieldsoLuongToiThieu.setText("");
        jDateChooserngayBatDau.setDate(null);
        jDateChooserngayKetThuc.setDate(null);
        jRadioButtonphanTram.setSelected(false);
        jRadioButtonquaTang.setSelected(false);
        jRadioButtontien.setSelected(false);
        jRadioButtonmonAn.setSelected(false);
        
        // Bỏ chọn dòng trong bảng
        jTabledanhSachKhuyenMai.clearSelection();
    }

    
    private void jTextFieldtenKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldtenKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldtenKhuyenMaiActionPerformed

    private void jComboBoxtenMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtenMonAnActionPerformed
        // TODO add your handling code here:

      

    }//GEN-LAST:event_jComboBoxtenMonAnActionPerformed

    private void jButtonReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReloadActionPerformed
        // TODO add your handling code here:                            
        try {
            // Cập nhật trạng thái khuyến mãi
            khuyenMaiDAO.capNhatTrangThaiKhuyenMaiDuy();

            // Hiển thị lại danh sách khuyến mãi
            hienThiDanhSachKhuyenMai();

            JOptionPane.showMessageDialog(this, "Vui lòng chờ giây lát !");

            // Reload danh sách áp dụng cho tất cả các khuyến mãi
            reloadKhuyenMaiKhachHang();

            // Tạo model cho bảng
            DefaultTableModel model = (DefaultTableModel) jTabledanhSachApDung.getModel();
            model.setRowCount(0); // Xóa các dòng hiện tại

            // Gọi hàm để tạo hiệu ứng nhấp nháy cho bảng
            flashTable(jTabledanhSachKhuyenMai);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonReloadActionPerformed

    private void jComboBoxtenDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxtenDanhMucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxtenDanhMucActionPerformed

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
                //themKhachHangButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Khách hàng chưa có tài khoản!!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                jTextFieldkhachHang.setText("");
                //themKhachHangButton.setEnabled(true);
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

    private void jButtonthemKhuyenMaiKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonthemKhuyenMaiKhachHangActionPerformed
        // TODO add your handling code here:


    
   // Kiểm tra xem checkbox đã được chọn chưa
    if (!jCheckBoxsoDienThoai.isSelected()) {
        JOptionPane.showMessageDialog(this, "Bạn chưa xác nhận bằng cách chọn vào ô kê bên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Lấy số điện thoại từ jTextFieldsoDienThoai
    String soDienThoai = jTextFieldsoDienThoai.getText().trim();
    
    // Kiểm tra số điện thoại
    if (!soDienThoai.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số điện thoại (10 số)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
    try {
        KhachHang kh = khachHang_DAO.timKhachHangDuy(soDienThoai);
        if (kh != null) {
            // Cập nhật điểm tích lũy
            int diemTichLuy = kh.getDiemTichLuy() != null ? kh.getDiemTichLuy() + 999999 : 999999; // Kiểm tra null
            khachHang_DAO.capNhatDiemTichLuy(kh.getMaKhachHang(), diemTichLuy);

            // Gọi hàm thêm khuyến mãi
            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setTrangThai("Active"); // Thiết lập trạng thái
            khuyenMai.setQuanLy(new QuanLy("QL0001")); // Thiết lập mã quản lý mặc định

            // Tạo mã khuyến mãi tự động
            String maKhuyenMai = khuyenMaiDAO.generateMaKhuyenMaiDuy();
            if (maKhuyenMai == null || maKhuyenMai.isEmpty()) {
                showError("Lỗi hệ thống: Không thể tạo mã khuyến mãi.");
                return;
            }
            khuyenMai.setMaKhuyenMai(maKhuyenMai); // Gán mã khuyến mãi

            // Thiết lập các thông tin khác cho khuyến mãi
            if (setKhuyenMaiDetails(khuyenMai, true)) {
                // Thiết lập giá trị điểm yêu cầu
                khuyenMai.setDiemYeuCau(999999); // Gán giá trị điểm yêu cầu

                // Thêm khuyến mãi vào bảng KhuyenMai
                khuyenMaiDAO.themKhuyenMaiDuy(khuyenMai);

                // Thêm khuyến mãi vào bảng KhuyenMaiKhachHang
                khuyenMaiKhachHangDAO.themKhuyenMaiKhachHangDuy(kh.getMaKhachHang(), khuyenMai.getMaKhuyenMai());

                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công cho khách hàng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Khách hàng chưa có tài khoản!!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // In ra chi tiết lỗi
        JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu: " + ex.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
    }




    }//GEN-LAST:event_jButtonthemKhuyenMaiKhachHangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonReload;
    private javax.swing.JButton jButtondungKhuyenMai;
    private javax.swing.JButton jButtontaoMoi;
    private javax.swing.JButton jButtonthemKhuyenMaiKhachHang;
    private javax.swing.JButton jButtonxoaTrang;
    private javax.swing.JCheckBox jCheckBoxsoDienThoai;
    private javax.swing.JComboBox<String> jComboBoxtenDanhMuc;
    private javax.swing.JComboBox<String> jComboBoxtenMonAn;
    private com.toedter.calendar.JDateChooser jDateChooserngayBatDau;
    private com.toedter.calendar.JDateChooser jDateChooserngayKetThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabeldiemYeuCau;
    private javax.swing.JLabel jLabelgiamGia;
    private javax.swing.JLabel jLabelkhachHang1;
    private javax.swing.JLabel jLabelloaiKhuyenMai;
    private javax.swing.JLabel jLabelmaKhuyenMai;
    private javax.swing.JLabel jLabelmoTa;
    private javax.swing.JLabel jLabelngayBatDau;
    private javax.swing.JLabel jLabelngayKetThuc;
    private javax.swing.JLabel jLabelsoDienThoai;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButtonmonAn;
    private javax.swing.JRadioButton jRadioButtonphanTram;
    private javax.swing.JRadioButton jRadioButtonquaTang;
    private javax.swing.JRadioButton jRadioButtontien;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTabledanhSachApDung;
    private javax.swing.JTable jTabledanhSachKhuyenMai;
    private javax.swing.JTextField jTextFielddiemYeuCau;
    private javax.swing.JTextField jTextFieldgiaTriKhuyenMai;
    private javax.swing.JTextField jTextFieldkhachHang;
    private javax.swing.JTextField jTextFieldmaKhuyenMai;
    private javax.swing.JTextField jTextFieldmoTaKhuyenMai;
    private javax.swing.JTextField jTextFieldsoDienThoai;
    private javax.swing.JTextField jTextFieldsoLuongToiThieu;
    private javax.swing.JTextField jTextFieldtenKhuyenMai;
    // End of variables declaration//GEN-END:variables

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
    
}
