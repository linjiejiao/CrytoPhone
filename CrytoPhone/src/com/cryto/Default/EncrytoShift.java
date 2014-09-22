package com.cryto.Default;
import com.cryto.Impl.BaseCrytoImpl;
import android.util.Base64;
import com.cryto.util.Debug;
import com.cryto.Interface.IKeyGenerator;

public class EncrytoShift extends BaseCrytoImpl {
	public EncrytoShift() {
		super(VERSION, ID, DESC);
	}
	protected EncrytoShift(int nVersion, int nID, String szDesc) {
		super(nVersion, nID, szDesc);
	}

	public int[] getKeyType() {
		// INT KEY
		return KEY_SET;
	}

	@Override
	public byte[] decode(byte[] bytessrc, int nSrcLen, int nStartPos, Object obj) {
		int nLen = nSrcLen - nStartPos;
		byte[] byteOut = new byte[nLen];
		return decode(bytessrc, nStartPos, nSrcLen, byteOut, nLen, 0, obj);
	}

	@Override
	public byte[] encode(byte[] bytessrc, int nSrcLen, int nStartPos, Object obj) {
		int nLen = nSrcLen - nStartPos;
		return encode(bytessrc, nStartPos, nSrcLen, null, -1, 0, obj);
	}

	@Override
	public byte[] decode(byte[] bytessrc, int nSrcStart, int nSrcLen,
			byte[] bytedesc, int nDesLen, int nWritePos, Object obj) {
		int nShiftBit = getShiftBit(obj);

		byte byteEncode = 0;
		byte byteCurByte = 0;
		byte byteMask = getMaskBit(nShiftBit);
		int nCurrentWritePos = nWritePos;
		for (int nIndex = nSrcStart; nIndex < nSrcLen; ++nIndex) {
			byteCurByte = bytessrc[nIndex];
			byte byteL = (byte) (byteCurByte >> nShiftBit);
			byte byteH = (byte) (byteCurByte << (8 - nShiftBit));

			byteEncode = (byte) (byteH + (byteL & byteMask));

			bytedesc[nCurrentWritePos] = byteEncode;
			++nCurrentWritePos;
		}
        Debug.PrintBytes("Base64  bytes ", bytedesc);

		return bytedesc;
	}

	@Override
	public byte[] encode(byte[] bytessrc, int nSrcStart, int nSrcLen,
			byte[] bytedesc, int nDesLen, int nWritePos, Object obj) {
		byte byteEncode = 0;
		byte byteCurByte = 0;
		int nShiftBit = getShiftBit(obj);
		byte byteMask = getMaskBit(nShiftBit);

		int nCurrentWritePos = nWritePos;
		for (int nIndex = nSrcStart; nIndex < nSrcLen; ++nIndex) {
			byteCurByte = bytessrc[nIndex];

			byte byteL = (byte) (byteCurByte << nShiftBit);

			byte byteH = (byte) (byteCurByte >> (8 - nShiftBit));

			byteEncode = (byte) ((byteH & byteMask) + byteL);

			bytedesc[nCurrentWritePos] = byteEncode;
			++nCurrentWritePos;

		}
		return bytedesc;
	}

	private int getShiftBit(Object obj) {
		if (obj == null) {
			throw new RuntimeException("obj Parameter Is NULL");
		} else if (!(obj instanceof Integer)) {
			throw new RuntimeException("obj Parameter Type Error");
		} else {
			return ((Integer) obj).intValue();
		}
	}

	private byte getMaskBit(int nShiftBit) {
		int nFrom = 8 - nShiftBit;
		return getMaskBit(nFrom, 7);
	}

	private byte getMaskBit(int nFrom, int nTo) {
		int nIntValue = -1;
		for (; nFrom <= nTo; ++nFrom) {
			int nBitValue = ~(1 << nFrom);
			nIntValue = nIntValue & nBitValue;
		}

		return (byte) nIntValue;
	}

	private static final int VERSION = 1;
	private static final int ID = 0;
	private static final String DESC = "EncrytoShift";
    private static final int [] KEY_SET={IKeyGenerator.KEY_NUMBER_LIMIT_BIT};

}
