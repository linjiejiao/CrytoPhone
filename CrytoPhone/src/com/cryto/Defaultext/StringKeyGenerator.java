package com.cryto.Defaultext;

import android.util.Base64;

import com.cryto.Default.EncrytoShift;
import com.cryto.Default.RandomKeyGenerator;

public class StringKeyGenerator extends RandomKeyGenerator {

	public StringKeyGenerator()
	{
		setID(KEY_STRNG);
	}
	
	@Override
	public Object getKey(int nType, Object objParameter) {
		 
		// Create Number Data
		String szValue = String.valueOf(RandomData());
		byte[] bytessrc = szValue.getBytes();
		
		//Bit Encryto data
		EncrytoShift ins = new EncrytoShift();
		int nShiftBit = (int)RandomData() % 8;
		byte[] bytesOut = ins.decode(bytessrc, bytessrc.length, 0, Integer.valueOf(nShiftBit));
		
		// Base64 output
		return String.valueOf(Base64.encode(bytesOut, Base64.DEFAULT));
	}
}
