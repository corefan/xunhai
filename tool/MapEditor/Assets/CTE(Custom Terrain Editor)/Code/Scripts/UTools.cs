#if UNITY_EDITOR
using UnityEditor;
using UnityEngine;
using System.Collections;
using System.IO;
using System.Linq;
namespace CTEUtil.CTE {
    public static class UTools{
        public static void CheckAndCreateFolder(string path) {
            path = path.Substring(6);
            path = Application.dataPath + path;
            if (!Directory.Exists(path)) {
                Directory.CreateDirectory(path);
            }
        }
        public static string[] GetAllFilesInPath(string directory, params string[] extensions) {
            directory = "Assets" + directory.Substring(Application.dataPath.Length);
            return AssetDatabase.FindAssets("t:Object", new string[] { directory }).Select(guid => AssetDatabase.GUIDToAssetPath(guid).Replace(directory + "/", "")).Where(p => extensions.Any(ex => p.EndsWith(ex))).ToArray();
        }
        //internal static UTerrain CopyTerrain(UTerrain terrain){
        //    if (asset == null){
        //        Debug.LogError("Asset is null!");
        //        return asset;
        //    }
        //    string assetPath = AssetDatabase.GetAssetPath(asset);
        //    string newPath = AssetDatabase.GenerateUniqueAssetPath(assetPath);
        //    Object prefab = AssetDatabase.LoadAssetAtPath(newPath,typeof(Object));
        //    if (prefab == null){
        //        prefab = PrefabUtility.CreateEmptyPrefab(newPath);
        //    }
        //    T newAsset = Object.Instantiate(asset) as T;
        //    AssetDatabase.AddObjectToAsset(newAsset, prefab);
        //    return newAsset;
        //}
    }
}
#endif