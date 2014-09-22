package com.cryto.util;

import java.util.HashMap;
import java.util.Set;

import com.cryto.Impl.DataIntegrityImpl;
import com.cryto.Impl.KeyImpl;
import com.cryto.Impl.SampleKey;
import com.cryto.Interface.ICryto;
import com.cryto.Interface.IDataInfo;
import com.cryto.Interface.IKey;
import com.cryto.mgr.EncrytoMgr;
import com.cryto.mgr.KeyGeneratorMgr;

/* *********************************************************************/
 /* |start_flag|function|key_data |method|src_data_length|encryto_data_length|data| end_flag|*/ 
 /* |1         | 1      | n       |2     | 4             | 4                 |  n |    1    |*/
 /* *********************************************************************/
public class Segment extends DataIntegrityImpl {
	public Segment()
	{
		mKeyIns = null;
	}
	public boolean checkDataIntegrity(byte[] bytes) {
		if (!super.checkDataIntegrity(bytes)) {
			System.out.println(" =====>>>>data NO Header And End Flag");
			return false;
		}
       
		int nIndex = 1;
		nIndex += processFun(bytes, nIndex);
		System.out.println("Segment checkDataIntegrity KeyLen  Pos =["+nIndex+"]");
		
 
		setEncrytoType(DataTypeTranslate.readShort(bytes, nIndex));
		System.out.println("Segment checkDataIntegrity Encryto Pos =["+nIndex+"]");
		
		nIndex += DataTypeTranslate.getShortBytesCount();
		setOriginalDataLength(DataTypeTranslate.readInt(bytes, nIndex));
		System.out.println("Segment checkDataIntegrity OriginalDataLen Pos =["+nIndex+"]");
		
		nIndex  += DataTypeTranslate.getIntBytesCount();
		setEncryptedDataLeng(DataTypeTranslate.readInt(bytes, nIndex));
		System.out.println("Segment checkDataIntegrity EncryptedDataLen Pos =["+nIndex+"]");
		
		nIndex  += DataTypeTranslate.getIntBytesCount();
		// encry data
		mbytesData = new byte[getEncryptedDataLeng()];
		System.out.println("Segment checkDataIntegrity EncryptedData Pos =["+nIndex+"]");
		System.arraycopy(bytes, nIndex, mbytesData, 0, getEncryptedDataLeng());
		System.out.println("Segment checkDataIntegrity  debug");
		// get decode data
		getDecodeStream();
		Debug();

		return true;
	}

	public byte[] data2Bytes() {
		// init env 
		Object obj = InitEncodeEnv();
		getEncodeStream(obj);
		
		// Other Flow is Ok
		//because function has include IKey£¬
		int nLen = getKeyIns().getLength() /*+  FUNCTION_DATA_COUNT*/ +getKeyIns().getExtLength()+ 
		DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT, SHORT_DATA_COUNT) + getFixHeaderLen() + getEncryptedDataLeng();
		System.out.println("Segment   ===>>All Data Len  ["+ nLen+"]");
		byte[] bytes = new byte[nLen];
		int nWritePos = 0;
		addStartFlag(bytes);
		++nWritePos;
		//fun
		bytes[nWritePos] = (byte)getFunction();
		++nWritePos;
	
	    // all function data to byte
		nWritePos += key2Bytes(bytes, nWritePos) ;
		
		System.out.println("Segment   ===>>Write function Type Pos ["+ nWritePos+"]");
	//	System.out.println("Segment data2Bytes ===>> Write Context ["+ String.valueOf(bytes)+"]");
		
	  // encryto Type
	  System.out.println("Segment   ===>>Write Encryto Type Pos ["+ nWritePos+"]");
	  nWritePos +=  DataTypeTranslate.writeShort2byte(getEncrytoType(), bytes, nWritePos);
	
		
		// original data len 
		System.out.println("Segment   ===>>Write Original  Data Pos ["+ nWritePos+"]");
		nWritePos += DataTypeTranslate.writeInt2byte(getOriginalDataLength(), bytes, 	nWritePos);
		
		
		//encryto  data len 
		System.out.println("Segment   ===>>Write EncryptedData  Len  Pos ["+ nWritePos+"]");
		nWritePos += DataTypeTranslate.writeInt2byte(getEncryptedDataLeng(), bytes,nWritePos);
	 
		System.out.println("Segment   ===>>Write EncryptedData  Pos ["+ nWritePos+"]");

		// encry to data
		if(getByte()!= null){
		   System.arraycopy(getByte(), 0, bytes, nWritePos, getByte().length);
		   nWritePos += getByte().length;
		}
		//end flag 
		addEndFlag(bytes, nWritePos);
		
		Debug.PrintBytes("Encode Segment data2Bytes\r\n", bytes);
		return bytes;
	}

	private void getEncodeStream(Object obj){
		// encode bytes
		setOriginalDataLength(getByte().length);
		byte[] outBytes =getCrytoIns().encode(getByte(), getOriginalDataLength(), 0, obj);
		Debug.PrintBytes("Segment Encode", this.getByte());
		setData(outBytes);
		setEncryptedDataLeng(outBytes.length);
	}
	
	private void getDecodeStream()
	{
		// encode bytes
		setEncryptedDataLeng(getByte().length);
		
		int nCount = getKeyIns().getAllKey().size();
		System.out.println(" Key count = ["+ nCount +"]");
		Set<Integer> setKey = getKeyIns().getAllKey().keySet();
		Object obj  = null;
		for(int nId : setKey){
			System.out.println(" Key ID = ["+ nId +"]");
		    obj = getKeyIns().getAllKey().get(nId).getObject();
		    break;
		}
		
		
		byte[] outBytes =getCrytoIns(getEncrytoType()).decode(getByte(), getByte().length, 0, obj);
		setData(outBytes);
		setOriginalDataLength(outBytes.length);
	}
	
	private int processFun(byte[] bytes, int nStart) {
		mbyteFun = bytes[nStart];
		int nIndex = nStart;
		switch (mbyteFun) {
		case FUN_KEY:
			if (initKey(bytes,  nIndex)) {
				return getKeyIns().getLength() + getKeyIns().getExtLength();
			} else {
				throw new RuntimeException(" Segment Read Key Error");
			}

		default:
			throw new RuntimeException("Segment Read Function System not Define ["
					+ mbyteFun + "]");
		}

	}

	private int key2Bytes(byte[] bytes, int nStart) {
		int nLen = 0;
		int nKeyLen = getKeyIns().getLength() - 5;
		// add key length
		System.out.println(" Segment  key2Bytes  key Total Len = ["+ nKeyLen +"]");
		DataTypeTranslate.writeInt2byte(nKeyLen, bytes, nStart);
		nLen +=DataTypeTranslate.getIntBytesCount();
		
		HashMap<Integer, IDataInfo> map = getKeyIns().getAllKey();
		Set<Integer> iobj = map.keySet();
		for (Integer i : iobj) {
			System.out.print("Segment  key2Bytes OutPut Key ["+i+"] value="  );
			map.get(i).Debug();
			System.out.println("\r\n");
			byte[] keybytes = map.get(i).data2Byte();
			System.arraycopy(keybytes, 0, bytes, nStart + nLen, keybytes.length);
			nLen = nLen + map.get(i).getLength() + map.get(i).getExtLength();
		}
		System.out.print("Segment  All Key Length =["+nLen+"]"  );
		return nLen;
	}

	public byte[] getByte() {
		return mbytesData;
	}
/*
	private int calcAllKeyLen() {
		HashMap<Integer, IDataInfo> map = getKeyIns().getAllKey();
		Set<Integer> iobj = map.keySet();
		int nLen = 0;

		for (Integer nID : iobj) {
			nLen = nLen + map.get(nID).getLength()
					+ map.get(nID).getExtLength();
		}
		return nLen;
	}
*/
	private ICryto getCrytoIns(int nType) {
		return EncrytoMgr.getInstance().getAllEncrytoByID(nType);
	}

	protected boolean initKey(byte[] bytes, int nStartIndex) {
		/* all data */
		if( mKeyIns != null)
			{
			  System.out.println(" Key Has Init ");
			}
		mKeyIns = new KeyImpl( );
		mKeyIns.checkDataIntegrity(bytes, nStartIndex);
		
		return true;
	}

	public void Debug() {
		System.out.println(" \r\n <<<==== Print Segment  Info ====>>>");
		StringBuilder szInfo = new StringBuilder();
		szInfo.append(" START FLAG = [" + START_FLAG + "]");
		szInfo.append(" Function  = [" + getFunction() + "]");
	//	szInfo.append(" Function  = [" +  + "]");
		// need function data
		szInfo.append(" EncrytoType  = [" + getEncrytoType() + "]");
		szInfo.append(" Original Data  Len= [" + getOriginalDataLength() + "]");
		szInfo.append(" Encryto  Data  Len= [" + getEncryptedDataLeng() + "]");
		// desc data
		 
		szInfo.append(" END FLAG = [" + END_FLAG + "]");
		System.out.println(szInfo.toString());
		System.out.println(" \r\n  <<<==== End Print Segment Info ====>>>");
	}

	private short getEncrytoType() {
		return mEncrytoType;
	}

	public void setEncrytoType(short nEncryto) {
		mEncrytoType = nEncryto;
	}

	public void setKeyIns(IKey key) {
		mKeyIns = key;
	}

	protected IKey getKeyIns() {
		if(mKeyIns == null)
			mKeyIns = new KeyImpl();
		
		return mKeyIns;
	}

	private byte getFunction() {
		return mbyteFun;
	}

	public void setFunction(byte nFun) {
		mbyteFun = nFun;
	}

	public void  setData(byte[] bytesData)
	{
		mbytesData = null;
		int nLen = bytesData.length;
		mbytesData = new byte[nLen];
		System.arraycopy(bytesData, 0, mbytesData, 0, nLen);
	}
	
	protected Object  InitEncodeEnv(){		
		// encry id 
		mCrytoIns = null ;
		int nCount = EncrytoMgr.getInstance().getEncrytoCount();
		int  nCrytoID = Math.round(System.currentTimeMillis()) % nCount;
		mCrytoIns = EncrytoMgr.getInstance().getAllEncrytoByID(nCrytoID);
		
		setFunction(FUN_KEY);
		// key type
		mKeyIns = null;
		 
		int[] nKeyType = mCrytoIns.getKeyType();
		int nKeyID = Math.round(System.currentTimeMillis()) % (nKeyType.length);
		
		//create key item 
		Object obj = KeyGeneratorMgr.getInstance().getKey(nKeyType[nKeyID], null);
		SampleKey keyInfo = new SampleKey();
		keyInfo.setData(ObjectTobyte(obj));
		keyInfo.setType((byte)nKeyType[nKeyID]);
		getKeyIns().getAllKey().put(keyInfo.getType(), keyInfo);
		return obj;
	}
	
	private byte[] ObjectTobyte(Object obj){
		byte[] bytes = null;
		if( obj instanceof Integer){
			bytes = new byte[DataTypeTranslate.getLongBytesCount()];
			int nValue = ((Integer)(obj)).intValue();
			DataTypeTranslate.writeInt2byte(nValue, bytes, 0);
		}else if( obj instanceof String){
			return ((String)obj).getBytes();
		}else if(obj instanceof byte[]){
			return (byte[])obj;
		}
		else{
			throw new RuntimeException("Type Error");
		}
		return bytes;
	}
	
	private ICryto getCrytoIns(){ return mCrytoIns;}
 
	
	private ICryto mCrytoIns;
	private IKey mKeyIns;
	private byte mbyteFun;
	private short mEncrytoType;
	private byte[] mbytesData;

	private static final int INT_DATA_COUNT = 2;
	private static final int SHORT_DATA_COUNT = 1;
	private static final int FUNCTION_DATA_COUNT = 1;
	private static final byte FUN_KEY = 0X01;
}
