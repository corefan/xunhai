using UnityEngine;
public class DrawUtils {

    public static void DrawLine(Vector3 v1, Vector3 v2, Color color, float duration=0)
    {
        Debug.DrawLine(v1, v2, color, duration);
    }
    public static void DrawRay(Vector3 v1, Vector3 v2, Color color, float duration=0)
    {
        Debug.DrawRay(v1, v2, color, duration);
    }

    //int row = 0;
    //int col = 0;
    //float height=0;
    //public void DrawGrid(int row, int col, float height)
    //{
    //    this.row = row;
    //    this.col = col;
    //    this.height = height;
    //}

    //void Update()
    //{
    //    for (float i = 0; i <= row; i += 0.5f)
    //    {
    //        for (float j = 0; j <= col; j += 0.5f)
    //        {
    //            DrawRectLine(i, j, height);
    //        }
    //    }
    //}

    //void DrawLine1(Vector3 v1, Vector3 v2)
    //{
    //    Debug.DrawLine(v1, v2, Color.red);
    //}
    //void DrawLine2(Vector3 v1, Vector3 v2)
    //{
    //    Debug.DrawLine(v1, v2, Color.green);
    //}
    //void DrawLine3(Vector3 v1, Vector3 v2)
    //{
    //    Debug.DrawLine(v1, v2, Color.blue);
    //}

    //void DrawRectLine(float x, float y, float height, int state=0)
    //{
    //    Vector3 s = new Vector3(x, height, y);
    //    Vector3 e = new Vector3(x, height, y);

    //    DrawLine1(new Vector3(x + 1, height, y + 1), new Vector3(x, height, y + 1));
    //    DrawLine1(new Vector3(x, height, y), new Vector3(x + 1, height, y));
    //    DrawLine1(new Vector3(x, height, y), new Vector3(x, height, y + 1));
    //    DrawLine1(new Vector3(x + 1, height, y + 1), new Vector3(x + 1, height, y));
    //    if (state == 0)
    //    {
    //        DrawLine2(new Vector3(x, height, y), new Vector3(x + 1, height, y + 1));
    //        DrawLine2(new Vector3(x + 1, height, y), new Vector3(x, height, y + 1));
    //        DrawLine2(new Vector3(x, height, y + 1), new Vector3(x + 1, height, y));
    //    }
    //    else
    //    {
    //        DrawLine3(new Vector3(x, height, y), new Vector3(x + 1, height, y + 1));
    //        DrawLine3(new Vector3(x + 1, height, y), new Vector3(x, height, y + 1));
    //        DrawLine3(new Vector3(x, height, y + 1), new Vector3(x + 1, height, y));
    //    }
    //}
}
