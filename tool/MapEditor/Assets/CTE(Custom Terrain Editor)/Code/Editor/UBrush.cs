using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
using System;
namespace CTEUtil.CTEEditor {
    [Serializable]
    public class UBrush : IDisposable {
        [SerializeField]
        Texture2D m_Texture;

        public Texture2D texture {
            get {
                return m_Texture;
            }
            set {
                m_Texture = value;
            }
        }
        [SerializeField]
        Projector m_Projector;

        public Projector projector {
            get {
                return m_Projector;
            }
        }
        [SerializeField]
        Texture2D m_Preview;
        [SerializeField]
        int m_Size;
        [SerializeField]
        float[] m_Strength;

        public bool enabled {
            get {
                if (projector != null)
                    return projector.enabled;
                return false;
            }
            set {
                if (projector != null)
                    projector.enabled = value;
            }
        }

        void CreatePreviewBrush() {
            Type[] components = new Type[] { typeof(Projector) };
            GameObject obj2 = EditorUtility.CreateGameObjectWithHideFlags("CTETerrainPaintBrushPreview", HideFlags.HideAndDontSave, components);
            m_Projector = obj2.GetComponent(typeof(Projector)) as Projector;
            m_Projector.enabled = false;
            m_Projector.nearClipPlane = -5000f;
            m_Projector.farClipPlane = 5000f;
            m_Projector.orthographic = true;
            m_Projector.orthographicSize = 10f;
            m_Projector.transform.eulerAngles= new Vector3(90,90,0); 
#if UNITY_5
            Material material = Material.Instantiate<Material>(EditorGUIUtility.LoadRequired("SceneView/TerrainBrushMaterial.mat") as Material);
#else
			Material material = new Material ("Shader \"Hidden/Terrain Brush Preview\" {\nProperties {\n\t_MainTex (\"Main\", 2D) = \"gray\" { TexGen ObjectLinear }\n\t_CutoutTex (\"Cutout\", 2D) = \"black\" { TexGen ObjectLinear }\n}\nSubshader {\n\tZWrite Off\n\tOffset -1, -1\n\tFog { Mode Off }\n\tAlphaTest Greater 0\n\tColorMask RGB\n\tPass\n\t{\n\t\tBlend SrcAlpha OneMinusSrcAlpha\n\t\tSetTexture [_MainTex]\n\t\t{\n\t\t\tconstantColor (.2,.7,1,.5)\n\t\t\tcombine constant, texture * constant\n\t\t\tMatrix [_Projector]\n\t\t}\n\n\t\tSetTexture [_CutoutTex]\n\t\t{\n\t\t\tcombine previous, previous * texture\n\t\t\tMatrix [_Projector]\n\t\t}\n\t}\n}\n}");
#endif
			material.shader.hideFlags = HideFlags.HideAndDontSave;
            material.hideFlags = HideFlags.HideAndDontSave;
            material.SetTexture("_CutoutTex", AssetDatabase.LoadAssetAtPath("Assets/CTE(Custom Terrain Editor)/Image/Brushes/brush_cutout.png", typeof(Texture2D)) as Texture2D);
            m_Projector.material = material;
            m_Projector.enabled = false;
        }

        public void Dispose() {
			if (m_Projector != null) {
#if !UNITY_5
                UnityEngine.Object.DestroyImmediate(m_Projector.material.shader);
#endif
                UnityEngine.Object.DestroyImmediate(m_Projector.material);
                UnityEngine.Object.DestroyImmediate(m_Projector.gameObject);
                m_Projector = null; 
            }
            UnityEngine.Object.DestroyImmediate(m_Preview);
            m_Preview = null;
        }


        public float GetStrengthInt(int ix, int iy) {
            ix = Mathf.Clamp(ix, 0, m_Size - 1);
            iy = Mathf.Clamp(iy, 0, m_Size - 1);
            return m_Strength[(iy * m_Size) + ix];
        }

        public Color GetColor(int i) {
            i = Mathf.Clamp(i, 0, 3);
            if (i == 0)
                return (new Color(1, 0, 0, 0));
            if (i == 1)
                return (new Color(0, 1, 0, 0));
            if (i == 2)
                return (new Color(0, 0, 1, 0));
            if (i == 3)
                return (new Color(0, 0, 0, 1));
            return Color.black;
        }

        public static UBrush Load(Texture2D brushTex, UBrush brush) {
            if (brush == null)
                brush = new UBrush();
            brush.Load(brushTex, 64);
            return brush;

        }

        void Load(Texture2D brushTex, int size) {
            if (((m_Texture == brushTex) && (size == m_Size)) && (m_Strength != null)) {
                return;
            }
            if (brushTex != null) {
                float num = size;
                m_Size = size;
                m_Strength = new float[m_Size * m_Size];
                if (m_Size > 3) {
                    for (int j = 0; j < m_Size; j++) {
                        for (int k = 0; k < m_Size; k++) {
                            m_Strength[(j * m_Size) + k] = brushTex.GetPixelBilinear((k + 0.5f) / num, ((float)j) / num).a;
                        }
                    }
                }
                else {
                    for (int m = 0; m < m_Strength.Length; m++) {
                        m_Strength[m] = 1f;
                    }
                }
                UnityEngine.Object.DestroyImmediate(m_Preview);
                m_Preview = new Texture2D(m_Size, m_Size, TextureFormat.ARGB32, false);
                m_Preview.hideFlags = HideFlags.HideAndDontSave;
                m_Preview.wrapMode = TextureWrapMode.Repeat;
                m_Preview.filterMode = FilterMode.Point;
                Color[] colors = new Color[m_Size * m_Size];
                for (int i = 0; i < colors.Length; i++) {
                    colors[i] = new Color(1f, 1f, 1f, m_Strength[i]);
                }
                m_Preview.SetPixels(0, 0, m_Size, m_Size, colors, 0);
                m_Preview.Apply();
                if (m_Projector == null) {
                    CreatePreviewBrush();
                }
                m_Projector.material.mainTexture = m_Preview;
                m_Texture = brushTex;
                return;
            }
            m_Strength = new float[] { 1f };
            m_Size = 1;
        }

        public static void PreviewBrush(GameObject terrain, UBrush brush, float brushSize) {
            Vector3 hitPos = Vector3.zero;
            brush.enabled = true;
            float scaleSize = terrain.transform.lossyScale.x;

            Transform PPtransform = brush.projector.transform;
            bool flag = true;

            Vector2 newMousePostion = Event.current.mousePosition;
            newMousePostion.y = Screen.height - (Event.current.mousePosition.y + 35);
            Ray ray = SceneView.currentDrawingSceneView.camera.ScreenPointToRay(newMousePostion);
            
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit, 50000f)) {
                hitPos = hit.point;
                if (hit.collider.gameObject != terrain) {
                    flag = false;
                }
            }
            else {
                flag = false;
            }

            brush.enabled = flag;
            if (flag) {
                PPtransform.position = hitPos + Vector3.up * 100; //+ (normal * 100);
            }
            brush.projector.orthographicSize = brushSize * scaleSize;
        }

        public static void PreviewBrushOfNormal(GameObject terrain, UBrush brush, float brushSize) {
            Vector3 normal = Vector3.zero;
            Vector3 hitPos = Vector3.zero;
            brush.enabled = true;

            Transform PPtransform = brush.projector.transform;
            bool flag = true;

            Vector2 newMousePostion = Event.current.mousePosition;
            newMousePostion.y = Screen.height - (Event.current.mousePosition.y + 35);
            Ray ray = Camera.current.ScreenPointToRay(newMousePostion);
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit, 50000f)) {
                hitPos = hit.point;
                normal = hit.normal;
                if (hit.collider.gameObject != terrain) {
                    flag = false;
                }
            }
            else {
                flag = false;
            }

            brush.enabled = flag;
            if (flag) {
                PPtransform.position = hitPos + (normal * 100);
                PPtransform.rotation = Quaternion.LookRotation(normal);
            }
            brush.projector.orthographicSize = brushSize;
        }

        public static int IntFiled(int index) {
            GUILayout.Label("Brushes", EditorStyles.boldLabel);
            index = UEditorTools.SelectionGrid(index, UIcon.brushes, 0x20, "No brushes defined.");
            return index;
        }

        public static Texture2D Field(Texture2D brush) {
            GUILayout.Label("Brushes", EditorStyles.boldLabel);
            int selectedBrush = -1;
            if (brush != null) {
                for (int i = 0, max = UIcon.brushes.Length; i < max; i++) {
                    if (brush == UIcon.brushes[i]) {
                        selectedBrush = i;
                        break;
                    }
                }
            }
            selectedBrush = UEditorTools.SelectionGrid(selectedBrush, UIcon.brushes, 0x20, "No brushes defined.");
            if (selectedBrush == -1)
                return null;
            return UIcon.brushes[selectedBrush];
        }
    }
}