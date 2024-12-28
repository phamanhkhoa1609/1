package dao;

import connectDB.ConnectJDBC;
import java.sql.Statement;
import modal.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modal.KhuyenMai;
import modal.KhachHang;

public class KhachHang_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();

    public KhachHang timKhachHangTheoSDT(String soDienThoai) throws SQLException{
        String sql = "Select maKhachHang, tenKhachHang, soDienThoai "
                + "FROM KhachHang "
                + "Where soDienThoai = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, soDienThoai);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            KhachHang kh = new KhachHang();
            kh.setMaKhachHang(rs.getString("maKhachHang")); // Gán mã khách hàng
            kh.setTenKhachHang(rs.getString("tenKhachHang")); // Gán tên khách hàng
            kh.setSoDienThoai(rs.getString("soDienThoai")); // Gán số điện thoại
            return kh;
        }

        return null; // Không tìm thấy khách hàng
    }
     //dhau 10/12
    public String layMaKhachHangCuoiDHAU() {
    String maKhachHangCuoi = null;
    String sql = "SELECT MAX(maKhachHang) FROM KhachHang";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            maKhachHangCuoi = rs.getString(1);
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Ghi log hoặc xử lý lỗi nếu cần thiết
    }
    return maKhachHangCuoi;
}
    // Lấy mã khách hàng cuối cùng từ cơ sở dữ liệu
    private String layMaKhachHangCuoi() throws SQLException {
        String maKhachHangCuoi = null;
        String sql = "SELECT MAX(maKhachHang) FROM KhachHang";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            maKhachHangCuoi = rs.getString(1);
        }
        return maKhachHangCuoi;
    }

    // Tạo mã khách hàng mới dựa trên mã cuối cùng từ cơ sở dữ liệu
    private String taoMaKhachHangMoi() throws SQLException {
        String maKhachHangCuoi = layMaKhachHangCuoi();
        String maKhachHangMoi;
        if (maKhachHangCuoi == null || maKhachHangCuoi.isEmpty()) {
            maKhachHangMoi = "KH001";
        } else {
            int soLuong = Integer.parseInt(maKhachHangCuoi.substring(2)) + 1; // Lấy số sau "KH"
            maKhachHangMoi = String.format("KH%03d", soLuong); // Tạo mã mới
        }
        return maKhachHangMoi;
    }

    // Phương thức thêm khách hàng mới
    public void themKhachHang(String tenKhachHang, String soDienThoai) throws SQLException {
        String sql = "INSERT INTO KhachHang VALUES (?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, taoMaKhachHangMoi());
        ps.setString(2, tenKhachHang);
        ps.setString(3, soDienThoai);
        ps.setInt(4, 0); // Điểm tích lũy khởi tạo bằng 0
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // Ngày hiện tại
        ps.setBoolean(6, true);
        
        ps.executeUpdate(); // Thực hiện thêm vào cơ sở dữ liệu
    }
 

    
    // Lấy danh sách tất cả khách hàng
    public List<KhachHang>getAllKhachHang() {
        List<KhachHang> dsKhachHang = new ArrayList<KhachHang>();
        try {
            Connection con = ConnectJDBC.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE trangThai = 1 AND tenKhachHang <> N'Khách ẩn danh' ";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String maKhachHang = rs.getString("maKhachHang");
                String tenKhachHang = rs.getString("tenKhachHang");
                String soDienThoai = rs.getString("soDienThoai");
                Integer diemTichLuy = rs.getInt("diemTichLuy");
                Date ngayThamGia = rs.getDate("ngayThamGia");
             
                boolean trangThai = rs.getBoolean("trangThai");

       
                                      
                KhachHang khachHang = new KhachHang(maKhachHang, tenKhachHang, soDienThoai, ngayThamGia, diemTichLuy, trangThai);
                dsKhachHang.add(khachHang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }
    
    
   // Thêm khách hàng
    public boolean themKhachHang(KhachHang khachHang) {
        Connection con = ConnectJDBC.getConnection();
        int n = 0;
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO KhachHang(maKhachHang, tenKhachHang, soDienThoai, diemTichLuy, ngayThamGia, trangThai) VALUES(?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, khachHang.getMaKhachHang());
            stmt.setString(2, khachHang.getTenKhachHang());
            stmt.setString(3, khachHang.getSoDienThoai());
            stmt.setInt(4, 0);
            stmt.setDate(5, new java.sql.Date(khachHang.getNgayThamGia().getTime()));
           
            stmt.setBoolean(6, true);

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
 
// hàm để cập nhật trạng thái khách hàng hoạt động hay không hoạt động
public void capNhatTrangThaiKhachHang(String maKhachHang, boolean trangThai) throws SQLException {
    Connection con = ConnectJDBC.getConnection();
    String sql = "UPDATE KhachHang SET trangThai = ? WHERE maKhachHang = ?";
    PreparedStatement statement = con.prepareStatement(sql);
    statement.setBoolean(1, trangThai);
    statement.setString(2, maKhachHang);

    statement.executeUpdate();
}

    // Sửa thông tin khách hàng
   public void SuaThongTinKhachHang(KhachHang khachHang, String maKhachHang) throws SQLException {
    Connection con = ConnectJDBC.getConnection();
    String sql = "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, diemTichLuy = ? WHERE maKhachHang = ?";
    PreparedStatement statement = con.prepareStatement(sql);
    statement.setString(1, khachHang.getTenKhachHang());
    statement.setString(2, khachHang.getSoDienThoai());
    statement.setInt(3, khachHang.getDiemTichLuy());
    statement.setString(4, maKhachHang);

    statement.executeUpdate();
}
   // hàm tìm khách hàng
    public List<KhachHang> timKhachHang(String query) throws SQLException {
        List<KhachHang> khachHangs = new ArrayList<>();
        Connection con = ConnectJDBC.getConnection();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            String maKhachHang = rs.getString("maKhachHang");
            String tenKhachHang = rs.getString("tenKhachHang");
            String soDienThoai = rs.getString("soDienThoai");
            Integer diemTichLuy = rs.getInt("diemTichLuy");
            Date ngayThamGia = rs.getDate("ngayThamGia");
          
            boolean trangThai = rs.getBoolean("trangThai");

          

            KhachHang khachHang = new KhachHang(maKhachHang, tenKhachHang, soDienThoai, ngayThamGia, diemTichLuy, trangThai);
            khachHangs.add(khachHang);
        }   
        return khachHangs;
}

    // Hàm kiểm tra mã nhân viên có tồn tại trong cơ sở dữ liệu hay không
    public boolean checkMaKhachHangDao(String maKhachHang) {
        // Kết nối cơ sở dữ liệu và thực hiện truy vấn
        String sql = "SELECT COUNT(*) FROM dbo.KhachHang WHERE maKhachHang = ?";
        try (PreparedStatement stmt = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
            stmt.setString(1, maKhachHang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Trả về true nếu không tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    // hàm cập nhật điểm tích lũy
     // Hàm cập nhật điểm tích lũy của khách hàng
      // Hàm cập nhật điểm tích lũy của khách hàng
    public void capNhatDiemTichLuy(String maKhachHang, int diemTichLuy) throws SQLException {
        Connection con = ConnectJDBC.getConnection();
        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE maKhachHang = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, diemTichLuy);
        statement.setString(2, maKhachHang);

        statement.executeUpdate();
    }
    
    
    
    // Duyyyyyyyyyyyyyyyyyyyyyyyyyyyy:
    
    
    
    // Hàm lấy thông tin khách hàng theo mã khách hàng
    public KhachHang layKhachHangTheoMaDuy(String maKhachHang) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, maKhachHang);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new KhachHang(
                    resultSet.getString("maKhachHang"),
                    resultSet.getString("tenKhachHang"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getDate("ngayThamGia"),
                    resultSet.getInt("diemTichLuy"),
                    null // Để trống maKhuyenMai nếu không cần thiết
                 
                );
            }
        }
        return null;
    }

    // Hàm lấy điểm tích lũy của khách hàng theo mã khách hàng
    public int layDiemTichLuyDuy(String maKhachHang) throws SQLException {
        String sql = "SELECT diemTichLuy FROM KhachHang WHERE maKhachHang = ?";
        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, maKhachHang);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("diemTichLuy");
            }
        }
        return 0; // Trả về 0 nếu không tìm thấy
    }
    
    
    
    // update lại cách reload bảng KhuyenMaiKhachHang
    public int getDiemTichLuyDuy(String maKhachHang) throws SQLException {
        String sql = "SELECT diemTichLuy FROM KhachHang WHERE maKhachHang = ?";
        try (Connection con = ConnectJDBC.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, maKhachHang);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("diemTichLuy");
            }
        }
        return 0; // Hoặc giá trị mặc định khác
    }


    
    // Thêm vào lớp KhachHang_DAO __ chưa dùng tới
    public List<KhachHang> layDanhSachKhachHangTheoDiemTichLuy(int diemYeuCau) throws SQLException {
        List<KhachHang> khachHangs = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE diemTichLuy >= ?";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, diemYeuCau);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                KhachHang khachHang = new KhachHang(
                    resultSet.getString("maKhachHang"),
                    resultSet.getString("tenKhachHang"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getDate("ngayThamGia"),
                    resultSet.getInt("diemTichLuy"),
                    null // Để trống maKhuyenMai nếu không cần thiết

                );
                khachHangs.add(khachHang);
            }
        }
        return khachHangs;
    }

    // Hàm để lấy điểm tích lũy của khách hàng
    public int layDiemTichLuyDuy2(String maKhachHang) throws SQLException {
        String sql = "SELECT diemTichLuy FROM KhachHang WHERE maKhachHang = ?";
        int diemTichLuy = 0;

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                diemTichLuy = rs.getInt("diemTichLuy");
            }
        }
        return diemTichLuy;
    }
    
    public List<KhachHang> layDanhSachKhachHangDuy() throws SQLException {
        List<KhachHang> khachHangs = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                KhachHang khachHang = new KhachHang(
                    resultSet.getString("maKhachHang"),
                    resultSet.getString("tenKhachHang"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getDate("ngayThamGia"),
                    resultSet.getInt("diemTichLuy"),
                    null // Để trống maKhuyenMai nếu không cần thiết
                  
                );
                khachHangs.add(khachHang);
            }
        }
        return khachHangs;
    }
    
    public KhachHang timKhachHangDuy(String soDienThoai) throws SQLException {
        String sql = "SELECT maKhachHang, tenKhachHang, soDienThoai, diemTichLuy "
                   + "FROM KhachHang "
                   + "WHERE soDienThoai = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang")); // Gán mã khách hàng
                kh.setTenKhachHang(rs.getString("tenKhachHang")); // Gán tên khách hàng
                kh.setSoDienThoai(rs.getString("soDienThoai")); // Gán số điện thoại
                kh.setDiemTichLuy(rs.getInt("diemTichLuy")); // Gán điểm tích lũy
                return kh;
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi truy vấn khách hàng: " + e.getMessage());
        }

        return null; // Không tìm thấy khách hàng
    }
    
     public String laySoDienThoaiKhachGB(String maBanChinh) {
        String soDienThoai = null;
        String sql = "SELECT kh.soDienThoai " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE hd.maBan = ? " ;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set giá trị cho tham số trong câu lệnh SQL
            stmt.setString(1, maBanChinh);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    soDienThoai = rs.getString("soDienThoai");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: có thể hiển thị thông báo lỗi cho người dùng nếu cần
        }
        return soDienThoai;
    }
    
}
