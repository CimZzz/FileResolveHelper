package com.virtualightning.fileresolver.ui;

public class ByteUtils {
	
	/*
	 * 
	 * 整数
	 * 
	 * */
	
	/**
	 * 将整数转换为 Byte 数组
	 * @param num 整数
	 * @param length Byte数组长度（如果长度超出会自动截取）
	 * @param bigEndianOrder 是否为大端字节序
	 * @return Byte 数组
	 */
	public static byte[] intConvertByte(int num,int length,boolean bigEndianOrder) {
		byte[] bytes = new byte[length];
		boolean isOverLength = length >= Integer.BYTES;
		int validCount = 0;
		
		if(bigEndianOrder) {
			if(!isOverLength) {
				for(int i = 0 , flag = 0xFF000000; i < Integer.BYTES ; i ++,flag >>>= 8) {
					if((flag & num) != 0) {
						validCount = Integer.BYTES - i;
						if(validCount > length)
							num >>>= (8 * (validCount - length));
						break;
					}
				}
			} else validCount = Integer.BYTES;
			
			while(length -- > 0) {
				if(validCount == 0) {
					bytes[length] = 0;
					continue;
				}
				bytes[length] = (byte) (num & 0xFF);
				num >>>= 8;
				validCount --;
			}
		} else {
			if(!isOverLength) {
				for(int i = 0,flag = 0xFF000000; i < Integer.BYTES ; i ++,flag >>>= 8) {
					if((flag & num) != 0) {
						validCount = Integer.BYTES - i;

						if(validCount > length) 
							num >>>= (8 * (validCount - length));
						
						break;
					}
				}
			} else validCount = Integer.BYTES;
			
			for(int i = 0 ; i < length ; i ++) {
				if(validCount == 0) {
					bytes[i] = 0;
					continue;
				}
				bytes[i] = (byte) (num & 0xFF);
				num >>>= 8;
				validCount --;
			}
		}
		return bytes;
	}

	/**
	 * 转换字节数组为整数
	 * @param bytes 字节数组
	 * @param isBigEndian 是否为大端字节序
	 * @return 整数
	 */
	public static int byteConvertInt(byte[] bytes,boolean isBigEndian)  {
		return byteConvertInt(bytes,0,bytes.length,isBigEndian);
	}
	
	/**
	 * 转换字节数组为整数
	 * @param bytes 字节数组
	 * @param off 偏移值
	 * @param length 截取长度
	 * @param isBigEndian 是否为大端字节序
	 * @return 整数
	 */
	public static int byteConvertInt(byte[] bytes,int off,int length,boolean isBigEndian) {
		int tmp = 0;
		if(isBigEndian)
			for(int i = off ; i < length ; i ++)
				tmp = (tmp << 8) + (((int)bytes[i]) & 0xFF);
		else
			for(int i = off + length - 1 ; i >= off ; i --)
				tmp = (tmp << 8) + (((int)bytes[i]) & 0xFF);
			
		return tmp;
	}
	
	/**
	 * 获取整数实际占用几个字节
	 * @param num 整数
	 * @return 占用字节数
	 */
	public static int getIntegerRealByteLength(int num) {
		if(num <= 0xFF)
			return 1;
		else if (num <= 0xFFFF)
			return 2;
		else if (num <= 0xFFFFFF)
			return 3;
		else return 4;
	}
	
	/*
	 * 
	 * 长整数
	 * 
	 * */
	
	/**
	 * 将长整数转换为 Byte 数组
	 * @param num 长整数
	 * @param length Byte数组长度（如果长度超出会自动截取）
	 * @param bigEndianOrder 是否为大端字节序
	 * @return Byte 数组
	 */
	public static byte[] longConvertByte(long num,int length,boolean bigEndianOrder) {
		byte[] bytes = new byte[length];
		boolean isOverLength = length >= Long.BYTES;
		int validCount = 0;
		if(bigEndianOrder) {
			if(!isOverLength) {

				long flag = 0xFF00000000000000L;
				for(int i = 0 ; i < Long.BYTES ; i ++,flag >>>= 8) {
					if((flag & num) != 0) {
						validCount = Long.BYTES - i;
						if(validCount > length)
							num >>>= (8 * (validCount - length));
						break;
					}
				}
			} else validCount = Long.BYTES;
			
			while(length -- > 0) {
				if(validCount == 0) {
					bytes[length] = 0;
					continue;
				}
				bytes[length] = (byte) (num & 0xFF);
				num >>>= 8;
				validCount --;
			}
		} else {
			if(!isOverLength) {
				long flag = 0xFF00000000000000L;
				
				for(int i = 0; i < Long.BYTES ; i ++,flag >>>= 8) {
					if((flag & num) != 0) {
						validCount = Long.BYTES - i;

						if(validCount > length) 
							num >>>= (8 * (validCount - length));
						
						break;
					}
				}
			} else validCount = Long.BYTES;
			
			for(int i = 0 ; i < length ; i ++) {
				if(validCount == 0) {
					bytes[i] = 0;
					continue;
				}
				bytes[i] = (byte) (num & 0xFF);
				num >>>= 8;
				validCount --;
			}
		}
		return bytes;
	}

	/**
	 * 转换字节数组为长整数
	 * @param bytes 字节数组
	 * @param isBigEndian 是否为大端字节序
	 * @return 长整数
	 */
	public static long byteConvertLong(byte[] bytes,boolean isBigEndian)  {
		return byteConvertLong(bytes,0,bytes.length,isBigEndian);
	}
	
	/**
	 * 转换字节数组为长整数
	 * @param bytes 字节数组
	 * @param off 偏移值
	 * @param length 截取长度
	 * @param isBigEndian 是否为大端字节序
	 * @return 长整数
	 */
	public static long byteConvertLong(byte[] bytes,int off,int length,boolean isBigEndian) {
		long tmp = 0;
		if(isBigEndian)
			for(int i = off ; i < length ; i ++)
				tmp = (tmp << 8) + (((int)bytes[i]) & 0xFF);
		else
			for(int i = off + length - 1 ; i >= off ; i --)
				tmp = (tmp << 8) + (((int)bytes[i]) & 0xFF);
			
		return tmp;
	}
	
	/**
	 * 获取长整数实际占用几个字节
	 * @param num 长整数
	 * @return 占用字节数
	 */
	public static int getLongRealByteLength(int num) {
		if(num <= 0xFF)
			return 1;
		else if (num <= 0xFFFF)
			return 2;
		else if (num <= 0xFFFFFF)
			return 3;
		else if (num <= 0xFFFFFFFF)
			return 4;
		else if (num <= 0xFFFFFFFFFFL)
			return 5;
		else if (num <= 0xFFFFFFFFFFFFL)
			return 6;
		else if (num <= 0xFFFFFFFFFFFFFFL)
			return 7;
		else return 8;
	}
} 
