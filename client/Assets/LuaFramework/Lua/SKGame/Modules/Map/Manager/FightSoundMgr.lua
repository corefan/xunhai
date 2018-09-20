FightSoundMgr =BaseClass()
local soundMap = {}
function FightSoundMgr:__init(career)
	self.career = career

	self.cfg = GetCfgData( "newroleDefaultvalue" ):Get(self.career)

	self.attackAudio = self.cfg.attackAudio
	self.hurtAudio = self.cfg.hurtAudio
	self.deadAudio = self.cfg.deadAudio
	self.hitOnAudio = self.cfg.hitOnAudio

end

--攻击
function FightSoundMgr:Attack()
	if not self.attackAudio then return end
	if math.random(2) == 1 then
		if not soundMap[self.attackAudio] then
			soundMap[self.attackAudio] = "audio/"..self.attackAudio..".unity3d"
		end
		soundMgr:PlayEffect(soundMap[self.attackAudio], self.attackAudio)
	end
end

--受击
function FightSoundMgr:Hurt()
	if not self.hurtAudio then return end
	if not soundMap[self.hurtAudio] then
		soundMap[self.hurtAudio] = "audio/"..self.hurtAudio..".unity3d"
	end
	soundMgr:PlayEffect(soundMap[self.hurtAudio], self.hurtAudio)
end

--死亡
function FightSoundMgr:Dead()
	if not self.deadAudio then return end
	if not soundMap[self.deadAudio] then
		soundMap[self.deadAudio] = "audio/"..self.deadAudio..".unity3d"
	end
	soundMgr:PlayEffect(soundMap[self.deadAudio], self.deadAudio)
end

--命中
function FightSoundMgr:hitOn()
	if not self.hitOnAudio then return end
	if not soundMap[self.attackAudio] then
		soundMap[self.attackAudio] = "audio/"..self.attackAudio..".unity3d"
	end
	soundMgr:PlayEffect(soundMap[self.attackAudio], self.hitOnAudio)
end

function FightSoundMgr:__delete()
	self.attackAudio = nil
	self.hurtAudio = nil
	self.deadAudio = nil
	self.hitOnAudio = nil
end