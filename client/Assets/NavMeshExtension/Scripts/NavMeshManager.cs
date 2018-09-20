/*  This file is part of the "NavMesh Extension" project by Rebound Games.
 *  You are only allowed to use these resources if you've bought them directly or indirectly
 *  from Rebound Games. You shall not license, sublicense, sell, resell, transfer, assign,
 *  distribute or otherwise make available to any third party the Service or the Content. 
 */

using UnityEngine;
using System.Collections;
#if UNITY_EDITOR
using UnityEditor;
#endif

namespace NavMeshExtension
{
    /// <summary>
    /// NavMesh Manager class storing NavMesh properties.
    /// </summary>
    public class NavMeshManager : MonoBehaviour
    {
        /// <summary>
        /// Material for newly created meshes.
        /// </summary>
        public Material meshMaterial;

        /// <summary>
        /// Boolean used when toggling mesh renderers.
        /// </summary>
        [HideInInspector]
        public bool rendererToggle = true;


        //----------zwx-------------------------自动添加导出脚本------------------------------------
        #if UNITY_EDITOR
        void Awake()
        {
            EditorApplication.playmodeStateChanged = OnUnityPlayModeChanged;
        }

        private void OnUnityPlayModeChanged()
        {
            if (EditorApplication.isPlaying)
            {
                GameObject navMgr = GameObject.Find("NavMesh Manager");
                if (navMgr != null)
                {
                    NavExport navExport = navMgr.AddComponent<NavExport>();

                    //GameObject scene = GameObject.Find("scene");
                    //if (scene != null)
                    //scene.SetActive(false);
                }
            }
        }
        public static void Play()
        {
            EditorApplication.isPlaying = true;
        }

        public static void Pause()
        {
            EditorApplication.isPaused = true;
        }

        public static void Stop()
        {
            EditorApplication.isPlaying = false;
        }
        #endif
        //-----------------------------------------------------------------------------------
    }


}