Shader "Blue/Effect/WNiuqu" {
	Properties {
		_Color ("Main Color", Color) = (1,1,1,1)
		jialiang  ("jialiang", range (0,10)) = 1
		_MainTex ("Base (RGB)", 2D) = "white" {}
		_Raodong1 ("Raodong1", 2D) = "white" {}
		_HeatForce  ("Heat Force", range (0,3)) = 0.1
		_HeatTime1x  ("Heat Time1x", float) = 1
		_HeatTime1y  ("Heat Time1y", float) = 1
	}
	SubShader {
		Tags { "Queue"="Transparent" "IgnoreProjector"="True" "RenderType"="Transparent"}
		LOD 200
		CGPROGRAM
		#pragma surface surf Lambert alpha 
		//#pragma surface surf Lambert decal:add
		fixed4 _Color;
		sampler2D _MainTex;
		sampler2D _Raodong1;
		float _HeatForce;
		float _HeatTime1x;
		float _HeatTime1y;
		
		float jialiang;
		
		struct Input {
			float2 uv_MainTex;
			float2 uv_Raodong1;
			float4 color : COLOR;
		};

		void surf (Input IN, inout SurfaceOutput o) {
			
			IN.uv_Raodong1.x += _Time.x*_HeatTime1x;
			IN.uv_Raodong1.y += _Time.y*_HeatTime1y*.1;
			fixed4 c1 = tex2D(_Raodong1, IN.uv_Raodong1);
    		fixed4 c0 = tex2D(_MainTex, IN.uv_MainTex);
    		IN.uv_MainTex.x+=c0.r*c1.r*_HeatForce;
    		IN.uv_MainTex.y+=c0.g*c1.g*_HeatForce;
			
			half4 c =tex2D(_MainTex, IN.uv_MainTex); //get it
			o.Emission = c.rgb*jialiang*_Color;
			
			fixed4 bb = tex2D(_MainTex, IN.uv_MainTex);		
			float4 Multiply4= bb* _Color * IN.color;
			o.Alpha = Multiply4;
		}
		ENDCG
	} 
	FallBack "Diffuse"
}
