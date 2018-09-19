using UnityEngine;
using UnityEditor;
using System.Collections;
using CTEUtil.CTE;
using System.Linq;
namespace CTEUtil.CTEEditor {
    public static class UIcon {
        const string iconPath = "Assets/CTE(Custom Terrain Editor)/Image/Icons/";
        const string brushPath = "Assets/CTE(Custom Terrain Editor)/Image/Brushes/";
        static Texture2D m_Save;
        static Texture2D m_Height;
        static Texture2D m_Texture;
        static Texture2D m_Tree;
        static Texture2D m_Grass;
        static Texture2D m_About;
        static Texture2D m_Add;
        static Texture2D m_Del;
        static Texture2D m_Set;
        static Texture2D m_Logo;
        static Texture2D m_ScriptIcon;
        static Texture2D[] m_Brushes;

        public static Texture2D scriptIcon {
            get {
                if (m_ScriptIcon == null)
                    m_ScriptIcon = AssetDatabase.LoadAssetAtPath(iconPath + "ScriptIcon.png", typeof(Texture2D)) as Texture2D;
                return m_ScriptIcon;
            }
        }
        public static Texture2D[] brushes {
            get{
                if (m_Brushes == null) {
                    ArrayList list = new ArrayList();
                    int num = 1;
                    Texture2D texture = null;
                    do {
                        texture = AssetDatabase.LoadAssetAtPath(brushPath + "brush_" + num.ToString() + ".png", typeof(Texture2D)) as Texture2D;
                        if (texture != null) {
                            list.Add(texture);
                        }
                        num++;
                    } while (texture != null);
                    m_Brushes = list.ToArray(typeof(Texture2D)) as Texture2D[];
                }
                return m_Brushes;
            }
        }
        public static Texture2D save {
            get{
                if(m_Save == null)
                    m_Save = AssetDatabase.LoadAssetAtPath(iconPath + "Save.png", typeof(Texture2D)) as Texture2D;
                return m_Save;
            }
        }
        public static Texture2D height {
            get {
                if (m_Height == null)
                    m_Height = AssetDatabase.LoadAssetAtPath(iconPath + "Height.png", typeof(Texture2D)) as Texture2D;
                return m_Height;
            }
        }
        public static Texture2D texture {
            get {
                if (m_Texture == null)
                    m_Texture = AssetDatabase.LoadAssetAtPath(iconPath + "Texture.png", typeof(Texture2D)) as Texture2D;
                return m_Texture;
            }
        }
        public static Texture2D tree {
            get {
                if (m_Tree == null)
                    m_Tree = AssetDatabase.LoadAssetAtPath(iconPath + "Tree.png", typeof(Texture2D)) as Texture2D;
                return m_Tree;
            }
        }
        public static Texture2D grass {
            get {
                if (m_Grass == null)
                    m_Grass = AssetDatabase.LoadAssetAtPath(iconPath + "Grass.png", typeof(Texture2D)) as Texture2D;
                return m_Grass;
            }
        }
        public static Texture2D about {
            get {
                if (m_About == null)
                    m_About = AssetDatabase.LoadAssetAtPath(iconPath + "About.png", typeof(Texture2D)) as Texture2D;
                return m_About;
            }
        }
        public static Texture2D add {
            get {
                if (m_Add == null)
                    m_Add = AssetDatabase.LoadAssetAtPath(iconPath + "Add.png", typeof(Texture2D)) as Texture2D;
                return m_Add;
            }
        }
        public static Texture2D del {
            get {
                if (m_Del == null)
                    m_Del = AssetDatabase.LoadAssetAtPath(iconPath + "Del.png", typeof(Texture2D)) as Texture2D;
                return m_Del;
            }
        }
        public static Texture2D set {
            get {
                if (m_Set == null)
                    m_Set = AssetDatabase.LoadAssetAtPath(iconPath + "Set.png", typeof(Texture2D)) as Texture2D;
                return m_Set;
            }
        }
        public static Texture2D logo {
            get {
                if (m_Logo == null)
                    m_Logo = AssetDatabase.LoadAssetAtPath(iconPath + "Logo.png", typeof(Texture2D)) as Texture2D;
                return m_Logo;
            }
        }
    }
}
