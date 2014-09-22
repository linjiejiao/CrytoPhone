package com.cryto.util;

import com.cryto.Impl.DataIntegrityImpl;

public class Header extends DataIntegrityImpl {
	/* *****************************************/
	/* * |start_flag|version|src_length|des_length|segment_cout|end_flag| */
	/*  * |   1     | 2     |  4       |  4       |4           | 1      | */
	/* ********************************************************** */
	public boolean checkDataIntegrity(byte[] bytes) {
		if (!super.checkDataIntegrity(bytes)) {
			return false;
		}
		Debug.PrintBytes("Header. checkDataIntegrity" , bytes);
		int nIndex = 1;
		setVersion(DataTypeTranslate.readShort(bytes, nIndex));
		nIndex += DataTypeTranslate.getShortBytesCount();

		setOriginalDataLength(DataTypeTranslate.readInt(bytes, nIndex));
		nIndex += DataTypeTranslate.getIntBytesCount();

		setEncryptedDataLeng(DataTypeTranslate.readInt(bytes, nIndex));
		nIndex += DataTypeTranslate.getIntBytesCount();

		setSegmentCount(DataTypeTranslate.readInt(bytes, nIndex));

		return true;
	}
	
	public byte[]     getByte(){
		// package header infomation;
		return data2Bytes();
	}

	public byte[] data2Bytes() {
		int nContextLen = DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT,
				SHORT_DATA_COUNT) + getFixHeaderLen();
		byte[] bytes = new byte[nContextLen];
		int nIndex = 1;
		addStartFlag(bytes);
		nIndex += DataTypeTranslate
				.writeShort2byte(getVersion(), bytes, nIndex);
		
		nIndex += DataTypeTranslate.writeInt2byte(getOriginalDataLength(),
				bytes, nIndex);
		
		nIndex += DataTypeTranslate.writeInt2byte(getEncryptedDataLeng(),
				bytes, nIndex);
		
		nIndex += DataTypeTranslate.writeInt2byte(getSegmentCount(), bytes,
				nIndex);
		addEndFlag(bytes, nIndex);
		return bytes;
	}
	
	public void setVersion(short nVersion){
		mnVersion = nVersion;
	}
	
	public void setSegmentCount(int nSegmentCount){
		mnSegmentCount = nSegmentCount;
	}
	public   short  getVersion(){
		return mnVersion;
	}
	
	public int  getSegmentCount(){
		return mnSegmentCount;
	}
	
	public void Debug(){
		 System.out.println(" \r\n <<<==== Print Header Info ====>>>");
		 StringBuilder szInfo = new StringBuilder();
		 szInfo.append(" START FLAG = ["+START_FLAG+"]" );
		 szInfo.append(" VERSION  = ["+getVersion()+"]" );
		 szInfo.append(" Original Data  Len= ["+getOriginalDataLength()+"]" );
		 szInfo.append(" Encryto  Data  Len= ["+getEncryptedDataLeng()+"]" );
		 szInfo.append(" Segment Count  = ["+getSegmentCount()+"]" );
		 szInfo.append(" END FLAG = ["+END_FLAG+"]" );
		 System.out.println(szInfo.toString());
		 System.out.println(" \r\n  <<<==== End Print Header Info ====>>>");
	}

	private short mnVersion;
	private int mnSegmentCount;

	private static final int INT_DATA_COUNT = 3;
	private static final int SHORT_DATA_COUNT = 1;

}
