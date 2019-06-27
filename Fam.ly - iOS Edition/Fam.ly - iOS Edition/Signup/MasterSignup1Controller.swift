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
        
        // On 'enter,' move to next input field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch family name input field
        if sender == familyNameInput {
            
            // If input is valid...
            if InputUtils.validName(Name: familyNameInput.text!) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: familyNameInput)
                
                // Reference user input and indicate input is valid
                mFamilyName = familyNameInput.text
                mValidFamilyName = true
                
                // If all fields have valid input, enable continue button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: familyNameInput)
                mValidFamilyName = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
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
                
                // If all fields have valid input, enable continue button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: streetAddressInput)
                mValidStreetAddress = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        // Watch postal code input
        else if sender == postalCodeInput {
            
            // If input is valid...
            if InputUtils.validPostalCode(PostalCode: postalCodeInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: postalCodeInput)
                
                // Reference user input and indicate input is valid
                mPostalCode = Int(postalCodeInput.text!)
                mValidPostalCode = true
                
                // If all fields have valid input, enable continue button
                if mValidFamilyName && mValidStreetAddress && mValidPostalCode {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: postalCodeInput)
                mValidPostalCode = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
