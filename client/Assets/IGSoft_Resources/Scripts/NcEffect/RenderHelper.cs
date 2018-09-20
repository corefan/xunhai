using System;
using System.Collections.Generic;
using System.Text;
using UnityEngine;

// 渲染系统静态公用方法 渲染辅助方法
public class RenderHelper
{
    public static void RefreshShader(ref GameObject obj)
    {
        //#if UNITY_EDITOR
        if (!Application.isEditor)
        {
            return;
        }
        Renderer[] pss = obj.GetComponentsInChildren<Renderer>(true);
        if (pss != null)
        {
            foreach (Renderer ps in pss)
            {
                if (ps.GetComponent<Renderer>().sharedMaterials != null)
                {
                    Material[] mtarr = ps.GetComponent<Renderer>().sharedMaterials;
                    if (mtarr != null)
                    {
                        for (int i = 0, len = mtarr.Length; i < len; i++)
                        {
                            if (mtarr[i] == null)
                            {
                                continue;
                            }

                            //if (mtarr[i].shader.name == "_Lucifer/CharacterCartoon-Transparent" || mtarr[i].shader.name == "_Lucifer/CharacterCartoon" || mtarr[i].shader.name == "_Lucifer/CharacterCartoon_outLine")
                            {
                                mtarr[i].shader = Shader.Find(mtarr[i].shader.name);
                            }
                        }
                    }
                }
            }
        }
        //#endif
    }
    public static void RefreshShader(GameObject obj)
    {
        //#if UNITY_EDITOR
        if (!Application.isEditor)
        {
            return;
        }
        Renderer[] pss = obj.GetComponentsInChildren<Renderer>(true);
        if (pss != null)
        {
            foreach (Renderer ps in pss)
            {
                if (ps.GetComponent<Renderer>().sharedMaterials != null)
                {
                    Material[] mtarr = ps.GetComponent<Renderer>().sharedMaterials;
                    if (mtarr != null)
                    {
                        for (int i = 0, len = mtarr.Length; i < len; i++)
                        {
                            if (mtarr[i] == null)
                            {
                                continue;
                            }

                            //if (mtarr[i].shader.name == "_Lucifer/CharacterCartoon-Transparent" || mtarr[i].shader.name == "_Lucifer/CharacterCartoon" || mtarr[i].shader.name == "_Lucifer/CharacterCartoon_outLine")
                            {
                                mtarr[i].shader = Shader.Find(mtarr[i].shader.name);
                            }
                        }
                    }
                }
            }
        }
        //#endif
    }
}
