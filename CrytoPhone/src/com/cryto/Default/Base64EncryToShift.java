package com.cryto.Default;

import android.util.Base64;
import com.cryto.util.Debug;

public class Base64EncryToShift  extends EncrytoShift{

	@Override
	public byte[] decode(byte[] bytessrc, int nSrcLen, int  nStartPos, Object obj) {
		  // base decode 
	    int nLen = nSrcLen - nStartPos;
		byte[] byteBase64 = new byte[nLen];
		byte[] base64bytes = super.decode(bytessrc, nStartPos, nSrcLen, byteBase64, nLen, 0, obj);
		Debug.PrintBytes("Base64EncryToShift Decode ", base64bytes);
		byteBase64 = Base64.decode(base64bytes, Base64.NO_PADDING);
		Debug.PrintBytes("Base64EncryToShift Decode", byteBase64);
		return Base64.decode(byteBase64, Base64.NO_PADDING);
	}

	@Override
	public byte[] decode(byte[] bytessrc, int nSrcStart, int nSrcLen,
			byte[] bytedesc, int nDesLen, int nWritePos, Object obj) {
		byte[] byteout =  super.decode(bytessrc, nSrcStart, nSrcLen, bytedesc, nDesLen, nWritePos, obj);
		Debug.PrintBytes("Base64EncryToShift Decode ", byteout);
		 
		byte[] base64Bytes = Base64.decode(byteout, Base64.NO_PADDING);
		// base 64
		bytedesc = null;
		bytedesc = base64Bytes;
		return base64Bytes;
	}
	
	@Override
	public byte[] encode(byte[] bytessrc, int nSrcLen, int nStartPos, Object obj) {
		//int nLen = nSrcLen - nStartPos;		 
		byte[] base64Encode = Base64.encode(bytessrc, nStartPos, nSrcLen, Base64.NO_PADDING);
		byte[] bytesout = new byte[base64Encode.length];
		Debug.PrintBytes("Base64EncryToShift Encode ", base64Encode);
		return super.encode(base64Encode, 0, base64Encode.length, bytesout, base64Encode.length, 0, obj);
	}
	 
	@Override
	public byte[] encode(byte[] bytessrc, int nSrcStart, int nSrcLen,
			byte[] bytedesc, int nDesLen, int nWritePos, Object obj) {
		 
		byte[] base64Encode = Base64.encode(bytessrc, nSrcStart, nSrcLen, Base64.NO_PADDING); 
        Debug.PrintBytes("Base64EncryToShift Encode ", base64Encode);
		return super.encode(base64Encode, 0, base64Encode.length, bytedesc, nDesLen, nWritePos, obj);
	}
	
}
