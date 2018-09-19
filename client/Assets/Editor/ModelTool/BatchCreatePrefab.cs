using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

/// <summary>
/// 批量替换场景模型为Prefab
/// </summary>
public class BatchCreatePrefab : ScriptableObject
{
    private static List<Object> m_clearList = new List<Object>();

    [MenuItem("SceneTool/批量替换场景模型为Prefab")]
    static void DoIt()
    {
        m_clearList.Clear();
        var objects = Selection.gameObjects;
        foreach (var o in objects)
        {
            Process(o);
        }
        foreach (var o in m_clearList)
        {
            DestroyImmediate(o);
        }
    }

    private static void Process(GameObject o)
    {
        PrefabType prefabType = PrefabUtility.GetPrefabType(o);
        if (prefabType == PrefabType.ModelPrefabInstance)
        {
            var prefab = FindPrefab(o.name);
            if (prefab == null)
            {
                prefab = CreatePrefab(o);
            }
            if (prefab != null)
            {
                GameObject newobj = PrefabUtility.InstantiatePrefab(prefab) as GameObject;
                newobj.transform.parent = o.transform.parent;
                newobj.transform.localRotation = o.transform.localRotation;
                newobj.transform.localPosition = o.transform.localPosition;
                newobj.transform.localScale = o.transform.localScale;
                m_clearList.Add(o);
            }
            return;
        }

        for (int i = 0; i < o.transform.childCount; i++)
        {
            Process(o.transform.GetChild(i).gameObject);
        }
    }

    private static Object CreatePrefab(GameObject gameObject)
    {
        Object tempPrefab = PrefabUtility.CreatePrefab("Assets/SceneRaw/Prefabs/" + gameObject.name + ".prefab", gameObject);
        return tempPrefab;
    }

    private static Object FindPrefab(string prefabName)
    {
        string[] findAssets = AssetDatabase.FindAssets(prefabName);
        foreach (string findAsset in findAssets)
        {
            Object asset = AssetDatabase.LoadMainAssetAtPath(AssetDatabase.GUIDToAssetPath(findAsset));
            PrefabType assetType = PrefabUtility.GetPrefabType(asset);
            if (assetType == PrefabType.Prefab)
            {
                return asset;
            }
        }
        return null;
    }

    [MenuItem("SceneTool/处理场景LightMap问题")]
    static void CheckSceneLightMap()
    {
        GameObject root = GameObject.Find("Scene");
        CheckGameObjectLightMap(root);

        EditorUtility.DisplayDialog("处理完毕", "", "ok");
    }

    private static void CheckGameObjectLightMap(GameObject o)
    {
        if (o == null)
            return;

        PrefabType prefabType = PrefabUtility.GetPrefabType(o);
        if (prefabType == PrefabType.PrefabInstance)
        {
            Renderer r = o.GetComponent<Renderer>();
            Object findPrefab = PrefabUtility.GetPrefabParent(o);
            string path = AssetDatabase.GetAssetPath(findPrefab);
            r = (Renderer)AssetDatabase.LoadAssetAtPath(path, typeof(Renderer));
            if (r != null && r.lightmapIndex == -1)
            {
                GameObject go = (GameObject)PrefabUtility.InstantiatePrefab(findPrefab);
                r = go.GetComponent<Renderer>();
                r.lightmapIndex = 0;
                r.lightmapScaleOffset = Vector4.zero;
                PrefabUtility.ReplacePrefab(go, findPrefab);
                AssetDatabase.SaveAssets();
                AssetDatabase.Refresh(ImportAssetOptions.ForceUpdate);
                GameObject.DestroyImmediate(go);
            }
        }
        else
        {
            for (int i = 0; i < o.transform.childCount; i++)
            {
                CheckGameObjectLightMap(o.transform.GetChild(i).gameObject);
            }
        }
    }
}