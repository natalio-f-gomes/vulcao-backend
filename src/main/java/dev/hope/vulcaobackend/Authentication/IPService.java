package dev.hope.vulcaobackend.Authentication;


import jakarta.servlet.http.HttpServletRequest;

public class IPService {

    public static String getClientIpAddress(HttpServletRequest request){
        String[] headersToCheck ={
                "X-Forwarded-For",
                "X-Real-IP",
                "Forwarded",
                "CF-Connecting-IP",      // Cloudflare
                "True-Client-IP",        //Akamai / Cloudflare Enterprise
                "Fastly-Client-IP",      // Fastly
                "X-Cluster-Client-IP",   // Rackspace / Load balancers
                "X-Client-IP",
                "Client-IP",
                "X-Remote-Addr",
                "X-Remote-IP",
                "REMOTE_ADDR"            // Direct connection or server variable
        };
        for (String header: headersToCheck){
            String ip = request.getHeader(header);
            System.out.println(ip);
            if(ip!= null && ip.length()!=0 && !ip.equalsIgnoreCase("unknown")){
                //return ip.split(",")[0].trim();
                System.out.println(ip);
            }

        }
        return request.getRemoteAddr();
    }
}
