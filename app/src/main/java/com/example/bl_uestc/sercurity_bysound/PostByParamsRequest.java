package com.example.bl_uestc.sercurity_bysound;

import com.duowan.mobile.netroid.AuthFailureError;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.request.StringRequest;

import java.util.Map;

public class PostByParamsRequest  extends StringRequest{
	private Map<String, String> mParams;
	public PostByParamsRequest(String url, Map<String, String> params,Listener<String> listener) {
		super(Method.POST,url,listener);
		mParams=params;

	}
	public Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
