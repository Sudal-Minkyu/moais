package com.minkyu.moais.common;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DataResponse {

    Map<String, Object> res;

    public DataResponse(){
        this.res = new HashMap<>();
    }

    // 호출이 성공할시 해당함수 사용
    public Map<String, Object> success(HashMap<String,Object> response) {
        res.clear();
        res.put("response",response);
        res.put("status",200);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "SUCCESS");
        return this.res;
    }

    // 호출이 실패할시 해당함수 사용
    public Map<String, Object> fail(String err_code,String err_msg) {
        res.clear();
        res.put("status",500);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "Error");
        res.put("err_code", err_code);
        res.put("err_msg", err_msg);
        return this.res;
    }

}