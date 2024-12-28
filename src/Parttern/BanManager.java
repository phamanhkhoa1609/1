/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parttern;

/**
 *
 * @author ACER
 */
public class BanManager {
    private static BanManager instance;
    private String maBanChinh;
    private String maBanPhu;

    private BanManager() {}

    public static BanManager getInstance() {
        if (instance == null) {
            instance = new BanManager();
        }
        return instance;
    }

    public String getMaBanChinh() {
        return maBanChinh;
    }

    public void setMaBanChinh(String maBanChinh) {
        this.maBanChinh = maBanChinh;
    }

    public String getMaBanPhu() {
        return maBanPhu;
    }

    public void setMaBanPhu(String maBanPhu) {
        this.maBanPhu = maBanPhu;
    }
}
