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
    public static func validName(Name name: String?) -> Bool {
        return name != nil && name!.trimmingCharacters(in: .whitespacesAndNewlines) != ""
    }
    
    public static func validAddress(Address address: String?) -> Bool {
        if address == nil || address!.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            let regex = "^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$"
            
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            
            return test.evaluate(with: address!)
        }
    }
    
    public static func validPostalCode(PostalCode postalCode: String?) -> Bool {
        if postalCode == nil || postalCode?.trimmingCharacters(in: .whitespacesAndNewlines) == "" ||
            postalCode?.trimmingCharacters(in: .whitespacesAndNewlines).count != 5 {
            
            return false
        }
        
        else {
            return true
        }
    }
    
    public static func validEmail(Email email: String?) -> Bool {
        if email == nil || email?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            let regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
            
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            
            return test.evaluate(with: email)
        }
    }
    
    public static func validPassword(Password password: String?) -> Bool {
        if password == nil || password?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            let regex = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
            "$"
            
            let test = NSPredicate(format:"SELF MATCHES %@", regex)
            
            return test.evaluate(with: password)
        }
    }
    
    public static func passwordsMatch(password: String?, passwordConfirm: String?) -> Bool {
        if password == nil || password?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || passwordConfirm == nil || passwordConfirm?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || passwordConfirm != password {
            
            return false
        }
        
        else {
            return true
        }
    }
    
    public static func validPin(pin: String?) -> Bool {
        if pin == nil || pin?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return false
        }
        
        else {
            if pin!.count == 4 {
                return true
            }
            
            else {
                return false
            }
        }
    }
    
    public static func pinsMatch(pin: String?, pinConfirm: String?) -> Bool {
        if pin == nil || pin?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || pinConfirm == nil || pinConfirm?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || pinConfirm != pin {
            
            return false
        }
            
        else {
            return true
        }
    }
}
