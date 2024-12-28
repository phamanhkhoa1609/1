package dao;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modal.LoaiBan;

public class LoaiBan_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
    
    // Hàm lấy danh sách tất cả các tầng từ cơ sở dữ liệu
    public List<LoaiBan> layTatCaTenLoaiBan() throws SQLException {
        List<LoaiBan> danhSachLoaiBan = new ArrayList<>();
        String sql = "SELECT tenLoaiBan FROM LoaiBan";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LoaiBan lb = new LoaiBan();
            lb.setTenLoaiBan(rs.getString("tenLoaiBan"));
            
            danhSachLoaiBan.add(lb);
        }
        return danhSachLoaiBan;
    }
    
        // Hàm lấy danh sách tất cả tên loại bàn từ cơ sở dữ liệu
    public String[] layTatCaTenLoaiBan2() throws SQLException {
        List<String> danhSachLoaiBan = new ArrayList<>();
        String sql = "SELECT DISTINCT tenLoaiBan FROM LoaiBan"; // Câu truy vấn lấy các tên loại bàn không trùng lặp
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            danhSachLoaiBan.add(rs.getString("tenLoaiBan")); // Thêm tên loại bàn vào danh sách
        }
        // Chuyển danh sách thành mảng
        return danhSachLoaiBan.toArray(new String[0]);
    }

}
