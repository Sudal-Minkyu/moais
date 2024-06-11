package com.minkyu.moais.common;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// 공동 유틸리티
@Slf4j
public class CommonUtil {

    public static boolean isCheckYyyymmdd(String yyyymmdd) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(yyyymmdd);
        } catch (ParseException e) {
            return false; // 날짜 변환 실패 시 형식에 맞지 않는 것으로 판단
        }

        return true;

    }


}
