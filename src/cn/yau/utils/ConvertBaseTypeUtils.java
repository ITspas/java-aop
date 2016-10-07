package cn.yau.utils;

public final class ConvertBaseTypeUtils {
	public static boolean getBoolean(String value) {
		return value.matches("true")?true:false;
	}

	public static byte getByte(String value) {
		throw new RuntimeException("亲,不知道怎么转");
	}

	public static char getChar(String value) {
		return value.toCharArray()[0];
	}

	public static double getDouble(String value) {
		double result = -1;
		if(value.matches("[0-9]*(.[0-9]*)?"))
			result = Double.parseDouble(value);
		return result;
	}

	public static float getFloat(String value) {
		float result = -1;
		if(value.matches("[0-9]*(.[0-9]*)?F?"))
			result = Float.parseFloat(value);
		return result;
	}

	public static int getInt(String value) {
		int reuslt =-1;
		if(value.matches("[0-9]*"))
			reuslt = Integer.parseInt(value);
		return reuslt;
	}

	public static long getLong(String value) {
		long reuslt =-1;
		if(value.matches("[0-9]*"))
			reuslt = Integer.parseInt(value);
		return reuslt;
	}

	public static short getShort(String value) {
		short reuslt =-1;
		if(value.matches("[0-9]*"))
			reuslt = Short.parseShort(value);
		return reuslt;
	}

	public static Object get(Object value) {
		return value;
	}
}
