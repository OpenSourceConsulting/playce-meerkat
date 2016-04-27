package com.athena.meerkat.controller.web.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관련 유틸.
 *
 * @author 'Bongjin Kwon'
 * @version
 * @see
 */
public abstract class FileUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 파일 크기 제한
	 */
	public static Long limit = 1024L * 1024L;

	/**
	 * FileUtil
	 *
	 * @param
	 * @exception
	 */
	public FileUtil() {
	}

	public static boolean validateSize(MultipartFile file, long fileSize) {
		boolean chk = false;

		limit = Long.valueOf(fileSize);
		if (file.getSize() <= limit) {
			chk = true;
		}

		return chk;
	}
	
	public static String getFileName(String filePath) {
		int pos = filePath.lastIndexOf(File.separator);
		
		if(pos > -1) {
			return filePath.substring(pos + 1);
		}
		return filePath;
	}

	/**
	 * 주어진 File 에서 파일 확장자를 반환한다. File 은 isFile() true 조건을 만족해야한다.
	 *
	 * @param file
	 *            the file
	 * @return String
	 */
	public static String getExtension(MultipartFile file) {
		String ext = null;
		if (file != null) {
			ext = getExtension(file.getOriginalFilename());
		}
		return ext;
	}

	/**
	 * 주어진 File 에서 파일 확장자를 반환한다. File 은 isFile() true 조건을 만족해야한다.
	 *
	 * @param file
	 *            the file
	 * @return String
	 */
	public static String getExtension(File file) {
		String ext = null;
		if (file != null) {
			ext = getExtension(file.getName());
		}
		return ext;
	}

	/**
	 * 주어진 File 명에서 파일 확장자를 반환한다.
	 *
	 * @param fileName
	 *            the file name
	 * @return String
	 */
	public static String getExtension(String fileName) {
		String ext = null;
		if (fileName != null) {
			int lastIndex = fileName.lastIndexOf(".");
			if (lastIndex > -1) {
				ext = fileName.substring(lastIndex + 1);
			}
		}
		return ext;
	}

	/**
	 * <pre>
	 *  파일 다운로드 처리.
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @param serverFilePath
	 *            실제서버에 저장된 파일 전체 경로.
	 * @param realFileName
	 *            사용자가 다운로드할 파일명 (업로드한 파일명)
	 */
	public static void processDownFile(HttpServletRequest request, HttpServletResponse response, String serverFilePath, String realFileName) {

		try {

			response.setContentType("application/octet-stream");

			if (request.getHeader("User-Agent").toLowerCase().contains("firefox")) {
				String name = new String(realFileName.getBytes("UTF-8"), "ISO-8859-1");

				response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
				response.setHeader("Content-Transfer-Encoding", "binary");
			} else {
				// 익스플로러 등등.
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realFileName, "UTF-8"));
			}

			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			dumpFile(new File(serverFilePath), response.getOutputStream());

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 파일을 ServletOutputStream 에 출력하기.
	 *
	 * @param realFile
	 * @param response
	 * @throws Exception
	 */
	private static void dumpFile(File realFile, javax.servlet.ServletOutputStream outStream) {
		byte[] readByte = new byte[8192];
		BufferedInputStream bufferedinputstream = null;
		try {
			bufferedinputstream = new BufferedInputStream(new FileInputStream(realFile));
			int i;
			while ((i = bufferedinputstream.read(readByte, 0, 8192)) != -1) {
				outStream.write(readByte, 0, i);
			}
			outStream.flush();

		} catch (Exception ex) {
			throw new RuntimeException(ex);

		} finally {
			IOUtils.closeQuietly(bufferedinputstream);
			IOUtils.closeQuietly(outStream);
		}
	}

	/**
	 * <pre>
	 * 업로드 파일을 서버에 저장한다. (업로드한 파일명으로 저장된다.)
	 * </pre>
	 *
	 * @param dir
	 * @param mfile
	 * @return
	 */
	public static void dumpFile(String dir, MultipartFile mfile) {
		File dumpPath = new File(dir);

		if (!dumpPath.exists()) {
			dumpPath.mkdir();
		}

		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}

		try {
			File dest = new File(dir + mfile.getOriginalFilename());
			mfile.transferTo(dest);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * <pre>
	 * 업로드 파일을 서버에 저장한다.
	 * - 파일명을 유닉하게 변경해서 저장한다.
	 * </pre>
	 *
	 * @param dir
	 * @param mfile
	 * @param session
	 * @return 서버에 저장된 파일명. (경로 및 확장자 제외)
	 */
	public static String dumpUniqFile(String dir, MultipartFile mfile) {
		File dumpPath = new File(dir);

		if (!dumpPath.exists()) {
			dumpPath.mkdir();
		}

		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}

		String originalFilename = mfile.getOriginalFilename();

		UUID uid = UUID.randomUUID();
		String uniqName = uid.toString();
		// String fType =
		// originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자
		String saveFile = dir + uniqName + originalFilename;

		try {
			File dest = new File(saveFile);
			mfile.transferTo(dest);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		return uniqName;

	}

}
