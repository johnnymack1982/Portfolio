//
//  SignUpViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/1/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController, UITextFieldDelegate {
    
    
    
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
        
        // Register keyboard notifications. This will be used later to move text fields when the keyboard is active.
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        
        // Call custom function to validate input and determine if segue should perform
        return validateInput()
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
    
    // Determine what should happen when the 'return' key is tapped
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField {
        case emailEntry:
            emailEntry.resignFirstResponder()
            passwordEntry.becomeFirstResponder()
            
        case passwordEntry:
            passwordEntry.resignFirstResponder()
            confirmPasswordEntry.becomeFirstResponder()
            
        case confirmPasswordEntry:
            if validateInput() == true {
                performSegue(withIdentifier: "SignUpToParentCode", sender: self)
            }
            
        default:
            print("Invalid Text Field")
        }
        
        return true
    }
    
    
    
    // MARK: - Action Functions
    
    // Move text fields up when keyboard is active
    @objc func keyboardWillShow(notification: NSNotification) {
        guard let userInfo = notification.userInfo
            
            else {
                return
        }
        
        guard let keyboardSize = userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue
            
            else {
                return
        }
        
        let keyboardFrame = keyboardSize.cgRectValue
        
        if self.view.frame.origin.y == 0{
            self.view.frame.origin.y -= keyboardFrame.height
        }
    }
    
    // Move text fields down when keyboard is hidden
    @objc func keyboardWillHide(notification: NSNotification) {
        if self.view.frame.origin.y != 0{
            self.view.frame.origin.y = 0
        }
    }
    
    
    
    // MARK: - Custom Functions
    
    // Custom function to validate input and determine if segue should perform
    func validateInput() -> Bool {
        var validInput = false
        
        // If the user didn't enter a valid e-mail address let them know and prevent segue from performing
        if emailEntry.text?.trimmingCharacters(in: .whitespaces).isValidEmail() == false {
            let alert = UIAlertController(title: "Invalid E-mail", message: "Please enter a valid e-mail address.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            validInput = false
        }
            
            // If the user didn't enter a valid password, let them know and prevent segue from performing
        else if passwordEntry.text?.trimmingCharacters(in: .whitespaces).isvalidPassword() == false  {
            let alert = UIAlertController(title: "Invalid Password", message: "Your password should be at least 8 characters long.\n\nIt should contain at least one LETTER and one NUMBER.\n\nSpecial characters should not be included.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            validInput = false
        }
            
            // If the user entered an incorrect password, let them know and prevent segue from performing
        else if confirmPasswordEntry.text?.trimmingCharacters(in: .whitespaces) != passwordEntry.text?.trimmingCharacters(in: .whitespaces) {
            let alert = UIAlertController(title: "Invalid Password", message: "Your passwords don't match.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
        }
            
            // If all input is valid, allow segue to perform
        else {
            validInput = true
        }
        
        return validInput
    }
}
