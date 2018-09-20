using UnityEngine;
using System.Collections;
using System.Text;

public class NavExport : MonoBehaviour
{
    #region Public Attributes  
    public Vector3 leftUpStart = Vector3.zero;
    public int SampleHieght = 100;
    public float SampleRange = 0.5f;
    public float accuracy = 1;
    public int height = 30;
    public int width = 30;

    #endregion

    #region Unity Messages  

    void OnGUI()
    {
        if (GUI.Button(new Rect(Screen.width - 120, Screen.height - 35, 120, 35),"Export"))
        {
            exportPoint(leftUpStart, height, width, accuracy);
        }
    }

    #endregion

    #region Public Methods  

    public void Exp()
    {
        exportPoint(leftUpStart, width, height, accuracy);
    }

    public void exportPoint(Vector3 startPos, int x, int y, float accuracy)
    {
        StringBuilder str = new StringBuilder();
        int[,] list = new int[x, y];
        str.Append("startpos=").Append(startPos).Append("\r\n");
        str.Append("height=").Append(y).Append("\r\nwidth=").Append(x).Append("\r\naccuracy=").Append(accuracy).Append("\r\n");
        for (int i = y - 1; i >= 0; --i)
        {
            str.Append("{");
            for (int j = 0; j < x; ++j)
            {
                int res = list[j, i];
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
            }
            str.Append("},\n");
        }
        Debug.Log(str.ToString());
    }
    #endregion

}

