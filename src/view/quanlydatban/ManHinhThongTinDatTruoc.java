/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.quanlydatban;

import com.toedter.calendar.JDateChooser;
import converter.BoDinhDangTien;
import converter.ConvertoLocalDate;
import converter.DinhDangTien;
import dao.BanAn_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.DanhMucMonAn_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.LoaiBan_DAO;
import dao.MonAnKhuyenMai_DAO;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.QuanLy_DAO;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import modal.BanAn;
import modal.ChiTietHoaDon;
import modal.DanhMucMonAn;
import modal.HoaDon;
import modal.KhachHang;
import modal.LoaiBan;
import modal.MonAn;
import modal.MonAnKhuyenMai;
import modal.NhanVien;
import modal.QuanLy;
import modal.TaiKhoan;

/**
 *
 * @author ADMIN
 */
public class ManHinhThongTinDatTruoc extends JDialog {
    DanhMucMonAn_DAO danhMuc_DAO;
    MonAn_DAO monAn_DAO;
    BanAn_DAO banAn_DAO;
    MonAnKhuyenMai_DAO monAnKhuyenMai_dao;
    LoaiBan_DAO loaiBan_DAO;
    JDateChooser chonNgayNhan;
    JSpinner timeSpinner;
    private TaiKhoan taiKhoan;
    DefaultTableModel model;

    public ManHinhThongTinDatTruoc(TaiKhoan taiKhoan) throws SQLException {
        
        initComponents();
        this.taiKhoan = taiKhoan;
        
        chonNgay();
        layDanhSachDanhMuc();
        layThongTinBan();
        
        suaButton.setEnabled(false);
    }
    
    public void chonNgay() {
        // Tạo JDateChooser
        chonNgayNhan = new JDateChooser();
        chonNgayNhan.setDateFormatString("dd-MM-yyyy");
        chonNgayNhan.getJCalendar().setLocale(new Locale("vi", "VN"));
        chonNgayNhan.getDateEditor().setEnabled(false);
        chonNgayNhan.setFont(new Font("Arial", Font.BOLD, 14));
        chonNgayNhan.setBounds(0, 0, 150, 30);
        panelChonNgay.setLayout(null);
        panelChonNgay.add(chonNgayNhan);

        // Giới hạn ngày được chọn
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime(); // Lấy ngày hiện tại

        calendar.add(Calendar.DAY_OF_YEAR, 1); // Thêm 1 ngày cho ngày mai
        Date tomorrow = calendar.getTime();

        // Đặt giới hạn cho JDateChooser
        chonNgayNhan.setMinSelectableDate(today);
        chonNgayNhan.setMaxSelectableDate(tomorrow);

        // Tạo JSpinner để chọn giờ
        SpinnerDateModel modelChonGio = new SpinnerDateModel();
        timeSpinner = new JSpinner(modelChonGio);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // Đặt giá trị ban đầu cho JSpinner là giờ hiện tại
        timeSpinner.setBounds(170, 0, 80, 30);
        panelChonNgay.add(timeSpinner);

        panelChonNgay.revalidate();
        panelChonNgay.repaint();
    }
    
    public void layDanhSachDanhMuc() throws SQLException{
        danhMuc_DAO = new DanhMucMonAn_DAO();
        List<DanhMucMonAn> danhMuc = danhMuc_DAO.layTatCaDanhMuc();
        for(DanhMucMonAn dm : danhMuc){
            comboBoxDanhMuc.addItem(dm.getTenDanhMuc());
        }
    }
    
    public void layMonAnTheoTenDanhMuc(String tenDanhMuc) throws SQLException{
        comboBoxMonAn.removeAllItems();
        
        monAn_DAO = new MonAn_DAO();
        List<MonAn> dsMonAn = monAn_DAO.layMonAnTheoTenDanhMuc1(tenDanhMuc);
        for(MonAn mon : dsMonAn){
            comboBoxMonAn.addItem(mon.getTenMonAn());
        }
    }
    
    public void layThongTinBan() throws SQLException{
        loaiBan_DAO = new LoaiBan_DAO();
        List<LoaiBan> dsLoaiBan = loaiBan_DAO.layTatCaTenLoaiBan();
        for(LoaiBan lb : dsLoaiBan){
            comboBoxLoaiBan.addItem(lb.getTenLoaiBan());
        }
        
        banAn_DAO = new BanAn_DAO();
        String[] dsViTri = banAn_DAO.layTatCaViTri();
        for(String viTri : dsViTri){
            comBoBoxViTri.addItem(viTri);
        }
        
        String[] dsTang = banAn_DAO.layTatCaTang();
        for(String tang : dsTang){
            comBoBoxTang.addItem(tang);
        }
    }
    
    // Phương thức để lọc bàn theo các tiêu chí
    public void locBan(){
        String loaiBan = comboBoxLoaiBan.getSelectedItem().toString();
        String viTri = comBoBoxViTri.getSelectedItem().toString();
        String tang = comBoBoxTang.getSelectedItem().toString();
        
        banAn_DAO = new BanAn_DAO();
        
        List<BanAn> dsBan = null;
        try {
            dsBan = banAn_DAO.layBanTheoTieuChi(loaiBan, tang, viTri);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhThongTinDatTruoc.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Lọc ra các bàn có trạng thái "trống"
        List<BanAn> dsBanTrong = new ArrayList<>();
        for (BanAn ban : dsBan) {
            if (ban.getTrangThaiBanAn().equals("Trống")) {
                dsBanTrong.add(ban);
            }
        }

        comBoBoxMaBan.removeAllItems();
        for (BanAn ban : dsBanTrong) {
            comBoBoxMaBan.addItem(ban.getMaBan());
        }
    }
    
    public void capNhatThanhTien(){
        monAn_DAO = new MonAn_DAO();
        monAnKhuyenMai_dao = new MonAnKhuyenMai_DAO();
        
        Object selectedItem = comboBoxMonAn.getSelectedItem();
        if (selectedItem == null) {
            giaText.setText("");
            return;
        }

        String tenMonAn = selectedItem.toString();

        MonAn mon = null;
        try {
            mon = monAn_DAO.layThongTinMonAn1(tenMonAn);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi");
        }

        if (mon != null) {
            int soLuong = (Integer) spinnerSoLuongMon.getValue();
            double phantramKhuyenMai = 0;
            double thanhTien = soLuong * mon.getGiaMonAn();
            MonAnKhuyenMai monkm = monAnKhuyenMai_dao.layMonAnKhuyenMaiBangMa(mon.getMaMonAn());
            if(monkm==null){
                phantramKhuyenMai = 0;
            }else if((monkm != null) && (soLuong >= monkm.getSoLuongToiThieu())){
                phantramKhuyenMai = monkm.getPhanTramGiamGia();
            }
            thanhTien -= thanhTien*phantramKhuyenMai/100;
            
            String thanh = DinhDangTien.chuyenSangVND(thanhTien);
            giaText.setText(thanh);
        } else {
            giaText.setText("Món ăn không tồn tại");
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

        panelMain = new javax.swing.JPanel();
        panelThongTin = new javax.swing.JPanel();
        khachHangLabel = new javax.swing.JLabel();
        khachHangText = new javax.swing.JTextField();
        themKhachHangButton = new javax.swing.JButton();
        soDienThoaiText = new javax.swing.JTextField();
        soDienThoaiLabel = new javax.swing.JLabel();
        ghiChuLabel = new javax.swing.JLabel();
        soLuongKhachLabel = new javax.swing.JLabel();
        spinnerSoLuongKhach = new javax.swing.JSpinner();
        panelDatMon = new javax.swing.JPanel();
        danhMucLabel = new javax.swing.JLabel();
        comboBoxDanhMuc = new javax.swing.JComboBox<>();
        monAnLabel = new javax.swing.JLabel();
        comboBoxMonAn = new javax.swing.JComboBox<>();
        soLuongLabel = new javax.swing.JLabel();
        spinnerSoLuongMon = new javax.swing.JSpinner();
        giaLabel = new javax.swing.JLabel();
        giaText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        ghiChuText = new javax.swing.JTextArea();
        panelThongTinBan = new javax.swing.JPanel();
        loaiBanLabel = new javax.swing.JLabel();
        comboBoxLoaiBan = new javax.swing.JComboBox<>();
        viTriLabel = new javax.swing.JLabel();
        comBoBoxViTri = new javax.swing.JComboBox<>();
        tangLabel = new javax.swing.JLabel();
        comBoBoxTang = new javax.swing.JComboBox<>();
        comBoBoxMaBan = new javax.swing.JComboBox<>();
        banLabel = new javax.swing.JLabel();
        ngayGioNhanBan = new javax.swing.JLabel();
        panelChonNgay = new javax.swing.JPanel();
        panelThongTinDatMon = new javax.swing.JPanel();
        panelNorth = new javax.swing.JPanel();
        labelTittle = new javax.swing.JLabel();
        scrollPanelDanhSachDatMon = new javax.swing.JScrollPane();
        tableDanhSach = new javax.swing.JTable();
        chucNangPanel = new javax.swing.JPanel();
        suaPanel = new javax.swing.JPanel();
        textmaBanCanSua = new javax.swing.JTextField();
        textsdtCanSua = new javax.swing.JTextField();
        suaButton = new javax.swing.JButton();
        labelmaBanCanSua = new javax.swing.JLabel();
        labelsdtCanSua = new javax.swing.JLabel();
        chucNangLabel = new javax.swing.JPanel();
        lamMoiButton = new javax.swing.JButton();
        huyButton = new javax.swing.JButton();
        DatButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        panelMain.setMinimumSize(new java.awt.Dimension(100, 106));
        panelMain.setPreferredSize(new java.awt.Dimension(900, 500));
        panelMain.setLayout(new java.awt.BorderLayout());

        panelThongTin.setBackground(new java.awt.Color(255, 255, 255));
        panelThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin Đặt bàn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 16))); // NOI18N
        panelThongTin.setPreferredSize(new java.awt.Dimension(500, 390));

        khachHangLabel.setText("Tên Khách hàng:");

        themKhachHangButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ThemKhachHang.png"))); // NOI18N
        themKhachHangButton.setEnabled(false);
        themKhachHangButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themKhachHangButtonActionPerformed(evt);
            }
        });

        soDienThoaiText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soDienThoaiTextActionPerformed(evt);
            }
        });

        soDienThoaiLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        soDienThoaiLabel.setText("Số điện thoại:");

        ghiChuLabel.setText("Ghi Chú:");

        soLuongKhachLabel.setText("Số lượng khách:");

        spinnerSoLuongKhach.setModel(new javax.swing.SpinnerNumberModel(1, 1, 15, 1));
        spinnerSoLuongKhach.setEditor(new javax.swing.JSpinner.NumberEditor(spinnerSoLuongKhach, ""));

        panelDatMon.setBackground(new java.awt.Color(255, 255, 255));
        panelDatMon.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đặt món trước", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        danhMucLabel.setText("Danh mục:");

        comboBoxDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxDanhMucActionPerformed(evt);
            }
        });

        monAnLabel.setText("Món ăn:");

        comboBoxMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMonAnActionPerformed(evt);
            }
        });

        soLuongLabel.setText("Số lượng:");

        spinnerSoLuongMon.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spinnerSoLuongMon.setEditor(new javax.swing.JSpinner.NumberEditor(spinnerSoLuongMon, ""));
        spinnerSoLuongMon.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSoLuongMonStateChanged(evt);
            }
        });

        giaLabel.setText("Giá:");

        giaText.setEditable(false);
        giaText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout panelDatMonLayout = new javax.swing.GroupLayout(panelDatMon);
        panelDatMon.setLayout(panelDatMonLayout);
        panelDatMonLayout.setHorizontalGroup(
            panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatMonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatMonLayout.createSequentialGroup()
                        .addComponent(danhMucLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelDatMonLayout.createSequentialGroup()
                        .addComponent(monAnLabel)
                        .addGap(20, 20, 20)))
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxDanhMuc, 0, 160, Short.MAX_VALUE)
                    .addComponent(comboBoxMonAn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(giaLabel)
                    .addComponent(soLuongLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(giaText)
                    .addComponent(spinnerSoLuongMon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelDatMonLayout.setVerticalGroup(
            panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatMonLayout.createSequentialGroup()
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(danhMucLabel)
                    .addComponent(comboBoxDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monAnLabel)
                    .addComponent(comboBoxMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatMonLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerSoLuongMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soLuongLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(giaLabel)
                    .addComponent(giaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        ghiChuText.setColumns(20);
        ghiChuText.setRows(5);
        jScrollPane2.setViewportView(ghiChuText);

        panelThongTinBan.setBackground(new java.awt.Color(255, 255, 255));
        panelThongTinBan.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin Bàn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        loaiBanLabel.setText("Loại bàn:");

        comboBoxLoaiBan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Loại bàn--" }));
        comboBoxLoaiBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxLoaiBanActionPerformed(evt);
            }
        });

        viTriLabel.setText("Vị trí:");

        comBoBoxViTri.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Vị trí--" }));
        comBoBoxViTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comBoBoxViTriActionPerformed(evt);
            }
        });

        tangLabel.setText("Tầng:");

        comBoBoxTang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Tầng--" }));
        comBoBoxTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comBoBoxTangActionPerformed(evt);
            }
        });

        comBoBoxMaBan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn bàn--" }));

        banLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        banLabel.setText("Bàn:");

        javax.swing.GroupLayout panelThongTinBanLayout = new javax.swing.GroupLayout(panelThongTinBan);
        panelThongTinBan.setLayout(panelThongTinBanLayout);
        panelThongTinBanLayout.setHorizontalGroup(
            panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinBanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viTriLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tangLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comBoBoxViTri, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comBoBoxTang, 0, 120, Short.MAX_VALUE))
                .addGap(52, 52, 52)
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loaiBanLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(banLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comBoBoxMaBan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxLoaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );
        panelThongTinBanLayout.setVerticalGroup(
            panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinBanLayout.createSequentialGroup()
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comBoBoxViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viTriLabel)
                    .addComponent(comboBoxLoaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loaiBanLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelThongTinBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tangLabel)
                    .addComponent(comBoBoxTang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comBoBoxMaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(banLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ngayGioNhanBan.setText("Ngày nhận:");

        panelChonNgay.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelThongTinLayout = new javax.swing.GroupLayout(panelThongTin);
        panelThongTin.setLayout(panelThongTinLayout);
        panelThongTinLayout.setHorizontalGroup(
            panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(soLuongKhachLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ngayGioNhanBan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerSoLuongKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelChonNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panelThongTinLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(ghiChuLabel)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane2))
                        .addComponent(panelDatMon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelThongTinBan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(khachHangText, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(khachHangLabel)
                            .addGroup(panelThongTinLayout.createSequentialGroup()
                                .addComponent(soDienThoaiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(soDienThoaiText, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themKhachHangButton)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        panelThongTinLayout.setVerticalGroup(
            panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongTinLayout.createSequentialGroup()
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(soDienThoaiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(soDienThoaiText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(khachHangLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(khachHangText))
                        .addGap(18, 18, 18))
                    .addGroup(panelThongTinLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(themKhachHangButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soLuongKhachLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spinnerSoLuongKhach))
                .addGap(18, 18, 18)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelChonNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ngayGioNhanBan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panelThongTinBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelDatMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(panelThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ghiChuLabel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(571, 571, 571))
        );

        panelMain.add(panelThongTin, java.awt.BorderLayout.LINE_START);

        panelThongTinDatMon.setBackground(new java.awt.Color(255, 255, 255));
        panelThongTinDatMon.setPreferredSize(new java.awt.Dimension(400, 503));
        panelThongTinDatMon.setLayout(new java.awt.BorderLayout());

        panelNorth.setBackground(new java.awt.Color(255, 255, 255));
        panelNorth.setPreferredSize(new java.awt.Dimension(500, 50));

        labelTittle.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labelTittle.setForeground(new java.awt.Color(0, 204, 204));
        labelTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTittle.setText("Danh sách Đặt món");
        labelTittle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelNorthLayout = new javax.swing.GroupLayout(panelNorth);
        panelNorth.setLayout(panelNorthLayout);
        panelNorthLayout.setHorizontalGroup(
            panelNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTittle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );
        panelNorthLayout.setVerticalGroup(
            panelNorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTittle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panelThongTinDatMon.add(panelNorth, java.awt.BorderLayout.PAGE_START);

        scrollPanelDanhSachDatMon.setBackground(new java.awt.Color(255, 255, 255));
        scrollPanelDanhSachDatMon.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelDanhSachDatMon.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelDanhSachDatMon.setPreferredSize(new java.awt.Dimension(500, 600));

        tableDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã món ăn", "Món Ăn", "Số lượng", "Đơn giá", "Khuyến mãi", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDanhSach.setRowHeight(40);
        tableDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDanhSachMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableDanhSachMouseReleased(evt);
            }
        });
        scrollPanelDanhSachDatMon.setViewportView(tableDanhSach);
        if (tableDanhSach.getColumnModel().getColumnCount() > 0) {
            tableDanhSach.getColumnModel().getColumn(0).setMinWidth(40);
            tableDanhSach.getColumnModel().getColumn(0).setMaxWidth(40);
            tableDanhSach.getColumnModel().getColumn(1).setMinWidth(100);
            tableDanhSach.getColumnModel().getColumn(1).setMaxWidth(100);
            tableDanhSach.getColumnModel().getColumn(3).setMinWidth(100);
            tableDanhSach.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        panelThongTinDatMon.add(scrollPanelDanhSachDatMon, java.awt.BorderLayout.CENTER);

        chucNangPanel.setBackground(new java.awt.Color(255, 255, 255));
        chucNangPanel.setLayout(new java.awt.BorderLayout());

        suaPanel.setBackground(new java.awt.Color(255, 255, 255));
        suaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sửa thông tin đặt trước?"));
        suaPanel.setPreferredSize(new java.awt.Dimension(303, 110));

        textmaBanCanSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textmaBanCanSuaActionPerformed(evt);
            }
        });

        textsdtCanSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textsdtCanSuaActionPerformed(evt);
            }
        });

        suaButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        suaButton.setText("Sửa");
        suaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaButtonActionPerformed(evt);
            }
        });

        labelmaBanCanSua.setText("Nhập mã bàn:");

        labelsdtCanSua.setText("Nhập số điện thoại:");

        javax.swing.GroupLayout suaPanelLayout = new javax.swing.GroupLayout(suaPanel);
        suaPanel.setLayout(suaPanelLayout);
        suaPanelLayout.setHorizontalGroup(
            suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelmaBanCanSua)
                    .addComponent(labelsdtCanSua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textsdtCanSua, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(textmaBanCanSua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        suaPanelLayout.setVerticalGroup(
            suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(suaButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, suaPanelLayout.createSequentialGroup()
                        .addGroup(suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textmaBanCanSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelmaBanCanSua))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(suaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textsdtCanSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelsdtCanSua))))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        chucNangPanel.add(suaPanel, java.awt.BorderLayout.LINE_END);

        chucNangLabel.setBackground(new java.awt.Color(255, 255, 255));
        chucNangLabel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        chucNangLabel.setPreferredSize(new java.awt.Dimension(500, 110));

        lamMoiButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lamMoiButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        lamMoiButton.setText("Làm mới");
        lamMoiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lamMoiButtonActionPerformed(evt);
            }
        });

        huyButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        huyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/xoanew.png"))); // NOI18N
        huyButton.setText("Hủy đặt");
        huyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                huyButtonActionPerformed(evt);
            }
        });

        DatButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/checkout.png"))); // NOI18N
        DatButton.setText("Đặt");
        DatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chucNangLabelLayout = new javax.swing.GroupLayout(chucNangLabel);
        chucNangLabel.setLayout(chucNangLabelLayout);
        chucNangLabelLayout.setHorizontalGroup(
            chucNangLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chucNangLabelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lamMoiButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(huyButton)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        chucNangLabelLayout.setVerticalGroup(
            chucNangLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chucNangLabelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chucNangLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lamMoiButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                    .addComponent(huyButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        chucNangPanel.add(chucNangLabel, java.awt.BorderLayout.CENTER);

        panelThongTinDatMon.add(chucNangPanel, java.awt.BorderLayout.PAGE_END);

        panelMain.add(panelThongTinDatMon, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void themKhachHangButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themKhachHangButtonActionPerformed
        String tenKhachHang = khachHangText.getText();
        String soDT = soDienThoaiText.getText();

        if (tenKhachHang.isEmpty() || soDT.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại");
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
            Logger.getLogger(ManHinhThongTinBan.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra!");
        }
    }//GEN-LAST:event_themKhachHangButtonActionPerformed

    private void soDienThoaiTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soDienThoaiTextActionPerformed
        String soDienThoai = soDienThoaiText.getText().trim();
        if (soDienThoai.length() != 10 || !soDienThoai.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số điện thoại (10 số)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            khachHangText.setText("");
            return; 
        }

        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        try {
            KhachHang kh = khachHang_DAO.timKhachHangTheoSDT(soDienThoai);
            if (kh != null) {
                khachHangText.setText(kh.getTenKhachHang());
                themKhachHangButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Khách hàng chưa có tài khoản!!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                khachHangText.setText("");
                themKhachHangButton.setEnabled(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_soDienThoaiTextActionPerformed

    private void comboBoxDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxDanhMucActionPerformed
        String danhMucChon = comboBoxDanhMuc.getSelectedItem().toString();
        try {
            layMonAnTheoTenDanhMuc(danhMucChon);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhThongTinDatTruoc.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }//GEN-LAST:event_comboBoxDanhMucActionPerformed

    private void comBoBoxViTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comBoBoxViTriActionPerformed
        locBan();
    }//GEN-LAST:event_comBoBoxViTriActionPerformed

    private void comboBoxLoaiBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLoaiBanActionPerformed
        locBan();
    }//GEN-LAST:event_comboBoxLoaiBanActionPerformed

    private void comBoBoxTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comBoBoxTangActionPerformed
        locBan();
    }//GEN-LAST:event_comBoBoxTangActionPerformed

    // Hàm cập nhật dữ liệu lên bảng
    public void duaDuLieuLenBang() {
        monAn_DAO = new MonAn_DAO();
        monAnKhuyenMai_dao = new MonAnKhuyenMai_DAO();

        Object selectedItem = comboBoxMonAn.getSelectedItem();
        if (selectedItem == null) return;

        String tenMonAn = selectedItem.toString();
        MonAn mon = null;
        try {
            mon = monAn_DAO.layThongTinMonAn1(tenMonAn);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin món ăn");
            return;
        }

        int soLuong = (Integer) spinnerSoLuongMon.getValue();
        model = (DefaultTableModel) tableDanhSach.getModel();

        if (soLuong > 0) {
            boolean daCoTrongBang = false;
            
            // Duyệt qua từng dòng của bảng để kiểm tra món ăn đã có hay chưa
            for (int i = 0; i < model.getRowCount(); i++) {
                Object valueAt = model.getValueAt(i, 2);  // Lấy giá trị ở cột Tên Món Ăn

                if (valueAt != null) {
                    String tenMonAnTrongBang = valueAt.toString();

                    // Nếu món ăn đã có trong bảng, cập nhật số lượng và thành tiền
                    if (tenMonAnTrongBang.equals(tenMonAn)) {
                        model.setValueAt(mon.getMaMonAn(), i, 1);
                        model.setValueAt(soLuong, i, 3); // Cập nhật số lượng
                        double donGia = mon.getGiaMonAn();
                        double thanhTien = donGia * soLuong;
                        double phantramKhuyenMai = 0;
                        MonAnKhuyenMai monkm = monAnKhuyenMai_dao.layMonAnKhuyenMaiBangMa(mon.getMaMonAn());
                        if(monkm==null){
                            phantramKhuyenMai = 0;
                        }else if((monkm!=null) && soLuong >= monkm.getSoLuongToiThieu()){
                            phantramKhuyenMai = monkm.getPhanTramGiamGia();
                        }
                        thanhTien -= thanhTien*phantramKhuyenMai/100;
                        model.setValueAt(DinhDangTien.chuyenSangVND(donGia), i, 4); // Cập nhật đơn giá với định dạng
                        model.setValueAt(phantramKhuyenMai + "%", i, 5);
                        model.setValueAt(DinhDangTien.chuyenSangVND(thanhTien), i, 6); // Cập nhật thành tiền với định dạng
                        daCoTrongBang = true;
                        break;
                    }
                }
            }
            
            // Nếu món ăn chưa có trong bảng, thêm mới
            if (!daCoTrongBang) {
                double donGia = mon.getGiaMonAn();
                double thanhTien = donGia * soLuong; 
                double phantramKhuyenMai = 0;
                MonAnKhuyenMai monkm = monAnKhuyenMai_dao.layMonAnKhuyenMaiBangMa(mon.getMaMonAn());
                if(monkm==null){
                    phantramKhuyenMai = 0;
                }else if((monkm != null) && (soLuong >= monkm.getSoLuongToiThieu())){
                    phantramKhuyenMai = monkm.getPhanTramGiamGia();
                }
                thanhTien -= thanhTien*phantramKhuyenMai/100;
                model.addRow(new Object[]{
                    model.getRowCount() + 1, mon.getMaMonAn(),mon.getTenMonAn(), soLuong, DinhDangTien.chuyenSangVND(donGia),phantramKhuyenMai + "%", DinhDangTien.chuyenSangVND(thanhTien)
                });
            }

        } else {
            // Nếu số lượng bằng 0, tìm và xóa món ăn khỏi bảng
            for (int i = 0; i < model.getRowCount(); i++) {
                Object valueAt = model.getValueAt(i, 2);  // Lấy giá trị ở cột Tên Món Ăn

                if (valueAt != null) {
                    String tenMonAnTrongBang = valueAt.toString();

                    // Nếu tìm thấy món ăn trong bảng, xóa dòng
                    if (tenMonAnTrongBang.equals(tenMonAn)) {
                        model.removeRow(i);
                        capNhatSTTSauKhiXoa(model); // Cập nhật lại số thứ tự
                        break;
                    }
                }
            }
        }
    }

    // Hàm cập nhật lại số thứ tự (STT) sau khi xóa một mục
    private void capNhatSTTSauKhiXoa(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // Cột 0 là cột STT
        }
    }

    private void spinnerSoLuongMonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongMonStateChanged
        capNhatThanhTien();
        duaDuLieuLenBang();
    }//GEN-LAST:event_spinnerSoLuongMonStateChanged

    private void comboBoxMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMonAnActionPerformed
        Object selectedItem = comboBoxMonAn.getSelectedItem();
        if (selectedItem == null) return;

        String tenMonAn = selectedItem.toString();
        model = (DefaultTableModel) tableDanhSach.getModel();
        boolean daCoTrongBang = false;

        // Duyệt qua từng dòng của bảng để kiểm tra món ăn đã có hay chưa
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valueAt = model.getValueAt(i, 2);  // Lấy giá trị ở cột Tên Món Ăn

            if (valueAt != null) {
                String tenMonAnTrongBang = valueAt.toString();

                // Nếu món ăn đã có trong bảng, cập nhật số lượng vào spinner
                if (tenMonAnTrongBang.equals(tenMonAn)) {
                    int soLuong = (Integer) model.getValueAt(i, 3); // Lấy số lượng từ bảng
                    spinnerSoLuongMon.setValue(soLuong); // Cập nhật số lượng vào spinner
                    daCoTrongBang = true;
                    break;
                }
            }
        }

        if (!daCoTrongBang) {
            spinnerSoLuongMon.setValue(0);
        }
    }//GEN-LAST:event_comboBoxMonAnActionPerformed

    private void tableDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDanhSachMouseClicked
        monAn_DAO = new MonAn_DAO();
       
        int row = tableDanhSach.getSelectedRow();
        if (row != -1) {
            model = (DefaultTableModel) tableDanhSach.getModel();
            String tenMonAn = model.getValueAt(row, 2).toString();
            
            MonAn mon = null;
            try {
                mon = monAn_DAO.layThongTinMonAn1(tenMonAn);
            } catch (SQLException ex) {
                Logger.getLogger(ManHinhThongTinDatTruoc.class.getName()).log(Level.SEVERE, null, ex);
            }

            String tenDanhMuc = mon.getDanhMucMonAn().getTenDanhMuc();
            comboBoxDanhMuc.setSelectedItem(tenDanhMuc);
            comboBoxMonAn.setSelectedItem(tenMonAn);

            int soLuong = (Integer) model.getValueAt(row, 3);
            spinnerSoLuongMon.setValue(soLuong);
        }
    }//GEN-LAST:event_tableDanhSachMouseClicked

    private void huyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_huyButtonActionPerformed
        String maBan = comBoBoxMaBan.getSelectedItem().toString();
        BanAn ban = null;
            try {
                ban = banAn_DAO.layThongTinBanAn(maBan);
            } catch (SQLException ex) {
                Logger.getLogger(ManHinhThongTinDatTruoc.class.getName()).log(Level.SEVERE, null, ex);
            }
        if(ban==null){
            JOptionPane.showMessageDialog(this, "Chưa có mã bàn cần hủy", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(!ban.getTrangThaiBanAn().equals("Đã đặt")){
            JOptionPane.showMessageDialog(this, "Bàn này chưa được đặt trước đó, hãy kiểm tra lại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy đặt bàn?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();

            String trangThaiMoi = "Đã hủy";
            try {
                hoaDon_DAO.capNhatTrangThaiHoaDonSauKhiHuy(trangThaiMoi, maBan);
                JOptionPane.showMessageDialog(this, "Đã hủy đặt bàn!", "Thành công!", JOptionPane.INFORMATION_MESSAGE);
                capNhatTrangThaiBanAn(maBan, "Trống");
                DatButton.setEnabled(true);
            } catch (SQLException ex) {
                Logger.getLogger(ManHinhThongTinBan.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra!");
            }
        }
    }//GEN-LAST:event_huyButtonActionPerformed

    //Lấy thông tin khách hàng thông qua số điện thoại để phục vụ cho việc 'đặt bàn' và lập hóa đơn (nếu có) hoặc ẩn danh
    public KhachHang layThongTinKhachHangSDT() {
        KhachHang_DAO khachHang_DAO = new KhachHang_DAO();
        
        String tenKhach = khachHangText.getText();
        String soDienThoai = soDienThoaiText.getText();
            if (tenKhach.isEmpty() || soDienThoai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return null;
            }
        try {
            KhachHang khach =  khachHang_DAO.timKhachHangTheoSDT(soDienThoai);
            if (khach != null) {
                return khach;
            }else{
                JOptionPane.showMessageDialog(this, "Khách hàng chưa tạo tài khoản!");
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public void capNhatTrangThaiBanAn(String maBan, String trangThai){
        banAn_DAO = new BanAn_DAO();
        try{
            banAn_DAO.capNhatTrangThaiBan(maBan, trangThai);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Cập nhật không thành công!");
        }
    }
    
    public boolean kiemTraDuLieu(){
        String tenKhachHang = khachHangText.getText();
        String soDienThoai = soDienThoaiText.getText();
        Date ngay = chonNgayNhan.getDate();
        String maBan = comBoBoxMaBan.getSelectedItem().toString();
        
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (tenKhachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (ngay == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (maBan.isEmpty() || maBan.equals("--Chọn bàn--")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void DatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatButtonActionPerformed
        // Lấy thông tin bàn ăn
        String maBan = comBoBoxMaBan.getSelectedItem().toString();
        BanAn ban;
        try {
            ban = banAn_DAO.layThongTinBanAn(maBan);
            if (!ban.getTrangThaiBanAn().equals("Trống")) {
                JOptionPane.showMessageDialog(this, "Bàn đã được đặt trước đó. Vui lòng chọn bàn khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể lấy thông tin bàn. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!kiemTraDuLieu() || 
            JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đặt bàn không?", "Xác nhận đặt?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

            HoaDon hd = new HoaDon();
            hd.setBanAn(new BanAn(maBan));
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
            hd.setGhiChu(ghiChuText.getText());
            
        try {
            hd.setSoLuongKhach((int) spinnerSoLuongKhach.getValue());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ");
            return;
        }

        LocalDateTime localDateTime = ConvertoLocalDate.convertToLocalDateTime(
            ConvertoLocalDate.convertToLocalDate(chonNgayNhan.getDate()), 
            ConvertoLocalDate.convertToLocalTime((Date) timeSpinner.getValue())
        );
        hd.setNgayGioNhanBan(localDateTime);

        BoDinhDangTien boDinhDangTien = new BoDinhDangTien();
        for (int i = 0; i < tableDanhSach.getRowCount(); i++) {
            String maMonAn = tableDanhSach.getValueAt(i, 1).toString();
            String tenMonAn = tableDanhSach.getValueAt(i, 2).toString();
            int soLuongMonAn = Integer.parseInt(tableDanhSach.getValueAt(i, 3).toString());
            double giaMonAn = boDinhDangTien.parsePrice(tableDanhSach.getValueAt(i, 4).toString());
            double phantramKhuyenMai = 0;
            double thanhTien = soLuongMonAn * giaMonAn;
            MonAnKhuyenMai monkm = monAnKhuyenMai_dao.layMonAnKhuyenMaiBangMa(maMonAn);
            if(monkm==null){
                phantramKhuyenMai = 0;
            }else if((monkm != null) && (soLuongMonAn >= monkm.getSoLuongToiThieu())){
                phantramKhuyenMai = monkm.getPhanTramGiamGia();
            }
            thanhTien -= thanhTien*phantramKhuyenMai/100;

            MonAn monAn = new MonAn();
            monAn.setMaMonAn(maMonAn);
            monAn.setTenMonAn(tenMonAn);
            monAn.setGiaMonAn(giaMonAn);

            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
            chiTiet.setMonAn(monAn);
            chiTiet.setSoLuongMonAn(soLuongMonAn);
            chiTiet.setThanhTien(thanhTien);

            hd.getChiTietHoaDons().add(chiTiet);
        }
        hd.setTongTien(hd.tinhTongTien());
        hd.setVAT(0.1 * hd.getTongTien());

        try {
            HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
            HoaDon newHoaDon = hoaDon_DAO.lapHoaDon1(hd, "Đang xử lý");
            if (newHoaDon != null) {
                ChiTietHoaDon_DAO ct_DAO = new ChiTietHoaDon_DAO();
                for (ChiTietHoaDon chiTiet : hd.getChiTietHoaDons()) {
                    chiTiet.setHoaDon(hoaDon_DAO.layHoaDon(maBan));
                    ct_DAO.themChiTietHoaDon(chiTiet);
                }
                JOptionPane.showMessageDialog(this, "Đặt bàn thành công!");
                capNhatTrangThaiBanAn(maBan, "Đã đặt");
            } 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đặt bàn không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_DatButtonActionPerformed

    private void layThongTinDatTruoc(HoaDon hd) throws SQLException{
        khachHangText.setText(hd.getKhachHang().getTenKhachHang());
        soDienThoaiText.setText(hd.getKhachHang().getSoDienThoai());
        soDienThoaiText.setEditable(false);
        khachHangText.setEditable(false);
        
        spinnerSoLuongKhach.setValue(hd.getSoLuongKhach());
        ghiChuText.setText(hd.getGhiChu());
        LocalDateTime ngayGioNhan = hd.getNgayGioNhanBan();
            // Lấy phần ngày và chuyển sang java.util.Date
            Date ngay = java.sql.Date.valueOf(ngayGioNhan.toLocalDate());
            chonNgayNhan.setDate(ngay);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, ngayGioNhan.getHour());
            calendar.set(Calendar.MINUTE, ngayGioNhan.getMinute());
            calendar.set(Calendar.SECOND, 0);
            timeSpinner.setValue(calendar.getTime());
  
        BanAn_DAO banAn_dao = new BanAn_DAO();
        BanAn ban = banAn_dao.layThongTinBanAn(hd.getBanAn().getMaBan());
        
        for (int i = 0; i < comBoBoxTang.getItemCount(); i++) {
            if (comBoBoxTang.getItemAt(i).equals(ban.getTang())) {
                comBoBoxTang.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < comBoBoxViTri.getItemCount(); i++) {
            if (comBoBoxViTri.getItemAt(i).equals(ban.getViTri())) {
                comBoBoxViTri.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < comboBoxLoaiBan.getItemCount(); i++) {
            if (comboBoxLoaiBan.getItemAt(i).equals(ban.getLoaiBan().getTenLoaiBan())) {
                comboBoxLoaiBan.setSelectedIndex(i);
                break;
            }
        }
        comBoBoxMaBan.addItem(ban.getMaBan());
        comBoBoxMaBan.setSelectedItem(ban.getMaBan());
    }
    
    private void layThongTinMonAnDatTruoc(HoaDon hd) throws SQLException{
        ChiTietHoaDon_DAO cthd_dao = new ChiTietHoaDon_DAO();
        monAnKhuyenMai_dao = new MonAnKhuyenMai_DAO();
        List<ChiTietHoaDon> ct = cthd_dao.getAllChiTietHoaDon(hd.getMaHoaDon());
        
        model = (DefaultTableModel) tableDanhSach.getModel();
        model.setRowCount(0);
        double phantramKhuyenMai = 0;
        
        for(ChiTietHoaDon chitiet : ct){
            MonAnKhuyenMai monkm = monAnKhuyenMai_dao.layMonAnKhuyenMaiBangMa(chitiet.getMonAn().getMaMonAn());
            if(monkm==null){
                phantramKhuyenMai = 0;
            }else if(chitiet.getSoLuongMonAn() >= monkm.getSoLuongToiThieu()) {
                phantramKhuyenMai = monkm.getPhanTramGiamGia();
            }
            MonAn mon = monAn_DAO.layThongTinMonAnTheoMa(chitiet.getMonAn().getMaMonAn());
            model.addRow(new Object[] {
                model.getRowCount() + 1, mon.getMaMonAn(), mon.getTenMonAn(), chitiet.getSoLuongMonAn(),
                DinhDangTien.chuyenSangVND(mon.getGiaMonAn()), phantramKhuyenMai + "%", DinhDangTien.chuyenSangVND(chitiet.getThanhTien())
            });
        }

        model.fireTableDataChanged();
    }
    
    private void timkiemBanCanSua(){
        String ma = textmaBanCanSua.getText();
        String sdt = textsdtCanSua.getText();
        HoaDon_DAO hoadon_dao = new HoaDon_DAO();
        HoaDon hd;
        
        // Kiểm tra điều kiện mã bàn
        if (ma.isEmpty() && sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập tiêu chí tìm kiếm", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(!ma.isEmpty() && !ma.matches("^B\\d{4}$")){
            JOptionPane.showMessageDialog(this, "Mã bàn không hợp lệ! Định dạng: BYXXX, (Y là tầng 1 - 2 - hoặc 3)(X là số)", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(!sdt.isEmpty() && !sdt.matches("\\d{10}")){
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Cần đủ 10 số.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            hd = hoadon_dao.layThongTinDatTruoc(ma, sdt);
            if(hd==null){
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin đặt trước!");
                return;
            }else{
                layThongTinDatTruoc(hd);
                layThongTinMonAnDatTruoc(hd);         
                DatButton.setEnabled(false);
                suaButton.setEnabled(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhThongTinDatTruoc.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi truy xuất dữ liệu: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void textmaBanCanSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textmaBanCanSuaActionPerformed
        timkiemBanCanSua();
    }//GEN-LAST:event_textmaBanCanSuaActionPerformed

    private void textsdtCanSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textsdtCanSuaActionPerformed
        timkiemBanCanSua();
    }//GEN-LAST:event_textsdtCanSuaActionPerformed

    private void tableDanhSachMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDanhSachMouseReleased
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemDelete = new JMenuItem("Xóa");
        popupMenu.add(menuItemDelete);
        model = (DefaultTableModel) tableDanhSach.getModel();
            menuItemDelete.addActionListener(e -> {
                int selectedRow = tableDanhSach.getSelectedRow();
                if (selectedRow >= 0) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if(selectedRow == i){
                            String mon = (String) model.getValueAt(i, 2);
                            model.removeRow(i);
                            capNhatSTTSauKhiXoa(model);
                            JOptionPane.showMessageDialog(null, "Đã xóa món ăn: " + mon);
                            break;
                        }
                    }
                }
            });

        // Thêm xử lý chuột phải vào sự kiện
            if (evt.isPopupTrigger()) { // Kiểm tra nếu là chuột phải
                int row = tableDanhSach.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    tableDanhSach.setRowSelectionInterval(row, row); // Chọn dòng
                    popupMenu.show(tableDanhSach, evt.getX(), evt.getY()); // Hiển thị menu
                }
            }
    }//GEN-LAST:event_tableDanhSachMouseReleased

    private void xoaTrang(){
        khachHangText.setText("");
        soDienThoaiText.setText("");
        soDienThoaiText.requestFocus();
        soDienThoaiText.setEditable(true);
        khachHangText.setEditable(true);
        spinnerSoLuongKhach.setValue(0);
        ghiChuText.setText("");
        spinnerSoLuongMon.setValue(0);
        comBoBoxTang.setSelectedIndex(0);
        comBoBoxViTri.setSelectedIndex(0);
        comboBoxLoaiBan.setSelectedIndex(0);
        textmaBanCanSua.setText("");
        textsdtCanSua.setText("");
            model = (DefaultTableModel) tableDanhSach.getModel();
            model.setRowCount(0);
        DatButton.setEnabled(true);
        suaButton.setEnabled(false);
    }
    
    private void lamMoiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lamMoiButtonActionPerformed
        xoaTrang();
    }//GEN-LAST:event_lamMoiButtonActionPerformed

    private void suaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaButtonActionPerformed
        String maBanCanSua = textmaBanCanSua.getText();
        if(maBanCanSua.isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã bàn cần sửa và nhấn Enter trước!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(!maBanCanSua.matches("^B\\d{4}$")){
            JOptionPane.showMessageDialog(this, "Mã bàn không hợp lệ! Định dạng: BYXXX, (Y là tầng 1 - 2 - hoặc 3)(X là số)", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int soluongKhachMoi = (int) spinnerSoLuongKhach.getValue();
        String ghiChuMoi = ghiChuText.getText();
        String maBanMoi = comBoBoxMaBan.getSelectedItem().toString();
          LocalDateTime ngayGioNhanMoi = ConvertoLocalDate.convertToLocalDateTime(
              ConvertoLocalDate.convertToLocalDate(chonNgayNhan.getDate()), 
              ConvertoLocalDate.convertToLocalTime((Date) timeSpinner.getValue())
          );
          
        if (ngayGioNhanMoi.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this, "Ngày giờ nhận bàn không hợp lệ! Ngày giờ phải bằng hoặc lớn hơn hiện tại.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        HoaDon_DAO hoadon_dao = new HoaDon_DAO();
        HoaDon hd = null;
        try {
            hd = hoadon_dao.layThongTinDatTruoc(textmaBanCanSua.getText(), textsdtCanSua.getText());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi!!!" + ex.getMessage());
        }
        
        ChiTietHoaDon_DAO ct_dao = new ChiTietHoaDon_DAO();
        
        try {
            if (hoadon_dao.capNhatHoaDon(soluongKhachMoi, ngayGioNhanMoi, ghiChuMoi, maBanMoi, hd.getMaHoaDon())) {
                capNhatTrangThaiBanAn(maBanMoi, "Đã đặt");
                if(!maBanMoi.equals(maBanCanSua)){
                    capNhatTrangThaiBanAn(maBanCanSua, "Trống");
                }
                
                BoDinhDangTien boDinhDangTien = new BoDinhDangTien();
                double tongTien = 0.0;
                
                // Lấy danh sách mã món ăn hiện tại trong bảng
                ArrayList<String> danhSachMaMonHienTai = new ArrayList<>();
                for (int i = 0; i < tableDanhSach.getRowCount(); i++) {
                    danhSachMaMonHienTai.add(tableDanhSach.getValueAt(i, 1).toString());
                }

                // Lấy danh sách món ăn hiện tại trong hóa đơn từ cơ sở dữ liệu
                List<ChiTietHoaDon> ds = ct_dao.layTatCaChiTiet(hd.getMaHoaDon());

                // Xóa món ăn không còn trong danh sách hiện tại
                for (ChiTietHoaDon chiTiet : ds) {
                    if (!danhSachMaMonHienTai.contains(chiTiet.getMonAn().getMaMonAn())) {
                        if (!ct_dao.xoaChiTietHoaDon(chiTiet.getHoaDon().getMaHoaDon(), chiTiet.getMonAn().getMaMonAn())) {
                            JOptionPane.showMessageDialog(this, "Xóa chi tiết hóa đơn không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                
                // Duyệt qua bảng chi tiết hóa đơn để thực hiện thêm, sửa, xóa món ăn
                for (int i = 0; i < tableDanhSach.getRowCount(); i++) {
                    String maMonAn = tableDanhSach.getValueAt(i, 1).toString();
                    String tenMonAn = tableDanhSach.getValueAt(i, 2).toString();
                    int soLuong = Integer.parseInt(tableDanhSach.getValueAt(i, 3).toString());
                    double thanhTien = boDinhDangTien.parsePrice(tableDanhSach.getValueAt(i, 6).toString());

                    if (ct_dao.kiemTraMonAn(hd.getMaHoaDon(), maMonAn)) {
                        if (!ct_dao.suaChiTietHoaDon(hd.getMaHoaDon(), maMonAn, soLuong, thanhTien)) {
                            JOptionPane.showMessageDialog(this, "Sửa chi tiết hóa đơn không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Thêm món ăn vào chi tiết hóa đơn
                        MonAn monAn = new MonAn();
                        monAn.setMaMonAn(maMonAn);
                        monAn.setTenMonAn(tenMonAn);

                        ChiTietHoaDon chiTietMoi = new ChiTietHoaDon();
                        chiTietMoi.setHoaDon(hd);
                        chiTietMoi.setMonAn(monAn);
                        chiTietMoi.setSoLuongMonAn(soLuong);
                        chiTietMoi.setThanhTien(thanhTien);

                        if (!ct_dao.themChiTietHoaDon(chiTietMoi)) {
                            JOptionPane.showMessageDialog(this, "Thêm chi tiết hóa đơn không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    tongTien += thanhTien;
                }
                System.out.println("Tổng" + tongTien);
                double vat = tongTien * 0.1;
                
                if(!hoadon_dao.capNhatTongTienHoaDon(hd.getMaHoaDon(), vat, tongTien)){
                     JOptionPane.showMessageDialog(this, "Không thể cập nhật thông tin Tổng tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin đặt bàn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật thông tin đặt bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật thông tin đặt bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }  
    }//GEN-LAST:event_suaButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DatButton;
    private javax.swing.JLabel banLabel;
    private javax.swing.JPanel chucNangLabel;
    private javax.swing.JPanel chucNangPanel;
    private javax.swing.JComboBox<String> comBoBoxMaBan;
    private javax.swing.JComboBox<String> comBoBoxTang;
    private javax.swing.JComboBox<String> comBoBoxViTri;
    private javax.swing.JComboBox<String> comboBoxDanhMuc;
    private javax.swing.JComboBox<String> comboBoxLoaiBan;
    private javax.swing.JComboBox<String> comboBoxMonAn;
    private javax.swing.JLabel danhMucLabel;
    private javax.swing.JLabel ghiChuLabel;
    private javax.swing.JTextArea ghiChuText;
    private javax.swing.JLabel giaLabel;
    private javax.swing.JTextField giaText;
    private javax.swing.JButton huyButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel khachHangLabel;
    private javax.swing.JTextField khachHangText;
    private javax.swing.JLabel labelTittle;
    private javax.swing.JLabel labelmaBanCanSua;
    private javax.swing.JLabel labelsdtCanSua;
    private javax.swing.JButton lamMoiButton;
    private javax.swing.JLabel loaiBanLabel;
    private javax.swing.JLabel monAnLabel;
    private javax.swing.JLabel ngayGioNhanBan;
    javax.swing.JPanel panelChonNgay;
    private javax.swing.JPanel panelDatMon;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelNorth;
    private javax.swing.JPanel panelThongTin;
    private javax.swing.JPanel panelThongTinBan;
    private javax.swing.JPanel panelThongTinDatMon;
    private javax.swing.JScrollPane scrollPanelDanhSachDatMon;
    private javax.swing.JLabel soDienThoaiLabel;
    private javax.swing.JTextField soDienThoaiText;
    private javax.swing.JLabel soLuongKhachLabel;
    private javax.swing.JLabel soLuongLabel;
    private javax.swing.JSpinner spinnerSoLuongKhach;
    private javax.swing.JSpinner spinnerSoLuongMon;
    private javax.swing.JButton suaButton;
    private javax.swing.JPanel suaPanel;
    private javax.swing.JTable tableDanhSach;
    private javax.swing.JLabel tangLabel;
    private javax.swing.JTextField textmaBanCanSua;
    private javax.swing.JTextField textsdtCanSua;
    private javax.swing.JButton themKhachHangButton;
    private javax.swing.JLabel viTriLabel;
    // End of variables declaration//GEN-END:variables
}
