/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.quanlydatban;

import Parttern.BanManager;
import dao.BanAn_DAO;
import dao.HoaDon_DAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import modal.BanAn;
import modal.HoaDon;
import modal.TaiKhoan;
import utils.RoundedButton;

/**
 *
 * @author ADMIN
 */
public class ManHinhDatBan extends javax.swing.JPanel {
    BanAn_DAO banAn_DAO = new BanAn_DAO();
    HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();
    private String[] tangHienTai = banAn_DAO.layTatCaTang();
    private String[] viTriBan = banAn_DAO.layTatCaViTri();
    private int tangNay = 0;
    private int viTriNay = 0;
    private int soLuongBanTrong = 0;
    private boolean banDaChon = false;
    private String banCanChuyen = null; // Bàn đang cần chuyển
    private String banDuocChon = null; // Bàn được chọn để chuyển đến
    private TaiKhoan taiKhoan;

    /**
     * Creates new form ManHinhQuanLyDatBan
     * @throws java.sql.SQLException
     */
    public ManHinhDatBan(TaiKhoan taiKhoan) throws SQLException {
        initComponents();
        this.taiKhoan = taiKhoan;
        taoPanelBan();
    }
    
   private void taoPanelBan() {
        soLuongBanTrong = 0;
        labelThongTin.setText(tangHienTai[tangNay] + " >> " + viTriBan[viTriNay]);
        panelDanhSachBan.removeAll();

        try {
            List<BanAn> danhSachBan = banAn_DAO.layBanTheoTangVaViTri(tangHienTai[tangNay], viTriBan[viTriNay]);
            layDanhSach(danhSachBan);
            labelSoLuongBanTrong.setText("Bàn trống " + soLuongBanTrong + "/" + danhSachBan.size());
            
             // Cập nhật panel danh sách bàn vào JScrollPane
            scrollPanelDanhSachBan.setViewportView(panelDanhSachBan);
            
            // Cập nhật lại giao diện sau khi thêm các panel
            scrollPanelDanhSachBan.revalidate();
            scrollPanelDanhSachBan.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong lúc tạo Panel!!!", "Lỗi", JOptionPane.ERROR_MESSAGE);         
        }
    }
    
        private void layDanhSach(List<BanAn> danhSachBan) {
            for (BanAn ban : danhSachBan) {
                JPanel panelBan = new JPanel(new BorderLayout());
                panelBan.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                panelBan.setBackground(Color.DARK_GRAY);

                JLabel labelMaBan = new JLabel(ban.getMaBan(), SwingConstants.CENTER);
                labelMaBan.setFont(new Font("Times New Roman", Font.BOLD, 14));

                JLabel labelTrangThai = taoLabelTrangThai(ban);

                JPanel topPanel = new JPanel(new BorderLayout());
                topPanel.add(labelTrangThai, BorderLayout.WEST);
                topPanel.add(labelMaBan, BorderLayout.CENTER);
                topPanel.add(new JPanel(), BorderLayout.EAST);

                JButton buttonBan = taoButtonBan(ban);
                panelBan.add(topPanel, BorderLayout.NORTH);
                panelBan.add(buttonBan, BorderLayout.CENTER);

                panelDanhSachBan.add(panelBan);
            }
        }

        private JLabel taoLabelTrangThai(BanAn ban) {
            JLabel labelTrangThai = new JLabel();
            labelTrangThai.setOpaque(true);
            labelTrangThai.setPreferredSize(new Dimension(40, 40));
            labelTrangThai.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));

            switch (ban.getTrangThaiBanAn()) {
                case "Trống":
                    labelTrangThai.setBackground(Color.GREEN);
                    soLuongBanTrong++;
                    break;
                case "Đã đặt":
                    labelTrangThai.setBackground(Color.RED);
                    break;
                case "Đang sử dụng":
                    labelTrangThai.setBackground(Color.YELLOW);
                    break;
            }
            return labelTrangThai;
        }
        
        private JButton taoButtonBan(BanAn ban) {
            JButton buttonBan = new JButton();
            if (ban.getLoaiBan().getMaLoaiBan().equals("LB001")) {
                buttonBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/chair4.png")));
            } else if (ban.getLoaiBan().getMaLoaiBan().equals("LB002")) {
                buttonBan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/banVIP.png")));
            }

            buttonBan.addActionListener(e -> {
            if (banDaChon) {
                // Nếu đang ở chế độ chọn bàn, chỉ ghi nhận mã bàn
                banDuocChon = ban.getMaBan(); // Ghi nhận bàn được chọn
                try {
                    BanAn banAnDaChon = banAn_DAO.layThongTinBanAn(banDuocChon);

                    if (!banAnDaChon.getTrangThaiBanAn().equals("Trống")) {
                        JOptionPane.showMessageDialog(this, "<html><span style='color: red; font-size: 16px;'><b>Bàn " 
                        + banDuocChon + " không trống. Vui lòng chọn bàn khác!</b></span></html>", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Không cho phép chuyển bàn về chính nó
                    if (banCanChuyen.equals(banDuocChon)) {
                        JOptionPane.showMessageDialog(this, "Không thể chuyển bàn về chính nó!");
                        return;
                    }

                    // Hiển thị hộp thoại xác nhận
                    int confirm = JOptionPane.showConfirmDialog(this, 
                            "Bạn có chắc muốn chuyển bàn " + banCanChuyen + " sang bàn " + banDuocChon + " không?", 
                            "Xác nhận chuyển bàn", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        chuyenBan(banCanChuyen, banDuocChon);
                        banDaChon = false; // Tắt chế độ chọn bàn
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin bàn: " + ex.getMessage());
                }
            } else {
                // Nếu không ở chế độ chọn bàn, mở thông tin bàn
                moThongTinBan(ban);
            }
        });
        return buttonBan;
        }

        public  void moThongTinBan(BanAn ban) {
        try {
            ManHinhThongTinBan manHinhThongTinBan = new ManHinhThongTinBan(ban, this, taiKhoan);  
            manHinhThongTinBan.setSize(620, 400);
            manHinhThongTinBan.setLocationRelativeTo(null);
            manHinhThongTinBan.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            manHinhThongTinBan.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    taoPanelBan();  // Cập nhật lại giao diện sau khi đóng dialog nếu cần
                }
            });
            manHinhThongTinBan.setModal(true);
            manHinhThongTinBan.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhDatBan.class.getName()).log(Level.SEVERE, null, ex);
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

        panelSouth = new javax.swing.JPanel();
        panelChucNang = new javax.swing.JPanel();
        timKiemLabel = new javax.swing.JLabel();
        timKiemText = new javax.swing.JTextField();
        timKiemButton = new javax.swing.JButton();
        chuyenBanButton = new javax.swing.JButton();
        buttonGopBan = new javax.swing.JButton();
        datBanButton = new javax.swing.JButton();
        panelLoc = new javax.swing.JPanel();
        comboBoxLoaiBan = new javax.swing.JComboBox<>();
        comboBoxViTri = new javax.swing.JComboBox<>();
        comboBoxTang = new javax.swing.JComboBox<>();
        buttonLocTheo3TieuChi = new javax.swing.JButton();
        panelChuThich = new javax.swing.JPanel();
        mauXanh = new javax.swing.JPanel();
        mauDo = new javax.swing.JPanel();
        mauVang = new javax.swing.JPanel();
        mauXanhLabel = new javax.swing.JLabel();
        mauDoLabel = new javax.swing.JLabel();
        mauVangLabel = new javax.swing.JLabel();
        panelMain = new javax.swing.JPanel();
        panelWest = new javax.swing.JPanel();
        diLenButton = new javax.swing.JButton();
        diXuongButton = new javax.swing.JButton();
        panelEast = new javax.swing.JPanel();
        panelTop = new javax.swing.JPanel();
        resetButton = new RoundedButton(100);
        labelThongTin = new javax.swing.JLabel();
        labelSoLuongBanTrong = new javax.swing.JLabel();
        dsBanGopButton = new javax.swing.JButton();
        panelBottom = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        prevousButton = new javax.swing.JButton();
        scrollPanelDanhSachBan = new javax.swing.JScrollPane();
        panelDanhSachBan = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        panelSouth.setBackground(new java.awt.Color(255, 255, 255));
        panelSouth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelChucNang.setBackground(new java.awt.Color(255, 255, 255));
        panelChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        timKiemLabel.setText("Nhập mã bàn ở đây:");

        timKiemText.setToolTipText("");

        timKiemButton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        timKiemButton.setText("Tìm kiếm");
        timKiemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timKiemButtonActionPerformed(evt);
            }
        });

        chuyenBanButton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        chuyenBanButton.setText("Chuyển bàn");
        chuyenBanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chuyenBanButtonActionPerformed(evt);
            }
        });

        buttonGopBan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        buttonGopBan.setText("Gộp bàn");
        buttonGopBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGopBanActionPerformed(evt);
            }
        });

        datBanButton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        datBanButton.setText("Đặt trước");
        datBanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datBanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelChucNangLayout = new javax.swing.GroupLayout(panelChucNang);
        panelChucNang.setLayout(panelChucNangLayout);
        panelChucNangLayout.setHorizontalGroup(
            panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChucNangLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelChucNangLayout.createSequentialGroup()
                        .addComponent(timKiemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timKiemText, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timKiemButton))
                    .addGroup(panelChucNangLayout.createSequentialGroup()
                        .addComponent(datBanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chuyenBanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonGopBan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelChucNangLayout.setVerticalGroup(
            panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChucNangLayout.createSequentialGroup()
                .addGroup(panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timKiemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timKiemText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timKiemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonGopBan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chuyenBanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(datBanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelLoc.setBackground(new java.awt.Color(255, 255, 255));
        panelLoc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lọc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        comboBoxLoaiBan.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        comboBoxLoaiBan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Loại bàn--", "Bàn Thường", "Bàn VIP" }));

        comboBoxViTri.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        comboBoxViTri.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Vị trí--", "Gần cửa sổ", "Ngoài trời", "Giữa phòng" }));

        comboBoxTang.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        comboBoxTang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Tầng--", "Tầng 1", "Tầng 2", "Tầng 3" }));

        buttonLocTheo3TieuChi.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        buttonLocTheo3TieuChi.setText("Lọc");
        buttonLocTheo3TieuChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLocTheo3TieuChiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLocLayout = new javax.swing.GroupLayout(panelLoc);
        panelLoc.setLayout(panelLocLayout);
        panelLocLayout.setHorizontalGroup(
            panelLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLocLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(comboBoxLoaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxViTri, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxTang, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonLocTheo3TieuChi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLocLayout.setVerticalGroup(
            panelLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLocLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxLoaiBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxTang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonLocTheo3TieuChi, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelChuThich.setBackground(new java.awt.Color(255, 255, 255));
        panelChuThich.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chú thích trạng thái", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        mauXanh.setBackground(new java.awt.Color(0, 255, 0));
        mauXanh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mauXanh.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout mauXanhLayout = new javax.swing.GroupLayout(mauXanh);
        mauXanh.setLayout(mauXanhLayout);
        mauXanhLayout.setHorizontalGroup(
            mauXanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        mauXanhLayout.setVerticalGroup(
            mauXanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        mauDo.setBackground(new java.awt.Color(255, 0, 51));
        mauDo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mauDo.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout mauDoLayout = new javax.swing.GroupLayout(mauDo);
        mauDo.setLayout(mauDoLayout);
        mauDoLayout.setHorizontalGroup(
            mauDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        mauDoLayout.setVerticalGroup(
            mauDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        mauVang.setBackground(new java.awt.Color(255, 255, 0));
        mauVang.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mauVang.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout mauVangLayout = new javax.swing.GroupLayout(mauVang);
        mauVang.setLayout(mauVangLayout);
        mauVangLayout.setHorizontalGroup(
            mauVangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        mauVangLayout.setVerticalGroup(
            mauVangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        mauXanhLabel.setText("Đang trống");

        mauDoLabel.setText("Đã đặt");

        mauVangLabel.setText("Đang sử dụng");

        javax.swing.GroupLayout panelChuThichLayout = new javax.swing.GroupLayout(panelChuThich);
        panelChuThich.setLayout(panelChuThichLayout);
        panelChuThichLayout.setHorizontalGroup(
            panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChuThichLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mauXanhLabel)
                    .addComponent(mauXanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mauDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mauDoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mauVangLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChuThichLayout.createSequentialGroup()
                        .addComponent(mauVang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );
        panelChuThichLayout.setVerticalGroup(
            panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChuThichLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mauVang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mauDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mauXanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelChuThichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mauXanhLabel)
                    .addComponent(mauDoLabel)
                    .addComponent(mauVangLabel))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelSouthLayout = new javax.swing.GroupLayout(panelSouth);
        panelSouth.setLayout(panelSouthLayout);
        panelSouthLayout.setHorizontalGroup(
            panelSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSouthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelChuThich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        panelSouthLayout.setVerticalGroup(
            panelSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelSouthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelChuThich, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(panelSouth, java.awt.BorderLayout.PAGE_END);

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setPreferredSize(new java.awt.Dimension(1000, 1012));
        panelMain.setLayout(new java.awt.BorderLayout());

        panelWest.setBackground(new java.awt.Color(255, 255, 255));

        diLenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/diLen.png"))); // NOI18N
        diLenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diLenButtonActionPerformed(evt);
            }
        });

        diXuongButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/diXuong.png"))); // NOI18N
        diXuongButton.setEnabled(false);
        diXuongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diXuongButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelWestLayout = new javax.swing.GroupLayout(panelWest);
        panelWest.setLayout(panelWestLayout);
        panelWestLayout.setHorizontalGroup(
            panelWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(diLenButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(diXuongButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panelWestLayout.setVerticalGroup(
            panelWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWestLayout.createSequentialGroup()
                .addComponent(diLenButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 824, Short.MAX_VALUE)
                .addComponent(diXuongButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelMain.add(panelWest, java.awt.BorderLayout.LINE_START);

        panelEast.setBackground(new java.awt.Color(255, 255, 255));
        panelEast.setPreferredSize(new java.awt.Dimension(60, 270));

        javax.swing.GroupLayout panelEastLayout = new javax.swing.GroupLayout(panelEast);
        panelEast.setLayout(panelEastLayout);
        panelEastLayout.setHorizontalGroup(
            panelEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panelEastLayout.setVerticalGroup(
            panelEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );

        panelMain.add(panelEast, java.awt.BorderLayout.LINE_END);

        panelTop.setBackground(new java.awt.Color(255, 255, 255));
        panelTop.setPreferredSize(new java.awt.Dimension(853, 70));

        resetButton.setForeground(new java.awt.Color(255, 255, 255));
        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refesh.png"))); // NOI18N
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        labelThongTin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labelThongTin.setForeground(new java.awt.Color(0, 51, 255));
        labelThongTin.setText("Tầng >> Vị trí >> Loại bàn");

        labelSoLuongBanTrong.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labelSoLuongBanTrong.setForeground(new java.awt.Color(0, 204, 0));
        labelSoLuongBanTrong.setText("Bàn trống 1/10");

        dsBanGopButton.setText("Danh sách bàn gộp");
        dsBanGopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dsBanGopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTopLayout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelThongTin)
                    .addComponent(labelSoLuongBanTrong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
                .addComponent(dsBanGopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(resetButton)
                .addGap(14, 14, 14))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dsBanGopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(labelThongTin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSoLuongBanTrong))
                    .addComponent(resetButton))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panelMain.add(panelTop, java.awt.BorderLayout.PAGE_START);

        panelBottom.setBackground(new java.awt.Color(255, 255, 255));

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/right-arrow.png"))); // NOI18N
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        prevousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/left-arrow.png"))); // NOI18N
        prevousButton.setEnabled(false);
        prevousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevousButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBottomLayout = new javax.swing.GroupLayout(panelBottom);
        panelBottom.setLayout(panelBottomLayout);
        panelBottomLayout.setHorizontalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                .addContainerGap(485, Short.MAX_VALUE)
                .addComponent(prevousButton)
                .addGap(18, 18, 18)
                .addComponent(nextButton)
                .addGap(528, 528, 528))
        );
        panelBottomLayout.setVerticalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(prevousButton, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        panelMain.add(panelBottom, java.awt.BorderLayout.PAGE_END);

        scrollPanelDanhSachBan.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelDanhSachBan.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelDanhSachBan.setPreferredSize(new java.awt.Dimension(100, 900));

        panelDanhSachBan.setBackground(new java.awt.Color(255, 255, 255));
        panelDanhSachBan.setLayout(new java.awt.GridLayout(0, 4, 20, 20));
        scrollPanelDanhSachBan.setViewportView(panelDanhSachBan);

        panelMain.add(scrollPanelDanhSachBan, java.awt.BorderLayout.CENTER);

        add(panelMain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        tangNay = 0;
        viTriNay = 0;
        timKiemText.setText("");
        banDaChon = false;
        
        comboBoxLoaiBan.setSelectedIndex(0);
        comboBoxTang.setSelectedIndex(0);
        comboBoxViTri.setSelectedIndex(0);
        
        diLenButton.setEnabled(true);
        diXuongButton.setEnabled(false);
        prevousButton.setEnabled(false);
        nextButton.setEnabled(true);
        taoPanelBan();
    }//GEN-LAST:event_resetButtonActionPerformed
     
    private void capNhatTrangThaiBanAn(String maBan, String trangThai){
        banAn_DAO = new BanAn_DAO();
        try{
            banAn_DAO.capNhatTrangThaiBan(maBan, trangThai);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Cập nhật không thành công!");
        }
    }
     
    private void chuyenBan(String banCanChuyen, String banDaChon) throws SQLException {
        try {
            HoaDon hd = hoaDon_DAO.layHoaDon(banCanChuyen);
            if (hd == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn của bàn " + banCanChuyen + ". Không thể chuyển bàn!");
                return;
            }
            
            if (!hoaDon_DAO.capNhatChuyenBan(hd.getMaHoaDon(), banDaChon)) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật hóa đơn. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            capNhatTrangThaiBanAn(banDaChon, "Đang sử dụng");
            capNhatTrangThaiBanAn(banCanChuyen, "Trống");
            JOptionPane.showMessageDialog(this, 
            "<html><span style='color: green; font-size: 16px;'><b>Chuyển bàn thành công từ " 
            + banCanChuyen + " sang " + banDaChon + "!</b></span></html>", 
            "Thành công", JOptionPane.INFORMATION_MESSAGE);

            taoPanelBan();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Lỗi xảy ra khi chuyển bàn: ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chuyenBanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chuyenBanButtonActionPerformed
        banCanChuyen = timKiemText.getText();
        if (banCanChuyen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "<html><span style='font-size: 16px;'><b>Vui lòng nhập mã bàn cần chuyển!</b>",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(!banCanChuyen.matches("^B\\d{4}$")){
            JOptionPane.showMessageDialog(this, 
    "<html><span style='font-size: 16px;'><b>Mã bàn không hợp lệ!</b><br>Định dạng: BYXXX, (Y là tầng 1 - 2 - hoặc 3)(X là số)</span></html>", 
    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        BanAn ban;
        try {
            ban = banAn_DAO.layThongTinBanAn(banCanChuyen);
            if(ban==null){
                JOptionPane.showMessageDialog(this, "<html><span style='font-size: 16px;'><b>Không tìm thấy bàn với mã: </b>" + banCanChuyen, 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }else if(!ban.getTrangThaiBanAn().equals("Đang sử dụng")){
                JOptionPane.showMessageDialog(this, "<html><span style='font-size: 16px;'><b>Bàn phải ở trạng thái 'Đang sử dụng' thì mới được chuyển bàn </b>", 
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhDatBan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        banDaChon = true; // Bật chế độ chọn bàn
        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn mới để chuyển!");
    }//GEN-LAST:event_chuyenBanButtonActionPerformed

    private void buttonGopBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGopBanActionPerformed
       gopBan();
    }//GEN-LAST:event_buttonGopBanActionPerformed
   
    public void gopBan() {
        String banChinh = JOptionPane.showInputDialog(this, "Nhập mã bàn chính:");
        String banPhu = JOptionPane.showInputDialog(this, "Nhập mã bàn phụ:");

//        BanAn_DAO banAn_DAO = new BanAn_DAO();
//        HoaDon_DAO hoaDon_DAO = new HoaDon_DAO();

        if (banChinh == null || banPhu == null || banChinh.isEmpty() || banPhu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã bàn hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (banChinh.equals(banPhu)) {
            JOptionPane.showMessageDialog(this, "Không thể gộp cùng một bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Lấy thông tin bàn chính và bàn phụ
            BanAn banAnChinh = banAn_DAO.layThongTinBanAn(banChinh);
            BanAn banAnPhu = banAn_DAO.layThongTinBanAn(banPhu);

            
            BanManager.getInstance().setMaBanChinh(banAnChinh.getMaBan());
            BanManager.getInstance().setMaBanPhu(banAnPhu.getMaBan());
            
            if (banAnChinh == null || banAnPhu == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin của một trong hai bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra cùng tầng và vị trí
            if (!banAnChinh.getTang().equals(banAnPhu.getTang()) || 
                !banAnChinh.getViTri().equals(banAnPhu.getViTri())) {
                JOptionPane.showMessageDialog(this, "Hai bàn phải cùng tầng và cùng vị trí để gộp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra trạng thái bàn
            if (!banAnChinh.getTrangThaiBanAn().equals("Đang sử dụng") || 
                !banAnPhu.getTrangThaiBanAn().equals("Đang sử dụng")) {
                JOptionPane.showMessageDialog(this, "Cả hai bàn phải đang sử dụng để gộp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gọi phương thức gopBan1 để thực hiện gộp bàn
            boolean ketQua = banAn_DAO.gopBan1(banChinh, banPhu);
            if (ketQua) {
                JOptionPane.showMessageDialog(this, "Gộp bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                // Cập nhật giao diện
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thực hiện gộp bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thực hiện gộp bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatDanhSachBan() {
        panelDanhSachBan.removeAll();
        panelDanhSachBan.repaint();
    }
    private void datBanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datBanButtonActionPerformed
        ManHinhThongTinDatTruoc manHinhTTDatBan = null;
        try {
            manHinhTTDatBan = new ManHinhThongTinDatTruoc(taiKhoan);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhDatBan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        manHinhTTDatBan.setSize(1200, 650);
        manHinhTTDatBan.setLocationRelativeTo(null);
        manHinhTTDatBan.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        manHinhTTDatBan.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                
            }
        });
        manHinhTTDatBan.setModal(true);
        manHinhTTDatBan.setVisible(true);
    }//GEN-LAST:event_datBanButtonActionPerformed

    private void prevousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevousButtonActionPerformed
        if (viTriNay > 0) {
            viTriNay--; // Tăng chỉ số vị trí
            nextButton.setEnabled(true);
            if(viTriNay == 0){
                prevousButton.setEnabled(false);
            }
        }
        taoPanelBan();
    }//GEN-LAST:event_prevousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (viTriNay < viTriBan.length-1) {
            viTriNay++; // Tăng chỉ số vị trí
            prevousButton.setEnabled(true);
            if(viTriNay == viTriBan.length-1){
                nextButton.setEnabled(false);
            }
        }
        taoPanelBan(); // Gọi lại phương thức để cập nhật giao diện
    }//GEN-LAST:event_nextButtonActionPerformed

    private void buttonLocTheo3TieuChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLocTheo3TieuChiActionPerformed
        prevousButton.setEnabled(false);
        nextButton.setEnabled(false);
        diLenButton.setEnabled(false);
        diXuongButton.setEnabled(false);
        soLuongBanTrong = 0;
        labelThongTin.setText("");
        
        banAn_DAO = new BanAn_DAO();
        panelDanhSachBan.removeAll();
        
        String loaiBan = comboBoxLoaiBan.getSelectedItem().toString();
        String viTri = comboBoxViTri.getSelectedItem().toString();
        String tang = comboBoxTang.getSelectedItem().toString();
        
        // Kiểm tra xem có tiêu chí nào được chọn không
        if (loaiBan.equals(comboBoxLoaiBan.getItemAt(0))
                && tang.equals(comboBoxTang.getItemAt(0))
                && viTri.equals(comboBoxViTri.getItemAt(0))) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn các tiêu chí muốn lọc.");
           return;
        }else{
            // Tiến hành lấy danh sách bàn theo các tiêu chí đã cho
            try {
                List<BanAn> danhSachBan = banAn_DAO.layBanTheoTieuChi(loaiBan, tang, viTri);

                layDanhSach(danhSachBan);
                labelSoLuongBanTrong.setText("Bàn trống " + soLuongBanTrong + "/" + danhSachBan.size());
                // Cập nhật panel danh sách bàn vào JScrollPane
                scrollPanelDanhSachBan.setViewportView(panelDanhSachBan);
                // Cập nhật lại giao diện sau khi thêm các panel
                scrollPanelDanhSachBan.revalidate();
                scrollPanelDanhSachBan.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_buttonLocTheo3TieuChiActionPerformed

    private void diLenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diLenButtonActionPerformed
        if (tangNay < tangHienTai.length-1) {
            tangNay++; // Tăng chỉ số vị trí
            diXuongButton.setEnabled(true);
            if(tangNay == tangHienTai.length-1){
                diLenButton.setEnabled(false);
            }
        }
        taoPanelBan(); // Gọi lại phương thức để cập nhật giao diện
    }//GEN-LAST:event_diLenButtonActionPerformed

    private void diXuongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diXuongButtonActionPerformed
        if (tangNay > 0) {
            tangNay--; // Tăng chỉ số vị trí
            diLenButton.setEnabled(true);
            if(tangNay == 0){
                diXuongButton.setEnabled(false);
            }
        }
        taoPanelBan(); // Gọi lại phương thức để cập nhật giao diện
    }//GEN-LAST:event_diXuongButtonActionPerformed

    private void timKiemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timKiemButtonActionPerformed
        String maBan = timKiemText.getText();
        if(maBan.isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã bàn!!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }else if(!maBan.matches("^B\\d{4}$")){
            JOptionPane.showMessageDialog(this, "Mã bàn sai định dạng. 'BY001' đổi Y thành tầng 1 2 3 !!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        BanAn ban = null;
        try {
            ban = banAn_DAO.layThongTinBanAn(maBan);
            if(ban!=null){
                moThongTinBan(ban);
            }else{
                JOptionPane.showMessageDialog(this, "Không tìm thấy bàn với mã: " + maBan);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManHinhDatBan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_timKiemButtonActionPerformed

    private void dsBanGopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dsBanGopButtonActionPerformed
       JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);  // Lấy JFrame cha của JPanel
        BanDuocGopDialog dialog = new BanDuocGopDialog(parentFrame, true);
        dialog.setVisible(true);
    }//GEN-LAST:event_dsBanGopButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonGopBan;
    private javax.swing.JButton buttonLocTheo3TieuChi;
    private javax.swing.JButton chuyenBanButton;
    private javax.swing.JComboBox<String> comboBoxLoaiBan;
    private javax.swing.JComboBox<String> comboBoxTang;
    private javax.swing.JComboBox<String> comboBoxViTri;
    private javax.swing.JButton datBanButton;
    private javax.swing.JButton diLenButton;
    private javax.swing.JButton diXuongButton;
    private javax.swing.JButton dsBanGopButton;
    private javax.swing.JLabel labelSoLuongBanTrong;
    private javax.swing.JLabel labelThongTin;
    private javax.swing.JPanel mauDo;
    private javax.swing.JLabel mauDoLabel;
    private javax.swing.JPanel mauVang;
    private javax.swing.JLabel mauVangLabel;
    private javax.swing.JPanel mauXanh;
    private javax.swing.JLabel mauXanhLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel panelBottom;
    private javax.swing.JPanel panelChuThich;
    private javax.swing.JPanel panelChucNang;
    private javax.swing.JPanel panelDanhSachBan;
    private javax.swing.JPanel panelEast;
    private javax.swing.JPanel panelLoc;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelSouth;
    private javax.swing.JPanel panelTop;
    private javax.swing.JPanel panelWest;
    private javax.swing.JButton prevousButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JScrollPane scrollPanelDanhSachBan;
    private javax.swing.JButton timKiemButton;
    private javax.swing.JLabel timKiemLabel;
    private javax.swing.JTextField timKiemText;
    // End of variables declaration//GEN-END:variables
}
