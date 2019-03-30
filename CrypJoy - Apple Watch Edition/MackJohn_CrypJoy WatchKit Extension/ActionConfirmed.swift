//
//  ActionConfirmed.swift
//  MackJohn_CrypJoy WatchKit Extension
//
//  Created by Johnny Mack on 2/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import WatchKit
import Foundation


class ActionConfirmed: WKInterfaceController {
    
    
    
    // MARK: - Class Properties
    var joy: Joy?
    
    
    
    // MARK: - System Generated Functions
    override func awake(withContext context: Any?) {
        super.awake(withContext: context)
        
        // Reference Joy object received from sending controller
        if let context: Joy = context as? Joy {
            self.joy = context
        }
        
        setTitle("")
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func buttonTapped() {
        // Send current Joy values to global variable.
        // This will be referenced to update the class property in the main controller
        globalJoy = joy
    }
}
