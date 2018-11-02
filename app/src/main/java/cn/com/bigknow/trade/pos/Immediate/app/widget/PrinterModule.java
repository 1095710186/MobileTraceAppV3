package cn.com.bigknow.trade.pos.Immediate.app.widget;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.device.Printer.Alignment;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.QrCode;

import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;

/**
 * 打印模块包括针式打印模块和热敏打印模块。
 * <p>注:  打印机直接输出或者影响输出的接口都应该在 Printer.Progress或者 Printer.Step的doPrint方法里调用，
 *   	     无法脱离这两个场景。</p>
 *   
 * <p>操作步骤：
 * <br>1、创建Printer.Progress实例
 * <br>2、在Printer.Progress的doPrint方法中设置签购单的打印样式（中西文字体大小、图片、条码、二维码等打印设置）
 * <br>3、在Printer.Progress启动打印</p>
 * 
 * @author xiexc
 * 
 */
public class PrinterModule extends AbstractModule {
	private Printer.Progress progress;
	int selectIndex = 1;
	HashMap<String,Object> ordermap;
	ArrayList<HashMap<String,Object>> food_list;
	public PrinterModule(Context context) {
		super(context);
	}

	/**
	 * POS签购单打印步骤示例
	 */     //OrderSettlementResultBean bean, Billbean billbean
	public void print(ArrayList<HashMap<String,Object>> list_map,HashMap<String,Object> ordermap){

		food_list=list_map;
		/** 1、创建Printer.Progress实例 */
		progress = new Printer.Progress() {

			/** 2、在Printer.Progress的doPrint方法中设置签购单的打印样式 */
			@Override
			public void doPrint(Printer printer) throws Exception {
				/** 设置打印格式 */
				Format format = new Format();

				/** 打印图片 */
//				InputStream is = getContext().getAssets().open("landi.bmp");
				InputStream is = getContext().getAssets().open("jkzs.bmp");
				try {
					printer.printImage(Alignment.CENTER, is);
				} finally {
					is.close();
				}

				/** 西文字符打印， 此处使用 5x7点， 1倍宽&&2倍高打印签购单标题  */
				format.setAscSize(Format.ASC_DOT5x7);
				format.setAscScale(Format.ASC_SC1x2);
				printer.setFormat(format);
//
//				printer.printText(Alignment.CENTER, "即刻追溯\n");

				/** 西文字符打印， 此处使用 5x7点， 1倍宽&&1倍高打印签购单内容  */
				format.setAscScale(Format.ASC_SC1x1);
				printer.setFormat(format);
//				printer.printText("--Public utility bill payment receipt--\n");
				printer.printText(Alignment.CENTER,"交易凭证\n");
//				printer.printText("\n");
				/*printer.printText("Transaction : Repayment\n");
				printer.printText("Credit Card No.: XXXX XXXX XXXX XXXX\n");
				printer.printText("Term No.: 2200306\n");
				printer.printText("Amount: RMB 100.00\n");
				printer.printText("Reference No.: 191017234668\n");*/

				int nameWidth = 6;
				int priceWidth = 5;
				int qtyWidth = 5;
				int amtWidth = 5;
//

				if (food_list.size()>=0){
					printer.printText("------------------------------\n");
					//printer.printMid("商品名称"+"   "+"单价  数量"+"     金额\n");

					StringBuffer title = new StringBuffer();
					title.append(getPrintStr("商品名称",6,"left"))
							.append(getPrintStr("单价",5,"right"))
							.append(getPrintStr("重量/斤",5,"right"))
							.append(getPrintStr("金额",5,"right"));
//							.append("\n");

					printer.printText(title.toString());
					LogUtil.v("title",title.toString());

					printer.printText("\n");

					for (int i = 0; i <food_list.size() ; i++) {

						StringBuffer content = new StringBuffer();
						content.append(getPrintStr(food_list.get(i).get("alias").toString().replaceAll(" ", ""),nameWidth,"left"))
								.append(getPrintStr(food_list.get(i).get("price").toString(),priceWidth,"right")).append("\t")
								.append(getPrintStr(food_list.get(i).get("qty").toString(),qtyWidth,"right")).append("\t")
								.append(getPrintStr(food_list.get(i).get("amt").toString(),amtWidth,"right"))
								.append("\n");
						printer.printText(content.toString());

					}
				}

				printer.printText("\n");


				printer.printText("------------------------------\n");
				UserInfo userInfo = UserManager.getInstance().getUserInfo();
				printer.printText("商户信息 : "+userInfo.getUserName()+"("+userInfo.getAdminOrgName()+")\n");

				String[] payNames = ordermap.get("payCorrInfo").toString().split(",");
				if (payNames != null && payNames.length > 1) {
					try {
						printer.printText("付 款 人 : "+payNames[2]+"\n");//付款人

					} catch (Exception e) {
						printer.printText("付 款 人 : "+"非系统用户"+"\n");//付款人
					}

				} else {
					printer.printText("付 款 人 : "+"非系统用户"+"\n");//付款人

				}

				if(!TextUtils.isEmpty(ordermap.get("state").toString())){
					if(ordermap.get("state").toString().equals("Y")){
						printer.printText("当前状态 : 支付成功\n");  //支付状态
					}else{
						printer.printText("当前状态 : 支付失败\n"); //支付状态
					}

				}else{
					printer.printText("当前状态 : 未知\n"); //支付状态
				}



				printer.printText("交易时间 : "+ordermap.get("payDate").toString()+"\n");

				if(!TextUtils.isEmpty(ordermap.get("payChanner").toString())){
					if(ordermap.get("payChanner").toString().equals("P")){
						printer.printText("支付方式 : 刷卡支付\n");
					}else{
						if(ordermap.get("payChanner").toString().equals("C")){
							printer.printText("支付方式 : 现金支付\n");
						}else if(ordermap.get("payChanner").toString().equals("B")){
							printer.printText("支付方式 : 联名卡支付\n");
						}else if(ordermap.get("payChanner").toString().equals("W")){
							printer.printText("支付方式 : 微信支付\n");
						}
					}
				}else{
					printer.printText("支付方式 : 未知\n");
				}

				printer.printText("订 单 号 : "+ordermap.get("orderNo").toString()+"\n");

				if(ordermap.containsKey("responseId")){

				if(!TextUtils.isEmpty(ordermap.get("responseId").toString())){
					String[] payliuid = ordermap.get("responseId").toString().split(",");
					printer.printText("流 水 号 : "+payliuid[0]+"\n"); //流 水 号

				}else{
					printer.printText("流 水 号 : "+"未知"+"\n"); //流 水 号
				}
				}

				printer.printText("\n");
				printer.printText("------------------------------\n");
//				printer.printText("应付金额 : "+billbean.getSumAmt()+"\n");
//				printer.printText("折 扣 率 : "+billbean.getDiscRate()*100+"\n");
//				printer.printText("抹    零 : "+billbean.getIgnoreAmt()+"\n");
				printer.printText("实付金额 : "+"￥"+ordermap.get("amt").toString()+"\n");
				printer.printText("\n");
//				printer.printText("---The Client Stub---\n");

				/** 中文字符打印，此处使用16x16点，1倍宽&&1倍高  */



				/** 打印条码  */
				printer.printBarCode(ordermap.get("orderNo")+"");

				printer.printText("\n");
				printer.printText("------------------------------\n");
				format.setHzScale(Format.HZ_SC1x1);
				format.setHzSize(Format.HZ_DOT16x16);
				printer.printText(Alignment.CENTER,"即刻追溯（北京）数据科技有限公司\n");

				/** 打印二维码  */
			/*	printer.printQrCode(0, new QrCode("sdafsadf", QrCode.ECLEVEL_Q), 100);
				printer.printQrCode(Alignment.CENTER, new QrCode("landi", QrCode.ECLEVEL_Q), 124);
				printer.printQrCode(Alignment.RIGHT, new QrCode("landi", QrCode.ECLEVEL_Q), 124);*/

//				printer.printText(Alignment.CENTER, "------时刻追溯------\n");
//				printer.printText(Alignment.RIGHT, "www.landicorp.com\n");

				/** 进纸5行  */
				printer.feedLine(5);
			}

			@Override
			public void onFinish(int code) {
				/** Printer.ERROR_NONE即打印成功 */
				if (code == Printer.ERROR_NONE) {
					showMessage("打印签购单成功！");
				}
				else {

					if(code == Printer.ERROR_PAPERENDED){
						ToastUtil.makeToast("打印纸不够，请装纸后再打印！");
					}

//					showMessage("[打印失败]"+getErrorDescription(code));
				}
			}

			/** 根据错误码获取相应错误提示
			 *  @param code 错误码
			 *  @return 错误提示
			 */
			public String getErrorDescription(int code) {
				switch(code) {
					case Printer.ERROR_PAPERENDED: return "Paper-out, the operation is invalid this time";
					case Printer.ERROR_HARDERR: return "Hardware fault, can not find HP signal";
					case Printer.ERROR_OVERHEAT: return "Overheat";
					case Printer.ERROR_BUFOVERFLOW: return "The operation buffer mode position is out of range";
					case Printer.ERROR_LOWVOL: return "Low voltage protect";
					case Printer.ERROR_PAPERENDING: return "Paper-out, permit the latter operation";
					case Printer.ERROR_MOTORERR: return "The printer core fault (too fast or too slow)";
					case Printer.ERROR_PENOFOUND: return "Automatic positioning did not find the alignment position, the paper back to its original position";
					case Printer.ERROR_PAPERJAM: return "paper got jammed";
					case Printer.ERROR_NOBM: return "Black mark not found";
					case Printer.ERROR_BUSY: return "The printer is busy";
					case Printer.ERROR_BMBLACK: return "Black label detection to black signal";
					case Printer.ERROR_WORKON: return "The printer power is open";
					case Printer.ERROR_LIFTHEAD: return "Printer head lift";
					case Printer.ERROR_LOWTEMP: return "Low temperature protect";
				}
				return "unknown error ("+code+")";
			}

			@Override
			public void onCrash() {
				onDeviceServiceCrash();
			}
		};

		/** 3、启动打印 */
		try {
			progress.start();
		} catch (RequestException e) {
			e.printStackTrace();
			onDeviceServiceCrash();
		}
	}

	protected String getPrintStr(String printStr,int width,String align ){

		String blankStr = "";

		int num = width - printStr.length();

		for(int i=0;i<num;i++){
			blankStr +="\t";
		}


		if("left".equals(align)){
			printStr = printStr + blankStr;
		}else{
			printStr = blankStr + printStr;
		}
		LogUtil.v("title",printStr);
		return printStr;

	}

	/*
         * 将时间戳转换为时间
         */
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	@Override
	protected void showMessage(String msg) {

	}

	@Override
	protected void onDeviceServiceCrash() {
		Log.d(TAG, TAG+"onDeviceServiceCrash!");
	}
}
