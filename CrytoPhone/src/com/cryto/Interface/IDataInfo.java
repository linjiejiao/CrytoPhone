package com.cryto.Interface;

public interface IDataInfo {
	public int getType();

	public void setType(byte byteType);

	public byte[] getData();

	public Object getObject();

	public void setData(byte[] bytes);

	public void setDataRef(byte[] bytes);

	public int getLength();

	public int getExtLength();

	public byte[] data2Byte();

	public void Debug();

}
