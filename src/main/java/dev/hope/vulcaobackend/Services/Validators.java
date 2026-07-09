package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.DTOs.ImageMetadata;
import dev.hope.vulcaobackend.DTOs.ProductDTO;
import dev.hope.vulcaobackend.Properties.*;
import dev.hope.vulcaobackend.Properties.Currency;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;


import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class Validators {

    public static void debugInput(String field){
        System.out.println(field.toUpperCase() + "=[" + field + "] len=" + (field == null ? -1 : field.length()));
    }



    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_-]{8,20}$");

    // Allows letters (\p{L}) and digits (\d) at the start, middle, and end.
    private static final Pattern FIELD_PATTERN =
            Pattern.compile("^[\\p{L}\\d][\\p{L}\\d '-]{1,20}[\\p{L}\\d]$|^[\\p{L}\\d]$");




    public static void validateUsername(String username){
        if(!USERNAME_PATTERN.matcher(username).matches()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome de usuário deve ter entre 8 e 20 caracteres (use apenas letras, números, hífen ou sublinhado).");
        }
    }
    public static void validateName(String firstName, String lastName){

        if(!FIELD_PATTERN.matcher(firstName).matches()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "O primeiro nome não pode estar vazio e deve conter apenas caracteres em português/inglês com tamanho máximo de 20.");
        }
        if(!FIELD_PATTERN.matcher(lastName).matches()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "O sobrenome não pode estar vazio e deve conter apenas caracteres em português/inglês com tamanho máximo de 20.");
        }
    }

    public static  void validateEmail(String email){
        Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Por favor, insira um e-mail válido (exemplo: nome@dominio.com).");
        }
        String firstPart = email.split("@")[0];
        if(firstPart.charAt(firstPart.length()-1) == '.'){
            throw new ApiException(HttpStatus.BAD_REQUEST, "O e-mail não pode ter um ponto final logo antes do @.");
        }
    }

    public static  void validatePhoneNumber(String phoneNumber){
        // Validates: Optional '+', allows spaces anywhere, max 20 total characters, min 7 digits.
        Pattern PHONE_PATTERN = Pattern.compile("^(?=^[+\\d ]{7,20}$)\\+?\\d+(?: \\d+)*$");

        if(!PHONE_PATTERN.matcher(phoneNumber).matches()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Por favor, insira um número de telefone válido, sem espaços, contendo entre 7 e 20 dígitos.\nEx:+238 111 9090");
        }
    }

    public static  void validatePassword(String password){
        if(password.length() < 8 || password.length() != password.trim().length()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 8 caracteres e não pode conter espaços no início ou no fim.");
        }
    }

    public static void validateAddress(Address address) {
        if (address == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O endereço não pode ser nulo.");
        }

        String island = address.getIsland();
        if (island != null && !island.isBlank() && !FIELD_PATTERN.matcher(island).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome de ilha(opcional) deve conter apenas caracteres válidos (letras, espaços, hífen ou sublinhado).");
        }

        // Validação da Cidade (Obrigatório)
        String city = address.getCity();
        if (city == null || city.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A cidade não pode estar vazia.");
        }
        if (!FIELD_PATTERN.matcher(city).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A cidade deve conter apenas caracteres válidos (letras, espaços, hífen ou sublinhado).");
        }

        // Validação da Zona/Bairro (Obrigatório)
        String zone = address.getZone();
        if (zone != null && !zone.isBlank() && !FIELD_PATTERN.matcher(zone).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A Zona/Bairo(Opcional) deve conter apenas caracteres válidos (letras, espaços, hífen ou sublinhado).");
        }

        // Validação da Rua (Opcional - Só valida o padrão se NÃO estiver vazia)
        String street = address.getStreet();
        if (street != null && !street.isBlank() && !FIELD_PATTERN.matcher(street).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A rua deve conter apenas caracteres válidos (letras, espaços, hífen ou sublinhado).");
        }

        String state = address.getState();
        if (street != null && !street.isBlank() && !FIELD_PATTERN.matcher(state).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O estado deve conter apenas caracteres válidos (letras, espaços, hífen ou sublinhado).");
        }

        // Validação do Código Postal (Opcional - Só valida o padrão se NÃO estiver vazia)
        Pattern POSTAL_CODE_PATTERN = Pattern.compile("^\\d{4}$");
        String postalCode = address.getPostalCode();
        if (postalCode != null && !postalCode.isBlank() && !POSTAL_CODE_PATTERN.matcher(postalCode).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O código postal deve conter apenas numeros de 4 digitos.");
        }
    }

    public static void validateCompanyName(String name){
        String COMPANY_NAME_PATTERN = "^[\\p{L}0-9 +_.\\-/()&',]{0,100}$";
        if(name == null || name.isEmpty()){
             throw new ApiException(HttpStatus.BAD_REQUEST, "O nome da empresa deve ser preenchido.");
        }
        debugInput(name);
        if(!name.matches(COMPANY_NAME_PATTERN)){
            if (name.length() > 100) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "O nome da empresa é muito longo. O limite máximo é de 100 caracteres.");
            }
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome da empresa contém caracteres inválidos. Utilize apenas letras, números e pontuação básica.");

        }


    }

    public static void validateCompanyDescription(String description){
        String COMPANY_NAME_PATTERN = "^[\\p{L}0-9 +_.\\-/()&',]{0,500}$";
        if(description == null || description.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"A descrição da empresa deve ser preenchida.");
        }
        debugInput(description);
        if(!description.matches(COMPANY_NAME_PATTERN)){
            if (description.length() > 500) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "A  descrição da empresa é muito longo. O limite máximo é de 500 caracteres.");
            }
            throw new ApiException(HttpStatus.BAD_REQUEST,"A descrição da empresa é inválida. Utilize apenas letras e números.");
        }
    }

    public static void validateCompanyFaxNumber(String fax){
        debugInput(fax);
        String FAX_FORMAT = "^\\d{0,50}$";
        if(!fax.matches(FAX_FORMAT)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"O fax da empresa é inválido. Utilize apenas números (máximo 50 dígitos).");
        }
    }

    public static void validateCompanyTaxId(String taxId){
        debugInput(taxId);
        String TAX_FORMAT = "^\\d{0,50}$";
        if(!taxId.matches(TAX_FORMAT)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"O fax da empresa é inválido. Utilize apenas números (máximo 50 dígitos).");
        }
    }

    public static void assertEnumFields( String currency, String unit) {
//        try {
//            System.out.println("Product Type"+ type);
//            ProductType.valueOf(type);
//        } catch (IllegalArgumentException | NullPointerException e) {
//            throw new ApiException(HttpStatus.BAD_REQUEST, "O tipo de oferta selecionado é inválido.");
//        }

        try {
            Currency.valueOf(currency); // Fixed: using parameter instead of product.getCurrency()
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A moeda selecionada é inválida.");
        }

        try {
            Unit.valueOf(unit); // Fixed: using parameter instead of product.getUnit()
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A unidade de medida selecionada é inválida.");
        }
    }


    public static void validateProduct(ProductDTO product) {
        if (product == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Os dados do produto não foram enviados.");
        }

        assertEnumFields(product.getCurrency(), product.getUnit());

        String name = product.getName();
        Double price = product.getPrice();
        Integer stock = product.getStock();
        String description = product.getDescription();

        debugInput(name);
        debugInput(price != null ? price.toString() : "null");
        debugInput(stock != null ? stock.toString() : "null");
        debugInput(description);
        debugInput(product.getBusinessId());
        debugInput(product.getCreatorId());
        debugInput(product.getUpdatedBy());
        debugInput(product.getCurrency());
        debugInput(product.getUnit());

        if (name == null || name.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome (ou título) do produto é obrigatório.");
        }
        if (price == null || price < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O preço do produto é obrigatório e não pode ser menor que zero.");
        }
        if (stock == null || stock < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A quantidade em estoque é obrigatória e não pode ser menor que zero.");
        }
        if(!product.getExpirationDate().isAfter(LocalDate.now())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "A data de validade do produto deve ser posterior à data de hoje.");
        }

        // 4. Pattern Validation
        String safeTextPattern = "^[\\p{L}0-9 +_.\\-/()&',]+$";

        if (name.length() > 20 || !name.matches(safeTextPattern)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "O nome deve ter no máximo 20 caracteres e não pode conter símbolos especiais inválidos.");
        }

        // Description is optional; validate only if provided
        if (description != null && !description.isBlank()) {
            if (description.length() > 500 || !description.matches(safeTextPattern)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "A descrição deve ter no máximo 500 caracteres e não pode conter símbolos especiais inválidos.");
            }
        }

        List<String> imgS3UrlKeys = product.getImgS3UrlKeys() != null
                ? new ArrayList<>(product.getImgS3UrlKeys())
                : new ArrayList<>();

        List<ImageMetadata> imagesMetadata = product.getImagesMetadata() != null
                ? new ArrayList<>(product.getImagesMetadata())
                : new ArrayList<>();
    }


}
