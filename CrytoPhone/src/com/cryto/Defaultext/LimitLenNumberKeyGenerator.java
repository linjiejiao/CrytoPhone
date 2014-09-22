package com.cryto.Defaultext;

import com.cryto.Default.RandomKeyGenerator;

public class LimitLenNumberKeyGenerator extends RandomKeyGenerator{
	public LimitLenNumberKeyGenerator()
	{
		setID(KEY_NUMBER_LIMIT_LEN);
	}
	
	public Object getKey(int nType, Object objParameter){
		if(objParameter == null){
			objParameter = Integer.valueOf(DEFAULT_LEN);
		}
		int nBit = getIntFromObject(objParameter);
		long   nMax= (long)Math.pow(10,nBit);
		long nVal =  RandomData();
		nVal = nVal % nMax;
		return (int)nVal;
	}
	
	private static final int DEFAULT_LEN =8;
}
