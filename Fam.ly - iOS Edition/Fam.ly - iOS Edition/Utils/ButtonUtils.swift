//
//  ButtonUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import UIKit

public class ButtonUtils {
    
    
    
    // Custom methods
    // Custom method to disable selected button
    public static func enableButton(button: UIButton) {
        button.isHidden = false
    }
    
    // Custom method to enable selected button
    public static func disableButton(button: UIButton) {
        button.isHidden = true
    }
}
