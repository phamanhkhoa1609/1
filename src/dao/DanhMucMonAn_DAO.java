package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modal.DanhMucMonAn;

public class DanhMucMonAn_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
    
     // Hàm lấy danh sách tất cả các Dnah mục từ cơ sở dữ liệu
    public List<DanhMucMonAn> layTatCaDanhMuc() throws SQLException{
        List<DanhMucMonAn> danhSachDanhMuc = new ArrayList<>();
        String sql = "SELECT * FROM DanhMucMonAn";// Câu truy vấn lấy tất cả dữ liệu từ bảng DanhMucMonAn
        
        PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();// Duyệt qua từng dòng kết quả từ ResultSet
            while (rs.next()) { 
                // Lấy dữ liệu từ các cột của bảng BanAn và gán cho các thuộc tính của đối tượng BanAn
                String maDanhMuc = rs.getString(1);
                String tenDanhMuc = rs.getString(2);
		
		DanhMucMonAn danhMuc = new DanhMucMonAn(maDanhMuc, tenDanhMuc);
                danhSachDanhMuc.add(danhMuc);
               }
        return danhSachDanhMuc;
    }
    
}
