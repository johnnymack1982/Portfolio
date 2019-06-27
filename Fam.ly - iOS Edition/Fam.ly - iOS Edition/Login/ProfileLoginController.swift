//
//  ProfileLoginController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/27/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseFirestore

class ProfileLoginController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var firstNameInput: UITextField!
    @IBOutlet weak var profilePinInput: UITextField!
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    
    
    // Class properties
    var mFirstName: String?
    var mPin: String?
    
    var mValidFirstName = false
    var mValidPin = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Disable continue button by default
        ButtonUtils.disableButton(button: continueButton)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter' move to next text field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Action methods
    @IBAction func continueClicked(_ sender: UIButton) {
        
        // Set default button states
        continueButton.isHidden = true
        cancelButton.isHidden = true
        loadingIndicator.isHidden = false
        
        // Reference firebase user account and database
        let firebaseAuth = Auth.auth()
        let account = firebaseAuth.currentUser
        let database = Firestore.firestore()
        
        // Reference database location and attempt to download
        let documentReference = database.collection("accounts").document(account!.uid).collection("profiles").document(account!.uid + mFirstName! + mPin!)
        documentReference.getDocument { (document, error) in
            
            // If document exists...
            if let document = document, document.exists {
                
                // If profile is a parent...
                if document.get("roleId") != nil {
                    
                    // Reference profile data
                    let dateOfBirth = document.get("dateOfBirth") as? Date
                    let genderId = document.get("genderId") as? Int
                    let profilePin = Int(self.mPin!)
                    let roleId = document.get("roleId") as? Int
                    
                    // Create profile object and save to file
                    let parent = Parent(FirstName: self.mFirstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                    AccountUtils.saveProfile(Parent: parent, Child: nil)
                }
                
                // If profile is a child...
                else {
                    
                    // Reference profile data
                    let dateOfBirth = document.get("dateOfBirth") as? Date
                    let genderId = document.get("genderId") as? Int
                    let profilePin = Int(self.mPin!)
                    
                    // Create profile object and save to file
                    let child = Child(FirstName: self.mFirstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                    AccountUtils.saveProfile(Parent: nil, Child: child)
                }
                
                // Launch navigation activity
                self.performSegue(withIdentifier: "ProfileLoginToNavigation", sender: nil)
            }
                
            // If there is a problem...
            else {
                
                // Build alert and present to user
                let alert = UIAlertController(title: "Profile Not Found", message: "We couldn't find a profile for this person. Please try again.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                self.present(alert, animated: true)
                
                // Reset button states
                self.continueButton.isHidden = false
                self.cancelButton.isHidden = false
                self.loadingIndicator.isHidden = true
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch first name input field
        if sender == firstNameInput {
            
            // If input is valid...
            if InputUtils.validName(Name: firstNameInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: firstNameInput)
                
                // Reference user input and indicate input is valid
                mFirstName = firstNameInput.text
                mValidFirstName = true
                
                // If all fields have valid input, enable continue button
                if mValidFirstName && mValidPin {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: firstNameInput)
                mValidFirstName = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
            
        // Watch profile pin input
        else if sender == profilePinInput {
            
            // If input is valid...
            if InputUtils.validPin(pin: profilePinInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: profilePinInput)
                
                // Reference user input and indicate input is valid
                mValidPin = true
                mPin = profilePinInput.text
                
                // If all fields have valid input, enable cotinue button
                if mValidFirstName && mValidPin {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            // if input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: profilePinInput)
                mValidPin = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
