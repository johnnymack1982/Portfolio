//
//  Profile.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

public class Child: Codable {
    
    
    
    // Class properties
    var firstName: String = ""
    var dateOfBirth: Date
    var genderId: Int // 0 == Male, 1 == Female
    var profilePin: Int
    
    
    
    // Constructor
    init(FirstName firstName: String, DateOfBirth dateOfBirth: Date, GenderID genderId: Int, ProfilePIN profilePin: Int) {
        self.firstName = firstName
        self.dateOfBirth = dateOfBirth
        self.genderId = genderId
        self.profilePin = profilePin
    }
    
    
    
    // Getters / Setters
    func getFirstName() -> String {
        return firstName
    }
    
    func setFirstName(firstName: String) {
        self.firstName = firstName
    }
    
    func getDateOfBirth() -> Date {
        return dateOfBirth
    }
    
    func setDateOfBirth(dateOfBirth: Date) {
        self.dateOfBirth = dateOfBirth
    }
    
    func getGenderId() -> Int {
        return genderId
    }
    
    func setGenderId(genderId: Int) {
        self.genderId = genderId
    }
    
    func getProfilePin() -> Int {
        return profilePin
    }
    
    func setProfilePin(profilePin: Int) {
        self.profilePin = profilePin
    }
    
    
    
    // Custom methods
    func getProfileId() -> String {
        return firstName + String(profilePin)
    }
}
