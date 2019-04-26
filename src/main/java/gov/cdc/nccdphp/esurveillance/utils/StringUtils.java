package gov.cdc.nccdphp.esurveillance.utils;

import java.util.*;


/**
 * <P>Utility class for String manipulation.
 * </P>
 * <p/>
 * <P>Date: Apr 13, 2004 - 10:28:15 AM</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public abstract class StringUtils {

	/** Constant <code>PAD_RIGHT='R'</code> */
	public static final char PAD_RIGHT = 'R';
	/** Constant <code>PAD_LEFT='L'</code> */
	public static final char PAD_LEFT = 'L';
	/**
	 * This method checks if a specific string is null or empty (ignoring white spaces).
	 *
	 * @param str the string we're checking.
	 * @return a boolean indicating whether the String is empty (true) or not (false).
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	/**
	 * This method is the exactly reverse of isEmpty() method. It checks whether a given string str
	 * is not null, and not empty, ignorign spaces.
	 *
	 * @param str The string we're checking.
	 * @return a boolean indicating whether the String is empty (false) or not (true).
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}
	/**
	 * <P>This method capitalizes a String by converting its first character to uppercase.</P>
	 * Ex.: thisisastring, ThisIsAString, THISISASTRING   will return: Thisisastring<BR>
	 *
	 * @param str The string to be modified
	 * @return a new string with first letter "Capital" and remaining lower letters.
	 */
	public static String capitalizeAndLower(String str) {
		if (isEmpty(str)) {
			return null;
		}
		String firstLetter = str.substring(0, 1);
		String rest = str.substring(1, str.length());
		String result = null;
		if (firstLetter != null) {
			result = firstLetter.toUpperCase();
			if (rest != null) {
				result += rest.toLowerCase();
			}
		}
		return result;
	}
	/**
	 * This method capitalizes a given string. And it may lower the case of the rest of the string.
	 * <p/>
	 * Ex.:
	 * thisisastring will return: Thisisastring<BR>
	 * ThisIsAString will return: ThisIsAString<BR>
	 * THISISASTRING will return: THISISASTRING<BR>
	 * tHISISASTRING will return: THISISASTRING<BR>
	 *
	 * @param str The String we want to capitalize.
	 * @return A New String with first letter upper case and the remaining untouched!
	 */
	public static String capitalize(String str) {
		if (isEmpty(str)) {
			return null;
		}
		String firstLetter = str.substring(0, 1);
		String rest = str.substring(1, str.length());
		String result = null;
		if (firstLetter != null) {
			result = firstLetter.toUpperCase();
			if (rest != null) {
				result += rest;
			}
		}
		return result;
	}
	/**
	 * <p>capitalizeEachWord.</p>
	 *
	 * @param str a {@link String} object.
	 * @return a {@link String} object.
	 */
	public static String capitalizeEachWord(String str) {
		if (isNotEmpty(str)) {
			String[] words = str.split(" ");
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < words.length; i++) {
				String aWord = words[i];
				if (isNotEmpty(aWord)) {
					result.append(capitalizeAndLower(aWord));
					if (i < words.length - 1) {
						result.append(" ");
					}
				}
			}
			return result.toString().trim();
		}
		return null;
	}
	/**
	 * Inserts a number of characters into a String.
	 *
	 * @param txt The String to be padded
	 * @param c   The character to be appended.
	 * @param len The number of characters to append
	 * @param LR  Flag indicating if it's to be appended on the left or
	 *            right of the string
	 * @return The new String modified with the characters appended.
	 */
	public static String pad(String txt, char c, int len, char LR) {
		String retval = txt;
		StringBuffer charsPad = new StringBuffer();
		for (int i = 0; i < len; i++) {
			charsPad.append(c);
		}
		if (LR == PAD_RIGHT) {
			retval = retval + charsPad.toString();
		} else {
			retval = charsPad.toString() + retval;
		}
		return retval;
	}
	/**
	 * Makes a string grow up to a number of Characters.
	 *
	 * @param txt The original String to be modified
	 * @param len Thed final size of the String.
	 * @param LR  Flag indicating where to add the extra spaces.
	 * @return The modified string with a specified size.
	 * @param c a char.
	 */
	public static String padGrow(String txt, char c, int len, char LR) {
		if (txt == null)
				txt = "";
		while (txt.length() < len) {
			if (LR == PAD_LEFT) {
				txt = c + txt;
			} else if (LR == PAD_RIGHT) {
				txt = txt + c;
			}
		}
		return txt;
	}
	/**
	 * Parses out the words that comprise a title-case string and returns them in an array.
	 * following the java standard, now words should start with an UpperCase. This method splits into
	 * multiple words everytime a uppercase is found!
	 *
	 * @param txt String to parseIsoDate
	 * @return A String array of the title-case words.
	 */
	public static String[] splitCapitalWords(String txt) {
		return txt.split("(?<=[a-z])(?=[A-Z])");
	}
	/**
	 * Identifies if the character passed is a Decimal digit or not.
	 *
	 * @param digit a char.
	 * @return true if the character passed is a decimal digit. False otherwise.
	 */
	public static boolean isDigit(char digit) {
		return (digit == '0') || (digit == '1') || (digit == '2') || (digit == '3') || (digit == '4') || (digit == '5') || (digit == '6') || (digit == '7') || (digit == '8') || (digit == '9');
	}
	/**
	 * Identifies if the character passed is a Hexadecimal digit or not.
	 *
	 * @param digit a char.
	 * @return tru if the character passes is a hexadecimal digit. False otherwise.
	 */
	public static boolean isHexaDigit(char digit) {
		return isDigit(digit) || (digit == 'A') || (digit == 'B') || (digit == 'C') || (digit == 'D') || (digit == 'E') || (digit == 'F');
	}

	/**
	 * <p>equalsNotEmpty.</p>
	 *
	 * @param str1 a {@link String} object.
	 * @param str2 a {@link String} object.
	 * @return a boolean.
	 */
	public static boolean equalsNotEmpty(String str1, String str2) {
		return (str1 != null) && (str2 != null) && (str1.equals(str2));
	}

	/**
	 * <p>isNumber.</p>
	 *
	 * @param userStr a {@link String} object.
	 * @return a boolean.
	 */
	public static boolean isNumber(String userStr) {
		if (isEmpty(userStr)) { //Null is treated as zero.
			return true;
		}
		try {
			new Double(userStr); ///Try to create a number out of the string.
			return true; //If no exception is thrown, return true.
		} catch (NumberFormatException e) {
			return false; //Otherwise return false.
		}
	}

	/**
	 * <p>stringIn.</p>
	 *
	 * @param aString a {@link String} object.
	 * @param validValues an array of {@link String} objects.
	 * @return a boolean.
	 */
	public static boolean stringIn(String aString, String[] validValues) {
		boolean found = false;
		for (int i = 0; i < validValues.length && !found; i++) {
			found = aString.equals(validValues[i]);
		}
		return found;
	}
	/**
	 * This method truncates, if necessary, the given string to obey the maximun
	 * length provided attaching '...' to indicate truncation of the string.
	 *
	 * @param str	   The String we need to verify truncation
	 * @param maxLength The maximum length (including ...) to be given to the string
	 * @return A String trucated up to maxLength.
	 */
	public static String truncateStr(String str, int maxLength) {
		if (str.length() <= maxLength) {
			return str;
		}
		if (maxLength < 3) {
			return null;
		}
		return str.substring(0, maxLength - 3).trim() + "...";
	}

	/**
	 * <p>This method is to trim a string.
	 *
	 * @param str String to be trimmed.
	 * @return the string trimmed.  If a string is null, it returns null
	 */
	public static String trim(String str) {
		if (str != null) {
			str = str.trim();
		}
		return str;
	}

	public static String trim(String str, String s) {
		String result = str.replaceAll("^" + s + "+", "");
		result = result.replaceAll(s + "+$", "");
		return result;
	}
	/**
	 * <p>isSame.</p>
	 *
	 * @param strPre1 a {@link String} object.
	 * @param strPre2 a {@link String} object.
	 * @return a boolean.
	 */
	public static boolean isSame(String strPre1, String strPre2) {
		boolean isSame;
		if (strPre1 == null) {
			isSame = StringUtils.isEmpty(strPre2);
		} else {
			if (strPre2 != null) {
				isSame = strPre1.trim().equalsIgnoreCase(strPre2.trim());
			} else {
				isSame = strPre1.trim().length() == 0;
			}
		}
		return isSame;
	}
	/**
	 * <p>concatWithDelimiter.</p>
	 *
	 * @param str an array of {@link String} objects.
	 * @param delimiter a {@link String} object.
	 * @param skipNulls a boolean.
	 * @return a {@link String} object.
	 */
	public static String concatWithDelimiter(String[] str, String delimiter, boolean skipNulls) {
		if (StringUtils.isEmpty(delimiter)) {
			delimiter = "^"; //default;
		}
		if (str != null && str.length > 0) {
			StringBuffer result = new StringBuffer();
			for (String aStr : str) {
				if (isNotEmpty(aStr) || !skipNulls) {
					result.append(aStr).append(delimiter);
				}
			}
			result.deleteCharAt(result.length() - delimiter.length());
			return result.toString();
		}
		return null;
	}

	/**
	 * <p>toArrayWithDelimiters.</p>
	 *
	 * @param str a {@link String} object.
	 * @param delimiter a {@link String} object.
	 * @return an array of {@link String} objects.
	 */
	@SuppressWarnings("unchecked")
	public static String[] toArrayWithDelimiters(String str, String delimiter) {
        Scanner scanner = new Scanner(str).useDelimiter(delimiter);
        if (isNotEmpty(str)) {
            List<String> result = new ArrayList<String>();
            while (scanner.hasNext()) {
                result.add(scanner.next().trim());
            }
            return result.toArray(new String[]{});
        }
        return null;
	}
	/**
	 * Replace oldstr with newstr within line
	 *
	 * @param line - Input line where oldstr needs to be replaced with newstr
	 * @param oldstr - source string
	 * @param newstr - target string
	 * @return Updated line
	 */
	public static String replace(String line, String oldstr, String newstr) {
	    int pos;
	    String buff = line;
	    String buff2;

	    pos = buff.indexOf(oldstr);

	    while (pos != -1) {
	        buff2 = buff.substring(0, pos) + newstr + buff.substring(pos + oldstr.length());
	        buff = buff2;
	        pos = buff.indexOf(oldstr);
	    }

	    return buff;
	}

    /**
     * <p>getPathWithEndSlash.</p>
     *
     * @param path a {@link String} object.
     * @return a {@link String} object.
     */
    public static String getPathWithEndSlash(String path) {
        if (StringUtils.isNotEmpty(path) && !path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path;
    }
}
