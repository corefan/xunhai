using UnityEngine;
using System;
using System.Collections.Generic;
using LuaInterface;
using LuaFramework;

using BindType = ToLuaMenu.BindType;
using UnityEngine.UI;
using System.Reflection;
using FairyGUI;
using System.Collections;

public static class CustomSettings
{
	public static string FrameworkPath = AppConst.FrameworkRoot;
	public static string saveDir = FrameworkPath + "/ToLua/Source/Generate/";
	public static string luaDir = FrameworkPath + "/Lua/";
	public static string toluaBaseType = FrameworkPath + "/ToLua/BaseType/";
	public static string toluaLuaDir = FrameworkPath + "/ToLua/Lua";

	//导出时强制做为静态类的类型(注意customTypeList 还要添加这个类型才能导出)
	//unity 有些类作为sealed class, 其实完全等价于静态类
	public static List<Type> staticClassTypes = new List<Type>
	{
		typeof(UnityEngine.Application),
		typeof(UnityEngine.Time),
		typeof(UnityEngine.Screen),
		typeof(UnityEngine.SleepTimeout),
		typeof(UnityEngine.Input),
		typeof(UnityEngine.Resources),
		typeof(UnityEngine.Physics),
		typeof(UnityEngine.RenderSettings),
		typeof(UnityEngine.QualitySettings),
		typeof(UnityEngine.GL),
	};

	//附加导出委托类型(在导出委托时, customTypeList 中牵扯的委托类型都会导出， 无需写在这里)
	public static DelegateType[] customDelegateList = 
	{		
		_DT(typeof(Action)),		
		_DT(typeof(UnityEngine.Events.UnityAction)),
		_DT(typeof(System.Predicate<int>)),
        _DT(typeof(System.Action<int>)),
        _DT(typeof(System.Comparison<int>)),
		//fairy gui
		_DT(typeof(EventCallback0)),
		_DT(typeof(EventCallback1)),
		_DT(typeof(PlayCompleteCallback)),
		_DT(typeof(TransitionHook)),
	};

	//在这里添加你要导出注册到lua的类型列表
	public static BindType[] customTypeList =
	{				
		//------------------------为例子导出--------------------------------
		//_GT(typeof(TestEventListener)),				
		//_GT(typeof(TestAccount)),
		//_GT(typeof(Dictionary<int, TestAccount>)).SetLibName("AccountMap"),				
		//_GT(typeof(KeyValuePair<int, TestAccount>)),	
		//_GT(typeof(TestExport)),
		//_GT(typeof(TestExport.Space)),
		//-------------------------------------------------------------------		
        //_GT(typeof(Debugger)).SetNameSpace(null),          

#if USING_DOTWEENING
		_GT(typeof(DG.Tweening.DOTween)),
		_GT(typeof(DG.Tweening.Tween)).SetBaseType(typeof(System.Object)).AddExtendType(typeof(DG.Tweening.TweenExtensions)),
		_GT(typeof(DG.Tweening.Sequence)).AddExtendType(typeof(DG.Tweening.TweenSettingsExtensions)),
		_GT(typeof(DG.Tweening.Tweener)).AddExtendType(typeof(DG.Tweening.TweenSettingsExtensions)),
		_GT(typeof(DG.Tweening.LoopType)),
		_GT(typeof(DG.Tweening.PathMode)),
		_GT(typeof(DG.Tweening.PathType)),
		_GT(typeof(DG.Tweening.RotateMode)),
		_GT(typeof(Component)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(Transform)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(Light)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(Material)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(Rigidbody)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(Camera)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		_GT(typeof(AudioSource)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		//_GT(typeof(LineRenderer)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),
		//_GT(typeof(TrailRenderer)).AddExtendType(typeof(DG.Tweening.ShortcutExtensions)),	
#else
									 
		_GT(typeof(Component)),
		_GT(typeof(Transform)),
		_GT(typeof(Material)),
		_GT(typeof(Light)),
		_GT(typeof(Rigidbody)),
		_GT(typeof(Camera)),
		_GT(typeof(AudioSource)),
		//_GT(typeof(LineRenderer))
		//_GT(typeof(TrailRenderer))
#endif   
						
		_GT(typeof(Behaviour)),
		_GT(typeof(MonoBehaviour)),		
		_GT(typeof(GameObject)),
		_GT(typeof(TrackedReference)),
		_GT(typeof(Application)),
		_GT(typeof(Physics)),
		_GT(typeof(Collider)),
		_GT(typeof(Time)),		
		_GT(typeof(Texture)),
		_GT(typeof(Texture2D)),
		_GT(typeof(Shader)),
		_GT(typeof(Renderer)),
		_GT(typeof(WWW)),
		_GT(typeof(Screen)),
		_GT(typeof(CameraClearFlags)),
		_GT(typeof(AudioClip)),
		_GT(typeof(AssetBundle)),
		_GT(typeof(ParticleSystem)),
		_GT(typeof(AsyncOperation)).SetBaseType(typeof(System.Object)),
		_GT(typeof(LightType)),
		_GT(typeof(SleepTimeout)),
#if UNITY_5_3_OR_NEWER && !UNITY_5_6_OR_NEWER
        _GT(typeof(UnityEngine.Experimental.Director.DirectorPlayer)),
#endif
		_GT(typeof(Animator)),
		_GT(typeof(Input)),
		_GT(typeof(KeyCode)),
		_GT(typeof(SkinnedMeshRenderer)),
		_GT(typeof(Space)),		
										   
		_GT(typeof(MeshRenderer)),			
#if !UNITY_5_4_OR_NEWER
        _GT(typeof(ParticleEmitter)),
        _GT(typeof(ParticleRenderer)),
        _GT(typeof(ParticleAnimator)), 
#endif
		_GT(typeof(BoxCollider)),
		_GT(typeof(MeshCollider)),
		_GT(typeof(SphereCollider)),		
		_GT(typeof(CharacterController)),
		_GT(typeof(CapsuleCollider)),

        _GT(typeof(Animation)),		
		_GT(typeof(AnimationClip)).SetBaseType(typeof(UnityEngine.Object)),		
		_GT(typeof(AnimationState)),
		_GT(typeof(AnimationBlendMode)),
		_GT(typeof(QueueMode)),  
		_GT(typeof(PlayMode)),
		_GT(typeof(WrapMode)),

		_GT(typeof(QualitySettings)),
		_GT(typeof(RenderSettings)),												   
		_GT(typeof(BlendWeights)),		   
		_GT(typeof(RenderTexture)),
		_GT(typeof(Resources)),
		_GT(typeof(UnityEngine.SceneManagement.LoadSceneMode)),		 
		_GT(typeof(UnityEngine.SceneManagement.SceneManager)),		 
		_GT(typeof(UnityEngine.SceneManagement.Scene)),		 
		  
		//for LuaFramework
		_GT(typeof(RectTransform)),
		_GT(typeof(Text)),

		_GT(typeof(Util)),
		_GT(typeof(AppConst)),
		_GT(typeof(LuaHelper)),
		_GT(typeof(ByteBuffer)),
		_GT(typeof(LuaBehaviour)),

		_GT(typeof(GameManager)),
		_GT(typeof(LuaManager)),
		_GT(typeof(LoaderManager)),

		_GT(typeof(SoundManager)),
		_GT(typeof(TimerManager)),

		_GT(typeof(NetworkManager)),
		_GT(typeof(ResourceManager)),

		//fairy gui
		_GT(typeof(EventContext)),
		_GT(typeof(EventDispatcher)),
		_GT(typeof(EventListener)),
		_GT(typeof(InputEvent)),
		_GT(typeof(DisplayObject)),
		_GT(typeof(Container)),
		_GT(typeof(Stage)),
		_GT(typeof(Controller)),
		_GT(typeof(GObject)),
		_GT(typeof(GGraph)),
		_GT(typeof(GGroup)),
		_GT(typeof(GImage)),
		_GT(typeof(GLoader)),
		_GT(typeof(PlayState)),
		_GT(typeof(GMovieClip)),
		_GT(typeof(TextFormat)),
		_GT(typeof(GTextField)),
		_GT(typeof(GRichTextField)),
		_GT(typeof(GTextInput)),
		_GT(typeof(GComponent)),
		_GT(typeof(GList)),
		_GT(typeof(GRoot)),
		_GT(typeof(GLabel)),
		_GT(typeof(GButton)),
		_GT(typeof(GComboBox)),
		_GT(typeof(GProgressBar)),
		_GT(typeof(GSlider)),
		_GT(typeof(PopupMenu)),
		_GT(typeof(ScrollPane)),
		_GT(typeof(Transition)),
		_GT(typeof(UIPackage)),
		_GT(typeof(Window)),
		_GT(typeof(GObjectPool)),
		_GT(typeof(Relations)),
		_GT(typeof(RelationType)),

		_GT(typeof(Timers)),

		_GT(typeof(LuaUIHelper)),
		_GT(typeof(GLuaComponent)),
		_GT(typeof(GLuaLabel)),
		_GT(typeof(GLuaButton)),
		_GT(typeof(GLuaProgressBar)),
		_GT(typeof(GLuaSlider)),
		_GT(typeof(GLuaComboBox)),
		_GT(typeof(LuaWindow)),
		
		_GT(typeof(TweenUtils)),
        _GT(typeof(DG.Tweening.Sequence)),
		_GT(typeof(DG.Tweening.Ease)),
		//----------------------------------
		_GT(typeof(GoWrapper)),
		_GT(typeof(TreeView)),
		_GT(typeof(TreeNode)),
		_GT(typeof(PageOption)),
		_GT(typeof(UIPanel)),
		_GT(typeof(UIPainter)),
		_GT(typeof(UIObjectFactory)),
		_GT(typeof(UIContentScaler)),
		_GT(typeof(Margin)),
		
		_GT(typeof(NTexture)),
		_GT(typeof(Queue)),
		_GT(typeof(UIConfig)),
		_GT(typeof(Matrix4x4)),
		_GT(typeof(EffectRenderObjManager)),
		_GT(typeof(EffectRenderObj)),
		_GT(typeof(EffectController)),
		_GT(typeof(Node)),
		//_GT(typeof(RenderImage)),
		
		//_GT(typeof(GearXY)),
		//_GT(typeof(GearAnimation)),
		//_GT(typeof(GearLook)),
		//_GT(typeof(GearSize)),
		//_GT(typeof(GearDisplay)),
		//_GT(typeof(GearColor)),
		//_GT(typeof(GearBase)),

		//_GT(typeof(RotationGesture)),
		//_GT(typeof(SwipeGesture)),
		//_GT(typeof(PinchGesture)),
		//_GT(typeof(FairyGUI.Utils.UBBParser)),
		//_GT(typeof(FairyGUI.Utils.ByteBuffer)),
		//_GT(typeof(FairyGUI.Utils.ToolSet)),

		//_GT(typeof(DragDropManager)),
		//_GT(typeof(StageCamera)),
		//_GT(typeof(StageEngine)),
		//_GT(typeof(UpdateContext)),
		//_GT(typeof(Shape)),

		// engine
		_GT(typeof(GlobalDispatcher)),
		_GT(typeof(LayerTag)),
		_GT(typeof(LayersMgr)),
		_GT(typeof(GSprite)),
		_GT(typeof(UBBParserExtension)),
		_GT(typeof(BaseWindow)),
		_GT(typeof(CameraController)),
		_GT(typeof(PlayerPrefs)),
		_GT(typeof(LuaBindSceneObj)),
		_GT(typeof(ControllerColliderHit)),
		_GT(typeof(AnimatorStateInfo)),
		_GT(typeof(CanYing)),
		_GT(typeof(DrawUtils)),
        _GT(typeof(AutoSizeType)),
        _GT(typeof(ScrollType)),
        _GT(typeof(AlignType)),
        _GT(typeof(VertAlignType)),
        _GT(typeof(OverflowType)),
        _GT(typeof(FillType)),

        _GT(typeof(Endian)),
        _GT(typeof(NavMeshPathStatus)),
        _GT(typeof(NavMeshAgent)),
        _GT(typeof(ListLayoutType)),
        _GT(typeof(NormalCameraController)),
        _GT(typeof(MicroPhoneInput)),
        _GT(typeof(PayMgr)),
        _GT(typeof(SdkToIOS)),
        _GT(typeof(DeviceInfo)),
        //_GT(typeof(Canvas)),
        _GT(typeof(RawImage)),
        _GT(typeof(SceneObjTrigger)),
	};

    public static List<Type> dynamicList = new List<Type>()
    {
        typeof(MeshRenderer),
#if !UNITY_5_4_OR_NEWER
        typeof(ParticleEmitter),
        typeof(ParticleRenderer),
        typeof(ParticleAnimator),
#endif

        typeof(BoxCollider),
        typeof(MeshCollider),
        typeof(SphereCollider),
        typeof(CharacterController),
        typeof(CapsuleCollider),

        typeof(Animation),
        typeof(AnimationClip),
        typeof(AnimationState),

        typeof(BlendWeights),
        typeof(RenderTexture),
        typeof(Rigidbody),
    };

    //重载函数，相同参数个数，相同位置out参数匹配出问题时, 需要强制匹配解决
    //使用方法参见例子14
    public static List<Type> outList = new List<Type>()
    {
        
    };

    public static BindType _GT(Type t)
    {
        return new BindType(t);
    }

    public static DelegateType _DT(Type t)
    {
        return new DelegateType(t);
    }    
}
