package com.athena.meerkat.agent.monitoring.utils;

import java.io.File;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


/**
 * <pre>
 * Sigar를 이용한 Memory, CPU의 상태정보를 수집하는 유틸 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public final class SigarUtil {
	
	private static Sigar sigar;
	
	private SigarUtil() {
		// nothing to do.
	}

	/**
	 * <pre>
	 * sigar 라이브러리를 이용한 시스템 상태정보 수집 시 사용하는 /src/main/resources/sigar 경로의 
	 * 라이브러리 파일들을 java.library.path에 추가한다.
	 * </pre>
	 */
	static {
		String sigarLibPath = SigarUtil.class.getResource("/sigar").getFile();
		String javaLibPath = System.getProperty("java.library.path");
		
		if (javaLibPath.indexOf(sigarLibPath) < 0) {
			System.setProperty("java.library.path", new StringBuilder()
														.append(sigarLibPath)
														.append(File.pathSeparator)
														.append(javaLibPath)
														.toString());
		}
	}
	
	/**
	 * <pre>
	 * Sigar의 인스턴스를 반환한다.(Memory, CPU 외의 정보를 직접 조회할 때 사용)
	 * </pre>
	 * @return
	 */
	public static Sigar getInstance() {
		if (sigar == null) {
			sigar = new Sigar();
		}
		
		return sigar;
	}//end of getInstance()
	
	/**
	 * <pre>
	 * CPU Clock을 조회한다 (단위 : Mhz)
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static int getCpuClock() throws SigarException {
		return getInstance().getCpuInfoList()[0].getMhz();
	}//end of getCpuClock()
	
	/**
	 * <pre>
	 * CPU 갯수를 조회한다. (core 포함)
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static int getCpuNum() throws SigarException {
		return getInstance().getCpuInfoList().length;
	}//end of getCpuNum()
	
	/**
	 * <pre>
	 * CPU 모델을 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static String getCpuModel() throws SigarException {
		return getInstance().getCpuInfoList()[0].getModel();
	}//end of getCpuModel()
	
	/**
	 * <pre>
	 * CPU Vendor를 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static String getCpuVendor() throws SigarException {
		return getInstance().getCpuInfoList()[0].getVendor();
	}//end of getCpuVendor()
	
	/**
	 * <pre>
	 * RAM의 크기를 조회한다. (단위 : MByte)
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static long getMemSize() throws SigarException {
		return getInstance().getMem().getRam();
	}//end of getMemSize()

	/**
	 * <pre>
	 * Memory 상태 정보를 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static Mem getMem() throws SigarException {
		return getInstance().getMem();
	}//end of getMem()
	
	/**
	 * <pre>
	 * 전체 CPU 사용량을 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static CpuPerc getCpuPerc() throws SigarException {
		return getInstance().getCpuPerc();
	}//end of getCpuPerc()
	
	/**
	 * <pre>
	 * 개별 CPU 사용량을 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static CpuPerc[] getCpuPercList() throws SigarException {
		return getInstance().getCpuPercList();
	}//end of getCpuPercList()
	
	/**
	 * <pre>
	 * Network 정보를 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static NetInfo getNetInfo() throws SigarException {
		return getInstance().getNetInfo();
	}//end of getNetInfo()
	
	/**
	 * <pre>
	 * FileSystem 정보를 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static FileSystem[] getFileSystem() throws SigarException {
		return getInstance().getFileSystemList();
	}//end of getFileSystem()
	
	/**
	 * <pre>
	 * 특정 디렉토리의 FileSystem 사용 정보를 조회한다.
	 * </pre>
	 * @return
	 * @throws SigarException
	 */
	public static FileSystemUsage getFileSystemUsage(String dirName) throws SigarException {
		return getInstance().getFileSystemUsage(dirName);
	}//end of getFileSystemUsage()
	
	
	public static void main(String[] args)throws Exception {
		System.out.println(SigarUtil.getCpuPerc());
		System.out.println(SigarUtil.getNetInfo().getHostName());
	}
}
//end of SigarUtil.java