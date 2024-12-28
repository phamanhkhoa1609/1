package modal;

import java.util.Objects;

public class MonAn {
	
	private String maMonAn;
	private String tenMonAn;
	private double giaMonAn;
	private Integer soLuong;
	private String donVi;
	private String moTaMonAn;
	private String ghiChu;
	private String anhMonAn;
	private boolean tinhTrangMon;
	private DanhMucMonAn danhMucMonAn;
        
        
        //,,
        MonAnKhuyenMai monAnKhuyenMai;
	
	
	
	public MonAn() {
		super();

	}
	
	
	public MonAn(String maMonAn) {
		super();
		this.maMonAn = maMonAn;
	}
	
	
	
	public MonAn(String maMonAn, String tenMonAn, double giaMonAn, Integer soLuong, String donVi, String moTaMonAn,
			String ghiChu, String anhMonAn, boolean tinhTrangMon, DanhMucMonAn danhMucMonAn) {
		super();
		this.maMonAn = maMonAn;
		this.tenMonAn = tenMonAn;
		this.giaMonAn = giaMonAn;
		this.soLuong = soLuong;
		this.donVi = donVi;
		this.moTaMonAn = moTaMonAn;
		this.ghiChu = ghiChu;
		this.anhMonAn = anhMonAn;
		this.tinhTrangMon = tinhTrangMon;
		this.danhMucMonAn = danhMucMonAn;
	}

    public MonAnKhuyenMai getMonAnKhuyenMai() {
        return monAnKhuyenMai;
    }

    public void setMonAnKhuyenMai(MonAnKhuyenMai monAnKhuyenMai) {
        this.monAnKhuyenMai = monAnKhuyenMai;
    }

        
        

	public String getMaMonAn() {
		return maMonAn;
	}


	public void setMaMonAn(String maMonAn) {
		this.maMonAn = maMonAn;
	}


	public String getTenMonAn() {
		return tenMonAn;
	}


	public void setTenMonAn(String tenMonAn) {
		this.tenMonAn = tenMonAn;
	}


	public double getGiaMonAn() {
		return giaMonAn;
	}


	public void setGiaMonAn(double giaMonAn) {
		this.giaMonAn = giaMonAn;
	}


	public Integer getSoLuong() {
		return soLuong;
	}


	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}


	public String getDonVi() {
		return donVi;
	}


	public void setDonVi(String donVi) {
		this.donVi = donVi;
	}


	public String getMoTaMonAn() {
		return moTaMonAn;
	}


	public void setMoTaMonAn(String moTaMonAn) {
		this.moTaMonAn = moTaMonAn;
	}


	public String getGhiChu() {
		return ghiChu;
	}


	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}


	public String getAnhMonAn() {
		return anhMonAn;
	}


	public void setAnhMonAn(String anhMonAn) {
		this.anhMonAn = anhMonAn;
	}


	public boolean isTinhTrangMon() {
		return tinhTrangMon;
	}


	public void setTinhTrangMon(boolean tinhTrangMon) {
		this.tinhTrangMon = tinhTrangMon;
	}


	public DanhMucMonAn getDanhMucMonAn() {
		return danhMucMonAn;
	}


	public void setDanhMucMonAn(DanhMucMonAn danhMucMonAn) {
		this.danhMucMonAn = danhMucMonAn;
	}


	@Override
	public int hashCode() {
		return Objects.hash(maMonAn);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		MonAn other = (MonAn) obj;
		return Objects.equals(maMonAn, other.maMonAn);
	}
	
	

	
	
	
}
