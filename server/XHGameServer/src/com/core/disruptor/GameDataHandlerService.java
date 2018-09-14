package com.core.disruptor;


/**
 * 2013-10-29
 *
 */
public class GameDataHandlerService extends ThreadLocal<GameDataHandler> {

	@Override
	public GameDataHandler get() {
		return super.get();
	}

	@Override
	protected GameDataHandler initialValue() {
		return new GameDataHandler();
	}

	@Override
	public void remove() {
		super.remove();
	}

	@Override
	public void set(GameDataHandler value) {
		super.set(value);
	}
}
