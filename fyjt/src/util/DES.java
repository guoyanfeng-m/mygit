package util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
	 	
public class DES {
	    public static String encryptDES(String encryptString, String encryptKey) throws Exception {	//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
	    	IvParameterSpec iv = new IvParameterSpec(encryptKey.getBytes("UTF-8"));
	        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
	      
	        return Base64.encode(encryptedData);
    }
    @SuppressWarnings("static-access")
	public static String decryptDES(String decryptString, String decryptKey)  {
        byte[] byteMi = new Base64().decode(decryptString);
        IvParameterSpec iv;
		try {
			iv = new IvParameterSpec(decryptKey.getBytes("UTF-8"));
			 SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		        cipher.init(Cipher.DECRYPT_MODE, key, iv);
			        byte decryptedData[] = cipher.doFinal(byteMi);
			        return new String(decryptedData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("解密错误");
			return null;
		}
   
      
    }
}
 	