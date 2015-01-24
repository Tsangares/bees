package attributes;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by William on Dec-26-2014.
 */
/*All classes inheriting this class needs to follow a protocol:
Must overload & call derived function loadSave.
Must overload & call derived function getSave.
In getSave create StringBuffer or StringBuilder and append it to the output of the super function.
See class Item implementation of getSave for an example.
 */
/*time: 10:41*/
public abstract class SavableObject extends Group{
    protected static int idOut = 1;
    public int id = idOut++;
    public String name = "undefined";

    public void loadSave(String save){
        name = findTag("name", save);
    }
    public String getSave(){
        StringBuffer output = new StringBuffer();
        output.append(createTag("name", name));
        return output.toString();
    }
    protected String findTag(String tag, String source){
        String opener = "["+ name + "." +tag + "]";
        String closer = ":["+ name + "." + tag + "]";
        int start = source.indexOf(opener) + opener.length();
        int end = source.indexOf(closer);
        if(start != -1 && end != -1)
            return source.substring(start, end);
        System.out.print("\nNo data for key " + tag + " in saved memory. -findTag, " + this.getClass().toString());
        return "0";
    }
    protected String createTag(String tag, int value){
        return createTag(tag, String.valueOf(value));
    }
    protected String createTag(String tag, double value){
        return createTag(tag, String.valueOf(value));
    }
    protected String createTag(String tag, long value){
        return createTag(tag, String.valueOf(value));
    }
    protected String createTag(String tag, boolean value){
        return createTag(tag, String.valueOf(value));
    }
    protected String createTag(String tag, String value){
        String opener = "["+ name + "." + tag + "]";
        String closer = ":["+ name + "." + tag + "]";
        return opener+value+closer;
    }
}
