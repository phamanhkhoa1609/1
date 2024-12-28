package modal;

public class ChiTietHoaDon {
	
	private Integer soLuongMonAn;
	private double thanhTien;
	private HoaDon hoaDon;
	private MonAn monAn;
        //
	private MonAnKhuyenMai monAnKhuyenMai;
	
	public ChiTietHoaDon() {
		super();
	}
	
	public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn) {
		super();
		this.hoaDon = hoaDon;
		this.monAn = monAn;
	}
	
	
	public ChiTietHoaDon(Integer soLuongMonAn, double thanhTien, HoaDon hoaDon, MonAn monAn) {
		super();
		this.soLuongMonAn = soLuongMonAn;
		this.thanhTien = thanhTien;
		this.hoaDon = hoaDon;
		this.monAn = monAn;
	}
	
	public double tinhThanhTien() {
		return 0.0;
	}

	public Integer getSoLuongMonAn() {
		return soLuongMonAn;
	}

	public void setSoLuongMonAn(Integer soLuongMonAn) {
		this.soLuongMonAn = soLuongMonAn;
	}

	public double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public MonAn getMonAn() {
		return monAn;
	}

	public void setMonAn(MonAn monAn) {
		this.monAn = monAn;
	}

    public MonAnKhuyenMai getMonAnKhuyenMai() {
        return monAnKhuyenMai;
    }

    public void setMonAnKhuyenMai(MonAnKhuyenMai monAnKhuyenMai) {
        this.monAnKhuyenMai = monAnKhuyenMai;
    }
	
	
	
	
        
	
	
}
