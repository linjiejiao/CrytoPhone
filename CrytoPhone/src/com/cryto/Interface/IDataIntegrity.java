package com.cryto.Interface;

public interface IDataIntegrity {
	public void 		 Debug();
	public byte[]      getByte();
	public byte[]      data2Bytes();
	
	public int			 getOriginalDataLength();
	public int			 getEncryptedDataLeng();
	public  boolean  checkDataIntegrity(byte[] bytes);
	public void 		 setOriginalDataLength(int nDataLen);
	public void 		 setEncryptedDataLeng(int nDataLen) ;
	
	public static final byte START_FLAG = 0x10;
	public static final byte END_FLAG  = 0x10;
	public static final int  HEADER_ERROR   =0x00000001;
	public static final int  TAILER_ERROR    =0x00000002;
}
