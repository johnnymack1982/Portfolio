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
    public static func enableButton(button: UIButton) {
        button.isHidden = false
    }
    
    public static func disableButton(button: UIButton) {
        button.isHidden = true
    }
}
