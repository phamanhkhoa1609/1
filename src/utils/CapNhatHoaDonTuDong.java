/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author ADMIN
 */
import dao.BanAn_DAO;
import dao.HoaDon_DAO;
import java.sql.SQLException;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;
import modal.HoaDon;

public class CapNhatHoaDonTuDong {
    private Timer timer;
    private HoaDon_DAO hoaDonDAO;
    private BanAn_DAO banAnDAO;

    public CapNhatHoaDonTuDong() {
        timer = new Timer();
        hoaDonDAO = new HoaDon_DAO();
        banAnDAO = new BanAn_DAO();
    }

    public void batDau() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                capNhatHoaDonVaBan();
            }
        }, 0, 60000); // Chạy mỗi 1 phút
    }

    public void dung() {
        timer.cancel(); // Dừng công việc tự động
    }

    private void capNhatHoaDonVaBan() {
        List<HoaDon> ds =  hoaDonDAO.layDSMaBanQuaHanDat();

        if (!ds.isEmpty()) {
            for (HoaDon hd : ds) {
                try {
                    banAnDAO.capNhatTrangThaiBan(hd.getBanAn().getMaBan(), "Trống");
                    hoaDonDAO.capNhatTrangThaiHoaDonSauKhiHuy("Đã hủy", hd.getBanAn().getMaBan());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Không có mã bàn quá hạn.");
        }
    }
}




