import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

// Password, stored in a way suitable for iterating over.
class Password {
    static char _outchars[]="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWX0123456789,./<>?;':\"[]{}|-=_+`~!@#$%^&*()".toCharArray();
    // password space
    static char _passchars[]=_outchars;
    int[] _idata;// current password -- array of indices into _passchars
    char[] _data;// temporary output var -- chars of password 
    int _beg;// valid range in _idata,_data
    int _end;

    Password() {
	_idata=new int[16];
	_data=new char[16];
	_beg=16;
	_end=16;
    }

    // Get password bytes as a byte array
    byte[] getBytes() {
	byte[] ret = new byte[_end-_beg];
	int j=0;
	for (int i=_beg;i<_end;i++) {
	    ret[j++]=(byte)_passchars[_idata[i]];
	}
	return ret;
    }

    // Set current password as a string
    void setPass(String pass) {
	_end = _idata.length;
	_beg = _end - pass.length();
	for (int i=0; i<pass.length(); i++) {
	    _idata[_beg+i] = findChar(_passchars,pass.charAt(i));
	}
    }

    // Return current password as a string
    String getString() {
	for (int i=_beg;i<_end;i++) {
	    _data[i]=_passchars[_idata[i]];
	}
	return new String(_data,_beg,_end-_beg);
    }

    // Locate index of char C in array ARY, or return -1
    static int findChar(char[] ary, char c) {
	int ret=-1;
	for (int i=0; i<ary.length; i++) {
	    if (ary[i]==c) {
		ret=i;
		break;
	    }
	}
	return ret;
    }

    // Determine if byte sequence is likely valid
    // (80% of chars are contained in _outchars array)
    public boolean likelyStringQ(byte[] s) {
	int nlikely=0;
	for (int i=0; i<s.length; i++) {
	    nlikely += (findChar(_outchars,(char)s[i]) != -1 ? 1:0);
	}
	return (nlikely >= s.length*4/5);
    }

    // Pick next password lexicographically
    void next() {
	int i=-1;
	for (i=_end-1; i>=_beg; i--) {
	    if (_idata[i] < _passchars.length-1) {
		_idata[i]++;
		return;
	    } else {
		_idata[i]=0;
	    }
	}
	if (i==_beg-1) {// reached the _beginning of the string -- add new char
	    _idata[--_beg] = 0;
	}
    }

};
