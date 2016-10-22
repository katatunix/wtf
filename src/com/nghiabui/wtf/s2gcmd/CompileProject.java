package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.*;
import com.nghiabui.s2gparsing.GlobalConfig;
import com.nghiabui.s2gparsing.ProjectConfig;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CompileProject {
	
	private final GlobalConfig globalConfig;
	private final ProjectConfig projectConfig;
	private final Supplier<List<Path>> allSrcSupplier;
	private final Supplier<List<Path>> necessSrcSupplier;
	private final boolean release;
	
	public CompileProject(GlobalConfig globalConfig,
	                      ProjectConfig projectConfig,
	                      Supplier<List<Path>> allSrcSupplier,
	                      Supplier<List<Path>> necessSrcSupplier,
	                      boolean release) {
		this.globalConfig = globalConfig;
		this.projectConfig = projectConfig;
		this.allSrcSupplier = allSrcSupplier;
		this.necessSrcSupplier = necessSrcSupplier;
		this.release = release;
	}
	
	public Optional<List<String>> compileCommands() {
		return ListOperation.mapWithEarlyExit(necessSrcSupplier.get(), this::compileCommand);
	}
	
	private Optional<String> compileCommand(Path srcPath) {
		final Optional<Path> opCompiler = isCppSource(srcPath) ?
			globalConfig.cppCompiler(release) : globalConfig.cCompiler(release);
		
		return opCompiler.map(compiler -> StringOperation.join(
			compiler.absolute(),
			macroString(),
			includePathString(),
			flagString(srcPath),
			"-c",
			srcPath.absolute()
		));
	}
	
	private static boolean isCppSource(Path srcPath) {
		return CppSourceExt.isCpp(srcPath.extension());
	}
	
	//==============================================================================================================
	
	private String cachedMacroString = null;
	private String macroString() {
		if (cachedMacroString == null) {
			cachedMacroString = macroString(
				SetOperation.union(globalConfig.defines(release), projectConfig.defines(release))
			);
		}
		return cachedMacroString;
	}
	
	private static String macroString(Collection<String> macros) {
		return StringOperation.join(
			macros.stream().map(macro -> "-D" + macro).collect(Collectors.toList())
		);
	}
	
	//==============================================================================================================
	
	private String cachedIncludePathString = null;
	private String includePathString() {
		if (cachedIncludePathString == null) {
			cachedIncludePathString = includePathString(
				SetOperation.union(globalConfig.includePaths(release), projectConfig.includePaths(release))
			);
		}
		return cachedIncludePathString;
	}
	
	private static String includePathString(Collection<Path> paths) {
		return StringOperation.join(
			paths.stream().map(path -> "-I" + path.absolute()).collect(Collectors.toList())
		);
	}
	
	//==============================================================================================================
	
	private String flagString(Path srcPath) {
		final Set<String> flags = flags(isCppSource(srcPath));
		final Set<String> specFlags = projectConfig.specFlags(release).get(srcPath);
		final Set<String> total = specFlags == null ? flags : SetOperation.union(flags, specFlags);
		return StringOperation.join(total);
	}
	
	private Set<String> cachedCflags = null;
	private Set<String> cachedCppflags = null;
	private Set<String> flags(boolean isCpp) {
		if (cachedCflags == null) {
			final Set<String> cflags   = SetOperation.union(globalConfig.cflags(release), projectConfig.cflags(release));
			final Set<String> cppflags = SetOperation.union(cflags,
				globalConfig.cppflags(release), projectConfig.cppflags(release));
			cachedCflags    = cflags;
			cachedCppflags  = cppflags;
		}
		return isCpp ? cachedCppflags : cachedCflags;
	}
	
}
