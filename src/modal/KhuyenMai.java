package modal;

import java.time.LocalDateTime;
import java.util.Objects;

public class KhuyenMai {
	
	private String maKhuyenMai;
	private String tenKhuyenMai;
	private String moTaKhuyenMai;
	private LocalDateTime ngayBatDau;
	private LocalDateTime ngayKetThuc;
	private String trangThai;
	private double giaTriKhuyenMai;
	private String loaiKhuyenMai;
	private double diemYeuCau;
	private Integer soLuongToiThieu;
	private QuanLy quanLy;
	
	
	public KhuyenMai() {
		super();
	}
	
	
	public KhuyenMai(String maKhuyenMai) {
		super();
		this.maKhuyenMai = maKhuyenMai;
	}
	
	
	
	
	public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String moTaKhuyenMai, LocalDateTime ngayBatDau,
			LocalDateTime ngayKetThuc, String trangThai, double giaTriKhuyenMai, String loaiKhuyenMai, double diemYeuCau,
			Integer soLuongToiThieu, QuanLy quanLy) {
		super();
		this.maKhuyenMai = maKhuyenMai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.moTaKhuyenMai = moTaKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.trangThai = trangThai;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
		this.loaiKhuyenMai = loaiKhuyenMai;
		this.diemYeuCau = diemYeuCau;
		this.soLuongToiThieu = soLuongToiThieu;
		this.quanLy = quanLy;
	}
	
	
	public String getMaKhuyenMai() {
		return maKhuyenMai;
	}
	public void setMaKhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}
	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}
	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
	}
	public String getMoTaKhuyenMai() {
		return moTaKhuyenMai;
	}
	public void setMoTaKhuyenMai(String moTaKhuyenMai) {
		this.moTaKhuyenMai = moTaKhuyenMai;
	}
	public LocalDateTime getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(LocalDateTime ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}
	public LocalDateTime getNgayKetThuc() {
		return ngayKetThuc;
	}
	public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public double getGiaTriKhuyenMai() {
		return giaTriKhuyenMai;
	}
	public void setGiaTriKhuyenMai(double giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}
	public String getLoaiKhuyenMai() {
		return loaiKhuyenMai;
	}
	public void setLoaiKhuyenMai(String loaiKhuyenMai) {
		this.loaiKhuyenMai = loaiKhuyenMai;
	}
	public double getDiemYeuCau() {
		return diemYeuCau;
	}
	public void setDiemYeuCau(double diemYeuCau) {
		this.diemYeuCau = diemYeuCau;
	}
	public Integer getSoLuongToiThieu() {
		return soLuongToiThieu;
	}
	public void setSoLuongToiThieu(Integer soLuongToiThieu) {
		this.soLuongToiThieu = soLuongToiThieu;
	}
	public QuanLy getQuanLy() {
		return quanLy;
	}
	public void setQuanLy(QuanLy quanLy) {
		this.quanLy = quanLy;
	}


	@Override
	public int hashCode() {
		return Objects.hash(maKhuyenMai);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKhuyenMai, other.maKhuyenMai);
	}

	
	
	
	
	
}
