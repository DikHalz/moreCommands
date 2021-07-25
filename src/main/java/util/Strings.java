package util;


public class Strings extends arc.util.Strings {
	public static String fillAtLeft(String str, int newLenght) { return fillAtLeft(str, newLenght, " "); }
	public static String fillAtLeft(String str, int newLenght, String filler) {
		if (filler.length() >= str.length() + newLenght) return str;
	
		while (str.length() < newLenght) str = filler + str;
		return str;
	}
	
	public static String fillAtRight(String str, int newLenght) { return fillAtRight(str, newLenght, " "); }
	public static String fillAtRight(String str, int newLenght, String filler) {
		if (filler.length() >= str.length() + newLenght) return str;
		
		while (str.length() < newLenght) {
			str += filler;
		}
		
		return str;
	}
	
	public static String fillAtMiddle(String left, String right, int newLenght) { return fillAtMiddle(left, right, newLenght, " "); }
	public static String fillAtMiddle(String left, String right, int newLenght, String filler) {
		if (filler.length() >= left.length() + right.length() + newLenght) return left + right;

		while (left.length() + right.length() < newLenght) left += filler;
		return left + right;
	}
	
	public static String createSpaces(int length) {
    	String spaces = "";
    	for (int i=0; i<length; i++) spaces+=" ";
    	return spaces;
    }
	
	public static int bestLength(java.util.ArrayList<String> list) {
    	int best = 0;
    	
    	for (String i : list) 
    		if (i.length() > best) best = i.length();

    	return best;
    }
	
	public static String RGBString(String str, int hue) {
    	String out = "";
    	
    	for (char c : str.toCharArray()) {
    		if (hue < 360) hue+=10;
    		else hue = 0;
    		
    		out += "[#" + Integer.toHexString(java.awt.Color.getHSBColor(hue / 360f, 1f, 1f).getRGB()).substring(2) + "]" + c;
    	}
    	
        return out;
    }
	
	public static boolean canParseByteList(String[] list) {
		for (String str : list) 
			if (!canParseInt(str) || Integer.parseInt(str) > 255) 
				return false;
		
		return true;
	}
	
}