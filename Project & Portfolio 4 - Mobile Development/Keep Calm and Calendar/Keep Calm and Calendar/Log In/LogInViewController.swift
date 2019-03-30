//
//  LogInViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class LogInViewController: UIViewController, UITextFieldDelegate {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var emailEntry: UITextField!
    @IBOutlet weak var passwordEntry: UITextField!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var continueButton: UIButton!
    
    
    
    // MARK: - Class Properties
    var userEmail: String?
    var userPassword: String?
    var validUser = false
    var databaseReference: DatabaseReference?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        loadingIndicator.isHidden = true
        
        databaseReference = Database.database().reference()
        
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
        
        // Reference destination view and pass required data
        if let destination = segue.destination as? ScheduleViewController {
            destination.userEmail = userEmail
            destination.userPassword = userPassword
            destination.newUser = false
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
            
            if validateInput() == true {
                performSegue(withIdentifier: "LogInToSchedule", sender: self)
            }
            
        default:
            print("Invalid Text Field")
        }
        
        return true
    }
    
    // Custom function to validate input and determine if segue should perform
    func validateInput() -> Bool {
        var validInput = false
        
        // If user entered an invalid e-mail address let them know and prevent segue from performing
        if emailEntry.text?.trimmingCharacters(in: .whitespaces).isValidEmail() == false {
            let alert = UIAlertController(title: "Invalid E-mail", message: "Please enter a valid e-mail address.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            validInput = false
        }
            
            // If user entered an invalid password let them know and prevent segue from performing
        else if passwordEntry.text?.trimmingCharacters(in: .whitespaces).isvalidPassword() == false  {
            let alert = UIAlertController(title: "Invalid Password", message: "Your password should be at least 8 characters long.\n\nIt should contain at least one LETTER and one NUMBER.\n\nSpecial characters should not be included.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            validInput = false
        }
            
            // If user entered valid input, attempt to log in
        else {
            userEmail = emailEntry.text?.trimmingCharacters(in: .whitespaces)
            userPassword = passwordEntry.text?.trimmingCharacters(in: .whitespaces)
            
            // Hide Continue button and show progress indicator
            self.continueButton.isHidden = true
            self.loadingIndicator.isHidden = false
            self.loadingIndicator.startAnimating()
            
            // Attempt to log into Firebase servers
            Auth.auth().signIn(withEmail: userEmail!, password: userPassword!) { (user, error) in
                
                // If login is unsuccessful, let the user know and prevent segue
                if error != nil {
                    let alert = UIAlertController(title: "Invalid Account", message: "The e-mail address or password you entered is incorrect.", preferredStyle: .alert)
                    let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alert.addAction(okButton)
                    self.present(alert, animated: true)
                    
                    self.continueButton.isHidden = false
                    self.loadingIndicator.isHidden = true
                    
                    self.validUser = false
                }
                    
                    // If login is successful, segue to next view
                else {
                    self.performSegue(withIdentifier: "LogInToSchedule", sender: nil)
                    
                    self.validUser = true
                }
            }
            
            if validUser == true {
                validInput = true
            }
                
            else {
                validInput = false
            }
        }
        
        return validInput
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
}
