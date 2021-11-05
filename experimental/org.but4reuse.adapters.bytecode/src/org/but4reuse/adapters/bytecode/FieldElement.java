package org.but4reuse.adapters.bytecode;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * An AbstractElement that represents a class field
 */

public class FieldElement extends AbstractElement{
	
	/**
	 * The field's data
	 */
	
	private FieldNode field;
	
	/**
	 * The field's static instructions
	 */
	
	private ArrayList<AbstractInsnNode> instructions;
	
	/**
	 * Class name that owns this field
	 */
	
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof FieldElement){
			FieldElement tmp=(FieldElement)anotherElement;
			if(className.equals(tmp.getClassName())&&BytecodeUtils.fieldComparator(field, tmp.getField())){
				if(instructions.size()!=tmp.instructions.size())
					return 0;
				else{
					for(int i=0; i<instructions.size(); i++){
						if(!BytecodeUtils.instructionComparator(instructions.get(i), tmp.instructions.get(i)))
							return 0;
					}
				}
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Field-"+field.name;
	}

	/**
	 * Constructs a new FieldElement
	 * @param field The field
	 * @param className The class name
	 */
	
	public FieldElement(FieldNode field, String className) {
		super();
		this.field = field;
		this.className = className;
	}

	/**
	 * Return the field's data
	 * @return The field
	 */
	
	public FieldNode getField() {
		return field;
	}

	/**
	 * Return the owner class name
	 * @return The name of the class
	 */
	
	public String getClassName() {
		return className;
	}

	/**
	 * Return the instructions
	 * @return The instructions
	 */
	public ArrayList<AbstractInsnNode> getInstructions() {
		return instructions;
	}

	/**
	 * Setter for instructions
	 * @param instructions The instructions to set
	 */
	public void setInstructions(ArrayList<AbstractInsnNode> instructions) {
		this.instructions = instructions;
	}
}
