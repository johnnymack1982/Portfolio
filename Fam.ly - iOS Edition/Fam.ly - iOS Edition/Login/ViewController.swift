//
//  ViewController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth

class ViewController: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var masterPasswordInput: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var passwordRulesDisplay: UILabel!
    
    
    
    // Class properties
    var mEmail: String?
    var mPassword: String?
    
    var mValidEmail = false
    var mValidPassword = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ButtonUtils.disableButton(button: loginButton)
        
        let user = Auth.auth().currentUser
        
        if user != nil {
            DispatchQueue.main.async(){
                self.performSegue(withIdentifier: "AutoLogin", sender: self)
            }
        }
    }
    
    
    
    // Action methods
    @IBAction func loginClicked(_ sender: UIButton) {
        Auth.auth().signIn(withEmail: mEmail!, password: mPassword!) { [weak self] user, error in
            if error == nil {
                self!.performSegue(withIdentifier: "MasterToProfileLogin", sender: nil)
            }
            
            else {
                let alert = UIAlertController(title: "Couldn't Login", message: "Couldn\'t sign you in. Please check your info and try again", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                
                self!.present(alert, animated: true)
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

