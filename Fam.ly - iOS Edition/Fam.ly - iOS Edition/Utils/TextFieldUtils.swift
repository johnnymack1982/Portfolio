//
//  TextFieldUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import UIKit
import QuartzCore

public class TextFieldUtils {
    
    
    
    // Custom methods
    public static func turnGreen(textField: UITextField) {
        textField.layer.borderColor = UIColor.green.cgColor
        textField.layer.borderWidth = 1.0
        textField.layer.cornerRadius = 8.0
        textField.layer.masksToBounds = true
    }
    
    public static func turnRed(textField: UITextField) {
        textField.layer.borderColor = UIColor.red.cgColor
        textField.layer.borderWidth = 1.0
        textField.layer.cornerRadius = 8.0
        textField.layer.masksToBounds = true
    }
}
