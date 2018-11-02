package cn.com.bigknow.trade.pos.Immediate.model.api;

import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeJhInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeDzBillinfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeDzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzePzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BankCardInfoBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetailbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BoHuiInfoBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.CustomerBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FlowRecordBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodAlias;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodImgInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeDetailModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.IdResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.LiveSell;
import cn.com.bigknow.trade.pos.Immediate.model.bean.LoginResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MarchantInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MerchantInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MessageInfoBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MyInventory;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MyInventoryDetial;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MyWalletBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.OrderSettlementResultBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.OutBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayReques_isorNo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCodeInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode_;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayResult;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UnitModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Version;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hujie on 16/6/18.
 */
public interface RetrofitApi {

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<LoginResultWrap>> logon(@Url String url, @Field("userNo") String userNo, @Field("password") String password,@Field("client") String client);

    @FormUrlEncoded
    @POST("api/user/login.action")
    Observable<BaseEntity<UserInfo>> logon_( @Field("userNo") String userNo, @Field("password") String password);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> pushId(@Url String url,
                                  @Field("action") String action,
                                  @Field("psnId") String psnId,
                                  @Field("pushId") String pushId,
                                  @Field("client") String client);


    @POST
    Observable<BaseEntity<String>> checkToken(@Url String url);



    @POST
    Observable<BaseEntity<String>> exitLpgin(@Url String url);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> resetPassword(@Url String url, @Field("userNo") String userNo, @Field("idNo") String idNo, @Field("verifyCode") String verifyCode, @Field("pwd") String pwd, @Field("repwd") String repwd);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> changePassword(@Url String url, @Field("userId") String userId, @Field("oldPwd") String oldPwd, @Field("pwd") String pwd, @Field("repwd") String repwd);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> changePhone(@Url String url, @Field("userNo") String userNo, @Field("verifyCode") String verifyCode, @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> getSmsCode(@Url String url, @Field("userNo") String userNo);


    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> getPhoCode(@Url String url, @Field("userNo") String userNo, @Field("verifyCode") String verifyCode);

    @FormUrlEncoded
    @POST("mvpInventry/findInv.action")
    Observable<ListResultWrap<List<MyInventory>>> getMyInventoryList(@Field("startDate") String startDate, @Field("endDate") String endDate);

    @FormUrlEncoded
    @POST("mvpInventry/find.action")
    Observable<ListResultWrap<List<MyInventoryDetial>>> getMyInventoryDetialList(@Field("batchNo") String batchNo, @Field("itemCode") String itemCode, @Field("itemName") String itemName, @Field("onlyBal") String onlyBal, @Field("supportPaging") boolean supportPaging, @Field("page") String page, @Field("rows") String rows);

    @FormUrlEncoded
    @POST("mvpOrder/findVsDet.action")
    Observable<BaseEntity<List<BillDetailbean>>> getDzBillDetail(@Field("billNo") String billNo,@Field("id") String id);

    @FormUrlEncoded
    @POST("mvpMerchantItem/findItem.action")
    Observable<BaseEntity<List<FoodInfo>>> getFoodList(@Field("flag") String flag, @Field("state") String state);

    @POST("api/appItem/hs/trash.action")
    Observable<BaseEntity<Object>> getFoodOut(@Body List<String> itemIds);

      @FormUrlEncoded
    @POST("mvpMerchantItem/findIdstem.action")
    Observable<BaseEntity<List<FoodInfo>>> getFoosdfdList(@Field("id") String id, @Field("state") String state);

    @FormUrlEncoded
    @POST("mvpMerchantItem/findVendor.action")
    Observable<BaseEntity<List<VendorAreaBean>>> getVendorList(@Field("itemId") String itemId, @Field("merchantId") String merchantId);

    @FormUrlEncoded
    @POST("mvpMerchantItem/findAlias.action")
    Observable<BaseEntity<List<FoodAlias>>> getAliasList(@Field("itemId") String itemId, @Field("def") String def);


    @POST("mvpMerchantItem/update.action")
    Observable<BaseEntity<IdResultWrap>> updateFoodInfo(@Body SimpleRequestWrap simpleRequestWrap);

    /**
     * REQUEST参数：
     * transType:业务类型，state:[N-新增，S-已提交，Y-通过，R-驳回，C-撤回，可同时传入多个使用|分隔]
     * 单据状态,billNo:入场编号、itemCode:菜品编码、itemName菜品名称、starBillDate:进货日期起始、endBillDate:进货日期截止
     */
    @FormUrlEncoded
    @POST("mvpEntry/findEntry.action")
    Observable<BaseEntity<List<FoodEntryInfo>>> getFoodEntryList( @Field("transType") String transType);

    /**
     * 统计 -=买菜--list
     *
     * @param payChanner
     * @param startBillDate
     *@param endBillDate
     * @param balState  @return
     */
    @FormUrlEncoded
    @POST("mvpOrder/findOrder.action")
    Observable<BaseEntity<List<AnalyzeDzBillinfo>>> getDzList(@Field("page") int page, @Field("rows") int rows,@Field("balState") String balState, @Field("startBillDate") String startBillDate, @Field("endBillDate") String endBillDate, @Field("payChanner") String payChanner);

    /**
     * 统计 -对账-方式
     * @return
     */
    @FormUrlEncoded
    @POST("mvpOrder/analyze.action")
    Observable<BaseEntity<List<AnalyzeDzInfo>>> getDzType(@Field("startDate") String startDate, @Field("endDate") String endDate);

    /**
     * 统计 -=进货--量
     * @return
     */
    @FormUrlEncoded
    @POST("mvpEntry/analyze.action")
    Observable<BaseEntity<AnalyzeJhInfo>> getJhAnalyze(@Field("startDate") String startDate, @Field("endDate") String endDate);
    /**
     * 统计 -进货--list
     *
     * @param
     * @param
     *@param state  @return
     */
    @FormUrlEncoded
    @POST("mvpEntry/findEntry.action")
    Observable<BaseEntity<List<FoodEntryInfo>>> getJhList(@Field("page") int page, @Field("rows") int rows, @Field("transType") String transType, @Field("state") String state, @Field("startDate") String startDate,  @Field("endDate") String endDate);
    /**
     * 统计 -品种--list
     * @param endDate
     * @return
     */
    @FormUrlEncoded
    @POST("mvpMerchantItem/analyze.action")
    Observable<BaseEntity<List<AnalyzePzInfo>>> getPzList(@Field("startDate") String startDate,@Field("endDate") String endDate);

    /**
     * J
     * \
     * 根据id 查询对应申报详情
     */
    @FormUrlEncoded
    @POST("mvpEntry/findEntry.action")
    Observable<BaseEntity<List<FoodEntryInfo>>> getFoodEntryInfo(@Field("id") String id);

    @Multipart
    @POST("servlet/fileupload")
    Observable<BaseEntity<String>> uploadFile(@Part MultipartBody.Part file,
                                      @Part("uploadSession") String uploadSession,
                                      @Part("masterId") String masterId,
                                      @Part("masterFileType") String masterFileType,
                                      @Part("userId") String userId,
                                      @Part("orgId") String orgId);

//上传车牌
    @Multipart
    @POST("api/appCommon/easypr.action")
    Observable<BaseEntity<String>> uploadChe(@Part MultipartBody.Part file);




    @Multipart
    @POST
    Observable<BaseEntity<String>> uploadHead(@Url String url,
                                      @Part MultipartBody.Part file,
                                      @Part("uploadSession") String uploadSession,
                                      @Part("masterId") String masterId,
                                      @Part("masterFileType") String masterFileType,
                                      @Part("userId") String userId,
                                      @Part("orgId") String orgId);

    @POST("mvpMerchantItem/updateState.action")
    Observable<BaseEntity<String>> updateFoodState(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpMerchantItem/delete.action")
    Observable<BaseEntity<String>> deleteFood(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("api/order/hs/delete.action")
    Observable<BaseEntity<String>> deleteBill(@Body List<String> ids);


    @FormUrlEncoded
    @POST("mvpMerchantItem/findImgs.action")
    Observable<ListResultWrap<List<FoodImgInfo>>> getFoodImg(@Field("id") String id, @Field("masterFileType") String masterFileType);

    @FormUrlEncoded
    @POST("mvpMerchantItem/setFirstImg.action")
    Observable<BaseEntity<String>> setTopFoodImg(@Field("fileId") String fileId, @Field("masterId") String masterId, @Field("uploadSession") String uploadSession);

    @FormUrlEncoded
    @POST("servlet/fileupload")
    Observable<BaseEntity<String>> associateFile(@Field("oper_type") String oper_type, @Field("masterId") String masterId, @Field("fileIds") String fileIds);

    @POST("mvpMerchantItem/save.action")
    Observable<BaseEntity<FoodInfo>> saveFoodInfo(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpOut/trashItem.action")
    Observable<BaseEntity<IdResultWrap>> celarMyInventory(@Body SimpleRequestWrap simpleRequestWrap);

    @FormUrlEncoded
    @POST("servlet/fileupload")
    Observable<BaseEntity<String>> deleteFile(@Field("oper_type") String oper_type, @Field("fileId") String fileId);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> tradePwd_MODIFY(@Url String url, @Field("action") String action, @Field("psnId") String psnId, @Field("tradePwd") String tradePwd, @Field("oldTradePwd") String oldTradePwd);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> tradePwd_SAVE(@Url String url, @Field("action") String action, @Field("psnId") String psnId, @Field("tradePwd") String tradePwd);


    @FormUrlEncoded
    @POST
    Observable<BaseEntity<String>> tradePwd_VERIFY(@Url String url, @Field("action") String action, @Field("psnId") String psnId, @Field("tradePwd") String tradePwd);
    /**
     * ---------------------------------------------------------------------------
     */
    @FormUrlEncoded
    @POST("api/appItem/hs/delItem.action")
    Observable<BaseEntity<String>> deleteFoodEntry(@Field("id") String id);

    @POST("mvpOrder/delete.action")
    Observable<BaseEntity<String>> deleteBill(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpOrder/cancel.action")
    Observable<BaseEntity<String>> cancelBill(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpPayinfo/save.action")
    Observable<BaseEntity<OrderSettlementResultBean>> cardBin(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpPayinfo/saveCredit.action")
    Observable<BaseEntity<OrderSettlementResultBean>> oncreditBin(@Body SimpleRequestWrap simpleRequestWrap);

    /*@FormUrlEncoded
    @POST("mvpEntry/findOpinion.action")
    Observable<ListResultWrap<List<BoHuiInfoBean>>> getBoHuiList(@Field("origId") String origId);*/
    @FormUrlEncoded
    @POST("mvpBillLog/find.action")
    Observable<ListResultWrap<List<BoHuiInfoBean>>> getBoHuiList(@Field("billId") String billId, @Field("billType") String billType);

    @POST("mvpAccountDet/accountOut.action")
    Observable<BaseEntity<String>> accountOut(@Body SimpleRequestWrap requestWrap);


    @FormUrlEncoded
    @POST("mvpItemtype/find.action")
    Observable<ListResultWrap<List<FoodTypeModel>>> getFoodType(@Field("code") String code, @Field("name") String name, @Field("enabledFlag") String enabledFlag,@Field("parentId") String parentId);


    @FormUrlEncoded
    @POST("mvpItem/findItem.action")
    Observable<ListResultWrap<List<FoodTypeDetailModel>>> getFoodTypeDetail(@Field("typeId") String typeId, @Field("name") String name);

    @FormUrlEncoded
    @POST("mvpUnit/find.action")
    Observable<ListResultWrap<List<UnitModel>>> getUnit(@Field("code") String code, @Field("name") String name);

    @POST("mvpEntry/save.action")
    Observable<BaseEntity<IdResultWrap>> updateFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpEntry/commit.action")
    Observable<BaseEntity<IdResultWrap>> submitFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpEntry/reEntry.action")
    Observable<BaseEntity<IdResultWrap>> reFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpEntry/save.action")
    Observable<BaseEntity<IdResultWrap>> newFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);


    @POST("mvpEntry/cancel.action")
    Observable<BaseEntity<String>> cancelFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpOut/cancel.action")
    Observable<BaseEntity<String>> resetInventory(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpEntry/send.action")
    Observable<BaseEntity<String>> addFoodEntry(@Body SimpleRequestWrap simpleRequestWrap);

    @FormUrlEncoded
    @POST("mvpMerchantItem/find.action")
    Observable<ListResultWrap<List<FoodInfo>>> getFoodSelectList(@Field("code") String code, @Field("name") String name, @Field("state") String state);

    @FormUrlEncoded
    @POST("mvpInventry/findTrash.action")
    Observable<ListResultWrap<List<OutBean>>> getOutList(@Field("billNo") String billNo, @Field("startBillDate") String startBillDate, @Field("endBillDate") String endBillDate, @Field("page") int page, @Field("rows") int rows);


    @FormUrlEncoded
    @POST("mvpInventry/findInv.action")
    Observable<ListResultWrap<List<LiveSell>>> getLiveSellList(@Field("startDate") String startDate, @Field("endDate") String endDate, @Field("type") String type);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<MerchantInfo>> getMerchantInfo(@Url String url, @Field("orgId") String orgId);

    @FormUrlEncoded
    @POST
    Observable<BaseEntity<UserInfo>> getUserInfo(@Url String url, @Field("userId") String userId);


    @FormUrlEncoded
    @POST("mvpOrder/findVsDet.action")
    Observable<ListResultWrap<List<Billbean>>> getBillList(@Field("balState") String balState, @Field("page") int page);

    @FormUrlEncoded
    @POST("mvpOrder/findVsDet.action")
    Observable<ListResultWrap<List<Billbean>>> getBillDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("mvpBankCard/find.action")
    Observable<ListResultWrap<List<BankCardInfoBean>>> getBankCardList(@Field("cardNo") String cardNo, @Field("cardType") String cardType);

    @FormUrlEncoded
    @POST("mvpAccountDet/find.action")
    Observable<ListResultWrap<List<FlowRecordBean>>> getBankCardDetialList(@Field("page") int page, @Field("rows") int rows);

    @FormUrlEncoded
    @POST("mvpCommon/findArea.action")
    Observable<BaseEntity<List<AreaBean>>> getAreaDetialList(@Field("parentId") String parentId,
                                                             @Field("areaName") String areaName,
                                                             @Field("level") int level,
                                                             @Field("supportPaging") boolean supportPaging,
                                                             @Field("page") int page, @Field("rows") int rows);

    @FormUrlEncoded
    @POST("mvpCommon/findCustomer.action")
    Observable<ListResultWrap<List<CustomerBean>>> getCustomerDetialList(@Field("param") String param, @Field("page") int page, @Field("rows") int rows);

    @POST("mvpMessage/api/findLatest.action")
    Observable<BaseEntity<List<MessageInfoBean>>> getMessageDetialListType();

    @FormUrlEncoded
    @POST("mvpMessage/api/find.action")
    Observable<BaseEntity<List<MessageInfoBean>>> getMessageDetialList(@Field("msgType") String msgType,@Field("page") int page, @Field("rows") int rows);

    @POST("mvpMessage/delete.action")
    Observable<BaseEntity> MessageDelete(@Body SimpleRequestWrap requestWrap);

    @POST("mvpOrder/save.action")
    Observable<BaseEntity<Billbean>> addNewOrder(@Body SimpleRequestWrap simpleRequestWrap);

    //提交刷卡支付成功后的信息
    @POST("mvpPayinfo/pay.action")
    Observable<BaseEntity<String>> PaymentSubmitted(@Body SimpleRequestWrap simpleRequestWrap);
    //广播提交刷卡支付成功后的信息
    @POST("mvpPayinfo/pay.action")
    Observable<BaseEntity<String>> PaymentSubmitgb(@Body SimpleRequestWrap simpleRequestWrap);

    @FormUrlEncoded
    @POST("mvpCommon/findUserByMobile.action")
    Observable<BaseEntity<MarchantInfo>> checkCorrInfo(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("mvpAccount/findAccount.action")
    Observable<BaseEntity<List<MyWalletBean>>> getAccountInfo(@Field("startDate") String startDate, @Field("endDate") String endDate);

    @FormUrlEncoded
    @POST("mvpCommon/findCardInfo.action")
    Observable<BaseEntity<String>> getBankNoInfo(@Field("cardNo") String cardNo);

    @POST("mvpBankCard/delete.action")
    Observable<BaseEntity<String>> deleteBankCard(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpBankCard/add.action")
    Observable<BaseEntity<String>> addBankCard(@Body SimpleRequestWrap simpleRequestWrap);

    @POST("mvpBankCard/updateDef.action")
    Observable<BaseEntity<String>> setDefBankCard(@Body SimpleRequestWrap simpleRequestWrap);


    @FormUrlEncoded
    @POST("mvpOrder/changeAmt.action")
    Observable<BaseEntity<String>> changePayAmt(@Field("id")String id,@Field("payAmt")String payAmt);
    
   /* @POST("mvpPayinfo/pay.action")*/
    @POST("mvpPayinfo/save.action")
    Observable<BaseEntity<PayResult>> pay(@Body SimpleRequestWrap simpleRequestWrap);

    @FormUrlEncoded
    @POST("mvpVersion/api/checkVersion.action")
    Observable<BaseEntity<Version>> checkUpdate(@Field("version")String version,@Field("appType")String appType);


    //微信支付
    @POST
    Observable<BaseEntity<PayRequestCodeInfo>> payCode(@Url String url, @Body PayRequestCode payRequestWrap);

    @POST
    Observable<BaseEntity<PayRequestCodeInfo>> payCodeResult(@Url String url, @Body PayRequestCode_ payRequestWrap);

    @FormUrlEncoded
    @POST("api/order/hs/checkState.action")
    Observable<BaseEntity<String>> payState(@Field("id") String id);

    @POST
    Observable<BaseEntity<PayReques_isorNo>> payIsNo(@Url String url , @Body PayReques_isorNo payReques_isorNo);




}


