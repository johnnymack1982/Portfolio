//
//  Account.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import UIKit

public class Account: Codable {
    
    
    
    
    // Class properties
    var familyName: String
    var streetAddress: String
    var postalCode: Int
    var masterEmail: String
    var masterPassword: String
    var parents: [Parent]
    var children: [Child]
    
    
    
    // Constructor
    init(FamilyName familyName: String, StreetAddress streetAddress: String, PostalCode postalCode: Int, MasterEmail masterEmail: String,
         MasterPassword masterPassword: String) {
        self.familyName = familyName
        self.streetAddress = streetAddress
        self.postalCode = postalCode
        self.masterEmail = masterEmail
        self.masterPassword = masterPassword
        self.parents = [Parent]()
        self.children = [Child]()
    }
    
    
    
    // Getters / Setters
    func getFamilyName() -> String {
        return familyName
    }
    
    func setFamilyName(familyName: String) {
        self.familyName = familyName
    }
    
    func getStreetAddress() -> String {
        return streetAddress
    }
    
    func setStreetAddress(streetAddress: String) {
        self.streetAddress = streetAddress
    }
    
    func getPostalCode() -> Int {
        return postalCode
    }
    
    func setPostalCode(postalCode: Int) {
        self.postalCode = postalCode
    }
    
    func getMasterEmail() -> String {
        return masterEmail
    }
    
    func setMasterEmail(masterEmail: String) {
        self.masterEmail = masterEmail
    }
    
    func getMasterPassword() -> String {
        return masterPassword
    }
    
    func setMasterPassword(masterPassword: String) {
        self.masterPassword = masterPassword
    }
    
    func getParents() -> [Parent] {
        return parents
    }
    
    func setParents(parents: [Parent]) {
        self.parents = parents
    }
    
    func getChildren() -> [Child] {
        return children
    }
    
    func setChildren(children: [Child]) {
        self.children = children
    }
    
    
    
    // Cutom methods
    func addParent(parent: Parent) {
        self.parents.append(parent)
    }
    
    func addChild(child: Child) {
        self.children.append(child)
    }
}
