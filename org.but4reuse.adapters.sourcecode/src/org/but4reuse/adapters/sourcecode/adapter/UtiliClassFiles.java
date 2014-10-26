package org.but4reuse.adapters.sourcecode.adapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class UtiliClassFiles {

    private String initialpath = "";
    private Boolean recursivePath = true;
    public int filecount = 0;
    public int dircount = 0;
    private String extension=null;

/**
 * Constructeur
 * @param path chemin du rpertoire
 * @param subFolder analyse des sous dossiers
 */
    public UtiliClassFiles(String path, Boolean subFolder) {
        super();
        this.initialpath = path;
        this.recursivePath = subFolder;
           }

   

    public UtiliClassFiles(String path, Boolean subFolder, String string) {
	// TODO Auto-generated constructor stub
    		super();
        this.initialpath = path;
        this.recursivePath = subFolder;
        this.extension =string; 
}


    
    public ArrayList<String> listFiles(ArrayList<String> files, File dir) {
        if (files == null) {
            files = new ArrayList<String>();
        }

        if (!dir.isDirectory()) {
            files.add(dir.toString());
            return files;
        }

        for (File file : dir.listFiles()) {
            listFiles(files, file);
        }
        return files;
    }


	public ArrayList<String> listeFiles(String dir) {
        ArrayList<String> classes = new ArrayList<String>();
    	File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
             
            
            	
            	
            	  if (files[i].isDirectory() == true) {
                 
                    //System.out.println("Dossier" + files[i].getAbsolutePath());
                    this.dircount++;
                    
                } else {
                    System.out.println("Fichier" + files[i].getAbsolutePath());
                   boolean test = false;
                   if (extension==null)
                	      test = LanguageConfigurator.getLanguage().isALanguageProgram(files[i].getAbsolutePath());
                	   else  test = files[i].getAbsolutePath().endsWith(extension);
                	    	  
                	    	  if (test	) {
                    		
                    classes.add(files[i].getAbsolutePath());
                    this.filecount++;
                        }
                if (files[i].isDirectory() == true && this.recursivePath == true) {
                	System.out.println("REPERTOIRE :"+files[i].getName());
                    classes.addAll(this.listeFiles(files[i].getAbsolutePath()));
                }
            }
        }
        }
       // System.out.println("ZISE before :"+classes.size());
        return classes;
    }

    
	public ArrayList<String> listeFilesName(String dir) {
        ArrayList<String> classes = new ArrayList<String>();
    	File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
             
            
            	
            	
            	  if (files[i].isDirectory() == true) {
                 
                    //System.out.println("Dossier" + files[i].getAbsolutePath());
                    this.dircount++;
                    
                } else {
                    //System.out.println("Fichier" + files[i].getAbsolutePath());
                   boolean test = false;
                   if (extension==null){
                	      test = LanguageConfigurator.getLanguage().isALanguageProgram(files[i].getName());
                   }
                	   else  test = files[i].getAbsolutePath().endsWith(extension);
                	    	  
                	    	  if (test	) {
                    		
                    classes.add(files[i].getName());
                    this.filecount++;
                        }
                if (files[i].isDirectory() == true && this.recursivePath == true) {
                	System.out.println("REPERTOIRE :"+files[i].getName());
                    classes.addAll(this.listeFiles(files[i].getName()));
                }
            }
        }
        }
       // System.out.println("ZISE before :"+classes.size());
        return classes;
    }

	
    public ArrayList<String> listDir(String dir) {
        ArrayList<String> classes = new ArrayList<String>();
    	File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
             
            
            	
            	
            	  if (files[i].isDirectory() == true) {
                 
                    System.out.println("Dossier" + files[i].getAbsolutePath());
                    this.dircount++;
                    if (!files[i].getName().contains(".")) classes.add(files[i].getAbsolutePath());
                    
                } else {
                    System.out.println("Fichier" + files[i].getAbsolutePath());

                    if (	LanguageConfigurator.getLanguage().isALanguageProgram(files[i].getAbsolutePath())) 
                    		//.endsWith("java"))
                    classes.add(files[i].getAbsolutePath());
                    this.filecount++;
                }
                if (files[i].isDirectory() == true && this.recursivePath == true) {
                    classes.addAll(this.listeFiles(files[i].getAbsolutePath()));
                }
            }
        }
       // System.out.println("ZISE before :"+classes.size());
        return classes;
    }
    
   public static String createRepWithDate(String name){        
        String format = "dd.MM.yyyy";
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
		Date date = new Date(); 
		String sdate=formater.format(date);
		String srep="./Results/Results_"+sdate+"_for_"+name;
		
		File out_rep=new File(srep);
		out_rep.mkdirs();
		return srep;
		
}
   public static void createRep(String path){        
       
	    File out_rep=new File(path);
		out_rep.mkdirs();
		
}
   
   
   public static void recursifDelete(String p) throws IOException {
      
	   String format = "dd.MM.yyyy";
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
		Date date = new Date(); 
		String sdate=formater.format(date);
		String srep="./Results/Results_"+sdate+"_for_"+p;
	   
	   
	   File path = new File(srep);
	   if (!path.exists()) {
		   
	   if (path.isDirectory()) {
          File[] children = path.listFiles();
          for (int i=0; children != null && i<children.length; i++)
             recursifDelete(children[i].getAbsolutePath());
          }
     }

   }
    
    /**
     * Exemple : lister les fichiers dans tous les sous-dossiers
     * @param args
     */
    public static void main(String[] args) {
        String pathToExplore = "./products/c/mail/Variant0001";
        UtiliClassFiles diskFileExplorer = new UtiliClassFiles(pathToExplore, true);
        Long start = System.currentTimeMillis();
      //  diskFileExplorer.list();
        System.out.println("----------");
        System.out.println("Analyse de " + pathToExplore + " en " + (System.currentTimeMillis() - start) + " mses");
        System.out.println(diskFileExplorer.dircount + " dossiers");
        System.out.println(diskFileExplorer.filecount + " fichiers");
    }
}