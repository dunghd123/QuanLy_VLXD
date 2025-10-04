package com.example.quanly_vlxd.help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VnProvinceHelper {
    private static final List<String> tinhMienBac = new ArrayList<>(Arrays.asList("Hà Nội", "Hải Phòng",
            "Quảng Ninh", "Bắc Giang", "Bắc Kạn", "Bắc Ninh",
            "Cao Bằng", "Điện Biên", "Hà Giang", "Hà Nam",
            "Hải Dương", "Hòa Bình", "Hưng Yên", "Lai Châu", "Lào Cai",
            "Lạng Sơn", "Nam Định", "Ninh Bình", "Phú Thọ", "Sơn La",
            "Thái Bình", "Thái Nguyên", "Tuyên Quang", "Vĩnh Phúc", "Yên Bái"));
    private static final List<String> tinhMienTrung = new ArrayList<>(Arrays.asList("Thanh Hóa",
            "Nghệ An", "Hà Tĩnh", "Quảng Bình", "Quảng Trị",
            "Thừa Thiên Huế", "Đà Nẵng", "Quảng Nam", "Quảng Ngãi",
            "Bình Định", "Phú Yên", "Khánh Hòa", "Ninh Thuận",
            "Bình Thuận", "Kon Tum", "Gia Lai", "Đắk Lắk", "Đắk Nông", "Lâm Đồng"));
    private static final List<String> tinhMienNam = new ArrayList<>(Arrays.asList("TP. Hồ Chí Minh", "Bình Dương", "Đồng Nai",
            "Bà Rịa - Vũng Tàu", "Bình Phước", "Tây Ninh", "Long An", "Tiền Giang",
            "Bến Tre", "Trà Vinh", "Vĩnh Long", "Đồng Tháp", "An Giang",
            "Kiên Giang", "Cần Thơ", "Sóc Trăng", "Bạc Liêu", "Cà Mau", "Hậu Giang"));


    public static String checkAddress(String address){
        String result="";
        for(String mb: tinhMienBac){
            if(address.contains(mb)){
                result="Miền Bắc";
                break;
            }
        }
        for(String mt: tinhMienTrung){
            if(address.contains(mt)){
                result="Miền Trung";
                break;
            }
        }
        for(String mb: tinhMienNam){
            if(address.contains(mb)){
                result="Miền Nam";
                break;
            }
        }
        return result;
    }
}
