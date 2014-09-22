package com.cryto.Default;

import com.cryto.Interface.IKeyGenerator;

public abstract class RandomKeyGenerator implements IKeyGenerator {
    protected long RandomData()
    {
    	return Math.round(System.currentTimeMillis());
    }

    protected int getIntFromObject(Object obj){
		if(obj == null){
			throw new RuntimeException("Input Parameter Error");
		}
		else if(!(obj instanceof Integer)){
			throw new RuntimeException("Input Parameter Not Integer ["+ obj.getClass().getName()+"]");
		}
		else
		{
			return ((Integer)obj).intValue();
		}
	}
    
    public  void      setID(byte nID){
    	mnID = nID;
    }
    public  byte       getID()
    {
    	return mnID;
    }
    
    private byte  mnID;
}
