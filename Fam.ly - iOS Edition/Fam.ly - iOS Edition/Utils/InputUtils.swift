//
//  InputUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

public class InputUtils {
    
    
    
    // Custom methods
    // Custom method to validate name input
    public static func validName(Name name: String?) -> Bool {
        return name != nil && name!.trimmingCharacters(in: .whitespacesAndNewlines) != ""
    }
    
    // Custom method to validate address input
    public static func validAddress(Address address: String?) -> Bool {
        
        // If input is blank it's invalid
        if address == nil || address!.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            
            // Regular expression for valid US street address
            let regex = "^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$"
            
            // Compare input to regex and return result
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            return test.evaluate(with: address!)
        }
    }
    
    // Custom method to validate postal code input
    public static func validPostalCode(PostalCode postalCode: String?) -> Bool {
        
        // If postal code is ot 5 digits, it is invalid
        if postalCode == nil || postalCode?.trimmingCharacters(in: .whitespacesAndNewlines) == "" ||
            postalCode?.trimmingCharacters(in: .whitespacesAndNewlines).count != 5 {
            
            return false
        }
        
        // Otherwise, input is valid
        else {
            return true
        }
    }
    
    // Custom method to validate e-mail input
    public static func validEmail(Email email: String?) -> Bool {
        
        // If input is blank, it is invalid
        if email == nil || email?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            
            // Regular expression for valid e-mail address
            let regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
            
            // Compare input to regex and return input
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            return test.evaluate(with: email)
        }
    }
    
    // Custom method to validate password input
    public static func validPassword(Password password: String?) -> Bool {
        
        // If input is blank, it is invalid
        if password == nil || password?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        // Regular expression for valid password
        else {
            let regex = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
            "$"
            
            // Compare input to regex and return result
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            return test.evaluate(with: password)
        }
    }
    
    // Custom method to validate if password confirmation matches password
    public static func passwordsMatch(password: String?, passwordConfirm: String?) -> Bool {
        
        // If input matches password, input is valid
        if password == nil || password?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || passwordConfirm == nil || passwordConfirm?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || passwordConfirm != password {
            
            return false
        }
        
        // Otherwise, input is invalid
        else {
            return true
        }
    }
    
    // Custom method to validate pin input
    public static func validPin(pin: String?) -> Bool {
        
        // If input is blank, it is invalid
        if pin == nil || pin?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        // If input is 4 digits it is valid
        else {
            if pin!.count == 4 {
                return true
            }
            
            // Otherwise, input is invalid
            else {
                return false
            }
        }
    }
    
    // Custom method to validate if pins match
    public static func pinsMatch(pin: String?, pinConfirm: String?) -> Bool {
        
        // If confirmation does not match pin input, it is invalid
        if pin == nil || pin?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || pinConfirm == nil || pinConfirm?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || pinConfirm != pin {
            
            return false
        }
            
        // Otherwise, input is valid
        else {
            return true
        }
    }
}
