package com.cryto.mgr;

import java.util.HashMap;

import com.cryto.Defaultext.LimitBitNumberKeyGenerator;
import com.cryto.Defaultext.LimitLenNumberKeyGenerator;
import com.cryto.Defaultext.StringKeyGenerator;
import com.cryto.Interface.IKey;
import com.cryto.Interface.IKeyGenerator;

 public class KeyGeneratorMgr
 {
	 private KeyGeneratorMgr()
	 {
		 mMapKey = new HashMap<Integer, IKeyGenerator>();
	 }
	 public synchronized static void ReleaseInstance() {
			if (mIns != null)
				mIns = null;
		}
	 
	 public synchronized static KeyGeneratorMgr getInstance() {
			if (mIns == null){
				mIns =  new KeyGeneratorMgr();
				mIns.init();
		}
	 return mIns;
	 }
	 
	 public Object getKey(int nType, Object objParameter){
		 if(! getHashMap().containsKey(nType))
		 {
			 throw new RuntimeException("Key Type Error type:["+nType+"]");
		 }
		 else
		 {
			return  getHashMap().get(nType).getKey(nType, objParameter);
		 }
	 }
	 
	 private void init()
	 {
		 getHashMap().clear();
		 
		 IKeyGenerator insString =new StringKeyGenerator();
		 getHashMap().put(Integer.valueOf(insString.getID()), insString);
		 
		 IKeyGenerator insLimitLen =new LimitLenNumberKeyGenerator();
		 getHashMap().put(Integer.valueOf(insLimitLen.getID()), insLimitLen);
		 
		 IKeyGenerator insLimitBit =new LimitBitNumberKeyGenerator();
		 getHashMap().put(Integer.valueOf(insLimitBit.getID()), insLimitBit);
		 
		 ///external
	 }
	 
	 private HashMap<Integer, IKeyGenerator> getHashMap(){ return mMapKey;}
	 
	 private HashMap<Integer, IKeyGenerator> mMapKey;
	 private static KeyGeneratorMgr mIns;
 }