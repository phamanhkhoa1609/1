package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modal.QuanLy;

public class QuanLy_DAO {
// Hàm lấy thông tin Quản lý khi biết tên tài khoản - mới
    public QuanLy layThongTinQuanLy(String tenNguoiDung) throws SQLException{
        QuanLy ql = new QuanLy();
        String sql = "Select maQuanLy, ho, ten, ngaySinh, gioiTinh, email, soDienThoai, diaChi from QuanLy Where tenNguoiDung = ?";
        PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql);
        ps.setString(1, tenNguoiDung);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ql.setMaQuanLy(rs.getString("maQuanLy"));
            ql.setHo(rs.getString("ho"));
            ql.setTen(rs.getString("ten"));
            ql.setNgaySinh(rs.getDate("ngaySinh"));
            ql.setGioiTinh(rs.getString("gioiTinh"));
            ql.setEmail(rs.getString("email"));
            ql.setSoDienThoai(rs.getString("soDienThoai"));
            ql.setDiaChi(rs.getString("diaChi"));
            return ql;
        }
          return null;
      }
    
    public boolean suaThongTinQuanLy(String tenNguoiDung, Date ngaySinh, String diaChi, String email, String soDienThoai) {
        String sql = "UPDATE QuanLy SET ngaySinh = ?, diaChi = ?, email = ?, soDienThoai = ? WHERE tenNguoiDung = ?";
        try (PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngaySinh.getTime()));
            ps.setString(2, diaChi);
            ps.setString(3, email);
            ps.setString(4, soDienThoai);
            ps.setString(5, tenNguoiDung);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
