--[[
	id:int#ID
	des:string#备注说明
	type:string#类型
]]

local cfg={
	[1]={
		id=1,
		des="开启音乐",
		type="openMusic"
	},
	[2]={
		id=2,
		des="开启音效",
		type="openSound"
	},
	[3]={
		id=3,
		des="开启语音聊天",
		type="openVoiceChat"
	},
	[4]={
		id=4,
		des="显示血条",
		type="showBlood"
	},
	[5]={
		id=5,
		des="显示名字",
		type="showName"
	},
	[6]={
		id=6,
		des="显示小地图",
		type="showSmallMap"
	}
}

function cfg:Get( key )
	return cfg[key]
end
return cfg