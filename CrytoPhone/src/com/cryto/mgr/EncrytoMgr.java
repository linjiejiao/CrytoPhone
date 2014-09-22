package com.cryto.mgr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.cryto.Default.EncrytoShift;
import com.cryto.Impl.BaseCrytoImpl;
import com.cryto.Interface.ICryto;
import com.cryto.util.Debug;
import com.customer.plus.Impl.PlusDetailImpl;
import com.customer.plus.Impl.XmlConfigImpl;
import com.xml.saxparse.Interface.INode;
import com.cryto.Default.Base64EncryToShift;

public class EncrytoMgr {

	private static EncrytoMgr mIns;
	private HashMap<Integer, ICryto> mCrytoList;
	public static String TAG="EncrytoMgr";
	private Context mCtx;

	private EncrytoMgr(){
		mCrytoList = new HashMap<Integer, ICryto>();
	}
	
	public synchronized static void ReleaseInstance() {
		if (mIns != null)
			mIns = null;
	}

	public synchronized static EncrytoMgr getInstance() {
		if (mIns == null) {
			mIns = new EncrytoMgr();
			mIns.Init();
		}
		return mIns;
	}

	public ICryto getAllEncrytoByID(int nID) {
		if (getAllEncrytoInstance().containsKey(nID)) {
			return getAllEncrytoInstance().get(nID);
		} else {
			return null;
		}
	}

	private void Init() {
		// System Default
		InitDefault();

		// Init From Config
		//InitExternal();
	}

	private void InitDefault() {
		// EncrytoShift Bit Shift
		EncrytoShift ins = new Base64EncryToShift();
		Log.v(TAG,"InitDefault ins:"+ins.getDesc()+" id:"+ins.getID()+" mCrytoList:"+mCrytoList);
		getAllEncrytoInstance().put(ins.getID(), ins);
	}

	public void InitExternal(Context ctx) {
		// from plus read all infomation
		// config /etc/encryto.xml;
		setContext(ctx);
		List<BaseCrytoImpl> baseCrytoImpl = parseConfig(XmlConfigImpl.CONFIG_FILE_PATH,XmlConfigImpl.PLUS_NODE_PATH);
		if(baseCrytoImpl !=null){
		    for(BaseCrytoImpl ins:baseCrytoImpl){
			    Log.v(TAG,"InitExternal ins:"+ins.getDesc()+" id:"+ins.getID());
			    if(mCrytoList != null){
			        mCrytoList.put(ins.getID(), ins);
			    }
		    }
		}
		
		
	}


	public int  getEncrytoCount(){
		return getAllEncrytoInstance().size();
	}
	
	private void setContext(Context ctx){
		mCtx =ctx;
	}
	
	public Context getContext(){
		return mCtx;
	}
	
	 public List<BaseCrytoImpl> parseConfig(String configFilePath,String plusNodePath){
		 List<BaseCrytoImpl> baseCrytoImpl = new ArrayList<BaseCrytoImpl>();
  	     if(configFilePath==null || configFilePath.length()<=0 || plusNodePath==null || plusNodePath.length()<=0){
  		     Log.v(TAG,"CONFIG_FILE_PATH or PLUS_NODE_PATH is null");
  		     return null;
  	     }

         File  file = new File(configFilePath);
		 if(!file.exists()){
		     Log.v(TAG,"CONFIG_FILE  is not exists");
		     return null;
		 }
			   
		 XmlConfigImpl ins = new XmlConfigImpl(configFilePath);
		 ins.Parser();
		 //ins.Debug();
		   
		  Object plusObject=null;
		  PlusDetailImpl plusDetailImpl=null;
		  BaseCrytoImpl obj=null;
		  List<INode> plusInstanceNode = ins.InitPlusNode(plusNodePath);
		  if(plusInstanceNode == null || plusInstanceNode.size()==0){
		      Log.v(TAG,"plusInstanceNode is null ");
			  return null;
		  }
		  
		  for(INode i:plusInstanceNode){
		      plusDetailImpl=new PlusDetailImpl(i);
		      plusObject=plusDetailImpl.InitPlusInstance();
		      if(plusObject==null){ 
		         Log.v(TAG,"plusObject is null ");
			     continue;
		      }else if(! (plusObject instanceof BaseCrytoImpl)){
		         Log.v(TAG,"plusObject is not BaseCrytoImpl ");
			     continue;
		      }else {
			     obj = (BaseCrytoImpl)(plusObject);
			     baseCrytoImpl.add(obj);
		      }
		  }
		  
		  return baseCrytoImpl;	  
	}  


	private HashMap<Integer, ICryto> getAllEncrytoInstance() {
		if(mCrytoList == null){
			mCrytoList = new HashMap<Integer, ICryto>();
		}
		return mCrytoList;
	}
}
