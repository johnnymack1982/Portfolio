//
//  Parent.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

public class Parent: Codable {
    
    
    
    // Class properties
    var firstName: String = ""
    var dateOfBirth: Date
    var genderId: Int // 0 == Male, 1 == Female
    var profilePin: Int
    var roleId: Int // 0 == Mother, 1 == Father, 2 == Grandmother, 3 == Grandfather, 4 == Aunt, 5 == Uncle
    
    
    
    // Constructor
    init(FirstName firstName: String, DateOfBirth dateOfBirth: Date, GenderID genderId: Int, ProfilePIN profilePin: Int, RoleID roleId: Int) {
        self.firstName = firstName
        self.dateOfBirth = dateOfBirth
        self.genderId = genderId
        self.profilePin = profilePin
        self.roleId = roleId
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
    
    func getRoleId() -> Int {
        return roleId
    }
    
    func setRoleId(roleId: Int) {
        self.roleId = roleId
    }
    
    
    
    // Custom methods
    func getProfileId() -> String {
        let interval = dateOfBirth.timeIntervalSince1970
        let date = Int(interval)
        
        return firstName + String(date) + String(profilePin)
    }
}
