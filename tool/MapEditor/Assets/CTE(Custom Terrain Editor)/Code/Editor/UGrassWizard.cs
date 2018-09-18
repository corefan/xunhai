
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    internal class UGrassWizard : UTerrainWizard {
        public Texture2D texture = null;
        public Color color = Color.white;
        public Color emission = Color.gray;
        [Range(0,1)]
        public float cutoff = 0.1f;
        [HideInInspector]
        public int grassIndex = -1;
        public static UGrassWizard GetWizard(string title) {
            return GetWizard(title, string.Empty, string.Empty);
        }
        public static UGrassWizard GetWizard(string title, string createButtonName) {
            return GetWizard(title, createButtonName, string.Empty);
        }
        public static UGrassWizard GetWizard(string title, string createButtonName, string otherButtonName) {
            return ScriptableWizard.DisplayWizard<UGrassWizard>(title, createButtonName, otherButtonName);
        }
        void OnWizardCreate() {
            DoApply();
        }
        public override void OnWizardUpdate() {
            base.OnWizardUpdate();
            if (texture == null) {
                base.errorString = "Please assign a tree";
                base.isValid = false;
            }
        }
        void DoApply() {
            if (m_Editor != null && terrain != null){
                if (grassIndex == -1)
                    terrain.data.grassData.Add(texture);
                else {
                    UGrass ug = terrain.data.grassData.grasses[grassIndex];
                    ug.texture = texture;
                    ug.color = color;
                    ug.emission = emission;
                    ug.cutoff = cutoff;
                }
            }
        }
        void OnWizardOtherButton() {
            DoApply();
        }
    }
}
