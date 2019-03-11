package org.but4reuse.adapters.javajdt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.javajdt.elements.CompilationUnitElement;
import org.but4reuse.adapters.javajdt.elements.FieldElement;
import org.but4reuse.adapters.javajdt.elements.ImportElement;
import org.but4reuse.adapters.javajdt.elements.JDTElement;
import org.but4reuse.adapters.javajdt.elements.MethodBodyElement;
import org.but4reuse.adapters.javajdt.elements.MethodElement;
import org.but4reuse.adapters.javajdt.elements.PackageElement;
import org.but4reuse.adapters.javajdt.elements.TypeElement;
import org.but4reuse.adapters.javajdt.elements.TypeExtendsElement;
import org.but4reuse.adapters.javajdt.elements.TypeImplementsElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * The parser of java based on JDT
 * 
 * @author jabier.martinez
 */
public class JDTParser {

	// Hashmaps
	Map<String, JDTElement> packagesMap;
	Map<String, JDTElement> typesMap;
	Map<String, JDTElement> methodsMap;

	/**
	 * Parse the java files
	 * 
	 * @param javaFiles
	 * @return list of elements
	 */
	public List<IElement> parse(List<File> javaFiles) {

		// Hashmaps
		packagesMap = new HashMap<String, JDTElement>();
		typesMap = new HashMap<String, JDTElement>();
		methodsMap = new HashMap<String, JDTElement>();

		// List of elements to return
		List<IElement> elements = new ArrayList<IElement>();

		// Parse the java files one by one
		for (File file : javaFiles) {

			// Prepare the parser
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(FileUtils.getStringOfFile(file).toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setBindingsRecovery(true);

			// Create the AST
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			JDTElement currentCompilationUnitElement = new CompilationUnitElement();
			currentCompilationUnitElement.node = cu;
			currentCompilationUnitElement.name = file.getName();
			currentCompilationUnitElement.id = IdUtils.getId(cu.getPackage()) + " " + file.getName();
			elements.add(currentCompilationUnitElement);

			// Visit the result to add elements
			cu.accept(new ASTVisitor() {
				JDTElement currentTypeElement = null;
				JDTElement currentPackageElement = null;

				// Packages
				public boolean visit(PackageDeclaration node) {
					String id = IdUtils.getId(node);
					// Check if we already have it, otherwise create it
					JDTElement element = packagesMap.get(id);
					if (element == null) {
						element = new PackageElement();
						element.node = node;
						element.name = node.getName().getFullyQualifiedName();
						element.id = id;
						packagesMap.put(id, element);
						elements.add(element);
					}
					currentPackageElement = element;
					return true;
				}

				// Types (Classes, Interfaces etc.)
				public boolean visit(TypeDeclaration node) {
					JDTElement element = new TypeElement();
					element.node = node;
					element.name = node.getName().getIdentifier();
					StringBuffer qname = new StringBuffer();
					if (currentPackageElement != null) {
						qname.append(currentPackageElement.id);
						qname.append(".");
					}
					qname.append(element.name);
					element.id = qname.toString();
					elements.add(element);
					currentTypeElement = element;

					typesMap.put(element.id, element);

					// Add dependency to the Package
					element.addDependency("package", currentPackageElement);

					// Add dependency to compilation unit
					element.addDependency("compilationUnit", currentCompilationUnitElement);
					return true;
				}

				// Methods
				public boolean visit(MethodDeclaration node) {
					if(currentTypeElement == null) {
						// This can happen for example with Java enum methods
						// TODO Java enum methods not supported. https://stackoverflow.com/questions/18883646/java-enum-methods
						return false;
					}
					JDTElement element = new MethodElement();
					element.node = node;
					element.name = node.getName().getIdentifier();
					StringBuffer qname = new StringBuffer();
					qname.append(currentTypeElement.id);
					qname.append(".");
					qname.append(element.name);
					// add parameters to signature
					qname.append("(");
					List<?> parameters = node.parameters();
					for (Object parameter : parameters) {
						if (parameter instanceof SingleVariableDeclaration) {
							qname.append(((SingleVariableDeclaration) parameter).getType());
							qname.append(",");
						}
					}
					// remove last comma
					if (!parameters.isEmpty()) {
						qname.setLength(qname.length() - 1);
					}
					qname.append(")");

					element.id = qname.toString();
					elements.add(element);

					methodsMap.put(element.id, element);

					// add dependency to the Type
					element.addDependency("type", currentTypeElement);
					
					// Adding MethodBodyElement
					MethodBodyElement methodBodyElement = new MethodBodyElement();
					methodBodyElement.body = element.node.toString();
					methodBodyElement.addDependency("methodBody", element);
					elements.add(methodBodyElement);
					return true;
				}

				// Imports
				public boolean visit(ImportDeclaration node) {
					JDTElement element = new ImportElement();
					element.node = node;
					element.name = node.getName().getFullyQualifiedName();
					element.id = currentCompilationUnitElement.id + " " + element.name;
					elements.add(element);

					// add dependency to the compilation unit
					element.addDependency("compilationUnit", currentCompilationUnitElement);
					return true;
				}

				// Fields
				public boolean visit(VariableDeclarationFragment node) {
					// it is a global variable
					if (node.getParent().getParent() instanceof TypeDeclaration) {
						JDTElement element = new FieldElement();
						element.node = node;
						element.name = node.getName().getFullyQualifiedName();
						element.id = currentTypeElement.id + " " + element.name;
						elements.add(element);

						// add dependency to the Type
						element.addDependency("type", currentTypeElement);
					}
					return true;
				}

			});

		}

		// Now that we have all the info of elements
		// Adding the dependencies and dependency-related elements
		List<IElement> newElements = new ArrayList<IElement>();
		
		for (IElement element : elements) {
			// Imports
			if (element instanceof ImportElement) {
				JDTElement imported = typesMap.get(((ImportElement) element).name);
				if (imported != null) {
					((AbstractElement) element).addDependency("importedType", imported);
				}
			}

			// Extends and Implements
			if (element instanceof TypeElement) {
				TypeElement typeElement = (TypeElement) element;

				// extends
				TypeElement superTypeElement = getSuperTypeElement(typeElement);
				if (superTypeElement != null) {
					TypeExtendsElement typeExtendsElement = new TypeExtendsElement();
					typeExtendsElement.type = typeElement;
					typeExtendsElement.extendedType = superTypeElement;
					typeExtendsElement.addDependency("extendedType", superTypeElement);
					typeExtendsElement.addDependency("extends", typeElement);
					newElements.add(typeExtendsElement);
				}

				// implements
				for (TypeElement interfaceTE : getSuperInterfaceTypeElements(typeElement)) {
					TypeImplementsElement typeImplementsElement = new TypeImplementsElement();
					typeImplementsElement.type = typeElement;
					typeImplementsElement.implementedType = interfaceTE;
					typeImplementsElement.addDependency("implementedType", interfaceTE);
					typeImplementsElement.addDependency("implements", typeElement);
					newElements.add(typeImplementsElement);
				}
			}
		}
		
		elements.addAll(newElements);

		// Add more dependencies here
		// TODO MethodElement -> TypeElement: based on the types of the method
		// parameters
		// element.addDependency("parameter",otherElement)
		// TODO MethodElement -> MethodElement: based on a call from one method
		// to another
		// element.addDependency("calls",otherElement)

		return elements;
	}

	/**
	 * Get list of implements type elements
	 * 
	 * @param typeElement
	 * @return list of implements type elements
	 */
	public List<TypeElement> getSuperInterfaceTypeElements(TypeElement typeElement) {
		List<TypeElement> listInterfaces = new ArrayList<TypeElement>();
		List<?> interfaceTypes = ((TypeDeclaration) typeElement.node).superInterfaceTypes();

		if (interfaceTypes == null || interfaceTypes.isEmpty()) {
			return listInterfaces;
		}

		for (Object o : interfaceTypes) {
			if (o instanceof Type) {
				Type type = (Type) o;
				TypeElement te = getReferencedTypeElementFromATypeElement(type, typeElement);
				if (te != null) {
					listInterfaces.add(te);
				}
			}
		}
		return listInterfaces;
	}

	/**
	 * Get super type element
	 * 
	 * @param typeElement
	 * @return super type element
	 */
	public TypeElement getSuperTypeElement(TypeElement typeElement) {
		Type superType = ((TypeDeclaration) typeElement.node).getSuperclassType();
		if (superType == null) {
			return null;
		}
		return getReferencedTypeElementFromATypeElement(superType, typeElement);
	}

	/**
	 * Method to know the type element behind a type when it is referenced
	 * inside a type element
	 * 
	 * @param referencedType
	 * @param fromTypeElement
	 * @return
	 */
	public TypeElement getReferencedTypeElementFromATypeElement(Type referencedType, TypeElement fromTypeElement) {
		JDTElement typeElementToReturn = null;
		// using directly a qualified name
		if (referencedType.toString().contains(".")) {
			typeElementToReturn = typesMap.get(referencedType.toString());
		} else {
			// check imports
			CompilationUnit cu = (CompilationUnit) fromTypeElement.node.getRoot();
			for (Object imp : cu.imports()) {
				if (((ImportDeclaration) imp).getName().getFullyQualifiedName()
						.endsWith("." + referencedType.toString())) {
					// found
					typeElementToReturn = typesMap.get(((ImportDeclaration) imp).getName().getFullyQualifiedName());
					break;
				}
			}
		}

		if (typeElementToReturn == null) {
			// try in the same package
			// getPackage, will be only one
			List<IDependencyObject> packages = fromTypeElement.getDependencies().get("package");
			if (packages != null && !packages.isEmpty()) {
				PackageElement epackage = (PackageElement) packages.get(0);
				// get all types within the same package
				List<IDependencyObject> types = epackage.getDependants().get("package");
				for (IDependencyObject type : types) {
					if (type instanceof TypeElement) {
						if (type != fromTypeElement) {
							typeElementToReturn = typesMap.get(((TypeElement) type).id);
							if (typeElementToReturn != null) {
								return (TypeElement) typeElementToReturn;
							}
						}
					}
				}
			}
		}

		if (typeElementToReturn != null) {
			return (TypeElement) typeElementToReturn;
		}
		return null;
	}

}
