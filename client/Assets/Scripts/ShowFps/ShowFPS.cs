using UnityEngine;
using System.Collections;
using System.Text;

public class ShowFPS : MonoBehaviour {

    public GUIText m_Text = null;
    public float m_UpdateInterval = 1.0f;
    public int m_Frame = 0;
    public float m_LastInterval = 0f;
    
	void Start () {
        m_LastInterval = Time.realtimeSinceStartup;
        m_Frame = 0;
        if (!m_Text)
        {
            m_Text = gameObject.AddComponent<GUIText>();
            m_Text.fontSize = 18;
            m_Text.pixelOffset = new Vector2(5, 80);
        }
    }
	
	// Update is called once per frame
	void Update () {
        ++m_Frame;
        float timeNow = Time.realtimeSinceStartup;
        if (timeNow > m_LastInterval + m_UpdateInterval)
        {
            m_Text.text = "";
            StringBuilder sb = new StringBuilder();
            float fps = Mathf.Floor(m_Frame / (timeNow - m_LastInterval));
            float ms = 1000.0f / Mathf.Max(fps, 0.00001f);
            string str1 = ms.ToString("f1") + "ms " + fps.ToString("f0") + "FPS" + "\n";
            sb.Append(str1);
            string str2 = SystemInfo.processorType;
            sb.Append(str2);
            string str3 = "\n" + SystemInfo.systemMemorySize + "M";
            sb.Append(str3);
            string str4 = "\n" + SystemInfo.graphicsDeviceType;
            sb.Append(str4);
            m_Text.text = string.Format("{0}", sb);
            if (fps < 20)
            {
                m_Text.color = Color.red;
            }
            else
            {
                m_Text.color = Color.white;
            }
            m_Frame = 0;
            m_LastInterval = timeNow;
        }

	}
}
