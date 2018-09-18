using UnityEngine;
using System.Collections;
using System.IO;
using System.Collections.Generic;
using System;

//public class FileUtil : MonoBehaviour
//{
//    //不同平台下StreamingAssets的路径是不同的，这里需要注意一下。
//    public static readonly string PathURL =
//#if UNITY_ANDROID   //安卓
//    "jar:file://" + Application.dataPath + "!/assets/";
//#elif UNITY_IPHONE  //iPhone
//    Application.dataPath + "/Raw/";
//#elif UNITY_STANDALONE_WIN || UNITY_EDITOR  //windows平台和web平台
// "file://" + Application.dataPath + "/StreamingAssets/";
//#else
//    string.Empty;
//#endif
//}

public class FileTool
{
    //void Start()
    //{
    //    UnityEngine.Debug.Log("当前文件路径:" + Application.persistentDataPath);
    //    //删除文件
    //    DeleteFile(Application.persistentDataPath, "FileName.txt");
    //    //创建文件，共写入3次数据
    //    CreateFile(Application.persistentDataPath, "FileName.txt", "dingxiaowei");
    //    CreateFile(Application.persistentDataPath, "FileName.txt", "丁小未");
    //    //CreateFile(Application.persistentDataPath ,"Filename.assetbundle","丁小未");
    //    //得到文本中每一行的内容
    //    infoall = LoadFile(Application.persistentDataPath, "FileName.txt");
    //}

    //文本中每行的内容
    public static ArrayList infoall;
    /**
    * path：文件创建目录
    * name：文件的名称
    *  info：写入的内容
    */
    public static void CreateFile(string path, string name, string info)
    {
        //文件流信息
        StreamWriter sw;
        FileInfo t = new FileInfo(path + "//" + name);
        if (!t.Exists){
            //如果此文件不存在则创建
            sw = t.CreateText();
        }else{
            //如果此文件存在则打开
            //sw = t.AppendText();
            sw = t.CreateText();
        }
        //以行的形式写入信息
        sw.WriteLine(info);
        //关闭流
        sw.Close();
        //销毁流
        sw.Dispose();
    }



    /**
     * 读取文本文件
     * path：读取文件的路径
     * name：读取文件的名称
     */
    public static ArrayList LoadFile(string path, string name)
    {
        //使用流的形式读取
        StreamReader sr = null;
        try{
            sr = File.OpenText(path + "//" + name);
        }catch (Exception e){
            //路径与名称未找到文件则直接返回空
            return null;
        }
        string line;
        ArrayList arrlist = new ArrayList();
        while ((line = sr.ReadLine()) != null){
            //一行一行的读取
            //将每一行的内容存入数组链表容器中
            arrlist.Add(line);
        }
        //关闭流
        sr.Close();
        //销毁流
        sr.Dispose();
        //将数组链表容器返回
        return arrlist;
    }
    public static string LoadFileContent(string path, string name)
    {
        //使用流的形式读取
        StreamReader sr = null;
        try
        {
            sr = File.OpenText(path + "//" + name);
        }
        catch (Exception e)
        {
            //路径与名称未找到文件则直接返回空
            return null;
        }
        string line;
        ArrayList arrlist = new ArrayList();
        line = sr.ReadToEnd();
        //关闭流
        sr.Close();
        //销毁流
        sr.Dispose();
        //将数组链表容器返回
        return line;
    }
    public static string LoadFileContent(string path)
    {
        //使用流的形式读取
        StreamReader sr = null;
        try
        {
            sr = File.OpenText(path);
        }
        catch (Exception e)
        {
            //路径与名称未找到文件则直接返回空
            return null;
        }
        string line;
        ArrayList arrlist = new ArrayList();
        line = sr.ReadToEnd();
        //关闭流
        sr.Close();
        //销毁流
        sr.Dispose();
        //将数组链表容器返回
        return line;
    }

    /**
     * path：删除文件的路径
     * name：删除文件的名称
     */
    public static void DeleteFile(string path, string name)
    {
        File.Delete(path + "//" + name);
    }

}


