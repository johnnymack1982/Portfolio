//
//  ParentCodeViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/1/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class ParentCodeViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var parentCodeEntry: UITextField!
    
    
    
    // MARK: - Class Properties
    var userEmail: String?
    var userPassword: String?
    var parentCode: String?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Register keyboard notifications. This will be used later to move text fields when the keyboard is active.
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        
        // If the user entered an invalid Parent Code, let them know and prevent segue from performing
        if parentCodeEntry.text?.trimmingCharacters(in: .whitespaces).count != 4 {
            let alert = UIAlertController(title: "Invalid Parent Code", message: "Please enter a four-digit Parent Code.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If user entered a valid Parent Code, use provided input to establish a new user account and allow segue to perform
        else {
            Auth.auth().createUser(withEmail: userEmail!, password: userPassword!) { (authResult, error) in
                // ...
                guard (authResult?.user) != nil else { return }
            }
            
            parentCode = (parentCodeEntry.text?.trimmingCharacters(in: .whitespaces))!
            
            return true
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Reference destination view controller and pass necessary information
        if let destination = segue.destination as? ScheduleViewController {
            destination.userEmail = userEmail
            destination.userPassword = userPassword
            destination.parentCode = parentCode
            destination.newUser = true
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // Force application to unfocus from current text field
        view.endEditing(true)
        
        // Call this function into action when touch is detected
        super.touchesBegan(touches, with: event)
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
