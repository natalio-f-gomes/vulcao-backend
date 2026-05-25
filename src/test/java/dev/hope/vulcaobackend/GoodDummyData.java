package dev.hope.vulcaobackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GoodDummyData {

    public List<JSONObject> getRandomUserRegistrationData() throws JSONException {
        ArrayList<JSONObject> users = new ArrayList<>();
        users.add(new JSONObject().put("username", "testUser1").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith1@gmail.com").put("password", "Password123"));
        users.add(new JSONObject().put("username", "testUser2").put("lastName", "Johnson").put("firstName", "Emily").put("email", "emily.johnson2@gmail.com").put("password", "Password123"));
        users.add(new JSONObject().put("username", "testUser3").put("lastName", "Williams").put("firstName", "Michael").put("email", "michael.williams3@gmail.com").put("password", "Password123"));
        users.add(new JSONObject().put("username", "testUser4").put("lastName", "Brown").put("firstName", "Sarah").put("email", "sarah.brown4@gmail.com").put("password", "Password123"));
        users.add(new JSONObject().put("username", "testUser5").put("lastName", "Jones").put("firstName", "David").put("email", "david.jones5@gmail.com").put("password", "Password123"));
        return users;
    }

    public List<JSONObject> getRandomEmployerRegistrationData() throws JSONException {
        ArrayList<JSONObject> employers = new ArrayList<>();
        employers.add(new JSONObject().put("username", "testEmployer1").put("lastName", "Garcia").put("firstName", "Sophia").put("email", "sophia.garcia1@gmail.com").put("password", "Password123"));
        employers.add(new JSONObject().put("username", "testEmployer2").put("lastName", "Miller").put("firstName", "Daniel").put("email", "daniel.miller2@gmail.com").put("password", "Password123"));
        employers.add(new JSONObject().put("username", "testEmployer3").put("lastName", "Davis").put("firstName", "Olivia").put("email", "olivia.davis3@gmail.com").put("password", "Password123"));
        employers.add(new JSONObject().put("username", "testEmployer4").put("lastName", "Martinez").put("firstName", "James").put("email", "james.martinez4@gmail.com").put("password", "Password123"));
        employers.add(new JSONObject().put("username", "testEmployer5").put("lastName", "Hernandez").put("firstName", "Isabella").put("email", "isabella.hernandez5@gmail.com").put("password", "Password123"));
        return employers;
    }

    public ArrayList<JSONObject> getRandomCompanyData(String creatorUsername) throws JSONException {
        JSONObject company1 = new JSONObject();
        company1.put("name", "Silverline Logistics");
        company1.put("type", "Limited Liability Company");
        company1.put("taxIdNumber", "98-7654321");
        company1.put("description", "Specializing in cold-chain distribution and last-mile delivery solutions for pharmaceutical clients.");
        company1.put("phoneNumber", "+44 20 7946 0123");
        company1.put("faxNumber", "+44 20 7946 0124");
        company1.put("revenue", 4850200.50);
        company1.put("address", new JSONObject()
                .put("island", "Oahu")
                .put("city", "Honolulu")
                .put("zone", "Honolulu County")
                .put("street", "123 Innovation Drive")
                .put("postalCode", "96813")).toString();
        company1.put("email", "ops@silverline-logistics.co.uk");
        company1.put("creatorUsername", creatorUsername);

        JSONObject company2 = new JSONObject();
        company2.put("name", "BluePeak Technologies");
        company2.put("type", "Corporation");
        company2.put("taxIdNumber", "12-3456789");
        company2.put("description", "Developing cloud-native software platforms for fintech startups and enterprise clients.");
        company2.put("phoneNumber", "+1 415 555 0198");
        company2.put("faxNumber", "+1 415 555 0199");
        company2.put("revenue", 12500000.75);
        company2.put("address", new JSONObject()
                .put("island", "Oahu")
                .put("city", "Honolulu")
                .put("zone", "Honolulu County")
                .put("street", "123 Innovation Drive")
                .put("postalCode", "96813")).toString();
        company2.put("creatorUsername", creatorUsername);

        JSONObject company3 = new JSONObject();
        company3.put("name", "GreenHarvest Foods");
        company3.put("type", "Partnership");
        company3.put("taxIdNumber", "23-9876543");
        company3.put("description", "Organic food supplier focused on sustainable farming and farm-to-table distribution.");
        company3.put("phoneNumber", "+1 312 555 0112");
        company3.put("faxNumber", "+1 312 555 0113");
        company3.put("revenue", 7321500.00);
        company3.put("address", new JSONObject()
                .put("island", "Oahu")
                .put("city", "Honolulu")
                .put("zone", "Honolulu County")
                .put("street", "123 Innovation Drive")
                .put("postalCode", "96813")).toString();
        company3.put("email", "contact@greenharvestfoods.com");
        company3.put("creatorUsername", creatorUsername);

        JSONObject company4 = new JSONObject();
        company4.put("name", "Ironclad Security Solutions");
        company4.put("type", "Limited Liability Company");
        company4.put("taxIdNumber", "34-1122334");
        company4.put("description", "Providing cybersecurity consulting, penetration testing, and managed security services.");
        company4.put("phoneNumber", "+1 202 555 0176");
        company4.put("faxNumber", "+1 202 555 0177");
        company4.put("revenue", 9800450.25);
        company4.put("email", "support@ironcladsecurity.com");
        company4.put("creatorUsername", creatorUsername);
        company4.put("address", new JSONObject()
                .put("island", "Oahu")
                .put("city", "Honolulu")
                .put("zone", "Honolulu County")
                .put("street", "123 Innovation Drive")
                .put("postalCode", "96813")).toString();

        JSONObject company5 = new JSONObject();
        company5.put("name", "SunWave Energy");
        company5.put("type", "Corporation");
        company5.put("taxIdNumber", "56-7788990");
        company5.put("description", "Renewable energy company specializing in solar farm development and energy storage solutions.");
        company5.put("phoneNumber", "+1 602 555 0145");
        company5.put("faxNumber", "+1 602 555 0146");
        company5.put("revenue", 21500000.00);
        company5.put("address", new JSONObject()
                .put("island", "Oahu")
                .put("city", "Honolulu")
                .put("zone", "Honolulu County")
                .put("street", "123 Innovation Drive")
                .put("postalCode", "96813")).toString();
        company5.put("email", "hello@sunwaveenergy.com");
        company5.put("creatorUsername", creatorUsername);

        ArrayList<JSONObject> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);
        companies.add(company4);
        companies.add(company5);
        return companies;
    }

    public JSONArray getRandomStaffRegistrationData(String companyId) throws JSONException {
        JSONArray staffList = new JSONArray();
        staffList.put(new JSONObject().put("username", "testStaff1").put("lastName", "Scott").put("firstName", "Liam").put("email", "liam.scott1@gmail.com").put("password", "Password123").put("companyId", companyId));
        staffList.put(new JSONObject().put("username", "testStaff2").put("lastName", "Adams").put("firstName", "Chloe").put("email", "chloe.adams2@gmail.com").put("password", "Password123").put("companyId", companyId));
        staffList.put(new JSONObject().put("username", "testStaff3").put("lastName", "Baker").put("firstName", "Noah").put("email", "noah.baker3@gmail.com").put("password", "Password123").put("companyId", companyId));
        staffList.put(new JSONObject().put("username", "testStaff4").put("lastName", "Carter").put("firstName", "Ella").put("email", "ella.carter4@gmail.com").put("password", "Password123").put("companyId", companyId));
        staffList.put(new JSONObject().put("username", "testStaff5").put("lastName", "Evans").put("firstName", "Lucas").put("email", "lucas.evans5@gmail.com").put("password", "Password123").put("companyId", companyId));
        return staffList;
    }

    public JSONArray getUserLoginData() throws JSONException {
        JSONArray loginList = new JSONArray();
        loginList.put(new JSONObject().put("username", "testUser1").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testUser2").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testUser3").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testUser4").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testUser5").put("password", "Password123"));
        return loginList;
    }

    public JSONArray getEmployerLoginData() throws JSONException {
        JSONArray loginList = new JSONArray();
        loginList.put(new JSONObject().put("username", "testEmployer1").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testEmployer2").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testEmployer3").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testEmployer4").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testEmployer5").put("password", "Password123"));
        return loginList;
    }

    public JSONArray getStaffLoginData() throws JSONException {
        JSONArray loginList = new JSONArray();
        loginList.put(new JSONObject().put("username", "testStaff1").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testStaff2").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testStaff3").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testStaff4").put("password", "Password123"));
        loginList.put(new JSONObject().put("username", "testStaff5").put("password", "Password123"));
        return loginList;
    }

    public JSONArray getAllLoginData() throws JSONException {
        JSONArray loginList = new JSONArray();
        for (int i = 0; i < getUserLoginData().length(); i++)
            loginList.put(getUserLoginData().get(i));
        for (int i = 0; i < getEmployerLoginData().length(); i++)
            loginList.put(getEmployerLoginData().get(i));
        return loginList;
    }

    public Map<Integer, String> getRandomUserRegisterData() throws JSONException {
        List<JSONObject> randomUsers = getRandomUserRegistrationData();
        Random randomInstance = new Random();
        int randomNumber = randomInstance.nextInt(randomUsers.size());
        String randomAccount = randomUsers.get(randomNumber).toString();
        System.out.println("Random USER Number: " + randomNumber);
        System.out.println("Random User Data: " + randomAccount);
        return Map.of(randomNumber, randomAccount);
    }

    public Map<Integer, String> getRandomEmployerRegisterData() throws JSONException {
        List<JSONObject> randomEmployers = getRandomEmployerRegistrationData();
        Random randomInstance = new Random();
        int randomNumber = randomInstance.nextInt(randomEmployers.size());
        String randomAccount = randomEmployers.get(randomNumber).toString();
        System.out.println("Random EMPLOYER Number: " + randomNumber);
        System.out.println("Random Employer Data: " + randomAccount);
        return Map.of(randomNumber, randomAccount);
    }

    public Map<Integer, String> getRandomStaffRegisterData(String companyId) throws JSONException {
        JSONArray randomStaff = getRandomStaffRegistrationData(companyId);
        Random randomInstance = new Random();
        int randomNumber = randomInstance.nextInt(randomStaff.length());
        String randomAccount = randomStaff.get(randomNumber).toString();
        System.out.println("Random STAFF Number: " + randomNumber);
        System.out.println("Random Staff Data: " + randomAccount);
        return Map.of(randomNumber, randomAccount);
    }

    public String getUserLoginData(int index) throws JSONException {
        JSONArray users = getUserLoginData();
        String account = users.get(index).toString();
        System.out.println("Index: " + index);
        System.out.println("User Login Data: " + account);
        return account;
    }
    public String getStaffLoginData(int index) throws JSONException {
        JSONArray staff = getStaffLoginData();
        String account = staff.get(index).toString();
        System.out.println("Index: " + index);
        System.out.println("Staff Login Data: " + account);
        return account;
    }

    public String getEmployerLoginData(int index) throws JSONException {
        JSONArray employers = getEmployerLoginData();
        String account = employers.get(index).toString();
        System.out.println("Index: " + index);
        System.out.println("Employer Login Data: " + account);
        return account;
    }

    public String getRandomCompany(String username) throws JSONException {
        List<JSONObject> randomCompany =  getRandomCompanyData(username);
        Random randomInstance = new Random();
        int randomNumber = randomInstance.nextInt(randomCompany.size());
        String company = randomCompany.get(randomNumber).toString();
        System.out.println("RANDOM COMPANY GENERATED: " + company);
        return company;
    }

    public Map<Integer, String> generateRandomEmployer() throws JSONException {
        Map<Integer, String> randomAccountData = getRandomEmployerRegisterData();
        int randomNumber = randomAccountData.keySet().iterator().next();
        String randomEmployer1 = randomAccountData.values().iterator().next();
        System.out.println("DEBUG: MAP DATA: " + randomAccountData);
        System.out.println("DEBUG: EMPLOYER DATA: " + randomEmployer1);
        return Map.of(randomNumber, randomEmployer1);
    }

    public Map<Integer, String> generateRandomStaff(String companyId) throws JSONException {
        Map<Integer, String> randomAccountData = getRandomStaffRegisterData(companyId);
        int randomNumber = randomAccountData.keySet().iterator().next();
        String randomStaff = randomAccountData.values().iterator().next();
        System.out.println("DEBUG: MAP DATA: " + randomAccountData);
        System.out.println("DEBUG: STAFF DATA: " + randomStaff);
        return Map.of(randomNumber, randomStaff);
    }

    public Map<Integer, String> generateRandomUser(String companyId) throws JSONException {
        Map<Integer, String> randomAccountData = getRandomUserRegisterData();
        int randomNumber = randomAccountData.keySet().iterator().next();
        String randomUser = randomAccountData.values().iterator().next();
        System.out.println("DEBUG: MAP DATA: " + randomAccountData);
        System.out.println("DEBUG: USER DATA: " + randomUser);
        return Map.of(randomNumber, randomUser);
    }

}