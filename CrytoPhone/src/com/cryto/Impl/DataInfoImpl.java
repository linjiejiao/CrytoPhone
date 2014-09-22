package com.cryto.Impl;

import com.cryto.Interface.IDataInfo;
import com.cryto.util.DataTypeTranslate;

/**********************************************
 * |type|len|data|
 * |1    | 4  |n |  
 *
 **********************************************/
public abstract class DataInfoImpl implements  IDataInfo{

	public int getLength() {
	
		return mnLen;
	}

	public int getType() {
	
		return mnType;
	}


	public byte[] getData() {
		// data copy, void user modify data
		byte[] bytes = new byte[getLength()];
		System.arraycopy(mbyteData, 0, bytes, 0, getLength());
		return bytes;
	}
	

	public byte[] data2Byte() {
		 int nIndex = 0;
		 int nLen = getLength() + getExtLength();
		 byte[] bytes = new byte[nLen];
		 bytes[0] = (byte)getType();
		 ++nIndex;
		 nIndex +=DataTypeTranslate.writeInt2byte(getLength(), bytes, nIndex);
		 System.arraycopy(mbyteData, 0, bytes, nIndex, getLength());
		return bytes;
	}
	
	public String toString()
	{
		return "";
	}
	
	 


	public int getExtLength() {
		 
		return (DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT, SHORT_DATA_COUNT) + TYPE_DATA_LEN);
	}
	
	public void Debug()
	{
		
	}
	private int mnLen;
	private int mnType;
	private byte[] mbyteData;
	
	private static final int TYPE_DATA_LEN  =1 ;

	private static final int INT_DATA_COUNT = 1;
	private static final int SHORT_DATA_COUNT = 0;


	

}
