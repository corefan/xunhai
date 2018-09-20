using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.IO;
using LuaInterface;
#if UNITY_5_3_OR_NEWER
using UnityEngine.SceneManagement;
#endif

namespace LuaFramework
{
    public class LoaderManager : Manager
    {
        string webUrl;// 远程资源目录
        string fileUrl;//= url + f + "?v=" + random; //接取服务器资源
        string dataPath;//本地数据目录
        string localfile;//= (dataPath + f).Trim();
        string random = DateTime.Now.ToString("yyyymmddhhmmss");
        Dictionary<string, bool> loadingMap;
        List<ResLoader> loaderQueue;
        Dictionary<string, LoaderInfo> assetBundleLoaderMap;
        void Awake()
        {
            loadingMap = new Dictionary<string, bool>();
            loaderQueue = new List<ResLoader>();
            assetBundleLoaderMap = new Dictionary<string, LoaderInfo>();
            webUrl = AppConst.WebUrl; // 远程资源目录
            dataPath = Util.DataPath; // 本地数据目录
        }
        /**
         * 加载除了GameManager 中已经加载剩下的场景资源，这里可以理解为动态地加载外部场景资源
         * sceneId 资源名字，注意这里的不是assetbundle资源名
         * finishCallback 加载结束回调，失败时，参数为null，成功时为场景ID
         * progressCallback 加载过程中回调，表作进度显示
         */
        public void LoadScene(string sceneId, Action<string> finishCallback, Action<float> progressCallback, bool useLocal=false)
        {
            LoadSceneResHandler(new ResLoader(sceneId, finishCallback, progressCallback), useLocal);
        }
        public void LoadScene(string sceneId, LuaFunction finishCallback, LuaFunction progressCallback, bool useLocal = false)
        {
            LoadSceneResHandler(new ResLoader(sceneId, finishCallback, progressCallback), useLocal);
        }
        private void LoadSceneResHandler(ResLoader resLoader, bool useLocal)
        {
            GC.Collect();
            if (AppConst.DebugMode || useLocal)
            {// 本地调试
                StartCoroutine(DoRenderScene(resLoader));
            }
            else
            {
#if SyncLocal //不进行更新
                ResManager.GetSceneAB(resLoader.sceneId);
			    StartCoroutine(DoRenderScene(resLoader));
#else
                LoadSceneRes(resLoader, (string v) =>
                {
                    if (!string.IsNullOrEmpty(v))
                    {
                        ResManager.GetSceneAB(resLoader.sceneId);
                        StartCoroutine(DoRenderScene(resLoader));
                    }
                    else
                    {
                        resLoader.CallFinish("");
                    }
                });
#endif
            }
        }

        private IEnumerator DoRenderScene(ResLoader resLoader)
        {
#if UNITY_5_3_OR_NEWER
            AsyncOperation op = SceneManager.LoadSceneAsync(resLoader.sceneId);
#else
		    AsyncOperation op = Application.LoadLevelAsync(resLoader.sceneId);
#endif
            
            while (!op.isDone)
            {
                resLoader.CallProgress(op.progress * 0.1f+0.8f);
                yield return null;
            }
            if (op.isDone)
            {
                resLoader.CallProgress(1.0f);
                yield return new WaitForEndOfFrame();
                resLoader.CallFinish(resLoader.sceneId);
                // Util.ClearMemory();
            }
        }
        /** 处理队列加载场景资源 */
        private void LoadSceneRes(ResLoader resLoader, Action<string> quequeHandler)
        {
            if (assetBundleLoaderMap.ContainsKey(resLoader.abName))
            {
                resLoader.quequeHandler = quequeHandler;//记录加载完成时的队列回调
                LoaderInfo loaderInfo = assetBundleLoaderMap[resLoader.abName];
                if (IsLoaded(resLoader.abName, loaderInfo.key))//已经加载
                {
                    resLoader.quequeHandler(resLoader.sceneId);
                    return;
                }
                if (loadingMap.ContainsKey(resLoader.abName))//已经加载中
                    return;
                loadingMap.Add(resLoader.abName, true);
                loaderQueue.Add(resLoader);
                StartCoroutine(_LoadRes());
            }
            else
            {
                quequeHandler(resLoader.sceneId);
            }
        }

        IEnumerator _LoadRes()
        {
            if (loaderQueue.Count == 0) yield break;
            ResLoader item = loaderQueue[0];
            loaderQueue.RemoveAt(0);

            string f = item.abName;
            if (!assetBundleLoaderMap.ContainsKey(f))
            {
                Debug.LogError("不存在资源:" + f);
                loadingMap.Remove(f);
                item.quequeHandler("");
                yield return StartCoroutine(_LoadRes());
                yield break;
            }
            fileUrl = webUrl + f + "?v=" + random; //接取服务器资源
            localfile = (dataPath + f).Trim();

            string path;
            string localKey;
            LoaderInfo loaderInfo = assetBundleLoaderMap[f];
            string remoteKey = loaderInfo.key;

            bool canUpdate = !File.Exists(localfile);// 是否需要更新
            path = Path.GetDirectoryName(localfile);
            if (!Directory.Exists(path))
                Directory.CreateDirectory(path);
            if (!canUpdate) //检查是否更新
            {
                localKey = PlayerPrefs.GetString(f);
                canUpdate = !remoteKey.Equals(localKey);
                if (canUpdate)
                    File.Delete(localfile);
            }
            if (canUpdate)
            { //更新或新增文件
                WWW www = new WWW(fileUrl);
                //yield return www;
                while (!www.isDone)
                {
                    item.CallProgress(www.progress * 0.7f);
                    yield return 1;
                }
                if (www.error != null)
                {
                    loadingMap.Remove(f);
                    item.quequeHandler("");
                    yield return StartCoroutine(_LoadRes());
                    www.Dispose();
                    yield break;
                }
                if (www.isDone)
                {
                    item.CallProgress(0.7f);
                    yield return 1;
                    File.WriteAllBytes(localfile, www.bytes);
                    yield return StartCoroutine(_LoadResManifest(item));
                    PlayerPrefs.SetString(f, remoteKey);
                    loadingMap.Remove(f);
                    item.quequeHandler(item.sceneId);
                    www.Dispose();
                }
            }
            yield return StartCoroutine(_LoadRes());
        }
        IEnumerator _LoadResManifest(ResLoader item)
        {
            string f = item.abName + ".manifest";
            fileUrl = webUrl + f + "?v=" + random; //接取服务器资源
            localfile = (dataPath + f).Trim();
            if (!assetBundleLoaderMap.ContainsKey(f))
            {
                Debug.LogError("不存在资源:" + f);
                item.quequeHandler("");
                yield break;
            }

            string path;
            string localKey;
            LoaderInfo loaderInfo = assetBundleLoaderMap[f];
            string remoteKey = loaderInfo.key;

            bool canUpdate = !File.Exists(localfile);// 是否需要更新
            path = Path.GetDirectoryName(localfile);
            if (!Directory.Exists(path))
                Directory.CreateDirectory(path);
            if (!canUpdate) //检查是否更新
            {
                localKey = PlayerPrefs.GetString(f);
                canUpdate = !remoteKey.Equals(localKey);
                if (canUpdate)
                    File.Delete(localfile);
            }
            if (canUpdate)
            { //更新或新增文件
                WWW www = new WWW(fileUrl);
                //yield return www;
                while (!www.isDone)
                {
                    item.CallProgress(www.progress * 0.1f + 0.7f);
                    yield return 1;
                }
                if (www.error != null)
                {
                    item.quequeHandler("");
                    www.Dispose();
                    yield break;
                }
                if (www.isDone)
                {
                    item.CallProgress(0.78f);
                    yield return new WaitForEndOfFrame();
                    File.WriteAllBytes(localfile, www.bytes);
                    PlayerPrefs.SetString(f, remoteKey);
                    www.Dispose();
                    yield break;
                }
            }
            yield return 0;
        }
        /**是否已经下载过的资源到本地*/
        public bool IsLoaded(string abName, string remoteKey)
        {
            string key = PlayerPrefs.GetString(abName, null);
            if (key == remoteKey)
                return true;
            return false;
        }
        /**记录动态加载的资源*/
        public void CacheAssetBundleLoaderData(string abName, string key)
        {
            if (abName != null && key != null)
            {
                // Trace.L("记录动态加载的资源: "+abName);
                if (assetBundleLoaderMap.ContainsKey(abName))
                {
                    //Trace.W("已经加载过的资源: "+abName);
                    assetBundleLoaderMap[abName].key = key;
                }
                else
                {
                    assetBundleLoaderMap.Add(abName, new LoaderInfo(abName, key));
                }
            }
        }

    /// <summary> 资源单元加载器  </summary>
        class ResLoader
        {
            const string sceneRoot = "scene/";//unity中 assets目录下场景资源的根目录
            internal string abName = null; //assetbundleName assetbundle资源名
            internal string sceneId = null; //场景资源id

            //C#
            internal Action<string> quequeHandler = null;
            internal Action<string> finishCallback = null;
            internal Action<float> progressCallback = null;
        	/// <summary> 资源单元加载器  </summary>
            internal ResLoader(string sceneId, Action<string> finish, Action<float> progress)
            {
                this.sceneId = sceneId;
                this.abName = sceneRoot + sceneId + AppConst.ExtName;
                this.finishCallback = finish;
                this.progressCallback = progress;
            }
			
            //Lua
            internal LuaFunction finishLuaCallback = null;
            internal LuaFunction progressLuaCallback = null;
			/// <summary> 资源单元加载器  </summary>
            internal ResLoader(string sceneId, LuaFunction finish, LuaFunction progress)
            {
                this.sceneId = sceneId;
                this.abName = sceneRoot + sceneId + AppConst.ExtName;
                this.finishLuaCallback = finish;
                this.progressLuaCallback = progress;
            }


            internal void CallFinish(string sceneId)
            {
                if (finishCallback != null)
                {
                    finishCallback(sceneId);
                    finishCallback = null;
                    progressCallback = null;
                }else if (finishLuaCallback != null){
                    finishLuaCallback.Call(sceneId);
                    finishLuaCallback.Dispose();
                    finishLuaCallback = null;
                }
                if (progressLuaCallback != null)
                {
                    progressLuaCallback.Dispose();
                    progressLuaCallback = null;
                }
                quequeHandler = null;
            }
            internal void CallProgress(float v)
            {
                if (progressCallback != null)
                    progressCallback(v);

                if (progressLuaCallback != null)
                    progressLuaCallback.Call(v);
            }
        }

    /// <summary> 加载资源单元信息  </summary>
        class LoaderInfo
        {
            internal string abName;
            internal string key;
        /// <summary> 加载资源单元信息  </summary>
            internal LoaderInfo(string name, string local)
            {
                this.abName = name;
                this.key = local;
            }
        }

    }
}