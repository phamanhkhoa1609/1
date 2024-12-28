package dao;

import java.sql.*;
import connectDB.ConnectJDBC;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modal.KhuyenMai;


import connectDB.ConnectJDBC;
import java.beans.Statement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//import jdk.jfr.Timestamp;


import modal.KhuyenMaiKhachHang;
import modal.KhachHang;
import modal.KhuyenMai;
import modal.MonAnKhuyenMai;
import modal.MonAn;




import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import static view.quanlykhuyenmai.ManHinhTaoKhuyenMai.removeDiacritics;

public class KhuyenMai_DAO {
     // Hàm cập nhật số lượng tối thiểu của khuyến mãi
    public void capNhatSoLuongToiThieu(String maKhuyenMai, int soLuongToiThieu) throws SQLException {
        Connection con = ConnectJDBC.getConnection();
        String sql = "UPDATE KhuyenMai SET soLuongToiThieu = ? WHERE maKhuyenMai = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setDouble(1, soLuongToiThieu);
        statement.setString(2, maKhuyenMai);

        statement.executeUpdate();
    }
    
    
    // Duyyyyyyyyyyyyyyyyyyyyyyyyyyyy:
    
    
    // Hàm cập nhật trạng thái khuyến mãi dựa trên ngày kết thúc
    public void capNhatTrangThaiKhuyenMaiDuy() throws SQLException {
        String sqlUpdateKhuyenMai = "UPDATE KhuyenMai SET trangThai = CASE " +
                                   "WHEN ngayKetThuc < ? THEN 'Expired' " +
                                   "ELSE 'Active' END";
        String sqlDeleteMonAnKhuyenMai = "DELETE FROM MonAnKhuyenMai WHERE maKhuyenMai IN (SELECT maKhuyenMai FROM KhuyenMai WHERE trangThai = 'Expired')";
        String sqlDeleteKhuyenMaiKhachHang = "DELETE FROM KhuyenMaiKhachHang WHERE maKhuyenMai IN (SELECT maKhuyenMai FROM KhuyenMai WHERE trangThai = 'Expired')";

        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement psUpdate = connection.prepareStatement(sqlUpdateKhuyenMai);
             PreparedStatement psDeleteMonAnKhuyenMai = connection.prepareStatement(sqlDeleteMonAnKhuyenMai);
             PreparedStatement psDeleteKhuyenMaiKhachHang = connection.prepareStatement(sqlDeleteKhuyenMaiKhachHang)) {
            LocalDate ngayHienTai = LocalDate.now();
            psUpdate.setObject(1, java.sql.Date.valueOf(ngayHienTai));
            psUpdate.executeUpdate();

            psDeleteMonAnKhuyenMai.executeUpdate();
            psDeleteKhuyenMaiKhachHang.executeUpdate();
        }
    }


    // Hàm lấy danh sách khuyến mãi
    public List<KhuyenMai> layDanhSachKhuyenMaiDuy() throws SQLException {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String sql = "SELECT maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, giaTriKhuyenMai, trangThai FROM KhuyenMai";

        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maKhuyenMai = rs.getString("maKhuyenMai");
                String tenKhuyenMai = rs.getString("tenKhuyenMai");
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                String trangThai = rs.getString("trangThai");

                // Tạo đối tượng KhuyenMai và thêm vào danh sách
                KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, null, null, null, trangThai, giaTriKhuyenMai, loaiKhuyenMai, 0, 0, null);
                danhSachKhuyenMai.add(km);
            }
        }
        return danhSachKhuyenMai;
    }

    public KhuyenMai layKhuyenMaiTheoMaDuy(String maKhuyenMai) throws SQLException {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new KhuyenMai(
                    resultSet.getString("maKhuyenMai"),
                    resultSet.getString("tenKhuyenMai"),
                    resultSet.getString("moTaKhuyenMai"),
                    convertToLocalDateTime(resultSet.getTimestamp("ngayBatDau")),
                    convertToLocalDateTime(resultSet.getTimestamp("ngayKetThuc")),
                    resultSet.getString("trangThai"),
                    resultSet.getDouble("giaTriKhuyenMai"),
                    resultSet.getString("loaiKhuyenMai"),
                    resultSet.getDouble("diemYeuCau"),
                    resultSet.getInt("soLuongToiThieu"),
                    null // Chỉnh sửa tại đây nếu cần lấy maQuanLy
                );
            }
        }
        return null;
    }

    // Hàm chuyển đổi Date thành LocalDateTime
    private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null; // Kiểm tra null
    }
    
    // Phương thức mới để lấy danh sách khách hàng theo mã khuyến mãi
    public List<String> layDanhSachKhachHangTheoKhuyenMaiDuy(String maKhuyenMai) throws SQLException {
        List<String> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT maKhachHang FROM KhuyenMaiKhachHang WHERE maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                danhSachKhachHang.add(resultSet.getString("maKhachHang"));
            }
        }
        return danhSachKhachHang;
    }

    // Tạo mã khuyến mãi mới +1 dựa trên mã đang có
    public String generateMaKhuyenMaiDuy() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(maKhuyenMai, 3, LEN(maKhuyenMai) - 2) AS INT)) AS maxMa FROM KhuyenMai";
        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxMa = rs.getInt("maxMa");
                return "KM" + String.format("%03d", maxMa + 1); // Tạo mã mới
            }
        }
        return "KM001"; // Nếu không có mã nào, bắt đầu từ KM001
    }
    
    public void themKhuyenMaiDuy(KhuyenMai khuyenMai) throws SQLException {
        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayBatDau, ngayKetThuc, trangThai, giaTriKhuyenMai, loaiKhuyenMai, diemYeuCau, soLuongToiThieu, maQuanLy) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, khuyenMai.getMaKhuyenMai());
            ps.setString(2, khuyenMai.getTenKhuyenMai());
            ps.setString(3, khuyenMai.getMoTaKhuyenMai());
            ps.setTimestamp(4, Timestamp.valueOf(khuyenMai.getNgayBatDau()));
            ps.setTimestamp(5, Timestamp.valueOf(khuyenMai.getNgayKetThuc()));
            ps.setString(6, khuyenMai.getTrangThai());
            ps.setDouble(7, khuyenMai.getGiaTriKhuyenMai());
            ps.setString(8, khuyenMai.getLoaiKhuyenMai());
            ps.setDouble(9, khuyenMai.getDiemYeuCau());
            ps.setInt(10, khuyenMai.getSoLuongToiThieu());
            ps.setString(11, khuyenMai.getQuanLy() != null ? khuyenMai.getQuanLy().getMaQuanLy() : null); // Kiểm tra null
            ps.executeUpdate();
        } catch (SQLException e) {
            // Kiểm tra xem có phải lỗi trùng lặp không
            if (e.getMessage().contains("Violation of PRIMARY KEY constraint")) {
                // Bỏ qua lỗi trùng lặp mà không hiển thị cho người dùng
                return;
            } else {
                // Nếu là lỗi khác, ném lại
                throw e;
            }
        }
    }
    
    // update lại cách reload bảng KhuyenMaiKhachHang 
    public boolean isKhuyenMaiActive(String maKhuyenMai) throws SQLException {
        String sql = "SELECT active FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectJDBC.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, maKhuyenMai);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("active");
            }
        }
        return false;
    }

    
    // update lại cách reload bảng KhuyenMaiKhachHang __ lấy điểm yêu cầu của khuyến mãi
    public int getDiemYeuCau(String maKhuyenMai) throws SQLException {
        String sql = "SELECT diemYeuCau FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectJDBC.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, maKhuyenMai);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("diemYeuCau");
            }
        }
        return 0; // Hoặc giá trị mặc định khác
    }
    
    // update lại cách reload bảng KhuyenMaiKhachHang __ lấy trạng thái khuyến mãi
    public List<KhuyenMai> layDanhSachKhuyenMaiTheoTrangThai(String trangThai) throws SQLException {
        List<KhuyenMai> khuyenMais = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThai = ?";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trangThai);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Tạo đối tượng KhuyenMai từ ResultSet
                KhuyenMai khuyenMai = new KhuyenMai(
                    resultSet.getString("maKhuyenMai"),
                    resultSet.getString("tenKhuyenMai"),
                    resultSet.getString("moTaKhuyenMai"),
                    convertToLocalDateTime(resultSet.getTimestamp("ngayBatDau")),
                    convertToLocalDateTime(resultSet.getTimestamp("ngayKetThuc")),
                    resultSet.getString("trangThai"),
                    resultSet.getDouble("giaTriKhuyenMai"),
                    resultSet.getString("loaiKhuyenMai"),
                    resultSet.getDouble("diemYeuCau"),
                    resultSet.getInt("soLuongToiThieu"),
                    null // Chỉnh sửa tại đây nếu cần lấy maQuanLy
                );
                khuyenMais.add(khuyenMai);
            }
        }
        return khuyenMais;
    }

    
    // Hàm để lấy danh sách mã khuyến mãi còn hiệu lực
    public List<KhuyenMai> layDanhSachKhuyenMaiHieuLucDuy() throws SQLException {
        List<KhuyenMai> khuyenMais = new ArrayList<>();
        String sql = "SELECT maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayBatDau, ngayKetThuc, " +
                     "trangThai, giaTriKhuyenMai, loaiKhuyenMai, diemYeuCau, soLuongToiThieu FROM KhuyenMai " +
                     "WHERE trangThai = 'Active' AND loaiKhuyenMai <> 'Quà tặng' AND loaiKhuyenMai <> 'Món ăn'";

        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maKhuyenMai = rs.getString("maKhuyenMai");
                String tenKhuyenMai = rs.getString("tenKhuyenMai");
                String moTaKhuyenMai = rs.getString("moTaKhuyenMai");
                LocalDateTime ngayBatDau = rs.getTimestamp("ngayBatDau").toLocalDateTime();
                LocalDateTime ngayKetThuc = rs.getTimestamp("ngayKetThuc").toLocalDateTime();
                String trangThai = rs.getString("trangThai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                double diemYeuCau = rs.getDouble("diemYeuCau");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");

                // Tạo đối tượng KhuyenMai và thêm vào danh sách
                KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayBatDau, 
                                             ngayKetThuc, trangThai, giaTriKhuyenMai, loaiKhuyenMai, 
                                             diemYeuCau, soLuongToiThieu, null);
                khuyenMais.add(km);
            }
        }
        return khuyenMais;
    }



    // Hàm để thêm mã khuyến mãi vào bảng KhuyenMaiKhachHang
    public void themKhuyenMaiKhachHangDuy(String maKhuyenMai, String maKhachHang) throws SQLException {
        String sqlInsert = "INSERT INTO KhuyenMaiKhachHang (maKhuyenMai, maKhachHang) VALUES (?, ?)";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement psInsert = connection.prepareStatement(sqlInsert)) {
            psInsert.setString(1, maKhuyenMai);
            psInsert.setString(2, maKhachHang);
            psInsert.executeUpdate();
        }
    }
    
    

    public void dungKhuyenMaiDuy(String maKhuyenMai) throws SQLException {
        String sql = "UPDATE KhuyenMai SET ngayKetThuc = ?, trangThai = 'Expired' WHERE maKhuyenMai = ?";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Lấy ngày hiện tại và trừ đi một ngày
            LocalDate ngayHienTai = LocalDate.now().minusDays(1);
            ps.setObject(1, java.sql.Date.valueOf(ngayHienTai)); // Set ngày kết thúc là ngày hôm trước
            ps.setString(2, maKhuyenMai);

            ps.executeUpdate();
        }
    }
    

    public List<KhuyenMai> timKiemKhuyenMaiDuy(String maKhuyenMai, String tenKhuyenMai, String moTaKhuyenMai, 
                                         Date ngayBatDau, Date ngayKetThuc, String trangThai, 
                                         Double diemYeuCau, String loaiKhuyenMai,Double giaTriKhuyenMai, Integer soLuongToiThieu) throws SQLException {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM KhuyenMai WHERE 1=1");

        if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
            sql.append(" AND maKhuyenMai LIKE ?");
        }
        if (tenKhuyenMai != null && !tenKhuyenMai.isEmpty()) {
            sql.append(" AND tenKhuyenMai LIKE ?");
        }
        if (moTaKhuyenMai != null && !moTaKhuyenMai.isEmpty()) {
            sql.append(" AND moTaKhuyenMai LIKE ?");
        }
        if (ngayBatDau != null) {
            sql.append(" AND ngayBatDau >= ?");
        }
        if (ngayKetThuc != null) {
            sql.append(" AND ngayKetThuc <= ?");
        }
        if (trangThai != null && !trangThai.isEmpty()) {
            sql.append(" AND trangThai = ?");
        }
        if (diemYeuCau != null) {
            sql.append(" AND diemYeuCau >= ?");
        }


        if (loaiKhuyenMai != null && !loaiKhuyenMai.isEmpty()) {
            sql.append(" AND loaiKhuyenMai = ?");
        }

        if (giaTriKhuyenMai != null) {
            sql.append(" AND giaTriKhuyenMai >= ?");
        }
        if (soLuongToiThieu != null) {
            sql.append(" AND soLuongToiThieu >= ?");
        }

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int index = 1;
            if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
                ps.setString(index++, "%" + maKhuyenMai + "%");
            }
            if (tenKhuyenMai != null && !tenKhuyenMai.isEmpty()) {
                ps.setString(index++, "%" + tenKhuyenMai + "%");
            }
            if (moTaKhuyenMai != null && !moTaKhuyenMai.isEmpty()) {
                ps.setString(index++, "%" + moTaKhuyenMai + "%");
            }
            if (ngayBatDau != null) {
                ps.setDate(index++, new java.sql.Date(ngayBatDau.getTime()));
            }
            if (ngayKetThuc != null) {
                ps.setDate(index++, new java.sql.Date(ngayKetThuc.getTime()));
            }
            if (trangThai != null && !trangThai.isEmpty()) {
                ps.setString(index++, trangThai);
            }
            if (diemYeuCau != null) {
                ps.setDouble(index++, diemYeuCau);
            }


            if (loaiKhuyenMai != null && !loaiKhuyenMai.isEmpty()) {
                ps.setString(index++, loaiKhuyenMai);
            }

            if (giaTriKhuyenMai != null) {
                ps.setDouble(index++, giaTriKhuyenMai);
            }
            if (soLuongToiThieu != null) {
                ps.setInt(index++, soLuongToiThieu);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhuyenMai khuyenMai = new KhuyenMai();
                    // Gán giá trị cho đối tượng khuyến mãi từ ResultSet
                    khuyenMai.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                    khuyenMai.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                    khuyenMai.setMoTaKhuyenMai(rs.getString("moTaKhuyenMai"));
                    khuyenMai.setNgayBatDau(rs.getTimestamp("ngayBatDau").toLocalDateTime());
                    khuyenMai.setNgayKetThuc(rs.getTimestamp("ngayKetThuc").toLocalDateTime());
                    khuyenMai.setTrangThai(rs.getString("trangThai"));
                    khuyenMai.setGiaTriKhuyenMai(rs.getDouble("giaTriKhuyenMai"));
                    khuyenMai.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                    khuyenMai.setDiemYeuCau(rs.getDouble("diemYeuCau"));
                    khuyenMai.setSoLuongToiThieu(rs.getInt("soLuongToiThieu")); // Đọc giá trị soLuongToiThieu
                    danhSachKhuyenMai.add(khuyenMai);
                }
            }
        }
        return danhSachKhuyenMai;
    }
    
    
    
    public List<KhuyenMai> timKiemKhuyenMaiTheoSDT(String soDienThoai, String maKhuyenMai, String tenKhuyenMai, 
                                                    String moTaKhuyenMai, Date ngayBatDau, Date ngayKetThuc, 
                                                    String trangThai, Double diemYeuCau, String loaiKhuyenMai, 
                                                    Double giaTriKhuyenMai, Integer soLuongToiThieu) throws SQLException {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String maKhachHang = null;

        // Bước 1: Tìm maKhachHang từ soDienThoai
        String sqlKhachHang = "SELECT maKhachHang FROM KhachHang WHERE soDienThoai = ?";
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement psKhachHang = connection.prepareStatement(sqlKhachHang)) {
            psKhachHang.setString(1, soDienThoai);
            try (ResultSet rsKhachHang = psKhachHang.executeQuery()) {
                if (rsKhachHang.next()) {
                    maKhachHang = rsKhachHang.getString("maKhachHang");
                } else {
                    JOptionPane.showMessageDialog(null, "Khách hàng không tồn tại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return danhSachKhuyenMai; // Trả về danh sách rỗng nếu không tìm thấy khách hàng
                }
            }
        }

        // Bước 2: Tìm maKhuyenMai từ maKhachHang trong bảng KhuyenMaiKhachHang
        String sqlKhuyenMaiKhachHang = "SELECT maKhuyenMai FROM KhuyenMaiKhachHang WHERE maKhachHang = ?";
        List<String> maKhuyenMaiList = new ArrayList<>();
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement psKhuyenMaiKhachHang = connection.prepareStatement(sqlKhuyenMaiKhachHang)) {
            psKhuyenMaiKhachHang.setString(1, maKhachHang);
            try (ResultSet rsKhuyenMaiKhachHang = psKhuyenMaiKhachHang.executeQuery()) {
                while (rsKhuyenMaiKhachHang.next()) {
                    maKhuyenMaiList.add(rsKhuyenMaiKhachHang.getString("maKhuyenMai"));
                }
            }
        }

        // Bước 3: Tìm kiếm thông tin khuyến mãi từ danh sách maKhuyenMai
        if (!maKhuyenMaiList.isEmpty()) {
            StringBuilder sql = new StringBuilder("SELECT * FROM KhuyenMai WHERE maKhuyenMai IN (");
            for (int i = 0; i < maKhuyenMaiList.size(); i++) {
                sql.append("?");
                if (i < maKhuyenMaiList.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");

            // Bước 4: Lọc thêm theo các tiêu chí khác
            if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
                sql.append(" AND maKhuyenMai LIKE ?");
            }
            if (tenKhuyenMai != null && !tenKhuyenMai.isEmpty()) {
                sql.append(" AND tenKhuyenMai LIKE ?");
            }
            if (moTaKhuyenMai != null && !moTaKhuyenMai.isEmpty()) {
                sql.append(" AND moTaKhuyenMai LIKE ?");
            }
            if (ngayBatDau != null) {
                sql.append(" AND ngayBatDau >= ?");
            }
            if (ngayKetThuc != null) {
                sql.append(" AND ngayKetThuc <= ?");
            }
            if (trangThai != null && !trangThai.isEmpty()) {
                sql.append(" AND trangThai = ?");
            }
            if (diemYeuCau != null) {
                sql.append(" AND diemYeuCau >= ?");
            }
            if (loaiKhuyenMai != null && !loaiKhuyenMai.isEmpty()) {
                sql.append(" AND loaiKhuyenMai = ?");
            }
            if (giaTriKhuyenMai != null) {
                sql.append(" AND giaTriKhuyenMai >= ?");
            }
            if (soLuongToiThieu != null) {
                sql.append(" AND soLuongToiThieu >= ?");
            }

            try (Connection connection = ConnectJDBC.getConnection();
                 PreparedStatement psKhuyenMai = connection.prepareStatement(sql.toString())) {
                int index = 1;
                for (String ma : maKhuyenMaiList) {
                    psKhuyenMai.setString(index++, ma);
                }
                if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
                    psKhuyenMai.setString(index++, "%" + maKhuyenMai + "%");
                }
                if (tenKhuyenMai != null && !tenKhuyenMai.isEmpty()) {
                    psKhuyenMai.setString(index++, "%" + tenKhuyenMai + "%");
                }
                if (moTaKhuyenMai != null && !moTaKhuyenMai.isEmpty()) {
                    psKhuyenMai.setString(index++, "%" + moTaKhuyenMai + "%");
                }
                if (ngayBatDau != null) {
                    psKhuyenMai.setDate(index++, new java.sql.Date(ngayBatDau.getTime()));
                }
                if (ngayKetThuc != null) {
                    psKhuyenMai.setDate(index++, new java.sql.Date(ngayKetThuc.getTime()));
                }
                if (trangThai != null && !trangThai.isEmpty()) {
                    psKhuyenMai.setString(index++, trangThai);
                }
                if (diemYeuCau != null) {
                    psKhuyenMai.setDouble(index++, diemYeuCau);
                }
                if (loaiKhuyenMai != null && !loaiKhuyenMai.isEmpty()) {
                    psKhuyenMai.setString(index++, loaiKhuyenMai);
                }
                if (giaTriKhuyenMai != null) {
                    psKhuyenMai.setDouble(index++, giaTriKhuyenMai);
                }
                if (soLuongToiThieu != null) {
                    psKhuyenMai.setInt(index++, soLuongToiThieu);
                }

                try (ResultSet rs = psKhuyenMai.executeQuery()) {
                    while (rs.next()) {
                        KhuyenMai khuyenMai = new KhuyenMai();
                        khuyenMai.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                        khuyenMai.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                        khuyenMai.setMoTaKhuyenMai(rs.getString("moTaKhuyenMai"));
                        khuyenMai.setNgayBatDau(rs.getTimestamp("ngayBatDau").toLocalDateTime());
                        khuyenMai.setNgayKetThuc(rs.getTimestamp("ngayKetThuc").toLocalDateTime());
                        khuyenMai.setTrangThai(rs.getString("trangThai"));
                        khuyenMai.setGiaTriKhuyenMai(rs.getDouble("giaTriKhuyenMai"));
                        khuyenMai.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                        khuyenMai.setDiemYeuCau(rs.getDouble("diemYeuCau"));
                        khuyenMai.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
                        danhSachKhuyenMai.add(khuyenMai);
                    }
                }
            }
        }

        return danhSachKhuyenMai;
    }


    public List<String> layDanhSachLoaiKhuyenMaiDuy() throws SQLException {
        Set<String> loaiKhuyenMaiSet = new HashSet<>();
        String sql = "SELECT DISTINCT loaiKhuyenMai FROM KhuyenMai";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                if (loaiKhuyenMai != null) {
                    loaiKhuyenMaiSet.add(loaiKhuyenMai); // Bỏ qua xử lý bỏ dấu và chữ thường
                }
            }
        }
        return new ArrayList<>(loaiKhuyenMaiSet);
    }


    public List<String> layDanhSachTrangThaiDuy() throws SQLException {
        List<String> trangThaiList = new ArrayList<>();
        String sql = "SELECT DISTINCT trangThai FROM KhuyenMai";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                trangThaiList.add(rs.getString("trangThai"));
            }
        }
        return trangThaiList;
    }

    // Hàm lấy danh sách khuyến mãi
    public List<KhuyenMai> layDanhSachKhuyenMaiDuy2() throws SQLException {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String sql = "SELECT maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayBatDau, ngayKetThuc, " +
                     "trangThai, giaTriKhuyenMai, loaiKhuyenMai, diemYeuCau, soLuongToiThieu FROM KhuyenMai";

        try (Connection connection = ConnectJDBC.getConnection(); // Sử dụng kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maKhuyenMai = rs.getString("maKhuyenMai");
                String tenKhuyenMai = rs.getString("tenKhuyenMai");
                String moTaKhuyenMai = rs.getString("moTaKhuyenMai");
                LocalDateTime ngayBatDau = rs.getTimestamp("ngayBatDau").toLocalDateTime();
                LocalDateTime ngayKetThuc = rs.getTimestamp("ngayKetThuc").toLocalDateTime();
                String trangThai = rs.getString("trangThai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                double diemYeuCau = rs.getDouble("diemYeuCau");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");

                // Tạo đối tượng KhuyenMai và thêm vào danh sách
                KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, moTaKhuyenMai, ngayBatDau, 
                                             ngayKetThuc, trangThai, giaTriKhuyenMai, loaiKhuyenMai, 
                                             diemYeuCau, soLuongToiThieu, null);
                danhSachKhuyenMai.add(km);
            }
        }
        return danhSachKhuyenMai;
    }


   public void suaKhuyenMaiDuy(String maKhuyenMai, String tenKhuyenMai, String moTaKhuyenMai, 
                                LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, 
                                double giaTriKhuyenMai, double diemYeuCau, 
                                Integer soLuongToiThieu) throws SQLException {
       String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, moTaKhuyenMai = ?, ngayBatDau = ?, "
                  + "ngayKetThuc = ?, giaTriKhuyenMai = ?, diemYeuCau = ?, soLuongToiThieu = ? "
                  + "WHERE maKhuyenMai = ?";

       try (Connection connection = ConnectJDBC.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
           ps.setString(1, tenKhuyenMai);
           ps.setString(2, moTaKhuyenMai);
           ps.setTimestamp(3, Timestamp.valueOf(ngayBatDau)); // Thêm ngày bắt đầu
           ps.setTimestamp(4, Timestamp.valueOf(ngayKetThuc));
           ps.setDouble(5, giaTriKhuyenMai);
           ps.setDouble(6, diemYeuCau);
           ps.setInt(7, soLuongToiThieu);
           ps.setString(8, maKhuyenMai); // Cập nhật vị trí tham số cho mã khuyến mãi

           ps.executeUpdate();
       }
   }
}
