//
//  ForgotPasswordController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/27/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth

class ForgotPasswordController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    
    
    
    // Class properties
    var mEmail: String?
    
    var mValidEmail = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        ButtonUtils.disableButton(button: sendButton)
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
    @IBAction func sendButtonClicked(_ sender: UIButton) {
        if mValidEmail {
            Auth.auth().sendPasswordReset(withEmail: mEmail!) { error in
                if error == nil {
                    let alert = UIAlertController(title: "On Its Way!", message: "We're sending you a password reset link. Please check your email.", preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true)
                }
                
                else {
                    let alert = UIAlertController(title: "Try Again!", message: "We can't find an account for this email address.", preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true)
                }
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
                
                if mValidEmail {
                    ButtonUtils.enableButton(button: sendButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: masterEmailInput)
                
                mValidEmail = false
                
                ButtonUtils.disableButton(button: sendButton)
            }
        }
    }
}
