//
//  ViewController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage

class ViewController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var masterPasswordInput: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var forgotPasswordButton: UIButton!
    @IBOutlet weak var passwordRulesDisplay: UILabel!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    
    
    // Class properties
    var mEmail: String?
    var mPassword: String?
    
    var mValidEmail = false
    var mValidPassword = false
    
    var mAccount: Account?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Disable login button by default
        ButtonUtils.disableButton(button: loginButton)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter,' move to next input field and dismiss keyboard after last
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Action methods
    @IBAction func loginClicked(_ sender: UIButton) {
        // Set default button states
        loginButton.isHidden = true
        forgotPasswordButton.isHidden = true
        loadingIndicator.isHidden = false
        
        // Attempt to log in
        Auth.auth().signIn(withEmail: mEmail!, password: mPassword!) { [weak self] user, error in
            
            // If successful...
            if error == nil {
                
                // Reference account database location and attempt to download account
                let database = Firestore.firestore()
                let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid)
                documentRef.getDocument { (document, error) in
                    
                    // If document exists...
                    if let document = document, document.exists {
                        
                        // Reference account data
                        let familyName = document.get("familyName") as? String
                        let masterEmail = document.get("masterEmail") as? String
                        let masterPassword = document.get("masterPassword") as? String
                        let postalCode = document.get("postalCode") as? Int
                        let streetAddress = document.get("streetAddress") as? String
                        
                        // Create account object
                        let account = Account(FamilyName: familyName!, StreetAddress: streetAddress!, PostalCode: postalCode!, MasterEmail: masterEmail!, MasterPassword: masterPassword!)
                        
                        // Reference profile database location and attempt to download
                        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").getDocuments() { (querySnapshot, err) in
                            if let err = err {
                                print("Error getting documents: \(err)")
                            }
                                
                            // If successful...
                            else {
                                
                                // Loop through all profiles in account
                                for document in querySnapshot!.documents {
                                    
                                    // If profile is a parent...
                                    if document.get("roleId") != nil {
                                        
                                        // Reference profile data
                                        let dateOfBirth = document.get("dateOfBirth") as? Date
                                        let firstName = document.get("firstName") as? String
                                        let genderId = document.get("genderId") as? Int
                                        let profilePin = document.get("profilePin") as? Int
                                        let roleId = document.get("roleId") as? Int
                                        
                                        // Create profile object
                                        let parent = Parent(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                                        
                                        // Add profile to account
                                        account.addParent(parent: parent)
                                    }
                                    
                                    // If profile is a child...
                                    else {
                                        
                                        // Reference profile data
                                        let dateOfBirth = document.get("dateOfBirth") as? Date
                                        let firstName = document.get("firstName") as? String
                                        let genderId = document.get("genderId") as? Int
                                        let profilePin = document.get("profilePin") as? Int
                                        
                                        // Create profile object
                                        let child = Child(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                                        
                                        // Add profile to account
                                        account.addChild(child: child)
                                        
                                        // Reference remote storage location for profile photo
                                        let storage = Storage.storage()
                                        let storageRef = storage.reference()
                                        let photoReference = storageRef.child("photos/" + Auth.auth().currentUser!.uid + ".jpg")
                                        
                                        var photo: UIImage?
                                        
                                        // Attempt to download family photo
                                        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
                                            if error != nil {
                                                print("Error downloading photo")
                                            }
                                                
                                            // If successful, save account to file
                                            else {
                                                photo = UIImage(data: data!)
                                                
                                                AccountUtils.saveAccount(Account: account, Photo: photo!)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    else {
                        print("Document does not exist")
                    }
                }
                
                // Launch profile login activity
                self!.performSegue(withIdentifier: "MasterToProfileLogin", sender: nil)
            }
            
            // If there is a problem...
            else {
                print(error!)
                
                // Build alert and present
                let alert = UIAlertController(title: "Couldn't Login", message: "Couldn\'t sign you in. Please check your info and try again", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                self!.present(alert, animated: true)
                
                // Reset button states
                self!.loginButton.isHidden = false
                self!.forgotPasswordButton.isHidden = false
                self!.loadingIndicator.isHidden = true
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch master email input
        if sender == masterEmailInput {
            
            // If input is valid...
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                // Reference user input and indicaate input is valid
                mEmail = masterEmailInput.text
                mValidEmail = true
                
                // If all fields have valid input, enable login button
                if mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: loginButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterEmailInput)
                mValidEmail = false
                
                // Disable login button
                ButtonUtils.disableButton(button: loginButton)
            }
        }
            
        // Watch master password input
        else if sender == masterPasswordInput {
            
            // If input is valid...
            if InputUtils.validPassword(Password: masterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: masterPasswordInput)
                
                // Reference user input and indicate input is valid
                mValidPassword = true
                mPassword = masterPasswordInput.text
                
                // Hide password rules
                passwordRulesDisplay.isHidden = true
                
                // If all fields have valid input, enable login button
                if mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: loginButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Tur input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                mValidPassword = false
                
                // Show password rules and disable logi button
                passwordRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: loginButton)
            }
        }
    }
}

