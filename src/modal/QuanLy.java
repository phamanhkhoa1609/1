package modal;

import java.util.Date;
import java.util.Objects;

public class QuanLy {
	
	private String maQuanLy;
	private String ho;
	private String ten;
	private String email;
	private Date ngaySinh;
	private String gioiTinh;
	private String soDienThoai;
	private String diaChi;
	private boolean trangThai;
	private TaiKhoan taiKhoan;
	
	public QuanLy() {
		super();
	}
	
	public QuanLy(String maQuanLy) {
		super();
		this.maQuanLy = maQuanLy;
		
	}
	
	
	public QuanLy(String maQuanLy, String ho, String ten, String email, Date ngaySinh, String gioiTinh,
			String soDienThoai, String diaChi, boolean trangThai, TaiKhoan taiKhoan) {
		super();
		this.maQuanLy = maQuanLy;
		this.ho = ho;
		this.ten = ten;
		this.email = email;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
		this.trangThai = trangThai;
		this.taiKhoan = taiKhoan;
	}

	public String getMaQuanLy() {
		return maQuanLy;
	}

	public void setMaQuanLy(String maQuanLy) {
		this.maQuanLy = maQuanLy;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		this.ho = ho;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public int hashCode() {
		return Objects.hash(maQuanLy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		QuanLy other = (QuanLy) obj;
		return Objects.equals(maQuanLy, other.maQuanLy);
	}
	
	
	
	
	
}
