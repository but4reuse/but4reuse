package org.but4reuse.adapters.bytecode;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Class that regroups all elements to recreate a bytecode file
 */

public class JClass {
	
	/**
	 * The list of fields
	 */
	
	private List<FieldNode> fields;
	
	/**
	 * The list of methods
	 */
	
	private List<MethodNode> methods;
	
	/**
	 * The list of interface names
	 */
	
	private List<String> interfaces;
	
	/**
	 * The name of superclass
	 */
	
	private String superClassName;
	
	/**
	 * the class name
	 */
	
	private String className;
	
	/**
	 * The class access flag
	 */
	
	private int accessFlag;
	
	/**
	 * List of instructions for static fields initialization
	 */
	
	private ArrayList<AbstractInsnNode> instructions;
	
	/**
	 * Construct a new JClass with the class name
	 * @param className The name of the class
	 */
	
	public JClass(String className){
		fields=new ArrayList<FieldNode>();
		methods=new ArrayList<MethodNode>();
		interfaces=new ArrayList<String>();
		this.className=className;
	}
	
	/**
	 * Return the list of fields
	 * @return The list of fields
	 */
	
	public List<FieldNode> getFields() {
		return fields;
	}
	
	/**
	 * Add a field to the field's list 
	 * @param field The field
	 */
	
	public void addField(FieldElement field) {
		this.fields.add(field.getField());
	}
	
	/**
	 * Return the list of methods
	 * @return The list of methods
	 */
	
	public List<MethodNode> getMethods() {
		return methods;
	}
	
	/**
	 * Add a method to the list of methods
	 * @param method The method
	 */
	
	public void addMethod(MethodElement method) {
		this.methods.add(method.getMethod());
	}
	
	/**
	 * Return the list of interfaces
	 * @return The list of interfaces
	 */
	
	public List<String> getInterfaces() {
		return interfaces/*.toArray(new String[interfaces.size()])*/;
	}
	
	/**
	 * Add an interface to the interface's list
	 * @param interfaceName The interface's name
	 */
	
	public void addInterface(String interfaceName) {
		this.interfaces.add(interfaceName);
	}
	
	/**
	 * Return the name of the class
	 * @return The name of the class
	 */
	
	public String getClassName() {
		return className;
	}
	
	/**
	 * Return the access flag
	 * @return The access flag
	 */
	
	public int getAccessFlags() {
		return accessFlag;
	}
	
	/**
	 * Setter for the accessFlag
	 * @param accessFlags The access flag
	 */
	
	public void setAccessFlags(int accessFlags) {
		this.accessFlag = accessFlags;
	}
	
	/**
	 * Return the name of the superclass
	 * @return The name of the superclass
	 */
	
	public String getSuperClassName() {
		return superClassName;
	}
	
	/**
	 * Setter for the name of the superclass
	 * @param superClassName The name of the superclass
	 */
	
	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	/**
	 * Return the list of instructions
	 * @return The instructions
	 */
	public ArrayList<AbstractInsnNode> getInstructions() {
		return instructions;
	}

	/**
	 * Setter for the instructions list
	 * @param instructions The instructions to set
	 */
	public void setInstructions(ArrayList<AbstractInsnNode> instructions) {
		this.instructions = instructions;
	}
}
