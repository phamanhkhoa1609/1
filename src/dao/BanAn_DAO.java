package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import modal.BanAn;
import modal.HoaDon;
import modal.LoaiBan;

public class BanAn_DAO {
    Connection connection = connectDB.ConnectJDBC.getConnection();
    
    // Hàm lấy danh sách tất cả các tầng từ cơ sở dữ liệu
    public String[] layTatCaTang() throws SQLException {
        List<String> danhSachTang = new ArrayList<>();
        String sql = "SELECT DISTINCT tang FROM BanAn"; // Câu truy vấn lấy các tầng không trùng lặp
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            danhSachTang.add(rs.getString("tang")); // Thêm tầng vào danh sách
        }
        // Chuyển danh sách thành mảng
        return danhSachTang.toArray(new String[0]);
    }

    // Hàm lấy danh sách tất cả vị trí bàn từ cơ sở dữ liệu
    public String[] layTatCaViTri() throws SQLException {
        List<String> danhSachViTri = new ArrayList<>();
        String sql = "SELECT DISTINCT viTri FROM BanAn"; // Câu truy vấn lấy các vị trí không trùng lặp
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            danhSachViTri.add(rs.getString("viTri")); // Thêm vị trí vào danh sách
        }
        // Chuyển danh sách thành mảng
//        System.out.println(danhSachViTri.size());
        return danhSachViTri.toArray(new String[0]);
    }
    
     // Hàm lấy danh sách tất cả các bàn ăn từ cơ sở dữ liệu
    public List<BanAn> layTatCaBan() throws SQLException{
        List<BanAn> danhSachBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";// Câu truy vấn lấy tất cả dữ liệu từ bảng BanAn
        PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();// Duyệt qua từng dòng kết quả từ ResultSet
            while (rs.next()) { 
                // Lấy dữ liệu từ các cột của bảng BanAn và gán cho các thuộc tính của đối tượng BanAn
                String maBan = rs.getString(1);
                String viTri = rs.getString(2);
                String moTa = rs.getString(3);
                String tang = rs.getString(4);
                String maLoaiBan = rs.getString(5);
                String trangThai = rs.getString(6);
		
		BanAn ban = new BanAn(maBan,viTri, moTa, tang, trangThai, new LoaiBan(maLoaiBan));
                danhSachBanAn.add(ban);
            }
        return danhSachBanAn;
    }
    
    //Hàm Cập nhật trạng thái bàn--
    public void capNhatTrangThaiBan(String maBan, String trangThaiMoi) throws SQLException{
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, trangThaiMoi);  // Gán trạng thái mới
        ps.setString(2, maBan);          // Gán mã bàn
        
        int n = ps.executeUpdate(); // Thực hiện cập nhật
        if (n > 0) {
            System.out.println("Cập nhật trạng thái bàn thành công.");
        } else {
            System.out.println("Không tìm thấy bàn để cập nhật.");
        }
    }


     public BanAn layThongTinBanAn(String maBan)throws SQLException {
        if (maBan == null || maBan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bàn không được để trống.");
        }

        String sql = "SELECT b.*, l.maLoaiBan, l.tenLoaiBan " +
                     "FROM BanAn b " +
                     "JOIN LoaiBan l ON b.maLoaiBan = l.maLoaiBan " +
                     "WHERE b.maBan = '" + maBan + "' ";

        System.out.println("truy van day : " + sql);
        try(Connection conn = connectDB.ConnectJDBC.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);){
                if(rs.next()){
                          BanAn banAn = new BanAn();
                    banAn.setMaBan(rs.getString("maBan"));
                    banAn.setMoTa(rs.getString("moTa"));
                    banAn.setTang(rs.getString("tang"));
                    banAn.setTrangThaiBanAn(rs.getString("trangThai"));
                    banAn.setViTri(rs.getString("viTri"));

                    LoaiBan lb = new LoaiBan();
                    lb.setMaLoaiBan(rs.getString("maLoaiBan"));
                    lb.setTenLoaiBan(rs.getString("tenLoaiBan"));

                    banAn.setLoaiBan(lb);
                    return banAn;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    

    
     // Hàm lấy danh sách tất cả các bàn ăn theo Tầng và vị trí
    public List<BanAn> layBanTheoTangVaViTri(String tangHienTai, String viTriBan) throws SQLException{
        List<BanAn> danhSachBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE tang = ? and viTri =?";// Câu truy vấn lấy tất cả dữ liệu từ bảng BanAn
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tangHienTai);
        ps.setString(2, viTriBan);
            ResultSet rs = ps.executeQuery();// Duyệt qua từng dòng kết quả từ ResultSet
                while (rs.next()) { 
                    // Lấy dữ liệu từ các cột của bảng BanAn và gán cho các thuộc tính của đối tượng BanAn
                    String maBan = rs.getString(1);
                    String viTri = rs.getString(2);
                    String moTa = rs.getString(3);
                    String tang = rs.getString(4);
                    String maLoaiBan = rs.getString(5);
                    String trangThai = rs.getString(6);
                    BanAn ban = new BanAn(maBan,viTri, moTa, tang, trangThai, new LoaiBan(maLoaiBan));
                    danhSachBanAn.add(ban);
                }
        return danhSachBanAn;
    }
    
    // Hàm lấy danh sách bàn ăn theo tên loại bàn, tầng và vị trí//
    public List<BanAn> layBanTheoTieuChi(String tenLoaiBan, String tang, String viTri) throws SQLException{
        List<BanAn> danhSachBanAn = new ArrayList<>();
        String sql = "SELECT b.*, l.tenLoaiBan FROM BanAn b JOIN LoaiBan l ON b.maLoaiBan = l.maLoaiBan WHERE 1=1";
        // Kiểm tra và thêm điều kiện lọc vào truy vấn
        if (!tenLoaiBan.equals("--Loại bàn--")) {
            sql += " AND l.tenLoaiBan = ?";
        }
        if (!tang.equals("--Tầng--")) {
            sql += " AND b.tang = ?";
        }
        if (!viTri.equals("--Vị trí--")) {
            sql += " AND b.viTri = ?";
        }

        // Tạo PreparedStatement
        PreparedStatement ps = connection.prepareStatement(sql);

        int chiSo = 1;
        if (!tenLoaiBan.equals("--Loại bàn--")) {
            ps.setString(chiSo++, tenLoaiBan);
        }
        if (!tang.equals("--Tầng--")) {
            ps.setString(chiSo++, tang);
        }
        if (!viTri.equals("--Vị trí--")) {
            ps.setString(chiSo++, viTri);
        }
            // Thực hiện truy vấn
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy dữ liệu từ các cột của bảng BanAn và gán cho các thuộc tính của đối tượng BanAn
                String maBan = rs.getString(1);
                String viTriBanAn = rs.getString(2);
                String moTa = rs.getString(3);
                String tangBan = rs.getString(4);
                String trangThai = rs.getString(6);
                String maLoai = rs.getString(5);
                // Lấy tên loại bàn từ kết quả truy vấn
                String tenLoai = rs.getString("tenLoaiBan"); 

                BanAn ban = new BanAn(maBan, viTriBanAn, moTa, tangBan, trangThai, new LoaiBan(maLoai, tenLoai));
                danhSachBanAn.add(ban);
               }
        return danhSachBanAn;
    }
    
    
    
    
    
    
    //Duyyyyyyyyyyyyyyyyyyyyyyyyyyyyy ___ bắt đầu:
    
    
    // Lấy dữ liệu cho Màn Hình Quản Lý Bàn
    
    // Hàm lấy danh sách bàn ăn với thông tin loại bàn theo tiêu chí tìm kiếm
    public List<BanAn> layDanhSachBanAnChiTietDuy(String maBan, String loaiBan, String tang, String viTri) throws SQLException {
        List<BanAn> danhSachBanAn = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT b.maBan, b.viTri, b.moTa, b.tang, b.trangThai, l.tenLoaiBan " +
                                             "FROM BanAn b JOIN LoaiBan l ON b.maLoaiBan = l.maLoaiBan WHERE 1=1");

        // Thêm điều kiện tìm kiếm nếu có
        if (maBan != null && !maBan.isEmpty()) {
            sql.append(" AND b.maBan = ?");
        }
        if (loaiBan != null && !loaiBan.isEmpty()) {
            sql.append(" AND l.tenLoaiBan = ?");
        }
        if (tang != null && !tang.isEmpty()) {
            sql.append(" AND b.tang = ?");
        }
        if (viTri != null && !viTri.isEmpty()) {
            sql.append(" AND b.viTri = ?");
        }

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        // Gán giá trị cho các tham số trong PreparedStatement
        int index = 1;
        if (maBan != null && !maBan.isEmpty()) {
            ps.setString(index++, maBan);
        }
        if (loaiBan != null && !loaiBan.isEmpty()) {
            ps.setString(index++, loaiBan);
        }
        if (tang != null && !tang.isEmpty()) {
            ps.setString(index++, tang);
        }
        if (viTri != null && !viTri.isEmpty()) {
            ps.setString(index++, viTri);
        }

        try {
            ResultSet rs = ps.executeQuery(); // Thực hiện truy vấn
            while (rs.next()) {
                // Lấy dữ liệu từ các cột của bảng BanAn và LoaiBan
                String maBanResult = rs.getString("maBan");
                String viTriResult = rs.getString("viTri");
                String moTa = rs.getString("moTa");
                String tangResult = rs.getString("tang");
                String trangThai = rs.getString("trangThai");
                String tenLoaiBan = rs.getString("tenLoaiBan");

                // Tạo đối tượng BanAn với dữ liệu lấy được
                BanAn ban = new BanAn(maBanResult, viTriResult, moTa, tangResult, trangThai, new LoaiBan(tenLoaiBan));
                danhSachBanAn.add(ban); // Thêm vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachBanAn; // Trả về danh sách bàn ăn


    }


    
    //--bị sai--//
    public String removeAccent(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }



    public void addBanDuy(String maBan, String moTa, String viTri, String tenLoaiBan, String trangThai, String tang) throws SQLException {
        // Khai báo maLoaiBan
        String maLoaiBan;

        // Chuyển đổi từ tên loại bàn sang chuỗi không dấu
        String tenLoaiBanKhongDau = removeAccent(tenLoaiBan);
        System.out.println("Tên loại bàn (không dấu): " + tenLoaiBanKhongDau); // In giá trị của tenLoaiBan không dấu ra console

        // Chuyển đổi từ tên loại bàn không dấu sang mã loại bàn
        if (tenLoaiBanKhongDau.equalsIgnoreCase("Ban Thuong")) {
            maLoaiBan = "LB001";
        } else if (tenLoaiBanKhongDau.equalsIgnoreCase("Ban VIP")) {
            maLoaiBan = "LB002";
        } else {
            System.out.println("Loại bàn không hợp lệ!"); // Giữ lại thông báo lỗi
            return; // Thoát nếu loại bàn không hợp lệ
        }

        // Thêm bàn vào bảng BanAn
        String query = "INSERT INTO BanAn (maBan, mota, viTri, maLoaiBan, trangThai, tang) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, maBan);
            ps.setString(2, moTa);
            ps.setString(3, viTri);
            ps.setString(4, maLoaiBan); // Sử dụng maLoaiBan đã chuyển đổi
            ps.setString(5, trangThai);
            ps.setString(6, tang);
            ps.executeUpdate();
            System.out.println("Thêm bàn thành công!");
        } catch (SQLException e) {
            System.out.println("Có lỗi xảy ra khi thêm bàn: " + e.getMessage());
        }
    }



    // Phương thức để lấy số bàn tiếp theo
       public int tuTaoMaBanTiepTheoDuy(String maBan) throws SQLException {
            String sql = "SELECT COUNT(*) AS count FROM BanAn WHERE maBan LIKE ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, maBan + "%");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count") + 1; // Tăng số bàn lên 1
                }
            }
            return 1; // Nếu không có bàn nào, bắt đầu từ 1
        }



    public List<BanAn> getAllBanDuy() throws SQLException {
        List<BanAn> danhSachBanAn = new ArrayList<>();
        String query = "SELECT b.maBan, b.viTri, b.moTa, b.tang, b.trangThai, l.tenLoaiBan " +
                       "FROM BanAn b JOIN LoaiBan l ON b.maLoaiBan = l.maLoaiBan";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maBanResult = rs.getString("maBan");
                String viTriResult = rs.getString("viTri");
                String moTa = rs.getString("moTa");
                String tangResult = rs.getString("tang");
                String trangThai = rs.getString("trangThai");
                String tenLoaiBan = rs.getString("tenLoaiBan");

                BanAn ban = new BanAn(maBanResult, viTriResult, moTa, tangResult, trangThai, new LoaiBan(tenLoaiBan));
                danhSachBanAn.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachBanAn;
    }








//    public void deleteBanDuy(String maBan) throws SQLException {
//        String sql = "DELETE FROM BanAn WHERE maBan = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, maBan);
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected == 0) {
//                throw new SQLException("Không tìm thấy bàn với mã: " + maBan);
//            }
//        }
//    }
    
    // Chức năng xóa bàn:
    public boolean isBanHasHoaDon(String maBan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE maBan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có hóa đơn, trả về true
            }
        }
        return false; // Không có hóa đơn nào
    }
    public void updateMaBanInHoaDon(String maBan) throws SQLException {
        String sqlSelect = "SELECT TOP 1 maBan FROM BanAn"; // Lấy maBan đầu tiên
        String sqlUpdate = "UPDATE HoaDon SET maBan = ? WHERE maBan = ?"; // Cập nhật maBan

        try (PreparedStatement selectStmt = connection.prepareStatement(sqlSelect);
             ResultSet rs = selectStmt.executeQuery()) {

            if (rs.next()) {
                String newMaBan = rs.getString("maBan"); // Lấy maBan đầu tiên

                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                    updateStmt.setString(1, newMaBan); // Cập nhật maBan mới
                    updateStmt.setString(2, maBan); // Điều kiện cập nhật
                    updateStmt.executeUpdate(); // Thực hiện cập nhật
                }
            } else {
                // Không có maBan nào trong bảng BanAn
                System.out.println("Không tìm thấy bàn nào trong bảng BanAn.");
            }
        }
    }

    public void deleteBanDuy(String maBan) throws SQLException {
        String sql = "DELETE FROM BanAn WHERE maBan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy bàn với mã: " + maBan);
            }
        }
    }
    


    public void updateBanDuy(BanAn banAn) throws SQLException {
        String sql = "UPDATE BanAn SET viTri = ?, mota = ?, maLoaiBan = ? , trangThai = ? WHERE maBan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, banAn.getViTri());
            stmt.setString(2, banAn.getMoTa());
    //        stmt.setString(3, banAn.getTang());
            stmt.setString(3, banAn.getLoaiBan().getMaLoaiBan());
            stmt.setString(4,banAn.getTrangThaiBanAn());
            stmt.setString(5, banAn.getMaBan());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sửa bàn thành công!");
            } else {
                System.out.println("Không có bản ghi nào bị ảnh hưởng!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật bàn: " + e.getMessage());
            throw e;
        }
    }



    // Phương thức để lấy mã loại bàn từ tên loại bàn
    // chưa dùng
    public LoaiBan getMaLoaiBanDuy(String tenLoaiBan) throws SQLException {
        String sql = "SELECT * FROM LoaiBan WHERE tenLoaiBan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tenLoaiBan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LoaiBan lb = new LoaiBan();
                    lb.setMaLoaiBan(rs.getString("maLoaiBan"));
                    lb.setTenLoaiBan(rs.getString("tenLoaiBan"));
                    return lb;
                }
            }
        }
        return null; // Nếu không tìm thấy
    }




    public void updateTrangThaiDuy(BanAn banAn) throws SQLException {
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, banAn.getTrangThaiBanAn());
            stmt.setString(2, banAn.getMaBan());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cập nhật trạng thái bàn thành công!");
            } else {
                System.out.println("Không có bản ghi nào bị ảnh hưởng!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật trạng thái bàn: " + e.getMessage());
            throw e;
        }
    }

    
    
    
     // Hàm lấy danh sách tất cả các trạng thái từ cơ sở dữ liệu
    public String[] layTatCaTrangThai() throws SQLException {
        List<String> danhSachTrangThai = new ArrayList<>();
        String sql = "SELECT DISTINCT trangThai FROM BanAn";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            danhSachTrangThai.add(rs.getString("trangThai"));
        }
        // Chuyển danh sách thành mảng
        return danhSachTrangThai.toArray(new String[0]);
    }
    
    //Duyyyyyyyyyyyyyyyyyyyyyyyyyyyyy ___ kết thúc:
    
    //
    
    public boolean gopBan(String maBanChinh, String maBanPhu) throws SQLException {
        Connection conn = null;
        PreparedStatement psChuyenMon = null;
        PreparedStatement psCapNhatTrangThai = null;

        try {
            conn = connectDB.ConnectJDBC.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Lấy mã hóa đơn của bàn chính và bàn phụ
            String sqlLayHoaDon = "SELECT maHoaDon FROM HoaDon WHERE maBan = ?";
            String maHoaDonChinh = null;
            String maHoaDonPhu = null;

            try (PreparedStatement psLayHoaDon = conn.prepareStatement(sqlLayHoaDon)) {
                psLayHoaDon.setString(1, maBanChinh);
                ResultSet rs = psLayHoaDon.executeQuery();
                if (rs.next()) {
                    maHoaDonChinh = rs.getString("maHoaDon");
                }

                psLayHoaDon.setString(1, maBanPhu);
                rs = psLayHoaDon.executeQuery();
                if (rs.next()) {
                    maHoaDonPhu = rs.getString("maHoaDon");
                }
            }

            if (maHoaDonChinh == null || maHoaDonPhu == null) {
                throw new SQLException("Không tìm thấy hóa đơn của một trong hai bàn.");
            }

            // Chuyển món ăn từ hóa đơn phụ sang hóa đơn chính
            String sqlChuyenMon = "UPDATE ChiTietHoaDon SET maHoaDon = ? WHERE maHoaDon = ?";
            psChuyenMon = conn.prepareStatement(sqlChuyenMon);
            psChuyenMon.setString(1, maHoaDonChinh);
            psChuyenMon.setString(2, maHoaDonPhu);
            psChuyenMon.executeUpdate();

            // Cập nhật trạng thái bàn phụ thành "Trống"
            String sqlCapNhatTrangThai = "UPDATE BanAn SET trangThai = N'Trống' WHERE maBan = ?";
            psCapNhatTrangThai = conn.prepareStatement(sqlCapNhatTrangThai);
            psCapNhatTrangThai.setString(1, maBanPhu);
            psCapNhatTrangThai.executeUpdate();

            conn.commit(); // Xác nhận giao dịch
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
            }
            throw e;
        } finally {
            if (psChuyenMon != null) psChuyenMon.close();
            if (psCapNhatTrangThai != null) psCapNhatTrangThai.close();
            if (conn != null) conn.close();
        }
    }

    public BanAn layThongTinBanAnChinh(String maBan) throws SQLException {
        String sql = "SELECT * FROM BanAn WHERE maBan = ?";
        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BanAn banAn = new BanAn();
                    banAn.setMaBan(rs.getString("maBan"));
                    banAn.setTang( rs.getString("tang"));
                    banAn.setViTri(rs.getString("viTri"));
                    banAn.setTrangThaiBanAn(rs.getString("trangThai"));
                    return banAn;
                }
            }
        }
        return null; 
    }


    
    public boolean gopBan1(String maBanChinh, String maBanPhu) throws SQLException {
        Connection conn = null;
        PreparedStatement psLayHoaDon = null;
        PreparedStatement psLayMonAn = null;
        PreparedStatement psKiemTraMonAn = null;
        PreparedStatement psThemMonAn = null;
        PreparedStatement psCapNhatTrangThaiBan = null;
        PreparedStatement psCapNhatMoTaBan = null; // Thêm PreparedStatement cho cập nhật mô tả

        try {
            conn = connectDB.ConnectJDBC.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Lấy mã hóa đơn của bàn chính và bàn phụ
            String sqlLayHoaDon = "SELECT maHoaDon FROM HoaDon WHERE maBan = ?";
            String maHoaDonChinh = null;
            String maHoaDonPhu = null;

            // Lấy hóa đơn của bàn chính
            psLayHoaDon = conn.prepareStatement(sqlLayHoaDon);
            psLayHoaDon.setString(1, maBanChinh);
            ResultSet rs = psLayHoaDon.executeQuery();
            if (rs.next()) {
                maHoaDonChinh = rs.getString("maHoaDon");
            }

            // Lấy hóa đơn của bàn phụ
            psLayHoaDon.setString(1, maBanPhu);
            rs = psLayHoaDon.executeQuery();
            if (rs.next()) {
                maHoaDonPhu = rs.getString("maHoaDon");
            }

            if (maHoaDonChinh == null || maHoaDonPhu == null) {
                throw new SQLException("Không tìm thấy hóa đơn của một trong hai bàn.");
            }

            // Lấy danh sách món ăn từ hóa đơn phụ và chuyển sang bàn chính
            String sqlLayMonAn = "SELECT maMonAn, soLuongMonAn FROM ChiTietHoaDon WHERE maHoaDon = ?";
            psLayMonAn = conn.prepareStatement(sqlLayMonAn);
            psLayMonAn.setString(1, maHoaDonPhu);
            ResultSet rsMonAnPhu = psLayMonAn.executeQuery();

            while (rsMonAnPhu.next()) {
                String maMonAn = rsMonAnPhu.getString("maMonAn");
                int soLuongMonAn = rsMonAnPhu.getInt("soLuongMonAn");

                // Kiểm tra món ăn đã tồn tại trong hóa đơn chính chưa
                String sqlKiemTraMonAnTrongHoaDonChinh = "SELECT soLuongMonAn FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
                psKiemTraMonAn = conn.prepareStatement(sqlKiemTraMonAnTrongHoaDonChinh);
                psKiemTraMonAn.setString(1, maHoaDonChinh);
                psKiemTraMonAn.setString(2, maMonAn);
                ResultSet rsKiemTra = psKiemTraMonAn.executeQuery();

                if (rsKiemTra.next()) {
                    // Món ăn đã có trong hóa đơn chính, cộng dồn số lượng
                    int soLuongHienTai = rsKiemTra.getInt("soLuongMonAn");
                    String sqlCapNhatSoLuong = "UPDATE ChiTietHoaDon SET soLuongMonAn = ? WHERE maHoaDon = ? AND maMonAn = ?";
                    psThemMonAn = conn.prepareStatement(sqlCapNhatSoLuong);
                    psThemMonAn.setInt(1, soLuongHienTai + soLuongMonAn); // Cộng thêm số lượng từ bàn phụ
                    psThemMonAn.setString(2, maHoaDonChinh);
                    psThemMonAn.setString(3, maMonAn);
                    psThemMonAn.executeUpdate();
                } else {
                    // Món ăn chưa có trong hóa đơn chính, thêm mới
                    String sqlThemMonAnMoi = "INSERT INTO ChiTietHoaDon(maHoaDon, maMonAn, soLuongMonAn) VALUES (?, ?, ?)";
                    psThemMonAn = conn.prepareStatement(sqlThemMonAnMoi);
                    psThemMonAn.setString(1, maHoaDonChinh);
                    psThemMonAn.setString(2, maMonAn);
                    psThemMonAn.setInt(3, soLuongMonAn);
                    psThemMonAn.executeUpdate();
                }
            }

            // Cập nhật mô tả của bàn chính và bàn phụ
            String moTaBanChinh = "Bàn ăn chính số " + maBanChinh + " Đang được gộp với bàn phụ " + maBanPhu;
            String sqlCapNhatMoTaBan = "UPDATE BanAn SET moTa = ? WHERE maBan = ?";
            psCapNhatMoTaBan = conn.prepareStatement(sqlCapNhatMoTaBan);
            psCapNhatMoTaBan.setString(1, moTaBanChinh);
            psCapNhatMoTaBan.setString(2, maBanChinh);
            psCapNhatMoTaBan.executeUpdate();
            
            // Cập nhật mô tả cho bàn phụ
            String sqlCapNhatMoTaBanPhu = "UPDATE BanAn SET moTa = ? WHERE maBan = ?";
            psCapNhatTrangThaiBan = conn.prepareStatement(sqlCapNhatMoTaBanPhu);
            psCapNhatTrangThaiBan.setString(1, "Đang được gộp với bàn chính " + maBanChinh);
            psCapNhatTrangThaiBan.setString(2, maBanPhu);
            psCapNhatTrangThaiBan.executeUpdate();


            // Cập nhật hóa đơn chính nếu cần thiết (ví dụ, tổng giá trị, trạng thái hoặc các thông tin khác)
            String sqlCapNhatHoaDonChinh = 
                "UPDATE HoaDon " +
                "SET tongTien = (SELECT SUM(ct.soLuongMonAn * m.giaMonAn) " +
                                "FROM ChiTietHoaDon ct " +
                                "JOIN MonAn m ON ct.maMonAn = m.maMonAn " +
                                "WHERE ct.maHoaDon = HoaDon.maHoaDon) " +
                "WHERE maHoaDon = ?";
            psThemMonAn = conn.prepareStatement(sqlCapNhatHoaDonChinh);
            psThemMonAn.setString(1, maHoaDonChinh);
            int rowsUpdatedHoaDon = psThemMonAn.executeUpdate();
            if (rowsUpdatedHoaDon > 0) {
                System.out.println("Cập nhật hóa đơn chính thành công.");
            } else {
                System.out.println("Cập nhật hóa đơn chính thất bại.");
            }

            // Cam kết các thay đổi
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
            }
            throw e;
        } finally {
            if (psLayHoaDon != null) psLayHoaDon.close();
            if (psLayMonAn != null) psLayMonAn.close();
            if (psKiemTraMonAn != null) psKiemTraMonAn.close();
            if (psThemMonAn != null) psThemMonAn.close();
            if (psCapNhatTrangThaiBan != null) psCapNhatTrangThaiBan.close();
            if (psCapNhatMoTaBan != null) psCapNhatMoTaBan.close(); // Đảm bảo đóng PreparedStatement
            if (conn != null) conn.close();
        }
    }

    
 
    
//public boolean gopBan1(String maBanChinh, String maBanPhu) throws SQLException {
//    Connection conn = null;
//    PreparedStatement psLayHoaDon = null;
//    PreparedStatement psLayMonAn = null;
//    PreparedStatement psKiemTraMonAn = null;
//    PreparedStatement psThemMonAn = null;
//    PreparedStatement psCapNhatTrangThaiBan = null;
//
//    try {
//        conn = connectDB.ConnectJDBC.getConnection();
//        conn.setAutoCommit(false); // Bắt đầu giao dịch
//
//        // Lấy mã hóa đơn của bàn chính và bàn phụ
//        String sqlLayHoaDon = "SELECT maHoaDon FROM HoaDon WHERE maBan = ?";
//        String maHoaDonChinh = null;
//        String maHoaDonPhu = null;
//
//        // Lấy hóa đơn của bàn chính
//        psLayHoaDon = conn.prepareStatement(sqlLayHoaDon);
//        psLayHoaDon.setString(1, maBanChinh);
//        ResultSet rs = psLayHoaDon.executeQuery();
//        if (rs.next()) {
//            maHoaDonChinh = rs.getString("maHoaDon");
//        }
//
//        // Lấy hóa đơn của bàn phụ
//        psLayHoaDon.setString(1, maBanPhu);
//        rs = psLayHoaDon.executeQuery();
//        if (rs.next()) {
//            maHoaDonPhu = rs.getString("maHoaDon");
//        }
//
//        if (maHoaDonChinh == null || maHoaDonPhu == null) {
//            throw new SQLException("Không tìm thấy hóa đơn của một trong hai bàn.");
//        }
//
//        // Lấy danh sách món ăn từ hóa đơn phụ và chuyển sang bàn chính
//        String sqlLayMonAn = "SELECT maMonAn, soLuongMonAn FROM ChiTietHoaDon WHERE maHoaDon = ?";
//        psLayMonAn = conn.prepareStatement(sqlLayMonAn);
//        psLayMonAn.setString(1, maHoaDonPhu);
//        ResultSet rsMonAnPhu = psLayMonAn.executeQuery();
//
//        while (rsMonAnPhu.next()) {
//            String maMonAn = rsMonAnPhu.getString("maMonAn");
//            int soLuongMonAn = rsMonAnPhu.getInt("soLuongMonAn");
//
////            // Kiểm tra món ăn đã tồn tại trong hóa đơn chính chưa
////            String sqlKiemTraMonAnTrongHoaDonChinh = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
////            psKiemTraMonAn = conn.prepareStatement(sqlKiemTraMonAnTrongHoaDonChinh);
////            psKiemTraMonAn.setString(1, maHoaDonChinh);
////            psKiemTraMonAn.setString(2, maMonAn);
////            ResultSet rsKiemTra = psKiemTraMonAn.executeQuery();
////
////            if (rsKiemTra.next() && rsKiemTra.getInt(1) > 0) {
////                // Món ăn đã có trong hóa đơn chính, chỉ cần cập nhật số lượng
////                String sqlCapNhatSoLuong = "UPDATE ChiTietHoaDon SET soLuongMonAn = soLuongMonAn + ? WHERE maHoaDon = ? AND maMonAn = ?";
////                psThemMonAn = conn.prepareStatement(sqlCapNhatSoLuong);
////                psThemMonAn.setInt(1, soLuongMonAn);
////                psThemMonAn.setString(2, maHoaDonChinh);
////                psThemMonAn.setString(3, maMonAn);
////                int rowsUpdated = psThemMonAn.executeUpdate();
////                if (rowsUpdated > 0) {
////                    System.out.println("Cập nhật món ăn " + maMonAn + " vào hóa đơn chính thành công.");
////                } else {
////                    System.out.println("Cập nhật món ăn " + maMonAn + " thất bại.");
////                }
////            } else {
////                // Món ăn chưa có trong hóa đơn chính, thêm món ăn vào hóa đơn chính
////                String sqlThemMonAnMoi = "INSERT INTO ChiTietHoaDon(maHoaDon, maMonAn, soLuongMonAn) VALUES (?, ?, ?)";
////                psThemMonAn = conn.prepareStatement(sqlThemMonAnMoi);
////                psThemMonAn.setString(1, maHoaDonChinh);
////                psThemMonAn.setString(2, maMonAn);
////                psThemMonAn.setInt(3, soLuongMonAn);
////                int rowsInserted = psThemMonAn.executeUpdate();
////                if (rowsInserted > 0) {
////                    System.out.println("Thêm món ăn " + maMonAn + " vào hóa đơn chính thành công.");
////                } else {
////                    System.out.println("Thêm món ăn " + maMonAn + " thất bại.");
////                }
////            }
//                // Kiểm tra món ăn đã tồn tại trong hóa đơn chính chưa
//                String sqlKiemTraMonAnTrongHoaDonChinh = "SELECT soLuongMonAn FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMonAn = ?";
//                psKiemTraMonAn = conn.prepareStatement(sqlKiemTraMonAnTrongHoaDonChinh);
//                psKiemTraMonAn.setString(1, maHoaDonChinh);
//                psKiemTraMonAn.setString(2, maMonAn);
//                ResultSet rsKiemTra = psKiemTraMonAn.executeQuery();
//
//                if (rsKiemTra.next()) {
//                    // Món ăn đã có trong hóa đơn chính, cộng dồn số lượng
//                    int soLuongHienTai = rsKiemTra.getInt("soLuongMonAn");
//                    String sqlCapNhatSoLuong = "UPDATE ChiTietHoaDon SET soLuongMonAn = ? WHERE maHoaDon = ? AND maMonAn = ?";
//                    psThemMonAn = conn.prepareStatement(sqlCapNhatSoLuong);
//                    psThemMonAn.setInt(1, soLuongHienTai + soLuongMonAn); // Cộng thêm số lượng từ bàn phụ
//                    psThemMonAn.setString(2, maHoaDonChinh);
//                    psThemMonAn.setString(3, maMonAn);
//                    psThemMonAn.executeUpdate();
//                } else {
//                    // Món ăn chưa có trong hóa đơn chính, thêm mới
//                    String sqlThemMonAnMoi = "INSERT INTO ChiTietHoaDon(maHoaDon, maMonAn, soLuongMonAn) VALUES (?, ?, ?)";
//                    psThemMonAn = conn.prepareStatement(sqlThemMonAnMoi);
//                    psThemMonAn.setString(1, maHoaDonChinh);
//                    psThemMonAn.setString(2, maMonAn);
//                    psThemMonAn.setInt(3, soLuongMonAn);
//                    psThemMonAn.executeUpdate();
//                }
//        }
//        
//        
//
//        // Cập nhật hóa đơn chính nếu cần thiết (ví dụ, tổng giá trị, trạng thái hoặc các thông tin khác)
//       String sqlCapNhatHoaDonChinh = 
//            "UPDATE HoaDon " +
//            "SET tongTien = (SELECT SUM(ct.soLuongMonAn * m.giaMonAn) " +
//                            "FROM ChiTietHoaDon ct " +
//                            "JOIN MonAn m ON ct.maMonAn = m.maMonAn " +
//                            "WHERE ct.maHoaDon = HoaDon.maHoaDon) " +
//            "WHERE maHoaDon = ?";
//        psThemMonAn = conn.prepareStatement(sqlCapNhatHoaDonChinh);
//        psThemMonAn.setString(1, maHoaDonChinh);
//        int rowsUpdatedHoaDon = psThemMonAn.executeUpdate();
//        if (rowsUpdatedHoaDon > 0) {
//            System.out.println("Cập nhật hóa đơn chính thành công.");
//        } else {
//            System.out.println("Cập nhật hóa đơn chính thất bại.");
//        }
//
//
//
////        // Cập nhật trạng thái bàn phụ thành "Trống" sau khi gộp bàn
////        String sqlCapNhatTrangThai = "UPDATE BanAn SET trangThai = N'Trống' WHERE maBan = ?";
////        psCapNhatTrangThaiBan = conn.prepareStatement(sqlCapNhatTrangThai);
////        psCapNhatTrangThaiBan.setString(1, maBanPhu);
////        psCapNhatTrangThaiBan.executeUpdate();
//
//        // Cam kết các thay đổi
//        conn.commit();
//        return true;
//    } catch (SQLException e) {
//        if (conn != null) {
//            conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
//        }
//        throw e;
//    } finally {
//        if (psLayHoaDon != null) psLayHoaDon.close();
//        if (psLayMonAn != null) psLayMonAn.close();
//        if (psKiemTraMonAn != null) psKiemTraMonAn.close();
//        if (psThemMonAn != null) psThemMonAn.close();
//        if (psCapNhatTrangThaiBan != null) psCapNhatTrangThaiBan.close();
//        if (conn != null) conn.close();
//    }
//}

    
    public HoaDon layTrangThaiBanNha(String maBan) {
       String sql = "SELECT a.*, b.* FROM BanAn a JOIN HoaDon b ON a.maBan = b.maBan WHERE a.trangThai LIKE N'%Trống%' AND b.maBan = ?";
       try (Connection conn = connectDB.ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

           stmt.setString(1, maBan); // Gán giá trị maBan vào câu truy vấn
           System.out.println("Query: " + stmt.toString()); // Debug câu truy vấn

           try (ResultSet rs = stmt.executeQuery()) {
               if (rs.next()) {
                   BanAn ba = new BanAn();
                   ba.setMaBan(rs.getString("maBan"));
                   ba.setTrangThaiBanAn(rs.getString("trangThai"));
                   HoaDon hoaDon = new HoaDon();
                   hoaDon.setBanAn(ba);

                   System.out.println("BanAn: " + ba.getMaBan() + ", TrangThai: " + ba.getTrangThaiBanAn());
                   return hoaDon;
               } else {
                   System.out.println("Không tìm thấy bàn với trạng thái 'Trống' và mã bàn: " + maBan);
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }
    
    public HoaDon layTrangThaiBanNha1(String maBanChinh, String maBanPhu) {
        // Câu truy vấn tìm bàn phụ có mô tả là "Đang được gộp với bàn chính" và khác với mã bàn chính
        String sql = "SELECT a.*, b.* FROM BanAn a JOIN HoaDon b ON a.maBan = b.maBan "
                   + "WHERE b.maBan = ? AND a.trangThai LIKE N'%Đang sử dụng%' "
                   + "AND a.moTa LIKE N'%Đang được gộp với bàn chính%' "
                   + "AND a.maBan <> ?"; // Kiểm tra mã bàn không giống với mã bàn chính

        try (Connection conn = connectDB.ConnectJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số
            stmt.setString(1, maBanPhu);  // Gán maBanPhu vào tham số đầu tiên
            stmt.setString(2, maBanChinh); // Gán maBanChinh vào tham số thứ hai

            System.out.println("Query: " + stmt.toString()); // Debug câu truy vấn

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Nếu có kết quả, tạo đối tượng HoaDon và trả về
                    BanAn ba = new BanAn();
                    ba.setMaBan(rs.getString("maBan"));
                    ba.setTrangThaiBanAn(rs.getString("trangThai"));
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setBanAn(ba);

                    System.out.println("BanAn: " + ba.getMaBan() + ", TrangThai: " + ba.getTrangThaiBanAn());
                    return hoaDon;
                } else {
                    System.out.println("Không tìm thấy bàn phụ với thông tin gộp.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    
//    public HoaDon layTrangThaiBanNha1(String maBanChinh, String maBanPhu) {
//        // Câu truy vấn tìm bàn phụ có mô tả là "Đang được gộp với bàn chính"
//        String sql = "SELECT a.*, b.* FROM BanAn a JOIN HoaDon b ON a.maBan = b.maBan "
//                   + "WHERE b.maBan = ? AND a.trangThai LIKE N'%Đang sử dụng%' "
//                   + "AND a.moTa LIKE N'%Đang được gộp với bàn chính " + maBanChinh + "%'";
//
//        try (Connection conn = connectDB.ConnectJDBC.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, maBanPhu); // Gán giá trị maBanPhu vào câu truy vấn
//            System.out.println("Query: " + stmt.toString()); // Debug câu truy vấn
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    // Nếu bàn phụ đã được gộp với bàn chính và đang sử dụng
//                    BanAn ba = new BanAn();
//                    ba.setMaBan(rs.getString("maBan"));
//                    ba.setTrangThaiBanAn(rs.getString("trangThai"));
//                    HoaDon hoaDon = new HoaDon();
//                    hoaDon.setBanAn(ba);
//
//                    System.out.println("BanAn: " + ba.getMaBan() + ", TrangThai: " + ba.getTrangThaiBanAn());
//                    return hoaDon;
//                } else {
//                    System.out.println("Bàn phụ chưa được gộp hoặc không có thông tin bàn chính.");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }




    // Phương thức lấy danh sách các bàn đang được gộp
    public List<String> getDanhSachBanDuocGop() {
        List<String> danhSachBanDuocGop = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Kết nối tới cơ sở dữ liệu
            conn = connectDB.ConnectJDBC.getConnection();

            // Câu lệnh SQL để lấy thông tin bàn gộp
            String sql = "SELECT maBan, moTa FROM BanAn WHERE moTa LIKE ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%Đang được gộp với%");

            rs = ps.executeQuery();

            // Duyệt qua kết quả và thêm vào danh sách
            while (rs.next()) {
                String maBan = rs.getString("maBan");
                String moTa = rs.getString("moTa");
                danhSachBanDuocGop.add("Bàn " + maBan + ": " + moTa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng các kết nối sau khi xong
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return danhSachBanDuocGop;
    }
    
}
