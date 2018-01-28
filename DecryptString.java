import java.io.IOException;
import java.lang.IllegalArgumentException;

// --------------------------------------------------------------------------------

class DecryptString {
    // usage: DecryptJson <pass> <string>
    public static void main(String args[]) {
	Decrypt decrypt=new Decrypt();
	decrypt._pass.setPass(args[0]);
	String out = decrypt.decryptString(args[1]);
	System.out.println(out == null ? "" : out);
    }
}
