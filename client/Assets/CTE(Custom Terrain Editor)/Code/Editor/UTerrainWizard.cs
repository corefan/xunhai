
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    internal class UTerrainWizard : ScriptableWizard {
        protected UTerrainInspector m_Editor;
        public void InitializeDefaults(UTerrainInspector editor) {
            m_Editor = editor;
            this.OnWizardUpdate();
        }
        public virtual void OnWizardUpdate() {
            base.isValid = true;
            base.errorString = string.Empty;
            if (m_Editor == null || m_Editor.terrain == null) {
                base.isValid = false;
                base.errorString = "Terrain does not exist";
            }
        }

        protected UTerrain terrain {
            get {
                return m_Editor.terrain;
            }
        }
        
    }
}
