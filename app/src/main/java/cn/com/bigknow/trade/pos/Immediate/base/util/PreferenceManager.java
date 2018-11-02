package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PreferenceManager {

	public static boolean save(Context context, Object entity) {
		try {
			
			clear(context, entity.getClass());
			
			// 保存对象
			SharedPreferences.Editor sharedata = context.getSharedPreferences(getKey(entity), 0).edit();
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			// 将对象序列化写入byte缓存
			os.writeObject(entity);
			// 将序列化的数据转为Base64保存
			String bytesToHexString = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
			// 保存该16进制数组
			sharedata.putString(getKey(entity), bytesToHexString);
			sharedata.commit();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String getKey(Object entity) {
		return entity.getClass().getName().toLowerCase();
	}

	private static String getKey(Class clazz) {
		return clazz.getName().toLowerCase();
	}

	/**
	 * desc:获取保存的Object对象
	 * 
	 * @param context
	 * @return modified:
	 */
	public static Object get(Context context, Class clazz) {
		try {
			SharedPreferences sharedata = context.getSharedPreferences(getKey(clazz), 0);
			if (sharedata.contains(getKey(clazz))) {
				String string = sharedata.getString(getKey(clazz), "");
				if (TextUtils.isEmpty(string)) {
					return null;
				} else {
					// 将Base64的数据转为数组，准备反序列化
					byte[] stringToBytes = Base64.decode(string, Base64.DEFAULT);
					ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is = new ObjectInputStream(bis);
					// 返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 所有异常返回null
		return null;
	}

	public static void clear(Context context, Class clazz) {
		SharedPreferences sharedata = context.getSharedPreferences(getKey(clazz), 0);
		sharedata.edit().clear().apply();
		delete(context,clazz);
	}

	private static void delete(Context context, Class clazz) {

		File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", getKey(clazz) + ".xml");
		if (file.exists()) {
			file.delete();
		}

	}

}
