//
//  GiveJoyConfirm.swift
//  MackJohn_CrypJoy WatchKit Extension
//
//  Created by Johnny Mack on 2/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import WatchKit
import Foundation


class GiveJoyConfirm: WKInterfaceController {
    
    
    
    // MARK: - Class Properties
    public var joy: Joy?
    
    
    
    // MARK: - System Generated Functions
    override func awake(withContext context: Any?) {
        super.awake(withContext: context)
        
        // Reference Joy object received from sending controller
        if let context: Joy = context as? Joy {
            self.joy = context
        }
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        // If user confirms that they want to log a new act of kindess, increase the Give Joy progress by one and send
        // updated Joy object to next controller
        if segueIdentifier == "GiveJoyConfirm" {
            self.joy?.joyGiven()
            return self.joy
        }
            
        else {
            return nil
        }
    }
}
