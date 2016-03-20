package com.example.bl_uestc.sercurity_bysound;

import java.util.Map;

import com.duowan.mobile.netroid.AuthFailureError;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.request.StringRequest;

public class PostByParamsRequest  extends StringRequest{
	private Map<String, String> mParams;
	public PostByParamsRequest(String url, Map<String, String> params,Listener<String> listener) {
		super(Method.POST,url,listener);
		mParams=params;
		// TODO Auto-generated constructor stub
	}
	public Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
