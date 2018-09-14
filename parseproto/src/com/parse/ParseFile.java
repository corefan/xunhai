package com.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ken
 *         2013-11-6
 *         解析文件
 */
public class ParseFile {

    private static Map<String, String> map;

    /** 华夏征途 */
    private static final String SKGAME = "skgame";
    /** 大唐诛仙 */
    private static final String XHGAME = "xhgame";

    //private static String TO_PATH = "";
    private static String TO_PATH = "E:/xunhai/server/XHGameServer/proto"; //proto生成指向路径  没设置默认当前路径

    static {
        map = new HashMap<String, String>();
        map.put(SKGAME, "华夏征途");
        map.put(XHGAME, "大唐诛仙");
    }

    public static void main(String[] args) {
        String type = parse(XHGAME);
        System.out.println(map.get(type) + " 文件生成完毕!");
    }

    /**
     * 解析文件
     */
    public static String parse(String type) {

        if (type == null) {
            type = XHGAME;
        }

        try {

            String binPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File binFile = new File(binPath);
            String projectPath = binFile.getParent();

            File file = new File(projectPath + "/src/file/" + type + "/proto");
            if (file != null && file.exists()) {

                //				// 先清空文件夹
                //				File cfile = new File(projectPath+"/src/file/"+type+"/proto_c");
                //				if (!cfile.exists()) {
                //					cfile.mkdir();
                //				}
                //				File[] cfiles = cfile.listFiles();
                //				for (int c=0;c<cfiles.length;c++) {
                //					cfiles[c].delete();
                //				}

                // 先清空文件夹
                if (TO_PATH == null || TO_PATH.length() <= 0) {
                    TO_PATH = projectPath + "/src/file/" + type + "/proto_s";
                }

                File sfile = new File(TO_PATH);
                if (!sfile.exists()) {
                    sfile.mkdir();
                }
                File[] sfiles = sfile.listFiles();
                for (int s = 0; s < sfiles.length; s++) {
                    sfiles[s].delete();
                }

                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (!".svn".equals(files[i].getName())) {

                        File copyFile = files[i];
                        FileInputStream fis = new FileInputStream(copyFile);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);

                        String fileName = copyFile.getName().substring(0, copyFile.getName().length() - 6);

                        //						String name_c = projectPath+"/src/file/"+type+"/proto_c/"+copyFile.getName();
                        String name_s = TO_PATH + "/" + copyFile.getName();
                        //						File file_c = new File(name_c);
                        //						if (file_c.exists()) {
                        //							file_c.delete();
                        //						}
                        //						file_c.createNewFile();
                        File file_s = new File(name_s);
                        if (file_s.exists()) {
                            file_s.delete();
                        }
                        file_s.createNewFile();

                        //						FileOutputStream fos_c = new FileOutputStream(file_c);
                        //						OutputStreamWriter osw_c = new OutputStreamWriter(fos_c);
                        //						BufferedWriter bw_c = new BufferedWriter(osw_c);

                        FileOutputStream fos_s = new FileOutputStream(file_s);
                        OutputStreamWriter osw_s = new OutputStreamWriter(fos_s);
                        BufferedWriter bw_s = new BufferedWriter(osw_s);

                        //						bw_c.write("package com.message;");
                        //						bw_c.write("\n");

                        String firstChar = String.valueOf(fileName.charAt(0)).toUpperCase();
                        String packageName = "\"com.message\"";
                        String className = "\"" + firstChar + fileName.substring(1) + "Proto" + "\"";

                        bw_s.write("option java_package=" + packageName + ";");
                        bw_s.write("\n");
                        bw_s.write("option java_outer_classname=" + className + ";");
                        bw_s.write("\n");

                        String line = null;
                        while ((line = br.readLine()) != null) {
                            //	bw_c.write(line);
                            bw_s.write(line);

                            //	bw_c.write("\n");
                            bw_s.write("\n");
                        }

                        // 关闭流
                        //						bw_c.close();
                        //						osw_c.close();
                        //						fos_c.close();

                        bw_s.close();
                        osw_s.close();
                        fos_s.close();

                        br.close();
                        isr.close();
                        fis.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }

}
