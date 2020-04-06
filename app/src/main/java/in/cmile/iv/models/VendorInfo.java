package in.cmile.iv.models;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pintu on 2020-03-27
 */
public class VendorInfo implements Serializable {
    private Context context;
    private ImageView ivLogo;
    private String name;
    private boolean isShopOpen = false;
    private boolean isFavouriteShop;
    private String address;
    private String area;
    private String city;
    private String state;
    private String pincode;
    private String mobileNo;
    private String type;
    private String vendorId;
    private String status;
    private String openTime;
    private String closeTime;
    private String lattitude;
    private String longitude;
    private ArrayList<String> categoryItem;
    private ArrayList<VendorInfo> categoryArray;

    public VendorInfo() {
    }

    public VendorInfo(String name, String address, String area, String city, String state, String pincode, String mobileNo, String type, String vendorId, String status, String openTime, String closeTime, String lattitude, String longitude, ArrayList<String> categoryItems) {
        this.name = name;
        this.address = address;
        this.area = area;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.mobileNo = mobileNo;
        this.type = type;
        this.vendorId = vendorId;
        this.status = status;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.categoryItem = categoryItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isFavouriteShop() {
        return isFavouriteShop;
    }

    public void setFavouriteShop(boolean favouriteShop) {
        isFavouriteShop = favouriteShop;
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public void setIvLogo(ImageView ivLogo) {
        this.ivLogo = ivLogo;
    }

    public boolean isShopOpen() {
        return isShopOpen;
    }

    public void setShopOpen(boolean shopOpen) {
        isShopOpen = shopOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(ArrayList<String> categoryItem) {
        this.categoryItem = categoryItem;
    }
}
