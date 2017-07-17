package controllers;

import play.data.DynamicForm;

import org.mindrot.jbcrypt.BCrypt;

import play.data.Form;
import play.mvc.Controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.avaje.ebean.Ebean;

import models.Officine;
import models.Utilisateur;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

public class Recuperation extends Controller {
	

	
	public  Result recup()
	{
		
		String randomPass = RandomStringUtils.random(10,"abcdefghijklmnopqrstuvwxyz1234567890");
		DynamicForm form = Form.form().bindFromRequest();
		String mail = form.get("Email");
		String inpe = form.get("inpe");
		Utilisateur user = Ebean.find(Utilisateur.class).where().eq("email", mail.toLowerCase()).findUnique();
		if(user != null){
			
			Officine officine=Ebean.find(Officine.class).where().eq("inpe", inpe).findUnique();
			if(officine==null){
				return Results.badRequest(authentification.render(4));
			}
			HtmlEmail email = new HtmlEmail();
	           try {           
	        	   String aHtml= "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' ><title>Amanassure| Email de r&eacute;cup&eacute;ration</title>"
	   					+ "<style type='text/css'>html { -webkit-text-size-adjust:none; -ms-text-size-adjust: none;}"
						+ "@media only screen and (max-device-width: 680px), only screen and (max-width: 680px) {"
						+ "*[class='table_width_100'] {width: 96% !important;}*[class='border-right_mob'] {border-right: 1px solid #dddddd;}"
						+ "*[class='mob_100'] {width: 100% !important;}*[class='mob_center'] {text-align: center !important;padding: 0 !important;}"
						+ "*[class='mob_center_bl'] {float: none !important;display: block !important;margin: 0px auto;}	.iage_footer a {"
						+ "text-decoration: none;color: #929ca8;}img.mob_display_none {width: 0px !important;height: 0px !important;display: none !important;}"
						+ "img.mob_width_50 {width: 40% !important;height: auto !important;}img.mob_width_80 {width: 80% !important;height: auto !important;}"
						+ "img.mob_width_80_center {width: 80% !important;height: auto !important;margin: 0px auto;}.img_margin_bottom {font-size: 0;height: 25px;"
						+ "line-height: 25px;}}</style></head><body style='padding: 0px; margin: 0px;'><div id='mailsub' class='notification' align='center'>"
						+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='min-width: 320px;'><tr><td align='center' bgcolor='#eff3f8'>"
						+ "<table border='0' cellspacing='0' cellpadding='0' class='table_width_100' width='100%' style='max-width: 680px; min-width: 300px;'>"
						+ "<tr><td>"
						+ "<div style='height: 80px; line-height: 80px; font-size: 10px;'>&nbsp;</div>"
						+ "</td></tr>"
						+ "<tr><td align='center' bgcolor='#ffffff'>"
						+ "<div style='height: 10px; line-height: 10px; font-size: 10px;'>&nbsp;</div>"
						+ "<table width='90%' border='0' cellspacing='0' cellpadding='0'>"
						+ "<tr><td align='left'>"
						+ "<div class='mob_center_bl' style='float: left; display: inline-block; width: 115px;'>"
						+ "<table class='mob_center' width='115' border='0' cellspacing='0' cellpadding='0' align='left' style='border-collapse: collapse;'>"
						+ "<tr><td align='left' valign='middle'>"
						+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
						+ "<table width='115' border='0' cellspacing='0' cellpadding='0' >"
						+ "<tr><td align='left' valign='top' class='mob_center'>"
						+ "<a href='/' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 13px;'>"
						+ "<font face='Arial, Helvetica, sans-seri; font-size: 13px;' size='3' color='#596167'>"
						+ "<img src='https://img15.hostingpics.net/pics/686524logobigwhite.png' width='170' height='57' alt='LOGILAN' border='0' style='display: block;'  /></font></a>"
						+ "</td></tr>"
						+ "</table>"
						+ "</td></tr>"
						+ "</table></div>"
						+ "<div class='mob_center_bl' style='float: right; display: inline-block; width: 88px;'>"
						+ "<table width='88' border='0' cellspacing='0' cellpadding='0' align='right' style='border-collapse: collapse;'>"
						+ "<tr><td align='right' valign='middle'>"
						+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
						+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' >"
						+ "<tr><td align='right'>"
						+ "<div class='mob_center_bl' style='width: 88px;'>"
						+ "<table border='0' cellspacing='0' cellpadding='0'>"
						+ "<tr><td width='30' align='center' style='line-height: 19px;'>"
						+ "<a href='#' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 12px;'>"
						+ "<font face='Arial, Helvetica, sans-serif' size='2' color='#596167'>"
						+ "<img src='cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_circle_color-48.png' alt='Facebook' border='0' style='display: block;' /></font></a>"
						+ "</td><td width='39' align='center' style='line-height: 19px;'>"
						+ "<a href='#' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 12px;'>"
						+ "<font face='Arial, Helvetica, sans-serif' size='2' color='#596167'>"
						+ "<img src='cdn3.iconfinder.com/data/icons/free-social-icons/67/twitter_circle_color-48.png' alt='Twitter' border='0' style='display: block;' /></font></a>"
						+ "</td> </tr>"
						+ "</table>"
						+ "</div>"
						+ "</td></tr>"
						+ "</table>"
						+ "</td></tr>"
						+ "</table></div></td>"
						+ "</tr>"
						+ "</table>"
						+ "<div style='height: 30px; line-height: 30px; font-size: 10px;'>&nbsp;</div>"
						+ "</td></tr>"
						+ "<tr><td align='center' bgcolor='#f8f8f8'>"
						+ "<table width='90%' border='0' cellspacing='0' cellpadding='0'>"
						+ "<tr><td align='center'>"
						+ "<div style='height: 60px; line-height: 60px; font-size: 10px;'>&nbsp;</div>"
						+ "<div style='line-height: 44px;'>"
						+ "<font face='Arial, Helvetica, sans-serif' size='5' color='#6b6b6b' style='font-size: 34px;'>"
						+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 30px; color: #6b6b6b;'>"
						+ "Bonjour <strong>"+user.getNom()+" "+user.getPrenom()+"</strong><br>"
						+ "<b> Bienvenue dans LOGIECHANGE.</b><br>"
						+ "Vous avez demand&eacute; de changer votre mot de passe. Votre nouveau mot de passe est : "+randomPass+" ."
						+ "</span></font>"
						+ "</div>"
						+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
						+ "</td></tr>"
						+ "<tr><td align='center'>"
						+ "<div style='line-height: 24px;'>"
						+ "<font face='Arial, Helvetica, sans-serif' size='4' color='#9c9c9c' style='font-size: 15px;'>"
						+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 15px; color: #9c9c9c;'>"
						+ "<br/> Note: Vous pouvez à tout moment modifier votre mot de passe à travers votre compte"
						+ "</span></font>"
						+ "</div>"
						+ "<div style='height: 50px; line-height: 50px; font-size: 10px;'>&nbsp;</div>"
						+ "</td></tr>"
						+ "</table>"
						+ "</td></tr>"
						+ "<tr><td class='iage_footer' align='center' bgcolor='#ffffff'>"
						+ "<table width='100%' border='0' cellspacing='0' cellpadding='0'>"
						+ "<tr><td align='center'>"
						+ "<font face='Arial, Helvetica, sans-serif' size='3' color='#96a5b5' style='font-size: 13px;'><br/>"
						+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #96a5b5;'>"
						+ "2017 &copy; LOGILAN MAROC."
						+ "</span></font>"
						+ "</td></tr>"
						+ "</table>"
						+ "</td></tr>"
						+ "<tr><td>"
						+ "</td></tr>"
						+ "</table>"
						+ "</td></tr>"
						+ "</table>"
						+ "</div>"
						+ "</body>" + "</html>";
	        	   
	        	   email.setHostName("smtp.googlemail.com");
	        	   String encodingOptions = "text/html; charset=UTF-8";
	               email.setSmtpPort(465);
	               email.setSSLOnConnect(true);
	               email.setFrom("logiechange@gmail.com","LogiEchange");
	               email.setAuthentication("logiechange", "A123456789++");
	               
	               email.addTo(user.getEmail());
	               email.setSubject("Regénération de votre mot de passe");
	               email.setHtmlMsg(aHtml); 
	               
	               email.send();
	           
	           } catch (EmailException e) {
	               e.printStackTrace();
	           }
	    String hashPass= BCrypt.hashpw(randomPass, BCrypt.gensalt());
		user.setPassword(hashPass);
		Ebean.update(user);
		return Results.ok(authentification.render(0));
		}
		else
			return Results.badRequest(authentification.render(4));
		
	}

	
	public  String getEmail()
	{
		String Email = "";
        DynamicForm form = Form.form().bindFromRequest();
		String login = form.get("Login");
		if(Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique()!=null)
		{	
	    Utilisateur user = Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique();
	    Email = user.getEmail().toString();
	    }
		return Email;
	}
	
	
	public  Utilisateur getUser(String login){
		
			 Utilisateur user = null ;
			    
			    if(Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique()!=null);		
				{
			    user = Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique();
				}
				return user;
				
			
		 }
}
