// 功  能： 双面
// 时  间： 2016-09-11 12:59:18
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
Shader "A_SKG/Monster" 
{
	Properties {
		_Color ("Main Color", Color) = (1,1,1,1)//Tint Color
		_MainTex ("Base (RGB)", 2D) = "white" {}
	}

	SubShader {
		Tags { "Queue" = "Transparent" }

		//LOD 100
		Material
		{
			Diffuse[_Color]
		} 
		Pass {
			Cull Off//直接删除剔除
			//Lighting Off
			Blend SrcAlpha OneMinusSrcAlpha
			color[_Color]
			SetTexture [_MainTex] { combine texture } 
			SetTexture [_MainTex]
			{
				ConstantColor [_Color]
				Combine Previous * Constant
			}
		}
	}
}