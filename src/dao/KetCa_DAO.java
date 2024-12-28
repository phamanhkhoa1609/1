/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;  // Sửa dòng này
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author ACER
 */
public class KetCa_DAO {
    
        // ket ca
    
    public Double getSoDuDauCa(String maNhanVien) {
       String sql = "SELECT a.maNhanVien, b.soDuDauCa " +
                    "FROM NhanVien a " +
                    "JOIN KetCa b ON a.maNhanVien = b.maNhanVien " +
                    "WHERE a.maNhanVien = ? AND b.ngayGioKetThuc IS NULL";
       try (Connection conn = connectDB.ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setString(1, maNhanVien);
           ResultSet rs = stmt.executeQuery();
           if (rs.next()) {
               return rs.getDouble("soDuDauCa");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return 0.0; // Nếu không tìm thấy, trả về 0
   }

    
    public float getTongDoanhThu(String maNhanVien, Date startDate, Date endDate) {
        String sql = "SELECT SUM(tongTien) AS tongDoanhThu " +
                     "FROM HoaDon " +
                     "WHERE maNhanVien = ? AND trangThaiThanhToan = 'Đã thanh toán' " +
                     "AND ngayGioLap BETWEEN ? AND ?";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNhanVien);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("tongDoanhThu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }

    public String generateMaKetCa() {
        String prefix = "KC-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-";
        String sql = "SELECT COUNT(*) + 1 AS nextId FROM KetCa WHERE maKetCa LIKE ?";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return prefix + String.format("%03d", rs.getInt("nextId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + "001"; 
    }

    public void insertKetCa(String maKetCa, String maNhanVien, Date ngayGioKetThuc, float soDuTienMat, float chenhlech, String ghiChu) {
        String sql = "INSERT INTO KetCa (maKetCa, maNhanVien, ngayGioKetThuc, soDuTienMat, tongDoanhThu, ghiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKetCa);
            stmt.setString(2, maNhanVien);
            stmt.setTimestamp(3, new java.sql.Timestamp(ngayGioKetThuc.getTime()));
            stmt.setFloat(4, soDuTienMat);
            stmt.setFloat(5, chenhlech);
            stmt.setString(6, ghiChu);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getNextMaKetCa() throws SQLException {
        String prefix = "KC-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        String sql = "SELECT COUNT(*) + 1 AS nextId FROM KetCa WHERE maKetCa LIKE ?";
        try (PreparedStatement ps = connectDB.ConnectJDBC.getConnection().prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt("nextId");
                return prefix + String.format("%03d", nextId); // Tạo mã KC-yyyyMMdd-XXX
            }
            return prefix + "001"; // Trường hợp không có mã nào trong ngày
        }
    }
    
    public double getDoanhThuCuaNhanVienTrongCa(String maNhanVien, String maKetCa) throws SQLException {
        String sql = "SELECT SUM(hd.tongTien) AS doanhThu " +
                     "FROM HoaDon hd " +
                     "JOIN NhanVien nv on  hd.maNhanVien = nv.maNhanVien "
                   + " join KetCa kc ON nv.maNhanVien = kc.maNhanVien " +
                     "WHERE kc.maNhanVien = ? " +
                     "AND kc.maKetCa = ? " +
                     "AND hd.ngayGioLap BETWEEN kc.ngayGioBatDau AND kc.ngayGioKetThuc " +
                     "AND hd.trangThaiThanhToan = N'Đã thanh toán'";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNhanVien);
            stmt.setString(2, maKetCa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("doanhThu");
                }
            }
        }
        return 0.0; 
    }

    
    public double getTongDoanhThu(String maNhanVien) {
        // Lấy ngày hiện tại
        LocalDate ngayHienTai = LocalDate.now();
        String sql = "SELECT SUM(hd.tongTien) AS tongDoanhThu " +
                     "FROM HoaDon hd " +
                     "WHERE hd.maNhanVien = ? " +
                     "AND hd.trangThaiThanhToan = N'Đã thanh toán' " +
                     "AND CAST(hd.ngayGioLap AS DATE)  = CAST(GETDATE() AS DATE)"; 

        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNhanVien);
    //        stmt.setDate(2, java.sql.Date.valueOf(ngayHienTai)); // Chuyển LocalDate thành Date

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongDoanhThu");
                }
                System.out.println("khong co");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("khong co1");
        return 0.0; 
    }






//   public void saveShiftData(String maKetCa, String maNhanVien, LocalDateTime ngayGioBatDau, LocalDateTime ngayGioKetThuc) throws SQLException {
//    String sql = "INSERT INTO KetCa (maKetCa, maNhanVien, ngayGioBatDau, ngayGioKetThuc) VALUES (?, ?, ?, ?)";
//    try (Connection conn = connectDB.ConnectJDBC.getConnection();
//         PreparedStatement ps = conn.prepareStatement(sql)) {
//        ps.setString(1, maKetCa);
//        ps.setString(2, maNhanVien);
//        ps.setTimestamp(3, Timestamp.valueOf(ngayGioBatDau));
//
//        if (ngayGioKetThuc != null) {
//            ps.setTimestamp(4, Timestamp.valueOf(ngayGioKetThuc));
//        } else {
//            ps.setNull(4, Types.TIMESTAMP); // Nếu chưa kết thúc ca, để trường này là NULL
//        }
//
//        ps.executeUpdate(); 
//    }
//}
    
    
   public void saveShiftData(String role, String maNguoiDung, String maKetCa, LocalDateTime ngayGioBatDau, LocalDateTime ngayGioKetThuc) throws SQLException {
        // Kiểm tra vai trò và xác định cột cần lưu dữ liệu
        String column;
        String checkSql;
        if ("NhanVien".equalsIgnoreCase(role)) {
            column = "maNhanVien";
            checkSql = "SELECT COUNT(*) FROM NhanVien WHERE maNhanVien = ?";
            System.out.println("day ni nv" + maNguoiDung);
        } else if ("QuanLy".equalsIgnoreCase(role)) {
            column = "maQuanLy";
            checkSql = "SELECT COUNT(*) FROM QuanLy WHERE maQuanLy = ?";
             System.out.println("day ni ql" + maNguoiDung);
        } else {
            throw new IllegalArgumentException("Vai trò không hợp lệ: " + role);
        }

        // Kiểm tra tồn tại của mã người dùng trong bảng tương ứng
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, maNguoiDung);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next() || rs.getInt(1) == 0) {
                    System.out.println("Cảnh báo: Không tìm thấy mã người dùng: " + maNguoiDung);
                    return; // Dừng thực thi nếu không tìm thấy mã người dùng
                }
            }
        }

        // Chèn dữ liệu vào bảng KetCa
        String sql = "INSERT INTO KetCa (maKetCa, " + column + ", ngayGioBatDau, ngayGioKetThuc) VALUES (?, ?, ?, ?)";
        System.out.println("day ni:" + sql);
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKetCa); // Mã kết ca
            ps.setString(2, maNguoiDung); // Mã nhân viên hoặc mã quản lý
            ps.setTimestamp(3, Timestamp.valueOf(ngayGioBatDau)); // Thời gian bắt đầu ca

            // Nếu chưa kết thúc ca, để NULL
            if (ngayGioKetThuc != null) {
                ps.setTimestamp(4, Timestamp.valueOf(ngayGioKetThuc));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.executeUpdate(); // Thực thi câu lệnh SQL
            System.out.println("Dữ liệu đã được lưu thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại lỗi để xử lý ở cấp cao hơn
        }
    }





   public boolean isNewDay(LocalDate currentDate) {
        String sql = "SELECT COUNT(*) FROM KetCa WHERE CONVERT(DATE, ngayGioKetThuc) = ?";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(currentDate));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Nếu không có ca nào, tức là ngày mới
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   public double getSoDuCuoiCaGanNhat() {
        String sql = "SELECT TOP 1 soDuTienMat FROM KetCa ORDER BY ngayGioKetThuc DESC";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("soDuTienMat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1000000.0; 
    }


    public double getSoDuCuoiCa(String maNhanVien) {
        String sql = "SELECT TOP 1 soDuTienMat FROM KetCa WHERE maNhanVien = ? ORDER BY ngayGioKetThuc DESC";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("soDuTienMat");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; 
    }


   
   public boolean saveKetCa(String maKetCa, String maNhanVien, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, double soDuDauCa, double soDuTienMat, double tongDoanhThu, String ghiChu) {
        String sql = "INSERT INTO KetCa (maKetCa, maNhanVien, ngayGioBatDau, ngayGioKetThuc, soDuDauCa, soDuTienMat, tongDoanhThu, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKetCa);
            stmt.setString(2, maNhanVien);
            stmt.setTimestamp(3, Timestamp.valueOf(ngayBatDau));
            stmt.setTimestamp(4, Timestamp.valueOf(ngayKetThuc));
            stmt.setDouble(5, soDuDauCa); // Sửa tham số thứ 5
            stmt.setDouble(6, soDuTienMat); // Tham số thứ 6
            stmt.setDouble(7, tongDoanhThu); // Tham số thứ 7
            stmt.setString(8, ghiChu); // Tham số thứ 8
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    
}
