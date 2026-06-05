package com.samtheo.instructif.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Pour simuler l'envoi d'un mail ou d'un sms.
 *
 * @author DASI Team
 */
public class Message {

    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
    public static final SimpleDateFormat HORODATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");

    private static final PrintStream OUT = System.out;

    private static void debut() {
        Date maintenant = new Date();
        OUT.println();
        OUT.println();
        OUT.println(FG_BLUE + "---<([ MESSAGE @ " + TIMESTAMP_FORMAT.format(maintenant) + " ])>---" + RESET);
        OUT.println();
    }

    private static void fin() {
        OUT.println();
        OUT.println(FG_BLUE + "---<([ FIN DU MESSAGE ])>---" + RESET);
        OUT.println();
        OUT.println();
    }

    public static void envoyerMail(String mailExpediteur, String mailDestinataire, String objet, String corps) {
        String MAIL_COLOR = FG_CYAN;
        Date maintenant = new Date();
        Message.debut();
        OUT.println(MAIL_COLOR + "~~~ E-mail envoyé le " + HORODATE_FORMAT.format(maintenant) + " ~~~");
        OUT.println(MAIL_COLOR + "De : " + mailExpediteur);
        OUT.println(MAIL_COLOR + "À  : " + mailDestinataire);
        OUT.println(MAIL_COLOR + "Obj: " + objet);
        OUT.println();
        OUT.println(MAIL_COLOR + corps.replace("\n", "\n" + MAIL_COLOR));
        OUT.print(RESET);
        Message.fin();
    }

    public static void envoyerNotification(String telephoneDestinataire, String message) {
        String NOTIFICATION_COLOR = FG_MAGENTA;
        Date maintenant = new Date();
        Message.debut();
        OUT.println(NOTIFICATION_COLOR + "~~~ Notification envoyée le " + HORODATE_FORMAT.format(maintenant) + " ~~~");
        OUT.println(NOTIFICATION_COLOR + "À : " + telephoneDestinataire);
        OUT.println();
        OUT.println(NOTIFICATION_COLOR + message.replace("\n", "\n" + NOTIFICATION_COLOR));
        OUT.print(RESET);
        Message.fin();
    }

    public static final String FG_BLACK = "[30m";
    public static final String FG_BLUE = "[34m";
    public static final String FG_CYAN = "[36m";
    public static final String FG_GREEN = "[32m";
    public static final String FG_MAGENTA = "[35m";
    public static final String FG_RED = "[31m";
    public static final String FG_WHITE = "[37m";
    public static final String FG_YELLOW = "[33m";

    public static final String BG_BLACK = "[40m";
    public static final String BG_BLUE = "[44m";
    public static final String BG_CYAN = "[46m";
    public static final String BG_GREEN = "[42m";
    public static final String BG_MAGENTA = "[45m";
    public static final String BG_RED = "[41m";
    public static final String BG_WHITE = "[47m";
    public static final String BG_YELLOW = "[43m";

    public static final String RESET = "[0m";
}
