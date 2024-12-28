package modal;

public class HoaDonKhuyenMai {
	
	private double phanTramKhuyenMai;
	private HoaDon hoaDon;
	private KhuyenMai khuyenMai;
	
	
	public HoaDonKhuyenMai() {
		super();
	}
	
	public HoaDonKhuyenMai( HoaDon hoaDon, KhuyenMai khuyenMai) {
		super();
		this.hoaDon = hoaDon;
		this.khuyenMai = khuyenMai;
	}
	
	public HoaDonKhuyenMai(double phanTramKhuyenMai, HoaDon hoaDon, KhuyenMai khuyenMai) {
		super();
		this.phanTramKhuyenMai = phanTramKhuyenMai;
		this.hoaDon = hoaDon;
		this.khuyenMai = khuyenMai;
	}

	public double getPhanTramKhuyenMai() {
		return phanTramKhuyenMai;
	}

	public void setPhanTramKhuyenMai(double phanTramKhuyenMai) {
		this.phanTramKhuyenMai = phanTramKhuyenMai;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	
	
	
	

}
