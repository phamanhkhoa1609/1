package dao;
import connectDB.ConnectJDBC;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modal.TaiKhoan;

public class TaiKhoan_DAO {
            public List<TaiKhoan> getAllTaiKhoan() {
    List<TaiKhoan> dsTaiKhoan = new ArrayList<TaiKhoan>();
    try {
        Connection con = ConnectJDBC.getConnection();
        String sql = "SELECT * FROM TaiKhoan"; // Bạn có thể thêm điều kiện WHERE nếu cần
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);

        while (rs.next()) {
            String tenNguoiDung = rs.getString("tenNguoiDung");
            String matKhau = rs.getString("matKhau");
            String vaiTro = rs.getString("vaiTro");
            Date ngayThamGia = rs.getDate("ngayThamGia");

            TaiKhoan taiKhoan = new TaiKhoan(tenNguoiDung, matKhau, vaiTro, ngayThamGia);
            dsTaiKhoan.add(taiKhoan);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsTaiKhoan;
    }
            
    //Phương thức đổi mật khẩu
    public boolean capNhatMatKhau(String tenNguoiDung, String matKhauMoi) throws Exception{
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenNguoiDung = ?";
        try (PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setString(2, tenNguoiDung);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
        }
        return false;
    }
}
