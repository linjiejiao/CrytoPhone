package com.cryto.Impl;

import com.cryto.Interface.IDataIntegrity;

public abstract class DataIntegrityImpl implements  IDataIntegrity{
	
	public boolean checkDataIntegrity(byte[] bytes) {
		 int nLen = bytes.length;
		 boolean bHeader = ((bytes[0] & START_FLAG)  == START_FLAG);
		 boolean bTailer   = ((bytes[nLen - 1] & END_FLAG)  == END_FLAG);
		 
		return (bHeader & bTailer);
	}

	public int getOriginalDataLength() {
	
		return mnOriginalDataLength;
	}

	public int getEncryptedDataLeng() {
		return mnEncryptedDataLeng;
	}

	public void setOriginalDataLength(int nDataLen) {
		
		  mnOriginalDataLength = nDataLen;
	}

	public void setEncryptedDataLeng(int nDataLen) {
		  mnEncryptedDataLeng = nDataLen;
	}
	
	protected void addStartFlag(byte[] bytes){
		bytes[0] = START_FLAG;
	}
	protected void addEndFlag(byte[] bytes, int nIndex){
		bytes[nIndex] = END_FLAG;
	}
	
	protected int getFixHeaderLen()
	{
		return FIX_HEAD_FLAG_LENGTH;
	}
	private   int      mnOriginalDataLength;
	private   int      mnEncryptedDataLeng;
	
	private static final int FIX_HEAD_FLAG_LENGTH =2;
}
