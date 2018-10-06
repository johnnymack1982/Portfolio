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
    var parentCode: Int?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if parentCodeEntry.text?.count != 4 {
            let alert = UIAlertController(title: "Invalid Parent Code", message: "Please enter a four-digit Parent Code.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        else {
            Auth.auth().createUser(withEmail: userEmail!, password: userPassword!) { (authResult, error) in
                // ...
                guard (authResult?.user) != nil else { return }
            }
            
            return true
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
}
