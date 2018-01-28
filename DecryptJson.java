import java.nio.file.Paths;
import java.nio.file.Files;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.lang.IllegalArgumentException;

// --------------------------------------------------------------------------------

class DecryptJson {
    Decrypt _decrypt = new Decrypt();

    // File to string
    static String readFile(String path) throws IOException {
	return new String(Files.readAllBytes(Paths.get(path)));
    }

    // Destructively decrypt attribute ATTR in object OBJ
    // (and write it back)
    void decryptAttr(JSONObject obj, String attr) {
	try {
	    if (obj != null) {
		String value = obj.getString(attr);
		if (value != null) {
		    String newstr = _decrypt.decryptString(value);
		    if (newstr != null) {
			obj.put(attr, newstr);
		    }
		}
	    }
	} catch (JSONException e) {
	}
    }

    // Decrypt "remember passwords" database file at path PATH
    // and print new result to screen
    void decryptJsonFile(String path) {
	try {
	    String text=readFile(path);
	    //System.out.println(text);
	    JSONObject obj = new JSONObject(text);
	    JSONArray array = obj.getJSONArray("list");
	    if (array != null) {
		for(int i = 0 ; i < array.length() ; i++){
		    JSONObject elem = array.getJSONObject(i);
		    decryptAttr(elem,"mTitle");
		    decryptAttr(elem,"mLogin");
		    decryptAttr(elem,"mPassword");
		    decryptAttr(elem,"mNotes");
		}
	    }
	    // print decrypted json
	    System.out.println(obj.toString(1));
	} catch (IOException e) {
	} catch (JSONException e) {
	}
    }

    // usage: DecryptJson <pass> <file>
    public static void main(String args[]) {
	DecryptJson decrypt=new DecryptJson();
	String pass=null;
	for (String s:args) {
	    if (pass == null) {
		pass=s;
		System.out.println(String.format("Using password %s",pass));
		decrypt._decrypt._pass.setPass(pass);
	    } else {
		decrypt.decryptJsonFile(s);
	    }
	}
    }
}
