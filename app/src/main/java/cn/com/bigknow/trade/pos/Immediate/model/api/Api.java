package cn.com.bigknow.trade.pos.Immediate.model.api;

import java.util.List;

import javax.inject.Inject;

import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayReques_isorNo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode_;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.RxNetErrorProcessor;
import okhttp3.MultipartBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hujie on 16/6/18.
 */
public class Api {


    /**
     * 登录接口
     *
     * @param transformer
     * @param requestListener
     * @param userNo
     * @param password
     */
    public void logon(Observable.Transformer transformer, String userNo, String password, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.logon(ModelConfig.LOGON, userNo, password,"POS").compose(transformer), requestListener);
    }

    public void logon_(Observable.Transformer transformer, String userNo, String password, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.logon_(userNo, password).compose(transformer), requestListener);
    }

    /**
     * 退出登录接口
     *
     * @param transformer
     * @param requestListener
     */
    public void exitLogin(Observable.Transformer transformer, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.exitLpgin(ModelConfig.EXIT_LOGON).compose(transformer), requestListener);
    }

    /**
     * 保存推送Id
     *
     * @param
     * @param requestListener
     * @param action
     * @param psnId
     * @param pushId
     */
    public void pushId(String action, String psnId, String pushId, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.pushId(ModelConfig.PUSHID, action, psnId, pushId, "POS"), requestListener);
    }

    /**
     * 检查token是否过期了
     *
     * @param requestListener
     */
    public void checkToken(SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.checkToken(ModelConfig.CHECK_TOKEN), requestListener);
    }

    /**
     * 获取手机短信验证码
     *
     * @param requestListener
     */
    public void getSmsCode(String UserNo, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getSmsCode(ModelConfig.GET_SMS_CODE, UserNo), requestListener);
    }

    /**
     * 验证手机，验证码
     *
     * @param requestListener
     */
    public void getFromPhoCode(String userNo, String verifyCode, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getPhoCode(ModelConfig.GET_SMS_CODE, userNo, verifyCode), requestListener);
    }

    /**
     * 重置密码
     *
     * @param UserNo          手机号
     * @param idNo            身份证后六位
     * @param verifyCode      验证码
     * @param pwd             密码
     * @param repwd           确认密码
     * @param requestListener
     */
    public void resetPassword(String UserNo, String idNo, String verifyCode, String pwd, String repwd, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.resetPassword(ModelConfig.RESET_PASSWORD, UserNo, idNo, verifyCode, pwd, repwd), requestListener);
    }

    /**
     * 修改密码密码
     *
     * @param userId 用户Id
     * @param oldPwd 旧密码
     * @param pwd    新密码
     * @param repwd  确认新密码
     *               用户id+旧密码(oldPwd)、新密码(pwd)、确认新密码(repwd)
     */
    public void changePassword(String userId, String oldPwd, String pwd, String repwd, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.changePassword(ModelConfig.CHANGE_PASSWORD, userId, oldPwd, pwd, repwd), requestListener);
    }

    /**
     * 修改手机号
     *
     * @param UserNo                                账户名
     * @param verifyCode                            验证码
     * @param (UserNo)+验证码(verifyCode)+新手机号(mobile)
     */
    public void changePhone(String UserNo, String verifyCode, String mobel, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.changePhone(ModelConfig.CHANGE_PHONE, UserNo, verifyCode, mobel), requestListener);
    }

    /**
     * 获取我的库存列表
     *
     * @param transformer
     * @param requestListener
     */
    public void getMyInventoryList(Observable.Transformer transformer, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getMyInventoryList("", "").compose(transformer), requestListener);
    }

    /**
     * 获取我的库存批次详情
     *
     * @param transformer
     * @param itemCode
     * @param requestListener
     */
    public void getMyInventoryDetialList(Observable.Transformer transformer, String itemCode, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getMyInventoryDetialList("", itemCode, "", "Y", false, "", "").compose(transformer), requestListener);
    }

    /**
     * 获取订单详情
     *
     * @param requestListener
     */
    public void getDzBillDetail(Observable.Transformer transformer, String billNo, String id, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getDzBillDetail(billNo, id).compose(transformer), requestListener);
//        apiWrapper(retrofitApi.getDzBillDetail(billNo,id).compose(transformer), requestListener);
    }

    /**
     * 获取菜品列表中信息
     *
     * @param requestListener
     */
    public void getFoodList(Observable.Transformer transformer, String flag, String state, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getFoodList(flag, state).compose(transformer), requestListener);
    }

    /**
     * 清库存
     *
     * @param requestListener
     */
    public void getFoodOut(Observable.Transformer transformer, List<String> itemIds, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getFoodOut(itemIds).compose(transformer), requestListener);
    }

    /**
     * 供货商产地查询接口
     * @param transformer
     * @param itemId
     * @param merchantId
     * @param requestListener
     */
    public void getVendorList(Observable.Transformer transformer, String itemId, String merchantId, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getVendorList(itemId, merchantId).compose(transformer), requestListener);
    }

    /**
     * 更新菜品信息
     *
     * @param requestListener
     */
    public void updateFoodInfo(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.updateFoodInfo(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 修改菜名
     *
     * @param requestListener
     */
    public void changeFoodName(Observable.Transformer transformer, String itemId, String def, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getAliasList(itemId, def).compose(transformer), requestListener);
    }

    /**
     * 上传文件通用接口
     *
     * @param requestListener
     */
    public void uploadFile(Observable.Transformer transformer,
                           MultipartBody.Part file,
                           String uploadSession,
                           String masterId,
                           String masterFileType,
                           String userId,
                           String orgId,
                           SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.uploadFile(file, uploadSession, masterId, masterFileType, userId, orgId).compose(transformer), requestListener);
    }


    /**
     * 上传车牌接口
     *
     * @param requestListener
     */
    public void uploadChe(Observable.Transformer transformer,
                          MultipartBody.Part file,
                          SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.uploadChe(file).compose(transformer), requestListener);
    }

    /**
     * 上传头像接口
     *
     * @param requestListener
     */
    public void uploadHead(Observable.Transformer transformer,
                           String url,
                           MultipartBody.Part file,
                           String uploadSession,
                           String masterId,
                           String masterFileType,
                           String userId,
                           String orgId,
                           SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.uploadHead(url, file, uploadSession, masterId, masterFileType, userId, orgId).compose(transformer), requestListener);
    }

    /**
     * 更新菜品状态
     *
     * @param requestListener
     */
    public void updateFoodState(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.updateFoodState(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 删除菜品
     *
     * @param requestListener
     */
    public void deleteFood(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.deleteBill(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 删除订单
     *
     * @param requestListener
     */
    public void deleteBill(Observable.Transformer transformer, List<String> ids, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.deleteBill(ids).compose(transformer), requestListener);
    }


    /**
     * 设置菜品头图
     *
     * @param requestListener
     */
    public void setTopFoodImg(Observable.Transformer transformer, String fileId, String masterId, String uploadSession, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.setTopFoodImg(fileId, masterId, uploadSession).compose(transformer), requestListener);
    }

    /**
     * 关联文件
     *
     * @param requestListener
     */
    public void associateFile(Observable.Transformer transformer, String masterId, String fileIds, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.associateFile("associate", masterId, fileIds).compose(transformer), requestListener);
    }

    /**
     * 更新菜品信息
     *
     * @param requestListener
     */
    public void saveFoodInfo(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
//        apiWrapper(retrofitApi.saveFoodInfo(simpleRequestWrap).compose(transformer), requestListener);
        apiNoMapWrapper(retrofitApi.saveFoodInfo(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 销库存
     *
     * @param requestListener
     */
    public void celarMyInventory(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.celarMyInventory(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 设置菜品头图
     *
     * @param requestListener
     */
    public void getFoodImg(Observable.Transformer transformer, String id, String masterFileType, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getFoodImg(id, masterFileType).compose(transformer), requestListener);
    }

    /**
     * 获取菜品申报列表
     *
     * @param transformer
     * @param requestListener
     */
    public void getFoodEntryList(Observable.Transformer transformer, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getFoodEntryList("C").compose(transformer), requestListener);
    }

    /**
     * 获取统计 -对账-订单结算状列表
     *  @param transformer
     * @param payChanner
     * @param startBillDate
     * @param endBillDate
     * @param balState           结算状态
     * @param requestListener
     */
    public void getDzList(Observable.Transformer transformer, int page, int rows, String balState, String startBillDate, String endBillDate, String payChanner, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getDzList(page, rows, balState, startBillDate, endBillDate, payChanner).compose(transformer), requestListener);
    }

    /**
     * 获取统计 -对账 --方式
     *  @param transformer
     * @param requestListener
     */
    public void getDzType(Observable.Transformer transformer, String startDate, String endDate, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getDzType(startDate, endDate).compose(transformer), requestListener);
    }

    /**
     * 获取统计 -进货统计
     *  @param transformer
     * @param requestListener
     */
    public void getJhAnaluze(Observable.Transformer transformer, String startDate, String endDate, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getJhAnalyze(startDate, endDate).compose(transformer), requestListener);
    }

    /**
     * 获取统计 -进货 状列表
     *  @param transformer
     * @param
     * @param startDate
     * @param state          入场状态
     * @param requestListener
     */
    public void getJhList(Observable.Transformer transformer, int page, int rows, String state, String startDate, String endDate, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getJhList(page, rows, "C", state, startDate, endDate).compose(transformer), requestListener);
    }

    /**
     * 获取统计 -品种 状列表
     *
     * @param transformer
     * @param startDate
     * @param startDate
     * @param requestListener
     */
    public void getPzList(Observable.Transformer transformer, String startDate, String endDate, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getPzList(startDate, endDate).compose(transformer), requestListener);
    }

    /**
     * 删除文件
     *
     * @param requestListener
     */
    public void deleteFile(Observable.Transformer transformer, String fileId, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.deleteFile("delete", fileId).compose(transformer), requestListener);
    }


    /**
     * 获取菜品类型
     *
     * @param code        菜品类型编码[模糊]
     * @param name        名称[模糊]
     * @param enabledFlag 有效标识[Y有效/N无效/空值不过滤]
     */
    public void getFoodType(Observable.Transformer transformer, String code, String name, String enabledFlag,String parentId ,SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getFoodType(code, name, enabledFlag,parentId).compose(transformer), requestListener);
    }


    /**
     * 获取菜品类型详情
     *
     * @param name      名称[模糊]
     * @param helpCode  助记码[模糊]
     * @param state     状态[Y有效/N无效/空值不过滤]
     * @param innerCode 类型编码
     */
    public void getFoodTypeDetail(Observable.Transformer transformer, String typeId, String name, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getFoodTypeDetail(typeId, name).compose(transformer), requestListener);
    }

    /**
     * 获取菜品类型详情
     *
     * @param code 计量单位编码[模糊]
     * @param name 名称[模糊]
     */
    public void getUnit(Observable.Transformer transformer, String code, String name, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getUnit(code, name).compose(transformer), requestListener);
    }

    /**
     * 获取现场销售列表以及批次
     *
     * @param transformer
     * @param requestListener
     */
    public void getLiveSellList(Observable.Transformer transformer, String startDate, String endDate, String type, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getLiveSellList(startDate, endDate, type).compose(transformer), requestListener);
    }

    /**
     * 获得商户信息
     *
     * @param transformer
     * @param orgId
     * @param requestListener
     */
    public void getMerchantInfo(Observable.Transformer transformer, String orgId, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getMerchantInfo(ModelConfig.MERCHANT_INFO, orgId).compose(transformer), requestListener);
    }

    /**
     * 核查用户信息
     */
    public void checkCorrInfo(Observable.Transformer transformer, String mobile, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.checkCorrInfo(mobile).compose(transformer), requestListener);
    }

    /**
     * 生成新的订单
     */
    public void addNewOrder(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.addNewOrder(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 提交刷卡支付成功后的信息
     */
    public void PaymentSubmitted(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.PaymentSubmitted(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 广播提交刷卡支付成功后的信息
     */
    public void PaymentSubmitgb(SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.PaymentSubmitgb(simpleRequestWrap), requestListener);
    }

    /**
     * 获取消息列表1
     *
     * @param transformer
     * @param requestListener
     */
    public void getMessageListType(Observable.Transformer transformer, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getMessageDetialListType().compose(transformer), requestListener);
    }

    /**
     * 获取消息列表
     *
     * @param transformer
     * @param requestListener
     */
    public void getMessageList(Observable.Transformer transformer, String msgType, int page, int rows, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getMessageDetialList(msgType, page, rows).compose(transformer), requestListener);
    }

    /**
     * 删除消息
     *
     * @param transformer
     * @param requestListener
     */
    public void getMessageDelete(Observable.Transformer transformer, SimpleRequestWrap requestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.MessageDelete(requestWrap).compose(transformer), requestListener);
    }

    /**
     * 提现
     */
    public void accountOut(Observable.Transformer transformer, SimpleRequestWrap requestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.accountOut(requestWrap).compose(transformer), requestListener);
    }

    /**
     * 修改支付密码
     */
    public void tradePwd_MODIFY(Observable.Transformer transformer, String psnId, String tradePwd, String oldTradePwd, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.tradePwd_MODIFY(ModelConfig.TRADEPWD, "MODIFY_TRADE_PWD", psnId, tradePwd, oldTradePwd).compose(transformer), requestListener);
    }

    /**
     * 保存支付密码
     */
    public void tradePwd_SAVE(Observable.Transformer transformer, String psnId, String tradePwd, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.tradePwd_SAVE(ModelConfig.TRADEPWD, "SAVE_TRADE_PWD", psnId, tradePwd).compose(transformer), requestListener);
    }

    /**
     * 验证支付密码
     */
    public void tradePwd_VERIFY(Observable.Transformer transformer, String psnId, String tradePwd, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.tradePwd_VERIFY(ModelConfig.TRADEPWD, "VERIFY_TRADE_PWD", psnId, tradePwd).compose(transformer), requestListener);
    }

    private <T> void apiWrapper(Observable<BaseEntity<T>> o, RequestListener<T> requestListener) {
        o.map(new ResultFunc<>()).subscribeOn(Schedulers.io())
                .doOnSubscribe(requestListener::onStart)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((t) -> {
                    requestListener.onFinish();
                    requestListener.onSuccess(t);
                }, throwable -> netErrorProcessor.tryWithApiError(throwable, (error) -> {
                    requestListener.onFinish();
                    requestListener.onError(error);
                }));
    }


    /**-csw--------------------------------------------------------------------------*/
    /**
     * 删除菜品申请
     *
     * @param requestListener
     */
    public void deleteFoodEntry(Observable.Transformer transformer, String id, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.deleteFoodEntry(id).compose(transformer), requestListener);
    }

    /**
     * 删除撤销的订单
     */
    public void deleteOrCancelBill(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener, boolean isDetel) {
        if (isDetel)
            apiNoMapWrapper(retrofitApi.deleteBill(simpleRequestWrap).compose(transformer), requestListener);
        else
            apiNoMapWrapper(retrofitApi.cancelBill(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     *刷卡/赊销
     */
    public void card_oncredit_BTN(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener, boolean isCard) {
        if (isCard)
            apiNoMapWrapper(retrofitApi.cardBin(simpleRequestWrap).compose(transformer), requestListener);
        else
            apiNoMapWrapper(retrofitApi.oncreditBin(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 撤回、上报菜品申请
     *
     * @param requestListener
     */
    public void cancelOrSubmitFoodEntry(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener, boolean isCancel) {
        if (isCancel)
            apiNoMapWrapper(retrofitApi.cancelFoodEntry(simpleRequestWrap).compose(transformer), requestListener);
        else
            apiNoMapWrapper(retrofitApi.addFoodEntry(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 还原消库
     */
    public void resetInventory(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.resetInventory(simpleRequestWrap).compose(transformer), requestListener);
    }


    /**
     * 获取驳回信息列表
     *
     * @param requestListener
     */
    public void getBoHuiList(Observable.Transformer transformer, String billId, String billType, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getBoHuiList(billId, billType).compose(transformer), requestListener);
    }

    /**
     * REQUEST参数：
     * 入场申请数据集dsMain,明细数据集dsDet
     * [{name:
     * dsMain,data:[{
     * id:[必需]申请ID，
     * transType:[C菜品入场/O其他入场，必需]业务类型..}]},
     * {name:dsDet,data:
     * [{id:[若存在]入场明细ID，
     * updateDate:原更新时间，
     * state:原状态,
     * itemId:[必需]我的菜品ID、
     * vendor:供货商,
     * area:产地,
     * qty:[数量]入场数量,
     * unitId:采购单位ID，
     * mainUnitId:库存计量单位}{...}]}]
     *
     * @param requestListener
     */
    public void submitFoodEntry(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {

        apiWrapper(retrofitApi.submitFoodEntry(simpleRequestWrap).compose(transformer), requestListener);


    }

    //修改
    public void reFoodEntry(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {

        apiWrapper(retrofitApi.reFoodEntry(simpleRequestWrap).compose(transformer), requestListener);


    }

    /**
     * 新建菜品申請
     */
    public void newFoodEntry(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.newFoodEntry(simpleRequestWrap).compose(transformer), requestListener);

    }

    /**
     * 新增菜品申請(選擇菜品)
     */
    public void getFoodSelectList(Observable.Transformer transformer, String code, String name, String state, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getFoodSelectList(code, name, state).compose(transformer), requestListener);
    }

    /**
     * 獲取庫存操作記錄
     */
    public void getOutList(Observable.Transformer transformer, String billNo, String startBillDate, String endBillDate, int page, int rows, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getOutList(billNo, startBillDate, endBillDate, page, rows).compose(transformer), requestListener);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(Observable.Transformer transformer, String userId, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getUserInfo(ModelConfig.USERINFO, userId).compose(transformer), requestListener);
    }

    /**
     * 获取地理信息，省市区县
     */
    public void getAreaDetialList(Observable.Transformer transformer,
                                  String parentId,//上级编码，省份上级编码为0
                                  String areaName,//	N	区域名，模糊查询
                                  int level,//	N	区域级别，1-省，2-市，3-区县
                                  boolean supportPaging,//	N	是否分页
                                  int page,   //当前页
                                  int rows, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getAreaDetialList(parentId, areaName, level, supportPaging, page, rows).compose(transformer), requestListener);
    }

    /**
     * 获取客户明细列表
     */
    public void getCustomerDetialList(Observable.Transformer transformer, String param, int page, int rows, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getCustomerDetialList(param, page, rows).compose(transformer), requestListener);
    }

    /**
     * 获取我的订单列表
     */
    public void getBillList(Observable.Transformer transformer, String balState, int page, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getBillList(balState, page).compose(transformer), requestListener);
    }

    /**
     * 获取我的订单列表[0]
     */
    public void getBillDetail(Observable.Transformer transformer, String id, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.getBillDetail(id).compose(transformer), requestListener);
    }

    /**
     * 获取菜品申报列表
     *
     * @param id
     */
    public void getFoodEntryInfo(Observable.Transformer transformer, String id, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getFoodEntryInfo(id).compose(transformer), requestListener);
    }


    /**
     * 获取绑定银行卡列表
     */
    public void getBankCardList(Observable.Transformer transformer, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getBankCardList("", "").compose(transformer), requestListener);
    }

    /**
     * 获取银行卡明细列表
     */
    public void getBankCardDetialList(Observable.Transformer transformer, int page, int rows, SimpleRequestListener requestListener) {
        apiListWrapper(retrofitApi.getBankCardDetialList(page, rows).compose(transformer), requestListener);
    }

    /**
     * 获取账户信息
     */
    public void getAccountInfo(Observable.Transformer transformer, String startDate, String endDate, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getAccountInfo(startDate, endDate).compose(transformer), requestListener);
    }

    /**
     * 获取银行卡账号信息
     */
    public void getBankNoInfo(Observable.Transformer transformer, String bankNo, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.getBankNoInfo(bankNo).compose(transformer), requestListener);
    }

    /**
     * 添加银行卡
     */
    public void addBankCard(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.addBankCard(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 删除绑定的银行卡
     */
    public void deleteBankCard(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.deleteBankCard(simpleRequestWrap).compose(transformer), requestListener);
    }

    /**
     * 设置默认银行卡
     */
    public void setDefBankCard(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.setDefBankCard(simpleRequestWrap).compose(transformer), requestListener);
    }


    /**
     * 改价
     * @param transformer
     * @param id
     * @param payAmt
     * @param requestListener
     */
    public void changePayAmt(Observable.Transformer transformer, String id, String payAmt, SimpleRequestListener requestListener) {
        apiNoMapWrapper(retrofitApi.changePayAmt(id, payAmt).compose(transformer), requestListener);
    }

    /**
     * 支付
     * @param transformer
     * @param simpleRequestWrap
     * @param requestListener
     */
    public void pay(Observable.Transformer transformer, SimpleRequestWrap simpleRequestWrap, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.pay(simpleRequestWrap).compose(transformer), requestListener);
    }


    /**
     * 二维码生成

     * @param requestListener
     */
    public void payCode(String Url, PayRequestCode wrap, RequestListener requestListener) {
        apiWrapper(retrofitApi.payCode(Url,wrap), requestListener);
    }

    /**
     * 扫描二维码

     * @param requestListener
     */
    public void payCodeResult(String Url, PayRequestCode_ wrap, RequestListener requestListener) {
        apiWrapper(retrofitApi.payCodeResult(Url,wrap), requestListener);
    }
    public void payState(String id, RequestListener requestListener) {
        apiWrapper(retrofitApi.payState(id), requestListener);
    }

    /**
     * 查询是否已经支付
     * @param requestListener
     */
    public void payIsNo(String url, PayReques_isorNo order_no, RequestListener requestListener) {
        apiWrapper(retrofitApi.payIsNo(url ,order_no), requestListener);
    }




    /**
     * 列表数据使用此方法包装
     *
     * @param o
     * @param requestListener
     */
    private void apiListWrapper(Observable<ListResultWrap> o, RequestListener<ListResultWrap> requestListener) {
        o.subscribeOn(Schedulers.io())
                .doOnSubscribe(requestListener::onStart)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    requestListener.onFinish();
                    requestListener.onSuccess(t);
                }, throwable -> netErrorProcessor.tryWithApiError(throwable, (error) -> {
                    requestListener.onFinish();
                    requestListener.onError(error);
                }));
    }


    public void checkUpdate(String version, SimpleRequestListener requestListener) {
        apiWrapper(retrofitApi.checkUpdate(version, "POS"), requestListener);
    }

    private void apiResultWrap(Observable<ResultWrap> o, RequestListener<ResultWrap> requestListener) {
        o.subscribeOn(Schedulers.io())
                .doOnSubscribe(requestListener::onStart)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    requestListener.onFinish();
                    requestListener.onSuccess(t);
                }, throwable -> netErrorProcessor.tryWithApiError(throwable, (error) -> {
                    requestListener.onFinish();
                    requestListener.onError(error);
                }));
    }


    private void apiNoMapWrapper(Observable<BaseEntity<String>> o, SimpleRequestListener requestListener) {
        o.subscribeOn(Schedulers.io())
                .doOnSubscribe(requestListener::onStart)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    requestListener.onFinish();
                    requestListener.onSuccess(t);
                }, throwable -> netErrorProcessor.tryWithApiError(throwable, (error) -> {
                    requestListener.onFinish();
                    requestListener.onError(error);
                }));
    }


    @Inject
    RetrofitApi retrofitApi;

    @Inject
    RxNetErrorProcessor netErrorProcessor;


    private static Api api;

    public Api() {
        BootstrapApp.get().appComponent().apiComponent().inject(this);
    }

    public static Api api() {
        if (api == null) {
            api = new Api();
        }
        return api;
    }


}
