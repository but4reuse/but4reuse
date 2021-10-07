package org.but4reuse.adapters.javajdt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.javajdt.elements.CompilationUnitElement;
import org.but4reuse.adapters.javajdt.elements.FieldElement;
import org.but4reuse.adapters.javajdt.elements.ImportElement;
import org.but4reuse.adapters.javajdt.elements.MethodElement;
import org.but4reuse.adapters.javajdt.elements.PackageElement;
import org.but4reuse.adapters.javajdt.elements.TypeElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * JDTConstructor TODO in progress
 * 
 * @author jabier.martinez
 */
public class JDTConstructor {

	Map<String, File> cunitsFileMap;

	public void construct(File outputFolder, List<IElement> elements) {

		cunitsFileMap = new HashMap<String, File>();

		// First loop to create all compilation unit files and folders
		// also to feed the map of compilation units to files
		for (IElement element : elements) {

			if (element instanceof CompilationUnitElement) {
				CompilationUnitElement cunit = (CompilationUnitElement) element;
				createFile(outputFolder, cunit);
			}

			if (element instanceof PackageElement) {
				PackageElement pe = (PackageElement) element;
				PackageDeclaration p = (PackageDeclaration) pe.node;
				createPackage(outputFolder, p);
			}

			if (element instanceof TypeElement) {
				TypeElement type = (TypeElement) element;
				CompilationUnitElement cunit = getCompilationUnitElement(type);
				createFile(outputFolder, cunit);
			}

			if (element instanceof MethodElement) {
				MethodElement method = (MethodElement) element;
				CompilationUnitElement cunit = getCompilationUnitElement(method);
				createFile(outputFolder, cunit);
			}

			if (element instanceof FieldElement) {
				FieldElement field = (FieldElement) element;
				CompilationUnitElement cunit = getCompilationUnitElement(field);
				createFile(outputFolder, cunit);
			}

		}
		
		// Second loop to create types, packages, imports
		for (IElement element : elements) {
			
			if (element instanceof TypeElement) {
				TypeElement type = (TypeElement) element;
				TypeDeclaration originalType = (TypeDeclaration) type.node;
				CompilationUnitElement cunit = getCompilationUnitElement(type);
				File file = cunitsFileMap.get(cunit.id);
				
				// Prepare the parser
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setSource(FileUtils.getStringOfFile(file).toCharArray());
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setBindingsRecovery(true);

				// Create the AST
				CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				AST ast = cu.getAST();
				TypeDeclaration typeDeclaration = ast.newTypeDeclaration();
				typeDeclaration.setName(ast.newSimpleName(type.name));
				typeDeclaration.setInterface(originalType.isInterface());
				typeDeclaration.setFlags(originalType.getFlags());
				typeDeclaration.setJavadoc(originalType.getJavadoc());
				// TODO modifiers public etc.
				typeDeclaration.modifiers();
				
				cu.types().add(typeDeclaration);
				try {
					FileUtils.writeFile(file, cu.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (element instanceof ImportElement) {
				ImportElement importe = (ImportElement) element;
				ImportDeclaration originalImportD = (ImportDeclaration) importe.node;
				CompilationUnitElement cunit = getCompilationUnitElement(importe);
				File file = cunitsFileMap.get(cunit.id);
				
				// Prepare the parser
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setSource(FileUtils.getStringOfFile(file).toCharArray());
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setBindingsRecovery(true);

				// Create the AST
				CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				AST ast = cu.getAST();
				ImportDeclaration importDeclaration = ast.newImportDeclaration();
				Name name = originalImportD.getName();
				importDeclaration.setName(ast.newName(name.toString().split("[.]")));
				cu.imports().add(importDeclaration);
				try {
					FileUtils.writeFile(file, cu.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public File createPackage(File outputFolder, PackageDeclaration packageDeclaration) {
		if (packageDeclaration != null) {
			String packages = packageDeclaration.getName().toString().replaceAll("[.]", "\\\\");
			File p = new File(outputFolder, packages);
			p.mkdirs();
			return p;
		}
		return outputFolder;
	}

	public File createFileInPackage(File outputFolder, PackageDeclaration packageDeclaration, String fileName) {
		File packageFolder = createPackage(outputFolder, packageDeclaration);
		File f = new File(packageFolder, fileName);
		try {
			FileUtils.createFile(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public CompilationUnitElement getCompilationUnitElement(TypeElement element) {
		List<IDependencyObject> cunits = element.getDependencies().get(JDTParser.DEPENDENCY_COMPILATION_UNIT);
		CompilationUnitElement cunit = (CompilationUnitElement) cunits.get(0);
		return cunit;
	}
	
	public CompilationUnitElement getCompilationUnitElement(ImportElement element) {
		List<IDependencyObject> cunits = element.getDependencies().get(JDTParser.DEPENDENCY_COMPILATION_UNIT);
		CompilationUnitElement cunit = (CompilationUnitElement) cunits.get(0);
		return cunit;
	}

	public CompilationUnitElement getCompilationUnitElement(MethodElement element) {
		List<IDependencyObject> types = element.getDependencies().get(JDTParser.DEPENDENCY_TYPE);
		TypeElement type = (TypeElement) types.get(0);
		return getCompilationUnitElement(type);
	}

	public CompilationUnitElement getCompilationUnitElement(FieldElement element) {
		List<IDependencyObject> types = element.getDependencies().get(JDTParser.DEPENDENCY_TYPE);
		TypeElement type = (TypeElement) types.get(0);
		return getCompilationUnitElement(type);
	}

	public void createFile(File outputFolder, CompilationUnitElement compilationUnitElement) {
		CompilationUnit cu = (CompilationUnit) compilationUnitElement.node;
		File file = createFileInPackage(outputFolder, cu.getPackage(), compilationUnitElement.name);
		cunitsFileMap.put(compilationUnitElement.id, file);
	}

}
