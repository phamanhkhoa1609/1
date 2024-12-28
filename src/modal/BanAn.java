package modal;

import java.util.Objects;

public class BanAn {
    private String maBan;
    private String viTri;
    private String moTa;
    private String tang;
    private String trangThaiBanAn;
    private LoaiBan loaiBan;
	
    public BanAn() {
	super();
    }
	
    public BanAn(String maBan) {
	super();
	this.maBan = maBan;
    }

    //
    public BanAn(String maBan, String viTri, String moTa, String tang, String trangThaiBanAn, LoaiBan loaiBan) {
        this.maBan = maBan;
        this.viTri = viTri;
        this.moTa = moTa;
        this.tang = tang;
        this.trangThaiBanAn = trangThaiBanAn;
        this.loaiBan = loaiBan;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTang() {
        return tang;
    }

    public void setTang(String tang) {
        this.tang = tang;
    }

    public String getTrangThaiBanAn() {
        return trangThaiBanAn;
    }

    public void setTrangThaiBanAn(String trangThaiBanAn) {
        this.trangThaiBanAn = trangThaiBanAn;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }
        


    
    

    @Override
	public int hashCode() {
		return Objects.hash(maBan);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		BanAn other = (BanAn) obj;
		return Objects.equals(maBan, other.maBan);
	}

}
