
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
namespace CTEUtil.CTEEditor {
    public static class UEditorTools {
        public static Vector2 GetSizeOfMesh(Mesh mesh, Transform transform) {
            Vector2 result = Vector2.zero;
            result.x = mesh.bounds.size.x;
            if (Vector3.Angle(transform.up, Vector3.up) > 0) {
                result.y = mesh.bounds.size.y;
            }
            else {
                result.y = mesh.bounds.size.z;
            }
            return result;
        }

        public static float RandomOfVaration (float f, int var){
            int newVar = Random.Range(-var, var);
            f *= (100 + newVar) * 0.01f;
            return f;
        }

        public static void CheckAndCreateFolder(string path) {
            path = path.Substring(6);
            path = Application.dataPath + path;
            if (!Directory.Exists(path)) {
                Directory.CreateDirectory(path);
            }
        }

        public static int SelectionGrid(int selected, Texture[] textures, int size, string emptyString) {
            GUIStyle style = new GUIStyle("GridList");
            GUIContent[] contents = new GUIContent[textures.Length];
            for (int i = 0; i < textures.Length; i++) {
                contents[i] = new GUIContent(textures[i]);
            }
            GUILayout.BeginHorizontal("OL Box", GUILayout.MinHeight(50f));
            GUILayout.Space(2);
            GUILayout.BeginVertical();
            GUILayout.Space(2);
            if (textures.Length != 0) {
                float numH = (Screen.width - 20) / size;
                int numV = (int)Mathf.Ceil(textures.Length / numH);
                Rect aspectRect = GUILayoutUtility.GetAspectRect(numH / numV);
                style.alignment = TextAnchor.MiddleCenter;
                selected = GUI.SelectionGrid(aspectRect, selected, contents, (Screen.width - 20) / size, style);
            }
            else {
                GUILayout.Label(emptyString, GUILayout.MinHeight(50f));
            }
            GUILayout.EndVertical();
            GUILayout.Space(2);
            GUILayout.EndHorizontal();
            return selected;
        }
        public static int SelectionGridImageAndText(int selected, Texture[] textures, int size, string emptyString) {
            GUIStyle style = new GUIStyle("GridListText");
            GUIContent[] contents = new GUIContent[textures.Length];
            for (int i = 0; i < textures.Length; i++) {
                contents[i] = new GUIContent(textures[i].name, textures[i]);
            }
            GUILayout.BeginHorizontal("OL Box", GUILayout.MinHeight(50f));
            GUILayout.Space(2);
            GUILayout.BeginVertical();
            GUILayout.Space(2);
            if (textures.Length != 0) {
                float numH = (Screen.width - 20) / size;
                int numV = (int)Mathf.Ceil(textures.Length / numH);
                Rect aspectRect = GUILayoutUtility.GetAspectRect(numH / numV);
                style.alignment = TextAnchor.MiddleCenter;
                selected = GUI.SelectionGrid(aspectRect, selected, contents, (Screen.width - 20) / size, style);
            }
            else {
                GUILayout.Label(emptyString, GUILayout.MinHeight(50f));
            }
            GUILayout.EndVertical();
            GUILayout.Space(2);
            GUILayout.EndHorizontal();
            return selected;
        }
    }  
}

