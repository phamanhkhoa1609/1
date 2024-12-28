package dao;

import connectDB.ConnectJDBC;
import java.util.ArrayList;
import java.util.List;
import modal.MonAn;
import modal.MonAnKhuyenMai;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modal.DanhMucMonAn;
import modal.KhuyenMai;


public class MonAnKhuyenMai_DAO {
    public List<MonAnKhuyenMai> getAllMonAnKhuyenMai() {
    List<MonAnKhuyenMai> dsMonAnKhuyenMai = new ArrayList<>();

    try {
        Connection con = ConnectJDBC.getConnection();

       // Câu truy vấn SQL JOIN bảng MonAn và MonAnKhuyenMai
       String sql = "SELECT mon.maMonAn, mon.tenMonAn, mon.giaMonAn, " +
             "monkm.phanTramGiamGia, monkm.soLuongToiThieu, " +
             "km.tenKhuyenMai, km.ngayBatDau, km.ngayKetThuc " +
             "FROM MonAn mon " +
             "JOIN MonAnKhuyenMai monkm ON mon.maMonAn = monkm.maMonAn " +
             "JOIN KhuyenMai km ON monkm.maKhuyenMai = km.maKhuyenMai " +
             "WHERE km.trangThai = 'Active' AND km.ngayKetThuc >= GETDATE()";

        PreparedStatement statement = con.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            // Lấy dữ liệu từ kết quả truy vấn
            MonAn monAn = new MonAn();
            monAn.setMaMonAn(rs.getString("maMonAn"));
            monAn.setTenMonAn(rs.getString("tenMonAn"));
            monAn.setGiaMonAn(rs.getDouble("giaMonAn"));

            MonAnKhuyenMai monAnKhuyenMai = new MonAnKhuyenMai();
            monAnKhuyenMai.setMonAn(monAn);
            monAnKhuyenMai.setPhanTramGiamGia(rs.getDouble("phanTramGiamGia"));
            monAnKhuyenMai.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));

            dsMonAnKhuyenMai.add(monAnKhuyenMai);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsMonAnKhuyenMai;
}
    
    
    
    // Duyyyyyyyyyyyyyyyyyyyyyyyyyyyy:

    // Kiểm tra xem mã khuyến mãi có trong bảng MonAnKhuyenMai không
    public boolean checkKhuyenMaiMonAnDuy(String maKhuyenMai) throws SQLException {
        String sql = "SELECT * FROM MonAnKhuyenMai WHERE maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Nếu có dòng nào trả về thì mã khuyến mãi tồn tại
        }
    }

    // Lấy danh sách món ăn theo mã khuyến mãi
    public List<MonAn> layMonAnTheoKhuyenMaiDuy(String maKhuyenMai) throws SQLException {
        List<MonAn> danhSachMonAn = new ArrayList<>();
        String sql = "SELECT MonAn.maMonAn, MonAn.tenMonAn, MonAn.giaMonAn, MonAn.soLuong, MonAn.donVi, " +
                     "MonAn.moTaSanPham, MonAn.ghiChu, MonAn.anhSanPham, MonAn.trangThai " +
                     "FROM MonAn " +
                     "JOIN MonAnKhuyenMai ON MonAn.maMonAn = MonAnKhuyenMai.maMonAn " +
                     "WHERE MonAnKhuyenMai.maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String maMonAn = resultSet.getString("maMonAn");
                String tenMonAn = resultSet.getString("tenMonAn");
                double giaMonAn = resultSet.getDouble("giaMonAn");
                int soLuong = resultSet.getInt("soLuong");
                String donVi = resultSet.getString("donVi");
                String moTaMonAn = resultSet.getString("moTaSanPham");
                String ghiChu = resultSet.getString("ghiChu");
                String anhSanPham = resultSet.getString("anhSanPham");
                boolean trangThai = resultSet.getBoolean("trangThai");
                DanhMucMonAn danhMucMonAn = null; // Cần lấy thông tin danh mục nếu cần

                // Tạo đối tượng MonAn với tất cả các thuộc tính
                MonAn monAn = new MonAn(maMonAn, tenMonAn, giaMonAn, soLuong, donVi, moTaMonAn, ghiChu, anhSanPham, trangThai, danhMucMonAn);
                danhSachMonAn.add(monAn);
            }
        }
        return danhSachMonAn;
    }

    // Thêm món ăn vào khuyến mãi
    public void themMonAnKhuyenMaiDuy(String maKhuyenMai, String maMonAn, double phanTramGiamGia, int soLuongToiThieu) throws SQLException {
        String sql = "INSERT INTO MonAnKhuyenMai (maKhuyenMai, maMonAn, phanTramGiamGia, soLuongToiThieu) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            ps.setString(2, maMonAn);
            ps.setDouble(3, phanTramGiamGia);
            ps.setInt(4, soLuongToiThieu);
            ps.executeUpdate();
        }
    }
    
    
    // Phương thức để thêm món ăn vào khuyến mãi ___ cho button nút thêm
    public void themMonAnKhuyenMaiDuy2(String maMonAn, String maKhuyenMai) throws SQLException {
        String sql = "INSERT INTO MonAnKhuyenMai (maKhuyenMai, maMonAn) VALUES (?, ?)";
        
        try (Connection connection = ConnectJDBC.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, maKhuyenMai);
            ps.setString(2, maMonAn);
            ps.executeUpdate(); // Thực thi câu lệnh SQL
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi thêm món ăn vào khuyến mãi: " + e.getMessage());
        }
    }
    
    public void updateMonAnKhuyenMaiSuaDuy(String maKhuyenMai, float phanTramGiamGia, int soLuongToiThieu) throws SQLException {
        String sql = "UPDATE MonAnKhuyenMai SET phanTramGiamGia = ?, soLuongToiThieu = ? WHERE maKhuyenMai = ?";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, phanTramGiamGia);
            ps.setInt(2, soLuongToiThieu);
            ps.setString(3, maKhuyenMai);

            ps.executeUpdate();
        }
    }
    
    // Duy __ kết thúc ////////////////////////////////////
    
    public MonAnKhuyenMai layMonAnKhuyenMaiBangMa(String maMonAn) {
        String sql = "SELECT * FROM MonAnKhuyenMai WHERE maMonAn = ?";
        try (Connection connection = connectDB.ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, maMonAn);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    MonAnKhuyenMai monAnKhuyenMai = new MonAnKhuyenMai();
                    monAnKhuyenMai.setMonAn(new MonAn(rs.getString("maMonAn")));
                    monAnKhuyenMai.setKhuyenMai(new KhuyenMai(rs.getString("maKhuyenMai")));
                    monAnKhuyenMai.setPhanTramGiamGia(rs.getDouble("phanTramGiamGia"));
                    monAnKhuyenMai.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
                    return monAnKhuyenMai;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
