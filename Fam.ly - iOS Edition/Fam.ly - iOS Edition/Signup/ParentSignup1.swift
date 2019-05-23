//
//  ParentSignup1.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class ParentSignup1: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    
    
    
    // UI Outlets
    @IBOutlet weak var roleLabel: UILabel!
    
    @IBOutlet weak var genderRolePicker: UIPickerView!
    
    
    
    // Class properties
    var genders: [String] = [String]()
    var gendersRoles: [[String]] = [[String]]()
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.genderRolePicker.delegate = self
        self.genderRolePicker.dataSource = self
        
        roleLabel.isHidden = true
        
        genders = ["Male", "Female"]
        gendersRoles = [["Male", "Female"],
                    ["Mother", "Father", "Grandmother", "Grandfather", "Aunt", "Uncle"]]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return genders.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return genders[row]
    }
}
