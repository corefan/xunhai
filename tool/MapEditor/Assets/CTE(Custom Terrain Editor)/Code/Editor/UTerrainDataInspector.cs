using UnityEngine;
using System.Collections;
using UnityEditor;
using CTEUtil.CTE;
namespace CTEUtil.CTEEditor{
    [CustomEditor(typeof(UTerrainData)), CanEditMultipleObjects]
    internal sealed class UTerrainDataInspector : Editor {
        public override void OnInspectorGUI() {
            //base.OnInspectorGUI();
        }
    }
}
