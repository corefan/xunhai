Shader "Hidden/CTE/Billboard" {
	Properties {
	    [HideInInspector] _Color ("Main Color", Color) = (1, 1, 1, 1)
	    [HideInInspector] _Emission ("Emission", Color) = (1, 1, 1, 1)
		[HideInInspector] _MainTex ("Base (RGB) Alpha (A)", 2D) = "white" {}
		[HideInInspector] _Cutoff("Cutoff",range(0.01,1)) = 0.2
	}
	SubShader {
		Tags {"Queue" = "Geometry"
		"IgnoreProjector"="True"}
	    Cull back
	    LOD 10
	    //colormask rgb
	    CGPROGRAM
	    #pragma surface surf Lambert vertex:Vert addshadow
	    #pragma exclude_renderers flash
	    float4 _BillboardRotAndStart;
	    float4 _BillboardPosAndDis;
		
	    struct Input {
	        float2 uv_MainTex;
	        fixed4 color : COLOR;
			float3 viewDir;
	    };

	    void TerrainBillboard(inout float4 pos, float2 offset, float2 size, float3 polyPos) {
			float dis = distance(polyPos.xyz,_BillboardPosAndDis.xyz);
			if( dis > _BillboardPosAndDis.w || dis < _BillboardRotAndStart.w){
				pos.xyz *= 0;
			}
			else {
	    		float angleH = _BillboardRotAndStart.y;
	    		angleH = -90 - angleH;
	    		if (angleH > 360)  angleH -= 360;
	    		float radiansH = radians(angleH);
	    		float halfPlaneWidth = size.x*0.5;
	    		float H_x = halfPlaneWidth*(1-sin(radiansH));
	    		float H_y = halfPlaneWidth*cos(radiansH);
	    		float3 billboardPos = float3(H_x,0,H_y);
	    		pos.xyz += billboardPos.xyz * (offset.x-1);
	    		pos.xyz += billboardPos.xyz * offset.x;
	    		
	    		float angleV = _BillboardRotAndStart.x;
	    		angleV = angleV;
	    		float radiansV = radians(angleV);
	    		radiansH = radians(180+ angleH);
	    		float V_x = sin(radiansV)*size.y*cos(radiansH);
	    		float V_y = (cos(radiansV) - 1)*size.y;
	    		float V_z = sin(radiansV)*size.y*sin(radiansH);
	    		float3 billboardTilt = float3(V_x,V_y,V_z);
	    		pos.xyz += billboardTilt.xyz * offset.y;
			}
	    }

		
	    
	    void Vert (inout appdata_full v) {
	    	TerrainBillboard(v.vertex, v.texcoord.xy, v.texcoord1.xy, v.tangent.xyz);	
	    	v.normal=float3(0,1,0);
	    }
	    
	    sampler2D _MainTex;
	    fixed4 _Color;
	    fixed4 _Emission;
	    fixed _Cutoff;
	    
	    void surf (Input IN, inout SurfaceOutput o) {
	    	fixed4 c = tex2D(_MainTex, IN.uv_MainTex) * _Color;
	    	o.Albedo = c.rgb;
			o.Emission = c.rgb * _Emission.rgb;
			o.Alpha = c.a;
	    	clip (o.Alpha - _Cutoff);
	    }
	    ENDCG
	}
	Fallback Off
}