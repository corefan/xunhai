public class ExtendHelper {
	
	public static string Bytes2HexString(byte[] bytes) {
        //byte[] bytes = Encoding.UTF8.GetBytes(str);
        string newstr = "";
        for (int i = 0; i < bytes.Length; i++) {
            //newstr += bytes[i].ToString("x2");
            newstr += Byte2HexChar(bytes[i]);
        }

        return newstr;
    }

    public static byte[] HexString2Bytes(string hex) {
        int len = hex.Length;
        DEBUG.Assert(len % 2 == 0, "hex string len must even number!");

        int arrLen = len / 2;
        byte[] bytes = new byte[arrLen];
        for (int i = 0; i < arrLen; i++) {
            byte ch = (byte)(HexChar2Byte(hex[i * 2]) * 16 + HexChar2Byte(hex[i * 2 + 1]));    //eg. E6 -> 15 * 16 + 6
            bytes[i] = ch;
        }

        return bytes;
        //string totalStr = Encoding.UTF8.GetString(bytes);
        //return totalStr;
    }

    static string Byte2HexChar(byte ch) {
        int ch1 = ch / 16;
        int ch2 = ch % 16;
        DEBUG.Assert(ch1 >= 0 && ch1 < 16, "ch1 is invalid");

        string table = "0123456789ABCDEF";
        string str = table[ch1].ToString() + table[ch2].ToString();
        return str;
    }

    static int HexChar2Byte(int ch) {

        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }

        if (ch >= 'A' && ch <= 'Z') {
            return ch - 'A' + 10;
        }

        if (ch >= 'a' && ch <= 'z') {
            return ch - 'a' + 10;
        }

        DEBUG.Assert(false, "ch is invalid");
        return -1;
    }
}