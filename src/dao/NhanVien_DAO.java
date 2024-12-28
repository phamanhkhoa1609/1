package dao;

import connectDB.ConnectJDBC;
import java.util.ArrayList;
import java.util.List;
import modal.NhanVien;
import java.sql.*;
import java.text.SimpleDateFormat;
import modal.QuanLy;
import modal.TaiKhoan;
import java.util.Date;

public class NhanVien_DAO {
    //10/12
    Connection connection = connectDB.ConnectJDBC.getConnection();
    public String layMaNhanVienCuoi() {
    String maNhanVienCuoi = null;
    String sql = "SELECT MAX(maNhanVien) AS maNhanVienCuoi FROM NhanVien"; // Lấy mã nhân viên lớn nhất

    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            maNhanVienCuoi = rs.getString("maNhanVienCuoi");
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Ghi log hoặc xử lý lỗi
    }
    return maNhanVienCuoi; // Trả về mã nhân viên cuối cùng (hoặc null nếu không có nhân viên nào)
}
    // Lấy danh sách tất cả khách hàng
    public List<NhanVien>getAllNhanVien() {
       List<NhanVien> dsNhanVien = new ArrayList<NhanVien>();
        try {
            Connection con = ConnectJDBC.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE trangThai = 1";  // Lấy tất cả các nhân viên có trạng thái là 1
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maNhanVien = rs.getString("maNhanVien");
                String ho = rs.getString("ho");
                String ten = rs.getString("ten");
                Date ngaySinh = rs.getDate("ngaySinh");
                String gioiTinh = rs.getString("gioiTinh");
                String diaChi = rs.getString("diaChi");
                String soDienThoai = rs.getString("soDienThoai");
                String tenNguoiDung = rs.getString("tenNguoiDung");
                boolean trangThai = rs.getBoolean("trangThai");
                float luong = rs.getFloat("luong");
                String caLam = rs.getString("caLam");
                String maQuanLy = rs.getString("maQuanLy");
                // các đối tượng
                TaiKhoan taiKhoan =new TaiKhoan();
                taiKhoan.setTenNguoiDung(tenNguoiDung);
                QuanLy quanLy=new QuanLy();
                quanLy.setMaQuanLy(maQuanLy);
                
                // Tạo đối tượng NhanVien với các thông tin từ ResultSet
//                NhanVien nhanVien = new NhanVien(maNhanVien, ho, ten, ngaySinh, gioiTinh, diaChi, soDienThoai, trangThai, luong, caLam,taiKhoan,quanLy);
                 NhanVien nhanVien = new NhanVien(maNhanVien, ho, ten, ngaySinh, gioiTinh, soDienThoai, diaChi, luong, trangThai, caLam, taiKhoan, quanLy);
                
                dsNhanVien.add(nhanVien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsNhanVien;
    }
        // Thêm nhân viên
     public boolean themNhanVien(NhanVien nhanVien) {
         Connection con = ConnectJDBC.getConnection();
         int n = 0;
         try (PreparedStatement stmt = con.prepareStatement(
                 "INSERT INTO NhanVien(maNhanVien, ho, ten, ngaySinh, gioiTinh, diaChi, soDienThoai, tenNguoiDung, trangThai, luong, caLam, maQuanLy) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

             // Đặt giá trị cho các tham số trong câu lệnh SQL
             stmt.setString(1, nhanVien.getMaNhanVien());   // Mã nhân viên
             stmt.setString(2, nhanVien.getHo());           // Họ nhân viên
             stmt.setString(3, nhanVien.getTen());          // Tên nhân viên
             stmt.setDate(4, new java.sql.Date(nhanVien.getNgaySinh().getTime())); // Ngày sinh
             stmt.setString(5, nhanVien.getGioiTinh());     // Giới tính
             stmt.setString(6, nhanVien.getDiaChi());       // Địa chỉ
             stmt.setString(7, nhanVien.getSoDienThoai());  // Số điện thoại
             stmt.setString(8, null); // Tên người dùng
             stmt.setBoolean(9, true);    // Trạng thái (đang hoạt động hay không)
             stmt.setDouble(10, 0);       // Lương
             stmt.setString(11, "");       // Ca làm
             stmt.setString(12, null);    // Mã quản lý

             // Thực thi câu lệnh và lưu kết quả
             n = stmt.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return n > 0;
     }
     // Hàm cập nhật trạng thái nhân viên hoạt động hoặc không hoạt động
     public void capNhatTrangThaiNhanVien(String maNhanVien, boolean trangThai) throws SQLException {
         Connection con = ConnectJDBC.getConnection();
         String sql = "UPDATE NhanVien SET trangThai = ? WHERE maNhanVien = ?";
         PreparedStatement statement = con.prepareStatement(sql);
         statement.setBoolean(1, trangThai); // Cập nhật trạng thái (true = hoạt động, false = không hoạt động)
         statement.setString(2, maNhanVien); // Đặt mã nhân viên vào câu lệnh SQL

         // Thực thi câu lệnh cập nhật
         statement.executeUpdate();
     }
     // Sửa thông tin nhân viên
     public void SuaThongTinNhanVien(NhanVien nhanVien, String maNhanVien) throws SQLException {
         Connection con = ConnectJDBC.getConnection();
         String sql = "UPDATE NhanVien SET ten = ?, soDienThoai = ?, diaChi = ?, ngaySinh = ?, gioiTinh = ? WHERE maNhanVien = ?";
         PreparedStatement statement = con.prepareStatement(sql);
         statement.setString(1, nhanVien.getTen());
         statement.setString(2, nhanVien.getSoDienThoai());
         statement.setString(3, nhanVien.getDiaChi());
         statement.setDate(4, new java.sql.Date(nhanVien.getNgaySinh().getTime())); // Chuyển đổi Date từ Java sang SQL
         statement.setString(5, nhanVien.getGioiTinh());
         statement.setString(6, maNhanVien);

         statement.executeUpdate();
     }

     // Hàm tìm kiếm nhân viên trong DAO
     public List<NhanVien> timNhanVien(String maNhanVien, String tenNhanVien, String soDienThoai) throws SQLException {
         List<NhanVien> nhanViens = new ArrayList<>();
         Connection con = ConnectJDBC.getConnection();
         StringBuilder query = new StringBuilder("SELECT * FROM NhanVien WHERE trangThai = 1");

         // Thêm điều kiện tìm kiếm dựa trên các tiêu chí
         if (maNhanVien != null && !maNhanVien.isEmpty()) {
             query.append(" AND maNhanVien LIKE '%" + maNhanVien + "%'");
         }
         if (tenNhanVien != null && !tenNhanVien.isEmpty()) {
             query.append(" AND ten LIKE '%" + tenNhanVien + "%'");
         }
         if (soDienThoai != null && !soDienThoai.isEmpty()) {
             query.append(" AND soDienThoai LIKE '%" + soDienThoai + "%'");
         }

         // Thực hiện câu truy vấn
         Statement statement = con.createStatement();
         ResultSet rs = statement.executeQuery(query.toString());

         // Xử lý kết quả truy vấn
         while (rs.next()) {
             String maNV = rs.getString("maNhanVien");
             String tenNV = rs.getString("ten");
             String sdt = rs.getString("soDienThoai");
             String diaChi = rs.getString("diaChi");
             Date ngaySinh = rs.getDate("ngaySinh");
             String gioiTinh = rs.getString("gioiTinh");
             boolean trangThai = rs.getBoolean("trangThai");
             NhanVien nhanVien = new NhanVien(maNV);
             nhanVien.setTen(tenNV);
             nhanVien.setSoDienThoai(soDienThoai);
             nhanVien.setDiaChi(diaChi);
             nhanVien.setNgaySinh(ngaySinh);
             nhanVien.setGioiTinh(gioiTinh);
             nhanVien.setTrangThai(trangThai);
             nhanViens.add(nhanVien);
         }

         return nhanViens;
     }
     // Hàm kiểm tra mã nhân viên có tồn tại trong cơ sở dữ liệu hay không
     public boolean checkMaNhanVienDao(String maNhanVien) {
         // Kết nối cơ sở dữ liệu và thực hiện truy vấn
         String sql = "SELECT COUNT(*) FROM dbo.NhanVien WHERE MaNhanVien = ?";
         try (PreparedStatement stmt = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
             stmt.setString(1, maNhanVien);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) {
                 return rs.getInt(1) == 0; // Trả về true nếu không tồn tại
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return false; // Trả về false nếu có lỗi
     }
     
     // Hàm lấy thông tin nhân viên khi biết tên tài khoản - mới
//    public NhanVien layThongTinNhanVien(String tenNguoiDung) throws SQLException{
//        NhanVien nv = new NhanVien();
//        String sql = "Select maNhanVien from NhanVien Where tenNguoiDung = ?";
//        PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql);
//        ps.setString(1, tenNguoiDung);
//        ResultSet rs = ps.executeQuery();
//        if (rs.next()) {
//            nv.setMaNhanVien(rs.getString("maNhanVien"));
//            return nv;
//        }
//          return null;
//      }

    

    public NhanVien layTenNhanVien(String maNhanVien) {
        String sql = "Select * from NhanVien where maNhanVien like('" + maNhanVien + "') ";
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);){
            if(rs.next()){
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHo(rs.getString("ho"));
                nv.setTen(rs.getString("ten"));
                return nv;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
      // Hàm lấy thông tin nhân viên khi biết tên tài khoản - mới
    public NhanVien layThongTinNhanVien(String tenNguoiDung) throws SQLException{
        NhanVien nv = new NhanVien();
        String sql = "Select maNhanVien, ho, ten, ngaySinh, gioiTinh, soDienThoai, diaChi from NhanVien Where tenNguoiDung = ?";
        PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql);
        ps.setString(1, tenNguoiDung);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            nv.setMaNhanVien(rs.getString("maNhanVien"));
            nv.setHo(rs.getString("ho"));
            nv.setTen(rs.getString("ten"));
            nv.setNgaySinh(rs.getDate("ngaySinh"));
            nv.setGioiTinh(rs.getString("gioiTinh"));
            nv.setSoDienThoai(rs.getString("soDienThoai"));
            nv.setDiaChi(rs.getString("diaChi"));
            return nv;
        }
          return null;
      }
    
    public boolean suaThongTinNhanVien2(String tenNguoiDung, java.sql.Date ngaySinh, String diaChi, String soDienThoai) {
        String sql = "UPDATE NhanVien SET ngaySinh = ?, diaChi = ?, soDienThoai = ? WHERE tenNguoiDung = ?";
        try (PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngaySinh.getTime()));
            ps.setString(2, diaChi);
            ps.setString(3, soDienThoai);
            ps.setString(4, tenNguoiDung);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
