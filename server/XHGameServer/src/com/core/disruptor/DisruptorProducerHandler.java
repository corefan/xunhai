package com.core.disruptor;

public class DisruptorProducerHandler extends ThreadLocal<DisruptorProducerService>{

	@Override
	protected DisruptorProducerService initialValue() {
		return new DisruptorProducerService();
	}

	@Override
	public DisruptorProducerService get() {
		return super.get();
	}

	@Override
	public void set(DisruptorProducerService value) {
		super.set(value);
	}

	@Override
	public void remove() {
		super.remove();
	}

	
}
