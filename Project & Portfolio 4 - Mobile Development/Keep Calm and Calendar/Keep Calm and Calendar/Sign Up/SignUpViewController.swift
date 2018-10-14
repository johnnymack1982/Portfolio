//
//  SignUpViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/1/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var emailEntry: UITextField!
    @IBOutlet weak var passwordEntry: UITextField!
    @IBOutlet weak var confirmPasswordEntry: UITextField!
    
    
    
    // MARK: - Class Properties
    var userEmail: String?
    var userPassword: String?
    var confirmPassword: String?
    
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Hide embedded navigation controller to properly display custom navigation bar
        self.navigationController?.navigationBar.isHidden = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        
        // If the user didn't enter a valid e-mail address let them know and prevent segue from performing
        if emailEntry.text?.trimmingCharacters(in: .whitespaces).isValidEmail() == false {
            let alert = UIAlertController(title: "Invalid E-mail", message: "Please enter a valid e-mail address.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If the user didn't enter a valid password, let them know and prevent segue from performing
        else if passwordEntry.text?.trimmingCharacters(in: .whitespaces).isvalidPassword() == false  {
            let alert = UIAlertController(title: "Invalid Password", message: "Your password should be at least 8 characters long.\n\nIt should contain at least one LETTER and one NUMBER.\n\nSpecial characters should not be included.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If the user entered an incorrect password, let them know and prevent segue from performing
        else if confirmPasswordEntry.text?.trimmingCharacters(in: .whitespaces) != passwordEntry.text?.trimmingCharacters(in: .whitespaces) {
            let alert = UIAlertController(title: "Invalid Password", message: "Your passwords don't match.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If all input is valid, allow segue to perform
        else {
            return true
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Reference destination view controller
        if let destination = segue.destination as? ParentCodeViewController {
            
            // Reference user e-mail input
            if let userEmailInput = emailEntry.text?.trimmingCharacters(in: .whitespaces) {
                userEmail = userEmailInput
            }
            
            // Reference user password input
            if let userPasswordInput = passwordEntry.text?.trimmingCharacters(in: .whitespaces) {
                userPassword = userPasswordInput
            }
            
            // Send user input to the next view
            destination.userEmail = userEmail
            destination.userPassword = userPassword
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // Force application to unfocus from current text field
        view.endEditing(true)
        
        // Call this function into action when touch is detected
        super.touchesBegan(touches, with: event)
    }
}
