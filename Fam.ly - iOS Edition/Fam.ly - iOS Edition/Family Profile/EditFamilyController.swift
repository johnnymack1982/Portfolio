//
//  EditFamilyController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/27/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage

class EditFamilyController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyNameInput: UITextField!
    @IBOutlet weak var streetAddressInput: UITextField!
    @IBOutlet weak var postalCodeInput: UITextField!
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var masterPasswordInput: UITextField!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var familyPhoto: UIImageView!
    @IBOutlet weak var passwordRulesDisplay: UILabel!
    
    
    
    // Class properties
    var mAccount: Account?
    
    var mFamilyName: String?
    var mStreetAddress: String?
    var mPostalCode: Int?
    var mOldEmail: String?
    var mNewEmail: String?
    var mPassword: String?
    
    var mValidFamilyName = true
    var mValidStreetAddress = true
    var mValidPostalCode = true
    var mValidEmail = true
    var mValidPassword = true
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Load account and family photo
        mAccount = AccountUtils.loadAccount()
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        
        // Reference account information
        mFamilyName = mAccount?.getFamilyName()
        mStreetAddress = mAccount?.getStreetAddress()
        mPostalCode = mAccount?.getPostalCode()
        mOldEmail = mAccount?.getMasterEmail()
        mNewEmail = mAccount?.getMasterEmail()
        
        // Populate input fields with current account information
        familyNameInput.text = mAccount?.getFamilyName()
        streetAddressInput.text = mAccount?.getStreetAddress()
        postalCodeInput.text = String(mAccount!.getPostalCode())
        masterEmailInput.text = mAccount?.getMasterEmail()
        
        // Disable save button by default
        ButtonUtils.disableButton(button: saveButton)
        
        // Call custom method to round image view
        roundImageView()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter,' move to next input field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Custom methods
    // Custom method to round image view
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    
    
    // Action methods
    @IBAction func saveButtonClicked(_ sender: UIButton) {
        
        // If user entered valid password...
        if mPassword == mAccount?.getMasterPassword() {
            
            // Create new account object using user input
            let account = Account(FamilyName: mFamilyName!, StreetAddress: mStreetAddress!, PostalCode: mPostalCode!, MasterEmail: mNewEmail!, MasterPassword: mPassword!)
            
            // If user is logged in correctly...
            if Auth.auth().currentUser != nil {
                
                // Reference database
                let database = Firestore.firestore()
                
                // Reference new family properties
                let familyName = account.getFamilyName()
                let masterEmail = account.getMasterEmail()
                let masterPassword = account.getMasterPassword()
                let streetAddress = account.getStreetAddress()
                let postalCode = account.getPostalCode()
                
                // Map account for upload
                let accountDocData: [String: Any] = [
                    "familyName": familyName,
                    "masterEmail": masterEmail,
                    "masterPassword": masterPassword,
                    "streetAddress": streetAddress,
                    "postalCode": postalCode,
                ]
                
                // Reference database location and attempt to upload
                let accountDoc: DocumentReference = database.collection("accounts").document(Auth.auth().currentUser!.uid)
                accountDoc.setData(accountDocData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                        
                    // If successful...
                    else {
                        if self.familyPhoto.image != nil {
                            
                            // Reference remote storage locationn for family photo
                            let photoStorage = Storage.storage()
                            let storageReference = photoStorage.reference()
                            let photoReference = storageReference.child("photos/" + Auth.auth().currentUser!.uid + ".jpg")
                            
                            // Convert family photo to JPEG data and upload
                            let imageData = self.familyPhoto.image!.jpegData(compressionQuality: 0.5)
                            photoReference.putData(imageData!)
                        }
                        
                        // Save account changes to file
                        AccountUtils.saveAccount(Account: account, Photo: self.familyPhoto.image!)
                        
                        // Attempt to reauthenticate
                        let credential = EmailAuthProvider.credential(withEmail: self.mOldEmail!, password: self.mPassword!)
                        self.mOldEmail = Auth.auth().currentUser?.email
                        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
                            
                            // If successful, update account email and return to previous activity
                            if error == nil {
                                Auth.auth().currentUser?.updateEmail(to: self.mNewEmail!)
                                self.performSegue(withIdentifier: "EditToFamilyProfile", sender: nil)
                            }
                            
                            // Otherwise, let the user know there was an issue
                            else {
                                let alert = UIAlertController(title: "Couldn't Update", message: "There was a problem updateing your information", preferredStyle: .alert)
                                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                                self.present(alert, animated: true)
                            }
                        })
                    }
                }
            }
                
            // Otherwise, let the user know there was an issue
            else {
                let alert = UIAlertController(title: "Incorrect Password", message: "The password you provided isn't correct.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                self.present(alert, animated: true)
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch family name input
        if sender == familyNameInput {
            
            // If input is valid...
            if InputUtils.validName(Name: familyNameInput.text!) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: familyNameInput)
                
                // Reference user input and indicate input is valid
                mFamilyName = familyNameInput.text
                mValidFamilyName = true
                
                // If all fields have valid input, enable save button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is innvalid
                TextFieldUtils.turnRed(textField: familyNameInput)
                mValidFamilyName = false
                
                // Disable save button
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        // Watch street address input
        else if sender == streetAddressInput {
            
            // If input is valid...
            if InputUtils.validAddress(Address: streetAddressInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: streetAddressInput)
                
                // Reference user input and indicate input is valid
                mStreetAddress = streetAddressInput.text
                mValidStreetAddress = true
                
                // If all fields have valid input, enable save button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: streetAddressInput)
                mValidStreetAddress = false
                
                // Disable save button
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        // Watch postal code innput
        else if sender == postalCodeInput {
            
            // If input is valid...
            if InputUtils.validPostalCode(PostalCode: postalCodeInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: postalCodeInput)
                
                // Reference user input and indicate input is valid
                mPostalCode = Int(postalCodeInput.text!)
                mValidPostalCode = true
                
                // If all fields have valid input, enable save button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: postalCodeInput)
                mValidPostalCode = false
                
                // Disable save button
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        // Watch master email input
        else if sender == masterEmailInput {
            
            // If input is valid...
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                // Reference user input and indicate input is valid
                mNewEmail = masterEmailInput.text
                mValidEmail = true
                
                // If all fields have valid input, enable save button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
             
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterEmailInput)
                mValidEmail = false
                
                // Disable save button
                ButtonUtils.disableButton(button: saveButton)
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
                
                // If all fields have valid input, enable save button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                mValidPassword = false
                
                // Hide password rules and disable save button
                passwordRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: saveButton)
            }
        }
    }
}
