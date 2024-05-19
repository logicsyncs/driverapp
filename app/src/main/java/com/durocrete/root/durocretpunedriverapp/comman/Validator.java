package com.durocrete.root.durocretpunedriverapp.comman;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isValid(String s) {
        return !TextUtils.isEmpty(s);
    }

    public static boolean isValidMobileNumber(String mobileNo) {
        return !TextUtils.isEmpty(mobileNo) && mobileNo.length() > 6 && mobileNo.length() < 15;

        // return !TextUtils.isEmpty(mobileNo) && mobileNo.length() == 10;
    }

    public static boolean isValidOTP(String otp) {
        return !TextUtils.isEmpty(otp) && otp.length() > 0 && otp.length() < 7;
    }

    public static boolean isAlphaNumeric(String s) {
        boolean isValid = true;
        if (!s.matches(".*[0-9].*")) {
            isValid = false;
        } else if (!s.matches(".*[A-Z].*")) {
            if (!s.matches(".*[a-z].*")) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean isValidEmailId(String emailId) {
        return !TextUtils.isEmpty(emailId) && Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    public static boolean validateOptionalMobileNumber(String mobileNo) {
        return TextUtils.isEmpty(mobileNo) || mobileNo.length() == 10 && !mobileNo.startsWith("0");
    }

    public static boolean isValidOptionalEmailId(String emailId) {
        return TextUtils.isEmpty(emailId) || Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    public static boolean validatePincode(String pincode) {
        return !TextUtils.isEmpty(pincode) && pincode.length() == 6;
    }

    public static boolean isValidOptionalGstNumber(String gstin) {
        return TextUtils.isEmpty(gstin) || gstin.length() == 15;
    }

    public static boolean isValidGstNumber(String gstin) {
        return TextUtils.isEmpty(gstin) && gstin.length() == 15;
    }

    public static boolean isValidOptionalPhoneNumber(String phoneNo) {
        return TextUtils.isEmpty(phoneNo) || (phoneNo.length() >= 10 && phoneNo.length() <= 13);
    }

    public static boolean isValidPhoneNumber(String phoneNo) {
        return TextUtils.isEmpty(phoneNo) && (phoneNo.length() >= 10 && phoneNo.length() <= 13);
    }

    public static boolean isValidWithRegex(String s, String regex) {
        if (!isValid(s))
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean isValidMobileNumber(String phoneNumber, String countryCode1, String countryCode) {
        boolean isValid;
        if (phoneNumber.substring(0, 5).equals(countryCode1) || phoneNumber.substring(0, 2).equals(countryCode)) {
            if (phoneNumber.substring(0, 5).equals(countryCode1)) {
                isValid = phoneNumber.substring(5).length() == 7;
            } else if (phoneNumber.substring(0, 2).equals(countryCode)) {
                isValid = phoneNumber.substring(2).length() == 7;
            } else {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
}
