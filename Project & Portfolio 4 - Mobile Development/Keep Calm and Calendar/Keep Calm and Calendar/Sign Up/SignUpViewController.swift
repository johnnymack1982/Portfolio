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
        
        // Do any additional setup after loading the view.
        
        self.navigationController?.navigationBar.isHidden = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if emailEntry.text?.isValidEmail() == false {
            let alert = UIAlertController(title: "Invalid E-mail", message: "Please enter a valid e-mail address.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        else if passwordEntry.text?.isvalidPassword() == false  {
            let alert = UIAlertController(title: "Invalid Password", message: "Your password should be at least 8 characters long.\n\nIt should contain at least one LETTER and one NUMBER.\n\nSpecial characters should not be included.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        else if confirmPasswordEntry.text != passwordEntry.text {
            let alert = UIAlertController(title: "Invalid Password", message: "Your passwords don't match.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        else {
            return true
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? ParentCodeViewController {
            if let userEmailInput = emailEntry.text {
                userEmail = userEmailInput
            }
            
            if let userPasswordInput = passwordEntry.text {
                userPassword = userPasswordInput
            }
            
            destination.userEmail = userEmail
            destination.userPassword = userPassword
        }
    }
}
