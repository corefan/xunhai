// 功  能： 透视 双面 alpha
// 时  间： 2018-04-20 12:59:15
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
Shader "A_SKG/Character" 
{

	Properties  
	{
		_MainTex ("Base (RGB)", 2D) = "white" {}
		_Color ("Main Color", Color) = (1,1,1,1)//Tint Color
		_XRayColor("XRay Color", Color) = (1,0.8,0.5,1)
		[Enum(UnityEngine.Rendering.CompareFunction)] _ZTest ("ZTest", Float) = 5  //声明外部控制
		[Enum(Off, 0, On, 1)] _ZWrite ("ZWrite", Float) = 0  //声明外部控制开关
	}

	SubShader  
	{
		//Tags{ "Queue" = "Geometry+100" "RenderType" = "Opaque" }
		Tags { "Queue" = "Transparent" }// 启用调alpha
		  
		//渲染X光效果的Pass  
		Pass  
		{
			Blend SrcAlpha One  
			ZWrite Off  //ZWrite [_ZWrite]
			ZTest Greater //ZTest [_ZTest]
			
			////Blend [_SrcBlend] [_DstBlend] //获取值应用

			CGPROGRAM  
			#include "Lighting.cginc" 
			fixed4 _XRayColor;  
			struct v2f  
			{
				float4 pos : SV_POSITION;  
				float3 normal : TEXCOORD1; //normal; 
				float3 viewDir : TEXCOORD0;  
			};  
  
			v2f vert (appdata_base v)  
			{
				v2f o;  
				o.pos = mul(UNITY_MATRIX_MVP, v.vertex);  
				o.viewDir = ObjSpaceViewDir(v.vertex);  
				o.normal = v.normal;  
				return o;  
			}
  
			fixed4 frag(v2f i) : SV_Target  
			{
				float3 normal = normalize(i.normal);  
				float3 viewDir = normalize(i.viewDir);  
				float rim = 1 - dot(normal, viewDir);  
				return _XRayColor * rim;  
			}
			#pragma vertex vert  
			#pragma fragment frag  
			ENDCG  
		}
		//Tags { "RenderType"="Opaque" }
		Tags { "Queue" = "Transparent" }// 启用调alpha
		LOD 100
		Material
		{
			Diffuse[_Color]
		}
		Pass {
			Cull Off//直接删除剔除
			Lighting Off
			
			Blend SrcAlpha OneMinusSrcAlpha // 启用调alpha
			color[_Color] // 启用调alpha

			SetTexture [_MainTex] { combine texture } 
			//混合原色
			SetTexture [_MainTex]
			{
				ConstantColor [_Color]
				Combine Previous * Constant
			}
		}
	}
	  
	FallBack "Diffuse"
}