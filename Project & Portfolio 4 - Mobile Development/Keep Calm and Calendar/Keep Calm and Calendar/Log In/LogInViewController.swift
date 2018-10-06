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

class LogInViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var emailEntry: UITextField!
    @IBOutlet weak var passwordEntry: UITextField!
    
    
    
    // MARK: - Class Properties
    var userEmail: String?
    var userPassword: String?
    var validUser = false
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
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
            
        else {
            userEmail = emailEntry.text
            userPassword = passwordEntry.text
            
            Auth.auth().signIn(withEmail: userEmail!, password: userPassword!) { (user, error) in
                if error != nil {
                    let alert = UIAlertController(title: "Invalid Account", message: "The e-mail address or password you entered is incorrect.", preferredStyle: .alert)
                    let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alert.addAction(okButton)
                    self.present(alert, animated: true)
                    
                    self.validUser = false
                }
                
                else {
                    self.validUser = true
                }
            }
            
            if validUser == true {
                return true
            }
            
            else {
                return false
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
}
