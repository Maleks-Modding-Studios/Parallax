import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.javac.JavacCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavacTest {
	public static void main(String[] args) throws Exception {
		System.out.println(new File("").getAbsolutePath());
		
		File target = new File("src/main/java");
		File output = new File("test/javac/output");
		
		JavacCompiler compiler = new JavacCompiler();
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.addSourceLocation(target.getAbsolutePath());
		configuration.addCompilerCustomArgument("-deprecation", "-verbose");
		configuration.setShowWarnings(true);
		configuration.setDebug(true);
		configuration.setTargetVersion("16");
		configuration.setSourceVersion("1.8");
		configuration.addClasspathEntry("C:\\Users\\Owner\\.gradle\\caches\\modules-2\\files-2.1\\org.lwjgl\\lwjgl-glfw\\3.3.0-SNAPSHOT\\f430b5568899ec5decb49afbc39f6dddd2a7ad40\\lwjgl-glfw-3.3.0-SNAPSHOT.jar");
		configuration.addClasspathEntry("C:\\Users\\Owner\\.gradle\\caches\\modules-2\\files-2.1\\org.lwjgl\\lwjgl\\3.3.0-SNAPSHOT\\2b1c8ae2ff254f102db2546176f2d3d60bed2967\\lwjgl-3.3.0-SNAPSHOT.jar");
		configuration.addClasspathEntry("C:\\Users\\Owner\\.gradle\\caches\\modules-2\\files-2.1\\plexus\\plexus-compiler-javac\\1.5\\9a2dfbfbc3e75bf10af98cbfb93499a88c42b59d\\\\plexus-compiler-javac-1.5.jar");
		configuration.setOutputLocation(output.getAbsolutePath());
		System.out.println("--START JAVAC EXECUTION--");
		List result = compiler.compile(configuration);
		System.out.println("--FINISH JAVAC EXECUTION--");
		System.out.println("--START JAVAC LOG--");
		for (Object o : result) System.out.println(o);
		System.out.println("--FINISH JAVAC LOG--");
	}
	
	public static ArrayList<File> walk(File path, ArrayList<File> list) {
		for (String s : path.list()) {
			File file = new File(path.getAbsolutePath() + "/" + s);
			if (file.isDirectory()) {
				walk(file, list);
			} else {
				list.add(file);
			}
		}
		return list;
	}
}
