package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.Path;

import java.util.List;
import java.util.function.Supplier;

public class NecessSourceList implements Supplier<List<Path>> {
	
	private final Supplier<List<Path>> originSupplier;
	private final Path workFolder;
	
	public NecessSourceList(Supplier<List<Path>> originSupplier, Path workFolder) {
		this.originSupplier = originSupplier;
		this.workFolder = workFolder;
	}
	
	@Override
	public List<Path> get() {
		return null;
	}
	
}
