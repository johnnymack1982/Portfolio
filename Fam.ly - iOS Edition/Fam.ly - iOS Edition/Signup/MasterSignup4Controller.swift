//
//  MasterSignup4Controller.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/26/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class MasterSignup4Controller: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var parentButton: UIButton!
    
    
    
    // Class properties
    var mIsParent = true
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Call custom method to toggle add parent button
        toggleParentButton()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is ProfileSignup1 {
            
            // Send parent profile to next activity
            let destination = segue.destination as? ProfileSignup1
            
            destination?.mIsParent = mIsParent
        }
    }
    
    
    
    // Custom methods
    // Custom method to toggle add parent button
    private func toggleParentButton() {
        
        // Load account
        let account = AccountUtils.loadAccount()
        
        // If profile has two parents, disable add parent button
        if account!.getParents().count == 2 {
            ButtonUtils.disableButton(button: parentButton)
        }
        
        // Otherwise, enable add parent button
        else {
            ButtonUtils.enableButton(button: parentButton)
        }
    }
    
    
    
    // Action methods
    @IBAction func addProfile(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked add parent button, let destination know user is adding a parent
        case 0:
            mIsParent = true
            
        // If user clicked add child button, let destination know user is adding a child
        case 1:
            mIsParent = false
            
        default:
            print("Invalid option")
        }
    }
}
