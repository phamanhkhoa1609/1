package modal;

import java.util.Objects;

public class LoaiBan {
	
	private String maLoaiBan;
	private String tenLoaiBan;
        private Integer soGhe;
	
	public LoaiBan() {
		super();
	}
	
        //
	public LoaiBan(String maLoaiBan, String tenLoaiBan) {
		super();
		this.maLoaiBan = maLoaiBan;
                this.tenLoaiBan = tenLoaiBan;
	}
        
        public LoaiBan(String maLoaiBan) {
		super();
		this.maLoaiBan = maLoaiBan;
	}

    public LoaiBan(String maLoaiBan, String tenLoaiBan, Integer soGhe) {
        this.maLoaiBan = maLoaiBan;
        this.tenLoaiBan = tenLoaiBan;
        this.soGhe = soGhe;

    }

    public String getMaLoaiBan() {
        return maLoaiBan;
    }

    public void setMaLoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    public String getTenLoaiBan() {
        return tenLoaiBan;
    }

    public void setTenLoaiBan(String tenLoaiBan) {
        this.tenLoaiBan = tenLoaiBan;
    }

    public Integer getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(Integer soGhe) {
        this.soGhe = soGhe;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
        final LoaiBan other = (LoaiBan) obj;
        return Objects.equals(this.maLoaiBan, other.maLoaiBan);
    }
	
	
	
        
	
	
	
	

}
