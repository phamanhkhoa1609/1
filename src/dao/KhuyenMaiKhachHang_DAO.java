/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectJDBC;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import modal.KhuyenMaiKhachHang;
import modal.KhachHang;
import modal.KhuyenMai;


/**
 *
 * @author ADMIN
 */
public class KhuyenMaiKhachHang_DAO {
    
    // Lấy danh sách khuyến mãi của khách hàng theo mã khách hàng
public List<KhuyenMaiKhachHang> getKhuyenMaiByMaKhachHang(String maKhachHang) {
   List<KhuyenMaiKhachHang> dsKhuyenMaiKhachHang = new ArrayList<>();

    
//    String sql = "SELECT a.*, c.* , c.trangThai as trangthaiC FROM KhachHang a join KhuyenMaiKhachHang b on a.maKhachHang = b.maKhachHang"
//            + " join KhuyenMai c on b.maKhuyenMai = c.maKhuyenMai WHERE a.maKhachHang = ?";
 String sql = "SELECT a.*, c.*, c.trangThai as trangthaiC " +
                 "FROM KhachHang a " +
                 "JOIN KhuyenMaiKhachHang b ON a.maKhachHang = b.maKhachHang " +
                 "JOIN KhuyenMai c ON b.maKhuyenMai = c.maKhuyenMai " +
                 "WHERE a.maKhachHang = ?";
    try (Connection con = ConnectJDBC.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        pstmt.setString(1, maKhachHang);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String maKhuyenMai = rs.getString("maKhuyenMai");
                
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(maKhachHang);
                
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(maKhuyenMai);
                km.setMoTaKhuyenMai(rs.getString("moTaKhuyenMai"));
                km.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                km.setGiaTriKhuyenMai(rs.getDouble("giaTriKhuyenMai"));
                km.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
                km.setDiemYeuCau(rs.getInt("diemYeuCau"));
                km.setNgayBatDau(rs.getTimestamp("ngayBatDau").toLocalDateTime());
                km.setNgayKetThuc(rs.getTimestamp("ngayKetThuc").toLocalDateTime());
                 //11/12
                km.setTrangThai(rs.getString("trangThaiC"));
                //
                KhuyenMaiKhachHang kmkh = new KhuyenMaiKhachHang(kh, km);
                dsKhuyenMaiKhachHang.add(kmkh);
                
            }
        }
    } catch (SQLException e) {
        // Xử lý ngoại lệ (có thể ghi log hoặc thông báo cho người dùng)
        System.err.println("Lỗi khi lấy khuyến mãi theo mã khách hàng: " + e.getMessage());
    }
    System.out.println(dsKhuyenMaiKhachHang.size());
    
    return dsKhuyenMaiKhachHang;
}
 private List<KhuyenMai> getKhuyenMaiByLoai(String loaiKhuyenMai) {
    List<KhuyenMai> dsKhuyenMai = new ArrayList<>();
    
    // Kết nối và truy vấn cơ sở dữ liệu để lấy khuyến mãi theo loại
    String sql = "SELECT * FROM KhuyenMai WHERE loaiKhuyenMai = ?";
    try (Connection con = ConnectJDBC.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, loaiKhuyenMai);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String maKhuyenMai = rs.getString("maKhuyenMai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                // Khởi tạo và thêm vào danh sách
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(maKhuyenMai);
                km.setGiaTriKhuyenMai(giaTriKhuyenMai);
               
                dsKhuyenMai.add(km);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return dsKhuyenMai;
}  
 
 
 
 
    // Duyyyyyyyyyyyyyyyyyyyyyyyyyyyy:
 
    
    // Hàm lấy danh sách khách hàng theo mã khuyến mãi
    public List<String> layDanhSachKhachHangTheoKhuyenMaiDuy(String maKhuyenMai) throws SQLException {
        List<String> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT maKhachHang FROM KhuyenMaiKhachHang WHERE maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                danhSachKhachHang.add(resultSet.getString("maKhachHang"));
            }
        }
        return danhSachKhachHang;
    }
 
 
    // Phương thức xóa mã khuyến mãi hết hạn
    public void xoaKhuyenMaiHetHan() throws SQLException {
        String sql = "DELETE FROM KhuyenMaiKhachHang WHERE maKhuyenMai IN (SELECT maKhuyenMai FROM KhuyenMai WHERE tinhTrang = 'Expired')";
        try (Connection con = ConnectJDBC.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khuyến mãi hết hạn: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
        }
    }
    
    
    
    // update lại cách reload bảng KhuyenMaiKhachHang
    // Hàm kiểm tra xem khách hàng đã nhận khuyến mãi chưa
    public boolean checkKhuyenMaiKhachHang(String maKhachHang, String maKhuyenMai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhuyenMaiKhachHang WHERE maKhachHang = ? AND maKhuyenMai = ?";
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, maKhachHang);
            statement.setString(2, maKhuyenMai);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Nếu có ít nhất 1 bản ghi, trả về true
            }
        }
        return false; // Nếu không có bản ghi nào, trả về false
    }


    
    // Phương thức để thêm khuyến mãi cho khách hàng
    public void themKhuyenMaiKhachHangDuy(String maKhachHang, String maKhuyenMai) throws SQLException {
        String sql = "INSERT INTO KhuyenMaiKhachHang (maKhachHang, maKhuyenMai) VALUES (?, ?)";

        try (Connection connection = ConnectJDBC.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);
            ps.setString(2, maKhuyenMai);
            ps.executeUpdate(); // Thực thi câu lệnh SQL
        } catch (SQLException e) {
            // Kiểm tra lỗi trùng lặp
            if (e.getMessage().contains("Violation of PRIMARY KEY constraint")) {
                // Bỏ qua lỗi trùng lặp mà không hiển thị cho người dùng
                return;
            } else {
                throw new SQLException("Lỗi khi thêm khuyến mãi cho khách hàng: " + e.getMessage());
            }
        }
    }


    public void xoaTatCaKhuyenMaiKhachHangDuy() throws SQLException {
        String sql = "DELETE FROM KhuyenMaiKhachHang"; // Thay đổi tên bảng nếu cần
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
    
    
}
