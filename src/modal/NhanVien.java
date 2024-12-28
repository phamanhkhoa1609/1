package modal;

import java.util.Date;
import java.util.Objects;

public class NhanVien {
	
	private String maNhanVien;
	private String ho;
	private String ten;
	private Date ngaySinh;
	private String gioiTinh;
	private String soDienThoai;
	private String diaChi;
	private double luong;
	private boolean trangThai;
        private String caLam;
	private TaiKhoan taiKhoan;
	private QuanLy quanLy;
	
	
	
	public NhanVien() {
		super();
	}
	
	public NhanVien(String maNhanVien) {
		super();
		this.maNhanVien = maNhanVien;
	}
        
        // Constructor lấy thông tin cho "Lịch sử đặt bàn"
        public NhanVien(String maNhanVien, String ho, String ten) {
		super();
		this.maNhanVien = maNhanVien;
                this.ho = ho;
		this.ten = ten;
	}
	
	public NhanVien(String maNhanVien, String ho, String ten, Date ngaySinh, String gioiTinh, String soDienThoai,
			String diaChi, double luong, boolean trangThai,String caLam, TaiKhoan taiKhoan, QuanLy quanLy) {
		super();
		this.maNhanVien = maNhanVien;
		this.ho = ho;
		this.ten = ten;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
		this.luong = luong;
		this.trangThai = trangThai;
		this.taiKhoan = taiKhoan;
		this.quanLy = quanLy;
                this.caLam = caLam;
	}

	public String getmaNhanVien() {
		return maNhanVien;
	}

	public void setmaNhanVien(String maQuanLy) {
		this.maNhanVien = maQuanLy;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		this.ho = ho;
	}

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getCaLam() {
        return caLam;
    }

    public void setCaLam(String caLam) {
        this.caLam = caLam;
    }
        
        

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public double getLuong() {
		return luong;
	}

	public void setLuong(double luong) {
		this.luong = luong;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public TaiKhoan getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public QuanLy getQuanLy() {
		return quanLy;
	}

	public void setQuanLy(QuanLy quanLy) {
		this.quanLy = quanLy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}
	
	
	
	
	
	
	

}
