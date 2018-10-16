package cn.com.ths.ssoprovider;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * 类名: SimpleCrypto</br> 
 * 包名：cn.com.ths.sso </br> 
 * 描述: 加密解密</br> 
 * 发布版本号：</br> 
 * 开发人员：chixin</br> 
 * 创建时间： 2016-11-13
 */
public class SimpleCrypto {
	public SimpleCrypto() {
	}

	static final String algorithmStr = "AES/ECB/PKCS5Padding";

	private byte[] encrypt(String content, String password) {
		try {
			byte[] keyStr = getKey(password);
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(algorithmStr);// algorithmStr
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// ʼ
			byte[] result = cipher.doFinal(byteContent);
			return result; //
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] decrypt(byte[] content, String password) {
		try {
			if (content == null) {
				return null;
			}
			byte[] keyStr = getKey(password);
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(algorithmStr);// algorithmStr
			cipher.init(Cipher.DECRYPT_MODE, key);// ʼ
			byte[] result = cipher.doFinal(content);
			return result; //
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getKey(String password) {
		byte[] rByte = null;
		if (password != null) {
			rByte = password.getBytes();
		} else {
			rByte = new byte[24];
		}
		return rByte;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public byte[] parseHexStr2Byte(String hexStr) {
		try {
			if (hexStr.length() < 1)
				return null;
			byte[] result = new byte[hexStr.length() / 2];
			for (int i = 0; i < hexStr.length() / 2; i++) {
				int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1),
						16);
				int low = Integer.parseInt(
						hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
				result[i] = (byte) (high * 16 + low);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 注意: 这里的password(秘钥必须是16位的)
	private static final String keyBytes = "beijingsolutionc";

	/**
	 * 描述:加密 </br> 开发人员：chixin</br> 创建时间：2016-11-13</br>
	 * 
	 * @param content
	 *            原始数据
	 * @return 返回加密数据
	 */
	public String encode(String content) {
		// 加密之后的字节数组,转成16进制的字符串形式输出
		return parseByte2HexStr(encrypt(content, keyBytes));
	}

	/**
	 * 描述: 解密</br> 开发人员：chixin</br> 创建时间：2016-11-13</br>
	 * 
	 * @param content
	 *            加密数据
	 * @return 返回解密后的数据
	 */
	public String decode(String content) {
		// 解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
		byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
		return b == null ? "" : new String(b);
	}
}