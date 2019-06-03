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

class EditFamilyController: UIViewController {
    
    
    
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
        
        mAccount = AccountUtils.loadAccount()
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        
        mFamilyName = mAccount?.getFamilyName()
        mStreetAddress = mAccount?.getStreetAddress()
        mPostalCode = mAccount?.getPostalCode()
        mOldEmail = mAccount?.getMasterEmail()
        mNewEmail = mAccount?.getMasterEmail()
        
        familyNameInput.text = mAccount?.getFamilyName()
        streetAddressInput.text = mAccount?.getStreetAddress()
        postalCodeInput.text = String(mAccount!.getPostalCode())
        masterEmailInput.text = mAccount?.getMasterEmail()
        
        ButtonUtils.disableButton(button: saveButton)
        
        roundImageView()
    }
    
    
    
    // Custom methods
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    
    
    // Action methods
    @IBAction func saveButtonClicked(_ sender: UIButton) {
        if mPassword == mAccount?.getMasterPassword() {
            let account = Account(FamilyName: mFamilyName!, StreetAddress: mStreetAddress!, PostalCode: mPostalCode!, MasterEmail: mNewEmail!, MasterPassword: mPassword!)
            
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
                
                let accountDoc: DocumentReference = database.collection("accounts").document(Auth.auth().currentUser!.uid)
                accountDoc.setData(accountDocData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                        
                    else {
                        if self.familyPhoto.image != nil {
                            let photoStorage = Storage.storage()
                            let storageReference = photoStorage.reference()
                            let photoReference = storageReference.child("photos/" + Auth.auth().currentUser!.uid + ".jpg")
                            
                            let imageData = self.familyPhoto.image!.jpegData(compressionQuality: 0.5)
                            
                            photoReference.putData(imageData!)
                        }
                        
                        AccountUtils.saveAccount(Account: account, Photo: self.familyPhoto.image!)
                        
                        let credential = EmailAuthProvider.credential(withEmail: self.mOldEmail!, password: self.mPassword!)
                        
                        self.mOldEmail = Auth.auth().currentUser?.email
                        
                        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
                            if error == nil {
                                
                                Auth.auth().currentUser?.updateEmail(to: self.mNewEmail!)
                                
                                self.performSegue(withIdentifier: "EditToFamilyProfile", sender: nil)
                            }
                            
                            else {
                                let alert = UIAlertController(title: "Couldn't Update", message: "There was a problem updateing your information", preferredStyle: .alert)
                                
                                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                                
                                self.present(alert, animated: true)
                            }
                        })
                    }
                }
            }
                
            else {
                let alert = UIAlertController(title: "Incorrect Password", message: "The password you provided isn't correct.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            }
        }
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == familyNameInput {
            
            if InputUtils.validName(Name: familyNameInput.text!) {
                
                TextFieldUtils.turnGreen(textField: familyNameInput)
                
                mFamilyName = familyNameInput.text
                
                mValidFamilyName = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: familyNameInput)
                
                mValidFamilyName = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        else if sender == streetAddressInput {
            
            if InputUtils.validAddress(Address: streetAddressInput.text) {
                
                TextFieldUtils.turnGreen(textField: streetAddressInput)
                
                mStreetAddress = streetAddressInput.text
                
                mValidStreetAddress = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: streetAddressInput)
                
                mValidStreetAddress = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        else if sender == postalCodeInput {
            
            if InputUtils.validPostalCode(PostalCode: postalCodeInput.text) {
                
                TextFieldUtils.turnGreen(textField: postalCodeInput)
                
                mPostalCode = Int(postalCodeInput.text!)
                
                mValidPostalCode = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: postalCodeInput)
                
                mValidPostalCode = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        else if sender == masterEmailInput {
            
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                mNewEmail = masterEmailInput.text
                
                mValidEmail = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: masterEmailInput)
                
                mValidEmail = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
            
        else if sender == masterPasswordInput {
            
            if InputUtils.validPassword(Password: masterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterPasswordInput)
                
                mValidPassword = true
                
                mPassword = masterPasswordInput.text
                
                passwordRulesDisplay.isHidden = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                
                mValidPassword = false
                
                passwordRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
    }
}
