package cn.ibrother.tools.data.filter;

/**
 * 符号过滤器,全角转半角
 * 
 * @author ken
 * @since 2011-10-15
 * @version 1.0
 */

public class SymbolDataFilter implements IDataFilter {

	@Override
	public String convert(String str) {
		String sResult = SBC2DBC(str);

		return sResult;
	}

	private String SBC2DBC(String str) {
		// 全角转半角
		if (str == null || str.length() == 0) {
			return str;
		}
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = charToDBC(chars[i]);
		}
		return new String(chars);
	}

	private char charToDBC(char ch) {
		if (ch == '\u3000') {
			return '\u0020';// 半角空格:
		} else if (ch > '\uFF00' && ch < '\uFF5F') {
			return (char) (ch - 65248);
		}
		return ch;
	}
}
