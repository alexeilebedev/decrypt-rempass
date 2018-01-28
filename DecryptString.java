import java.io.IOException;
import java.lang.IllegalArgumentException;

// --------------------------------------------------------------------------------

class DecryptString {
    // Decrypt string based on currente password setting
    static String decryptString(Decrypt decrypt, String text) {
	byte[] output=null;
	try {
	    byte[] bytes = decrypt.undoBase64(text);
	    byte[] pass = decrypt._pass.getBytes();
	    output= decrypt.decrypt(bytes,pass);
	} catch (IllegalArgumentException e) {
	    // from Base64.decode
	}
	return output==null ? "" : new String(output);
    }

    // usage: DecryptJson <pass> <string>
    public static void main(String args[]) {
	Decrypt decrypt=new Decrypt();
	decrypt._pass.setPass(args[0]);
	System.out.println(decryptString(decrypt,args[1]));
    }
}
