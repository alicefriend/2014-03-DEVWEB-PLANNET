package net.plannet.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.plannet.db.SignInDAO;
import net.plannet.model.User;
import net.plannet.util.ErrorUtil;
import net.plannet.util.GsonUtil;
import net.plannet.util.RequestResult;
import net.plannet.util.UUIDControl;

@WebServlet("/SignIn")
public class SignInServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(SignInServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// 클라로부터 id/pw를 받는다.
			User userFromReq = GsonUtil.getObjectFromRequest(req, User.class);
			logger.info("SingIn System에 진입하였습니다. email:{}", userFromReq.getEmail());
			
			if (userFromReq.isValid()) {
				// 로그인을 한다.(select쿼리를 던진다.)
				ArrayList<User> userRecords = new SignInDAO().selectSignIn(userFromReq);
				
				if (userRecords.size() == 1) {
					User userRecord = userRecords.get(0);
					// 성공했을 경우 클라에게 uuid를 던져준다.
					String uuid = new UUIDControl().createUUID();
					
					//DB에 집어넣는다.
					new SignInDAO().updateUUID(userRecord.getUid(), uuid);
					
					resp.setHeader("uuid", uuid);
					resp.setHeader("SigninResult", RequestResult.Success);
					logger.info("SignIn이 성공적으로 완료되었습니다. 발급UUID:{}", uuid);
					HttpSession session = req.getSession();
					session.setAttribute(RequestResult.SESSION_USER_ID, userRecord.getUid());
				// 가가입은 되어 있으나 인증이 되지 않은 경우에 대한 처리 	
				// }else if(){
				} else {
					// 실패했을 경우
					resp.setHeader("SigninResult", RequestResult.Fail);
					logger.info("SignIn에 실패하였습니다.");
					// id가 없을 경우에 대한 예외처리 (옵션)
					// pw가 없을 경우에 대한 예외처리 (옵션)
				}
			}
		} catch (Exception e) {
			ErrorUtil.printError("HTTP error", e);
		}
	}
}
