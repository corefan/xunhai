using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System;

public class NavExport : MonoBehaviour
{
    #region Public Attributes  
    public Vector3 leftUpStart = Vector3.zero;
    public int SampleHieght = 100;
    public float SampleRange = 0.5f;//扫瞄精确边界
    public float accuracy = 0.5f;//精密度（0.5米/格长）(4格/平米)
    /// <summary>
    /// 单位米
    /// </summary>
    public float width = 80;
    public float height = 60;

    /// <summary>
    /// 结果信息
    /// </summary>
    private string block;
    /// <summary>
    /// 指定到相应场景id中
    /// </summary>
    public int sceneId = 0;
    #endregion

    #region Unity Messages  

    void OnGUI()
    {
        int btnWidth = 360;
        int btnHeight = 35;
        if (GUI.Button(new Rect(Screen.width - btnWidth, Screen.height - btnHeight, btnWidth, btnHeight), "Inspector中改对应的 SceneId Json后 --> 导出"))
        {
            if (sceneId == 0 || sceneId == null)
            {
                throw new Exception("场景id没设置好！！！");
                return;
            }
            int w = Mathf.FloorToInt(width / accuracy);
            int h = Mathf.FloorToInt(height / accuracy);
            exportPoint(leftUpStart, w, h, accuracy);
        }
    }

    #endregion

    #region Public Methods  

    public void Exp()
    {
        int w = Mathf.FloorToInt(width / accuracy);
        int h = Mathf.FloorToInt(height / accuracy);
        exportPoint(leftUpStart, w, h, accuracy);
    }

    public void exportPoint(Vector3 startPos, int x, int y, float accuracy) //x,z
    {
        Debug.ClearDeveloperConsole();

        List<List<int>> row = new List<List<int>>();//行
        List<int> col;//列

        int[,] list = new int[x, y];
        for (int i = 0; i < y; i++)
        {
            col = new List<int>();
            for (int j = 0; j < x; ++j)
            {
                int status = 1;//不可通过点
                NavMeshHit hit;
                for (int k = -SampleHieght; k < SampleHieght; ++k)
                {
                    if (NavMesh.SamplePosition(startPos + new Vector3(j * accuracy, k, i * accuracy), out hit, SampleRange, 8))//沼泽地
                    {
                        status = 3;
                        break;
                    }else if (NavMesh.SamplePosition(startPos + new Vector3(j * accuracy, k, i * accuracy), out hit, SampleRange, 4))//跳跃区
                    {
                        status = 2;
                        break;
                    }
                    else if (NavMesh.SamplePosition(startPos + new Vector3(j * accuracy, k, i * accuracy), out hit, SampleRange, NavMesh.AllAreas))
                    {
                        status = 0;//可通过点
                        break;
                    }
                    
                }
                Color drawColor = Color.red;
                if (status == 0)
                    drawColor = Color.green;
                else if (status == 2)
                    drawColor = Color.yellow;
                else if (status == 3)
                    drawColor = Color.blue;
                Debug.DrawRay(startPos + new Vector3(j * accuracy, 0, i * accuracy), Vector3.up, drawColor, 60);
                col.Add(status);
                list[j, i] = status;
            }
            row.Add(col);
        }

        StringBuilder s = new StringBuilder();
        s.Append("\"block\":[");
        for (int i = 0; i < row.Count; i++)
        {
            col = row[i];
            s.Append("[");
            for (int j = 0; j < col.Count; j++)
            {
                if(j != col.Count-1)
                    s.Append(col[j]).Append(",");
                else
                    s.Append(col[j]);
            }
            if (i != row.Count - 1)
                s.Append("],");
            else
                s.Append("]");
        }
        s.Append("]");
        block = s.ToString();
        Debug.Log(block);

        /////////////////////全并之前布置好场景元素的地图json中去///////////////////////////////////////////////////
        if (!string.IsNullOrEmpty(block))
        {
            string cfgRoot = Application.dataPath + "/Res/Cfg";
            string fileContent = LoadFileContent(cfgRoot +"/"+ sceneId + ".json");
            
            if (!string.IsNullOrEmpty(fileContent))
            {
                string resultContent = "";
                if (fileContent.IndexOf(",\"block\":") != -1)
                {
                    resultContent = fileContent.Substring(0, fileContent.IndexOf(",\"block\":"));
                }
                else
                {
                    resultContent = fileContent.Substring(0, fileContent.LastIndexOf("}"));
                }
                resultContent = resultContent + "," + block + "}";
                Debug.Log(resultContent);

                //object[] objs = GameObject.FindSceneObjectsOfType(typeof(Transform));//清除摄像机
                //foreach (Transform item in objs)
                //{
                //    if (item.name.ToLower().IndexOf("camera") != -1)
                //    {
                //        GameObject.DestroyImmediate(item.gameObject);
                //    }
                //}

                CreateFile(cfgRoot, sceneId + ".json", resultContent);

            }
            else
            {
                Debug.LogError(string.Format("生成到场景id->{0}.json中的障碍，出错误！可能不存在！",sceneId));
            }
        }
    }

    public string LoadFileContent(string path)
    {
        StreamReader sr = null;
        try
        {
            sr = File.OpenText(path);
        }
        catch (Exception e)
        {
            return null;
        }
        string line;
        ArrayList arrlist = new ArrayList();
        line = sr.ReadToEnd();
        sr.Close();
        sr.Dispose();
        return line;
    }
    public void CreateFile(string path, string name, string info)
    {
        StreamWriter sw;
        FileInfo t = new FileInfo(path + "//" + name);
        if (!t.Exists)
        {
            sw = t.CreateText();
        }
        else
        {
            sw = t.CreateText();
        }
        sw.WriteLine(info);
        sw.Close();
        sw.Dispose();
    }

    /*
    public void exportPoint(Vector3 startPos, int x, int y, float accuracy) //x,z
    {
        StringBuilder str = new StringBuilder();
        int[,] list = new int[x, y];
        str.Append("startpos=").Append(startPos).Append("\r\n");
        str.Append("height=").Append(y).Append("\r\nwidth=").Append(x).Append("\r\naccuracy=").Append(accuracy).Append("\r\n");
        Debug.ClearDeveloperConsole();
        //for (int i = y - 1; i >= 0; --i)
        for (int i = 0; i < y; i++)
        {
            str.Append("{");
            for (int j = 0; j < x; ++j)
            {
                int res=0;
                NavMeshHit hit;
                for (int k = -SampleHieght; k < SampleHieght; ++k)
                {
                    if (NavMesh.SamplePosition(startPos + new Vector3(j * accuracy, k, i * accuracy), out hit, SampleRange, NavMesh.AllAreas))
                    {
                        res = 1;
                        break;
                    }
                }
                Debug.DrawRay(startPos + new Vector3(j * accuracy, 0, i * accuracy), Vector3.up, res == 1 ? Color.green : Color.red, 10000);
                str.Append(res).Append(",");
                
                list[j, i] = res;
            }
            str.Append("},\n");
        }
        Debug.Log(str.ToString());
    }*/
    #endregion

}

