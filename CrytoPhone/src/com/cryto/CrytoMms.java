package com.cryto;

import com.cryto.Default.EncrytoShift;
import com.cryto.Impl.BaseCrytoImpl;
import com.cryto.Impl.KeyImpl;
import com.cryto.Impl.SampleKey;
import com.cryto.Interface.ICryto;
import com.cryto.Interface.IDataInfo;
import com.cryto.Interface.IKeyGenerator;
import com.cryto.mgr.EncrytoMgr;
import com.cryto.mgr.KeyGeneratorMgr;
import com.cryto.util.DataTypeTranslate;
import com.cryto.util.Debug;
import com.cryto.util.Header;
import com.cryto.util.Segment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.cryto.Default.Base64EncryToShift;

public class CrytoMms extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.main);
        //TestEncrytoShiftMothed();
        // TestHeader();
        //TestSegment();
        //TestKey();
        
       // TestWR(128);
       //  TestWR(300);
       //  testIntToHeader();
       //  testDataTranslate(); 	         
        TestExternalXML();
    }

    private void TestAutoSegment()
    {
    	String szEncrytoData ="dddd";
    	Segment ins = new Segment();

    	ins.setData(szEncrytoData.getBytes());

    	System.out.println("<<<=== TestSegment encode ====>>> debug");
    
    	byte[] outbytes = ins.data2Bytes();
    	Debug.PrintBytes("TestSegment" , outbytes);
    	
    	Segment ins2 = new Segment();
    	ins2.checkDataIntegrity(outbytes);
    	System.out.println("<<<=== TestSegment decode ====>>> debug");
    	ins2.Debug();   	
    } 
 
    
    private void PrintData(short[] nData){
    	System.out.println("\r\n <<<=== Short Data ===>>>\r\n");
    	for(short nValue : nData){
    		System.out.print(" " + nValue);
    	}
    }
    
    private void PrintData(int[] nData){
    	System.out.println("\r\n <<<=== Int Data ===>>>\r\n");
    	for(int nValue : nData){
    		System.out.print(" " + nValue);
    	}
    }

    private void PrintData(long[] nData){
    	System.out.println("\r\n <<<=== Long Data ===>>>\r\n");
    	for(long nValue : nData){
    		System.out.print(" " + nValue);
    	}
    }

    private void testDataTranslate()
    {
    	byte[][] bytes = new byte[12][];
    	int nIndex = 0;
    	 short[] shortValue = {12,333, 15678, 999};
    	 PrintData( shortValue);
    	 for(short value : shortValue){
    		 bytes[nIndex++] =DataTypeTranslate.short2byte(value);
    	 }
    
    	 int[] intValue = {12,333, 45678, 9999999};
    	 PrintData( intValue);
    	 for(int value : intValue){
    		 bytes[nIndex++] =DataTypeTranslate.int2byte(value);
    	 }
    	 long[] longValue = {12,333, 45678, 9999999};
    	 PrintData( longValue);
    	 for(long value : longValue){
    		 bytes[nIndex++] =DataTypeTranslate.long2byte(value);
    	 }
    	 
    	 nIndex = 0;
    	 System.out.println("\r\n<<=== Read Data From Bytes \r\n");
    	 for(  nIndex = 0; nIndex < bytes.length; ++nIndex){
    		   int nValue  = nIndex/ 4;
    	 
    		 if(nIndex %4 == 0)
    			 System.out.println("\r\n CRLF\r\n");
    		 Debug.PrintBytes(" decode [" + nIndex+"]", bytes[nIndex]);
    		 switch(nValue){
    		 case 0:
    			 System.out.print(" "+DataTypeTranslate.byte2short(bytes[nIndex]) );
    			 break;
    			 
    		 case 1:
    			 System.out.print(" "+DataTypeTranslate.byte2int(bytes[nIndex]) );
    			 break;
    		 case 2:
    			 System.out.print(" "+DataTypeTranslate.byte2long(bytes[nIndex]) );
    			 break;
    		 }
    	 }
    }
 
    private void testIntToHeader(){
    	 
    	int [] nValues = {10,256,1024, 1124};
    	for( int nValue : nValues){
    		System.out.println(Integer.toHexString(nValue));
    	}
    	String szKey = "10a";
    	String szValue = "00000000";
    	StringBuilder builder = new StringBuilder(8);
    	System.out.println(" skey len ["+szKey.length()+"] builder len =["+builder.length()+"]");
    	builder.replace(8 - szKey.length() , 8, szKey);
    
    	System.out.println("output HexToString:["+builder.toString() +"] builder len =["+builder.length()+"]");
    	Debug.PrintBytes("Trans to Bytes", builder.toString().getBytes());
    	System.out.println(Integer.parseInt("000a", 16));
    	
    }
    
    
    public Context getContext(){
    	return getApplicationContext();
    }
    
    private void TestExternalXML(){
    	Log.v("fang","TestExternalXML....");
        Thread t1 = new Thread(new Runnable() {  
            @Override  
            public void run() {  
            	EncrytoMgr.getInstance().InitExternal(CrytoMms.this);  
                TestEncrytoMothed(1);
                TestEncrytoMothed(2);
            };  
        });  
        t1.start();
        

    }
    
    private void TestEncrytoMothed(int id){
        String szValue ="1"; 
        byte[] bytesrc = szValue.getBytes();
        ICryto ins =EncrytoMgr.getInstance().getAllEncrytoByID(id);
        Debug.PrintBytes("SrcString "+ ins.getDesc(), bytesrc);		
    	byte[] encodebyte = ins.encode(bytesrc, bytesrc.length, 0, 3);
        Debug.PrintBytes("EncString "+ ins.getDesc(), encodebyte);
    	System.out.println("\r\n");
    	
    	byte[] decodebyte = ins.decode(encodebyte, encodebyte.length, 0, 3);
      	Debug.PrintBytes("DecString "+ ins.getDesc(), decodebyte);
    }
    
    private void TestWR(int nValue){
   
    	byte[] bytes4  = new byte[4];
    	bytes4[0] = (byte) (nValue >>0);
    	bytes4[1] = (byte) (nValue >> 8);
    	bytes4[2] = (byte) (nValue >> 16);
    	bytes4[3] = (byte) (nValue >> 24);
    	System.out.println("\r\n===>>>Write signal byte ["+ nValue +"]  0x"+Integer.toHexString(nValue));
    	for(byte bt : bytes4){
    		System.out.print(" "+ bt);
    	}
    	
    	 int nOutValue = bytes4[3];
         nOutValue =  (nOutValue << 8) + bytes4[2];
         nOutValue =  (nOutValue << 8) + bytes4[1];
         nOutValue =  (nOutValue << 8) + bytes4[0];
         System.out.println("\r\n===>>>Read  signal byte ["+nOutValue +"]  0x"+Integer.toHexString(nOutValue));
     	 for(byte bt : bytes4){
     		System.out.print(" "+ bt);
     	}
     	
    }

    private void TestEncrytoShiftMothed()
    {
    	//String szValue ="中国";

    	String szValue = "子有" ;
    	EncrytoShift ins = new Base64EncryToShift();
    	 
    	Debug.PrintBytes("TestEncrytoShiftMothed Source bytes  "+ ins.getDesc(), szValue.getBytes());
    	byte[] encodebyte = ins.encode(szValue.getBytes(), szValue.getBytes().length, 0, 3);
    	Debug.PrintBytes("TestEncrytoShiftMothed After Encode shiftbit "+ ins.getDesc(), encodebyte);
    	System.out.println("\r\n length = ["+encodebyte.length+"]");
    	
    	  byte[] decodebyte = ins.decode(encodebyte, encodebyte.length, 0, 3);
    	  Debug.PrintBytes("TestEncrytoShiftMothed After Decode shiftbit "+ ins.getDesc(), decodebyte);

    	
    }
    
    private void TestHeader(){
    	Header header = new Header();
    	header.setEncryptedDataLeng(20);
    	header.setOriginalDataLength(23);
    	header.setVersion((short)1);
    	header.setSegmentCount(3);
    	header.Debug();
    	byte[] byteout = header.data2Bytes();
    	
    	Header header2 = new Header();
    	header2.checkDataIntegrity(byteout);
    	header2.Debug();  	
    }
    
    private KeyImpl TestKey(){
	   KeyImpl insKeyImpl = new KeyImpl();
    	
    	// one key
	    System.out.println("<<< ============TestKey   Encode   ============ >>>");
    	Object obj = KeyGeneratorMgr.getInstance().getKey(IKeyGenerator.KEY_NUMBER_LIMIT_BIT, Integer.valueOf(8));
    	IDataInfo datakey = new SampleKey();
    	byte[] bytesInt = new byte[DataTypeTranslate.getIntBytesCount()];
    	DataTypeTranslate.writeInt2byte(((Integer)obj).intValue(), bytesInt, 0);
    	datakey.setData(bytesInt);
    	datakey.setType(IKeyGenerator.KEY_NUMBER_LIMIT_BIT);
    	datakey.Debug();
    	System.out.println("key ext len =["+ datakey.getExtLength() +"] data len ["+ datakey.getLength()+"]");
    	insKeyImpl.getAllKey().put(datakey.getType(), datakey);
    	insKeyImpl.Debug();
    	/*
    	System.out.println("<<< ============TestKey  Decode   ============ >>>");
    	byte[] bytesOut = insKeyImpl.data2Bytes();
      	Debug.PrintBytes("TestSegment" , bytesOut);
    

    	insKeyImpl = new KeyImpl();
    	insKeyImpl.checkDataIntegrity(bytesOut, 0);
    	/*
    //	System.out.println("<<< ============  Decode   ============ >>>");
    	byte[] byteOut = insKeyImpl.data2Bytes();
    	insKeyImpl = new KeyImpl();
    	insKeyImpl.checkDataIntegrity(byteOut);
    //	insKeyImpl.Debug();
    	
    	
      	// one key 
    	obj = KeyGeneratorMgr.getInstance().getKey(IKeyGenerator.KEY_NUMBER_LIMIT_LEN, Integer.valueOf(8));
    	IDataInfo datakey2 = new SampleKey();
    	 bytes4 = new byte[4];
    	DataTypeTranslate.writeInt2byte(((Integer)obj).intValue(), bytes4, 0);
    	datakey2.setData(bytes4);
    	datakey2.setType(IKeyGenerator.KEY_NUMBER_LIMIT_BIT);
    	insKeyImpl.getAllKey().put(datakey2.getType(), datakey2);
    	
    	// String
    	String szInfo =(String) (KeyGeneratorMgr.getInstance().getKey(IKeyGenerator.KEY_STRNG, null));
    	IDataInfo datakey3 = new SampleKey();
    //	System.out.println("[ key is =["+ szInfo+"]");
    	 bytes4 = new byte[4];
    	DataTypeTranslate.writeInt2byte(((Integer)obj).intValue(), bytes4, 0);
    	datakey3.setData(szInfo.getBytes());
    	datakey3.setType(IKeyGenerator.KEY_STRNG);
    	insKeyImpl.getAllKey().put(datakey3.getType(), datakey3);
    	*/
     return insKeyImpl;
    }
 
    private void TestSegment(){
    	String szEncrytoData ="dddd";
    	Segment ins = new Segment();
    	ins.setEncryptedDataLeng(szEncrytoData.length());
    	ins.setOriginalDataLength(szEncrytoData.length());
    	ins.setData(szEncrytoData.getBytes());
    	ins.setEncrytoType((short)1);
    	ins.setFunction((byte)1);
   
    	//set Keyvalue ;
    	ins.setKeyIns(TestKey());
    	System.out.println("<<<=== TestSegment encode ====>>> debug");
    	ins.Debug();
    	byte[] outbytes = ins.data2Bytes();
    	Debug.PrintBytes("TestSegment" , outbytes);
    	
    	Segment ins2 = new Segment();
    	ins2.checkDataIntegrity(outbytes);
    	System.out.println("<<<=== TestSegment decode ====>>> debug");
    	ins2.Debug(); 	
    }
}