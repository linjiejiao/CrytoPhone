package com.cryto.Defaultext;

import com.cryto.Default.RandomKeyGenerator;

public class LimitBitNumberKeyGenerator  extends RandomKeyGenerator{
	public LimitBitNumberKeyGenerator()
	{
		setID(KEY_NUMBER_LIMIT_BIT);
	}
	public Object getKey(int nType, Object objParameter) {
		if(objParameter == null){
			objParameter = Integer.valueOf(DEFAULT_BIT);
		}
		int nMaxData = getIntFromObject(objParameter);
		long nVal =  RandomData();
		return (int) (nVal % nMaxData);
		
	}
	
	private static final int DEFAULT_BIT =8;
}
