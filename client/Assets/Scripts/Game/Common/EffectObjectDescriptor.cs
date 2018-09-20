using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

/// <summary>
/// 特效描述器
/// </summary>
public class EffectObjectDescriptor
{
    private GameObject _gameObject;
    //private Dictionary<Transform, TransformData> _transformMap = new Dictionary<Transform, TransformData>();
    public string effectName;
    public Vector3 position;
    public Vector3 scale;
    public Quaternion rotation;

    //public Transform[] transformList;
    public List<ParticleSystem> particleSystemList;
    public List<Animation> animationList;
    public List<Animator> animatorList;
    //public SpriteAnimation[] spriteAnimationList;
    public List<NcCurveAnimation> ncCurveAnimationList;
    public List<NcUvAnimation> ncUvAnimationList;
    public List<NcSpriteAnimation> ncSpriteAnimationList;
    /*        public List<NcDuplicator> NcDuplicatorList;*/
    public List<ParticleSystemRenderer> particleSystemRendererList;
    public List<SkinnedMeshRenderer> skinnedMeshRendererList;
    public List<MeshRenderer> meshRendererList;
    public List<NcEffectAniBehaviour> ncEffectAniBehaviourList;

    public List<NcRotation> ncRotationList;
    public List<Delay> delayList;
    public List<NcDuplicator> ncDuplicatorList;
    public List<NcTrailTexture> ncTrailTextureList;
    public List<GameObject> cloneObjectList = new List<GameObject>();
    //        public List<ReflectScaleX> reflectScaleXList = new List<ReflectScaleX>();

    public void Init(GameObject obj)
    {
        _gameObject = obj;
        position = _gameObject.transform.localPosition;
        scale = _gameObject.transform.localScale;
        rotation = _gameObject.transform.localRotation;
        //transformList = _gameObject.GetComponentsInChildren<Transform>(true).ToList();
        particleSystemList = _gameObject.GetComponentsInChildren<ParticleSystem>(true).ToList();
        animationList = _gameObject.GetComponentsInChildren<Animation>(true).ToList();
        animatorList = _gameObject.GetComponentsInChildren<Animator>(true).ToList();
        //spriteAnimationList = _gameObject.GetComponentsInChildren<SpriteAnimation>(true);
        ncCurveAnimationList = _gameObject.GetComponentsInChildren<NcCurveAnimation>(true).ToList();
        ncSpriteAnimationList = _gameObject.GetComponentsInChildren<NcSpriteAnimation>(true).ToList();
        ncUvAnimationList = _gameObject.GetComponentsInChildren<NcUvAnimation>(true).ToList();
        particleSystemRendererList = _gameObject.GetComponentsInChildren<ParticleSystemRenderer>(true).ToList();
        skinnedMeshRendererList = _gameObject.GetComponentsInChildren<SkinnedMeshRenderer>(true).ToList();
        meshRendererList = _gameObject.GetComponentsInChildren<MeshRenderer>(true).ToList();
        ncEffectAniBehaviourList = _gameObject.GetComponentsInChildren<NcEffectAniBehaviour>(true).ToList();
        delayList = _gameObject.GetComponentsInChildren<Delay>(true).ToList();

        ncRotationList = _gameObject.GetComponentsInChildren<NcRotation>(true).ToList();

        ncDuplicatorList = _gameObject.GetComponentsInChildren<NcDuplicator>(true).ToList();

        ncTrailTextureList = _gameObject.GetComponentsInChildren<NcTrailTexture>(true).ToList();
        //recordTransform();
        for (int i = 0; i < ncEffectAniBehaviourList.Count; i++)
        {
            NcEffectAniBehaviour ncEAB = ncEffectAniBehaviourList[i];
            ncEAB.isReuse = true;
            ncEAB.RecordStartState();
        }
    }
    public void Release()
    {
        _gameObject = null;
        particleSystemList.Clear();
        particleSystemList = null;
        animationList.Clear();
        animationList = null;
        animatorList.Clear();
        animatorList = null;
        ncCurveAnimationList.Clear();
        ncCurveAnimationList = null;
        ncSpriteAnimationList.Clear();
        ncSpriteAnimationList = null;
        ncUvAnimationList.Clear();
        ncUvAnimationList = null;
        particleSystemRendererList.Clear();
        particleSystemRendererList = null;
        skinnedMeshRendererList.Clear();
        skinnedMeshRendererList = null;
        meshRendererList.Clear();
        meshRendererList = null;
        ncEffectAniBehaviourList.Clear();
        ncEffectAniBehaviourList = null;
        delayList.Clear();
        delayList = null;
        ncRotationList.Clear();
        ncRotationList = null;
        ncDuplicatorList.Clear();
        ncDuplicatorList = null;
    }
    public void addDynamicEffectObject(GameObject go)
    {
        //             skinnedMeshRendererList.AddRange(go.GetComponentsInChildren<SkinnedMeshRenderer>(true).ToList());
        //             meshRendererList.AddRange(go.GetComponentsInChildren<MeshRenderer>(true).ToList());
        //             particleSystemRendererList.AddRange(go.GetComponentsInChildren<ParticleSystemRenderer>(true).ToList());
    }

    private bool _hasInit = false;
    //
    //       [ContextMenu("RecordState")]
    public void RecordState()
    {
        // Profiler.BeginSample("effect desc");
        if (_hasInit)
        {

            // Profiler.EndSample();
            return;
        }

        _hasInit = true;
        for (int i = 0; i < ncEffectAniBehaviourList.Count; i++)
        {
            NcEffectAniBehaviour ncEAB = ncEffectAniBehaviourList[i];
            ncEAB.isReuse = true;
            ncEAB.RecordStartState();
        }
        for (int i = 0; i < ncDuplicatorList.Count; i++)
        {
            NcDuplicator ncd = ncDuplicatorList[i];
            ncd.effectObjectDescriptor = this;
            ncd.isReuse = true;
            ncd.RecordStartState();
        }
        for (int i = 0; i < ncRotationList.Count; i++)
        {
            NcRotation ncR = ncRotationList[i];
            ncR.isReuse = true;
            ncR.RecordStartState();
        }
        for (int i = 0; i < ncRotationList.Count; i++)
        {
            NcRotation ncR = ncRotationList[i];
            ncR.isReuse = true;
            ncR.RecordStartState();
        }
        for (int i = 0; i < ncTrailTextureList.Count; i++)
        {
            NcTrailTexture ncT = ncTrailTextureList[i];
            ncT.isReuse = true;
            ncT.RecordStartState();
        }
        // Profiler.EndSample();
    }
    public void reStart()
    {
        // Profiler.BeginSample("effect restart");
        if (_gameObject == null) return;
        _gameObject.SetActive(true);

        for (int i = 0; i < delayList.Count; i++)
        {
            Delay delay = delayList[i];
            delay.Reset();
        }

        for (int i = 0; i < particleSystemList.Count; i++)
        {
            ParticleSystem ps = particleSystemList[i];
            //ps.time = 0;
            ps.playbackSpeed = 1;
            ps.Simulate(0, false, true);
            ps.Play();
        }

        for (int i = 0; i < ncEffectAniBehaviourList.Count; i++)
        {
            NcEffectAniBehaviour ncEAB = ncEffectAniBehaviourList[i];
            if (ncEAB == null) continue;
            ncEAB.ResetAnimation();
        }
        for (int i = 0; i < animatorList.Count; i++)
        {
            Animator animator = animatorList[i];
            animator.speed = 1;
        }
        for (int i = 0; i < animationList.Count; i++)
        {
            Animation animation = animationList[i];
            animation.Play();
        }
        for (int i = 0; i < ncDuplicatorList.Count; i++)
        {
            NcDuplicator ncDuplicator = ncDuplicatorList[i];
            ncDuplicator.ReStart();
        }

        for (int i = 0; i < ncTrailTextureList.Count; i++)
        {
            NcTrailTexture ncT = ncTrailTextureList[i];
            ncT.reset();
        }
        // Profiler.EndSample();
    }
    //镜面翻转.....
    //         public void setMirrorObject(bool bMirror=false) {
    //             for (int i = 0; i < particleSystemList.Count; i++)
    //             {
    //                 ParticleSystem ps = particleSystemList[i];
    //                 if (ps.GetComponent<MirrorEffect>() != null)
    //                 {
    //                     if (bMirror)
    //                     {
    //                         Vector3 scale = ps.transform.localScale;
    //                         scale.x = -Mathf.Abs(ps.transform.localScale.x);
    //                         ps.transform.localScale = scale;
    //                     }
    //                     else
    //                     {
    //                         Vector3 scale = ps.transform.localScale;
    //                         scale.x = Mathf.Abs(ps.transform.localScale.x);
    //                         ps.transform.localScale = scale;
    //                     }
    //                 }
    //             }
    //         }
    public void setDisable()
    {
        // Profiler.BeginSample("effect disable");
        //             if (ncDuplicator.Count > 0)
        //             {
        //                 particleSystemList = _gameObject.GetComponentsInChildren<ParticleSystem>(true).ToList();
        //                 animationList = _gameObject.GetComponentsInChildren<Animation>(true).ToList();
        //                 //spriteAnimationList = _gameObject.GetComponentsInChildren<SpriteAnimation>(true);
        //                 ncCurveAnimationList = _gameObject.GetComponentsInChildren<NcCurveAnimation>(true).ToList();
        //                 ncSpriteAnimationList = _gameObject.GetComponentsInChildren<NcSpriteAnimation>(true).ToList();
        //                 ncUvAnimationList = _gameObject.GetComponentsInChildren<NcUvAnimation>(true).ToList();
        //                 particleSystemRendererList = _gameObject.GetComponentsInChildren<ParticleSystemRenderer>(true).ToList();
        //                 skinnedMeshRendererList = _gameObject.GetComponentsInChildren<SkinnedMeshRenderer>(true).ToList();
        //                 meshRendererList = _gameObject.GetComponentsInChildren<MeshRenderer>(true).ToList();
        //                 ncEffectAniBehaviourList = _gameObject.GetComponentsInChildren<NcEffectAniBehaviour>(true).ToList();
        //                 delayList = _gameObject.GetComponentsInChildren<Delay>(true).ToList();
        //             }

        //             for (int i = 0; i < particleSystemList.Count; i++)
        //             {
        //                 ParticleSystem ps = particleSystemList[i];
        //                 //ps.Stop();
        //             }
        if ((_gameObject == null) || (animatorList == null) || (( (animatorList.Count > 0) && (animatorList[0] == null) )))
            return;
        for (int i = 0; i < animatorList.Count; i++)
        {
            Animator animator = animatorList[i];
            animator.speed = 0;
        }
        for (int i = 0; i < animationList.Count; i++)
        {
            Animation animation = animationList[i];
            animation.Stop();
        }

        for (int i = 0; i < ncEffectAniBehaviourList.Count; i++)
        {
            NcEffectAniBehaviour ncEAB = ncEffectAniBehaviourList[i];
            if (ncEAB == null)
            {
                ncEffectAniBehaviourList.RemoveAt(i);
                i--;
                continue;
            }
            ncEAB.PauseAnimation();
            ncEAB.ResetToStartState();
        }
        for (int i = 0; i < ncDuplicatorList.Count; i++)
        {
            NcDuplicator ncd = ncDuplicatorList[i];
            ncd.ResetToStartState();
        }

        for (int i = 0; i < ncRotationList.Count; i++)
        {
            NcRotation ncR = ncRotationList[i];
            ncR.ResetToStartState();
        }

        for (int i = 0; i < ncTrailTextureList.Count; i++)
        {
            NcTrailTexture ncT = ncTrailTextureList[i];
            ncT.ResetToStartState();
        }
        for (int i = 0; i < cloneObjectList.Count; i++)
        {
            GameObject.DestroyImmediate(cloneObjectList[i]);
        }
        cloneObjectList.Clear();
        _gameObject.transform.localPosition = position;
        _gameObject.transform.localScale = scale;
        _gameObject.transform.localRotation = rotation;
        //resetTransform();
        _gameObject.SetActive(false);
        // Profiler.EndSample();
    }

    //private void recordTransform()
    //{
    //    for (int i = 0; i < transformList.Length; i++)
    //    {
    //        Transform transform = transformList[i];
    //        _transformMap[transform] = new TransformData(transform.position, transform.localScale, transform.rotation);
    //    }
    //}

    //private void resetTransform()
    //{
    //    for (int i = 0; i < transformList.Length; i++)
    //    {
    //        Transform transform = transformList[i];
    //        TransformData td = _transformMap[transform];
    //        transform.position = td.position;
    //        transform.localScale = td.scale;
    //        transform.rotation = td.rotation;
    //    }
    //}
    //class TransformData
    //{
    //    public Vector3 position;
    //    public Vector3 scale;
    //    public Quaternion rotation;
    //    public TransformData(Vector3 position, Vector3 scale, Quaternion rotation)
    //    {
    //        this.position = position;
    //        this.scale = scale;
    //        this.rotation = rotation;
    //    }
    //}

    //                     for (int i = 0; i < particleSystemRendererList.Count; i++)
    //             {
    //                 if (particleSystemRendererList[i].renderMode == ParticleSystemRenderMode.Mesh)
    //                 {
    //                      Mesh mesh=_gameObject.Instantiate(particleSystemRendererList[i].mesh) as Mesh;
    //                      mesh.name = "abc__" + mesh.name;
    //                      particleSystemRendererList[i].mesh = mesh;
    //                     //GameEffectResourceManager.EffectResources[effectName].Load(texM.name) as Texture
    //   //                   particleSystemRendererList[i].renderMode = ParticleSystemRenderMode.HorizontalBillboard;
    //                     Material[] mtarr = particleSystemRendererList[i].materials;
    //                     for (int j = 0, len = mtarr.Length; j < len; j++)
    //                     {
    //                         Texture texM = mtarr[j].mainTexture;
    //                         Texture texN = GameEffectResourceManager.EffectResources[effectName].Load(texM.name) as Texture;
    //                         textureDic[mesh.name] = texN;
    // 
    //                         
    //                    //     Texture texN = 
    //             //          Texture texN =  Texture.Instantiate(texM) as Texture;
    //            //             texM.wrapMode = TextureWrapMode.Clamp;
    //                         mtarr[j].mainTexture = null;
    //        //                 mtarr[j].mainTexture = texM;
    //       //                  Debug.Log(">>>>>>>>>>>>>>>>mainTexture :" + mtarr[j].mainTexture.name);
    //                     }
    // //                     particleSystemRendererList[i].materials = mtarr;
    //                 }
    //             }
}
