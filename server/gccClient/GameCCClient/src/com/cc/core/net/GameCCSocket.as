package com.cc.core.net
{
	
	import com.cc.core.Globals;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.util.MessageUtil;
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.external.ExternalInterface;
	import flash.net.Socket;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	import mx.controls.Alert;
	
	public class GameCCSocket extends Socket
	{
		private static var _instance:GameCCSocket;
		private var len:int;
		private var mEventListeners:Dictionary;
		
		public static function get instance():GameCCSocket{
			if(_instance == null) _instance = new GameCCSocket();
			return _instance;
		}
		
		public function GameCCSocket(host:String=null, port:int=0)
		{
			super(host, port);
			addEventListener(Event.CONNECT, onConnect);
			addEventListener(ProgressEvent.SOCKET_DATA, onData);
			addEventListener(IOErrorEvent.IO_ERROR, onIOError);
			addEventListener(Event.CLOSE, onClose);
			addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
		}
		private function onClose(e:Event):void{
			Alert.show("连接已经断开，请刷新重新连接。","温馨提示", 4, null, function():void{
				ExternalInterface.call("location.reload");
			}); 
		}
		
		private function onIOError(e:IOErrorEvent):void{
			trace(e.text);
		}
		private function onSecurityError(e:SecurityErrorEvent):void{
		}
		
		private function onConnect(e:Event):void{
			Globals.instance.setBusy = false;
			
		}
		
		override public function close():void{
			super.close();
		}
		private function onData(e:ProgressEvent):void{
			if(!connected) return;
			len == 0
			while(connected && this.bytesAvailable > 0){
				if(len == 0 ){
					if(this.bytesAvailable < 4) break;
					var byteHead:ByteArray = new ByteArray();
					this.readBytes(byteHead, 0, 4);
					len = byteHead.readInt();
				}
				if(this.bytesAvailable < len) break;
				var msgID:int = readInt();
				var bytes:ByteArray = new ByteArray();
				this.readBytes(bytes, 0, len - 4);
				len = 0;
			}
			dispatchCmd(msgID, bytes);
		}
		
		public function send(msgID:int, msg:String="{}") : void {
			if(connected == false) return;
			var bytes : ByteArray = new ByteArray();
			
			bytes.writeUTFBytes(msg);

			var msgLength : int = bytes.length;
			this.writeInt(msgLength + 4);
			this.writeInt(msgID);
			this.writeBytes(bytes);
			this.flush();
			
			Globals.instance.setBusy = true;
		}
		
		public function addCmdListener(mid:int, handler:Function):void{
			if (this.mEventListeners == null){
				this.mEventListeners = new Dictionary(true);
			};
			var handlerVec:Vector.<Function> = (this.mEventListeners[mid] as Vector.<Function>);
			if (handlerVec == null){
				var v:Vector.<Function> = new Vector.<Function>(1);
				v[0] = handler;
				this.mEventListeners[mid] = v;
			} else {
				if (handlerVec.indexOf(handler) == -1){
					handlerVec.push(handler);
				}; 
			};
		}
		public function removeCmdListener(mid:int, handler:Function):void{
			var handlerVec:Vector.<Function>;
			var handlerLen:int;
			var otherHandlerVec:Vector.<Function>;
			var count:int;
			if (this.mEventListeners){
				handlerVec = (this.mEventListeners[mid] as Vector.<Function>);
				if (handlerVec){
					var index:int = handlerVec.indexOf(handler);
					if(index != -1){
						handlerVec.splice(index, 1);
					}
				};
			};
		}
		
		public function dispatchCmd(mid:int, data:ByteArray=null):void{
			Globals.instance.setBusy = false;

			if(mid == OptTypeConstant.EXCEPTION){
				//显示异常信息
				MessageUtil.showFaultMessage(data.readUTFBytes(data.length));
				return;
			}
			var handlerVec:Vector.<Function>;
			var handler:Function;
			
			if (this.mEventListeners[mid]){
				handlerVec = this.mEventListeners[mid];
				for each (handler in handlerVec) {
					var bytes:ByteArray = new ByteArray();
					bytes.writeBytes(data);
					bytes.position = 0;
					if(handler.length == 1){
							handler(bytes);
					}else if(handler.length == 2){
							handler(bytes, mid);
					}
				};
			};
		}
	}
}
