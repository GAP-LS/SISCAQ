package com.suchorski.siscaq.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event)  { 
		HttpSession session = event.getSession();
		session.setAttribute("logged", false);
		session.setAttribute("user", null);
		session.setAttribute("unhighlight", false);
		session.setAttribute("highlight", 0);
	}

	public void sessionDestroyed(HttpSessionEvent event)  {
		HttpSession session = event.getSession();
		session.removeAttribute("logged");
		session.removeAttribute("user");
		session.removeAttribute("unhighlight");
		session.removeAttribute("highlight");
	}
	
}
