
package dao;

import connectDB.ConnectJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.BanAn;
import modal.HoaDon;
import modal.KhachHang;
import modal.KhuyenMai;
import modal.MonAn;
import modal.NhanVien;
import modal.QuanLy;

public class HoaDon_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
    
    public HoaDon timHoaDonTheoMaBanVaTrangThai12(String maBan) {
        String sql = " SELECT * \n" +
                        "FROM HoaDon \n" +
                        "WHERE maBan like('" + maBan +"') AND  trangThaiThanhToan IN (N'Chưa thanh toán', N'Đang xử lý') ORDER BY ngayGioLap DESC" ;

        System.out.println("day ni : " + sql);
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();) {
            try (ResultSet rs = stm.executeQuery(sql)) {
                if (rs.next()) {
                    HoaDon hoaDon = new HoaDon();
                    BanAn ba = new BanAn();
                    ba.setMaBan(rs.getString("maBan"));

                    hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                    hoaDon.setBanAn(ba);
                    hoaDon.setNgayGioLap(
                        rs.getTimestamp("ngayGioLap") != null 
                        ? rs.getTimestamp("ngayGioLap").toLocalDateTime() 
                        : null
                    );
                    hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    hoaDon.setTongTien(rs.getDouble("tongTien"));
                    hoaDon.setVAT(rs.getDouble("VAT"));
                    return hoaDon;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
//    public HoaDon timHoaDonTheoMaBanVaTrangThai(String maBan, String... trangThai) throws SQLException {
//    HoaDon hoaDon = null;
//
//    // Khởi tạo kết nối và tài nguyên
//    try (Connection connection = connectDB.ConnectJDBC.getConnection()) {
//        StringBuilder sql = new StringBuilder("SELECT * FROM HoaDon WHERE maBan = ? AND (");
//        
//        // Tạo điều kiện WHERE dựa trên số lượng trạng thái
//        for (int i = 0; i < trangThai.length; i++) {
//            sql.append("trangThaiThanhToan = ?");
//            if (i < trangThai.length - 1) {
//                sql.append(" OR ");
//            }
//        }
//        sql.append(")");
//
//        // Chuẩn bị truy vấn
//        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
//            stm.setString(1, maBan); // Gán giá trị cho tham số maBan
//
//            // Gán giá trị cho các tham số trạng thái
//            for (int i = 0; i < trangThai.length; i++) {
//                stm.setString(i + 2, trangThai[i]);
//            }
//
//            // Thực thi truy vấn
//            try (ResultSet rs = stm.executeQuery()) {
//                if (rs.next()) {
//                    BanAn ba = new BanAn();
//                    ba.setMaBan(rs.getString("maBan"));
//
//                    hoaDon = new HoaDon();
//                    hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
//                    hoaDon.setBanAn(ba);
//                    hoaDon.setNgayGioLap(
//                        rs.getTimestamp("ngayGioLap") != null 
//                        ? rs.getTimestamp("ngayGioLap").toLocalDateTime() 
//                        : null
//                    );
//                    hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
//                    hoaDon.setTongTien(rs.getDouble("tongTien"));
//                    hoaDon.setVAT(rs.getDouble("VAT"));
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        throw e; // Ném lại ngoại lệ để xử lý ở cấp cao hơn
//    }
//
//    return hoaDon;
//}

    
        public HoaDon timHoaDonTheoMaBanVaTrangThai(String maBan, String... trangThai) throws SQLException {
            HoaDon hoaDon = null;
            Connection connection = connectDB.ConnectJDBC.getConnection(); 
            PreparedStatement stm = null;
            ResultSet rs = null;

            try {
                String sql = "SELECT * FROM HoaDon WHERE maBan = ? AND (";
                for (int i = 0; i < trangThai.length; i++) {
                    sql += "trangThaiThanhTOan = ?";
                    if (i < trangThai.length - 1) {
                        sql += " OR "; 
                    }
                }
                sql += ")";
                stm = connection.prepareStatement(sql);
                stm.setString(1, maBan); 
                for (int i = 0; i < trangThai.length; i++) {
                    stm.setString(i + 2, trangThai[i]); 
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    BanAn ba = new BanAn();
                    ba.setMaBan(rs.getString("maBan"));
                    hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                    hoaDon.setBanAn(ba);
                    hoaDon.setNgayGioLap(rs.getTimestamp("ngayGioLap").toLocalDateTime());
                    hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    hoaDon.setTongTien(rs.getDouble("tongTien"));
                    hoaDon.setVAT(rs.getDouble("VAT"));
                }

            } catch(Exception e){
                e.printStackTrace();
            }

            return hoaDon; 
        }

    // Lấy mã hóa đơn cuối cùng trong ngày hiện tại từ cơ sở dữ liệu
     public String layMaHoaDonCuoi() throws SQLException {
        String sql = "SELECT MAX(maHoaDon) FROM HoaDon";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã hóa đơn cuối: " + e.getMessage());
            throw e;
        }
    }

    public String taoMaHoaDonMoi(String maNguoiTao, String maBan) throws SQLException {
        String maHoaDonCuoi = layMaHoaDonCuoi();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");

        // Lấy mã người tạo và định dạng ngày
        String maNguoiTaoCuoi = maNguoiTao.substring(maNguoiTao.length()-6);
        String ngay = formatter.format(now);
        int soLuong = 1;

        // Tạo mã hóa đơn mới và kiểm tra trùng lặp
        String maHoaDonMoi;
        do {
            maHoaDonMoi = String.format("%s%s%04d", maNguoiTaoCuoi, ngay, soLuong++);
        } while (maHoaDonTonTai(maHoaDonMoi));

        return maHoaDonMoi;
    }

    private boolean maHoaDonTonTai(String maHoaDon) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE maHoaDon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    public HoaDon lapHoaDon1(HoaDon hd, String trangThai) throws SQLException {
        String sql = "INSERT INTO HoaDon VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        // Lấy mã nhân viên hoặc mã quản lý và mã bàn từ đối tượng HoaDon
        String maBan = hd.getBanAn().getMaBan();
        String maNhanVien = (hd.getNhanVien() != null) ? hd.getNhanVien().getMaNhanVien() : null;
        String maQuanLy = (hd.getQuanLy() != null) ? hd.getQuanLy().getMaQuanLy() : null;

        // Tạo mã hóa đơn mới
        String maHoaDonMoi = (maNhanVien != null) ? taoMaHoaDonMoi(maNhanVien, maBan) : taoMaHoaDonMoi(maQuanLy, maBan);
        hd.setMaHoaDon(maHoaDonMoi);

        System.out.println("Mã hóa đơn mới: " + maHoaDonMoi); // Log mã hóa đơn mới

        // Thiết lập tham số cho câu lệnh SQL
        ps.setString(1, maHoaDonMoi);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Ngày lập hóa đơn
        ps.setTimestamp(3, null); // Ngày Hủy đặt, chưa có giá trị nên để null
        ps.setTimestamp(4, (hd.getNgayGioNhanBan() != null) ? Timestamp.valueOf(hd.getNgayGioNhanBan()) : null);
        ps.setString(5, maBan);
        ps.setInt(6, hd.getSoLuongKhach());
        ps.setString(7, hd.getKhachHang().getMaKhachHang());
        ps.setString(8, maNhanVien);  // Nếu null thì sẽ được gán null
        ps.setString(9, maQuanLy);  // Nếu null thì sẽ được gán null
        ps.setString(10, trangThai);
        ps.setString(11, hd.getGhiChu());  // Nếu null thì sẽ được gán null
        ps.setDouble(12, (hd.getTongTien() != 0) ? hd.getTongTien() : 0);
        ps.setDouble(13, (hd.getVAT() != 0) ? hd.getVAT() : 0.1);
        ps.setDouble(14, 0);  // Chiết khấu

        // Thực thi câu lệnh SQL
        return (ps.executeUpdate() > 0) ? hd : null;
    }

    //Phương thức lấy thông tin khách hàng từ hóa đơn "đang xử lý" hoặc "chưa thanh toán" 
    // tương ứng với bàn "đã đặt" hoặc "đang sử dụng" với thời gian gần nhất
    public HoaDon layHoaDon(String maBan) throws SQLException{
        String sql = "SELECT TOP 1 hd.maHoaDon, hd.maBan, hd.maKhachHang, hd.soLuongKhach, kh.tenKhachHang, kh.soDienThoai " +
                 "FROM HoaDon hd " +
                 "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                 "WHERE hd.maBan = ? AND (hd.trangThaiThanhToan = N'Đang xử lý' OR hd.trangThaiThanhToan = N'Chưa thanh toán') " +
                 "ORDER BY hd.ngayGioLap DESC"; // Sắp xếp theo thời gian gần nhất
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setSoLuongKhach(rs.getInt("soLuongKhach"));
                    BanAn ban = new BanAn(); // Thực thể bàn
                    ban.setMaBan(rs.getString("maBan"));
                    KhachHang kh = new KhachHang();// Thực thể khách hàng
                    kh.setMaKhachHang(rs.getString("maKhachHang"));
                    kh.setTenKhachHang(rs.getString("tenKhachHang"));
                    kh.setSoDienThoai(rs.getString("soDienThoai"));
                hd.setBanAn(ban);
                hd.setKhachHang(kh);
                return hd;
            }
        return null;
    }
    
    //Phương thức chung để lấy danh sách (thông tin đặt bàn)
    public List<HoaDon> layDanhSach(String sql, PreparedStatement stmt, ResultSet rs) throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        while (rs.next()) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
            hoaDon.setGhiChu(rs.getString("ghiChu"));
            hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));

            Timestamp ngayGioLap = rs.getTimestamp("ngayGioLap");
            Timestamp ngayHuyDat = rs.getTimestamp("ngayHuyDat");
            Timestamp gioNhanBan = rs.getTimestamp("gioNhanBan");

            hoaDon.setNgayGioLap(ngayGioLap != null ? ngayGioLap.toLocalDateTime() : null);
            hoaDon.setNgayHuyDat(ngayHuyDat != null ? ngayHuyDat.toLocalDateTime() : null);
            hoaDon.setNgayGioNhanBan(gioNhanBan != null ? gioNhanBan.toLocalDateTime() : null);

            NhanVien nv = null;
            if (rs.getString("maNhanVien") != null) {
                nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHo(rs.getString("nv_ho"));
                nv.setTen(rs.getString("nv_ten"));
            }
            hoaDon.setNhanVien(nv);

            QuanLy ql = null;
            if (rs.getString("ql_ho") != null) {
                ql = new QuanLy();
                ql.setMaQuanLy(rs.getString("maQuanLy"));
                ql.setHo(rs.getString("ql_ho"));
                ql.setTen(rs.getString("ql_ten"));
            }
            hoaDon.setQuanLy(ql);

            KhachHang kh = new KhachHang();
            kh.setMaKhachHang(rs.getString("maKhachHang"));
            kh.setTenKhachHang(rs.getString("tenKhachHang"));
            kh.setSoDienThoai(rs.getString("soDienThoai"));
            hoaDon.setKhachHang(kh);

            BanAn ban = new BanAn();
            ban.setMaBan(rs.getString("maBan"));
            hoaDon.setBanAn(ban);

            danhSachHoaDon.add(hoaDon);
        }
        return danhSachHoaDon;
    }
    
    // Phương thức chung để lấy danh sách hóa đơn với WHERE và ORDER BY động
    private List<HoaDon> locHoaDon(String whereCondition, String orderBy) throws SQLException {
        String sql = "SELECT hd.maHoaDon, hd.ghiChu, hd.trangThaiThanhToan, hd.ngayGioLap, "
                + "hd.ngayHuyDat, hd.gioNhanBan, hd.maBan, hd.maNhanVien, hd.maQuanLy, hd.maKhachHang, "
                + "kh.tenKhachHang, kh.soDienThoai, "
                + "nv.ho AS nv_ho, nv.ten AS nv_ten, "
                + "ql.ho AS ql_ho, ql.ten AS ql_ten "
                + "FROM HoaDon hd "
                + "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien "//Left join là vẫn lấy dữ liệu từ bảng Hóa đơn lên dù cho bảng bên phải ko có dl tương ứng
                + "LEFT JOIN QuanLy ql ON hd.maQuanLy = ql.maQuanLy "
                + "LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "WHERE " + whereCondition + " "
                + "ORDER BY " + orderBy;

        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        return layDanhSach(sql, ps, rs);
    }

    // Hàm lấy lịch sử đặt bàn 7 ngày gần nhất trong bảng hóa đơn từ cơ sở dữ liệu
    public List<HoaDon> layLichSuDatBanGanDay() throws SQLException {
        String whereCondition = "DATEDIFF(day, hd.ngayGioLap, GETDATE()) <= 7";
        String orderBy = "hd.ngayGioLap DESC";
        return locHoaDon(whereCondition, orderBy);
    }
    
    // Hàm lấy danh sách theo mã bàn và số điện thoại
    public List<HoaDon> timKiemTheoMaBanVaSDT(String maBanCanTim, String sdt) throws SQLException {
        StringBuilder whereCondition = new StringBuilder("1=1");
        if (!maBanCanTim.isEmpty()) {
            whereCondition.append(" AND hd.maBan = '").append(maBanCanTim).append("'");
        }
        if (!sdt.isEmpty()) {
            whereCondition.append(" AND kh.soDienThoai = '").append(sdt).append("'");
        }
        String orderBy = "hd.ngayGioLap DESC";
        return locHoaDon(whereCondition.toString(), orderBy);
    }

    // Hàm lấy danh sách theo Trạng thái thanh toán
    public List<HoaDon> locTheoTrangThai(String trangThai) throws SQLException {
        String whereCondition = "hd.trangThaiThanhToan = N'" + trangThai + "'";
        String orderBy = "hd.ngayGioLap DESC";
        return locHoaDon(whereCondition, orderBy);
    }
    
     // Hàm lọc hóa đơn theo ngày hôm nay
    public List<HoaDon> locTheoNgayHomNay() throws SQLException {
        String condition = "CAST(hd.ngayGioLap AS DATE) = CAST(GETDATE() AS DATE)";
        String orderBy = "hd.ngayGioLap DESC";
        return locHoaDon(condition, orderBy);
    }

    // Hàm lọc hóa đơn theo ngày hôm qua
    public List<HoaDon> locTheoNgayHomQua() throws SQLException {
        String whereCondition = "CAST(hd.ngayGioLap AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)";
        String orderBy = "hd.ngayGioLap DESC";
        return locHoaDon(whereCondition, orderBy);
    }
    
    // Hàm lọc theo ngày đặt và ngày hủy
    public List<HoaDon> locTheoNgay(Date ngayDat, Date ngayHuy) throws SQLException {
        String sql = "SELECT hd.maHoaDon, hd.ghiChu, hd.trangThaiThanhToan, hd.ngayGioLap, "
                + "hd.ngayHuyDat, hd.gioNhanBan, hd.maBan, hd.maNhanVien, hd.maQuanLy, hd.maKhachHang, "
                + "kh.tenKhachHang, kh.soDienThoai, "
                + "nv.ho AS nv_ho, nv.ten AS nv_ten, "
                + "ql.ho AS ql_ho, ql.ten AS ql_ten "
                + "FROM HoaDon hd "
                + "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien "
                + "LEFT JOIN QuanLy ql ON hd.maQuanLy = ql.maQuanLy "
                + "LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang "
                + "WHERE (CAST(hd.ngayGioLap AS date) = ? OR ? IS NULL) "
                + "AND (CAST(hd.ngayHuyDat AS date) = ? OR ? IS NULL)"
                +" ORDER By hd.ngayGioLap Desc";
        PreparedStatement ps = connection.prepareStatement(sql);
             // Chuyển đổi java.util.Date thành java.sql.Date
            java.sql.Date sqlNgayDat = (ngayDat != null) ? new java.sql.Date(ngayDat.getTime()) : null;
            java.sql.Date sqlNgayHuy = (ngayHuy != null) ? new java.sql.Date(ngayHuy.getTime()) : null;
            
            ps.setDate(1, sqlNgayDat);
            ps.setDate(2, sqlNgayDat); 
            ps.setDate(3, sqlNgayHuy);
            ps.setDate(4, sqlNgayHuy);
            ResultSet rs = ps.executeQuery();
            List<HoaDon> danhSach = layDanhSach(sql, ps, rs);
 
        return danhSach;
    }

    //Hàm lấy thông tin đặt trước
    public HoaDon layThongTinDatTruoc(String maBanCanTim, String sdtCanTim) throws SQLException{
        String sql = "SELECT hd.ghiChu, hd.maHoaDon, hd.soLuongKhach, hd.trangThaiThanhToan, hd.gioNhanBan, hd.maBan, hd.maKhachHang,"
                + " kh.tenKhachHang, kh.soDienThoai"
                + " FROM HoaDon hd"
                + " JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang"
                + " WHERE 1=1"; // Sử dụng 1=1 để dễ dàng thêm điều kiện
            if (!maBanCanTim.isEmpty()) {
                sql += ("AND hd.maBan = ? ");
            }
            if (!sdtCanTim.isEmpty()) {
                sql += ("AND kh.soDienThoai = ? ");
            }
            sql += ("AND hd.trangThaiThanhToan = N'Đang xử lý'");
    
        PreparedStatement ps = connection.prepareStatement(sql);
        int n = 1;
        if (!maBanCanTim.isEmpty()) {
            ps.setString(n++, maBanCanTim);
        }
        if (!sdtCanTim.isEmpty()) {
            ps.setString(n++, sdtCanTim);
        }
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setSoLuongKhach(rs.getInt("soLuongKhach"));
                hd.setGhiChu(rs.getString("ghiChu"));
                hd.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                
                    Timestamp ngaygioNhanBan = rs.getTimestamp("gioNhanBan"); // Lấy ngày giờ từ cột ngày giờ Nhận bàn
                    // Chuyển đổi sang LocalDateTime
                    LocalDateTime localNgayGioNhan = ngaygioNhanBan != null ? ngaygioNhanBan.toLocalDateTime() : null;
                hd.setNgayGioNhanBan(localNgayGioNhan);
                
                    BanAn ban = new BanAn(); // Thực thể bàn
                    ban.setMaBan(rs.getString("maBan"));
                    KhachHang kh = new KhachHang();// Thực thể khách hàng
                    kh.setMaKhachHang(rs.getString("maKhachHang"));
                    kh.setTenKhachHang(rs.getString("tenKhachHang"));
                    kh.setSoDienThoai(rs.getString("soDienThoai"));
                hd.setBanAn(ban);
                hd.setKhachHang(kh);
                return hd;
            }
        return null;
    }
    
    //Phương thức cập nhật trạng thái sau khi Hủy hóa đơn và thêm ngày hủy
    public void capNhatTrangThaiHoaDonSauKhiHuy(String trangThaiMoi, String maBan) throws SQLException{
        String sql = "UPDATE HoaDon SET trangThaiThanhToan = ?, ngayHuyDat = ? WHERE maBan = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, trangThaiMoi);  // Gán trạng thái mới
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(3, maBan);          // Gán mã bàn
        
        int n = ps.executeUpdate(); // Thực hiện cập nhật
        if (n > 0) {
            System.out.println("Cập nhật trạng thái Hóa đơn thành công.");
        } else {
            System.out.println("Không tìm thấy hóa đơn để cập nhật.");
        }
    }
    
    //Phương thức cập nhật trạng thái Hóa đơn
    public void capNhatTrangThaiHoaDon(String trangThaiMoi, String maBan) throws SQLException{
        String sql = "UPDATE HoaDon SET trangThaiThanhToan = ? WHERE maBan = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, trangThaiMoi);  // Gán trạng thái mới
        ps.setString(2, maBan);          // Gán mã bàn
        
        int n = ps.executeUpdate(); // Thực hiện cập nhật
        if (n > 0) {
            System.out.println("Cập nhật trạng thái Hóa đơn thành công.");
        } else {
            System.out.println("Không tìm thấy hóa đơn để cập nhật.");
        }
    }

    //Cập nhật thông tin đặt bàn
    public boolean capNhatHoaDon(int sl, LocalDateTime ngay, String gchu, String maBanMoi, String maHoaDon) {
        String sql = "UPDATE HoaDon SET soLuongKhach = ?, gioNhanBan = ?, ghiChu = ?, maBan = ? "
                + "WHERE maHoaDon = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, sl);
            ps.setTimestamp(2, Timestamp.valueOf(ngay));
            ps.setString(3, gchu);
            ps.setString(4, maBanMoi);
            ps.setString(5, maHoaDon);

            int n = ps.executeUpdate();

            return n > 0;
        } catch (SQLException e) { 
            return false;
        }
    }
    
    //Cập nhật thông tin tổng tiền từ việc đặt món
    public boolean capNhatTongTienHoaDon(String maHoaDon, double vat, double tongTien) {
        String sql = "UPDATE HoaDon SET VAT = ?, tongTien = ? "
                + "WHERE maHoaDon = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setDouble(1, vat);
            ps.setDouble(2, tongTien);
            ps.setString(3, maHoaDon);
            
            int n = ps.executeUpdate();

            return n > 0;
        } catch (SQLException e) { 
            return false;
        }
    }
    
    //Cập nhật thông tin sau khi chuyển bàn
    public boolean capNhatChuyenBan(String maHoaDon, String maBanMoi) {
        String sql = "UPDATE HoaDon SET maBan = ? "
                + "WHERE maHoaDon = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, maBanMoi);
            ps.setString(2, maHoaDon);

            int n = ps.executeUpdate();

            return n > 0;
        } catch (SQLException e) { 
            return false;
        }
    }

    // Phương thức lấy danh sách hóa đơn có mã bàn quá hạn đặt bàn
    public List<HoaDon> layDSMaBanQuaHanDat() {
        String sql = "SELECT maBan FROM HoaDon WHERE trangThaiThanhToan = N'Đang xử lý' AND " +
                           "DATEADD(MINUTE, 30, gioNhanBan) < GETDATE()";

        List<HoaDon> danhSach = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                BanAn ban = new BanAn();
                    ban.setMaBan(rs.getString("maBan"));
                hd.setBanAn(ban);
                danhSach.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }


//--
    
    
    

    
    //Phương thức lấy thông tin khách hàng từ hóa đơn "đang xử lý" hoặc "chưa thanh toán" 

    
    
    public  HoaDon layHoaDon1(String maBan) {
        try {
            String sql = "SELECT TOP 1 hd.maHoaDon, hd.maBan, hd.maKhachHang, hd.soLuongKhach, kh.tenKhachHang, kh.soDienThoai " +
                    "FROM HoaDon hd " +
                    "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                    "WHERE hd.maBan = ? AND (hd.trangThaiThanhToan = N'Đang xử lý' OR hd.trangThaiThanhToan = N'Chưa thanh toán') " +
                    "ORDER BY hd.ngayGioLap DESC"; // Sắp xếp theo thời gian gần nhất
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setSoLuongKhach(rs.getInt("soLuongKhach"));
                BanAn ban = new BanAn(); // Thực thể bàn
                ban.setMaBan(rs.getString("maBan"));
                KhachHang kh = new KhachHang();// Thực thể khách hàng
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                kh.setTenKhachHang(rs.getString("tenKhachHang"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                hd.setBanAn(ban);
                hd.setKhachHang(kh);
                return hd;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HoaDon_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
 




    
 

    
    public static HoaDon hienThiThongTinKhachHang(String maBan){
        String sql = "Select a.maKhachHang, a.tenKhachHang, a.soDienThoai, c.maBan from KhachHang a join HoaDon b on a.maKhachHang = b.maKhachHang join BanAn c on b.maBan = c.maBan"
                + " where b.maBan like'%" + maBan + "%' ";
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ){
            if(rs.next()){
                KhachHang khachHang = new KhachHang();
                khachHang.setMaKhachHang(rs.getString("maKhachHang"));
                khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
                khachHang.setSoDienThoai(rs.getString("soDienThoai"));
                
                BanAn banAn = new BanAn();
                banAn.setMaBan(rs.getString("maBan"));
                
                HoaDon hoaDon = new HoaDon();
                hoaDon.setBanAn(banAn);
                hoaDon.setKhachHang(khachHang);
                return hoaDon;
            }
            
        }     catch(SQLException e){
            e.printStackTrace();
        }   
        return null;
    }
    
    
    //
    
    
    // Lấy danh sách tất cả hóa đơn
    // ý tưởng: join nhiều bảng lấy hết thông tin trong 1 hàm này rồi khởi tạo đối tượng đầy đủ thông tin trong modal hóa đơn, rồi truyền hóa đơn vào frame thanh toán để lấy thông tin

    
  public List<HoaDon> getAllHoaDon() {
      List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
    List<String> maHoaDonDaThem = new ArrayList<>(); // Danh sách để kiểm tra mã hóa đơn đã được thêm vào hay chưa

    try {
        Connection con = ConnectJDBC.getConnection();

        // Câu lệnh SQL đã chỉnh sửa
        String sql = "SELECT hd.*, kh.soDienThoai, kh.tenKhachHang, kh.maKhachHang, kh.diemTichLuy, nv.ho as hoNhanVien, nv.ten as tenNhanVien, " +
                     "km.giaTriKhuyenMai, km.loaiKhuyenMai, km.trangThai, km.soLuongToiThieu, km.maKhuyenMai, "
                + "ql.ho AS hoQuanLy, ql.ten AS tenQuanLy " +
                     "FROM HoaDon hd " +
                     "LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
                     "LEFT JOIN KhuyenMaiKhachHang kmkh ON kh.maKhachHang = kmkh.maKhachHang " +
                     "LEFT JOIN KhuyenMai km ON kmkh.maKhuyenMai = km.maKhuyenMai " +
                     "LEFT JOIN QuanLy ql ON hd.maQuanLy = ql.maQuanLy" +
                     " WHERE hd.trangThaiThanhToan IN (N'Chưa thanh toán', N'Đang xử lý', N'Đã thanh toán')";

        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            String maHoaDon = rs.getString("maHoaDon");

            // Kiểm tra xem mã hóa đơn đã được thêm vào danh sách chưa
            if (!maHoaDonDaThem.contains(maHoaDon)) {
                LocalDateTime ngayGioLap = rs.getTimestamp("ngayGioLap").toLocalDateTime();
                LocalDateTime ngayHuyDat = rs.getTimestamp("ngayHuyDat") != null ? rs.getTimestamp("ngayHuyDat").toLocalDateTime() : null;
                LocalDateTime gioNhanBan = rs.getTimestamp("gioNhanBan") != null ? rs.getTimestamp("gioNhanBan").toLocalDateTime() : null;
                String maBan = rs.getString("maBan");
                Integer soLuongKhach = rs.getInt("soLuongKhach");
                String trangThaiThanhToan = rs.getString("trangThaiThanhToan");
                String ghiChu = rs.getString("ghiChu");
                double tongTien = rs.getDouble("tongTien");
                double VAT = rs.getDouble("VAT");
                double chietKhau = rs.getDouble("chietKhau");

                String soDienThoai = rs.getString("soDienThoai");
                String tenKhachHang = rs.getString("tenKhachHang");
                int diemTichLuy = rs.getInt("diemTichLuy");

                

                String maKhuyenMai = rs.getString("maKhuyenMai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                String trangThaiKhuyenMai = rs.getString("trangThai");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");

                KhachHang khachHang = new KhachHang();
                khachHang.setSoDienThoai(soDienThoai);
                khachHang.setTenKhachHang(tenKhachHang);
                khachHang.setMaKhachHang(rs.getString("maKhachHang"));
                khachHang.setDiemTichLuy(diemTichLuy);
                
                
                

                BanAn banAn = new BanAn(maBan);

                KhuyenMai khuyenMai = new KhuyenMai();
                khuyenMai.setMaKhuyenMai(maKhuyenMai);
                khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);
                khuyenMai.setLoaiKhuyenMai(loaiKhuyenMai);
                khuyenMai.setTrangThai(trangThaiKhuyenMai);
                khuyenMai.setSoLuongToiThieu(soLuongToiThieu);
              
                
                NhanVien nv = null;
            if (rs.getString("maNhanVien") != null) {
                nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHo(rs.getString("hoNhanVien"));
                nv.setTen(rs.getString("tenNhanVien"));
            }
            

            QuanLy ql = null;
            if (rs.getString("maQuanLy") != null) {
                ql = new QuanLy();
                ql.setMaQuanLy(rs.getString("maQuanLy"));
                ql.setHo(rs.getString("hoQuanLy"));
                ql.setTen(rs.getString("tenQuanly"));
            }
            
                
                HoaDon hoaDon = new HoaDon(maHoaDon, soLuongKhach, ghiChu, trangThaiThanhToan, ngayGioLap,
                        ngayHuyDat, gioNhanBan, chietKhau, VAT, tongTien, nv, khachHang, banAn, ql);//Vừa sửa ngày 09/11

                dsHoaDon.add(hoaDon); // Thêm hóa đơn vào danh sách
                maHoaDonDaThem.add(maHoaDon); // Thêm mã hóa đơn vào danh sách kiểm tra
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsHoaDon;
}




  
 public List<HoaDon> getAllHoaDonThongTin() {
      List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
    List<String> maHoaDonDaThem = new ArrayList<>(); // Danh sách để kiểm tra mã hóa đơn đã được thêm vào hay chưa

    try {
        Connection con = ConnectJDBC.getConnection();

        // Câu lệnh SQL đã chỉnh sửa
        String sql = "SELECT hd.*, kh.soDienThoai, kh.tenKhachHang, kh.maKhachHang, kh.diemTichLuy, nv.ho as hoNhanVien, nv.ten as tenNhanVien, " +
                     "km.giaTriKhuyenMai, km.loaiKhuyenMai, km.trangThai, km.soLuongToiThieu, km.maKhuyenMai, "
                + "ql.ho AS hoQuanLy, ql.ten AS tenQuanLy " +
                     "FROM HoaDon hd " +
                     "LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
                     "LEFT JOIN KhuyenMaiKhachHang kmkh ON kh.maKhachHang = kmkh.maKhachHang " +
                     "LEFT JOIN KhuyenMai km ON kmkh.maKhuyenMai = km.maKhuyenMai " +
                     "LEFT JOIN QuanLy ql ON hd.maQuanLy = ql.maQuanLy" +
                     " WHERE hd.trangThaiThanhToan IN (N'Chưa thanh toán', N'Đang xử lý')";

        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            String maHoaDon = rs.getString("maHoaDon");

            // Kiểm tra xem mã hóa đơn đã được thêm vào danh sách chưa
            if (!maHoaDonDaThem.contains(maHoaDon)) {
                LocalDateTime ngayGioLap = rs.getTimestamp("ngayGioLap").toLocalDateTime();
                LocalDateTime ngayHuyDat = rs.getTimestamp("ngayHuyDat") != null ? rs.getTimestamp("ngayHuyDat").toLocalDateTime() : null;
                LocalDateTime gioNhanBan = rs.getTimestamp("gioNhanBan") != null ? rs.getTimestamp("gioNhanBan").toLocalDateTime() : null;
                String maBan = rs.getString("maBan");
                Integer soLuongKhach = rs.getInt("soLuongKhach");
                String trangThaiThanhToan = rs.getString("trangThaiThanhToan");
                String ghiChu = rs.getString("ghiChu");
                double tongTien = rs.getDouble("tongTien");
                double VAT = rs.getDouble("VAT");
                double chietKhau = rs.getDouble("chietKhau");

                String soDienThoai = rs.getString("soDienThoai");
                String tenKhachHang = rs.getString("tenKhachHang");
                int diemTichLuy = rs.getInt("diemTichLuy");

                

                String maKhuyenMai = rs.getString("maKhuyenMai");
                double giaTriKhuyenMai = rs.getDouble("giaTriKhuyenMai");
                String loaiKhuyenMai = rs.getString("loaiKhuyenMai");
                String trangThaiKhuyenMai = rs.getString("trangThai");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");

                KhachHang khachHang = new KhachHang();
                khachHang.setSoDienThoai(soDienThoai);
                khachHang.setTenKhachHang(tenKhachHang);
                khachHang.setMaKhachHang(rs.getString("maKhachHang"));
                khachHang.setDiemTichLuy(diemTichLuy);
                
                
                

                BanAn banAn = new BanAn(maBan);

                KhuyenMai khuyenMai = new KhuyenMai();
                khuyenMai.setMaKhuyenMai(maKhuyenMai);
                khuyenMai.setGiaTriKhuyenMai(giaTriKhuyenMai);
                khuyenMai.setLoaiKhuyenMai(loaiKhuyenMai);
                khuyenMai.setTrangThai(trangThaiKhuyenMai);
                khuyenMai.setSoLuongToiThieu(soLuongToiThieu);
              
                
                NhanVien nv = null;
            if (rs.getString("maNhanVien") != null) {
                nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHo(rs.getString("hoNhanVien"));
                nv.setTen(rs.getString("tenNhanVien"));
            }
            

            QuanLy ql = null;
            if (rs.getString("maQuanLy") != null) {
                ql = new QuanLy();
                ql.setMaQuanLy(rs.getString("maQuanLy"));
                ql.setHo(rs.getString("hoQuanLy"));
                ql.setTen(rs.getString("tenQuanly"));
            }
            
                
                HoaDon hoaDon = new HoaDon(maHoaDon, soLuongKhach, ghiChu, trangThaiThanhToan, ngayGioLap,
                        ngayHuyDat, gioNhanBan, chietKhau, VAT, tongTien, nv, khachHang, banAn, ql);//Vừa sửa ngày 09/11

                dsHoaDon.add(hoaDon); // Thêm hóa đơn vào danh sách
                maHoaDonDaThem.add(maHoaDon); // Thêm mã hóa đơn vào danh sách kiểm tra
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsHoaDon;
   }

public void capNhatTrangThaiDaHuy(HoaDon hoaDon, LocalDateTime time) throws SQLException {
     Connection con = null;
       PreparedStatement pstmt = null;
     // Chuyển LocalDateTime thành Timestamp
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(time);
    try  {
         con = ConnectJDBC.getConnection();
         String sql = "UPDATE HoaDon SET trangThaiThanhToan = ?, ngayHuyDat = ? WHERE maHoaDon = ?";
         pstmt = con.prepareStatement(sql);
        pstmt.setString(1, hoaDon.getTrangThaiThanhToan());
         pstmt.setTimestamp(2, timestamp);  // Cập nhật ngày giờ hủy
        pstmt.setString(3, hoaDon.getMaHoaDon());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        throw new SQLException("Lỗi khi cập nhật trạng thái hóa đơn: " + e.getMessage());
    }
}

   public boolean updateInvoice(HoaDon hoaDon) {
       boolean isUpdated = false; // Biến kiểm tra cập nhật thành công hay không
       Connection con = null;
       PreparedStatement pstmt = null;

       try {
           // Kết nối đến cơ sở dữ liệu
           con = ConnectJDBC.getConnection();

           // Chuẩn bị câu lệnh SQL để cập nhật hóa đơn
           String sql = "UPDATE HoaDon SET " +
                        "soLuongKhach = ?, " +
                        "ghiChu = ?, " +
                        "trangThaiThanhToan = ?, " +
                        "tongTien = ?, " +
                        "VAT = ?, " +
                        "chietKhau = ?, " +
                        "ngayHuyDat = ? " +
                        "WHERE maHoaDon = ?";

           pstmt = con.prepareStatement(sql);

           // Thiết lập các tham số trong câu lệnh SQL
           pstmt.setInt(1, hoaDon.getSoLuongKhach());
           pstmt.setString(2, hoaDon.getGhiChu());
           pstmt.setString(3, "Đã thanh toán");
           pstmt.setDouble(4, hoaDon.getTongTien());
           pstmt.setDouble(5, hoaDon.getVAT());
           pstmt.setDouble(6, hoaDon.getChietKhau());
           pstmt.setTimestamp(7, hoaDon.getNgayHuyDat() != null ? Timestamp.valueOf(hoaDon.getNgayHuyDat()) : null);
           pstmt.setString(8, hoaDon.getMaHoaDon());

           // Thực hiện cập nhật
           int rowsAffected = pstmt.executeUpdate();
           if (rowsAffected > 0) {
               isUpdated = true; // Cập nhật thành công
           }
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           // Đóng kết nối và các tài nguyên
           try {
               if (pstmt != null) {
                   pstmt.close();
               }
               if (con != null) {
                   con.close();
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       return isUpdated;
   }
   
public boolean updateInvoicePhu(String maBanPhu) {
    boolean isUpdated = false; // Biến kiểm tra cập nhật thành công hay không
    Connection con = null;
    PreparedStatement pstmt = null;

    try {
        // Kết nối đến cơ sở dữ liệu
        con = ConnectJDBC.getConnection();

        // Câu lệnh SQL để cập nhật trạng thái thanh toán của hóa đơn cho bàn phụ
//        String sql = "UPDATE HoaDon SET trangThaiThanhToan = ? " +
//                     "WHERE maHoaDon = (SELECT maHoaDon FROM BanAn WHERE maBan = ? AND maHoaDon IS NOT NULL)";

          String sql = "UPDATE HoaDon SET trangThaiThanhToan = ? where maBan = ?";

        pstmt = con.prepareStatement(sql);

        // Thiết lập tham số trong câu lệnh SQL
        pstmt.setString(1, "Đã thanh toán");
        pstmt.setString(2, maBanPhu);
        
        System.out.println("phhhhhhhhhhhhhhhhhhhhhhhhh : " + maBanPhu);

        // Thực hiện cập nhật trạng thái hóa đơn
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            isUpdated = true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đóng kết nối và các tài nguyên
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return isUpdated;
}





    
public List<HoaDon> timHoaDon(String tenKhachHang, String soDienThoai) throws SQLException {
     List<HoaDon> hoaDons = new ArrayList<>();
    Connection con = ConnectJDBC.getConnection();
    
    // Câu lệnh SQL đã chỉnh sửa
    String sql = "SELECT hd.*, kh.soDienThoai, kh.tenKhachHang, kh.maKhachHang, kh.diemTichLuy, " +
                 "nv.ten AS tenNhanVien, km.giaTriKhuyenMai, km.loaiKhuyenMai, km.trangThai, " +
                 "km.soLuongToiThieu, km.maKhuyenMai " +
                 "FROM HoaDon hd " +
                 "JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                 "JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
                 "LEFT JOIN KhuyenMaiKhachHang kmkh ON kh.maKhachHang = kmkh.maKhachHang " +
                 "LEFT JOIN KhuyenMai km ON kmkh.maKhuyenMai = km.maKhuyenMai " +
                 "WHERE hd.trangThaiThanhToan IN (N'Chưa thanh toán', N'Đang xử lý', N'Đã thanh toán')";

    // Thêm điều kiện tìm kiếm dựa trên các tiêu chí
    List<String> conditions = new ArrayList<>();
    if (tenKhachHang != null && !tenKhachHang.isEmpty()) {
        conditions.add("kh.tenKhachHang LIKE '%" + tenKhachHang + "%'");
    }
    if (soDienThoai != null && !soDienThoai.isEmpty()) {
        conditions.add("kh.soDienThoai LIKE '%" + soDienThoai + "%'");
    }

    // Thêm điều kiện vào câu truy vấn nếu có
    if (!conditions.isEmpty()) {
        sql += " AND " + String.join(" AND ", conditions);
    }

    // Thực hiện câu truy vấn
    Statement statement = con.createStatement();
    ResultSet rs = statement.executeQuery(sql);

    // Xử lý kết quả truy vấn
    while (rs.next()) {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(rs.getString("maKhachHang"));
        khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
        khachHang.setSoDienThoai(rs.getString("soDienThoai"));
        String maHoaDon = rs.getString("maHoaDon");
        int soLuongKhach = rs.getInt("soLuongKhach");
        String ghiChu = rs.getString("ghiChu");
        String trangThaiThanhToan = rs.getString("trangThaiThanhToan");
        LocalDateTime ngayGioLap = rs.getTimestamp("ngayGioLap").toLocalDateTime();
        double tongTien = rs.getDouble("tongTien");

        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(rs.getString("maNhanVien"));
        BanAn banAn = new BanAn();
        
        
        //Vừa sửa ngày 09/11
        QuanLy quanLy = new QuanLy(rs.getString("maQuanLy"));
        // Khởi tạo đối tượng HoaDon
        HoaDon hoaDon = new HoaDon(maHoaDon, soLuongKhach, ghiChu, trangThaiThanhToan, ngayGioLap, null, null, 0, 0, tongTien, nhanVien, khachHang, banAn, quanLy);
        hoaDons.add(hoaDon);
    }

    return hoaDons;
}


// hihi


 // Hàm lấy doanh thu theo ngày, tháng, năm
    public Map<String, Double> getDoanhThuTheoNgay() {
    Map<String, Double> doanhThuMap = new TreeMap<>();
    
    try (Connection conn = ConnectJDBC.getConnection()) {
        
      String sql = "SELECT CAST(ngayGioLap AS DATE) AS ngay, SUM(tongTien) AS doanhThu " +
             "FROM HoaDon " +
            " where trangThaiThanhToan IN ( N'Đã thanh toán') " +
             "GROUP BY CAST(ngayGioLap AS DATE) " +
             "ORDER BY ngay";
                         
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                double doanhThu = rs.getDouble("doanhThu");
                doanhThuMap.put(ngay, doanhThu);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Kiểm tra xem có dữ liệu trả về hay không
    System.out.println("Dữ liệu doanh thu theo ngày: " + doanhThuMap);
    
    return doanhThuMap;
}

//public void layDoanhThuTheoNgay(DefaultCategoryDataset dataset) {
//    // Câu truy vấn SQL để lấy doanh thu theo ngày
//    String sql = "SELECT CAST(ngayGioLap AS DATE) AS ngay, SUM(tongTien) AS totalRevenue " +
//                 "FROM HoaDon " +
//                 
//                 "GROUP BY CAST(ngayGioLap AS DATE) " +
//                 "ORDER BY ngay";
//
//    try (Connection conn = ConnectJDBC.getConnection();
//         PreparedStatement stmt = conn.prepareStatement(sql);
//         ResultSet rs = stmt.executeQuery()) {
//
//        // Định dạng ngày tháng
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        // Duyệt qua kết quả và thêm dữ liệu vào dataset
//        while (rs.next()) {
//            Date date = rs.getDate("ngay");
//            double revenue = rs.getDouble("totalRevenue");
//            dataset.addValue(revenue, "Doanh thu", dateFormat.format(date)); // Thêm dữ liệu vào biểu đồ
//        }
//
//    } catch (Exception e) {
//        // Hiển thị thông báo lỗi nếu có vấn đề
//        JOptionPane.showMessageDialog(null, "Lỗi update biểu đồ: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
//    }
//}

  public List<HoaDon> getAllHoaDonThongKe() {
    List<HoaDon> dsHoaDon = new ArrayList<>();
    try (Connection con = ConnectJDBC.getConnection()) {
       String sql = "SELECT * FROM (" +
             "    SELECT hd.maHoaDon, hd.ngayGioLap, hd.ngayHuyDat, hd.gioNhanBan, hd.maBan, hd.soLuongKhach, " +
             "           hd.trangThaiThanhToan, hd.ghiChu, hd.tongTien, hd.VAT, hd.chietKhau, " +
             "           kh.soDienThoai, kh.tenKhachHang, kh.maKhachHang, kh.diemTichLuy, " +
             "           nv.ten , km.giaTriKhuyenMai, km.loaiKhuyenMai, km.trangThai , " +
             "           km.soLuongToiThieu, km.maKhuyenMai, " +
             "           ROW_NUMBER() OVER (PARTITION BY hd.maHoaDon ORDER BY hd.ngayGioLap DESC) AS row_num " +
             "    FROM HoaDon hd " +
             "    JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
             "    JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
             "    LEFT JOIN KhuyenMaiKhachHang kmkh ON kh.maKhachHang = kmkh.maKhachHang " +
             "    LEFT JOIN KhuyenMai km ON kmkh.maKhuyenMai = km.maKhuyenMai " +
             "    WHERE hd.trangThaiThanhToan = N'Đã thanh toán' " +
             ") AS HoaDonWithRowNum " +
             "WHERE row_num = 1 " +
             "ORDER BY ngayGioLap DESC";




        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            HoaDon hoaDon = createHoaDonFromResultSet(rs);
            dsHoaDon.add(hoaDon);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsHoaDon;
}

private HoaDon createHoaDonFromResultSet(ResultSet rs) throws SQLException {
    // Lấy thông tin từ bảng HoaDon
    String maHoaDon = rs.getString("maHoaDon");
    LocalDateTime ngayGioLap = rs.getTimestamp("ngayGioLap").toLocalDateTime();
    LocalDateTime ngayHuyDat = rs.getTimestamp("ngayHuyDat") != null ? rs.getTimestamp("ngayHuyDat").toLocalDateTime() : null;
    LocalDateTime gioNhanBan = rs.getTimestamp("gioNhanBan") != null ? rs.getTimestamp("gioNhanBan").toLocalDateTime() : null;
    String maBan = rs.getString("maBan");
    int soLuongKhach = rs.getInt("soLuongKhach");
    String trangThaiThanhToan = rs.getString("trangThaiThanhToan");
    String ghiChu = rs.getString("ghiChu");
    double tongTien = rs.getDouble("tongTien");
    double VAT = rs.getDouble("VAT");
    double chietKhau = rs.getDouble("chietKhau");

    // Tạo đối tượng KhachHang
    KhachHang khachHang = new KhachHang();
    khachHang.setSoDienThoai(rs.getString("soDienThoai"));
    khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
    khachHang.setMaKhachHang(rs.getString("maKhachHang"));
    khachHang.setDiemTichLuy(rs.getInt("diemTichLuy"));

    // Tạo đối tượng NhanVien
    NhanVien nhanVien = new NhanVien();
    nhanVien.setTen(rs.getString("ten"));

    // Tạo đối tượng BanAn
    BanAn banAn = new BanAn(maBan);

    // Tạo đối tượng HoaDon
    return new HoaDon(maHoaDon, soLuongKhach, ghiChu, trangThaiThanhToan, ngayGioLap,
                      ngayHuyDat, gioNhanBan, chietKhau, VAT, tongTien, nhanVien, khachHang, banAn, null);
}

private KhuyenMai createKhuyenMaiFromResultSet(ResultSet rs) throws SQLException {
    KhuyenMai khuyenMai = new KhuyenMai();
    khuyenMai.setMaKhuyenMai(rs.getString("maKhuyenMai"));
    khuyenMai.setGiaTriKhuyenMai(rs.getDouble("giaTriKhuyenMai"));
    khuyenMai.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
    khuyenMai.setTrangThai(rs.getString("trangThai"));
    khuyenMai.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
    return khuyenMai;
}

    public int getHoaDonTrongTuan() throws SQLException {
        String sql = "SELECT COUNT(*) AS soHoaDon FROM HoaDon " +
                     " where trangThaiThanhToan IN ( N'Đã thanh toán') " +
                     "AND DATEDIFF(day, ngayGioLap, GETDATE()) <= 7";
        try (Connection con = ConnectJDBC.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("soHoaDon");
            }
        }
        return 0;
    }


    public double getDoanhThuTrongTuan() throws SQLException {
        String sql = "SELECT SUM(tongTien) AS doanhThu FROM HoaDon " +
                     " where trangThaiThanhToan IN ( N'Đã thanh toán') " +
                     "AND DATEDIFF(day, ngayGioLap, GETDATE()) <= 7";
        try (Connection con = ConnectJDBC.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("doanhThu");
            }
        }
        return 0.0;
    }

    public int getSoLuongBanHuyTrongTuan() throws SQLException {
    String sql = "SELECT COUNT(*) AS soBanHuy FROM HoaDon " +
                 "WHERE ngayHuyDat IS NOT NULL " +
                 "AND DATEDIFF(day, ngayHuyDat, GETDATE()) <= 7";
    try (Connection con = ConnectJDBC.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        if (rs.next()) {
            return rs.getInt("soBanHuy");
        }
    }
    return 0;
}


//public double getDoanhThuNhanVienTrongNgay(String maNhanVien) throws SQLException {
//    String sql = "SELECT SUM(hd.tongTien) AS doanhThuNhanVien " +
//                 "FROM HoaDon hd JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
//                 " where trangThaiThanhToan IN ( N'Đã thanh toán') " +
//                 "AND nv.maNhanVien = ?";
//    
//    double doanhThu = 0;
//    try (Connection con = ConnectJDBC.getConnection();
//         PreparedStatement stmt = con.prepareStatement(sql)) {
//        stmt.setString(1, maNhanVien); 
//        try (ResultSet rs = stmt.executeQuery()) {
//            if (rs.next()) {
//                doanhThu = rs.getDouble("doanhThuNhanVien"); 
//            }
//        }
//    }
//    return doanhThu;
//}

     public boolean kiemTraHoaDonThanhToan(String maBan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE maBan = ? AND trangThaiThanhToan = N'Đã thanh toán'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Có hóa đơn chưa thanh toán
            }
        }
        return false;
    }
     
     
 public double getDoanhThuNhanVienTrongNgay(String maNhanVien) {
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


    public List<HoaDon> layHoaDonTheoNgay(Date ngayBatDau, Date ngayKetThuc) {
        List<HoaDon> hoaDons = new ArrayList<>();

        // Thiết lập thời gian cho ngày kết thúc (23:59:59) để đảm bảo bao gồm hết ngày đó
        Calendar cal = Calendar.getInstance();
        cal.setTime(ngayKetThuc);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date sqlNgayKetThuc = cal.getTime();  

        // Cập nhật câu SQL để tính tổng tiền theo ngày
        String query = "SELECT CAST(hd.ngayGioLap AS DATE) AS ngay, SUM(hd.tongTien) AS doanhThu " +
                       "FROM HoaDon hd " +
                       "WHERE hd.ngayGioLap >= ? AND hd.ngayGioLap <= ? " +  
                       "AND hd.trangThaiThanhToan = N'Đã thanh toán' " +
                       "GROUP BY CAST(hd.ngayGioLap AS DATE)";

        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(ngayBatDau.getTime())); 
            pstmt.setDate(2, new java.sql.Date(sqlNgayKetThuc.getTime()));  

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                LocalDateTime ngayGioLap = rs.getTimestamp("ngay").toLocalDateTime();
                hoaDon.setNgayGioLap(ngayGioLap);
                hoaDon.setTongTien(rs.getDouble("doanhThu"));
                hoaDons.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hoaDons;
    }

        public List<MonAn> layDanhSachMonTheoHoaDon(String maHoaDon) throws SQLException {
            List<MonAn> danhSachMon = new ArrayList<>();
            String sql = "SELECT b.MaMonAn, b.TenMonAn, b.GiaMonAn, b.soLuong " +
                         "FROM ChiTietHoaDon c " +
                         "JOIN MonAn b on c.MaMonAn = b.MaMonAn " +
                         "WHERE c.MaHoaDon = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, maHoaDon);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    MonAn monAn = new MonAn();
                    monAn.setMaMonAn(rs.getString("MaMonAn"));
                    monAn.setTenMonAn(rs.getString("TenMonAn"));
                    monAn.setGiaMonAn(rs.getDouble("GiaMonAn"));
                    monAn.setSoLuong(rs.getInt("soLuong")); 
                    danhSachMon.add(monAn);
                }
            }
            return danhSachMon;
        }

//   public boolean themMonAnVaoHoaDon(String maHoaDon, MonAn monAn) throws SQLException {
//        String sql = "MERGE INTO ChiTietHoaDon AS target " +
//                     "USING (SELECT ?, ?, ?) AS source (MaHoaDon, MaMonAn, soLuongMonAn) " +
//                     "ON target.MaHoaDon = source.MaHoaDon AND target.MaMonAn = source.MaMonAn " +
//                     "WHEN MATCHED THEN " +
//                     "    UPDATE SET target.soLuongMonAn = target.soLuongMonAn + source.soLuongMonAn " +
//                     "WHEN NOT MATCHED THEN " +
//                     "    INSERT (MaHoaDon, MaMonAn, soLuongMonAn) " +
//                     "    VALUES (source.MaHoaDon, source.MaMonAn, source.soLuongMonAn);";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, maHoaDon); // Mã hóa đơn
//            stmt.setString(2, monAn.getMaMonAn()); // Mã món ăn
//            stmt.setInt(3, monAn.getSoLuong()); // Số lượng món ăn
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
        
        public void themMonAnVaoHoaDon(String maHoaDon, MonAn monAn) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMonAn, soLuongMonAn) VALUES ( ?, ?, ?)";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            stmt.setString(2, monAn.getMaMonAn());
            stmt.setInt(3, monAn.getSoLuong());
            stmt.executeUpdate();
        }
    }
        


   public boolean thanhToanBanChinh(String maHoaDonChinh, String maHoaDonPhu) throws SQLException {
        // Cập nhật trạng thái bàn phụ thành "trống" sau khi gộp bàn
        String updateBanPhu = "UPDATE BanAn SET trangThai = 'Trống' WHERE maBan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateBanPhu)) {
            stmt.setString(1, maHoaDonPhu); // Mã bàn phụ
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public HoaDon layHoaDonTheoBan(String maBan) throws SQLException {
        String sql = "SELECT a.*, b.* FROM HoaDon a join BanAn b on a.maBan = b.maBan WHERE a.MaBan = ? "
                + " AND b.trangThai = N'Đang sử dụng' AND a.trangThaiThanhToan = N'Chưa thanh toán' ;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                BanAn banAn = new BanAn();
                 banAn.setMaBan(rs.getString("maBan"));
                    banAn.setTang( rs.getString("tang"));
                    banAn.setViTri(rs.getString("viTri"));
                    banAn.setTrangThaiBanAn(rs.getString("trangThai"));
                hoaDon.setMaHoaDon(rs.getString("MaHoaDon"));
                hoaDon.setBanAn(banAn);
                hoaDon.setNgayGioLap(rs.getTimestamp("ngayGioLap").toLocalDateTime());
                hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                return hoaDon;
            }
        }
        return null;
    }

}

