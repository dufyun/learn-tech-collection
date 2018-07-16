package org.learn.rest.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {
	
	static Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	static final String encoding="utf-8";
	
	public final static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			logger.error("MD5加密出现 异常:",e);
			return null;
		}
	}
	
	/**
	 * md5校验两密串是否相等
	 * @param  src<String> 源密串
	 * @param  des<String> 目标密串
	 * @return fase: 验证匹配 失败  true 验证匹配成功
	*/
	static public boolean verify(String src, String des) {
		MessageDigest md5 = null;
		StringBuffer hexValue = new StringBuffer(32);
		try {
			md5 = MessageDigest.getInstance("MD5");
		

		byte[] byteArray = null;
			byteArray = src.getBytes(encoding);
		
	
		byte[] md5Bytes = md5.digest(byteArray);
		
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
	} catch (UnsupportedEncodingException e) {
		logger.error("MD5加密出现 UnsupportedEncodingException异常:",e);
		return false;
	} catch (NoSuchAlgorithmException e) {
		logger.error("MD5加密出现 NoSuchAlgorithmException异常:",e);
		return false;
	}
		return hexValue.toString().equals(des);
	}
	
	/**
	 * 使用指定的字符编码加进行md5加密
	 * @param  src<String> 源串
	 * @param  charset<String> 编码格式
	 * @return 加密后的密串
	 */
	static public String MD5Encode(String src, String charset){
		MessageDigest md5 = null;
		StringBuffer hexValue = new StringBuffer(32);
		try{
		md5 = MessageDigest.getInstance("MD5");

		byte[] byteArray = null;
		byteArray = src.getBytes(charset);
	
		byte[] md5Bytes = md5.digest(byteArray);
		
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
	} catch (UnsupportedEncodingException e) {
		logger.error("MD5加密出现 UnsupportedEncodingException异常:",e);
		return "";
	} catch (NoSuchAlgorithmException e) {
		logger.error("MD5加密出现 NoSuchAlgorithmException异常:",e);
		return "";
	}
		return hexValue.toString();
	}
	
	/**
	 * 使用默认编码进行md5加密
	 * @param  src<String> 源串
	 * @return 加密后的密串
	 */
	static public String MD5Encode(String src){
		return MD5Encode(src,encoding);
	}

	public static void main(String[] args) {
		String md5Str=MD5Util.md5("rest");
		String md5encodingStr=MD5Util.MD5Encode("rest");
		logger.info("md5Str={},md5encodingStr={}",md5Str,md5encodingStr);
		logger.info("对比={}",md5Str.equals(md5encodingStr));
		
	}
}
