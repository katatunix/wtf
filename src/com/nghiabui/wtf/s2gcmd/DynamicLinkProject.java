package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.Path;
import com.nghiabui.s2gparsing.GlobalConfig;
import com.nghiabui.s2gparsing.ProjectConfig;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DynamicLinkProject {
	
	private final GlobalConfig globalConfig;
	private final ProjectConfig projectConfig;
	private final Supplier<List<Path>> srcSupplier;
	private final boolean release;
	
	public DynamicLinkProject(GlobalConfig globalConfig,
	                          ProjectConfig projectConfig,
	                          Supplier<List<Path>> srcSupplier,
	                          boolean release) {
		this.globalConfig = globalConfig;
		this.projectConfig = projectConfig;
		this.srcSupplier = srcSupplier;
		this.release = release;
	}
	
	public Optional<String> linkCommand(List<StaticLinkProject> staticProjects) {
		return null;
	}
	
}
