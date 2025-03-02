package modal;

import java.util.Objects;

public class DanhMucMonAn {
	
	private String maDanhMuc;
	private String tenDanhMuc;
	
	
	public DanhMucMonAn() {
		super();
	}
	
	public DanhMucMonAn(String maDanhMuc) {
		super();
		this.maDanhMuc = maDanhMuc;
	}
	
	
	public DanhMucMonAn(String maDanhMuc, String tenDanhMuc) {
		super();
		this.maDanhMuc = maDanhMuc;
		this.tenDanhMuc = tenDanhMuc;
	}

	public String getMaDanhMuc() {
		return maDanhMuc;
	}

	public void setMaDanhMuc(String maDanhMuc) {
		this.maDanhMuc = maDanhMuc;
	}

	public String getTenDanhMuc() {
		return tenDanhMuc;
	}

	public void setTenDanhMuc(String tenDanhMuc) {
		this.tenDanhMuc = tenDanhMuc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maDanhMuc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		DanhMucMonAn other = (DanhMucMonAn) obj;
		return Objects.equals(maDanhMuc, other.maDanhMuc);
	}
	
	
	
	
	
	

}
