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
    
    
    
    // Class properties
    var mFirstName: String?
    var mPin: String?
    
    var mValidFirstName = false
    var mValidPin = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        ButtonUtils.disableButton(button: continueButton)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Action methods
    @IBAction func continueClicked(_ sender: UIButton) {
        let firebaseAuth = Auth.auth()
        let account = firebaseAuth.currentUser
        let database = Firestore.firestore()
        
        let documentReference = database.collection("accounts").document(account!.uid).collection("profiles").document(account!.uid + mFirstName! + mPin!)
        documentReference.getDocument { (document, error) in
            if let document = document, document.exists {
                
                if document.get("roleId") != nil {
                    let dateOfBirth = document.get("dateOfBirth") as? Date
                    let genderId = document.get("genderId") as? Int
                    let profilePin = Int(self.mPin!)
                    let roleId = document.get("roleId") as? Int
                    
                    let parent = Parent(FirstName: self.mFirstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!, RoleID: roleId!)
                    
                    AccountUtils.saveProfile(Parent: parent, Child: nil)
                }
                
                else {
                    let dateOfBirth = document.get("dateOfBirth") as? Date
                    let genderId = document.get("genderId") as? Int
                    let profilePin = Int(self.mPin!)
                    
                    let child = Child(FirstName: self.mFirstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                    
                    AccountUtils.saveProfile(Parent: nil, Child: child)
                }
                
                self.performSegue(withIdentifier: "ProfileLoginToNavigation", sender: nil)
            }
                
            else {
                let alert = UIAlertController(title: "Profile Not Found", message: "We couldn't find a profile for this person. Please try again.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == firstNameInput {
            
            if InputUtils.validName(Name: firstNameInput.text) {
                TextFieldUtils.turnGreen(textField: firstNameInput)
                
                mFirstName = firstNameInput.text
                
                mValidFirstName = true
                
                
                
                if mValidFirstName && mValidPin {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: firstNameInput)
                
                mValidFirstName = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
            
        else if sender == profilePinInput {
            
            if InputUtils.validPin(pin: profilePinInput.text) {
                TextFieldUtils.turnGreen(textField: profilePinInput)
                
                mValidPin = true
                
                mPin = profilePinInput.text
                
                if mValidFirstName && mValidPin {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: profilePinInput)
                
                mValidPin = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
