import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;

// --------------------------------------------------------------------------------

// Decryption class for lame "remember passwords" databases
class Decrypt {
    // Obtained from decompiled code
    byte[] _salt = new byte[]{(byte)125, (byte)96, (byte)67, (byte)95, (byte)2, (byte)-23, (byte)-32, (byte)-82};
    MessageDigest _digest;
    SecretKeyFactory _keyfac;
    Password _pass = new Password();
    Cipher _cipher;
    PBEParameterSpec _pbespec;

    Decrypt() {
	try {
	    _digest = MessageDigest.getInstance("MD5");    
	    _keyfac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	    _pbespec = new PBEParameterSpec(this._salt,20);
	    _cipher = Cipher.getInstance("PBEWithMD5AndDES");
	} catch (NoSuchAlgorithmException e) {
	} catch (NoSuchPaddingException e) {
	}
    }

    // Decrypt BYTES according to PASSWORd and return result
    public byte[] decrypt(byte[] bytes, byte[] password) {
	byte[] ret=null;
	try {
	    SecretKey key = _keyfac.generateSecret(new PBEKeySpec(md5(password)));
	    _cipher.init(2, key, _pbespec);
	    ret = _cipher.doFinal(bytes);
	} catch (Exception e) {
	}
	return ret;
    }

    byte[] undoBase64(String text) {
	text=text.trim();
	return Base64.decode(text,0);
    }

    char toHexChar(int i) {
	return i>=10 ? (char)((int)'a' + i-10) : (char)((int)'0' + i);
    }

    // Perform MD5 on string S, return result
    public final char[] md5(byte[] s) {
	_digest.update(s);
	byte[] digest = _digest.digest();
	char[] out = new char[digest.length*2];
	int i=0;
	for (byte b : digest) {
	    out[i++] = toHexChar((b >> 4) & 0xf);
	    out[i++] = toHexChar((b     ) & 0xf);
	}
	return out;
    }
}
