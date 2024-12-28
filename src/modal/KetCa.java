/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author ACER
 */
public class KetCa {
    
    private String maKetCa;
    private NhanVien nhanVien;
    private QuanLy quanLy;
    private LocalDateTime ngayGioBatDau;
    private LocalDateTime ngayGioKetThuc;
    private Double soDuDauCa;
    private Double soDuTienMat;
    private Double tongDoanhThu;
    private String ghiChu;
    
    public KetCa(String maKetCa) {
        this.maKetCa = maKetCa;
    }

    public KetCa(String maKetCa, NhanVien nhanVien, QuanLy quanLy, LocalDateTime ngayGioBatDau, LocalDateTime ngayGioKetThuc, Double soDuDauCa, Double soDuTienMat, Double tongDoanhThu, String ghiChu) {
        this.maKetCa = maKetCa;
        this.nhanVien = nhanVien;
        this.quanLy = quanLy;
        this.ngayGioBatDau = ngayGioBatDau;
        this.ngayGioKetThuc = ngayGioKetThuc;
        this.soDuDauCa = soDuDauCa;
        this.soDuTienMat = soDuTienMat;
        this.tongDoanhThu = tongDoanhThu;
        this.ghiChu = ghiChu;
    }

    public String getMaKetCa() {
        return maKetCa;
    }

    public void setMaKetCa(String maKetCa) {
        this.maKetCa = maKetCa;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public QuanLy getQuanLy() {
        return quanLy;
    }

    public void setQuanLy   (QuanLy quanLy) {
        this.quanLy = quanLy;
    }

    public LocalDateTime getNgayGioBatDau() {
        return ngayGioBatDau;
    }

    public void setNgayGioBatDau(LocalDateTime ngayGioBatDau) {
        this.ngayGioBatDau = ngayGioBatDau;
    }

    public LocalDateTime getNgayGioKetThuc() {
        return ngayGioKetThuc;
    }

    public void setNgayGioKetThuc(LocalDateTime ngayGioKetThuc) {
        this.ngayGioKetThuc = ngayGioKetThuc;
    }

    public Double getSoDuDauCa() {
        return soDuDauCa;
    }

    public void setSoDuDauCa(Double soDuDauCa) {
        this.soDuDauCa = soDuDauCa;
    }

    public Double getSoDuTienMat() {
        return soDuTienMat;
    }

    public void setSoDuTienMat(Double soDuTienMat) {
        this.soDuTienMat = soDuTienMat;
    }

    public Double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(Double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
        final KetCa other = (KetCa) obj;
        return Objects.equals(this.maKetCa, other.maKetCa);
    }
    
    
    
    
    
}
