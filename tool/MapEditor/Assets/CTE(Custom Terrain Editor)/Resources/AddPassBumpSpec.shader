Shader "Hidden/CTE/AddPass Bumped Specular" {
	Properties {
		_SpecColor ("Specular Color", Color) = (0.5, 0.5, 0.5, 1)
		_Shininess ("Shininess", Range (0.03, 1)) = 0.078125

		// set by terrain engine
		[HideInInspector] _Control ("Control (RGBA)", 2D) = "red" {}
		[HideInInspector] _Splat3 ("Layer 3 (A)", 2D) = "white" {}
		[HideInInspector] _Splat2 ("Layer 2 (B)", 2D) = "white" {}
		[HideInInspector] _Splat1 ("Layer 1 (G)", 2D) = "white" {}
		[HideInInspector] _Splat0 ("Layer 0 (R)", 2D) = "white" {}
		[HideInInspector] _Normal3 ("Normal 3 (A)", 2D) = "bump" {}
		[HideInInspector] _Normal2 ("Normal 2 (B)", 2D) = "bump" {}
		[HideInInspector] _Normal1 ("Normal 1 (G)", 2D) = "bump" {}
		[HideInInspector] _Normal0 ("Normal 0 (R)", 2D) = "bump" {}
	}

	SubShader {
		Tags {
			"SplatCount" = "4"
			"Queue" = "Geometry+1"
			"IgnoreProjector"="True"
			"RenderType" = "Opaque"
		}

		CGPROGRAM
		#pragma surface surf BlinnPhong decal:add vertex:Vert finalcolor:myfinal exclude_path:prepass exclude_path:deferred
		#pragma multi_compile_fog
		#pragma multi_compile _TERRAIN_NORMAL_MAP
		#pragma target 3.0
		// needs more than 8 texcoords
		#pragma exclude_renderers gles

		#define TERRAIN_SPLAT_ADDPASS
		#include "TerrainSplatmapCommon.cginc"

		half _Shininess;

		void Vert(inout appdata_full v, out Input data){
			UNITY_INITIALIZE_OUTPUT(Input, data);
			data.tc_Control = TRANSFORM_TEX(v.texcoord, _Control);	// Need to manually transform uv here, as we choose not to use 'uv' prefix for this texcoord.
			float4 pos = mul (UNITY_MATRIX_MVP, v.vertex);
			UNITY_TRANSFER_FOG(data, pos);
	
			#ifdef _TERRAIN_NORMAL_MAP
			v.tangent.xyz = cross(v.normal, float3(0,1,0));
			v.tangent.w = -1;
			#endif
		}

		void surf(Input IN, inout SurfaceOutput o)
		{
			half4 splat_control;
			half weight;
			fixed4 mixedDiffuse;
			SplatmapMix(IN, splat_control, weight, mixedDiffuse, o.Normal);
			o.Albedo = mixedDiffuse.rgb;
			o.Alpha = weight;
			o.Gloss = mixedDiffuse.a;
			o.Specular = _Shininess;
		}

		void myfinal(Input IN, SurfaceOutput o, inout fixed4 color)
		{
			color.rgb *= o.Alpha;
			color.a = 1.0f;
#ifdef TERRAIN_SPLAT_ADDPASS
			UNITY_APPLY_FOG_COLOR(IN.fogCoord, color, fixed4(0, 0, 0, 0));
#else
			UNITY_APPLY_FOG(IN.fogCoord, color);
#endif
		}

		ENDCG
	}

	Fallback "Hidden/CTE/AddPass"
}
