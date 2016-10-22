package com.nghiabui.wtf.common;

import com.nghiabui.kommon.Path;

import java.util.Optional;

public interface Command {
	
	Optional<Path> workFolder();
	Path program();
	String argString();
	String fullString();
	
}
