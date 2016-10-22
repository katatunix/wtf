package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.AppException;
import com.nghiabui.kommon.CppSourceExt;
import com.nghiabui.kommon.Path;
import com.nghiabui.kommon.SetOperation;
import com.nghiabui.s2gparsing.GlobalConfig;
import com.nghiabui.s2gparsing.ProjectConfig;

import java.util.Optional;
import java.util.Set;

public class SumProjectConfig {
	
	private final GlobalConfig globalConfig;
	private final ProjectConfig projectConfig;
	private final boolean release;
	
	public SumProjectConfig(GlobalConfig globalConfig, ProjectConfig projectConfig, boolean release) {
		this.globalConfig = globalConfig;
		this.projectConfig = projectConfig;
		this.release = release;
	}
	
	public String outputName() {
		return projectConfig.outputName(release);
	}
	
	public Path cCompiler() {
		final Optional<Path> op = globalConfig.cCompiler(release);
		if (!op.isPresent()) throw new AppException("No C compiler is declared");
		return op.get();
	}
	
	public Path cppCompiler() {
		final Optional<Path> op = globalConfig.cppCompiler(release);
		if (!op.isPresent()) throw new AppException("No CPP compiler is declared");
		return op.get();
	}
	
	public Set<String> defines() {
		return SetOperation.union(globalConfig.defines(release), projectConfig.defines(release));
	}
	
	public Set<Path> includePaths() {
		return SetOperation.union(globalConfig.includePaths(release), projectConfig.includePaths(release));
	}
	
	public Set<String> compileFlags(Path src) {
		Set<String> f = cflags();
		if (CppSourceExt.isCpp(src.extension())) {
			f = SetOperation.union(f, cppflags());
		}
		final Set<String> spec = projectConfig.specFlags(release).get(src);
		if (spec != null) {
			f = SetOperation.union(f, spec);
		}
		return f;
	}
	
	private Set<String> cachedCflags = null;
	private Set<String> cflags() {
		if (cachedCflags == null)
			cachedCflags = SetOperation.union(globalConfig.cflags(release), projectConfig.cflags(release));
		return cachedCflags;
	}
	
	private Set<String> cachedCppflags = null;
	private Set<String> cppflags() {
		if (cachedCppflags == null)
			cachedCppflags = SetOperation.union(globalConfig.cppflags(release), projectConfig.cppflags(release));
		return cachedCppflags;
	}
	
	public Path archiver() {
		final Optional<Path> op = globalConfig.archiver(release);
		if (!op.isPresent()) throw new AppException("No archiver is declared");
		return op.get();
	}
	
}
