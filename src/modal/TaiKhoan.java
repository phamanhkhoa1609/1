package modal;

import java.util.Date;
import java.util.Objects;

public class TaiKhoan {
	private String tenNguoiDung;
	private String matKhau;
	private String vaiTro;
	private Date ngayTao;
	
	public TaiKhoan() {
		super();
	}
	
	public TaiKhoan(String tenNguoiDung) {
		super();
		this.tenNguoiDung = tenNguoiDung;
	}
	
	public TaiKhoan(String tenNguoiDung, String matKhau, String vaiTro, Date ngayTao) {
		super();
		this.tenNguoiDung = tenNguoiDung;
		this.matKhau = matKhau;
		this.vaiTro = vaiTro;
		this.ngayTao = ngayTao;
	}

	public String getTenNguoiDung() {
		return tenNguoiDung;
	}

	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}

	public Date getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenNguoiDung);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(tenNguoiDung, other.tenNguoiDung);
	}
	
	
	

}
