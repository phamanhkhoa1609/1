package dao;


import java.sql.SQLException;


import connectDB.ConnectJDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modal.ChiTietHoaDon;
import modal.MonAn;
import java.sql.PreparedStatement;
import modal.HoaDon;

public class ChiTietHoaDon_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
     private MonAn monAn;
     
    // Phương thức thêm chi tiết hóa đơn
    public boolean themChiTietHoaDon(ChiTietHoaDon chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, chiTiet.getHoaDon().getMaHoaDon());
            ps.setString(2, chiTiet.getMonAn().getMaMonAn());
            ps.setInt(3, chiTiet.getSoLuongMonAn());
            ps.setDouble(4, chiTiet.getThanhTien());
            
            int n = ps.executeUpdate();
            if (n > 0) {
                return true;
            }
        return false;
    }
    
    // Phương thức kiểm tra món ăn có tồn tại trong chi tiết hóa đơn--
    public boolean kiemTraMonAn(String maHoaDon, String maMonAn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ps.setString(2, maMonAn);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;  // Nếu count > 0 thì sản phẩm đã tồn tại
                }
            }
        }
        return false;
    }
    
    // Phương thức sửa chi tiết hóa đơn--
    public boolean suaChiTietHoaDon(String maHoaDon, String maMonAn, int soLuongMoi, double thanhTienMoi) throws SQLException {
        String sql = "UPDATE ChiTietHoaDon SET soLuongMonAn = ?, thanhTien = ? WHERE maHoaDon = ? AND maMonAn = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setDouble(2, thanhTienMoi);
            ps.setString(3, maHoaDon);
            ps.setString(4, maMonAn);

            int n = ps.executeUpdate();
            if (n > 0) {
                return true;
            }
        }
        return false;
    }
    
    //Lấy danh sách chi tiết hóa đơn theo mã hóa đơn --
    public List<ChiTietHoaDon> layTatCaChiTiet(String maHoaDon) throws SQLException {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, maHoaDon);

        ResultSet rs = ps.executeQuery();
        List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();

        while (rs.next()) {
            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
            MonAn monAn = new MonAn();
            monAn.setMaMonAn(rs.getString("maMonAn"));
            chiTiet.setMonAn(monAn);
            chiTiet.setHoaDon(new HoaDon(rs.getString("maHoaDon")));
            chiTiet.setSoLuongMonAn(rs.getInt("soLuongMonAn"));
            chiTiet.setThanhTien(rs.getDouble("thanhTien"));

            danhSachChiTiet.add(chiTiet);
        }
        return danhSachChiTiet;
    }
    
    //Xóa chi tiết hóa đơn
    public boolean xoaChiTietHoaDon(String maHoaDon, String maMonAn) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, maHoaDon);
        ps.setString(2, maMonAn);

        int n = ps.executeUpdate();
        if (n > 0) {
            return true;
        }
        return false;
    }

    //----------------------------------

    
//   public boolean themVaoHoaHoaCu(ChiTietHoaDon chiTiet) {
//        try {
//            String sql = "UPDATE ChiTietHoaDon SET soLuongMonAn = soLuongMonAn + ?, thanhTien = thanhTien + ? " +
//                         "WHERE maHoaDon = ? AND maMonAn = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setInt(1, chiTiet.getSoLuongMonAn());
//            ps.setDouble(2, chiTiet.getThanhTien());
//            ps.setString(3, chiTiet.getHoaDon().getMaHoaDon());
//            ps.setString(4, chiTiet.getMonAn().getMaMonAn());
//
//            int n = ps.executeUpdate();
//            if (n > 0) {
//                System.out.println("Cập nhật chi tiết hóa đơn thành công!");
//                return true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false; 
//    }
    
 
        public boolean themVaoHoaHoaCu(ChiTietHoaDon chiTiet) {
            try {
                // Kiểm tra xem đã có bản ghi nào với maHoaDon và maMonAn 
                String checkSql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                checkStmt.setString(1, chiTiet.getHoaDon().getMaHoaDon());
                checkStmt.setString(2, chiTiet.getMonAn().getMaMonAn());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu đã có, chỉ cần cập nhật số lượng
                    String updateSql = "UPDATE ChiTietHoaDon SET soLuongMonAn = soLuongMonAn + ?, thanhTien = thanhTien + ? " +
                                       "WHERE maHoaDon = ? AND maMonAn = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                    updateStmt.setInt(1, chiTiet.getSoLuongMonAn());
                    updateStmt.setDouble(2, chiTiet.getSoLuongMonAn() * chiTiet.getMonAn().getGiaMonAn()); 
                    updateStmt.setString(3, chiTiet.getHoaDon().getMaHoaDon());
                    updateStmt.setString(4, chiTiet.getMonAn().getMaMonAn());

                    int n = updateStmt.executeUpdate();
                    if (n > 0) {
                        System.out.println("Cập nhật chi tiết hóa đơn thành công!");
                        return true; // Đã cập nhật thành công
                    }
                } 

                // Nếu chưa có, chèn mới
                String insertSql = "INSERT INTO ChiTietHoaDon(maHoaDon, maMonAn, soLuongMonAn, thanhTien) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setString(1, chiTiet.getHoaDon().getMaHoaDon());
                insertStmt.setString(2, chiTiet.getMonAn().getMaMonAn());
                insertStmt.setInt(3, chiTiet.getSoLuongMonAn());
                insertStmt.setDouble(4, chiTiet.getSoLuongMonAn() * chiTiet.getMonAn().getGiaMonAn());

                int n = insertStmt.executeUpdate();
                if (n > 0) {
                    System.out.println("Thêm chi tiết hóa đơn thành công!");
                    return true; 
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; 
        }




    
        public boolean capNhatChiTietHoaDonCu(ChiTietHoaDon chiTiet)  {
            try {
                String sql = "UPDATE ChiTietHoaDon SET soLuongMonAn = ?, thanhTien = ? WHERE maHoaDon = ? AND maMonAn = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, chiTiet.getSoLuongMonAn());
            ps.setDouble(2, chiTiet.getThanhTien());
            ps.setString(3, chiTiet.getHoaDon().getMaHoaDon());
            ps.setString(4, chiTiet.getMonAn().getMaMonAn());

            int n = ps.executeUpdate();
            if (n > 0) {
                System.out.println("Cập nhật chi tiết hóa đơn thành công!");
                return true;
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
    }

    
    /////////////////////////////////////////////////////////////////
    
     public List<ChiTietHoaDon> getAllChiTietHoaDon(String maHoaDon) {
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();
       
        try {
            Connection con = ConnectJDBC.getConnection();


             String sql = "SELECT  hd.maHoaDon, hd.ngayGioLap, cthd.maMonAn, cthd.soLuongMonAn, cthd.thanhTien, " +
                 "mon.tenMonAn, mon.giaMonAn, mon.soLuong " + 
                 "FROM HoaDon hd " +
                 "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
                 "JOIN MonAn mon ON cthd.maMonAn = mon.maMonAn " + 
                 "WHERE hd.maHoaDon = ?"; 

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHoaDon);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
    
//                MonAnKhuyenMai monkm = new MonAnKhuyenMai();
//                monkm.setPhanTramGiamGia(rs.getDouble("phanTramGiamGia"));
               
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getString("maMonAn"));
                monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
                monAn.setTenMonAn(rs.getString("tenMonAn"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                
//                HoaDon hoaDon = new HoaDon();
//                hoaDon.setMaHoaDon(rs.getString("maHoaDon"));

                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                chiTietHoaDon.setSoLuongMonAn(rs.getInt("soLuongMonAn"));
                chiTietHoaDon.setMonAn(monAn);
                

                chiTietHoaDon.setThanhTien(rs.getDouble("thanhTien"));
                dsChiTietHoaDon.add(chiTietHoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChiTietHoaDon;
    }
     public void updateChiTietHoaDon(String maHoaDon, String maMonAn, int soLuongMonAn, double thanhTien) {
    Connection conn = null;
    PreparedStatement pst = null;
    
    try {
        conn = ConnectJDBC.getConnection();

        // Cập nhật số lượng món ăn và thành tiền trong bảng ChiTietHoaDon
        String updateChiTietHoaDonSQL = "UPDATE ChiTietHoaDon SET soLuongMonAn = ?, thanhTien = ? "
                + "WHERE maHoaDon = ? AND maMonAn = ?";
        pst = conn.prepareStatement(updateChiTietHoaDonSQL);
        pst.setInt(1, soLuongMonAn);
        pst.setDouble(2, thanhTien);
        pst.setString(3, maHoaDon);
        pst.setString(4, maMonAn);
        pst.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }  
}public void deleteChiTietHoaDon(String maHoaDon, String maMonAn) {
    Connection conn = null;
    PreparedStatement pst = null;

    try {
        conn = ConnectJDBC.getConnection();

        // Câu lệnh SQL để xóa chi tiết hóa đơn
        String deleteChiTietHoaDonSQL = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
        pst = conn.prepareStatement(deleteChiTietHoaDonSQL);
        pst.setString(1, maHoaDon);
        pst.setString(2, maMonAn);
        
        // Thực thi câu lệnh DELETE
        pst.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    } 
    
}
     
     /////////////////////////////////////////////////////////////////
     
     
     
     
     
     
     public ChiTietHoaDon timChiTietTheoHoaDonVaMonAn(HoaDon maHoaDon, MonAn maMonAn) throws SQLException {
             ChiTietHoaDon chiTietHoaDon = null;

            String sql = "SELECT a.*, b.*, c.* FROM HoaDon a join ChiTietHoaDon b  on a.maHoaDon = b.maHoaDon"
                    + " join MonAn c on b.maMonAn = c.maMonAn  WHERE b.maHoaDon = ? AND c.maMonAn = ?";

            try (Connection conn = connectDB.ConnectJDBC.getConnection(); 
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maHoaDon.getMaHoaDon());
                stmt.setString(2, maMonAn.getMaMonAn());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                    
                    MonAn monAn = new MonAn();
                    monAn.setMaMonAn(rs.getString("maMonAn"));
                    chiTietHoaDon = new ChiTietHoaDon();
                    chiTietHoaDon.setHoaDon(hoaDon);
                    chiTietHoaDon.setMonAn(monAn);
                    chiTietHoaDon.setSoLuongMonAn(rs.getInt("soLuongMonAn"));
                    chiTietHoaDon.setThanhTien(rs.getDouble("thanhTien"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Lỗi khi tìm chi tiết hóa đơn", e);
            }

            return chiTietHoaDon; //
     }
     
     public ChiTietHoaDon timChiTietTheoMaHoaDonVaMaMonAn(String maHoaDon, String maMonAn)  {
            ChiTietHoaDon chiTietHoaDon = null;
            Connection connection = connectDB.ConnectJDBC.getConnection();

            try {
                String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, maHoaDon); // Đặt giá trị cho mã hóa đơn
            statement.setString(2, maMonAn);  // Đặt giá trị cho mã món ăn

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
               HoaDon hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                    
                    MonAn monAn = new MonAn();
                    monAn.setMaMonAn(rs.getString("maMonAn"));
                    chiTietHoaDon = new ChiTietHoaDon();
                    chiTietHoaDon.setHoaDon(hoaDon);
                    chiTietHoaDon.setMonAn(monAn);
                    chiTietHoaDon.setSoLuongMonAn(rs.getInt("soLuongMonAn"));
                    chiTietHoaDon.setThanhTien(rs.getDouble("thanhTien"));
            }
             
         } catch (Exception e) {
             e.printStackTrace();
         }


            return chiTietHoaDon;
        }

      public List<ChiTietHoaDon> layMonAnBanChayTrongThang() {
    List<ChiTietHoaDon> monAns = new ArrayList<>();

    String query = "SELECT b.maMonAn, a.tenMonAn, a.giaMonAn, SUM(b.soLuongMonAn) AS soLuongBan " +
            "FROM MonAn a " +
            "JOIN ChiTietHoaDon b ON a.maMonAn = b.maMonAn " +
            "JOIN HoaDon ON HoaDon.maHoaDon = b.maHoaDon " +
            "WHERE HoaDon.ngayGioLap >= DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0) " +  // Lọc theo tháng hiện tại
            "AND HoaDon.ngayGioLap < DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) + 1, 0) " +  // Lọc theo tháng hiện tại
            "GROUP BY b.maMonAn, a.tenMonAn, a.giaMonAn " +  // Thêm tenMonAn vào GROUP BY
            "ORDER BY soLuongBan DESC";

    // Kết nối với cơ sở dữ liệu
    try (Connection conn = ConnectJDBC.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        // Thực thi truy vấn
        ResultSet rs = pstmt.executeQuery();

        // Duyệt kết quả và thêm vào danh sách
        while (rs.next()) {
            MonAn monAn = new MonAn();
            monAn.setMaMonAn(rs.getString("maMonAn"));
            monAn.setTenMonAn(rs.getString("tenMonAn"));
            monAn.setGiaMonAn(rs.getDouble("giaMonAn"));
            // Thêm số lượng bán vào đối tượng ChiTietHoaDon
            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setMonAn(monAn);
            cthd.setSoLuongMonAn(rs.getInt("soLuongBan"));  // Thiết lập số lượng cho ChiTietHoaDon

            monAns.add(cthd);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return monAns;
}

}
