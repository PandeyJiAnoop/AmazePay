package com.akp.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GlobalAppApis {

    public String Operator(String servicename) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ServiceName", servicename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String PostRechargeAPI(String UserId, String amount, String number, String provider_id) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", UserId);
            jsonObject1.put("amount", amount);
            jsonObject1.put("number", number);
            jsonObject1.put("provider_id", provider_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }




    public String Login(String MemberId, String Password) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("Action", "");
            jsonObject1.put("Password", Password);
            jsonObject1.put("Token", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String VerifyOtp (String MemberId, String Otp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("Action", "");
            jsonObject1.put("Otp", Otp);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ResendOtp (String user_id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String Dashboard (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String AEPSBankBalancedata (String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("IPAddress", ip);
            jsonObject1.put("latitude", lat);
            jsonObject1.put("longitude", lag);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("Aadhar", adhar);
            jsonObject1.put("iinno", innon);
            jsonObject1.put("XmlDocument", xmldata);
            jsonObject1.put("Tpin", tp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String AEPSMiniStatement (String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("IPAddress", ip);
            jsonObject1.put("latitude", lat);
            jsonObject1.put("longitude", lag);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("Aadhar", adhar);
            jsonObject1.put("iinno", innon);
            jsonObject1.put("XmlDocument", xmldata);
            jsonObject1.put("Tpin", tp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String AEPSWithdraw (String MemberId,String ip,String lat,String lag,String mob,String adhar,String amt,String innon,String xmldata,String tp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("IPAddress", ip);
            jsonObject1.put("latitude", lat);
            jsonObject1.put("longitude", lag);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("Aadhar", adhar);
            jsonObject1.put("Amount", amt);
            jsonObject1.put("iinno", innon);
            jsonObject1.put("XmlDocument", xmldata);
            jsonObject1.put("Tpin", tp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetProfile (String user_id) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String AEPSReport (String MemberId,String TransType,String TransNumber) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("TransType", TransType);
            jsonObject1.put("TransNumber", TransNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String FundRequestReport (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String WalletTransactionReport (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String DMTReport(String MemberId,String TransNumber) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("TransType", "DMT2");
            jsonObject1.put("TransNumber", TransNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String DMT1Report(String MemberId,String TransNumber) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("TransType", "DMT1");
            jsonObject1.put("TransNumber", TransNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String UpdateProfile (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("MemberName", "");
            jsonObject1.put("MobileNo", "");
            jsonObject1.put("EmialId", "");
            jsonObject1.put("Address", "");
            jsonObject1.put("AadharNo", "");
            jsonObject1.put("Password", "");
            jsonObject1.put("PanNo", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String Register (String RefrerralId,String MemberName,String MobileNo,String EmialId,String Address,String AadharNo,String PanNo,String FirmName,String Password,String RoleType) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("RefrerralId", RefrerralId);
            jsonObject1.put("MemberName", MemberName);
            jsonObject1.put("MobileNo", MobileNo);
            jsonObject1.put("EmialId", EmialId);
            jsonObject1.put("Address", Address);
            jsonObject1.put("AadharNo", AadharNo);
            jsonObject1.put("PanNo", PanNo);
            jsonObject1.put("FirmName", FirmName);
            jsonObject1.put("Password", Password);
            jsonObject1.put("RoleType", RoleType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String VerifyReferral (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String Logout (String MemberId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("Action", "");
            jsonObject1.put("Password", "");
            jsonObject1.put("Token", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String OnboardKyc (String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String ChangePass (String MemberId,String oldp,String newp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("OldPassword", oldp);
            jsonObject1.put("NewPassword", newp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String T_ChangePass (String t_MemberId,String t_oldp,String t_newp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", t_MemberId);
            jsonObject1.put("OldPassword", t_oldp);
            jsonObject1.put("NewPassword", t_newp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String TwoFactorAuthenticationReg (String MemberId,String ip,String lat,String lag,
                                              String mob,String adhar,String innon,String xmldata,String tp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MemberId", MemberId);
            jsonObject1.put("IPAddress", ip);
            jsonObject1.put("latitude", lat);
            jsonObject1.put("longitude", lag);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("Aadhar", adhar);
            jsonObject1.put("iinno", innon);
            jsonObject1.put("XmlDocument", xmldata);
            jsonObject1.put("Tpin", tp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
}
