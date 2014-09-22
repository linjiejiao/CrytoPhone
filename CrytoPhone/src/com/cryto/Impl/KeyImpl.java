package com.cryto.Impl;

import java.util.HashMap;
import java.util.Set;

import com.cryto.Interface.IDataInfo;
import com.cryto.Interface.IKey;
import com.cryto.util.DataTypeTranslate;
/************************************** */
/*|ID | length |data| */
/*|1  |4         | n | */
/************************************** */
public  class KeyImpl implements IKey {

	public KeyImpl()
	{
		  mKeyInfo = new HashMap<Integer, IDataInfo>();
	}
	
	public KeyImpl(byte nID)
	{
		this();
		mnID = nID;
		
	}
	public int getID() {
		return mnID;
	}

	public void setID(byte nID) {
		mnID = nID;
	}

 
	private int getTotalLen()
	{
		int nTotal = 0;
		Set<Integer> keyset = getAllKey().keySet();
		for(Integer i : keyset){
			IDataInfo ins = getAllKey().get(i);
			nTotal += ins.getLength() +ins.getExtLength();
		}
		return nTotal;
	}
	
	private byte[] getAllKeyData2Bytes(byte[]  bytes)
	{
		return getAllKeyData2Bytes(bytes, 0);
	}
	
	private byte[] getAllKeyData2Bytes(byte[]  bytes, int nPos)
	{
		int nIndex = nPos;
		
		Set<Integer> keyset = getAllKey().keySet();
		for(Integer i : keyset){
			IDataInfo ins = getAllKey().get(i);
			byte[] bytesOneKey = ins.data2Byte();
			System.arraycopy(bytesOneKey, 0, bytes, nIndex, bytesOneKey.length);
			nIndex += bytesOneKey.length;
		}
		
		return bytes;
	}
	
	
	public byte[] data2Bytes(){
		// calc all key len 
		int nIndex = 0;
		int nTotallen = getTotalLen()  ;
		byte[] bytes = new byte[nTotallen +DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT, SHORT_DATA_COUNT)];
		
		bytes[nIndex] = (byte)getID();
		++nIndex;
		
		nIndex+= DataTypeTranslate.writeInt2byte(nTotallen, bytes, nIndex);
		 
		
		return getAllKeyData2Bytes(bytes, nIndex);
	}
	
	public boolean checkDataIntegrity(byte[] bytes){
		return checkDataIntegrity(bytes, 0);
	}

	public int getExtLength() {
		return 	 DataTypeTranslate.getCalcByteCount(INT_DATA_COUNT, SHORT_DATA_COUNT) + 1;
	}
	public int		getLength()
	{
		int nLen = 0;
		Set<Integer> keyset = getAllKey().keySet();
		for(Integer i : keyset){
			IDataInfo ins = getAllKey().get(i);
			nLen = ins.getExtLength() + ins.getLength();
		}
		
		return nLen ;
	}

	public boolean checkDataIntegrity(byte[] bytes, int nIndex){
		int nReadPos = nIndex;
		int nStartReadData = 0;
		System.out.println("<<<====KeyImpl  Enter checkDataIntegrity  Read Key From Buffer Pos ["+nReadPos+"] ======>>>>");
		setID(bytes[nReadPos]);
		++nReadPos;
		
		 //read keylen 
	 	setKeyDataLen(DataTypeTranslate.readInt(bytes, nReadPos));
	 	nReadPos+= DataTypeTranslate.getIntBytesCount() ;
	 	nStartReadData = nReadPos;
		// read one Item Info
		System.out.println("<<<====KeyImpl Read Key From Buffer Pos ["+nReadPos+"]  KEY LEN=["+getKeyDataLen()+"]======>>>>");
		int nIndexKey = 0;
		while((nReadPos  - nStartReadData)< getKeyDataLen()){
			
			SampleKey ins = new SampleKey(bytes, nReadPos);
			nReadPos+= ins.getLength() + ins.getExtLength();
			int nLen = ins.getLength() + ins.getExtLength();
			System.out.println(" read Next Key ["+nIndexKey+"] len = ["+ nLen+"] nReadPos =["+(nReadPos - nStartReadData)+"]");
			++nIndexKey;
			Addkey(ins);
		}
		
		if((nReadPos  - nStartReadData ) != getKeyDataLen()){
			throw new RuntimeException(" Read Key Error Key Len["+getKeyDataLen()+"] Real Read bytes ["+ (nReadPos  - nStartReadData)+"]");
		}
		return true;
	}
	
	public byte[] getKeyByType(int nType) {
		if (getAllKey().containsKey(nType)) {
			return getAllKey().get(nType).getData();
		} else {
			throw new RuntimeException(" subclass Define Key Interface");
		}
	}

	protected boolean Addkey(IDataInfo info) {
		if (getAllKey().containsKey(info.getType())) {
			return false;
		} else {
			getAllKey().put(info.getType(), info);
			return true;
		}
	}

	public HashMap<Integer, IDataInfo> getAllKey() {
		return mKeyInfo;
	}
   
	private  void    setKeyDataLen(int nLen){
		mnLength = nLen;
	}
	
	public void Debug()
	{
		StringBuilder szInfo = new StringBuilder();
		szInfo.append(" KEY ID ["+ getID() +"]");
		szInfo.append(" KEY Len ["+ getKeyDataLen() +"]");
		szInfo.append(" KEY Count ["+ getAllKey().size() +"]");
		System.out.println(szInfo);
		System.out.println("Key Detail");
		
		int nIndex = 0;
		Set<Integer> keyset = getAllKey().keySet();
		for(Integer i : keyset){
			System.out.println(" Key Index =["+ nIndex +"]");
			 getAllKey().get(i).Debug();
		}
	}
	
	private int    getKeyDataLen(){ return mnLength;}
	private byte mnID;
	private int    mnLength;
	private HashMap<Integer, IDataInfo> mKeyInfo;
	
	private static final int INT_DATA_COUNT = 1;
	private static final int SHORT_DATA_COUNT =0 ;

}
