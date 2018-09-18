
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    internal class UTreeWizard : UTerrainWizard {
        public GameObject tree = null;
        public Texture2D billBoardTexture = null;
        public Color color = Color.white;
        public Color emission = Color.gray;
        [Range(0, 1)]
        public float cutoff = 0.1f;
        [HideInInspector]
        public int treeIndex = -1;
        public static UTreeWizard GetWizard(string title) {
            return GetWizard(title, string.Empty, string.Empty);
        }
        public static UTreeWizard GetWizard(string title, string createButtonName) {
            return GetWizard(title, createButtonName, string.Empty);
        }
        public static UTreeWizard GetWizard(string title, string createButtonName, string otherButtonName) {
            return ScriptableWizard.DisplayWizard<UTreeWizard>(title, createButtonName, otherButtonName);
        }
        void OnWizardCreate() {
            DoApply();
        }
        public override void OnWizardUpdate() {
            base.OnWizardUpdate();
            if (tree == null ) {
                base.errorString = "Please assign a tree";
                base.isValid = false;
                return;
            }
            if (tree.GetComponent<MeshFilter>() == null){
                base.errorString = "Please add component 'MeshFilter' on the tree";
                base.isValid = false;
            }
            if (tree.GetComponent<MeshRenderer>() == null) {
                base.errorString = "Please add component 'MeshRenderer' on the tree";
                base.isValid = false;
            }
            if (billBoardTexture == null){
                base.errorString = "If 'Billboard Texture' is null, it will be assigned 'AssetPreview.GetAssetPreview(Tree)'.";
            }
        }
        void DoApply() {
            if (m_Editor != null && terrain != null){
                if (treeIndex == -1)
                    terrain.data.treeData.Add(tree, billBoardTexture);
                else {
                    UTree ut = terrain.data.treeData.trees[treeIndex];
                    ut.prefab = tree;
                    ut.texture = billBoardTexture;
                    ut.color = color;
                    ut.cutoff = cutoff;
                    ut.emission = emission;
                }
            }
        }
        void OnWizardOtherButton() {
            DoApply();
        }
    }
}
