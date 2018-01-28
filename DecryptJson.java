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

    // Decrypt attribute ATTR in object OBJ and return it
    String decryptAttr(JSONObject obj, String attr) {
	String ret=attr;
	try {
	    String value = obj.getString(attr);
	    if (value != null) {
		String newstr = _decrypt.decryptString(value);
		if (newstr != null) {
		    ret=newstr;
		}
	    }
	} catch (JSONException e) {
	}
	return ret;
    }

    // Decrypt "remember passwords" database file at path PATH
    // and print results to stdout
    void decryptJsonFile(String path) {
	try {
	    String text=readFile(path);
	    JSONObject obj = new JSONObject(text);
	    JSONArray array = obj.getJSONArray("list");
	    if (array != null) {
		for(int i = 0 ; i < array.length() ; i++){
		    JSONObject elem = array.getJSONObject(i);
		    String title=decryptAttr(elem,"mTitle");
		    String login=decryptAttr(elem,"mLogin");
		    String pass=decryptAttr(elem,"mPassword");
		    String notes=decryptAttr(elem,"mNotes");
		    System.out.println(String.format("decryptjson.entry  title:'%s'  login:'%s'  password:'%s'  notes:'%s'",title,login,pass,notes));
		}
	    }
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
