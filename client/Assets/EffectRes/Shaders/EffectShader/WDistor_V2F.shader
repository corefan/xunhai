Shader "Blue/Effect/WNiuqu_Sheng" {
    Properties {
    	_Color ("Main Color", Color) = (0,-0.5,1,.1)
    	jialiang  ("jialiang", range (0,10)) = 1
        _MainTex ("MainTexture", 2D) = "white" {}
        _Raodong ("Raodong", 2D) = "white" {}
        _HeatForce  ("Heat Force", range (-1,1)) = 0.1
		_HeatTime1x  ("Heat Time1x", float) = 1
		_HeatTime1y  ("Heat Time1y", float) = 1
    }
    SubShader {
        Tags {"IgnoreProjector"="True"	 "Queue"="Transparent"	"RenderType"="Transparent"	}
        LOD 200
         Blend SrcAlpha OneMinusSrcAlpha
		Cull Off Lighting Off ZWrite Off Fog{ Mode Off}
        Pass {
            CGPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            #include "UnityCG.cginc"
            fixed4 _Color;
            sampler2D _MainTex;  float4 _MainTex_ST;
            sampler2D _Raodong;  float4 _Raodong_ST;
            
            float jialiang;
            float _HeatForce;
			float _HeatTime1x;
			float _HeatTime1y;
            
           struct v2f
			{
				float4 pos:POSITION;
				float4 uv:TEXCOORD0;
			};

           v2f vert(appdata_base v)
			{
				v2f o;
				o.pos = mul(UNITY_MATRIX_MVP, v.vertex);
				o.uv = v.texcoord;
				return o;
			}

			
            fixed4 frag(v2f  i) : COLOR {
                
                float2 niudong = (i.uv.rg+_Time*float2(_HeatTime1x,_HeatTime1y*.1));
                float2 finally = (i.uv.rg+tex2D(_Raodong,TRANSFORM_TEX(niudong, _Raodong)).r*_HeatForce);
                float4 aa=tex2D(_MainTex,TRANSFORM_TEX(finally, _MainTex));
                
                return aa* jialiang*_Color;
            }
            ENDCG
        }
    }
    FallBack "Diffuse"
}
