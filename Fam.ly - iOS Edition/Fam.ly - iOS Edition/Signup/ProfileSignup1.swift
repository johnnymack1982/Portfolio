//
//  ParentSignup1.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class ProfileSignup1: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate {
    
    
    
    // Class properties
    var mAccount: Account?
    
    var parentOrChild = 0
    
    
    
    
    // UI Outlets
    @IBOutlet weak var roleLabel: UILabel!
    @IBOutlet weak var genderRolePicker: UIPickerView!
    
    @IBOutlet weak var firstNameInput: UITextField!
    @IBOutlet weak var dateOfBirthPicker: UIDatePicker!
    @IBOutlet weak var pinInput: UITextField!
    @IBOutlet weak var confirmPinInput: UITextField!
    
    @IBOutlet weak var continueButton: UIButton!
    
    
    
    // Class properties
    var genders: [String] = [String]()
    var gendersRoles: [[String]] = [[String]]()
    
    var mFirstName: String?
    var mDateOfBirth: Date?
    var mGenderId: Int?
    var mRoleId: Int?
    var mProfilePin: Int?
    
    var mValidFirstName = false
    var mValidPin = false
    var mPinsMatch = false
    
    var mIsParent = true
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Disable continue button by default
        ButtonUtils.disableButton(button: continueButton)
        
        roleLabel.isHidden = true
        
        // Set geder / family role picker options
        self.genderRolePicker.delegate = self
        self.genderRolePicker.dataSource = self
        genders = ["Male", "Female"]
        gendersRoles = [["Male", "Female"],
                    ["Mother", "Father", "Grandmother", "Grandfather", "Aunt", "Uncle"]]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        
        // If adding parent, show role id selection in picker
        if mIsParent {
            return 2
        }
        
        // Otherwise, hide role id selection in picker
        else {
            return 1
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        
        // Return number of available options in gender / family role picker
        if mIsParent {
            return gendersRoles[component].count
        }
        
        else {
            return genders.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        
        // Display selected values in gender / family role picker
        if mIsParent {
            return gendersRoles[component][row]
        }
        
        else {
            return genders[row]
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is ProfileSignup2 {
            
            // Send user input to destination
            let destination = segue.destination as? ProfileSignup2
            
            mDateOfBirth = dateOfBirthPicker.date
            mGenderId = genderRolePicker.selectedRow(inComponent: 0)
            
            destination?.mFirstName = mFirstName
            destination?.mDateOfBirth = mDateOfBirth
            destination?.mGenderId = mGenderId
            destination?.mProfilePin = mProfilePin
            
            if mIsParent {
                mRoleId = genderRolePicker.selectedRow(inComponent: 1)
                destination?.mRoleId = mRoleId
                mIsParent = true
            }
            
            else {
                mIsParent = false
            }
            
            destination?.mIsParent = mIsParent
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
        
        // Watch first name input
        if sender == firstNameInput {
            
            // If input is valid...
            if InputUtils.validName(Name: firstNameInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: firstNameInput)
                
                // Reference user input and indicate input is valid
                mFirstName = firstNameInput.text
                mValidFirstName = true
                
                // If all fields have valid input, enable continue button
                if mValidFirstName && mValidPin && mPinsMatch {
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
        
        // Watch pin input
        else if sender == pinInput {
            
            // If input is valid...
            if InputUtils.validPin(pin: pinInput.text) {
                
                // Turn input field green and indicate input is valid
                TextFieldUtils.turnGreen(textField: pinInput)
                mValidPin = true
                
                // If all fields have valid input, enable continue button
                if mValidFirstName && mValidPin && mPinsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: pinInput)
                mValidPin = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        // Watch pin confirm input
        else if sender == confirmPinInput {
            
            // If input is valid...
            if InputUtils.pinsMatch(pin: pinInput.text, pinConfirm: confirmPinInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: confirmPinInput)
                
                // Reference user input and indicate input is valid
                mPinsMatch = true
                mProfilePin = Int(pinInput.text!)
                
                // If all fields have valid input, enable continue button
                if mValidFirstName && mValidPin && mPinsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: confirmPinInput)
                mPinsMatch = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
    
}
