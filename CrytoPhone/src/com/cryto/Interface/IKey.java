package com.cryto.Interface;

import java.util.HashMap;

public interface IKey {

	public int  getID();
	public void setID(byte nType);

	public int		getLength();
	public int		getExtLength();
	public byte[] data2Bytes() ;
	public boolean checkDataIntegrity(byte[] bytes);
	public boolean checkDataIntegrity(byte[] bytes, int nIndex);
	
	
	public HashMap<Integer, IDataInfo> getAllKey() ;
	
	public static final int DEFAULT       = 0x00000001;
	public static final int PUBLIC_KEY  = 0x00000002;
	public static final int PRIVATE_KEY = 0x00000004;
}
