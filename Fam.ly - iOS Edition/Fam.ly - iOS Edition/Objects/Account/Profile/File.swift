//
//  File.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

class Child: Profile {
    
    
    
    // Constructors
    override init(FirstName firstName: String, DateOfBirth dateOfBirth: Date, GenderID genderId: Int, ProfilePIN profilePin: Int) {
        super.init(FirstName: firstName, DateOfBirth: dateOfBirth, GenderID: genderId, ProfilePIN: profilePin)
    }
}
