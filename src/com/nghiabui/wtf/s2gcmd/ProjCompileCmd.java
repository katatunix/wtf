package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ProjCompileCmd {
	
	private final SumProjectConfig projectConfig;
	private final Supplier<List<Path>> srcSupplier;
	
	public ProjCompileCmd(SumProjectConfig projectConfig, Supplier<List<Path>> srcSupplier) {
		this.projectConfig = projectConfig;
		this.srcSupplier = srcSupplier;
	}
	
	public List<String> compileCommands() {
		return srcSupplier.get().stream().map(this::compileCommand).collect(Collectors.toList());
	}
	
	private String compileCommand(Path src) {
		final Path compiler = CppSourceExt.isCpp(src.extension()) ?
			projectConfig.cppCompiler() : projectConfig.cCompiler();
		
		return StringOperation.join(
			compiler.absolute(),
			defines(),
			includePaths(),
			compileFlags(src),
			"-c",
			src.absolute()
		);
	}
	
	private String cachedDefines = null;
	private String defines() {
		if (cachedDefines == null)
			cachedDefines = StringOperation.join(
				projectConfig.defines().stream().map(define -> "-D" + define).collect(Collectors.toList())
			);
		return cachedDefines;
	}
	
	private String cachedIncludePaths = null;
	private String includePaths() {
		if (cachedIncludePaths == null)
			cachedIncludePaths = StringOperation.join(
				projectConfig.includePaths().stream().map(path -> "-I" + path.absolute()).collect(Collectors.toList())
			);
		return cachedIncludePaths;
	}
	
	private String compileFlags(Path src) {
		return StringOperation.join(projectConfig.compileFlags(src));
	}
	
}
