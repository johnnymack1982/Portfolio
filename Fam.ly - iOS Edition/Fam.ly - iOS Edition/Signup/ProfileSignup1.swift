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
        
        ButtonUtils.disableButton(button: continueButton)
        
        self.genderRolePicker.delegate = self
        self.genderRolePicker.dataSource = self
        
        roleLabel.isHidden = true
        
        genders = ["Male", "Female"]
        gendersRoles = [["Male", "Female"],
                    ["Mother", "Father", "Grandmother", "Grandfather", "Aunt", "Uncle"]]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        if mIsParent {
            return 2
        }
        
        else {
            return 1
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if mIsParent {
            return gendersRoles[component].count
        }
        
        else {
            return genders.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if mIsParent {
            return gendersRoles[component][row]
        }
        
        else {
            return genders[row]
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is ProfileSignup2 {
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
        
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == firstNameInput {
            
            if InputUtils.validName(Name: firstNameInput.text) {
                TextFieldUtils.turnGreen(textField: firstNameInput)
                
                mFirstName = firstNameInput.text
                
                mValidFirstName = true
                
                if mValidFirstName && mValidPin && mPinsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: firstNameInput)
                
                mValidFirstName = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == pinInput {
            
            if InputUtils.validPin(pin: pinInput.text) {
                TextFieldUtils.turnGreen(textField: pinInput)
                
                mValidPin = true
                
                if mValidFirstName && mValidPin && mPinsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: pinInput)
                
                mValidPin = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == confirmPinInput {
            
            if InputUtils.pinsMatch(pin: pinInput.text, pinConfirm: confirmPinInput.text) {
                TextFieldUtils.turnGreen(textField: confirmPinInput)
                
                mPinsMatch = true
                
                mProfilePin = Int(pinInput.text!)
                
                if mValidFirstName && mValidPin && mPinsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: confirmPinInput)
                
                mPinsMatch = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
    
}
