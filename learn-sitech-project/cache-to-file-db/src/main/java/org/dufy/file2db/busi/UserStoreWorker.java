package org.dufy.file2db.busi;

import java.text.ParseException;

import javax.annotation.Resource;

import org.dufy.dao.UserDao;
import org.dufy.file2db.StoreManager;
import org.dufy.file2db.StoreWorker;
import org.dufy.log.DataLogger;
import org.dufy.model.User;
import org.dufy.util.StringSplitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Scope("prototype")
public class UserStoreWorker extends StoreWorker {

    private static final Logger logger = LoggerFactory.getLogger(UserStoreWorker.class);

	@Resource
	private UserDao dao;

	@Override
	protected boolean processRecorder(String str) throws Throwable{
		if(StringUtils.isEmpty(str)){
			StoreManager.logger.error("数据字符串为空!");
			return false;
		}
		StoreManager.logger.debug("执行请求数据入库,原始数据: "+str);

		User request = parseUser(str);
		
		return request2db(request);
	}
	
	
	/**
	 * Parse a chat user record to instance
	 * 
	 * @param str
	 * @return
	 * @throws ParseException 
	 */
	private User parseUser(String str) throws ParseException {
		StringSplitHelper helper = new StringSplitHelper(str, DataLogger.UNIT_SEP);
		if(!"User".equals(helper.getFirst())) {
			throw new ParseException("Must start with User!", 0);
		}
		
		User user = new User();

		user.setId(Integer.parseInt(helper.getNext()));
		user.setUserName(helper.getNext());
		user.setPassword(helper.getNext());
        user.setAge(Integer.parseInt(helper.getNext()));

		return user;
	}


	/**
	 * Import chat user message to database
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private boolean request2db(User user) throws Exception {

		try{
            User uDb = dao.selectByPrimaryKey(user.getId());
            if(uDb != null){
                logger.info("update user -------->" + user);
                dao.updateByPrimaryKey(user);
            }else{
                logger.info("insert user -------->" + user);
                dao.insert(user);
            }
		}
		catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
