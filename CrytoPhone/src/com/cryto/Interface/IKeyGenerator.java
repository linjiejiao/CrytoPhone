package com.cryto.Interface;

public interface IKeyGenerator {
	     public   byte       getID();
	     public   Object getKey(int nType, Object objParameter);
	     
	     public  static final byte  KEY_STRNG   = 0X01;
	     public  static final byte  KEY_NUMBER_LIMIT_LEN = 0X02;
	     public  static final byte  KEY_NUMBER_LIMIT_BIT = 0X03;
}
