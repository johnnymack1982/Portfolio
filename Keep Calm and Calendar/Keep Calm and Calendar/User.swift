//
//  User.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/16/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation

class User {
    // MARK: - Stored Properties
    var userEmail: String
    var userPassword: String
    var parentCode: String
    var userID: String
    var events: [Event]
    
    
    
    // MARK: - Initializers
    init(userEmail: String, userPassword: String, parentCode: String, userID: String, events: [Event]){
        self.userEmail = userEmail
        self.userPassword = userPassword
        self.parentCode = parentCode
        self.userID = userID
        self.events = events
    }
}
