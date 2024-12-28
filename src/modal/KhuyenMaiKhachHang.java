/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modal;

import java.util.Objects;

/**
 *
 * @author ADMIN
 */
public class KhuyenMaiKhachHang {
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;

    public KhuyenMaiKhachHang() {
        super();
    }

    public KhuyenMaiKhachHang(KhachHang khachHang, KhuyenMai khuyenMai) {
        super();
        this.khachHang = khachHang;
        this.khuyenMai = khuyenMai;
    }


    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.khachHang);
        hash = 41 * hash + Objects.hashCode(this.khuyenMai);
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
        final KhuyenMaiKhachHang other = (KhuyenMaiKhachHang) obj;
        if (!Objects.equals(this.khachHang, other.khachHang)) {
            return false;
        }
        return Objects.equals(this.khuyenMai, other.khuyenMai);
    }
    
}

