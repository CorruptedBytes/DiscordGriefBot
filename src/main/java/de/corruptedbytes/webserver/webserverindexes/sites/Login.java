package de.corruptedbytes.webserver.webserverindexes.sites;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

import de.corruptedbytes.User;
import de.corruptedbytes.utils.Config;
import de.corruptedbytes.webserver.WebServerIndex;
import de.corruptedbytes.webserver.WebServerUtil;

public class Login implements WebServerIndex {

	@Override
	public void index(HttpServer httpServer) throws IOException {
		httpServer.createContext("/login", exchange -> {
			if (!exchange.getRequestMethod().equals("GET")) {
				exchange.close();
				return;
			}
			
			if (!Config.CONFIG_FILE.exists()) {
				WebServerUtil.redirect(exchange, "/setup");
			} else {
				String session = WebServerUtil.getCookie(exchange, "session");
				if (new User(session).verifyUser()) {
					WebServerUtil.redirect(exchange, "/panel");
				} else {
					WebServerUtil.sendFile(exchange, 200, new File(Config.WEB_DIRECTORY + "/login.html"));
				}
			}
		});
	}

}
