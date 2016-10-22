package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.s2gparsing.GlobalConfig;
import com.nghiabui.s2gparsing.ProjectConfig;

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
	
	
	
}
