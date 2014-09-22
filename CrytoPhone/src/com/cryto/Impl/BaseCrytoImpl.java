package com.cryto.Impl;

import com.cryto.Interface.ICryto;

public  abstract class BaseCrytoImpl  implements ICryto{

	protected BaseCrytoImpl(int nVersion,  int nID, String szDesc){
		mnID = nID;
		mnVersion = nVersion;
		mszDesc  = new String(szDesc);
	}

	public int getVersion() {
		// TODO Auto-generated method stub
		return mnVersion;
	}


	public int getID() {
		// TODO Auto-generated method stub
		return mnID;
	}


	public String getDesc() {
		// TODO Auto-generated method stub
		return mszDesc;
	}


  private int mnID;
  private int mnVersion;
  private String mszDesc;

}
