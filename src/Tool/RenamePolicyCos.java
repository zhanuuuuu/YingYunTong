package Tool;

import java.io.File;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class RenamePolicyCos implements FileRenamePolicy {

	public File rename(File uploadFile) {
		String newName = getNewFileName(uploadFile.getName());
		
		 //得到路径                                     文件名
		File renameFile = new File(uploadFile.getParent(), newName);
		return renameFile;
	}

	private String getNewFileName(String fileName) {
		StringBuffer newName = new StringBuffer();
		if (null != fileName && !"".equals(fileName)) {
			String type = "";
			String name = "";
			if (fileName.indexOf(".") != -1) {
				type = fileName.substring(fileName.indexOf("."));
				name = fileName.substring(0, fileName.indexOf("."));
			} else {
				name = fileName;
			}
			newName.append(name);
			newName.append(type);
		}
		return newName.toString();
	}

//	private String getSuffix() {
//		StringBuffer suffix = new StringBuffer("_");
//		String now = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss")
//				.format(new Date());
//		suffix.append(now);
//		suffix.append("_");
//		Random random = new Random();
//		String randomValue = String.valueOf(random.nextInt(1000));
//		suffix.append(randomValue);
//		return suffix.toString();
//	}

	// public static void main(String[] args) {
	// RenamePolicyCos my = new RenamePolicyCos();
	// LoggerUtil.info(my.getNewFileName("log.txt"));
	// }

}
