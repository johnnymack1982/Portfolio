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
        
        toggleParentButton()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is ProfileSignup1 {
            let destination = segue.destination as? ProfileSignup1
            
            destination?.mIsParent = mIsParent
        }
    }
    
    
    
    // Custom methods
    private func toggleParentButton() {
        let account = AccountUtils.loadAccount()
        
        if account!.getParents().count == 2 {
            ButtonUtils.disableButton(button: parentButton)
        }
        
        else {
            ButtonUtils.enableButton(button: parentButton)
        }
    }
    
    
    
    // Action methods
    @IBAction func addProfile(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            mIsParent = true
            
        case 1:
            mIsParent = false
            
        default:
            print("Invalid option")
        }
    }
}
