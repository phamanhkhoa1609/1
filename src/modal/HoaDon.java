package modal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HoaDon {
	
	private String maHoaDon;
	private Integer soLuongKhach;
	private String ghiChu;
	private String trangThaiThanhToan;
	private LocalDateTime ngayGioLap;
        private LocalDateTime ngayHuyDat;
        private LocalDateTime ngayGioNhanBan;
	private double chietKhau;
	private double VAT;
	private double tongTien;
	private NhanVien nhanVien;
	private KhachHang khachHang;
        private QuanLy quanLy; //Vừa sửa ngày 09/11
	private BanAn banAn;
	
	private List<ChiTietHoaDon> chiTietHoaDons =  new ArrayList<>();
	
	
	
	public HoaDon() {
		super();
	}
	
	public HoaDon(String maHoaDon) {
		super();
		this.maHoaDon = maHoaDon;
	}

        //Vừa sửa ngày 09/11
        public HoaDon(String maHoaDon, Integer soLuongKhach, String ghiChu, String trangThaiThanhToan, LocalDateTime ngayGioLap, LocalDateTime ngayHuyDat, LocalDateTime ngayGioNhanBan,double chietKhau, double VAT, double tongTien, NhanVien nhanVien, KhachHang khachHang, BanAn banAn, QuanLy quanLy) {
            this.maHoaDon = maHoaDon;
            this.soLuongKhach = soLuongKhach;
            this.ghiChu = ghiChu;
            this.trangThaiThanhToan = trangThaiThanhToan;
            this.ngayGioLap = ngayGioLap;
            this.ngayHuyDat = ngayHuyDat;
            this.ngayGioNhanBan = ngayGioNhanBan;
            this.chietKhau = chietKhau;
            this.VAT = VAT;
            this.tongTien = tongTien;
            this.nhanVien = nhanVien;
            this.khachHang = khachHang;
            this.banAn = banAn;
            this.quanLy = quanLy;
        }
	
	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public Integer getSoLuongKhach() {
		return soLuongKhach;
	}

	public void setSoLuongKhach(Integer soLuongKhach) {
		this.soLuongKhach = soLuongKhach;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public String getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void setTrangThaiThanhToan(String trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	public LocalDateTime getNgayGioLap() {
		return ngayGioLap;
	}

	public void setNgayGioLap(LocalDateTime ngayGioLap) {
		this.ngayGioLap = ngayGioLap;
	}

        public LocalDateTime getNgayHuyDat() {
            return ngayHuyDat;
        }

        public void setNgayHuyDat(LocalDateTime ngayHuyDat) {
            this.ngayHuyDat = ngayHuyDat;
        }

        public LocalDateTime getNgayGioNhanBan() {
            return ngayGioNhanBan;
        }

        public void setNgayGioNhanBan(LocalDateTime ngayGioNhanBan) {
            this.ngayGioNhanBan = ngayGioNhanBan;
        }

	public double getChietKhau() {
		return chietKhau;
	}

	public void setChietKhau(double chietKhau) {
		this.chietKhau = chietKhau;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public BanAn getBanAn() {
		return banAn;
	}

	public void setBanAn(BanAn banAn) {
		this.banAn = banAn;
	}

        //Vừa sửa ngày 09/11
    public QuanLy getQuanLy() {
        return quanLy;
    }

    public void setQuanLy(QuanLy quanLy) {
        this.quanLy = quanLy;
    }
        
        

	public List<ChiTietHoaDon> getChiTietHoaDons() {
		return chiTietHoaDons;
	}

	public void setChiTietHoaDons(List<ChiTietHoaDon> chiTietHoaDons) {
		this.chiTietHoaDons = chiTietHoaDons;
	}

	public void setVAT(double vAT) {
		this.VAT = vAT;
	}
    
	public void tinhChietKhau() {
		if(khachHang != null) {
			int diemTichLuy = khachHang.getDiemTichLuy();
			if(diemTichLuy >= 1000){
				/*
				 điểm tích lũy lấy từ khách hàng để tính chiết khấu
				 Ví dụ : Khách hàng có 1000 điểm = 1% ( áp dụng cho tổng hóa đơn )
				 * */
				chietKhau = diemTichLuy / 1000.0; 
				khachHang.setDiemTichLuy(diemTichLuy - (int)(chietKhau * 1000)); // số điểm tích lũy còn lại
			}
			else {
				chietKhau = 0;
			}
			    
		}else {
			chietKhau = 0;
		}
	}

	// Tính VAT mặc định là 10%
    public double getVAT() {
        return VAT; 
    }
    
    public double tinhThanhTien() {
    	double tongTien = 0.0;
    	for(ChiTietHoaDon it : chiTietHoaDons) {
    		tongTien += (it.getSoLuongMonAn() * it.getMonAn().getGiaMonAn());
    	}
    	return tongTien;
    }
    
//    public double tinhTongTien() {
//    	
//    	double giaTriSauChietKhau = tinhThanhTien() - (chietKhau / 100 * tinhThanhTien());
//    	double tinhVAT = (VAT/100) * giaTriSauChietKhau;
//    	double tongHoaDon = 0.0;
//    	if(khachHang != null) {
//    	     String ktKm = khachHang.getKmkh().getKhuyenMai().getLoaiKhuyenMai();
//    	     if(!ktKm.equals("")) {
//    	    	 Double phanTramKM = khachHang.getKmkh().getKhuyenMai().getGiaTriKhuyenMai();
//    	    	 tongHoaDon = giaTriSauChietKhau + tinhVAT - phanTramKM;
//    	     }else {
//    	    	 tongHoaDon = giaTriSauChietKhau + tinhVAT;
//    	     }
//    	}
//    	return tongHoaDon;
//    }
    //--
        public double tinhTongTien() {
    	
    	double giaTriSauChietKhau = tinhThanhTien() - (chietKhau / 100 * tinhThanhTien());
    	double tinhVAT = (VAT/100) * giaTriSauChietKhau;
    	double tongHoaDon = 0.0;
    	
    	tongHoaDon = giaTriSauChietKhau + tinhVAT;
  
    	return tongHoaDon;
    }

	@Override
	public int hashCode() {
		return Objects.hash(maHoaDon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHoaDon, other.maHoaDon);
	}
}
