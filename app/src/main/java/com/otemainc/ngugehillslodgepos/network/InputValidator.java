package com.otemainc.ngugehillslodgepos.network;

import com.google.common.base.Strings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    //Will contain the boolean value of the validation response
    boolean response;
    private Pattern pattern;
    private Matcher matcher;
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{6,40})";
    private static final String PASSWORD_UPPER_CASE_PATTERN = ".*[A-Z].*";
    private static final String PASSWORD_LOWER_CASE_PATTERN = ".*[a-z].*";
    private static final String PASSWORD_NUMBER_PATTERN = ".*\\d.*";
    private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,60}$";
    private static final String PHONE_PATTERN = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

    /**This method should return true if the text is not empty and not less that four characters long
     * and false if otherwise
     *
     */
    public boolean validateText(String text){
        response = !text.equals("") && text.length() >= 4;
        return  response;
    }
    /**This method should return true if the input is not empty and not less that four characters long
     * and false if otherwise
     *
     */
    public boolean validatePasswordLength(String password){
        response = !password.equals("") && password.length() >= 4;
        return  response;
    }

    /**This method returns true if email is valid and false if not
     *
     */
    public boolean validateEmail(String email){
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        response = matcher.matches();
        return response;
    }

    /** This method returns true if password is valid and false if not
     * Valid password is at least 6 characters long and is composed of atleast one letter,
     * and at least one number
     *
     */
    public boolean validatePassword(String password){
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        response = matcher.matches();
        return response;
    }

    /**This method returns true if phone number is valid and false if not
     *
     */
    public boolean validatePhone(String phone){
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        response = matcher.matches();
        return response;
    }

    /**This method returns true if the input has a lowercase letter and false if not
     *
     */
    public boolean validateForLowerCase(String input){
        pattern = Pattern.compile(PASSWORD_LOWER_CASE_PATTERN);
        matcher = pattern.matcher(input);
        response = matcher.matches();
        return response;
    }

    /**This method returns true if the input has a uppercase letter and false if not
     *
     */
    public boolean validateForUpperCase(String input){
        pattern = Pattern.compile(PASSWORD_UPPER_CASE_PATTERN);
        matcher = pattern.matcher(input);
        response = matcher.matches();
        return response;
    }

    /**This method returns true if the input has a lowercase letter and false if not
     *
     */
    public boolean validateForNumber(String input){
        pattern = Pattern.compile(PASSWORD_NUMBER_PATTERN);
        matcher = pattern.matcher(input);
        response = matcher.matches();
        return response;
    }

    public String updatePhoneNumber(String userPhone) {
        if(userPhone.startsWith("0")){
            return userPhone;
        }else {
            return Strings.padStart(userPhone,10,'0');
        }
    }
}
