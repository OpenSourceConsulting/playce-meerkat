package com.athena.meerkat.controller.web.provisioning.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.controller.web.provisioning.ProcessAction;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 */
public class CommandUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandUtil.class);
	
	private static final String[] ERROR_LOGS = new String[]{"BUILD FAILED", "[sshexec] Error", "[sshexec] Caught exception:"};// except "[sshexec] Remote command failed", 
	
	public static String execWithLog(File workingDir, List<String> cmds){
		
		return CommandUtil.exec(workingDir, cmds, true, new ProcessAction() {
			
			@Override
			public String actionPerform(Process p, BufferedReader br) throws Exception {
				
				boolean success = true;
				String line;
				
				while ((line = br.readLine()) != null) {
					
					if (success) {
						for (String errs : ERROR_LOGS) {
							if (line.contains(errs)) {
								success = false;
							}
						}
					}
					
					LOGGER.info(line);
				}
				
				return String.valueOf(success);
			}
		});
	}
	
	/**
	 * <pre>
	 * not loging, return logs.
	 * </pre>
	 * @param workingDir
	 * @param cmds
	 * @return logs.
	 */
	public static List<String> execAndReturnLogs(File workingDir, List<String> cmds){
		
		ProcessBuilder bld = new ProcessBuilder(cmds);
		BufferedReader br = null;
		Process p = null;
		String result = null;
		List<String> logs = new ArrayList<String>();
		try {
			
			bld.redirectErrorStream(true);
			bld.directory(workingDir);
			p = bld.start();
			
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String line;
			
			while ((line = br.readLine()) != null) {
				
				logs.add(line);
				LOGGER.info(line);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(br != null){
				try{ br.close(); }catch(IOException e){}
			}
			
			if(p != null){
				p.destroy();
			}
		}
		
		return logs;
	}

	/*
	public static void execWaitFor(List<String> cmds){
		CommandUtil.exec(cmds, false, new ProcessAction() {
			
			@Override
			public String actionPerform(Process p, BufferedReader br) throws Exception {
				p.waitFor();
				
				return null;
			}
		});
	}
	*/
	
	/**
	 * <pre>
	 * execute process
	 * </pre>
	 * @param cmds 
	 * 			commands
	 * @param getReader
	 *          process 의 InputStreamReader 를 생성할지 여부. true 이면 ProcessAction.actionPerform 의 br 을 사용할수 있음. false 이면 br 이 null 이다.
	 * @param pAction
	 *          process 를 생성한후 처리할 action 을 수행한다.
	 * @return
	 */
	private static String exec(File workingDir, List<String> cmds, boolean getReader, ProcessAction pAction){
		
		ProcessBuilder bld = new ProcessBuilder(cmds);
		BufferedReader br = null;
		Process p = null;
		String result = null;
		try {
			
			bld.redirectErrorStream(true);
			bld.directory(workingDir);
			p = bld.start();
			
			if (getReader) {
				InputStream is = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
			}
			
			result = pAction.actionPerform(p, br);
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(br != null){
				try{ br.close(); }catch(IOException e){}
			}
			
			if(p != null){
				p.destroy();
			}
		}
		
		return result;
	}
	
}
//end of CommandUtil.java