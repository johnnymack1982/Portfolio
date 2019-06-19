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
        
        ButtonUtils.disableButton(button: loginButton)
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
    @IBAction func loginClicked(_ sender: UIButton) {
        loginButton.isHidden = true
        forgotPasswordButton.isHidden = true
        loadingIndicator.isHidden = false
        
        Auth.auth().signIn(withEmail: mEmail!, password: mPassword!) { [weak self] user, error in
            if error == nil {
                let database = Firestore.firestore()
                let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid)
                documentRef.getDocument { (document, error) in
                    if let document = document, document.exists {
                        let familyName = document.get("familyName") as? String
                        let masterEmail = document.get("masterEmail") as? String
                        let masterPassword = document.get("masterPassword") as? String
                        let postalCode = document.get("postalCode") as? Int
                        let streetAddress = document.get("streetAddress") as? String
                        
                        let account = Account(FamilyName: familyName!, StreetAddress: streetAddress!, PostalCode: postalCode!, MasterEmail: masterEmail!, MasterPassword: masterPassword!)
                        
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
                                        
                                        account.addParent(parent: parent)
                                    }
                                    
                                    else {
                                        let dateOfBirth = document.get("dateOfBirth") as? Date
                                        let firstName = document.get("firstName") as? String
                                        let genderId = document.get("genderId") as? Int
                                        let profilePin = document.get("profilePin") as? Int
                                        
                                        let child = Child(FirstName: firstName!, DateOfBirth: dateOfBirth!, GenderID: genderId!, ProfilePIN: profilePin!)
                                        
                                        account.addChild(child: child)
                                        
                                        let storage = Storage.storage()
                                        let storageRef = storage.reference()
                                        let photoReference = storageRef.child("photos/" + Auth.auth().currentUser!.uid + ".jpg")
                                        
                                        var photo: UIImage?
                                        
                                        photoReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
                                            if error != nil {
                                                print("Error downloading photo")
                                            }
                                                
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
                
                self!.performSegue(withIdentifier: "MasterToProfileLogin", sender: nil)
            }
            
            else {
                print(error!)
                
                let alert = UIAlertController(title: "Couldn't Login", message: "Couldn\'t sign you in. Please check your info and try again", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                
                self!.present(alert, animated: true)
                
                self!.loginButton.isHidden = false
                self!.forgotPasswordButton.isHidden = false
                self!.loadingIndicator.isHidden = true
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == masterEmailInput {
            
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                mEmail = masterEmailInput.text
                
                mValidEmail = true
                
                if mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: loginButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: masterEmailInput)
                
                mValidEmail = false
                
                ButtonUtils.disableButton(button: loginButton)
            }
        }
            
        else if sender == masterPasswordInput {
            
            if InputUtils.validPassword(Password: masterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterPasswordInput)
                
                mValidPassword = true
                
                mPassword = masterPasswordInput.text
                
                passwordRulesDisplay.isHidden = true
                
                if mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: loginButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                
                mValidPassword = false
                
                passwordRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: loginButton)
            }
        }
    }
}

