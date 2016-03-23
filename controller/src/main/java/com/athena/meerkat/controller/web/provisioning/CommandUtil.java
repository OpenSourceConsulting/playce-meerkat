package com.athena.meerkat.controller.web.provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 */
public class CommandUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandUtil.class);
	
	
	
	public static void execWithLog(File workingDir, List<String> cmds){
		CommandUtil.exec(workingDir, cmds, true, new ProcessAction() {
			
			@Override
			public String actionPerform(Process p, BufferedReader br) throws Exception {
				String line;

				while ((line = br.readLine()) != null) {
					LOGGER.info(line);
				}
				
				return null;
			}
		});
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