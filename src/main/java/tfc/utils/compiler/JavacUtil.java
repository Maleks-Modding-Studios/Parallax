package tfc.utils.compiler;

import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.javac.JavacCompiler;

import java.io.File;
import java.util.List;

public class JavacUtil {
	private final JavacCompiler compiler = new JavacCompiler();
	
	public JavacUtil() {
	}
	
	public void compile(CompilerConfiguration configuration) throws CompilerException {
		System.out.println("--START JAVAC EXECUTION--");
		List result = compiler.compile(configuration);
		System.out.println("--FINISH JAVAC EXECUTION--");
		if (!result.isEmpty()) {
			System.out.println();
			System.out.println("--START JAVAC LOG--");
			for (Object o : result) System.out.println(o);
			System.out.println("--FINISH JAVAC LOG--");
		}
	}
	
	public void compile(File src, File dst, String[] dependencies, String srcVersion, String targetVersion) throws CompilerException {
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.addSourceLocation(src.getAbsolutePath());
		configuration.addCompilerCustomArgument("-deprecation", "-verbose");
		configuration.setShowWarnings(true);
		configuration.setDebug(true);
		configuration.setTargetVersion(srcVersion);
		configuration.setSourceVersion(targetVersion);
		for (String dependency : dependencies) configuration.addClasspathEntry(dependency);
		configuration.setOutputLocation(dst.getAbsolutePath());
		compile(configuration);
	}
}
