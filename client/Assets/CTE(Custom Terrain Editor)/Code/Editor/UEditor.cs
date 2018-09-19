using UnityEngine;
using System.Collections;
using CTEUtil.CTE;
using UnityEditor;
using System;
using System.Collections.Generic;
using System.Linq;
namespace CTEUtil.CTEEditor {
    [Serializable]
    internal abstract class UEditor {
        [SerializeField]
        protected Texture2D m_BrushTex;
        [SerializeField]
        protected UTerrainInspector m_Editor;
        [SerializeField]
        protected UBrush m_Brush;
        [SerializeField]
        protected bool m_ShowBrushUI = true;

        [SerializeField]
        protected string m_Title = "";
        [SerializeField]
        protected string m_Intro = "";

        protected abstract int selectedBrush {
            get;
            set;
        }

        public int instanceID{
            get {
                return m_Editor.terrain.GetInstanceID();
            }
        } 

        protected float m_BrushSize {
            get{
                return EditorPrefs.GetFloat("CTEBrushSize" + instanceID, 1);
            }
            set{
                EditorPrefs.SetFloat("CTEBrushSize" + instanceID, value);
            }
        }


        GUIContent m_BrushSizeContent = new GUIContent("Brush Size", "Size of brush used to paint");


        public UEditor(UTerrainInspector editor) {
            this.m_Editor = editor;
            
        }
        public void InspectorUI() {
            GUILayout.Space(4);
            EditorGUILayout.BeginVertical(GUILayout.MinHeight(1));
            Info();
            if (m_ShowBrushUI)
                m_BrushTex = BrushField(m_BrushTex);
            else
                m_BrushTex = UIcon.brushes[2];
            OtherUI();
            GUILayout.Label("Settings", EditorStyles.boldLabel);
            Settings();
            EditorGUILayout.EndVertical();
        }

        Texture2D BrushField(Texture2D brushTex) {
            if (brushTex != null) {
                for (int i = 0, max = UIcon.brushes.Length; i < max; i++) {
                    if (brushTex == UIcon.brushes[i]) {
                        selectedBrush = i;
                        break;
                    }
                }
            }
            selectedBrush = UBrush.IntFiled(selectedBrush);
            if (selectedBrush == -1)
                return null;
            return UIcon.brushes[selectedBrush];
        }

        protected virtual void OtherUI() {

        }
        protected virtual void Settings() {
            m_BrushSize = EditorGUILayout.Slider(m_BrushSizeContent, m_BrushSize, 0.1f, 50f);
        }
        public virtual void SceneUI(){
            Controls();
            if (m_BrushTex != null) {
                m_Brush = UBrush.Load(m_BrushTex, m_Brush);
                UBrush.PreviewBrush(m_Editor.terrain.gameObject, m_Brush, m_BrushSize);
            }
        }
        void Info() {
            if (string.IsNullOrEmpty(m_Intro) && string.IsNullOrEmpty(m_Title))
                return;
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1));
            GUILayout.Label(m_Title);
            GUILayout.Space(2);
            GUIStyle style = new GUIStyle(GUI.skin.textArea);
            style.fontSize = 9;
            style.normal.background = null;
            style.onNormal.background = null;
            GUILayout.Label(m_Intro, style);
            EditorGUILayout.EndVertical();
        }

        protected abstract void CtrlAndClick();
        protected abstract void CtrlAndHoldDown();
        protected abstract void ShiftAndHoldDown();

        void Controls() {
            if (m_BrushTex == null)
                return;
            if (Event.current.control) {
                EditorGUIUtility.AddCursorRect(new Rect(0, 0, Screen.width, Screen.height), MouseCursor.ArrowPlus);
            }
            if (Event.current.shift) {
                EditorGUIUtility.AddCursorRect(new Rect(0, 0, Screen.width, Screen.height), MouseCursor.ArrowMinus);
            }
            
            switch (Event.current.type) {
                case EventType.MouseDrag:
                    if (Event.current.button == 1) {
                        if (Event.current.shift) {
                            ShiftAndHoldDown();
                            Event.current.Use();
                        }
                        else if (Event.current.control) {
                            CtrlAndHoldDown();
                            Event.current.Use();
                        }
                    }
                    break;
                case EventType.MouseDown:
                    if ((Event.current.control || Event.current.command) && Event.current.button == 1) {
                        CtrlAndClick();
                        Event.current.Use();
                    }
                    break;
                case EventType.MouseMove:
                    break;
                case EventType.keyUp:
                    m_Editor.Save();
                    break;
                case EventType.Repaint:
                    if (m_Brush != null)
                        m_Brush.enabled = false;
                    break;
            }
        }

        public void Dispose() {
            if (m_Brush != null)
                m_Brush.Dispose();
        }
    }
}