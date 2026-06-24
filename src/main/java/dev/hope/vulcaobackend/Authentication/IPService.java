package dev.hope.vulcaobackend.Authentication;


import jakarta.servlet.http.HttpServletRequest;

public class IPService {

    public static String getClientIpAddress(HttpServletRequest request) {


        String ip = request.getHeader("X-Forwarded-For");
        System.out.println(ip);
        if (ip != null && !ip.isEmpty() && !ip.equalsIgnoreCase("unknown")) {
            String ipAddress = ip.split(",")[0].trim();
            System.out.println("Ip Address captured: " + ipAddress);
            return ipAddress;
        }
        return request.getRemoteAddr();
    }
}
