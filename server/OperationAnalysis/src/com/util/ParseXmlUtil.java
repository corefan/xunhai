package com.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.domain.ServerConf;

public final class ParseXmlUtil {

	//public static final String headXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static String parse(Map<String, List<ServerConf>> serverConfigList) {
		StringBuilder sb = new StringBuilder();
		//		sb.append(headXml);
		//		sb.append("\n");
		sb.append("<nodes label=\'管理后台\'>");
		sb.append("\n");
		Iterator<String> it = serverConfigList.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			sb.append("\t");
			sb.append("<node1 label=" + "'" + key + "'" + ">");
			sb.append("\n");
			List<ServerConf> serverConfList = serverConfigList.get(key);
			int size = serverConfList.size();
			for (int i = 0; i < size; i++) {
				sb.append("\t\t");
				sb.append("<node2 label=" + "'" + (i + 1) + "服" + "'" + " value=" + "'"
						+ serverConfList.get(i).getGameSite() + "'" + ">");
				sb.append("</node2>");
				if (i != size - 1)
					sb.append("\n");

			}
			sb.append("\n");
			sb.append("\t");
			sb.append("</node1>");
		}

		sb.append("\n");
		sb.append("</nodes>");
		return sb.toString();
	}

}
