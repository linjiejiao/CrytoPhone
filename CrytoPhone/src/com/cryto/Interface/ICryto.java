package com.cryto.Interface;

public interface ICryto  {
	public int getVersion();
	public int getID();
	public String getDesc();
	public int[]  getKeyType();
	
	public byte[] decode(byte[] bytessrc, int nSrcLen,   int nStartPos, Object obj);
	public byte[] encode(byte[] bytessrc, int nSrcLen,   int nStartPos, Object obj);

	public byte[] decode(byte[] bytessrc, int nSrcStart, int nSrcLen,   byte[] bytedesc, int nDesLen, int nWritePos, Object obj);
	public byte[] encode(byte[] bytessrc, int nSrcStart, int nSrcLen,   byte[] bytedesc, int nDesLen, int nWritePos, Object obj);
}
