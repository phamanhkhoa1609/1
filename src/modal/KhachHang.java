package modal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class KhachHang {
	
	private String maKhachHang;
	private String tenKhachHang;
	private String soDienThoai;
	private Date ngayThamGia;
	private Integer diemTichLuy;
	private Boolean trangThai;
	private KhuyenMaiKhachHang kmkh;

    public KhuyenMaiKhachHang getKmkh() {
        return kmkh;
    }

    public void setKmkh(KhuyenMaiKhachHang kmkh) {
        this.kmkh = kmkh;
    }
	private List<HoaDon> hoaDons = new ArrayList<>();
	
	
	
	public KhachHang() {
		super();
	}
	
	public KhachHang(String maKhachHang) {
		super();
		this.maKhachHang = maKhachHang;
	}

    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai, Date ngayThamGia, Integer diemTichLuy, Boolean trangThai) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.ngayThamGia = ngayThamGia;
        this.diemTichLuy = diemTichLuy;
        this.trangThai = trangThai;
     
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Date getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(Date ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }

    public Integer getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(Integer diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

  

    public List<HoaDon> getHoaDons() {
        return hoaDons;
    }

    public void setHoaDons(List<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }
    
    
    
//    public int getDiemTichLuy() {
//        int diemTichLuy = 0;
//        for (HoaDon hoaDon : hoaDons) {
//        	// diem tích lũy sẽ là tổng hóa đơn 
//            diemTichLuy += hoaDon.getTongTien() ; 
//        }
//        return diemTichLuy;
//    }

	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}
	
    
    
    
	
	
	

	
	
	
}
