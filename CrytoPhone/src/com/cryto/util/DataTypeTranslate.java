package com.cryto.util;

public class DataTypeTranslate {
	public static int   writeShort2byte(short nValue, byte[] bytes, int nIndex)
	{	
			byte[] shortTobytes = short2byte(nValue);
			Debug.PrintBytes("writeShort2byte " , shortTobytes);
			System.arraycopy(shortTobytes, 0 , bytes, nIndex, shortTobytes.length);
	   	    return  getShortBytesCount();
	}
	
	public static int   writeInt2byte(int nValue, byte[] bytes, int nIndex)
	{
		byte[] shortTobytes = int2byte(nValue);
		Debug.PrintBytes("writeInt2byte " , shortTobytes);
		System.arraycopy(shortTobytes, 0 ,bytes , nIndex, shortTobytes.length);
   	    return  getIntBytesCount();
	}
	
	public static int   writeLong2byte(long nValue, byte[] bytes, int nIndex)
	{
 	     return writeInt2byte((int)nValue, bytes, nIndex);
	}
	
	// read from buffer ;
	private static byte[] getByteFromBuffer(byte[] bytes, int nIndex, int nLength){
		  byte[] Outbytes =new byte[nLength];
		  System.arraycopy(bytes, nIndex, Outbytes, 0 , nLength);
		  return Outbytes;
	}

	public static short  readShort(byte[] bytes, int nIndex){
		 
		byte[] bytesdata = getByteFromBuffer(bytes, nIndex, getShortBytesCount());
	    String szValue = new String(bytesdata);
	    
		 Debug.PrintBytes("Read Short", bytesdata);
		 System.out.println(" read short ["+ szValue +"]");
	   	return Integer.valueOf(szValue, 16).shortValue();
	}
	
	public static int  readInt(byte[] bytes, int nIndex){
		byte[] bytesdata = getByteFromBuffer(bytes, nIndex, getIntBytesCount());
		Debug.PrintBytes("Read Int", bytesdata);
		String szValue =  new String( bytesdata);
		System.out.println(" read Int ["+ szValue +"]");
		return Integer.parseInt(szValue, 16);
	}
	
	
	// type to bytes
	public static byte[]   short2byte(short nValue){
        return ValueToHex(nValue, SHORT_TEMPLATE);
	}

	public static byte[]   int2byte(int nValue){
	   	return  ValueToHex(nValue, INTEGE_TEMPLATE);
	}
	
	public static byte[]  long2byte(long nValue)
	{
		 
	   	return  int2byte((int)nValue);
	}
	
	private static byte[]  ValueToHex(int nValue ,String szModel){
		   int nMaxBytes = szModel.length();
		   StringBuilder szValue = new StringBuilder(szModel);
		   String szShortValue = Integer.toHexString(nValue);
		   int nLen = szShortValue.length();
		   if( nLen > nMaxBytes){
			   throw new RuntimeException(" Translate Data Type Error" );
		   }else{
			   int nStart = nMaxBytes - nLen;
			   szValue.replace(nStart, nMaxBytes, szShortValue);
		   }
		   return szValue.toString().getBytes();
	}
	
	// byte to other type 
	public static short  byte2short(byte[] bytes){
		return readShort(bytes, 0);
	}
	
	public static int  byte2int(byte[] bytes){
	   	return readInt(bytes, 0);
	}
	
	public static long  byte2long(byte[] bytes){
		return byte2int(bytes);
	}
	
	
	public static int getLongBytesCount(){
		return 8;
	}
	
	public static int getIntBytesCount(){
		return 8;
	}
	

	public static int getShortBytesCount(){
		return 4;
	}
	
	public static int getCalcByteCount(int nIntCout, int nShortCount){
		return (nIntCout * getIntBytesCount() + nShortCount * getShortBytesCount());
	}

	private static final String SHORT_TEMPLATE ="0000";
	private static final String INTEGE_TEMPLATE ="00000000";

}
