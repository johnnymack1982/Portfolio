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
    // Custom method to save account to file
    public static func saveAccount (Account account: Account, Photo photo: UIImage?) {
        
        // Encode account data and attempt to save
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
        
        // If a photo exists, save to file
        if photo != nil {
            if let imageData = photo!.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg")
                try? imageData.write(to: fileName)
            }
        }
    }
    
    // Custom method to save profile to file
    public static func saveProfile(Parent parent: Parent?, Child child: Child?) {
        
        // Save parent to file
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
            
        // Save child to file
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
    
    // Custom method to load parent from file
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
    
    // Custom method to load child from file
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
    
    // Custom method to load account from file
    public static func loadAccount() -> Account? {
        let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
        var account: Account?
        
        do {
            let jsonString = try String(contentsOf: fileName)
            if let jsonData = jsonString.data(using: .utf8) {
            account = try? JSONDecoder().decode(Account.self, from: jsonData)
            }
        }
            
        // If unable to load, sign out
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
    
    // Custom method to load family photo
    public static func loadAccountPhoto(FamilyPhoto familyPhoto: UIImageView) {
        
        // Reference remote storage location and attempt to download
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoReference = storageRef.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
            
            // If successful, populaate UI with photo
            if error != nil {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg").absoluteString
                familyPhoto.image = UIImage(contentsOfFile: fileName)
                
                // If no photo exists, populate default image
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
    
    // Custom method to load profile photo
    public static func loadProfilePhoto(ProfileId profileId: String, ProfilePhoto profilePhoto: UIImageView) {
        
        // Reference remote storage location and attempt to download
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoReference = storageRef.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + profileId + ".jpg")
        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
            
            // If successful, populate UI with photo
            if error != nil {
                let fileName = getDocumentsDirectory().appendingPathComponent("family_photo.jpg").absoluteString
                profilePhoto.image = UIImage(contentsOfFile: fileName)
                
                // If no photo exists, populate default image
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
    
    // Custom method to get documents directory for app
    static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    // Custom method to create new family account
    public static func createAccount(Account account: Account, Photo photo: UIImage) {
        
        // Attempt to create new firebase account with user input
        Auth.auth().createUser(withEmail: account.getMasterEmail(), password: account.getMasterPassword()) { (authResult, error) in
            if error != nil {
                print(error!)
            }
                
            // If successful...
            else {
                
                // Reference database
                let database = Firestore.firestore()
                
                // Map account for upload
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
                
                // Upload account data
                let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
                accountDoc.setData(accountDocData)
                
                // Reference photo remote storate location ad upload image data
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
                let imageData = photo.jpegData(compressionQuality: 0.5)
                photoReference.putData(imageData!)
            }
        }
    }
    
    // Custom method to add parent to account
    public static func addParent (Parent parent: Parent, Photo photo: UIImage) {
        
        // Reference file location
        let fileName = getDocumentsDirectory().appendingPathComponent("account.fam")
        
        // Load account from file
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
        
        // If account exists...
        if account != nil {
            
            // Add parent to account
            account?.addParent(parent: parent)
            
            // Save account to file
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
            
            // Reference database location
            let database = Firestore.firestore()
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            let profilesRef: CollectionReference = accountDoc.collection("profiles")
            
            // Map profile for upload
            let parentDocData: [String: Any] = [
                "firstName": parent.getFirstName(),
                "dateOfBirth": parent.getDateOfBirth(),
                "genderId": parent.getGenderId(),
                "profilePin": parent.getProfilePin(),
                "roleId": parent.getRoleId()
            ]
            
            // Upload data
            let profileDoc: DocumentReference = profilesRef.document((Auth.auth().currentUser!.uid + parent.getProfileId()))
            profileDoc.setData(parentDocData)
            
            // Write profile photo to file
            if let imageData = photo.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile_photo")
                try? imageData.write(to: fileName)
            }
            
            // Reference remote storage location and upload profile photo
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + parent.getProfileId() + ".jpg")
            let imageData = photo.jpegData(compressionQuality: 0.5)
            photoReference.putData(imageData!)
            
            // If this is the first parent account, save it to file
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
    
    // Custom method to add child to account
    public static func addChild (Child child: Child, Photo photo: UIImage) {
        
        // Reference file location and load account
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
        
        // If account exists...
        if account != nil {
            
            // Add child to account
            account?.addChild(child: child)
            
            // Save account to file
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
            
            // Reference databse location
            let database = Firestore.firestore()
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            let profilesRef: CollectionReference = accountDoc.collection("profiles")
            
            // Map profile for upload
            let parentDocData: [String: Any] = [
                "firstName": child.getFirstName(),
                "dateOfBirth": child.getDateOfBirth(),
                "genderId": child.getGenderId(),
                "profilePin": child.getProfilePin(),
            ]
            
            // Upload profile data
            let profileDoc: DocumentReference = profilesRef.document((Auth.auth().currentUser!.uid + child.getProfileId()))
            profileDoc.setData(parentDocData)
            
            // Save profile photo to file
            if let imageData = photo.jpegData(compressionQuality: 0.5) {
                let fileName = getDocumentsDirectory().appendingPathComponent("profile_photo")
                try? imageData.write(to: fileName)
            }
            
            // Upload profile photo to remote storage
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + child.getProfileId() + ".jpg")
            let imageData = photo.jpegData(compressionQuality: 0.5)
            photoReference.putData(imageData!)
        }
    }
    
    // Custom method to update selected profile
    public static func updateProfile(FamilyName familyName: String, StreetAddress streetAddress: String, PostalCode postalCode: Int, Email email: String, Password password: String, Photo photo: UIImage?) {
        
        // Create blank account object
        let account = Account(FamilyName: familyName, StreetAddress: streetAddress, PostalCode: postalCode, MasterEmail: email, MasterPassword: password)
        
        // If user lis logged in...
        if Auth.auth().currentUser != nil {
            
            // Reference databse
            let database = Firestore.firestore()
            
            // Update account user input and map for upload
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
            
            // Upload account data
            let accountDoc: DocumentReference = database.collection(COLLECTION_ACCOUNTS).document(Auth.auth().currentUser!.uid)
            accountDoc.setData(accountDocData)
            
            // Upload family photo
            if photo != nil {
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
                let imageData = photo!.jpegData(compressionQuality: 0.5)
                photoReference.putData(imageData!)
            }
            
            // Save account to file
            saveAccount(Account: account, Photo: photo!)
        }
    }
    
    // Custom method to upload family photo
    public static func uploadFamilyPhoto(Photo photo: UIImage) {
        if Auth.auth().currentUser != nil {
            let photoStorage = Storage.storage()
            let storageReference = photoStorage.reference()
            let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + ".jpg")
            let imageData = photo.jpegData(compressionQuality: 0.5)
            photoReference.putData(imageData!)
        }
    }
    
    // Custom method to upload profile photo
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
    
    // Custom method to delete selected profile
    public static func deleteProfile(ProfileId profileId: String) {
        print(Auth.auth().currentUser!.uid + profileId)
        
        // Reference database location and attempt to delete
        let database = Firestore.firestore()
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId).delete() { err in
            if let err = err {
                print("Error removing document: \(err)")
            }
                
            // Reference photo location in remote storage and attempt to delete
            else {
                let photoStorage = Storage.storage()
                let storageReference = photoStorage.reference()
                let photoReference = storageReference.child(PHOTOS_REFERENCE + Auth.auth().currentUser!.uid + profileId + ".jpg")
                photoReference.delete()
            }
        }
    }
    
    // Custom method to get udates to profiles
    public static func getUpdatedProfiles() {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Load account and clear profiles
        let account = loadAccount()
        account!.setParents(parents: [Parent]())
        account!.setChildren(children: [Child]())
        
        // Reference database location and liste for changes
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            }
                
            else {
                
                // Loop through all profiles in account
                for document in querySnapshot!.documents {
                    
                    // Add parents to account
                    if document.get("roleId") != nil {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        let roleId = document.get("roleId") as? Int
                        
                        let parent = Parent(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                        
                        account!.addParent(parent: parent)
                    }
                        
                    // Add children to account
                    else  {
                        let dateOfBirth = document.get("dateOfBirth") as? Date
                        let firstName = document.get("firstName") as? String
                        let genderId = document.get("genderId") as? Int
                        let profilePin = document.get("profilePin") as? Int
                        
                        let child = Child(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                        
                        account!.addChild(child: child)
                    }
                }
                
                // Save account to file
                saveAccount(Account: account!, Photo: nil)
            }
        }
    }
    
    // Custom method to get family from database
    public static func getUpdatedFamily() {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Load account
        let account = loadAccount()
        
        // Reference database location and attempt to download
        database.collection("accounts").document(Auth.auth().currentUser!.uid).getDocument { (document, error) in
            if let document = document, document.exists {
                
                // Reference updated data and save to file
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
    
    // Custom method to get listen for updates to family account
    public static func listenForUpdates() {
        
        // Reference databse
        let database = Firestore.firestore()
        
        // Reference database location for account and listen for updates
        database.collection("accounts").document(Auth.auth().currentUser!.uid).addSnapshotListener { documentSnapshot, error in
            guard let document = documentSnapshot else {
                print("Error fetching document: \(error!)")
                return
            }
            
            // If successful, update account and save to file
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
        
        // Reference database locatio for profiles and listen for updates
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").addSnapshotListener { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            }
                
            else {
                
                // Load account and clear profiles
                let account = loadAccount()
                account!.setParents(parents: [Parent]())
                account!.setChildren(children: [Child]())
                
                // Loop through profiles and update. Save account to file when done
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

