package com.athena.meerkat.controller.web.common.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.entities.CommonCode;

/**
 * 공통 코드 목록 전역 저장소.
 *
 * Created on 2010. 7. 15
 * 
 * @author 권봉진
 */
@Service
public class CommonCodeHandler implements InitializingBean {

	protected final Logger logger = LoggerFactory
			.getLogger(CommonCodeHandler.class);

	private static final String CODE_GROUPS = "groups";

	Map allCodes = new HashMap();

	@Autowired
	protected CommonCodeRepository repository;

	private static final String Y = "Y";

	private String statement = "enr.CommonCode.getCommonCodeAllList";

	public CommonCodeHandler() {
	}

	/**
	 * <pre>
	 * group code 가져오기.
	 * </pre>
	 *
	 * @return
	 */
	/*
	 * public ArrayList getGroups(){
	 * 
	 * Map allCodes = getAllCodes();
	 * 
	 * return (ArrayList)allCodes.get(CODE_GROUPS); }
	 */

	/**
	 * cd_grp 의 코드리스트를 반환한다.
	 * 
	 * @param cd_grp
	 * @return
	 */
	public List<CommonCode> getCodes(String cd_grp) {

		Map allCodes = getAllCodes();
		if (allCodes == null)
			return null;

		List<CommonCode> codes = (ArrayList) allCodes.get(cd_grp);

		if (codes == null) {
			// 캐시에 없으면 db query.
			try {
				codes = repository.findByGropId(cd_grp);
				// StringUtil.changeModelListCharset(codes, new
				// String[]{"cdnm"});
				allCodes.put(cd_grp, codes);
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
		}

		// return (ArrayList)allCodes.get(cd_grp);
		return codes;
	}

	@SuppressWarnings("unchecked")
	private Map getAllCodes() {

		return allCodes;
	}

	/*
	 * public static void setCodes(String group_cd, ArrayList codes){
	 * allCodes.put(group_cd, codes); }
	 */

	/**
	 * 해당 코드 정보 가져오기.
	 * 
	 * @param cd_grp
	 * @param codeId
	 * @return
	 */
	public CommonCode getCode(String cd_grp, int codeId) {
		List<CommonCode> codes = getCodes(cd_grp);
		CommonCode codef = null;

		if (codes != null) {
			for (CommonCode code : codes) {
				codef = code;
				if (codef.getId() == codeId) {
					break;
				} else {
					codef = new CommonCode();
				}
			}
		}

		return codef;
	}

	public CommonCode getCode(String name) {
		CommonCode code = repository.findByCodeNm(name);
		return code;
	}

	/**
	 * <pre>
	 * 코드명 가져오기.
	 * </pre>
	 *
	 * @param cd_grp
	 * @param code
	 * @return
	 */
	public String getCodeNm(String cd_grp, int codeId) {
		CommonCode codef = getCode(cd_grp, codeId);

		if (codef == null) {
			return "";
		}

		return codef.getCodeNm();
	}

	@SuppressWarnings("unchecked")
	private void loadCodesProcess() {
		logger.info("공통 코드 loading...");

		List<CommonCode> list = new ArrayList<CommonCode>();

		try {
			list = repository.findAll(new Sort(Sort.Direction.ASC, "gropId",
					"prtoSeq"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(), e);
		}

		ArrayList<CommonCode> codes = null;
		int cntCodes = 0;

		String cd_grp = "";
		for (CommonCode codef : list) {

			String temp = codef.getGropId();

			if (!cd_grp.equals(temp)) {
				codes = new ArrayList<CommonCode>();
				allCodes.put(temp, codes);
			}

			cntCodes++;
			codes.add(codef);
			cd_grp = temp;
		}

		logger.info("공통 코드 완료 (총: {}개).", cntCodes);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadCodesProcess();
	}

}
