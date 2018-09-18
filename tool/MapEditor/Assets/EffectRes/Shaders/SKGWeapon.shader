// 功  能： 双面 带阴影 边沿发光 带穿透
// 时  间： 2016-09-11 12:59:15
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
Shader "A_SKG/Weapon" 
{
	Properties 
	{
		_MainTex ("Base (RGB)", 2D) = "white" {}
		_Color ("Main Color", Color) = (1.0,1.0,1.0,1.0)
		_SpecularColor ("Specular Color", Color) = (0.6,0.6,0.6,1)
		_SpecularPow("Specular Pow" , float) = 0

	    _LightDir("Light Direction" , Vector) = (0,0,0,1)
	    _EdgeWidth("Edge Width", float) = 0
		_EdgePower("Edge Power", float) = 0
		_EdgeColor("Edge Color", Color) = (0, 0, 0, 1)

		//_PassThroughColor ("PassThrough Color", Color) = (0.38,0.86,0.74,0)

		// shadow
	    _ShadowDir("Shadow Direction", Vector) = (-0.2, 1, 0.2, 1)
		_ShadowColor("Shadow Color", Color) = (0, 0, 0, 0.3)
		_ShadowAlpha("Shadow Alpha", float) = 0.3
		_ShadowHeight("Shadow Height", float) = 0.1
	}
	
	SubShader 
	{
		Pass {
			Tags { "RenderType"="Opaque" }
			Lighting Off
			//ZTest Greater // 穿透
			//ZWrite Off
			//Color [_PassThroughColor]
		}

		Pass 
		{
			Tags { "Queue"="Transparent" }
			Cull off
			//Blend SrcAlpha OneMinusSrcAlpha
			Name "FORWARD"
			Tags { "LightMode" = "ForwardBase" }

			CGPROGRAM

			#pragma vertex vert_surf
			#pragma fragment frag_surf

			#include "UnityCG.cginc"
			#include "Lighting.cginc"
			#include "AutoLight.cginc"

			sampler2D _MainTex;
			float4 _MainTex_ST;
			fixed4 _Color;
			fixed4 _SpecularColor;
			half _SpecularPow;

			half4 _LightDir;
			half 	_EdgeWidth;
            half 	_EdgePower;
            half4 _EdgeColor;

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
			VertexOutput vert_surf (VertexInput v) 
			{
			  	VertexOutput o;

			  	o.pos = mul (UNITY_MATRIX_MVP, v.vertex);
			  	o.uv.xy = v.uv;
			  	o.normal = normalize(mul((float3x3)UNITY_MATRIX_IT_MV, SCALED_NORMAL));
			  	o.vlight = ShadeSH9 (float4(o.normal,1.0));
			  	o.posWorld 	= mul(_Object2World, v.vertex).xyz;
			  
			  	return o;
			}

			// fragment shader
			fixed4 frag_surf (VertexOutput IN) : COLOR 
			{
				IN.normal = normalize(IN.normal);
			  	fixed4 texColor = tex2D(_MainTex , TRANSFORM_TEX(IN.uv, _MainTex)) * _Color;

			  	fixed4 finalColor = texColor;
			  
			  	half spec = max(dot(IN.normal , normalize(_LightDir.xyz)),0.001);
			  	finalColor.rgb += texColor * (_SpecularColor.rgb*8*spec*pow(spec , _SpecularPow)+min(_SpecularPow/10,0.3)+IN.vlight);
			  	
			  	half3 view_dir = normalize(_WorldSpaceCameraPos.xyz - IN.posWorld);
			  	
			  	float edgeWeight=_EdgeWidth-dot(view_dir,IN.normal);
				edgeWeight*=_EdgePower;
				edgeWeight=clamp(edgeWeight, 0, 1);
				finalColor.rgb=lerp(finalColor.rgb, _EdgeColor.rgb, edgeWeight);

			  	return finalColor;
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
			    Ref 2
			    Comp NotEqual  
			    Pass Replace
			}
			
			Fog{Mode off}
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
				vt.xyz*=1.0;
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
				//if (input.xy.y<_ShadowHeight)
				//{
				//	discard;
				//}

				return float4(_ShadowColor.xyz, _ShadowAlpha);
			}
	 		ENDCG 
		}
    }
	FallBack "Diffuse"
}