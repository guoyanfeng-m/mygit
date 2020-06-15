package util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import service.config.ConfigService;
import service.errorLog.ErrorLogService;
import beans.errorLog.ErrorLogBean;

@Component
public class PublishUtil{
	@Autowired
	private ConfigService configService;
	@Autowired
	private ErrorLogService errorLogService;

	public  boolean publishTask(JSONObject jsons,String ip,String modulename) {
		Socket socket = null;
		OutputStream os=null;
		try {
			if (null==ip||"".equals(ip)) {
				ip="localhost";
			}
			socket = new Socket(ip,60001);
			os = socket.getOutputStream();
			String str = jsons.toString();
			byte[] bys = str.getBytes("UTF-8");
			System.out.println("压缩前字节数：" + bys.length);
			//字节压缩  
			byte[] data = ZLibUtil.compress(bys);  
			int size = data.length;
			System.out.println("压缩后字节数:" + size);
//			byte[] sizes = new byte[4];
//			sizes[3] = (byte)(0xff & size);
//			sizes[2] = (byte)((0xff00 & size) >> 8);
//			sizes[1] = (byte)((0xff0000 & size) >> 16);
//			sizes[0] = (byte)((0xff000000 & size) >> 24);
			byte[] bufret = null;
			byte[] prefixArray = intToByte(0x90785634);
            byte[] midArray = intToByte(0x00000000);
            byte[] array = intToByte(size);
            bufret = new byte[prefixArray.length + midArray.length];
            System.arraycopy(prefixArray, 0, bufret, 0, 4);
            System.arraycopy(midArray, 0, bufret, 4, 4);
//			sizes[0] = (byte) (0xff & size);
//			sizes[1] = (byte) ((0xff00 & size) >> 8);
//			sizes[2] = (byte) ((0xff0000 & size) >> 16);
//			sizes[3] = (byte) ((0xff000000 & size) >> 24);
			os.write(bufret);
			os.flush();
//			os.write(midArray);
//			os.flush();
			os.write(array);
			os.flush();
			os.write(data);
			os.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println(e.toString());
			if(e.toString().indexOf("ConnectException") > -1){
				if(configService.queryConfig("islisten").equals("1")){
		        	ErrorLogBean elb = new ErrorLogBean();
					elb.setClass_name(PublishUtil.class.toString());
					elb.setException_type("ConnectException");
					elb.setException_reason("socket连接失败(请检查tfbs是否开启)");
					elb.setFunction_name("socket连接");
					elb.setModule_name(modulename);
					elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
					errorLogService.insertErrorLog(elb);
	        	}
			}
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(null!=os){
					os.close();
				}
				if(null!=socket){
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	public static byte[] intToByte(int length) {
	    byte[] array = new byte[4];
        array[0] = (byte) (0xff & length);
        array[1] = (byte) ((0xff00 & length) >> 8);
        array[2] = (byte) ((0xff0000 & length) >> 16);
        array[3] = (byte) ((0xff000000 & length) >> 24);
	    return array;
	}
//	public boolean SendData(String message) {
//	    boolean success = true;
//	    try {
//	        if (Constant.IS_NEW_COMPRESS_PROTOCOL_VERSION) {
//	            //write head
//	            byte[] prefixArray = intToByte(0x90785634);
//	            byte[] midArray = intToByte(0x00000000);
//
//	            byte[] buffer = ZLibUtil.compress(message.getBytes("UTF-8"));
//	            int size = buffer.length;
//	            byte[] array = intToByte(size);
//	            byte[] sendContent = new byte[4 + 4 + 4 + size];
//	            System.arraycopy(prefixArray, 0, sendContent, 0, 4);
//	            System.arraycopy(midArray, 0, sendContent, 4, 4);
//	            System.arraycopy(array, 0, sendContent, 8, 4);
//	            System.arraycopy(buffer, 0, sendContent, 12, size);
//
//	            this.m_out.write(sendContent);
//	            this.m_out.flush();
//	        } else {
//	            byte[] buffer = message.getBytes("UTF-8");
//	            int size = buffer.length;
//	            byte[] array = intToByte(size);
//	            this.m_out.write(array);
//	            this.m_out.flush();
//	            this.m_out.write(buffer);
//	            this.m_out.flush();
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        success = false;
//	    }
//	    return success;
//	}
//
//	public byte[] receiveData() {
//	    byte[] prefixContent = new byte[4];
//	    byte[] midContent = new byte[4];
//	    byte[] length = new byte[4];
//	    byte[] bits = new byte[1];
//	    byte[] buffers;
//
//	    try {
//	        if (Constant.IS_NEW_COMPRESS_PROTOCOL_VERSION) {
//	            int readPrefixResult = this.m_in.read(prefixContent);
//	            if (readPrefixResult == -1) {// 掉线了
//	                return null;
//	            }
//	            this.m_in.read(midContent);
//	            this.m_in.read(length);
//	            int size = byte2Int(length);
//	            if (size == 0) {// 没发消息
//	                return bits;
//	            }
//	            int readCount = 0;
//	            byte [] readSrcContent = new byte[size];
//	            while (readCount < size) {
//	                readCount += this.m_in.read(readSrcContent, readCount, size - readCount);
//	            }
//	            buffers = ZLibUtil.decompress(readSrcContent);
//	        } else {
//	            int result = this.m_in.read(length);
//	            if (result == -1) {// 掉线了
//	                return null;
//	            }
//	            int size = byte2Int(length);
//	            if (size == 0) {// 没发消息
//	                return bits;
//	            }
//	            int readCount = 0;
//	            buffers = new byte[size];
//	            while (readCount < size) {
//	                readCount += this.m_in.read(buffers, readCount, size - readCount);
//	            }
//	        }
//
//	        return buffers;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	    return null;
//	}

//
//	/**
//	 * 把字节数组转化为整型
//	 *
//	 * @param bytes 字节数组
//	 * @return int 长度
//	 */
//	public int byte2Int(byte[] bytes) {
//	    byte[] temp = new byte[4];
//	    byte b;
//	    int size = 0;
//	    if (Constant.IS_NEW_COMPRESS_PROTOCOL_VERSION) {
//	        for (int j = 0; j < 4; j++) {
//	            b = bytes[j];
//	            size += (b & 0xFF) << (8 * j);
//	        }
//	    } else {
//	        for (int i = 0; i < 4; i++) {
//	            temp[3 - i] = bytes[i];
//	        }
//	        for (int j = 0; j < 4; j++) {
//	            b = temp[j];
//	            size += (b & 0xFF) << (8 * j);
//	        }
//	    }
//	    return size;
//	}

}


