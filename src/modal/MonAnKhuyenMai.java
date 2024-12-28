package modal;

public class MonAnKhuyenMai {
	
	private Integer soLuongToiThieu;
	private Double phanTramGiamGia;
	private MonAn monAn;
	private KhuyenMai khuyenMai;
	
	
	public MonAnKhuyenMai() {
		super();
	}
	
	public MonAnKhuyenMai(MonAn monAn, KhuyenMai khuyenMai) {
		super();
		this.monAn = monAn;
		this.khuyenMai = khuyenMai;
	}
	
	public MonAnKhuyenMai(Integer soLuongToiThieu, Double phanTramGiamGia, MonAn monAn, KhuyenMai khuyenMai) {
		super();
		this.soLuongToiThieu = soLuongToiThieu;
		this.phanTramGiamGia = phanTramGiamGia;
		this.monAn = monAn;
		this.khuyenMai = khuyenMai;
	}

	public Integer getSoLuongToiThieu() {
		return soLuongToiThieu;
	}

	public void setSoLuongToiThieu(Integer soLuongToiThieu) {
		this.soLuongToiThieu = soLuongToiThieu;
	}

	public Double getPhanTramGiamGia() {
		return phanTramGiamGia;
	}

	public void setPhanTramGiamGia(Double phanTramGiamGia) {
		this.phanTramGiamGia = phanTramGiamGia;
	}

	public MonAn getMonAn() {
		return monAn;
	}

	public void setMonAn(MonAn monAn) {
		this.monAn = monAn;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	
	
	

}
