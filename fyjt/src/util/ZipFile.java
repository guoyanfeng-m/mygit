package util;

import java.io.File;  

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;  
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;  
import org.apache.tools.ant.types.FileSet;  
  
/**
 * @ClassName: ZipCompressorByAnt
 * @CreateTime Apr 28, 2013 1:23:45 PM
 * @author : Mayi
 * @Description: 压缩文件的通用工具类-采用ant中的org.apache.tools.ant.taskdefs.Zip来实现，更加简单。
 *
 */
public class ZipFile {  
	 public final static String encoding = "UTF8";
    private File zipFile;  
  
    /**
     * 压缩文件构造函数
     * @param pathName 最终压缩生成的压缩文件：目录+压缩文件名.zip
     */
    public ZipFile(String finalFile) {  
        zipFile = new File(finalFile);  
    }  
      
    /**
     * 执行压缩操作
     * @param srcPathName 需要被压缩的文件/文件夹
     */
    public void compressExe(String srcPathName) {  
        File srcdir = new File(srcPathName);  
        if (!srcdir.exists()){
            System.out.println(srcPathName + "不存在！");  
        } 
          
        Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(zipFile);  
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(srcdir);  
        //fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹 eg:zip.setIncludes("*.java");  
        //fileSet.setExcludes(...); //排除哪些文件或文件夹  
        zip.addFileset(fileSet);  
        zip.execute();  
    }  
 
    public static void unzip(String zipFilepath, String destDir)
		    throws BuildException, RuntimeException {
		   if (!new File(zipFilepath).exists())
		    throw new RuntimeException("zip file " + zipFilepath
		      + " does not exist.");
		
		   Project proj = new Project();
		   Expand expand = new Expand();
		   expand.setProject(proj);
		   expand.setTaskType("unzip");
		   expand.setTaskName("unzip");
		   expand.setEncoding(encoding);
		
		   expand.setSrc(new File(zipFilepath));
		   expand.setDest(new File(destDir));
		   expand.execute();
		  }

} 