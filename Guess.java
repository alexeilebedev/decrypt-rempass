import java.io.IOException;

// --------------------------------------------------------------------------------

// Guess password given a string encrypted with "remember passwords"
// Usage: java Guess "encrypted text" ["initial password"]
class Guess {
    long _starttime;
    Decrypt _decrypt;

    Guess() {
	_starttime = System.currentTimeMillis();
	_decrypt=new Decrypt();
    }

    // Find a password that maps to TEXT
    // Print results to stdout -- this is a human-facing tool
    // The function never exits.
    public void doGuess(String text) {
	Password _pass = _decrypt._pass;
	long i=0;
	byte[] bytes = _decrypt.undoBase64(text);
	do {
	    byte[] pass = _pass.getBytes();
	    byte[] output=_decrypt.decrypt(bytes,pass);
	    if (output!=null && _pass.likelyStringQ(output)) {
		System.out.println(String.format("guess.likely  input:'%s'  password:%s  output:%s",text,new String(pass),new String(output)));
	    }
	    _pass.next();
	    if (++i % 1000000 == 0) {
		long elapsed = System.currentTimeMillis()-_starttime;
		System.out.println(String.format("guess.progress  iter:%d  elapsed:%s  guess:%s",i,elapsed*1e-3,new String(pass)));
	    }
	} while (true);
    }

    //
    public static void main(String args[]) {
	Guess guess=new Guess();
	if (args.length>1) {
	    guess._decrypt._pass.setPass(args[1]);
	}
	guess.doGuess(args[0]);
    }
}
