package com.nghiabui.wtf.s2gcmd;

import com.nghiabui.kommon.Path;
import com.nghiabui.kommon.StringOperation;
import com.nghiabui.kommon.io.FileWriter;
import com.nghiabui.s2gparsing.GlobalConfig;
import com.nghiabui.s2gparsing.ProjectConfig;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class StaticLinkProject {
	
	private final GlobalConfig globalConfig;
	private final ProjectConfig projectConfig;
	private final Supplier<List<Path>> srcSupplier;
	private final boolean release;
	private final Path workFolder;
	
	public StaticLinkProject(GlobalConfig globalConfig,
	                         ProjectConfig projectConfig,
	                         Supplier<List<Path>> srcSupplier,
	                         boolean release,
	                         Path workFolder) {
		this.globalConfig = globalConfig;
		this.projectConfig = projectConfig;
		this.srcSupplier = srcSupplier;
		this.release = release;
		this.workFolder = workFolder;
	}
	
	public Optional<String> linkCommand() {
		final List<Path> sources = srcSupplier.get();
		if (sources.isEmpty()) return Optional.empty();
		
		final String file = "test.txt"; // TODO
		
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("-r -s ");
			writer.write(artifactName() + " ");
			writer.write(StringOperation.join(
				sources.stream().map(src -> src.baseName() + ".o").collect(Collectors.toList())
			));
		}
		
		return Optional.of(globalConfig.archiver(release) + " @" + file);
	}
	
	private String artifactName() {
		return projectConfig.outputName(release) + ".a";
	}
	
	public Path artifactPath() {
		return workFolder.combination(artifactName());
	}
	
}
