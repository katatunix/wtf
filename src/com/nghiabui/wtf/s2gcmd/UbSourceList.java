package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.Path;

import java.util.List;
import java.util.function.Supplier;

public class UbSourceList implements Supplier<List<Path>> {
	
	private final Supplier<List<Path>> originSupplier;
	
	public UbSourceList(Supplier<List<Path>> originSupplier) {
		this.originSupplier = originSupplier;
	}
	
	@Override
	public List<Path> get() {
		final List<Path> originSources = originSupplier.get();
		return originSources;
	}
	
}
