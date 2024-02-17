package com.akp.RetrofitAPI;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("GetOperatorList")
    Call<String> OperatorService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("PostRecharge")
    Call<String> PostRecharge(
            @Body String body);



    @Headers("Content-Type: application/json")
    @GET("getStateList")
    Call<String> API_getStateList();



    @Headers("Content-Type: application/json")
    @POST("OtpVerify")
    Call<String> API_verifyOtp(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Login")
    Call<String> API_login(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("resendOtp")
    Call<String> API_resendOtp(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetWalletBalance")
    Call<String> API_dashboard(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProfileView")
    Call<String> API_getProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProfileUpdate")
    Call<String> API_updateProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Registration")
    Call<String> API_Registration(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("VerifyReferral")
    Call<String> API_VerifyReferral(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("AEPSReport")
    Call<String> API_AEPSReport(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DMTReport")
    Call<String> API_DMTReport(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DMT1Report")
    Call<String> API_DMT1Report(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("FundRequestReport")
    Call<String> API_FundRequestReport(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("WalletTransactionReport")
    Call<String> API_WalletTransactionReport(
            @Body String body);
    @Headers("Content-Type: application/json")
    @GET("ExternalLinkList")
    Call<String> API_ExternalLinkList();

    @Headers("Content-Type: application/json")
    @POST("Logout")
    Call<String> API_Logout(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("OnboardKyc")
    Call<String> API_OnboardKyc(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AEPSBankBalance")
    Call<String> API_AEPSBankBalance(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("AEPSMiniStatement")
    Call<String> API_AEPSMiniStatement(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("AEPSWithdraw")
    Call<String> API_AEPSWithdraw(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ChangePassword")
    Call<String> API_ChangePassword(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ChangeTpin")
    Call<String> API_ChangeTpin(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("TwoFactorAuthenticationReg")
    Call<String> API_TwoFactorAuthenticationReg(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("TwoFactorAuthentication")
    Call<String> API_TwoFactorAuthentication(
            @Body String body);
}



