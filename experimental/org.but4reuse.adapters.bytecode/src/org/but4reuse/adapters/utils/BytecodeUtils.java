package org.but4reuse.adapters.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.bytecode.AccessFlagsElement;
import org.but4reuse.adapters.bytecode.FieldElement;
import org.but4reuse.adapters.bytecode.InterfaceNameElement;
import org.but4reuse.adapters.bytecode.MethodElement;
import org.but4reuse.adapters.bytecode.NameDependencyObject;
import org.but4reuse.adapters.bytecode.SuperClassNameElement;
import org.but4reuse.utils.files.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Bytecode utils
 *
 */

public class BytecodeUtils {
	
	/**
	 * Bytecode's magic number
	 */
	
	public static final byte []magicNumber={(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
	
	/**
	 * Check if a file is a bytecode based on the magic number (CAFEBABE)
	 * 
	 * @param file The file
	 * @return true if it is a bytecode
	 */
	
	public static boolean isBytecodeFile(File file) {
		if(file==null||!file.exists())
			return false;
		BufferedInputStream bis=null;
		byte []buf=new byte[4];
		try {
			bis=new BufferedInputStream(new FileInputStream(file));
			bis.read(buf, 0, 4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return Arrays.equals(buf, magicNumber);
	}

	/**
	 * Check if a file is a bytecode or if a directory contains a bytecode file based on the extension and the magic number using isBytecodeFile
	 * 
	 * @param file The file
	 * @param name The file name
	 * @return whether it is found or not
	 */
	
	public static boolean containsBytecodeFile(File file, String name){
		if(file.isDirectory()){
			for (File child : file.listFiles()) {
				if(containsBytecodeFile(child, child.getName())){
					return true;
				}
			}
			return false;
		}else{
			return name.endsWith(".class")&&isBytecodeFile(file);
		}
	}
	
	/**
	 * Read all bytecode from a given uri and return the list of all elements
	 * @param uri The uri
	 * @return list of IElement
	 */
	
	public static List<IElement> getArtefactElements(URI uri) {
		List<IElement> listElem=new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		List<File> listF=FileUtils.getAllFiles(file);
		for(File f:listF){
			if(isBytecodeFile(f)){
				try {
					ClassReader cr=new ClassReader(new FileInputStream(f));
					ClassNode cn=new ClassNode();
					cr.accept(cn, ClassReader.EXPAND_FRAMES);
					String className=cn.name;
					Map<String, ArrayList<AbstractInsnNode>> fieldInsn=new HashMap<String, ArrayList<AbstractInsnNode>>();
					for(MethodNode mn:cn.methods){
						if(mn.name.equals("<clinit>")){
							InsnList il=mn.instructions;
							ArrayList<AbstractInsnNode> fil=null;
							String fname=null;
							/*for(int i=0; i<il.size(); i++){
								AbstractInsnNode ab=il.get(i);
								if(ab.getOpcode()==-1&&ab.getType()==8){
									if(fil!=null&&fname!=null){
										fieldInsn.put(fname, fil);
										fname=null;
									}
									fil=new ArrayList<AbstractInsnNode>();
									fil.add(insnClone(ab));
								}else if(ab.getOpcode()==177&&ab.getType()==0){
									fieldInsn.put(fname, fil);
								}else{
									fil.add(insnClone(ab));
									if(ab instanceof FieldInsnNode)
										fname=((FieldInsnNode)ab).name;
								}							
							}*/
						}else{
							MethodElement me=new MethodElement(mn, className);
							me.addDependency(new NameDependencyObject(me.getText()));
	// 
	//						List<String> requiredList=new ArrayList<String>();
	//						InsnList instructions=mn.instructions;
	//						for(int i=0; i<instructions.size(); i++){
	//							AbstractInsnNode ai=instructions.get(i);
	//							if(ai instanceof FieldInsnNode){
	//								FieldInsnNode fi=(FieldInsnNode)ai;
	//								if(fi.owner.equals(className)){
	//									String s=className+"-Field-"+fi.name;
	//									if(!requiredList.contains(s)){
	//										requiredList.add(s);
	//									}
	//									//me.addDependency(new RequiredDependencyObject(className, fi.name));
	//								}
	//							}else if(ai instanceof MethodInsnNode){
	//								MethodInsnNode mi=(MethodInsnNode)ai;
	//								if(mi.owner.equals(className)){
	//									String s=className+"-Method-"+mi.name+"-"+mi.desc;
	//									if(!requiredList.contains(s)){
	//										requiredList.add(s);
	//									}
	//									//me.addDependency(new RequiredDependencyObject(className, mi.name, mi.desc));
	//								}
	//							}
	//						}
	//						for(String s:requiredList){
	//							System.out.println(mn.name+" "+mn.desc+"   "+s);
	//							me.addDependency(new RequiredDependencyObject(s, mn.name+" "+mn.desc));
	//						}
							listElem.add(me);
						}
					}
					
					for(FieldNode fn:cn.fields){
						FieldElement fe=new FieldElement(fn, className);
						fe.addDependency(new NameDependencyObject(fe.getText()));
						ArrayList<AbstractInsnNode> il=fieldInsn.get(fn.name);
						if(il!=null)
							fe.setInstructions(il);
						else
							fe.setInstructions(new ArrayList<AbstractInsnNode>());
						listElem.add(fe);
					}
					for(String i:cn.interfaces){
						InterfaceNameElement ine=new InterfaceNameElement(i, className);
						ine.addDependency(new NameDependencyObject(ine.getText()));
						listElem.add(ine);
					}
					SuperClassNameElement scne=new SuperClassNameElement(cn.superName, className);
					scne.addDependency(new NameDependencyObject(scne.getText()));
					listElem.add(scne);
					AccessFlagsElement afe=new AccessFlagsElement(cn.access, className);
					listElem.add(afe);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		return listElem;
	}
	
	/**
	 * Field comparator
	 * @param f1 The first field
	 * @param f2 The second field
	 * @return return true if two fields are equals
	 */
	
	public static boolean fieldComparator(FieldNode f1, FieldNode f2){
		return f1.name.equals(f2.name)&&f1.desc.equals(f2.desc)&&f1.access==f2.access;
	}
	
	/**
	 * Instruction comparator
	 * @param ai1 The first instruction
	 * @param ai2 The second instruction
	 * @return return true if two instructions are equals
	 */
	
	public static boolean instructionComparator(AbstractInsnNode ai1, AbstractInsnNode ai2){
		if(ai1.getOpcode()==ai2.getOpcode()&&ai1.getType()==ai2.getType()){
			if(ai1 instanceof FieldInsnNode){
				FieldInsnNode i1=(FieldInsnNode)ai1, i2=(FieldInsnNode)ai2;
				return i1.owner.equals(i2.owner)&&i1.name.equals(i2.name)&&i1.desc.equals(i2.desc);
			}else if(ai1 instanceof LdcInsnNode){
				LdcInsnNode i1=(LdcInsnNode)ai1, i2= (LdcInsnNode)ai2;
				return i1.cst.equals(i2.cst);
			}else if(ai1 instanceof IntInsnNode){
				IntInsnNode i1=(IntInsnNode)ai1, i2= (IntInsnNode)ai2;
				return i1.operand==i2.operand;
			}else if(ai1 instanceof VarInsnNode){
				VarInsnNode i1=(VarInsnNode)ai1, i2= (VarInsnNode)ai2;
				return i1.var==i2.var;
			}else if(ai1 instanceof TypeInsnNode){
				TypeInsnNode i1=(TypeInsnNode)ai1, i2= (TypeInsnNode)ai2;
				return i1.desc.equals(i2.desc);
			}else if(ai1 instanceof InvokeDynamicInsnNode){
				InvokeDynamicInsnNode i1=(InvokeDynamicInsnNode)ai1, i2= (InvokeDynamicInsnNode)ai2;
				return i1.name.equals(i2.name)&&i1.desc.equals(i2.desc);
			}else if(ai1 instanceof JumpInsnNode){
				/*JumpInsnNode i1=(JumpInsnNode)ai1, i2= (JumpInsnNode)ai2;
				return i1.label.getLabel().getOffset()==i2.label.getLabel().getOffset();*/
			}else if(ai1 instanceof LabelNode){
				/*LabelNode i1=(LabelNode)ai1, i2= (LabelNode)ai2;
				if(i1.getLabel()!=null&&i2.getLabel()!=null)
					return i1.getLabel().getOffset()==i2.getLabel().getOffset();*/
			}else if(ai1 instanceof IincInsnNode){
				IincInsnNode i1=(IincInsnNode)ai1, i2=(IincInsnNode)ai2;
				return i1.incr==i2.incr&&i1.var==i2.var;
			}else if(ai1 instanceof LineNumberNode){
				/*LineNumberNode i1=(LineNumberNode)ai1, i2=(LineNumberNode)ai2;
				return i1.line==i2.line&&i1.start.getLabel().getOffset()==i2.start.getLabel().getOffset();*/
			}else if(ai1 instanceof MethodInsnNode){
				MethodInsnNode i1=(MethodInsnNode)ai1, i2=(MethodInsnNode)ai2;
				return i1.owner.equals(i2.owner)&&i1.name.equals(i2.name)&&i1.desc.equals(i2.desc)&&i1.itf==i2.itf;
			}else if(ai1 instanceof MultiANewArrayInsnNode){
				MultiANewArrayInsnNode i1=(MultiANewArrayInsnNode)ai1, i2=(MultiANewArrayInsnNode)ai2;
				return i1.desc.equals(i2.desc)&&i1.dims==i2.dims;
			}else if(ai1 instanceof LookupSwitchInsnNode){
				LookupSwitchInsnNode i1=(LookupSwitchInsnNode)ai1, i2=(LookupSwitchInsnNode)ai2;
				boolean ret=/*i1.dflt.getLabel().getOffset()==i2.dflt.getLabel().getOffset()&&*/i1.keys.size()==i2.keys.size()&&i1.labels.size()==i2.labels.size();
				int i=0;
				/*while (i<i1.labels.size()&&ret) {
					ret&=i1.labels.get(i).getLabel().getOffset()==i2.labels.get(i).getLabel().getOffset();
					i++;
				}
				i=0;*/
				while (i<i1.keys.size()&&ret) {
					ret&=i1.keys.get(i)==i2.keys.get(i);
					i++;
				}
				return ret;
				
			}else if(ai1 instanceof TableSwitchInsnNode){
				TableSwitchInsnNode i1=(TableSwitchInsnNode)ai1, i2=(TableSwitchInsnNode)ai2;
				boolean ret=/*i1.dflt.getLabel().getOffset()==i2.dflt.getLabel().getOffset()&&*/i1.labels.size()==i2.labels.size()&&i1.max==i2.max&&i1.min==i2.min;
				/*int i=0;
				while (i<i1.labels.size()&&ret) {
					ret&=i1.labels.get(i).getLabel().getOffset()==i2.labels.get(i).getLabel().getOffset();
					i++;
				}*/
				return ret;
				
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Method comparator
	 * @param m1 The first method
	 * @param m2 The second method
	 * @return return true if two methods are equals
	 */
	
	public static boolean methodComparator(MethodNode m1, MethodNode m2){
		if(m1.name.equals(m2.name)&&m1.desc.equals(m2.desc)&&m1.access==m2.access){//&&m1.instructions.size()==m2.instructions.size()
			/*for(int i=0; i<m1.instructions.size(); i++){
				if(!instructionComparator(m1.instructions.get(i), m2.instructions.get(i)))
					return false;
			}*/
			return true;
		}
		return false;
	}
	
	/**
	 * Clone an AbstractInsnNode
	 * @param insn The given AbstractInsnNode 
	 * @return The clone of insn
	 */
	
	public static AbstractInsnNode insnClone(AbstractInsnNode insn){
		AbstractInsnNode ret=null;
		if(insn instanceof InsnNode){
			InsnNode i=(InsnNode)insn;
			ret=new InsnNode(i.getOpcode());
		}else if(insn instanceof FieldInsnNode){
			FieldInsnNode i=(FieldInsnNode)insn;
			ret=new FieldInsnNode(i.getOpcode(), i.owner, i.name, i.desc);
		}else if(insn instanceof LdcInsnNode){
			LdcInsnNode i=(LdcInsnNode)insn;
			ret=new LdcInsnNode(i.cst);
		}else if(insn instanceof IntInsnNode){
			IntInsnNode i=(IntInsnNode)insn;
			ret=new IntInsnNode(i.getOpcode(), i.operand);
		}else if(insn instanceof VarInsnNode){
			VarInsnNode i=(VarInsnNode)insn;
			ret=new VarInsnNode(i.getOpcode(), i.var);
		}else if(insn instanceof TypeInsnNode){
			TypeInsnNode i=(TypeInsnNode)insn;
			ret=new TypeInsnNode(i.getOpcode(), i.desc);
		}else if(insn instanceof InvokeDynamicInsnNode){
			InvokeDynamicInsnNode i=(InvokeDynamicInsnNode)insn;
			ret=new InvokeDynamicInsnNode(i.name, i.desc, i.bsm, i.bsmArgs);
		}else if(insn instanceof JumpInsnNode){
			JumpInsnNode i=(JumpInsnNode)insn;
			ret=new JumpInsnNode(i.getOpcode(), i.label);
		}else if(insn instanceof LabelNode){
			LabelNode i=(LabelNode)insn;
			ret=new LabelNode(i.getLabel());
		}else if(insn instanceof IincInsnNode){
			IincInsnNode i=(IincInsnNode)insn;
			ret=new IincInsnNode(i.var, i.incr);
		}else if(insn instanceof LineNumberNode){
			LineNumberNode i=(LineNumberNode)insn;
			ret=new LineNumberNode(i.line, i.start);
		}else if(insn instanceof MethodInsnNode){
			MethodInsnNode i=(MethodInsnNode)insn;
			ret=new MethodInsnNode(i.getOpcode(), i.owner, i.name, i.desc, i.itf);
		}else if(insn instanceof MultiANewArrayInsnNode){
			MultiANewArrayInsnNode i=(MultiANewArrayInsnNode)insn;
			ret=new MultiANewArrayInsnNode(i.desc, i.dims);
		}else if(insn instanceof LookupSwitchInsnNode){
			LookupSwitchInsnNode i=(LookupSwitchInsnNode)insn;
			int []keys=new int[i.keys.size()];
			int cpt=0;
			for(int j:i.keys)
				keys[cpt++]=j;
			ret=new LookupSwitchInsnNode(i.dflt, keys, (LabelNode[]) i.labels.toArray());
		}else if(insn instanceof TableSwitchInsnNode){
			TableSwitchInsnNode i=(TableSwitchInsnNode)insn;
			ret=new TableSwitchInsnNode(i.min, i.max, i.dflt, (LabelNode[]) i.labels.toArray());
		}else if(insn instanceof FrameNode){
			FrameNode i=(FrameNode)insn;
			ret=new FrameNode(i.type, i.local.size(), i.local.toArray(), i.stack.size(), i.stack.toArray());
		}
		return ret;
	}
}
