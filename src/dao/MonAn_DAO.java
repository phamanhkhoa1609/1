package dao;
import connectDB.ConnectJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modal.DanhMucMonAn;
import modal.KhuyenMai;
import modal.MonAn;
import modal.MonAnKhuyenMai;


import java.sql.Connection;
import java.sql.PreparedStatement;

import java.time.LocalDate;
import java.time.LocalDateTime;




public class MonAn_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
    
    //Lấy món ăn theo danh mục lên combobox
    public List<MonAn> layMonAnTheoTenDanhMuc1(String tenDanhMuc) throws SQLException{
        List<MonAn> dsMonAn = new ArrayList<>();
        String sql = "Select m.tenMonAn "
                      + "From MonAn m JOIN DanhMucMonAn dm ON m.maDanhMuc = dm.maDanhMuc "
                      + "Where dm.tenDanhMuc = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tenDanhMuc);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            MonAn monAn = new MonAn();
            monAn.setTenMonAn(rs.getString("tenMonAn"));
            dsMonAn.add(monAn);
        }
        return dsMonAn;
    }
    
    //Hàm lấy thông tin món ăn
    public MonAn layThongTinMonAn1(String tenMonAn) throws SQLException{
        String sql = "Select m.maMonAn, m.tenMonAn, m.giaMonAn, dm.tenDanhMuc "
                + "From MonAn m JOIN DanhMucMonAn dm ON m.maDanhMuc = dm.maDanhMuc "
                + "Where tenMonAn = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tenMonAn);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            MonAn mon = new MonAn();
            mon.setMaMonAn(rs.getString("maMonAn"));
            mon.setTenMonAn(rs.getString("tenMonAn"));
            mon.setGiaMonAn(rs.getDouble("giaMonAn"));
            
                DanhMucMonAn danhMuc = new DanhMucMonAn();
                danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
            mon.setDanhMucMonAn(danhMuc);
            
            return mon;

        } 
        return null;
    }
    
    //Hàm lấy thông tin món ăn theo mã -- 
    public MonAn layThongTinMonAnTheoMa(String maMon) throws SQLException{
        String sql = "Select maMonAn, tenMonAn, giaMonAn "
                + "From MonAn "
                + "Where maMonAn = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, maMon);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            MonAn mon = new MonAn();
            mon.setMaMonAn(rs.getString("maMonAn"));
            mon.setTenMonAn(rs.getString("tenMonAn"));
            mon.setGiaMonAn(rs.getDouble("giaMonAn"));
            return mon;
        } 
        return null;
    }

    
     public  String taoMaMonAn(String maDanhMuc) {
       // lay 2 số cuối cùng
        String maDanhMucCuoi = maDanhMuc.substring(maDanhMuc.length() - 2); 

        int soLonNhat = layMaMonAn(maDanhMucCuoi); 
        int soMoi = soLonNhat + 1;
        String maMonAnMoi = "MA" + maDanhMucCuoi + String.format("%04d", soMoi); 

        return maMonAnMoi;
    }
    
   public int layMaMonAn(String maDanhMucCuoi) {
        int maxNumber = 0;
        String query = "SELECT MAX(CAST(SUBSTRING(maMonAn, 6, 4) AS INT)) FROM MonAn WHERE maMonAn LIKE 'MA" + maDanhMucCuoi + "%'";

        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            if (rs.next()) {
                maxNumber = rs.getInt(1); 
                if (rs.wasNull()) {
                    maxNumber = 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxNumber;
}


        public void truSoLuongMonAn(String maMonAn, int soLuongGiam) throws SQLException {
            String sql = "UPDATE MonAn SET soLuong = soLuong - ? WHERE maMonAn = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, soLuongGiam);
            stmt.setString(2, maMonAn);
            stmt.executeUpdate();
        }


    
    public static List<MonAn> layTatCaMonAn(){
        String sql = "Select * from MonAn ";
        List<MonAn> tatCaMonAn = new ArrayList<>();
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
               Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ){
            while(rs.next()){
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                tatCaMonAn.add(monAn);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return tatCaMonAn; 
    }
    
    public static MonAn layMonAn(String tenMonAn){
        String sql = "Select * from MonAn where tenMonAn like('%" + tenMonAn + "%')";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
               Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ){
            while(rs.next()){
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                return monAn;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null; 
    }
    
    public static MonAn layMonAnByMa(String maMonAn){
        String sql = "Select * from MonAn where maMonAn like('%" + maMonAn + "%')";
        System.out.println(sql);
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
               Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ){
            while(rs.next()){
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                return monAn;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null; 
    }
   

    
public static List<MonAnKhuyenMai> layMonAnKhuyenMai() {
    String sql = "SELECT a.maMonAn, a.tenMonAn, a.giaMonAn, b.phanTramGiamGia, b.soLuongToiThieu, c.maKhuyenMai " +
                 "FROM MonAn a " +
                 "LEFT JOIN MonAnKhuyenMai b ON a.maMonAn = b.maMonAn " +
                 "LEFT JOIN KhuyenMai c ON b.maKhuyenMai = c.maKhuyenMai " +
                 "WHERE c.trangThai = 'Active' AND c.ngayKetThuc >= GETDATE()";
    
    List<MonAnKhuyenMai> danhSachMonAn = new ArrayList<>();
    
    try (Connection conn = connectDB.ConnectJDBC.getConnection();
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery(sql)) {
        
        while (rs.next()) {
            MonAn monAn = new MonAn();
            monAn.setMaMonAn(rs.getString("maMonAn"));
            monAn.setTenMonAn(rs.getString("tenMonAn"));
            monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
            
            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setMaKhuyenMai(rs.getString("maKhuyenMai"));
            
            double phanTramGiamGia = rs.getDouble("phanTramGiamGia");
            Integer soLuongToiThieu = rs.getInt("soLuongToiThieu");
             
            MonAnKhuyenMai monAnKhuyenMai = new MonAnKhuyenMai(soLuongToiThieu, phanTramGiamGia, monAn, khuyenMai);
            danhSachMonAn.add(monAnKhuyenMai);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return danhSachMonAn;
}




    
    public static List<MonAn> danhSachMonAnTheoDanhMuc(String maDanhMuc){
        String sql = "Select a.* from MonAn a join DanhMucMonAn b on a.maDanhMuc = b.maDanhMuc where b.maDanhMuc like('%" + maDanhMuc + "%') ";
        List<MonAn> danhSachMonAn = new ArrayList<MonAn>();
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql)) {
            while(rs.next()){
                DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
                danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
                
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                 monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                monAn.setDanhMucMonAn(danhMucMonAn);
                danhSachMonAn.add(monAn);
                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSachMonAn;
    }
    
     public static MonAn layMonAnTheoMa(String maMonAn){
        String sql = "Select a.* from MonAn a join DanhMucMonAn b on a.maDanhMuc = b.maDanhMuc where a.maMonAn like('%" + maMonAn + "%') ";
        List<MonAn> danhSachMonAn = new ArrayList<MonAn>();
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql)) {
            if(rs.next()){
                DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
                danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
                
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                 monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                monAn.setDanhMucMonAn(danhMucMonAn);
                return monAn;
                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public static List<MonAn> danhSachMonAnTheoVaDanhMuc(){
        String sql = "Select DISTINCT  a.*, b.maDanhMuc, b.tenDanhMuc from MonAn a join DanhMucMonAn b on a.maDanhMuc = b.maDanhMuc ";
        List<MonAn> danhSachMonAn = new ArrayList<MonAn>();
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql)) {
            while(rs.next()){
                DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
                danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
                danhMucMonAn.setTenDanhMuc(rs.getString("tenDanhMuc"));
                
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                 monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                monAn.setTinhTrangMon(rs.getBoolean("trangThai"));
                monAn.setDanhMucMonAn(danhMucMonAn);
                danhSachMonAn.add(monAn);
                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSachMonAn;
    }
   
        public static List<MonAn> timKiemMonAn(String tenMonAn) {
        // Chuẩn hóa tên món ăn trước khi đưa vào SQL

        String sql = "SELECT DISTINCT a.*, b.maDanhMuc, b.tenDanhMuc " +
                     "FROM MonAn a JOIN DanhMucMonAn b ON a.maDanhMuc = b.maDanhMuc " +
                     "WHERE a.tenMonAn LIKE '%" + tenMonAn + "%'";

        List<MonAn> danhSachMonAn = new ArrayList<>();
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
                danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
                danhMucMonAn.setTenDanhMuc(rs.getString("tenDanhMuc"));

                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
                monAn.setTinhTrangMon(rs.getBoolean("trangThai"));
                monAn.setDanhMucMonAn(danhMucMonAn);
                danhSachMonAn.add(monAn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSachMonAn;
    }
//    public static List<MonAn> timKiemMonAn(String tenMonAn) {
//    String sql = "SELECT DISTINCT a.*, b.maDanhMuc, b.tenDanhMuc " +
//                 "FROM MonAn a JOIN DanhMucMonAn b ON a.maDanhMuc = b.maDanhMuc " +
//                 "WHERE LOWER(REPLACE(REPLACE(REPLACE(a.tenMonAn, 'á', 'a'), 'à', 'a'), 'ã', 'a')) LIKE '%" + tenMonAn.toLowerCase() + "%'";
//
//    List<MonAn> danhSachMonAn = new ArrayList<MonAn>();
//    try (Connection conn = connectDB.ConnectJDBC.getConnection();
//         Statement stm = conn.createStatement();
//         ResultSet rs = stm.executeQuery(sql)) {
//        while (rs.next()) {
//            DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
//            danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
//            danhMucMonAn.setTenDanhMuc(rs.getString("tenDanhMuc"));
//
//            MonAn monAn = new MonAn();
//            monAn.setMaMonAn(rs.getString("maMonAn"));
//            monAn.setTenMonAn(rs.getString("tenMonAn"));
//            monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
//            monAn.setSoLuong(rs.getInt("soLuong"));
//            monAn.setDonVi(rs.getString("donVi"));
//            monAn.setAnhMonAn(rs.getString("anhSanPham"));
//            monAn.setGhiChu(rs.getString("ghiChu"));
//            monAn.setTinhTrangMon(rs.getBoolean("trangThai"));
//            monAn.setDanhMucMonAn(danhMucMonAn);
//            danhSachMonAn.add(monAn);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return danhSachMonAn;
//}


    public static List<MonAn> layMonAnTheoGia(String sortOrder) {
    String sql = "SELECT DISTINCT a.*, b.maDanhMuc, b.tenDanhMuc " +
                 "FROM MonAn a JOIN DanhMucMonAn b ON a.maDanhMuc = b.maDanhMuc " +
                 "ORDER BY a.giaMonAn " + sortOrder; // ASC hoặc DESC

    List<MonAn> danhSachMonAn = new ArrayList<>();
    try (Connection conn = connectDB.ConnectJDBC.getConnection();
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery(sql)) {
        while (rs.next()) {
            DanhMucMonAn danhMucMonAn = new DanhMucMonAn();
            danhMucMonAn.setMaDanhMuc(rs.getString("maDanhMuc"));
            danhMucMonAn.setTenDanhMuc(rs.getString("tenDanhMuc"));

            MonAn monAn = new MonAn();
            monAn.setMaMonAn(rs.getString("maMonAn"));
            monAn.setTenMonAn(rs.getString("tenMonAn"));
            monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
            monAn.setSoLuong(rs.getInt("soLuong"));
            monAn.setDonVi(rs.getString("donVi"));
            monAn.setAnhMonAn(rs.getString("anhSanPham"));
            monAn.setGhiChu(rs.getString("ghiChu"));
            monAn.setTinhTrangMon(rs.getBoolean("trangThai"));
            monAn.setDanhMucMonAn(danhMucMonAn);
            danhSachMonAn.add(monAn);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return danhSachMonAn;
}
    
   
    
    public  boolean themMonAn(MonAn monAn) {
        String maDanhMucCuoi = monAn.getDanhMucMonAn().getMaDanhMuc(); 

         // Gọi hàm phát sinh mã món ăn
         String maMonAnTuDong = taoMaMonAn(maDanhMucCuoi); 

         String sql = "INSERT INTO MonAn (maMonAn, tenMonAn, giaMonAn, soLuong, ghiChu, trangThai, donVi, maDanhMuc, anhSanPham) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

         try (Connection conn = connectDB.ConnectJDBC.getConnection()) {
             PreparedStatement pstmt = conn.prepareStatement(sql);

             // Set giá trị cho các tham số trong truy vấn
             pstmt.setString(1, maMonAnTuDong); // Sử dụng mã món ăn tự động
             pstmt.setString(2, monAn.getTenMonAn());
             pstmt.setDouble(3, monAn.getGiaMonAn());
             pstmt.setInt(4, monAn.getSoLuong());
             pstmt.setString(5, monAn.getGhiChu());
             pstmt.setBoolean(6, monAn.isTinhTrangMon());
             pstmt.setString(7, monAn.getDonVi());
             pstmt.setString(8, monAn.getDanhMucMonAn().getMaDanhMuc());
             pstmt.setString(9, monAn.getAnhMonAn()); // Gán đường dẫn ảnh

             int rowsAffected = pstmt.executeUpdate();
             return rowsAffected > 0; 

             } catch (SQLException e) {
                 e.printStackTrace();
                 return false;
             }
    }
    
    public static boolean xoaMonAn(String maMonAn) {
        String sql = "DELETE FROM MonAn WHERE maMonAn LIKE ?";
        PreparedStatement pstmt = null;
        boolean isDeleted = false;

        try (Connection conn = connectDB.ConnectJDBC.getConnection()) {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + maMonAn + "%"); // Sử dụng '%' trong giá trị để tương thích với LIKE

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    
         public static boolean capNhatMonAn(MonAn monAn) {
            String sql = "UPDATE MonAn SET tenMonAn = ?, giaMonAn = ?, soLuong = ?, donVi = ?, ghiChu = ?, anhSanPham = ?, trangThai = ?, maDanhMuc = ? WHERE maMonAn = ?";
            PreparedStatement pstmt = null;
            boolean isUpdated = false;

            try (Connection conn = connectDB.ConnectJDBC.getConnection()) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, monAn.getTenMonAn());
                pstmt.setDouble(2, monAn.getGiaMonAn());
                pstmt.setInt(3, monAn.getSoLuong());
                pstmt.setString(4, monAn.getDonVi());
                pstmt.setString(5, monAn.getGhiChu());
                pstmt.setString(6, monAn.getAnhMonAn());
                pstmt.setBoolean(7, monAn.isTinhTrangMon());

                if (monAn.getDanhMucMonAn() != null) {
                    // Kiểm tra xem maDanhMuc có tồn tại trong bảng DanhMucMonAn không
                    String maDanhMuc = monAn.getDanhMucMonAn().getMaDanhMuc();
                    if (checkMaDanhMucExists(maDanhMuc)) {
                        pstmt.setString(8, maDanhMuc);
                    } else {
                        // Ghi log hoặc thông báo lỗi nếu maDanhMuc không tồn tại
                        System.out.println("maDanhMuc không tồn tại: " + maDanhMuc);
                        return false; // Trả về false nếu không cập nhật
                    }
                } else {
                    pstmt.setString(8, null);
                } 

                pstmt.setString(9, monAn.getMaMonAn());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    isUpdated = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

         return isUpdated;
     }

     // Phương thức kiểm tra sự tồn tại của maDanhMuc
     private static boolean checkMaDanhMucExists(String maDanhMuc) {
         String sql = "SELECT COUNT(*) FROM DanhMucMonAn WHERE maDanhMuc = ?";
         try (Connection conn = connectDB.ConnectJDBC.getConnection(); 
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setString(1, maDanhMuc);
             ResultSet rs = pstmt.executeQuery();
             if (rs.next()) {
                 return rs.getInt(1) > 0; // Trả về true nếu có ít nhất 1 bản ghi
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return false; // Trả về false nếu có lỗi hoặc không tìm thấy
     }
     
     
     
     
     
    // Duyyyyyyyyyyyyyyyyyyyyyyyyyyyy:

    // Lấy món ăn theo mã món ăn
    public MonAn layMonAnTheoMaDuy(String maMonAn) throws SQLException {
        String sql = "SELECT * FROM MonAn WHERE maMonAn = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, maMonAn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new MonAn(
                    resultSet.getString("maMonAn"),
                    resultSet.getString("tenMonAn"),
                    resultSet.getDouble("giaMonAn"),
                    resultSet.getInt("soLuong"),
                    resultSet.getString("donVi"),
                    resultSet.getString("moTaSanPham"),
                    resultSet.getString("ghiChu"),
                    resultSet.getString("anhSanPham"),
                    resultSet.getBoolean("trangThai"),
                    null // Chỉnh sửa tại đây nếu cần lấy maDanhMuc
                );
            }
        }
        return null; // Nếu không tìm thấy
    }

    // Lấy tất cả món ăn
    public List<MonAn> layTatCaMonAnDuy() throws SQLException {
        List<MonAn> danhSachMonAn = new ArrayList<>();
        String sql = "SELECT * FROM MonAn"; // Lấy tất cả các món ăn

        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                MonAn monAn = new MonAn(
                    resultSet.getString("maMonAn"),
                    resultSet.getString("tenMonAn"),
                    resultSet.getDouble("giaMonAn"),
                    resultSet.getInt("soLuong"),
                    resultSet.getString("donVi"),
                    resultSet.getString("moTaSanPham"),
                    resultSet.getString("ghiChu"),
                    resultSet.getString("anhSanPham"),
                    resultSet.getBoolean("trangThai"),
                    null // Nếu cần, có thể lấy thêm thông tin danh mục
                );
                danhSachMonAn.add(monAn);
            }
        }
        return danhSachMonAn;
    }
    
    public List<MonAn> layMonAnTheoDanhMucDuy(String tenDanhMuc) throws SQLException {
    List<MonAn> danhSachMonAn = new ArrayList<>();
    String sql = "SELECT * FROM MonAn WHERE maDanhMuc = (SELECT maDanhMuc FROM DanhMucMonAn WHERE tenDanhMuc = ?)";

    try (Connection connection = ConnectJDBC.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, tenDanhMuc);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            MonAn monAn = new MonAn(
                resultSet.getString("maMonAn"),
                resultSet.getString("tenMonAn"),
                resultSet.getDouble("giaMonAn"),
                resultSet.getInt("soLuong"),
                resultSet.getString("donVi"),
                resultSet.getString("moTaSanPham"),
                resultSet.getString("ghiChu"),
                resultSet.getString("anhSanPham"),
                resultSet.getBoolean("trangThai"),
                null // Nếu cần, có thể lấy thêm thông tin danh mục
            );
            danhSachMonAn.add(monAn);
        }
    }
    return danhSachMonAn;
}

    // Lấy mã món ăn theo tên món ăn
    public String layMaMonAnTheoTenDuy(String tenMonAn) throws SQLException {
        String sql = "SELECT maMonAn FROM MonAn WHERE tenMonAn = ?";
        try (Connection connection = ConnectJDBC.getConnection(); // Lấy kết nối từ ConnectJDBC
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tenMonAn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maMonAn");
            }
        }
        return null; // Nếu không tìm thấy
    }
    
    // Phương thức để lấy mã món ăn dựa trên tên món ăn
    public String getMaMonAnDuy(String tenMonAn) throws SQLException {
        String maMonAn = null;
        String sql = "SELECT maMonAn FROM MonAn WHERE tenMonAn = ?";

        try (Connection connection = ConnectJDBC.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, tenMonAn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                maMonAn = rs.getString("maMonAn");
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy mã món ăn: " + e.getMessage());
        }
        return maMonAn;
    }
    
    // Duy __ kết thúc ////////////////////////////////////
    
     public List<MonAn> timKiemMonAn1(String tenMonAn){
        List<MonAn> dsMonAn = new ArrayList<>();
        String sql = "Select m.* "
                      + "From MonAn m JOIN DanhMucMonAn dm ON m.maDanhMuc = dm.maDanhMuc "
                      + "Where m.tenMonAn like(N'%" + tenMonAn + "%') ";
        System.out.println(sql);
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);) {
            while (rs.next()) {
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                monAn.setDonVi(rs.getString("donVi"));
                monAn.setAnhMonAn(rs.getString("anhSanPham"));
                monAn.setGhiChu(rs.getString("ghiChu"));
            dsMonAn.add(monAn);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsMonAn;
    }
    
   
    
/////////////
     // Phương thức cập nhật số lượng món ăn trong bảng MonAn
    public void updateSoLuongMonAn(String maMonAn, int soLuongMonAn) {
        String updateMonAnSQL = "UPDATE MonAn SET soLuong = soLuong - ? WHERE maMonAn = ?";
        
        try (Connection conn = ConnectJDBC.getConnection();
             PreparedStatement pst = conn.prepareStatement(updateMonAnSQL)) {
            
            pst.setInt(1, soLuongMonAn);
            pst.setString(2, maMonAn);
            pst.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//    public List<MonAn> layMonAnDaGoiTheoBan(String maBan) {
//        List<MonAn> gioHang = new ArrayList<>();
//        String sql = """
//            SELECT c.maMonAn, c.tenMonAn, c.giaMonAn 
//            FROM HoaDon a 
//            JOIN ChiTietHoaDon b ON a.maHoaDon = b.maHoaDon
//            JOIN MonAn c ON b.maMonAn = c.maMonAn
//            WHERE a.maBan = ?
//        """;
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, maBan);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    MonAn monAn = new MonAn();
//                    monAn.setMaMonAn(rs.getString("maMonAn"));
//                    monAn.setTenMonAn(rs.getString("tenMonAn"));
//                    monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
//                    gioHang.add(monAn);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách món ăn đã gọi: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return gioHang;
//    }
    
    public List<MonAn> layMonAnDaGoiTheoBan(String maBan) {
        List<MonAn> gioHang = new ArrayList<>();
        String sql = """
            SELECT c.maMonAn, c.tenMonAn, c.giaMonAn
            FROM HoaDon a 
            JOIN ChiTietHoaDon b ON a.maHoaDon = b.maHoaDon
            JOIN MonAn c ON b.maMonAn = c.maMonAn
            WHERE a.maBan = ? 
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maBan);  // Thiết lập maBan cho câu lệnh chính

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonAn monAn = new MonAn();
                    monAn.setMaMonAn(rs.getString("maMonAn"));
                    monAn.setTenMonAn(rs.getString("tenMonAn"));
                    monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                    gioHang.add(monAn);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách món ăn đã gọi: " + e.getMessage());
            e.printStackTrace();
        }
        return gioHang;
    }

    

    
    public static boolean capNhatAnhMonAn(String maMonAn, String duongDanAnh) {
        String sql = "UPDATE MonAn SET AnhSanPham = ? WHERE MaMonAn = ?";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, duongDanAnh);
            ps.setString(2, maMonAn);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
}


}
