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

public class ProjStaticLinkCmd {
	
	private final SumProjectConfig projectConfig;
	private final Supplier<List<Path>> srcSupplierFull;
	private final Supplier<List<Path>> srcSupplierNecess;
	private final Path workFolder;
	
	public ProjStaticLinkCmd(SumProjectConfig projectConfig,
	                         Supplier<List<Path>> srcSupplierFull,
	                         Supplier<List<Path>> srcSupplierNecess,
	                         Path workFolder) {
		this.projectConfig = projectConfig;
		this.srcSupplierFull = srcSupplierFull;
		this.srcSupplierNecess = srcSupplierNecess;
		this.workFolder = workFolder;
	}
	
	public Optional<String> linkCommand() {
		if (srcSupplierNecess.get().isEmpty() && artifactPath().exists()) {
			return Optional.empty();
		}
		
		final String file = "tmp.txt"; // TODO
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("-r -s ");
			writer.write(artifactName() + " ");
			writer.write(StringOperation.join(
				srcSupplierFull.get().stream().map(src -> src.baseName() + ".o").collect(Collectors.toList())
			));
		}
		
		return Optional.of(projectConfig.archiver() + " @" + file);
	}
	
	private String artifactName() {
		return projectConfig.outputName() + ".a";
	}
	
	public Path artifactPath() {
		return workFolder.combination(artifactName());
	}
	
}
