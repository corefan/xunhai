// 功  能： 双面 带阴影 带穿透
// 时  间： 2016-09-11 12:59:15
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
Shader "A_SKG/Character" 
{
	Properties 
	{
		_MainTex ("Base (RGB)", 2D) = "white" {}
		_Color ("Main Color", Color) = (1.0,1.0,1.0,1.0)
		_Cutoff ("Base Alpha cutoff", Range (0,.9)) = .5
		_PassThroughColor ("PassThrough Color", Color) = (0.38,0.86,0.74,0)//穿墙颜色设置
	    _ShadowDir("Shadow Direction", Vector) = (-0.2, 1, 0.2, 1)
		_ShadowColor("Shadow Color", Color) = (0, 0, 0, 0.3)
		_ShadowAlpha("Shadow Alpha", float) = 0.3
		_ShadowHeight("Shadow Height", float) = 0.06
	}
	
	SubShader 
	{
		Pass {
			Tags { "RenderType"="Opaque" }
			Lighting off
			ZTest Greater // 穿透(拉前)
			ZWrite off // 穿透(转纯色)
			Color [_PassThroughColor]//透视
		}

		Pass 
		{
			//Tags { "Queue"="Transparent" }
			Cull Off
			Name "FORWARD"
			Tags { "LightMode" = "ForwardBase" }
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#include "UnityCG.cginc"
			sampler2D _MainTex;
			float4 _MainTex_ST;
			fixed4 _Color;
			float _Cutoff;
			struct VertexInput 
			{
				float4 vertex 	: POSITION;
				float3 normal 	: NORMAL;
				float4 uv 		: TEXCOORD0;
			};
			struct VertexOutput
			{
				float4 pos : SV_POSITION;
			  	half2 uv : TEXCOORD0; // _MainTex
			  	half3 normal : TEXCOORD1;
			  	fixed3 vlight : TEXCOORD2; // ambient/SH/vertexlights
			  	half3 posWorld : TEXCOORD3;
			};
			// vertex shader
			//VertexOutput vert_surf (VertexInput v) 
			//{
			//  	VertexOutput o;

			//  	o.pos = mul (UNITY_MATRIX_MVP, v.vertex);
			//  	o.uv.xy = v.uv;
			//  	o.normal = normalize(mul((float3x3)UNITY_MATRIX_IT_MV, SCALED_NORMAL));
			//  	o.vlight = ShadeSH9 (float4(o.normal,1.0));
			//  	o.posWorld 	= mul(_Object2World, v.vertex).xyz;
			//  	return o;
			//}

			//// fragment shader
			//fixed4 frag_surf (VertexOutput IN) : COLOR 
			//{
			//		IN.normal = normalize(IN.normal);
			//  	fixed4 texColor = tex2D(_MainTex , TRANSFORM_TEX(IN.uv, _MainTex)) * _Color;
			//  	fixed4 finalColor = texColor;
			//  	return finalColor;
			//}
			
			struct v2f {
				float4 vertex : POSITION;
				float4 color : COLOR;
				float2 texcoord : TEXCOORD0;
			};

			v2f vert (v2f v)
			{
				v2f o;
				o.vertex = mul(UNITY_MATRIX_MVP, v.vertex);
				o.color = v.color;
				o.texcoord = TRANSFORM_TEX(v.texcoord, _MainTex);
				return o;
			}
			
			half4 frag (v2f i) : COLOR
			{
				half4 col = _Color * tex2D(_MainTex, i.texcoord);
				clip(col.a - _Cutoff);
				return col;
			}
			ENDCG
		}

		Pass 
		{
			Tags { "RenderType"="Opaque"}
			Blend SrcAlpha OneMinusSrcAlpha
			//Blend Zero OneMinusSrcAlpha
			Stencil
			{  
			    Ref 1
			    Comp NotEqual  
			    Pass Replace
			}
			
			//Fog{Mode off}
			CGPROGRAM
			#pragma vertex vert 
			#pragma fragment frag
			#include "UnityCG.cginc"
			
			struct vertexOutput 
			{
				float4 pos : SV_POSITION;
				float2 xy : TEXCOORD0;
			};

			fixed4 _ShadowColor;
			half _ShadowAlpha;
			half _ShadowHeight;
			fixed4 _ShadowDir;
			
			vertexOutput vert(appdata_base v)
			{
				float4 vt;
				vertexOutput output;
				output.xy=float2(0,0);
				vt= mul(_Object2World, v.vertex);
				//vt.xyz*=1.0;
				output.xy.y=vt.y/vt.w;
				fixed3 lightDir=v.vertex.xyz-float3(_ShadowDir.x*1000,_ShadowDir.y*1000,_ShadowDir.z*1000);
				vt.xz=vt.xz-((vt.y-_ShadowHeight)/lightDir.y)*lightDir.xz;
				vt.y=0.001+_ShadowHeight;
				vt=mul(_World2Object,vt);
				output.pos=mul(UNITY_MATRIX_MVP, vt);
				return output;
			}
	 		float4 frag(vertexOutput input) : COLOR 
			{
				return float4(_ShadowColor.xyz, _ShadowAlpha);
			}
	 		ENDCG 
		}

		
		//Pass {  
		//	CGPROGRAM
		//		#pragma vertex vert
		//		#pragma fragment frag
		//		#include "UnityCG.cginc"
		//		struct v2f {
		//			float4 vertex : POSITION;
		//			float4 color : COLOR;
		//			float2 texcoord : TEXCOORD0;
		//		};
		//		sampler2D _MainTex;
		//		float4 _MainTex_ST;
		//		float _Cutoff;
		//		v2f vert (v2f v)
		//		{
		//			v2f o;
		//			o.vertex = mul(UNITY_MATRIX_MVP, v.vertex);
		//			o.color = v.color;
		//			o.texcoord = TRANSFORM_TEX(v.texcoord, _MainTex);
		//			return o;
		//		}
		//		float4 _Color;
		//		half4 frag (v2f i) : COLOR
		//		{
		//			half4 col = _Color * tex2D(_MainTex, i.texcoord);
		//			clip(col.a - _Cutoff);
		//			return col;
		//		}
		//	ENDCG
		//}

		// Second pass:
		// render the semitransparent details.
		Pass {
			Tags { "RequireOption" = "SoftVegetation" }
		
			// Dont write to the depth buffer
			ZWrite off
		
			// Set up alpha blending
			Blend SrcAlpha OneMinusSrcAlpha
		
			CGPROGRAM
				#pragma vertex vert
				#pragma fragment frag
				#include "UnityCG.cginc"
				struct v2f {
					float4 vertex : POSITION;
					float4 color : COLOR;
					float2 texcoord : TEXCOORD0;
				};

				sampler2D _MainTex;
				float4 _MainTex_ST;
				float _Cutoff;
			
				v2f vert (v2f v)
				{
					v2f o;
					o.vertex = mul(UNITY_MATRIX_MVP, v.vertex);
					o.color = v.color;
					o.texcoord = TRANSFORM_TEX(v.texcoord, _MainTex);
					return o;
				}
			
				float4 _Color;
				half4 frag (v2f i) : COLOR
				{
					half4 col = _Color * tex2D(_MainTex, i.texcoord);
					clip(-(col.a - _Cutoff));
					return col;
				}
			ENDCG
		}

    }
	FallBack "Diffuse"
}