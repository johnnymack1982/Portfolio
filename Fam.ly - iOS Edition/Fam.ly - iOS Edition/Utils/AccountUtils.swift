//
//  AccountUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import UIKit
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage

public class AccountUtils {
    
    
    
    // Class properties
    private static let COLLECTION_ACCOUNTS = "accounts"
    private static let PHOTOS_REFERENCE = "photos/"
    
    
    
    // Custom methods
    public static func saveAccount (Account account: Account, Photo photo: UIImage?) {
        let encodedObject = try? JSONEncoder().encode(account)
        
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
            
            do {
                // Attempt to save JSON string to file
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
                
            catch {
                print("Failed to write data to file")
            }
        }
        
        if photo != nil {
            if let imageData = photo!.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg")
                try? imageData.write(to: fileName)
            }
        }
    }
    
    public static func saveProfile(Parent parent: Parent?, Child child: Child?) {
        if parent != nil {
            let encodedObject = try? JSONEncoder().encode(parent)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
        }
            
        else if child != nil {
            let encodedObject = try? JSONEncoder().encode(child)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
        }
    }
    
    public static func loadParent() -> Parent? {
        let fileName = getDocumentsDirectory().appendingPathComponent("profile.fam")
        
        var parent: Parent?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                parent = try? JSONDecoder().decode(Parent.self, from: jsonData)
            }
        }
            
        catch {
            print("Error loading parent from file")
        }
        
        return parent
    }
    
    public static func loadChild() -> Child? {
        let fileName = getDocumentsDirectory().appendingPathComponent("profile.fam")
        
        var child: Child?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                child = try? JSONDecoder().decode(Child.self, from: jsonData)
            }
        }
            
        catch {
            print("Error loading child from file")
        }
        
        return child
    }
    
    public static func loadAccount() -> Account? {
        let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
        
        var account: Account?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                account = try? JSONDecoder().decode(Account.self, from: jsonData)
            }
        }
            
        catch {
            print("Error loading account from file")
            
            do {
                try Auth.auth().signOut()
            }
            
            catch {
                
            }
        }
        
        return account
    }
    
    public static func loadAccountPhoto(FamilyPhoto familyPhoto: UIImageView) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoReference = storageRef.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
        
        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
            if error != nil {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg").absoluteString
                familyPhoto.image = UIImage(contentsOfFile: fileName)
                
                if familyPhoto.image == nil {
                    familyPhoto.image = UIImage(named: "Family Icon Large")
                }
            }
                
            else {
                let image = UIImage(data: data!)
                familyPhoto.image = image
            }
        }
    }
    
    public static func loadProfilePhoto(ProfileId profileId: String, ProfilePhoto profilePhoto: UIImageView) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoReference = storageRef.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + profileId + ".jpg")
        
        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
            if error != nil {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg").absoluteString
                profilePhoto.image = UIImage(contentsOfFile: fileName)
                
                if profilePhoto.image == nil {
                    profilePhoto.image = UIImage(named: "Male Icon Large")
                }
            }
                
            else {
                let image = UIImage(data: data!)
                profilePhoto.image = image
            }
        }
    }
    
    static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    public static func createAccount(Account account: Account, Photo photo: UIImage) {
        Auth.auth().createUser(withEmail: account.getMasterEmail(), password: account.getMasterPassword()) { (authResult, error) in
            if error != nil {
                print(error!)
            }
                
            else {
                let database = Firestore.firestore()
                
                let familyName = account.getFamilyName()
                let masterEmail = account.getMasterEmail()
                let masterPassword = account.getMasterPassword()
                let streetAddress = account.getStreetAddress()
                let postalCode = account.getPostalCode()
                
                let accountDocData: [String: Any] = [
                    "familyName": familyName,
                    "masterEmail": masterEmail,
                    "masterPassword": masterPassword,
                    "streetAddress": streetAddress,
                    "postalCode": postalCode,
                ]
                
                let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
                accountDoc.setData(accountDocData)
                
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
                
                let imageData = photo.jpegData(compressionQuality: 0.5)
                
                photoReference.putData(imageData!)
            }
        }
    }
    
    public static func addParent (Parent parent: Parent, Photo photo: UIImage) {
        let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
        
        var account: Account?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                account = try? JSONDecoder().decode(Account.self, from: jsonData)
            }
        }
            
        catch {
            print("Error loading account from file")
        }
        
        if account != nil {
            account?.addParent(parent: parent)
            
            let encodedObject = try? JSONEncoder().encode(account)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
            
            let database = Firestore.firestore()
            
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            
            let profilesRef: CollectionReference = accountDoc.collection("profiles")
            
            let parentDocData: [String: Any] = [
                "firstName": parent.getFirstName(),
                "dateOfBirth": parent.getDateOfBirth(),
                "genderId": parent.getGenderId(),
                "profilePin": parent.getProfilePin(),
                "roleId": parent.getRoleId()
            ]
            
            let profileDoc: DocumentReference = profilesRef.document((Auth.auth().currentUser!.uid + parent.getProfileId()))
            profileDoc.setData(parentDocData)
            
            
            if let imageData = photo.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile_photo")
                try? imageData.write(to: fileName)
            }
            
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + parent.getProfileId() + ".jpg")
            
            let imageData = photo.jpegData(compressionQuality: 0.5)
            
            photoReference.putData(imageData!)
            
            if account?.parents.count == 1 {
                let encodedObject = try? JSONEncoder().encode(parent)
                
                if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                    let fileName = getDocumentsDirectory().appendingPathComponent("profile.fam")
                    
                    do {
                        // Attempt to save JSON string to file
                        try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                    }
                        
                    catch {
                        print("Failed to write data to file")
                    }
                }
            }
        }
    }
    
    public static func addChild (Child child: Child, Photo photo: UIImage) {
        let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
        
        var account: Account?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                account = try? JSONDecoder().decode(Account.self, from: jsonData)
            }
        }
            
        catch {
            print("Error loading account from file")
        }
        
        if account != nil {
            account?.addChild(child: child)
            
            let encodedObject = try? JSONEncoder().encode(account)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
            
            let database = Firestore.firestore()
            
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            
            let profilesRef: CollectionReference = accountDoc.collection("profiles")
            
            let parentDocData: [String: Any] = [
                "firstName": child.getFirstName(),
                "dateOfBirth": child.getDateOfBirth(),
                "genderId": child.getGenderId(),
                "profilePin": child.getProfilePin(),
            ]
            
            let profileDoc: DocumentReference = profilesRef.document((Auth.auth().currentUser!.uid + child.getProfileId()))
            profileDoc.setData(parentDocData)
            
            
            if let imageData = photo.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile_photo")
                try? imageData.write(to: fileName)
            }
            
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + child.getProfileId() + ".jpg")
            
            let imageData = photo.jpegData(compressionQuality: 0.5)
            
            photoReference.putData(imageData!)
        }
    }
    
    public static func updateProfile(FamilyName familyName: String, StreetAddress streetAddress: String, PostalCode postalCode: Int, Email email: String, Password password: String, Photo photo: UIImage?) {
        
        let account = Account(FamilyName: familyName, StreetAddress: streetAddress, PostalCode: postalCode, MasterEmail: email, MasterPassword: password)
        
        if Auth.auth().currentUser != nil {
            let database = Firestore.firestore()
            
            let familyName = account.getFamilyName()
            let masterEmail = account.getMasterEmail()
            let masterPassword = account.getMasterPassword()
            let streetAddress = account.getStreetAddress()
            let postalCode = account.getPostalCode()
            
            let accountDocData: [String: Any] = [
                "familyName": familyName,
                "masterEmail": masterEmail,
                "masterPassword": masterPassword,
                "streetAddress": streetAddress,
                "postalCode": postalCode,
            ]
            
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            accountDoc.setData(accountDocData)
            
            if photo != nil {
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
                
                let imageData = photo!.jpegData(compressionQuality: 0.5)
                
                photoReference.putData(imageData!)
            }
            
            saveAccount(Account: account, Photo: photo!)
        }
    }
    
    public static func uploadFamilyPhoto(Photo photo: UIImage) {
        if Auth.auth().currentUser != nil {
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
            
            let imageData = photo.jpegData(compressionQuality: 0.5)
            
            photoReference.putData(imageData!)
        }
    }
    
    public static func uploadProfilePhoto(Parent parent: Parent?, Child child: Child?, Photo photo: UIImage) {
        var profileId: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
        }
            
        else if child != nil {
            profileId = child?.getProfileId()
        }
        
        let photoStorage = Storage.storage()
        let storageReference = photoStorage.reference()
        let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + profileId! + ".jpg")
        
        let imageData = photo.jpegData(compressionQuality: 0.5)
        
        photoReference.putData(imageData!)
    }
    
    public static func deleteProfile(ProfileId profileId: String) {
        print(Auth.auth().currentUser!.uid + profileId)
        
        let database = Firestore.firestore()
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId).delete() { err in
            if let err = err {
                print("Error removing document: \(err)")
            }
                
            else {
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + profileId + ".jpg")
                
                photoReference.delete()
            }
        }
    }
    
    public static func getUpdatedProfiles() {
        let database = Firestore.firestore()
        
        let account = loadAccount()
        account!.setParents(parents: [Parent]())
        account!.setChildren(children: [Child]())
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            }
                
            else {
                for document in querySnapshot!.documents {
                    if document.get("roleId") != nil {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        let roleId = document.get("roleId") as? Int
                        
                        let parent = Parent(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                        
                        account!.addParent(parent: parent)
                    }
                        
                    else  {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        
                        let child = Child(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                        
                        account!.addChild(child: child)
                    }
                }
                
                saveAccount(Account: account!, Photo: nil)
            }
        }
    }
    
    public static func getUpdatedFamily() {
        let database = Firestore.firestore()
        let account = loadAccount()
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).getDocument { (document, error) in
            if let document = document, document.exists {
                let familyName = document.get("familyName") as? String
                let masterEmail = document.get("masterEmail") as? String
                let masterPassword = document.get("masterPassword") as? String
                let postalCode = document.get("postalCode") as? Int
                let streetAddress = document.get("streetAddress") as? String
                
                account!.setFamilyName(familyName: familyName!)
                account!.setMasterEmail(masterEmail: masterEmail!)
                account!.setMasterPassword(masterPassword: masterPassword!)
                account!.setPostalCode(postalCode: postalCode!)
                account!.setStreetAddress(streetAddress: streetAddress!)
                
                saveAccount(Account: account!, Photo: nil)
            }
            
            else {
                print("Document does not exist")
            }
        }
    }
    
    public static func listenForUpdates() {
        let database = Firestore.firestore()
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).addSnapshotListener { documentSnapshot, error in
            guard let document = documentSnapshot else {
                print("Error fetching document: \(error!)")
                return
            }
            
            if document.exists {
                let account = loadAccount()
                
                let familyName = document.get("familyName") as? String
                let masterEmail = document.get("masterEmail") as? String
                let masterPassword = document.get("masterPassword") as? String
                let postalCode = document.get("postalCode") as? Int
                let streetAddress = document.get("streetAddress") as? String
                
                account!.setFamilyName(familyName: familyName!)
                account!.setMasterEmail(masterEmail: masterEmail!)
                account!.setMasterPassword(masterPassword: masterPassword!)
                account!.setPostalCode(postalCode: postalCode!)
                account!.setStreetAddress(streetAddress: streetAddress!)
                
                saveAccount(Account: account!, Photo: nil)
            }
                
            else {
                print("Document does not exist")
            }
        }
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").addSnapshotListener { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            }
                
            else {
                let account = loadAccount()
                account!.setParents(parents: [Parent]())
                account!.setChildren(children: [Child]())
                
                for document in querySnapshot!.documents {
                    if document.get("roleId") != nil {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        let roleId = document.get("roleId") as? Int
                        
                        let parent = Parent(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                        
                        account!.addParent(parent: parent)
                    }
                        
                    else  {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        
                        let child = Child(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                        
                        account!.addChild(child: child)
                    }
                }
                
                saveAccount(Account: account!, Photo: nil)
            }
        }
    }
}

