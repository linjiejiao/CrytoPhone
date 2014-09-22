package com.cryto.Impl;

import com.cryto.Interface.IDataInfo;
import com.cryto.Interface.IKeyGenerator;
import com.cryto.util.DataTypeTranslate;

public class SampleKey implements IDataInfo {
	/* **************************************************** */
	/* ** |key_type| keylen|key_value| */
	/* ** |1 | 2 | n | */
	/* ***************************************************** */
	public SampleKey() {
	
	}
	public SampleKey(byte[] bytes) {
		init(bytes);
	}

	public SampleKey(byte[] bytes, int nStart) {
		init(bytes, nStart);
	}

	public boolean init(byte[] bytes) {
		return init(bytes, 0);
	}

	public boolean init(byte[] bytes, int nStartIndex) {
		int nIndex = nStartIndex;
		
		setType(bytes[nIndex]);
		++nIndex;

		int nLen = DataTypeTranslate.readShort(bytes, nIndex);
		System.out.println("SampleKey nStartIndex =["+nIndex +"] Key Len =["+ nLen +"]");
		setLength(nLen);
	  //	Debug();
		mbytes = null;
		mbytes = new byte[nLen];
	 //	Debug();
		System.arraycopy(bytes, nIndex , mbytes, 0, nLen);

		return true;
	}

	public byte[] data2Byte() {
		int nLen = getLength() + getExtLength();
		int nIndex = 0;
		byte[] bytes = new byte[nLen ];
		
		bytes[nIndex] = (byte) (getType());
		++nIndex;
		
		System.out.println(" ======>>>>SampleKey Context Len =["+ getLength()+"]");
		nIndex +=DataTypeTranslate.writeShort2byte((short) (getLength()), bytes, nIndex);
		
		
	//	Debug();
		if (getData() != null) {
			System.arraycopy(getData(), 0, bytes, nIndex, getLength());
		}
		return bytes;

	}

	public int getType() {
		return mnType;
	}

	public  void    	setDataRef(byte[] bytes )
	{
		mbytes = null;
		mbytes = bytes;
		setLength(bytes.length);
	}
	public void  setData(byte[] bytes) {
		mbytes = null;
		int nLen = bytes.length;
		mbytes = new byte[nLen];
		System.arraycopy(bytes, 0, mbytes, 0, nLen);
		setLength(nLen);
	}
	
	public byte[] getData() {
		return mbytes;
	}

	public Object getObject() {
		switch (this.getType()) {
		case IKeyGenerator.KEY_NUMBER_LIMIT_BIT:
		case IKeyGenerator.KEY_NUMBER_LIMIT_LEN:
			return DataTypeTranslate.readInt(getData(), 0);
		 
		case IKeyGenerator.KEY_STRNG:
			return new String(this.getData());
		default:
			System.out.println("Type =[" + getType() + "]");
			return null;
		}
		
	}
	
	public int getLength() {
		return mnLength;
	}

	public int getExtLength() {
		return DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT, SHORT_DATA_COUNT) +EXT_DATA_LEN;
	}

	protected void setLength(int nLen) {
		mnLength = nLen;
	}

	public void setType(byte bytetype) {
		mnType = bytetype;
	}

	public void Debug() {
		System.out.println(" <<<=== Sample Key start===>>>");
		System.out.println(" TYPE =[" + mnType + "]");
		System.out.println(" Length =[" + mnLength + "]");
		if(mbytes != null){
		System.out.println(" Releal Data Length =[" + mbytes.length + "]");
		System.out.println("Context [" + new String(mbytes) + "]");
		}
		System.out.println(" <<<=== Sample Key End===>>>");
	}

	private byte mnType;
	private int mnLength;
	private byte[] mbytes;
	private static final int INT_DATA_COUNT = 0;
	private static final int SHORT_DATA_COUNT =1 ;
	private static final int EXT_DATA_LEN = 1;
}
