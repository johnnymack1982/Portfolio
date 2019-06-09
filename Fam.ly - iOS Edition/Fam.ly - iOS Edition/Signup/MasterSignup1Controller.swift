//
//  MasterSignup1Controller.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class MasterSignup1Controller: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyNameInput: UITextField!
    @IBOutlet weak var streetAddressInput: UITextField!
    @IBOutlet weak var postalCodeInput: UITextField!
    @IBOutlet weak var continueButton: UIButton!
    
    
    
    // Class properties
    var mFamilyName: String?
    var mStreetAddress: String?
    var mPostalCode: Int?
    
    var mValidFamilyName = false;
    var mValidStreetAddress = false;
    var mValidPostalCode = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        ButtonUtils.disableButton(button: continueButton)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is MasterSignup2Controller {
            let destination = segue.destination as? MasterSignup2Controller
            
            destination?.mFamilyName = mFamilyName
            destination?.mStreetAddress = mStreetAddress
            destination?.mPostalCode = mPostalCode
        }
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
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == familyNameInput {
            
            if InputUtils.validName(Name: familyNameInput.text!) {
                
                TextFieldUtils.turnGreen(textField: familyNameInput)
                
                mFamilyName = familyNameInput.text
                
                mValidFamilyName = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: familyNameInput)
                
                mValidFamilyName = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == streetAddressInput {
            
            if InputUtils.validAddress(Address: streetAddressInput.text) {
                
                TextFieldUtils.turnGreen(textField: streetAddressInput)
                
                mStreetAddress = streetAddressInput.text
                
                mValidStreetAddress = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: streetAddressInput)
                
                mValidStreetAddress = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == postalCodeInput {
            
            if InputUtils.validPostalCode(PostalCode: postalCodeInput.text) {
                
                TextFieldUtils.turnGreen(textField: postalCodeInput)
                
                mPostalCode = Int(postalCodeInput.text!)
                
                mValidPostalCode = true
                
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: postalCodeInput)
                
                mValidPostalCode = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
